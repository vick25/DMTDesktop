package com.osfac.dmt.workbench.ui;

import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.action.DefaultDockableBarDockableHolder;
import com.jidesoft.action.DockableBarContext;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.document.*;
import com.jidesoft.document.DocumentPane.TabbedPaneCustomizer;
import com.jidesoft.status.*;
import com.jidesoft.swing.ContentContainer;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.tooltip.BalloonTip;
import com.jidesoft.utils.Lm;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.about.About;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.setting.panel.LanguagePanel;
import com.osfac.dmt.tools.DBUpdating;
import com.osfac.dmt.tools.LoadDefaultLayers;
import com.osfac.dmt.tools.UnZipTool;
import com.osfac.dmt.tools.geosearch.GeoResult;
import com.osfac.dmt.tools.request.DataRequestManager;
import com.osfac.dmt.tools.request.DataRequestSync;
import com.osfac.dmt.tools.search.QuerySearch;
import com.osfac.dmt.tools.statistic.Statistic;
import com.osfac.dmt.tools.update.UpdateDB;
import com.osfac.dmt.user.UserManager;
import com.osfac.dmt.util.Block;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.DMTCommandBarFactory;
import com.osfac.dmt.workbench.DMTConfiguration;
import com.osfac.dmt.workbench.DMTIconsFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerEventType;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.model.UndoableEditReceiver;
import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.osfac.dmt.workbench.ui.renderer.style.ChoosableStyle;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.openjump.core.model.TaskEvent;
import org.openjump.core.model.TaskListener;
import org.openjump.core.ui.plugin.file.OpenFilePlugIn;
import org.openjump.swing.factory.component.ComponentFactory;

public class WorkbenchFrame extends DefaultDockableBarDockableHolder implements LayerViewPanelContext, ViewportListener, ErrorHandlerV2 {

    @SuppressWarnings("LeakingThisInConstructor")
    public WorkbenchFrame(String title, final WorkbenchContext workbenchContext) throws Exception {
        if (Config.isFullVersion()) {
            this.setTitle(title + "   [" + I18N.get("Text.Server-IP-Address-text") + " " + Config.host + "]");
        } else {
            this.setTitle(title);
        }
        BSConnect = new JideButton(new ImageIcon(getClass().getResource(""
                + "/com/osfac/dmt/images/base.png")));
        BSConnect.setToolTipText(I18N.get("Text.connect-to-FTP-server-text") + " [" + Config.host + "]");
        BSConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToFTPServer();
            }
        });
//        dbprocessing = new DBProcessing(this, true);
        menuBar.setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        menuBar.setInitIndex(0);
        menuBar.setInitSubindex(0);
        menuBar.setPaintBackground(false);
        menuBar.setStretch(true);
        menuBar.setFloatable(true);
        // add toolbars annd menus
        this.getDockableBarManager().addDockableBar(menuBar);
        this.getDockableBarManager().addDockableBar(cbf.createStandardCommandBar());
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitMenuItem_actionPerformed(e);
            }
        });
        windowMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuSelected(MenuEvent e) {
                windowMenu_menuSelected(e);
            }
        });
        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        WorkbenchFrame.workbenchContext = workbenchContext;
        // set icon for the app frame
        DMTWorkbench.setIcon(this);
        toolBar = new WorkbenchToolBar(workbenchContext);
        toolBar.setTaskMonitorManager(new TaskMonitorManager());
        new RecursiveKeyListener(this) {
            public void keyTyped(KeyEvent e) {
                for (Iterator i = easyKeyListeners.iterator(); i.hasNext();) {
                    KeyListener l = (KeyListener) i.next();
                    l.keyTyped(e);
                }
            }

            public void keyPressed(KeyEvent e) {
                for (Iterator i = new ArrayList(easyKeyListeners).iterator(); i.hasNext();) {
                    KeyListener l = (KeyListener) i.next();
                    l.keyPressed(e);
                }
            }

            public void keyReleased(KeyEvent e) {
                for (Iterator i = new ArrayList(easyKeyListeners).iterator(); i.hasNext();) {
                    KeyListener l = (KeyListener) i.next();
                    l.keyReleased(e);
                }
            }
        };
        installKeyboardShortcutListener();
    }

    public DefaultDockableBarDockableHolder showFrame() {
        this.setIconImage(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.ICON).getImage());
        // add a window listener to do clear up when windows closing.
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        // set the profile key
        this.getLayoutPersistence().setProfileKey(PROFILE_NAME);
        this.getLayoutPersistence().setXmlFormat(true);
        // create tabbed-document interface and add it to workspace area
        _documentPane = createDocumentTab();
        _documentPane.setTabbedPaneCustomizer(new TabbedPaneCustomizer() {
            @Override
            public void customize(final JideTabbedPane tabbedPane) {
                tabbedPane.setShowCloseButtonOnTab(true);
                tabbedPane.setShowCloseButtonOnSelectedTab(true);
            }
        });
        this.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        this.getDockingManager().getWorkspace().add(_documentPane, BorderLayout.CENTER);

        this.getDockableBarManager().addDockableBar(cbf.createToolsCommandBar());
//        menuBar.getMenu(3).getItem(10).setVisible(false);
        menuBar.getMenu(2).add(cbf.createViewMenu(), 0);
        menuBar.getMenu(4).getItem(0).setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.SETTING));
        exitMenuItem.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.EXIT));
        cbf.createToolsMenu(menuBar.getMenu(5));
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            if (i == IndexMenu) {
                continue;
            } else {
                menuBar.getMenu(i).addMenuListener(new MenuListener() {
                    @Override
                    public void menuSelected(MenuEvent e) {
                        _documentPane.setActiveDocument(I18N.get("Text.Geographic-Search"));
                    }

                    @Override
                    public void menuDeselected(MenuEvent e) {
                    }

                    @Override
                    public void menuCanceled(MenuEvent e) {
                    }
                });
            }
        }
        createLayersMenu(menuBar.getMenu(IndexMenu - 2));
        createCustomizeMenu(menuBar.getMenu(IndexMenu - 1));
        cbf.createWindowsMenu(menuBar.getMenu(++IndexMenu));
        createHelpMenu(menuBar.getMenu(++IndexMenu));

        _statusBar = createStatusBar();
        findImagesCategoriesAll();
        new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DMTConfiguration.findData.setEnabled(activeTaskFrame != null
                        && activeTaskFrame.getLayerViewPanel().getSelectionManager().getSelectedItemsCount() > 0);
            }
        }).start();
        if (Config.isFullVersion() && Config.isAdministrator()) {
            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Thread thread = new Thread() {
                        public void run() {
                            Connection con = remoteDBConnecting();
                            if (con != null) {
                                boolean requestFound = isNewDataRequestFound(con);
                                if (requestFound) {
                                    if (dataRequestSync != null) {
                                        dataRequestSync.setVisible(true);
                                    } else {
                                        dataRequestSync = new DataRequestSync(DMTWorkbench.frame, true);
                                        dataRequestSync.setVisible(true);
                                    }
                                }
//                        if (DataRequestManager.BSynchronize != null) {
//                            DataRequestManager.BSynchronize.setEnabled(requestFound);
//                        }
                            }
                        }
                    };
                    thread.start();
                }
            }, 5000);
        }
        this.getContentPane().add(_statusBar, BorderLayout.AFTER_LAST_LINE);
        this.getDockingManager().getWorkspace().setAdjustOpacityOnFly(true);
        this.getDockingManager().setUndoLimit(10);
        this.getDockingManager().beginLoadLayoutData();
        // add all dockable frames
        this.getDockingManager().addFrame(cbf.createFrameMenuTree());
        this.getDockingManager().setShowGripper(true);
        this.getDockingManager().setOutlineMode(DockingManager.TRANSPARENT_OUTLINE_MODE);
        this.getDockingManager().setPopupMenuCustomizer(new com.jidesoft.docking.PopupMenuCustomizer() {
            @Override
            public void customizePopupMenu(JPopupMenu menu, final DockingManager dockingManager, final DockableFrame dockableFrame, boolean onTab) {
                menu.addSeparator();
                menu.add(new AbstractAction(I18N.get("WorkbenchFrame.Move-to-Document-Area")) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dockingManager.removeFrame(dockableFrame.getKey(), true);

                        DocumentComponent documentComponent = new DocumentComponent((JComponent) dockableFrame.getContentPane(),
                                dockableFrame.getKey(),
                                dockableFrame.getTitle(),
                                dockableFrame.getFrameIcon());
//                        _documentPane.openDocument(documentComponent);
//                        _documentPane.setActiveDocument(documentComponent.getName());
                    }
                });
            }
        });
        // load layout information from previous session
        this.setState(JFrame.NORMAL);
        this.getLayoutPersistence().loadLayoutData();
        if (Lm.DEMO) {
            Lm.z();
        }
        timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panRequest.fillRequestTable();
                timer.stop();
            }
        });
        new Thread() {
            public void run() {
                Connection con = remoteDBConnecting();
                if (con != null) {
                    try {
                        PreparedStatement ps = con.prepareStatement("select * from updater");
                        ResultSet res = ps.executeQuery();
                        while (res.next()) {
                            if (!res.getString(2).equals(I18N.get("JUMPWorkbench.version.number"))) {
                                new_version = res.getString(2);
                                url_update = res.getString(3);
                                updater.setVisible(true);
                                showToolTip();
                            }
                        }
                    } catch (SQLException ex) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                                + ""), ex.getMessage(), null, null, ex, Level.SEVERE, null));
                    }
                }
            }
        }.start();
        fillDatabase();
        this.toFront();
        if (Config.isFullVersion()) {
            connectToFTPServer();
        }
        if (Config.pref.getBoolean(SettingKeyFactory.OtherFeatures.ChKLayer, true)) {
            OpenFilePlugIn filePlugin = new OpenFilePlugIn(workbenchContext, new File(""
                    + "default layers/Pays_COMIFAC.shp"));
            filePlugin.actionPerformed(new ActionEvent(this, 0, ""));
        }
        return this;
    }

    public void showFrame(JWindow splashWindow) {
        splashWindow.setVisible(false);
        showFrame();
    }

    private void connectToFTPServer() {
        try {
            final Socket sClient = new Socket(Config.host, Config.PORTMAINSERVER);
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        DataInputStream in = new DataInputStream(sClient.getInputStream());
                        DataOutputStream out = new DataOutputStream(sClient.getOutputStream());
                        byte[] buffer = new byte[512];
                        int nbbit;
                        while ((nbbit = in.read(buffer)) != -1) {
                            Thread.sleep(5000);
                            out.write("connection test".getBytes());
                            BSConnect.setEnabled(false);
                        }
                    } catch (IOException | InterruptedException e) {
                        BSConnect.setEnabled(true);
                        BSConnect.setToolTipText(I18N.get("Text.connect-to-FTP-server-text") + " [" + Config.host + "]");
                        progress.setProgress(100);
                        JXErrorPane.showDialog(DMTWorkbench.frame, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                                + ""), I18N.get("WorkbenchFrame.Text.not-cennected-to-server") + "\n" + e.getMessage(), null, null, e, Level.SEVERE, null));
