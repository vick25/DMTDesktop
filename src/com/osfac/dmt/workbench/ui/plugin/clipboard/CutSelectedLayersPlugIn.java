package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.MacroPlugIn;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.ui.images.famfam.IconLoaderFamFam;
import com.osfac.dmt.workbench.ui.plugin.RemoveSelectedLayersPlugIn;
import javax.swing.ImageIcon;

public class CutSelectedLayersPlugIn extends MacroPlugIn {

    public CutSelectedLayersPlugIn() {
        super(new PlugIn[]{
                    new CopySelectedLayersPlugIn(), new RemoveSelectedLayersPlugIn()
                });
    }
    public static final ImageIcon ICON = IconLoaderFamFam.icon("layers_cut.gif");

    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "t", "&t", false);
    }

    public String getName() {
        return AbstractPlugIn.createName(getClass());
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        return new MultiEnableCheck().add(((CopySelectedLayersPlugIn) plugIns[0]).createEnableCheck(
                workbenchContext)).add(((RemoveSelectedLayersPlugIn) plugIns[1]).createEnableCheck(
                workbenchContext));
    }
}
