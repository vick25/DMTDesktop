package com.osfac.dmt.workbench;

/**
 * Install most of the menus and toolbar buttons. Called when the app starts up.
 */
public interface Setup {

    public void setup(WorkbenchContext workbenchContext) throws Exception;
}
