package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.famfam.IconLoaderFamFam;
import com.osfac.dmt.workbench.ui.plugin.ExportImagePlugIn;
import com.vividsolutions.jts.util.Assert;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class CopyImagePlugIn extends ExportImagePlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        Transferable transferable = createTransferable(context);
        if (transferable == null) {
            context.getWorkbenchFrame()
                    .warnUser(I18N.get("ui.plugin.clipboard.CopyImagePlugIn.could-not-copy-the-image-for-some-reason"));
            return false;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(transferable, new DummyClipboardOwner());
        return true;
    }
    public static final ImageIcon ICON = IconLoaderFamFam.icon("image_copy.gif");

    private Transferable createTransferable(final PlugInContext context) {
        return new AbstractTransferable(
                new DataFlavor[]{DataFlavor.imageFlavor}) {
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                Assert.isTrue(flavor == DataFlavor.imageFlavor);
                return image(context.getLayerViewPanel());
            }
        };
    }

    public static MultiEnableCheck createEnableCheck(
            WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(
                workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory
                .createWindowWithLayerViewPanelMustBeActiveCheck()).add(new EnableCheck() {
            public String check(JComponent component) {
                //Need Java 1.4's ability to auto-convert DataFlavor.imageFlavor to
                //the native image format for the platform 
                //(see http://access1.sun.com/tutorials/Swing_Tutorial/Dnd-Merlin-Tutorial/3.html).
                //[Bob Boseko 11/6/2003]
                return !java14OrNewer()
                        ? I18N.get("ui.plugin.clipboard.CopyImagePlugIn.this-feature-requires-Java-1.4-or-newer")
                        : null;
            }
        });
    }
}