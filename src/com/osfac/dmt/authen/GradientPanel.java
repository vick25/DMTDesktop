package com.osfac.dmt.authen;

import com.osfac.dmt.Config;
import com.osfac.dmt.setting.SettingKeyFactory;
import java.awt.*;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Color gradientEnd = Color.black;
        Color gradientStart = null;
        switch (Config.pref.get(SettingKeyFactory.Theme.LOOKANDFEEL, "Gray")) {
            case "Gray":
                gradientStart = new Color(102, 102, 102);
                break;
            case "HomeStead":
                gradientStart = new Color(0, 102, 0);
                break;
            case "Metallic":
                gradientStart = new Color(102, 102, 102);
                break;
            case "NormalColor":
                gradientStart = new Color(4, 24, 82);
                break;
        }
//        Color gradientEnd = new Color(158, 211, 102);//183, 234, 98);

        Graphics2D g2 = (Graphics2D) g;
        GradientPaint painter = new GradientPaint(0, 0, gradientStart,
                0, height, gradientEnd);
        Paint oldPainter = g2.getPaint();
        g2.setPaint(painter);
        g2.fill(g2.getClip());

//        gradientStart = new Color(183, 234, 98, 200);
//        gradientEnd = new Color(220, 255, 149, 255);

        painter = new GradientPaint(0, 0, gradientEnd,
                0, height / 2, gradientStart);
        g2.setPaint(painter);
        g2.fill(g2.getClip());

        painter = new GradientPaint(0, height / 2, gradientStart,
                0, height, gradientEnd);
        g2.setPaint(painter);
        g2.fill(g2.getClip());

        g2.setPaint(oldPainter);
    }
}
