package de.latlon.deejump.plugin.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.osfac.dmt.workbench.ui.renderer.style.ColorThemingStylePanel;
import com.osfac.dmt.workbench.ui.style.DecorationStylePanel;
import com.osfac.dmt.workbench.ui.style.LabelStylePanel;
import com.osfac.dmt.workbench.ui.style.ScaleStylePanel;
import com.osfac.dmt.workbench.ui.style.StylePanel;
import com.vividsolutions.jts.util.Assert;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * <code>DeeChangeStylesPlugIn</code>
 *
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: michaudm $
 *
 * @version $Revision: 2239 $, $Date: 2008-02-14 14:37:00 +0100 (Thu, 14 Feb
 * 2008) $
 */
public class DeeChangeStylesPlugIn extends AbstractPlugIn {

    private final static String LAST_TAB_KEY = DeeChangeStylesPlugIn.class.getName() + " - LAST TAB";

    @Override
    public String getName() {
        return I18N.get("ui.style.ChangeStylesPlugIn.change-styles");
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        WorkbenchFrame wbframe = context.getWorkbenchFrame();
        WorkbenchContext wbcontext = context.getWorkbenchContext();
        Blackboard blackboard = wbcontext.getWorkbench().getBlackboard();
        Blackboard pb = PersistentBlackboardPlugIn.get(wbcontext);

        final Layer layer = context.getSelectedLayer(0);
        MultiInputDialog dialog = new MultiInputDialog(wbframe, I18N.get("ui.style.ChangeStylesPlugIn.change-styles"),
                true);
        dialog.setInset(0);
        dialog.setSideBarImage(IconLoader.icon("Symbology.gif"));
        dialog.setSideBarDescription(I18N
                .get("ui.style.ChangeStylesPlugIn.you-can-use-this-dialog-to-change-the-colour-line-width"));

        final ArrayList<StylePanel> stylePanels = new ArrayList<>();
        final DeeRenderingStylePanel renderingStylePanel = new DeeRenderingStylePanel(blackboard, layer, pb);

        stylePanels.add(renderingStylePanel);
        stylePanels.add(new ScaleStylePanel(layer, context.getLayerViewPanel()));

        // Only set preferred size for DecorationStylePanel or
        // ColorThemingStylePanel;
        // otherwise they will grow to the height of the screen. But don't set
        // the preferred size of LabelStylePanel to (400, 300) -- in fact, it
        // needs
        // a bit more height -- any less, and its text boxes will shrink to
        // zero-width. I've found that if you don't give text boxes enough
        // height,
        // they simply shrink to zero-width. [Bob Boseko]
        DecorationStylePanel decorationStylePanel = new DecorationStylePanel(layer, wbframe.getChoosableStyleClasses());
        decorationStylePanel.setPreferredSize(new Dimension(400, 300));
        if (layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount() > 1) {
            ColorThemingStylePanel colorThemingStylePanel = new ColorThemingStylePanel(layer, wbcontext);
            colorThemingStylePanel.setPreferredSize(new Dimension(400, 300));
            stylePanels.add(colorThemingStylePanel);
            GUIUtil.sync(renderingStylePanel.getTransparencySlider(), colorThemingStylePanel.getTransparencySlider());
            GUIUtil.sync(renderingStylePanel.getSynchronizeCheckBox(), colorThemingStylePanel.getSynchronizeCheckBox());

        } else {
            stylePanels.add(new DummyColorThemingStylePanel());
        }

        stylePanels.add(new LabelStylePanel(layer, context.getLayerViewPanel(), dialog, context.getErrorHandler()));
        stylePanels.add(decorationStylePanel);

        JTabbedPane tabbedPane = new JTabbedPane();

        for (Iterator<StylePanel> i = stylePanels.iterator(); i.hasNext();) {
            final StylePanel stylePanel = i.next();
            tabbedPane.add((Component) stylePanel, stylePanel.getTitle());
            dialog.addEnableChecks(stylePanel.getTitle(), Arrays.asList(new EnableCheck[]{new EnableCheck() {
                    public String check(JComponent component) {
                        return stylePanel.validateInput();
                    }
                }}));
        }

        dialog.addRow(tabbedPane);

        String selectedTab = (String) blackboard.get(LAST_TAB_KEY, (stylePanels.iterator().next()).getTitle());
        tabbedPane.setFocusable(false);
        tabbedPane.setSelectedComponent(find(stylePanels, selectedTab));
        dialog.pack();
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        blackboard.put(LAST_TAB_KEY, ((StylePanel) tabbedPane.getSelectedComponent()).getTitle());

        if (dialog.wasOKPressed()) {
            final Collection<?> oldStyles = layer.cloneStyles();
            layer.getLayerManager().deferFiringEvents(new Runnable() {
                public void run() {
                    for (Iterator<StylePanel> i = stylePanels.iterator(); i.hasNext();) {
                        StylePanel stylePanel = i.next();
                        stylePanel.updateStyles();
                    }

                }
            });

            // fix the problem with mixing styles
            layer.getLayerManager().deferFiringEvents(new Runnable() {
                public void run() {
                    if (layer.getVertexStyle().isEnabled()) {
                        layer.getBasicStyle().setRenderingVertices(false);
                    }
                }
            });

            final Collection<?> newStyles = layer.cloneStyles();
            execute(new UndoableCommand(getName()) {
                @Override
                public void execute() {
                    layer.setStyles(newStyles);
                }

                @Override
                public void unexecute() {
                    layer.setStyles(oldStyles);
                }
            }, context);
            return true;
        } else {
            reportNothingToUndoYet(context);
        }

        return false;
    }

    private Component find(Collection<StylePanel> stylePanels, String title) {
        for (Iterator<StylePanel> i = stylePanels.iterator(); i.hasNext();) {
            StylePanel stylePanel = i.next();

            if (stylePanel.getTitle().equals(title)) {
                return (Component) stylePanel;
            }
        }

        Assert.shouldNeverReachHere();

        return null;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        //return IconLoaderFamFam.icon("palette.png");
        return IconLoader.icon("Palette.gif");
    }

    /**
     * @param workbenchContext
     * @return the enable check
     */
    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                // ScaledStylePanel assumes that the active window has a
                // LayerViewPanel. [Bob Boseko
                // 2005-08-09]
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()).add(
                checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }

    private class DummyColorThemingStylePanel extends JPanel implements StylePanel {

        private static final long serialVersionUID = 2217457292163045134L;

        /**
         *
         */
        public DummyColorThemingStylePanel() {
            // GridBagLayout so it gets centered. [Bob Boseko]
            super(new GridBagLayout());
            add(new JLabel(I18N.get("ui.style.ChangeStylesPlugIn.this-layer-has-no-attributes")));
        }

        public String getTitle() {
            return ColorThemingStylePanel.TITLE;
        }

        public void updateStyles() {
            // unused but defined in the interface
        }

        public String validateInput() {
            return null;
        }
    }
}
