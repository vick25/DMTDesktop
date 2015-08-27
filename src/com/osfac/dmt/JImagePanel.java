package com.osfac.dmt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JImagePanel extends JPanel {

    private static final long serialVersionUID = 2724980460740151616L;
    private Image _image;
    private int _x;
    private int _y;
    private boolean _autoSize;
    private boolean _keepAspect;

    public JImagePanel(File file, int x, int y, boolean autoSize, boolean keepAspect) throws IOException {
        super();
        setImage(ImageIO.read(file));
        setX(x);
        setY(y);
        setAutoSize(autoSize);
        setKeepAspect(keepAspect);
    }

    public JImagePanel(File file, int x, int y, boolean autoSize) throws IOException {
        this(file, x, y, true, false);
    }

    public JImagePanel(File file, int x, int y) throws IOException {
        this(file, x, y, true);
    }

    public JImagePanel(File file) throws IOException {
        this(file, 0, 0, true);
    }

    public JImagePanel(Image image, int x, int y, boolean autoSize, boolean keepAspect) {
        super();
        setImage(image);
        setX(x);
        setY(y);
        setAutoSize(autoSize);
        setKeepAspect(keepAspect);
    }

    public JImagePanel(Image image, int x, int y, boolean autoSize) {
        this(image, x, y, true, false);
    }

    public JImagePanel(Image image, int x, int y) {
        this(image, x, y, true);
    }

    public JImagePanel(Image image) {
        this(image, 0, 0, true);
    }

    public JImagePanel() {
        super();
        setX(0);
        setY(0);
        setImage(null);
        setAutoSize(false);
    }

    public Image getImage() {
        return _image;
    }

    public final void setImage(Image image) {
        _image = image;
    }

    public final void setX(int x) {
        _x = x;
    }

    public final void setY(int y) {
        _y = y;
    }

    public final void setAutoSize(boolean autoSize) {
        _autoSize = autoSize;
    }

    public final void setKeepAspect(boolean keepAspect) {
        _keepAspect = keepAspect;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_image != null) {
            if (!_autoSize) {
                g.drawImage(_image, _x, _y, _image.getWidth(null), _image.getHeight(null), null);
            } else {
                Graphics2D g2d = (Graphics2D) g;
                Double scaleWidth = (double) getWidth() / (double) _image.getWidth(null);
                Double scaleHeight = (double) getHeight() / (double) _image.getHeight(null);
                if (_keepAspect) {
                    if (scaleWidth > scaleHeight) {
                        scaleWidth = scaleHeight;
                    } else {
                        scaleHeight = scaleWidth;
                    }
                }
                g2d.scale(scaleWidth, scaleHeight);
                g2d.drawImage(_image, _x, _y, null);
            }
        }
    }
}
