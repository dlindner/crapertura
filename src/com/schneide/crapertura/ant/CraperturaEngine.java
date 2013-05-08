package com.schneide.crapertura.ant;

import org.crap4j.MethodComplexity;

import com.schneide.crapertura.ant.CraperturaTask.CrapReporting;
import com.schneide.crapertura.ant.CraperturaTask.MethodCoverageProvider;
import com.schneide.crapertura.coverage.report.MethodCoverage;

public class CraperturaEngine {

    public CraperturaEngine() {
        super();
    }

    public Iterable<CrapReporting> performOn(final Iterable<CrapReporting> reportings,
            final MethodCoverageProvider coverages,
            final Iterable<MethodComplexity> complexities) {
        for (MethodComplexity each : complexities) {
            if (!coverages.hasCoverageFor(each.getMatchingMethodSignature())) {
                continue;
            }
            final MethodCoverage methodCoverage = coverages.getCoverageFor(each.getMatchingMethodSignature());
            for (CrapReporting crapReporting : reportings) {
                crapReporting.consider(each, methodCoverage);
            }
        }
        return reportings;
    }
}
