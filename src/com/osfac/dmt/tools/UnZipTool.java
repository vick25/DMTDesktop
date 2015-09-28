package com.osfac.dmt.tools;

import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class UnZipTool extends javax.swing.JDialog {

    public UnZipTool(java.awt.Frame parent, boolean modal, ArrayList list) {
        super(parent, modal);
        this.parent = (JFrame) parent;
        initComponents(I18N.DMTResourceBundle);
        ChBOpenFolder.setEnabled(false);
        ChBOpenFolder.setSelected(false);
        timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ListFiles.isEmpty()) {
                    BUnzip.setEnabled(false);
                } else {
                    BUnzip.setEnabled(true);
                }
            }
        });
        timer.start();
        displayFilesChoosen(list);
        this.setTitle(I18N.get("com.osfac.dmt.tools.UnZipTool.title"));
        this.setLocationRelativeTo(parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panAll = new javax.swing.JPanel();
        BUnzip = new com.jidesoft.swing.JideButton();
        BCancel = new com.jidesoft.swing.JideButton();
        BAddFiles = new com.jidesoft.swing.JideButton();
        BRemoveFile = new com.jidesoft.swing.JideButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JListFiles = new javax.swing.JList();
        ProgressBar = new javax.swing.JProgressBar();
        ChBOpenFolder = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        BUnzip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        BUnzip.setText(bundle.getString("UnZipTool.BUnzip.text")); // NOI18N
        BUnzip.setEnabled(false);
        BUnzip.setFocusable(false);
        BUnzip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUnzipActionPerformed(evt);
            }
        });

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("UnZipTool.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BAddFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/plus.png"))); // NOI18N
        BAddFiles.setFocusable(false);
        BAddFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAddFilesActionPerformed(evt);
            }
        });

        BRemoveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/minus.png"))); // NOI18N
        BRemoveFile.setEnabled(false);
        BRemoveFile.setFocusable(false);
        BRemoveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRemoveFileActionPerformed(evt);
            }
        });

        JListFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                JListFilesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(JListFiles);

        ProgressBar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ProgressBar.setStringPainted(true);

        ChBOpenFolder.setSelected(true);
        ChBOpenFolder.setText(bundle.getString("UnZipTool.ChBOpenFolder.text")); // NOI18N
        ChBOpenFolder.setFocusable(false);

        javax.swing.GroupLayout panAllLayout = new javax.swing.GroupLayout(panAll);
        panAll.setLayout(panAllLayout);
        panAllLayout.setHorizontalGroup(
            panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAllLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAllLayout.createSequentialGroup()
                        .addComponent(ChBOpenFolder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BUnzip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panAllLayout.createSequentialGroup()
                        .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panAllLayout.createSequentialGroup()
                                .addComponent(BAddFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(BRemoveFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panAllLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BCancel, BUnzip});

        panAllLayout.setVerticalGroup(
            panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAllLayout.createSequentialGroup()
                .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BAddFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BRemoveFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BUnzip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChBOpenFolder))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        panAll = new javax.swing.JPanel();
        BUnzip = new com.jidesoft.swing.JideButton();
        BCancel = new com.jidesoft.swing.JideButton();
        BAddFiles = new com.jidesoft.swing.JideButton();
        BRemoveFile = new com.jidesoft.swing.JideButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JListFiles = new javax.swing.JList();
        ProgressBar = new javax.swing.JProgressBar();
        ChBOpenFolder = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        BUnzip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png"))); // NOI18N
        BUnzip.setText(bundle.getString("UnZipTool.BUnzip.text")); // NOI18N
        BUnzip.setEnabled(false);
        BUnzip.setFocusable(false);
        BUnzip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUnzipActionPerformed(evt);
            }
        });

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("UnZipTool.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BAddFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/plus.png"))); // NOI18N
        BAddFiles.setFocusable(false);
        BAddFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAddFilesActionPerformed(evt);
            }
        });

        BRemoveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/minus.png"))); // NOI18N
        BRemoveFile.setEnabled(false);
        BRemoveFile.setFocusable(false);
        BRemoveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRemoveFileActionPerformed(evt);
            }
        });

        JListFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                JListFilesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(JListFiles);

        ProgressBar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ProgressBar.setStringPainted(true);

        ChBOpenFolder.setSelected(true);
        ChBOpenFolder.setText(bundle.getString("UnZipTool.ChBOpenFolder.text")); // NOI18N
        ChBOpenFolder.setFocusable(false);

        javax.swing.GroupLayout panAllLayout = new javax.swing.GroupLayout(panAll);
        panAll.setLayout(panAllLayout);
        panAllLayout.setHorizontalGroup(
                panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panAllLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panAllLayout.createSequentialGroup()
                                        .addComponent(ChBOpenFolder)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(BUnzip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panAllLayout.createSequentialGroup()
                                        .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(panAllLayout.createSequentialGroup()
                                                        .addComponent(BAddFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(3, 3, 3)
                                                        .addComponent(BRemoveFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap()));

        panAllLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{BCancel, BUnzip});

        panAllLayout.setVerticalGroup(
                panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAllLayout.createSequentialGroup()
                        .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BAddFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BRemoveFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BUnzip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ChBOpenFolder))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }// </editor-fold>

    private void BUnzipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUnzipActionPerformed
        disableComponent();
        Thread thread = new Thread() {
            @Override
            public void run() {
                ProgressBar.setIndeterminate(true);
                for (int i = 0; i < ListFiles.size(); i++) {
                    progressValMax += new File(ListFiles.get(i)).length();
                }
                ProgressBar.setIndeterminate(false);
                progressValue = 0;
                ProgressBar.setMaximum(progressValMax);
                ProgressBar.setValue(progressValue);
                for (int i = 0; i < ListFiles.size(); i++) {
                    if (ListFiles.get(i).toLowerCase().endsWith("zip")) {
                        unzipZipFile(new File(ListFiles.get(i)));
                    } else if (ListFiles.get(i).toLowerCase().endsWith("gz")
                            || ListFiles.get(i).toLowerCase().endsWith("gzip")) {
                        unzipGZipFile(new File(ListFiles.get(i)));
                    }
                }
                enableComponent();
            }
        };
        thread.start();
    }//GEN-LAST:event_BUnzipActionPerformed

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_BCancelActionPerformed

    private void BAddFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAddFilesActionPerformed
        JFileChooser fc = new JFileChooser(Config.getDefaultDirectory());
        fc.setFileFilter(new FileNameExtensionFilter(I18N.get("UnZipTool.description-format"), "zip"));
        fc.setMultiSelectionEnabled(true);
        int result = fc.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            Config.setDefaultDirectory(fc.getSelectedFile().getParent());
            File[] tabFiles = fc.getSelectedFiles();
            for (int i = 0; i < tabFiles.length; i++) {
                if (ListFiles.contains(tabFiles[i].getAbsolutePath())) {
                    JOptionPane.showMessageDialog(parent, new StringBuilder("\"").append(tabFiles[i].getName())
                            .append("\" ").append(I18N.get("UnZipTool.already-shown-on-the-list")).toString(),
                            I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
                } else if (!tabFiles[i].getAbsolutePath().toLowerCase().endsWith("zip")) {
                    JOptionPane.showMessageDialog(parent, new StringBuilder(I18N.get("UnZipTool.format-not-accaptable"))
                            .append(" : ").append("\"").append(tabFiles[i].getName()).append("\"").toString(),
                            I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
                } else {
                    ListFiles.add(tabFiles[i].getAbsolutePath());
                    listFileName.add(tabFiles[i].getName());
                }
            }
            JListFiles.setListData(listFileName.toArray());
        }
    }//GEN-LAST:event_BAddFilesActionPerformed

    private void BRemoveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRemoveFileActionPerformed
        ListFiles.remove(JListFiles.getSelectedIndex());
        listFileName.remove(JListFiles.getSelectedIndex());
        JListFiles.setListData(listFileName.toArray());
    }//GEN-LAST:event_BRemoveFileActionPerformed

    private void JListFilesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_JListFilesValueChanged
        if (JListFiles.getSelectedIndex() != -1) {
            BRemoveFile.setEnabled(true);
        } else {
            BRemoveFile.setEnabled(false);
        }
    }//GEN-LAST:event_JListFilesValueChanged

    private void unzipZipFile(File source) {
        FileInputStream fis = null;
        try {
            File dest = new File(source.getParent() + File.separator
                    + source.getName().substring(0, source.getName().lastIndexOf('.')));
            fis = new FileInputStream(source.getAbsolutePath());
            try (BufferedInputStream buffis = new BufferedInputStream(fis)) {
                zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(source)));
                while ((entry = zis.getNextEntry()) != null) {
                    //                lblInfos.setText(entry.getName());
                    if (entry.isDirectory()) {
                        File dossierEntry = new File(dest.getAbsolutePath()
                                + File.separator + entry.getName() + File.separator);
                        dossierEntry.mkdirs();
                    } else {
                        buffos = new BufferedOutputStream(new FileOutputStream(
                                dest.getAbsolutePath() + File.separator + entry.getName()), BUFFER);
                        while ((count = zis.read(data, 0, BUFFER)) != -1) {
                            buffos.write(data, 0, count);
                            progressValue += (count);
                            ProgressBar.setValue(progressValue);
                        }
                        buffos.close();
                        buffos.flush();
                    }
                }
                zis.closeEntry();
                zis.close();
            }
            doneSuccessfully = true;
        } catch (IOException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
            doneSuccessfully = false;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        e.getMessage(), null, null, e, Level.SEVERE, null));
                doneSuccessfully = false;
            }
        }
    }

    private void unzipGZipFile(File source) {
        try {
            File dest = new File(source.getParent() + File.separator
                    + source.getName().substring(0, source.getName().lastIndexOf('.')));
            dest.mkdir();
            gzis = new GZIPInputStream(new BufferedInputStream(new FileInputStream(source)));
            buffos = new BufferedOutputStream(new FileOutputStream(dest));
            while ((count = gzis.read(data, 0, BUFFER)) != -1) {
                buffos.write(data, 0, count);
                progressValue += count;
                ProgressBar.setValue(progressValue);
            }
            buffos.flush();
            buffos.close();
            gzis.close();
            doneSuccessfully = true;
        } catch (IOException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
            doneSuccessfully = false;
        }
    }

    private void disableComponent() {
        timer.stop();
        BUnzip.setEnabled(false);
        BAddFiles.setEnabled(false);
        ChBOpenFolder.setEnabled(false);
        JListFiles.setSelectedIndex(-1);
    }

    private void enableComponent() {
        timer.start();
        BUnzip.setEnabled(true);
        BAddFiles.setEnabled(true);
        ChBOpenFolder.setEnabled(true);
        JListFiles.setSelectedIndex(-1);
        ProgressBar.setValue(0);
        if (doneSuccessfully) {
            JOptionPane.showMessageDialog(parent, I18N.get("UnZipTool.extraction-done"));
//            if (ChBOpenFolder.isSelected()) {
//
//            }
        }
    }

    private void displayFilesChoosen(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).toLowerCase().endsWith("zip")) {
                JOptionPane.showMessageDialog(parent, new StringBuilder(I18N.get("UnZipTool.format-not-accaptable"))
                        .append("\"").append(new File(list.get(i)).getName()).append("\"").toString(),
                        I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
            } else {
                ListFiles.add(list.get(i));
                listFileName.add(new File(list.get(i)).getName());
            }
        }
        JListFiles.setListData(listFileName.toArray());
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                UnZipTool dialog = new UnZipTool(new javax.swing.JFrame(), true, new ArrayList());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BAddFiles;
    private com.jidesoft.swing.JideButton BCancel;
    private com.jidesoft.swing.JideButton BRemoveFile;
    private com.jidesoft.swing.JideButton BUnzip;
    private javax.swing.JCheckBox ChBOpenFolder;
    private javax.swing.JList JListFiles;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panAll;
    // End of variables declaration//GEN-END:variables
    JFrame parent;
    Timer timer;
    ArrayList<String> ListFiles = new ArrayList<>();
    ArrayList<String> listFileName = new ArrayList<>();
    private ZipInputStream zis;
    private GZIPInputStream gzis;
    private BufferedOutputStream buffos;
    private ZipEntry entry;
    private final int BUFFER = 4096;
    private byte[] data = new byte[BUFFER];
    private int count, progressValue, progressValMax;
    boolean doneSuccessfully = false;
}
