package com.osfac.dmt.workbench.ui;

import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingOptionsDialog;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.ui.cursortool.editing.SnapVerticesOp;
import com.vividsolutions.jts.util.Assert;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class SnapVerticesToolsOptionsPanel extends JPanel{

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JCheckBox insertVerticesCheckBox = new JCheckBox();
    private JPanel jPanel2 = new JPanel();
    private Blackboard blackboard;
    boolean start = false;

    public SnapVerticesToolsOptionsPanel(Blackboard blackboard) {
        this.blackboard = blackboard;

        try {
            jbInit();
            init();
        } catch (Exception e) {
            Assert.shouldNeverReachHere(e.toString());
        }
    }

    public String validateInput() {
        return null;
    }

    public void okPressed() {
        blackboard.put(SnapVerticesOp.INSERT_VERTICES_IF_NECESSARY_KEY, insertVerticesCheckBox.isSelected());
    }

    public final void init() {
        insertVerticesCheckBox.setSelected(blackboard.get(SnapVerticesOp.INSERT_VERTICES_IF_NECESSARY_KEY, true));
        start = true;
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        jPanel1.setLayout(gridBagLayout1);
        insertVerticesCheckBox.setText(I18N.get("ui.SnapVerticeToolsOptionsPanel.insert-vertex-if-none-in-segment"));
        insertVerticesCheckBox.setFocusable(false);
        insertVerticesCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (start) {
                    SettingOptionsDialog.page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
                }
            }
        });
        this.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(
                insertVerticesCheckBox,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        jPanel1.add(
                jPanel2,
                new GridBagConstraints(
                1,
                1,
                1,
                1,
                1.0,
                1.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0,
                0));
    }
}
