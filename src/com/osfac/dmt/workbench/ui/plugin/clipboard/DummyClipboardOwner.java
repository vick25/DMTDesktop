package com.osfac.dmt.workbench.ui.plugin.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

public class DummyClipboardOwner implements ClipboardOwner {

    public DummyClipboardOwner() {
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
