package com.osfac.dmt.task;

import com.osfac.dmt.util.StringUtil;
import java.io.PrintStream;

/**
 * A TaskMonitor that reports its output to a PrintStream. User can control the
 * level of output reported, as well as whether timing information is logged as
 * well.
 */
public class PrintStreamTaskMonitor implements TaskMonitor {

    private PrintStream stream;
    boolean isLoggingSubtasks = false;

    public PrintStreamTaskMonitor(PrintStream stream) {
        this.stream = stream;
    }

    public PrintStreamTaskMonitor() {
        stream = System.out;
    }

    public void setLoggingSubtasks(boolean isLoggingSubtasks) {
        this.isLoggingSubtasks = isLoggingSubtasks;
    }

    public void report(String description) {
        stream.println(description);
    }

    public void report(Exception exception) {
        stream.println(StringUtil.stackTrace(exception));
    }

    public void report(int subtasksDone, int totalSubtasks,
            String subtaskDescription) {
        if (isLoggingSubtasks) {
            stream.println(subtasksDone + " / " + totalSubtasks + " "
                    + subtaskDescription);
        }
    }

    public void allowCancellationRequests() {
    }

    public boolean isCancelRequested() {
        return false;
    }
}
