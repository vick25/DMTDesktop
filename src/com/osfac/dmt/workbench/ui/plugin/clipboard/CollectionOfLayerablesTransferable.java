package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.workbench.model.Layerable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionOfLayerablesTransferable extends AbstractTransferable {

    public static final DataFlavor COLLECTION_OF_LAYERABLES_FLAVOR = new DataFlavor(Collection.class,
            "Collection of Layerables") {
        public boolean equals(DataFlavor that) {
            //Needed so #equals will return false for COLLECTION_OF_FEATURES_FLAVOR. [Bob Boseko]
            return super.equals(that)
                    && getHumanPresentableName().equals(that.getHumanPresentableName());
        }
    };
    private static final DataFlavor[] flavors = {
        DataFlavor.stringFlavor,
        //plainTextFlavor is deprecated, but JDK 1.3 needs it to paste to
        //non-Java apps (like Notepad). [Bob Boseko]
        DataFlavor.plainTextFlavor, COLLECTION_OF_LAYERABLES_FLAVOR
    };
    private Collection layerables;

    public CollectionOfLayerablesTransferable(Collection layerables) {
        super(flavors);
        this.layerables = new ArrayList(layerables);
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (flavor.equals(COLLECTION_OF_LAYERABLES_FLAVOR)) {
            return Collections.unmodifiableCollection(layerables);
        }

        if (flavor.equals(DataFlavor.stringFlavor)) {
            return toString(new ArrayList(layerables));
        }

        if (flavor.equals(DataFlavor.plainTextFlavor)) {
            return new StringReader(toString(new ArrayList(layerables)));
        }

        throw new UnsupportedFlavorException(flavor);
    }

    private String toString(List layerables) {
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < layerables.size(); i++) {
            Layerable layerable = (Layerable) layerables.get(i);

            if (i != 0) {
                b.append(", ");
            }

            b.append(layerable.getName());
        }

        return b.toString();
    }
}
