package com.osfac.dmt.workbench;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.osfac.dmt.Config;
import com.osfac.dmt.DMTVersion;
import com.osfac.dmt.I18N;
import com.osfac.dmt.authen.AuthenDialog;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.util.commandline.CommandLine;
import com.osfac.dmt.util.commandline.OptionSpec;
import com.osfac.dmt.util.commandline.ParseException;
import com.osfac.dmt.workbench.driver.DriverManager;
import com.osfac.dmt.workbench.plugin.PlugInManager;
import com.osfac.dmt.workbench.ui.SplashPanel;
import com.osfac.dmt.workbench.ui.SplashWindow;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.openjump.OpenJumpConfiguration;

/**
 * This class is responsible for setting up and displaying the main JUMP workbench window.
 */
public final class DMTWorkbench {

    private static ImageIcon splashImage;

    public static ImageIcon splashImage() {
        // Lazily initialize it, as it may not even be called (e.g. EZiLink),
        // and we want the splash screen to appear ASAP [Bob Boseko]
        if (splashImage == null) {
            splashImage = IconLoader.icon("splash.png");
            //splashImage = IconLoader.icon(I18N.get("splash.png"));
        }
        return splashImage;
    }

    private static ArrayList<Image> appIcons() {
        ArrayList iconlist = new ArrayList();
        iconlist.add(IconLoader.image("app-icon.png"));
//        iconlist.add(IconLoader.image("oj_24.png"));
//        iconlist.add(IconLoader.image("oj_32.png"));
//        iconlist.add(IconLoader.image("oj_48.png"));
//        iconlist.add(IconLoader.image("oj_256.png"));
        //java.util.Collections.reverse(iconlist);
        return iconlist;
    }
    // for java 1.5-
    public static final ImageIcon APP_ICON = IconLoader.icon("app-icon.png");
    // for java 1.6+
    public static final ArrayList APP_ICONS = appIcons();
//    public static final String VERSION_TEXT = I18N.get("JUMPWorkbench.version.number");
    //-- dont change the following strings
    public final static String PROPERTIES_OPTION = "properties";
    public final static String DEFAULT_PLUGINS = "default-plugins";
    public final static String PLUG_IN_DIRECTORY_OPTION = "plug-in-directory";
    public final static String I18N_FILE = "i18n";
    public static final String INITIAL_PROJECT_FILE = "Project";
    public static final String STATE_OPTION = "state";
    // Added by STanner to allow I18N to have access to this
    public static String I18N_SETLOCALE = "";
    private static Class progressMonitorClass = SingleLineProgressMonitor.class;
    //<<TODO:REFACTORING>> Move images package under
    // com.osfac.dmt.workbench to avoid naming conflicts with other libraries.
    private static CommandLine commandLine;
    private WorkbenchContext context = new DMTWorkbenchContext(this);
    public static WorkbenchFrame frame;
    private DriverManager driverManager = new DriverManager(frame);
    private WorkbenchProperties dummyProperties = new WorkbenchProperties() {
        @Override
        public List getPlugInClasses() {
            return new ArrayList();
        }

        @Override
        public List getPlugInClasses(ClassLoader classLoader) {
            return new ArrayList();
        }

        @Override
        public List getInputDriverClasses() {
            return new ArrayList();
        }

        @Override
        public List getOutputDriverClasses() {
            return new ArrayList();
        }

        @Override
        public List getConfigurationClasses() {
            return new ArrayList();
        }
    };
    private WorkbenchProperties properties = dummyProperties;
    private PlugInManager plugInManager;
    private Blackboard blackboard = new Blackboard();
    private static ImageIcon icon;

    /**
     * @param o a window to decorate with icon
     */
    public static void setIcon(Object o) {
        // attach the right icon, depending on
        //  - availability of method setIconImages (java 1.5 vs. 1.6), several icons for different sizes
        //  - underlying object type (JFrame, JInternalFrame, others? )
        // let's go
        if (o instanceof JFrame) {
            JFrame f = (JFrame) o;
            try {
                // case java 1.6+
                Class[] types = {java.util.List.class};
                java.lang.reflect.Method method = JFrame.class.getMethod("setIconImages", types);

                Object[] params = {APP_ICONS};
                method.invoke(f, params);
                //System.out.println("jep");

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // case java 1.5-, is really bad with transparent pngs, so we stick with the old gif
                f.setIconImage((Image) APP_ICON.getImage());
                //System.out.println("noe");
            }
        } else if (o instanceof javax.swing.JInternalFrame) {
            //System.out.println("internal");
            javax.swing.JInternalFrame f = (javax.swing.JInternalFrame) o;
            f.setFrameIcon(getIcon());
        }
    }

