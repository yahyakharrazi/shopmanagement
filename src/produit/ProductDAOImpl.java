package produit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import connexion.DAO;
import connexion.DataConnection;

public class ProductDAOImpl implements DAO{

	Connection connection = null;
	PreparedStatement statement = null;
	
	public ProductDAOImpl() {
		DataConnection dc = new DataConnection();
		connection= dc.getConnection();
		statement = dc.statement;
	}
		
	public long create(Object prod) {
		try {
			Product product = Product.class.cast(prod);
			System.out.println(product);
			statement = connection.prepareStatement("insert into Produit(designation,prixAchat,prixVente,categorie) values(?,?,?,?)");
			statement.setString(1, product.getDesignation());
			statement.setFloat(2, product.getPrixAchat());
			statement.setFloat(3, product.getPrixVente());
			statement.setFloat(4, product.getCategorie().getId());
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
		Product product = Product.class.cast(prod);
		try {
			
			statement = connection.prepareStatement("delete from produit where id = ?");
			statement.setLong(1,product.getId());
			statement.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(Object prod) {
		Product product = Product.class.cast(prod);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("update produit set designation=? , prixAchat=? , prixVente=? , categorie=? where produit.id = ?");
			statement.setString(1, product.getDesignation());
			statement.setFloat(2, product.getPrixAchat());
			statement.setFloat(3, product.getPrixVente());
			statement.setFloat(4, product.getCategorie().getId());
			statement.setLong(5, product.getId());
			statement.executeUpdate();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<Product> findAll(){
		List<Product> l = new ArrayList<Product>();
		try {
			statement = connection.prepareStatement("select * from produit");
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
	
	public Product find(long id) {
		try {
			statement = connection.prepareStatement("select * from produit where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Product(rs.getInt(1),rs.getString(2),rs.getFloat(3),rs.getFloat(4),new CategorieDAOImpl().find(rs.getInt(5)));
			}
			return null;
		}
		catch(Exception ex) {
			ex.getMessage();
			return null;
		}
		
	}
	
	public List<Product> findAll(String key){
		List<Product> l = new ArrayList<Product>();
		try {
			statement = connection.prepareStatement("select * from produit where designation like ?");
			statement.setString(1, key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				l.add(new Product(rs.getLong(1),rs.getString(2),rs.getFloat(3),rs.getFloat(4),new CategorieDAOImpl().find(rs.getInt(5))));
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
