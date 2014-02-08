package com.osfac.dmt.workbench.ui.task;

import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedPlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class TaskMonitorManager {

    public TaskMonitorManager() {
    }

    /**
     * Executes the task in a separate thread, reporting progress in a dialog.
     */
    public void execute(ThreadedPlugIn plugIn, PlugInContext context) {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(context
                .getWorkbenchFrame(), context.getErrorHandler());
        progressDialog.setTitle(plugIn.getName());

        //Do not refer to context inside the anonymous class, otherwise it (and
        //the Task it refers to) will not be garbage collected. [Bob Boseko]
        //<<TODO>> Eliminate TaskWrapper. The Task no longer needs to be
        //garbage-collectable for the data to be garbage-collected. [Jon
        // Aquino]
        final TaskWrapper taskWrapper = new TaskWrapper(plugIn, context,
                progressDialog);
        final Thread thread = new Thread(taskWrapper);
        progressDialog.addWindowListener(new WindowAdapter() {
            private int attempts = 0;

            public void windowClosing(WindowEvent e) {
                if (JOptionPane.NO_OPTION == JOptionPane
                        .showConfirmDialog(
                        progressDialog,
                        StringUtil
                        .split(
                        "Warning: Killing the process may result in data corruption or data loss. "
                        + "Are you sure you want to kill the process?",
                        80), "Kill Process",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE)) {
                    return;
                }
                attempts++;
                if (attempts > 1) {
                    // Sometimes the thread seems to take a while to die.
                    // So force the dialog to close if the user has pressed
                    // the close button for the second time.
                    // [Bob Boseko 2005-03-14]
                    progressDialog.setVisible(false);
                    progressDialog.dispose();
                }
                thread.stop();
            }
        });
        progressDialog.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                //Wait for the dialog to appear before starting the task.
                // Otherwise the task might possibly finish before the dialog
                // appeared and the dialog would never close. [Bob Boseko]
                thread.start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);

        Timer timer = timer(new Date(), plugIn, context.getWorkbenchFrame());
        timer.start();

        try {
            progressDialog.setVisible(true);
        } finally {
            timer.stop();
            progressDialog.dispose();
        }
    }

    private Timer timer(final Date start, final ThreadedPlugIn plugIn,
            final WorkbenchFrame workbenchFrame) {
        return new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "";
                message += StringUtil.toTimeString(new Date().getTime()
                        - start.getTime());
                message += (" (" + plugIn.getName() + ")");
                workbenchFrame.setTimeMessage(message);
            }
        });
    }

    private class TaskWrapper implements Runnable {

        private ThreadedPlugIn plugIn;
        private PlugInContext context;
        private TaskMonitorDialog dialog;

        public TaskWrapper(ThreadedPlugIn plugIn, PlugInContext context,
                TaskMonitorDialog dialog) {
            this.plugIn = plugIn;
            this.context = context;
            this.dialog = dialog;
        }

        public void run() {
            Throwable throwable = null;

            try {
                plugIn.run(dialog, context);
            } catch (Throwable t) {
                throwable = t;
            } finally {
                // Hmm - race conditions because we are doing a GUI action
                // (#setVisible) outside the AWT event thread? Case in point:
                // AutoConflatePlugIn displays a dialog using #invokeLater,
                // but timer keeps on running until dialog is closed . . .
                // [Bob Boseko 2004-09-07]

                dialog.setVisible(false);
                dialog.dispose();

                if (throwable != null) {
                    context.getErrorHandler().handleThrowable(throwable);
                }

                // Releases references to the data, to facilitate garbage
                // collection. [Bob Boseko]
                context = null;
            }
        }
    }
}