    public static ImageIcon getIcon() {
        // java 1.5 is really bad with transparent pngs, so we stick with the old gif
        if (!(icon instanceof ImageIcon)) {
            Double jre_version = Double.parseDouble(System.getProperty("java.version").substring(0, 3));
            if (jre_version < 1.6) {
                icon = APP_ICON;
            } else {
                icon = new ImageIcon();
                icon.setImage((Image) APP_ICONS.get(0));
            }
        }
        return icon;
    }

    /**
     * @param title
     * @param args
     * @param monitor
     * @throws java.lang.Exception
     */
    public DMTWorkbench(String title, String[] args, TaskMonitor monitor) throws Exception {
        Config config = new Config();
        frame = new WorkbenchFrame(title, context); //Application Main Interface

        boolean defaultFileExists = false;
        File defaultFile = null;
        if (commandLine.hasOption(DEFAULT_PLUGINS)) {
            defaultFile = new File(commandLine.getOption(DEFAULT_PLUGINS).getArg(0));
            if (defaultFile.exists()) {
                defaultFileExists = true;
                //[sstein 6.July.2008] disabled to enable loading of two properties files
                //properties = new WorkbenchPropertiesFile(defaultFile, frame);
            } else {
                System.out.println(new StringBuilder("OSFAC-DMT: Warning: Default plugins file does not exist: ")
                        .append(defaultFile).toString());
            }
        }

        boolean propertiesFileExists = false;
        File propertiesFile = null;
        if (commandLine.hasOption(PROPERTIES_OPTION)) {
            propertiesFile = new File(commandLine.getOption(PROPERTIES_OPTION).getArg(0));
            if (propertiesFile.exists()) {
                //[sstein 6.July.2008] disabled to enable loading of two properties files
                //properties = new WorkbenchPropertiesFile(propertiesFile, frame);
                propertiesFileExists = true;
            } else {
                System.out.println(new StringBuilder("OSFAC-DMT: Warning: Properties file does not exist: ")
                        .append(propertiesFile).toString());
            }
        }

        if ((defaultFileExists) && (propertiesFileExists)) {
            properties = new WorkbenchPropertiesFile(defaultFile, propertiesFile, frame);
        } else if (defaultFileExists) {
            properties = new WorkbenchPropertiesFile(defaultFile, frame);
        } else if (propertiesFileExists) {
            properties = new WorkbenchPropertiesFile(propertiesFile, frame);
        }

        File extensionsDirectory;
        if (commandLine.hasOption(PLUG_IN_DIRECTORY_OPTION)) {
            extensionsDirectory = new File(commandLine.getOption(PLUG_IN_DIRECTORY_OPTION).getArg(0));
            if (!extensionsDirectory.exists()) {
                System.out.println(new StringBuilder("OSFAC-DMT: Warning: Extensions directory does not exist: ")
                        .append(extensionsDirectory).toString());
                extensionsDirectory = null;
            }
        } else {
//            extensionsDirectory = new File(getClass().getResource(I18N.get("JUMPWorkbench.lib-ext")).toURI());
            extensionsDirectory = new File(I18N.get("JUMPWorkbench.lib-ext"));
            if (!extensionsDirectory.exists()) {
                // Added further information so that debug user will know where
                // it is actually looking for as the extension directory.
                System.out.println(new StringBuilder("OSFAC-DMT: Warning: Extensions directory does not exist: ")
                        .append(extensionsDirectory).append(" where homedir = [")
                        .append(System.getProperty("user.dir")).append("]").toString());
                extensionsDirectory = null;
            }
        }
        if (commandLine.hasOption(INITIAL_PROJECT_FILE)) {
            String task = commandLine.getOption(INITIAL_PROJECT_FILE).getArg(0);
            this.getBlackboard().put(INITIAL_PROJECT_FILE, task);
        }

        // open files from command line takes place in FirstTaskFramePlugIn
        if (commandLine.hasOption(STATE_OPTION)) {
            File option = new File(commandLine.getOption(STATE_OPTION).getArg(0));
            if (option.isDirectory()) {
                PersistentBlackboardPlugIn.setPersistenceDirectory(option.getPath());
            }
            if (option.isFile()) {
                PersistentBlackboardPlugIn.setFileName(option.getName());
                PersistentBlackboardPlugIn.setPersistenceDirectory(option.getAbsoluteFile().getParent());
            }
        }
        plugInManager = new PlugInManager(context, extensionsDirectory, monitor);
        //Load drivers before initializing the frame because part of the frame
        //initialization is the initialization of the driver dialogs. [Jon
        //The initialization of some plug-ins (e.g. LoadDatasetPlugIn) requires
        // that the drivers be loaded. Thus load the drivers here, before the
        // plug-ins are initialized.
        driverManager.loadDrivers(properties);
    }

