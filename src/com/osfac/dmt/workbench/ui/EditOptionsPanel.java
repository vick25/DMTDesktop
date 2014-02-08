package com.osfac.dmt.workbench.ui;

import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingOptionsDialog;
import com.osfac.dmt.util.Blackboard;
import com.vividsolutions.jts.util.Assert;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

/**
 * Implements an {@link OptionsPanel} for Edit.
 */
public class EditOptionsPanel extends JPanel {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JCheckBox preventEditsCheckBox = new JCheckBox();
    private JPanel jPanel2 = new JPanel();
    private Blackboard blackboard;
    private JDesktopPane desktopPane;
    boolean start = false;

    public EditOptionsPanel(final Blackboard blackboard,
            JDesktopPane desktopPane) {
        this.blackboard = blackboard;
        this.desktopPane = desktopPane;
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
        blackboard.put(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, preventEditsCheckBox.isSelected());
    }

    public final void init() {
        preventEditsCheckBox.setSelected(blackboard.get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false));
        start = true;
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        jPanel1.setLayout(gridBagLayout1);
        preventEditsCheckBox.setText(I18N.get("ui.EditOptionsPanel.prevent-edits-resulting-in-invalid-geometries"));
        preventEditsCheckBox.setFocusable(false);
        preventEditsCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (start) {
                    SettingOptionsDialog.page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
                }
            }
        });
        this.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(
                preventEditsCheckBox,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 4, 0), 0, 0));
        jPanel1.add(
                jPanel2,
                new GridBagConstraints(
                100,
                100,
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
