package client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import connexion.DAO;
import connexion.DataConnection;
import produit.CategorieDAOImpl;

public class ClientDAOImpl implements DAO{

	Connection connection = null;
	PreparedStatement statement = null;
	
	public ClientDAOImpl() {
		DataConnection dc = new DataConnection();
		connection= dc.getConnection();
		statement = dc.statement;
	}
	
	public long create(Object prod) {
		try {
			Client client = Client.class.cast(prod);
			statement = connection.prepareStatement("insert into Client(nom,prenom,telephone,email,adresse) values(?,?,?,?,?)");
			statement.setString(1, client.getNom());
			statement.setString(2, client.getPrenom());
			statement.setString(3, client.getTelephone());
			statement.setString(4, client.getEmail());
			statement.setString(5, client.getAdresse());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			while(rs.next()) {
				return rs.getInt(1);
			}
			return -1;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	
	public void delete(Object prod) {
		PreparedStatement statement;
		Client client = Client.class.cast(prod);
		try {
			
			statement = connection.prepareStatement("delete from client where id = ?");
			statement.setLong(1,client.getId());
			statement.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(Object cli) {
		Client client = Client.class.cast(cli);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("update client set nom=? , prenom=? , telephone=? , email=? , adresse=? where id = ?");
			statement.setString(1, client.getNom());
			statement.setString(2, client.getPrenom());
			statement.setString(3, client.getTelephone());
			statement.setString(4, client.getEmail());
			statement.setString(5, client.getAdresse());
			statement.setLong(6, client.getId());
			statement.executeUpdate();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<Client> findAll(){
		List<Client> l = new ArrayList<Client>();
		try {
			statement = connection.prepareStatement("select * from client");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				l.add(new Client(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
			}
			return l;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			l = null;
			return l;
		}
	}
	
	
	
	public Client find(long id) {
		try {
			statement = connection.prepareStatement("select * from client where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Client(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
			}
			return null;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		
	}
	
	public List<Client> findAll(String key){
		List<Client> l = new ArrayList<Client>();
		try {
			statement = connection.prepareStatement("select * from client where nom like ?");
			statement.setString(1, key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				l.add(new Client(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
			}
			return l;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			l = null;
			return l;
		}
	}
}
