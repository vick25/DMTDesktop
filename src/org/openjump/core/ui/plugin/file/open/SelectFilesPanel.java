package org.openjump.core.ui.plugin.file.open;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.datasource.LoadFileDataSourceQueryChooser;
import com.osfac.dmt.workbench.registry.Registry;
import com.osfac.dmt.workbench.ui.InputChangedListener;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.osfac.dmt.workbench.ui.wizard.WizardDialog;
import com.osfac.dmt.workbench.ui.wizard.WizardPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openjump.core.ui.io.file.FileLayerLoader;
import org.openjump.core.ui.io.file.FileLayerLoaderExtensionFilter;
import org.openjump.core.ui.io.file.FileNameExtensionFilter;
import org.openjump.swing.listener.InvokeMethodActionListener;

public class SelectFilesPanel extends JFileChooser implements WizardPanel {

    public static final String KEY = SelectFilesPanel.class.getName();
    public static final String TITLE = I18N.get(KEY);
    public static final String INSTRUCTIONS = I18N.get(KEY + ".instructions");
    public static final String ALL_FILES = I18N.get(KEY + ".all-files");
    public static final String ALL_SUPPORTED_FILES = I18N.get(KEY + ".all-supported-files");
    public static final String ARCHIVED_FILES = I18N.get(KEY + ".archived-files");
    private Set<InputChangedListener> listeners = new LinkedHashSet<>();
    private Blackboard blackboard;
    private OpenFileWizardState state;
    private WorkbenchContext workbenchContext;
    private boolean initialized = false;
    private WizardDialog dialog;

    public SelectFilesPanel(final WorkbenchContext workbenchContext) {
        this.workbenchContext = workbenchContext;
    }

    public OpenFileWizardState getState() {
        return state;
    }

    public void setState(OpenFileWizardState state) {
        this.state = state;
        for (FileFilter filter : getChoosableFileFilters()) {
            removeChoosableFileFilter(filter);
        }
    }

    public WizardDialog getDialog() {
        return dialog;
    }

    public void setDialog(WizardDialog dialog) {
        this.dialog = dialog;
    }

    private void initialize() {
        initialized = true;
        blackboard = PersistentBlackboardPlugIn.get(workbenchContext);
        Registry registry = workbenchContext.getRegistry();

        String savedDirectoryName = (String) blackboard.get(LoadFileDataSourceQueryChooser.FILE_CHOOSER_DIRECTORY_KEY);
        if (savedDirectoryName != null) {
            setCurrentDirectory(new File(savedDirectoryName));
        }

        setAcceptAllFileFilterUsed(false);
        setMultiSelectionEnabled(true);
        List loaders = registry.getEntries(FileLayerLoader.KEY);
        Set<String> allExtensions = new TreeSet<>();
        Map<String, FileFilter> filters = new TreeMap<>();

        // zip support is hardcoded in OpenFileWizard.run()
        String[] zipExtensions = new String[]{"zip", "gz"};
        allExtensions.addAll(Arrays.asList(zipExtensions));
        FileFilter zipFilter = new FileNameExtensionFilter(ARCHIVED_FILES, zipExtensions);
        filters.put(zipFilter.getDescription(), zipFilter);

        for (Object loader : loaders) {
            final FileLayerLoader fileLayerLoader = (FileLayerLoader) loader;
            FileFilter filter = new FileLayerLoaderExtensionFilter(fileLayerLoader);
            allExtensions.addAll(fileLayerLoader.getFileExtensions());
            filters.put(filter.getDescription(), filter);
        }

        // ATTENTION: ALL and ALL_SUPPORTED have leading spaces so they get sorted to the 
        //            beginning of the formats list regardless of translations first character ;) 
        FileFilter filterNone = new FileNameExtensionFilter(" " + ALL_FILES, new String[]{});
        filters.put(filterNone.getDescription(), filterNone);

        FileFilter allFilter = new FileNameExtensionFilter(" " + ALL_SUPPORTED_FILES,
                allExtensions.toArray(new String[0]));
        filters.put(allFilter.getDescription(), allFilter);

        // add all filters from above
        for (FileFilter filter : filters.values()) {
            addChoosableFileFilter(filter);
        }

        setFileFilter(allFilter);

        setControlButtonsAreShown(false);

        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                FileLayerLoader fileLayerLoader = null;
                File[] files = getSelectedFiles();
                FileFilter selectedFileFilter = getFileFilter();
                if (selectedFileFilter instanceof FileLayerLoaderExtensionFilter) {
                    FileLayerLoaderExtensionFilter filter = (FileLayerLoaderExtensionFilter) selectedFileFilter;
                    fileLayerLoader = filter.getFileLoader();
                }
                state.setupFileLoaders(files, fileLayerLoader);
                fireInputChanged();
            }
        });

        addActionListener(new InvokeMethodActionListener(dialog, "next"));
    }

    public void enteredFromLeft(final Map dataMap) {
        initialize();
        rescanCurrentDirectory();
        state.setCurrentPanel(KEY);
    }

    public void exitingToRight() throws Exception {
        blackboard.put(LoadFileDataSourceQueryChooser.FILE_CHOOSER_DIRECTORY_KEY,
                getCurrentDirectory().getAbsolutePath());
    }

    public String getID() {
        return getClass().getName();
    }

    public String getInstructions() {
        return INSTRUCTIONS;
    }

    public String getNextID() {
        return state.getNextPanel(KEY);
    }

    public String getTitle() {
        return TITLE;
    }

    public boolean isInputValid() {
        return state.hasSelectedFiles();
    }

    public void add(InputChangedListener listener) {
        listeners.add(listener);
    }

    public void remove(InputChangedListener listener) {
        listeners.remove(listener);
    }

    private void fireInputChanged() {
        for (InputChangedListener listener : listeners) {
            listener.inputChanged();
        }
    }
}
