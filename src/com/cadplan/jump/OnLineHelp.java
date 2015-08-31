package com.cadplan.jump;

import com.cadplan.designer.GridBagDesigner;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;

public class OnLineHelp extends JDialog implements ActionListener, WindowListener {

    boolean debug = true;
    JScrollPane scrollPane;
    JButton closeButton;
    JEditorPane editorPane;
    String bookmark;
    URL url = null;
    Window parent;
    String helpFileName = "JumpPrinterHelp.html";
    File inFile;

    public OnLineHelp(JDialog parent, String bookmark) {
        super(parent, "Jump Printer Help", false);
        this.bookmark = bookmark;
        this.parent = parent;
        //int n = bookmark.indexOf(":");
        // this.bookmark = bookmark; //.substring(n+1);
        if (debug) {
            System.out.println("Opening help for: " + bookmark);
        }
        init();
    }

//    public OnlineHelp(JFrame parent, String helpFile)
//    {
//        super(parent, "P-Coder: Java API");
//        this.parent = parent;
//        apiSelected = true;
//        init();
//    }
    public void init() {
        // System.out.println("inFile: "+inFile.getAbsolutePath());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel();
        GridBagDesigner gb = new GridBagDesigner(panel);
        File inFile;
        helpFileName = helpFileName + ":" + bookmark;
        if (debug) {
            System.out.println("helpFileName=" + helpFileName);
        }
        JarURLConnection jarConnection = null;
        try {
            //url = new URL("jar:file:JumpPrinter.jar!/"+helpFileName );
            url = new URL("jar:file:c:/Geoff/JavaPrograms/JumpExt/" + "JumpPrinter.jar!/" + helpFileName);
            if (debug) {
                System.out.println("Loading help from JAR");
            }
            if (debug) {
                System.out.println("URL= " + url);
            }
            jarConnection = (JarURLConnection) url.openConnection();
            if (debug) {
                System.out.println("Loading help from JAR");
            }

        } catch (MalformedURLException e) {
            if (debug) {
                System.out.println("ERROR:" + e);
            }
        } catch (IOException ex) {
            if (debug) {
                System.out.println("ERROR:" + ex);
            }
        }

        try {
            editorPane = new JEditorPane(url);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "WARNING: cannot find Help file \"" + url + "\"", "Warning...", JOptionPane.WARNING_MESSAGE);
            //System.out.println("ERROR: error reading from URL:"+url+", "+ex);
            return;
        }
        editorPane.setEditable(false);
        HyperlinkListener hyperlinkListener = new ActivatedHyperlinkListener(new JFrame(), editorPane);
        editorPane.addHyperlinkListener(hyperlinkListener);

//       editorPane.setContentType("text/html");
        scrollPane = new JScrollPane(editorPane);
        gb.setPosition(0, 0);
        gb.setWeight(1.0, 1.0);
        gb.setFill(GridBagConstraints.BOTH);
        gb.addComponent(scrollPane);

        closeButton = new JButton("Close");
        gb.setPosition(0, 1);
        gb.addComponent(closeButton);
        closeButton.addActionListener(this);

        addWindowListener(this);
        getContentPane().add(panel);
        setSize(500, 300);
        //setSize(800,800);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == closeButton) {
            dispose();
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    public void windowOpening(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
