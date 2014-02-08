package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.feature.Feature;
import com.vividsolutions.jts.io.WKTWriter;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class CollectionOfFeaturesTransferable extends AbstractTransferable {

    /**
     * A java.util.Collection, not a FeatureCollection
     */
    public static final DataFlavor COLLECTION_OF_FEATURES_FLAVOR =
            new DataFlavor(Collection.class, "Collection of Features") {
                public boolean equals(DataFlavor that) {
                    //Needed so #equals will return false for COLLECTION_OF_LAYERS_FLAVOR. [Bob Boseko]
                    return super.equals(that)
                            && getHumanPresentableName().equals(that.getHumanPresentableName());
                }
            };
    private static final DataFlavor[] flavors = {
        DataFlavor.stringFlavor,
        //plainTextFlavor is deprecated, but JDK 1.3 needs it to paste to
        //non-Java apps (like Notepad). [Bob Boseko]
        //DataFlavor.plainTextFlavor,
        COLLECTION_OF_FEATURES_FLAVOR};
    private Collection features;
    private WKTWriter writer = new WKTWriter(3);

    public CollectionOfFeaturesTransferable(Collection features) {
        super(flavors);
        this.features = new ArrayList(features);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(COLLECTION_OF_FEATURES_FLAVOR)) {
            return Collections.unmodifiableCollection(features);
        }
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return toString(features);
        }
        //if (flavor.equals(DataFlavor.plainTextFlavor)) {
        //    return new StringReader(toString(features));
        //}
        throw new UnsupportedFlavorException(flavor);
    }

    private String toString(Collection features) {
        StringBuilder b = new StringBuilder();
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            //Not System.getProperty("line.separator"); otherwise, when you copy
            //into, say, Notepad, you get garbage characters at the end of each line 
            //(\r\r\n). [Bob Boseko]
            b.append(writer.writeFormatted(feature.getGeometry())).append("\n\n");
        }
        return b.toString();
    }
}
