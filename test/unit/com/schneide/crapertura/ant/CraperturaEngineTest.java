package com.schneide.crapertura.ant;

import org.crap4j.MethodComplexity;
import org.junit.Test;

import com.schneide.crapertura.ant.CraperturaTask.CrapReporting;
import com.schneide.crapertura.ant.CraperturaTask.MethodCoverageProvider;
import com.schneide.crapertura.coverage.report.MethodCoverage;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItem;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class CraperturaEngineTest {

    private static final MethodCoverage SOME_COVERAGE = new MethodCoverage(0.25d, 0.5d);

    public CraperturaEngineTest() {
        super();
    }

    @SuppressWarnings("boxing")
    @Test
    public void usesCoverageDataAsBaseInsteadOfMethodComplexity() {
        final String methodA = "methodA";
        final String methodB = "methodB";
        final MethodComplexity complexityA = complexityFor(methodA, 4);
        final MethodComplexity complexityB = complexityFor(methodB, 40);
        final MethodCoverageProvider coverages = mock(MethodCoverageProvider.class, "coverages");
        when(coverages.hasCoverageFor(methodA)).thenReturn(true);
        when(coverages.getCoverageFor(methodA)).thenReturn(SOME_COVERAGE);
        when(coverages.hasCoverageFor(methodB)).thenReturn(false);
        final CrapReporting reporting = mock(CrapReporting.class, "crap report");
        final Iterable<CrapReporting> result = new CraperturaEngine().performOn(
                asList(reporting),
                coverages,
                asList(complexityA, complexityB));
        assertThat(result, hasItem(reporting));
        verify(reporting, times(1)).consider(complexityA, SOME_COVERAGE);
        verify(reporting, never()).consider(complexityB, null);
    }

    @SuppressWarnings("boxing")
    protected MethodComplexity complexityFor(String methodName, int cyclomaticComplexity) {
        final MethodComplexity result = mock(MethodComplexity.class, methodName);
        when(result.getMatchingMethodSignature()).thenReturn(methodName);
        when(result.getComplexity()).thenReturn(cyclomaticComplexity);
        return result;
    }
}
