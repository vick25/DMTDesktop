package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.plugin.WKTDisplayHelper;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * Allows user to enter annotations in Well Known Text format.
 * 
*/
public class EnterWKTDialog extends JDialog {

    private final static int THRESHOLD_WKT_LENGTH = 500;
    JPanel mainPanel = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    private ArrayList actionListeners = new ArrayList();
    JLabel descriptionLabel = new JLabel();
    private boolean syncing = false;
    //Updating the annotations is slow, so wait until the user stops typing. 
    private Timer annotationUpdateTimer =
            GUIUtil.createRestartableSingleEventTimer(500, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            updateAnnotations();
        }
    });
    private WKTDisplayHelper helper = new WKTDisplayHelper();
    private JPanel buttonPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton formatButton = new JButton();
    private OKCancelPanel okCancelPanel = new OKCancelPanel();
    private JButton compressButton = new JButton();
    private JPanel fillerPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private JScrollPane annotationScrollPane = new JScrollPane();
    private JTextArea annotationTextArea = new JTextArea();
    private JTextArea textArea = new JTextArea() {
        public void setText(String t) {
            super.setText(t);
            setCaretPosition(0);
        }
    };
    private JScrollPane mainScrollPane = new JScrollPane();

    public EnterWKTDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                queueAnnotationUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                queueAnnotationUpdate();
            }

            public void changedUpdate(DocumentEvent e) {
                queueAnnotationUpdate();
            }
        });
        //Listen to mainScrollPane to get events when user moves scrollbar.
        //Listen to annotationScrollPane to get events when user edits text.
        //(Can't simply listen to document because
        //BasicScrollPaneUI#syncScrollPaneWithViewport may get called after
        //the document events are fired). [Bob Boseko]
        mainScrollPane.getVerticalScrollBar()
                .addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                syncScrollBars();
            }
        });
        annotationScrollPane.getVerticalScrollBar()
                .addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                syncScrollBars();
            }
        });
    }

    public EnterWKTDialog() {
        this(null, "", true);
    }

    private void syncScrollBars() {
        if (syncing) {
            return;
        }
        syncing = true;
        try {
            annotationScrollPane.getVerticalScrollBar().setValue(
                    Math.max(1, mainScrollPane.getVerticalScrollBar().getValue()));
        } finally {
            syncing = false;
        }
    }

    private void queueAnnotationUpdate() {
        if (textArea.getText().length() < THRESHOLD_WKT_LENGTH) {
            updateAnnotations();
        } else {
            annotationUpdateTimer.restart();
        }
    }

    private void updateAnnotations() {
        if (textArea.getLineWrap()) {
            //User has pressed the Compress button. [Bob Boseko]
            annotationTextArea.setText("");
        } else {
            annotationTextArea.setText(helper.annotate(textArea.getText()));
        }
    }

    public void setDescription(String d) {
        descriptionLabel.setText(d);
    }

    public void setEditable(boolean editable) {
        textArea.setEditable(editable);
        okCancelPanel.setCancelVisible(editable);
        textArea.setOpaque(editable);
    }

    public boolean wasOKPressed() {
        return okCancelPanel.wasOKPressed();
    }

    public void addActionListener(ActionListener l) {
        actionListeners.add(l);
    }

    void jbInit() throws Exception {
        formatButton.setToolTipText(I18N.get("com.osfac.dmt.workbench.ui.EnterWKTDialog.beautify"));
        mainPanel.setLayout(borderLayout1);
        buttonPanel.setLayout(gridBagLayout1);
        formatButton.setText(I18N.get("com.osfac.dmt.workbench.ui.EnterWKTDialog.format"));
        formatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                formatButton_actionPerformed(e);
            }
        });
        okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okCancelPanel_actionPerformed(e);
            }
        });
        compressButton.setText(I18N.get("com.osfac.dmt.workbench.ui.EnterWKTDialog.compress"));
        compressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compressButton_actionPerformed(e);
            }
        });
        fillerPanel.setLayout(gridBagLayout2);
        centerPanel.setLayout(gridBagLayout3);
        annotationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        annotationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        annotationScrollPane.setBorder(null);
        annotationScrollPane.setPreferredSize(new Dimension(73, 21));
        //Must set the minimum size because a GridBagLayout "collapse" will occur
        //if the WKT is higher than the window height. [Bob Boseko]
        annotationScrollPane.setMinimumSize(annotationScrollPane.getPreferredSize());
        annotationTextArea.setMargin(new Insets(0, 5, 0, 0));
        annotationTextArea.setBackground(Color.lightGray);
        //remove setFont so that the same one is used for textArea and annotationTextArea
        //if you want to change the font, change it in both components
        //annotationTextArea.setFont(new java.awt.Font("Monospaced", 1, 12));
        annotationTextArea.setEditable(false);
        textArea.setWrapStyleWord(false);
        textArea.setLineWrap(false);
        mainScrollPane.setBorder(null);
        centerPanel.setBorder(BorderFactory.createEtchedBorder());
        getContentPane().add(mainPanel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(
                formatButton,
                new GridBagConstraints(
                0,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4),
                0,
                0));
        this.getContentPane().add(descriptionLabel, BorderLayout.NORTH);
        buttonPanel.add(
                okCancelPanel,
                new GridBagConstraints(
                3,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        buttonPanel.add(
                compressButton,
                new GridBagConstraints(
                1,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4),
                0,
                0));
        buttonPanel.add(
                fillerPanel,
                new GridBagConstraints(
                2,
                0,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(annotationScrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        centerPanel.add(mainScrollPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        mainScrollPane.getViewport().add(textArea);
        annotationScrollPane.getViewport().add(annotationTextArea);
    }

    public String getText() {
        return textArea.getText();
    }

    /**
     * @param text surround with <HTML> tags if you want word-wrap
     */
    public void setText(String text) {
        textArea.setText(helper.format(text));
    }

    void okCancelPanel_actionPerformed(ActionEvent e) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(e);
        }
        //Let listeners decide whether to close dialog e.g. don't if a parse error
        //occurs [Bob Boseko]
    }

    void formatButton_actionPerformed(ActionEvent e) {
        textArea.setLineWrap(false);
        textArea.setText(helper.format(textArea.getText()));
    }

    public static void main(String[] args) throws Exception {
        new EnterWKTDialog().setVisible(true);
    }

    void compressButton_actionPerformed(ActionEvent e) {
        textArea.setLineWrap(true);
        textArea.setText(compress(textArea.getText()));
    }

    private String compress(String s) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(s);
        while (tokenizer.hasMoreTokens()) {
            buffer.append(tokenizer.nextToken() + " ");
        }
        return buffer.toString().trim();
    }
}
