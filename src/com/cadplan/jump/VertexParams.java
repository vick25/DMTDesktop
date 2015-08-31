package com.cadplan.jump;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

public class VertexParams {

    public static String version = "0.180";
    public static int POLYGON = 0;
    public static int STAR = 1;
    public static int IMAGE = 2;
    public static int ANYSHAPE = 3;
    public static int WKT = 4;
    public static int EXTERNAL = 5;
    public static int type = POLYGON;
    public static double orientation = 0.0;
    public static int size = 32;
    public static boolean showLine = true;
    public static boolean showFill = true;
    public static boolean dotted = false;
    public static int sides = 4;
    public static Image[] images;
    public static String[] imageNames;
    public static WKTshape[] wktShapes;
    public static String[] wktNames;
    public static int selectedImage = -1;
    public static int selectedWKT = -1;
    public static boolean byValue = true;
    public static String attName = "";
    public static boolean singleLayer = true;
    public static Layer selectedLayer = null;
    public static boolean sizeByScale = false;
    public static double baseScale = 1.0;
    public static double actualScale = 1.0;
    public static String attTextName = "";
    public static boolean textEnabled = false;
    public static String textFontName = "SansSerif";
    public static int textFontSize = 10;
    public static int textStyle = Font.PLAIN;
    public static int textJustification = 0;
    public static int textPosition = 0;
    public static int textOffset = 1;
    public static int textOffsetValue = 0;
    public static int textScope = 1;
    public static int textBorder = 0;
    public static boolean textFill = false;
    public static Color textBackgroundColor = Color.WHITE;
    public static Color textForegroundColor = Color.BLACK;
    public static String symbolName;
    public static int symbolType;
    public static int symbolNumber;
    public static WorkbenchContext context;
}
