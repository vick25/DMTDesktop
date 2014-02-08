package com.osfac.dmt.task;

public class TaskMonitorFilter extends DummyTaskMonitor {

    public TaskMonitorFilter(TaskMonitor taskMonitor) {
        this.taskMonitor = taskMonitor;
    }
    private TaskMonitor taskMonitor;

    public static TaskMonitor get(TaskMonitor taskMonitor) {
        if (taskMonitor instanceof TaskMonitorFilter) {
            return ((TaskMonitorFilter) taskMonitor).taskMonitor;
        }
        return taskMonitor;
    }
}