package com.osfac.dmt.workbench.ui.plugin;

import com.jidesoft.swing.JideMenu;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jts.util.Assert;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openjump.core.ui.plugin.AbstractUiPlugIn;
import org.openjump.core.ui.swing.listener.EnableCheckMenuItemShownListener;
import org.openjump.core.ui.swing.listener.MenuItemShownMenuListener;

/**
 * Adds a plug-in to the JUMP Workbench as a menu item.
 */
// TODO - Refactoring: Rename this class to PlugInMenuInstaller [Bob Boseko
// 10/22/2003]
public class FeatureInstaller {

    private interface Menu {

        void insert(JMenuItem menuItem, int i);

        String getText();

        int getItemCount();

        void add(JMenuItem menuItem);
    }
    private WorkbenchContext workbenchContext;
    private TaskMonitorManager taskMonitorManager = new TaskMonitorManager();
    private EnableCheckFactory checkFactory;
    private static Map plugin_EnableCheckMap = new HashMap();
    private static Map repeatMenuItemMap = new HashMap();
    private static RepeatableMenuItem[] RepeatableMenuItemArray = {null, null, null};

    public FeatureInstaller(WorkbenchContext workbenchContext) {
        this.workbenchContext = workbenchContext;
        checkFactory = new EnableCheckFactory(workbenchContext);
    }

    /**
     * @deprecated Use the EnableCheckFactory methods instead
     */
    public MultiEnableCheck createLayersSelectedCheck() {
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()).add(
                        checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    /**
     * @deprecated Use the EnableCheckFactory methods instead
     */
    public MultiEnableCheck createOneLayerSelectedCheck() {
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()).add(
                        checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }

    /**
     * @deprecated Use the EnableCheckFactory methods instead
     */
    public MultiEnableCheck createVectorsExistCheck() {
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()).add(
                        checkFactory.createAtLeastNVectorsMustBeDrawnCheck(1));
    }

