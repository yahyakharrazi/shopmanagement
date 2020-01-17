package login;

import connexion.DataConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    public Login(){
        DataConnection dc = DataConnection.getConnexion();
        connection= dc.getConnection();
        statement = dc.statement;
    }

    public long signIn(String user,String pwd){
        try {
            statement = connection.prepareStatement("select id from admin where username = ? and password = ?");
            statement.setString(1,user);
            statement.setString(2,pwd);
            rs = statement.executeQuery();
            while(rs.next()){
                return rs.getLong(1);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public long signUp(Admin admin){
        try {
            statement = connection.prepareStatement("insert into admin(username, password, firstname, lastname) values(?,?,?,?)");
            statement.setString(1,admin.getUsername());
            statement.setString(2,admin.getPassword());
            statement.setString(3,admin.getFirstName());
            statement.setString(4,admin.getLastName());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            while (rs.next()){
                return rs.getLong(1);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }
}
