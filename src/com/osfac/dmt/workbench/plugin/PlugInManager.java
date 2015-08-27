package com.osfac.dmt.workbench.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.vividsolutions.jts.util.Assert;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.log4j.Logger;

/**
 * Loads plug-ins (or more precisely, Extensions), and any JAR files that they depend on, from the
 * plug-in directory.
 */
public class PlugInManager {

    private static Logger LOG = Logger.getLogger(PlugInManager.class);
    private static final String NOT_INITIALIZED
            = I18N.get("com.osfac.dmt.workbench.plugin.PlugInManager.could-not-be-initialized");
    private static final String LOADING
            = I18N.get("com.osfac.dmt.workbench.plugin.PlugInManager.loading");
    private static final String LOADING_ERROR
            = I18N.get("com.osfac.dmt.workbench.plugin.PlugInManager.throwable-encountered-loading");
    private TaskMonitor monitor;
    private WorkbenchContext context;
    private Collection configurations = new ArrayList();
    private File plugInDirectory;

    /**
     * @param plugInDirectory null to leave unspecified
     */
    public PlugInManager(WorkbenchContext context, File plugInDirectory, TaskMonitor monitor) throws Exception {
        this.monitor = monitor;
        Assert.isTrue((plugInDirectory == null)
                || plugInDirectory.isDirectory());
        classLoader = plugInDirectory != null ? new URLClassLoader(
                toURLs((File[]) findFilesRecursively(plugInDirectory).toArray(
                                new File[]{}))) : getClass().getClassLoader();
        I18N.setClassLoader(classLoader);
        this.context = context;
        //Find the configurations right away so they get reported to the splash
        //screen ASAP. [Bob Boseko]
        configurations
                .addAll(plugInDirectory != null ? findConfigurations(plugInDirectory)
                                : new ArrayList());
        configurations.addAll(findConfigurations(context.getWorkbench()
                .getProperties().getConfigurationClasses()));
        this.plugInDirectory = plugInDirectory;
    }

    public void load() throws Exception {
        loadPlugInClasses(context.getWorkbench().getProperties().getPlugInClasses(getClassLoader()));
        loadConfigurations();
    }

    private void loadConfigurations() throws Exception {
        for (Iterator i = configurations.iterator(); i.hasNext();) {
            Configuration configuration = (Configuration) i.next();
            configuration.configure(new PlugInContext(context, null, null, null, null));
        }
    }

    public static String name(Configuration configuration) {
        if (configuration instanceof Extension) {
            return ((Extension) configuration).getName();
        }
        return new StringBuilder(StringUtil.toFriendlyName(configuration.getClass().getName(),
                "Configuration")).append(" (").append(configuration.getClass().getPackage().getName())
                .append(")").toString();
    }

    public static String version(Configuration configuration) {
        if (configuration instanceof Extension) {
            return ((Extension) configuration).getVersion();
        }
        return "";
    }

    private Collection findConfigurations(List classes) throws Exception {
        ArrayList configurations = new ArrayList();
        for (Iterator i = classes.iterator(); i.hasNext();) {
            Class c = (Class) i.next();
            if (!Configuration.class.isAssignableFrom(c)) {
                continue;
            }
            LOG.debug(new StringBuilder(LOADING).append(" ").append(c.getName()).toString());
            System.out.println(LOADING + " " + c.getName());
            Configuration configuration = (Configuration) c.newInstance();
            configurations.add(configuration);
            monitor.report(new StringBuilder(LOADING).append(" ").append(name(configuration))
                    .append(" ").append(version(configuration)).toString());
        }
        return configurations;
    }

    private void loadPlugInClasses(List plugInClasses) throws Exception {
        for (Iterator i = plugInClasses.iterator(); i.hasNext();) {
            Class plugInClass = null;
            try {
                plugInClass = (Class) i.next();
                PlugIn plugIn = (PlugIn) plugInClass.newInstance();
                plugIn.initialize(new PlugInContext(context, null, null, null, null));
            } catch (NoClassDefFoundError e) {
                LOG.warn(plugInClass + " " + NOT_INITIALIZED);
                LOG.info(e.getCause().toString());
                System.out.println(plugInClass + " " + NOT_INITIALIZED);
            }
        }
    }
    private ClassLoader classLoader;

    private Collection findFilesRecursively(File directory) {
        Assert.isTrue(directory.isDirectory());
        Collection files = new ArrayList();
        for (Iterator i = Arrays.asList(directory.listFiles()).iterator(); i
                .hasNext();) {
            File file = (File) i.next();
            if (file.isDirectory()) {
                files.addAll(findFilesRecursively(file));
            }
            if (!file.isFile()) {
                continue;
            }
            files.add(file);
        }
        return files;
    }

    private Collection findConfigurations(File plugInDirectory) throws Exception {
        ArrayList configurations = new ArrayList();
        for (Iterator i = findFilesRecursively(plugInDirectory).iterator(); i
                .hasNext();) {
            File file = (File) i.next();
            try {
                configurations.addAll(findConfigurations(classes(new ZipFile(file), classLoader)));
            } catch (ZipException e) {
                //Might not be a zipfile. Eat it.
            }
        }
        return configurations;
    }

    private URL[] toURLs(File[] files) {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                urls[i] = new URL(new StringBuilder("jar:file:").append(files[i].getPath()).append("!/").toString());
            } catch (MalformedURLException e) {
                Assert.shouldNeverReachHere(e.toString());
            }
        }
        return urls;
    }

    private List classes(ZipFile zipFile, ClassLoader classLoader) {
        ArrayList classes = new ArrayList();
        for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            //Filter by filename; otherwise we'll be loading all the classes,
            // which takes
            //significantly longer [Bob Boseko]
            if (!(entry.getName().endsWith("Extension.class") || entry
                    .getName().endsWith("Configuration.class"))) {
                //Include "Configuration" for backwards compatibility. [Jon
                // Aquino]
                continue;
            }
            Class c = toClass(entry, classLoader);
            if (c != null) {
                classes.add(c);
            }
        }
        return classes;
    }

    private Class toClass(ZipEntry entry, ClassLoader classLoader) {
        if (entry.isDirectory()) {
            return null;
        }
        if (!entry.getName().endsWith(".class")) {
            return null;
        }
        if (entry.getName().indexOf("$") != -1) {
            //I assume it's not necessary to load inner classes explicitly.
            // [Bob Boseko]
            return null;
        }
        String className = entry.getName();
        className = className.substring(0, className.length() - ".class".length());
        className = StringUtil.replaceAll(className, "/", ".");
        Class candidate;
        try {
            candidate = classLoader.loadClass(className);
//////            candidate = Class.forName("com/osfac/dmt/workbench/plugin/Extension");
        } catch (ClassNotFoundException e) {
            Assert.shouldNeverReachHere("Class not found: " + className + ". Refine class name algorithm.");
            return null;
        } catch (Throwable t) {
            LOG.error(LOADING_ERROR + " " + className + ":");
            //e.g. java.lang.VerifyError: class
            // org.apache.xml.serialize.XML11Serializer
            //overrides final method
            t.printStackTrace(System.out);
            return null;
        }
        return candidate;
    }

    public Collection getConfigurations() {
        return Collections.unmodifiableCollection(configurations);
    }

    /**
     * To access extension classes, use this ClassLoader rather than the default ClassLoader.
     * Extension classes will not be present in the latter.
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * @return possibly null
     */
    public File getPlugInDirectory() {
        return plugInDirectory;
    }
}
