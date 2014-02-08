package org.openjump.core.ui.io.file;

import com.osfac.dmt.task.TaskMonitor;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p> The FileLayerLoader defines the interface for plug-ins that can load
 * files into the current Task. </p> <p> A file loader has a
 * {@link #getDescription()} used in the GUI and a list of
 * {@link #getFileExtensions()} that it can be used to load. </p> <p> The
 * {@link #getOptionMetadata()} can be used to define a list of {@link Option}s
 * that a user can/must provide when loading the file. These will be used by the
 * GUI to create fields for entry of these options. </p>
 *
 * @author Paul Austin
 */
public interface FileLayerLoader {

    /**
     * The key in the registry where loaders are registered.
     */
    String KEY = FileLayerLoader.class.getName();

    /**
     * Get the list of file extensions supported by the plug-in.
     *
     * @return The list of file extensions.
     */
    Collection<String> getFileExtensions();

    /**
     * Get the descriptive name of the file format (e.g. ESRI Shapefile).
     *
     * @return The file format name.
     */
    String getDescription();

    /**
     * Open the file specified by the URI with the map of option values.
     *
     * @param monitor The TaskMonitor.
     * @param uri The URI to the file to load.
     * @param options The map of options.
     * @return True if the file could be loaded false otherwise.
     * @throws Exception
     */
    boolean open(TaskMonitor monitor, URI uri, Map<String, Object> options) throws Exception;

    /**
     * Get the list of Options supported by the plug-in.
     *
     * @return The list of Options.
     */
    List<Option> getOptionMetadata();
}
