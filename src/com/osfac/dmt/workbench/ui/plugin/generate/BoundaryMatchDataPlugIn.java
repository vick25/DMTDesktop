package com.osfac.dmt.workbench.ui.plugin.generate;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.vividsolutions.jts.geom.Coordinate;
import javax.swing.ImageIcon;

/**
 * Creates two polygon-grid layers that interlock with sinusoidal "teeth".
 */
public class BoundaryMatchDataPlugIn extends AbstractPlugIn {

    private BoundaryMatchDataEngine engine = new BoundaryMatchDataEngine();

    public BoundaryMatchDataPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addLayerViewMenuItem(
                this,
                new String[]{I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.tools"),
                    I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.generate")},
                getName() + "...");
    }

    public boolean execute(PlugInContext context) throws Exception {
        MultiInputDialog dialog =
                new MultiInputDialog(context.getWorkbenchFrame(),
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.generate-boundary-match-data"), true);
        setDialogValues(dialog);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        getDialogValues(dialog);
        engine.execute(context);

        return true;
    }

    private void setDialogValues(MultiInputDialog dialog) {
        dialog.setTitle(I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.generate-boundary-match-data"));
        dialog.setSideBarImage(new ImageIcon(getClass().getResource("GenerateBdyMatchData.gif")));
        dialog.setSideBarDescription(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.generates-two-sample-datasets-containing-random-boundary-perturbations"));

        //<<TODO>> Add the concept of pluggable validators to MultiInputDialog. [Bob Boseko]
        dialog.addPositiveIntegerField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.layer-width-cells"),
                engine.getLayerWidthInCells(),
                5);
        dialog.addPositiveIntegerField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.layer-height-cells"),
                engine.getLayerHeightInCells(),
                5);
        dialog.addPositiveDoubleField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.cells-side-length"),
                engine.getCellSideLength(),
                5);
        dialog.addPositiveIntegerField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.vertices-per-cell-side"),
                engine.getVerticesPerCellSide(),
                5);
        dialog.addPositiveIntegerField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.vertices-per-boundary-side"),
                engine.getVerticesPerBoundarySide(),
                5);
        dialog.addNonNegativeDoubleField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.boundary-amplitude"),
                engine.getBoundaryAmplitude(),
                5);
        dialog.addPositiveDoubleField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.boundary-period"),
                engine.getBoundaryPeriod(),
                5);
        dialog.addNonNegativeDoubleField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.max-boundary-perturbation"),
                engine.getMaxBoundaryPerturbation(),
                5);
        dialog.addNonNegativeDoubleField(
                I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.perturbation-probability"),
                engine.getPerturbationProbability(),
                5);
        dialog.addDoubleField("Min X", engine.getSouthwestCornerOfLeftLayer().x, 5);
        dialog.addDoubleField("Min Y", engine.getSouthwestCornerOfLeftLayer().y, 5);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        engine.setSouthwestCornerOfLeftLayer(
                new Coordinate(dialog.getDouble(("Min X")), dialog.getDouble(("Min Y"))));
        engine.setLayerHeightInCells(dialog.getInteger((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.layer-height-cells"))));
        engine.setLayerWidthInCells(dialog.getInteger((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.layer-width-cells"))));
        engine.setCellSideLength(dialog.getDouble((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.cells-side-length"))));
        engine.setVerticesPerCellSide(dialog.getInteger((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.vertices-per-cell-side"))));
        engine.setBoundaryAmplitude(dialog.getDouble((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.boundary-amplitude"))));
        engine.setBoundaryPeriod(dialog.getDouble((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.boundary-period"))));
        engine.setVerticesPerBoundarySide(dialog.getInteger((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.vertices-per-boundary-side"))));
        engine.setMaxBoundaryPerturbation(dialog.getDouble((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.max-boundary-perturbation"))));
        engine.setPerturbationProbability(dialog.getDouble((I18N.get("ui.plugin.generate.BoundaryMatchDataPlugIn.perturbation-probability"))));
    }
}
