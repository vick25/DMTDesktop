package com.osfac.dmt.download;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.swing.FolderChooser;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTIconsFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class DownloadData extends javax.swing.JFrame {

    public DownloadData(ArrayList<String> IDImagesList, int idDelivery) {
        this(IDImagesList);
        this.idDelivery = idDelivery;
    }

    public DownloadData(ArrayList<String> IDImagesList) {
        this.idImagesList = IDImagesList;
        try {
            sClientDownload = new Socket(Config.host, Config.PORTDOWNLOAD);
        } catch (IOException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        initComponents(I18N.DMTResourceBundle);

        //Initialize the status column list for the images column to be downloaded in the table
        initializingStatusList(true, idImagesList);
        //Create the table of images
        createTable(idImagesList);
        PanMore.setVisible(false);//hide the lower panel
        setCellStyle();

        //Method thats read and copy images from Server to target
        runningSocketClient(idImagesList);
        downloadData = this;

        timerSpeedTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ProgressOnGoing.getValue() > 3) {
                    speedAndDuration();
                    FileTime = remainingTime(fileSize, cumulSizeFile, t1);
                    TotalTime = remainingTime(dataSizeTotal, cumulTotalSize, t1);
                }
            }
        });
        timerSpeedTime.start();

        //Show the support name for each image in the table
        table.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                SortableTable aTable = (SortableTable) e.getSource();
                itsRow = aTable.rowAtPoint(e.getPoint());
                itsColumn = aTable.columnAtPoint(e.getPoint());
                if (itsColumn == 0 && !imageSupportNameList.isEmpty()) {
                    aTable.setToolTipText(imageSupportNameList.get(itsRow));
                }
                aTable.repaint();
            }
        });
        this.setSize(this.getWidth(), this.getHeight() - 200);
        this.setIconImage(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.DOWNLOADDATA).getImage());
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ProgressOnGoing = new javax.swing.JProgressBar();
        ProgressTotal = new javax.swing.JProgressBar();
        BMoreLess = new com.jidesoft.swing.JideButton();
        BCancel = new com.jidesoft.swing.JideButton();
        PanMore = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CBFailedCauses = new javax.swing.JComboBox();
        BDownloadFailedFiles = new com.jidesoft.swing.JideButton();
        BDownload = new com.jidesoft.swing.JideButton();
        BTargetFolder = new com.jidesoft.swing.JideButton();
        BTClose = new com.jidesoft.swing.JideToggleButton();
        jideToggleButton2 = new com.jidesoft.swing.JideToggleButton();
        BTFileExists = new com.jidesoft.swing.JideToggleButton();
        labTargetFolder = new javax.swing.JLabel();
        labSize = new javax.swing.JLabel();
        labImageName = new javax.swing.JLabel();
        labNumber = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        setTitle(bundle.getString("DownloadData.title")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        ProgressOnGoing.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ProgressOnGoing.setStringPainted(true);

        ProgressTotal.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ProgressTotal.setStringPainted(true);

        BMoreLess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/option.png"))); // NOI18N
        BMoreLess.setText(bundle.getString("DownloadData.BMoreLess.text")); // NOI18N
        BMoreLess.setFocusable(false);
        BMoreLess.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BMoreLess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BMoreLessActionPerformed(evt);
            }
        });

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("DownloadData.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        CBFailedCauses.setEnabled(false);
        CBFailedCauses.setFocusable(false);

        BDownloadFailedFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager2.png"))); // NOI18N
        BDownloadFailedFiles.setText(bundle.getString("DownloadData.BDownloadFailedFiles.text")); // NOI18N
        BDownloadFailedFiles.setEnabled(false);
        BDownloadFailedFiles.setFocusable(false);
        BDownloadFailedFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDownloadFailedFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanMoreLayout = new javax.swing.GroupLayout(PanMore);
        PanMore.setLayout(PanMoreLayout);
        PanMoreLayout.setHorizontalGroup(
            PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(PanMoreLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanMoreLayout.createSequentialGroup()
                        .addComponent(BDownloadFailedFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBFailedCauses, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PanMoreLayout.setVerticalGroup(
            PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanMoreLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CBFailedCauses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BDownloadFailedFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        BDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager(7).png"))); // NOI18N
        BDownload.setText(bundle.getString("DownloadData.BDownload.text")); // NOI18N
        BDownload.setEnabled(false);
        BDownload.setFocusable(false);
        BDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDownloadActionPerformed(evt);
            }
        });

        BTargetFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download(5).png"))); // NOI18N
        BTargetFolder.setText(bundle.getString("DownloadData.BTargetFolder.text")); // NOI18N
        BTargetFolder.setEnabled(false);
        BTargetFolder.setFocusable(false);
        BTargetFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTargetFolderActionPerformed(evt);
            }
        });

        BTClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/close.png"))); // NOI18N
        BTClose.setToolTipText(bundle.getString("DownloadData.BTClose.toolTipText")); // NOI18N
        BTClose.setFocusable(false);
        BTClose.setSelected(true);

        jideToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/ok.png"))); // NOI18N
        jideToggleButton2.setToolTipText(bundle.getString("DownloadData.jideToggleButton2.toolTipText")); // NOI18N
        jideToggleButton2.setEnabled(false);
        jideToggleButton2.setFocusable(false);

        BTFileExists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/pyramid1_2.png"))); // NOI18N
        BTFileExists.setToolTipText(bundle.getString("DownloadData.BTFileExists.toolTipText")); // NOI18N
        BTFileExists.setFocusable(false);
        BTFileExists.setSelected(true);

        labTargetFolder.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        labSize.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labSize.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        labImageName.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        labNumber.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(BMoreLess, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(BTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(labImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labSize, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BTFileExists, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jideToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(BTClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProgressOnGoing, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProgressTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PanMore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSize, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProgressOnGoing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BTClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jideToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTFileExists, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProgressTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BMoreLess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BDownload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanMore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        jPanel1 = new javax.swing.JPanel();
        ProgressOnGoing = new javax.swing.JProgressBar();
        ProgressTotal = new javax.swing.JProgressBar();
        BMoreLess = new com.jidesoft.swing.JideButton();
        BCancel = new com.jidesoft.swing.JideButton();
        PanMore = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CBFailedCauses = new javax.swing.JComboBox();
        BDownloadFailedFiles = new com.jidesoft.swing.JideButton();
        BDownload = new com.jidesoft.swing.JideButton();
        BTargetFolder = new com.jidesoft.swing.JideButton();
        BTClose = new com.jidesoft.swing.JideToggleButton();
        jideToggleButton2 = new com.jidesoft.swing.JideToggleButton();
        BTFileExists = new com.jidesoft.swing.JideToggleButton();
        labTargetFolder = new javax.swing.JLabel();
        labSize = new javax.swing.JLabel();
        labImageName = new javax.swing.JLabel();
        labNumber = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(bundle.getString("DownloadData.title")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        ProgressOnGoing.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ProgressOnGoing.setStringPainted(true);

        ProgressTotal.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ProgressTotal.setStringPainted(true);

        BMoreLess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/option.png"))); // NOI18N
        BMoreLess.setText(bundle.getString("DownloadData.BMoreLess.text")); // NOI18N
        BMoreLess.setFocusable(false);
        BMoreLess.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BMoreLess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BMoreLessActionPerformed(evt);
            }
        });

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("DownloadData.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        CBFailedCauses.setEnabled(false);
        CBFailedCauses.setFocusable(false);

        BDownloadFailedFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager2.png"))); // NOI18N
        BDownloadFailedFiles.setText(bundle.getString("DownloadData.BDownloadFailedFiles.text")); // NOI18N
        BDownloadFailedFiles.setEnabled(false);
        BDownloadFailedFiles.setFocusable(false);
        BDownloadFailedFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDownloadFailedFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanMoreLayout = new javax.swing.GroupLayout(PanMore);
        PanMore.setLayout(PanMoreLayout);
        PanMoreLayout.setHorizontalGroup(
                PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(PanMoreLayout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanMoreLayout.createSequentialGroup()
                                        .addComponent(BDownloadFailedFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CBFailedCauses, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)));
        PanMoreLayout.setVerticalGroup(
                PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanMoreLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(CBFailedCauses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BDownloadFailedFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap()));

        BDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager(7).png"))); // NOI18N
        BDownload.setText(bundle.getString("DownloadData.BDownload.text")); // NOI18N
        BDownload.setEnabled(false);
        BDownload.setFocusable(false);
        BDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDownloadActionPerformed(evt);
            }
        });

        BTargetFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download(5).png"))); // NOI18N
        BTargetFolder.setText(bundle.getString("DownloadData.BTargetFolder.text")); // NOI18N
        BTargetFolder.setEnabled(false);
        BTargetFolder.setFocusable(false);
        BTargetFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTargetFolderActionPerformed(evt);
            }
        });

        BTClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/close.png"))); // NOI18N
        BTClose.setToolTipText(bundle.getString("DownloadData.BTClose.toolTipText")); // NOI18N
        BTClose.setFocusable(false);
        BTClose.setSelected(true);

        jideToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/ok.png"))); // NOI18N
        jideToggleButton2.setToolTipText(bundle.getString("DownloadData.jideToggleButton2.toolTipText")); // NOI18N
        jideToggleButton2.setEnabled(false);
        jideToggleButton2.setFocusable(false);

        BTFileExists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/pyramid1_2.png"))); // NOI18N
        BTFileExists.setToolTipText(bundle.getString("DownloadData.BTFileExists.toolTipText")); // NOI18N
        BTFileExists.setEnabled(true);
        BTFileExists.setFocusable(false);
        BTFileExists.setSelected(true);

        labTargetFolder.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        labSize.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labSize.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        labImageName.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        labNumber.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(BMoreLess, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(BTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(BDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(labImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labSize, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(labTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(BTFileExists, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jideToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(BTClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(ProgressOnGoing, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(ProgressTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(PanMore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labSize, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProgressOnGoing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BTClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jideToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BTFileExists, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProgressTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BMoreLess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BDownload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BTargetFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanMore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }// </editor-fold>

    private void BMoreLessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BMoreLessActionPerformed
        if (PanMore.isVisible()) {
            PanMore.setVisible(false);
            this.setSize(this.getWidth(), this.getHeight() - 200);
            BMoreLess.setText(I18N.get("DownloadData.BMoreLess.text"));
            BMoreLess.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/option.png")));
        } else {
            PanMore.setVisible(true);
            this.setSize(this.getWidth(), this.getHeight() + 200);
            BMoreLess.setText(I18N.get("DownloadData.BMoreLess.text-less"));
            BMoreLess.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/option2.png")));
        }
    }//GEN-LAST:event_BMoreLessActionPerformed

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        formWindowClosing(null);
    }//GEN-LAST:event_BCancelActionPerformed

    private void BTargetFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTargetFolderActionPerformed
        List recentList = new ArrayList(); // create recent list
        recentList.add(dir);
        FolderChooser fc = new FolderChooser(Config.getDefaultDirectory());
        fc.setRecentList(recentList);
        fc.setFileHidingEnabled(false);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle(I18N.get("DownloadData.Target-Folder"));
        int result = fc.showOpenDialog(this);
        if (JFileChooser.OPEN_DIALOG == result) {
            dir = fc.getSelectedFile().getAbsolutePath();
            Config.setDefaultDirectory(dir);
            if (testBeforeCopyFile()) {
                labTargetFolder.setText("");
                BDownload.setEnabled(false);
                JOptionPane.showMessageDialog(this, I18N.get("DownloadData.Target-Folder-not-enough-privileges"),
                        I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
            } else {
                long freeSpace = new File(dir).getFreeSpace();
                if (dataSizeTotal >= freeSpace) {
                    BDownload.setEnabled(false);
                    labTargetFolder.setText("");
                    JOptionPane.showMessageDialog(this, I18N.get("DownloadData.Target-Folder-not-enough-space"),
                            I18N.get("com.osfac.dmt.Config.Error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    labTargetFolder.setText(dir);
                    BDownload.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_BTargetFolderActionPerformed

    private void BDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDownloadActionPerformed
        BDownload.setEnabled(false);
        BTargetFolder.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                try {
                    if (idDelivery != 0) {
                        confirmDataTreated(idDelivery);
                    }
                    for (a = 0; a < idImagesList.size(); a++) {
                        //Check if file exists in destination folder and skip if true
                        if (!fileInTargetFolder(idImagesList.get(a))) {
                            changeRowStatus(SPROGRESSING);
                            outDownload.write(String.valueOf(idImagesList.get(a)).getBytes());
                            outDownload.flush();
                            Thread.sleep(10);
                            waitingFileBeSending = true;
                            while (waitingFileBeSending) {
//////////                System.out.println("Waiting ...");
                            }
                        } else {
                            continue;
                        }
                    }
//                    labImageName.setText("");
//                    BDownload.setEnabled(true);
//                    BTargetFolder.setEnabled(true);
                    timerSpeedTime.stop();
                    if (idImagesFailedFiles.isEmpty()) {
                        endOfDownload(I18N.get("DownloadData.done-successfully"));
                        if (BTClose.isSelected()) {
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(downloadData, I18N.get("DownloadData.done-successfully"));
                            dispose();
                        }
                    } else {
                        endOfDownload(I18N.get("DownloadData.completed-with-some-errors"));
//                        BDownloadFailedFiles.setEnabled(true);
                    }
                } catch (IOException | InterruptedException e) {
                    JXErrorPane.showDialog(downloadData, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
                }
            }
        }.start();
    }//GEN-LAST:event_BDownloadActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if ((ProgressTotal.getValue() < dataSizeTotal) && (a > 0)) {
            if (JOptionPane.showConfirmDialog(this, I18N.get("DownloadData.cancel-download-not-yet-finish"), I18N.get("Text.Confirm"), 0) == 0) {
                this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                this.dispose();
            }
        } else {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private void BDownloadFailedFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDownloadFailedFilesActionPerformed
//        createTable(idImagesFailedFiles);
//        initializingStatusList(false, idImagesFailedFiles);
//        table.repaint();
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    timerSpeedTime.start();
//                    for (a = 0; a < idImagesList.size(); a++) {
//                        changeRowStatus(SPROGRESSING);
//                        outDownload.write(String.valueOf(idImagesList.get(a)).getBytes());
//                        outDownload.flush();
//                        Thread.sleep(10);
//                        waitingFileBeSending = true;
//                        while (waitingFileBeSending) {
//                            //////////                System.out.println("Waiting ...");
//                        }
//                    }
//                    timerSpeedTime.stop();
//                    if (idImagesFailedFiles.isEmpty()) {
//                        if (BTClose.isSelected()) {
//                            dispose();
//                        } else {
//                            JOptionPane.showMessageDialog(downloadData, "Download has been done successfully");
//                            dispose();
//                        }
//                    } else {
//                        BDownloadFailedFiles.setEnabled(true);
//                    }
//                } catch (IOException | InterruptedException e) {
//                    JXErrorPane.showDialog(downloadData, new ErrorInfo("Fatal error",
//                        e.getMessage(), null, null, e, Level.SEVERE, null));
//            }
//        }
//        }.start();
    }//GEN-LAST:event_BDownloadFailedFilesActionPerformed

    private void confirmDataTreated(int idDelivery) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("UPDATE dmt_delivery SET\n"
                    + "confirm_request_treated = ? WHERE id_delivery = ?");
            ps.setString(1, "Yes");
            ps.setInt(2, idDelivery);
            int result = ps.executeUpdate();
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private void changeRowStatus(int status) {
        statusImages.set(a, status);
        jScrollPane1.repaint();
        table.repaint();
        this.repaint();
    }

    private void setCellStyle() {
        cellTextBold.setFontStyle(Font.BOLD);
        cellTextBold.setToolTipText(I18N.get("DownloadData.Progressing"));
        completed.setHorizontalAlignment(SwingConstants.CENTER);
        waiting.setHorizontalAlignment(SwingConstants.CENTER);
        progressing.setHorizontalAlignment(SwingConstants.CENTER);
        failed.setHorizontalAlignment(SwingConstants.CENTER);

        waiting.setToolTipText(I18N.get("DownloadData.Waiting"));
        progressing.setToolTipText(I18N.get("DownloadData.Progressing"));
        completed.setToolTipText(I18N.get("DownloadData.Completed"));
        failed.setToolTipText(I18N.get("DownloadData.Failed-due-to-HDD-or-File-does-not-exist"));

        waiting.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/busy.png")));
        progressing.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/download_manager(8).png")));
        completed.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png")));
        failed.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/dialog_close.png")));
    }

    private void createTable(ArrayList<String> idImagesList) {
        tableModel = new MyTableModel();
        table = new SortableTable(tableModel);
        table.setSortable(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{Config.getColorFromKey(Config.pref.get(
            SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")), Config.getColorFromKey(Config.pref.get(
            SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
        table.setColumnResizable(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        try {
            ResultSet res = Config.con.createStatement().executeQuery("SELECT image_path, size, support_name FROM dmt_image INNER JOIN\n"
                    + "dmt_support ON dmt_support.id_support = dmt_image.id_support WHERE dmt_image.id_image\n"
                    + "IN (" + manyCriteria(idImagesList) + ")");
            int i = 0;
            while (res.next()) {
                if (table.getRowCount() >= i) {
                    tableModel.addNewRow();
                }
                table.setValueAt(res.getString(1), i, 0); //image path
                table.setValueAt(res.getString(2), i, 1); //size
                if (!Config.isLiteVersion()) {
                    imageSupportNameList.add(res.getString(3));
                }
                i++;
            }
            TableUtils.autoResizeAllColumns(table);
            TableUtils.autoResizeAllRows(table);
            Config.centerTableHeadAndBold(table);
            table.setRowSelectionAllowed(false);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        jScrollPane1.setViewportView(table);
    }

    private String manyCriteria(ArrayList list) {
        String values = "";
        for (int i = 0; i < list.size(); i++) {
            values += "\'" + list.get(i) + "\',";
        }
        return values.substring(0, values.length() - 1);
    }

    //Initialize the the status column of images to be downloaded in the table
    private void initializingStatusList(boolean targetFolder, ArrayList<String> listOfIDs) {
        for (int i = 0; i < listOfIDs.size(); i++) {
            statusImages.add(SWAITING);
        }
        initializingOthers(targetFolder, listOfIDs);
    }

    //Set the Number, Total size labels and the ProgressTotal with their initialize values
    private void initializingOthers(boolean targetFolder, ArrayList<String> listOfIDs) {
        ProgressTotal.setIndeterminate(true);
        try {
            ResultSet res = Config.con.createStatement().executeQuery("SELECT SUM(size) FROM dmt_image "
                    + "WHERE id_image IN (" + manyCriteria(listOfIDs) + ")");
            while (res.next()) {
                dataSizeTotal += res.getDouble(1) * (1024 * 1024);
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        ProgressTotal.setIndeterminate(false);
        BTargetFolder.setEnabled(targetFolder);
        labSize.setText(I18N.get("DownloadData.label-Total-size") + " "
                + convertCapacity(dataSizeTotal));
        labNumber.setText(I18N.get("DownloadData.label-Number") + " "
                + idImagesList.size());
        Long positionMax = new Long(dataSizeTotal);
        if (positionMax > 2047483647) {
            positionMax /= 1048576;
        }
        ProgressTotal.setMaximum(positionMax.intValue());
    }

    private boolean testBeforeCopyFile() {
        boolean val = false;
        try {
            InputStream inImportant = getClass().getResourceAsStream("/com/osfac/dmt/form/jasper/001.png");
            File dest = new File(dir + File.separator + "001.png");
            try (FileOutputStream fout = new FileOutputStream(dest)) {
                int nbRead;
                while ((nbRead = inImportant.read(TAMPON)) != -1) {
                    fout.write(TAMPON, 0, nbRead);
                }
            }
            dest.delete();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            val = true;
        }
        return val;
    }

    private void minusSize() {
        cumulSizeFile = 0;
        capacity_Treated = 0;
        fileSize = (long) Config.getImageSize(idImagesList.get(a)) * (1024 * 1024);
        ProgressOnGoing.setMaximum((int) fileSize);
        cumulSizeFile += fileSize;
        cumulTotalSize += fileSize;
        capacity_Treated += fileSize;
        Long positionCumul = new Long(cumulTotalSize);
        if (dataSizeTotal > 2047483647) {
            positionCumul = new Long(cumulTotalSize / 1048576);
        } else {
            positionCumul = new Long(cumulTotalSize);
        }
        ProgressTotal.setValue(positionCumul.intValue());
        ProgressOnGoing.setValue((int) cumulSizeFile);
        pourcentAll = (int) (ProgressTotal.getPercentComplete() * 100) + "%";
        pourcentFile = (int) (ProgressOnGoing.getPercentComplete() * 100) + "%";
        ProgressTotal.setString(Config.convertOctetToAnyInDouble(cumulTotalSize)
                + " " + I18N.get("DownloadData.text-of") + " " + Config.convertOctetToAnyInDouble(dataSizeTotal)
                + "                                         " + pourcentAll
                + "                                         " + TotalTime);
        ProgressOnGoing.setString(conversion(cumulSizeFile) + " "
                + I18N.get("DownloadData.text-of") + " " + conversion(fileSize)
                + "                                               " + pourcentFile
                + "                                              " + FileTime);
    }

    private void endOfDownload(String title) {
        labImageName.setText("");
        this.setTitle(title);
        ProgressTotal.setString("                                         "
                + pourcentAll + "                                         ");
        ProgressOnGoing.setString("                                               "
                + pourcentFile + "                                              ");
    }

    private void speedAndDuration() {
        calculateSpeed(t1);
        setSpeed(getSpeed());
    }

    private void setSpeed(String s) {
        StringTokenizer token = new StringTokenizer(s);
        if (token.countTokens() > 0) {
            String svit = token.nextToken(), grandeur = token.nextToken();
            double vit = Double.parseDouble(svit);
            if (grandeur.equalsIgnoreCase("Ko/s")) {
                vit = vit * 1024;
            } else if (grandeur.equalsIgnoreCase("Mo/s")) {
                vit = vit * 1024 * 1024;
            } else if (grandeur.equalsIgnoreCase("Go/s")) {
                vit = vit * 1024 * 1024 * 1024;
            }
            int tpsSec = (int) ((dataSizeTotal - cumulTotalSize) / vit);
            time = "";
            if (tpsSec > 60 * 60) {
                time = (tpsSec / 60 / 60) + " h " + (tpsSec / 60 - 60 * (tpsSec / 60 / 60)) + " min";
            } else if (tpsSec > 60) {
                time = (tpsSec / 60) + " min " + (tpsSec - 60 * (tpsSec / 60)) + " sec";
            } else {
                time = tpsSec + " sec";
            }
            this.setTitle(" " + pourcentAll + " - " + time + " " + "(" + s
                    + ") - " + (a + 1) + " " + I18N.get("DownloadData.text-of") + " "
                    + idImagesList.size());
        }
    }

    private String remainingTime(long total, long cumul, long t1) {
        String temps = "";
        long t2, t, v;
        t2 = new Date().getTime();
        t = (t2 - t1) / 1000;
        if (t == 0) {
            t = 1;
        }
        v = capacity_Treated / t;
        StringTokenizer token = new StringTokenizer(convertCapacity(v) + "/s");
        if (token.countTokens() > 0) {
            String svit = token.nextToken(), grandeur = token.nextToken();
            double vit = Double.parseDouble(svit);
            if (grandeur.equalsIgnoreCase("Ko/s")) {
                vit = vit * 1024;
            } else if (grandeur.equalsIgnoreCase("Mo/s")) {
                vit = vit * 1024 * 1024;
            } else if (grandeur.equalsIgnoreCase("Go/s")) {
                vit = vit * 1024 * 1024 * 1024;
            }
            int tpsSec = (int) ((total - cumul) / vit);
            if (tpsSec > 60 * 60) {
                temps = (tpsSec / 60 / 60) + " h " + (tpsSec / 60 - 60 * (tpsSec / 60 / 60)) + " min";
            } else if (tpsSec > 60) {
                temps = (tpsSec / 60) + " min " + (tpsSec - 60 * (tpsSec / 60)) + " sec";
            } else {
                if (tpsSec < 0) {
                    tpsSec = 0;
                }
                temps = tpsSec + " sec";
            }
        }
        return temps;
    }

    private String getSpeed() {
        return speed;
    }

    private void calculateSpeed(long t1) {
        long t2, t, v;
        t2 = new Date().getTime();
        t = (t2 - t1) / 1000;
        if (t == 0) {
            t = 1;
        }
        v = capacity_Treated / t;
        speed = convertCapacity(v) + "/s";
    }

    private String convertCapacity(long size) {
        if (size >= 1024 * 1024 * 1024) {
            return (size / 1024 / 1024 / 1024) + "." + (size / 1024 / 1024 - 1024 * (size / 1024 / 1024 / 1024)) / 100 + " Go";
        } else if (size >= 1024 * 1024) {
            return (size / 1024 / 1024) + "." + (size / 1024 - 1024 * (size / 1024 / 1024)) / 100 + " Mo";
        } else if (size >= 1024) {
            return (size / 1024) + "." + (size - 1024 * (size / 1024)) / 100 + " Ko";
        }
        return size + " Octets";
    }

    private String getImageName(String idImage) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT image_name FROM dmt_image\n"
                    + "WHERE id_image = ?");
            ps.setString(1, idImage);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return "";
    }

    private String getImageFormat(String idImage) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT format FROM dmt_image "
                    + "WHERE id_image = ?");
            ps.setString(1, idImage);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return "";
    }

    private String getHardDriveName(String idImage) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT support_name FROM dmt_support "
                    + "INNER JOIN dmt_image ON dmt_support.id_support = dmt_image.id_support "
                    + "WHERE id_image = ?");
            ps.setString(1, idImage);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return "";
    }

    //Method that reads and copy the images from server to target
    private void runningSocketClient(final ArrayList<String> idImagesList) {
        new Thread() {
            @Override
            public void run() {
                DataInputStream in = null;
                try {
                    in = new DataInputStream(sClientDownload.getInputStream());
                    outDownload = new DataOutputStream(sClientDownload.getOutputStream());
                    int nbbit;
                    String dataRead;
                    while ((nbbit = in.read(TAMPON)) != -1) {
                        dataRead = new String(TAMPON, 0, nbbit);
                        String imageName = getImageName(idImagesList.get(a));
                        String HDD_Name = getHardDriveName(idImagesList.get(a));
                        table.scrollRowToVisible(a);
                        if (dataRead.equals(HDDNOTCONNECTED)) {
                            showFailureAction(HDD_Name + " " + I18N.get("DownloadData.text-HDD-not-connected"));
//                            JOptionPane.showMessageDialog(downloadData, getHardDriveName(idImagesList.get(a))
//                                    + " is not connected to the server !!!", "Fatal Error", JOptionPane.ERROR_MESSAGE);
                        } else if (dataRead.equals(FILENOTEXIST)) {
                            showFailureAction(imageName + " " + I18N.get("DownloadData.text-File-does-not-existed"));
//                            JOptionPane.showMessageDialog(downloadData, imageName
//                                    + " doesn't exist in the server !!!", "Fatal Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            fileSize = Long.parseLong(dataRead);
                            Long positionCumul = new Long(cumulSizeFile);
                            if (getImageFormat(idImagesList.get(a)).equals("")) {
                                imageName = imageName + ".zip";
                            }
                            File targetFile = new File(destByCategory(labTargetFolder.getText(), idImagesList.get(a))
                                    + File.separator + imageName);
                            downloadingAction(targetFile, positionCumul, imageName, in);
                        }
                        waitingFileBeSending = false;
                    }
                } catch (IOException e) {
                    JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
                    }
                }
            }
        }.start();
    }

    private boolean fileInTargetFolder(String imageID) {
        if (BTFileExists.isSelected()) {
            try {
                PreparedStatement ps = Config.con.prepareStatement("SELECT DISTINCT image_name, size FROM dmt_image "
                        + "WHERE id_image = ?");
                ps.setString(1, imageID);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    String imageName = res.getString(1),
                            targetSize,
                            imageSize = Config.convertOctetToAnyInDouble((long) (res.getDouble(2) * (1024 * 1024)));
                    if (getImageFormat(idImagesList.get(a)).equals("")) {
                        imageName = imageName + ".zip";
                    }
                    File targetFile = new File(destByCategory(labTargetFolder.getText(), idImagesList.get(a))
                            + File.separator + imageName);

                    if (targetFile.exists()) {
                        targetSize = Config.convertOctetToAnyInDouble(targetFile.length());

                        double imageSizeDbl = Double.valueOf(imageSize.substring(0, imageSize.length() - 3).trim()),
                                targetFileDbl = Double.valueOf(targetSize.substring(0, targetSize.length() - 3).trim());

                        if ((targetFileDbl >= imageSizeDbl) || (imageSize.equals(targetSize))) {
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                JXErrorPane.showDialog(null, new ErrorInfo("Fatal error",
                        e.getMessage(), null, null, e, Level.SEVERE, null));
            }
        }
        return false;
    }

    private void downloadingAction(File targetFile, Long positionCumul,
            String imageName, DataInputStream in) throws FileNotFoundException, IOException {
        t1 = new Date().getTime();
        capacity_Treated = 0;
        try (FileOutputStream fout = new FileOutputStream(targetFile)) {
            long endCopyFile = 0;
            int nbRead;
            labImageName.setText(imageName);
            cumulSizeFile = 0;
            ProgressOnGoing.setMaximum((int) fileSize);
            while ((nbRead = in.read(TAMPON)) != -1) {
                fout.write(TAMPON, 0, nbRead);
                endCopyFile += nbRead;
                cumulSizeFile += nbRead;
                cumulTotalSize += nbRead;
                capacity_Treated += nbRead;
                if (dataSizeTotal > 2047483647) {
                    positionCumul = new Long(cumulTotalSize / 1048576);
                } else {
                    positionCumul = new Long(cumulTotalSize);
                }
                ProgressTotal.setValue(positionCumul.intValue());
                ProgressOnGoing.setValue((int) cumulSizeFile);
                pourcentAll = (int) (ProgressTotal.getPercentComplete() * 100) + "%";
                pourcentFile = (int) (ProgressOnGoing.getPercentComplete() * 100) + "%";
                ProgressTotal.setString(Config.convertOctetToAnyInDouble(cumulTotalSize)
                        + " " + I18N.get("DownloadData.text-of") + " "
                        + Config.convertOctetToAnyInDouble(dataSizeTotal)
                        + "                                         " + pourcentAll
                        + "                                         " + TotalTime);
                ProgressOnGoing.setString(conversion(cumulSizeFile)
                        + " " + I18N.get("DownloadData.text-of") + " " + conversion(fileSize)
                        + "                                               " + pourcentFile
                        + "                                              " + FileTime);
                if (endCopyFile >= fileSize) {
                    changeRowStatus(SCOMPLETED);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void showFailureAction(String cause) {
        minusSize();
        changeRowStatus(SFAILED);
        if (!causesFaillure.contains(cause)) {
            causesFaillure.add(cause);
        }
        idImagesFailedFiles.add(idImagesList.get(a));
        if (!PanMore.isVisible()) {
            BMoreLess.doClick();
        }
        CBFailedCauses.setModel(new DefaultComboBoxModel(causesFaillure.toArray()));
        CBFailedCauses.setEnabled(true);
    }

    private String destByCategory(String dest, String idImage) {
        String destCategory = dest;
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT category_name FROM dmt_category INNER JOIN dmt_image ON "
                    + "dmt_category.id_category = dmt_image.id_category WHERE id_image = ?");
            ps.setString(1, idImage);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                destCategory = dest + File.separator + res.getString(1);
            }
            File dirCat = new File(destCategory);
            dirCat.mkdirs();
        } catch (Exception e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return destCategory;
    }

    private String conversion(long valeur) {
        if ((valeur >= 1024) && (valeur < (1024 * 1024))) {
            return ((long) valeur / 1024) + " Ko";
        } else if ((valeur >= (1024 * 1024)) && (valeur < (1024 * 1024 * 1024))) {
            return ((long) valeur / (1024 * 1024)) + " Mo";
        } else if ((valeur >= (1024 * 1024 * 1024))) {
            return ((long) valeur / (1024 * 1024 * 1024)) + " Go";
        } else {
            return ((long) valeur) + " Octets";
        }
    }

    private class MyTableModel extends AbstractTableModel implements StyleModel {

        private final String[] COLUMN_NAMES = {I18N.get("DownloadData.table-header-File"), I18N.get("DownloadData.table-header-Size"),
            I18N.get("DownloadData.table-header-Status")};
        private final ArrayList[] DATA;

        public MyTableModel() {
            DATA = new ArrayList[COLUMN_NAMES.length];
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i] = new ArrayList();
            }
        }

        public boolean isNavigationOn() {
            return true;
        }

        @Override
        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == table.getColumnCount() - 1) {
                if (statusImages.get(rowIndex) == SWAITING) {
                    return waiting;
                } else if (statusImages.get(rowIndex) == SPROGRESSING) {
                    return progressing;
                } else if (statusImages.get(rowIndex) == SCOMPLETED) {
                    return completed;
                } else {
                    return failed;
                }
            } else if (statusImages.get(rowIndex) == 1) {
                return cellTextBold;
            } else {
                return cellText;
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
            return false;
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BCancel;
    private com.jidesoft.swing.JideButton BDownload;
    private com.jidesoft.swing.JideButton BDownloadFailedFiles;
    private com.jidesoft.swing.JideButton BMoreLess;
    private com.jidesoft.swing.JideToggleButton BTClose;
    private com.jidesoft.swing.JideToggleButton BTFileExists;
    private com.jidesoft.swing.JideButton BTargetFolder;
    private javax.swing.JComboBox CBFailedCauses;
    private javax.swing.JPanel PanMore;
    private javax.swing.JProgressBar ProgressOnGoing;
    private javax.swing.JProgressBar ProgressTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.jidesoft.swing.JideToggleButton jideToggleButton2;
    private javax.swing.JLabel labImageName;
    private javax.swing.JLabel labNumber;
    private javax.swing.JLabel labSize;
    private javax.swing.JLabel labTargetFolder;
    // End of variables declaration//GEN-END:variables
    private SortableTable table;
    private MyTableModel tableModel;
    private ArrayList<String> idImagesList = new ArrayList<>();
    private ArrayList<String> idImagesFailedFiles = new ArrayList<>();
    private ArrayList<String> causesFaillure = new ArrayList<>();
    private ArrayList<Integer> statusImages = new ArrayList<>();
    private ArrayList<String> imageSupportNameList = new ArrayList<>();
    private CellStyle progressing = new CellStyle();
    private CellStyle completed = new CellStyle();
    private CellStyle waiting = new CellStyle();
    private CellStyle failed = new CellStyle();
    private CellStyle cellText = new CellStyle();
    private CellStyle cellTextBold = new CellStyle();
    private Socket sClientDownload = null;
    private final int SWAITING = 0;
    private final int SPROGRESSING = 1;
    private final int SCOMPLETED = 2;
    private final int SFAILED = 3;
    private String dir;
    private volatile boolean waitingFileBeSending = false;
    private long dataSizeTotal;
    private final int BUFFER = 1024;
    private byte TAMPON[] = new byte[BUFFER];
    private long fileSize, cumulSizeFile, cumulTotalSize, t1, capacity_Treated = 0;
    private DataOutputStream outDownload;
    private int a;
    private int itsRow = 0, itsColumn = 0;
    int idDelivery = 0;
    private String speed = "";
    private String pourcentAll = "0%", time = "", pourcentFile = "0%", FileTime = "0:0:0", TotalTime = "0:0:0";
    private String HDDNOTCONNECTED = "HDD_NOT_CONNECTED", FILENOTEXIST = "FILE_NOT_EXIST";
    private DownloadData downloadData;
    private Timer timerSpeedTime;
}
