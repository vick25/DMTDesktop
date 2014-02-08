package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.workbench.ui.ColorPanel;
import com.osfac.dmt.workbench.ui.GUIUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class BasicStyleListCellRenderer implements ListCellRenderer {

    protected ColorPanel colorPanel = new ColorPanel();
    private JPanel panel = new JPanel();
    protected DefaultListCellRenderer defaultListCellRenderer =
            new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(
                        JList list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {
                    JLabel label =
                            (JLabel) super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus);
                    label.setFont(label.getFont().deriveFont(0, 10));
                    return label;
                }
            };

    {
        panel.setLayout(new GridBagLayout());
        panel.add(
                colorPanel,
                new GridBagConstraints(
                0,
                0,
                1,
                1,
                1,
                0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2),
                0,
                0));
        setColorPanelSize(new Dimension(45, 8));
    }

    protected void setColorPanelSize(Dimension d) {
        colorPanel.setMinimumSize(d);
        colorPanel.setMaximumSize(d);
        colorPanel.setPreferredSize(d);
    }
    private int alpha = 255;

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof String) {
            //Get here for "Custom..." [Bob Boseko]
            return defaultListCellRenderer.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus);
        }
        BasicStyle basicStyle = (BasicStyle) value;
        //colorPanel.setLineWidth(Math.min(3, basicStyle.getLineWidth()));
        //colorPanel.setLineStroke(basicStyle.getLineStroke());
        colorPanel.setStyle(basicStyle);
        colorPanel.setLineColor(
                basicStyle instanceof BasicStyle
                && ((BasicStyle) basicStyle).isRenderingLine()
                ? GUIUtil.alphaColor(
                ((BasicStyle) basicStyle).getLineColor(),
                alpha)
                : (isSelected
                ? list.getSelectionBackground()
                : list.getBackground()));
        colorPanel.setFillColor(
                basicStyle instanceof BasicStyle
                && ((BasicStyle) basicStyle).isRenderingFill()
                ? GUIUtil.alphaColor(
                ((BasicStyle) basicStyle).getFillColor(),
                alpha)
                : (isSelected
                ? list.getSelectionBackground()
                : list.getBackground()));
        if (isSelected) {
            colorPanel.setForeground(list.getSelectionForeground());
            colorPanel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
            panel.setBackground(list.getSelectionBackground());
        } else {
            colorPanel.setForeground(list.getForeground());
            colorPanel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
            panel.setBackground(list.getBackground());
        }
        return panel;
    }
}
