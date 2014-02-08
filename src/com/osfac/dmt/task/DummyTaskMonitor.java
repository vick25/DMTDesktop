package com.osfac.dmt.task;

/**
 * Implementation of TaskMonitor that does nothing. Can be used for Tasks that
 * do not need to report on their status (for instance, tasks run only in batch
 * mode).
 */
public class DummyTaskMonitor implements TaskMonitor {

    public DummyTaskMonitor() {
    }

    public void report(String description) {
    }

    public void report(int subtasksDone, int totalSubtasks,
            String subtaskDescription) {
    }

    public void allowCancellationRequests() {
    }

    public boolean isCancelRequested() {
        return false;
    }

    public void report(Exception exception) {
    }
}
