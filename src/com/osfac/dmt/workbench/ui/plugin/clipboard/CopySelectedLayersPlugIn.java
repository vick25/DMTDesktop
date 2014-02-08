package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.famfam.IconLoaderFamFam;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class CopySelectedLayersPlugIn extends LayerableClipboardPlugIn {
    //Note: Need to copy the data twice: once when the user hits Copy, so she is
    //free to modify the original afterwards, and again when the user hits Paste,
    //so she is free to modify the first copy then hit Paste again. [Bob Boseko]

    public CopySelectedLayersPlugIn() {
    }
    public static final ImageIcon ICON = IconLoaderFamFam.icon("layers_copy.gif");

    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "C", "&C", false);
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new CollectionOfLayerablesTransferable(
                clone((context.getLayerNamePanel()).selectedNodes(
                Layerable.class))), new DummyClipboardOwner());

        return true;
    }

    private Collection clone(Collection layerables) {
        ArrayList clones = new ArrayList();

        for (Iterator i = layerables.iterator(); i.hasNext();) {
            Layerable layerable = (Layerable) i.next();

            if (!(layerable instanceof Layer || layerable instanceof WMSLayer)) {
                continue;
            }

            clones.add(cloneLayerable(layerable));
        }

        return clones;
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayerablesMustBeSelectedCheck(
                1, Layerable.class));
    }
}
