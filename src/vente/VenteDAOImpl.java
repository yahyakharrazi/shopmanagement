package vente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import client.ClientDAOImpl;
import connexion.DAO;
import connexion.DataConnection;

public class VenteDAOImpl implements DAO{

	Connection connection = null;
	PreparedStatement statement = null;
	
	public VenteDAOImpl() {
		DataConnection dc = DataConnection.getConnexion();
		connection= dc.getConnection();
		statement = dc.statement;
	}
		
	public long create(Object prod) {
		try {
			Vente vente = Vente.class.cast(prod);
			statement = connection.prepareStatement("insert into Vente(date,total,client) values(?,?,?)");
			statement.setString(1, vente.getDate());
			statement.setFloat(2, vente.getTotal());
			statement.setLong(3, vente.getClient().getId());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			while(rs.next()) {
				return rs.getInt(1);
			}
			return -1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	public void delete(Object prod) {
		PreparedStatement statement;
		Vente vente = Vente.class.cast(prod);
		try {
			statement = connection.prepareStatement("delete from produit where id = ?");
			statement.setLong(1,vente.getId());
			statement.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(Object prod) {
		Vente Vente = Vente.class.cast(prod);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("update vente set date=? ,total=? , client=? where id = ?");
			statement.setString(1, Vente.getDate());
			statement.setFloat(2, Vente.getTotal());
			statement.setFloat(3, Vente.getClient().getId());
			statement.setLong(4, Vente.getId());
			statement.executeUpdate();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<Vente> findAll(){
		List<Vente> l = new ArrayList<Vente>();
		try {
			statement = connection.prepareStatement("select * from vente");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				l.add(find(rs.getInt(1)));
			}
			return l;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			l = null;
			return l;
		}
	}
	
	public Vente find(long id) {
		try {
			statement = connection.prepareStatement("select * from vente where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Vente(rs.getLong(1),rs.getString(2),rs.getFloat(4),new ClientDAOImpl().find(rs.getInt(3)));
			}
			return null;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Vente> findAll(String key){
		List<Vente> l = new ArrayList<Vente>();
		try {
			statement = connection.prepareStatement("select * from vente where designation like ?");
			statement.setString(1, key);
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
