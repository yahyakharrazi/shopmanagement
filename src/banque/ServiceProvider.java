package banque;

import client.ClientDAOImpl;
import connexion.DataConnection;
import vente.Vente;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServiceProvider {

    public ServiceProvider(){    }


    public static void main(String[] args) {

        try {
            Compte c;
            ServerSocket server = new ServerSocket(8818, 10);
            Socket service;
            while (true) {
                service = server.accept();
                InputStream is = service.getInputStream();
                ObjectInputStream dis = new ObjectInputStream(is);
                c = (Compte) dis.readObject();
                System.out.println(c.getNom());
                OutputStream os = service.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                Compte cmpt = new CompteController().getCompte(c.getNom());
                System.out.println(cmpt.getNom());

                if(cmpt!=null){
                    if(cmpt.getSolde()>c.getSolde()){
                        dos.writeByte(1);
                    }
                    else {
                        dos.writeByte(0);
                    }
                }
                else {
                    System.out.println("not found");
                    dos.writeByte(0);
                }
                service.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
