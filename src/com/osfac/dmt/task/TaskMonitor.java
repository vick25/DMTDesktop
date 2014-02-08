package com.osfac.dmt.task;

/**
 * Provides a simple interface between an operation (or "task") and the
 * application in which it executes. Enables the task to report its progress,
 * and to check whether the application has requested that it be cancelled.
 */
public interface TaskMonitor {

    /**
     * Describes the status of the task.
     *
     * @param description a description of the progress of the overall task
     */
    public void report(String description);

    /**
     * Reports the number of items processed.
     *
     * @param itemsDone the number of items that have been processed
     * @param totalItems the total number of items being processed, or -1 if the
     * total number is not known
     * @param itemDescription a one-word description of the items, such as
     * "features"
     */
    public void report(int itemsDone, int totalItems, String itemDescription);

    /**
     * Reports an Exception that occurred. The task may choose to carry on.
     *
     * @param exception an Exception that occurred during the execution of the
     * task.
     */
    public void report(Exception exception);

    /**
     * Notifies parties that the task will accept requests for cancellation
     * (though the task is not obligated to cancel immediately, or at all for
     * that matter).
     */
    public void allowCancellationRequests();

    /**
     * Checks whether a party has requested that the task be cancelled. However,
     * the task is not obligated to cancel immediately (or at all).
     *
     * @return whether a party has requested that the task be cancelled
     */
    public boolean isCancelRequested();
}
