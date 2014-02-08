package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.I18N;
import com.osfac.dmt.io.FMEGMLReader;
import com.osfac.dmt.io.FMEGMLWriter;
import com.osfac.dmt.io.JMLReader;
import com.osfac.dmt.io.JMLWriter;
import com.osfac.dmt.io.JUMPReader;
import com.osfac.dmt.io.JUMPWriter;
import com.osfac.dmt.io.ShapefileReader;
import com.osfac.dmt.io.ShapefileWriter;
import com.osfac.dmt.io.WKTReader;
import com.osfac.dmt.io.WKTWriter;
import com.osfac.dmt.io.datasource.StandardReaderWriterFileDataSource;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.vividsolutions.jts.util.Assert;
import java.awt.Component;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import org.openjump.core.ui.DatasetOptionsPanel;
import org.openjump.core.ui.swing.ComboBoxComponentPanel;
import org.openjump.core.ui.swing.factory.field.ComboBoxFieldComponentFactory;
import org.openjump.swing.factory.field.FieldComponentFactory;

/**
 * Adds to the JUMP Workbench the UIs for opening and saving files with the
 * basic file formats.
 */
public class InstallStandardDataSourceQueryChoosersPlugIn extends AbstractPlugIn {

    private void addFileDataSourceQueryChoosers(
            JUMPReader reader,
            JUMPWriter writer,
            final String description,
            final WorkbenchContext context,
            Class readerWriterDataSourceClass) {

        DataSourceQueryChooserManager chooserManager = DataSourceQueryChooserManager.get(context.getBlackboard());

        chooserManager.addLoadDataSourceQueryChooser(new LoadFileDataSourceQueryChooser(
                readerWriterDataSourceClass,
                description,
                extensions(readerWriterDataSourceClass),
                context) {
            protected void addFileFilters(JFileChooser chooser) {
                super.addFileFilters(chooser);
                InstallStandardDataSourceQueryChoosersPlugIn.addCompressedFileFilter(description, chooser);
            }
        });

        if (readerWriterDataSourceClass != StandardReaderWriterFileDataSource.Shapefile.class) {
            chooserManager.addSaveDataSourceQueryChooser(new SaveFileDataSourceQueryChooser(
                    readerWriterDataSourceClass,
                    description,
                    extensions(readerWriterDataSourceClass),
                    context));
        } else {
            // if we write ESRI Shapefiles, we add an option for the Charset
            chooserManager.addSaveDataSourceQueryChooser(new SaveFileDataSourceQueryChooser(
                    readerWriterDataSourceClass,
                    description,
                    extensions(readerWriterDataSourceClass),
                    context) {
                private JComponent comboboxFieldComponent;

                protected Map toProperties(File file) {
                    HashMap properties = new HashMap(super.toProperties(file));
                    String charsetName = Charset.defaultCharset().name();
                    if (comboboxFieldComponent instanceof ComboBoxComponentPanel) {
                        charsetName = (String) ((ComboBoxComponentPanel) comboboxFieldComponent).getSelectedItem();
                    }
                    properties.put("charset", charsetName);

                    return properties;
                }

                protected Component getSouthComponent1() {
                    boolean showCharsetSelection = false;
                    Object showCharsetSelectionObject = PersistentBlackboardPlugIn.get(context.getBlackboard()).get(DatasetOptionsPanel.BB_DATASET_OPTIONS_SHOW_CHARSET_SELECTION);
                    if (showCharsetSelectionObject instanceof Boolean) {
                        showCharsetSelection = ((Boolean) showCharsetSelectionObject).booleanValue();
                    }
                    if (showCharsetSelection) {
                        FieldComponentFactory fieldComponentFactory = new ComboBoxFieldComponentFactory(context, I18N.get("org.openjump.core.ui.io.file.DataSourceFileLayerLoader.charset") + ":", Charset.availableCharsets().keySet().toArray());
                        comboboxFieldComponent = fieldComponentFactory.createComponent();
                        fieldComponentFactory.setValue(comboboxFieldComponent, Charset.defaultCharset().name());
                        return comboboxFieldComponent;
                    } else {
                        return new Component() {
                        };
                    }
                }
            });

        }
    }

    public static String[] extensions(Class readerWriterDataSourceClass) {
        String[] exts = null;

        try {
            exts = ((StandardReaderWriterFileDataSource) readerWriterDataSourceClass.newInstance()).getExtensions();
        } catch (Exception e) {
            Assert.shouldNeverReachHere(e.toString());
        }

        return exts;
    }

    public void initialize(final PlugInContext context) throws Exception {
        addFileDataSourceQueryChoosers(
                new JMLReader(),
                new JMLWriter(),
                "OSFAC-DMT",
                context.getWorkbenchContext(),
                StandardReaderWriterFileDataSource.JML.class);

        new GMLDataSourceQueryChooserInstaller().addLoadGMLFileDataSourceQueryChooser(context);
        new GMLDataSourceQueryChooserInstaller().addSaveGMLFileDataSourceQueryChooser(context);

        addFileDataSourceQueryChoosers(
                new FMEGMLReader(),
                new FMEGMLWriter(),
                "FME GML",
                context.getWorkbenchContext(),
                StandardReaderWriterFileDataSource.FMEGML.class);

        addFileDataSourceQueryChoosers(
                new WKTReader(),
                new WKTWriter(),
                "WKT",
                context.getWorkbenchContext(),
                StandardReaderWriterFileDataSource.WKT.class);

        addFileDataSourceQueryChoosers(
                new ShapefileReader(),
                new ShapefileWriter(),
                "ESRI Shapefile",
                context.getWorkbenchContext(),
                StandardReaderWriterFileDataSource.Shapefile.class);
    }

    public static void addCompressedFileFilter(final String description,
            JFileChooser chooser) {
        chooser.addChoosableFileFilter(GUIUtil.createFileFilter(I18N.get("datasource.InstallStandardDataSourceQueryChoosersPlugIn.compressed") + " "
                + description, new String[]{"zip", "gz"}));
    }
}
