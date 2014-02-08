package com.osfac.dmt.workbench.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

public class TextFrame extends JInternalFrame {

    private static Logger LOG = Logger.getLogger(TextFrame.class);
    BorderLayout borderLayout1 = new BorderLayout();
    private OKCancelPanel okCancelPanel = new OKCancelPanel();
    protected JPanel scrollPanePanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane();
    GridBagLayout gridBagLayout = new GridBagLayout();
    private JEditorPane editorPane = new JEditorPane();
    private ErrorHandler errorHandler;

    public TextFrame(ErrorHandler errorHandler) {
        this(false, errorHandler);
    }

    public TextFrame(boolean showingButtons, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;

        try {
            jbInit();
            okCancelPanel.setVisible(showingButtons);
        } catch (Exception e) {
            errorHandler.handleThrowable(e);
        }
    }

    public OKCancelPanel getOKCancelPanel() {
        return okCancelPanel;
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setSize(500, 300);
        scrollPanePanel.setLayout(gridBagLayout);
        editorPane.setBackground(UIManager.getColor("inactiveCaptionBorder"));
        editorPane.setText("jEditorPane1");
        editorPane.setContentType("text/html");
        getContentPane().add(getOKCancelPanel(), BorderLayout.SOUTH);
        this.getContentPane().add(scrollPanePanel, BorderLayout.CENTER);
        scrollPanePanel.add(scrollPane,
                new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        scrollPane.getViewport().add(editorPane, null);
    }

    public void clear() {
        setText("");
    }

    public void setText(final String s) {
        try {
            editorPane.setText(s);
            editorPane.setCaretPosition(0);
        } catch (Throwable t) {
            LOG.error(s);
            errorHandler.handleThrowable(t);
        }
    }

    public String getText() {
        return editorPane.getText();
    }
}
