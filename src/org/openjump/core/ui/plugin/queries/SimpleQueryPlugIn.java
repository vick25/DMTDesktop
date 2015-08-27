package org.openjump.core.ui.plugin.queries;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;

/**
 * SimpleQueryPlugIn is a query editor and processor. It has the following capabilities :
 * <OL><LI>query one or more layers</LI>
 * <LI>attribute queries and spatial queries</LI>
 * <LI>numerical and string functions</LI>
 * <LI>regular expression to find strings</LI>
 * <LI>results as a selection, a table or a new layer</LI></OL>
 * Version 0.2 of the SimpleQueryPlugIn is an adaptation of the original version to the core of
 * OpenJUMP (refactoring, internationalization)
 *
 * @author Micha&euml;l MICHAUD
 * @version 0.2 (16 Oct 2005)
 */
public class SimpleQueryPlugIn extends AbstractPlugIn {

    static QueryDialog queryDialog;

    @Override
    public void initialize(PlugInContext context) throws Exception {

        context.getFeatureInstaller().addMainMenuItemWithJava14Fix(this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_QUERIES}, new StringBuilder(
                        this.getName()).append("...").toString(), false, null, null);
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        if (queryDialog == null) {
            queryDialog = new QueryDialog(context);
        } else {
            queryDialog.setVisible(true);
            // Refresh layer list in case the user switched to another project
            queryDialog.initComboBoxes();
        }
        return false;
    }

    @Override
    public String getName() {
        return I18N.get("org.openjump.core.ui.plugin.queries.SimpleQuery.menuitem");
    }
}
