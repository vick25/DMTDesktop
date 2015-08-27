package com.osfac.dmt.workbench.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Workbench filter that filters by fileType.
 */
public class WorkbenchFileFilter extends FileFilter {

    private String description;

    //CVS issue:
    //
    //This class used to be called JcsFileFilter. I renamed it to JCSFileFilter
    //and the Unix developers reported they were getting errors. So I deleted
    //the file from the CVS repository and tried to re-add it, but I got
    //CVS errors:
    //
    //  cvs server: cannot add file `com/vividsolutions/jcs/workbench/ui/JCSFileFilter.java'
    //  when RCS file `/home/cvs/jcs/jcs/com/vividsolutions/jcs/workbench/ui/JCSFileFilter.java,v'
    //  already exists
    //  cvs [server aborted]: correct above errors first!
    //
    //This is a CVS bug mentioned on the Usenet newsgroups (deleting and re-adding
    //a file). So I just renamed it to something totally different:
    //WorkbenchFileFilter. [Bob Boseko]
    public WorkbenchFileFilter(String fileType) {
        description = fileType;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String extension = GUIUtil.getExtension(file);

        if (extension != null) {
            //changed by david to be more readable and maintainable
            if (description.equals(GUIUtil.jmlDesc)) {
                return extension.equals(GUIUtil.jml)
                        || extension.equals("zip") || extension.equals("gz");
            }

            if (description.equals(GUIUtil.xmlDesc)) {
                return extension.equals(GUIUtil.xml)
                        || extension.equals("zip") || extension.equals("gz");
            }

            if (description.equals(GUIUtil.shpDesc)) {
                return extension.equals(GUIUtil.shp)
                        || extension.equals("zip");
            }

            if (description.equals(GUIUtil.shxDesc)) {
                return extension.equals(GUIUtil.shx);
            }

            if (description.equals(GUIUtil.dbfDesc)) {
                return extension.equals(GUIUtil.dbf);
            }

            if (description.equals(GUIUtil.gmlDesc)) {
                return extension.equals(GUIUtil.gml)
                        || extension.equals(GUIUtil.fme) || extension.equals("zip")
                        || extension.equals("gz");
            }

            if (description.equals(GUIUtil.wktDesc)) {
                return extension.equals(GUIUtil.wkt)
                        || extension.equals("zip") || extension.equals("gz");
            }

            if (description.equals(GUIUtil.fmeDesc)) {
                return extension.equals(GUIUtil.xml)
                        || extension.equals(GUIUtil.fme) || extension.equals("zip")
                        || extension.equals("gz");
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
