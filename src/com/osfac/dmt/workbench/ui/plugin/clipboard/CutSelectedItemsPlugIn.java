package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.MacroPlugIn;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.ui.images.famfam.IconLoaderFamFam;
import com.osfac.dmt.workbench.ui.plugin.DeleteSelectedItemsPlugIn;
import javax.swing.ImageIcon;

public class CutSelectedItemsPlugIn extends MacroPlugIn {

    public CutSelectedItemsPlugIn() {
        super(new PlugIn[]{
                    new CopySelectedItemsPlugIn(),
                    new DeleteSelectedItemsPlugIn()
                });
    }

    public String getName() {
        return AbstractPlugIn.createName(getClass());
    }

    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "t", "&t", false);
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        return DeleteSelectedItemsPlugIn.createEnableCheck(workbenchContext);
    }
    public static final ImageIcon ICON = IconLoaderFamFam.icon("cut.gif");
}
