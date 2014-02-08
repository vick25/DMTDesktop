package com.osfac.dmt.workbench.ui.plugin.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public abstract class AbstractTransferable implements Transferable {

    private final DataFlavor[] flavors;

    public AbstractTransferable(DataFlavor[] flavors) {
        this.flavors = flavors;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }

        return false;
    }

    public abstract Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException;
}
