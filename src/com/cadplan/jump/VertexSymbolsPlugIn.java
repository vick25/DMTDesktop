package com.cadplan.jump;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.WorkbenchToolBar;
import javax.swing.*;

public class VertexSymbolsPlugIn extends AbstractPlugIn {

    private I18NPlug iPlug;

    public void initialize(PlugInContext context) throws Exception {
        iPlug = new I18NPlug("VertexSymbols", "language.VertexSymbolsPlugin");
        EnableCheckFactory check = new EnableCheckFactory(context.getWorkbenchContext());
        MultiEnableCheck mcheck = new MultiEnableCheck();
        mcheck.add(check.createAtLeastNLayersMustExistCheck(1));
        mcheck.add(check.createAtLeastNLayersMustBeEditableCheck(1));


        String menuName = MenuNames.PLUGINS; //iPlug.get("VertexSymbols.MenuName");
        String menuItem = iPlug.get("VertexSymbols.MenuItem");
        context.getFeatureInstaller().addMainMenuItem(this, new String[]{menuName},
                menuItem, false, null, mcheck);

        String dirName = context.getWorkbenchContext().getWorkbench().getPlugInManager().getPlugInDirectory().getAbsolutePath();
        //IconLoader loader = new IconLoader(dirName,"VertexSymbols");
        //Image image = loader.loadImage("vsicon.gif");
        //ImageIcon icon = new ImageIcon(image);
        // System.out.println("Symbols Resource path: "+this.getClass().getResource("/Resources/vsicon.gif"));
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Resources/vsicon.gif"));

        WorkbenchToolBar toolBar = context.getWorkbenchFrame().getToolBar();





        JButton button = toolBar.addPlugIn(icon, this, mcheck, context.getWorkbenchContext());
        LoadImages imageLoader = new LoadImages(context);
        //System.out.println("Initialize plugin");
        VertexParams.context = context.getWorkbenchContext();

    }

    public boolean execute(PlugInContext context) throws Exception {
        VertexSymbols vs = new VertexSymbols(context, iPlug);
        return true;
    }
}
