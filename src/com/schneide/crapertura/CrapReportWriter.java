package com.schneide.crapertura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.schneide.crapertura.statistics.CrapStatistics;
import com.schneide.crapertura.statistics.MethodCrapData;

@SuppressWarnings("nls")
public class CrapReportWriter {

	public CrapReportWriter() {
		super();
	}

	public void writeReportTo(File targetFile, Iterable<MethodCrapData> data) throws IOException {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		//outformat.setEncoding(aEncodingScheme);
		XMLWriter writer = new XMLWriter(new FileOutputStream(targetFile), outformat);
		writer.write(createReportDocument(data));
		writer.flush();
	}

    protected Document createReportDocument(Iterable<MethodCrapData> data) {
		Document document = DocumentFactory.getInstance().createDocument();
		Element rootElement = document.addElement("crap_result");
		// TODO: Add real project name
		rootElement.addElement("project").addText("TODO");
        // TODO: Add some good looking project ID
		rootElement.addElement("project_id").addText("TODO");
        // TODO: Add current date here
		rootElement.addElement("timestamp").addText("TODO");
		addClassDirectories(rootElement.addElement("classDirectories"));
		addTestClassDirectories(rootElement.addElement("testClassDirectories"));
		addSourceDirectories(rootElement.addElement("sourceDirectories"));
		addLibClasspaths(rootElement.addElement("libClasspaths"));
		addStats(
				rootElement.addElement("stats"),
				CrapStatistics.fromMethodData(data));
		addMethods(rootElement.addElement("methods"), data);
		return document;
	}

	private static class HistogramBar {
		private final String place;
		private final int value;
		private final double height;

		public HistogramBar(String place, int value, double height) {
			super();
			this.place = place;
			this.value = value;
			this.height = height;
		}

		public void appendToElement(Element parent) {
			Element element = parent.addElement("hist");
			element.addElement("place").addText(this.place);
			element.addElement("value").addText(String.valueOf(this.value));
			element.addElement("height").addText(String.valueOf(this.height) + "px");
		}
	}

	protected void addHistogram(Element histogramElement) {
	    // TODO: Calculate real histogram
		HistogramBar[] bars = new HistogramBar[] {
			new HistogramBar("one", 0, 0.0d),
			new HistogramBar("two", 0, 0.0d),
			new HistogramBar("four", 0, 0.0d),
			new HistogramBar("eight", 0, 0.0d),
			new HistogramBar("sixteen", 0, 0.0d),
			new HistogramBar("thirtytwo", 0, 0.0d),
			new HistogramBar("sixtyfour", 0, 0.0d),
			new HistogramBar("one28", 0, 0.0d),
			new HistogramBar("two56", 0, 0.0d),
		};
		for (HistogramBar histogramBar : bars) {
			histogramBar.appendToElement(histogramElement);
		}
	}

	protected void addNumberElement(Element root, String elementName, double value) {
		root.addElement(elementName).addText(String.valueOf(value));
	}

	protected void addNumberElement(Element root, String elementName, int value) {
		root.addElement(elementName).addText(String.valueOf(value));
	}

	protected void addStats(Element statsElement, CrapStatistics statistics) {
		statsElement.addElement("name").addText("Method Crap Stats");
		addNumberElement(statsElement, "totalCrap", statistics.getTotalCrap());
		addNumberElement(statsElement, "crap", statistics.getCrap());
		addNumberElement(statsElement, "median", statistics.getMedian());
		addNumberElement(statsElement, "average", statistics.getAverage());
		addNumberElement(statsElement, "stdDev", statistics.getStandardDeviation());
		addNumberElement(statsElement, "methodCount", statistics.getMethodCount());
		addNumberElement(statsElement, "crapMethodCount", statistics.getCrapMethodCount());
		addNumberElement(statsElement, "crapMethodPercent", statistics.getCrapMethodPercent());
		addNumberElement(statsElement, "crapLoad", statistics.getCrapLoad());
		addNumberElement(statsElement, "crapThreshold", statistics.getCrapThreshold());
		addNumberElement(statsElement, "globalAverage", statistics.getGlobalAverage());
		addNumberElement(statsElement, "globalCraploadAverage", statistics.getGlobalCraploadAverage());
		addNumberElement(statsElement, "globalCrapMethodAverage", statistics.getGlobalCrapMethodAverage());
		addNumberElement(statsElement, "globalTotalMethodAverage", statistics.getGlobalCrapMethodPercent());
		addNumberElement(statsElement, "globalAverageDiff", statistics.getGlobalAverageDiff());
		addNumberElement(statsElement, "globalCraploadAverageDiff", statistics.getGlobalCraploadAverageDiff());
		addNumberElement(statsElement, "globalCrapMethodAverageDiff", statistics.getGlobalCrapMethodAverageDiff());
		addNumberElement(statsElement, "globalTotalMethodAverageDiff", statistics.getGlobalTotalMethodAverageDiff());
		statsElement.addElement("shareStatsUrl").addText("http://127.0.0.1/sharingIsDisabled");
		addHistogram(statsElement.addElement("histogram"));
	}

	private void addClassDirectories(Element addElement) {
	    // TODO: Add class directories
		addElement.addElement("classDirectory").addText("");
	}

	private void addTestClassDirectories(Element addElement) {
        // TODO: Add test class directories
		addElement.addElement("testClassDirectory").addText("");
	}

	private void addSourceDirectories(Element addElement) {
        // TODO: Add source directories
		addElement.addElement("sourceDirectory").addText("");
	}

	private void addLibClasspaths(Element addElement) {
        // TODO: Add library classpath directories
		addElement.addElement("libClasspath").addText("");
	}

	protected void addMethods(Element rootElement, Iterable<MethodCrapData> data) {
		for (MethodCrapData method : data) {
			Element methodElement = rootElement.addElement("method");
			methodElement.addElement("package").addText(method.getPackage());
			methodElement.addElement("className").addText(method.getClassName());
			methodElement.addElement("methodName").addText(method.getMethodName());
			methodElement.addElement("methodSignature").addText(method.getMethodSignature());
			methodElement.addElement("fullMethod").addText(method.getFullMethod());
			methodElement.addElement("crap").addText(String.valueOf(method.getCrap()));
			methodElement.addElement("complexity").addText(String.valueOf(method.getComplexity()));
			methodElement.addElement("coverage").addText(String.valueOf(method.getCoverage() * 100.0d));
			methodElement.addElement("crapLoad").addText(String.valueOf(method.getCrapLoad()));
		}
	}
}
