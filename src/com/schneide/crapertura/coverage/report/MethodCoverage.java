package com.schneide.crapertura.coverage.report;

public class MethodCoverage {

	private final double lineCoverageRate;
	private final double branchCoverageRate;

	public MethodCoverage(double lineCoverageRate, double branchCoverageRate) {
		super();
		this.lineCoverageRate = lineCoverageRate;
		this.branchCoverageRate = branchCoverageRate;
	}

	public double getLineCoverageRate() {
		return this.lineCoverageRate;
	}

	public double getBranchCoverageRate() {
		return this.branchCoverageRate;
	}
}
