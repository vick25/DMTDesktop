package com.osfac.dmt.workbench.imagery;

import com.osfac.dmt.workbench.WorkbenchContext;

/**
 * A factory for {@link ReferencedImage}s.
 */
public interface ReferencedImageFactory {

    public static final Object REGISTRY_CLASSIFICATION = ReferencedImageFactory.class.getName();

    String getTypeName();

    ReferencedImage createImage(String location) throws Exception;

    public String getDescription();

    public String[] getExtensions();

    public boolean isEditableImage(String location);

    /**
     *
     * @param wbContext can be null, depending on the implementation (e.g. not
     * null for MrSid driver)
     * @return true if it is available
     */
    boolean isAvailable(WorkbenchContext wbContext);
}