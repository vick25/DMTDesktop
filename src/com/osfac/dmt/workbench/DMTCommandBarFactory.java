package com.osfac.dmt.workbench;

import com.jidesoft.action.*;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolder;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.PersistenceUtils;
import com.jidesoft.utils.Lm;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.ui.ShortCut;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.JPopupMenu.Separator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.w3c.dom.Document;

public class DMTCommandBarFactory extends CommandBarFactory {

    public DMTCommandBarFactory(WorkbenchFrame parent) {
        DMTCommandBarFactory.parent = parent;
    }

    public static void actionTheme(String theme) {
        Config.pref.put(SettingKeyFactory.Theme.LOOKANDFEEL, theme);
        ((Office2003Painter) Office2003Painter.getInstance()).setColorName(theme);
        parent.getDockableBarManager().updateComponentTreeUI();
        parent.getDockingManager().updateComponentTreeUI();
        SwingUtilities.updateComponentTreeUI(parent);
    }

    public JideMenu createThemeMenu() {
        JideMenu ThemeMenu = new JideMenu(I18N.get("ThemePanel.jLabel1.text"));
        JMenuItem defaul = new JMenuItem(I18N.get("Text.Default"));
        JMenuItem gray = new JMenuItem("Gray");
        JMenuItem HomeStead = new JMenuItem("Green");
        JMenuItem Metallic = new JMenuItem("Metallic");
        JMenuItem NormalColor = new JMenuItem("Blue");
        defaul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionTheme("Gray");
            }
        });
        gray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionTheme("Gray");
            }
        });
        HomeStead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionTheme("HomeStead");
            }
        });
        Metallic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionTheme("Metallic");
            }
        });
        NormalColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionTheme("NormalColor");
            }
        });
        ThemeMenu.add(gray);
        ThemeMenu.add(HomeStead);
        ThemeMenu.add(Metallic);
        ThemeMenu.add(NormalColor);
        ThemeMenu.add(new Separator());
        ThemeMenu.add(defaul);
        return ThemeMenu;
    }

    public void createToolsMenu(JMenu menu) {
        JMenuItem MIGeoSearch = new JMenuItem(I18N.get("Text.Geographic-Search"));
        JMenuItem MIQuerySearch = new JMenuItem(I18N.get("Text.Query-Search"));
        JMenuItem MIUnzip = new JMenuItem(I18N.get("Text.Unzip-Files"));
        MIGeoSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionGeoSearch();
            }
        });
        MIQuerySearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionQuerySearch();
            }
        });
        MIUnzip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionUnzipFiles();
            }
        });
        MIGeoSearch.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.ICON));
        MIUnzip.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.UNZIP));
        MIQuerySearch.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.QUERYSEARCH));

        JMenuItem MIUpdateDatabase = new JMenuItem(I18N.get("Text.Update-Database"));
        JMenuItem MIManageRequest = new JMenuItem(I18N.get("Text.Data-Request"));
        JMenuItem MIStatistic = new JMenuItem(I18N.get("Text.Statistic"));
        JMenuItem MICreateUser = new JMenuItem(I18N.get("Text.User-Manager"));
        JMenuItem MIStack = new JMenuItem(I18N.get("Text.Composite-Band"));
        MIUpdateDatabase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionUpdateDatabase();
            }
        });
        MIManageRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionDataRequest();
            }
        });
        MIStatistic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionStatistic();
            }
        });
        MICreateUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionUserManagement();
            }
        });
        MIStack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionCompositeBand();
            }
        });
        MIUpdateDatabase.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.DATABASE));
        MIManageRequest.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.REQUEST));
        MIStatistic.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.STATISTIC));
        MICreateUser.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.USER));
        MIStack.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.STACK));
        MIStack.setEnabled(false);
        if (Config.isLiteVersion() || Config.isSimpleUser()) {
            menu.add(MIGeoSearch, 0);
            menu.add(MIQuerySearch, 1);
            menu.add(new JPopupMenu.Separator(), 2);
            menu.add(MIUnzip, 3);
            menu.add(MIStack, 4);
            menu.add(new JPopupMenu.Separator(), 5);
        } else {
            menu.add(MIGeoSearch, 0);
            menu.add(MIQuerySearch, 1);
            menu.add(new JPopupMenu.Separator(), 2);
            menu.add(MIUpdateDatabase, 3);
            menu.add(MIStatistic, 4);
            menu.add(MIManageRequest, 5);
            menu.add(new JPopupMenu.Separator(), 6);
            menu.add(MICreateUser, 7);
            menu.add(new JPopupMenu.Separator(), 8);
            menu.add(MIUnzip, 9);
            menu.add(MIStack, 10);
            menu.add(new JPopupMenu.Separator(), 11);
        }
    }

    public void createWindowsMenu(JMenu menu) {
        menu.setMnemonic('W');
        JMenuItem item;
        item = new JMenuItem(I18N.get("WindowMenu.Load-Default-Layout"));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder) {
                    parent.getLayoutPersistence().loadLayoutData();
                }
            }
        });
        menu.add(item, 0);
        item = new JMenuItem(I18N.get("WindowMenu.Load-Design-Layout"));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder) {
                    parent.getLayoutPersistence().loadLayoutDataFrom("design");
                }
            }
        });
        menu.add(item, 1);
        menu.add(new JPopupMenu.Separator(), 2);
        item = new JMenuItem(I18N.get("WindowMenu.Save-as-Default-Layout"));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder) {
                    parent.getLayoutPersistence().saveLayoutData();
                }
            }
        });
        menu.add(item, 3);
        item = new JMenuItem(I18N.get("WindowMenu.Save-as-Design-Layout"));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder) {
                    parent.getLayoutPersistence().saveLayoutDataAs("design");
                }
            }
        });
        menu.add(item, 4);
        menu.add(new JPopupMenu.Separator(), 5);
        item = new JMenuItem(I18N.get("WindowMenu.Reset-Layout"));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder) {
                    parent.getLayoutPersistence().resetToDefault();
                }
            }
        });
        menu.add(item, 6);
        menu.add(new JPopupMenu.Separator(), 7);
        item = new JMenuItem(I18N.get("WindowMenu.Save-XML-Layout-to"));
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 4119879332837884473L;

            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder && parent.getLayoutPersistence() != null) {
                    try {
                        JFileChooser chooser = new JFileChooser() {
                            @Override
                            protected JDialog createDialog(Component parent) throws HeadlessException {
                                JDialog dialog = super.createDialog(parent);
                                dialog.setTitle(I18N.get("WindowMenu.Save-the-layout-as-an-xml-file"));
                                return dialog;
                            }
                        };
                        chooser.setCurrentDirectory(new File(_lastDirectory));
                        int result = chooser.showDialog(((JMenuItem) e.getSource()).getTopLevelAncestor(), I18N.get("Text.Save"));
                        if (result == JFileChooser.APPROVE_OPTION) {
                            _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document document = builder.newDocument();
                            parent.getLayoutPersistence().saveLayoutTo(document);
                            PersistenceUtils.saveXMLDocumentToFile(document, chooser.getSelectedFile().getAbsolutePath(), PersistenceUtils.getDefaultXmlEncoding());
                        }
                    } catch (IOException | HeadlessException | ParserConfigurationException ex) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
                    }
                }
            }
        });
        menu.add(item, 8);
        item = new JMenuItem(I18N.get("WindowMenu.Load-XML-Layout-from"));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof DefaultDockableBarDockableHolder) {
                    JFileChooser chooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(Component parent) throws HeadlessException {
                            JDialog dialog = super.createDialog(parent);
                            dialog.setTitle(I18N.get("WindowMenu.Load-an-xml-file"));
                            return dialog;
                        }
                    };
                    chooser.setCurrentDirectory(new File(_lastDirectory));
                    int result = chooser.showDialog(((JMenuItem) e.getSource()).getTopLevelAncestor(), I18N.get("Text.Open"));
                    if (result == JFileChooser.APPROVE_OPTION) {
                        _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                        parent.getLayoutPersistence().loadLayoutDataFromFile(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            }
        });
        menu.add(item, 9);
        menu.add(new JPopupMenu.Separator(), 10);
        item = new JMenuItem(I18N.get("WindowMenu.Toggle-Auto-Hide-All"));
        item.setMnemonic('T');
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!_autohideAll) {
                    _fullScreenLayout = parent.getDockingManager().getLayoutRawData();
                    parent.getDockingManager().autohideAll();
                    _autohideAll = true;
                } else {
                    if (_fullScreenLayout != null) {
                        parent.getDockingManager().setLayoutRawData(_fullScreenLayout);
                    }
                    _autohideAll = false;
                }
            }
        });
        menu.add(item, 11);
        menu.add(new JPopupMenu.Separator(), 12);
    }

    public JideMenu createViewMenu() {
        JideMenu menu = new JideMenu(I18N.get("Text.Frame"));
        menu.setMnemonic('P');
        JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Frames-Floatable"));
        checkBoxMenuItem.setMnemonic('F');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setFloatable(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    WorkbenchFrame._documentPane.setFloatingAllowed(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isFloatable());
        menu.add(checkBoxMenuItem);
        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Frames-Autohidable"));
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setAutohidable(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isAutohidable());
        menu.add(checkBoxMenuItem);
        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Frames-Hidable"));
        checkBoxMenuItem.setMnemonic('H');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setHidable(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isHidable());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Frames-Rearrangable"));
        checkBoxMenuItem.setMnemonic('R');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setRearrangable(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isHidable());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Frames-Resizable"));
        checkBoxMenuItem.setMnemonic('S');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setResizable(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isResizable());
        menu.add(checkBoxMenuItem);

        menu.addSeparator();

        JMenu buttonsMenu = new JideMenu(I18N.get("Text.Available-Titlebar-Buttons"));

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Close-Button"));
        checkBoxMenuItem.setMnemonic('C');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_CLOSE);
            }
        });
        checkBoxMenuItem.setSelected(true);
        buttonsMenu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Autohide-Button"));
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_AUTOHIDE);
            }
        });
        checkBoxMenuItem.setSelected(true);
        buttonsMenu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Float-Button"));
        checkBoxMenuItem.setMnemonic('F');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_FLOATING);
            }
        });
        checkBoxMenuItem.setSelected(true);
        buttonsMenu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Maximize-Button"));
        checkBoxMenuItem.setMnemonic('M');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_MAXIMIZE);
            }
        });
        checkBoxMenuItem.setSelected(false);
        buttonsMenu.add(checkBoxMenuItem);

        menu.add(buttonsMenu);

        menu.addSeparator();

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Continuous-Layout"));
        checkBoxMenuItem.setMnemonic('C');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setContinuousLayout(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (parent.getDockingManager().isContinuousLayout()) {
                        Lm.showPopupMessageBox("<HTML>"
                                + "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Continuous Layout</FONT></B><FONT FACE='Tahoma'>"
                                + "<FONT FACE='Tahoma' SIZE='3'><BR><BR><B>An option to continuously layout affected components during resizing."
                                + "<BR></B><BR>This is the same option as in JSplitPane. If the option is true, when you resize"
                                + "<BR>the JSplitPane's divider, it will continuously redisplay and laid out during user"
                                + "<BR>intervention."
                                + "<BR><BR>Default: off</FONT>"
                                + "<BR></HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isContinuousLayout());
        menu.add(checkBoxMenuItem);

        menu.addSeparator();

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Show-Gripper"));
        checkBoxMenuItem.setMnemonic('S');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setShowGripper(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (parent.getDockingManager().isShowGripper()) {
                        Lm.showPopupMessageBox("<HTML>"
                                + "<FONT FACE='Tahoma' SIZE='4'><FONT COLOR='#0000FF'><B>Show Gripper</B><BR></FONT><BR></FONT>"
                                + "<FONT FACE='Tahoma' SIZE='3'><B>An option to give user a visual hint that the dockable frame can be dragged<BR></B>"
                                + "<BR>Normal tabs in JTabbedPane can not be dragged. However in our demo, "
                                + "<BR>most of them can be dragged. To make it obvious to user, we added an "
                                + "<BR>option so that a gripper is painted on the tab or the title bar of those "
                                + "<BR>dockable frames which can be dragged."
                                + "<BR><BR>Default: off</FONT><BR>"
                                + "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(true);//frame.getDockingManager().isShowGripper());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Show-TitleBar"));
        checkBoxMenuItem.setMnemonic('T');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setShowTitleBar(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (parent.getDockingManager().isShowTitleBar()) {
                        Lm.showPopupMessageBox("<HTML>"
                                + "<FONT FACE='Tahoma' SIZE='4'><FONT COLOR='#0000FF'><B>Show TitleBar</B><BR></FONT><BR></FONT>"
                                + "<FONT FACE='Tahoma' SIZE='3'><B>An option to show/hide dockable frame's title bar<BR></B>"
                                + "<BR><BR>Default: on</FONT><BR>"
                                + "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isShowTitleBar());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.SideBar-Rollover"));
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setSidebarRollover(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (parent.getDockingManager().isSidebarRollover()) {
                        Lm.showPopupMessageBox("<HTML>"
                                + "<FONT FACE='Tahoma' SIZE='4'><FONT COLOR='#0000FF'><B>SideBar Rollover</B><BR></FONT><BR></FONT>"
                                + "<FONT FACE='Tahoma' SIZE='3'><B>An option to control the sensibility of tabs on sidebar<BR></B>"
                                + "<BR>Each tab on four sidebars is corresponding to a dockable frame. Usually when "
                                + "<BR>user moves mouse over the tab, the dockable frame will show up. However in Eclipse"
                                + "<BR>you must click on it to show the dockable frame. This option will allow you to "
                                + "<BR>control the sensibility of it."
                                + "<BR><BR>Default: on</FONT><BR>"
                                + "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isSidebarRollover());
        menu.add(checkBoxMenuItem);

        menu.addSeparator();

        JRadioButtonMenuItem radioButtonMenuItem1 = new JRadioButtonMenuItem(I18N.get("Text.Draw-Full-Outline-When-Dragging"));
        radioButtonMenuItem1.setMnemonic('D');
        radioButtonMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    parent.getDockingManager().setOutlineMode(DefaultDockingManager.FULL_OUTLINE_MODE);
                    Lm.showPopupMessageBox("<HTML>"
                            + "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Outline Paint Mode</FONT></B><FONT FACE='Tahoma'>"
                            + "<FONT SIZE='4'>"
                            + "<FONT COLOR='#0000FF' SIZE='3'><BR><BR><B>An option of how to paint the outline during dragging.</B></FONT>"
                            + "<BR><BR><FONT SIZE='3'>Since our demo is purely based on Swing, and there is no way to have transparent native "
                            + "<BR>window using Swing. So we have to develop workarounds to paint the outline of a dragging frame. "
                            + "<BR>As a result, we get two ways to draw the outline. Since neither is perfect, we just leave it as "
                            + "<BR>an option to user to choose. You can try each of the option and see which one you like most."
                            + "<BR><B><BR>Option 1: PARTIAL_OUTLINE_MODE</B><BR>Pros: Fast, very smooth, works the best if user "
                            + "of your application always keeps it as full screen"
                            + "<BR>Cons: Partial outline or no outline at all if outside main frame although it's there wherever "
                            + "your mouse is."
                            + "<BR><BR><B>Option 2: FULL_OUTLINE_MODE</B>"
                            + "<BR>Pros: It always draw the full outline"
                            + "<BR>Cons: Sometimes it's flickering. Slower comparing with partial outline mode."
                            + "<BR><BR>Default: PARTIAL_OUTLINE_MODE</FONT>"
                            + "<BR></HTML>");
                }
            }
        });
        radioButtonMenuItem1.setSelected(parent.getDockingManager().getOutlineMode() == DefaultDockingManager.FULL_OUTLINE_MODE);
        menu.add(radioButtonMenuItem1);

        JRadioButtonMenuItem radioButtonMenuItem2 = new JRadioButtonMenuItem(I18N.get("Text.Draw-Partial-Outline-When-Dragging"));
        radioButtonMenuItem2.setMnemonic('P');
        radioButtonMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    parent.getDockingManager().setOutlineMode(DefaultDockingManager.PARTIAL_OUTLINE_MODE);
                    Lm.showPopupMessageBox("<HTML>"
                            + "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Outline Paint Mode</FONT></B><FONT FACE='Tahoma'>"
                            + "<FONT SIZE='4'><FONT COLOR='#0000FF'><BR></FONT><BR></FONT><B>An option of how to paint the outline during dragging. "
                            + "<BR><BR></B>Since our demo is purely based on Swing, and there is no way to have transparent native "
                            + "<BR>window using Swing. So we have to develop workarounds to paint the outline of a dragging frame. "
                            + "<BR>As a result, we get two ways to draw the outline. Since neither is perfect, we just leave it as "
                            + "<BR>an option to user to choose. You can try each of the option and see which one you like most."
                            + "<BR><B><BR>Option 1: PARTIAL_OUTLINE_MODE</B>"
                            + "<BR>Pros: Fast, very smooth"
                            + "<BR>Cons: Partial outline or no outline at all if outside main frame although it&#39;s there wherever your mouse is."
                            + "<BR><BR><B>Option 2: FULL_OUTLINE_MODE</B>"
                            + "<BR>Pros: It always draw the full outline<BR>Cons: Sometimes it&#39;s flickering. Slower comparing with partial outline mode.</FONT>"
                            + "<BR><BR><FONT FACE='Tahoma'>Default: PARTIAL_OUTLINE_MODE</FONT>"
                            + "<BR></HTML>");
                }
            }
        });
        radioButtonMenuItem2.setSelected(parent.getDockingManager().getOutlineMode() == DefaultDockingManager.PARTIAL_OUTLINE_MODE);
        menu.add(radioButtonMenuItem2);

        JRadioButtonMenuItem radioButtonMenuItem3 = new JRadioButtonMenuItem(I18N.get("Text.Draw-Transparent-Pane-When-Dragging"));
        radioButtonMenuItem3.setMnemonic('P');
        radioButtonMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    parent.getDockingManager().setOutlineMode(DefaultDockingManager.TRANSPARENT_OUTLINE_MODE);
                    Lm.showPopupMessageBox("<HTML>"
                            + "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Outline Paint Mode</FONT></B><FONT FACE='Tahoma'>"
                            + "<FONT SIZE='4'><FONT COLOR='#0000FF'><BR></FONT><BR></FONT><B>An option of how to paint the outline during dragging. "
                            + "<BR><BR></B>Instead of drawing an outline as all other options, this option will draw a transparent pane"
                            + "<BR>which looks better than the outline only."
                            + "<BR></HTML>");
                }
            }
        });
        radioButtonMenuItem3.setSelected(parent.getDockingManager().getOutlineMode() == DefaultDockingManager.TRANSPARENT_OUTLINE_MODE);
        menu.add(radioButtonMenuItem3);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonMenuItem1);
        buttonGroup.add(radioButtonMenuItem2);
        buttonGroup.add(radioButtonMenuItem3);
        menu.addSeparator();
        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Show-Title-on-Outline"));
        checkBoxMenuItem.setMnemonic('O');
        checkBoxMenuItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.getDockingManager().setShowTitleOnOutline(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.getDockingManager().isShowTitleOnOutline());
        menu.add(checkBoxMenuItem);
        menu.addSeparator();
        checkBoxMenuItem = new JCheckBoxMenuItem(I18N.get("Text.Always-on-top"));
        checkBoxMenuItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    parent.setAlwaysOnTop(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            }
        });
        checkBoxMenuItem.setSelected(parent.isAlwaysOnTop());
        menu.add(checkBoxMenuItem);
        return menu;
    }

    private void toggleButton(boolean selected, int button) {
        if (parent instanceof DockableHolder) {
            Collection<String> names = parent.getDockingManager().getAllFrames();
            for (String name : names) {
                DockableFrame f = parent.getDockingManager().getFrame(name);
                if (selected) {
                    f.setAvailableButtons(f.getAvailableButtons() | button);
                } else {
                    f.setAvailableButtons(f.getAvailableButtons() & ~button);
                }
            }
        }
    }

    public CommandBar createStandardCommandBar() {
        CommandBar commandBar = new CommandBar(I18N.get("Text.Standard"));
        commandBar.setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        commandBar.setInitMode(DockableBarContext.STATE_HORI_DOCKED);
        commandBar.setInitIndex(1);
        commandBar.setInitSubindex(0);
        newProject = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.NEW_PROJECT_BIG));
        newProject.setToolTipText(I18N.get("com.osfac.dmt.workbench.ui.plugin.NewTaskPlugIn"));
        open = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.OPEN_BIG));
        open.setToolTipText(I18N.get("org.openjump.core.ui.plugin.file.OpenWizardPlugIn"));
        saveDataset = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.SAVE_DATASETS));
        saveDataset.setToolTipText(I18N.get("org.openjump.core.ui.plugin.mousemenu.SaveDatasetsPlugIn"));
        undo = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.UNDO));
        undo.setToolTipText(I18N.get("com.osfac.dmt.workbench.ui.plugin.UndoPlugIn") + " (Ctrl+Z)");
        redo = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.REDO));
        redo.setToolTipText(I18N.get("com.osfac.dmt.workbench.ui.plugin.RedoPlugIn") + " (Ctrl+Y)");
        AbstractButton help = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.HELP));
        help.setToolTipText(I18N.get("ui.MenuNames.HELP"));
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionHelp();
            }
        });
        commandBar.add(newProject);
        commandBar.add(open);
        commandBar.addSeparator();
        commandBar.add(saveDataset);
        commandBar.addSeparator();
        commandBar.add(undo);
        commandBar.add(redo);
        commandBar.addSeparator();
        commandBar.add(help);
        return commandBar;
    }

    public CommandBar createToolsCommandBar() {
        CommandBar commandBar = new CommandBar(I18N.get("Text.Tools"));
        commandBar.setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        commandBar.setInitMode(DockableBarContext.STATE_HORI_DOCKED);
        commandBar.setInitIndex(1);
        commandBar.setInitSubindex(0);

        AbstractButton MIGeoSearch = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.ICON2));
        MIGeoSearch.setToolTipText(I18N.get("Text.Geographic-Search"));
        AbstractButton MIQuerySearch = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.QUERYSEARCH));
        MIQuerySearch.setToolTipText(I18N.get("Text.Query-Search"));
        AbstractButton MIUnzip = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.UNZIP));
        MIUnzip.setToolTipText(I18N.get("Text.Unzip-Files"));

        MIGeoSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionGeoSearch();
            }
        });
        MIQuerySearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionQuerySearch();
            }
        });
        MIUnzip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionUnzipFiles();
            }
        });

        AbstractButton MIUpdateDatabase = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.DATABASE));
        MIUpdateDatabase.setToolTipText(I18N.get("Text.Update-Database"));
        AbstractButton MIManageRequest = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.REQUEST));
        MIManageRequest.setToolTipText(I18N.get("Text.Data-Request"));
        AbstractButton MIStatistic = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.STATISTIC));
        MIStatistic.setToolTipText(I18N.get("Text.Statistic"));
        AbstractButton MICreateUser = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.USER));
        MICreateUser.setToolTipText(I18N.get("Text.User-Manager"));
        AbstractButton MIStack = createButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.STACK));
        MIStack.setToolTipText(I18N.get("Text.Composite-Band"));
        MIStack.setEnabled(false);

        MIUpdateDatabase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionUpdateDatabase();
            }
        });
        MIManageRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionDataRequest();
            }
        });
        MIStatistic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionStatistic();
            }
        });
        MICreateUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionUserManagement();
            }
        });
        MIStack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionCompositeBand();
            }
        });

        if (Config.isLiteVersion() || Config.isSimpleUser()) {
            commandBar.add(MIGeoSearch);
            commandBar.add(MIQuerySearch);
            commandBar.addSeparator();
            commandBar.add(MIUnzip);
        } else {
            commandBar.add(MIGeoSearch);
            commandBar.add(MIQuerySearch);
            commandBar.addSeparator();
            commandBar.add(MIUpdateDatabase);
            commandBar.add(MIStatistic);
            commandBar.add(MIManageRequest);
            commandBar.addSeparator();
            commandBar.add(MICreateUser);
            commandBar.addSeparator();
            commandBar.add(MIUnzip);
//            commandBar.add(MIStack);
        }
        return commandBar;
    }

    public DockableFrame createFrameMenuTree() {
        DockableFrame frame = new DockableFrame(I18N.get("Text.Tutorial-and-Requests"
                + ""), DMTIconsFactory.getImageIcon(DMTIconsFactory.ShortCut.SHORTCUT));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(new ShortCut());
        frame.setPreferredSize(new Dimension(220, 200));
        frame.setFocusable(false);
        return frame;
    }
    public static AbstractButton newProject, open, saveDataset, undo, redo;
    public String _lastDirectory = ".";
    private byte[] _fullScreenLayout;
    private boolean _autohideAll = false;
    static WorkbenchFrame parent;
}
