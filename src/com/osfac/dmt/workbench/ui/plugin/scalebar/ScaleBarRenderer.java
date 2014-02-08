package com.osfac.dmt.workbench.ui.plugin.scalebar;

import com.osfac.dmt.util.MathUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.renderer.SimpleRenderer;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class ScaleBarRenderer extends SimpleRenderer {

    public static String CONTENT_ID = "SCALE_BAR";
    /**
     * Height of the increment boxes, in view-space units.
     */
    private static int BAR_HEIGHT = 10;
    private static Color FILL2 = new Color(255, 204, 204);
    private static Color FILL1 = Color.white;
    /**
     * Distance from the right edge, in view-space units.
     */
    private static int HORIZONTAL_MARGIN = 3;
    /**
     * In view-space units; the actual increment may be a bit larger or smaller
     * than this amount.
     */
    private static int IDEAL_INCREMENT = 75;
    private static Color LINE_COLOR = Color.black;
    private static int TEXT_BOTTOM_MARGIN = 1;
    private static int UNIT_TEXT_BOTTOM_MARGIN = 1;
    private static Color TEXT_COLOR = Color.black;
    private static Color UNIT_TEXT_COLOR = Color.blue;
    /**
     * Distance from the bottom edge, in view-space units.
     */
    private static int VERTICAL_MARGIN = 3;
    private static String ENABLED_KEY = ScaleBarRenderer.class + " - ENABLED";
    private static int INCREMENT_COUNT = 5;
    private static Font FONT = new Font("Dialog", Font.PLAIN, 10);
    private static Font UNIT_FONT = new Font("Dialog", Font.BOLD, 11);

    public ScaleBarRenderer(LayerViewPanel panel) {
        super(CONTENT_ID, panel);
    }

    public static boolean isEnabled(LayerViewPanel panel) {
        return panel.getBlackboard().get(ENABLED_KEY, false);
    }

    public static void setEnabled(boolean enabled, LayerViewPanel panel) {
        panel.getBlackboard().put(ENABLED_KEY, enabled);
    }
    private Stroke stroke = new BasicStroke();

    protected void paint(Graphics2D g) {
        paint(g, panel.getViewport().getScale());
    }

    public void paint(Graphics2D g, double scale) {
        if (!isEnabled(panel)) {
            return;
        }
        //Override dashes set in GridRenderer [Bob Boseko]
        g.setStroke(stroke);

        RoundQuantity increment =
                new IncrementChooser().chooseGoodIncrement(
                new MetricSystem(1).createUnits(),
                IDEAL_INCREMENT / scale);
        paintIncrements(increment, INCREMENT_COUNT, g, scale);
    }

    private int barBottom() {
        return panel.getHeight() - VERTICAL_MARGIN;
    }

    private int barTop() {
        return barBottom() - BAR_HEIGHT;
    }

    private TextLayout createTextLayout(String text, Font font, Graphics2D g) {
        return new TextLayout(text, font, g.getFontRenderContext());
    }

    private void paintIncrement(int i, RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
        Rectangle2D.Double shape =
                new Rectangle2D.Double(
                x(i, increment, incrementCount, scale),
                barTop(),
                x(i + 1, increment, incrementCount, scale) - x(i, increment, incrementCount, scale),
                barBottom() - barTop());
        g.setColor(((i % 2) == 0) ? FILL1 : FILL2);
        g.fill(shape);
        g.setColor(LINE_COLOR);
        g.draw(shape);
    }

    private void paintIncrements(RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
        for (int i = 0; i < incrementCount; i++) {
            paintIncrement(i, increment, incrementCount, g, scale);
            paintLabel(i, increment, incrementCount, g, scale);
        }
    }

    private void paintLabel(int i, RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
        String text =
                new RoundQuantity(
                increment.getMantissa() * (i + 1),
                increment.getExponent(),
                increment.getUnit()).getAmountString();
        Font font = FONT;
        g.setColor(TEXT_COLOR);

        int textBottomMargin = TEXT_BOTTOM_MARGIN;

        if (i == (incrementCount - 1)) {
            text = increment.getUnit().getName();
            font = UNIT_FONT;
            g.setColor(UNIT_TEXT_COLOR);
            textBottomMargin = UNIT_TEXT_BOTTOM_MARGIN;
        }

        TextLayout layout = createTextLayout(text, font, g);
        double center =
                MathUtil.avg(x(i, increment, incrementCount, scale), x(i + 1, increment, incrementCount, scale));
        layout.draw(
                g,
                (float) (center - (layout.getAdvance() / 2)),
                (float) (barBottom() - textBottomMargin));
    }

    private double x(int i, RoundQuantity increment, int incrementCount, double scale) {
        return HORIZONTAL_MARGIN + (i * increment.getModelValue() * scale);
    }

    public static int getBAR_HEIGHT() {
        return BAR_HEIGHT;
    }

    public static void setBAR_HEIGHT(int bar_height) {
        BAR_HEIGHT = bar_height;
    }

    public static Color getFILL1() {
        return FILL1;
    }

    public static void setFILL1(Color fill1) {
        FILL1 = fill1;
    }

    public static Color getFILL2() {
        return FILL2;
    }

    public static void setFILL2(Color fill2) {
        FILL2 = fill2;
    }

    public static int getHORIZONTAL_MARGIN() {
        return HORIZONTAL_MARGIN;
    }

    public static void setHORIZONTAL_MARGIN(int horizontal_margin) {
        HORIZONTAL_MARGIN = horizontal_margin;
    }

    public static Color getLINE_COLOR() {
        return LINE_COLOR;
    }

    public static void setLINE_COLOR(Color line_color) {
        LINE_COLOR = line_color;
    }

    public static int getTEXT_BOTTOM_MARGIN() {
        return TEXT_BOTTOM_MARGIN;
    }

    public static void setTEXT_BOTTOM_MARGIN(int text_bottom_margin) {
        TEXT_BOTTOM_MARGIN = text_bottom_margin;
    }

    public static Color getTEXT_COLOR() {
        return TEXT_COLOR;
    }

    public static void setTEXT_COLOR(Color text_color) {
        TEXT_COLOR = text_color;
    }

    public static int getUNIT_TEXT_BOTTOM_MARGIN() {
        return UNIT_TEXT_BOTTOM_MARGIN;
    }

    public static void setUNIT_TEXT_BOTTOM_MARGIN(int unit_text_bottom_margin) {
        UNIT_TEXT_BOTTOM_MARGIN = unit_text_bottom_margin;
    }

    public static Color getUNIT_TEXT_COLOR() {
        return UNIT_TEXT_COLOR;
    }

    public static void setUNIT_TEXT_COLOR(Color unit_text_color) {
        UNIT_TEXT_COLOR = unit_text_color;
    }

    public static int getVERTICAL_MARGIN() {
        return VERTICAL_MARGIN;
    }

    public static void setVERTICAL_MARGIN(int vertical_margin) {
        VERTICAL_MARGIN = vertical_margin;
    }

    public static Font getFONT() {
        return FONT;
    }

    public static void setFONT(Font font) {
        FONT = font;
    }

    public static Font getUNIT_FONT() {
        return UNIT_FONT;
    }

    public static void setUNIT_FONT(Font unit_font) {
        UNIT_FONT = unit_font;
    }
}
