package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.FlexibleDateParser;
import com.osfac.dmt.workbench.ui.GenericNames;
import com.vividsolutions.jts.geom.Geometry;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A function object for {@link Geometry} functions (which return a Geometry).
 * Provides metadata about the function.
 *
 * @author Martin Davis
 * @version 1.0
 */
public abstract class AttributePredicate {

    static AttributePredicate[] method = {
        new EqualPredicate(),
        new NotEqualPredicate(),
        new LessThanPredicate(),
        new LessThanOrEqualPredicate(),
        new GreaterThanPredicate(),
        new GreaterThanOrEqualPredicate(),
        new ContainsPredicate(),
        new StartsWithPredicate(),};

    static List getNames() {
        List names = new ArrayList();
        for (int i = 0; i < method.length; i++) {
            names.add(method[i].name);
        }
        return names;
    }

    static AttributePredicate getPredicate(String name) {
        for (int i = 0; i < method.length; i++) {
            if (method[i].name.equals(name)) {
                return method[i];
            }
        }
        return null;
    }
    private static FlexibleDateParser dateParser = new FlexibleDateParser();
    private String name;
    private String description;

    public AttributePredicate(String name) {
        this(name, null);
    }

    public AttributePredicate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isTrue(Object arg1, Object arg2);

    protected boolean compareObjects(Object arg1, Object arg2) {
        Object o2 = coerce((String) arg2, arg1);
        if (o2 == null) {
            return false;
        }
        int comp = compareTo(arg1, o2);
        if (comp == NOT_COMPARABLE) {
            return false;
        }
        return testCompareValue(comp);
    }

    /**
     * Subclasses calling compareObjects should override this method
     *
     * @param comp
     * @return false
     */
    protected boolean testCompareValue(int comp) {
        return false;
    }

    public static Object coerce(String constantValue, Object attrVal) {
        try {
            if (attrVal instanceof Boolean) {
                return getBooleanLoose(constantValue);
            }
            if (attrVal instanceof Double) {
                return new Double(constantValue);
            }
            if (attrVal instanceof Integer) {
                return new Integer(constantValue);
            }
            if (attrVal instanceof String) {
                return constantValue;
            }
            if (attrVal instanceof Date) {
                return dateParser.parse(constantValue, true);
            }
        } catch (ParseException | NumberFormatException ex) {
            // eat it
        }
        // just return it as a String
        return null;
    }
    protected static final int NOT_COMPARABLE = Integer.MIN_VALUE;

    protected static int compareTo(Object o1, Object o2) {
        if (o1 instanceof Boolean && o2 instanceof Boolean) {
            return ((Boolean) o1).equals(o2) ? 0 : 1;
        }

        if (!(o1 instanceof Comparable)) {
            return NOT_COMPARABLE;
        }
        if (!(o2 instanceof Comparable)) {
            return NOT_COMPARABLE;
        }
        return ((Comparable) o1).compareTo((Comparable) o2);
    }

    private static boolean getBooleanLoose(String boolStr) {
        return boolStr.equalsIgnoreCase("true")
                || boolStr.equalsIgnoreCase("yes")
                || boolStr.equalsIgnoreCase("1")
                || boolStr.equalsIgnoreCase("y");
    }

    private static class EqualPredicate extends AttributePredicate {

        public EqualPredicate() {
            super("=");
        }

        public boolean isTrue(Object arg1, Object arg2) {
            return compareObjects(arg1, arg2);
        }

        protected boolean testCompareValue(int comp) {
            return comp == 0;
        }
    }

    private static class NotEqualPredicate extends AttributePredicate {

        public NotEqualPredicate() {
            super("<>");
        }

        public boolean isTrue(Object arg1, Object arg2) {
            return compareObjects(arg1, arg2);
        }

        protected boolean testCompareValue(int comp) {
            return comp != 0;
        }
    }

    private static class LessThanPredicate extends AttributePredicate {

        public LessThanPredicate() {
            super("<");
        }

        public boolean isTrue(Object arg1, Object arg2) {
            return compareObjects(arg1, arg2);
        }

        protected boolean testCompareValue(int comp) {
            return comp < 0;
        }
    }

    private static class LessThanOrEqualPredicate extends AttributePredicate {

        public LessThanOrEqualPredicate() {
            super("<=");
        }

        public boolean isTrue(Object arg1, Object arg2) {
            return compareObjects(arg1, arg2);
        }

        protected boolean testCompareValue(int comp) {
            return comp <= 0;
        }
    }

    private static class GreaterThanPredicate extends AttributePredicate {

        public GreaterThanPredicate() {
            super(">");
        }

        public boolean isTrue(Object arg1, Object arg2) {
            return compareObjects(arg1, arg2);
        }

        protected boolean testCompareValue(int comp) {
            return comp > 0;
        }
    }

    private static class GreaterThanOrEqualPredicate extends AttributePredicate {

        public GreaterThanOrEqualPredicate() {
            super(">=");
        }

        public boolean isTrue(Object arg1, Object arg2) {
            return compareObjects(arg1, arg2);
        }

        protected boolean testCompareValue(int comp) {
            return comp >= 0;
        }
    }

    private static class ContainsPredicate extends AttributePredicate {

        public ContainsPredicate() {
            super(GenericNames.CONTAINS);
        }

        public boolean isTrue(Object arg1, Object arg2) {
            if (arg1 == null || arg2 == null) {
                return false;
            }
            return arg1.toString().indexOf(arg2.toString()) >= 0;
        }
    }

    private static class StartsWithPredicate extends AttributePredicate {

        public StartsWithPredicate() {
            super(I18N.get("ui.plugin.analysis.AttributePredicate.starts-with"));
        }

        public boolean isTrue(Object arg1, Object arg2) {
            if (arg1 == null || arg2 == null) {
                return false;
            }
            return arg1.toString().startsWith(arg2.toString());
        }
    }
}
