package vente;

import java.util.Date;

import client.Client;

public class Vente {

	private long Id;
	private String date;
	private float total;
	private Client client;
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public Vente(long id, String date, float total, Client client) {
		super();
		this.Id = id;
		this.date = date;
		this.total = total;
		this.client = client;
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

	public Vente() {
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return getId()+"";
	}

}
