package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.util.StringUtil;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

    FlowLayout flowLayout1 = new FlowLayout();
    GridLayout gridLayout1 = new GridLayout();
    JPanel innerButtonPanel = new JPanel();

    public JButton getButton(String text) {
        return (JButton) textToButtonMap.get(text);
    }
    private ArrayList actionListeners = new ArrayList();
    // null if none selected
    protected JButton selectedButton;
    private HashMap textToButtonMap = new HashMap();

    /**
     * @param buttonText use ampersands to denote mnemonics
     */
    public ButtonPanel(String[] buttonText) {
        innerButtonPanel.setLayout(gridLayout1);
        this.setLayout(flowLayout1);
        gridLayout1.setVgap(5);
        gridLayout1.setHgap(5);
        this.add(innerButtonPanel, null);
        for (int i = 0; i < buttonText.length; i++) {
            innerButtonPanel.add(createButton(buttonText[i]), null);
        }
    }

    private JButton createButton(String buttonText) {
        final JButton button = new JButton(StringUtil.replaceAll(buttonText,
                "&", ""));
        button.setMnemonic(buttonText.indexOf("&") > -1 ? buttonText
                .charAt(buttonText.indexOf("&") + 1) : buttonText.charAt(0));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedButton = button;
                fireActionPerformed();
            }
        });
        textToButtonMap.put(button.getText(), button);
        return button;
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    /**
     *
     * @return null if no button selected
     */
    public JButton getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(JButton selectedButton) {
        this.selectedButton = selectedButton;
    }
}
