package com.schneide.crapertura;

import java.io.File;

import org.apache.tools.ant.Project;

import com.schneide.crapertura.ant.CraperturaTask;

public class Main {

	public static void main(String[] args) throws Exception {
		CraperturaTask task = new CraperturaTask();
		task.setCoberturaReportFile(new File("C:/Dokumente und Einstellungen/dsl/Desktop/ramses-center-coverage.xml"));
		task.setClassesDirectory(new File("C:/Dokumente und Einstellungen/dsl/Desktop/report-classes/report-classes"));
		task.setTargetFile(new File("C:/Dokumente und Einstellungen/dsl/Desktop/tested_crapreport.xml"));
		task.setProject(new Project());
		task.execute();
	}
}
