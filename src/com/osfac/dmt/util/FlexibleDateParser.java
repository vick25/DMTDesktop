package com.osfac.dmt.util;

import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Warning: This class can parse a wide variety of formats. This flexibility is
 * fine for parsing user input because the user immediately sees whether the
 * parser is correct and can fix it if necessary. However, GML files are advised
 * to stick with a safe format like yyyy-MM-dd. yy/MM/dd is not as safe because
 * while 99/03/04 will be parsed as yyyy/MM/dd, 02/03/04 will be parsed as
 * MM/dd/yyyy (because MM/dd/yyyy appears earlier than yyyy/MM/dd in
 * FlexibleDateParser.txt).
 */
public class FlexibleDateParser {

    private static Collection lenientFormatters = null;
    private static Collection unlenientFormatters = null;
    //CellEditor used to be a static field CELL_EDITOR, but I was getting
    //problems calling it from ESETextField (it simply didn't appear).
    //The problems vanished when I turned it into a static class. I didn't
    //investigate further. [Bob Boseko]

    public static final class CellEditor extends DefaultCellEditor {

        public CellEditor() {
            super(new JTextField());
        }
        private Object value;
        private FlexibleDateParser parser = new FlexibleDateParser();

        public boolean stopCellEditing() {
            try {
                value = parser.parse((String) super.getCellEditorValue(), true);
            } catch (Exception e) {
                ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));

                return false;
            }

            return super.stopCellEditing();
        }

        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {
            this.value = null;
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));

            return super.getTableCellEditorComponent(
                    table,
                    format((Date) value),
                    isSelected,
                    row,
                    column);
        }

        private String format(Date date) {
            return (date == null) ? "" : formatter.format(date);
        }

        public Object getCellEditorValue() {
            return value;
        }
        //Same formatter as used by JTable.DateRenderer. [Bob Boseko]
        private DateFormat formatter = DateFormat.getDateInstance();
    };
    private boolean verbose = false;

    private Collection sortByComplexity(Collection patterns) {
        //Least complex to most complex. [Bob Boseko]
        TreeSet sortedPatterns = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                int result = complexity(o1.toString()) - complexity(o2.toString());
                if (result == 0) {
                    //The two patterns have the same level of complexity.
                    //Sort by order of appearance (e.g. to resolve
                    //MM/dd/yyyy vs dd/MM/yyyy [Bob Boseko]
                    result = ((Pattern) o1).index - ((Pattern) o2).index;
                }
                return result;
            }
            private TreeSet uniqueCharacters = new TreeSet();

            private int complexity(String pattern) {
                uniqueCharacters.clear();

                for (int i = 0; i < pattern.length(); i++) {
                    if (("" + pattern.charAt(i)).trim().length() > 0) {
                        uniqueCharacters.add("" + pattern.charAt(i));
                    }
                }

                return uniqueCharacters.size();
            }
        });
        sortedPatterns.addAll(patterns);

        return sortedPatterns;
    }

    private Collection lenientFormatters() {
        if (lenientFormatters == null) {
            load();
        }

        return lenientFormatters;
    }

    private Collection unlenientFormatters() {
        if (unlenientFormatters == null) {
            load();
        }

        return unlenientFormatters;
    }

    /**
     * @return null if s is empty
     */
    public Date parse(String s, boolean lenient) throws ParseException {
        if (s.trim().length() == 0) {
            return null;
        }
        //The deprecated Date#parse method is actually pretty flexible. [Bob Boseko]
        try {
            if (verbose) {
                System.out.println(s + " -- Date constructor");
            }
            return new Date(s);
        } catch (Exception e) {
            //Eat it. [Bob Boseko]
        }

        try {
            return parse(s, unlenientFormatters());
        } catch (ParseException e) {
            if (lenient) {
                return parse(s, lenientFormatters());
            }

            throw e;
        }
    }

    private Date parse(String s, Collection formatters) throws ParseException {
        ParseException firstParseException = null;

        for (Iterator i = formatters.iterator(); i.hasNext();) {
            SimpleDateFormat formatter = (SimpleDateFormat) i.next();

            if (verbose) {
                System.out.println(
                        s
                        + " -- "
                        + formatter.toPattern()
                        + (formatter.isLenient() ? "lenient" : ""));
            }

            try {
                return parse(s, formatter);
            } catch (ParseException e) {
                if (firstParseException == null) {
                    firstParseException = e;
                }
            }
        }

        throw firstParseException;
    }

    private Date parse(String s, SimpleDateFormat formatter) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(s, pos);

        if (pos.getIndex() == 0) {
            throw new ParseException(
                    "Unparseable date: \"" + s + "\"",
                    pos.getErrorIndex());
        }

        //SimpleDateFormat ignores trailing characters in the pattern string that it
        //doesn't need. Don't allow it to ignore any characters. [Bob Boseko]
        if (pos.getIndex() != s.length()) {
            throw new ParseException(
                    "Unparseable date: \"" + s + "\"",
                    pos.getErrorIndex());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if ((calendar.get(Calendar.YEAR) == 1970) && (s.indexOf("70") == -1)) {
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        }

        return calendar.getTime();
    }

    private static class Pattern {

        private String pattern;
        private int index;

        public Pattern(String pattern, int index) {
            this.pattern = pattern;
            this.index = index;
        }

        public String toString() {
            return pattern;
        }
    }

    private void load() {
        if (lenientFormatters == null) {
            InputStream inputStream =
                    getClass().getResourceAsStream("FlexibleDateParser.txt");

            try {
                try {
                    Collection patterns = new ArrayList();
                    int index = 0;
                    for (Iterator i = FileUtil.getContents(inputStream).iterator();
                            i.hasNext();) {
                        String line = ((String) i.next()).trim();

                        if (line.startsWith("#")) {
                            continue;
                        }

                        if (line.length() == 0) {
                            continue;
                        }

                        patterns.add(new Pattern(line, index));
                        index++;
                    }

                    unlenientFormatters = toFormatters(false, patterns);
                    lenientFormatters = toFormatters(true, patterns);
                } finally {
                    inputStream.close();
                }
            } catch (IOException e) {
                Assert.shouldNeverReachHere(e.toString());
            }
        }
    }

    private Collection toFormatters(boolean lenient, Collection patterns) {
        ArrayList formatters = new ArrayList();
        //Sort from least complex to most complex; otherwise, ddMMMyyyy 
        //instead of MMMd will match "May 15". [Bob Boseko]
        for (Iterator i = sortByComplexity(patterns).iterator(); i.hasNext();) {
            Pattern pattern = (Pattern) i.next();
            SimpleDateFormat formatter = new SimpleDateFormat(pattern.pattern);
            formatter.setLenient(lenient);
            formatters.add(formatter);
        }

        return formatters;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(DateFormat.getDateInstance().parse("03-Mar-1998"));
    }

    public void setVerbose(boolean b) {
        verbose = b;
    }
}
