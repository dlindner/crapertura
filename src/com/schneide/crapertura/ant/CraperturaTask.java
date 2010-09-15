package com.schneide.crapertura.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.crap4j.MethodComplexity;

import com.schneide.crapertura.CrapReportWriter;
import com.schneide.crapertura.complexity.ComplexityCalculator;
import com.schneide.crapertura.coverage.report.CoberturaXMLReportReader;
import com.schneide.crapertura.coverage.report.MethodCoverage;
import com.schneide.crapertura.statistics.MethodCrapData;

public class CraperturaTask extends Task {
	
	private File coberturaReportFile;
	private File targetFile;
	private File classesDirectory;
	private int crapThreshold;
	
	public CraperturaTask() {
		super();
		this.crapThreshold = 30;
	}
	
	@Override
	public void execute() throws BuildException {
		failIfAbsent(this.coberturaReportFile, "coberturaReportFile");
		failIfAbsent(this.targetFile, "targetFile");
		failIfAbsent(this.classesDirectory, "classesDirectory");
		try {
			executeChecked();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
	protected void executeChecked() throws Exception {
		CoberturaXMLReportReader reader = new CoberturaXMLReportReader();
		Map<String, MethodCoverage> map = reader.readXMLReport(this.coberturaReportFile);
		ComplexityCalculator calculator = new ComplexityCalculator();
		Collection<MethodComplexity> calculateComplexitiesFor = calculator.calculateComplexitiesFor(this.classesDirectory);
		List<MethodCrapData> crapData = new ArrayList<MethodCrapData>();
		for (MethodComplexity methodComplexity : calculateComplexitiesFor) {
			MethodCoverage currentCoverage = new MethodCoverage(0.0d, 0.0d);
			if (map.containsKey(methodComplexity.getMatchingMethodSignature())) {
				currentCoverage = map.get(methodComplexity.getMatchingMethodSignature());
			}
			MethodCrapData newData = handleMethodWithBothData(methodComplexity, currentCoverage);
			crapData.add(newData);
		}
		CrapReportWriter writer = new CrapReportWriter();
		writer.writeReportTo(
				this.targetFile,
				crapData);
	}
	
	protected MethodCrapData handleMethodWithBothData(MethodComplexity complexity, MethodCoverage coverage) {
		return new MethodCrapData(complexity, coverage, this.crapThreshold);
	}
	
	protected void failIfAbsent(Object instance, String description) {
		if (null != instance) {
			return;
		}
		throw new BuildException("Required parameter " + description + " missing.");
	}
	
	public void setCoberturaReportFile(File coberturaReportFile) {
		this.coberturaReportFile = coberturaReportFile;
	}
	
	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}
	
	public void setClassesDirectory(File classesDirectory) {
		this.classesDirectory = classesDirectory;
	}
	
	public void setCrapThreshold(int crapThreshold) {
		this.crapThreshold = crapThreshold;
	}
}
