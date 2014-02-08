package com.osfac.dmt.workbench.ui.plugin;

import com.vividsolutions.jts.util.Assert;
import com.osfac.dmt.I18N;
import com.osfac.dmt.util.OrderedMap;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;

public class RemoveSelectedCategoriesPlugIn extends AbstractPlugIn {

    public RemoveSelectedCategoriesPlugIn() {
    }

    private Category pickUnselectedCategory(LayerNamePanel layerNamePanel,
            LayerManager layerManager) {
        Collection selectedCategories = layerNamePanel.getSelectedCategories();
        Category workingCategory = layerManager.getCategory(StandardCategoryNames.WORKING);

        if ((workingCategory != null)
                && !selectedCategories.contains(workingCategory)) {
            return workingCategory;
        }

        for (Iterator i = layerManager.getCategories().iterator(); i.hasNext();) {
            Category category = (Category) i.next();

            if (!selectedCategories.contains(category)) {
                return category;
            }
        }

        return null;
    }

    public boolean execute(PlugInContext context) throws Exception {
        execute(toCategorySpecToLayerablesMap(toOrderedCategories(
                context.getLayerNamePanel().getSelectedCategories())),
                pickUnselectedCategory(context.getLayerNamePanel(),
                context.getLayerManager()), context);

        return true;
    }

    private List toOrderedCategories(Collection unorderedCategories) {
        ArrayList orderedCategories = new ArrayList(unorderedCategories);
        Collections.sort(orderedCategories,
                new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Category c1 = (Category) o1;
                        Category c2 = (Category) o2;

                        return new Integer(c1.getLayerManager().indexOf(c1)).compareTo(new Integer(
                                c2.getLayerManager().indexOf(c2)));
                    }
                });

        return orderedCategories;
    }

    private OrderedMap toCategorySpecToLayerablesMap(
            List selectedCategoriesInOrder) {
        //Need OrderedMap so that categories get re-inserted in the correct order.
        //[Bob Boseko]
        OrderedMap map = new OrderedMap();

        for (Iterator i = selectedCategoriesInOrder.iterator(); i.hasNext();) {
            Category category = (Category) i.next();

            //new ArrayList because #getLayers returns a view of the category's
            //layers, which will be cleared. [Bob Boseko]
            map.put(new CategorySpec(category.getName(),
                    category.getLayerManager().indexOf(category)),
                    new ArrayList(category.getLayerables()));
        }

        return map;
    }

    private void execute(final OrderedMap originalCategorySpecToLayerablesMap,
            final Category newCategory, final PlugInContext context)
            throws Exception {
        execute(new UndoableCommand(getName()) {
            public void execute() {
                for (Iterator i = originalCategorySpecToLayerablesMap.keyList()
                        .iterator();
                        i.hasNext();) {
                    final CategorySpec originalCategorySpec = (CategorySpec) i.next();
                    List layers = (List) originalCategorySpecToLayerablesMap.get(originalCategorySpec);

                    for (Iterator j = layers.iterator(); j.hasNext();) {
                        final Layerable layerable = (Layerable) j.next();
                        context.getLayerManager().remove(layerable);
                        context.getLayerManager().addLayerable(newCategory.getName(),
                                layerable);
                    }

                    context.getLayerManager().removeIfEmpty(context.getLayerManager()
                            .getCategory(originalCategorySpec.name));
                }
            }

            public void unexecute() {
                for (Iterator i = originalCategorySpecToLayerablesMap.keyList()
                        .iterator();
                        i.hasNext();) {
                    final CategorySpec originalCategorySpec = (CategorySpec) i.next();
                    List layers = (List) originalCategorySpecToLayerablesMap.get(originalCategorySpec);
                    Assert.isTrue(null == context.getLayerManager()
                            .getCategory(originalCategorySpec.name));
                    context.getLayerManager().addCategory(originalCategorySpec.name,
                            originalCategorySpec.index);

                    for (Iterator j = layers.iterator(); j.hasNext();) {
                        final Layerable layerable = (Layerable) j.next();
                        Assert.isTrue(context.getLayerManager().getCategory(layerable) == newCategory);
                        context.getLayerManager().remove(layerable);
                        context.getLayerManager().addLayerable(originalCategorySpec.name,
                                layerable);
                    }
                }
            }
        }, context);
    }

    public MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNCategoriesMustBeSelectedCheck(
                1)).add(new EnableCheck() {
            public String check(JComponent component) {
                return (pickUnselectedCategory(workbenchContext.getLayerNamePanel(),
                        workbenchContext.getLayerManager()) == null)
                        ? I18N.get("ui.plugin.RemoveSelectedCategoriesPlugIn.at-least-1-category-must-be-left-unselected") : null;
            }
        });
    }

    private static class CategorySpec {

        private int index;
        private String name;

        public CategorySpec(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }
}
