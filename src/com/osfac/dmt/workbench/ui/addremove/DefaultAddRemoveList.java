package com.osfac.dmt.workbench.ui.addremove;

import com.osfac.dmt.workbench.ui.InputChangedFirer;
import com.osfac.dmt.workbench.ui.InputChangedListener;
import com.osfac.dmt.workbench.ui.JListTypeAheadKeyListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DefaultAddRemoveList extends JPanel implements AddRemoveList {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JList list = new JList();
    private DefaultAddRemoveListModel model;
    private InputChangedFirer inputChangedFirer = new InputChangedFirer();
    private Border border1;

    public DefaultAddRemoveList() {
        this(new DefaultListModel());
    }

    public void add(MouseListener listener) {
        list.addMouseListener(listener);
    }

    public DefaultAddRemoveList(DefaultListModel listModel) {
        model = new DefaultAddRemoveListModel(listModel);
        list.setModel(listModel);
        list.addKeyListener(new JListTypeAheadKeyListener(list));
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                inputChangedFirer.fire();
            }
        });

        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSelectedItems(Collection items) {
        ArrayList indicesToSelect = new ArrayList();

        for (Iterator i = items.iterator(); i.hasNext();) {
            Object item = (Object) i.next();
            int index = getModel().getItems().indexOf(item);

            if (index == -1) {
                continue;
            }

            indicesToSelect.add(new Integer(index));
        }

        int[] indexArray = new int[indicesToSelect.size()];

        for (int i = 0; i < indicesToSelect.size(); i++) {
            Integer index = (Integer) indicesToSelect.get(i);
            indexArray[i] = index.intValue();
        }

        list.setSelectedIndices(indexArray);
    }

    public AddRemoveListModel getModel() {
        return model;
    }

    public void add(InputChangedListener listener) {
        inputChangedFirer.add(listener);
    }

    public JList getList() {
        return list;
    }

    public List getSelectedItems() {
        return Arrays.asList(list.getSelectedValues());
    }

    void jbInit() throws Exception {
        border1 = new EtchedBorder(EtchedBorder.RAISED, new Color(0, 0, 51),
                new Color(0, 0, 25));
        this.setLayout(borderLayout1);
        this.add(list, BorderLayout.CENTER);
    }
}
