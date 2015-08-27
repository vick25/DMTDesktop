package com.osfac.dmt.workbench;

import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.OverlayableIconsFactory;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.datastore.DataStoreDriver;
import com.osfac.dmt.datastore.DataStoreException;
import com.osfac.dmt.datastore.postgis.PostgisDataStoreDriver;
import com.osfac.dmt.setting.panel.GeneralPanel;
import com.osfac.dmt.workbench.datasource.AbstractSaveDatasetAsPlugIn;
import com.osfac.dmt.workbench.datasource.InstallStandardDataSourceQueryChoosersPlugIn;
import com.osfac.dmt.workbench.datasource.LoadDatasetPlugIn;
import com.osfac.dmt.workbench.datasource.SaveDatasetAsPlugIn;
import com.osfac.dmt.workbench.datastore.ConnectionManager;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.ApplicationExitHandler;
import com.osfac.dmt.workbench.ui.AttributeTab;
import com.osfac.dmt.workbench.ui.CloneableInternalFrame;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.cursortool.DrawPolygonFenceTool;
import com.osfac.dmt.workbench.ui.cursortool.DrawRectangleFenceTool;
import com.osfac.dmt.workbench.ui.cursortool.FeatureInfoTool;
import com.osfac.dmt.workbench.ui.cursortool.OrCompositeTool;
import com.osfac.dmt.workbench.ui.cursortool.QuasimodeTool;
import com.osfac.dmt.workbench.ui.cursortool.SelectFeaturesTool;
import com.osfac.dmt.workbench.ui.cursortool.editing.EditingPlugIn;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.AddNewCategoryPlugIn;
import com.osfac.dmt.workbench.ui.plugin.AddNewFeaturesPlugIn;
import com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn;
import com.osfac.dmt.workbench.ui.plugin.AddWMSDemoBoxEasterEggPlugIn;
import com.osfac.dmt.workbench.ui.plugin.ClearSelectionPlugIn;
import com.osfac.dmt.workbench.ui.plugin.CloneWindowPlugIn;
import com.osfac.dmt.workbench.ui.plugin.CombineSelectedFeaturesPlugIn;
import com.osfac.dmt.workbench.ui.plugin.CopySchemaPlugIn;
import com.osfac.dmt.workbench.ui.plugin.DeleteAllFeaturesPlugIn;
import com.osfac.dmt.workbench.ui.plugin.DeleteSelectedItemsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.EditSelectedFeaturePlugIn;
import com.osfac.dmt.workbench.ui.plugin.EditablePlugIn;
import com.osfac.dmt.workbench.ui.plugin.ExplodeSelectedFeaturesPlugIn;
import com.osfac.dmt.workbench.ui.plugin.FeatureInfoPlugIn;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.osfac.dmt.workbench.ui.plugin.FirstTaskFramePlugIn;
import com.osfac.dmt.workbench.ui.plugin.GenerateLogPlugIn;
import com.osfac.dmt.workbench.ui.plugin.InstallStandardFeatureTextWritersPlugIn;
import com.osfac.dmt.workbench.ui.plugin.MapToolTipsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.MoveLayerablePlugIn;
import com.osfac.dmt.workbench.ui.plugin.NewTaskPlugIn;
import com.osfac.dmt.workbench.ui.plugin.OptionsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.OutputWindowPlugIn;
import com.osfac.dmt.workbench.ui.plugin.PasteSchemaPlugIn;
import com.osfac.dmt.workbench.ui.plugin.RedoPlugIn;
import com.osfac.dmt.workbench.ui.plugin.RemoveSelectedCategoriesPlugIn;
import com.osfac.dmt.workbench.ui.plugin.RemoveSelectedLayersPlugIn;
import com.osfac.dmt.workbench.ui.plugin.SaveImageAsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.SaveProjectAsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.SaveProjectPlugIn;
import com.osfac.dmt.workbench.ui.plugin.SelectFeaturesInFencePlugIn;
import com.osfac.dmt.workbench.ui.plugin.SelectablePlugIn;
import com.osfac.dmt.workbench.ui.plugin.ShortcutKeysPlugIn;
import com.osfac.dmt.workbench.ui.plugin.UndoPlugIn;
import com.osfac.dmt.workbench.ui.plugin.VerticesInFencePlugIn;
import com.osfac.dmt.workbench.ui.plugin.ViewAttributesPlugIn;
import com.osfac.dmt.workbench.ui.plugin.ViewSchemaPlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.CopyImagePlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.CopySelectedItemsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.CopySelectedLayersPlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.CopyThisCoordinatePlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.CutSelectedItemsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.CutSelectedLayersPlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.PasteItemsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.clipboard.PasteLayersPlugIn;
import com.osfac.dmt.workbench.ui.plugin.datastore.AddDatastoreLayerPlugIn;
import com.osfac.dmt.workbench.ui.plugin.datastore.InstallDatastoreLayerRendererHintsPlugIn;
import com.osfac.dmt.workbench.ui.plugin.datastore.RefreshDataStoreLayerPlugin;
import com.osfac.dmt.workbench.ui.plugin.imagery.ImageLayerManagerPlugIn;
import com.osfac.dmt.workbench.ui.plugin.scalebar.InstallScaleBarPlugIn;
import com.osfac.dmt.workbench.ui.plugin.scalebar.ScaleBarPlugIn;
import com.osfac.dmt.workbench.ui.plugin.scalebar.ScaleBarRenderer;
import com.osfac.dmt.workbench.ui.plugin.wms.AddWMSQueryPlugIn;
import com.osfac.dmt.workbench.ui.plugin.wms.EditWMSQueryPlugIn;
import com.osfac.dmt.workbench.ui.renderer.LayerRendererFactory;
import com.osfac.dmt.workbench.ui.renderer.RenderingManager;
import com.osfac.dmt.workbench.ui.renderer.WmsLayerRendererFactory;
import com.osfac.dmt.workbench.ui.renderer.style.ArrowLineStringEndpointStyle;
import com.osfac.dmt.workbench.ui.renderer.style.ArrowLineStringSegmentStyle;
import com.osfac.dmt.workbench.ui.renderer.style.CircleLineStringEndpointStyle;
import com.osfac.dmt.workbench.ui.renderer.style.MetricsLineStringSegmentStyle;
import com.osfac.dmt.workbench.ui.renderer.style.VertexIndexLineSegmentStyle;
import com.osfac.dmt.workbench.ui.renderer.style.VertexXYLineSegmentStyle;
import com.osfac.dmt.workbench.ui.snap.InstallGridPlugIn;
import com.osfac.dmt.workbench.ui.snap.SnapToVerticesPolicy;
import com.osfac.dmt.workbench.ui.style.CopyStylesPlugIn;
import com.osfac.dmt.workbench.ui.style.PasteStylesPlugIn;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;
import com.osfac.dmt.workbench.ui.zoom.InstallZoomBarPlugIn;
import com.osfac.dmt.workbench.ui.zoom.PanTool;
import com.osfac.dmt.workbench.ui.zoom.ZoomBarPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomNextPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomPreviousPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomToClickPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomToCoordinatePlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomToFencePlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomToFullExtentPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomToLayerPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;
import com.osfac.dmt.workbench.ui.zoom.ZoomTool;
import com.vividsolutions.jts.util.Assert;
import de.latlon.deejump.plugin.style.DeeChangeStylesPlugIn;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Field;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import org.openjump.OpenJumpConfiguration;
import org.openjump.core.ui.plugin.mousemenu.DuplicateItemPlugIn;
import org.openjump.core.ui.plugin.tools.AdvancedMeasureOptionsPanel;
import org.openjump.core.ui.plugin.tools.AdvancedMeasureTool;
import org.openjump.core.ui.plugin.tools.ZoomRealtimeTool;
import org.openjump.core.ui.plugin.view.ShowScalePlugIn;
import org.openjump.core.ui.plugin.view.helpclassescale.InstallShowScalePlugIn;
import org.openjump.core.ui.plugin.view.helpclassescale.ShowScaleRenderer;

