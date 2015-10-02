package com.osfac.dmt;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.OverlayableUtils;
import com.jidesoft.swing.StyledLabelBuilder;
import com.osfac.dmt.authen.ChangeIP;
import com.osfac.dmt.authen.PanAuthen;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.setting.SettingOptionsDialog;
import com.osfac.dmt.setting.panel.OtherFeatures;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class Config {

    public Config() {
        setDefaultLocale(pref.getInt(SettingKeyFactory.Language.INDEX, 0));
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                if (isLiteVersion()) {
                    host = "127.0.0.1";
                    username = "root";
                    password = "";
                } else {
                    host = "192.168.1.250";
                    username = "admin";
                    password = "OsfacLab01";
                    host = pref.get(SettingKeyFactory.Connection.HOST, host);
                }
                con = DriverManager.getConnection(new StringBuilder("jdbc:mysql://").append(host).
                        append("/").append(DATABASE).toString(), username, password);
                optionsDialog = SettingOptionsDialog.showOptionsDialog();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                withoutError = true;
                dateFormat = new SimpleDateFormat(I18N.get("language.format.date"), new DateFormatSymbols());
            } catch (SQLException e) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        new StringBuilder(I18N.get("com.osfac.dmt.Config.Database-Unable")).append(e.getMessage()).toString(),
                        null, null, e, Level.SEVERE, null));
                if (isLiteVersion()) {
                    System.exit(0);
                } else {
                    if (JOptionPane.showConfirmDialog(null, I18N.get("com.osfac.dmt.Config.Database-Wrong-IP-Address"),
                            I18N.get("Text.Confirm"), 0) == 0) {
                        new ChangeIP(DMTWorkbench.frame, true, true).setVisible(true);
                    } else {
                        System.exit(0);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
            System.exit(0);
        }
    }

    public static void dbConnect() {
        setDefaultLocale(pref.getInt(SettingKeyFactory.Language.INDEX, 0));
        try {
            host = "192.168.1.250";
            username = "admin";
            password = "OsfacLab01";
            host = pref.get(SettingKeyFactory.Connection.HOST, host);
            con = DriverManager.getConnection(new StringBuilder("jdbc:mysql://").append(host).append("/").append(DATABASE).toString(), username, password);
            if (!withoutError) {
                optionsDialog = SettingOptionsDialog.showOptionsDialog();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                dateFormat = new SimpleDateFormat(I18N.get("language.format.date"), new DateFormatSymbols());
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    new StringBuilder(I18N.get("com.osfac.dmt.Config.Database-Unable")).append(e.getMessage()).toString(), null, null, e, Level.SEVERE, null));
            if (isLiteVersion()) {
                System.exit(0);
            } else {
                if (JOptionPane.showConfirmDialog(null, I18N.get("com.osfac.dmt.Config.Database-Wrong-IP-Address"),
                        I18N.get("Text.Confirm"), 0) == 0) {
                    new ChangeIP(DMTWorkbench.frame, true, true).setVisible(true);
                } else {
                    System.exit(0);
                }
            }
        }
    }

    public static boolean isLiteVersion() {
        return WorkbenchFrame.TypeOfVersion.equals(LITE_VERSION);
    }

    public static boolean isFullVersion() {
        return WorkbenchFrame.TypeOfVersion.equals(FULL_VERSION);
    }

    public static boolean isSimpleUser() {
        return WorkbenchFrame.idUser != 7;
    }

    public static boolean isAdministrator() {
        return WorkbenchFrame.idUser == 7;
    }

    public static void setDefaultLocale(int index) {
        if (index == 0) {
            LOCALE = Locale.US;
        } else if (index == 1) {
            LOCALE = Locale.FRANCE;
        } else {
            LOCALE = Locale.US;
        }
        Locale.setDefault(LOCALE);
        JComponent.setDefaultLocale(LOCALE);
    }

    public static double getImageSize(String idImage) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT size FROM dmt_image WHERE id_image = ?");
            ps.setString(1, idImage);
            ResultSet ress = ps.executeQuery();
            while (ress.next()) {
                return ress.getDouble(1);
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return 0.0;
    }

    public static String convertOctetToAnyInDouble(long valeur) {
        if ((valeur >= 1024) && (valeur < (1024 * 1024))) {
            return new StringBuilder().append(df.format((double) valeur / 1024)).append(" Ko").toString();
        } else if ((valeur >= (1024 * 1024)) && (valeur < (1024 * 1024 * 1024))) {
            return new StringBuilder().append(df.format((double) valeur / (1024 * 1024))).append(" Mo").toString();
        } else if ((valeur >= (1024 * 1024 * 1024))) {
            return new StringBuilder().append(df.format((double) valeur / (1024 * 1024 * 1024))).append(" Go").toString();
        } else if ((valeur >= (1024 * 1024 * 1024 * 1024))) {
            return new StringBuilder().append(df.format((double) valeur / (1024 * 1024 * 1024 * 1024))).append(" To").toString();
        } else {
            return new StringBuilder().append(df.format((double) valeur)).append(" Octets").toString();
        }
    }

    public static boolean valideEmail(JTextField txtEmail) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(txtEmail.getText().toUpperCase());
        if (m.matches()) {
            return m.matches();
        } else {
            JOptionPane.showMessageDialog(DMTWorkbench.frame, I18N.get("com.osfac.dmt.Config.Email-Not-Valid"),
                    I18N.get("com.osfac.dmt.Config.Error"), JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
    }

    public static void saveIPValue(String IPAddress) {
        pref.put(SettingKeyFactory.Connection.HOST, IPAddress);
        String quickLookSource = Config.pref.get(SettingKeyFactory.OtherFeatures.HOST, new StringBuilder("http://")
                .append(IPAddress).toString());
        if (!quickLookSource.contains("www.osfac.net")) {
            Config.pref.put(SettingKeyFactory.OtherFeatures.HOST, new StringBuilder("http://")
                    .append(IPAddress).toString());
        }
        if (withoutError) {
            OtherFeatures.RBHostProvider.setText(new StringBuilder(I18N.get("com.osfac.dmt.Config.Server-Text")).
                    append(" ").append("http://").append(IPAddress).toString());
            OtherFeatures.txtIP.setText(IPAddress);
        }
        if (!ChangeIP.useAtBeginning) {
            PanAuthen.BServerIP.setText(new StringBuilder(I18N.get("com.osfac.dmt.Config.Server-Text")).
                    append(" ").append(IPAddress).toString());
        }
        host = IPAddress;

        //Change the Workbench Frame Title upon changing the Server IP address [Vick]
        if (DMTWorkbench.frame != null) {
            DMTWorkbench.frame.setTitle(new StringBuilder(I18N.get("ui.WorkbenchFrame.title")).append(" ")
                    .append(I18N.get("JUMPWorkbench.version.number")).append(" ")
                    .append(WorkbenchFrame.TypeOfVersion).append("   [").append(I18N.get("Text.Server-IP-Address-text"))
                    .append(" ").append(Config.host).append("]").toString());
        }
    }

    public static String encrypt(String s) {
        int code = (KEYCRYPTO.hashCode() % 5 == 0 ? (KEYCRYPTO.length() % 2 == 1 ? -1 : 1) * 3 : (KEYCRYPTO.length() % 2 == 1 ? -1 : 1) * KEYCRYPTO.hashCode() % 5);
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] - code);
        }
        return new String(chars);
    }

    public static String decrypt(String s) {
        int code = (KEYCRYPTO.hashCode() % 5 == 0 ? (KEYCRYPTO.length() % 2 == 1 ? -1 : 1) * 3 : (KEYCRYPTO.length() % 2 == 1 ? -1 : 1) * KEYCRYPTO.hashCode() % 5);
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] + code);
        }
        return new String(chars);
    }

    public static String getDefaultDirectory() {
        return pref.get(SettingKeyFactory.DefaultProperties.DEFAULTDIRECTORY, defaultDirectory);
    }

    public static void setDefaultDirectory(String dir) {
        pref.put(SettingKeyFactory.DefaultProperties.DEFAULTDIRECTORY, dir);
    }

    public static String withoutAccent(String source) {
        return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
    }

    public static String requiredField(String text) {
        return new StringBuilder("<html>").append(text).append("<font color=").append("red")
                .append(">*</font></html>").toString();
    }

    public static String capitalFirstLetter(String s) {
        if (s.isEmpty()) {
            return s;
        } else {
            return new StringBuilder().append(s.substring(0, 1).toUpperCase()).append(s.substring(1)).toString();
        }
    }

    public static void centerTableHeadAndBold(JTable table) {
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 11));
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
    }

    public static String monthInt(String mois) {
        String moisChiffre = "";
        if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.JANUARY"))) {
            moisChiffre = "01";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.FEBRUARY"))) {
            moisChiffre = "02";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.MARCH"))) {
            moisChiffre = "03";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.APRIL"))) {
            moisChiffre = "04";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.MAY"))) {
            moisChiffre = "05";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.JUNE"))) {
            moisChiffre = "06";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.JULY"))) {
            moisChiffre = "07";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.AUGUST"))) {
            moisChiffre = "08";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.SEPTEMBER"))) {
            moisChiffre = "09";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.OCTOBER"))) {
            moisChiffre = "10";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.NOVEMBER"))) {
            moisChiffre = "11";
        } else if (mois.equalsIgnoreCase(I18N.get("com.osfac.dmt.Config.DECEMBER"))) {
            moisChiffre = "12";
        }
        return moisChiffre;
    }

    public static String monthString(int mois) {
        String moisLettre = "";
        if (mois == 1) {
            moisLettre = I18N.get("com.osfac.dmt.Config.JANUARY");
        } else if (mois == 2) {
            moisLettre = I18N.get("com.osfac.dmt.Config.FEBRUARY");
        } else if (mois == 3) {
            moisLettre = I18N.get("com.osfac.dmt.Config.MARCH");
        } else if (mois == 4) {
            moisLettre = I18N.get("com.osfac.dmt.Config.APRIL");
        } else if (mois == 5) {
            moisLettre = I18N.get("com.osfac.dmt.Config.MAY");
        } else if (mois == 6) {
            moisLettre = I18N.get("com.osfac.dmt.Config.JUNE");
        } else if (mois == 7) {
            moisLettre = I18N.get("com.osfac.dmt.Config.JULY");
        } else if (mois == 8) {
            moisLettre = I18N.get("com.osfac.dmt.Config.AUGUST");
        } else if (mois == 9) {
            moisLettre = I18N.get("com.osfac.dmt.Config.SEPTEMBER");
        } else if (mois == 10) {
            moisLettre = I18N.get("com.osfac.dmt.Config.OCTOBER");
        } else if (mois == 11) {
            moisLettre = I18N.get("com.osfac.dmt.Config.NOVEMBER");
        } else if (mois == 12) {
            moisLettre = I18N.get("com.osfac.dmt.Config.DECEMBER");
        }
        return moisLettre;
    }

    public static String correctionBarre(String texte) {
        if (texte.contains("\\")) {
            texte = texte.replace("\\", "\\\\");
        }
        return texte;
    }

    public static String correctText(String s) {
        s = correctionApostrophe(s);
        if (s.isEmpty()) {
            return s;
        } else {
            return new StringBuilder().append(s.substring(0, 1).toUpperCase()).append(s.substring(1)).toString();
        }
    }

    public static String correctionApostrophe(String texte) {
        texte = correctionBarre(texte);
        if (texte.contains("\'")) {
            texte = texte.replace("\'", "\\'");
        }
        return texte;
    }

    public static String[] getSex() {
        String[] tab = {I18N.get("language.sex-female"), I18N.get("language.sex-male")};
        return tab;
    }

    public static String[] getAllCountry() {
        String[] tab = new String[]{"AFGHANISTAN", "ALAND", "ALBANIA", "ALGERIA", "AMERICAN SAMOA", "ANDORRA",
            "ANGOLA", "ARGENTINA", "ARMENIA", "ARUBA", "AUSTRALIA", "AUSTRIA", "AZERBAIJAN", "BAHAMAS",
            "BAHRAIN", "BANGLADESH", "BARBADOS", "BELARUS", "BELGIUM", "BELIZE", "BENIN", "BERMUDA",
            "BHUTAN", "BOLIVIA", "BOSNIA AND HERZEGOVINA", "BOTSWANA", "BOUVET ISLAND", "BRAZIL", "BRITISH",
            "BRUNEI DARUSSALAM", "BULGARIA", "BURKINA FASO", "BURUNDI", "CAMBODIA", "CAMEROON", "CANADA",
            "CAPE VERDE", "CAYMAN ISLANDS", "CENTRAL AFRICAN REPUBLIC", "CHAD", "CHILE", "CHINA", "CHRISTMAS ISLAND",
            "COCOS ISLANDS", "COMOROS", "CONGO", "COOK ISLANDS", "COSTA RICA", "COTE D'IVOIRE", "CROATIA",
            "CUBA", "CURACAO", "CYPRUS", "CZECH REPUBLIC", "DENMARK", "DJIBOUTI", "DOMINIQUE", "DR CONGO",
            "ECUADOR", "EGYPT", "ERITREA", "ESTONIA", "ETHIOPIA", "FAROE ISLANDS", "FIJI", "FINLAND",
            "FRANCE", "GABON", "GAMBIA", "GEORGIA", "GERMANY", "GHANA", "GIBRALTAR", "GREECE", "GREENLAND",
            "GRENADA", "GUADELOUPE", "GUAM", "GUATEMALA", "GUERNSEY", "GUINEA", "GUINEA EQ.", "GUINEA-BISSAU",
            "GUYANA", "HAITI", "HONDURAS", "HONG KONG", "HUNGARY", "ICELAND", "INDIA", "INDONESIA", "IRAN",
            "IRAQ", "IRELAND", "ISLE OF MAN", "ISRAEL", "ITALY", "JAMAICA", "JAPAN", "JERSEY", "JORDAN",
            "KAZAKHSTAN", "KENYA", "KIRIBATI", "KUWAIT", "KYRGYZSTAN", "LAOS", "LATVIA", "LEBANON", "LESOTHO",
            "LIBERIA", "LIBYA", "LIECHTENSTEIN", "LITHUANIA", "LUXEMBOURG", "MACEDONIA", "MADAGASCAR",
            "MALAWI", "MALAYSIA", "MALDIVES", "MALI", "MALTA", "MARTINIQUE", "MAURICE", "MAURITANIA", "MAYOTTE",
            "MEXICO", "MOLDOVA", "MONACO", "MONGOLIA", "MONTENEGRO", "MONTSERRAT", "MOROCCO", "MOZAMBIQUE",
            "MYANMAR", "NAMIBIA", "NAURU", "NEPAL", "NETHERLANDS", "NEW CALEDONIA", "NEW ZEALAND", "NICARAGUA",
            "NIGER", "NIGERIA", "NORTH KOREA", "NORWAY", "OMAN", "PAKISTAN", "PALAU", "PALESTINE", "PANAMA",
            "PARAGUAY", "PERU", "PHILIPPINES", "PITCAIRN", "POLAND", "PORTUGAL", "PUERTO RICO", "QATAR",
            "R. DOMINICAN", "ROMANIA", "RUSSIA", "RWANDA", "SALOMON", "SAMOA", "SAO TOME AND PRINCIPE",
            "SAUDI ARABIA", "SENEGAL", "SERBIA", "SEYCHELLES", "SIERRA LEONE", "SINGAPORE", "SLOVAKIA",
            "SLOVENIA", "SOMALIA", "SOUTH AFRICA", "SOUTH KOREA", "SPAIN", "SRI LANKA", "SUDAN", "SURINAME", "SWAZILAND",
            "SWEDEN", "SWITZERLAND", "SYRIA", "TAIWAN", "TAJIKISTAN", "TANZANIA", "THAILAND", "THE MEETING",
            "TIMOR-LESTE", "TOGO", "TOKELAU", "TONGA", "TRINIDAD AND TOBAGO", "TUNISIA", "TURKEY", "TURKMENISTAN",
            "TUVALU", "U.S.A", "UGANDA", "UKRAINE", "UNITED ARAB EMIRATES", "UNITED KINGDOM", "URUGUAY",
            "UZBEKISTAN", "VANUATU", "VENEZUELA", "VIET NAM", "WALLIS AND FUTUNA", "WESTERN SAHARA",
            "YEMEN", "ZAMBIA", "ZIMBABWE"};
        return tab;
    }

    public static Color getColorFromKey(String value) {
        String tab[] = value.split(", ");
        return new Color(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }

    public static void setEditorTable(String[] tab, JTable table, int column) {
        JComboBox comboBox = new JComboBox(tab);
        DefaultCellEditor editor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(column).setCellEditor(editor);
    }

    public static void setEditorTable(ArrayList vect, JTable table, int column) {
        JComboBox comboBox = new JComboBox(vect.toArray());
        comboBox.setEditable(true);
        DefaultCellEditor editor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(column).setCellEditor(editor);
    }

    public static DefaultOverlayable createOverLayable(final JTextArea textArea, String text) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        final DefaultOverlayable olay = new DefaultOverlayable(new JScrollPane(textArea));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (textArea.getDocument().getLength() > 0) {
                    olay.setOverlayVisible(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (textArea.getDocument().getLength() == 0) {
                    olay.setOverlayVisible(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                olay.setOverlayVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                olay.setOverlayVisible(textArea.getDocument().getLength() == 0);
            }
        });
        olay.addOverlayComponent(StyledLabelBuilder.createStyledLabel(new StringBuilder("{").append(text)
                .append(":f:gray}").toString()));
        return olay;
    }

    public static DefaultOverlayable createOverLayable(DefaultOverlayable oLay, String icon, int location,
            String toolTip, final String help) {
        final JLabel labIcon = new JLabel(OverlayableUtils.getPredefinedOverlayIcon(icon));
        labIcon.setToolTipText(toolTip);
        labIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JidePopup popup = new JidePopup();
                JLabel label = new JLabel(help);
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                popup.add(label);
                popup.showPopup(new Insets(-5, 0, -5, 0), labIcon);
            }
        });
        DefaultOverlayable overlayableCode = new DefaultOverlayable(oLay, labIcon, location);
        return overlayableCode;
    }

    public static DefaultOverlayable createOverLayableIcon(JComponent component, String icon, int location,
            String toolTip, final String help) {
        final JLabel labIcon = new JLabel(OverlayableUtils.getPredefinedOverlayIcon(icon));
        labIcon.setToolTipText(toolTip);
        labIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JidePopup popup = new JidePopup();
                JLabel label = new JLabel(help);
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                popup.add(label);
                popup.showPopup(new Insets(-5, 0, -5, 0), labIcon);
            }
        });
        DefaultOverlayable overlayableCode = new DefaultOverlayable(component, labIcon, location);
        return overlayableCode;
    }
    public static String defaultDirectory = new JFileChooser().getCurrentDirectory().getAbsolutePath();
    public static String lookAndFeel = "Gray";
    private static boolean withoutError = false;
    public static java.sql.Connection con = null;
    public static ResultSet res = null;
    public static DecimalFormat df = new DecimalFormat();
    public static Preferences pref = Preferences.userNodeForPackage(Config.class);
    public static Locale LOCALE = Locale.US;
    static final String KEYCRYPTO = "CrYpToGrApHy";
    public static SettingOptionsDialog optionsDialog;
    public static SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM-dd", new DateFormatSymbols());
    public static SimpleDateFormat dateFormat;
    private static final String DATABASE = "dbosfacdmt";
    public static volatile String host = "127.0.0.1";
    private static String username = "root";
    private static String password = "";
    //
    public static final int PORTMAINSERVER = 5651;
    public static final int PORTDOWNLOAD = 5657;
    public static final int PORTMETADATA = 5654;
    //
    public static final String LITE_VERSION = "LITE";
    public static final String FULL_VERSION = "FULL";
//
//    private static String database = "dbosfacdmt";
//    private static String host = "dbosfacdmt.db.8487892.hostedresource.com";
//    private static String username = "dbosfacdmt";
//    private static String password = "OsfacLab01";
}
