package vente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import connexion.DAO;
import connexion.DataConnection;
import produit.ProductDAOImpl;

public class CommandeDAOImpl implements DAO{

	Connection connection = null;
	PreparedStatement statement = null;
	
	public CommandeDAOImpl() {
		DataConnection dc = new DataConnection();
		connection= dc.getConnection();
		statement = dc.statement;
	}
		
	public long create(Object prod) {
		try {
			Commande commande = Commande.class.cast(prod);
			System.out.println(commande);
			statement = connection.prepareStatement("insert into lignecommande(produit,vente,total,quantite) values(?,?,?,?)");
			statement.setLong(1, commande.getProduct().getId());
			statement.setLong(2, commande.getVente().getId());
			statement.setFloat(3, commande.getTotal());
			statement.setInt(4, commande.getQuantite());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			int id=-1;
			while(rs.next()) {
				id = rs.getInt(1);
			}
			statement = connection.prepareStatement("select sum(total) from lignecommande where vente=?");
			statement.setLong(1,commande.getVente().getId());
			rs = statement.executeQuery();
			float sumTotal = 0;
			while(rs.next()) {
				sumTotal = rs.getFloat(1);
			}

			statement = connection.prepareStatement("update vente set total=? where id = ?");
			statement.setFloat(1, sumTotal);
			statement.setLong(2, commande.getVente().getId());

			return id;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	public void delete(Object prod) {
		PreparedStatement statement;
		Commande Commande = Commande.class.cast(prod);
		try {
			
			statement = connection.prepareStatement("delete from lignecommande where id = ?");
			statement.setLong(1,Commande.getId());
			statement.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(Object prod) {
		Commande commande = Commande.class.cast(prod);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("update lignecommande set produit=? , vente=? , total=? , quantite=? where id = ?");
			float sousTotal = commande.getQuantite() * commande.getProduct().getPrixVente() ;
			statement.setLong(1, commande.getProduct().getId());
			statement.setLong(2, commande.getVente().getId());
			statement.setFloat(3, sousTotal);
			statement.setInt(4, commande.getQuantite());
			statement.setLong(5, commande.getId());
			statement.executeUpdate();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<Commande> findAll(){
		List<Commande> l = new ArrayList<Commande>();
		try {
			statement = connection.prepareStatement("select * from lignecommande");
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
	
	public Commande find(long id) {
		try {
			statement = connection.prepareStatement("select * from lignecommande where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Commande(rs.getLong(1), new ProductDAOImpl().find(rs.getLong(2)), new VenteDAOImpl().find(rs.getLong(3)),rs.getInt(4),rs.getFloat(5));
			}
			return null;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public List<Commande> findAll(String key){
		List<Commande> l = new ArrayList<Commande>();
		try {
			statement = connection.prepareStatement("select * from lignecommande");
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