/**
 * Initializes the Workbench with various menus and cursor tools. Accesses the Workbench structure
 * through a WorkbenchContext.
 */
public class DMTConfiguration implements Setup {

    /**
     * Built-in plugins must be defined as instance variables, since they are located for
     * initialization via reflection on this class
     */
    private InstallShowScalePlugIn installShowScalePlugIn = new InstallShowScalePlugIn();
    private InstallScaleBarPlugIn installScaleBarPlugIn = new InstallScaleBarPlugIn();
    private InstallGridPlugIn installGridPlugIn = new InstallGridPlugIn();
//    private PersistentBlackboardPlugIn persistentBlackboardPlugIn = new PersistentBlackboardPlugIn();
    //FirstTaskFramePlugIn will be initialized using reflection in #initializePlugIns
    private FirstTaskFramePlugIn firstTaskFramePlugIn = new FirstTaskFramePlugIn();
    private InstallZoomBarPlugIn installZoomBarPlugIn = new InstallZoomBarPlugIn();
    private MoveLayerablePlugIn moveUpPlugIn = MoveLayerablePlugIn.UP;
    private InstallStandardDataSourceQueryChoosersPlugIn installStandardDataSourceQueryChoosersPlugIn = new InstallStandardDataSourceQueryChoosersPlugIn();
    private InstallStandardFeatureTextWritersPlugIn installStandardFeatureTextWritersPlugIn = new InstallStandardFeatureTextWritersPlugIn();
    private ShortcutKeysPlugIn shortcutKeysPlugIn = new ShortcutKeysPlugIn();
    private ClearSelectionPlugIn clearSelectionPlugIn = new ClearSelectionPlugIn();
    private EditWMSQueryPlugIn editWMSQueryPlugIn = new EditWMSQueryPlugIn();
    private MoveLayerablePlugIn moveDownPlugIn = MoveLayerablePlugIn.DOWN;
    private AddWMSQueryPlugIn addWMSQueryPlugIn = new AddWMSQueryPlugIn();
    private AddNewFeaturesPlugIn addNewFeaturesPlugIn = new AddNewFeaturesPlugIn();
    private OptionsPlugIn optionsPlugIn = new OptionsPlugIn();
    private AddNewCategoryPlugIn addNewCategoryPlugIn = new AddNewCategoryPlugIn();
    private CloneWindowPlugIn cloneWindowPlugIn = new CloneWindowPlugIn();
    private CopySelectedItemsPlugIn copySelectedItemsPlugIn = new CopySelectedItemsPlugIn();
    private CopyThisCoordinatePlugIn copyThisCoordinatePlugIn = new CopyThisCoordinatePlugIn();
    private CopyImagePlugIn copyImagePlugIn = new CopyImagePlugIn();
    private MapToolTipsPlugIn toolTipsPlugIn = new MapToolTipsPlugIn();
    private CopySelectedLayersPlugIn copySelectedLayersPlugIn = new CopySelectedLayersPlugIn();
    private AddNewLayerPlugIn addNewLayerPlugIn = new AddNewLayerPlugIn();
    private AddWMSDemoBoxEasterEggPlugIn addWMSDemoBoxEasterEggPlugIn = new AddWMSDemoBoxEasterEggPlugIn();
    private EditSelectedFeaturePlugIn editSelectedFeaturePlugIn = new EditSelectedFeaturePlugIn();
    private EditingPlugIn editingPlugIn = new EditingPlugIn();
    private EditablePlugIn editablePlugIn = new EditablePlugIn(editingPlugIn);
    private SelectablePlugIn selectablePlugIn = new SelectablePlugIn();
    // [Michael Michaud 2007-03-23] Moved BeanShellPlugIn initialization in OpenJUMPConfiguration
    // private BeanShellPlugIn beanShellPlugIn = new BeanShellPlugIn();
    private LoadDatasetPlugIn loadDatasetPlugIn = new LoadDatasetPlugIn();
    private SaveDatasetAsPlugIn saveDatasetAsPlugIn = new SaveDatasetAsPlugIn();
    //[sstein 18.Oct.2011] Not needed anymore after fix for MacOSX bug 3428076
    //private SaveDatasetAsFilePlugIn saveDatasetAsFilePlugIn = new SaveDatasetAsFilePlugIn();
    private SaveImageAsPlugIn saveImageAsPlugIn = new SaveImageAsPlugIn();
    private GenerateLogPlugIn generateLogPlugIn = new GenerateLogPlugIn();
    private NewTaskPlugIn newTaskPlugIn = new NewTaskPlugIn();
    // Moved to OpenJUMPConfigurationPlugIn
    //private OpenProjectPlugIn openProjectPlugIn = new OpenProjectPlugIn();
    private PasteItemsPlugIn pasteItemsPlugIn = new PasteItemsPlugIn();
    private PasteLayersPlugIn pasteLayersPlugIn = new PasteLayersPlugIn();
    private DeleteAllFeaturesPlugIn deleteAllFeaturesPlugIn = new DeleteAllFeaturesPlugIn();
    private DeleteSelectedItemsPlugIn deleteSelectedItemsPlugIn = new DeleteSelectedItemsPlugIn();
    private RemoveSelectedLayersPlugIn removeSelectedLayersPlugIn = new RemoveSelectedLayersPlugIn();
    private RemoveSelectedCategoriesPlugIn removeSelectedCategoriesPlugIn = new RemoveSelectedCategoriesPlugIn();
    private SaveProjectAsPlugIn saveProjectAsPlugIn = new SaveProjectAsPlugIn();
    private SaveProjectPlugIn saveProjectPlugIn = new SaveProjectPlugIn(saveProjectAsPlugIn);
    private SelectFeaturesInFencePlugIn selectFeaturesInFencePlugIn = new SelectFeaturesInFencePlugIn();
    private ShowScalePlugIn showScalePlugIn = new ShowScalePlugIn();
    private ScaleBarPlugIn scaleBarPlugIn = new ScaleBarPlugIn();
    private ZoomBarPlugIn zoomBarPlugIn = new ZoomBarPlugIn();
    private DeeChangeStylesPlugIn changeStylesPlugIn = new DeeChangeStylesPlugIn();
    private UndoPlugIn undoPlugIn = new UndoPlugIn();
    private RedoPlugIn redoPlugIn = new RedoPlugIn();
    private ViewAttributesPlugIn viewAttributesPlugIn = new ViewAttributesPlugIn();
    private ViewSchemaPlugIn viewSchemaPlugIn = new ViewSchemaPlugIn(editingPlugIn);
    private CopySchemaPlugIn copySchemaPlugIn = new CopySchemaPlugIn();
    private PasteSchemaPlugIn pasteSchemaPlugIn = new PasteSchemaPlugIn();
    private FeatureInfoPlugIn featureInfoPlugIn = new FeatureInfoPlugIn();
    private OutputWindowPlugIn outputWindowPlugIn = new OutputWindowPlugIn();
    private VerticesInFencePlugIn verticesInFencePlugIn = new VerticesInFencePlugIn();
    private ZoomNextPlugIn zoomNextPlugIn = new ZoomNextPlugIn();
    private ZoomToClickPlugIn zoomToClickPlugIn = new ZoomToClickPlugIn(0.5);
    private ZoomPreviousPlugIn zoomPreviousPlugIn = new ZoomPreviousPlugIn();
    private ZoomToFencePlugIn zoomToFencePlugIn = new ZoomToFencePlugIn();
    private ZoomToCoordinatePlugIn zoomToCoordinatePlugIn = new ZoomToCoordinatePlugIn();
    private ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    private ZoomToLayerPlugIn zoomToLayerPlugIn = new ZoomToLayerPlugIn();
    private ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn = new ZoomToSelectedItemsPlugIn();
    private CutSelectedItemsPlugIn cutSelectedItemsPlugIn = new CutSelectedItemsPlugIn();
    private CutSelectedLayersPlugIn cutSelectedLayersPlugIn = new CutSelectedLayersPlugIn();
    private CopyStylesPlugIn copyStylesPlugIn = new CopyStylesPlugIn();
    private PasteStylesPlugIn pasteStylesPlugIn = new PasteStylesPlugIn();
    private CombineSelectedFeaturesPlugIn combineSelectedFeaturesPlugIn = new CombineSelectedFeaturesPlugIn();
//  private MergeSelectedFeaturesPlugIn mergeSelectedFeaturesPlugIn = new MergeSelectedFeaturesPlugIn();
    private ExplodeSelectedFeaturesPlugIn explodeSelectedFeaturesPlugIn = new ExplodeSelectedFeaturesPlugIn();
    // superseded by org/openjump/OpenJumpConfiguration.java
    //private InstallReferencedImageFactoriesPlugin installReferencedImageFactoriesPlugin = new InstallReferencedImageFactoriesPlugin();
    private ImageLayerManagerPlugIn imageLayerManagerPlugIn = new ImageLayerManagerPlugIn();
    private RefreshDataStoreLayerPlugin refreshDataStoreLayerPlugin = new RefreshDataStoreLayerPlugin();
    private DuplicateItemPlugIn duplicateItemPlugIn = new DuplicateItemPlugIn();

