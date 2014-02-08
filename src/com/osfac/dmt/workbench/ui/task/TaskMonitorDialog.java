package com.osfac.dmt.workbench.ui.task;

import com.osfac.dmt.I18N;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.ui.AnimatedClockPanel;
import com.osfac.dmt.workbench.ui.ErrorHandler;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TaskMonitorDialog extends JDialog implements TaskMonitor {

    JPanel mainPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel labelPanel = new JPanel();
    private JButton cancelButton = new JButton();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private ErrorHandler errorHandler;
    private boolean cancelled;
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private JLabel taskProgressLabel = new JLabel();
    private JLabel subtaskProgressLabel = new JLabel();
    private String taskProgress = "";
    private String subtaskProgress = "";
    private Timer timer = new Timer(500,
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateLabels();
                }
            });
    private AnimatedClockPanel clockPanel = new AnimatedClockPanel();

    public TaskMonitorDialog(Frame frame, ErrorHandler errorHandler) {
        this(frame, errorHandler, true);
    }

    public TaskMonitorDialog(Frame frame, ErrorHandler errorHandler, boolean modal) {
        super(frame, I18N.get("ui.task.TaskMonitorDialog.busy"), modal);
        this.errorHandler = errorHandler;

        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setSize(400, 100);
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                cancelButton.setEnabled(true);
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void jbInit() throws Exception {
        mainPanel.setLayout(gridBagLayout1);
        cancelButton.setText(I18N.get("ui.task.TaskMonitorDialog.cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                this_componentShown(e);
            }

            public void componentHidden(ComponentEvent e) {
                this_componentHidden(e);
            }
        });
        this.getContentPane().setLayout(gridBagLayout2);
        labelPanel.setLayout(gridBagLayout3);
        subtaskProgressLabel.setText(I18N.get("ui.task.TaskMonitorDialog.subtask-progress-goes-here"));
        taskProgressLabel.setText(I18N.get("ui.task.TaskMonitorDialog.task-progress-goes-here"));
        getContentPane().add(mainPanel,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 0, 15, 15), 0, 0));
        mainPanel.add(labelPanel,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        labelPanel.add(taskProgressLabel,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        labelPanel.add(subtaskProgressLabel,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(cancelButton,
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(clockPanel,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 0, 0));
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        cancelButton.setEnabled(false);
        cancelled = true;
    }

    void this_componentHidden(ComponentEvent e) {
        clockPanel.stop();
        timer.stop();
    }

    void this_componentShown(ComponentEvent e) {
        cancelled = false;
        updateLabels();
        cancelButton.setVisible(false);
        timer.start();
        clockPanel.start();
    }

    private void updateLabels() {
        taskProgressLabel.setText(taskProgress);
        subtaskProgressLabel.setText(subtaskProgress);
    }

    public void setRefreshRate(int millisecondDelay) {
        //This feature was requested by Georgi Kostadinov. [Bob Boseko]
        timer.setDelay(millisecondDelay);
    }

    public void report(final String description) {
        this.taskProgress = description;
        subtaskProgress = "";
    }

    public void report(int subtasksDone, int totalSubtasks,
            String subtaskDescription) {
        subtaskProgress = "";
        subtaskProgress += subtasksDone;

        if (totalSubtasks != -1) {
            subtaskProgress += (" / " + totalSubtasks);
        }

        subtaskProgress += (" " + subtaskDescription);
    }

    public void allowCancellationRequests() {
        cancelButton.setVisible(true);
    }

    public void report(Exception exception) {
        errorHandler.handleThrowable(exception);
    }

    public boolean isCancelRequested() {
        return cancelled;
    }
}
