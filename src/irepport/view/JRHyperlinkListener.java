package irepport.view;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintHyperlink;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHyperlinkListener.java 3035 2009-08-27 12:05:03Z teodord $
 */
public interface JRHyperlinkListener {

    /**
     *
     */
    public void gotoHyperlink(JRPrintHyperlink hyperlink) throws JRException;
}
