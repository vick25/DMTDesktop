package com.osfac.dmt.workbench.ui.plugin.test;

import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jts.util.Assert;

public class ProgressReportingPlugIn extends ThreadedBasePlugIn {

    private final static int MS_PER_SUBTASK = 3000;
    private final static int SUBTASK_COUNT = 5;
    private final static int SUBSUBTASK_COUNT = 1000;

    public ProgressReportingPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItemWithJava14Fix(this,
                new String[]{"Tools", "Test"}, getName(), false, null, null);
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);

        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context) {
        monitor.allowCancellationRequests();
        context.getOutputFrame().createNewDocument();
        context.getOutputFrame().addHeader(1, "Header 1");
        context.getOutputFrame().addHeader(2, "Header 2");
        context.getOutputFrame().addHeader(3, "Header 3");
        context.getOutputFrame().addHeader(4, "Header 4");
        context.getOutputFrame().addHeader(5, "Header 5");

        for (int i = 1; i <= SUBTASK_COUNT; i++) {
            if (monitor.isCancelRequested()) {
                break;
            }

            monitor.report("Doing Subtask " + i);
            context.getOutputFrame().addField("Progress:", String.valueOf(i),
                    "tasks");

            for (int j = 1; j <= SUBSUBTASK_COUNT; j++) {
                monitor.report(j, SUBSUBTASK_COUNT, "subsubtasks");

                try {
                    Thread.sleep(MS_PER_SUBTASK / SUBSUBTASK_COUNT);
                } catch (InterruptedException e) {
                    Assert.shouldNeverReachHere();
                }
            }
        }
    }
}
