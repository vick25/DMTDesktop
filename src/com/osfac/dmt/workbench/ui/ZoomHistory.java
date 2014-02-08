package com.osfac.dmt.workbench.ui;

import com.vividsolutions.jts.geom.Envelope;
import java.util.ArrayList;

public class ZoomHistory {

    private LayerViewPanel layerViewPanel;
    private ArrayList envelopes = new ArrayList();
    private int currentIndex = -1;
    private boolean adding = true;

    public ZoomHistory(LayerViewPanel layerViewPanel) {
        this.layerViewPanel = layerViewPanel;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    public void add(Envelope envelope) {
        if (!adding) {
            return;
        }

        envelopes.subList(currentIndex + 1, envelopes.size()).clear();
        envelopes.add(envelope);
        currentIndex = envelopes.size() - 1;
    }

    public Envelope next() {
        currentIndex++;

        return getCurrentEnvelope();
    }

    public Envelope prev() {
        currentIndex--;

        return getCurrentEnvelope();
    }

    private Envelope getCurrentEnvelope() {
        return (Envelope) envelopes.get(currentIndex);
    }

    public boolean hasPrev() {
        return currentIndex > 0;
    }

    public boolean hasNext() {
        return currentIndex < (envelopes.size() - 1);
    }
}
