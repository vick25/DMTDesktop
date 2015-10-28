package com.osfac.dmt.tools.request;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.grid.HierarchicalTableModel;
import com.jidesoft.grid.ListSelectionModelGroup;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeLikeHierarchicalPanel;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.InfiniteProgressPanel;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.Overlayable;
import com.jidesoft.swing.OverlayableUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.download.DownloadData;
import com.osfac.dmt.form.DataRequestFormedit;
import com.osfac.dmt.form.ReviewDataRequestForm;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class DataRequestManager extends javax.swing.JPanel {

    public DataRequestManager() {
        initComponents(I18N.DMTResourceBundle);
        table = new HierarchicalTable();
        table.setTableStyleProvider(new RowStripeTableStyleProvider(
                new Color[]{Config.getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color1, ""
                            + "253, 253, 244")), Config.getColorFromKey(Config.pref
                            .get(SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setComponentFactory(new HierarchicalTableComponentFactory() {
            @Override
            public Component createChildComponent(HierarchicalTable table, Object value, int row) {
                if (value == null) {
                    return new JPanel();
                } else {
                    return createPanel(((TableModel) value), row);
                }
            }

            private JComponent createPanel(final TableModel model, final int row) {
                TableModel emptyTableModel = new TableModel() {
                    @Override
                    public int getRowCount() {
                        return 0;
                    }

                    @Override
                    public int getColumnCount() {
                        return model.getColumnCount();
                    }

                    @Override
                    public String getColumnName(int columnIndex) {
                        return model.getColumnName(columnIndex);
                    }

                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return model.getColumnClass(columnIndex);
                    }

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }

                    @Override
                    public Object getValueAt(int rowIndex, int columnIndex) {
                        return null;
                    }

                    @Override
                    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                    }

                    @Override
                    public void addTableModelListener(TableModelListener l) {
                    }

                    @Override
                    public void removeTableModelListener(TableModelListener l) {
                    }
                };
                SortableTable sortableTable = new SortableTable(emptyTableModel) {
                    @Override
                    public void scrollRectToVisible(Rectangle aRect) {
//                        scrollRectToVisible(this, aRect);
                    }
                };
                _group.add(sortableTable.getSelectionModel());
                TreeLikeHierarchicalPanel treeLikeHierarchicalPanel = new TreeLikeHierarchicalPanel(new FitScrollPane(sortableTable));
                treeLikeHierarchicalPanel.setBackground(sortableTable.getMarginBackground());
                DefaultOverlayable overlayable = new DefaultOverlayable(treeLikeHierarchicalPanel);
                InfiniteProgressPanel progressPanel = new InfiniteProgressPanel() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(20, 20);
                    }
                };
                overlayable.addOverlayComponent(progressPanel);
                progressPanel.start();
                overlayable.setOverlayVisible(true);
                _tables.put(row, sortableTable);
                _tableModels.put(row, model);
                sortableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                sortableTable.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{Config.getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")), Config.getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
//                sortableTable.getColumnModel().getColumn(0).setMaxWidth(30);
                TableUtils.autoResizeAllColumns(sortableTable);
                TableUtils.autoResizeAllRows(sortableTable);
                Config.centerTableHeadAndBold(sortableTable);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000); // you could use this thread to calculate your table model.
                        } catch (InterruptedException ex) {
                            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                final SortableTable internalTable = (SortableTable) _tables.get(row);
                                Container parent = internalTable.getParent();
                                while (parent != null && !(parent instanceof TreeLikeHierarchicalPanel)) {
                                    parent = parent.getParent();
                                }
                                if (parent != null) {
                                    Overlayable overlayable = OverlayableUtils.getOverlayable((JComponent) parent);
                                    overlayable.setOverlayVisible(false);
                                    JComponent[] overlayComponents = overlayable.getOverlayComponents();
                                    for (JComponent comp : overlayComponents) {
                                        if (comp instanceof InfiniteProgressPanel) {
                                            ((InfiniteProgressPanel) comp).stop();
                                        }
                                    }
                                }
                                internalTable.setColumnResizable(true);
                                internalTable.setModel(_tableModels.get(row));
                                internalTable.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        try {
                                            if (!internalTable.getValueAt(internalTable.getSelectedRow(), 1).toString().equals("")) {
                                                enabledButton(true);
                                                indexParent = row;
                                            } else {
                                                enabledButton(false);
                                            }
                                        } catch (Exception ex) {
                                            enabledButton(false);
                                        }
                                    }
                                });
                                TableUtils.autoResizeAllColumns(internalTable);
                                TableUtils.autoResizeAllRows(internalTable);
                                table.doLayout();
                            }
                        });
                    }
                };
                thread.start();
                return overlayable;
            }

            @Override
            public void destroyChildComponent(HierarchicalTable table, Component component, int row) {
                Component t = JideSwingUtilities.getFirstChildOf(JTable.class, component);
                if (t instanceof JTable) {
                    _group.remove(((JTable) t).getSelectionModel());
                }
                _destroyedCount++;
            }
        });
        _group.add(table.getSelectionModel());
        table.getTableHeader().setPreferredSize(new Dimension(0, 0));
        Config.centerTableHeadAndBold(table);
        setCellStyle();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BDownload.setEnabled(false);
                BReviewForm.setEnabled(false);
                BDelete.setEnabled(false);
                BUpdate.setEnabled(false);
            }
        });
        if (table.getRowCount() > 0) {
            table.expandRow(0);
        }
