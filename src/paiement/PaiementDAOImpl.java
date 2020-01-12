package paiement;

import connexion.DAO;
import connexion.DataConnection;
import produit.ProductDAOImpl;
import vente.VenteDAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAOImpl implements DAO {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs;

    public PaiementDAOImpl() {
        DataConnection dc = new DataConnection();
        connection= dc.getConnection();
        statement = dc.statement;
    }

    public long create(Object p) {
        long id=-1;
        try {
           Paiement paiement =Paiement.class.cast(p);
            System.out.println(paiement);

            statement = connection.prepareStatement("insert into paiement(vente,total,date,reglement) values(?,?,?,?)");
            statement.setLong(1, paiement.getVente().getId());
            statement.setFloat(2, paiement.getTotal());
            statement.setString(3, paiement.getDate());
            statement.setString(4, paiement.getReglement());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;


        }
        catch(Exception ex) {
            ex.printStackTrace();
            return id;
        }
    }

    public void delete(Object prod) {
        PreparedStatement statement;
        Paiement paiement = Paiement.class.cast(prod);
        try {

            statement = connection.prepareStatement("delete from paiement where id = ?");
            statement.setLong(1,paiement.getId());
            statement.executeUpdate();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update(Object prod) {
       Paiement commande =Paiement.class.cast(prod);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update paiement set vente=? , total=? , date=? , reglement=? where id = ?");
            statement.setLong(1, commande.getVente().getId());
            statement.setFloat(2, commande.getTotal());
            statement.setString(3, commande.getDate());
            statement.setString(4, commande.getReglement());
            statement.setLong(5, commande.getId());
            statement.executeUpdate();

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Paiement> findAll(){
        List<Paiement> l = new ArrayList<>();
        try{
            statement = connection.prepareStatement("select * from paiement");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                l.add(find(rs.getLong(1)));
            }
//            System.out.println(l);
            return l;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            l = null;
            return l;
        }
    }

    public Paiement find(long id) {
        try {
            statement = connection.prepareStatement("select * from paiement where id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                return new Paiement(rs.getLong(1), new VenteDAOImpl().find(rs.getLong(2)),rs.getFloat(3),rs.getString(4),rs.getString(5));
            }
            return null;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public List<Paiement> findAll(String key){
        List<Paiement> l = new ArrayList<Paiement>();
        try {
            statement = connection.prepareStatement("select * from paiement");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                l.add(find(rs.getLong(1)));
            }
            return l;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            l = null;
            return l;
        }
    }
}
