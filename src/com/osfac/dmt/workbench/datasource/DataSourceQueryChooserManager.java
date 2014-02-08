package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.util.Blackboard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry for DataSourceQueryChoosers which plug-ins may add to when the
 * JUMP Workbench starts up.
 */
public class DataSourceQueryChooserManager {
    private ArrayList loadDataSourceQueryChoosers = new ArrayList();
    public List getLoadDataSourceQueryChoosers() {
        return Collections.unmodifiableList(loadDataSourceQueryChoosers);
    }
    public DataSourceQueryChooserManager addLoadDataSourceQueryChooser(DataSourceQueryChooser chooser) {
        loadDataSourceQueryChoosers.add(chooser);
        return this;
    }    
    private ArrayList saveDataSourceQueryChoosers = new ArrayList();
    public List getSaveDataSourceQueryChoosers() {
            return Collections.unmodifiableList(saveDataSourceQueryChoosers);
        }
    public DataSourceQueryChooserManager addSaveDataSourceQueryChooser(DataSourceQueryChooser chooser) {
        saveDataSourceQueryChoosers.add(chooser);
        return this;
    }    
    /**
     * @param blackboard typically the Workbench blackboard
     */
    public static DataSourceQueryChooserManager get(Blackboard blackboard) {
        return (DataSourceQueryChooserManager) blackboard.get(DataSourceQueryChooserManager.class.getName() + " - INSTANCE", new DataSourceQueryChooserManager());
    }
}
