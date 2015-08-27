package com.osfac.dmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MergeDataRequest {

    public MergeDataRequest() {
        try {
            conNew = connectToTargetDB("bd_osfac_new");
            PreparedStatement ps = conNew.prepareStatement("select id_requerant from t_requerant");
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int idRequesterNew = insertRequester(findRequesterData(res.getInt(1)));
                if (idRequesterNew != -1) {
                    insertDelivery(idRequesterNew, findDeliveryData(res.getInt(1)), res.getInt(1));
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MergeDataRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int insertDelivery(int idRequester, ArrayList<String> data, int idRequerantOld) throws SQLException {
        int idDelivery = -1;
        PreparedStatement ps = Config.con.prepareStatement("insert into dmt_delivery values (?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, null);
        ps.setInt(2, idRequester);
        for (int i = 0; i < data.size(); i++) {
            ps.setString((i + 3), Config.capitalFirstLetter(data.get(i)));
        }
//        System.out.print(data.size() + " ");
        if (data.size() > 0) {
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    idDelivery = res.getInt(1);
                    ArrayList<Integer> list = getIDUsage(idRequerantOld);
                    for (int i = 0; i < list.size(); i++) {
                        ps = Config.con.prepareStatement("insert into dmt_choose values (?,?)");
                        ps.setInt(1, idDelivery);
                        ps.setInt(2, list.get(i));
                        int result2 = ps.executeUpdate();
                    }
                    ArrayList<Integer> IDImages = getIDImages(idRequerantOld);
                    for (int i = 0; i < IDImages.size(); i++) {
                        ps = Config.con.prepareStatement("insert into dmt_deliver values (?,?)");
                        ps.setInt(1, IDImages.get(i));
                        ps.setInt(2, idDelivery);
                        int result3 = ps.executeUpdate();
                    }
                }
            }
        }
        System.err.println(new StringBuilder(NewRequester).append(" has been added in the database successfully ...").toString());
        return idDelivery;
    }

    private ArrayList<Integer> getIDImages(int idRequerant) throws SQLException {
        ArrayList<Integer> list = new ArrayList<>();
        PreparedStatement ps = conNew.prepareStatement("select id_image from t_livrer where id_livraison = "
                + "(select id_livraison from t_livraison where id_requerant = ?)");
        ps.setInt(1, idRequerant);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            if (isExistedInDB(res.getInt(1))) {
                list.add(res.getInt(1));
            }
        }
        return list;
    }

    private boolean isExistedInDB(int idImage) throws SQLException {
        PreparedStatement ps = Config.con.prepareStatement("select id_image from dmt_image where id_image = ?");
        ps.setInt(1, idImage);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            return true;
        }
        return false;
    }

    private ArrayList<Integer> getIDUsage(int idRequerant) throws SQLException {
        ArrayList<Integer> list = new ArrayList<>();
        PreparedStatement ps = conNew.prepareStatement("select id_usage from t_choisir where id_requerant = ?");
        ps.setInt(1, idRequerant);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            list.add(res.getInt(1));
        }
        return list;
    }

    private ArrayList<String> findDeliveryData(int idRequester) throws SQLException {
        ArrayList<String> data = new ArrayList<>();
        PreparedStatement ps = conNew.prepareStatement("select id_utilisateur,volume,date,pathrow from "
                + "t_livraison where id_requerant = ?");
        ps.setInt(1, idRequester);
        ResultSet res = ps.executeQuery();
        ResultSetMetaData rsmd = res.getMetaData();
        int nbCols = rsmd.getColumnCount();
        while (res.next()) {
            for (int i = 1; i <= nbCols; i++) {
                if (i == 1) {
                    if (res.getInt(i) == 1 || res.getInt(i) == 5 || res.getInt(i) == 6) {
                        data.add(res.getString(i));
                    } else {
                        data.add(5 + "");
                    }
                } else {
                    data.add(res.getString(i));
                }
            }
            data.add("Yes");
            data.add("Yes");
            data.add("");
        }
        return data;
    }

    private int insertRequester(ArrayList<String> data) throws SQLException {
        PreparedStatement ps = Config.con.prepareStatement("insert into dmt_requester values "
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, null);
        for (int i = 0; i < data.size(); i++) {
            if (i == 3) {
                if (data.get(i).equalsIgnoreCase("M")) {
                    ps.setString((i + 2), "Male");
                } else {
                    ps.setString((i + 2), "Female");
                }
            } else {
                ps.setString((i + 2), Config.capitalFirstLetter(data.get(i)));
            }
        }
        int result = ps.executeUpdate();
        if (result == 1) {
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                NewRequester = new StringBuilder(data.get(0)).append(" ").append(data.get(1)).
                        append(" ").append(data.get(2)).toString();
//                System.out.print(NewRequester + " has been found ... ");
                return res.getInt(1);
            }
        }
        return -1;
    }

    private ArrayList<String> findRequesterData(int idRequester) throws SQLException {
        ArrayList<String> RequesterData = new ArrayList<>();
        PreparedStatement ps = conNew.prepareStatement("select prenom,nom,postnom,sexe,adresse,telephone,"
                + "email,profession,nom_institution,nationalite,zone_interet,note,commentaire from "
                + "t_requerant inner join t_institution on t_institution.id_institution = "
                + "t_requerant.id_institution where id_requerant = ?");
        ps.setInt(1, idRequester);
        ResultSet res = ps.executeQuery();
        ResultSetMetaData rsmd = res.getMetaData();
        int nbCols = rsmd.getColumnCount();
        while (res.next()) {
            for (int i = 1; i <= nbCols; i++) {
                RequesterData.add(res.getString(i));
            }
        }
        return RequesterData;
    }

    private Connection connectToTargetDB(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, "root", "");
        return con;
    }

//////    public static void main(String[] args) {
//////        Config config = new Config();
//////        MergeDataRequest mergeDataRequest = new MergeDataRequest();
//////    }
    Connection conNew;
    private String NewRequester;
}
