package com.osfac.dmt.util;

import com.vividsolutions.jts.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utilities to support the Java language.
 */
public class LangUtil {

    private static Map primitiveToWrapperMap = new HashMap() {
        {
            put(byte.class, Byte.class);
            put(char.class, Character.class);
            put(short.class, Short.class);
            put(int.class, Integer.class);
            put(long.class, Long.class);
            put(float.class, Float.class);
            put(double.class, Double.class);
            put(boolean.class, Boolean.class);
        }
    };

    public static String emptyStringIfNull(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * Useful because an expression used to generate o need only be evaluated
     * once.
     */
    public static Object ifNull(Object o, Object alternative) {
        return (o == null) ? alternative : o;
    }

    public static Object ifNotNull(Object o, Object alternative) {
        return (o != null) ? alternative : o;
    }

    public static Class toPrimitiveWrapperClass(Class primitiveClass) {
        return (Class) primitiveToWrapperMap.get(primitiveClass);
    }

    public static boolean isPrimitive(Class c) {
        return primitiveToWrapperMap.containsKey(c);
    }

    public static boolean bothNullOrEqual(Object a, Object b) {
        return (a == null && b == null) || (a != null && b != null && a.equals(b));
    }

    public static Object newInstance(Class c) {
        try {
            return c.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            Assert.shouldNeverReachHere(e.toString());
            return null;
        }
    }

    public static Collection classesAndInterfaces(Class c) {
        ArrayList classesAndInterfaces = new ArrayList();
        classesAndInterfaces.add(c);
        superclasses(c, classesAndInterfaces);
        for (Iterator i = new ArrayList(classesAndInterfaces).iterator(); i.hasNext();) {
            Class x = (Class) i.next();
            classesAndInterfaces.addAll(Arrays.asList(x.getInterfaces()));
        }
        return classesAndInterfaces;
    }

    private static void superclasses(Class c, Collection results) {
        if (c.getSuperclass() == null) {
            return;
        }
        results.add(c.getSuperclass());
        superclasses(c.getSuperclass(), results);
    }
}
