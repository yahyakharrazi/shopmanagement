package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DataConnection {

	private static DataConnection connexionSingle = null;
	private Connection connection;
	public PreparedStatement statement = null;
	
	public DataConnection() {
		String url = "jdbc:mysql://localhost:3306/magasinmanagement2";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url,"root","");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	
	public static DataConnection getConnexion(){
		if(connexionSingle==null){
			connexionSingle=new DataConnection();
		}
		return connexionSingle;
	}
}