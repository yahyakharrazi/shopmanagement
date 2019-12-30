package vente;

import produit.Product;

public class Commande {
	private long id;
	private Product product;
	private Vente vente;	
	private int quantite;
	private float total;
	
	@Override
	public String toString() {
		return "Commande [id=" + id + ", quantite=" + quantite + ", total=" + total + ", product=" + product
				+ ", vente=" + vente + "]";
	}

	public Commande(long id, Product product, Vente vente, int quantite, float total) {
		super();
		this.id = id;
		this.quantite = quantite;
		this.total = total;
		this.product = product;
		this.vente = vente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Vente getVente() {
		return vente;
	}

	public void setVente(Vente vente) {
		this.vente = vente;
	}

	public Commande() {
		
	}

}
