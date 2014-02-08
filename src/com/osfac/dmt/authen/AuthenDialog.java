package com.osfac.dmt.authen;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.DMTIconsFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class AuthenDialog extends JFrame implements FadeListener {

    public AuthenDialog() {
        this(null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public AuthenDialog(final JWindow splashWindow) {
        this.glassPane = new FadingPanel(this);
        this.setGlassPane(glassPane);
        this.buildContentPane();
        this.buildLoginForm();
        this.startAnimation();
        this.setSize(new Dimension(400, 190));
        this.setResizable(false);
        this.setTitle(I18N.get("com.osfac.dmt.authen.AuthenDialog-Title-of-frame"));
        this.setIconImage(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.ICON).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(this);
        this.frame = this;
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                if (splashWindow != null) {
                    splashWindow.setVisible(false);
                }
            }
        });
    }

    private void buildContentPane() {
        contentPane = new CurvesPanel();
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);
    }

    private void startAnimation() {
        Timer animation = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contentPane.repaint();
            }
        });
        animation.start();
    }

    private void buildLoginForm() {
        PanAuthen form = new PanAuthen(glassPane, this);
        form.setOpaque(false);
        contentPane.add(form);
    }

    public void fadeInFinished() {
        glassPane.setVisible(false);
    }

    public void fadeOutFinished() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                contentPane = new CirclesPanel();
                contentPane.setLayout(new BorderLayout());
                WaitAnimation waitAnimation = new WaitAnimation(frame);
                contentPane.add(waitAnimation, BorderLayout.CENTER);
                setContentPane(contentPane);
                validate();
                glassPane.switchDirection();
            }
        });
    }
    private JComponent contentPane;
    private FadingPanel glassPane;
    private JFrame frame;
}
