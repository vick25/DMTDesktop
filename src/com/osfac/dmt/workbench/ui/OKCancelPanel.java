package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.I18N;

public class OKCancelPanel extends ButtonPanel {

    public OKCancelPanel() {
        super(new String[]{I18N.get("ui.OKCancelPanel.ok"), I18N.get("ui.OKCancelPanel.cancel")});
    }

    public boolean wasOKPressed() {
        return getSelectedButton() == getButton(I18N.get("ui.OKCancelPanel.ok"));
    }

    public void setOKPressed(boolean okPressed) {
        if (okPressed) {
            setSelectedButton(getButton(I18N.get("ui.OKCancelPanel.ok")));
        } else {
            setSelectedButton(null);
        }
    }

    public void setOKEnabled(boolean okEnabled) {
        getButton(I18N.get("ui.OKCancelPanel.ok")).setEnabled(okEnabled);
    }

    public void setCancelVisible(boolean cancelVisible) {
        if (cancelVisible && !innerButtonPanel.isAncestorOf(getButton(I18N.get("ui.OKCancelPanel.cancel")))) {
            innerButtonPanel.add(getButton(I18N.get("ui.OKCancelPanel.cancel")), null);
        }

        if (!cancelVisible && innerButtonPanel.isAncestorOf(getButton(I18N.get("ui.OKCancelPanel.cancel")))) {
            innerButtonPanel.remove(getButton(I18N.get("ui.OKCancelPanel.cancel")));
        }
    }
}