    /**
     * @deprecated Use the EnableCheckFactory methods instead
     */
    public MultiEnableCheck createFenceExistsCheck() {
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()).add(
                        checkFactory.createFenceMustBeDrawnCheck());
    }

    public void addMenuSeparator(String menu) {
        addMenuSeparator(new String[]{
            menu
        });
    }

    public void addMenuSeparator(String[] menuPath) {
        JideMenu mainMenu = menuBarMenu(menuPath[0]);
        addMenuSeparator(createMenusIfNecessary(mainMenu, behead(menuPath)));
    }

    public void addMenuSeparator(JideMenu menu) {
        Component separator = null;
        Component exitMenu = null;
        if (menu.getText().equals(MenuNames.FILE)) {
            // Ensure separator and Exit appear last
            separator = menu.getMenuComponent(menu.getMenuComponentCount() - 2);
            exitMenu = menu.getMenuComponent(menu.getMenuComponentCount() - 1);
            menu.remove(separator);
            menu.remove(exitMenu);
        }
        menu.addSeparator();
        if (menu.getText().equals(MenuNames.FILE)) {
            menu.add(separator);
            menu.add(exitMenu);
        }
    }

    private void associate(final JMenuItem menuItem, PlugIn plugIn) {
        menuItem.addActionListener(AbstractPlugIn.toActionListener(plugIn, workbenchContext, taskMonitorManager));
    }

    public String[] behead(String[] a1) {
        String[] a2 = new String[a1.length - 1];
        System.arraycopy(a1, 1, a2, 0, a2.length);
        return a2;
    }

    /**
     * @deprecated
     */
    public void addMainMenuItem(PlugIn executable, String menuName,
            String menuItemName, Icon icon, EnableCheck enableCheck) {
        addMainMenuItem(executable, new String[]{menuName}, menuItemName,
                false, icon, enableCheck);
    }

    public void addLayerViewMenuItem(PlugIn executable, String menuName,
            String menuItemName) {
        addLayerViewMenuItem(executable, new String[]{
            menuName
        }, menuItemName);
    }

    public void addLayerNameViewMenuItem(PlugIn executable, String menuName,
            String menuItemName) {
        addLayerNameViewMenuItem(executable, new String[]{
            menuName
        }, menuItemName);
    }

    /**
     * Add a menu item to the main menu that is enabled only if the active internal frame is a
     * LayerViewPanelProxy.
     */
    public void addLayerViewMenuItem(PlugIn executable, String[] menuPath,
            String menuItemName) {
        addMainMenuItem(executable, menuPath, menuItemName, false, null,
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }

    /**
     * Add a menu item to the main menu that is enabled only if the active internal frame is a
     * LayerViewPanelProxy and a LayerNamePanelProxy.
     */
    public void addLayerNameViewMenuItem(PlugIn executable, String[] menuPath,
            String menuItemName) {
        addMainMenuItem(executable, menuPath, menuItemName, false, null,
                new MultiEnableCheck().add(
                        checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()).add(
                        checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()));
    }

    /**
     * @param menuPath separate items with slashes; items will be created if they do not already
     * exist
     * @param menuItemName name of the menu item
     * @param checkBox whether to create a JCheckBoxMenuItem or a JMenuItem
     * @param icon an Icon or null
     * @param enableCheck conditions to make the plugin available to the user
     * @see GUIUtil#toSmallIcon
     * @deprecated
     */
    public void addMainMenuItem(PlugIn executable, String[] menuPath,
            String menuItemName, boolean checkBox, Icon icon, EnableCheck enableCheck) {
        Map properties = extractProperties(menuItemName);
        int pos = -1;
        if (properties.get("pos") != null) {
            pos = Integer.parseInt((String) properties.get("pos"));
        }
        menuItemName = removeProperties(menuItemName);
        final JMenuItem menuItem = checkBox
                ? new JCheckBoxMenuItem(menuItemName)
                : new JMenuItem(menuItemName);
        addMainMenuItem(executable, menuPath, menuItem, enableCheck, pos);
        menuItem.setIcon(icon);
    }

    /**
     * Add a Plugin as a JMenuItem with enableCheck conditions.
     *
     * @param menuPath path from the main menu to the menu item
     * @param plugin the plugin associated to this menu item
     */
    public JMenuItem addMainMenuItem(String[] menuPath, AbstractUiPlugIn plugin) {
        JMenuItem menuItem = new JMenuItem(plugin.getName());
        return addMainMenuItem(menuPath, plugin, menuItem, null, -1);
    }

    /**
     * Add a Plugin as a JMenuItem with enableCheck conditions.
     *
     * @param menuPath path from the main menu to the menu item
     * @param plugin the plugin associated to this menu item
     * @param pos defines the position of the menu item in the menu -1 adds menuItem at the end
     * except for FILE menu where -1 adds menuItem before the separator preceding exit menu item
     */
    public JMenuItem addMainMenuItem(final String[] menuPath,
            final AbstractUiPlugIn plugin,
            final int pos) {
        JMenuItem menuItem = new JMenuItem(plugin.getName());
        return addMainMenuItem(menuPath, plugin, menuItem, null, pos);
    }

    /**
     * Add a Plugin as a JMenuItem with enableCheck conditions.
     *
     * @param menuPath path from the main menu to the menu item
     * @param plugin the plugin associated to this menu item
     * @param enableCheck conditions making the plugin enabled
     */
    public JMenuItem addMainMenuItem(final String[] menuPath,
            final AbstractUiPlugIn plugin,
            final EnableCheck enableCheck) {
        JMenuItem menuItem = new JMenuItem(plugin.getName());
        return addMainMenuItem(menuPath, plugin, menuItem, enableCheck, -1);
    }

    /**
     * Add a Plugin as a JMenuItem with enableCheck
     *
     * @param menuPath path from the main menu to the menu item
     * @param plugin the plugin associated to this menu item
     * @param enableCheck conditions making the plugin enabled
     * @param pos defines the position of the menu item in the menu -1 adds menuItem at the end
     * except for FILE menu where -1 adds menuItem before the separator preceding exit menu item
     */
    public JMenuItem addMainMenuItem(final String[] menuPath,
            final AbstractUiPlugIn plugin,
            final EnableCheck enableCheck,
            final int pos) {
        JMenuItem menuItem = new JMenuItem(plugin.getName());
        return addMainMenuItem(menuPath, plugin, menuItem, enableCheck, pos);
    }

    /**
     * Add a Plugin as a JMenuItem or a subclass of JMenuItem to the main menu
     *
     * @param menuPath path from the main menu to the menu item
     * @param plugin the plugin associated to this menu item
     * @param menuItem the menu item (JMenuItem, JCheckBoxMenuItem, JMenu, JRadioButtonMenuItem)
     * @param pos defines the position of the menu item in the menu -1 adds menuItem at the end
     * except for FILE menu where -1 adds menuItem before the separator preceding exit menu item
     */
    //Added by Michael Michaud on 2008-04-06
    //This method makes it possible to add any subclasses of JMenuItem
    public JMenuItem addMainMenuItem(final String[] menuPath,
            final AbstractUiPlugIn plugin,
            final JMenuItem menuItem,
            final int pos) {
        return addMainMenuItem(menuPath, plugin, menuItem, null, pos);
    }

    /**
     * New generic addMainMenuItem method using AbstractUiPlugIn.
     *
     * @param menuPath the menu path made of the menu and submenu names
     * @param plugin the plugin to execute with this item
     * @param menuItem the JMenuItem (or JCheckBoxMenuItem or JRadioButtonMenuItem) to the parent
     * menu
     * @param enableCheck conditions making the plugin enabled
     * @param pos defines the position of the menu item in the menu -1 adds menuItem at the end
     * except for FILE menu where -1 adds menuItem before the separator preceding exit menu item
     */
    // [mmichaud 2011-10-01]
    public JMenuItem addMainMenuItem(final String[] menuPath,
            final AbstractUiPlugIn plugin,
            final JMenuItem menuItem,
            final EnableCheck enableCheck,
            final int pos) {
        if (menuItem.getIcon() == null) {
            menuItem.setIcon(plugin.getIcon());
        }
        return addMainMenuItem(plugin, menuPath, menuItem, enableCheck, pos);
    }

    /**
     * New generic addMainMenuItem method. Adds menuItem at the end of the menu except for FILE menu
     * where menuItem is added before the separator preceding exit menu item
     *
     * @param plugin the plugin to execute with this item
     * @param menuPath the menu path made of the menu and submenu names
     * @param menuItem the JMenuItem (or JCheckBoxMenuItem or JRadioButtonMenuItem) to the parent
     * menu
     * @param enableCheck conditions making the plugin enabled
     */
    // [mmichaud 2011-10-20]
    public JMenuItem addMainMenuItem(PlugIn plugin, String[] menuPath,
            JMenuItem menuItem, EnableCheck enableCheck) {
        return addMainMenuItem(plugin, menuPath, menuItem, enableCheck, -1);
    }

    /**
     * New generic addMainMenuItem method.
     *
     * @param plugin the plugin to execute with this item
     * @param menuPath the menu path made of the menu and submenu names
     * @param menuItem the JMenuItem (or JCheckBoxMenuItem or JRadioButtonMenuItem) to the parent
     * menu
     * @param enableCheck conditions making the plugin enabled
     * @param pos defines the position of menuItem in the menu -1 adds menuItem at the end of the
     * menu except for FILE menu where -1 adds menuItem before the separator preceding exit menuItem
     */
    // [mmichaud 2011-09-13]
    public JMenuItem addMainMenuItem(PlugIn plugin, String[] menuPath,
            JMenuItem menuItem, EnableCheck enableCheck, int pos) {
        JideMenu menu = menuBarMenu(menuPath[0]);
        if (menu == null) {
            menu = (JideMenu) installMnemonic(new JideMenu(menuPath[0]), menuBar());
            addToMenuBar(menu);
        }
        JideMenu parent = createMenusIfNecessary(menu, behead(menuPath));
        if (menuItem.getText().trim().length() == 0) {
            menuItem.setText(plugin.getName());
        }
        installMnemonic(menuItem, parent);
        associate(menuItem, plugin);
        //insert(menuItem, createMenu(parent), properties);
        if (pos >= 0) {
            parent.insert(menuItem, pos);
        } else if (parent.getText().equals(MenuNames.FILE)) {
            // In File menu, insert new items before the separator before Exit [Bob Boseko]
            parent.insert(menuItem, parent.getItemCount() - 2);
        } else {
            parent.add(menuItem);
        }
        if (enableCheck != null) {
            addMenuItemShownListener(menuItem, toMenuItemShownListener(enableCheck));
        }
        return menuItem;
    }

    private Menu createMenu(final JideMenu menu) {
        return new Menu() {
            @Override
            public void insert(JMenuItem menuItem, int i) {
                menu.insert(menuItem, i);
            }

            @Override
            public String getText() {
                return menu.getText();
            }

            @Override
            public int getItemCount() {
                return menu.getItemCount();
            }

            @Override
            public void add(JMenuItem menuItem) {
                menu.add(menuItem);
            }
        };
    }

    private void insert(final JMenuItem menuItem, Menu parent, Map properties) {
        if (properties.get("pos") != null) {
            parent.insert(menuItem, Integer.parseInt((String) properties.get("pos")));
        } else if (parent.getText().equals(MenuNames.FILE)) {
            // If menu is File, insert menu item just before Exit and its
            // separator [Bob Boseko]
            parent.insert(menuItem, parent.getItemCount() - 2);
        } else {
            parent.add(menuItem);
        }
    }

    private Map extractProperties(String menuItemName) {
        if (menuItemName.indexOf('{') == -1) {
            return new HashMap();
        }
        Map properties = new HashMap();
        String s = menuItemName.substring(menuItemName.indexOf('{') + 1,
                menuItemName.indexOf('}'));
        for (Iterator i = StringUtil.fromCommaDelimitedString(s).iterator(); i.hasNext();) {
            String property = (String) i.next();
            properties.put(property.substring(0, property.indexOf(':')).trim(),
                    property.substring(property.indexOf(':') + 1, property.length()).trim());
        }
        return properties;
    }

    public static String removeProperties(String menuItemName) {
        return menuItemName.indexOf('{') > -1 ? menuItemName.substring(0,
                menuItemName.indexOf('{')) : menuItemName;
    }

    public static JMenuItem installMnemonic(JMenuItem menuItem, MenuElement parent) {
        String text = menuItem.getText();
        StringUtil.replaceAll(text, "&&", "##");
        int ampersandPosition = text.indexOf('&');
        if (-1 < ampersandPosition && ampersandPosition + 1 < text.length()) {
            menuItem.setMnemonic(text.charAt(ampersandPosition + 1));
            text = StringUtil.replace(text, "&", "", false);
        } else {
            installDefaultMnemonic(menuItem, parent);
        }
        // Double-ampersands get converted to single-ampersands. [Bob Boseko]
        StringUtil.replaceAll(text, "##", "&");
        menuItem.setText(text);
        return menuItem;
    }

    private static void installDefaultMnemonic(JMenuItem menuItem, MenuElement parent) {
        outer:
        for (int i = 0; i < menuItem.getText().length(); i++) {
            // Swing stores mnemonics in upper case [Bob Boseko]
            char candidate = Character.toUpperCase(menuItem.getText().charAt(i));
            if (!Character.isLetter(candidate)) {
                continue;
            }
            for (Iterator j = menuItems(parent).iterator(); j.hasNext();) {
                JMenuItem other = (JMenuItem) j.next();
                if (other.getMnemonic() == candidate) {
                    continue outer;
                }
            }
            menuItem.setMnemonic(candidate);
            return;
        }
        menuItem.setMnemonic(menuItem.getText().charAt(0));
    }

    private static Collection menuItems(MenuElement element) {
        ArrayList menuItems = new ArrayList();
        if (element instanceof JMenuBar) {
            for (int i = 0; i < ((JMenuBar) element).getMenuCount(); i++) {
                CollectionUtil.addIfNotNull(((JMenuBar) element).getMenu(i), menuItems);
            }
        } else if (element instanceof JideMenu) {
            for (int i = 0; i < ((JideMenu) element).getItemCount(); i++) {
                CollectionUtil.addIfNotNull(((JideMenu) element).getItem(i), menuItems);
            }
        } else if (element instanceof JPopupMenu) {
            MenuElement[] children = ((JPopupMenu) element).getSubElements();
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JMenuItem) {
                    menuItems.add(children[i]);
                }
            }
        } else {
            Assert.shouldNeverReachHere(element.getClass().getName());
        }
        return menuItems;
    }

    private MenuItemShownListener toMenuItemShownListener(final EnableCheck enableCheck) {
        return new EnableCheckMenuItemShownListener(workbenchContext, enableCheck);
    }

    /**
     * @return the leaf
     */
    public JideMenu createMenusIfNecessary(JideMenu parent, String[] menuPath) {
        if (menuPath.length == 0) {
            return parent;
        }
        JideMenu child = (JideMenu) childMenuItem(menuPath[0], parent);
        if (child == null) {
            child = (JideMenu) installMnemonic(new JideMenu(menuPath[0]), parent);
            parent.add(child);
        }
        return createMenusIfNecessary(child, behead(menuPath));
    }

    public void addMenuItemShownListener(final JMenuItem menuItem,
            final MenuItemShownListener menuItemShownListener) {
        JideMenu menu = (JideMenu) ((JPopupMenu) menuItem.getParent()).getInvoker();
        menu.addMenuListener(new MenuItemShownMenuListener(menuItem,
                menuItemShownListener));
    }

    /**
     * @param enableCheck null to leave unspecified
     */
    public void addPopupMenuItem(JPopupMenu popupMenu, PlugIn executable,
            String menuItemName, boolean checkBox, Icon icon, EnableCheck enableCheck) {
        Map properties = extractProperties(menuItemName);
        menuItemName = removeProperties(menuItemName);
        JMenuItem menuItem = installMnemonic(checkBox ? new JCheckBoxMenuItem(
                menuItemName) : new JMenuItem(menuItemName), popupMenu);
        menuItem.setIcon(icon);
        addPopupMenuItem(popupMenu, executable, new String[0], menuItem, properties, enableCheck);
    }

    /**
     * Add a menu item to a JPopupMenu with optional submenus defined by menuPath Added by mmichaud
     * on 2011-03-20 to reorganize LayerNamePanel JPopupMenu
     */
    public void addPopupMenuItem(JPopupMenu popupMenu, PlugIn executable,
            String[] menuPath, String menuItemName, boolean checkBox, Icon icon, EnableCheck enableCheck) {
        Map properties = extractProperties(menuItemName);
        menuItemName = removeProperties(menuItemName);
        JMenuItem menuItem = installMnemonic(checkBox ? new JCheckBoxMenuItem(
                menuItemName) : new JMenuItem(menuItemName), popupMenu);
        menuItem.setIcon(icon);
        addPopupMenuItem(popupMenu, executable, menuPath, menuItem, properties, enableCheck);
    }

    private void addPopupMenuItem(JPopupMenu popupMenu, final PlugIn executable,
            final String[] menuPath, final JMenuItem menuItem, Map properties, final EnableCheck enableCheck) {
        associate(menuItem, executable);
        if (menuPath == null || menuPath.length == 0) {
            insert(menuItem, createMenu(popupMenu), properties);
        } else {
            JideMenu menu = popupMenu(popupMenu, menuPath[0]);
            if (menu == null) {
                menu = (JideMenu) popupMenu.add(new JideMenu(menuPath[0]));
            }
            JideMenu parent = createMenusIfNecessary(menu, behead(menuPath));
            insert(menuItem, createMenu(parent), properties);
        }
        if (enableCheck != null) {
            popupMenu.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    toMenuItemShownListener(enableCheck).menuItemShown(menuItem);
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
        }
    }

    public void addPopupMenuSeparator(JPopupMenu popupMenu, String[] menuPath) {
        if (menuPath == null || menuPath.length == 0) {
            popupMenu.addSeparator();
        } else {
            JideMenu menu = popupMenu(popupMenu, menuPath[0]);
            if (menu == null) {
                menu = (JideMenu) popupMenu.add(new JideMenu(menuPath[0]));
            }
            JideMenu parent = createMenusIfNecessary(menu, behead(menuPath));
            parent.addSeparator();
        }
    }

    /**
     * @return the menu with the given name, or null if no such menu exists
     */
    public static JideMenu popupMenu(JPopupMenu popupMenu, String childName) {
        MenuElement[] subElements = popupMenu.getSubElements();
        for (int i = 0; i < subElements.length; i++) {
            if (!(subElements[i] instanceof JMenuItem)) {
                continue;
            }
            JMenuItem menuItem = (JMenuItem) subElements[i];
            if (menuItem.getText().equals(childName)) {
                return (JideMenu) menuItem;
            }
        }
        return null;
    }

    private Menu createMenu(final JPopupMenu popupMenu) {
        return new Menu() {
            @Override
            public void insert(JMenuItem menuItem, int i) {
                popupMenu.insert(menuItem, i);
            }

            @Override
            public String getText() {
                return "";
            }

            @Override
            public int getItemCount() {
                return popupMenu.getComponentCount();
            }

            @Override
            public void add(JMenuItem menuItem) {
                popupMenu.add(menuItem);
            }
        };
    }

    public JMenuBar menuBar() {
        return workbenchContext.getWorkbench().getFrame().getJMenuBar();
    }

    /**
     * @return the menu with the given name, or null if no such menu exists
     */
    public JideMenu menuBarMenu(String childName) {
        MenuElement[] subElements = menuBar().getSubElements();
        for (int i = 0; i < subElements.length; i++) {
            if (!(subElements[i] instanceof JMenuItem)) {
                continue;
            }
            JMenuItem menuItem = (JMenuItem) subElements[i];
            if (menuItem.getText().equals(childName)) {
                return (JideMenu) menuItem;
            }
        }
        return null;
    }

    private void addToMenuBar(JideMenu menu) {
        menuBar().add(menu);
        // Ensure Window and Help are placed at the end. Remove #windowMenu and
        // #helpMenu
        // *after* adding #menu, because #menu might be the Window or Help menu!
        // [Bob Boseko]
        JideMenu windowMenu = menuBarMenu(MenuNames.WINDOW);
        JideMenu helpMenu = menuBarMenu(MenuNames.HELP);
        // Customized workbenches may not have Window or Help menus [Bob Boseko]
        if (windowMenu != null) {
            menuBar().remove(windowMenu);
        }
        if (helpMenu != null) {
            menuBar().remove(helpMenu);
        }
        if (windowMenu != null) {
            menuBar().add(windowMenu);
        }
        if (helpMenu != null) {
            menuBar().add(helpMenu);
        }
    }

    public static JMenuItem childMenuItem(String childName, MenuElement menu) {
        if (menu instanceof JideMenu) {
            return childMenuItem(childName, ((JideMenu) menu).getPopupMenu());
        }
        MenuElement[] childMenuItems = menu.getSubElements();
        for (int i = 0; i < childMenuItems.length; i++) {
            if (childMenuItems[i] instanceof JMenuItem
                    && ((JMenuItem) childMenuItems[i]).getText().equals(childName)) {
                return ((JMenuItem) childMenuItems[i]);
            }
        }
        return null;
    }

    /**
     * Workaround for Java Bug 4809393: "Menus disappear prematurely after displaying modal dialog"
     * Evidently fixed in Java 1.5. The workaround is to wrap #actionPerformed with
     * SwingUtilities#invokeLater.
     */
    public void addMainMenuItemWithJava14Fix(PlugIn executable,
            String[] menuPath, String menuItemName, boolean checkBox, Icon icon,
            EnableCheck enableCheck) {
        addMainMenuItem(executable, menuPath, menuItemName, checkBox, icon,
                enableCheck);
        JMenuItem menuItem = FeatureInstaller.childMenuItem(
                FeatureInstaller.removeProperties(menuItemName),
                ((JideMenu) createMenusIfNecessary(menuBarMenu(menuPath[0]), behead(menuPath))));
        final ActionListener listener = abstractPlugInActionListener(menuItem.getActionListeners());
        menuItem.removeActionListener(listener);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        listener.actionPerformed(e);
                    }
                });
            }
        });
    }

    private ActionListener abstractPlugInActionListener(ActionListener[] actionListeners) {
        for (int i = 0; i < actionListeners.length; i++) {
            if (actionListeners[i].getClass().getName().indexOf(AbstractPlugIn.class.getName()) > -1) {
                return actionListeners[i];
            }
        }
        Assert.shouldNeverReachHere();
        return null;
    }

    public static JideMenu addMainMenu(FeatureInstaller featureInstaller,
            final String[] menuPath, String menuName, int index) {
        JideMenu menu = new JideMenu(menuName);
        JideMenu parent = featureInstaller.createMenusIfNecessary(
                featureInstaller.menuBarMenu(menuPath[0]),
                featureInstaller.behead(menuPath));
        parent.insert(menu, index);
        return menu;
    }

    public void associateWithRepeat(PlugIn executable) {
        JPopupMenu popupMenu = LayerViewPanel.popupMenu();

        if (!plugin_EnableCheckMap.containsKey(executable)) {
            return;
        }

        //if this executable already associated with a repeat menu then nothing to do
        for (int i = 0; i < RepeatableMenuItemArray.length; i++) {
            if (RepeatableMenuItemArray[i] != null) {
                if (RepeatableMenuItemArray[i].isSetTo(executable)) {
                    return;
                }
            }
        }

        //we are going to be moving executables to different repeatMenuItems
        //so remove all menuListeners
        for (int i = 0; i < RepeatableMenuItemArray.length; i++) {
            if (RepeatableMenuItemArray[i] == null) {
                break;
            }
            PopupMenuListener menuListener = RepeatableMenuItemArray[i].getMenuListener();
            if (menuListener != null) {
                popupMenu.removePopupMenuListener(menuListener);
            }
        }

        //this plugin not on list of repeats
        //so pull down existing repeats and add this to top
        for (int i = RepeatableMenuItemArray.length - 1; i > 0; i--) {
            if (RepeatableMenuItemArray[i - 1] == null) //nothing to move
            {
                continue;
            }

            //at this point we have one above to pull down
            PlugIn prevExec = RepeatableMenuItemArray[i - 1].getExecutable();
            EnableCheck prevEnableCheck = (EnableCheck) plugin_EnableCheckMap.get(prevExec);

            if (RepeatableMenuItemArray[i] == null) {
                RepeatableMenuItem repeatMenuItem = new RepeatableMenuItem(prevExec, prevEnableCheck);
                popupMenu.insert(repeatMenuItem.getMenuItem(), i + 3);
                RepeatableMenuItemArray[i] = repeatMenuItem;
            } else {
                RepeatableMenuItemArray[i].setExecutable(prevExec, prevEnableCheck);
            }
        }

        EnableCheck enableCheck = (EnableCheck) plugin_EnableCheckMap.get(executable);

        if (RepeatableMenuItemArray[0] == null) {
            RepeatableMenuItem repeatMenuItem = new RepeatableMenuItem(executable, enableCheck);
            popupMenu.insert(repeatMenuItem.getMenuItem(), 2);
            popupMenu.insert(new JPopupMenu.Separator(), 2);
            RepeatableMenuItemArray[0] = repeatMenuItem;
        } else {
            RepeatableMenuItemArray[0].setExecutable(executable, enableCheck);
        }

        //now add all listeners to popupMenu
        for (int i = 0; i < RepeatableMenuItemArray.length; i++) {
            if (RepeatableMenuItemArray[i] == null) {
                break;
            }
            PopupMenuListener menuListener = RepeatableMenuItemArray[i].getMenuListener();
            if (menuListener != null) {
                popupMenu.addPopupMenuListener(menuListener);
            }
        }
    }

    private class RepeatableMenuItem {

        private PlugIn executable = null;
        private JMenuItem menuItem = null;
        private PopupMenuListener menuListener = null;

        public RepeatableMenuItem(PlugIn executable, final EnableCheck enableCheck) {
            this.menuItem = new JMenuItem("Repeat");
            setExecutable(executable, enableCheck);
        }

        public final void setExecutable(PlugIn executable, final EnableCheck enableCheck) {
            this.executable = executable;
            ActionListener[] al = menuItem.getActionListeners();

            for (int i = 0; i < al.length; i++) {
                menuItem.removeActionListener(al[0]);
            }

            menuItem.addActionListener(AbstractPlugIn.toActionListener(executable,
                    workbenchContext, taskMonitorManager));

            this.menuItem.setText("Repeat: " + executable.getName());

            if (enableCheck == null) {
                this.menuListener = null;
            } else {
                this.menuListener = new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        toMenuItemShownListener(enableCheck).menuItemShown(menuItem);
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                };
            }
        }

        JMenuItem getMenuItem() {
            return menuItem;
        }

        PlugIn getExecutable() {
            return executable;
        }

        PopupMenuListener getMenuListener() {
            return menuListener;
        }

        boolean isSetTo(PlugIn executable) {
            return this.executable == executable;
        }
    }

    /**
     * @author Larry Becker Needed this class to be not anonymous so it's type could be determined
     * at runtime.
     */
    public class JumpMenuListener implements MenuListener {

        MenuItemShownListener menuItemShownListener;
        JMenuItem menuItem;

        public JumpMenuListener(MenuItemShownListener menuItemShownListener,
                JMenuItem menuItem) {
            super();
            this.menuItemShownListener = menuItemShownListener;
            this.menuItem = menuItem;
        }

        @Override
        public void menuSelected(MenuEvent e) {
            menuItemShownListener.menuItemShown(menuItem);
        }

        @Override
        public void menuCanceled(MenuEvent e) {
        }

        @Override
        public void menuDeselected(MenuEvent e) {
        }
    }
}
