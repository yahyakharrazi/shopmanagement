package categorie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import connexion.DAO;
import connexion.DataConnection;

public class CategorieDAOImpl implements DAO{

	Connection connection = null;
	PreparedStatement statement = null;
	
	public CategorieDAOImpl() {
		DataConnection dc = new DataConnection();
		connection= dc.getConnection();
		statement = dc.statement;
	}
			
	public long create(Object prod) {
		try {
			Categorie product = Categorie.class.cast(prod);
			statement = connection.prepareStatement("insert into Categorie(designation) values(?)");
			statement.setString(1, product.getDesignation());
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
		Categorie product = Categorie.class.cast(prod);
		try {
			
			statement = connection.prepareStatement("delete from categorie where id = ?");
			statement.setLong(1,product.getId());
			statement.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(Object prod) {
		Categorie product = Categorie.class.cast(prod);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("update categorie set designation=? where id = ?");
			statement.setString(1, product.getDesignation());
			statement.setLong(2, product.getId());
			statement.executeUpdate();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<Categorie> findAll(){
		List<Categorie> l = new ArrayList<Categorie>();
		try {
			statement = connection.prepareStatement("select * from categorie");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				l.add(new Categorie(rs.getInt(1),rs.getString(2)));
			}
			return l;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			l = null;
			return l;
		}
	}
	
	public Categorie find(long id) {
		try {
			statement = connection.prepareStatement("select * from categorie where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Categorie(rs.getInt(1),rs.getString(2));
			}
			return null;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public List<Categorie> findAll(String key){
		List<Categorie> l = new ArrayList<Categorie>();
		try {
			statement = connection.prepareStatement("select * from categorie where designation like ?");
			statement.setString(1, key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				l.add(new Categorie(rs.getInt(1),rs.getString(2)));
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
