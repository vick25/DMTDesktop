package com.osfac.dmt.tools.statistic;

import com.jidesoft.chart.BarResizePolicy;
import com.jidesoft.chart.Chart;
import com.jidesoft.chart.ChartType;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.event.MouseDragPanner;
import com.jidesoft.chart.event.PanIndicator;
import com.jidesoft.chart.event.PointSelection;
import com.jidesoft.chart.model.*;
import com.jidesoft.chart.render.*;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.chart.util.ChartUtils;
import com.jidesoft.combobox.CheckBoxListComboBox;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;
import com.jidesoft.swing.*;
import com.jidesoft.tree.TreeUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import irepport.view.Print;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class Statistic extends javax.swing.JPanel {

    public Statistic() {
        ColorChart.add(Color.blue);
        ColorChart.add(Color.red);
        ColorChart.add(Color.green);
        ColorChart.add(Color.yellow);
        ColorChart.add(Color.orange);
        ColorChart.add(Color.cyan);
        ColorChart.add(Color.magenta);
        ColorChart.add(Color.gray);
        ColorChart.add(Color.pink);
        initComponents();
        BClose = new JideButton("Close", new ImageIcon(getClass().getResource(""
                + "/com/osfac/dmt/images/exit32.png")));
        BClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkbenchFrame._documentPane.closeDocument("Statistic");
            }
        });
        BClose.setBackground(Color.white);
        BClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BClose.setOpaque(false);
        BClose.setFocusable(false);
        BClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BReset = new JideButton("Reset", new ImageIcon(getClass().getResource(""
                + "/com/osfac/dmt/images/icon-48-clear.png")));
        BReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CBCategory.setSelectedIndex(-1);
                CBThematic.setSelectedIndex(-1);
                CBLocation.setSelectedIndex(-1);
                CBSoftware.setSelectedIndex(-1);
                CBMonth.setSelectedIndex(-1);
                CBYear.setSelectedIndex(-1);
                CBTrimester.setSelectedIndex(-1);
                CBSemester.setSelectedIndex(-1);
                panChart.removeAll();
                panChart.repaint();
                _field.setText(null);
                _tree.setSelectionRow(-1);
                BPieChartActionPerformed(evt);
            }
        });
        BReset.setBackground(Color.white);
        BReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BReset.setOpaque(false);
        BReset.setFocusable(false);
        BReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BPrint = new JideSplitButton("Preview and Print", new ImageIcon(getClass().getResource(""
                + "/com/osfac/dmt/images/fileprint(23).png")));
        BPrint.setAlwaysDropdown(true);
        BPrint.add(new AbstractAction("Print Chart") {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (chart == null || !BTable.isEnabled()) {
                        JOptionPane.showMessageDialog(DMTWorkbench.frame, "No chart has been found ...", ""
                                + "Fatal error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        chart.setTitle("");
                        repTemp = new File(tampon + File.separator + "DATA_Training");
                        if (!repTemp.exists()) {
                            repTemp.mkdirs();
                        }
                        dirIcon = repTemp.getAbsolutePath() + File.separator + "chart.png";
                        ChartUtils.writePngToFile(panChart, new File(dirIcon));
                        HashMap parameter = new HashMap();
                        parameter.put("logo", getClass().getResourceAsStream("/jasper/001.png"));
                        parameter.put("photo", new FileInputStream(new File(dirIcon)));
                        parameter.put("title", titledChart());
                        JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream(""
                                + "/jasper/chart.jasper"), parameter, Config.con);
                        Print.viewReport(jp, false, Config.LOCALE);
                        chart.setTitle(titledChart());
                    }
                } catch (FileNotFoundException | JRException ex) {
                    JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                            + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
                }
            }
        });
        BPrint.add(new AbstractAction("Print Report") {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement ps = Config.con.prepareStatement("TRUNCATE TABLE `t_tampon`");
                    ps.executeUpdate();
                    String query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                            + "t_location.id_location = t_training.id_location "
                            + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                            + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                            + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                            + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                            + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                            + "" + criteriaSearch();
                    Statement stat = Config.con.createStatement();
                    Config.res = stat.executeQuery(query);
                    while (Config.res.next()) {
                        fillTampon(Config.res.getInt(1));
                    }

                    HashMap parameter = new HashMap();
                    parameter.put("logo", getClass().getResourceAsStream("/jasper/001.png"));
                    JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream(""
                            + "/jasper/report.jasper"), parameter, Config.con);
                    Print.viewReport(jp, false, Config.LOCALE);
                    ps = Config.con.prepareStatement("TRUNCATE TABLE `t_tampon`");
                    ps.executeUpdate();
                } catch (SQLException | JRException ex) {
                    JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                            + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
                }
            }
        });
        BPrint.setBackground(Color.white);
        BPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BPrint.setOpaque(false);
        BPrint.setFocusable(false);
        BPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        TB1.add(BPrint);
        TB1.add(BReset);
        TB1.add(BClose);
        _field.setMatchesLeafNodeOnly(true);
        _field.setHideEmptyParentNode(true);
        _field.setWildcardEnabled(true);
        _tree = new JTree(_field.getDisplayTreeModel());
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) _tree.getCellRenderer();
        renderer.setLeafIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/new12.gif")));
        renderer.setClosedIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/background(6).png")));
        renderer.setOpenIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/background2.png")));
        _field.setTree(_tree);
        TreeSearchable searchable = SearchableUtils.installSearchable(_tree);
        searchable.setFromStart(false);
        _tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                if (titledChart().equalsIgnoreCase("Number of trainees by frequency")) {
                    CBSoftware.setEnabled(false);
                    CBThematic.setEnabled(false);
                    CBSoftware.setSelectedIndex(-1);
                    CBThematic.setSelectedIndex(-1);
                } else {
                    CBSoftware.setEnabled(true);
                    CBThematic.setEnabled(true);
                }
                drawChart();
            }
        });
        TreeUtils.expandAll(_tree, true);
        _tree.setRootVisible(false);
        _tree.setShowsRootHandles(true);
        jScrollPane2.setViewportView(_tree);
        loadComboBoxes();
        model = new MyTableModel();
        table = new SortableTable(model);
        HyperlinkTableCellEditorRenderer render = new HyperlinkTableCellEditorRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(render);
        table.getColumnModel().getColumn(0).setCellEditor(render);
        RolloverTableUtils.install(table);
        render.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    ArrayList<String> list = new ArrayList<>();
                    String val = titledChart();
                    Object source = evt.getSource();
                    Statement stat = Config.con.createStatement();
                    Config.res = stat.executeQuery(linkQuery(val, Config.correctText(((JideButton) source).getText())));
                    while (Config.res.next()) {
                        list.add(Config.res.getString(1));
                    }
                    if (val.contains("training")) {
//                        new ShowTraining(DMTWorkbench.frame, true, list).setVisible(true);
                    } else {
                        if (!val.equalsIgnoreCase("Trainees list by institution")
                                || !val.equalsIgnoreCase("Trainees list by category")
                                || !val.equalsIgnoreCase("Institution list")) {
//                            new ShowTrainee(DMTWorkbench.frame, true, list).setVisible(true);
                        }
                    }
                    //JOptionPane.showMessageDialog(DMTWorkbench.frame, linkQuery(titledChart(), ((JideButton) source).getText()));
                } catch (SQLException ex) {
                    JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                            + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
                }
            }
        });
        table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{getColorFromKey(Config.pref.get(
                    SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")), getColorFromKey(Config.pref.get(
                    SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
        Config.centerTableHeadAndBold(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pan = new JPanel(new BorderLayout());
        pan.setBackground(Color.white);
        SimpleScrollPane Scrll = new SimpleScrollPane(table);
        Scrll.setHorizontalScrollBarPolicy(SimpleScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Scrll.setVerticalScrollBarPolicy(SimpleScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        Scrll.getViewport().setBackground(Color.white);
        pan.add(Scrll, BorderLayout.CENTER);
        JideButton bt = new JideButton("Export to excel", new ImageIcon(getClass().getResource("/com/osfac/dmt/images/excel.png")));
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileSystemView fsv = FileSystemView.getFileSystemView();
                JFileChooser fc = new JFileChooser(fsv.getRoots()[0]);
                fc.setFileFilter(new FileNameExtensionFilter("Excel file", "xls"));
                File fichier = new File(fsv.getRoots()[0] + File.separator + value + ".xls");
                fc.setSelectedFile(fichier);
                int result = fc.showSaveDialog(DMTWorkbench.frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    if (!fc.getSelectedFile().exists()) {
                        try {
                            if (!fc.getSelectedFile().getAbsolutePath().contains(".")) {
                                CreerExcel(new File(fc.getSelectedFile().getAbsolutePath() + ".xls"));
                            } else {
                                CreerExcel(fc.getSelectedFile());
                            }
                        } catch (Exception ex) {
                            JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
                        }
                    } else {
                        JOptionPane.showMessageDialog(DMTWorkbench.frame, fc.getSelectedFile().getName() + " already exists ..."
                                + "", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        pan.add(bt, BorderLayout.AFTER_LAST_LINE);
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableResetButton();
                if (panChart.getComponentCount() > 0) {
                    BClipboard.setEnabled(true);
//                    BPrint.setEnabled(true);
                } else {
                    BClipboard.setEnabled(false);
//                    BPrint.setEnabled(false);
                }
                if (panChart.getComponentCount() > 0 || !_tree.isSelectionEmpty()) {
                    BRefresh.setEnabled(true);
                } else {
                    BRefresh.setEnabled(false);
                }
            }
        });
        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BGPeriod = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        panChart = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        RBMonth = new javax.swing.JRadioButton();
        RBTrimester = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        RBSemester = new javax.swing.JRadioButton();
        CBMonth = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBTrimester = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBSemester = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBYear = new com.jidesoft.combobox.CheckBoxListComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        CBLocation = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBSoftware = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBThematic = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBCategory = new com.jidesoft.combobox.CheckBoxListComboBox();
        jToolBar2 = new javax.swing.JToolBar();
        TB1 = new javax.swing.JToolBar();
        jToolBar8 = new javax.swing.JToolBar();
        BPieChart = new javax.swing.JButton();
        BBarChart = new javax.swing.JButton();
        BLineChart = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        BTable = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        BRefresh = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        BClipboard = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        BSetting = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        _field = new com.jidesoft.tree.QuickTreeFilterField(){
            @Override
            public void applyFilter() {
                super.applyFilter();
                TreeUtils.expandAll(_tree);
            }
        };
        jScrollPane2 = new javax.swing.JScrollPane();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        panChart.setBackground(new java.awt.Color(255, 255, 255));
        panChart.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(panChart);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Period"));

        BGPeriod.add(RBMonth);
        RBMonth.setSelected(true);
        RBMonth.setText("Month");
        RBMonth.setFocusable(false);
        RBMonth.setOpaque(false);
        RBMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBMonthItemStateChanged(evt);
            }
        });

        BGPeriod.add(RBTrimester);
        RBTrimester.setText("Trimester");
        RBTrimester.setFocusable(false);
        RBTrimester.setOpaque(false);
        RBTrimester.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBTrimesterItemStateChanged(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Year");

        BGPeriod.add(RBSemester);
        RBSemester.setText("Semester");
        RBSemester.setFocusable(false);
        RBSemester.setOpaque(false);
        RBSemester.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBSemesterItemStateChanged(evt);
            }
        });

        CBMonth.setEditable(false);
        CBMonth.setFocusable(false);

        CBTrimester.setEditable(false);
        CBTrimester.setEnabled(false);
        CBTrimester.setFocusable(false);

        CBSemester.setEditable(false);
        CBSemester.setEnabled(false);
        CBSemester.setFocusable(false);

        CBYear.setEditable(false);
        CBYear.setFocusable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RBTrimester)
                    .addComponent(RBSemester)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RBMonth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBSemester, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(CBTrimester, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(CBMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(CBYear, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12)
                    .addComponent(CBYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RBSemester))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBTrimester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RBTrimester))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RBMonth))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));

        jLabel3.setText("Location");

        jLabel2.setText("<html>Thematic");

        jLabel13.setText("Category");

        jLabel14.setText("Software");

        CBLocation.setEditable(false);
        CBLocation.setFocusable(false);

        CBSoftware.setEditable(false);
        CBSoftware.setFocusable(false);

        CBThematic.setEditable(false);
        CBThematic.setFocusable(false);

        CBCategory.setEditable(false);
        CBCategory.setFocusable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBSoftware, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CBThematic, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(CBLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBThematic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(CBSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 528, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel3, jPanel4});

        jSplitPane1.setLeftComponent(jPanel2);

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        TB1.setBackground(new java.awt.Color(255, 255, 255));
        TB1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statistics", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 9), new java.awt.Color(0, 102, 255))); // NOI18N
        TB1.setRollover(true);
        jToolBar2.add(TB1);

        jToolBar8.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Charts", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 9), new java.awt.Color(0, 102, 255))); // NOI18N
        jToolBar8.setRollover(true);

        BPieChart.setBackground(new java.awt.Color(255, 255, 255));
        BPieChart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/pie2d.png"))); // NOI18N
        BPieChart.setText("Pie chart");
        BPieChart.setEnabled(false);
        BPieChart.setFocusable(false);
        BPieChart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BPieChart.setOpaque(false);
        BPieChart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BPieChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPieChartActionPerformed(evt);
            }
        });
        jToolBar8.add(BPieChart);

        BBarChart.setBackground(new java.awt.Color(255, 255, 255));
        BBarChart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/columns2d.png"))); // NOI18N
        BBarChart.setText("Bar chart");
        BBarChart.setFocusable(false);
        BBarChart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BBarChart.setOpaque(false);
        BBarChart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BBarChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBarChartActionPerformed(evt);
            }
        });
        jToolBar8.add(BBarChart);

        BLineChart.setBackground(new java.awt.Color(255, 255, 255));
        BLineChart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/stackdirect2d.png"))); // NOI18N
        BLineChart.setText("Line chart");
        BLineChart.setFocusable(false);
        BLineChart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BLineChart.setOpaque(false);
        BLineChart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BLineChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BLineChartActionPerformed(evt);
            }
        });
        jToolBar8.add(BLineChart);
        jToolBar8.add(jSeparator4);

        BTable.setBackground(new java.awt.Color(255, 255, 255));
        BTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/view_multicolumn(10).png"))); // NOI18N
        BTable.setText("View Table");
        BTable.setFocusable(false);
        BTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BTable.setOpaque(false);
        BTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTableActionPerformed(evt);
            }
        });
        jToolBar8.add(BTable);
        jToolBar8.add(jSeparator2);

        BRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/refresh2.png"))); // NOI18N
        BRefresh.setText("Refresh");
        BRefresh.setFocusable(false);
        BRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BRefresh.setOpaque(false);
        BRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRefreshActionPerformed(evt);
            }
        });
        jToolBar8.add(BRefresh);
        jToolBar8.add(jSeparator3);

        BClipboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/1_folder2.png"))); // NOI18N
        BClipboard.setText("Chart to clipboard");
        BClipboard.setEnabled(false);
        BClipboard.setFocusable(false);
        BClipboard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BClipboard.setOpaque(false);
        BClipboard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BClipboardActionPerformed(evt);
            }
        });
        jToolBar8.add(BClipboard);
        jToolBar8.add(jSeparator1);

        BSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/advancedsettings(4).png"))); // NOI18N
        BSetting.setText("Settings");
        BSetting.setFocusable(false);
        BSetting.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSetting.setOpaque(false);
        BSetting.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSettingActionPerformed(evt);
            }
        });
        jToolBar8.add(BSetting);

        jToolBar2.add(jToolBar8);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        _field.setHintText("Filter");
        _field.setShowMismatchColor(true);
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("JTree");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Number of Trainees");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Period");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("By Month");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("By Trimester");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("By Year");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Sex");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Frequency");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Nationality");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Institution");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Fonction");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Department");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Number of Training");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Period");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("By Month");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("By Trimester");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("By Year");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Category");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Topic");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Thematic");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Software");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Institution");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By CARPE Partner");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Sponsor");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Trainees list");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Category");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("By Institution");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Institution list");
        treeNode1.add(treeNode2);
        _field.setTreeModel(new javax.swing.tree.DefaultTreeModel(treeNode1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(_field, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void RBMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBMonthItemStateChanged
        if (RBMonth.isSelected()) {
            CBMonth.setEnabled(true);
        } else {
            CBMonth.setEnabled(false);
            CBMonth.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_RBMonthItemStateChanged

    private void RBTrimesterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBTrimesterItemStateChanged
        if (RBTrimester.isSelected()) {
            CBTrimester.setEnabled(true);
        } else {
            CBTrimester.setEnabled(false);
            CBTrimester.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_RBTrimesterItemStateChanged

    private void RBSemesterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBSemesterItemStateChanged
        if (RBSemester.isSelected()) {
            CBSemester.setEnabled(true);
        } else {
            CBSemester.setEnabled(false);
            CBSemester.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_RBSemesterItemStateChanged

    private void BLineChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BLineChartActionPerformed
        BBarChart.setEnabled(true);
        BPieChart.setEnabled(true);
        BLineChart.setEnabled(false);
        BTable.setEnabled(true);
        if (_tree.getSelectionPath() != null) {
            createLineChart();
        }
    }//GEN-LAST:event_BLineChartActionPerformed

    private void BBarChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BBarChartActionPerformed
        BBarChart.setEnabled(false);
        BPieChart.setEnabled(true);
        BLineChart.setEnabled(true);
        BTable.setEnabled(true);
        if (_tree.getSelectionPath() != null) {
            createBarChart();
        }
    }//GEN-LAST:event_BBarChartActionPerformed

    private void BPieChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPieChartActionPerformed
        BBarChart.setEnabled(true);
        BPieChart.setEnabled(false);
        BLineChart.setEnabled(true);
        BTable.setEnabled(true);
        if (_tree.getSelectionPath() != null) {
            createPieChart();
        }
    }//GEN-LAST:event_BPieChartActionPerformed

    private void BSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSettingActionPerformed
//        Config.optionsDialog.setCurrentPageN("Chart Feature");
        Config.optionsDialog.setVisible(true);
    }//GEN-LAST:event_BSettingActionPerformed

    private void BRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRefreshActionPerformed
        drawChart();
    }//GEN-LAST:event_BRefreshActionPerformed

    private void BClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BClipboardActionPerformed
        ChartUtils.copyImageToClipboard(panChart);
    }//GEN-LAST:event_BClipboardActionPerformed

    private void BTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTableActionPerformed
        BBarChart.setEnabled(true);
        BPieChart.setEnabled(true);
        BLineChart.setEnabled(true);
        BTable.setEnabled(false);
        if (_tree.getSelectionPath() != null) {
            createTable();
        }
    }//GEN-LAST:event_BTableActionPerformed

    private void fillTampon(int id_training) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("select id_training,begin_date,title,duration,number "
                    + "from t_training where id_training = ?");
            ps.setInt(1, id_training);
            ResultSet res = ps.executeQuery();
            int male = getMaleNumber(id_training), female = getFemaleNumber(id_training);
            String institution = getInstitution(id_training);
            while (res.next()) {
                ps = Config.con.prepareStatement("insert into t_tampon values (?,?,?,?,?,?,?,?)");
                ps.setString(1, null);
                ps.setDate(2, res.getDate("begin_date"));
                ps.setString(3, res.getString("title"));
                ps.setString(4, res.getString("duration"));
                ps.setInt(5, male);
                ps.setInt(6, female);
                ps.setInt(7, res.getInt("number"));
                ps.setString(8, institution);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String getInstitution(int id_training) {
        String institution = "";
        try {
            PreparedStatement ps = Config.con.prepareStatement("select distinct institution_name from t_workfor inner "
                    + "join t_institution on t_workfor.id_institution = t_institution.id_institution "
                    + "where id_training = ?");
            ps.setInt(1, id_training);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                institution += res.getString(1) + ',';
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        institution = institution.substring(0, institution.length() - 1);
        return institution;
    }

    private int getMaleNumber(int id_training) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("select count(t_follow.id_trainee) from t_follow inner "
                    + "join t_trainee on t_follow.id_trainee = t_trainee.id_trainee where id_training = ? and sex = 'male'");
            ps.setInt(1, id_training);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private int getFemaleNumber(int id_training) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("select count(t_follow.id_trainee) from t_follow inner "
                    + "join t_trainee on t_follow.id_trainee = t_trainee.id_trainee where id_training = ? and sex = 'female'");
            ps.setInt(1, id_training);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void showList(String val) {
        String query = "";
        if (val.equalsIgnoreCase("Trainees list by category")) {
            try {
                ArrayList category = new ArrayList();
                ArrayList trainee = new ArrayList();
                query = "SELECT distinct t_category.id_category,t_workfor.id_trainee FROM t_training INNER JOIN "
                        + "t_category ON t_training.id_category = t_category.id_category "
                        + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                        + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                        + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                        + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                        + "t_training.`id_training` = t_workfor.`id_training` " + criteriaSearch();
                Statement stat = Config.con.createStatement();
                Config.res = stat.executeQuery(query);
                while (Config.res.next()) {
                    category.add(Config.res.getString(1));
                    trainee.add(Config.res.getString(2));
                }
//                new CategoryTraineeList(DMTWorkbench.frame, true, category, trainee).setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (val.equalsIgnoreCase("Trainees list by institution")) {
            try {
                ArrayList inst = new ArrayList();
                ArrayList trainee = new ArrayList();
                query = "SELECT distinct t_institution.id_institution,t_workfor.id_trainee FROM t_training INNER JOIN "
                        + "t_category ON t_training.id_category = t_category.id_category "
                        + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                        + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                        + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                        + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                        + "t_training.`id_training` = t_workfor.`id_training` INNER JOIN `t_institution` t_institution ON "
                        + "t_workfor.`id_institution` = t_institution.`id_institution` " + criteriaSearch();
                Statement stat = Config.con.createStatement();
                Config.res = stat.executeQuery(query);
                while (Config.res.next()) {
                    inst.add(Config.res.getString(1));
                    trainee.add(Config.res.getString(2));
                }
//                new InstitutionTraineeList(DMTWorkbench.frame, true, inst, trainee).setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            query = "SELECT distinct t_institution.id_institution,institution_name FROM t_training INNER JOIN "
                    + "t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` INNER JOIN `t_institution` t_institution ON "
                    + "t_workfor.`id_institution` = t_institution.`id_institution` " + criteriaSearch() + ""
                    + " order by institution_name";
//            new InstitutionList(DMTWorkbench.frame, true, query).setVisible(true);
        }
    }

    private void drawChart() {
        try {
            TreePath path = _tree.getSelectionPath();
            if (path != null) {
                String val = titledChart();
                if (_tree.getModel().isLeaf(path.getLastPathComponent())) {
                    if (val.equalsIgnoreCase("Trainees list by institution")
                            || val.equalsIgnoreCase("Trainees list by category")
                            || val.equalsIgnoreCase("Institution list")) {
                        value = "";
                        cleanTable();
                        panChart.removeAll();
                        panChart.repaint();
                        showList(val);
                    } else {
                        runQuery(factoryQuery(val));
                    }
                } else {
                    value = "";
                    cleanTable();
                    panChart.removeAll();
                    panChart.repaint();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    String titledChart() {
        String title = "";
        TreePath path = _tree.getSelectionPath();
        if (path != null) {
            if (_tree.getModel().isLeaf(path.getLastPathComponent())) {
                for (int i = 1; i < path.getPath().length; i++) {
                    if (path.getPath()[i].toString().equalsIgnoreCase("By Period")) {
                        continue;
                    } else {
                        title += path.getPath()[i] + " ";
                    }
                }
                title = title.substring(0, title.length() - 1);
                title = Config.capitalFirstLetter(title.toLowerCase());
            }
        }
        return title;
    }

    private String factoryQuery(String criteria) {
        String query = "";
        if (criteria.equalsIgnoreCase("Number of trainees by month")) {
            return query = "SELECT month(begin_date),count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic " + criteriaSearch() + " GROUP BY month(begin_date)";
        } else if (criteria.equalsIgnoreCase("Number of trainees by trimester")) {
            return query = "SELECT month(begin_date),count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic " + criteriaSearch() + " GROUP BY month(begin_date)";
        } else if (criteria.equalsIgnoreCase("Number of trainees by year")) {
            return query = "SELECT year(begin_date),count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic " + criteriaSearch() + " GROUP BY year(begin_date)";
        } else if (criteria.equalsIgnoreCase("Number of trainees by sex")) {
            return query = "SELECT sex,count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic " + criteriaSearch() + " GROUP BY sex";
        } else if (criteria.equalsIgnoreCase("Number of trainees by frequency")) {
            return query = "SELECT bob,count(bob) FROM (select count(t_follow.id_trainee) as bob FROM t_training "
                    + "INNER JOIN t_follow ON t_training.id_training = t_follow.id_training INNER JOIN "
                    + "t_location ON t_training.id_location = t_location.id_location INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category " + criteriaSearch() + ""
                    + " group by t_follow.id_trainee) as nn group by bob order by bob desc";
        } else if (criteria.equalsIgnoreCase("Number of trainees by nationality")) {
            return query = "SELECT nationality,count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic " + criteriaSearch() + " GROUP BY nationality";
        } else if (criteria.equalsIgnoreCase("Number of trainees by institution")) {
            return query = "SELECT institution_name,count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON "
                    + "t_trainee.`id_trainee` = t_follow.`id_trainee` INNER JOIN `t_training` t_training ON "
                    + "t_follow.`id_training` = t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` AND t_trainee.`id_trainee` = "
                    + "t_workfor.`id_trainee` INNER JOIN `t_institution` t_institution ON t_workfor.`id_institution` = "
                    + "t_institution.`id_institution` " + criteriaSearch() + " GROUP BY institution_name";
        } else if (criteria.equalsIgnoreCase("Number of trainees by fonction")) {
            return query = "SELECT fonction,count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON "
                    + "t_trainee.`id_trainee` = t_follow.`id_trainee` INNER JOIN `t_training` t_training ON "
                    + "t_follow.`id_training` = t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN "
                    + "t_use ON t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` AND t_trainee.`id_trainee` = "
                    + "t_workfor.`id_trainee` " + criteriaSearch() + " GROUP BY fonction";
        } else if (criteria.equalsIgnoreCase("Number of trainees by department")) {
            return query = "SELECT department,count(distinct t_trainee.id_trainee) FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON "
                    + "t_trainee.`id_trainee` = t_follow.`id_trainee` INNER JOIN `t_training` t_training ON "
                    + "t_follow.`id_training` = t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` AND t_trainee.`id_trainee` = "
                    + "t_workfor.`id_trainee` " + criteriaSearch() + " GROUP BY department";
        } else if (criteria.equalsIgnoreCase("Number of training by month")) {
            return query = "SELECT month(begin_date),count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY month(begin_date)";
        } else if (criteria.equalsIgnoreCase("Number of training by trimester")) {
            return query = "SELECT month(begin_date),count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY month(begin_date)";
        } else if (criteria.equalsIgnoreCase("Number of training by year")) {
            return query = "SELECT year(begin_date),count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY year(begin_date)";
        } else if (criteria.equalsIgnoreCase("Number of training by category")) {
            return query = "SELECT category_name,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY category_name";
        } else if (criteria.equalsIgnoreCase("Number of training by topic")) {
            return query = "SELECT title,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY title";
        } else if (criteria.equalsIgnoreCase("Number of training by thematic")) {
            return query = "SELECT thematic_name,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY thematic_name";
        } else if (criteria.equalsIgnoreCase("Number of training by software")) {
            return query = "SELECT software_name,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "" + criteriaSearch() + " GROUP BY software_name";
        } else if (criteria.equalsIgnoreCase("Number of training by institution")) {
            return query = "SELECT institution_name,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` INNER JOIN `t_institution` t_institution ON "
                    + "t_workfor.`id_institution` = t_institution.`id_institution` " + criteriaSearch() + ""
                    + " GROUP BY institution_name";
        } else if (criteria.equalsIgnoreCase("Number of training by carpe partner")) {
            return query = "SELECT carpe,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` INNER JOIN `t_institution` t_institution ON "
                    + "t_workfor.`id_institution` = t_institution.`id_institution` " + criteriaSearch() + " GROUP BY carpe";
        } else if (criteria.equalsIgnoreCase("Number of training by sponsor")) {
            return query = "SELECT designation,count(distinct t_training.`id_training`) FROM t_training INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN t_support ON "
                    + "t_training.`id_training` = t_support.`id_training` INNER JOIN t_sponsor ON "
                    + "t_sponsor.`id_sponsor` = t_support.`id_sponsor` " + criteriaSearch() + " GROUP BY designation";
        } else {
            return query;
        }
    }

    private String linkQuery(String criteria, String choice) {
        String query = "";
        String val = "";
        if (criteria.equalsIgnoreCase("Number of trainees by month")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "month(t_training.begin_date) = " + Config.monthInt(choice);
        } else if (criteria.equalsIgnoreCase("Number of trainees by trimester")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "month(t_training.begin_date) in (" + getMonthsTrimester(choice) + ")";
        } else if (criteria.equalsIgnoreCase("Number of trainees by year")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "year(t_training.begin_date) = " + choice;
        } else if (criteria.equalsIgnoreCase("Number of trainees by sex")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "t_trainee.sex = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of trainees by frequency")) {
//            query = "SELECT ID from (select t_follow.id_trainee as ID,count(t_follow.id_trainee) as bob "
//                    + "FROM t_training JOIN t_follow ON t_training.id_training = "
//                    + "t_follow.id_training INNER JOIN t_location ON t_training.id_location = t_location.id_location "
//                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
//                    + ""
//                    + "group by t_follow.id_trainee) as bob ";
//            val = "bob = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of trainees by nationality")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON t_trainee.`id_trainee` = "
                    + "t_follow.`id_trainee` INNER JOIN `t_training` t_training ON t_follow.`id_training` = "
                    + "t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "t_trainee.nationality = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of trainees by institution")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON "
                    + "t_trainee.`id_trainee` = t_follow.`id_trainee` INNER JOIN `t_training` t_training ON "
                    + "t_follow.`id_training` = t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` AND t_trainee.`id_trainee` = "
                    + "t_workfor.`id_trainee` INNER JOIN `t_institution` t_institution ON t_workfor.`id_institution` = "
                    + "t_institution.`id_institution` ";
            val = "t_institution.institution_name = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of trainees by fonction")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON "
                    + "t_trainee.`id_trainee` = t_follow.`id_trainee` INNER JOIN `t_training` t_training ON "
                    + "t_follow.`id_training` = t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN "
                    + "t_use ON t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` AND t_trainee.`id_trainee` = "
                    + "t_workfor.`id_trainee` ";
            val = "t_workfor.fonction = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of trainees by department")) {
            query = "SELECT distinct t_trainee.id_trainee FROM `t_trainee` t_trainee INNER JOIN `t_follow` t_follow ON "
                    + "t_trainee.`id_trainee` = t_follow.`id_trainee` INNER JOIN `t_training` t_training ON "
                    + "t_follow.`id_training` = t_training.`id_training` INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` AND t_trainee.`id_trainee` = "
                    + "t_workfor.`id_trainee` ";
            val = "t_workfor.department = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by month")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "month(t_training.begin_date) = " + Config.monthInt(choice);
        } else if (criteria.equalsIgnoreCase("Number of training by trimester")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "month(t_training.begin_date) in (" + getMonthsTrimester(choice) + ")";
        } else if (criteria.equalsIgnoreCase("Number of training by year")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic ";
            val = "year(t_training.begin_date) = " + choice;
        } else if (criteria.equalsIgnoreCase("Number of training by category")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "";
            val = "t_category.category_name = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by topic")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "";
            val = "t_training.title = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by thematic")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "";
            val = "t_thematic.thematic_name = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by software")) {
            query = "SELECT distinct * FROM t_training INNER JOIN t_location on "
                    + "t_location.id_location = t_training.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_use ON t_use.id_training = t_training.id_training "
                    + "INNER JOIN t_software ON t_use.id_software = t_software.id_software "
                    + "INNER JOIN t_content ON t_software.id_software = t_content.id_software "
                    + "INNER JOIN t_thematic ON t_content.id_thematic = t_thematic.id_thematic "
                    + "";
            val = "t_software.software_name = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by institution")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` INNER JOIN `t_institution` t_institution ON "
                    + "t_workfor.`id_institution` = t_institution.`id_institution` ";
            val = "t_institution.institution_name = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by carpe partner")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN `t_workfor` t_workfor ON "
                    + "t_training.`id_training` = t_workfor.`id_training` INNER JOIN `t_institution` t_institution ON "
                    + "t_workfor.`id_institution` = t_institution.`id_institution` ";
            val = "t_institution.carpe = '" + choice + "'";
        } else if (criteria.equalsIgnoreCase("Number of training by sponsor")) {
            query = "SELECT distinct t_training.id_training FROM t_training INNER JOIN t_category ON "
                    + "t_training.id_category = t_category.id_category "
                    + "INNER JOIN t_location on t_location.id_location = t_training.id_location INNER JOIN t_use ON "
                    + "t_use.id_training = t_training.id_training INNER JOIN t_software ON t_use.id_software = "
                    + "t_software.id_software INNER JOIN t_content ON t_software.id_software = t_content.id_software INNER JOIN "
                    + "t_thematic ON t_content.id_thematic = t_thematic.id_thematic INNER JOIN t_support ON "
                    + "t_training.`id_training` = t_support.`id_training` INNER JOIN t_sponsor ON "
                    + "t_sponsor.`id_sponsor` = t_support.`id_sponsor` ";
            val = "t_sponsor.designation = '" + choice + "'";
        } else {
//            query;
        }
        if (criteria.equalsIgnoreCase("Number of trainees by frequency")) {
            String criterias = criteriaSearch();
            query = "SELECT ID from (select t_follow.id_trainee as ID,count(t_follow.id_trainee) as bob "
                    + "FROM t_training JOIN t_follow ON t_training.id_training = "
                    + "t_follow.id_training INNER JOIN t_location ON t_training.id_location = t_location.id_location "
                    + "INNER JOIN t_category ON t_training.id_category = t_category.id_category " + criterias
                    + " group by t_follow.id_trainee) as bob where (bob = '" + choice + "')";
        } else {
            String criterias = criteriaSearch();
            if (!criterias.isEmpty()) {
                query += criterias + " and (" + val + ")";
            } else {
                query += " where (" + val + ")";
            }
        }
        return query;
    }

    private void cleanTable() {
        int nbTable = table.getRowCount();
        while (nbTable > 0) {
            model.removeNewRow();
            nbTable = table.getRowCount();
        }
    }

    private void runQuery(String query) {
        try {
            value = Config.capitalFirstLetter(_tree.getSelectionPath().getLastPathComponent().toString().substring(3));
            cleanTable();
            Model.clearPoints();
            colors = new CategoryRange<>();
            HL.clear();
            if (!query.isEmpty()) {
                Statement stat = Config.con.createStatement();
                Config.res = stat.executeQuery(query);
                int i = 0;
                ArrayList<Integer> vQuarter = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    vQuarter.add(0);
                }
                max = 0;
                while (Config.res.next()) {
                    if (_tree.getSelectionPath().getLastPathComponent().toString().equalsIgnoreCase("By Month")) {
                        if (table.getRowCount() <= i) {
                            model.addNewRow();
                        }
                        table.setValueAt(Config.monthString(Config.res.getInt(1)), i, 0);
                        table.setValueAt("" + Config.res.getInt(2), i, 1);
                        HL.add(new ChartCategory((Object) Config.monthString(Config.res.getInt(1)), new Highlight("hl" + i)));
                        colors.add((ChartCategory) HL.get(i));
                        Model.addPoint(new ChartPoint((ChartCategory) HL.get(i), Config.res.getInt(2)));
                        if (max < Config.res.getInt(2)) {
                            max = Config.res.getInt(2);
                        }
                        i++;
                    } else if (_tree.getSelectionPath().getLastPathComponent().toString().equalsIgnoreCase("By Trimester")) {
                        if (Config.res.getInt(1) == 1 || Config.res.getInt(1) == 2 || Config.res.getInt(1) == 3) {
                            vQuarter.set(0, vQuarter.get(0) + Config.res.getInt(2));
                        } else if (Config.res.getInt(1) == 4 || Config.res.getInt(1) == 5 || Config.res.getInt(1) == 6) {
                            vQuarter.set(1, vQuarter.get(1) + Config.res.getInt(2));
                        } else if (Config.res.getInt(1) == 7 || Config.res.getInt(1) == 8 || Config.res.getInt(1) == 9) {
                            vQuarter.set(2, vQuarter.get(2) + Config.res.getInt(2));
                        } else if (Config.res.getInt(1) == 10 || Config.res.getInt(1) == 11 || Config.res.getInt(1) == 12) {
                            vQuarter.set(3, vQuarter.get(3) + Config.res.getInt(2));
                        }
                    } else {
                        if (table.getRowCount() <= i) {
                            model.addNewRow();
                        }
                        table.setValueAt(Config.res.getString(1), i, 0);
                        table.setValueAt("" + Config.res.getInt(2), i, 1);
                        HL.add(new ChartCategory((Object) Config.res.getString(1), new Highlight("hl" + i)));
                        colors.add((ChartCategory) HL.get(i));
                        Model.addPoint(new ChartPoint((ChartCategory) HL.get(i), Config.res.getInt(2)));
                        if (max < Config.res.getInt(2)) {
                            max = Config.res.getInt(2);
                        }
                        i++;
                    }
                }
                if (_tree.getSelectionPath().getLastPathComponent().toString().equalsIgnoreCase("By Trimester")) {
                    for (int j = 0; j < vQuarter.size(); j++) {
                        if (table.getRowCount() <= j) {
                            model.addNewRow();
                        }
                        table.setValueAt(("Quarter " + (j + 1)), j, 0);
                        table.setValueAt("" + vQuarter.get(j), j, 1);
                        HL.add(new ChartCategory((Object) ("Quarter " + (j + 1)), new Highlight("hl" + j)));
                        colors.add((ChartCategory) HL.get(j));
                        Model.addPoint(new ChartPoint((ChartCategory) HL.get(j), vQuarter.get(j)));
                        if (max < vQuarter.get(j)) {
                            max = vQuarter.get(j);
                        }
                    }
                }
            }

            TableUtils.autoResizeAllColumns(table);
            if (!BBarChart.isEnabled()) {
                createBarChart();
            } else if (!BPieChart.isEnabled()) {
                createPieChart();
            } else if (!BLineChart.isEnabled()) {
                createLineChart();
            } else if (!BTable.isEnabled()) {
                createTable();
            }
            panChart.repaint();
        } catch (SQLException ex) {
            JXErrorPane.showDialog(DMTWorkbench.frame, new ErrorInfo("Fatal error", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private String criteriaSearch() {
        String where = "";
        String category = "";
        String thematic = "";
        String software = "";
        String location = "";
        String month = "";
        String trimester = "";
        String semester = "";
        String year = "";
        if (CBCategory.getSelectedObjects().length != 0) {
            category = " AND (category_name in (" + manyCriteria(CBCategory.getSelectedObjects()) + "))";
        } else {
            category = "";
        }
        if (CBSoftware.isEnabled()) {
            if (CBThematic.getSelectedObjects().length != 0) {
                thematic = " AND (thematic_name in (" + manyCriteria(CBThematic.getSelectedObjects()) + "))";
            } else {
                thematic = "";
            }
        }
        if (CBSoftware.isEnabled()) {
            if (CBSoftware.getSelectedObjects().length != 0) {
                software = " AND (software_name in (" + manyCriteria(CBSoftware.getSelectedObjects()) + "))";
            } else {
                software = "";
            }
        }
        if (CBLocation.getSelectedObjects().length != 0) {
            location = " AND (location_name in (" + manyCriteria(CBLocation.getSelectedObjects()) + "))";
        } else {
            location = "";
        }
        if (CBYear.getSelectedObjects().length != 0) {
            year = " AND (year(begin_date) in (" + manyCriteriaYear(CBYear.getSelectedObjects()) + "))";
        } else {
            year = "";
        }
        if (CBMonth.getSelectedObjects().length != 0) {
            month = " AND (month(begin_date) in (" + manyCriteriaMonth(CBMonth.getSelectedObjects()) + "))";
        } else {
            month = "";
        }
        if (CBTrimester.getSelectedObjects().length != 0) {
            trimester = " AND (month(begin_date) in (" + manyCriteriaQuarter(CBTrimester.getSelectedObjects()) + "))";
        } else {
            trimester = "";
        }
        if (CBSemester.getSelectedObjects().length != 0) {
            semester = " AND (month(begin_date) in (" + manyCriteriaSemester(CBSemester.getSelectedObjects()) + "))";
        } else {
            semester = "";
        }
        where = category + thematic + software + location + month + trimester + semester + year;
        if (where.startsWith(" AND")) {
            where = where.substring(4);
        }
        if (!where.isEmpty()) {
            where = "WHERE " + where;
        }
        return where;
    }

    private String manyCriteria(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "\'" + Config.correctText(list[i].toString()) + "\',";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String manyCriteriaYear(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "" + list[i].toString() + ",";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String manyCriteriaSemester(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "" + getMonthsSemester(list[i].toString()) + ",";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String manyCriteriaQuarter(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "" + getMonthsTrimester(list[i].toString()) + ",";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String manyCriteriaMonth(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "" + Config.monthInt(list[i].toString()) + ",";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String manyMonths(String list) {
        String values = "";
        if (list.contains(";")) {
            String tab[] = list.split(";");
            for (String s : tab) {
                values += "" + Config.monthInt(s) + ",";
            }
            values = values.substring(0, values.length() - 1);
        } else {
            return list = "" + Config.monthInt(list) + "";
        }
        return values;
    }

    private String getMonthsTrimester(String trimester) {
        if (trimester.contains("Quarter 1")) {
            return manyMonths("January;February;March");
        } else if (trimester.contains("Quarter 2")) {
            return manyMonths("April;May;June");
        } else if (trimester.contains("Quarter 3")) {
            return manyMonths("July;August;September");
        } else {
            return manyMonths("October;November;December");
        }
    }

    private String getMonthsSemester(String semester) {
        if (semester.contains("Semester 1")) {
            return manyMonths("January;February;March;April;May;June");
        } else {
            return manyMonths("July;August;September;October;November;December");
        }
    }

    private void enableResetButton() {
        if (CBCategory.getSelectedObjects().length != 0 || CBSoftware.getSelectedObjects().length != 0
                || CBThematic.getSelectedObjects().length != 0 || CBLocation.getSelectedObjects().length != 0
                || CBMonth.getSelectedObjects().length != 0 || CBYear.getSelectedObjects().length != 0
                || CBSemester.getSelectedObjects().length != 0 || CBTrimester.getSelectedObjects().length != 0
                || panChart.getComponentCount() > 0) {
            BReset.setEnabled(true);
            BPrint.setEnabled(true);
        } else {
            BReset.setEnabled(false);
            BPrint.setEnabled(false);
        }
    }

    private void loadComboxWithAll(CheckBoxListComboBox cb, ResultSet res, int field) throws SQLException {
        DefaultComboBoxModel modelC = new DefaultComboBoxModel();
        modelC.insertElementAt(CheckBoxList.ALL, 0);
        int index = 1;
        while (res.next()) {
            modelC.insertElementAt(res.getString(field), index);
            index++;
        }
        cb.setModel(modelC);
        cb.setSelectedIndex(-1);
    }

    private void loadComboBoxes() {
        try {
            PreparedStatement ps = null;
//            ps = Config.con.prepareStatement("select distinct id_category,category_name from t_category "
//                    + "where id_category in (select id_category from t_training)");
//            Config.res = ps.executeQuery();
//            loadComboxWithAll(CBCategory, Config.res, 2);
//
//            ps = Config.con.prepareStatement("select distinct id_thematic, thematic_name from "
//                    + "t_thematic where id_thematic in (select id_thematic from t_content)");
//            Config.res = ps.executeQuery();
//            loadComboxWithAll(CBThematic, Config.res, 2);
//
//            ps = Config.con.prepareStatement("select distinct id_software, software_name from "
//                    + "t_software where id_software in (select id_software from t_use)");
//            Config.res = ps.executeQuery();
//            loadComboxWithAll(CBSoftware, Config.res, 2);
//
//            ps = Config.con.prepareStatement("select distinct id_location, location_name from "
//                    + "t_location where id_location in (select id_location from t_training)");
//            Config.res = ps.executeQuery();
//            loadComboxWithAll(CBLocation, Config.res, 2);

            ps = Config.con.prepareStatement("select distinct year(request_date) from dmt_delivery order by year(request_date)");
            Config.res = ps.executeQuery();
            loadComboxWithAll(CBYear, Config.res, 1);

            ps = Config.con.prepareStatement("select distinct month(request_date) from dmt_delivery order by month(request_date)");
            Config.res = ps.executeQuery();
            DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();
            modelCombo.insertElementAt(CheckBoxList.ALL, 0);
            int index = 1;
            while (Config.res.next()) {
                modelCombo.insertElementAt(Config.monthString(Config.res.getInt(1)), index);
                index++;
            }
            CBMonth.setModel(modelCombo);
            CBMonth.setSelectedIndex(-1);

            DefaultComboBoxModel model2 = new DefaultComboBoxModel();
            model2.insertElementAt(CheckBoxList.ALL, 0);
            for (int i = 1; i < 5; i++) {
                model2.insertElementAt("Quarter " + i, i);
            }
            CBTrimester.setModel(model2);
            CBTrimester.setSelectedIndex(-1);

            DefaultComboBoxModel model3 = new DefaultComboBoxModel();
            model3.insertElementAt(CheckBoxList.ALL, 0);
            for (int i = 1; i < 3; i++) {
                model3.insertElementAt("Semester " + i, i);
            }
            CBSemester.setModel(model3);
            CBSemester.setSelectedIndex(-1);
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
        }
    }

//    private static void createColor() {
//        Color col;
//        do {
//            col = rand.randomColor();
//        } while (ColorChart.contains(col));
//        ColorChart.add(col);
//    }
    private void createTable() {
        panChart.removeAll();
        pan.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialGradientLineBorder(new Color[]{new Color(0, 0, 128), UIDefaultsLookup.getColor("control"
                    + "")}, 2, PartialSide.NORTH), "<html><b>" + titledChart() + "</b></html>",
                TitledBorder.CENTER, TitledBorder.ABOVE_TOP), BorderFactory.createEmptyBorder(3, 0, 0, 0)));
        panChart.add(pan);
        panChart.repaint();
    }

    private void CreerExcel(File file) {
        try {
            WritableWorkbook wbb;
            WritableSheet sheet;
            Label label;
            wbb = Workbook.createWorkbook(file);
            String feuille = value;
            sheet = wbb.createSheet(feuille, 0);
            int p = 0;
            for (int gh = 0; gh < table.getColumnCount(); gh++) {
                p = gh;
                if (p < table.getColumnCount()) {
                    label = new Label(gh, 0, table.getColumnName(p));
                    sheet.addCell(label);
                    p++;
                }
            }
            int a = 0;
            int b = 0;
            for (int i = 0; i < table.getColumnCount(); i++) {
                b = 0;
                for (int j = 1; j < table.getRowCount() + 1; j++) {
                    if (a < table.getColumnCount()) {
                        label = new Label(i, j, table.getValueAt(b, a).toString());
                        sheet.addCell(label);
                        b++;
                    }
                }
                a++;
            }
            wbb.write();
            wbb.close();
            JOptionPane.showMessageDialog(this, "Exportation has been done succesfully ..."
                    + "", "Confirm", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | WriteException e) {
            JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
        }
    }

    private void createBarChart() {
        panChart.removeAll();
        Axis xAxis = new CategoryAxis(colors);
        Axis yAxis = new Axis(new NumericRange(0, max + 3));
//        yAxis.setLabel("values");
        chart = new Chart(Model);
        chart.setBackground(Color.white);
        chart.setTitle(titledChart());
        ChartStyle style = new ChartStyle();
        style.setBarsVisible(true);
        style.setLinesVisible(false);
        chart.setStyle(Model, style);
        chart.setGridColor(new Color(150, 150, 150));
        chart.setChartBackground(new GradientPaint(0f, 0f, Color.lightGray.brighter(), 300f, 300f, Color.lightGray));
        chart.setLayout(new BorderLayout());
//        chart.setBarGap(10);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setHorizontalGridLinesVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.BarChBHLine, true));
        chart.setVerticalGridLinesVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.BarChBVLine, false));
        chart.setRolloverEnabled(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBRollover, false));
        chart.setShadowVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBShadow, true));
        chart.setSelectionEnabled(true);
        chart.setSelectionShowsOutline(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBSelectionOutline, false));
        for (int i = 0; i < HL.size(); i++) {
            if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RandomColor, true)) {
//                if (i < ColorChart.size()) {
                chart.setHighlightStyle(((ChartCategory) HL.get(i)).getHighlight(), new ChartStyle(rand.randomColor()).withBars());
//                } else {
////                    createColor();
//                    chart.setHighlightStyle(((ChartCategory) HL.get(i)).getHighlight(), new ChartStyle(ColorChart.get(i)).withBars());
//                }
            } else {
                chart.setHighlightStyle(((ChartCategory) HL.get(i)).getHighlight(), new ChartStyle(getColorFromKey(Config.pref.get(SettingKeyFactory.ChartFeatures.CBColor2, "0, 204, 0"))).withBars());
            }
        }
//        chart.addMousePanner().addMouseZoomer();
        if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RB3D, false)) {
            CylinderBarRenderer barR = new CylinderBarRenderer();
            barR.setAlwaysShowOutlines(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
            barR.setOutlineWidth(outlineWidth);
            chart.setBarRenderer(barR);
            chart.getXAxis().setAxisRenderer(new Axis3DRenderer());
        } else if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBRaised, false)) {
            RaisedBarRenderer barRenderer = new RaisedBarRenderer();
            barRenderer.setOutlineWidth(outlineWidth);
            barRenderer.setAlwaysShowOutlines(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
            chart.setBarRenderer(barRenderer);
            chart.getXAxis().setAxisRenderer(new NoAxisRenderer());
        } else {
            DefaultBarRenderer barRenderer = new DefaultBarRenderer();
            barRenderer.setOutlineWidth(outlineWidth);
            barRenderer.setAlwaysShowOutlines(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
            chart.setBarRenderer(barRenderer);
            chart.getXAxis().setAxisRenderer(new NoAxisRenderer());
        }
        chart.setBarResizePolicy(BarResizePolicy.RESIZE_OFF);
        chart.setBarGap(10);
        panner = new MouseDragPanner(chart, true, false);
        panner.setContinuous(true);
        chart.addMouseListener(panner);
        chart.addMouseMotionListener(panner);
        chart.addDrawable(new PanIndicator(chart, PanIndicator.Placement.LEFT));
        chart.addDrawable(new PanIndicator(chart, PanIndicator.Placement.RIGHT));
        panChart.add(chart);
        panChart.repaint();
    }

    private void createPieChart() {
        panChart.removeAll();
        stylePieChart = new ChartStyle();
        chart = new Chart(Model);
        chart.setBarGap(10);
        chart.setBackground(Color.white);
        if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RB3D, false)) {
            chart.setPieSegmentRenderer(new Pie3DRenderer());
            Pie3DRenderer pSeg = (Pie3DRenderer) chart.getPieSegmentRenderer();
            pSeg.setAlwaysShowOutlines(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
            pSeg.setOutlineWidth(outlineWidth);
        } else if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBRaised, false)) {
            chart.setPieSegmentRenderer(new RaisedPieSegmentRenderer());
            RaisedPieSegmentRenderer pSeg = (RaisedPieSegmentRenderer) chart.getPieSegmentRenderer();
            pSeg.setAlwaysShowOutlines(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
            pSeg.setOutlineWidth(outlineWidth);
        } else {
            chart.setPieSegmentRenderer(new DefaultPieSegmentRenderer());
            DefaultPieSegmentRenderer pSeg = (DefaultPieSegmentRenderer) chart.getPieSegmentRenderer();
            pSeg.setAlwaysShowOutlines(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
            pSeg.setOutlineWidth(outlineWidth);
        }
        stylePieChart.setPieOffsetAngle(Config.pref.getInt(SettingKeyFactory.ChartFeatures.SLAngle, 0));
        chart.setTitle(titledChart());
        chart.setChartType(ChartType.PIE);
        chart.setStyle(Model, stylePieChart);
        chart.setSelectionEnabled(true);
        chart.setRolloverEnabled(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBRollover, false));
        chart.setSelectionShowsExplodedSegments(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBExplodedSegment, false));

        AbstractPieSegmentRenderer renderer = (AbstractPieSegmentRenderer) chart.getPieSegmentRenderer();
        PieLabelRenderer labelRenderer;
        if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBLineLabel, true)) {
            labelRenderer = new LinePieLabelRenderer();
        } else if (Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBSimpleLabel, false)) {
            labelRenderer = new SimplePieLabelRenderer();
        } else {
            labelRenderer = null;
        }
        renderer.setPieLabelRenderer(labelRenderer);
        for (int i = 0; i < HL.size(); i++) {
//            if (i < ColorChart.size()) {
            chart.setHighlightStyle(((ChartCategory) HL.get(i)).getHighlight(), new ChartStyle(rand.randomColor()).withBars());
//            } else {
////                createColor();
//                chart.setHighlightStyle(((ChartCategory) HL.get(i)).getHighlight(), new ChartStyle(ColorChart.get(i)).withBars());
//            }
        }
        chart.setShadowVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBShadow, true));
        chart.setSelectionShowsOutline(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBSelectionOutline, false));
        panChart.add(chart);
        panChart.repaint();
    }

    private void createLineChart() {
        panChart.removeAll();
        Axis xAxis = new CategoryAxis(colors);
        Axis yAxis = new Axis(new NumericRange(0, max + 3));
        ChartStyle style = new ChartStyle(getColorFromKey(Config.pref.get(SettingKeyFactory.ChartFeatures.CBColor, "255, 0, 0")), true, true);
        style.setLineWidth(Config.pref.getInt(SettingKeyFactory.ChartFeatures.SPLineWidth, 2));
        chart = new Chart();
        chart.setBackground(Color.white);
        chart.setTitle(titledChart());
        chart.setGridColor(new Color(150, 150, 150));
        style.setPointSize(12);
        chart.addModel(Model, style).setPointRenderer(new SphericalPointRenderer());
        chart.addMousePanner().addMouseZoomer();
        chart.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    rollover(e);
                } catch (Exception ex) {
                }
            }

            private void rollover(MouseEvent e) {
                Point p = e.getPoint();
                PointSelection selection = chart.nearestPoint(p, Model);
                Chartable selected = selection.getSelected();
                Point2D selectedCoords = new Point2D.Double(selected.getX().position(), selected.getY().position());
                Point dp = chart.calculatePixelPoint(selectedCoords);
                if (p.distance(dp) < 20) {
                    ChartCategory<?> x = (ChartCategory<?>) selected.getX();
                    chart.setToolTipText(String.format("%s : %.0f", x.getName(), selected.getY().position()));
                } else {
                    chart.setToolTipText(null);
                }
                chart.repaint();
            }
        });
        chart.setChartBackground(new GradientPaint(0f, 0f, Color.lightGray.brighter(), 300f, 300f, Color.lightGray));
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setHorizontalGridLinesVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.LineChBHLine, true));
        chart.setVerticalGridLinesVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.LineChBVLine, true));
        chart.setSelectionEnabled(true);
        chart.setRolloverEnabled(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBRollover, false));
        chart.setShadowVisible(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBShadow, true));
        chart.setBarResizePolicy(BarResizePolicy.RESIZE_OFF);
        chart.setBarGap(10);
        panner = new MouseDragPanner(chart, true, false);
        panner.setContinuous(true);
        chart.addMouseListener(panner);
        chart.addMouseMotionListener(panner);
        chart.addDrawable(new PanIndicator(chart, PanIndicator.Placement.LEFT));
        chart.addDrawable(new PanIndicator(chart, PanIndicator.Placement.RIGHT));
        panChart.add(chart);
        panChart.repaint();
    }

    private class MyTableModel extends AbstractTableModel {

        String[] columnNames = {value, "Value"};
        ArrayList[] Data;

        public MyTableModel() {
            Data = new ArrayList[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                Data[i] = new ArrayList();
            }
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return Data[0].size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return Data[col].get(row);
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return (false);
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            Data[col].set(row, value);
            fireTableCellUpdated(row, col);
        }

        public void addNewRow() {
            for (int i = 0; i < columnNames.length; i++) {
                Data[i].add("");
            }
            this.fireTableRowsInserted(0, Data[0].size() - 1);
        }

        public void removeNewRow() {
            for (int i = 0; i < columnNames.length; i++) {
                Data[i].remove(Data[i].size() - 1);
            }
            this.fireTableRowsDeleted(0, Data[0].size() - 1);
        }

        public void removeNewRow(int index) {
            for (int i = 0; i < columnNames.length; i++) {
                Data[i].remove(index);
            }
            this.fireTableRowsDeleted(0, Data[0].size() - 1);
        }
    }

    private static Color getColorFromKey(String value) {
        String tab[] = value.split(", ");
        return new Color(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton BBarChart;
    private javax.swing.JButton BClipboard;
    private javax.swing.ButtonGroup BGPeriod;
    public static javax.swing.JButton BLineChart;
    public static javax.swing.JButton BPieChart;
    private javax.swing.JButton BRefresh;
    private javax.swing.JButton BSetting;
    private javax.swing.JButton BTable;
    private com.jidesoft.combobox.CheckBoxListComboBox CBCategory;
    private com.jidesoft.combobox.CheckBoxListComboBox CBLocation;
    private com.jidesoft.combobox.CheckBoxListComboBox CBMonth;
    private com.jidesoft.combobox.CheckBoxListComboBox CBSemester;
    private com.jidesoft.combobox.CheckBoxListComboBox CBSoftware;
    private com.jidesoft.combobox.CheckBoxListComboBox CBThematic;
    private com.jidesoft.combobox.CheckBoxListComboBox CBTrimester;
    private com.jidesoft.combobox.CheckBoxListComboBox CBYear;
    private javax.swing.JRadioButton RBMonth;
    private javax.swing.JRadioButton RBSemester;
    private javax.swing.JRadioButton RBTrimester;
    private javax.swing.JToolBar TB1;
    private com.jidesoft.tree.QuickTreeFilterField _field;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar8;
    private static javax.swing.JPanel panChart;
    // End of variables declaration//GEN-END:variables
    int max = 0;
    JideSplitButton BPrint;
    JideButton BClose, BReset;
    DefaultChartModel Model = new DefaultChartModel();
    CategoryRange<ChartCategory> colors = new CategoryRange<>();
    private final float outlineWidth = 3f;
    ArrayList HL = new ArrayList();
    ArrayList<Color> ColorChart = new ArrayList<>();
    public static ChartStyle stylePieChart;
    RandomColor rand = new RandomColor();
    public static Chart chart;
    String dirIcon = "";
    File repTemp;
    SortableTable table;
    public static JTree _tree;
    JPanel pan;
    MyTableModel model;
    String value = "";
    String tampon = new File(new JFileChooser().getCurrentDirectory().getAbsolutePath()).getParent();
    private MouseDragPanner panner;
}