    //Main entry method for the application
    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("OSFAC", "OSFAC-DMT", "vx1xhNgC4CtD2SQc.kC5mp99mO0Bs1d2");
        //Get the previous theme (lookAndFeel) used
        Config.lookAndFeel = Config.pref.get(SettingKeyFactory.Theme.LOOKANDFEEL, Config.lookAndFeel);
        ((Office2003Painter) Office2003Painter.getInstance()).setColorName(Config.lookAndFeel);
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);//Set the theme
//        long start = PlugInManager.secondsSince(0);
        try {
            // first fetch parameters, LOCALE might be changed with -i18n switch
            parseCommandLine(args);
            // load i18n specified in command line ( '-i18n translation' )
            if (commandLine.hasOption(I18N_FILE)) {
                I18N_SETLOCALE = commandLine.getOption(I18N_FILE).getArg(0);
                // initialize I18N
                I18N.loadFile(I18N_SETLOCALE);
            }

            // setFont to switch fonts if defaults cannot display current language
            setFont();
            ProgressMonitor progressMonitor = (ProgressMonitor) progressMonitorClass.newInstance();
            SplashPanel splashPanel = new SplashPanel(splashImage(),
                    new StringBuilder(I18N.get("ui.AboutDialog.version")).append(" ").
                    append(DMTVersion.CURRENT_VERSION).toString());
            splashPanel.add(progressMonitor, new GridBagConstraints(0, 10, 1, 1, 1,
                    0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));

            main(args, new StringBuilder(I18N.get("ui.WorkbenchFrame.title")).append(" ")
                    .append(I18N.get("JUMPWorkbench.version.number")).append(" ")
                    .append(WorkbenchFrame.TypeOfVersion).toString(), new DMTConfiguration(), splashPanel, progressMonitor);
//            System.out.println("OJ start took " + PlugInManager.secondsSince(start) + "s alltogether.");
        } catch (Throwable t) {
            WorkbenchFrame.showThrowable(t, null);
        }
    }

    /**
     * setupClass is specified as a String to prevent it from being loaded before we display the
     * splash screen, in case setupClass takes a long time to load.
     *
     * @param args main application arguments
     * @param title application title
     * @param setup an object implementing the Setup interface (e.g. JUMPConfiguration)
     * @param splashComponent a component to open until the workbench frame is displayed
     * @param taskMonitor notified of progress of plug-in loading
     */
    public static void main(String[] args, String title, Setup setup, JComponent splashComponent, TaskMonitor taskMonitor) {
        try {
            SplashWindow splashWindow = new SplashWindow(splashComponent);
            splashWindow.setVisible(true);
            taskMonitor.report(I18N.get("JUMPWorkbench.status.create"));

            DMTWorkbench workbench = new DMTWorkbench(title, args, taskMonitor);
            taskMonitor.report(I18N.get("JUMPWorkbench.status.configure-core"));
            setup.setup(workbench.context);
            //must wait until after setup initializes the persistent blackboard to recall settings

            WorkbenchFrame _frame = workbench.getFrame();
            taskMonitor.report(I18N.get("JUMPWorkbench.status.restore-state"));
//            _frame.restore();
            taskMonitor.report(I18N.get("JUMPWorkbench.status.load-extensions"));

            workbench.context.getWorkbench().getPlugInManager().load(); //load the plugIn Manager

            /**
             * The OpenJumpConfiguration class contains some plugins shown as menuitems in the
             * different application menus
             */
            OpenJumpConfiguration.postExtensionInitialization(workbench.context);
//////            Config.optionsDialog = SettingOptionsDialog.showOptionsDialog();

            if (Config.isLiteVersion()) {
                WorkbenchFrame.idUser = 4;
                _frame.showFrame(splashWindow);
            } else {
                new AuthenDialog(splashWindow).setVisible(true);
            }
        } catch (Throwable t) {
            WorkbenchFrame.showThrowable(t, null);
        }
    }

    // this is in preparation that we might want to support more fonts in the future
    private static Font[] loadFonts() throws Exception {
        Font font = Font.createFont(Font.TRUETYPE_FONT, Class.class.getClass()
                .getResource("/language/fonts/code2000.ttf").openStream());
        // since 1.6 we could register the font and use it by name
        // but using the font directly makes us 1.5 compatible
        // GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        return new Font[]{font};
    }

    private static boolean setFont() throws Exception {
        String test = I18N.get("ui.MenuNames.FILE");
        boolean replaced = false;
        Font font = null;
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            // loop over fontuires entries
            if (value instanceof javax.swing.plaf.FontUIResource) {
                FontUIResource fold = ((javax.swing.plaf.FontUIResource) value);
                // can default font display test sentence?
                if (fold.canDisplayUpTo(test) != -1) {
                    // fetch replacement candidate
                    if (font == null) {
                        font = loadFonts()[0];
                    }
                    // copy attributes
                    Map attrs = fold.getAttributes();
                    // remove family attribute
                    java.text.AttributedCharacterIterator.Attribute fam = null;
                    for (Iterator iterator = attrs.keySet().iterator(); iterator
                            .hasNext();) {
                        fam = (java.text.AttributedCharacterIterator.Attribute) iterator
                                .next();
                        if (fam.toString().equals("java.awt.font.TextAttribute(family)")) {
                            break;
                        }
                    }
                    if (fam != null) {
                        attrs.remove(fam);
                    }
                    // create the new fontuires
                    FontUIResource fnew = new javax.swing.plaf.FontUIResource(font.deriveFont(attrs));

                    // check if new font can display and set
                    if (fnew.canDisplayUpTo(test) == -1) {
                        UIManager.put(key, fnew);
                        replaced = true;
                    }
                }
            }
        }

        /*
         * // show all registered available fonts GraphicsEnvironment e =
         * GraphicsEnvironment.getLocalGraphicsEnvironment(); for (String foo :
         * e.getAvailableFontFamilyNames()){ System.out.println(foo); }
         */
        // replaced any?
        return replaced;
    }

    public DriverManager getDriverManager() {
        return driverManager;
    }

    /**
     * The properties file; not to be confused with the WorkbenchContext properties.
     */
    public WorkbenchProperties getProperties() {
        return properties;
    }

    public WorkbenchFrame getFrame() {
        return frame;
    }

    public WorkbenchContext getContext() {
        return context;
    }

    private static void parseCommandLine(String[] args) throws WorkbenchException {
        //<<TODO:QUESTION>> Notify MD: using CommandLine [Bob Boseko]
        commandLine = new CommandLine('-');
        commandLine.addOptionSpec(new OptionSpec(PROPERTIES_OPTION, 1));
        commandLine.addOptionSpec(new OptionSpec(DEFAULT_PLUGINS, 1));
        commandLine.addOptionSpec(new OptionSpec(PLUG_IN_DIRECTORY_OPTION, 1));
        commandLine.addOptionSpec(new OptionSpec(I18N_FILE, 1));
        //[UT] 17.08.2005
        commandLine.addOptionSpec(new OptionSpec(INITIAL_PROJECT_FILE, 1));
        commandLine.addOptionSpec(new OptionSpec(STATE_OPTION, 1));
        try {
            commandLine.parse(args);
        } catch (ParseException e) {
            throw new WorkbenchException("A problem occurred parsing the command line: " + e.toString());
        }
    }

    public PlugInManager getPlugInManager() {
        return plugInManager;
    }

    //<<TODO>> Make some properties persistent using a #makePersistent(key) method.
    /**
     * Expensive data structures can be cached on the blackboard so that several plug-ins can share
     * them.
     */
    public Blackboard getBlackboard() {
        return blackboard;
    }

    private static abstract class ProgressMonitor extends JPanel implements TaskMonitor {

        private Component component;

        public ProgressMonitor(Component component) {
            this.component = component;
            setLayout(new BorderLayout());
            add(component, BorderLayout.CENTER);
            setOpaque(false);
        }

        protected Component getComponent() {
            return component;
        }

        protected abstract void addText(String s);

        @Override
        public void report(String description) {
            addText(description);
        }

        @Override
        public void report(int itemsDone, int totalItems, String itemDescription) {
            addText(itemsDone + " / " + totalItems + " " + itemDescription);
        }

        @Override
        public void report(Exception exception) {
            addText(StringUtil.toFriendlyName(exception.getClass().getName()));
        }

        @Override
        public void allowCancellationRequests() {
        }

        @Override
        public boolean isCancelRequested() {
            return false;
        }
    }

    private static class SingleLineProgressMonitor extends ProgressMonitor {

        public SingleLineProgressMonitor() {
            super(new JLabel(" "));
            ((JLabel) getComponent()).setFont(((JLabel) getComponent())
                    .getFont().deriveFont(Font.BOLD));
            ((JLabel) getComponent()).setHorizontalAlignment(JLabel.LEFT);
        }

        @Override
        protected void addText(String s) {
            ((JLabel) getComponent()).setText(s);
        }
    }
}
