/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerEventType;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.addremove.AddRemovePanel;
import com.osfac.dmt.workbench.ui.cursortool.editing.EditingPlugIn;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.EditablePlugIn;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.util.Assert;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class SchemaPanel extends JPanel {

    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel statusLabel = new JLabel();
    private Layer layer;
    private Point currentClickPoint;
    private JPopupMenu popupMenu = new JPopupMenu();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JPanel buttonPanel = new JPanel();
    private JButton applyButton = new JButton();
    private JCheckBox forceInvalidConversionsToNullCheckBox = new JCheckBox();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private JPanel jPanel2 = new JPanel();
    private Border border1;
    private boolean modified = false;
    private ArrayList listeners = new ArrayList();
    private JButton revertButton = new JButton();
    private BorderLayout borderLayout1 = new BorderLayout();
    private WorkbenchToolBar toolBar = new WorkbenchToolBar(null) {
        @Override
        public JButton addPlugIn(
                Icon icon,
                PlugIn plugIn,
                EnableCheck enableCheck,
                WorkbenchContext workbenchContext) {
            return super.addPlugIn(icon, addCleanUp(plugIn), enableCheck, workbenchContext);
        }
    };

    private void setModel(SchemaTableModel model) {
        table.setModel(model);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                updateComponents();
            }
        });
        //Must init cell editors every time we init model. [Bob Boseko]
        initCellEditors();
        updateComponents();
    }

    public SchemaPanel(final Layer layer, EditingPlugIn editingPlugIn, WorkbenchContext context) {
        editablePlugIn = new EditablePlugIn(editingPlugIn);
        try {
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    updateComponents();
                }
            });
            this.layer = layer;
            jbInit();
            //Call #initPopupMenu and #initToolBar before #setModel, because #setModel
            //calls #updateComponents. [Bob Boseko]
            initPopupMenu(context);
            initToolBar(context);
            setModel(new SchemaTableModel(layer));
            layer.getLayerManager().addLayerListener(new LayerListener() {
                @Override
                public void categoryChanged(CategoryEvent e) {
                }

                @Override
                public void featuresChanged(FeatureEvent e) {
                }

                @Override
                public void layerChanged(LayerEvent e) {
                    if (e.getLayerable() != layer) {
                        return;
                    }

                    if (e.getType() == LayerEventType.METADATA_CHANGED) {
                        //If layer becomes editable, apply row striping and remove gridlines,
                        //as recommended in Java Look and Feel Design Guidelines: Advanced Topics [Bob Boseko]
                        updateComponents();
                        repaint();
                    }
                }
            });
        } catch (Exception ex) {
            Assert.shouldNeverReachHere(ex.toString());
        }
    }

    private int[] rowsToActOn() {
        if (table.getSelectedRowCount() > 0) {
            return table.getSelectedRows();
        }
        if (getCurrentClickPoint() != null && table.rowAtPoint(getCurrentClickPoint()) != -1) {
            return new int[]{table.rowAtPoint(getCurrentClickPoint())};
        }
        return new int[]{};
    }

    private EnableCheck basicEnableCheck = new EnableCheck() {
        @Override
        public String check(JComponent component) {
            if (!layer.isEditable()) {
                return I18N.get("ui.SchemaPanel.layer-must-be-editable");
            }
            if (rowsToActOn().length == 0) {
                return I18N.get("ui.SchemaPanel.at-least-1-row-must-be-selected");
            }
            return null;
        }
    };

    private abstract class MyPlugIn extends AbstractPlugIn {

        public MultiEnableCheck createEnableCheck() {
            return new MultiEnableCheck().add(basicEnableCheck);
        }

        public abstract Icon getIcon();
    }

    private MyPlugIn insertPlugIn = new MyPlugIn() {
        @Override
        public String getName() {
            return I18N.get("ui.SchemaPanel.insert");
        }

        @Override
        public Icon getIcon() {
            return GUIUtil.toSmallIcon(IconLoader.icon("Plus.gif"));
        }

        @Override
        public boolean execute(PlugInContext context) throws Exception {
            markAsModified();
            getModel().insertBlankRow(rowsToActOn()[0]);
            return true;
        }
    };

    private MyPlugIn deletePlugIn = new MyPlugIn() {
        @Override
        public String getName() {
            return I18N.get("ui.SchemaPanel.delete");
        }

        @Override
        public Icon getIcon() {
            return GUIUtil.toSmallIcon(IconLoader.icon("Delete.gif"));
        }

        @Override
        public boolean execute(PlugInContext context) throws Exception {
            markAsModified();
            getModel().removeFields(rowsToActOn());
            return true;
        }
    };

    private MyPlugIn moveUpPlugIn = new MyPlugIn() {
        @Override
        public Icon getIcon() {
            return GUIUtil.toSmallIcon(IconLoader.icon("VCRUp.gif"));
        }

        @Override
        public String getName() {
            return I18N.get("ui.SchemaPanel.move-field-up");
        }

        @Override
        public boolean execute(PlugInContext context) throws Exception {
            markAsModified();
            move(AddRemovePanel.itemsToMoveUp(getModel().getFields(), toFields(rowsToActOn())), -1);
            return true;
        }

        @Override
        public MultiEnableCheck createEnableCheck() {
            return super.createEnableCheck().add(new EnableCheck() {
                @Override
                public String check(JComponent component) {
                    return min(rowsToActOn()) == 0 ? I18N.get("ui.SchemaPanel.field-is-already-at-the-top") : null;
                    //No corresponding check in moveDownPlugIn because there is no
                    //bottom! (We keep adding rows as necessary) [Bob Boseko]
                }
            });
        }
    };
    private EditablePlugIn editablePlugIn;

    private MyPlugIn moveDownPlugIn = new MyPlugIn() {
        @Override
        public Icon getIcon() {
            return GUIUtil.toSmallIcon(IconLoader.icon("VCRDown.gif"));
        }

        @Override
        public String getName() {
            return I18N.get("ui.SchemaPanel.move-field-down");
        }

        @Override
        public boolean execute(PlugInContext context) throws Exception {
            markAsModified();
            move(
                    AddRemovePanel.itemsToMoveDown(getModel().getFields(), toFields(rowsToActOn())),
                    1);
            return true;
        }
    };
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTable table = new JTable();

    private void initToolBar(WorkbenchContext context) {
        toolBar.addPlugIn(
                insertPlugIn.getIcon(),
                insertPlugIn,
                insertPlugIn.createEnableCheck(),
                context);
        toolBar.addPlugIn(
                deletePlugIn.getIcon(),
                deletePlugIn,
                deletePlugIn.createEnableCheck(),
                context);
        toolBar.addPlugIn(
                moveUpPlugIn.getIcon(),
                moveUpPlugIn,
                moveUpPlugIn.createEnableCheck(),
                context);
        toolBar.addPlugIn(
                moveDownPlugIn.getIcon(),
                moveDownPlugIn,
                moveDownPlugIn.createEnableCheck(),
                context);
    }

    private void initPopupMenu(WorkbenchContext context) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setCurrentClickPoint(e.getPoint());

                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        addPopupMenuItem(
                editablePlugIn,
                true,
                null,
                editablePlugIn.createEnableCheck(context), context);
        popupMenu.addSeparator();
        addPopupMenuItem(
                insertPlugIn,
                false,
                insertPlugIn.getIcon(),
                insertPlugIn.createEnableCheck(), context);
        addPopupMenuItem(
                deletePlugIn,
                false,
                deletePlugIn.getIcon(),
                deletePlugIn.createEnableCheck(), context);
        popupMenu.addSeparator();
        addPopupMenuItem(
                moveUpPlugIn,
                false,
                moveUpPlugIn.getIcon(),
                moveUpPlugIn.createEnableCheck(), context);
        addPopupMenuItem(
                moveDownPlugIn,
                false,
                moveDownPlugIn.getIcon(),
                moveDownPlugIn.createEnableCheck(), context);
    }

    private void addPopupMenuItem(PlugIn plugIn, boolean checkBox, Icon icon, EnableCheck enableCheck, WorkbenchContext context) {
        FeatureInstaller installer = new FeatureInstaller(context);
        installer.addPopupMenuItem(popupMenu,
                addCleanUp(plugIn),
                plugIn.getName(), checkBox, icon, enableCheck);
    }

    private PlugIn addCleanUp(final PlugIn plugIn) {
        return new PlugIn() {
            @Override
            public String toString() {
                return plugIn.toString();
            }

            @Override
            public boolean execute(PlugInContext context) throws Exception {
                try {
                    return plugIn.execute(context);
                } finally {
                    setCurrentClickPoint(null);
                    updateComponents();
                }
            }

            @Override
            public void initialize(PlugInContext context) throws Exception {
                plugIn.initialize(context);
            }

            @Override
            public String getName() {
                return plugIn.getName();
            }
        };
    }

    public boolean isModified() {
        return modified;
    }

    private Collection toFields(int[] rows) {
        ArrayList fields = new ArrayList();

        for (int i = 0; i < rows.length; i++) {
            fields.add(getModel().get(rows[i]));
        }

        return fields;
    }

    private void updateComponents() {
        table.setShowGrid(layer.isEditable());
        table.setRowHeight(20); // fix proposed by uwe to have readable comboboxes with MetalL&F
        applyButton.setEnabled(layer.isEditable());
        revertButton.setEnabled(layer.isEditable());
        forceInvalidConversionsToNullCheckBox.setEnabled(layer.isEditable());
        reportError(validateInput());
        toolBar.updateEnabledState();
    }

    public SchemaTableModel getModel() {
        return (SchemaTableModel) table.getModel();
    }

    private TableColumn fieldNameColumn() {
        return table.getColumnModel().getColumn(
                getModel().indexOfColumn(SchemaTableModel.FIELD_NAME_COLUMN_NAME));
    }

    private TableColumn dataTypeColumn() {
        return table.getColumnModel().getColumn(
                getModel().indexOfColumn(SchemaTableModel.DATA_TYPE_COLUMN_NAME));
    }

    private void initCellEditors() {
        fieldNameColumn().setCellEditor(new MyFieldNameEditor());
        dataTypeColumn().setCellEditor(new MyDataTypeEditor(AttributeType.allTypes().toArray()));
        fieldNameColumn().setCellRenderer(
                new StripingRenderer(table.getDefaultRenderer(String.class)));
        dataTypeColumn().setCellRenderer(new StripingRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                return table.getDefaultRenderer(String.class).getTableCellRendererComponent(
                        table,
                        (value != null) ? capitalizeFirstLetter(value.toString()) : null,
                        isSelected,
                        hasFocus,
                        row,
                        column);
            }
        }));
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                for (int i = 0; i < table.getColumnCount(); i++) {
                    ((MyEditor) table.getColumnModel().getColumn(i).getCellEditor())
                            .cancelCellEditing();
                }
            }
        });
    }

    private String capitalizeFirstLetter(String string) {
        return string.toUpperCase().charAt(0) + string.toLowerCase().substring(1);
    }

    void jbInit() throws Exception {
        toolBar.setOrientation(JToolBar.VERTICAL);
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.setLayout(gridBagLayout2);
        jPanel1.setLayout(gridBagLayout1);
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setText(" ");
        applyButton.setText(I18N.get("ui.SchemaPanel.apply-changes"));
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyButton_actionPerformed(e);
            }
        });
        forceInvalidConversionsToNullCheckBox.setToolTipText(I18N.get("ui.SchemaPanel.leave-unchecked-if-you-want-to-be-notified-of-conversion-errors"));
        forceInvalidConversionsToNullCheckBox.setText(I18N.get("ui.SchemaPanel.force-invalid-conversions-to-null"));
        buttonPanel.setLayout(gridBagLayout3);
        buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        revertButton.setText(I18N.get("ui.SchemaPanel.revert-changes"));
        revertButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revertButton_actionPerformed(e);
            }
        });
        jPanel3.setLayout(borderLayout1);
        this.add(
                jPanel3,
                new GridBagConstraints(
                        0,
                        0,
                        1,
                        1,
                        1.0,
                        1.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
        jPanel3.add(toolBar, BorderLayout.WEST);
        jPanel3.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(table, null);
        this.add(
                jPanel1,
                new GridBagConstraints(
                        0,
                        3,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
        jPanel1.add(
                statusLabel,
                new GridBagConstraints(
                        0,
                        0,
                        1,
                        1,
                        1.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
        this.add(
                buttonPanel,
                new GridBagConstraints(
                        0,
                        2,
                        1,
                        1,
                        1.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
        buttonPanel.add(
                applyButton,
                new GridBagConstraints(
                        1,
                        1,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4),
                        0,
                        0));
        buttonPanel.add(
                forceInvalidConversionsToNullCheckBox,
                new GridBagConstraints(
                        4,
                        1,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
        buttonPanel.add(
                jPanel2,
                new GridBagConstraints(
                        3,
                        1,
                        1,
                        1,
                        1.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
        buttonPanel.add(
                revertButton,
                new GridBagConstraints(
                        2,
                        1,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 4),
                        0,
                        0));
    }

    private void reportError(String message) {
        if (message != null) {
            statusLabel.setText(message);
            statusLabel.setIcon(GUIUtil.toSmallIcon(IconLoader.icon("Delete.gif")));
        } else {
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }
    }

    private int geometryCount() {
        int geometryCount = 0;

        for (int i = 0; i < getModel().getRowCount(); i++) {
            if (getModel().get(i).getType() == AttributeType.GEOMETRY) {
                geometryCount++;
            }
        }

        return geometryCount;
    }

    public String validateInput() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            String error
                    = ((MyEditor) table.getColumnModel().getColumn(i).getCellEditor())
                    .getCurrentErrorMessage();

            if (error != null) {
                return error;
            }
        }

        if (geometryCount() > 1) {
            return I18N.get("ui.SchemaPanel.only-one-geometry-field-is-allowed");
        }

        if (geometryCount() == 0) {
            return I18N.get("ui.SchemaPanel.a-geometry-field-must-be-defined");
        }

        return null;
    }

    private String validate(int row, AttributeType type) {
        if (type == AttributeType.GEOMETRY) {
            for (int i = 0; i < getModel().getRowCount(); i++) {
                if (i == row) {
                    continue;
                }

                if (getModel().get(i).getType() == null) {
                    //One of the blank rows. [Bob Boseko]
                    continue;
                }
            }
        }

        return null;
    }

    private String validate(int row, String name) {
        if (name.trim().length() == 0) {
            return I18N.get("ui.SchemaPanel.field-name-cannot-be-blank");
        }

        //Existing fields are already trimmed. [Bob Boseko]
        for (int i = 0; i < getModel().getRowCount(); i++) {
            if (i == row) {
                continue;
            }

            if (getModel().get(i).getName() == null) {
                //One of the blank rows. [Bob Boseko]
                continue;
            }

            if (getModel().get(i).getName().equalsIgnoreCase(name.trim())) {
                return I18N.get("ui.SchemaPanel.field-name-already-exists") + ": " + name;
            }
        }

        return null;
    }

    private void markAsModified() {
        modified = true;
    }

    public void markAsUnmodified() {
        modified = false;
    }

    public JTable getTable() {
        return table;
    }

    void applyButton_actionPerformed(ActionEvent e) {
        fireActionPerformed();
    }

    public void add(ActionListener l) {
        listeners.add(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(null);
        }
    }

    public boolean isForcingInvalidConversionsToNull() {
        return forceInvalidConversionsToNullCheckBox.isSelected();
    }

    public void move(Collection fieldsToMove, int displacement) {
        //Use rows-to-act-on, not selected row, because no rows may be selected
        //i.e. we might just be operating on the row the user right-clicked on. [Bob Boseko]
        int guaranteedVisibleRow = displacement > 0 ? max(rowsToActOn()) : min(rowsToActOn());
        guaranteedVisibleRow += displacement;
        //Compute guaranteedVisibleRow before doing the move, because after the
        //move the selection would have moved, or if a row were merely clicked and not
        //selected, its click point would *not* have moved -- would tricky to compute it
        //after the move! [Bob Boseko]

        ArrayList selectedFields = new ArrayList();
        int[] selectedRows = table.getSelectedRows();

        for (int i = 0; i < selectedRows.length; i++) {
            selectedFields.add(getModel().get(selectedRows[i]));
        }

        getModel().move(fieldsToMove, displacement);
        table.clearSelection();

        for (Iterator i = selectedFields.iterator(); i.hasNext();) {
            SchemaTableModel.Field field = (SchemaTableModel.Field) i.next();
            table.addRowSelectionInterval(getModel().indexOf(field), getModel().indexOf(field));
        }
        Rectangle r = table.getCellRect(guaranteedVisibleRow, 0, true);
        table.scrollRectToVisible(r);
    }

    private int min(int[] ints) {
        int min = ints[0];
        for (int i = 0; i < ints.length; i++) {
            min = Math.min(min, ints[i]);
        }
        return min;
    }

    private int max(int[] ints) {
        int max = ints[0];
        for (int i = 0; i < ints.length; i++) {
            max = Math.max(max, ints[i]);
        }
        return max;
    }

    private class StripingRenderer implements TableCellRenderer {

        private TableCellRenderer originalRenderer;

        //Row-stripe colour recommended in
        //Java Look and Feel Design Guidelines: Advanced Topics [Bob Boseko]
        private final Color LIGHT_GRAY = new Color(230, 230, 230);

        public StripingRenderer(TableCellRenderer originalRenderer) {
            this.originalRenderer = originalRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            JComponent component
                    = (JComponent) originalRenderer.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column);

            //If not editable, use row striping, as recommended in
            //Java Look and Feel Design Guidelines: Advanced Topics [Bob Boseko]
            component.setOpaque(true);

            if (!isSelected) {
                component.setForeground(Color.black);
                component.setBackground(
                        (layer.isEditable() || ((row % 2) == 0)) ? Color.white : LIGHT_GRAY);
            }

            return component;
        }
    }

    public class MyDataTypeEditor extends MyEditor {

        private AttributeType originalType;

        public MyDataTypeEditor(Object[] items) {
            super(new JComboBox(items));

            final ListCellRenderer originalRenderer = comboBox().getRenderer();
            comboBox().setRenderer(new ListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {
                    return originalRenderer.getListCellRendererComponent(
                            list,
                            (value != null) ? capitalizeFirstLetter(value.toString()) : null,
                            index,
                            isSelected,
                            cellHasFocus);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {
            originalType = (AttributeType) value;
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        private JComboBox comboBox() {
            return (JComboBox) getComponent();
        }

        @Override
        public boolean stopCellEditing() {
            if (originalType != comboBox().getSelectedItem()) {
                markAsModified();
            }
            return super.stopCellEditing();
        }

        @Override
        protected String validate() {
            return SchemaPanel.this.validate(row, (AttributeType) comboBox().getSelectedItem());
        }
    }

    public abstract class MyEditor extends DefaultCellEditor {

        protected int row;
        private String currentErrorMessage = null;

        public MyEditor(JComboBox comboBox) {
            super(comboBox);
        }

        public MyEditor(JTextField textField) {
            super(textField);
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {
            this.row = row;
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));

            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public void cancelCellEditing() {
            currentErrorMessage = null;
            updateComponents();
            super.cancelCellEditing();
        }

        @Override
        public boolean stopCellEditing() {
            try {
                if (validate() != null) {
                    ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));

                    return false;
                }

                return super.stopCellEditing();
            } finally {
                //Can't just call #validate at the top of this method, because other validations
                //apply to when the edit is finished (e.g. checking the number of geometries). [Bob Boseko]
                currentErrorMessage = validate();
                updateComponents();
            }
        }

        protected abstract String validate();

        public String getCurrentErrorMessage() {
            return currentErrorMessage;
        }
    }

    public class MyFieldNameEditor extends MyEditor {

        public MyFieldNameEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            if (!textField().getText().equals(originalText)) {
                markAsModified();
            }
            return super.stopCellEditing();
        }

        private String originalText;

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {
            originalText = (String) value;
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        private JTextField textField() {
            return (JTextField) getComponent();
        }

        @Override
        protected String validate() {
            return SchemaPanel.this.validate(row, textField().getText());
        }
    }

    void revertButton_actionPerformed(ActionEvent e) {
        setModel(new SchemaTableModel(layer));
    }

    private void setCurrentClickPoint(Point currentClickPoint) {
        this.currentClickPoint = currentClickPoint;
    }

    private Point getCurrentClickPoint() {
        return currentClickPoint;
    }
}