//////        table.requestFocus();
//////        ScrllTable.setViewportView(table);
        waitFilling();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        BDownload = new com.jidesoft.swing.JideButton();
        BReviewForm = new com.jidesoft.swing.JideButton();
        BUpdate = new com.jidesoft.swing.JideButton();
        BDelete = new com.jidesoft.swing.JideButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        BRefresh = new com.jidesoft.swing.JideButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        BSynchronize = new com.jidesoft.swing.JideButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        BStatus = new com.jidesoft.swing.JideButton();
        ScrllTable = new javax.swing.JScrollPane();
        LBWait = new org.jdesktop.swingx.JXBusyLabel();
        filterField = new com.jidesoft.grid.QuickTableFilterField();

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setRollover(true);

        BDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager(7).png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        BDownload.setText(bundle.getString("DataRequestManager.BDownload.text")); // NOI18N
        BDownload.setToolTipText(bundle.getString("DataRequestManager.BDownload.toolTipText")); // NOI18N
        BDownload.setEnabled(false);
        BDownload.setFocusable(false);
        BDownload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDownload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDownloadActionPerformed(evt);
            }
        });
        jToolBar1.add(BDownload);

        BReviewForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/view_multicolumn(10).png"))); // NOI18N
        BReviewForm.setText(bundle.getString("DataRequestManager.BReviewForm.text")); // NOI18N
        BReviewForm.setEnabled(false);
        BReviewForm.setFocusable(false);
        BReviewForm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BReviewForm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BReviewForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BReviewFormActionPerformed(evt);
            }
        });
        jToolBar1.add(BReviewForm);

        BUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download(8).png"))); // NOI18N
        BUpdate.setText(bundle.getString("DataRequestManager.BUpdate.text")); // NOI18N
        BUpdate.setToolTipText(bundle.getString("DataRequestManager.BUpdate.toolTipText")); // NOI18N
        BUpdate.setEnabled(false);
        BUpdate.setFocusable(false);
        BUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUpdateActionPerformed(evt);
            }
        });
        jToolBar1.add(BUpdate);

        BDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/dialog_close(2).png"))); // NOI18N
        BDelete.setText(bundle.getString("DataRequestManager.BDelete.text")); // NOI18N
        BDelete.setToolTipText(bundle.getString("DataRequestManager.BDelete.toolTipText")); // NOI18N
        BDelete.setEnabled(false);
        BDelete.setFocusable(false);
        BDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(BDelete);
        jToolBar1.add(jSeparator2);

        BRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-clear.png"))); // NOI18N
        BRefresh.setText(bundle.getString("DataRequestManager.BRefresh.text")); // NOI18N
        BRefresh.setToolTipText(bundle.getString("DataRequestManager.BRefresh.toolTipText")); // NOI18N
        BRefresh.setFocusable(false);
        BRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(BRefresh);
        jToolBar1.add(jSeparator1);

        BSynchronize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/refresh2.png"))); // NOI18N
        BSynchronize.setText(bundle.getString("DataRequestManager.BSynchronize.text")); // NOI18N
        BSynchronize.setToolTipText(bundle.getString("DataRequestManager.BSynchronize.toolTipText")); // NOI18N
        BSynchronize.setFocusable(false);
        BSynchronize.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSynchronize.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSynchronize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSynchronizeActionPerformed(evt);
            }
        });
        jToolBar1.add(BSynchronize);
        jToolBar1.add(jSeparator3);

        BStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/gtk-info(3).png"))); // NOI18N
        BStatus.setText(bundle.getString("DataRequestManager.BStatus.text")); // NOI18N
        BStatus.setToolTipText(bundle.getString("DataRequestManager.BStatus.toolTipText")); // NOI18N
        BStatus.setEnabled(false);
        BStatus.setFocusable(false);
        BStatus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BStatus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BStatusActionPerformed(evt);
            }
        });
        jToolBar1.add(BStatus);

        jToolBar2.add(jToolBar1);

        LBWait.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LBWait.setText(bundle.getString("DataRequestManager.LBWait.text")); // NOI18N
        LBWait.setBusy(true);
        ScrllTable.setViewportView(LBWait);

        filterField.setHintText(bundle.getString("DataRequestManager.filterField.hintText")); // NOI18N
        filterField.setShowMismatchColor(true);
        filterField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                filterFieldFocusGained(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(ScrllTable)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrllTable, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        BDownload = new com.jidesoft.swing.JideButton();
        BReviewForm = new com.jidesoft.swing.JideButton();
        BUpdate = new com.jidesoft.swing.JideButton();
        BDelete = new com.jidesoft.swing.JideButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        BRefresh = new com.jidesoft.swing.JideButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        BSynchronize = new com.jidesoft.swing.JideButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        BStatus = new com.jidesoft.swing.JideButton();
        ScrllTable = new javax.swing.JScrollPane();
        LBWait = new org.jdesktop.swingx.JXBusyLabel();
        filterField = new com.jidesoft.grid.QuickTableFilterField();

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setRollover(true);

        BDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager(7).png"))); // NOI18N

        BDownload.setText(bundle.getString("DataRequestManager.BDownload.text")); // NOI18N
        BDownload.setToolTipText(bundle.getString("DataRequestManager.BDownload.toolTipText")); // NOI18N
        BDownload.setEnabled(false);
        BDownload.setFocusable(false);
        BDownload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDownload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDownloadActionPerformed(evt);
            }
        });
        jToolBar1.add(BDownload);

        BReviewForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/view_multicolumn(10).png"))); // NOI18N
        BReviewForm.setText(bundle.getString("DataRequestManager.BReviewForm.text")); // NOI18N
        BReviewForm.setEnabled(false);
        BReviewForm.setFocusable(false);
        BReviewForm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BReviewForm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BReviewForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BReviewFormActionPerformed(evt);
            }
        });
        jToolBar1.add(BReviewForm);

        BUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download(8).png"))); // NOI18N
        BUpdate.setText(bundle.getString("DataRequestManager.BUpdate.text")); // NOI18N
        BUpdate.setToolTipText(bundle.getString("DataRequestManager.BUpdate.toolTipText")); // NOI18N
        BUpdate.setEnabled(false);
        BUpdate.setFocusable(false);
        BUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUpdateActionPerformed(evt);
            }
        });
        jToolBar1.add(BUpdate);

        BDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/dialog_close(2).png"))); // NOI18N
        BDelete.setText(bundle.getString("DataRequestManager.BDelete.text")); // NOI18N
        BDelete.setToolTipText(bundle.getString("DataRequestManager.BDelete.toolTipText")); // NOI18N
        BDelete.setEnabled(false);
        BDelete.setFocusable(false);
        BDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(BDelete);
        jToolBar1.add(jSeparator2);

        BRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-clear.png"))); // NOI18N
        BRefresh.setText(bundle.getString("DataRequestManager.BRefresh.text")); // NOI18N
        BRefresh.setToolTipText(bundle.getString("DataRequestManager.BRefresh.toolTipText")); // NOI18N
        BRefresh.setFocusable(false);
        BRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(BRefresh);
        jToolBar1.add(jSeparator1);

        BSynchronize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/refresh2.png"))); // NOI18N
        BSynchronize.setText(bundle.getString("DataRequestManager.BSynchronize.text")); // NOI18N
        BSynchronize.setToolTipText(bundle.getString("DataRequestManager.BSynchronize.toolTipText")); // NOI18N
        BSynchronize.setFocusable(false);
        BSynchronize.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSynchronize.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSynchronize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSynchronizeActionPerformed(evt);
            }
        });
        jToolBar1.add(BSynchronize);
        jToolBar1.add(jSeparator3);

        BStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/gtk-info(3).png"))); // NOI18N
        BStatus.setText(bundle.getString("DataRequestManager.BStatus.text")); // NOI18N
        BStatus.setToolTipText(bundle.getString("DataRequestManager.BStatus.toolTipText")); // NOI18N
        BStatus.setEnabled(false);
        BStatus.setFocusable(false);
        BStatus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BStatus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BStatusActionPerformed(evt);
            }
        });
        jToolBar1.add(BStatus);

        jToolBar2.add(jToolBar1);

        LBWait.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LBWait.setText(bundle.getString("DataRequestManager.LBWait.text")); // NOI18N
        LBWait.setBusy(true);
        ScrllTable.setViewportView(LBWait);

        filterField.setHintText(bundle.getString("DataRequestManager.filterField.hintText")); // NOI18N
        filterField.setShowMismatchColor(true);
        filterField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                filterFieldFocusGained(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                .addComponent(ScrllTable));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ScrllTable, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)));
    }// </editor-fold>

    private void BDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDownloadActionPerformed
        if (WorkbenchFrame.BSConnect.isEnabled()) {
            JOptionPane.showMessageDialog(DMTWorkbench.frame, I18N.get("GeoResult.message-server-connection-error"),
                    I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                JTable internalTable = _tables.get(indexParent);
                int idDelivery = Integer.parseInt(internalTable.getValueAt(internalTable.getSelectedRow(), 0).toString());
                new DownloadData(getIDsImage(idDelivery), idDelivery).setVisible(true);
            } catch (SQLException ex) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        ex.getMessage(), null, null, ex, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BDownloadActionPerformed

    private void BRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRefreshActionPerformed
        enabledButton(false);
        waitFilling(LBWait);
    }//GEN-LAST:event_BRefreshActionPerformed

    private void BReviewFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BReviewFormActionPerformed
        JTable internalTable = _tables.get(indexParent);
        int idDelivery = Integer.parseInt(internalTable.getValueAt(internalTable.getSelectedRow(), 0).toString());
        new ReviewDataRequestForm(DMTWorkbench.frame, true, idDelivery).setVisible(true);
    }//GEN-LAST:event_BReviewFormActionPerformed

    private void BSynchronizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSynchronizeActionPerformed
        if (WorkbenchFrame.dataRequestSync != null) {
            WorkbenchFrame.dataRequestSync.setVisible(true);
        } else {
            WorkbenchFrame.dataRequestSync = new DataRequestSync(DMTWorkbench.frame, true);
            WorkbenchFrame.dataRequestSync.setVisible(true);
        }
    }//GEN-LAST:event_BSynchronizeActionPerformed

    private void BDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDeleteActionPerformed
        JTable internalTable = _tables.get(indexParent);
        int idDelivery = Integer.parseInt(internalTable.getValueAt(internalTable.getSelectedRow(), 0).toString());
        if (JOptionPane.showConfirmDialog(DMTWorkbench.frame, I18N.get("DataRequestManager.delete-data-request-confirm"), I18N.get("Text.Confirm"), 0) == 0) {
            try {
                PreparedStatement ps = Config.con.prepareStatement("DELETE FROM dmt_requester "
                        + "WHERE id_requester = ?");
                ps.setInt(1, getIdRequester(idDelivery));
                int result = ps.executeUpdate();
                BRefresh.doClick();
            } catch (SQLException ex) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        ex.getMessage(), null, null, ex, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BDeleteActionPerformed

    private void BUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUpdateActionPerformed
        JTable internalTable = _tables.get(indexParent);
        int idDelivery = Integer.parseInt(internalTable.getValueAt(internalTable.getSelectedRow(), 0).toString());
        new DataRequestFormedit(DMTWorkbench.frame, true, idDelivery).setVisible(true);
    }//GEN-LAST:event_BUpdateActionPerformed

    private void filterFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_filterFieldFocusGained
        filterField.setTableModel(_tableModels.get(indexParent));
        filterField.setTable(_tables.get(indexParent));
    }//GEN-LAST:event_filterFieldFocusGained

    private void BStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BStatusActionPerformed
        if (JOptionPane.showConfirmDialog(DMTWorkbench.frame, I18N.get("DataRequestManager.change-status-of-request-confirm"), I18N.get("Text.Confirm"), 0) == 0) {
            JTable internalTable = _tables.get(indexParent);
            int idDelivery = Integer.parseInt(internalTable.getValueAt(internalTable.getSelectedRow(), 0).toString());
            String status;
            if (internalTable.getValueAt(internalTable.getSelectedRow(), internalTable.getColumnCount() - 1).toString().equalsIgnoreCase("Yes")) {
                status = "No";
            } else {
                status = "Yes";
            }
            confirmDataTreated(idDelivery, status);
        }
    }//GEN-LAST:event_BStatusActionPerformed

    private void waitFilling() {
        WorkbenchFrame.progress.setProgressStatus(I18N.get("DataRequestManager.Loading-data-request"));
        WorkbenchFrame.progress.setIndeterminate(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                table.setModel(new MyTableModel());
                if (table.getRowCount() > 0) {
                    table.expandRow(indexParent);
                }
                ScrllTable.setViewportView(table);
                WorkbenchFrame.progress.setProgress(100);
            }
        }, 5000);
    }

    private void waitFilling(JXBusyLabel lab) {
        ScrllTable.setViewportView(lab);
        waitFilling();
    }

    private void enabledButton(boolean value) {
        BDownload.setEnabled(value);
        BReviewForm.setEnabled(value);
        BDelete.setEnabled(value);
        BUpdate.setEnabled(value);
        BStatus.setEnabled(value);
    }

    private void confirmDataTreated(int idDelivery, String status) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("UPDATE dmt_delivery SET "
                    + "confirm_request_treated = ? WHERE id_delivery = ?");
            ps.setString(1, status);
            ps.setInt(2, idDelivery);
            int result = ps.executeUpdate();
            if (result == 1) {
                BRefresh.doClick();
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private int getIdRequester(int idDelivery) {
        int idRequester = 0;
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT dmt_delivery.id_requester FROM "
                    + "dmt_delivery INNER JOIN dmt_requester ON dmt_delivery.id_requester = "
                    + "dmt_requester.id_requester WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return idRequester;
    }

    private ArrayList<String> getIDsImage(int idDelivery) throws SQLException {
        ArrayList<String> IDList = new ArrayList<>();
        PreparedStatement ps = Config.con.prepareStatement("SELECT id_image FROM "
                + "dmt_deliver WHERE id_delivery = ?");
        ps.setInt(1, idDelivery);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            IDList.add(res.getString(1));
        }
        return IDList;
    }

    private class FitScrollPane extends JScrollPane implements ComponentListener {

        public FitScrollPane() {
            initScrollPane();
        }

        public FitScrollPane(Component view) {
            super(view);
            initScrollPane();
        }

        public FitScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
            super(view, vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        public FitScrollPane(int vsbPolicy, int hsbPolicy) {
            super(vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        private void initScrollPane() {
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            getViewport().getView().addComponentListener(this);
            removeMouseWheelListeners();
        }

        // remove MouseWheelListener as there is no need for it in FitScrollPane.
        private void removeMouseWheelListeners() {
            MouseWheelListener[] listeners = getMouseWheelListeners();
            for (MouseWheelListener listener : listeners) {
                removeMouseWheelListener(listener);
            }
        }

        @Override
        public void updateUI() {
            super.updateUI();
            removeMouseWheelListeners();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            setSize(getSize().width, getPreferredSize().height);
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public Dimension getPreferredSize() {
            getViewport().setPreferredSize(getViewport().getView().getPreferredSize());
            return super.getPreferredSize();
        }
    }

    private void scrollRectToVisible(Component component, Rectangle aRect) {
        Container parent;
        int dx = component.getX(), dy = component.getY();
        for (parent = component.getParent();
                parent != null && (!(parent instanceof JViewport)
                || (((JViewport) parent).getClientProperty("HierarchicalTable.mainViewport") == null));
                parent = parent.getParent()) {
            Rectangle bounds = parent.getBounds();
            dx += bounds.x;
            dy += bounds.y;
        }
        if (parent != null) {
            aRect.x += dx;
            aRect.y += dy;
            ((JComponent) parent).scrollRectToVisible(aRect);
            aRect.x -= dx;
            aRect.y -= dy;
        }
    }

    private class MyTableModel extends DefaultTableModel implements HierarchicalTableModel {

        public MyTableModel() {
            super(getDataRequestYears(), new String[]{I18N.get("DataRequestManager.text_year"), I18N.get("DataRequestManager.text-Number-of-Data-Request")});
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public boolean hasChild(int row) {
            return true;
        }

        @Override
        public boolean isExpandable(int row) {
            return true;
        }

        @Override
        public boolean isHierarchical(int row) {
            return true;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getChildValueAt(int row) {
            MyTableModelChild model = null;
            Object name = getValueAt(row, 0);
            model = new MyTableModelChild(getDataRequestDetails(getIdRequestFromYear(name.toString(), "SELECT "
                    + "id_delivery FROM dmt_delivery WHERE YEAR(request_date) = ?")), new String[]{"ID", "Date",
                        "Names", "Interest Area", "Size", "Status"}) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            return model;
        }
    }

    private class MyTableModelChild extends AbstractTableModel implements StyleModel {

        private final String[] COLUMN_NAMES;
        private final ArrayList[] DATA;

        public MyTableModelChild(String[][] data, String[] columnNames) {
            this.COLUMN_NAMES = columnNames;
            DATA = new ArrayList[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                DATA[i] = new ArrayList();
            }
            for (int j = 0; j < data.length; j++) {
                for (int i = 0; i < columnNames.length; i++) {
                    DATA[i].add(data[j][i]);
                }
            }
        }

        public boolean isNavigationOn() {
            return true;
        }

        @Override
        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == COLUMN_NAMES.length - 1) {
                if ("Yes".equalsIgnoreCase(getValueAt(rowIndex, columnIndex).toString())) {
//                    setValueAt("", rowIndex, columnIndex);
                    return completed;
                } else {
//                    setValueAt("", rowIndex, columnIndex);
                    return failed;
                }
            } else {
                return null;
            }
        }

        @Override
        public boolean isCellStyleOn() {
            return true;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return DATA[0].size();
        }

        @Override
        public String getColumnName(int col) {
            return COLUMN_NAMES[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return DATA[col].get(row);
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
            DATA[col].set(row, value);
            fireTableCellUpdated(row, col);
        }

        public void addNewRow() {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].add("");
            }
            this.fireTableRowsInserted(0, DATA[0].size() - 1);
        }

        public void removeNewRow() {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].remove(DATA[i].size() - 1);
            }
            this.fireTableRowsDeleted(0, DATA[0].size() - 1);
        }

        public void removeNewRow(int index) {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].remove(index);
            }
            this.fireTableRowsDeleted(0, DATA[0].size() - 1);
        }
    }

    private String[][] getDataRequestDetails(ArrayList list) {
        String[][] RequestTab = new String[doCount("SELECT COUNT(dmt_delivery.id_delivery) FROM dmt_delivery "
                + "INNER JOIN dmt_requester ON dmt_delivery.id_requester = dmt_requester.id_requester "
                + "WHERE dmt_delivery.id_delivery IN (" + manyCriterias(list) + ")")][6];
        try {
            ResultSet res = Config.con.createStatement().executeQuery("SELECT * FROM dmt_delivery "
                    + "INNER JOIN dmt_requester ON dmt_delivery.id_requester = dmt_requester.id_requester "
                    + "WHERE dmt_delivery.id_delivery IN (" + manyCriterias(list) + ") ORDER BY request_date DESC");
            int i = 0;
            while (res.next()) {
                RequestTab[i][0] = res.getString("dmt_delivery.id_delivery");
                RequestTab[i][1] = Config.dateFormat.format(res.getDate("request_date"));
                RequestTab[i][2] = res.getString("firstname") + " "
                        + res.getString("familyname").toUpperCase() + " "
                        + res.getString("othername").toUpperCase();
                RequestTab[i][3] = res.getString("interest_area");
                RequestTab[i][4] = res.getString("image_size");
                RequestTab[i][5] = res.getString("confirm_request_treated");
                i++;
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return RequestTab;
    }

    private ArrayList getIdRequestFromYear(String year, String query) {
        ArrayList list = new ArrayList<>();
        try {
            PreparedStatement ps = Config.con.prepareStatement(query);
            ps.setString(1, year);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getInt(1));
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return list;
    }

    private String[][] getDataRequestYears() {
        String[][] tabYears = new String[doCount("SELECT COUNT(DISTINCT YEAR(request_date)) FROM dmt_delivery")][2];
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT DISTINCT YEAR(request_date), "
                    + "COUNT(id_delivery) FROM dmt_delivery group by YEAR(request_date) ORDER BY "
                    + "YEAR(request_date) DESC");
            ResultSet res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                tabYears[i][0] = res.getString(1);
                tabYears[i][1] = res.getString(2);
                i++;
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return tabYears;
    }

    private int doCount(String query) {
        try {
            PreparedStatement ps = Config.con.prepareStatement(query);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return 0;
    }

    private void setCellStyle() {
        completed.setHorizontalAlignment(SwingConstants.CENTER);
        failed.setHorizontalAlignment(SwingConstants.CENTER);

//        completed.setText("Yes");
//        failed.setText("No");
        completed.setToolTipText("Completed");
        failed.setToolTipText("Not completed yet");

        completed.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png")));
        failed.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/dialog_close.png")));
    }

    private String manyCriterias(ArrayList list) {
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            values.append("\'").append(list.get(i)).append("\',");
        }
        return values.substring(0, values.length() - 1);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BDelete;
    private com.jidesoft.swing.JideButton BDownload;
    public static com.jidesoft.swing.JideButton BRefresh;
    private com.jidesoft.swing.JideButton BReviewForm;
    private com.jidesoft.swing.JideButton BStatus;
    public static com.jidesoft.swing.JideButton BSynchronize;
    public static com.jidesoft.swing.JideButton BUpdate;
    private org.jdesktop.swingx.JXBusyLabel LBWait;
    private javax.swing.JScrollPane ScrllTable;
    private com.jidesoft.grid.QuickTableFilterField filterField;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
    private HierarchicalTable table;
    private int _destroyedCount = 0;
    private Map<Integer, TableModel> _tableModels = new HashMap<>();
    Map<Integer, JTable> _tables = new HashMap<>();
    private ListSelectionModelGroup _group = new ListSelectionModelGroup();
    private int indexParent;
    private CellStyle completed = new CellStyle();
    private CellStyle failed = new CellStyle();
}
