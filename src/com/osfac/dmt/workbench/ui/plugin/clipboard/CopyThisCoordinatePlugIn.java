package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.vividsolutions.jts.geom.Coordinate;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CopyThisCoordinatePlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        final Coordinate c =
                context.getLayerViewPanel().getViewport().toModelCoordinate(
                context.getLayerViewPanel().getLastClickedPoint());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new AbstractTransferable(
                new DataFlavor[]{DataFlavor.plainTextFlavor}) {
                    public Object getTransferData(DataFlavor flavor)
                            throws UnsupportedFlavorException, IOException {
                        return c.x + ", " + c.y;
                    }
                }, new DummyClipboardOwner());

        return true;
    }

    public static MultiEnableCheck createEnableCheck(
            WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory =
                new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }
}
