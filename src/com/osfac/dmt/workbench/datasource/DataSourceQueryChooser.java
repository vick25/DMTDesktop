package com.osfac.dmt.workbench.datasource;

import java.awt.Component;
import java.util.Collection;

/**
 * UI for picking datasets for a given format. Ideally allows a user to select multiple datasets.
 * Produces {@link com.osfac.dmt.io.datasource.DataSourceQuery DataSourceQueries} each of which
 * encapsulates a query string and the DataSource to run it against.
 */
public interface DataSourceQueryChooser {

    public Component getComponent();

    public Collection getDataSourceQueries();

    /**
     * @return a brief description of the dataset type, suitable for display in a combo box.
     */
    @Override
    public String toString();

    /**
     * The user has pressed the OK button.
     */
    public boolean isInputValid();
}
