package com.osfac.dmt.workbench.plugin;

import static com.osfac.dmt.I18N.get;
import static com.osfac.dmt.I18N.getMessage;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelProxy;
import com.osfac.dmt.workbench.ui.SelectionManagerProxy;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.TaskFrameProxy;
import com.osfac.dmt.workbench.ui.warp.WarpingVectorLayerFinder;
import com.vividsolutions.jts.util.Assert;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 * Creates basic EnableChecks.
 *
 * @see EnableCheck
 */
public class EnableCheckFactory {

    WorkbenchContext workbenchContext;

    public EnableCheckFactory(WorkbenchContext workbenchContext) {
        Assert.isTrue(workbenchContext != null);
        this.workbenchContext = workbenchContext;
    }

    //<<TODO:WORKAROUND>> I came across a bug in the JBuilder 4 compiler (bcj.exe)
    //that occurs when using the Java ternary operator ( ? : ). For it to
    //happen, [1] the middle operand must be null and [2] an inner class
    //must be nearby. For example, try using JBuilder to compile the following code:
    //
    //  import java.awt.event.WindowAdapter;
    //  public class TestClass {
    //    static public void main(String[] args) {
    //      System.out.println(true ? null : "FALSE");
    //      WindowAdapter w = new WindowAdapter() { };
    //    }
    //  }
    //
    //You'd expect it to print out "null", but "FALSE" is printed! And if you comment
    //out the line with the anonymous inner class (WindowAdapter w), it prints out
    //"null" as expected! I've submitted a bug report to Borland (case number 488569).
    //
    //So, if you're using JBuilder, don't use ?: if the middle operand could be null!
    //[Bob Boseko]
    public EnableCheck createTaskWindowMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (!(workbenchContext.getWorkbench().getFrame().getActiveInternalFrame() instanceof TaskFrame))
                        ? get("com.osfac.dmt.workbench.plugin.A-Task-Window-must-be-active")
                        : null;
            }
        };
    }

    public EnableCheck createWindowWithSelectionManagerMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (!(workbenchContext.getWorkbench().getFrame().getActiveInternalFrame() instanceof SelectionManagerProxy))
                        ? get("com.osfac.dmt.workbench.plugin.A-window-with-a-selection-manager-must-be-active")
                        : null;
            }
        };
    }

    public EnableCheck createWindowWithLayerManagerMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (!(workbenchContext.getWorkbench().getFrame().getActiveInternalFrame() instanceof LayerManagerProxy))
                        ? get("com.osfac.dmt.workbench.plugin.A-window-with-a-layer-manager-must-be-active")
                        : null;
            }
        };
    }

    public EnableCheck createWindowWithAssociatedTaskFrameMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (!(workbenchContext.getWorkbench().getFrame().getActiveInternalFrame() instanceof TaskFrameProxy))
                        ? get("com.osfac.dmt.workbench.plugin.A-window-with-an-associated-task-frame-must-be-active")
                        : null;
            }
        };
    }

    public EnableCheck createWindowWithLayerNamePanelMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (!(workbenchContext.getWorkbench().getFrame().getActiveInternalFrame() instanceof LayerNamePanelProxy))
                        ? get("com.osfac.dmt.workbench.plugin.A-window-with-a-layer-name-panel-must-be-active")
                        : null;
            }
        };
    }

    public EnableCheck createWindowWithLayerViewPanelMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (!(workbenchContext.getWorkbench().getFrame().getActiveInternalFrame() instanceof LayerViewPanelProxy))
                        ? get("com.osfac.dmt.workbench.plugin.A-window-with-a-layer-view-panel-must-be-active")
                        : null;
            }
        };
    }

    public EnableCheck createOnlyOneLayerMayHaveSelectedFeaturesCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                Collection layersWithSelectedFeatures =
                        ((SelectionManagerProxy) workbenchContext
                        .getWorkbench()
                        .getFrame()
                        .getActiveInternalFrame())
                        .getSelectionManager()
                        .getFeatureSelection()
                        .getLayersWithSelectedItems();

                return (layersWithSelectedFeatures.size() > 1)
                        ? get("com.osfac.dmt.workbench.plugin.Only-one-layer-may-have-selected-features")
                        : null;
            }
        };
    }

    public EnableCheck createOnlyOneLayerMayHaveSelectedItemsCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                Collection layersWithSelectedItems =
                        ((SelectionManagerProxy) workbenchContext
                        .getWorkbench()
                        .getFrame()
                        .getActiveInternalFrame())
                        .getSelectionManager()
                        .getLayersWithSelectedItems();
                return (layersWithSelectedItems.size() > 1)
                        ? get("com.osfac.dmt.workbench.plugin.Only-one-layer-may-have-selected-items")
                        : null;
            }
        };
    }

    public EnableCheck createSelectedItemsLayersMustBeEditableCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                for (Iterator i =
                        ((SelectionManagerProxy) workbenchContext
                        .getWorkbench()
                        .getFrame()
                        .getActiveInternalFrame())
                        .getSelectionManager()
                        .getLayersWithSelectedItems()
                        .iterator();
                        i.hasNext();) {
                    Layer layer = (Layer) i.next();

                    if (!layer.isEditable()) {
                        return getMessage(
                                "com.osfac.dmt.workbench.plugin.Selected-items-layers-must-be-editable",
                                new String[]{layer.getName()});
                    }
                }

                return null;
            }
        };
    }

    public EnableCheck createExactlyNCategoriesMustBeSelectedCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-category-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-categories-must-be-selected",
                            new Object[]{n});
                }
                return (n != workbenchContext.getLayerNamePanel()
                        .getSelectedCategories().size()) ? msg : null;
            }
        };
    }

    public EnableCheck createExactlyNLayerablesMustBeSelectedCheck(
            final int n,
            final Class layerableClass) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-layer-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-layers-must-be-selected",
                            new Object[]{n});
                }
                return (n != (workbenchContext.getLayerNamePanel())
                        .selectedNodes(layerableClass).size()) ? msg : null;
            }
        };
    }

    public EnableCheck createExactlyNLayersMustBeSelectedCheck(final int n) {
        return createExactlyNLayerablesMustBeSelectedCheck(n, Layer.class);
    }

    public EnableCheck createAtLeastNCategoriesMustBeSelectedCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-category-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-categories-must-be-selected",
                            new Object[]{n});
                }
                return (n > workbenchContext.getLayerNamePanel()
                        .getSelectedCategories().size()) ? msg : null;
            }
        };
    }

    public EnableCheck createAtLeastNLayerablesMustBeSelectedCheck(
            final int n,
            final Class layerableClass) {
        return new EnableCheck() {
            public String check(JComponent component) {
                LayerNamePanel layerNamePanel = workbenchContext
                        .getLayerNamePanel();
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-layer-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-layers-must-be-selected",
                            new Object[]{n});
                }
                return (layerNamePanel == null || n > (workbenchContext
                        .getLayerNamePanel()).selectedNodes(layerableClass)
                        .size()) ? msg : null;
            }
        };
    }

    public EnableCheck createAtLeastNLayersMustBeSelectedCheck(final int n) {
        return createAtLeastNLayerablesMustBeSelectedCheck(n, Layer.class);
    }

    public EnableCheck createAtLeastNLayersMustBeEditableCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-layer-must-be-editable");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-layers-must-be-editable",
                            new Object[]{n});
                }
                return (n > workbenchContext.getLayerManager()
                        .getEditableLayers().size()) ? msg : null;
            }
        };
    }

    public EnableCheck createExactlyOneSelectedLayerMustBeEditableCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-selected-layer-must-be-editable");
                Layer[] layers = workbenchContext.getLayerNamePanel().getSelectedLayers();
                int countSelectedEditable = 0;
                for (int i = 0; i < layers.length; i++) {
                    if (layers[i].isEditable()) {
                        countSelectedEditable++;
                    }
                }
                return 1 != countSelectedEditable ? msg : null;
            }
        };
    }

    public EnableCheck createAtLeastNLayersMustExistCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                LayerManager layerManager = workbenchContext.getLayerManager();
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-layer-must-exist");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-layers-must-exist",
                            new Object[]{n});
                }
                return (layerManager == null || n > layerManager.size()) ? msg
                        : null;
            }
        };
    }

    public EnableCheck createAtMostNLayersMustExistCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-most-one-layer-must-exist");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-most-n-layers-must-exist",
                            new Object[]{n});
                }
                return (n < workbenchContext.getLayerManager().size()) ? msg
                        : null;
            }
        };
    }

    public EnableCheck createExactlyNVectorsMustBeDrawnCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-vector-must-be-drawn");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-vectors-must-be-drawn",
                            new Object[]{n});
                }
                return (n != vectorCount()) ? msg : null;
            }
        };
    }

    // <<TODO:REFACTORING>> I wonder if we can refactor some of these methods
    // [Bob Boseko]
    public EnableCheck createAtLeastNVectorsMustBeDrawnCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-vector-must-be-drawn");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-vectors-must-be-drawn",
                            new Object[]{n});
                }
                return (n > vectorCount()) ? msg : null;
            }
        };
    }

    public EnableCheck createAtLeastNFeaturesMustBeSelectedCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-feature-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-features-must-be-selected",
                            new Object[]{n});
                }
                return (n > ((SelectionManagerProxy) workbenchContext
                        .getWorkbench().getFrame().getActiveInternalFrame())
                        .getSelectionManager()
                        .getFeaturesWithSelectedItemsCount()) ? msg : null;
            }
        };
    }

    public EnableCheck createAtLeastNItemsMustBeSelectedCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                JInternalFrame iFrame = workbenchContext.getWorkbench()
                        .getFrame().getActiveInternalFrame();
                int selected = 0;
                try {
                    // Modified bu sstein [13. Aug. 2006], [13. Mar. 2008] mmichaud [11. Dec. 2011]
                    // It should now works homogeneously for ViewPnale, ViewAttributes and InfoPanel
                    selected = ((SelectionManagerProxy) iFrame).getSelectionManager().getSelectedItems().size();
                } catch (Exception e) {
                    //-- sstein:
                    //== eat exception ==
                    System.out.println("eat exception @ EnableCheckFactory.createAtLeastNItemsMustBeSelectedCheck(i) if a non taskframe(or child) is selected");
                    //necessary if iFrame is OutputFrame or something 
                    //and i dont know how to test for alle iFrames which exist or rather i do not know
                    //which are the ones accessible to the SelectionManager
                }
                String retVal = null;
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-item-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-items-must-be-selected",
                            new Object[]{n});
                }
                if ((iFrame == null) || (n > selected)) {
                    retVal = msg;
                } else {
                    retVal = null;
                }
                return retVal;
            }
        };
    }

    public EnableCheck createExactlyNFeaturesMustBeSelectedCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-feature-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-features-must-be-selected",
                            new Object[]{n});
                }
                return (n != ((SelectionManagerProxy) workbenchContext
                        .getWorkbench().getFrame().getActiveInternalFrame())
                        .getSelectionManager()
                        .getFeaturesWithSelectedItemsCount()) ? msg : null;
            }
        };
    }

    public EnableCheck createExactlyNItemsMustBeSelectedCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-item-must-be-selected");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-items-must-be-selected",
                            new Object[]{n});
                }
                return (n != ((SelectionManagerProxy) workbenchContext
                        .getWorkbench().getFrame().getActiveInternalFrame())
                        .getSelectionManager().getSelectedItemsCount()) ? msg
                        : null;
            }
        };
    }

    public EnableCheck createExactlyNLayersMustHaveSelectedItemsCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-layer-must-have-selected-items");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-layers-must-have-selected-items",
                            new Object[]{n});
                }
                return (n != ((SelectionManagerProxy) workbenchContext
                        .getWorkbench().getFrame().getActiveInternalFrame())
                        .getSelectionManager().getLayersWithSelectedItems()
                        .size()) ? msg : null;
            }
        };
    }

    public EnableCheck createExactlyNFeaturesMustHaveSelectedItemsCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.Exactly-one-feature-must-have-selected-items");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.Exactly-n-features-must-have-selected-items",
                            new Object[]{n});
                }
                return (n != ((SelectionManagerProxy) workbenchContext
                        .getWorkbench().getFrame().getActiveInternalFrame())
                        .getSelectionManager()
                        .getFeaturesWithSelectedItemsCount()) ? msg : null;
            }
        };
    }

    public EnableCheck createSelectedLayersMustBeEditableCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                for (Iterator<Layer> i = Arrays.asList(workbenchContext.getLayerNamePanel().getSelectedLayers()).iterator(); i.hasNext();) {
                    Layer layer = i.next();
                    if (!layer.isEditable()) {
                        return getMessage(
                                "com.osfac.dmt.workbench.plugin.Selected-layers-must-be-editable",
                                new String[]{layer.getName()});
                    }
                }
                return null;
            }
        };
    }

    public EnableCheck createFenceMustBeDrawnCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                LayerViewPanel layerViewPanel = workbenchContext.getLayerViewPanel();
                return (layerViewPanel == null || //[UT] 20.10.2005 not quite the error mesg
                        null == layerViewPanel.getFence())
                        ? get("com.osfac.dmt.workbench.plugin.A-fence-must-be-drawn")
                        : null;
            }
        };
    }

    public EnableCheck createBetweenNAndMVectorsMustBeDrawnCheck(final int min, final int max) {
        return new EnableCheck() {
            public String check(JComponent component) {
                return ((vectorCount() > max) || (vectorCount() < min))
                        ? getMessage(
                        "com.osfac.dmt.workbench.plugin.Between-and-vectors-must-be-drawn",
                        new Object[]{min, max})
                        : null;
            }
        };
    }

    int vectorCount() {
        return new WarpingVectorLayerFinder(workbenchContext).getVectors().size();
    }

    public EnableCheck createAtLeastNFeaturesMustHaveSelectedItemsCheck(
            final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                String msg;
                if (n == 1) {
                    msg = get("com.osfac.dmt.workbench.plugin.At-least-one-feature-must-have-selected-items");
                } else {
                    msg = getMessage(
                            "com.osfac.dmt.workbench.plugin.At-least-n-features-must-have-selected-items",
                            new Object[]{n});
                }
                return (n > ((SelectionManagerProxy) workbenchContext
                        .getWorkbench().getFrame().getActiveInternalFrame())
                        .getSelectionManager()
                        .getFeaturesWithSelectedItemsCount()) ? msg : null;
            }
        };
    }
}
