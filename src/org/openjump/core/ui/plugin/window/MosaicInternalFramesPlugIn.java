package org.openjump.core.ui.plugin.window;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import org.openjump.core.ui.images.IconLoader;
import org.openjump.core.ui.plugin.AbstractUiPlugIn;

/**
 * A plugin to layout opened internal frames as a mosaic.
 *
 * @author Michael Michaud
 * @version 0.1 (2008-04-06)
 * @since 1.2F
 */
public class MosaicInternalFramesPlugIn extends AbstractUiPlugIn {

    public static final ImageIcon ICON = IconLoader.icon("application_mosaic.png");

    public MosaicInternalFramesPlugIn() {
        super(I18N.get("org.openjump.core.ui.plugin.window.MosaicInternalFramesPlugIn.Mosaic"), ICON);
    }

    public void initialize(PlugInContext context) throws Exception {
        super.initialize(context);
        final JMenuItem jmi = context.getFeatureInstaller().addMainMenuItem(
                new String[]{MenuNames.WINDOW}, this, Integer.MAX_VALUE);

    }

    public EnableCheck getEnableCheck() {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }

    public boolean execute(PlugInContext context) throws Exception {

        JInternalFrame[] iframes = context.getWorkbenchFrame().getInternalFrames();
        // number of opened internal frames 
        int nbFrames = iframes.length;
        int n = nbFrames;
        for (int i = 0; i < nbFrames; ++i) {
            if (iframes[i].isIcon()) {
                --n;
            }
        }
        // give some place for iconified internal frames
        // the necessary place may vary with the look and field
        // 30 will let a white space for window look and field
        int iconified_frame_strip = 0;
        if (n != nbFrames) {
            iconified_frame_strip = 30;
        }
        // compute column number
        if (n == 0) {
            return true;
        }
        int nColumns = (int) Math.sqrt(n), nLines;
        if (n != nColumns * nColumns) {
            ++nColumns;
        }
        // compute line number
        if ((n - 1) / nColumns + 1 < nColumns) {
            nLines = nColumns - 1;
        } else {
            nLines = nColumns;
        }
        int dx = context.getWorkbenchFrame().getDesktopPane().getWidth() / nColumns;
        int dy = context.getWorkbenchFrame().getDesktopPane().getHeight() / nLines - iconified_frame_strip;
        int k = 0;
        for (int i = 0; i < nColumns; ++i) {
            for (int j = 0; j < nColumns && k < n; ++j, ++k) {
                iframes[i * nColumns + j].setBounds(j * dx, i * dy, dx, dy);
            }
        }

        return true;

    }
}
