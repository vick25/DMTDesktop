package com.osfac.dmt.workbench;

import com.jidesoft.icons.IconsFactory;
import javax.swing.ImageIcon;

public class DMTIconsFactory {

    public static class Standard {
        // ---- file menu

        public static final String NEW_PROJECT = "/com/osfac/dmt/images/project.png";
        public static final String NEW_LAYER = "/com/osfac/dmt/images/layer.png";
        public static final String NEW_CATEGORY = "/com/osfac/dmt/images/category.png";
        public static final String OPEN = "/com/osfac/dmt/images/open.png";
        public static final String OPEN_FILE = "/com/osfac/dmt/images/open_file.png";
        public static final String OPEN_PROJECT = "/com/osfac/dmt/images/open_project.png";
        public static final String RUN_DATASTORE_QUERY = "/com/osfac/dmt/images/sql.png";
        public static final String ADD_IMAGE_LAYER = "/com/osfac/dmt/images/add_image.png";
        public static final String SAVE_DATASET = "/com/osfac/dmt/images/save_dataset.png";
        public static final String SAVE_PROJECT = "/com/osfac/dmt/images/save_project.png";
        public static final String SAVE_PROJECT_AS = "/com/osfac/dmt/images/save_project_as.png";
        public static final String COPY_VIEW = "/com/osfac/dmt/images/copy_view.gif";
        public static final String EXIT = "/com/osfac/dmt/images/exit.png";
        //---Tool bar
        public static final String NEW_PROJECT_BIG = "/com/osfac/dmt/images/project_big.png";
        public static final String OPEN_BIG = "/com/osfac/dmt/images/open_big.png";
        public static final String SAVE_DATASETS = "/com/osfac/dmt/images/save_datasets.png";
        public static final String HELP = "/com/osfac/dmt/images/help.png";
        //---Tool bar - Tools Queries
        public static final String SPATIAL_QUERY = "/com/osfac/dmt/images/spatial_query.png";
        public static final String ATTRB_QUERY = "/com/osfac/dmt/images/regular-expression-search.png";
        public static final String SIMPLE_QUERY = "/com/osfac/dmt/images/simple_query.png";
        public static final String SEARCH_ALL_ATTRB = "/com/osfac/dmt/images/search.png";
        //---Map Tool bar
        public static final String ZOOM_IN_OUT = "/com/osfac/dmt/images/Magnify2.gif";
        public static final String HAND = "/com/osfac/dmt/images/BigHand.gif";
        public static final String FULL_EXTENT = "/com/osfac/dmt/images/World2.gif";
        public static final String ZOOM_SELECTED = "/com/osfac/dmt/images/ZoomSelected.gif";
        public static final String ZOOM_REALTIME = "/com/osfac/dmt/images/Magnify3.gif";
        public static final String ZOOM_FENCE = "/com/osfac/dmt/images/ZoomFence.gif";
        public static final String ZOOM_NEXT = "/com/osfac/dmt/images/go_next.png";
        public static final String ZOOM_PREVIOUS = "/com/osfac/dmt/images/go_previous.png";
        public static final String PALETTE = "/com/osfac/dmt/images/palette.png";
        public static final String ATTRIBUTES = "/com/osfac/dmt/images/attributes.gif";
        public static final String SELECT = "/com/osfac/dmt/images/Select.gif";
        public static final String DESELECT = "/com/osfac/dmt/images/deselect.gif";
        public static final String FENCE = "/com/osfac/dmt/images/Fence.png";
        public static final String INFO = "/com/osfac/dmt/images/info.png";
        public static final String EDITTOOL = "/com/osfac/dmt/images/EditingToolbox.gif";
        public static final String DISTANCE = "/com/osfac/dmt/images/Ruler.gif";
        public static final String AREA = "/com/osfac/dmt/images/Ruler_area.gif";
        public static final String FRAME = "/com/osfac/dmt/images/Frame.gif";
        //---edit menu
        public static final String UNDO = "/com/osfac/dmt/images/undo.png";
        public static final String REDO = "/com/osfac/dmt/images/redo.png";
        public static final String CUT = "/com/osfac/dmt/images/cut.gif";
        public static final String COPY = "/com/osfac/dmt/images/copy.gif";
        public static final String PASTE = "/com/osfac/dmt/images/paste.gif";
        // ----tools menu
        public static final String DATABASE = "/com/osfac/dmt/images/database.png";
        public static final String REQUEST = "/com/osfac/dmt/images/gnome-session-switch(2).png";
        public static final String SYNC = "/com/osfac/dmt/images/refresh.png";
        public static final String STATISTIC = "/com/osfac/dmt/images/statistic.png";
        public static final String QUERYSEARCH = "/com/osfac/dmt/images/search16.png";
        public static final String USER = "/com/osfac/dmt/images/user.png";
        public static final String SETTING = "/com/osfac/dmt/images/settings.png";
        // ----window menu
        public static final String LOG = "/com/osfac/dmt/images/log.png";
        public static final String MOSAIC = "/com/osfac/dmt/images/mosaic.png";
        // ----help menu
        public static final String ABOUT = "/com/osfac/dmt/images/about.png";
        public static final String UPDATE = "/com/osfac/dmt/images/refresh.png";
    }

    public static class ShortCut {

        public static final String SHORTCUT = "/com/osfac/dmt/images/display.png";
    }

    public static class DMTIcon {

        public static final String ICON = "/com/osfac/dmt/images/icon.png";
        public static final String ICON2 = "/com/osfac/dmt/images/icon2.png";
        public static final String UNZIP = "/com/osfac/dmt/images/unzip.png";
        public static final String UNZIP2 = "/com/osfac/dmt/images/unzip2.png";
        public static final String STACK = "/com/osfac/dmt/images/preview.png";
        public static final String LAYER2 = "/com/osfac/dmt/images/layer2.png";
        public static final String FIND_DATA = "/com/osfac/dmt/images/find_data.png";
        public static final String REQUESTDATA = "/com/osfac/dmt/images/update.png";
        public static final String DOWNLOADDATA = "/com/osfac/dmt/images/download_manager(7).png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null) {
            return IconsFactory.getImageIcon(DMTIconsFactory.class, name);
        } else {
            return null;
        }
    }
}
