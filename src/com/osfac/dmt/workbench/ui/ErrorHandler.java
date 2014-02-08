package com.osfac.dmt.workbench.ui;

public interface ErrorHandler {

    /**
     * Note that this method may or may not be called from the AWT
     * event-dispatch thread.
     */
    public void handleThrowable(final Throwable t);
}
