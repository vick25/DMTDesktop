
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.osfac.dmt.workbench.ui.zoom;

import java.awt.geom.NoninvertibleTransformException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.util.Assert;
import com.osfac.dmt.geom.CoordUtil;
import com.osfac.dmt.geom.EnvelopeUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.images.famfam.IconLoaderFamFam;
import com.osfac.dmt.workbench.ui.renderer.ThreadQueue;

/**
 * Zoom to the features, then flash them.
 */
public class PanToSelectedItemsPlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        pan(
            context.getLayerViewPanel().getSelectionManager().getSelectedItems(),
            context.getLayerViewPanel());
        return true;
    }

    public void pan(final Collection geometries, final LayerViewPanel panel)
        throws NoninvertibleTransformException {
        if (envelope(geometries).isNull()) {
            return;
        }

        Envelope proposedEnvelope = panel.getViewport().getEnvelopeInModelCoordinates();

        EnvelopeUtil.translate(proposedEnvelope, CoordUtil.subtract(
                EnvelopeUtil.centre(envelope(geometries)),
                EnvelopeUtil.centre(proposedEnvelope)));
        panel.getViewport().zoom(proposedEnvelope);
        //Wait until the zoom is complete before executing the flash. [Bob Boseko]
        ThreadQueue.Listener listener = new ThreadQueue.Listener() {
            public void allRunningThreadsFinished() {
                panel.getRenderingManager().getDefaultRendererThreadQueue().remove(this);
                try {
                    GUIUtil.invokeOnEventThread(new Runnable() {
                        public void run() {
                            try {
                                flash(geometries, panel);
                            } catch (NoninvertibleTransformException e) {}
                        }
                    });
                } catch (InterruptedException e) {} catch (InvocationTargetException e) {}
            }
        };
        panel.getRenderingManager().getDefaultRendererThreadQueue().add(listener);

    }

    private Envelope envelope(Collection geometries) {
        Envelope envelope = new Envelope();

        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            envelope.expandToInclude(geometry.getEnvelopeInternal());
        }

        return envelope;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
            .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));
    }

    public void flash(Collection geometries, final LayerViewPanel panel)
        throws NoninvertibleTransformException {
        final GeometryCollection gc = toGeometryCollection(geometries);

        if (!panel
            .getViewport()
            .getEnvelopeInModelCoordinates()
            .intersects(gc.getEnvelopeInternal())) {
            return;
        }

        panel.flash(gc);
    }

    private GeometryCollection toGeometryCollection(Collection geometries) {
        return new GeometryFactory().createGeometryCollection(
            (Geometry[]) geometries.toArray(new Geometry[] {}));
    }

    //public ImageIcon getIcon() {
    //    //return IconLoaderFamFam.icon("ZoomSelected_small.png");
    //    return IconLoader.icon("PanSelected.gif");
    //}
}
