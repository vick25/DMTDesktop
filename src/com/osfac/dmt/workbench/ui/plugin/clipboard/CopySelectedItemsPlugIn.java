package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.SelectionManagerProxy;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import org.openjump.core.ui.images.IconLoader;

public class CopySelectedItemsPlugIn extends AbstractPlugIn {
    //Note: Need to copy the data twice: once when the user hits Copy, so she is
    //free to modify the original afterwards, and again when the user hits Paste,
    //so she is free to modify the first copy then hit Paste again. [Bob Boseko]

    public CopySelectedItemsPlugIn() {
    }
    public static ImageIcon ICON = IconLoader.icon("items_copy.png");

    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "C", "&C", false);
    }

    public boolean execute(PlugInContext context) throws Exception {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new CollectionOfFeaturesTransferable(
                ((SelectionManagerProxy) context.getActiveInternalFrame()).getSelectionManager().createFeaturesFromSelectedItems()), new DummyClipboardOwner());
        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
