package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.util.Block;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.ImmutableFirstElementList;
import com.osfac.dmt.workbench.ui.ColumnBasedTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class ColorThemingTableModel extends ColumnBasedTableModel {

    BasicStyle defaultStyle;

    public ColorThemingTableModel(
            BasicStyle defaultStyle,
            String attributeName,
            Map attributeValueToBasicStyleMap,
            Map attributeValueToLabelMap,
            FeatureSchema schema) {
        //Value doesn't matter. [Bob Boseko]
        this.defaultStyle = defaultStyle;
        attributeMappings =
                new ImmutableFirstElementList(
                new AttributeMapping(null, defaultStyle, null));
        this.attributeName = attributeName;
        setMaps(attributeValueToBasicStyleMap, attributeValueToLabelMap);
        setColumns(createColumns(schema));
    }
    public static final int COLOR_COLUMN = 0;
    public static final int ATTRIBUTE_COLUMN = 1;
    public static final int LABEL_COLUMN = 2;

    public void setMaps(Map attributeValueToBasicStyleMap, Map attributeValueToLabelMap) {
        attributeMappings.clear();
        for (Iterator i = attributeValueToBasicStyleMap.keySet().iterator(); i.hasNext();) {
            Object attributeValue = i.next();
            attributeMappings.add(
                    new AttributeMapping(
                    attributeValue,
                    (BasicStyle) attributeValueToBasicStyleMap.get(attributeValue),
                    (String) attributeValueToLabelMap.get(attributeValue)));
        }
        fireTableChanged(new TableModelEvent(this));
    }

    protected static class AttributeMapping implements Comparable {

        private String label;

        public AttributeMapping(Object attributeValue, BasicStyle basicStyle, String label) {
            this.attributeValue = attributeValue;
            this.basicStyle = basicStyle;
            this.label = label;
        }
        private Object attributeValue;
        private BasicStyle basicStyle;

        public Object getAttributeValue() {
            return attributeValue;
        }

        public BasicStyle getBasicStyle() {
            return basicStyle;
        }

        public int compareTo(Object o) {
            AttributeMapping other = (AttributeMapping) o;
            if (attributeValue == null) {
                return -1;
            }
            if (other.attributeValue == null) {
                return 1;
            }
            return ((Comparable) attributeValue).compareTo(
                    (Comparable) other.attributeValue);
        }

        public void setAttributeValue(Object object) {
            attributeValue = object;
        }

        public void setBasicStyle(BasicStyle style) {
            basicStyle = style;
        }

        protected String getLabel() {
            return label;
        }

        protected void setLabel(String label) {
            this.label = label;
        }
    }

    public void clear() {
        attributeMappings.clear();
        fireTableChanged(new TableModelEvent(this));
    }

    public boolean containsNullAttributeValues() {
        for (Iterator i = nonDefaultAttributeMappings().iterator();
                i.hasNext();) {
            AttributeMapping attributeMapping = (AttributeMapping) i.next();
            if (attributeMapping.getAttributeValue() == null) {
                return true;
            }
        }
        return false;
    }

    protected AttributeMapping attributeMapping(int i) {
        return (AttributeMapping) attributeMappings.get(i);
    }

    public BasicStyle getDefaultStyle() {
        return attributeMapping(0).getBasicStyle();
    }

    public Object findDuplicateAttributeValue() {
        TreeSet set = new TreeSet();
        for (Iterator i = nonDefaultAttributeMappings().iterator();
                i.hasNext();) {
            AttributeMapping attributeMapping = (AttributeMapping) i.next();
            if (attributeMapping.getAttributeValue() == null) {
                //Check nulls elsewhere. TreeSet won't accept nulls. [Bob Boseko]
                continue;
            }
            if (set.contains(attributeMapping.getAttributeValue())) {
                return attributeMapping.getAttributeValue();
            }
            set.add(attributeMapping.getAttributeValue());
        }
        return null;
    }
    //Can't use TreeMap because attributes may not be unique (this is
    //an invalid state of course and we won't let the user hit OK until he
    //resolves it). Can't use HashMap because Geometry doesn't implement
    //#hash. [Bob Boseko]
    protected List attributeMappings;

    protected List createColumns(final FeatureSchema schema) {
        ArrayList columns = new ArrayList();
        columns.add(new Column(I18N.get("ui.renderer.style.ColorThemingTableModel.attribute-value"), BasicStyle.class) {
            public Object getValueAt(int rowIndex) {
                return attributeMapping(rowIndex).getBasicStyle();
            }

            public void setValueAt(Object value, int rowIndex) {
                attributeMapping(rowIndex).setBasicStyle((BasicStyle) value);
                fireTableChanged(
                        new TableModelEvent(ColorThemingTableModel.this, rowIndex));
            }
        });
        columns.add(new Column(I18N.get("ui.renderer.style.ColorThemingTableModel.attribute-value"), null) {
            public Class getDataClass() {
                //attributeName will be null if layer has no non-spatial attributes 
                //[Bob Boseko]
                //attributeName will not be present in the schema if the user
                //has deleted this attribute. [Bob Boseko]
                return attributeName == null
                        || !schema.hasAttribute(attributeName)
                        ? Object.class
                        : schema.getAttributeType(attributeName).toJavaClass();
            }

            public Object getValueAt(int rowIndex) {
                return attributeMapping(rowIndex).getAttributeValue();
            }

            public void setValueAt(Object value, int rowIndex) {
                attributeMapping(rowIndex).setAttributeValue(value);
                //The validators need to know that the table has changed. [Bob Boseko]                            
                fireTableChanged(
                        new AttributeValueTableModelEvent(
                        ColorThemingTableModel.this,
                        rowIndex));
            }
        });
        columns.add(new Column("Label", String.class) {
            public Object getValueAt(int rowIndex) {
                return attributeMapping(rowIndex).getLabel();
            }

            public void setValueAt(Object value, int rowIndex) {
                attributeMapping(rowIndex).setLabel((String) value);
                fireTableChanged(
                        new AttributeValueTableModelEvent(
                        ColorThemingTableModel.this,
                        rowIndex));
            }
        });
        return columns;
    }

    public static class AttributeValueTableModelEvent extends TableModelEvent {

        public AttributeValueTableModelEvent(TableModel source, int row) {
            super(source, row);
        }
    }

    public void apply(
            ColorScheme colorScheme,
            boolean skipDefaultAttributeMapping) {
        //Leave the first element out of the sort, because it's the "(All other values)"
        //element. [Bob Boseko]     
        for (Iterator i =
                (skipDefaultAttributeMapping
                ? nonDefaultAttributeMappings()
                : attributeMappings)
                .iterator();
                i.hasNext();) {
            AttributeMapping attributeMapping = (AttributeMapping) i.next();
            BasicStyle newBasicStyle = new BasicStyle(colorScheme.next());
            newBasicStyle.setLineWidth(defaultStyle.getLineWidth());
            newBasicStyle.setRenderingLine(defaultStyle.isRenderingLine());
            newBasicStyle.setLinePattern(defaultStyle.getLinePattern());
            newBasicStyle.setRenderingFill(defaultStyle.isRenderingFill());
            newBasicStyle.setFillPattern(defaultStyle.getFillPattern());
            //newBasicStyle.setRenderingVertices(defaultStyle.getRenderingVertices());
            attributeMapping.setBasicStyle(/*new BasicStyle(colorScheme.next())*/newBasicStyle);
        }
        fireTableChanged(new TableModelEvent(this));
    }

    public int getRowCount() {
        return attributeMappings.size();
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Map getAttributeValueToBasicStyleMap() {
        return attributeValueToObjectMap(new Block() {
            public Object yield(Object attributeMapping) {
                return ((AttributeMapping) attributeMapping).getBasicStyle();
            }
        });
    }

    public Map getAttributeValueToLabelMap() {
        return attributeValueToObjectMap(new Block() {
            public Object yield(Object attributeMapping) {
                return ((AttributeMapping) attributeMapping).getLabel();
            }
        });
    }

    private Map attributeValueToObjectMap(Block getter) {
        TreeMap attributeValueToObjectMap = new TreeMap();
        //Skip the first element, which is the default style. [Bob Boseko]
        for (Iterator i = nonDefaultAttributeMappings().iterator();
                i.hasNext();) {
            AttributeMapping attributeMapping = (AttributeMapping) i.next();
            attributeValueToObjectMap.put(
                    attributeMapping.getAttributeValue(),
                    getter.yield(attributeMapping));
        }
        return attributeValueToObjectMap;
    }
    private boolean lastSortAscending = true;

    public boolean wasLastSortAscending() {
        return lastSortAscending;
    }

    public void sort() {
        sort(!lastSortAscending);
    }

    public void sort(boolean ascending) {
        //Leave the first element out of the sort, because it's the "(All other values)"
        //element. [Bob Boseko]
        if (ascending) {
            Collections.sort(nonDefaultAttributeMappings());
        } else {
            Collections.sort(
                    nonDefaultAttributeMappings(),
                    Collections.reverseOrder());
        }
        lastSortAscending = ascending;
        fireTableChanged(new TableModelEvent(this));
    }

    public void removeAttributeValues(int[] rows) {
        for (Iterator i = CollectionUtil.reverseSortedSet(rows).iterator();
                i.hasNext();) {
            Integer row = (Integer) i.next();
            attributeMappings.remove(row.intValue());
            fireTableChanged(
                    new TableModelEvent(
                    this,
                    row.intValue(),
                    row.intValue(),
                    TableModelEvent.ALL_COLUMNS,
                    TableModelEvent.DELETE));
        }
    }

    /**
     * @return row
     */
    public int insertAttributeValue(int row, ColorScheme colorScheme) {
        BasicStyle newBasicStyle = new BasicStyle(colorScheme.next());
        newBasicStyle.setLineWidth(defaultStyle.getLineWidth());
        newBasicStyle.setRenderingLine(defaultStyle.isRenderingLine());
        newBasicStyle.setLinePattern(defaultStyle.getLinePattern());
        newBasicStyle.setRenderingFill(defaultStyle.isRenderingFill());
        newBasicStyle.setFillPattern(defaultStyle.getFillPattern());
        //newBasicStyle.setRenderingVertices(defaultStyle.getRenderingVertices());
        attributeMappings.add(
                row,
                new AttributeMapping(null, /*new BasicStyle(colorScheme.next())*/ newBasicStyle, ""));
        fireTableChanged(
                new TableModelEvent(
                this,
                row,
                row,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT));
        return row;
    }
    protected String attributeName;

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //Any cell except the one that says "(All other values)" [Bob Boseko]
        if (rowIndex == 0 && columnIndex == ATTRIBUTE_COLUMN) {
            return false;
        }
        if (rowIndex == 0 && columnIndex == LABEL_COLUMN) {
            return false;
        }
        return true;
    }

    protected List nonDefaultAttributeMappings() {
        return attributeMappings.subList(1, attributeMappings.size());
    }
}
