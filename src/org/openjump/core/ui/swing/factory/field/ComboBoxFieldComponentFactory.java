package org.openjump.core.ui.swing.factory.field;

import com.osfac.dmt.workbench.WorkbenchContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.openjump.core.ui.swing.ComboBoxComponentPanel;
import org.openjump.swing.factory.field.FieldComponentFactory;
import org.openjump.swing.listener.ValueChangeEvent;
import org.openjump.swing.listener.ValueChangeListener;

/**
 * Factory to build a combobox component.
 *
 * @author Michael Michaud
 * @version 0.1.0
 */
public class ComboBoxFieldComponentFactory implements FieldComponentFactory {

    private WorkbenchContext workbenchContext;
    private String option;
    private Object[] items;

    public ComboBoxFieldComponentFactory(final WorkbenchContext workbenchContext) {
        this.workbenchContext = workbenchContext;
    }

    public ComboBoxFieldComponentFactory(final WorkbenchContext workbenchContext,
            final String option, final Object[] items) {
        this.workbenchContext = workbenchContext;
        this.option = option;
        this.items = items;
    }

    public Object getValue(final JComponent component) {
        if (component instanceof ComboBoxComponentPanel) {
            ComboBoxComponentPanel chooser = (ComboBoxComponentPanel) component;
            return chooser.getSelectedItem();
        }
        return null;
    }

    public void setValue(JComponent component, Object value) {
        if (component instanceof ComboBoxComponentPanel) {
            ComboBoxComponentPanel chooser = (ComboBoxComponentPanel) component;
            if (value != null) {
                chooser.getComboBox().setSelectedItem(value);
            }
        }
    }

    public JComponent createComponent() {
        ComboBoxComponentPanel chooser = new ComboBoxComponentPanel(
                option,
                items,
                workbenchContext.getErrorHandler());
        return chooser;
    }

    public JComponent createComponent(final ValueChangeListener listener) {
        final ComboBoxComponentPanel chooser = new ComboBoxComponentPanel(
                option,
                items,
                workbenchContext.getErrorHandler());
        chooser.getComboBox().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object item = chooser.getSelectedItem();
                listener.valueChanged(new ValueChangeEvent(chooser, item));
            }
        });
        return chooser;
    }
}