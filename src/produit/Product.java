package produit;

import categorie.Categorie;

public class Product {

	private long id;
	private String designation;
	private float prixVente;
	private float prixAchat;
	private Categorie categorie;
	
	public Product() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public float getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(float prixVente) {
		this.prixVente = prixVente;
	}

	public float getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(float prixAchat) {
		this.prixAchat = prixAchat;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public Product(long id, String designation, float prixVente, float prixAchat, Categorie categorie) {
		super();
		this.id = id;
		this.designation = designation;
		this.prixVente = prixVente;
		this.prixAchat = prixAchat;
		this.categorie = categorie;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", designation=" + designation + ", prixVente=" + prixVente + ", prixAchat="
				+ prixAchat + ", categorie=" + categorie + "]";
	}

}