//                        System.exit(0);                        
                        System.err.println(I18N.get("WorkbenchFrame.Text.not-cennected-to-server"));
//                        ex.printStackTrace();
                    }
                }
            };
            th.start();
            BSConnect.setEnabled(false);
            BSConnect.setToolTipText(I18N.get("Text.connect-to-FTP-server-text") + " [" + Config.host + "]");
        } catch (IOException e) {
            BSConnect.setEnabled(true);
            BSConnect.setToolTipText(I18N.get("Text.connect-to-FTP-server-text") + " [" + Config.host + "]");
            progress.setProgress(100);
            JXErrorPane.showDialog(DMTWorkbench.frame, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                    + ""), I18N.get("WorkbenchFrame.Text.unable-to-connect-to-server") + "\n" + e.getMessage(), null, null, e, Level.SEVERE, null));
////////            System.exit(0);
        }
    }

    private void fillDatabase() {
        try {
            PreparedStatement ps = Config.con.prepareStatement("select * from dmt_image");
            ResultSet res = ps.executeQuery();
        } catch (SQLException e) {
            new DBUpdating(this, true, new File("dbosfacdmt.sql")).setVisible(true);
        }
    }

    private class LogoContentContainer extends ContentContainer {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/osfac/dmt/images/home_text_logo.png"));
            imageIcon.paintIcon(this, g, getWidth() - imageIcon.getIconWidth() - 2, 2);
        }
    }

    protected ContentContainer createContentContainer() {
        return new LogoContentContainer();
    }

    private void jbInit(JPanel pan) {
        desktopPane.setBackground(Color.black);
        pan.setLayout(new BorderLayout());
        pan.add(toolBar, BorderLayout.NORTH);
        pan.add(desktopPane, BorderLayout.CENTER);
//        new Timer(1000, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                memoryLabel.setText(getMBCommittedMemory() + " MB Committed Memory");
//            }
//        }).start();
        messageTextField.setOpaque(true);
        messageTextField.setEditable(false);
        messageTextField.setToolTipText(I18N.get("WorkbenchFrame.Mark-and-copy-clipboard"));
        messageTextField.setFont(coordinateLabel.getFont());
        messageTextField.setText(" ");
        timeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        timeLabel.setText(" ");
        memoryLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        memoryLabel.setText(" ");

        scaleLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        scaleLabel.setText(" ");
        coordinateLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        coordinateLabel.setText(" ");

        statusPanel.setLayout(new BorderLayout());
        int dividerSize = 4;
        statusPanelSplitPane4 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, scaleLabel, coordinateLabel);
        statusPanelSplitPane4.setDividerSize(dividerSize);

        statusPanelSplitPane3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, memoryLabel, statusPanelSplitPane4);
        statusPanelSplitPane3.setDividerSize(dividerSize);

        statusPanelSplitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, timeLabel, statusPanelSplitPane3);
        statusPanelSplitPane2.setDividerSize(dividerSize);
        statusPanelSplitPane2.setResizeWeight(1.0);

        statusPanelSplitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, messageTextField, statusPanelSplitPane2);
        statusPanelSplitPane1.setDividerSize(dividerSize);
        statusPanelSplitPane1.setResizeWeight(1.0);
        // Workaround for java bug 4131528
        statusPanelSplitPane1.setBorder(null);
        statusPanelSplitPane2.setBorder(null);
        statusPanelSplitPane3.setBorder(null);
        statusPanelSplitPane4.setBorder(null);
        statusPanel.add(statusPanelSplitPane1, BorderLayout.CENTER);
        pan.add(statusPanel, BorderLayout.SOUTH);
    }

    private void findImagesCategoriesAll() {
        try {
            ChBCategoryList.clear();
            DMTConfiguration.findData.removeAll();
            ResultSet res = Config.con.createStatement().executeQuery("SELECT distinct category_name "
                    + "FROM dmt_category order by category_name");
            while (res.next()) {
                ChBCategoryList.add(new JCheckBoxMenuItem(res.getString(1)));
            }
            for (int i = 0; i < ChBCategoryList.size(); i++) {
                ChBCategoryList.get(i).setSelected(true);
                DMTConfiguration.findData.add(ChBCategoryList.get(i));
            }

            DMTConfiguration.findData.add(new JPopupMenu.Separator());
            DMTConfiguration.findData.add(MIClear);
            MIClear.setText(I18N.get("Text.Unselect-All-categories"));
            MIClear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < ChBCategoryList.size(); i++) {
                        ChBCategoryList.get(i).setSelected(checkAll);
                    }
                    if (checkAll) {
                        MIClear.setText(I18N.get("Text.Unselect-All-categories"));
                    } else {
                        MIClear.setText(I18N.get("Text.select-All-categories"));
                    }
                    checkAll = !checkAll;
                }
            });
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                    + ""), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
    }

    private void findImagesCategories() {
        try {
            ChBCategoryList.clear();
            DMTConfiguration.findData.removeAll();
            Object FeatureTab[] = activeTaskFrame.getLayerViewPanel().getSelectionManager().getFeatureSelection().getSelectedItems().toArray();
            String where = "";
            for (int i = 0; i < FeatureTab.length; i++) {
                where += " (Intersects(GeomFromText('" + FeatureTab[i] + "'), shape) = 1) or";
            }
            where = where.substring(0, where.length() - 3);
            ResultSet res = Config.con.createStatement().executeQuery("SELECT distinct category_name "
                    + "FROM dmt_category inner join dmt_image on dmt_category.id_category = "
                    + "dmt_image.id_category WHERE" + where + " order by category_name");
            while (res.next()) {
                ChBCategoryList.add(new JCheckBoxMenuItem(res.getString(1)));
            }
            for (int i = 0; i < ChBCategoryList.size(); i++) {
                ChBCategoryList.get(i).setSelected(true);
                DMTConfiguration.findData.add(ChBCategoryList.get(i));
            }

            DMTConfiguration.findData.add(new JPopupMenu.Separator());
            DMTConfiguration.findData.add(MIClear);
            MIClear.setText(I18N.get("Text.Unselect-All-categories"));
            MIClear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < ChBCategoryList.size(); i++) {
                        ChBCategoryList.get(i).setSelected(checkAll);
                    }
                    if (checkAll) {
                        MIClear.setText(I18N.get("Text.Unselect-All-categories"));
                    } else {
                        MIClear.setText(I18N.get("Text.select-All-categories"));
                    }
                    checkAll = !checkAll;
                }
            });
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                    + ""), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
    }

    public static void createPreviewLayer() {
        if (previewLayer == null) {
            PlugInContext plugincontext = workbenchContext.createPlugInContext();
            previewLayer = plugincontext.addLayer(StandardCategoryNames.WORKING, I18N.get(""
                    + "ui.plugin.AddNewLayerPlugIn.createPreviewLayer.title"),
                    AddNewLayerPlugIn.createBlankFeatureCollection());
            previewLayer.setFeatureCollectionModified(false).setEditable(true);
        }
    }

    private static String getCategoriesSelected() {
        String categories = "";
        for (int i = 0; i < ChBCategoryList.size(); i++) {
            if (ChBCategoryList.get(i).isSelected()) {
                categories += ChBCategoryList.get(i).getText() + ",";
            }
        }
        if (categories.endsWith(",")) {
            categories = categories.substring(0, categories.length() - 1);
        }

        String value = "";
        String tab[] = categories.split(",");
        for (int i = 0; i < tab.length; i++) {
            value += "\'" + tab[i].toString() + "\',";
        }
        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    public static Connection remoteDBConnecting() {
        Connection conN = null;
        try {
            String database = "dbosfacdmt";
            String hostN = "dbosfacdmt.db.8487892.hostedresource.com";
            String username = "dbosfacdmt";
            String password = "OsfacLab01";
            conN = DriverManager.getConnection("jdbc:mysql://" + hostN + "/"
                    + "" + database, username, password);
        } catch (SQLException ex) {
//            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
//                    + ""), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return conN;
    }

    private DocumentPane createDocumentTab() {
        DocumentPane documentPane = new DocumentPane() {
            // add function to maximize (autohideAll) the document pane when mouse double clicks on the tabs of DocumentPane.
            @Override
            protected IDocumentGroup createDocumentGroup() {
                IDocumentGroup group = super.createDocumentGroup();
                if (group instanceof JideTabbedPane) {
                    ((JideTabbedPane) group).addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                                if (!_autohideAll) {
                                    _fullScreenLayout = getDockingManager().getLayoutRawData();
                                    getDockingManager().autohideAll();
                                    _autohideAll = true;
                                } else {
                                    if (_fullScreenLayout != null) {
                                        getDockingManager().setLayoutRawData(_fullScreenLayout);
                                    }
                                    _autohideAll = false;
                                }
                                Component lastFocusedComponent = _documentPane.getActiveDocument().getLastFocusedComponent();
                                if (lastFocusedComponent != null) {
                                    lastFocusedComponent.requestFocusInWindow();
                                }
                            }
                        }
                    });
                }
                return group;
            }
        };
        documentPane.setTabPlacement(JTabbedPane.TOP);
        documentPane.setPopupMenuCustomizer(new PopupMenuCustomizer() {
            @Override
            public void customizePopupMenu(JPopupMenu menu, final IDocumentPane pane, final String dragComponentName,
                    final IDocumentGroup dropGroup, boolean onTab) {
                if (!pane.isDocumentFloating(dragComponentName)) {
                    menu.addSeparator();
                    menu.add(new AbstractAction(I18N.get("WorkbenchFrame.Dock-to-the-Side")) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DocumentComponent documentComponent = pane.getDocument(dragComponentName);
                            if (documentComponent != null) {
                                pane.closeDocument(dragComponentName);

                                // check if the document is really closed. There are cases a document is not closable or veto closing happens which can keep the document open after closeDocument call.
                                if (!pane.isDocumentOpened(dragComponentName)) {
                                    final DockableFrame frame = new DockableFrame(documentComponent.getName(), documentComponent.getIcon());
                                    frame.setTabTitle(documentComponent.getTitle());
                                    frame.getContentPane().add(documentComponent.getComponent());
                                    frame.setInitIndex(0);
                                    frame.setInitSide(DockContext.DOCK_SIDE_EAST);
                                    frame.setInitMode(DockContext.STATE_FRAMEDOCKED);
                                    getDockingManager().addFrame(frame);
                                    getDockingManager().activateFrame(frame.getKey());
                                }
                            }
                        }
                    });
                }
            }
        });
        JPanel pan = new JPanel();
        jbInit(pan);
        configureStatusLabel(timeLabel, 175);
        configureStatusLabel(scaleLabel, 90);
        configureStatusLabel(coordinateLabel, 175);
        DocumentComponent geoSearch = new DocumentComponent(pan, I18N.get("Text.Geographic-Search"), I18N.get("Text.Geographic-Search"),
                DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.ICON));
        geoSearch.setClosable(false);
        documentPane.setFocusable(false);
        documentPane.openDocument(geoSearch);
        return documentPane;
    }

    private StatusBar createStatusBar() {
        StatusBar statusBar = new StatusBar();
        progress = new ProgressStatusBarItem();
        statusBar.add(progress, JideBoxLayout.VARY);
        DataRequestFound = new JideButton();
        DataRequestFound.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.SYNC));
        DataRequestFound.setToolTipText(I18N.get("DataRequestSync.Synchronizing-database"));
        DataRequestFound.setFocusable(false);
        DataRequestFound.setVisible(false);
        DataRequestFound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataRequestFound.setVisible(false);
                if (dataRequestSync != null) {
                    dataRequestSync.setVisible(true);
                }
            }
        });
        if (Config.isFullVersion()) {
            statusBar.add(BSConnect, JideBoxLayout.FLEXIBLE);
            statusBar.add(DataRequestFound, JideBoxLayout.FLEXIBLE);
        }

        updater.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.REQUESTDATA));
        updater.setToolTipText(I18N.get("Text.updater.tooltip"));
        updater.setFocusable(false);
        updater.setVisible(false);
        updater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                actionUpdater();
