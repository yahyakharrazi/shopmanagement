package banque;

import connexion.DataConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompteController {

    Connection connection;
    PreparedStatement statement;

    public CompteController(){
        DataConnection dc = DataConnection.getConnexion();
        connection= dc.getConnection();
        statement = dc.statement;
    }


    public Compte getCompte(String rib,String code){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from compte where  rib= ? and code=?");
            statement.setString(1, rib);
            statement.setString(2, code);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                return new Compte(rs.getString(1),rs.getFloat(2),rs.getString(3),rs.getString(4));
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
