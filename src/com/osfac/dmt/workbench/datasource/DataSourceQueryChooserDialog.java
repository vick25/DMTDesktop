package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.OKCancelPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openjump.core.CheckOS;

/**
 * Contains the various DataSourceQueryChooser panels, regardless of whether they are for files,
 * databases, web services, or other kinds of DataSources.
 * <p>
 * A bit confusing for files, as there are two "format" comboboxes for the user to choose from: one
 * for the DataSource type, and another for the file extension. In the future, file DataSources may
 * have their own dialog, eliminating the first combobox.
 */
public class DataSourceQueryChooserDialog extends JDialog {

    private CardLayout cardLayout = new CardLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private JPanel formatPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JComboBox formatComboBox = new JComboBox();
    private JLabel formatLabel = new JLabel() {
        {
            setDisplayedMnemonic('F');
            setLabelFor(formatComboBox);
        }
    };
    private HashMap componentToNameMap = new HashMap();
    private OKCancelPanel okCancelPanel = new OKCancelPanel();
    public static int LOADDIALOG = 1;
    public static int SAVEDIALOG = 2;
    private int dialogTask = 0;

    public DataSourceQueryChooserDialog(Collection dataSourceQueryChoosers,
            Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        init(dataSourceQueryChoosers);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                okCancelPanel.setOKPressed(false);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //User may have hit OK, got a validation-error dialog, then hit the
                //X button. [Bob Boseko]
                okCancelPanel.setOKPressed(false);
            }
        });

        //Set the selected item to trigger the event that sets the panel. [Bob Boseko]
        formatComboBox.setSelectedItem(formatComboBox.getItemAt(0));
    }

    private void init(Collection dataSourceQueryChoosers) {
        //Some components may be shared, so use a Set. [Bob Boseko]
        HashSet components = new HashSet();
        for (Iterator i = dataSourceQueryChoosers.iterator(); i.hasNext();) {
            DataSourceQueryChooser chooser = (DataSourceQueryChooser) i.next();
            formatComboBox.addItem(chooser);
            components.add(chooser.getComponent());
        }

        int j = 0;
        for (Iterator i = components.iterator(); i.hasNext();) {
            Component component = (Component) i.next();

            //Can't use DataSourceQueryChooser name because several DataSourceQueryChoosers may
            //share a component (e.g. FileDataSourceQueryChooser). [Bob Boseko]
            j++;
            componentToNameMap.put(component, I18N.get("datasource.DataSourceQueryChooserDialog.card") + " " + j);
            mainPanel.add(component, name(component));
        }
    }

    private String name(Component component) {
        return (String) componentToNameMap.get(component);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout2);
        formatPanel.setLayout(gridBagLayout1);
        formatPanel.setBorder(BorderFactory.createEtchedBorder());
        formatLabel.setText(I18N.get("datasource.DataSourceQueryChooserDialog.format"));
        formatComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatComboBox_actionPerformed(e);
            }
        });
        okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okCancelPanel_actionPerformed(e);
            }
        });

        this.getContentPane().add(mainPanel, BorderLayout.NORTH);
        this.getContentPane().add(formatPanel, BorderLayout.CENTER);
        this.getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        formatPanel.add(formatComboBox,
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(16, 4, 16, 4), 0, 0));
        formatPanel.add(formatLabel,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(16, 4, 16, 4), 0, 0));
    }

    /**
     * @return true if the user hit OK; false if the user hit Cancel or the Close Window button.
     */
    public boolean wasOKPressed() {
        return okCancelPanel.wasOKPressed();
    }

    public void setOKPressed() {
        //
        // It is important to call setOKPressed before calling isInputValid
        // otherwise we run into an infinite loop of actionPerformed calls.
        //
        okCancelPanel.setOKPressed(true);

        if (getCurrentChooser().isInputValid()) {
            setVisible(false);
        }
    }

    void formatComboBox_actionPerformed(ActionEvent e) {
        cardLayout.show(mainPanel, name(getCurrentChooser().getComponent()));
    }

    public DataSourceQueryChooser getCurrentChooser() {
        return (DataSourceQueryChooser) formatComboBox.getSelectedItem();
    }

    void okCancelPanel_actionPerformed(ActionEvent e) {
        /*
         if (!okCancelPanel.wasOKPressed() ||
         getCurrentChooser().isInputValid()) {
         setVisible(false);
         }
         */
        if (!okCancelPanel.wasOKPressed()) { //cancel case
            setVisible(false);
        } else {
            if (this.getDialogTask() == DataSourceQueryChooserDialog.LOADDIALOG) {
                //--sstein: leave out validation - because it returns always "false" on Mac-OSX ?
                //          because the getCurrentChooser() returns has a null pointer
                //System.out.println("validate input:" + getCurrentChooser().isInputValid());
                if ((okCancelPanel.wasOKPressed()) && (CheckOS.isMacOsx())) {
                    //System.out.println("this is a mac and we load data");
                    okCancelPanel.setOKPressed(true);
                    setVisible(false);
                } else {
                    if (getCurrentChooser().isInputValid()) {
                        setVisible(false);
                    }
                }
            } else { //Now we use the dialog for saving
                if (getCurrentChooser().isInputValid()) {
                    setVisible(false);
                }
            }
        }
    }

    public String getSelectedFormat() {
        return formatComboBox.getSelectedItem().toString();
    }

    public void setSelectedFormat(String format) {
        for (int i = 0; i < formatComboBox.getItemCount(); i++) {
            DataSourceQueryChooser chooser = (DataSourceQueryChooser) formatComboBox.getItemAt(i);
            if (chooser.toString().equals(format)) {
                formatComboBox.setSelectedIndex(i);

                return;
            }
        }
    }

    public int getDialogTask() {
        return dialogTask;
    }

    public void setDialogTask(int dialogTask) {
        this.dialogTask = dialogTask;
    }
}
