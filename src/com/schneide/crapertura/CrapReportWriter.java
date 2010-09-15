package com.schneide.crapertura;

import java.io.File;
import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.schneide.crapertura.statistics.CrapStatistics;
import com.schneide.crapertura.statistics.MethodCrapData;

public class CrapReportWriter {

	public CrapReportWriter() {
		super();
	}

	public void writeReportTo(File targetFile, Iterable<MethodCrapData> data) throws Exception {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		//outformat.setEncoding(aEncodingScheme);
		XMLWriter writer = new XMLWriter(new FileOutputStream(targetFile), outformat);
		writer.write(createReportDocument(data));
		writer.flush();
	}

	protected Document createReportDocument(Iterable<MethodCrapData> data) {
		Document document = DocumentFactory.getInstance().createDocument();
		Element rootElement = document.addElement("crap_result");
		rootElement.addElement("project").addText("TODO");
		rootElement.addElement("project_id").addText("132");
		rootElement.addElement("timestamp").addText("10/5/08 2:03 PM");
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
		HistogramBar[] bars = new HistogramBar[] {
			new HistogramBar("one", 1987, 28.0d),
			new HistogramBar("two", 12606, 170.0d),
			new HistogramBar("four", 1502, 28.0d),
			new HistogramBar("eight", 500, 28.0d),
			new HistogramBar("sixteen", 200, 28.0d),
			new HistogramBar("thirtytwo", 27, 28.0d),
			new HistogramBar("sixtyfour", 12, 28.0d),
			new HistogramBar("one28", 2, 28.0d),
			new HistogramBar("two56", 1, 28.0d),
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

//		statsElement.addElement("totalCrap").addText("49517.77");
//		statsElement.addElement("crap").addText("2.94");
//		statsElement.addElement("median").addText("2.00");
//		statsElement.addElement("average").addText("2.94");
//		statsElement.addElement("stdDev").addText("4.90");
//		statsElement.addElement("methodCount").addText("16837");
//		statsElement.addElement("crapMethodCount").addText("95");
//		statsElement.addElement("crapMethodPercent").addText("0.56");
//		statsElement.addElement("crapLoad").addText("574");
//		statsElement.addElement("crapThreshold").addText("30");
//		statsElement.addElement("globalAverage").addText("-1.00");
//		statsElement.addElement("globalCraploadAverage").addText("-1.00");
//		statsElement.addElement("globalCrapMethodAverage").addText("-1.00");
//		statsElement.addElement("globalTotalMethodAverage").addText("-1.00");
//		statsElement.addElement("globalAverageDiff").addText("3.94");
//		statsElement.addElement("globalCraploadAverageDiff").addText("575.00");
//		statsElement.addElement("globalCrapMethodAverageDiff").addText("96.00");
//		statsElement.addElement("globalTotalMethodAverageDiff").addText("16838.00");
		statsElement.addElement("shareStatsUrl").addText("http://www.crap4j.org/benchmark/stats/new?stat[project_hash]=1223208041985&amp;stat[project_url]=ci&amp;stat[crap]=0.56&amp;stat[crap_load]=574&amp;stat[crap_methods]=95&amp;stat[total_methods]=16837&amp;stat[ones]=1987&amp;stat[twos]=12606&amp;stat[fours]=1502&amp;stat[eights]=500&amp;stat[sixteens]=200&amp;stat[thirtytwos]=27&amp;stat[sixtyfours]=12&amp;stat[one28s]=2&amp;stat[two56s]=1");
		addHistogram(statsElement.addElement("histogram"));
	}

	private void addClassDirectories(Element addElement) {
		addElement.addElement("classDirectory").addText("/home/ci/slavespace/workspace");
	}

	private void addTestClassDirectories(Element addElement) {
		addElement.addElement("testClassDirectory").addText("/home/ci/slavespace/workspace");
	}

	private void addSourceDirectories(Element addElement) {
		addElement.addElement("sourceDirectory").addText("/home/ci/slavespace/workspace");
	}

	private void addLibClasspaths(Element addElement) {
		addElement.addElement("libClasspath").addText("/_generated/ci-reporting/ci-reporting/lib/TimingFramework-1.0.jar");
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
