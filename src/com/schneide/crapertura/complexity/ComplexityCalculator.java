package com.schneide.crapertura.complexity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
		List<MethodComplexity> result = new ArrayList<MethodComplexity>();
		for (File file : classesDirectory.listFiles()) {
			if (file.isDirectory()) {
				result.addAll(calculateComplexitiesFor(file));
			}
			if (file.isFile() && file.getName().endsWith(".class")) {
				List<MethodComplexity> methodComplexities = this.complexity.getMethodComplexitiesFor(file);
//				if (file.getName().startsWith("I")) {
//					System.out.println(file.getName() + ": " + methodComplexities.size());
//				}
				result.addAll(methodComplexities);
			}
		}
		return result;
	}
}
