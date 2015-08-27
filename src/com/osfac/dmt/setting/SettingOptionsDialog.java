package com.osfac.dmt.setting;

import com.jidesoft.action.DefaultDockableBarDockableHolder;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.MultiplePageDialog;
import com.jidesoft.dialog.PageList;
import com.jidesoft.swing.JideSwingUtilities;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.panel.ChartFeaturePanel;
import com.osfac.dmt.setting.panel.FontColorPan;
import com.osfac.dmt.setting.panel.GeneralPanel;
import com.osfac.dmt.setting.panel.LanguagePanel;
import com.osfac.dmt.setting.panel.OtherFeatures;
import com.osfac.dmt.setting.panel.PrivacyPanel;
import com.osfac.dmt.setting.panel.ThemePanel;
import com.osfac.dmt.tools.statistic.Statistic;
import com.osfac.dmt.workbench.DMTCommandBarFactory;
import com.osfac.dmt.workbench.DMTConfiguration;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.plugin.OptionsPlugIn;
import com.osfac.dmt.workbench.ui.snap.InstallGridPlugIn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.openjump.core.ui.plugin.edittoolbox.DrawConstrainedPolygonPlugIn;

public class SettingOptionsDialog extends MultiplePageDialog {

    public SettingOptionsDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        getIndexPanel().setBackground(Color.white);
        getButtonPanel().setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        getPagesPanel().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = super.createButtonPanel();
        AbstractAction okAction = new AbstractAction(I18N.get("Text.OK")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getApplyButton().isEnabled()) {
                    getApplyButton().doClick();
                }
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                dispose();
            }
        };
        AbstractAction cancelAction = new AbstractAction(I18N.get("Text.CANCEL")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_CANCELLED);
                setVisible(false);
                dispose();
            }
        };
        ((JButton) buttonPanel.getButtonByName(ButtonNames.OK)).setAction(okAction);
        ((JButton) buttonPanel.getButtonByName(ButtonNames.CANCEL)).setAction(cancelAction);
        setDefaultCancelAction(cancelAction);
        setDefaultAction(okAction);
        ((JButton) buttonPanel.getButtonByName(ButtonNames.OK)).setFocusable(false);
        ((JButton) buttonPanel.getButtonByName(ButtonNames.CANCEL)).setFocusable(false);
        this.getApplyButton().setText(I18N.get("Text.APPLY"));
        this.getApplyButton().setFocusable(false);
        this.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyAction();
            }
        });
        return buttonPanel;
    }

    public void applyAction() {
        DMTConfiguration.advancedMeasureOP.okPressed();
        DrawConstrainedPolygonPlugIn.constraintsOPanel.okPressed();
        InstallGridPlugIn.snapOptionsPanel.okPressed();
        OptionsPlugIn.datasetOptionsPanel.okPressed();
        OptionsPlugIn.selectionStyllingOptionsPanel.okPressed();
        OptionsPlugIn.snapVerticesToolsOptionsPanel.okPressed();
        OptionsPlugIn.editOptionsPanel.okPressed();
        applyLanguage();
        applyPrivacy();
        applyTheme();
        applyGeneral();
        applyFontColor();
        applyChartFeature();
        applyOtherFeatures();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(650, 520);
    }

    public static SettingOptionsDialog showOptionsDialog() {
        SettingOptionsDialog dialog = new SettingOptionsDialog(null, I18N.get("com.osfac.dmt.workbench.ui.plugin.OptionsPlugIn"));
        dialog.setIconImage(SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.DIALOGICON).getImage());
        dialog.setStyle(MultiplePageDialog.ICON_STYLE);
        PageList model = new PageList();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//         setup model
        OptionPageGeneral panel1 = new OptionPageGeneral(I18N.get("SettingOptionsDialog.General"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.GENERAL));
        OptionPageOtherFeatures panel4 = new OptionPageOtherFeatures(I18N.get("SettingOptionsDialog.Other-Features"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.WEB));
        OptionPageTheme panel3 = new OptionPageTheme(I18N.get("SettingOptionsDialog.Themes"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.THEMES));
        OptionPageLanguage panel2 = new OptionPageLanguage(I18N.get("language.language"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.LANGUAGE));
        OptionPageFontColor panel6 = new OptionPageFontColor(I18N.get("SettingOptionsDialog.Fonts-Color"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.FONTSCOLOR));
        OptionPagePrivacy panel5 = new OptionPagePrivacy(I18N.get("SettingOptionsDialog.Privacy"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.PRIVACY));
        OptionPageChart panel7 = new OptionPageChart(I18N.get("SettingOptionsDialog.statistic"), SettingIconsFactory.getImageIcon(SettingIconsFactory.Options.CHARTFEATURE));

        model.append(panel1);
        model.append(panel2);
        model.append(panel3);
        model.append(panel4);
        model.append(panel6);
        if (Config.isFullVersion()) {
            model.append(panel5);
//            if (Config.isAdministrator()) {
            model.append(panel7);
//            }
        }

        dialog.setPageList(model);
        dialog.pack();
        JideSwingUtilities.globalCenterWindow(dialog);
        return dialog;
    }

    public void setCurrentPageN(String page) {
        this.setCurrentPage(page);
    }

    private static class OptionPage extends AbstractDialogPage {

        public OptionPage(String name) {
            super(name);
        }

        @SuppressWarnings("LeakingThisInConstructor")
        public OptionPage(String name, Icon icon) {
            super(name, icon);
            page = this;
        }

        @Override
        public void lazyInitialize() {
            initComponents();
        }

        public void initComponents() {
            BannerPanel headerPanel = new BannerPanel(getTitle(), null);
            headerPanel.setForeground(Color.WHITE);
            headerPanel.setBackground(new Color(10, 36, 106));
            headerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.darkGray, Color.darkGray, Color.gray));

            setLayout(new BorderLayout());
            add(headerPanel, BorderLayout.BEFORE_FIRST_LINE);
        }
    }

    private static class OptionPageGeneral extends OptionPage {

        public OptionPageGeneral(String name, Icon icon) {
            super(name, icon);
            general = new GeneralPanel(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(general, BorderLayout.CENTER);
        }
    }

    private static class OptionPagePrivacy extends OptionPage {

        public OptionPagePrivacy(String name, Icon icon) {
            super(name, icon);
            privacy = new PrivacyPanel(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(privacy, BorderLayout.CENTER);
        }
    }

    private static class OptionPageChart extends OptionPage {

        public OptionPageChart(String name, Icon icon) {
            super(name, icon);
            chart = new ChartFeaturePanel(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(chart, BorderLayout.CENTER);
        }
    }

    private static class OptionPageFontColor extends OptionPage {

        public OptionPageFontColor(String name, Icon icon) {
            super(name, icon);
            fontColor = new FontColorPan(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(fontColor, BorderLayout.CENTER);
        }
    }

    private static class OptionPageTheme extends OptionPage {

        public OptionPageTheme(String name, Icon icon) {
            super(name, icon);
            theme = new ThemePanel(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(theme, BorderLayout.CENTER);
        }
    }

    private static class OptionPageLanguage extends OptionPage {

        public OptionPageLanguage(String name, Icon icon) {
            super(name, icon);
            language = new LanguagePanel(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(language, BorderLayout.CENTER);
        }
    }

    private static class OptionPageOtherFeatures extends OptionPage {

        public OptionPageOtherFeatures(String name, Icon icon) {
            super(name, icon);
            otherFeatures = new OtherFeatures(this);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(otherFeatures, BorderLayout.CENTER);
        }
    }

    private void setSmoothnes() {
        switch (OtherFeatures.EnCBSmooth.getSelectedItem().toString()) {
            case "VERY SMOOTH":
                Config.pref.putInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_VERY_SMOOTH);
                break;
            case "SMOOTH":
                Config.pref.putInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_SMOOTH);
                break;
            case "MEDIUM":
                Config.pref.putInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM);
                break;
            case "ROUGH":
                Config.pref.putInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_ROUGH);
                break;
            case "VERY ROUGH":
                Config.pref.putInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_VERY_ROUGH);
                break;
        }
        switch (OtherFeatures.ExCBSmooth.getSelectedItem().toString()) {
            case "VERY SMOOTH":
                Config.pref.putInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_VERY_SMOOTH);
                break;
            case "SMOOTH":
                Config.pref.putInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_SMOOTH);
                break;
            case "MEDIUM":
                Config.pref.putInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM);
                break;
            case "ROUGH":
                Config.pref.putInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_ROUGH);
                break;
            case "VERY ROUGH":
                Config.pref.putInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_VERY_ROUGH);
                break;
        }
    }

    private void setSpeed() {
        switch (OtherFeatures.EnCBSpeed.getSelectedItem().toString()) {
            case "VERY SLOW":
                Config.pref.putInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_VERY_SLOW);
                break;
            case "SLOW":
                Config.pref.putInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_SLOW);
                break;
            case "MEDIUM":
                Config.pref.putInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_MEDIUM);
                break;
            case "FAST":
                Config.pref.putInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_FAST);
                break;
            case "VERY FAST":
                Config.pref.putInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_VERY_FAST);
                break;
        }

        switch (OtherFeatures.ExCBSpeed.getSelectedItem().toString()) {
            case "VERY SLOW":
                Config.pref.putInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_VERY_SLOW);
                break;
            case "SLOW":
                Config.pref.putInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_SLOW);
                break;
            case "MEDIUM":
                Config.pref.putInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM);
                break;
            case "FAST":
                Config.pref.putInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_FAST);
                break;
            case "VERY FAST":
                Config.pref.putInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_VERY_FAST);
                break;
        }
    }

    private void setEffect() {
        switch (OtherFeatures.EnCBEffect.getSelectedItem().toString()) {
            case "FLY":
                Config.pref.putInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_FLY);
                break;
            case "ZOOM":
                Config.pref.putInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_ZOOM);
                break;
            case "FADE":
                Config.pref.putInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_FADE);
                break;
        }
        switch (OtherFeatures.ExCBEffect.getSelectedItem().toString()) {
            case "FLY":
                Config.pref.putInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY);
                break;
            case "ZOOM":
                Config.pref.putInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_ZOOM);
                break;
            case "FADE":
                Config.pref.putInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FADE);
                break;
        }
    }

    private void setLocationFlyingDialog() {
        if (OtherFeatures.EnBCenter.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.CENTER);
        } else if (OtherFeatures.EnBEast.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.EAST);
        } else if (OtherFeatures.EnBNorth.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.NORTH);
        } else if (OtherFeatures.EnBNorthEast.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.NORTH_EAST);
        } else if (OtherFeatures.EnBNorthWest.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.NORTH_WEST);
        } else if (OtherFeatures.EnBSouth.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.SOUTH);
        } else if (OtherFeatures.EnBSouthEast.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.SOUTH_EAST);
        } else if (OtherFeatures.EnBSouthWest.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.SOUTH_WEST);
        } else if (OtherFeatures.EnBWest.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceLocation, SwingConstants.WEST);
        }
    }

    private void setDirectionFlyingDialog() {
        if (OtherFeatures.ExBTop.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.TOP);
        } else if (OtherFeatures.ExBTopRight.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.TOP_RIGHT);
        } else if (OtherFeatures.ExBTopLeft.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.TOP_LEFT);
        } else if (OtherFeatures.ExBBottom.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM);
        } else if (OtherFeatures.ExBBottomLeft.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM_LEFT);
        } else if (OtherFeatures.ExBBottomRight.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM_RIGHT);
        } else if (OtherFeatures.ExBLeft.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.LEFT);
        } else if (OtherFeatures.EBRight.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.exitDirection, CustomAnimation.RIGHT);
        }
        if (OtherFeatures.EBTop.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP);
        } else if (OtherFeatures.EBTopRight.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP_RIGHT);
        } else if (OtherFeatures.EBTopLeft.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP_LEFT);
        } else if (OtherFeatures.EBBottom.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.BOTTOM);
        } else if (OtherFeatures.EBBottomLeft.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.BOTTOM_LEFT);
        } else if (OtherFeatures.EBBottomRight.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.BOTTOM_RIGHT);
        } else if (OtherFeatures.EBLeft.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.LEFT);
        } else if (OtherFeatures.EBRight.isSelected()) {
            Config.pref.putInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.RIGHT);
        }
    }

