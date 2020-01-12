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
import paiement.Paiement;
import paiement.PaiementIHM;
import produit.CategorieIHM;
import produit.Product;
import produit.ProductIHM;
import vente.Vente;
import vente.VenteIHM;

public class MainForm extends Application{

	BorderPane root = new BorderPane();
	Scene scene = null;
	VBox sideBar = new VBox();
	BorderPane container = new BorderPane();

	Button btnClient = new Button("Clients");
	Button btnCategorie = new Button("Categories");
	Button btnProduit = new Button("Produits");
	Button btnVente = new Button("Ventes");
	Button btnPaiement = new Button("Paiements");

	public void initSideBar(){

		Image imageClient = new Image("./media/client.png");
		btnClient.setGraphic(new ImageView(imageClient));

		Image imageCategorie = new Image("./media/category.png");
		btnCategorie.setGraphic(new ImageView(imageCategorie));

		Image imageProduit = new Image("./media/list.png");
		btnProduit.setGraphic(new ImageView(imageProduit));

		Image imageVente = new Image("./media/vente.png");
		btnVente.setGraphic(new ImageView(imageVente));

		Image imagePaiement = new Image("./media/paiement.png");
		btnPaiement.setGraphic(new ImageView(imagePaiement));

		sideBar.getChildren().addAll(btnClient,btnCategorie,btnProduit,btnVente,btnPaiement);

		sideBar.getStyleClass().add("sideBar");
	}

	public void initContainer(){
		initSideBar();
		container.setLeft(sideBar);

		btnClient.setOnAction(event -> {
			ClientIHM cihm = new ClientIHM();
			cihm.initPanes();
			changeContent(cihm.container);
		});

		btnCategorie.setOnAction(event -> {
			CategorieIHM cihm = new CategorieIHM();
			cihm.initPanes();
			changeContent(cihm.container);
		});


		btnProduit.setOnAction(event -> {
			ProductIHM prdihm = new ProductIHM();
			prdihm.initPanes();
			changeContent(prdihm.container);
		});

		btnVente.setOnAction(event -> {
			VenteIHM vihm = new VenteIHM();
			vihm.initPanes();
			changeContent(vihm.container);
		});

		btnPaiement.setOnAction(event -> {
			PaiementIHM pihm = new PaiementIHM();
			pihm.initPanes();
			changeContent(pihm.container);
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
		window.getIcons().add(new Image("./media/store.png"));
		window.setTitle("Gestion Magasin");
		window.setHeight(600);
		window.setMaxHeight(1150);
		window.setMinWidth(1100);
		window.setMinHeight(500);
		initContainer();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
