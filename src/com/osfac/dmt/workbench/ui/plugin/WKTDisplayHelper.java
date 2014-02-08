package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.StringUtil;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.util.Assert;
import java.util.*;

public class WKTDisplayHelper {

    private static final int LINE_SPLIT_THRESHOLD = -1;

    /**
     * @param wkt may contain syntax errors
     */
    public String format(String wkt) {
        String formattedWKT = format(wkt, false);

        if (formattedWKT.length() > LINE_SPLIT_THRESHOLD) {
            formattedWKT = format(wkt, true);
        }

        return formattedWKT;
    }

    private String format(String wkt, boolean splitting) {
        int level = 0;
        String lastNonBlankToken = "";
        StringBuffer formattedWKT = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(wkt, " \t\n\r\f,()",
                true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (token.trim().length() == 0) {
                continue;
            }
            switch (token) {
                case ",":
                    formattedWKT.append(", ");
                    break;
                case "(":
                    level++;
                    if (wordToken(lastNonBlankToken)) {
                        formattedWKT.append(" ");
                    }
                    formattedWKT.append("(");
                    break;
                case ")":
                    int oldLevel = level;
                    level = Math.max(0, level - 1);
                    if (wordToken(lastNonBlankToken)) {
                        newLineAndIndentIfSplitting(formattedWKT, level, splitting);
                    }
                    formattedWKT.append(")");
                    if (oldLevel == 1) {
                        formattedWKT.append(newLine());
                    }
                    break;
                default:
                    if (wordToken(lastNonBlankToken)) {
                        formattedWKT.append(" ");
                    } else {
                        newLineAndIndentIfSplitting(formattedWKT, level, splitting);
                    }
                    formattedWKT.append(token);
                    break;
            }

            lastNonBlankToken = token;
        }

        return formattedWKT.toString().trim();
    }

    private String newLine() {
        //Not System.getProperty("line.separator"); otherwise, when you copy
        //into, say, Notepad, you get garbage characters at the end of each line 
        //(\r\r\n). [Bob Boseko]
        return "\n";
    }

    private void newLineAndIndentIfSplitting(StringBuffer formattedWKT,
            int level, boolean splitting) {
        if (splitting) {
            formattedWKT.append(newLine()).append(indent(level));
        }
    }

    private boolean wordToken(String token) {
        return !token.equals("(") && !token.equals(")") && !token.equals(",");
    }

    private Integer inc(Object i) {
        return new Integer(((Integer) i).intValue() + 1);
    }

    public String annotate(String wkt) {
        int lineIndex = 0;
        Stack stack = new Stack();
        stack.push(new Integer(0));

        ArrayList annotations = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(wkt, " \t\n\r\f,()",
                true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (token.equals("\n")) {
                lineIndex++;
            } else if (token.trim().length() == 0) {
                continue;
            } else if (token.equals(",")) {
                stack.push(inc(stack.pop()));
            } else if (token.equals("(")) {
                stack.push(new Integer(0));
            } else if (token.equals(")")) {
                if (stack.size() != 1) {
                    stack.pop();

                    //Handle inconsistency: geometries are separated by whitespace,
                    //not commas. [Bob Boseko]
                    if (stack.size() == 1) {
                        stack.push(inc(stack.pop()));
                    }
                }
            } else {
                if (StringUtil.isNumber(token)) {
                    CollectionUtil.setIfNull(lineIndex, annotations,
                            annotation(stack));
                }
            }
        }

        //Ensure that there are the same number of annotation lines as WKT lines,
        //for the sake of the scrollbars. [Bob Boseko]
        CollectionUtil.resize(annotations, lineIndex + 1);

        return StringUtil.toDelimitedString(annotations, newLine());
    }

    private String annotation(List indices) {
        String annotation = "";

        for (Iterator i = indices.subList(1, indices.size()).iterator();
                i.hasNext();) {
            Integer index = (Integer) i.next();

            if (annotation.trim().length() != 0) {
                annotation += ":";
            }

            annotation += index;
        }

        return annotation;
    }

    private String indent(int level) {
        return StringUtil.repeat(' ', level * 4);
    }

    public static void main(String[] args) {
        String wkt = new WKTDisplayHelper().format("POINT(5 5)POINT(10 10)",
                false);
        System.out.println(wkt);
        System.out.println(new WKTDisplayHelper().annotate(wkt));
    }

    public String annotation(Geometry geometry, Coordinate c) {
        Stack stack = new Stack();
        stack.push(new Integer(0));
        Assert.isTrue(annotation(geometry, c, stack));

        return annotation(stack);
    }

    private boolean annotation(Geometry geometry, Coordinate c, Stack stack) {
        stack.push(new Integer(0));

        if (geometry instanceof GeometryCollection) {
            for (int i = 0;
                    i < ((GeometryCollection) geometry).getNumGeometries();
                    i++) {
                if (annotation(((GeometryCollection) geometry).getGeometryN(i),
                        c, stack)) {
                    return true;
                }
            }
        } else if (geometry instanceof Polygon) {
            if (annotation(((Polygon) geometry).getExteriorRing(), c, stack)) {
                return true;
            }

            for (int i = 0; i < ((Polygon) geometry).getNumInteriorRing();
                    i++) {
                if (annotation(((Polygon) geometry).getInteriorRingN(i), c,
                        stack)) {
                    return true;
                }
            }
        } else if (geometry instanceof LineString || geometry instanceof Point) {
            Coordinate[] coordinates = geometry.getCoordinates();

            for (int i = 0; i < coordinates.length; i++) {
                if (coordinates[i] == c) {
                    return true;
                }

                stack.push(inc(stack.pop()));
            }
        } else {
            Assert.shouldNeverReachHere();
        }

        stack.pop();
        stack.push(inc(stack.pop()));

        return false;
    }
}
