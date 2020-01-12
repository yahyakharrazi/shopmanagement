package banque;

import connexion.DataConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompteController {

    Connection connection;
    PreparedStatement statement;

    public CompteController(){
        DataConnection dc = new DataConnection();
        connection= dc.getConnection();
        statement = dc.statement;
    }


    public Compte getCompte(String name){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from compte where nom = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                return new Compte(rs.getString(1),rs.getFloat(2),rs.getString(3));
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
