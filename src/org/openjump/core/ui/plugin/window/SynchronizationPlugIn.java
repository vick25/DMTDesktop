package org.openjump.core.ui.plugin.window;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.ViewportListener;
import com.vividsolutions.jts.geom.Envelope;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JRadioButtonMenuItem;
import org.openjump.core.ui.plugin.AbstractUiPlugIn;

/**
 * A plugin to synchronize / desynchronize LayerViewPanels.
 *
 * @author Michael Michaud
 * @version 0.1 (2008-04-06)
 * @since 1.2F
 */
public class SynchronizationPlugIn extends AbstractUiPlugIn {

    static ViewportListener vpl;

    public SynchronizationPlugIn(String name) {
        super(name);
    }

    public void initialize(final PlugInContext context) throws Exception {

        // Set the workbenchContext used in getEnableCheck method
        super.initialize(context);

        final JRadioButtonMenuItem desynchronizeMI =
                new JRadioButtonMenuItem(I18N.get("org.openjump.core.ui.plugin.window.SyncronizationPlugIn.Desynchronize"));
        final JRadioButtonMenuItem synchronizePanMI =
                new JRadioButtonMenuItem(I18N.get("org.openjump.core.ui.plugin.window.SyncronizationPlugIn.Synchronize-pan-only"));
        final JRadioButtonMenuItem synchronizeAllMI =
                new JRadioButtonMenuItem(I18N.get("org.openjump.core.ui.plugin.window.SyncronizationPlugIn.Synchronize-pan-and-zoom"));

        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(desynchronizeMI);
        bgroup.add(synchronizePanMI);
        bgroup.add(synchronizeAllMI);

        desynchronizeMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                desynchronize();
            }
        });
        synchronizePanMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronize(false);
            }
        });
        synchronizeAllMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronize(true);
            }
        });

        context.getFeatureInstaller().addMainMenuItem(
                new String[]{MenuNames.WINDOW, MenuNames.WINDOW_SYNCHRONIZATION},
                this,
                synchronizePanMI,
                0);
        context.getFeatureInstaller().addMainMenuItem(
                new String[]{MenuNames.WINDOW, MenuNames.WINDOW_SYNCHRONIZATION},
                this,
                synchronizeAllMI,
                1);
        context.getFeatureInstaller().addMainMenuItem(
                new String[]{MenuNames.WINDOW, MenuNames.WINDOW_SYNCHRONIZATION},
                this,
                desynchronizeMI,
                2);

    }

    public String getName() {
        return I18N.get("org.openjump.core.ui.plugin.window.SyncronizationPlugIn.Synchronization");
    }

    public EnableCheck getEnableCheck() {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }

    public void synchronize(boolean panAndZoom) {
        ViewportListener oldViewportListener = vpl;
        vpl = createViewportListener(panAndZoom);
        // add the listener to every active map window
        JInternalFrame[] iframes = workbenchContext.getWorkbench().getFrame().getInternalFrames();
        for (JInternalFrame iframe : iframes) {
            if (iframe instanceof TaskFrame) {
                // Remove the old listener if any before adding a new one
                // Thus, one can reinitialize synchronization if the number
                // of map windows change
                // Ideally, one should intercept new window creation or window focus
                // change to add new listeners to new windows..
                ((TaskFrame) iframe).getLayerViewPanel().getViewport().removeListener(oldViewportListener);
                ((TaskFrame) iframe).getLayerViewPanel().getViewport().addListener(vpl);
            }
        }
    }

    public void desynchronize() {
        ViewportListener oldViewPortListener = vpl;
        vpl = null;
        // add the listener to every active map window
        JInternalFrame[] iframes = workbenchContext.getWorkbench().getFrame().getInternalFrames();
        for (JInternalFrame iframe : iframes) {
            if (iframe instanceof TaskFrame) {
                // Remove the old listener if any before adding a new one
                // Thus, one can reinitialize synchronization if the number
                // of map windows change
                // Ideally, one should intercept new window creation or window focus
                // change to add new listeners to new windows..
                ((TaskFrame) iframe).getLayerViewPanel().getViewport().removeListener(oldViewPortListener);
            }
        }
    }

    private ViewportListener createViewportListener(final boolean panAndZoom) {
        final WorkbenchContext context = workbenchContext;
        return new ViewportListener() {
            public void zoomChanged(Envelope modelEnvelope) {
                JInternalFrame[] iframes = context.getWorkbench().getFrame().getInternalFrames();
                for (JInternalFrame iframe : iframes) {
                    if (iframe instanceof TaskFrame
                            && ((TaskFrame) iframe).getLayerViewPanel().getViewport()
                            != context.getLayerViewPanel().getViewport()) {
                        try {
                            // Copy of method viewport.zoom(envelope)
                            // without the statement fireZoomChanged(modelEnvelope)
                            // to avoid entering an infinite loop
                            // window 1 change --> window 2 change --> window 1 change ...
                            Viewport viewport = ((TaskFrame) iframe).getLayerViewPanel().getViewport();
                            double w = viewport.getPanel().getWidth();
                            double h = viewport.getPanel().getHeight();
                            double scale = viewport.getScale();
                            if (panAndZoom) {
                                scale = Math.min(w / modelEnvelope.getWidth(),
                                        h / modelEnvelope.getHeight());
                            }
                            double xCenteringOffset = ((w / scale) - modelEnvelope.getWidth()) / 2.0;
                            double yCenteringOffset = ((h / scale) - modelEnvelope.getHeight()) / 2.0;
                            Point2D.Double viewOriginAsPerceivedByModel =
                                    new Point2D.Double(modelEnvelope.getMinX() - xCenteringOffset,
                                    modelEnvelope.getMinY() - yCenteringOffset);
                            viewport.initialize(scale, viewOriginAsPerceivedByModel);
                            viewport.update();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };
    }

    /**
     * For this plugin, this method is unused All the work is defined in action
     * listeners
     */
    public boolean execute(PlugInContext context) throws Exception {
        return true;
    }
}