package com.cadplan.jump;

import com.cadplan.fileio.IconLoader;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.WorkbenchToolBar;
import javax.swing.*;

public class VertexNotePlugin extends AbstractPlugIn {

    private I18NPlug iPlug;

    public void initialize(PlugInContext context) throws Exception {
        iPlug = new I18NPlug("VertexSymbols", "language.VertexSymbolsPlugin");

        EnableCheckFactory check = new EnableCheckFactory(context.getWorkbenchContext());
        EnableCheck scheck = check.createAtLeastNFeaturesMustBeSelectedCheck(1);
        MultiEnableCheck mcheck = new MultiEnableCheck();

        mcheck.add(check.createAtLeastNLayersMustExistCheck(1));
        mcheck.add(check.createAtLeastNLayersMustBeEditableCheck(1));
        //mcheck.add(check.createAtLeastNFeaturesMustBeSelectedCheck(1));

        String menuName = MenuNames.PLUGINS; //iPlug.get("VertexSymbols.MenuName");
        String menuItem = "Vertex Note"; //iPlug.get("VertexSymbols.MenuItem");
        context.getFeatureInstaller().addMainMenuItem(this, new String[]{menuName},
                menuItem, false, null, scheck);



        String dirName = context.getWorkbenchContext().getWorkbench().getPlugInManager().getPlugInDirectory().getAbsolutePath();
        IconLoader loader = new IconLoader(dirName, "VertexSymbols");

        //Image image = loader.loadImage("noteicon.gif");
        //ImageIcon icon = new ImageIcon(image);
        //System.out.println("Note Resource path: "+this.getClass().getResource("/Resources/noteicon.gif"));
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Resources/noteicon.gif"));



        context.getFeatureInstaller().addPopupMenuItem(LayerViewPanel.popupMenu(), this, menuItem, false, icon,
                scheck);

        WorkbenchToolBar toolBar = context.getWorkbenchFrame().getToolBar();


        JButton button = toolBar.addPlugIn(icon, this, mcheck, context.getWorkbenchContext());
        //JButton button = toolBar.addPlugIn(new ImageIcon(image),this,check.createAtLeastNFeaturesMustBeSelectedCheck(1),context.getWorkbenchContext());

    }

    public boolean execute(PlugInContext context) throws Exception {
        VertexNote vn = new VertexNote(context, iPlug);
        return true;
    }
}
