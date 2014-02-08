package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedBasePlugIn;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

public abstract class AbstractLoadSaveDatasetPlugIn extends ThreadedBasePlugIn {

    private WorkbenchContext context;

    protected String getLastFormatKey() {
        return getClass().getName() + " - LAST FORMAT";
    }

    protected String getLastDirectoryKey() {
        return getClass().getName() + " - LAST DIRECTORY";
    }

    public void initialize(final PlugInContext context) throws Exception {
        this.context = context.getWorkbenchContext();
        //Give other plug-ins a chance to add DataSourceQueryChoosers
        //before the dialog is realized. [Bob Boseko]
        context.getWorkbenchFrame().addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                String format = (String) PersistentBlackboardPlugIn.get(context.getWorkbenchContext())
                        .get(getLastFormatKey());
                if (format != null) {
                    setSelectedFormat(format);
                }
            }
        });
    }

    protected abstract void setSelectedFormat(String format);

    protected abstract String getSelectedFormat();

    protected WorkbenchContext getContext() {
        return context;
    }
    private Collection dataSourceQueries;

    public boolean execute(PlugInContext context) throws Exception {
        dataSourceQueries = showDialog(context.getWorkbenchContext());
        if (dataSourceQueries != null) {
            PersistentBlackboardPlugIn.get(context.getWorkbenchContext()).put(getLastFormatKey(),
                    getSelectedFormat());
        }
        return dataSourceQueries != null;
    }

    protected abstract Collection showDialog(WorkbenchContext context);

    protected Collection getDataSourceQueries() {
        return dataSourceQueries;
    }
}
