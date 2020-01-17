package banque;

import java.io.Serializable;

public class Compte implements Serializable {
    private String nom;
    private float solde;
    private String code;
    private String rib;

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    private static final long serialVersionUID = 1L;

    public Compte(String nom, float solde, String code,String rib) {
        this.rib = rib;
        this.nom = nom;
        this.solde = solde;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "nom='" + nom + '\'' +
                ", solde=" + solde +
                ", code='" + code + '\'' +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
