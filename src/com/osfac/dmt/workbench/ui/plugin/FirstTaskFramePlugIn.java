package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import org.apache.log4j.Logger;
import org.openjump.core.ui.plugin.file.OpenProjectPlugIn;

/**
 * Opens a TaskFrame when the Workbench starts up
 */
public class FirstTaskFramePlugIn extends AbstractPlugIn {//AbstractPlugIn {

    private static Logger LOG = Logger.getLogger(FirstTaskFramePlugIn.class);

    public FirstTaskFramePlugIn() {
    }
    private ComponentListener componentListener;

    @Override
    public void initialize(final PlugInContext context) throws Exception {
        final WorkbenchContext workbenchContext = context.getWorkbenchContext();
        componentListener = new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                //Two reasons wait until the frame is shown before adding the task frame:
                //(1) Otherwise the task frame won't be selected (2) Otherwise GUIUtil.setLocation
                //will throw an IllegalComponentStateException. [Bob Boseko]
                //UT skip this; see 1st if there is a filename available
                // if so, load it
                String filename = (String) context.getWorkbenchContext().getBlackboard().get(DMTWorkbench.INITIAL_PROJECT_FILE);

                if (filename == null) {//create empty task
                    context.getWorkbenchFrame().addTaskFrame();
                } else {

                    LOG.info("Found initial project file: " + filename);

                    File f = new File(filename);

                    try {
                        // switch to new OpenProjectPlugIn [Matthias Scholz 11. Dec. 2011]
                        OpenProjectPlugIn openProjectPlugIn = new OpenProjectPlugIn(workbenchContext, f);
                        AbstractPlugIn.toActionListener(openProjectPlugIn, workbenchContext, new TaskMonitorManager()).actionPerformed(new ActionEvent(this, 0, ""));
                    } catch (Exception ex) {
                        String mesg = "Could not load initial file";
                        LOG.error(mesg);
                        context.getWorkbenchFrame().warnUser(mesg);
                        context.getWorkbenchFrame().addTaskFrame();
                    }
                }

                context.getWorkbenchFrame().removeComponentListener(componentListener);
            }
        };
        context.getWorkbenchFrame().addComponentListener(componentListener);
    }
}
