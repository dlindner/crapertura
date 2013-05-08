package com.schneide.crapertura.complexity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.crap4j.MethodComplexity;
import org.crap4j.complexity.CyclomaticComplexity;

public class ComplexityCalculator {

	private final CyclomaticComplexity complexity;

	public ComplexityCalculator() {
		super();
		this.complexity = new CyclomaticComplexity();
	}

	public Collection<MethodComplexity> calculateComplexitiesFor(File... classesDirectories) throws Exception {
		List<MethodComplexity> result = new ArrayList<MethodComplexity>();
		for (File directory : classesDirectories) {
			result.addAll(calculateComplexitiesFor(directory));
		}
		return result;
	}

	public Collection<MethodComplexity> calculateComplexitiesFor(File classesDirectory) throws Exception {
		final File[] files = classesDirectory.listFiles();
		if (null == files) {
		    return Collections.emptyList();
		}
		final List<MethodComplexity> result = new ArrayList<MethodComplexity>();
		for (File each : files) {
			if (each.isDirectory()) {
				result.addAll(calculateComplexitiesFor(each));
			}
			if (each.isFile() && each.getName().endsWith(".class")) { //$NON-NLS-1$
				List<MethodComplexity> methodComplexities = this.complexity.getMethodComplexitiesFor(each);
				result.addAll(methodComplexities);
			}
		}
		return result;
	}
}
