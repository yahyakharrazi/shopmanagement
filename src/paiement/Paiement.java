package paiement;

import vente.Vente;

public class Paiement {
	private long id;
	private String reglement;
	private String date;
	private float total;
	private Vente vente;

	@Override
	public String toString() {
		return "Paiement{ id=" + id + ", reglement='" + reglement + ", date='" + date + ", total='" + total + ", vente=" + vente + " }";
	}

	public Paiement(long id, Vente vente, float total, String date , String reglement) {
		this.id = id;
		this.vente = vente;
		this.total = total;
		this.date = date;
		this.reglement = reglement;
	}


	public Paiement() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReglement() {
		return reglement;
	}

	public void setReglement(String reglement) {
		this.reglement = reglement;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Vente getVente() {
		return vente;
	}

	public void setVente(Vente vente) {
		this.vente = vente;
	}
}
