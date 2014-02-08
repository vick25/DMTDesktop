package org.openjump.core.ui.swing.factory.field;

import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.WorkbenchContext;
import java.util.HashMap;
import java.util.Map;
import org.openjump.swing.factory.field.FieldComponentFactory;

public class FieldComponentFactoryRegistry {

    public static final String KEY = FieldComponentFactoryRegistry.class.getName();

    public static void setFactory(final WorkbenchContext context,
            final String type, final FieldComponentFactory factory) {
        Blackboard blackboard = context.getBlackboard();
        Map<String, FieldComponentFactory> fields = getFields(blackboard);
        fields.put(type, factory);
    }

    public static FieldComponentFactory getFactory(
            final WorkbenchContext context, final String type) {
        Blackboard blackboard = context.getBlackboard();
        Map<String, FieldComponentFactory> fields = getFields(blackboard);
        return fields.get(type);
    }

    private static Map<String, FieldComponentFactory> getFields(
            Blackboard blackboard) {
        Map<String, FieldComponentFactory> fields = (Map<String, FieldComponentFactory>) blackboard.get(KEY);
        if (fields == null) {
            fields = new HashMap<>();
            blackboard.put(KEY, fields);
        }
        return fields;
    }
}
