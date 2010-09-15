package com.schneide.crapertura.statistics;

import org.crap4j.MethodComplexity;

import com.schneide.crapertura.coverage.report.MethodCoverage;

public class MethodCrapData {

	private final MethodComplexity complexity;
	private final double coverage;
	private final double crap;
	private final int crapLoad;
	private final int crapThreshold;

	public MethodCrapData(MethodComplexity complexity, MethodCoverage coverage, int crapThreshold) {
		super();
		this.complexity = complexity;
		this.coverage = adjustBranchCoverage(coverage.getLineCoverageRate(), coverage.getBranchCoverageRate());
		this.crap = calculateCrap(complexity.getComplexity(), this.coverage);
		this.crapThreshold = crapThreshold;
		this.crapLoad = calculateCrapLoad(this.crap,
				this.complexity.getComplexity(), this.coverage, crapThreshold);
	}

	public String getPackage() {
		return this.complexity.getPackageName();
	}

	public String getClassName() {
		return this.complexity.getClassName();
	}

	public String getMethodName() {
		return this.complexity.getMethodName();
	}

	public String getMethodSignature() {
		return this.complexity.getSigOrDescriptor();
	}

	public String getFullMethod() {
		return this.complexity.prettyMethodSignature();
	}

	public double getCrap() {
		return this.crap;
	}

	public int getCrapLoad() {
		return this.crapLoad;
	}

	public int getComplexity() {
		return this.complexity.getComplexity();
	}

	public double getCoverage() {
		return this.coverage;
	}

	public int getCrapThreshold() {
		return this.crapThreshold;
	}

	protected double adjustBranchCoverage(double lineCoverage, double branchCoverage) {
		if (0.0d == lineCoverage) {
			return 0.0d;
		}
		return (branchCoverage * lineCoverage);
	}

    public static int calculateCrapLoad(double crap, int complexity, double coverage, double crapThreshold) {
        int crapLoad = 0;
        if (crap >= crapThreshold) {
            crapLoad = (int) (crapLoad + complexity * (1.0D - coverage));
            crapLoad = (int) (crapLoad + complexity / crapThreshold);
        }
        return crapLoad;
    }

	protected static double calculateCrap(double complexity, double coverage) {
		return (float) (complexity * (complexity * Math.pow(1.0D - coverage, 3D)) + complexity);
	}
}
