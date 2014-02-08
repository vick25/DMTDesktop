package org.openjump.core.ui.swing.factory.field;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.ui.FileNamePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComponent;
import org.openjump.swing.factory.field.FieldComponentFactory;
import org.openjump.swing.listener.ValueChangeEvent;
import org.openjump.swing.listener.ValueChangeListener;

public class FileFieldComponentFactory implements FieldComponentFactory {

    private WorkbenchContext workbenchContext;

    public FileFieldComponentFactory(final WorkbenchContext workbenchContext) {
        this.workbenchContext = workbenchContext;
    }

    public Object getValue(final JComponent component) {
        if (component instanceof FileNamePanel) {
            FileNamePanel fileNamePanel = (FileNamePanel) component;
            return fileNamePanel.getSelectedFile();
        }
        return null;
    }

    public void setValue(JComponent component, Object value) {
        if (component instanceof FileNamePanel) {
            FileNamePanel fileNamePanel = (FileNamePanel) component;
            File file = null;
            if (value != null) {
                file = new File(value.toString());
            }
            fileNamePanel.setSelectedFile(file);
        }
    }

    public JComponent createComponent() {
        FileNamePanel fileNamePanel = new FileNamePanel(
                workbenchContext.getErrorHandler());
        fileNamePanel.setUpperDescription("");
        return fileNamePanel;
    }

    public JComponent createComponent(final ValueChangeListener listener) {
        final FileNamePanel fileNamePanel = new FileNamePanel(
                workbenchContext.getErrorHandler());
        fileNamePanel.setUpperDescription("");
        fileNamePanel.addBrowseListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = fileNamePanel.getSelectedFile();
                listener.valueChanged(new ValueChangeEvent(fileNamePanel,
                        file.getAbsolutePath()));
            }
        });
        return fileNamePanel;
    }
}
