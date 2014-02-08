package com.osfac.dmt.workbench.plugin;

import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;

/**
 * Convenience superclass for classes that want to extend BasePlugIn and
 * implement ThreadedPlugIn.
 */
public abstract class ThreadedBasePlugIn
        extends AbstractPlugIn
        implements ThreadedPlugIn {

    public static void main(String[] args) {
        //Example of creating a ThreadedBasePlugIn on the fly
        new TaskMonitorManager().execute(new ThreadedBasePlugIn() {
            public boolean execute(PlugInContext context) throws Exception {
                return true;
            }

            public void run(TaskMonitor monitor, PlugInContext context)
                    throws Exception {
                //your code here
            }
        }, null);
    }
}
