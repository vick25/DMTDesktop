package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.InputChangedListener;
import com.osfac.dmt.workbench.ui.wizard.WizardPanel;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OneSRSWizardPanel extends JPanel implements WizardPanel {

    private Map dataMap;
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel srsLabel = new JLabel();
    private JPanel fillerPanel = new JPanel();
    private JTextField textField = new JTextField();

    public OneSRSWizardPanel() {
        try {
            jbInit();
            textField.setFont(new JLabel().getFont());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(InputChangedListener listener) {
    }

    public void remove(InputChangedListener listener) {
    }

    public String getInstructions() {
        return I18N.get("ui.plugin.wms.OneSRSWizardPanel.the-layers-you-have-chosen-support-only-one-coordinate-system");
    }

    void jbInit() throws Exception {
        srsLabel.setText(I18N.get("ui.plugin.wms.OneSRSWizardPanel.select-coordinate-reference-system"));
        this.setLayout(gridBagLayout1);
        textField.setEnabled(false);
        textField.setOpaque(false);
        textField.setPreferredSize(new Dimension(125, 21));
        textField.setDisabledTextColor(Color.black);
        textField.setEditable(false);
        textField.setText("jTextField1");
        this.add(srsLabel,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 4), 0, 0));
        this.add(fillerPanel,
                new GridBagConstraints(2, 10, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(textField,
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    public void exitingToRight() {
    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;

        List commonSRSList = (List) dataMap.get(MapLayerWizardPanel.COMMON_SRS_LIST_KEY);
        Assert.isTrue(commonSRSList.size() == 1);
        String srs = (String) commonSRSList.get(0);
        dataMap.put(SRSWizardPanel.SRS_KEY, srs);

        String stringToShow = SRSUtils.getName(srs);
        textField.setText(stringToShow);
    }

    public String getTitle() {
        return I18N.get("ui.plugin.wms.OneSRSWizardPanel.select-coordinate-reference-system");
    }

    public String getID() {
        return getClass().getName();
    }

    public boolean isInputValid() {
        return true;
    }

    public String getNextID() {
        return null;
    }
}
