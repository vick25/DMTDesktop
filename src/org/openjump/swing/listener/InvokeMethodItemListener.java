package org.openjump.swing.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import org.openjump.swing.util.InvokeMethodRunnable;

/**
 * An ActionListener that invokes the method on the object when the action is
 * performed.
 *
 * @author Paul Austin
 */
public class InvokeMethodItemListener implements ItemListener {

    private Runnable runnable;
    private boolean invokeLater;

    public InvokeMethodItemListener(final Object object, final String methodName) {
        this(object, methodName, new Object[0]);
    }

    public InvokeMethodItemListener(final Object object, final String methodName,
            boolean invokeLater) {
        this(object, methodName, new Object[0], invokeLater);
    }

    public InvokeMethodItemListener(final Object object, final String methodName,
            Object[] parameters) {
        this(object, methodName, parameters, false);
    }

    public InvokeMethodItemListener(final Object object, final String methodName,
            Object[] parameters, boolean invokeLater) {
        runnable = new InvokeMethodRunnable(object, methodName, parameters);
        this.invokeLater = invokeLater;
    }

    public void itemStateChanged(ItemEvent e) {
        if (invokeLater) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }
}
