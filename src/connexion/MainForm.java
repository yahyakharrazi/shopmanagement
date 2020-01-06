package connexion;

import client.ClientIHM;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainForm extends Application{

	BorderPane root = new BorderPane();
	Scene scene = null;
	VBox sideBar = new VBox();
	BorderPane container = new BorderPane();

	Button btnClient = new Button("Clients");
	Button btnProduit = new Button("Produits");
	Button btnVente = new Button("Ventes");
	Button btnPaiement = new Button("Paiements");

	public void initSideBar(){

		Image imageClient = new Image("./media/client.png");
		btnClient.setGraphic(new ImageView(imageClient));

		Image imageProduit = new Image("./media/list.png");
		btnProduit.setGraphic(new ImageView(imageProduit));

		Image imageVente = new Image("./media/vente.png");
		btnVente.setGraphic(new ImageView(imageVente));

		Image imagePaiement = new Image("./media/paiement.png");
		btnPaiement.setGraphic(new ImageView(imagePaiement));

		sideBar.getChildren().addAll(btnClient,btnProduit,btnVente,btnPaiement);

		sideBar.getStyleClass().add("sideBar");
	}

	public void initContainer(){
		initSideBar();
		container.setLeft(sideBar);

		btnClient.setOnAction(event -> {
			ClientIHM cihm = new ClientIHM();
			cihm.initPanes();
			changeContent(cihm.grid);
		});
	}

	public void changeContent(Node content){
		container.setCenter(content);
	}

	public MainForm() {

	}

	@Override
	public void start(Stage window) throws Exception {
		scene = new Scene(container);
		window.setTitle("Gestion Magasin");
		window.setHeight(600);
		window.setWidth(1000);
		initContainer();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
