package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.FileUtil;
import com.osfac.dmt.util.java2xml.Java2XML;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JInternalFrame;

/**
 * Subclass this to implement a 'Save Project' plugin.
 */
public abstract class AbstractSaveProjectPlugIn extends AbstractPlugIn {

    public AbstractSaveProjectPlugIn() {
    }

    protected void save(Task task, File file, WorkbenchFrame frame)
            throws Exception {
        //First use StringWriter to make sure no errors occur before we touch the
        //original file -- we don't want to damage the original if an error occurs.
        //[Bob Boseko]
        JInternalFrame taskWindow = frame.getActiveInternalFrame();
        task.setMaximized(taskWindow.isMaximum());
        if (taskWindow.isMaximum()) {  //save the rectangle that it would be restored to
            Rectangle normalBounds = taskWindow.getNormalBounds();
            task.setTaskWindowLocation(new Point(normalBounds.x, normalBounds.y));
            task.setTaskWindowSize(new Dimension(normalBounds.width, normalBounds.height));
        } else {
            task.setTaskWindowLocation(taskWindow.getLocation());
            task.setTaskWindowSize(taskWindow.getSize());
        }
        task.setSavedViewEnvelope(frame.getContext().getLayerViewPanel()
                .getViewport().getEnvelopeInModelCoordinates());

        StringWriter stringWriter = new StringWriter();

        try {
            new Java2XML().write(task, "project", stringWriter);
        } finally {
            stringWriter.flush();
        }

        FileUtil.setContents(file.getAbsolutePath(), stringWriter.toString());
        task.setName(GUIUtil.nameWithoutExtension(file));
        task.setProjectFile(file);

        ArrayList ignoredLayers = new ArrayList(ignoredLayers(task));

        if (!ignoredLayers.isEmpty()) {
            String warning = I18N.get("ui.plugin.AbstractSaveProjectPlugIn.some-layers-were-not-saved-to-the-task-file") + " ";

            for (int i = 0; i < ignoredLayers.size(); i++) {
                Layer ignoredLayer = (Layer) ignoredLayers.get(i);

                if (i > 0) {
                    //warning += "; ";
                    warning += "\n";
                }

                warning += ignoredLayer.getName();
            }

            warning += " (" + I18N.get("ui.plugin.AbstractSaveProjectPlugIn.data-source-is-write-only") + ")";

            //frame.warnUser(warning);
            frame.log(warning);
        }
    }

    protected Collection ignoredLayers(Task task) {
        ArrayList ignoredLayers = new ArrayList();

        for (Iterator i = task.getLayerManager().getLayers().iterator();
                i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (!layer.hasReadableDataSource()) {
                ignoredLayers.add(layer);
            }
        }

        return ignoredLayers;
    }
}