    @Override
    public void setup(WorkbenchContext workbenchContext) throws Exception {
        configureStyles(workbenchContext);
        configureDatastores(workbenchContext);
        workbenchContext.getWorkbench().getBlackboard().put(SnapToVerticesPolicy.ENABLED_KEY, true);
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
        configureToolBar(workbenchContext, checkFactory); //Method that build the toolbar for using the map
        configureMainMenus(workbenchContext, checkFactory, featureInstaller); //Application tool menu (File, Edit, etc.)
        configureLayerPopupMenu(workbenchContext, featureInstaller, checkFactory); //Method to view popupmenu of a layer
        configureAttributePopupMenu(workbenchContext, featureInstaller, checkFactory); //Method that manages the layer attributes table
        configureWMSQueryNamePopupMenu(workbenchContext, featureInstaller, checkFactory);
        configureCategoryPopupMenu(workbenchContext, featureInstaller); //Method to view the popupmenu when mouse right-click on a category
        configureLayerViewPanelPopupMenu(workbenchContext, checkFactory, featureInstaller);//Method to view the popupmenu when mouse right-click on map
        initializeRenderingManager();
        /**
         * ******************************************
         * [sstein] 11.08.2005 the following line calls the new OpenJump plugins
         * *****************************************
         */
        OpenJumpConfiguration.loadOpenJumpPlugIns(workbenchContext);
        //Call #initializeBuiltInPlugIns after #configureToolBar so that any
        // plug-ins that
        //add items to the toolbar will add them to the *end* of the toolbar.
        initializeBuiltInPlugIns(workbenchContext);
    }

    private void initializeRenderingManager() {
        RenderingManager.setRendererFactory(Layer.class, new LayerRendererFactory());
        RenderingManager.setRendererFactory(WMSLayer.class, new WmsLayerRendererFactory());
    }

    //Method to view the popupmenu when mouse right-click on a category (Working or System)
    private void configureCategoryPopupMenu(WorkbenchContext workbenchContext,
            FeatureInstaller featureInstaller) {
        featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
                .getFrame().getCategoryPopupMenu(), addNewLayerPlugIn,
                addNewLayerPlugIn.getName(), false, IconLoader.icon("layers.png"), null);

