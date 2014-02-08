package com.osfac.dmt.workbench.ui.cursortool.editing;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.DMTIconsFactory;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.cursortool.NodeLineStringsTool;
import com.osfac.dmt.workbench.ui.cursortool.QuasimodeTool;
import com.osfac.dmt.workbench.ui.cursortool.SelectFeaturesTool;
import com.osfac.dmt.workbench.ui.cursortool.SelectLineStringsTool;
import com.osfac.dmt.workbench.ui.cursortool.SelectPartsTool;
import com.osfac.dmt.workbench.ui.cursortool.SplitLineStringTool;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxPlugIn;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.openjump.core.ui.plugin.edittoolbox.cursortools.ScaleSelectedItemsTool;

public class EditingPlugIn extends ToolboxPlugIn {

    public String getName() {
        return I18N.get("ui.cursortool.editing.EditingPlugIn.editing-toolbox");
    }
    //public static ImageIcon ICON = IconLoaderFamFam.icon("page_white_wrench.png");
    public static ImageIcon ICON = IconLoader.icon("EditingToolbox.gif");
    public static final String KEY = EditingPlugIn.class.getName();
    private JButton optionsButton = new JButton(I18N.get("ui.cursortool.editing.EditingPlugIn.options"));

    public void initialize(PlugInContext context) throws Exception {
        context.getWorkbenchContext().getWorkbench().getBlackboard().put(KEY, this);
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        //-- [sstein, 15.07.2006] set again in correct language
        optionsButton.setFocusable(false);
        optionsButton.setText(I18N.get("ui.cursortool.editing.EditingPlugIn.options"));
        optionsButton.setIcon(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.SETTING));
        //The auto-generated title "Editing Toolbox" is too long to fit. [Bob Boseko]
        toolbox.setTitle(I18N.get("ui.cursortool.editing.EditingPlugIn.editing"));
        EnableCheckFactory checkFactory = new EnableCheckFactory(toolbox.getContext());
        //Null out the quasimodes for [Ctrl] because the Select tools will handle that case. [Bob Boseko]
        toolbox.add(
                new QuasimodeTool(new SelectFeaturesTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false),
                null));
        toolbox.add(
                new QuasimodeTool(new SelectPartsTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false),
                null));
        toolbox.add(
                new QuasimodeTool(new SelectLineStringsTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false),
                null));
        toolbox.add(new MoveSelectedItemsTool(checkFactory));

        toolbox.addToolBar();
        toolbox.add(DrawRectangleTool.create(toolbox.getContext()));
        toolbox.add(DrawPolygonTool.create(toolbox.getContext()));
        toolbox.add(DrawLineStringTool.create(toolbox.getContext()));
        toolbox.add(DrawPointTool.create(toolbox.getContext()));

        toolbox.addToolBar();
        toolbox.add(new InsertVertexTool(checkFactory));
        toolbox.add(new DeleteVertexTool(checkFactory));
        toolbox.add(new MoveVertexTool(checkFactory));
        //-- [sstein: 11.12.2006] added here to fill toolbox 
        toolbox.add(new ScaleSelectedItemsTool(checkFactory));

        toolbox.addToolBar();
        toolbox.add(new SnapVerticesTool(checkFactory));
        toolbox.add(new SnapVerticesToSelectedVertexTool(checkFactory));
        toolbox.add(new SplitLineStringTool());
        toolbox.add(new NodeLineStringsTool());

        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionShowOptionDialog();
            }
        });
        toolbox.getCenterPanel().add(optionsButton, BorderLayout.CENTER);
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 20, false));
        toolbox.setResizable(false);
    }

    protected JButton getOptionsButton() {
        return optionsButton;
    }
}