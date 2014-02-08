package org.openjump.swing.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import org.openjump.swing.util.InvokeMethodRunnable;

/**
 * An ActionListener that invokes the method on the object when the action is
 * performed.
 *
 * @author Paul Austin
 */
public class InvokeMethodKeyTypedListener extends KeyAdapter {

    private Runnable runnable;
    private boolean invokeLater;

    public InvokeMethodKeyTypedListener(final Object object,
            final String methodName) {
        this(object, methodName, new Object[0]);
    }

    public InvokeMethodKeyTypedListener(final Object object,
            final String methodName, boolean invokeLater) {
        this(object, methodName, new Object[0], invokeLater);
    }

    public InvokeMethodKeyTypedListener(final Object object,
            final String methodName, Object[] parameters) {
        this(object, methodName, parameters, false);
    }

    public InvokeMethodKeyTypedListener(final Object object,
            final String methodName, Object[] parameters, boolean invokeLater) {
        runnable = new InvokeMethodRunnable(object, methodName, parameters);
        this.invokeLater = invokeLater;
    }

    public void keyTyped(KeyEvent e) {
        if (invokeLater) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }
}
