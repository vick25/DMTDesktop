package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.InputChangedFirer;
import com.osfac.dmt.workbench.ui.InputChangedListener;
import static com.osfac.dmt.workbench.ui.plugin.wms.MapLayerWizardPanel.COMMON_SRS_LIST_KEY;
import static com.osfac.dmt.workbench.ui.plugin.wms.MapLayerWizardPanel.FORMAT_LIST_KEY;
import com.osfac.dmt.workbench.ui.wizard.WizardPanel;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.WEST;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SRSWizardPanel extends JPanel implements WizardPanel {

    public static final String SRS_KEY = "SRS";
    private InputChangedFirer inputChangedFirer = new InputChangedFirer();
    private Map dataMap;
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel srsLabel = new JLabel();
    private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
    private DefaultComboBoxModel formatBoxModel = new DefaultComboBoxModel();
    private JComboBox comboBox = new JComboBox();
    private JLabel formatLabel;
    private JComboBox formatBox;

    public SRSWizardPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(InputChangedListener listener) {
        inputChangedFirer.add(listener);
    }

    public void remove(InputChangedListener listener) {
        inputChangedFirer.remove(listener);
    }

    public String getInstructions() {
        return I18N.get("ui.plugin.wms.SRSWizardPanel.the-layers-you-chosen-support-more-than-one-coordinate-reference");
    }

    void jbInit() throws Exception {
        srsLabel.setText(I18N.get("ui.plugin.wms.SRSWizardPanel.coordinate-reference-system"));
        formatLabel = new JLabel(I18N.get("ui.plugin.wms.SRSWizardPanel.image-format"));
        formatBox = new JComboBox();
        this.setLayout(gridBagLayout1);
        GridBagConstraints gb = new GridBagConstraints();
        gb.anchor = WEST;
        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(5, 5, 5, 5);
        add(srsLabel, gb);
        ++gb.gridx;
        add(comboBox, gb);

        ++gb.gridy;
        gb.gridx = 0;

        add(formatLabel, gb);
        ++gb.gridx;
        add(formatBox, gb);
    }

    public void exitingToRight() {
        int index = comboBox.getSelectedIndex();
        String srsCode = (String) getCommonSrsList().get(index);
        dataMap.put(SRS_KEY, srsCode);
        dataMap.put(URLWizardPanel.FORMAT_KEY, formatBox.getSelectedItem());
    }

    private List getCommonSrsList() {
        return (List) dataMap.get(COMMON_SRS_LIST_KEY);
    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;

        for (Iterator i = getCommonSrsList().iterator(); i.hasNext();) {
            String srs = (String) i.next();
            String srsName = SRSUtils.getName(srs);
            comboBoxModel.addElement(srsName);
        }
        comboBox.setModel(comboBoxModel);

        String[] formats = (String[]) dataMap.get(FORMAT_LIST_KEY);
        formatBoxModel.removeAllElements();
        for (String f : formats) {
            formatBoxModel.addElement(f);
        }
        formatBox.setModel(formatBoxModel);
    }

    public String getTitle() {
        return I18N.get("ui.plugin.wms.SRSWizardPanel.select-coordinate-reference-system");
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
