package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class MoveLayerablePlugIn extends AbstractPlugIn {

    public static final ImageIcon UPICON = IconLoader.icon("arrow_up.png");
    public static final ImageIcon DOWNICON = IconLoader.icon("arrow_down.png");
    public static final MoveLayerablePlugIn UP = new MoveLayerablePlugIn(-1) {
        public String getName() {
            return I18N.get("ui.plugin.MoveLayerablePlugIn.move-layer-up");
        }

        public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
            return super.createEnableCheck(workbenchContext).add(new EnableCheck() {
                public String check(JComponent component) {
                    return (index(selectedLayerable(workbenchContext.getLayerNamePanel())) == 0)
                            ? I18N.get("ui.plugin.MoveLayerablePlugIn.layer-is-already-at-the-top")
                            : null;
                }
            });
        }
    };
    public static final MoveLayerablePlugIn DOWN = new MoveLayerablePlugIn(1) {
        public String getName() {
            return I18N.get("ui.plugin.MoveLayerablePlugIn.move-layer-down");
        }

        public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
            return super.createEnableCheck(workbenchContext).add(new EnableCheck() {
                public String check(JComponent component) {
                    return (index(selectedLayerable(workbenchContext.getLayerNamePanel()))
                            == (workbenchContext
                            .getLayerViewPanel()
                            .getLayerManager()
                            .getCategory(
                            selectedLayerable(workbenchContext.getLayerNamePanel()))
                            .getLayerables()
                            .size()
                            - 1))
                            ? I18N.get("ui.plugin.MoveLayerablePlugIn.layer-is-already-at-the-bottom")
                            : null;
                }
            });
        }
    };
    private int displacement;

    private MoveLayerablePlugIn(int displacement) {
        this.displacement = displacement;
    }

    protected Layerable selectedLayerable(LayerNamePanel layerNamePanel) {
        return (Layerable) layerNamePanel.selectedNodes(Layerable.class).iterator().next();
    }

    public boolean execute(final PlugInContext context) throws Exception {
        final Layerable layerable = selectedLayerable(context.getLayerNamePanel());
        final int index = index(layerable);
        final Category category = context.getLayerManager().getCategory(layerable);
        execute(new UndoableCommand(getName()) {
            public void execute() {
                moveLayerable(index + displacement);
            }

            public void unexecute() {
                moveLayerable(index);
            }

            private void moveLayerable(int newIndex) {
                context.getLayerManager().remove(layerable);
                category.add(newIndex, layerable);
            }
        }, context);

        return true;
    }

    protected int index(Layerable layerable) {
        return layerable.getLayerManager().getCategory(layerable).indexOf(layerable);
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayerablesMustBeSelectedCheck(1, Layerable.class));
    }
}
