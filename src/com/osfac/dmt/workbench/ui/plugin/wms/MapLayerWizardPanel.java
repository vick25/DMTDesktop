package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchException;
import com.osfac.dmt.workbench.ui.InputChangedListener;
import com.osfac.dmt.workbench.ui.wizard.WizardPanel;
import com.osfac.wms.WMService;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Map;
import javax.swing.JPanel;

public class MapLayerWizardPanel extends JPanel implements WizardPanel {

    public static final String LAYERS_KEY = "LAYERS";
    public static final String COMMON_SRS_LIST_KEY = "COMMON_SRS_LIST";
    public static final String FORMAT_LIST_KEY = "FORMAT_LIST";
    public final static String INITIAL_LAYER_NAMES_KEY = "INITIAL_LAYER_NAMES";
    public final static String NO_COMMON_SRS_MESSAGE = I18N.get("ui.plugin.wms.MapLayerWizardPanel.the-chosen-layers-do-not-have-a-common-epsg-coordinate-reference-system");
    private MapLayerPanel addRemovePanel = new MapLayerPanel();
    private Map dataMap;
    private String nextID = SRSWizardPanel.class.getName();
    private BorderLayout borderLayout1 = new BorderLayout();

    public MapLayerWizardPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(InputChangedListener listener) {
        addRemovePanel.add(listener);
    }

    public void remove(InputChangedListener listener) {
        addRemovePanel.remove(listener);
    }

    public String getInstructions() {
        //    return "Please choose the WMS layers that should appear on the image. You " +
        //    "can change the ordering of the WMS layers using the up and down buttons.";
        return I18N.get("ui.plugin.wms.MapLayerWizardPanel.please-choose-the-wms-layers-that-should-appear-on-the-image");
    }

    public void exitingToRight() throws WorkbenchException {
        dataMap.put(LAYERS_KEY, addRemovePanel.getChosenMapLayers());

        if (addRemovePanel.commonSRSList().isEmpty()) {
            throw new WorkbenchException(NO_COMMON_SRS_MESSAGE);
        }

        dataMap.put(COMMON_SRS_LIST_KEY, addRemovePanel.commonSRSList());
        if (addRemovePanel.commonSRSList().size() == 1) {
            nextID = OneSRSWizardPanel.class.getName();
        } else {
            nextID = SRSWizardPanel.class.getName();
        }
    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;
        addRemovePanel.init((WMService) dataMap.get(URLWizardPanel.SERVICE_KEY),
                (Collection) dataMap.get(INITIAL_LAYER_NAMES_KEY));
    }

    public String getTitle() {
        return I18N.get("ui.plugin.wms.MapLayerWizardPanel.choose-wms-layers");
    }

    public String getID() {
        return getClass().getName();
    }

    public boolean isInputValid() {
        return !addRemovePanel.getChosenMapLayers().isEmpty();
    }

    public String getNextID() {
        return nextID;
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        add(addRemovePanel, BorderLayout.CENTER);
    }
}
