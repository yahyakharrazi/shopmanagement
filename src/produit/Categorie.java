package produit;

public class Categorie {

	private long id;
	private String designation;
	
	public Categorie() {
		
	}
	
	public Categorie(long id,String designation) {
		this.id = id;
		this.designation = designation;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDesignation() {
		return this.designation;
	}
	
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public String toString() {
		return this.designation;
	}

}
