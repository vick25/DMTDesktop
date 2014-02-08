package com.osfac.dmt.workbench.plugin.util;

/**
 * Utility methods for generating layer names.
 *
 * @author Martin Davis
 * @version 1.0
 */
public class LayerNameGenerator {

    /**
     * Generates a name for a layer containing features produced by running an
     * operation on another layer.
     *
     * @param opName a short name for the operation
     * @param srcLayerName the input layer name
     * @return a new layer name
     */
    public static String generateOperationOnLayerName(String opName, String srcLayerName) {
        return srcLayerName + "-" + opName;
    }
}