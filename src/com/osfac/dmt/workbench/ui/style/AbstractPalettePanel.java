package com.osfac.dmt.workbench.ui.style;

import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.util.java2xml.XML2Java;
import com.osfac.dmt.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

public abstract class AbstractPalettePanel extends JPanel {

    protected ArrayList listeners = new ArrayList();

    public static class BasicStyleList {

        private ArrayList basicStyles = new ArrayList();

        public List getBasicStyles() {
            return Collections.unmodifiableList(basicStyles);
        }

        public void addBasicStyle(BasicStyle basicStyle) {
            basicStyles.add(basicStyle);
        }
    }

    public static interface Listener {

        public void basicStyleChosen(BasicStyle basicStyle);
    }

    public void add(Listener listener) {
        listeners.add(listener);
    }

    public abstract void setAlpha(int alpha);

    protected void fireBasicStyleChosen(BasicStyle basicStyle) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener listener = (Listener) i.next();
            listener.basicStyleChosen(basicStyle);
        }
    }

    public static List basicStyles() {
        try {
            if (basicStyleList == null) {
                InputStream stream =
                        AbstractPalettePanel.class.getResourceAsStream(
                        StringUtil.classNameWithoutQualifiers(
                        AbstractPalettePanel.class.getName())
                        + ".xml");
                try {
                    InputStreamReader reader = new InputStreamReader(stream);
                    try {
                        basicStyleList =
                                ((BasicStyleList) new XML2Java()
                                .read(reader, BasicStyleList.class));
                    } finally {
                        reader.close();
                    }
                } finally {
                    stream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.shouldNeverReachHere();
            return null;
        }
        return basicStyleList.getBasicStyles();
    }
    private static BasicStyleList basicStyleList = null;

    public static void main(String[] args) {
        Color c = new Color(255, 28, 174).darker();
        System.out.println(c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
    }
}
