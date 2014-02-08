package com.osfac.dmt.workbench.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

//<<TODO:DOC>> Create a package comment saying that the classes in this package
//are the framework for context-sensitive enabling of menus. Give an example
//of the use of this framework. [Bob Boseko]
/**
 * A sequence of EnableChecks treated as one.
 */
public class MultiEnableCheck implements EnableCheck {

    private ArrayList enableChecks = new ArrayList();

    /**
     * Create a new MultiEnableCheck
     */
    public MultiEnableCheck() {
    }

    public String check(JComponent component) {
        for (Iterator i = enableChecks.iterator(); i.hasNext();) {
            EnableCheck enableCheck = (EnableCheck) i.next();
            String errorMessage = enableCheck.check(component);

            if (errorMessage != null) {
                return errorMessage;
            }
        }

        return null;
    }

    /**
     * @return this, to allow "method chaining"
     */
    public MultiEnableCheck add(EnableCheck enableCheck) {
        enableChecks.add(enableCheck);

        return this;
    }
}
