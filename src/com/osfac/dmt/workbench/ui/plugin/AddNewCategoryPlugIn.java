package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.vividsolutions.jts.util.Assert;

public class AddNewCategoryPlugIn extends AbstractPlugIn {

    private final static String NEW_CATEGORY_NAME = I18N.get("ui.plugin.AbstractNewCategoryPlugIn.new-category");

    public AddNewCategoryPlugIn() {
    }

    public boolean execute(final PlugInContext context)
            throws Exception {
        final String categoryName = findNewCategoryName(context.getLayerManager());
        execute(new UndoableCommand(getName()) {
            public void execute() {
                context.getLayerManager().addCategory(categoryName);
            }

            public void unexecute() {
                Assert.isTrue(context.getLayerManager()
                        .getCategory(categoryName).isEmpty(),
                        I18N.get("ui.plugin.AbstractNewCategoryPlugIn.this-can-happen-when-a-plug-in-calls"));
                context.getLayerManager().removeIfEmpty(context.getLayerManager()
                        .getCategory(categoryName));
            }
        }, context);

        return true;
    }

    private String findNewCategoryName(LayerManager layerManager) {
        if (layerManager.getCategory(NEW_CATEGORY_NAME) == null) {
            return NEW_CATEGORY_NAME;
        }

        int i = 2;
        String newName;

        do {
            newName = NEW_CATEGORY_NAME + " (" + i + ")";
            i++;
        } while (layerManager.getCategory(newName) != null);

        return newName;
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }
}
