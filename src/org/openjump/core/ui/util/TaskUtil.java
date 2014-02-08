package org.openjump.core.ui.util;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import java.util.Collection;
import java.util.List;

public class TaskUtil {

    public static Category getSelectedCategoryName(WorkbenchContext workbenchContext) {
        LayerNamePanel layerNamePanel = workbenchContext.getLayerNamePanel();
        Collection<Category> selectedCategories = layerNamePanel.getSelectedCategories();
        if (selectedCategories.isEmpty()) {
            LayerManager layerManager = layerNamePanel.getLayerManager();
            List<Category> categories = layerManager.getCategories();
            return categories.get(0);
        } else {
            return selectedCategories.iterator().next();
        }

    }
}