//                toggleToolTip();
                showToolTip();
            }
        });
        statusBar.add(updater, JideBoxLayout.FLEXIBLE);

        LabelStatusBarItem label = new LabelStatusBarItem("Line");
        label.setText("dmt@osfac.net");
        label.setAlignment(JLabel.CENTER);
        statusBar.add(label, JideBoxLayout.FLEXIBLE);
        timeStatusBar = new TimeStatusBarItem();
        timeStatusBar.setTextFormat(new SimpleDateFormat(I18N.get("language.format.date") + "  HH:mm:ss"));
        statusBar.add(timeStatusBar, JideBoxLayout.FLEXIBLE);
        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
        statusBar.add(gc, JideBoxLayout.FLEXIBLE);
        progress.setProgress(100);
        return statusBar;
    }

    private boolean isNewDataRequestFound(Connection con) {
        try {
            PreparedStatement ps = con.prepareStatement("select id_delivery from "
                    + "dmt_delivery where confirm_request_treated = ?");
            ps.setString(1, "No");
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                res.getString(1);
                return true;
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                    + ""), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return false;
    }

    private JPanel createToolTipContent() {
        JPanel fieldPanel = new JPanel(new BorderLayout(6, 6));
        fieldPanel.setOpaque(false);
        JideButton bt = new JideButton(I18N.get("Text.updater.button-update-text") + " " + new_version);
        bt.setButtonStyle(3);
        bt.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/browser(17).png")));
        bt.setAlwaysShowHyperlink(true);
        bt.setForeground(Color.blue);
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionUpdater();
            }
        });
        fieldPanel.add(bt);
        return fieldPanel;
    }

    private void showToolTip() {
        _balloonTip = new BalloonTip(createToolTipContent());
        _balloonTip.show(updater, 10, 6);
    }

    private void hideToolTip() {
        if (_balloonTip != null) {
            _balloonTip.hide();
            _balloonTip = null;
        }
    }

    private void actionUpdater() {
        if (Desktop.isDesktopSupported()) {
            hideToolTip();
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url_update));
            } catch (URISyntaxException | IOException e) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                        + ""), e.getMessage(), null, null, e, Level.SEVERE, null));
            }
        }
    }

    public static void actionFindData() {
        final String categories = getCategoriesSelected();
        if (categories.isEmpty() || categories.equals("''")) {
            JOptionPane.showMessageDialog(DMTWorkbench.frame, I18N.get(""
                    + "Text.WorkbenchFrame.categories-not-selected"), I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        progress.setProgressStatus(I18N.get("WorkbenchFrame.Retrieving-satellite-images-in-database"));
                        progress.setIndeterminate(true);
                        Object FeatureTab[] = activeTaskFrame.getLayerViewPanel().
                                getSelectionManager().getFeatureSelection().getSelectedItems().toArray();
                        String where = " (category_name in (" + categories + ")) and";
                        for (int i = 0; i < FeatureTab.length; i++) {
                            where += " (Intersects(GeomFromText('" + FeatureTab[i] + "'), shape) = 1) or";
                        }
                        where = where.substring(0, where.length() - 3);
//                        System.out.println(where);
                        ResultSet res = Config.con.createStatement().executeQuery("SELECT distinct id_image "
                                + "FROM dmt_image inner join dmt_category on dmt_category.id_category = "
                                + "dmt_image.id_category WHERE" + where);
                        ArrayList<Integer> IDsList = new ArrayList<>();
                        while (res.next()) {
                            IDsList.add(res.getInt(1));
                        }
//                        dbprocessing.setVisible(false);
                        progress.setProgress(100);
                        if (IDsList.isEmpty()) {
                            JOptionPane.showMessageDialog(DMTWorkbench.frame, ""
                                    + I18N.get("WorkbenchFrame.No-image-found-in-database"), I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
                        } else {
                            new GeoResult(DMTWorkbench.frame, true, IDsList).setVisible(true);
                            if (previewLayer != null) {
                                previewLayer.getLayerManager().dispose(DMTWorkbench.frame, previewLayer);
                                previewLayer = null;
                            }
                        }
                    } catch (SQLException | HeadlessException e) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                                + "", e.getMessage(), null, null, e, Level.SEVERE, null));
                    }
                }
            }.start();