//    public static void main(String[] argv) {
//        LookAndFeelFactory.installDefaultLookAndFeel();
//        showOptionsDialog().setVisible(true);
//
//    }
    private void applyPrivacy() {
        try {
            PreparedStatement ps = Config.con.prepareStatement("select * from dmt_user where id_user = ?");
            ps.setInt(1, WorkbenchFrame.idUser);
            ResultSet res = ps.executeQuery();
            res.next();
            Config.pref.putBoolean(SettingKeyFactory.Privacy.rememberLoginInfo, PrivacyPanel.ChBParameter.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.Privacy.rememberEmailOnly, PrivacyPanel.RBEmail.isSelected());
            if (PrivacyPanel.ChBParameter.isSelected()) {
                if (PrivacyPanel.RBEmail.isSelected()) {
                    Config.pref.put(SettingKeyFactory.Privacy.rememberEmail, res.getString("email"));
                    Config.pref.put(SettingKeyFactory.Privacy.rememberPassword, "");
                } else {
                    Config.pref.put(SettingKeyFactory.Privacy.rememberEmail, res.getString("email"));
                    Config.pref.put(SettingKeyFactory.Privacy.rememberPassword, Config.decrypt(res.getString("password")));
                }
            } else {
                Config.pref.put(SettingKeyFactory.Privacy.rememberEmail, "");
                Config.pref.put(SettingKeyFactory.Privacy.rememberPassword, "");
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"
                    + ""), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private void applyOtherFeatures() {
        if (OtherFeatures.RBHostProvider.isSelected()) {
            Config.pref.put(SettingKeyFactory.OtherFeatures.HOST, "http://" + Config.host);
        } else {
            Config.pref.put(SettingKeyFactory.OtherFeatures.HOST, "http://www.osfac.net");
        }
        Config.pref.putBoolean(SettingKeyFactory.OtherFeatures.RBHostProvider, OtherFeatures.RBHostProvider.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.OtherFeatures.RBWebsiteProvider, !OtherFeatures.RBHostProvider.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.OtherFeatures.ChKLayer, OtherFeatures.ChKLayer.isSelected());
    }

    private void applyLanguage() {
        int index = LanguagePanel.customCombo.getComboBox().getSelectedIndex();
        if (index != Config.pref.getInt(SettingKeyFactory.Language.INDEX, index)) {
            Config.pref.put(SettingKeyFactory.Language.ABREV, LanguagePanel.customCombo.petStrings[index]);
            Config.pref.putInt(SettingKeyFactory.Language.INDEX, index);
            Config.setDefaultLocale(index);
            JOptionPane.showMessageDialog(this, I18N.get("Text.Restart-OSFAC-DMT"
                    + ""), I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
            if (DMTWorkbench.frame instanceof DefaultDockableBarDockableHolder) {
                DMTWorkbench.frame.getLayoutPersistence().resetToDefault();
            }
        }
    }

    private void applyTheme() {
        if (ThemePanel.CBTheme.getSelectedItem().equals("Default")) {
            DMTCommandBarFactory.actionTheme("Gray");
        } else if (ThemePanel.CBTheme.getSelectedItem().equals("Gray")) {
            DMTCommandBarFactory.actionTheme("Gray");
        } else if (ThemePanel.CBTheme.getSelectedItem().equals("Green")) {
            DMTCommandBarFactory.actionTheme("HomeStead");
        } else if (ThemePanel.CBTheme.getSelectedItem().equals("Metallic")) {
            DMTCommandBarFactory.actionTheme("Metallic");
        } else if (ThemePanel.CBTheme.getSelectedItem().equals("Blue")) {
            DMTCommandBarFactory.actionTheme("NormalColor");
        }
    }

    private void applyGeneral() {
        setSmoothnes();
        setEffect();
        setSpeed();
        setLocationFlyingDialog();
        setDirectionFlyingDialog();
    }

    private void applyFontColor() {
        setColorFromKey(FontColorPan.RStripe21Color1.getSelectedColor(), SettingKeyFactory.FontColor.RStripe21Color1);
        setColorFromKey(FontColorPan.RStripe21Color2.getSelectedColor(), SettingKeyFactory.FontColor.RStripe21Color2);
        setColorFromKey(FontColorPan.RStripe22Color1.getSelectedColor(), SettingKeyFactory.FontColor.RStripe22Color1);
        setColorFromKey(FontColorPan.RStripe22Color2.getSelectedColor(), SettingKeyFactory.FontColor.RStripe22Color2);
        setColorFromKey(FontColorPan.RStripe3Color1.getSelectedColor(), SettingKeyFactory.FontColor.RStripe3Color1);
        setColorFromKey(FontColorPan.RStripe3Color2.getSelectedColor(), SettingKeyFactory.FontColor.RStripe3Color2);
        setColorFromKey(FontColorPan.RStripe3Color3.getSelectedColor(), SettingKeyFactory.FontColor.RStripe3Color3);
    }

    private void applyChartFeature() {
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBLineLabel, ChartFeaturePanel.RBLineLabel.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBSimpleLabel, ChartFeaturePanel.RBSimpleLabel.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBNoLabel, ChartFeaturePanel.RBNoLabel.isSelected());
        Config.pref.putInt(SettingKeyFactory.ChartFeatures.SLAngle, ChartFeaturePanel.SLAngle.getValue());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBExplodedSegment, ChartFeaturePanel.ChBExplodedSegment.isSelected());

        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.BarChBVLine, ChartFeaturePanel.BarChBVLine.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.BarChBHLine, ChartFeaturePanel.BarChBHLine.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.LineChBVLine, ChartFeaturePanel.LineChBVLine.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.LineChBHLine, ChartFeaturePanel.LineChBHLine.isSelected());
        setColorFromKey(ChartFeaturePanel.CBColor.getSelectedColor(), SettingKeyFactory.ChartFeatures.CBColor);
        setColorFromKey(ChartFeaturePanel.CBColor2.getSelectedColor(), SettingKeyFactory.ChartFeatures.CBColor2);
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RandomColor, ChartFeaturePanel.RandomColor.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.OneColor, ChartFeaturePanel.OneColor.isSelected());
        Config.pref.putInt(SettingKeyFactory.ChartFeatures.SPLineWidth, Integer.parseInt(ChartFeaturePanel.SPLineWidth.getValue().toString()));

        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBFlat, ChartFeaturePanel.RBFlat.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBRaised, ChartFeaturePanel.RBRaised.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RB3D, ChartFeaturePanel.RB3D.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBShadow, ChartFeaturePanel.ChBShadow.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBRollover, ChartFeaturePanel.ChBRollover.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, ChartFeaturePanel.ChBOutline.isSelected());
        Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBSelectionOutline, ChartFeaturePanel.ChBSelectionOutline.isSelected());

        if (WorkbenchFrame._documentPane.isDocumentOpened(I18N.get("Text.Statistic-tab"))) {
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) Statistic._tree.getCellRenderer();
            renderer.setLeafIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/new12.gif")));
            renderer.setClosedIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/background(6).png")));
            renderer.setOpenIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/background2.png")));
        }
    }

    private void setColorFromKey(Color color, String key) {
        Config.pref.put(key, color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
    }
    static ThemePanel theme;
    static LanguagePanel language;
    static GeneralPanel general;
    static FontColorPan fontColor;
    static PrivacyPanel privacy;
    static OtherFeatures otherFeatures;
    static ChartFeaturePanel chart;
    public static AbstractDialogPage page;
}
