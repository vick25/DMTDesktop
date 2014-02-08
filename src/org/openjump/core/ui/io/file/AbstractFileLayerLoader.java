package org.openjump.core.ui.io.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A base implementation of {@link FileLayerLoader}.
 *
 * @author Paul Austin
 */
public abstract class AbstractFileLayerLoader implements FileLayerLoader {

    /**
     * The descriptive name of the file format (e.g. ESRI Shapefile).
     */
    private String description;
    /**
     * The list of file extensions supported by the plug-in.
     */
    private List<String> extensions = new ArrayList<>();
    /**
     * The list of Options supported by the plug-in.
     */
    private List<Option> optionMetadata = new ArrayList<>();

    /**
     * Construct a new AbstractFileLayerLoader with a description and list of
     * extensions.
     *
     * @param description The format description.
     * @param extensions The supported file extension.
     */
    public AbstractFileLayerLoader(final String description,
            final List<String> extensions) {
        this.description = description;
        addFileExtensions(extensions);
    }

    /**
     * Get the descriptive name of the file format (e.g. ESRI Shapefile).
     *
     * @return The file format name.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the list of file extensions supported by the plug-in.
     *
     * @return The list of file extensions.
     */
    public Collection<String> getFileExtensions() {
        return extensions;
    }

    /**
     * Add to the list of file extensions supported by the plug-in.
     *
     * @param newexts list of extensions
     * @return true on success
     */
    public boolean addFileExtensions(final List<String> newexts) {
        return extensions.addAll(newexts);
    }

    /**
     * Add an option.
     *
     * @param name The name of the option.
     * @param type The type of the option.
     * @param required True if a value for the option is required.
     * @see Option
     */
    public void addOption(String name, String type, boolean required) {
        optionMetadata.add(new Option(name, type, required));
    }

    /**
     * Add an option.
     *
     * @param name The name of the option.
     * @param type The type of the option.
     * @param defaultValue The defaultValue of the option.
     * @param required True if a value for the option is required.
     * @see Option
     */
    public void addOption(String name, String type, Object defaultValue, boolean required) {
        optionMetadata.add(new Option(name, type, defaultValue, required));
    }

    /**
     * Remove an option.
     *
     * @param name The name of the option.
     * @param type The type of the option.
     * @param required True if a value for the option is required.
     * @return true if this list contained the specified option
     * @see Option
     */
    public boolean removeOption(String name, String type, boolean required) {
        return optionMetadata.remove(new Option(name, type, required));
    }

    /**
     * Remove an option.
     *
     * @param name The name of the option.
     * @param type The type of the option.
     * @param defaultValue The defaultValue of the option.
     * @param required True if a value for the option is required.
     * @see Option
     */
    public boolean removeOption(String name, String type, Object defaultValue, boolean required) {
        return optionMetadata.remove(new Option(name, type, defaultValue, required));
    }

    /**
     * Get the list of Options supported by the plug-in.
     *
     * @return The list of Options.
     */
    public List<Option> getOptionMetadata() {
        return optionMetadata;
    }

    /**
     * Return a string representation of the loader.
     *
     * @return The string.
     */
    public String toString() {
        return description;
    }
}