//            dbprocessing.setVisible(true);
        }
    }

    public static void actionHelp() {
        try {
            Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                    + "" + new File("OSFAC-DMT.chm"));
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                    + ""), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private void actionAbout() {
        new About(this, true).setVisible(true);
    }

    private void actionDBUpdating() {
        JFileChooser fc = new JFileChooser(Config.getDefaultDirectory());
        fc.setFileFilter(new FileNameExtensionFilter(I18N.get("WorkbenchFrame.Text.db-Updates-format-sql-file"), "sql"));
        int result = fc.showOpenDialog(DMTWorkbench.frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            Config.setDefaultDirectory(fc.getSelectedFile().getParent());
            new DBUpdating(this, true, fc.getSelectedFile()).setVisible(true);
        }
    }

    public static void actionUnzipFiles() {
        JFileChooser fc = new JFileChooser(Config.getDefaultDirectory());
        fc.setFileFilter(new FileNameExtensionFilter(I18N.get("UnZipTool.description-format"), "zip"));
        fc.setMultiSelectionEnabled(true);
        int result = fc.showOpenDialog(DMTWorkbench.frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            Config.setDefaultDirectory(fc.getSelectedFile().getParent());
            ArrayList<String> list = new ArrayList<>();
            File[] tabFiles = fc.getSelectedFiles();
            for (int i = 0; i < tabFiles.length; i++) {
                list.add(tabFiles[i].getAbsolutePath());
            }
            new UnZipTool(DMTWorkbench.frame, true, list).setVisible(true);
        }
    }

    public static void actionShowOptionDialog() {
        Config.optionsDialog.setVisible(true);
    }

    public static void actionQuerySearch() {
        if (!_documentPane.isDocumentOpened(I18N.get("Text.Query-Search"))) {
            DocumentComponent document = new DocumentComponent(new QuerySearch(), I18N.get("Text.Query-Search"), I18N.get("Text.Query-Search")
                    + "", DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.QUERYSEARCH));
            _documentPane.openDocument(document);
        }
        _documentPane.setActiveDocument(I18N.get("Text.Query-Search"));
    }

    public static void actionGeoSearch() {
        _documentPane.setActiveDocument(I18N.get("Text.Geographic-Search"));
    }

    public static void actionDataRequest() {
        if (!_documentPane.isDocumentOpened(I18N.get("Text.data-request-manager-tab"))) {
            DocumentComponent document = new DocumentComponent(new DataRequestManager(), ""
                    + I18N.get("Text.data-request-manager-tab"), I18N.get("Text.data-request-manager-tab")
                    + "", DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.REQUEST));
            _documentPane.openDocument(document);
        }
        _documentPane.setActiveDocument(I18N.get("Text.data-request-manager-tab"));
    }

    public static void actionUpdateDatabase() {
        if (!_documentPane.isDocumentOpened(I18N.get("Text.Update-Database"))) {
            DocumentComponent document = new DocumentComponent(new UpdateDB(), I18N.get("Text.Update-Database"), I18N.get("Text.Update-Database")
                    + "", DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.DATABASE));
            _documentPane.openDocument(document);
        }
        _documentPane.setActiveDocument(I18N.get("Text.Update-Database"));
    }

    public static void actionStatistic() {
        if (!_documentPane.isDocumentOpened(I18N.get("Text.Statistic"))) {
            DocumentComponent document = new DocumentComponent(new Statistic(), I18N.get("Text.Statistic"), I18N.get("Text.Statistic")
                    + "", DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.STATISTIC));
            _documentPane.openDocument(document);
        }
        _documentPane.setActiveDocument(I18N.get("Text.Statistic"));
    }

    public static void actionUserManagement() {
        if (!_documentPane.isDocumentOpened(I18N.get("Text.User-Manager"))) {
            DocumentComponent document = new DocumentComponent(new UserManager(), I18N.get("Text.User-Manager"), I18N.get("Text.User-Manager")
                    + "", DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.USER));
            _documentPane.openDocument(document);
        }
        _documentPane.setActiveDocument(I18N.get("Text.User-Manager"));
    }

    public static void actionCompositeBand() {
    }

    public static void actionLoadLayers() {
        new LoadDefaultLayers(DMTWorkbench.frame, true).setVisible(true);
    }

    private void clearUp() {
        if (this.getLayoutPersistence() != null) {
            this.getLayoutPersistence().saveLayoutData();
        }
        if (_documentPane != null) {
            _documentPane.dispose();
            _documentPane = null;
        }
        if (_statusBar != null && _statusBar.getParent() != null) {
            _statusBar.getParent().remove(_statusBar);
        }
//        timer.stop();
        _statusBar = null;
        this.dispose();
    }

    private void createHelpMenu(JMenu HelpMenu) {
        JMenuItem MIHelp = new JMenuItem();
        JMenuItem MIAbout = new JMenuItem();
        JMenuItem MIUpdate = new JMenuItem();
        HelpMenu.add(new JPopupMenu.Separator());
        HelpMenu.setText(I18N.get("WorkbenchFrame.Text.Help"));
        MIHelp.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.HELP));
        MIHelp.setText(I18N.get("WorkbenchFrame.Text.Help"));
        MIHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionHelp();
            }
        });
        MIUpdate.setText(I18N.get("WorkbenchFrame.Text.db-Updates"));
        MIUpdate.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.UPDATE));
        HelpMenu.add(MIUpdate);
        HelpMenu.add(new JPopupMenu.Separator());
        HelpMenu.add(MIHelp);
        HelpMenu.add(MIAbout);
        MIAbout.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.ABOUT));
        MIAbout.setText(I18N.get("WorkbenchFrame.Text.About"));
        MIAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAbout();
            }
        });
        MIUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDBUpdating();
            }
        });
    }

    private void createCustomizeMenu(JMenu menu) {
        JMenu MILanguage = new JMenu();
        JMenuItem MIEnglish = new JMenuItem(I18N.get("language.english"));
        MIEnglish.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/setting/images/en.gif")));
        JMenuItem MIFrench = new JMenuItem(I18N.get("language.french"));
        MIFrench.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/setting/images/fr.gif")));
        JMenuItem MISpanish = new JMenuItem(I18N.get("language.spanish"));
        MISpanish.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/setting/images/es.gif")));
        MIEnglish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionLanguage(0);
            }
        });
        MIFrench.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionLanguage(1);
            }
        });
        MISpanish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionLanguage(2);
            }
        });
        MILanguage.add(MIEnglish);
        MILanguage.add(MIFrench);
        MILanguage.add(MISpanish);
        MILanguage.setText(I18N.get("language.language"));
        menu.add(MILanguage, 0);
        menu.add(cbf.createThemeMenu(), 1);
    }

    private void createLayersMenu(JMenu menu) {
        JMenuItem MILoadLayer = new JMenuItem(I18N.get("Text.Load-Default-Layers"));
        MILoadLayer.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/layer2.png")));
        MILoadLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionLoadLayers();
            }
        });
        menu.add(new JPopupMenu.Separator());
        menu.add(MILoadLayer);
    }

    private void actionLanguage(int index) {
        if (index != Config.pref.getInt(SettingKeyFactory.Language.INDEX, index)) {
            Config.pref.put(SettingKeyFactory.Language.ABREV, LanguagePanel.customCombo.petStrings[index]);
            Config.pref.putInt(SettingKeyFactory.Language.INDEX, index);
            Config.setDefaultLocale(index);
            JOptionPane.showMessageDialog(this, I18N.get("Text.Restart-OSFAC-DMT"
                    + ""), I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
            if (DMTWorkbench.frame instanceof DefaultDockableBarDockableHolder) {
                DMTWorkbench.frame.getLayoutPersistence().resetToDefault();
            }
        }
    }

    /**
     * Unlike #add(KeyListener), listeners registered using this method are
     * notified when KeyEvents occur on this frame's child components. Note:
     * Bug: KeyListeners registered using this method may receive events
     * multiple times.
     *
     * @see #addKeyboardShortcut
     */
    public void addEasyKeyListener(KeyListener l) {
        easyKeyListeners.add(l);
    }

    public void removeEasyKeyListener(KeyListener l) {
        easyKeyListeners.remove(l);
    }

    public String getMBCommittedMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double usedMemoryInMB = usedMemory / (1024 * 1024d);
        String memoryStr = memoryFormat.format(usedMemoryInMB);
        return memoryStr;
    }

    /**
     * @param newEnvelopeRenderingThreshold the number of on-screen features
     * above which envelope rendering should occur
     */
    public void setEnvelopeRenderingThreshold(int newEnvelopeRenderingThreshold) {
        envelopeRenderingThreshold = newEnvelopeRenderingThreshold;
    }

    public void setMaximumFeatureExtentForEnvelopeRenderingInPixels(
            int newMaximumFeatureExtentForEnvelopeRenderingInPixels) {
        maximumFeatureExtentForEnvelopeRenderingInPixels = newMaximumFeatureExtentForEnvelopeRenderingInPixels;
    }

    public void log(String message) {
        log.append(new Date()).append("  ").append(message).append(System.getProperty("line.separator"));
    }

    public String getLog() {
        return log.toString();
    }

    public void setMinimumFeatureExtentForAnyRenderingInPixels(
            int newMinimumFeatureExtentForAnyRenderingInPixels) {
        minimumFeatureExtentForAnyRenderingInPixels = newMinimumFeatureExtentForAnyRenderingInPixels;
    }

    public void displayLastStatusMessage() {
        setStatusMessage(lastStatusMessage);
    }

    public void setStatusMessage(String message) {
        lastStatusMessage = message;
        setStatusBarText(message);
        setStatusBarTextHighlighted(false, null);
    }

    private void setStatusBarText(String message) {
        // Make message at least a space so that the label won't collapse [Bob Boseko]
        message = (message == null || message.equals("")) ? " " : message;
        messageTextField.setText(message);
    }

    /**
     * To highlight a message, call #warnUser.
     */
    private void setStatusBarTextHighlighted(boolean highlighted, Color color) {
        // Use #coordinateLabel rather than (unattached) dummy label because
        // dummy label's background does not change when L&F changes. [Bob Boseko]
        messageTextField.setForeground(highlighted ? Color.black : coordinateLabel.getForeground());
        messageTextField.setBackground(highlighted ? color : coordinateLabel.getBackground());
    }

    public void setTimeMessage(String message) {
        // Make message at least a space so that the label won't collapse [Bob Boseko]
        message = (message == null || message.equals("")) ? " " : message;
        timeLabel.setText(message);
        timeLabel.setToolTipText(message);
    }

    //Add new JLabel for scale      //
    public void setScaleText(String message) {
        // Make message at least a space so that the label won't collapse
        message = (message == null || message.equals("")) ? " " : message;
        scaleLabel.setText(message);
        scaleLabel.setToolTipText(message);
    }

    public JInternalFrame getActiveInternalFrame() {
        return desktopPane.getSelectedFrame();
    }

    public JInternalFrame[] getInternalFrames() {
        return desktopPane.getAllFrames();
    }

    public TitledPopupMenu getCategoryPopupMenu() {
        return categoryPopupMenu;
    }

    public WorkbenchContext getContext() {
        return workbenchContext;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public int getEnvelopeRenderingThreshold() {
        return envelopeRenderingThreshold;
    }

    public TitledPopupMenu getLayerNamePopupMenu() {
        return layerNamePopupMenu;
    }

    public TitledPopupMenu getWMSLayerNamePopupMenu() {
        return wmsLayerNamePopupMenu;
    }

    public LayerViewPanelListener getLayerViewPanelListener() {
        return layerViewPanelListener;
    }

    public Map getNodeClassToPopupMenuMap() {
        return nodeClassToLayerNamePopupMenuMap;
    }

    public LayerNamePanelListener getLayerNamePanelListener() {
        return layerNamePanelListener;
    }

    public int getMaximumFeatureExtentForEnvelopeRenderingInPixels() {
        return maximumFeatureExtentForEnvelopeRenderingInPixels;
    }

    public int getMinimumFeatureExtentForAnyRenderingInPixels() {
        return minimumFeatureExtentForAnyRenderingInPixels;
    }

    public HTMLFrame getOutputFrame() {
        return outputFrame;
    }

    public WorkbenchToolBar getToolBar() {
        return toolBar;
    }

    public void activateFrame(JInternalFrame frame) {
        try {
            if (frame.isIcon()) {
                frame.setIcon(false);
            }
            frame.moveToFront();
            frame.requestFocus();
            frame.setSelected(true);
            if (!(frame instanceof TaskFrame)) {
                frame.setMaximum(false);
            }
        } catch (PropertyVetoException e) {
            warnUser(StringUtil.stackTrace(e));
        }
    }

    /**
     * If internalFrame is a LayerManagerProxy, the close behaviour will be
     * altered so that the user is prompted if it is the last window on the
     * LayerManager.
     */
    public void addInternalFrame(JInternalFrame internalFrame) {
        addInternalFrame(internalFrame, false, true);
    }

    public void addInternalFrame(final JInternalFrame internalFrame, boolean alwaysOnTop, boolean autoUpdateToolBar) {
        if (internalFrame instanceof LayerManagerProxy) {
            setClosingBehaviour((LayerManagerProxy) internalFrame);
            installTitleBarModifiedIndicator((LayerManagerProxy) internalFrame);
        }
        // <<TODO:IMPROVE>> Listen for when the frame closes, and when it does,
        // activate the topmost frame. Because Swing does not seem to do this
        // automatically. 
        DMTWorkbench.setIcon(internalFrame);
        // Call JInternalFrame#setVisible before JDesktopPane#add; otherwise, the
        // TreeLayerNamePanel starts too narrow (100 pixels or so) for some reason.
        // <<TODO>>Investigate. [Bob Boseko]
        internalFrame.setVisible(true);
        desktopPane.add(internalFrame, alwaysOnTop ? JLayeredPane.PALETTE_LAYER : JLayeredPane.DEFAULT_LAYER);
        if (autoUpdateToolBar) {
            internalFrame.addInternalFrameListener(new InternalFrameListener() {
                public void internalFrameActivated(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                    // Associate current cursortool with the new frame [Jon
                    // Aquino]
                    toolBar.reClickSelectedCursorToolButton();
                }

                public void internalFrameClosed(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                }

                public void internalFrameClosing(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                }

                public void internalFrameDeactivated(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                }

                public void internalFrameDeiconified(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                }

                public void internalFrameIconified(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                }

                public void internalFrameOpened(InternalFrameEvent e) {
                    toolBar.updateEnabledState();
                }
            });
            // Call #activateFrame *after* adding the listener.
            position(internalFrame);
            activateFrame(internalFrame);
        }
    }

    private void installTitleBarModifiedIndicator(final LayerManagerProxy internalFrame) {
        final JInternalFrame i = (JInternalFrame) internalFrame;
        new Block() {
            // Putting updatingTitle in a Block is better than making it an
            // instance variable, because this way there is one updatingTitle
            // for each
            // internal frame, rather than one for all internal frames. [Bob Boseko]
            private boolean updatingTitle = false;

            private void updateTitle() {
                if (updatingTitle) {
                    return;
                }
                updatingTitle = true;
                try {
                    String newTitle = i.getTitle();
                    if (newTitle.charAt(0) == '*') {
                        newTitle = newTitle.substring(1);
                    }
                    if (!internalFrame.getLayerManager().getLayersWithModifiedFeatureCollections().isEmpty()) {
                        newTitle = '*' + newTitle;
                    }
                    i.setTitle(newTitle);
                } finally {
                    updatingTitle = false;
                }
            }

            public Object yield() {
                internalFrame.getLayerManager().addLayerListener(new LayerListener() {
                    public void layerChanged(LayerEvent e) {
                        if ((e.getType() == LayerEventType.METADATA_CHANGED)
                                || (e.getType() == LayerEventType.REMOVED)) {
                            updateTitle();
                        }
                    }

                    public void categoryChanged(CategoryEvent e) {
                    }

                    public void featuresChanged(FeatureEvent e) {
                    }
                });
                i.addPropertyChangeListener(JInternalFrame.TITLE_PROPERTY,
                        new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        updateTitle();
                    }
                });
                return null;
            }
        }.yield();
    }

    private void setClosingBehaviour(final LayerManagerProxy proxy) {
        final JInternalFrame internalFrame = (JInternalFrame) proxy;
        internalFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                internalFrameCloseHandler.close(internalFrame);
            }
        });
    }

    // added by [mmichaud 2007-06-03]
    // Return TaskFrame s using the same layerManager
    private Collection getTaskFramesAssociatedWith(LayerManager layerManager) {
        ArrayList taskFramesAssociatedWithLayerManager = new ArrayList();
        JInternalFrame[] internalFrames = getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i] instanceof TaskFrame
                    && (((TaskFrame) internalFrames[i]).getLayerManager() == layerManager)) {
                taskFramesAssociatedWithLayerManager.add(internalFrames[i]);
            }
        }
        return taskFramesAssociatedWithLayerManager;
    }

    // added by 
    // Return every InternalFrame associated with taskFrame (taskFrame is
    // excluded)
    private Collection getInternalFramesAssociatedWith(TaskFrame taskFrame) {
        ArrayList internalFramesAssociatedWithTaskFrame = new ArrayList();
        JInternalFrame[] internalFrames = getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i] instanceof TaskFrameProxy
                    && (((TaskFrameProxy) internalFrames[i]).getTaskFrame() == taskFrame)
                    && internalFrames[i] != taskFrame) {
                internalFramesAssociatedWithTaskFrame.add(internalFrames[i]);
            }
        }
        return internalFramesAssociatedWithTaskFrame;
    }

    public TaskFrame addTaskFrame() {
        TaskFrame f = addTaskFrame(createTask());
        return f;
    }

    public Task createTask() {
        Task task = new Task();
        // LayerManager shouldn't automatically add categories in its
        // constructor.
        // Sometimes we want to create a LayerManager with no categories
        task.getLayerManager().addCategory(StandardCategoryNames.WORKING);
        task.getLayerManager().addCategory(StandardCategoryNames.SYSTEM);
        task.setName(I18N.get("ui.WorkbenchFrame.task") + " " + taskSequence++);
        return task;
    }

    public TaskFrame addTaskFrame(Task task) {
        TaskFrame taskFrame;
        if (taskFrameFactory != null) {
            taskFrame = taskFrameFactory.createComponent();
            taskFrame.setTask(task);
        } else {
            taskFrame = new TaskFrame(task, workbenchContext);
        }
        return addTaskFrame(taskFrame);
    }

    public TaskFrame addTaskFrame(TaskFrame taskFrame) {
        // track which taskframe is activated
        taskFrame.addInternalFrameListener(new WorkbenchFrame.ActivateTaskFrame());
        taskFrame.getTask().getLayerManager().addLayerListener(new LayerListener() {
            public void featuresChanged(FeatureEvent e) {
            }

            public void categoryChanged(CategoryEvent e) {
                toolBar.updateEnabledState();
            }

            public void layerChanged(LayerEvent layerEvent) {
                toolBar.updateEnabledState();
            }
        });
        addInternalFrame(taskFrame);
        taskFrame.getLayerViewPanel().getLayerManager().getUndoableEditReceiver().add(new UndoableEditReceiver.Listener() {
            public void undoHistoryChanged() {
                toolBar.updateEnabledState();
            }

            public void undoHistoryTruncated() {
                toolBar.updateEnabledState();
                log(I18N.get("ui.WorkbenchFrame.undo-history-was-truncated"));
            }
        });
        // fire TaskListener's
        Object[] listeners = getTaskListeners().toArray();
        for (int i = 0; i < listeners.length; i++) {
            TaskListener l = (TaskListener) listeners[i];
            l.taskAdded(new TaskEvent(this, taskFrame.getTask()));
        }
        return taskFrame;
    }

    private class ActivateTaskFrame extends InternalFrameAdapter {

        public void internalFrameActivated(InternalFrameEvent e) {
            activeTaskFrame = (TaskFrame) e.getInternalFrame();
        }
    }

    public TaskFrame getActiveTaskFrame() {
        return activeTaskFrame;
    }

    public void flash(final HTMLFrame frame) {
        final Color originalColor = frame.getBackgroundColor();
        new Timer(100, new ActionListener() {
            private int tickCount = 0;

            public void actionPerformed(ActionEvent e) {
                try {
                    tickCount++;
                    frame.setBackgroundColor(((tickCount % 2) == 0) ? originalColor : Color.yellow);
                    if (tickCount == 2) {
                        Timer timer = (Timer) e.getSource();
                        timer.stop();
                    }
                } catch (Throwable t) {
                    handleThrowable(t);
                }
            }
        }).start();
    }

    private void flashStatusMessage(final String message, final Color color) {
        new Timer(100, new ActionListener() {
            private int tickCount = 0;

            public void actionPerformed(ActionEvent e) {
                tickCount++;
                // This message is important, so overwrite whatever is on the
                // status bar. 
                setStatusBarText(message);
                setStatusBarTextHighlighted((tickCount % 2) == 0, color);
                if (tickCount == 4) {
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        }).start();
    }

    /**
     * Can be called regardless of whether the current thread is the AWT event
     * dispatch thread.
     *
     * @param t Description of the Parameter
     */
    public void handleThrowable(final Throwable t) {
        log(StringUtil.stackTrace(t));
        Component parent = this;
        Window[] ownedWindows = getOwnedWindows();
        for (int i = 0; i < ownedWindows.length; i++) {
            if (ownedWindows[i] instanceof Dialog && ownedWindows[i].isVisible()
                    && ((Dialog) ownedWindows[i]).isModal()) {
                parent = ownedWindows[i];
                break;
            }
        }
        handleThrowable(t, parent);
    }

    public void handleThrowable(final Throwable t, final Component parent) {
        showThrowable(t, parent);
    }

    public static void showThrowable(final Throwable t, final Component parent) {
        t.printStackTrace(System.err);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ErrorDialog.show(parent, StringUtil.toFriendlyName(t.getClass()
                        .getName()), toMessage(t), StringUtil.stackTrace(t));
            }
        });
    }

    public static String toMessage(Throwable t) {
        String message;
        if (t.getLocalizedMessage() == null) {
            message = I18N.get("ui.WorkbenchFrame.no-description-was-provided");
        } else if (t.getLocalizedMessage().toLowerCase().indexOf(
                I18N.get("ui.WorkbenchFrame.side-location-conflict")) > -1) {
            message = t.getLocalizedMessage() + " -- "
                    + I18N.get("ui.WorkbenchFrame.check-for-invalid-geometries");
        } else {
            message = t.getLocalizedMessage();
        }
        return message + "\n\n (" + StringUtil.toFriendlyName(t.getClass().getName())
                + ")";
    }

    public boolean hasInternalFrame(JInternalFrame internalFrame) {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == internalFrame) {
                return true;
            }
        }
        return false;
    }

    public void removeInternalFrame(JInternalFrame internalFrame) {
        // Looks like #closeFrame is the proper way to remove an internal frame.
        // It will activate the next frame. [Bob Boseko]
        desktopPane.getDesktopManager().closeFrame(internalFrame);
    }

    public void warnUser(String warning) {
        log(I18N.get("ui.WorkbenchFrame.warning") + ": " + warning);
        flashStatusMessage(warning, Color.yellow);
    }

    public void zoomChanged(Envelope modelEnvelope) {
        toolBar.updateEnabledState();
    }

    void exitMenuItem_actionPerformed(ActionEvent e) {
        closeApplication();
    }

    void this_componentShown(ComponentEvent e) {
        try {
            // If the first internal frame is not a TaskWindow (as may be the case in
            // custom workbenches), #updateEnabledState() will ensure that the
            // cursor-tool buttons are disabled. [Bob Boseko]
            toolBar.updateEnabledState();
        } catch (Throwable t) {
            handleThrowable(t);
        }
    }

    void this_windowClosing(WindowEvent e) {
        closeApplication();
    }

    void windowMenu_menuSelected(MenuEvent e) {
        // If this is the first call get the number of added menu items.
        // After this point no new menus can be added
        if (addedMenuItems == -1) {
            addedMenuItems = windowMenu.getItemCount();
        }
        while (windowMenu.getItemCount() > addedMenuItems) {
            windowMenu.remove(windowMenu.getItemCount() - 1);
        }
        final JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            JMenuItem menuItem = new JMenuItem();
            // Increase truncation threshold from 20 to 40, for eziLink [Bob Boseko]
            menuItem.setText(GUIUtil.truncateString(frames[i].getTitle(), 40));
            associate(menuItem, frames[i]);
            windowMenu.add(menuItem);
        }
        if (windowMenu.getItemCount() == addedMenuItems) {
            // For ezLink [Bob Boseko]
            windowMenu.add(new JMenuItem("("
                    + I18N.get("ui.WorkbenchFrame.no-windows") + ")"));
        }
    }

    private void associate(JMenuItem menuItem, final JInternalFrame frame) {
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    activateFrame(frame);
                } catch (Throwable t) {
                    handleThrowable(t);
                }
            }
        });
    }

    private void closeApplication() {
        applicationExitHandler.exitApplication(this);
    }

    private Collection getLayersWithModifiedFeatureCollections() {
        ArrayList layersWithModifiedFeatureCollections = new ArrayList();
        for (Iterator i = getLayerManagers().iterator(); i.hasNext();) {
            LayerManager layerManager = (LayerManager) i.next();
            layersWithModifiedFeatureCollections.addAll(layerManager.getLayersWithModifiedFeatureCollections());
        }
        return layersWithModifiedFeatureCollections;
    }

    private Collection getGeneratedLayers() {
        ArrayList list = new ArrayList();
        for (Iterator i = getLayerManagers().iterator(); i.hasNext();) {
            LayerManager layerManager = (LayerManager) i.next();
            list.addAll(layerManager.getLayersWithNullDataSource());
        }
        return list;
    }

    private Collection getLayerManagers() {
        // Multiple windows may point to the same LayerManager, so use
        // a Set. [Bob Boseko]
        HashSet layerManagers = new HashSet();
        JInternalFrame[] internalFrames = getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i] instanceof LayerManagerProxy) {
                layerManagers.add(((LayerManagerProxy) internalFrames[i]).getLayerManager());
            }
        }
        return layerManagers;
    }

    private void configureStatusLabel(JComponent component, int width) {
        component.setMinimumSize(new Dimension(width, (int) component.getMinimumSize()
                .getHeight()));
        component.setMaximumSize(new Dimension(width, (int) component.getMaximumSize()
                .getHeight()));
        component.setPreferredSize(new Dimension(width, (int) component.getPreferredSize()
                .getHeight()));
    }

    private void position(JInternalFrame internalFrame) {
        final int STEP = 5;
        GUIUtil.Location location;
        if (internalFrame instanceof PrimaryInfoFrame) {
            primaryInfoFrameIndex++;
            int offset = (primaryInfoFrameIndex % 3) * STEP;
            location = new GUIUtil.Location(offset, true, offset, true);
        } else {
            positionIndex++;
            int offset = (positionIndex % 5) * STEP;
            location = new GUIUtil.Location(offset, false, offset, false);
        }
        GUIUtil.setLocation(internalFrame, location, desktopPane);
    }

    /**
     * Fundamental Style classes (like BasicStyle, VertexStyle, and LabelStyle)
     * cannot be removed, and are thus excluded from the choosable Style
     * classes.
     */
    public Set getChoosableStyleClasses() {
        return Collections.unmodifiableSet(choosableStyleClasses);
    }

    public void addChoosableStyleClass(Class choosableStyleClass) {
        Assert.isTrue(ChoosableStyle.class.isAssignableFrom(choosableStyleClass));
        choosableStyleClasses.add(choosableStyleClass);
    }

    /**
     * Adds a keyboard shortcut for a plugin. logs plugin exceptions. note -
     * attaching to keyCode 'a', modifiers =1 will detect shift-A events. It
     * will *not* detect caps-lock-'a'. This is due to inconsistencies in
     * java.awt.event.KeyEvent. In the unlikely event you actually do want to
     * also also attach to caps-lock-'a', then make two shortcuts - one to
     * keyCode 'a' and modifiers =1 (shift-A) and one to keyCode 'A' and
     * modifiers=0 (caps-lock A). For more details, see the
     * java.awt.event.KeyEvent class - it has a full explaination.
     *
     * @param keyCode What key to attach to (See java.awt.event.KeyEvent)
     * @param modifiers 0= none, 1=shift, 2= cntrl, 8=alt, 3=shift+cntrl, etc...
     * See the modifier mask constants in the Event class
     * @param plugIn What plugin to execute
     * @param enableCheck Is the key enabled at the moment?
     */
    public void addKeyboardShortcut(final int keyCode, final int modifiers,
            final PlugIn plugIn, final EnableCheck enableCheck) {
        // Overwrite existing shortcut [Bob Boseko]
        keyCodeAndModifiersToPlugInAndEnableCheckMap.put(keyCode + ":" + modifiers,
                new Object[]{
            plugIn, enableCheck
        });
    }

    private void installKeyboardShortcutListener() {
        addEasyKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                Object[] plugInAndEnableCheck = (Object[]) keyCodeAndModifiersToPlugInAndEnableCheckMap.get(e.getKeyCode()
                        + ":" + e.getModifiers());
                if (plugInAndEnableCheck == null) {
                    return;
                }
                PlugIn plugIn = (PlugIn) plugInAndEnableCheck[0];
                EnableCheck enableCheck = (EnableCheck) plugInAndEnableCheck[1];
                if (enableCheck != null && enableCheck.check(null) != null) {
                    return;
                }
                // #toActionListener handles checking if the plugIn is a
                // ThreadedPlugIn,
                // and making calls to UndoableEditReceiver if necessary. [Jon
                // Aquino 10/15/2003]
                AbstractPlugIn.toActionListener(plugIn, workbenchContext,
                        new TaskMonitorManager()).actionPerformed(null);
            }
        });
    }
    // ==========================================================================
    // Applications (such as EziLink) want to override the default JUMP
    // frame closing behaviour and application exit behaviour with their own
    // behaviours.

    public InternalFrameCloseHandler getInternalFrameCloseHandler() {
        return internalFrameCloseHandler;
    }

    public void setInternalFrameCloseHandler(InternalFrameCloseHandler value) {
        internalFrameCloseHandler = value;
    }

    public ApplicationExitHandler getApplicationExitHandler() {
        return applicationExitHandler;
    }

    public void setApplicationExitHandler(ApplicationExitHandler value) {
        applicationExitHandler = value;
    }

    private class DefaultInternalFrameCloser implements InternalFrameCloseHandler {

        public void close(JInternalFrame internalFrame) {
            if (internalFrame instanceof TaskFrame) {
                // delete reference to taskframe to be closed
                if (activeTaskFrame == internalFrame) {
                    activeTaskFrame = null;
                }
                closeTaskFrame((TaskFrame) internalFrame);
            } else {
                GUIUtil.dispose(internalFrame, desktopPane);
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.runFinalization();
                    System.gc();
                }
            });
        }
    }

    private class DefaultApplicationExitHandler implements ApplicationExitHandler {

        public void exitApplication(JFrame mainFrame) {
            if (confirmClose(I18N.get("ui.WorkbenchFrame.exit-jump"),
                    getLayersWithModifiedFeatureCollections(),
                    getGeneratedLayers(), WorkbenchFrame.this)) {
                // PersistentBlackboardPlugIn listens for when the workbench is
                // hidden [Bob Boseko]
//                saveWindowState();
                setVisible(false);
                // Invoke System#exit after all pending GUI events have been fired
                // (e.g. the hiding of this WorkbenchFrame) [Bob Boseko]
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        clearUp();
                        System.exit(0);
                    }
                });
            }
        }
    }

    // Method completed by [mmichaud 2007-06-03] to close properly
    // internal frames depending on a TaskFrame.
    // Maybe this method should take place in TaskFrame instead...
    private void closeTaskFrame(TaskFrame taskFrame) {
        LayerManager layerManager = taskFrame.getLayerManager();
        Collection associatedFrames = getInternalFramesAssociatedWith(taskFrame);
        boolean lastTaskFrame = getTaskFramesAssociatedWith(layerManager).size() == 1;
        if (lastTaskFrame) {
            Collection modifiedItems = layerManager.getLayersWithModifiedFeatureCollections();
            Collection generatedItems = layerManager.getLayersWithNullDataSource();
            if (confirmClose(I18N.get("ui.WorkbenchFrame.close-task"), modifiedItems, generatedItems, taskFrame)) {
                // There are other internal frames associated with this task
                if (!associatedFrames.isEmpty()) {
                    // Confirm you want to close them first
                    if (confirmClose(
                            StringUtil.split(
                            I18N.get("ui.WorkbenchFrame.other-internal-frames-depend-on-this-task-frame")
                            + " "
                            + I18N.get("ui.WorkbenchFrame.do-you-want-to-close-them-also"),
                            60), I18N.get("ui.WorkbenchFrame.close-all"))) {
                        for (java.util.Iterator it = associatedFrames.iterator(); it.hasNext();) {
                            GUIUtil.dispose((JInternalFrame) it.next(), desktopPane);
                        }

                    } else {
                        return; // finally, I don't want to close
                    }
                }
                layerManager.dispose();
                taskFrame.getLayerViewPanel().dispose();
                taskFrame.getLayerNamePanel().dispose();
                GUIUtil.dispose(taskFrame, desktopPane);
            } else {
            }
        } else {
            // There are other internal frames associated with this task
            if (!associatedFrames.isEmpty()) {
                // Confirm you want to close them first
                if (confirmClose(
                        StringUtil.split(
                        I18N.get("ui.WorkbenchFrame.other-internal-frames-depend-on-this-task-frame")
                        + " "
                        + I18N.get("ui.WorkbenchFrame.do-you-want-to-close-them-also"),
                        60), I18N.get("ui.WorkbenchFrame.close-all"))) {
                    for (java.util.Iterator it = associatedFrames.iterator(); it.hasNext();) {
                        GUIUtil.dispose((JInternalFrame) it.next(), desktopPane);
                    }
                } else {
                    return; // finally, I don't want to close
                }
            }
            taskFrame.getLayerViewPanel().dispose();
            taskFrame.getLayerNamePanel().dispose();
            GUIUtil.dispose(taskFrame, desktopPane);
        }
    }

    /**
     * This method is used to confirm the close of a TaskFrame or the close of
     * the application. In both cases, we need to check there is no unsaved
     * layers.
     */
    private boolean confirmClose(String action,
            Collection modifiedLayers,
            Collection generatedLayers,
            Container container) {
        if (modifiedLayers.isEmpty()) {
            if (generatedLayers.isEmpty()) {
                return true;
            }
            JOptionPane pane = new JOptionPane();
            String message = null;
            if (container instanceof WorkbenchFrame) {
                message = I18N.getMessage("ui.WorkbenchFrame.do-you-really-want-to-close-openjump",
                        new Object[]{Integer.valueOf(generatedLayers.size())});
            } else if (container instanceof TaskFrame) {
                message = I18N.getMessage("ui.WorkbenchFrame.do-you-really-want-to-close-the-project",
                        new Object[]{Integer.valueOf(generatedLayers.size())});
            }
            pane.setMessage(message);
            pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
            pane.setOptions(new String[]{
                action, I18N.get("ui.WorkbenchFrame.cancel")
            });
            pane.createDialog(this, "OSFAC-DMT").setVisible(true);
            return pane.getValue().equals(action);
        }
        JOptionPane pane = new JOptionPane(
                StringUtil.split(modifiedLayers.size() + " "
                + I18N.get("ui.WorkbenchFrame.dataset")
                + StringUtil.s(modifiedLayers.size())
                + " "
                + ((modifiedLayers.size() > 1) ? I18N.get("ui.WorkbenchFrame.have-been-modified")
                : I18N.get("ui.WorkbenchFrame.has-been-modified"))
                + " ("
                + ((modifiedLayers.size() > 3) ? "e.g. " : "")
                + StringUtil.toCommaDelimitedString(new ArrayList(modifiedLayers).subList(
                0, Math.min(3, modifiedLayers.size()))) + ").\n"
                + I18N.get("ui.WorkbenchFrame.continue"), 80),
                JOptionPane.WARNING_MESSAGE);
        pane.setOptions(new String[]{action, I18N.get("ui.WorkbenchFrame.cancel")});
        pane.createDialog(this, "OSFAC-DMT").setVisible(true);
        return pane.getValue().equals(action);
    }

    private boolean confirmClose(String question, String action) {
        javax.swing.JOptionPane pane = new javax.swing.JOptionPane(question,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        pane.setOptions(new String[]{
            action, com.osfac.dmt.I18N.get("ui.WorkbenchFrame.cancel")
        });
        pane.createDialog(this, "OSFAC-DMT").setVisible(true);
        return pane.getValue().equals(action);
    }

    public Point initWindowLocation() {
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        return new Point((rect.width - getWidth()) / 2 + rect.x, (rect.height - getHeight()) / 2 + rect.y);
    }

    public Dimension initWindowSize() {
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        if (rect.width > 740) {
            rect.width = 740;
        }
        if (rect.height > 480) {
            rect.height = 480;
        }
        return rect.getSize();
    }

    /**
     * @return the taskListeners
     */
    public ArrayList<TaskListener> getTaskListeners() {
        return taskListeners;
    }

    /**
     * Add's a TaskListener, wich will be fired if a Task was added via the
     * WorkbenchFrame.addTaskFrame(TaskFrame taskFrame) or the a Task was loaded
     * completly with all his layers.
     *
     * @param l - The TaskListener to add.
     */
    public void addTaskListener(TaskListener l) {
        getTaskListeners().add(l);
    }

    /**
     * Remove's a TaskListener.
     *
     * @param l - The TaskListener to add.
     */
    public void removeTaskListener(TaskListener l) {
        getTaskListeners().remove(l);
    }
    DMTCommandBarFactory cbf = new DMTCommandBarFactory(this);
    private StatusBar _statusBar;
    private boolean _autohideAll = false;
    private byte[] _fullScreenLayout;
    private static JDesktopPane desktopPane = new JDesktopPane() {
        {
            setDesktopManager(new DefaultDesktopManager());
        }
    };
    public static DocumentPane _documentPane;
    private TimeStatusBarItem timeStatusBar;
    public static ProgressStatusBarItem progress;
    private static final String PROFILE_NAME = "DMTWorkBench_2_0_1_Desktop";
    WorkbenchToolBar toolBar;
    public static TaskFrame activeTaskFrame = null;
    // StatusBar
    private JPanel statusPanel = new JPanel();
    private JTextField messageTextField = new JTextField();
    private JLabel timeLabel = new JLabel();
    private JLabel memoryLabel = new JLabel();
    private JLabel scaleLabel = new JLabel();
    private JLabel coordinateLabel = new JLabel();
    private String lastStatusMessage = "";
    // the four SplitPanes for the statusbar
    private JSplitPane statusPanelSplitPane1;
    private JSplitPane statusPanelSplitPane2;
    private JSplitPane statusPanelSplitPane3;
    private JSplitPane statusPanelSplitPane4;
    private DecimalFormat memoryFormat = new DecimalFormat("###,###");
    private TitledPopupMenu categoryPopupMenu = new TitledPopupMenu() {
        {
            addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    LayerNamePanel panel = ((LayerNamePanelProxy) getActiveInternalFrame()).getLayerNamePanel();
                    setTitle((panel.selectedNodes(Category.class).size() != 1) ? ("("
                            + panel.selectedNodes(Category.class).size() + " categories selected)")
                            : ((Category) panel.selectedNodes(Category.class).iterator().next()).getName());
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
        }
    };
    // <<TODO:REMOVE>> Actually we're not using the three optimization parameters below. Remove. 
    private int envelopeRenderingThreshold = 500;
    private HTMLFrame outputFrame = new HTMLFrame(this) {
        public void setTitle(String title) {
            // Don't allow the title of the output frame to be changed.
        }

        {
            super.setTitle(I18N.get("ui.WorkbenchFrame.output"));
        }
    };
    private TitledPopupMenu layerNamePopupMenu = new TitledPopupMenu() {
        {
            addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    LayerNamePanel panel = ((LayerNamePanelProxy) getActiveInternalFrame()).getLayerNamePanel();
                    setTitle((panel.selectedNodes(Layer.class).size() != 1) ? ("("
                            + panel.selectedNodes(Layer.class).size() + " "
                            + I18N.get("ui.WorkbenchFrame.layers-selected") + ")")
                            : ((Layerable) panel.selectedNodes(Layer.class).iterator().next()).getName());
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
        }
    };
    private TitledPopupMenu wmsLayerNamePopupMenu = new TitledPopupMenu() {
        {
            addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    LayerNamePanel panel = ((LayerNamePanelProxy) getActiveInternalFrame()).getLayerNamePanel();
                    setTitle((panel.selectedNodes(WMSLayer.class).size() != 1) ? ("("
                            + panel.selectedNodes(WMSLayer.class).size() + " "
                            + I18N.get("ui.WorkbenchFrame.wms-layers-selected") + ")")
                            : ((Layerable) panel.selectedNodes(WMSLayer.class).iterator().next()).getName());
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
        }
    };
    private LayerNamePanelListener layerNamePanelListener = new LayerNamePanelListener() {
        public void layerSelectionChanged() {
            toolBar.updateEnabledState();
        }
    };
    // Here is a small patch to JUMP to avoid creating a StringBuffer every
    // coordinate change (which could be many thousands). Replace the innter
    // class in WorkbenchFrame.java with the following. I am assuming only one
    // thread can call the listener at a time. If that is untrue please
    // synchronize cursorPositionChanged().
    private LayerViewPanelListener layerViewPanelListener = new LayerViewPanelListener() {
        // Avoid creating an expensive StringBuffer when the cursor position
        // changes.
        private StringBuffer positionStatusBuf = new StringBuffer("(");

        public void cursorPositionChanged(String x, String y) {
            positionStatusBuf.setLength(1);
            positionStatusBuf.append(x).append(" ; ").append(y).append(")");
            coordinateLabel.setText(positionStatusBuf.toString());
        }

        public void selectionChanged() {
            toolBar.updateEnabledState();
        }

        public void fenceChanged() {
            toolBar.updateEnabledState();
        }

        public void painted(Graphics graphics) {
        }
    };
    // <<TODO:NAMING>> This name is not clear 
    private int maximumFeatureExtentForEnvelopeRenderingInPixels = 10;
    // <<TODO:NAMING>> This name is not clear 
    private int minimumFeatureExtentForAnyRenderingInPixels = 2;
    private StringBuffer log = new StringBuffer();
    private int taskSequence = 1;
    private byte IndexMenu = 5;
    public static WorkbenchContext workbenchContext;
    private HashMap keyCodeAndModifiersToPlugInAndEnableCheckMap = new HashMap();
    InternalFrameCloseHandler internalFrameCloseHandler = new DefaultInternalFrameCloser();
    ApplicationExitHandler applicationExitHandler = new DefaultApplicationExitHandler();
    private Set choosableStyleClasses = new HashSet();
    private ArrayList easyKeyListeners = new ArrayList();
    private ArrayList<TaskListener> taskListeners = new ArrayList<>();
    private Map nodeClassToLayerNamePopupMenuMap = CollectionUtil.createMap(new Object[]{
        Layer.class, layerNamePopupMenu, WMSLayer.class, wmsLayerNamePopupMenu,
        Category.class, categoryPopupMenu
    });
    private int positionIndex = -1;
    private int primaryInfoFrameIndex = -1;
    private int addedMenuItems = -1;
    public static Timer timer;
    private JideButton updater = new JideButton();
    private String url_update, new_version;
    private ComponentFactory<TaskFrame> taskFrameFactory;
    CommandMenuBar menuBar = new CommandMenuBar("Menu Bar");
    JideMenu fileMenu = (JideMenu) FeatureInstaller.installMnemonic(new JideMenu(MenuNames.FILE), menuBar);
    JMenuItem exitMenuItem = FeatureInstaller.installMnemonic(new JMenuItem(I18N.get("ui.WorkbenchFrame.exit")), fileMenu);
    JideMenu windowMenu = (JideMenu) FeatureInstaller.installMnemonic(new JideMenu(MenuNames.WINDOW), menuBar);
    private BalloonTip _balloonTip;
    public static Layer previewLayer = null;
    public static ArrayList<JCheckBoxMenuItem> ChBCategoryList = new ArrayList<>();
    private boolean checkAll = false;
    private JMenuItem MIClear = new JMenuItem();
    public static JideButton DataRequestFound;
    public static JideButton BSConnect;
    public static String TypeOfVersion = Config.FULL_VERSION;
    public static int idUser;
    public static DataRequestSync dataRequestSync;
}
