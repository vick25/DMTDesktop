package com.osfac.dmt.authen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

public class FadingPanel extends JComponent implements ActionListener {

    private Timer ticker = null;
    private int alpha = 0;
    private int step;
    private FadeListener fadeListener;

    public FadingPanel(FadeListener fadeListener) {
        this.fadeListener = fadeListener;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            if (ticker != null) {
                ticker.stop();
            }
            alpha = 0;
            step = 25;
            ticker = new Timer(50, this);
            ticker.start();
        } else {
            if (ticker != null) {
                ticker.stop();
                ticker = null;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(255, 255, 255, alpha));
        Rectangle clip = g.getClipBounds();
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

    public void switchDirection() {
        step = -step;
        ticker.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        alpha += step;
        if (alpha >= 255) {
            alpha = 255;
            ticker.stop();
            fadeListener.fadeOutFinished();
        } else if (alpha < 0) {
            alpha = 0;
            ticker.stop();
            fadeListener.fadeInFinished();
        }
        repaint();
    }
}
