package com.osfac.dmt.workbench;

import com.osfac.dmt.workbench.ui.ErrorHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class WorkbenchPropertiesFile implements WorkbenchProperties {

    private ErrorHandler errorHandler;
    private Element root1;
    private Element root2;

    public WorkbenchPropertiesFile(File file, ErrorHandler errorHandler) throws JDOMException, IOException {
        //alainvm [mav92@tiscali.fr] reports that he needs IOException in the throws
        //clause. I think he may be using a different version of JDOM.
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(file);
        root1 = document.getRootElement();
        root2 = null;
        this.errorHandler = errorHandler;
    }

    public WorkbenchPropertiesFile(File file1, File file2, ErrorHandler errorHandler) throws JDOMException, IOException {
        //alainvm [mav92@tiscali.fr] reports that he needs IOException in the throws
        //clause. I think he may be using a different version of JDOM.
        SAXBuilder builder = new SAXBuilder();
        Document document1 = builder.build(file1);
        Document document2 = builder.build(file2);
        root1 = document1.getRootElement();
        root2 = document2.getRootElement();
        this.errorHandler = errorHandler;
    }

    @Override
    public List getPlugInClasses() {
        return getPlugInClasses(null); //null invokes default ClassLoader
    }

    @Override
    public List getPlugInClasses(ClassLoader classLoader) {
        ArrayList plugInClasses = new ArrayList();
        for (Iterator i = root1.getChildren("plug-in").iterator(); i.hasNext();) {
            Element plugInElement = (Element) i.next();
            try {
                plugInClasses.add(Class.forName(plugInElement.getTextTrim(), false, classLoader));
            } catch (ClassNotFoundException e) {
                errorHandler.handleThrowable(e);
            }
        }
        if (root2 != null) {
            for (Iterator i = root2.getChildren("plug-in").iterator(); i.hasNext();) {
                Element plugInElement = (Element) i.next();
                try {
                    plugInClasses.add(Class.forName(plugInElement.getTextTrim(), false, classLoader));
                } catch (ClassNotFoundException e) {
                    errorHandler.handleThrowable(e);
                }
            }
        }
        return plugInClasses;
    }

    @Override
    public List getInputDriverClasses() throws ClassNotFoundException {
        ArrayList inputDriverClasses = new ArrayList();
        for (Iterator i = root1.getChildren("input-driver").iterator(); i.hasNext();) {
            Element inputDriverElement = (Element) i.next();
            inputDriverClasses.add(Class.forName(inputDriverElement.getTextTrim()));
        }
        if (root2 != null) {
            for (Iterator i = root2.getChildren("input-driver").iterator(); i.hasNext();) {
                Element inputDriverElement = (Element) i.next();
                inputDriverClasses.add(Class.forName(inputDriverElement.getTextTrim()));
            }
        }
        return inputDriverClasses;
    }

    @Override
    public List getOutputDriverClasses() throws ClassNotFoundException {
        ArrayList outputDriverClasses = new ArrayList();
        for (Iterator i = root1.getChildren("output-driver").iterator(); i.hasNext();) {
            Element outputDriverElement = (Element) i.next();
            outputDriverClasses.add(Class.forName(outputDriverElement.getTextTrim()));
        }
        if (root2 != null) {
            for (Iterator i = root2.getChildren("output-driver").iterator(); i.hasNext();) {
                Element outputDriverElement = (Element) i.next();
                outputDriverClasses.add(Class.forName(outputDriverElement.getTextTrim()));
            }
        }
        return outputDriverClasses;
    }

    @Override
    public List getConfigurationClasses() throws ClassNotFoundException {
        ArrayList getConfigurationClasses = new ArrayList();
        for (Iterator i = root1.getChildren("extension").iterator(); i.hasNext();) {
            Element configurationElement = (Element) i.next();
            getConfigurationClasses.add(Class.forName(configurationElement.getTextTrim()));
        }
        if (root2 != null) {
            for (Iterator i = root2.getChildren("extension").iterator(); i.hasNext();) {
                Element configurationElement = (Element) i.next();
                getConfigurationClasses.add(Class.forName(configurationElement.getTextTrim()));
            }
        }
        return getConfigurationClasses;
    }
}
