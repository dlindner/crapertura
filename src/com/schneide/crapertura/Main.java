package com.schneide.crapertura;

import java.io.File;

import org.apache.tools.ant.Project;

import com.schneide.crapertura.ant.CraperturaTask;

@SuppressWarnings("nls")
public class Main {

    public static void main(String[] args) throws Exception {
		CraperturaTask task = new CraperturaTask();
		task.setCoberturaReportFile(new File("C:/Dokumente und Einstellungen/User/Desktop/coverage.xml"));
		task.setClassesDirectory(new File("C:/Dokumente und Einstellungen/User/Desktop/report-classes"));
		task.setTargetDirectory(new File("C:/Dokumente und Einstellungen/User/Desktop/targetX"));
		task.setProject(new Project());
		task.execute();
	}
}
