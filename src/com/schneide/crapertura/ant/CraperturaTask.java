package com.schneide.crapertura.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.crap4j.Crap;
import org.crap4j.CrapProject;
import org.crap4j.MethodComplexity;
import org.crap4j.MethodCrap;
import org.crap4j.SystemCrapStats;
import org.crap4j.benchmark.GlobalStats;

import com.schneide.crapertura.CrapReportWriter;
import com.schneide.crapertura.complexity.ComplexityCalculator;
import com.schneide.crapertura.coverage.report.CoberturaXMLReportReader;
import com.schneide.crapertura.coverage.report.MethodCoverage;
import com.schneide.crapertura.statistics.MethodCrapData;

@SuppressWarnings("nls")
public class CraperturaTask extends Task {

    private static final MethodCoverage NO_COVERAGE = new MethodCoverage(0.0d, 0.0d);
	private File coberturaReportFile;
	private File reportTargetDirectory;
	private File classesDirectory;
	private int crapThreshold;
	private boolean generateHTMLReport;

	public CraperturaTask() {
		super();
		this.crapThreshold = 30;
		this.generateHTMLReport = true;
	}

    @Override
	public void execute() throws BuildException {
		failIfAbsent(this.coberturaReportFile, "coberturaReportFile");
		failIfAbsent(this.reportTargetDirectory, "targetDirectory");
		failIfAbsent(this.classesDirectory, "classesDirectory");
		if (!this.reportTargetDirectory.isDirectory()) {
		    this.reportTargetDirectory.mkdirs();
		}
		try {
			executeChecked();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

    protected interface CrapReporting {

        public void consider(MethodComplexity methodComplexity, MethodCoverage methodCoverage);

        public void writeTo(File targetFile) throws IOException;
    }

    protected class XMLCrapReporting implements CrapReporting {
        private final List<MethodCrapData> crapData;

        public XMLCrapReporting() {
            super();
            this.crapData = new ArrayList<MethodCrapData>();
        }

        @Override
        public void consider(MethodComplexity methodComplexity, MethodCoverage methodCoverage) {
            MethodCrapData newData = new MethodCrapData(methodComplexity, methodCoverage, CraperturaTask.this.crapThreshold);
            this.crapData.add(newData);
        }

        @Override
        public void writeTo(File targetFile) throws IOException {
            CrapReportWriter writer = new CrapReportWriter();
            writer.writeReportTo(
                    new File(targetFile, "report.xml"),
                    this.crapData);
        }
    }

    protected class OriginalHTMLCrapReporting implements CrapReporting {
        private final List<Crap> crapData;

        public OriginalHTMLCrapReporting() {
            super();
            this.crapData = new ArrayList<Crap>();
        }

        @Override
        public void consider(MethodComplexity methodComplexity, MethodCoverage methodCoverage) {
            MethodCrap newOriginalData = new MethodCrap(
                    methodComplexity.getMatchingMethodSignature(),
                    (float) methodCoverage.getCoverage(),
                    methodComplexity);
            this.crapData.add(newOriginalData);
        }

        @Override
        public void writeTo(File targetDirectory) throws IOException {
            CrapProject crapProject = new CrapProject(
                    "TODO projectDir",
                    new ArrayList<String>(),
                    new ArrayList<String>(),
                    new ArrayList<String>(),
                    new ArrayList<String>(),
                    targetDirectory.getAbsolutePath());
            SystemCrapStats originalReport = new SystemCrapStats(this.crapData,
                    "TODO original report",
                    crapProject,
                    CraperturaTask.this.crapThreshold,
                    10.0f,
                    5.0f,
                    GlobalStats.NULL_STATS,
                    "http://127.0.0.1/upload_disabled/");
            originalReport.writeReport();
        }
    }

    protected class MethodCoverageProvider {
        private Map<String, MethodCoverage> map;

        public MethodCoverageProvider(final File coberturaFile) throws Exception {
            super();
            CoberturaXMLReportReader reader = new CoberturaXMLReportReader();
            this.map = reader.readXMLReport(coberturaFile);
        }

        public MethodCoverage getCoverageFor(final String methodSignature) {
            MethodCoverage currentCoverage = NO_COVERAGE;
            if (this.map.containsKey(methodSignature)) {
                currentCoverage = this.map.get(methodSignature);
            }
            return currentCoverage;
        }
    }

	protected void executeChecked() throws Exception {
		Iterable<CrapReporting> reporting = buildReportings();
		MethodCoverageProvider coverageProvider = new MethodCoverageProvider(this.coberturaReportFile);
		for (MethodComplexity methodComplexity : fetchMethodComplexities()) {
		    MethodCoverage methodCoverage = coverageProvider.getCoverageFor(methodComplexity.getMatchingMethodSignature());
		    for (CrapReporting crapReporting : reporting) {
                crapReporting.consider(methodComplexity, methodCoverage);
            }
		}
		for (CrapReporting crapReporting : reporting) {
            crapReporting.writeTo(this.reportTargetDirectory);
        }
	}

	protected Iterable<MethodComplexity> fetchMethodComplexities() throws Exception {
        ComplexityCalculator calculator = new ComplexityCalculator();
        return calculator.calculateComplexitiesFor(this.classesDirectory);
	}

	private Iterable<CrapReporting> buildReportings() {
	    List<CrapReporting> result = new ArrayList<CraperturaTask.CrapReporting>();
	    result.add(new XMLCrapReporting());
	    if (this.generateHTMLReport) {
	        result.add(new OriginalHTMLCrapReporting());
	    }
        return result;
    }

    protected void failIfAbsent(Object instance, String parameterName) {
		if (null != instance) {
			return;
		}
		throw new BuildException("Required parameter " + parameterName + " missing.");
	}

	public void setCoberturaReportFile(File coberturaReportFile) {
		this.coberturaReportFile = coberturaReportFile;
	}

	public void setTargetDirectory(File targetDirectory) {
		this.reportTargetDirectory = targetDirectory;
	}

	public void setClassesDirectory(File classesDirectory) {
		this.classesDirectory = classesDirectory;
	}

	public void setCrapThreshold(int crapThreshold) {
		this.crapThreshold = crapThreshold;
	}

	public void setGenerateHTMLReport(boolean generateReport) {
        this.generateHTMLReport = generateReport;
    }
}
