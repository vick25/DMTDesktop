package org.openjump.swing.listener;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openjump.swing.util.InvokeMethodRunnable;

/**
 * An ActionListener that invokes the method on the object when the action is
 * performed.
 *
 * @author Paul Austin
 */
public class InvokeMethodListSelectionListener implements ListSelectionListener {

    private Runnable runnable;
    private boolean invokeLater;

    public InvokeMethodListSelectionListener(final Object object, final String methodName) {
        this(object, methodName, new Object[0]);
    }

    public InvokeMethodListSelectionListener(final Object object,
            final String methodName, boolean invokeLater) {
        this(object, methodName, new Object[0], invokeLater);
    }

    public InvokeMethodListSelectionListener(final Object object,
            final String methodName, Object[] parameters) {
        this(object, methodName, parameters, false);
    }

    public InvokeMethodListSelectionListener(final Object object,
            final String methodName, Object[] parameters, boolean invokeLater) {
        runnable = new InvokeMethodRunnable(object, methodName, parameters);
        this.invokeLater = invokeLater;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (invokeLater) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }
}
