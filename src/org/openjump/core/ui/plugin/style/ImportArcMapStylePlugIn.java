package org.openjump.core.ui.plugin.style;

import static com.osfac.dmt.I18N.get;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import static com.osfac.dmt.workbench.ui.MenuNames.LAYER;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import static com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn.get;
import java.io.BufferedReader;
import java.io.File;
import static java.io.File.createTempFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.xml.parsers.DocumentBuilderFactory;
import static javax.xml.parsers.DocumentBuilderFactory.newInstance;
import static org.openjump.core.ui.plugin.style.ImportSLDPlugIn.importSLD;
import org.w3c.dom.Document;

/**
 * <code>ImportArcMapStylePlugIn</code>
 *
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: stranger $
 *
 * @version $Revision: 1424 $, $Date: 2008-05-21 15:49:21 +0200 (mer., 21 mai
 * 2008) $
 */
public class ImportArcMapStylePlugIn extends AbstractPlugIn {

    @Override
    public void initialize(PlugInContext context) throws Exception {
        EnableCheckFactory enableCheckFactory = new EnableCheckFactory(context.getWorkbenchContext());

        EnableCheck enableCheck = new MultiEnableCheck().add(
                enableCheckFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                enableCheckFactory.createExactlyNLayerablesMustBeSelectedCheck(1, Layerable.class));

        context.getFeatureInstaller().addMainMenuItem(this, new String[]{LAYER},
                get("org.openjump.core.ui.plugin.style.ImportArcMapStylePlugIn.name"), false, null, enableCheck);
    }

    private static File findArcMap2SLD(WorkbenchFrame wbframe, Blackboard bb) throws IOException, InterruptedException {
        String arcmap2sld = (String) bb.get("ArcMapStylePlugin.toollocation");
        if (arcmap2sld == null) {
            File tmp = createTempFile("amtsldreg", null);
            ProcessBuilder pb = new ProcessBuilder("regedit", "/e", tmp.toString(),
                    "HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion");
            pb.start().waitFor();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(tmp), "UTF-16"))) {
                String s;
                while ((s = in.readLine()) != null) {
                    if (s.startsWith("\"ProgramFilesDir\"=\"")) {
                        s = s.split("=")[1];
                        s = s.substring(1, s.length() - 1);
                        arcmap2sld = s + "\\i3mainz\\ArcMap2SLD_Full_Setup\\ArcGIS_SLD_Converter.exe";
                        break;
                    }
                }
            }
            tmp.delete();
        }

        JFileChooser chooser = new JFileChooser();

        File am2sld = arcmap2sld == null ? null : new File(arcmap2sld);
        if (am2sld == null || !am2sld.exists()) {
            showMessageDialog(wbframe,
                    get("org.openjump.core.ui.plugin.style.ImportArcMapStylePlugIn.Must-Select-Location-Of-Tool"),
                    get("org.openjump.core.ui.plugin.style.ImportSLDPlugIn.Question"), INFORMATION_MESSAGE);
            if (arcmap2sld != null) {
                chooser.setSelectedFile(new File(arcmap2sld));
            }

            int res = chooser.showOpenDialog(wbframe);
            if (res == APPROVE_OPTION) {
                am2sld = chooser.getSelectedFile();
                if (!am2sld.exists()) {
                    return null;
                }
                bb.put("ArcMapStylePlugin.toollocation", am2sld.getAbsoluteFile().toString());
            } else {
                return null;
            }
        }

        return am2sld;
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        Blackboard bb = get(context.getWorkbenchContext());
        WorkbenchFrame wbframe = context.getWorkbenchFrame();

        String fileName = (String) bb.get("ArcMapStylePlugin.filename");

        File am2sld = findArcMap2SLD(wbframe, bb);
        if (am2sld == null) {
            return false;
        }

        ProcessBuilder pb = new ProcessBuilder(am2sld.toString());
        pb.start().waitFor(); // unfortunately, the code seems to always be
        // zero

        showMessageDialog(wbframe,
                get("org.openjump.core.ui.plugin.style.ImportArcMapStylePlugIn.Must-Select-Location-Of-SLD"),
                get("org.openjump.core.ui.plugin.style.ImportSLDPlugIn.Question"), INFORMATION_MESSAGE);

        JFileChooser chooser = new JFileChooser();

        if (fileName != null) {
            chooser.setCurrentDirectory(new File(fileName).getParentFile());
        }

        int res = chooser.showOpenDialog(context.getWorkbenchFrame());
        if (res == APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (!f.exists()) {
                return false;
            }
            bb.put("ArcMapStylePlugin.filename", f.getAbsoluteFile().toString());

            DocumentBuilderFactory dbf = newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(f);

            importSLD(doc, context);
        }

        return false;
    }

    @Override
    public String getName() {
        return get("org.openjump.core.ui.plugin.style.ImportArcMapStylePlugIn.name");
    }
}
