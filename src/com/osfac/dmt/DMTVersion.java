package com.osfac.dmt;

import com.osfac.dmt.workbench.ui.WorkbenchFrame;

public class DMTVersion {

    public static final DMTVersion CURRENT_VERSION = new DMTVersion();

    public String toString() {
        String ver = I18N.get("JUMPWorkbench.version.number");
        String releaseInfo = WorkbenchFrame.TypeOfVersion;
        if (releaseInfo != null && releaseInfo.length() > 0) {
            return ver + "  " + releaseInfo;
        }
        return ver;
    }
}