        //[sstein 20.01.2006] added again after user request
        featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
                .getFrame().getCategoryPopupMenu(), loadDatasetPlugIn,
                loadDatasetPlugIn.getName() + "...", false, LoadDatasetPlugIn.getIcon(),
                LoadDatasetPlugIn.createEnableCheck(workbenchContext));
        //--
        /*featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
         .getFrame().getCategoryPopupMenu(), loadDatasetFromFilePlugIn,
         loadDatasetFromFilePlugIn.getName() + "...", false, null,
         LoadDatasetPlugIn.createEnableCheck(workbenchContext));*/

        //[sstein 21.Mar.2008] removed since now contained in new open menu
        //[sstein 2.June.2008] added back due to table list history (need to check other way?)
        featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
                .getFrame().getCategoryPopupMenu(), addDatastoreLayerPlugIn,
                new StringBuilder(addDatastoreLayerPlugIn.getName()).append("...").toString(),
                false, AddDatastoreLayerPlugIn.ICON, null);

        /* //[sstein 21.Mar.2008] removed since now contained in new open menu
         featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
         .getFrame().getCategoryPopupMenu(), addWMSQueryPlugIn,
         addWMSQueryPlugIn.getName() + "...", false, null, null);

         featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
         .getFrame().getCategoryPopupMenu(), addImageLayerPlugIn,
         addImageLayerPlugIn.getName() + "...", false, null, null);
         */
        featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
                .getFrame().getCategoryPopupMenu(), pasteLayersPlugIn,
                pasteLayersPlugIn.getNameWithMnemonic(), false, null,
                pasteLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(workbenchContext.getWorkbench()
                .getFrame().getCategoryPopupMenu(),
                removeSelectedCategoriesPlugIn, removeSelectedCategoriesPlugIn
                .getName(), false, null, removeSelectedCategoriesPlugIn
                .createEnableCheck(workbenchContext));
    }

    private void configureWMSQueryNamePopupMenu(
            final WorkbenchContext workbenchContext,
            FeatureInstaller featureInstaller, EnableCheckFactory checkFactory) {
        JPopupMenu wmsLayerNamePopupMenu = workbenchContext.getWorkbench()
                .getFrame().getWMSLayerNamePopupMenu();
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
                editWMSQueryPlugIn, new StringBuilder(editWMSQueryPlugIn.getName()).append("...").toString(),
                false, null, editWMSQueryPlugIn
                .createEnableCheck(workbenchContext));
        wmsLayerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu, moveUpPlugIn,
                moveUpPlugIn.getName(), false, MoveLayerablePlugIn.UPICON, moveUpPlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
                moveDownPlugIn, moveDownPlugIn.getName(), false, MoveLayerablePlugIn.DOWNICON,
                moveDownPlugIn.createEnableCheck(workbenchContext));
        wmsLayerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
                cutSelectedLayersPlugIn, cutSelectedLayersPlugIn
                .getNameWithMnemonic(), false, CutSelectedLayersPlugIn.ICON,
                cutSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
                copySelectedLayersPlugIn, copySelectedLayersPlugIn
                .getNameWithMnemonic(), false, CopySelectedLayersPlugIn.ICON,
                copySelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
                removeSelectedLayersPlugIn, removeSelectedLayersPlugIn
                .getName(), false, RemoveSelectedLayersPlugIn.ICON, removeSelectedLayersPlugIn
                .createEnableCheck(workbenchContext));
    }

    //Method that manages the layer attributes table
    private void configureAttributePopupMenu(
            final WorkbenchContext workbenchContext,
            FeatureInstaller featureInstaller, EnableCheckFactory checkFactory) {
        AttributeTab.addPopupMenuItem(workbenchContext, editablePlugIn,
                editablePlugIn.getName(), true, EditablePlugIn.ICON, editablePlugIn
                .createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext, viewSchemaPlugIn,
                viewSchemaPlugIn.getName(), false, ViewSchemaPlugIn.ICON,
                ViewSchemaPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext, featureInfoPlugIn,
                featureInfoPlugIn.getName(), false, GUIUtil
                .toSmallIcon(FeatureInfoTool.ICON),
                FeatureInfoPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext, cutSelectedItemsPlugIn,
                cutSelectedItemsPlugIn.getName(), false, CutSelectedItemsPlugIn.ICON,
                cutSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext,
                copySelectedItemsPlugIn, copySelectedItemsPlugIn
                .getNameWithMnemonic(), false, CopySelectedItemsPlugIn.ICON,
                CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext,
                deleteSelectedItemsPlugIn, deleteSelectedItemsPlugIn.getName(),
                false, DeleteSelectedItemsPlugIn.ICON,
                DeleteSelectedItemsPlugIn.createEnableCheck(workbenchContext));
    }

    //Method to view popupmenu of a layer
    private void configureLayerPopupMenu(
            final WorkbenchContext workbenchContext,
            FeatureInstaller featureInstaller, EnableCheckFactory checkFactory) {

        JPopupMenu layerNamePopupMenu = workbenchContext.getWorkbench()
                .getFrame().getLayerNamePopupMenu();

        featureInstaller.addPopupMenuItem(layerNamePopupMenu, editablePlugIn,
                editablePlugIn.getName(), true, EditablePlugIn.ICON, editablePlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, selectablePlugIn,
                selectablePlugIn.getName(), true, SelectablePlugIn.ICON, selectablePlugIn
                .createEnableCheck(workbenchContext));
        layerNamePopupMenu.addSeparator(); // ===================

        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                removeSelectedLayersPlugIn, removeSelectedLayersPlugIn
                .getName(), false, RemoveSelectedLayersPlugIn.ICON, removeSelectedLayersPlugIn
                .createEnableCheck(workbenchContext));

        layerNamePopupMenu.addSeparator(); // ===================

        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                zoomToLayerPlugIn, zoomToLayerPlugIn.getName(), false, ZoomToLayerPlugIn.ICON,
                zoomToLayerPlugIn.createEnableCheck(workbenchContext));

        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                viewAttributesPlugIn, viewAttributesPlugIn.getName(), false,
                GUIUtil.toSmallIcon(viewAttributesPlugIn.getIcon()),
                viewAttributesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                viewSchemaPlugIn, new String[]{MenuNames.SCHEMA}, viewSchemaPlugIn.getName() + "...",
                false, ViewSchemaPlugIn.ICON, ViewSchemaPlugIn.createEnableCheck(workbenchContext));
        FeatureInstaller.childMenuItem(MenuNames.SCHEMA, layerNamePopupMenu).setIcon(ViewSchemaPlugIn.ICON);

        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                changeStylesPlugIn, new String[]{MenuNames.STYLE}, changeStylesPlugIn.getName() + "...",
                false, GUIUtil.toSmallIcon(changeStylesPlugIn.getIcon()),
                changeStylesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, copyStylesPlugIn,
                new String[]{MenuNames.STYLE}, copyStylesPlugIn.getName(),
                false, GUIUtil.toSmallIcon(copyStylesPlugIn.getIcon()),
                CopyStylesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, pasteStylesPlugIn,
                new String[]{MenuNames.STYLE}, pasteStylesPlugIn.getName(),
                false, GUIUtil.toSmallIcon(pasteStylesPlugIn.getIcon()),
                PasteStylesPlugIn.createEnableCheck(workbenchContext));
        FeatureInstaller.childMenuItem(MenuNames.STYLE, layerNamePopupMenu)
                .setIcon(GUIUtil.toSmallIcon(pasteStylesPlugIn.getIcon()));

        featureInstaller.addPopupMenuItem(layerNamePopupMenu, refreshDataStoreLayerPlugin,
                new String[]{MenuNames.DATASTORE}, refreshDataStoreLayerPlugin.getName(), false, RefreshDataStoreLayerPlugin.ICON,
                RefreshDataStoreLayerPlugin.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, imageLayerManagerPlugIn,
                imageLayerManagerPlugIn.getName() + "...", false, null,
                ImageLayerManagerPlugIn.createEnableCheck(workbenchContext));
        FeatureInstaller.childMenuItem(MenuNames.DATASTORE, layerNamePopupMenu).setIcon(IconLoader.icon("database_gear.png"));

        layerNamePopupMenu.addSeparator(); // ===================

        //[sstein 18.Oct.2011] re-added the saveDatasetAsFilePlugIn and the MacOSX check
        //        since under MacOSX the SaveDatasetAsPlugIn is missing a text field to
        //        write the name of the new file. Hence, the dialog could only be used
        //        for saving to an existing dataset.
        //[sstein 7.Nov.2011] not needed anymore as the bug 3428076 could be fixed
        /*
         if(CheckOS.isMacOsx()){
         featureInstaller.addPopupMenuItem(layerNamePopupMenu,
         saveDatasetAsFilePlugIn, saveDatasetAsFilePlugIn.getName() + "...",
         false, SaveDatasetAsPlugIn.ICON, AbstractSaveDatasetAsPlugIn
         .createEnableCheck(workbenchContext));
         }
         */
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                saveDatasetAsPlugIn, new StringBuilder(saveDatasetAsPlugIn.getName()).append("...").toString(),
                false, SaveDatasetAsPlugIn.ICON, AbstractSaveDatasetAsPlugIn
                .createEnableCheck(workbenchContext));

        layerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, moveUpPlugIn,
                moveUpPlugIn.getName(), false, MoveLayerablePlugIn.UPICON, moveUpPlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, moveDownPlugIn,
                moveDownPlugIn.getName(), false, MoveLayerablePlugIn.DOWNICON, moveDownPlugIn
                .createEnableCheck(workbenchContext));

        layerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                cutSelectedLayersPlugIn, cutSelectedLayersPlugIn
                .getNameWithMnemonic(), false, CutSelectedLayersPlugIn.ICON,
                cutSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                copySelectedLayersPlugIn, copySelectedLayersPlugIn
                .getNameWithMnemonic(), false, CopySelectedLayersPlugIn.ICON,
                copySelectedLayersPlugIn.createEnableCheck(workbenchContext));

        layerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                addNewFeaturesPlugIn, addNewFeaturesPlugIn.getName() + "...",
                false, AddNewFeaturesPlugIn.ICON, AddNewFeaturesPlugIn
                .createEnableCheck(workbenchContext));

        //<<TODO:REFACTORING>> JUMPConfiguration is polluted with a lot of
        // EnableCheck
        //logic. This logic should simply be moved to the individual PlugIns.
        // [Bob Boseko]
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, pasteItemsPlugIn,
                pasteItemsPlugIn.getNameWithMnemonic(), false, pasteItemsPlugIn.getIcon(),
                PasteItemsPlugIn.createEnableCheck(workbenchContext));

        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                deleteAllFeaturesPlugIn, deleteAllFeaturesPlugIn.getName(),
                false, DeleteAllFeaturesPlugIn.ICON, deleteAllFeaturesPlugIn
                .createEnableCheck(workbenchContext));
    }

    //Method to view the popupmenu when mouse right-click on map
    private void configureLayerViewPanelPopupMenu(
            WorkbenchContext workbenchContext, EnableCheckFactory checkFactory,
            FeatureInstaller featureInstaller) {
        JPopupMenu popupMenu = LayerViewPanel.popupMenu();
        featureInstaller.addPopupMenuItem(popupMenu, featureInfoPlugIn,
                featureInfoPlugIn.getName(), false, GUIUtil
                .toSmallIcon(FeatureInfoTool.ICON), FeatureInfoPlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu,
                verticesInFencePlugIn,
                verticesInFencePlugIn.getName(),
                false,
                null,
                new MultiEnableCheck()
                .add(
                        checkFactory
                        .createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createFenceMustBeDrawnCheck()));
        popupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(popupMenu, zoomToFencePlugIn,
                new String[]{I18N.get("ui.MenuNames.ZOOM")},
                I18N.get("JUMPConfiguration.fence"), false,
                GUIUtil.toSmallIcon(zoomToFencePlugIn.getIcon()),
                new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createFenceMustBeDrawnCheck()));
        featureInstaller.addPopupMenuItem(popupMenu, zoomToSelectedItemsPlugIn,
                new String[]{I18N.get("ui.MenuNames.ZOOM")},
                zoomToSelectedItemsPlugIn.getName(), false,
                GUIUtil.toSmallIcon(zoomToSelectedItemsPlugIn.getIcon()),
                ZoomToSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        //featureInstaller.addPopupMenuItem(popupMenu, zoomToClickPlugIn,
        //        I18N.get("JUMPConfiguration.zoom-out"), false, null, null);

        popupMenu.addSeparator(); // ===================
        //[sstein] 23Mar2009 -- remove from layer view context menu to get space but is still to be found in >edit>selection>
        featureInstaller.addPopupMenuItem(popupMenu,
                selectFeaturesInFencePlugIn, selectFeaturesInFencePlugIn
                .getName(), false, null, SelectFeaturesInFencePlugIn.createEnableCheck(workbenchContext));

        featureInstaller.addPopupMenuItem(popupMenu, cutSelectedItemsPlugIn,
                cutSelectedItemsPlugIn.getName(), false, CutSelectedItemsPlugIn.ICON,
                cutSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, copySelectedItemsPlugIn,
                copySelectedItemsPlugIn.getNameWithMnemonic(), false, copySelectedItemsPlugIn.getIcon(),
                CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, editSelectedFeaturePlugIn,
                editSelectedFeaturePlugIn.getName(), false, editSelectedFeaturePlugIn.getIcon(),
                EditSelectedFeaturePlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, deleteSelectedItemsPlugIn,
                deleteSelectedItemsPlugIn.getName(), false, deleteSelectedItemsPlugIn.getIcon(),
                DeleteSelectedItemsPlugIn.createEnableCheck(workbenchContext));

        featureInstaller.addPopupMenuItem(popupMenu, duplicateItemPlugIn,
                duplicateItemPlugIn.getName(), false, duplicateItemPlugIn.getIcon(),
                DuplicateItemPlugIn.createEnableCheck(workbenchContext));

        featureInstaller.addPopupMenuItem(popupMenu,
                combineSelectedFeaturesPlugIn, combineSelectedFeaturesPlugIn
                .getName(), false, combineSelectedFeaturesPlugIn.getIcon(),
                combineSelectedFeaturesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu,
                explodeSelectedFeaturesPlugIn, explodeSelectedFeaturesPlugIn
                .getName(), false, explodeSelectedFeaturesPlugIn.getIcon(),
                explodeSelectedFeaturesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, copyThisCoordinatePlugIn,
                copyThisCoordinatePlugIn.getName(), false, null,
                CopyThisCoordinatePlugIn.createEnableCheck(workbenchContext));
    }

    //Method used to manage the application menus
    private void configureMainMenus(final WorkbenchContext workbenchContext,
            final EnableCheckFactory checkFactory, FeatureInstaller featureInstaller) throws Exception {
        /**
         * FILE ===================================================================
         */
        FeatureInstaller.addMainMenu(featureInstaller, new String[]{MenuNames.FILE},
                MenuNames.FILE_NEW, 0);
        featureInstaller.addMainMenuItemWithJava14Fix(newTaskPlugIn, new String[]{
            MenuNames.FILE, MenuNames.FILE_NEW},
                newTaskPlugIn.getName(), false, NewTaskPlugIn.getIcon2(), null); //New Project
        featureInstaller.addMenuSeparator(new String[]{MenuNames.FILE, MenuNames.FILE_NEW}); // ===================
        featureInstaller.addMainMenuItem(addNewLayerPlugIn,
                new String[]{MenuNames.FILE, MenuNames.FILE_NEW},
                new JMenuItem(I18N.get("com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn.name"), IconLoader.icon("layers.png")),
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()); //New Layer
        featureInstaller.addMainMenuItem(addNewCategoryPlugIn,
                new String[]{MenuNames.FILE, MenuNames.FILE_NEW},
                new JMenuItem(I18N.get("com.osfac.dmt.workbench.ui.plugin.AddNewCategoryPlugIn.name"), IconLoader.icon("chart_organisation.png")),
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()); //New Category

        featureInstaller.addMenuSeparator(MenuNames.FILE); // ===================

        featureInstaller.addMainMenuItemWithJava14Fix(saveDatasetAsPlugIn, new String[]{MenuNames.FILE},
                saveDatasetAsPlugIn.getName() + "...", false, SaveDatasetAsPlugIn.ICON,
                SaveDatasetAsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(saveProjectPlugIn, new String[]{MenuNames.FILE},
                saveProjectPlugIn.getName(), false, SaveProjectPlugIn.ICON, checkFactory
                .createTaskWindowMustBeActiveCheck());
        featureInstaller.addMainMenuItemWithJava14Fix(saveProjectAsPlugIn, new String[]{MenuNames.FILE},
                saveProjectAsPlugIn.getName() + "...", false, SaveProjectAsPlugIn.ICON, checkFactory
                .createTaskWindowMustBeActiveCheck());
        FeatureInstaller.addMainMenu(featureInstaller, new String[]{MenuNames.FILE},
                MenuNames.FILE_SAVEVIEW, 5);
        featureInstaller.addMainMenuItemWithJava14Fix(
                saveImageAsPlugIn,
                new String[]{MenuNames.FILE, MenuNames.FILE_SAVEVIEW},
                new StringBuilder(saveImageAsPlugIn.getName()).append("...").toString(),
                false,
                null,
                SaveImageAsPlugIn.createEnableCheck(workbenchContext));

        /**
         * EDIT ===================================================================================
         */
        featureInstaller.addMainMenuItemWithJava14Fix(undoPlugIn, new String[]{MenuNames.EDIT}, undoPlugIn
                .getName(), false, GUIUtil.toSmallIcon(undoPlugIn.getIcon()),
                undoPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(redoPlugIn, new String[]{MenuNames.EDIT}, redoPlugIn
                .getName(), false, GUIUtil.toSmallIcon(redoPlugIn.getIcon()),
                redoPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator(MenuNames.EDIT); // ===================
        featureInstaller.addMainMenuItemWithJava14Fix(addNewFeaturesPlugIn, new String[]{MenuNames.EDIT},
                addNewFeaturesPlugIn.getName() + "...", false, AddNewFeaturesPlugIn.ICON,
                AddNewFeaturesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(editSelectedFeaturePlugIn, new String[]{MenuNames.EDIT},
                editSelectedFeaturePlugIn.getName(), false, editSelectedFeaturePlugIn.getIcon(),
                EditSelectedFeaturePlugIn.createEnableCheck(workbenchContext));

        FeatureInstaller.addMainMenu(featureInstaller, new String[]{MenuNames.EDIT},
                MenuNames.SELECTION, 6);
        featureInstaller.addMainMenuItemWithJava14Fix(selectFeaturesInFencePlugIn,
                new String[]{MenuNames.EDIT, MenuNames.SELECTION},
                selectFeaturesInFencePlugIn.getName(), false, null,
                SelectFeaturesInFencePlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(clearSelectionPlugIn, new String[]{MenuNames.EDIT},
                clearSelectionPlugIn.getName(), false, null, clearSelectionPlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator(MenuNames.EDIT); // ===================
        featureInstaller.addMainMenuItemWithJava14Fix(cutSelectedItemsPlugIn, new String[]{MenuNames.EDIT},
                cutSelectedItemsPlugIn.getName(), false, CutSelectedItemsPlugIn.ICON, cutSelectedItemsPlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(copySelectedItemsPlugIn, new String[]{MenuNames.EDIT},
                new JMenuItem(copySelectedItemsPlugIn.getNameWithMnemonic(), CopySelectedItemsPlugIn.ICON),
                CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(pasteItemsPlugIn, new String[]{MenuNames.EDIT},
                new JMenuItem(pasteItemsPlugIn.getNameWithMnemonic(), PasteItemsPlugIn.ICON),
                PasteItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator(MenuNames.EDIT); // ===================
        featureInstaller.addMainMenuItemWithJava14Fix(deleteSelectedItemsPlugIn, new String[]{MenuNames.EDIT},
                deleteSelectedItemsPlugIn.getName(), false, DeleteSelectedItemsPlugIn.ICON,
                DeleteSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        //featureInstaller.addMenuSeparator(MenuNames.EDIT); // ===================
        /*//--[sstein 24 march 2007] moved to new customize menu
         featureInstaller.addMainMenuItemWithJava14Fix(optionsPlugIn, new String[] {MenuNames.EDIT}, optionsPlugIn
         .getName()
         + "...", false, null, null);
         */

        /**
         * VIEW =================================================================================
         */
        editingPlugIn.createMainMenuItem(new String[]{MenuNames.VIEW}, GUIUtil
                .toSmallIcon(EditingPlugIn.ICON), workbenchContext);
        featureInstaller.addMainMenuItemWithJava14Fix(copyImagePlugIn, new String[]{MenuNames.FILE},
                copyImagePlugIn.getName(), false, CopyImagePlugIn.ICON, CopyImagePlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator(MenuNames.VIEW); // ===================
        featureInstaller.addMainMenuItemWithJava14Fix(featureInfoPlugIn, new String[]{MenuNames.VIEW},
                featureInfoPlugIn.getName(), false, GUIUtil
                .toSmallIcon(FeatureInfoTool.ICON), FeatureInfoPlugIn
                .createEnableCheck(workbenchContext));
        featureInstaller
                .addMainMenuItemWithJava14Fix(
                        verticesInFencePlugIn,
                        new String[]{MenuNames.VIEW},
                        verticesInFencePlugIn.getName(),
                        false,
                        null,
                        new MultiEnableCheck()
                        .add(checkFactory
                                .createWindowWithLayerViewPanelMustBeActiveCheck())
                        .add(checkFactory.createFenceMustBeDrawnCheck()));
        featureInstaller.addMenuSeparator(MenuNames.VIEW); // ===================
        featureInstaller.addMainMenuItemWithJava14Fix(zoomToFullExtentPlugIn, new String[]{MenuNames.VIEW},
                zoomToFullExtentPlugIn.getName(), false, GUIUtil
                .toSmallIcon(zoomToFullExtentPlugIn.getIcon()),
                zoomToFullExtentPlugIn.createEnableCheck(workbenchContext));
        featureInstaller
                .addMainMenuItemWithJava14Fix(
                        zoomToFencePlugIn,
                        new String[]{MenuNames.VIEW},
                        zoomToFencePlugIn.getName(),
                        false,
                        GUIUtil.toSmallIcon(zoomToFencePlugIn.getIcon()),
                        new MultiEnableCheck()
                        .add(checkFactory
                                .createWindowWithLayerViewPanelMustBeActiveCheck())
                        .add(checkFactory.createFenceMustBeDrawnCheck()));
        featureInstaller.addMainMenuItemWithJava14Fix(zoomToSelectedItemsPlugIn, new String[]{MenuNames.VIEW},
                zoomToSelectedItemsPlugIn.getName(), false, GUIUtil
                .toSmallIcon(zoomToSelectedItemsPlugIn.getIcon()),
                ZoomToSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(zoomToCoordinatePlugIn, new String[]{MenuNames.VIEW},
                new StringBuilder(zoomToCoordinatePlugIn.getName()).append("...").toString(), false, null,
                zoomToCoordinatePlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(zoomPreviousPlugIn, new String[]{MenuNames.VIEW},
                zoomPreviousPlugIn.getName(), false, GUIUtil
                .toSmallIcon(zoomPreviousPlugIn.getIcon()),
                zoomPreviousPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItemWithJava14Fix(zoomNextPlugIn, new String[]{MenuNames.VIEW}, zoomNextPlugIn
                .getName(), false, GUIUtil.toSmallIcon(zoomNextPlugIn.getIcon()),
                zoomNextPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator(MenuNames.VIEW); // ===================
        featureInstaller
                .addMainMenuItemWithJava14Fix(
                        showScalePlugIn,
                        new String[]{MenuNames.VIEW},
                        showScalePlugIn.getName(),
                        true,
                        null,
                        new MultiEnableCheck()
                        .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                        .add(new EnableCheck() {
                            @Override
                            public String check(JComponent component) {
                                ((JCheckBoxMenuItem) component)
                                .setSelected(ShowScaleRenderer
                                        .isEnabled(workbenchContext
                                                .getLayerViewPanel()));
                                return null;
                            }
                        }));
        featureInstaller
                .addMainMenuItemWithJava14Fix(
                        scaleBarPlugIn,
                        new String[]{MenuNames.VIEW},
                        scaleBarPlugIn.getName(),
                        true,
                        null,
                        new MultiEnableCheck()
                        .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                        .add(new EnableCheck() {
                            @Override
                            public String check(JComponent component) {
                                ((JCheckBoxMenuItem) component)
                                .setSelected(ScaleBarRenderer
                                        .isEnabled(workbenchContext
                                                .getLayerViewPanel()));
                                return null;
                            }
                        }));
        featureInstaller.addMainMenuItemWithJava14Fix(toolTipsPlugIn,
                new String[]{MenuNames.VIEW}, toolTipsPlugIn.getName(), true, null,
                MapToolTipsPlugIn.createEnableCheck(workbenchContext));
        zoomBarPlugIn.createMainMenuItem(new String[]{MenuNames.VIEW}, null, workbenchContext);

        /**
         * LAYER ===============================================================================
         */
        //-- [sstein: 23.02.2006 new sub method in VividJump]
        configLayer(workbenchContext, checkFactory, featureInstaller);

        /**
         * WINDOW ==============================================================================
         */
        /*
         featureInstaller.addMainMenuItemWithJava14Fix(optionsPlugIn, new String[] {MenuNames.WINDOW}, optionsPlugIn
         .getName() + "...", false, null, null);
         */
        featureInstaller.addMainMenuItemWithJava14Fix(outputWindowPlugIn,
                new String[]{MenuNames.WINDOW},
                outputWindowPlugIn.getName(), false,
                GUIUtil.toSmallIcon(outputWindowPlugIn.getIcon()), null);
        featureInstaller.addMainMenuItemWithJava14Fix(generateLogPlugIn,
                new String[]{MenuNames.WINDOW},
                generateLogPlugIn.getName(), false,
                GUIUtil.toSmallIcon(generateLogPlugIn.getIcon()), null);
        featureInstaller.addMenuSeparator(MenuNames.WINDOW); // ===================

        featureInstaller.addMainMenuItemWithJava14Fix(cloneWindowPlugIn, new String[]{MenuNames.WINDOW},
                cloneWindowPlugIn.getName(), false, null, new EnableCheck() {
                    @Override
                    public String check(JComponent component) {
                        return (!(workbenchContext.getWorkbench().getFrame()
                        .getActiveInternalFrame() instanceof CloneableInternalFrame)) ? I18N.get("JUMPConfiguration.not-available-for-the-current-window")
                                : null;
                    }
                });
        //featureInstaller.addMenuSeparator(MenuNames.WINDOW); // ===================

        /**
         * TOOLS ==============================================================================
         */
        /* [sstein 13.Aug.2008] -- initialization now in default-plugins.xml
         configToolsAnalysis(workbenchContext, checkFactory, featureInstaller);
         configToolsEdit(workbenchContext, checkFactory, featureInstaller);
         configToolsQA(workbenchContext, checkFactory, featureInstaller);
         configToolsAttributes(workbenchContext, checkFactory, featureInstaller);
         */
        /**
         * HELP ===========================================================================
         */
        featureInstaller.addMainMenuItemWithJava14Fix(shortcutKeysPlugIn, new String[]{MenuNames.HELP},
                new StringBuilder(shortcutKeysPlugIn.getName()).append("...").toString(), false, ShortcutKeysPlugIn.ICON, null);
//        new FeatureInstaller(workbenchContext).addMainMenuItemWithJava14Fix(
//                new AboutPlugIn(), new String[]{MenuNames.HELP}, I18N.get("JUMPConfiguration.about"), false, AboutPlugIn.ICON, null);

        /**
         * CUSTOMIZE ===========================================================================
         */
        //-- [sstein: 24.03.2007 new menu]
        featureInstaller.addMainMenuItemWithJava14Fix(optionsPlugIn, new String[]{MenuNames.CUSTOMIZE}, new StringBuilder(optionsPlugIn
                .getName()).append("...").toString(), false, null, null);
    }

    //==== [sstein: 23.02.2006] ======
    // this is a new method in VividJump (December 2005) to initialize
    // plugins working on layers
    //================================
    //public static String MENU_LAYER = MenuNames.LAYER;
    private AddDatastoreLayerPlugIn addDatastoreLayerPlugIn = new AddDatastoreLayerPlugIn();
    //private RunDatastoreQueryPlugIn runDatastoreQueryPlugIn = new RunDatastoreQueryPlugIn();
    private InstallDatastoreLayerRendererHintsPlugIn installDatastoreLayerRendererHintsPlugIn = new InstallDatastoreLayerRendererHintsPlugIn();
    //private AddImageLayerPlugIn addImageLayerPlugIn = new AddImageLayerPlugIn();

    //Method to configue the Layer menu
    private void configLayer(final WorkbenchContext workbenchContext,
            final EnableCheckFactory checkFactory, FeatureInstaller featureInstaller) throws Exception {

        String MENU_LAYER = MenuNames.LAYER;
        //--[sstein 21Mar2008] -- disabled because of new menu structure by Paul
       /*
         featureInstaller.addLayerViewMenuItem(addNewLayerPlugIn, MENU_LAYER,
         addNewLayerPlugIn.getName());
         featureInstaller.addLayerViewMenuItem(addDatastoreLayerPlugIn, MENU_LAYER,
         addDatastoreLayerPlugIn.getName() + "...");

         featureInstaller.addLayerViewMenuItem(runDatastoreQueryPlugIn, MENU_LAYER,
         runDatastoreQueryPlugIn.getName() + "...");
         */
        //--[sstein 21Mar2008] -- disabled because of new menu structure by Paul
       /*
         featureInstaller.addLayerViewMenuItem(addWMSQueryPlugIn, MENU_LAYER,
         addWMSQueryPlugIn.getName() + "...");

         featureInstaller.addLayerViewMenuItem(addImageLayerPlugIn, MENU_LAYER,
         addImageLayerPlugIn.getName() + "...");
         */
        /*
         featureInstaller.addMainMenuItem(addNewCategoryPlugIn, MENU_LAYER,
         addNewCategoryPlugIn.getName(), null, addNewCategoryPlugIn
         .createEnableCheck(workbenchContext));
         */

        //featureInstaller.addMenuSeparator(MENU_LAYER); // ===================
        featureInstaller.addMainMenuItem(cutSelectedLayersPlugIn, new String[]{MENU_LAYER},
                new JMenuItem(cutSelectedLayersPlugIn.getNameWithMnemonic(), CutSelectedLayersPlugIn.ICON),
                cutSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(copySelectedLayersPlugIn, new String[]{MENU_LAYER},
                new JMenuItem(copySelectedLayersPlugIn.getNameWithMnemonic(), CopySelectedLayersPlugIn.ICON),
                copySelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(pasteLayersPlugIn, new String[]{MENU_LAYER},
                new JMenuItem(pasteLayersPlugIn.getNameWithMnemonic()),
                pasteLayersPlugIn.createEnableCheck(workbenchContext));

        featureInstaller.addMenuSeparator(MENU_LAYER); // ===================
        featureInstaller.addMainMenuItem(removeSelectedLayersPlugIn, new String[]{MENU_LAYER},
                new JMenuItem(removeSelectedLayersPlugIn.getName(), RemoveSelectedLayersPlugIn.ICON),
                removeSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(removeSelectedCategoriesPlugIn, new String[]{MENU_LAYER},
                new JMenuItem(removeSelectedCategoriesPlugIn.getName()),
                removeSelectedCategoriesPlugIn.createEnableCheck(workbenchContext));
    }

    public void configureDatastores(final WorkbenchContext context) throws Exception {
        context.getRegistry().createEntry(DataStoreDriver.REGISTRY_CLASSIFICATION, new PostgisDataStoreDriver());
        // update exit handler
        final ApplicationExitHandler oldApplicationExitHandler = context.getWorkbench().getFrame().getApplicationExitHandler();
        context.getWorkbench().getFrame().setApplicationExitHandler(new ApplicationExitHandler() {
            @Override
            public void exitApplication(JFrame mainFrame) {
                try {
                    ConnectionManager.instance(context).closeConnections();
                } catch (DataStoreException e) {
                    throw new RuntimeException(e);
                }
                oldApplicationExitHandler.exitApplication(mainFrame);
            }
        });
    }

    private void configureStyles(WorkbenchContext workbenchContext) {
        WorkbenchFrame frame = workbenchContext.getWorkbench().getFrame();
        frame.addChoosableStyleClass(VertexXYLineSegmentStyle.VertexXY.class);
        frame.addChoosableStyleClass(VertexIndexLineSegmentStyle.VertexIndex.class);
        frame.addChoosableStyleClass(MetricsLineStringSegmentStyle.LengthAngle.class);
        frame.addChoosableStyleClass(ArrowLineStringSegmentStyle.Open.class);
        frame.addChoosableStyleClass(ArrowLineStringSegmentStyle.Solid.class);
        frame.addChoosableStyleClass(ArrowLineStringSegmentStyle.NarrowSolid.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.FeathersStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.FeathersEnd.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.OpenStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.OpenEnd.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.SolidStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.SolidEnd.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.NarrowSolidStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.NarrowSolidEnd.class);
        frame.addChoosableStyleClass(CircleLineStringEndpointStyle.Start.class);
        frame.addChoosableStyleClass(CircleLineStringEndpointStyle.End.class);
    }

    //Method build for the select feature tool of the toolbar map
    private QuasimodeTool add(CursorTool tool, WorkbenchContext context) {
        return context.getWorkbench().getFrame().getToolBar().addCursorTool(tool).getQuasimodeTool();
    }

    //Method that build the toolbar for using the map
    private void configureToolBar(final WorkbenchContext workbenchContext, EnableCheckFactory checkFactory) {
        WorkbenchFrame frame = workbenchContext.getWorkbench().getFrame();
        frame.getToolBar().addPlugIn(NewTaskPlugIn.getIcon(),
                newTaskPlugIn,
                NewTaskPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
//        frame.getToolBar().addSeparator();
        add(new ZoomTool(), workbenchContext);
        add(new PanTool(), workbenchContext);
        // Test for the new Zoom/Pan tool, comment the following line out, if it makes problems
        // add(new SuperZoomPanTool(), workbenchContext);
        frame.getToolBar().addPlugIn(zoomToFullExtentPlugIn.getIcon(),
                zoomToFullExtentPlugIn,
                zoomToFullExtentPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addSeparator();
        frame.getToolBar().addPlugIn(zoomToSelectedItemsPlugIn.getIcon(),
                zoomToSelectedItemsPlugIn,
                ZoomToSelectedItemsPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        add(new ZoomRealtimeTool(), workbenchContext);  //TODO: move to OpenJumpConfiguration if possible
        frame.getToolBar()
                .addPlugIn(
                        zoomToFencePlugIn.getIcon(),
                        zoomToFencePlugIn,
                        new MultiEnableCheck()
                        .add(
                                checkFactory
                                .createWindowWithLayerViewPanelMustBeActiveCheck())
                        .add(checkFactory.createFenceMustBeDrawnCheck()),
                        workbenchContext);
        frame.getToolBar().addSeparator();
        frame.getToolBar().addPlugIn(zoomPreviousPlugIn.getIcon(),
                zoomPreviousPlugIn,
                zoomPreviousPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addPlugIn(zoomNextPlugIn.getIcon(), zoomNextPlugIn,
                zoomNextPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addSeparator();
        //Null out the quasimodes for [Ctrl] because the Select tools will
        // handle that case. [Jon Aquino]
        add(new QuasimodeTool(new SelectFeaturesTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false), null),
                workbenchContext);
        frame.getToolBar().addPlugIn(ClearSelectionPlugIn.getIcon(),
                clearSelectionPlugIn,
                clearSelectionPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addSeparator();
        frame.getToolBar().addPlugIn(changeStylesPlugIn.getIcon(),
                changeStylesPlugIn,
                changeStylesPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addPlugIn(viewAttributesPlugIn.getIcon(),
                viewAttributesPlugIn,
                viewAttributesPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addSeparator();
        add(new OrCompositeTool() {
            @Override
            public String getName() {
                return I18N.get("JUMPConfiguration.fence");
            }
        }.add(new DrawRectangleFenceTool()).add(new DrawPolygonFenceTool()),
                workbenchContext);
        add(new FeatureInfoTool(), workbenchContext);
        frame.getToolBar().addSeparator();

//        configureEditingButton(workbenchContext);
        AdvancedMeasureTool advancedMeasureTool = new AdvancedMeasureTool(workbenchContext);
        workbenchContext.getWorkbench().getFrame().getToolBar().addCursorTool(advancedMeasureTool, advancedMeasureTool.getToolbarButton());
        advancedMeasureOP = new AdvancedMeasureOptionsPanel(workbenchContext);
        GeneralPanel.TabbedPaneAll.addTab(I18N.get(
                "org.openjump.core.ui.plugin.tools.AdvancedMeasurePlugin.OptionPanelTitle"),
                new JScrollPane(advancedMeasureOP));

        frame.getToolBar().addPlugIn(undoPlugIn.getIcon(), undoPlugIn,
                undoPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
        frame.getToolBar().addPlugIn(redoPlugIn.getIcon(), redoPlugIn,
                redoPlugIn.createEnableCheck(workbenchContext),
                workbenchContext);
//        frame.getToolBar().addSeparator();
//        workbenchContext.getWorkbench().getFrame().getOutputFrame().setButton(
//                frame.getToolBar().addPlugIn(outputWindowPlugIn.getIcon(),
//                outputWindowPlugIn, new MultiEnableCheck(), workbenchContext));
        frame.getToolBar().addSeparator();
        findImagesData = new JideSplitButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.STACK));
        findImagesData.setToolTipText(I18N.get("DMTConfiguration.Text.Tooltip.Search-Satellite-Images"));
        findImagesData.setText(I18N.get("DMTConfiguration.Text.Search-Satellite-Images"));
        findImagesData.setFocusable(false);
        findImagesData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionFindData();
            }
        });
        loadDefaultShapes = new JideButton(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.LAYER2));
        loadDefaultShapes.setToolTipText(I18N.get("Text.Load-Default-Layers"));
        loadDefaultShapes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame.actionLoadLayers();
            }
        });
        frame.getToolBar().add(loadDefaultShapes);
        frame.getToolBar().addSeparator();
        DefaultOverlayable findImage = Config.createOverLayableIcon(findImagesData, OverlayableIconsFactory.QUESTION, DefaultOverlayable.SOUTH_WEST,
                I18N.get("Text.Click-for-Help"), I18N.get("Text.selection-must-be-in-geographic"));
        findImage.setMaximumSize(new Dimension(140, 20));
        frame.getToolBar().add(findImage);
        //Last of all, add a separator because some plug-ins may add CursorTools.
        frame.getToolBar().addSeparator();
    }

    private void configureEditingButton(final WorkbenchContext workbenchContext) {
        final JideToggleButton toggleButton = new JideToggleButton();
        workbenchContext.getWorkbench().getFrame().getToolBar().add(toggleButton,
                editingPlugIn.getName(), EditingPlugIn.ICON,
                AbstractPlugIn.toActionListener(editingPlugIn, workbenchContext, new TaskMonitorManager()), null);
        workbenchContext.getWorkbench().getFrame().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                //Can't #getToolbox before Workbench is thrown.
                // Otherwise, get IllegalComponentStateException. Thus, do it inside
                // #componentShown.
                editingPlugIn.getToolbox(workbenchContext).addComponentListener(new ComponentAdapter() {
                    //There are other ways to show/hide the
                    // toolbox. Track 'em. [Bob Boseko]
                    @Override
                    public void componentShown(ComponentEvent e) {
                        toggleButton.setSelected(true);
                    }

                    @Override
                    public void componentHidden(ComponentEvent e) {
                        toggleButton.setSelected(false);
                    }
                });
            }
        });
    }

    /**
     * Call each PlugIn's #initialize() method. Uses reflection to build a list of plug-ins.
     *
     * @param workbenchContext Description of the Parameter
     * @exception Exception Description of the Exception
     */
    private void initializeBuiltInPlugIns(WorkbenchContext workbenchContext) throws Exception {
        Field[] fields = getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Object field = null;
            try {
                field = fields[i].get(this);
            } catch (IllegalAccessException e) {
                Assert.shouldNeverReachHere();
            }
            if (!(field instanceof PlugIn)) {
                continue;
            }
            PlugIn plugIn = (PlugIn) field;
            plugIn.initialize(new PlugInContext(workbenchContext, null, null, null, null));
        }
    }

    public static JideSplitButton findImagesData;
    public static JideButton loadDefaultShapes;
    public static AdvancedMeasureOptionsPanel advancedMeasureOP;
}
