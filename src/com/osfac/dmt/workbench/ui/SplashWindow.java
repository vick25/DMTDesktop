package com.osfac.dmt.workbench.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.JWindow;

/**
 * Based on "Java Tip 104: Make a splash with Swing" by Tony Colston
 * (http://www.javaworld.com/javaworld/javatips/jw-javatip104.html)
 */
public class SplashWindow extends JWindow {

    public SplashWindow(JComponent contents) {
        super();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        getContentPane().add(contents, BorderLayout.CENTER);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = contents.getPreferredSize();
        setLocation((screenSize.width / 2) - (labelSize.width / 2),
                (screenSize.height / 2) - (labelSize.height / 2));
    }
}
