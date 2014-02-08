package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.util.CollectionMap;
import com.osfac.dmt.util.FileUtil;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The colour schemes were taken from the following sources: <ul> <li> Visual
 * Mining, Inc. "Charts--Color Palettes" 2003. Available from
 * http://chartworks.com/resources/palettes.html. Internet; accessed 24 April
 * 2003. <li> Brewer, Cindy and Harrower, Mark. "ColorBrewer". Available from
 * http://www.personal.psu.edu/faculty/c/a/cab38/ColorBrewerBeta2.html.
 * Internet; accessed 24 April 2003. </ul>
 */
public class ColorScheme {

    public static ColorScheme create(String name) {
        Assert.isTrue(nameToColorsMap().keySet().contains(name));
        return new ColorScheme(name, nameToColorsMap().getItems(name));
    }
    private static ArrayList rangeColorSchemeNames;
    private static ArrayList discreteColorSchemeNames;

    private static void load() {
        try {
            rangeColorSchemeNames = new ArrayList();
            discreteColorSchemeNames = new ArrayList();
            nameToColorsMap = new CollectionMap();
            InputStream inputStream;
            inputStream = ColorScheme.class.getResourceAsStream("ColorScheme.txt");
            try {
                for (Iterator i = FileUtil.getContents(inputStream).iterator();
                        i.hasNext();) {
                    String line = (String) i.next();
                    add(line);
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            Assert.shouldNeverReachHere(e.toString());
        }
    }

    private static void add(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        String name = tokenizer.nextToken();
        boolean range = tokenizer.nextToken().equals("range");
        (range ? rangeColorSchemeNames : discreteColorSchemeNames).add(name);
        while (tokenizer.hasMoreTokens()) {
            String hex = tokenizer.nextToken();
            Assert.isTrue(hex.length() == 6, hex);
            nameToColorsMap().addItem(name, Color.decode("#" + hex));
        }
    }

    private static CollectionMap nameToColorsMap() {
        if (nameToColorsMap == null) {
            load();
        }
        return nameToColorsMap;
    }
    private static CollectionMap nameToColorsMap;

    public static Collection rangeColorSchemeNames() {
        if (rangeColorSchemeNames == null) {
            load();
        }
        return Collections.unmodifiableList(rangeColorSchemeNames);
    }

    public static Collection discreteColorSchemeNames() {
        if (discreteColorSchemeNames == null) {
            load();
        }
        return Collections.unmodifiableList(discreteColorSchemeNames);
    }
    private String name;

    public ColorScheme(String name, Collection colors) {
        this.name = name;
        this.colors = new ArrayList(colors);
    }
    private int lastColorReturned = -1;

    public int getLastColorReturned() {
        return lastColorReturned;
    }

    public void setLastColorReturned(int lastColorReturned) {
        this.lastColorReturned = lastColorReturned;
    }
    private List colors;

    public Color next() {
        lastColorReturned++;
        if (lastColorReturned >= colors.size()) {
            lastColorReturned = 0;
        }
        return (Color) colors.get(lastColorReturned);
    }

    public List getColors() {
        return Collections.unmodifiableList(colors);
    }

    public String getName() {
        return name;
    }
}
