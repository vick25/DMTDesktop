package com.osfac.dmt.workbench.driver;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchProperties;
import com.osfac.dmt.workbench.ui.BasicFileDriverPanel;
import com.osfac.dmt.workbench.ui.ErrorHandler;
import com.vividsolutions.jts.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DriverManager {

    private ArrayList inputDrivers = new ArrayList();
    private ArrayList outputDrivers = new ArrayList();
    private List builtInInputDriverClasses = Arrays.asList(new Class[]{
                FMEFileInputDriver.class, GMLFileInputDriver.class,
                JMLFileInputDriver.class, ShapeFileInputDriver.class,
                WKTFileInputDriver.class
            });
    private List builtInOutputDriverClasses = Arrays.asList(new Class[]{
                FMEFileOutputDriver.class, GMLFileOutputDriver.class,
                JMLFileOutputDriver.class, ShapefileOutputDriver.class,
                WKTFileOutputDriver.class
            });
    private BasicFileDriverPanel sharedOpenBasicFileDriverPanel;
    private BasicFileDriverPanel sharedSaveBasicFileDriverPanel;
    private ErrorHandler errorHandler;

    public DriverManager(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        sharedOpenBasicFileDriverPanel = new SharedFileDriverPanel(I18N.get("driver.DriverManager.file-to-open"),
                errorHandler);
        sharedSaveBasicFileDriverPanel = new SharedFileDriverPanel(I18N.get("driver.DriverManager.file-to-save"),
                errorHandler);
        sharedOpenBasicFileDriverPanel.setFileMustExist(true);
        sharedSaveBasicFileDriverPanel.setFileMustExist(false);
    }

    public List getInputDrivers() {
        return inputDrivers;
    }

    public List getOutputDrivers() {
        return outputDrivers;
    }

    /**
     * Need to share the file chooser; otherwise, whenever the user switched
     * drivers, he would lose the filename he typed in.
     */
    public BasicFileDriverPanel getSharedOpenBasicFileDriverPanel() {
        return sharedOpenBasicFileDriverPanel;
    }

    public BasicFileDriverPanel getSharedSaveBasicFileDriverPanel() {
        return sharedSaveBasicFileDriverPanel;
    }

    public void loadDrivers(WorkbenchProperties properties)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        ArrayList newInputDriverClasses = new ArrayList();
        newInputDriverClasses.addAll(builtInInputDriverClasses);
        newInputDriverClasses.addAll(properties.getInputDriverClasses());
        loadDrivers(newInputDriverClasses, inputDrivers);

        ArrayList newOutputDriverClasses = new ArrayList();
        newOutputDriverClasses.addAll(builtInOutputDriverClasses);
        newOutputDriverClasses.addAll(properties.getOutputDriverClasses());
        loadDrivers(newOutputDriverClasses, outputDrivers);
    }

    /**
     * Loads an instantiated, but not yet initialized, InputDriver, and adds it
     * to the list.
     *
     * @param driver an instantiated but not yet initialed, InputDriver
     */
    public void loadInputDriver(AbstractInputDriver driver) {
        driver.initialize(this, errorHandler);
        inputDrivers.add(driver);
    }

    /**
     * Loads an instantiated, but not yet initialized OutputDriver, and adds it
     * to the list.
     *
     * @param driver an instantiated but not yet initialized OutputDriver
     */
    public void loadOutputDriver(AbstractOutputDriver driver) {
        driver.initialize(this, errorHandler);
        outputDrivers.add(driver);
    }

    private void loadDrivers(List driverClasses, List drivers)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        for (Iterator i = driverClasses.iterator(); i.hasNext();) {
            Class driverClass = (Class) i.next();
            AbstractDriver driver = (AbstractDriver) driverClass.newInstance();
            driver.initialize(this, errorHandler);
            drivers.add(driver);
        }
    }

    private static class SharedFileDriverPanel extends BasicFileDriverPanel {

        public SharedFileDriverPanel(String description,
                ErrorHandler errorHandler) {
            super(errorHandler);
            fileNamePanel.setUpperDescription(description);
        }

        public void setFileDescription(String description) {
            Assert.shouldNeverReachHere(
                    "Panel is shared; thus description cannot be changed");
        }
    }
}
