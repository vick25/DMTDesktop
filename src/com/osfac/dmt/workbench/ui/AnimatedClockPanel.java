package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Implements an animated clock.
 */
public class AnimatedClockPanel extends JPanel {

    private ArrayList queue = new ArrayList();
    private Timer timer = new Timer(250, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nextImage();
        }
    });
    private JLabel label = new JLabel();
    private BorderLayout borderLayout1 = new BorderLayout();

    public AnimatedClockPanel() {
        add("ClockN.gif");
        add("ClockNE.gif");
        add("ClockE.gif");
        add("ClockSE.gif");
        add("ClockS.gif");
        add("ClockSW.gif");
        add("ClockW.gif");
        add("ClockNW.gif");

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void add(String icon) {
        queue.add(IconLoader.icon(icon));
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void nextImage() {
        ImageIcon icon = (ImageIcon) queue.remove(0);
        queue.add(icon);
        label.setIcon(icon);
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.add(label, BorderLayout.CENTER);
        label.setIcon(IconLoader.icon("ClockN.gif"));
    }

    public static void main(String[] args) {
        AnimatedClockPanel p = new AnimatedClockPanel();
        p.start();

        JFrame f = new JFrame();
        f.getContentPane().add(p);
        f.setVisible(true);
    }
}
