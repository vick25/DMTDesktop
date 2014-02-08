package de.latlon.deejump.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerTreeModel;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.ColorPanel;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.TreeLayerNamePanel;
import com.osfac.dmt.workbench.ui.renderer.style.BasicStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import org.openjump.core.ui.images.IconLoader;

/**
 * ...
 *
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: michaudm $
 *
 * @version 2.0, $Revision: 2261 $, $Date: 2011-05-08 18:59:01 +0200 (dim., 08
 * mai 2011) $
 *
 * @since 2.0
 */
public class SaveLegendPlugIn extends AbstractPlugIn {

    public SaveLegendPlugIn() {
        //nothing
    }
    private String filename = null;

    public void initialize(PlugInContext context) throws Exception {

        /*
         context.getFeatureInstaller().addPopupMenuItem(
         context.getWorkbenchContext().getWorkbench().getFrame().getLayerNamePopupMenu(), 
         this, 
         this.getName()+"{pos:13}", 				
         false, 
         null, 
         null);
         */

        EnableCheckFactory enableCheckFactory = new EnableCheckFactory(context
                .getWorkbenchContext());
        EnableCheck enableCheck = new MultiEnableCheck().add(
                enableCheckFactory
                .createWindowWithLayerManagerMustBeActiveCheck()).add(
                enableCheckFactory.createExactlyNLayersMustBeSelectedCheck(1));

        context.getFeatureInstaller().addPopupMenuItem(
                context.getWorkbenchFrame().getLayerNamePopupMenu(),
                this, new String[]{MenuNames.STYLE},
                this.getName() + "...", false, getIcon(), enableCheck);
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("save_legend.png");
    }

    public boolean execute(PlugInContext context) throws Exception {

        final Layer layer = context.getSelectedLayer(0);

        LayerNamePanel layerPanel = context.getLayerNamePanel();
        if (!(layerPanel instanceof TreeLayerNamePanel)) {
            return false;

        }

        JTree newTree =
                createLayerTree(layer, (TreeLayerNamePanel) layerPanel);

        saveLegend(context, newTree);

        return true;
    }

    private void saveLegend(PlugInContext context, JTree tree)
            throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText(I18N.get("deejump.plugin.SaveLegendPlugIn.Save"));
        fileChooser.setDialogTitle(I18N.get("deejump.plugin.SaveLegendPlugIn.Save-legend-as-image-png"));


        JPanel p = new JPanel();
        p.add(tree);

        fileChooser.setAccessory(p);

        if (JFileChooser.APPROVE_OPTION
                == fileChooser.showOpenDialog(context.getWorkbenchFrame())) {


            // can only save if has a sizw -> put in a frame
            JFrame f = new JFrame(I18N.get("deejump.plugin.SaveLegendPlugIn.Save-legend-as-image-png"));
            f.getContentPane().add(tree);
            f.pack();
//    		f.setVisible( true );

            saveComponentAsJPEG(tree,
                    fileChooser.getSelectedFile().getAbsolutePath());

            f.setVisible(false);
            f.dispose();

        }

    }

    private JTree createLayerTree(Layer layer, TreeLayerNamePanel treePanel) {


        JTree tree = treePanel.getTree();

        DefaultMutableTreeNode rootNode =
                new DefaultMutableTreeNode(layer.getName());

        for (int j = 0; j < tree.getModel().getChildCount(layer); j++) {
            rootNode.add(
                    new DefaultMutableTreeNode(
                    tree.getModel().getChild(layer, j)));
        }
        JTree newTree = new JTree(rootNode);
        newTree.setCellRenderer(createColorThemingValueRenderer());

        return newTree;
    }

    public String getName() {
        return I18N.get("deejump.plugin.SaveLegendPlugIn.Save-legend");
    }

    public static void saveComponentAsJPEG(Component myComponent,
            String filename)
            throws IOException {
        //-- [sstein, 06.08.2006]
        if (!filename.endsWith(".png")) {
            filename = filename + ".png";
        }
        //--
        Dimension size = myComponent.getSize();
        BufferedImage myImage =
                new BufferedImage(size.width, size.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = myImage.createGraphics();
        myComponent.paint(g2);


        ImageIO.write((RenderedImage) myImage, "PNG", new File(filename));

    }

    private TreeCellRenderer createColorThemingValueRenderer() {
        return new TreeCellRenderer() {
            private JPanel panel = new JPanel(new GridBagLayout());
            private ColorPanel colorPanel = new ColorPanel();
            private JLabel label = new JLabel();

            {
                panel.add(colorPanel,
                        new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

                panel.add(label, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

                panel.setBackground(Color.white);
                label.setBackground(Color.white);

            }

            public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {

                String txt;

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();

                if (userObject instanceof LayerTreeModel.ColorThemingValue) {

                    LayerTreeModel.ColorThemingValue entry =
                            (LayerTreeModel.ColorThemingValue) userObject;

                    txt = entry.toString();

                    BasicStyle style = entry.getStyle();
                    colorPanel.setLineColor(style.isRenderingLine()
                            ? GUIUtil.alphaColor(style.getLineColor(), style
                            .getAlpha())
                            : GUIUtil.alphaColor(Color.BLACK, 0));
                    colorPanel.setFillColor(style.isRenderingFill()
                            ? GUIUtil.alphaColor(style.getFillColor(), style
                            .getAlpha())
                            : GUIUtil.alphaColor(Color.BLACK, 0));

                } else {

                    txt = (String) userObject;
                    colorPanel.setFillColor(Color.white);
                    colorPanel.setLineColor(Color.white);
                }

                label.setText(txt);


                return panel;
            }
        };
    }
}


/* ********************************************************************
Changes to this class. What the people have been up to:
$Log$
Revision 1.2  2006/08/06 16:48:28  mentaer
changed menu pos
improved SaveLegendPlugIn by adding file ending ".png"

Revision 1.1  2006/08/06 16:22:11  mentaer
added savelegend

Revision 1.1  2006/03/06 09:42:11  ut
latest changes (key pan, legend, etc)


********************************************************************** */