package org.openjump.core.ui.plugin;

import com.osfac.dmt.workbench.plugin.ThreadedPlugIn;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;
import javax.swing.Icon;

public abstract class AbstractThreadedUiPlugIn extends AbstractUiPlugIn implements
        ThreadedPlugIn {

    private TaskMonitorManager taskMonitorManager = new TaskMonitorManager();

    public AbstractThreadedUiPlugIn() {
    }

    public AbstractThreadedUiPlugIn(String name, Icon icon, String toolTip) {
        super(name, icon, toolTip);
    }

    public AbstractThreadedUiPlugIn(String name, Icon icon) {
        super(name, icon);
    }

    public AbstractThreadedUiPlugIn(String name, String toolTip) {
        super(name, toolTip);
    }

    public AbstractThreadedUiPlugIn(String name) {
        super(name);
    }

    public AbstractThreadedUiPlugIn(Icon icon) {
        super(icon);
    }
}
