package com.osfac.dmt.io.datasource;

import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.DriverProperties;
import com.osfac.dmt.io.FMEGMLReader;
import com.osfac.dmt.io.FMEGMLWriter;
import com.osfac.dmt.io.GMLReader;
import com.osfac.dmt.io.GMLWriter;
import com.osfac.dmt.io.JMLReader;
import com.osfac.dmt.io.JMLWriter;
import com.osfac.dmt.io.JUMPReader;
import com.osfac.dmt.io.JUMPWriter;
import com.osfac.dmt.io.ShapefileReader;
import com.osfac.dmt.io.ShapefileWriter;
import com.osfac.dmt.io.WKTReader;
import com.osfac.dmt.io.WKTWriter;
import com.osfac.dmt.util.Block;
import com.osfac.dmt.util.CollectionUtil;
import java.util.Arrays;
import java.util.Collection;

/**
 * Contains DataSource classes for the standard JUMP Readers and Writers.
 * DataSource implementations cannot be anonymous classes if they are to be
 * saved to a project file (because the class name is saved).
 */
public abstract class StandardReaderWriterFileDataSource
        extends ReaderWriterFileDataSource {

    protected String[] extensions;
    public static final String[] GML_EXTENSIONS = new String[]{"gml", "xml"};
    public static final String OUTPUT_TEMPLATE_FILE_KEY = "Output Template File";
    public static final String INPUT_TEMPLATE_FILE_KEY = "Input Template File";

    public StandardReaderWriterFileDataSource(
            JUMPReader reader,
            JUMPWriter writer,
            String[] extensions) {
        super(reader, writer);
        this.extensions = extensions;
    }

    /**
     * The first JUMP Readers took responsibility for handling .zip and .gz
     * files (a more modular design choice would have been to handle compression
     * outside of the Readers); this class uses a
     * DelegatingCompressedFileHandler to ensure that these JUMP Readers receive
     * the properties they need to do decompression.
     */
    private static class ClassicReaderWriterFileDataSource extends StandardReaderWriterFileDataSource {

        public ClassicReaderWriterFileDataSource(
                JUMPReader reader,
                JUMPWriter writer,
                String[] extensions) {
            super(new DelegatingCompressedFileHandler(reader, toEndings(extensions)), writer, extensions);
            this.extensions = extensions;
        }
    }

    //DataSources must have a parameterless constructor so they can be
    //reconstructed by Java2XML. [Bob Boseko] 
    public String[] getExtensions() {
        return extensions;
    }

    private static GMLWriter createGMLWriter() {
        return new GMLWriter();
    }

    private static DelegatingCompressedFileHandler createGMLReader() {
        return new DelegatingCompressedFileHandler(
                new GMLReader(),
                toEndings(StandardReaderWriterFileDataSource.GML_EXTENSIONS)) {
            public FeatureCollection read(DriverProperties dp) throws Exception {
                mangle(
                        dp,
                        "TemplateFile",
                        "CompressedFileTemplate",
                        Arrays.asList(new String[]{"_input.xml", ".input", ".template"}));
                return super.read(dp);
            }
        };
    }

    public static Collection toEndings(String[] extensions) {
        return CollectionUtil.collect(Arrays.asList(extensions), new Block() {
            public Object yield(Object extension) {
                return "." + extension;
            }
        });
    }

    public static class JML extends ClassicReaderWriterFileDataSource {

        public JML() {
            super(new JMLReader(), new JMLWriter(), new String[]{"dmt"});
        }
    }

    public static class WKT extends ClassicReaderWriterFileDataSource {

        public WKT() {
            super(new WKTReader(), new WKTWriter(), new String[]{"wkt", "txt"});
        }
    }

    public static class Shapefile extends ClassicReaderWriterFileDataSource {

        public Shapefile() {
            super(new ShapefileReader(), new ShapefileWriter(), new String[]{"shp"});
        }
    }

    public static class FMEGML extends ClassicReaderWriterFileDataSource {

        public FMEGML() {
            super(new FMEGMLReader(), new FMEGMLWriter(), new String[]{"gml", "xml", "fme"});
        }
    }

    public static class GML extends ClassicReaderWriterFileDataSource {

        public GML() {
            super(createGMLReader(), createGMLWriter(), StandardReaderWriterFileDataSource.GML_EXTENSIONS);
        }

        protected DriverProperties getReaderDriverProperties() {
            return super.getReaderDriverProperties().set(
                    "TemplateFile",
                    (String) getProperties().get(StandardReaderWriterFileDataSource.INPUT_TEMPLATE_FILE_KEY));
        }

        protected DriverProperties getWriterDriverProperties() {
            return super.getWriterDriverProperties().set(
                    "TemplateFile",
                    (String) getProperties().get(StandardReaderWriterFileDataSource.OUTPUT_TEMPLATE_FILE_KEY));
        }

        public boolean isReadable() {
            return getProperties().containsKey(StandardReaderWriterFileDataSource.INPUT_TEMPLATE_FILE_KEY);
        }

        public boolean isWritable() {
            return getProperties().containsKey(StandardReaderWriterFileDataSource.OUTPUT_TEMPLATE_FILE_KEY);
        }
    }
}
