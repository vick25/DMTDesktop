package com.osfac.dmt.workbench.model;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;

/**
 * An action that can be rolled back. Similar to an UndoableEdit, but assumes
 * that the action is yet to be executed, whereas an UndoableEdit assumes that
 * the action has already been executed (i.e. it has a #redo method but not a
 * #do method).
 *
 * @see javax.swing.undo.UndoableEdit
 */
public abstract class UndoableCommand {

    private String name;

    public UndoableCommand(String name) {
        this.name = name;
    }

    /**
     * Releases resources.
     */
    protected void dispose() {
    }

    /**
     * If there is an exception that leaves this UndoableCommand execution
     * partially complete and non-unexecutable, be sure to call
     * #reportIrreversibleChange() on the UndoableEditReceiver (which can be
     * obtained from the LayerManager).
     *
     * @see UndoableEditReceiver#reportIrreversibleChange()
     */
    public abstract void execute();

    public abstract void unexecute();

    public UndoableEdit toUndoableEdit() {
        return new AbstractUndoableEdit() {
            public String getPresentationName() {
                return name;
            }

            public void redo() {
                execute();
                super.redo();
            }

            public void die() {
                dispose();
                super.die();
            }

            public void undo() {
                super.undo();
                unexecute();
            }
        };
    }

    public String getName() {
        return name;
    }
    public static final UndoableCommand DUMMY = new UndoableCommand("Dummy") {
        public void execute() {
        }

        public void unexecute() {
        }
    };
}
