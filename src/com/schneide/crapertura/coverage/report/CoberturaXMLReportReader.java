package com.schneide.crapertura.coverage.report;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("nls")
public class CoberturaXMLReportReader {

	public CoberturaXMLReportReader() {
		super();
	}

    public Map<String, MethodCoverage> readXMLReport(File reportFile) throws Exception {
		Map<String, MethodCoverage> result = new HashMap<String, MethodCoverage>();
	    SAXReader xmlReader = new SAXReader();
	    Document document = xmlReader.read(reportFile);
	    Element rootElement = document.getRootElement();
	    if (!"coverage".equals(rootElement.getName())) {
	    	return null;
	    }
	    Element packages = rootElement.element("packages");
	    Iterator<?> packageIterator = packages.elementIterator("package");
	    while (packageIterator.hasNext()) {
	    	readPackage(result, (Element) packageIterator.next());
	    }
	    return result;
	}

	protected void readPackage(Map<String, MethodCoverage> result, Element packageElement) {
		Element classes = packageElement.element("classes");
	    Iterator<?> classIterator = classes.elementIterator("class");
	    while (classIterator.hasNext()) {
	    	readClass(result,
	    	        //packageElement.attributeValue("name"),
	    			(Element) classIterator.next());
	    }
	}

	protected void readClass(Map<String, MethodCoverage> result, /*String packageName,*/ Element classElement) {
		Element methods = classElement.element("methods");
	    Iterator<?> methodIterator = methods.elementIterator("method");
	    while (methodIterator.hasNext()) {
	    	Element method = (Element) methodIterator.next();
	    	MethodCoverage coverage = new MethodCoverage(
	    			Double.parseDouble(method.attributeValue("line-rate")),
					Double.parseDouble(method.attributeValue("branch-rate")));
	    	result.put(classElement.attributeValue("name") + "." +
	    			method.attributeValue("name") + method.attributeValue("signature"),
	    			coverage);
	    }
	}
}
