package org.openjump.core.ui.swing;

import com.osfac.dmt.workbench.ui.ErrorHandler;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Component including a Label and a ComboBox
 *
 * @author Michael Michaud
 * @version 0.1.0
 */
public class ComboBoxComponentPanel extends JPanel {

    private JLabel label;
    private JComboBox comboBox;
    private ErrorHandler errorHandler;

    public ComboBoxComponentPanel(String descriptionLabel,
            Object[] items, ErrorHandler errorHandler) {
        this.label = new JLabel(descriptionLabel);
        this.errorHandler = errorHandler;
        try {
            comboBox = new JComboBox(new DefaultComboBoxModel(items));
            this.add(label);
            this.add(comboBox);
        } catch (Throwable t) {
            errorHandler.handleThrowable(t);
        }
    }

    public JComboBox getComboBox() {
        return comboBox;
    }

    public Object getSelectedItem() {
        return comboBox == null ? null : comboBox.getSelectedItem();
    }
}