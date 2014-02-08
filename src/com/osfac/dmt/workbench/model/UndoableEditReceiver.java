package com.osfac.dmt.workbench.model;

import com.vividsolutions.jts.util.Assert;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Receives UndoableEdits from PlugIns and CursorTools. Also provides access to
 * a Task's UndoManager. <P> In the documentation, the "receiving phase" refers
 * to the time between the calls to #start and #stop. <P> If there is an
 * exception that leaves this UndoableCommand execution partially complete and
 * non-unexecutable, be sure to call #reportIrreversibleChange()
 */
public class UndoableEditReceiver {

    private UndoManager undoManager = new UndoManager();
    private ArrayList newUndoableEdits = new ArrayList();
    /**
     * Handle nested calls to UndoableEditReceiver
     */
    private int transactions = 0;
    private boolean nothingToUndoReported = false;
    private boolean irreversibleChangeReported = false;
    private boolean undoManagerCouldUndoAtStart = false;
    private ArrayList listeners = new ArrayList();

    public UndoableEditReceiver() {
    }

    public void startReceiving() {
        transactions++;
        setNothingToUndoReported(false);
        irreversibleChangeReported = false;
        undoManagerCouldUndoAtStart = undoManager.canUndo();
    }

    /**
     * Specifies that the undo history should not be modified at the end of the
     * current receiving phase, if neither #receive nor
     * #reportIrreversibleChange is called. If none of the three methods are
     * called during the receiving phase, an irreversible change is assumed to
     * have occurred, and the undo history will be truncated.
     */
    public void reportNothingToUndoYet() {
        Assert.isTrue(isReceiving());
        setNothingToUndoReported(true);
    }

    /**
     * Notifies this UndoableEditReceiver that something non-undoable has
     * happened. Be sure to call this if an Exception occurs during the
     * execution of an UndoableCommand, leaving it partially complete and
     * non-unexecutable.
     */
    public void reportIrreversibleChange() {
        Assert.isTrue(isReceiving());
        irreversibleChangeReported = true;
    }

    public void stopReceiving() {
        transactions--;
        try {
            // disabled for further testing, before a plugin had to explicitly call 
            // reportNothingToUndoYet to _not_ reset the undo queue. now we are just ignoring
            // it so that in undo queue this event never existed
            if (/*(newUndoableEdits.isEmpty() && !wasNothingToUndoReported()) ||*/irreversibleChangeReported) {
                undoManager.discardAllEdits();
                return;
            }

            for (Iterator i = newUndoableEdits.iterator(); i.hasNext();) {
                UndoableEdit undoableEdit = (UndoableEdit) i.next();
                undoManager.addEdit(undoableEdit);
            }
            newUndoableEdits.clear();
        } finally {
            fireUndoHistoryChanged();
            if (undoManagerCouldUndoAtStart && !undoManager.canUndo()) {
                fireUndoHistoryTruncated();
            }
        }
    }

    private void fireUndoHistoryTruncated() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener listener = (Listener) i.next();
            listener.undoHistoryTruncated();
        }
    }

    private void fireUndoHistoryChanged() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener listener = (Listener) i.next();
            listener.undoHistoryChanged();
        }
    }

    public void add(Listener listener) {
        listeners.add(listener);
    }

    /**
     * If the currently executing PlugIn or AbstractCursorTool is not undoable,
     * it should simply not call this method; the undo history will be cleared.
     */
    public void receive(UndoableEdit undoableEdit) {
        Assert.isTrue(isReceiving());

        //Don't add the UndoableEdit to the UndoManager right away; the caller may
        //call #clearNewUndoableEdits. [Bob Boseko]
        newUndoableEdits.add(undoableEdit);
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    private void setNothingToUndoReported(boolean nothingToUndoReported) {
        this.nothingToUndoReported = nothingToUndoReported;
    }

    private boolean wasNothingToUndoReported() {
        return nothingToUndoReported;
    }

    public static interface Listener {

        public void undoHistoryChanged();

        public void undoHistoryTruncated();
    }

    public boolean isReceiving() {
        return transactions > 0;
    }
}
