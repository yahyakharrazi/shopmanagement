package vente;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import client.Client;
import client.ClientDAOImpl;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import produit.Product;
import produit.ProductDAOImpl;

import static java.lang.String.valueOf;

public class VenteIHM extends Application {

	Scene scene = null;

//	static ObservableList<Vente> list = null;
	static ObservableList<Client> listClient = null;
	static ObservableList<Commande> listCommand = null;
	static List<Commande> listCom = null;
	//	static List<Commande> listVente = null;
	static ObservableList<Product> listProduct = null;
	HashMap<Long,Commande> map = new HashMap<>();

	private static VenteDAOImpl pm = new VenteDAOImpl();
	private static ClientDAOImpl cm = new ClientDAOImpl();
	private  static CommandeDAOImpl cmd = new CommandeDAOImpl();
	private static VenteDAOImpl vm = new VenteDAOImpl();
	private static ProductDAOImpl pr = new ProductDAOImpl();


	public VenteIHM(){
//		list = FXCollections.observableArrayList(pm.findAll());
		listCommand = FXCollections.observableArrayList(cmd.findAll());
		listClient = FXCollections.observableArrayList(cm.findAll());
//		listCom = cmd.findAll();
		listProduct = FXCollections.observableArrayList(pr.findAll());
	}


	GridPane grid2 = new GridPane();
//	HBox navBar = new HBox();
	HBox sideBarLeft2 = new HBox();
	VBox sideBarRight2 = new VBox();
	BorderPane container2 = new BorderPane();

	public GridPane grid = new GridPane();
	HBox navBar = new HBox();
	HBox sideBarLeft = new HBox();
	VBox sideBarRight = new VBox();
	public BorderPane container = new BorderPane();

	Label labelIdCmd = new Label("ID : ");
	Label labelVente = new Label("ID Vente : ");
	Label labelTotalCmd = new Label("Total : ");
	Label labelQuantite = new Label("Quantite : ");
	Label labelProduct = new Label("Produit : ");

	TextField txtSearch2 = new TextField();

	TextField txtIdCmd = new TextField();
	TextField txtVente = new TextField();
	TextField txtTotalCmd = new TextField();
	TextField txtQuantite = new TextField();
	ChoiceBox<Product> comboProduct = null;
	TableView<Commande> tableCmd = new TableView<>();

	Label labelId = new Label("ID : ");
	Label labelDate = new Label("Date : ");
	Label labelTotal = new Label("Total : ");
	Label labelClient = new Label("Client : ");
	
	Label labelNav = new Label("Gestion ventes");
	
	TextField txtSearch = new TextField();
	
	TextField txtId = new TextField();
	TextField txtDate = new TextField();
	TextField txtTotal = new TextField();
	ChoiceBox<Client> comboClient = null;
	TableView<Vente> table = new TableView<>();

	Button btnModifierCmd = new Button("Modifier");
	Button btnAjouterCmd = new Button("Ajouter");
	Button btnNouveauCmd = new Button("Nouveau");
	Button btnSupprimerCmd = new Button("Supprimer");

	public int myIndexOf(List<Commande> al, Commande c){
		int i;
		if(c!=null){
			for(i=0;i<al.size();i++){
				if(al.get(i).getId()==c.getId())
					return i;
			}
		}
		return -1;
	}

	public void loadComponents(){
		txtIdCmd.setDisable(true);
		txtVente.setDisable(true);
		txtTotalCmd.setDisable(true);

		txtId.setDisable(true);
		txtTotal.setDisable(true);
		txtDate.setDisable(true);

		navBar.getChildren().add(labelNav);

		sideBarLeft2.getChildren().addAll(btnNouveauCmd,btnAjouterCmd,btnModifierCmd,btnSupprimerCmd);

		grid2.addRow(0, labelIdCmd,txtIdCmd);
		grid2.addRow(1, labelQuantite, txtQuantite);
		grid2.addRow(2, labelTotalCmd, txtTotalCmd);
		grid2.addRow(3, labelVente, txtVente);
		grid2.addRow(4, labelProduct, comboProduct);
		grid2.add(sideBarLeft2,0,5,2,1);
		sideBarRight2.getChildren().addAll(txtSearch2,tableCmd);

		navBar.getStyleClass().add("navBar");
//		navBar.getStyleClass().add("labelNav");
		sideBarLeft2.getStyleClass().add("sideBarLeft");
		sideBarRight2.getStyleClass().add("sideBarRight");
		grid2.getStyleClass().add("sideBarLeft");

//		container2.setTop(navBar);
//		container2.setLeft(sideBarLeft);
		container2.setRight(sideBarRight2);
		container2.setCenter(grid2);

		grid.addRow(3, labelClient, comboClient);
		grid.add(sideBarLeft,0,4,2,1);
		grid.add( txtSearch,0,5,2,1);
		grid.add(table,0,6,2,1);
		sideBarRight.getChildren().addAll(grid2,tableCmd);

		navBar.getStyleClass().add("navBar");
		navBar.getStyleClass().add("labelNav");
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("sideBarLeft");


		container.setTop(navBar);
//		container.setLeft(sideBarLeft);
		container.setRight(sideBarRight);
		container.setCenter(grid);

	}

	public void loadComboCmd() {
		comboProduct = new ChoiceBox<>(listProduct);
		comboProduct.getSelectionModel().selectFirst();
	}

	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;

	private void loadCombo() {
//		System.out.println(pm.findAll());
//		System.out.println(pm.find(1));
		comboClient = new ChoiceBox<>(listClient);
		comboClient.getSelectionModel().selectFirst();
	}

	public int myIndexOf(List<Vente> al,Vente c){
		int i;
		if(c!=null){
			for(i=0;i<al.size();i++){
				if(al.get(i).getId()==c.getId())
					return i;
			}
		}
		return -1;
	}

	private void resetTextFields() {
		txtId.clear();
		txtDate.clear();
		txtTotal.clear();
	}

	private void resetTextFieldsCmd() {
		txtIdCmd.clear();
//		txtVente.clear();
		txtQuantite.clear();
		txtTotalCmd.clear();
	}
	
	private void initTable(ObservableList<Vente> data) {
		table.setEditable(true);
		table.setItems(data);

		TableColumn<Vente, Long> colId = new TableColumn<>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		table.getColumns().add(colId);

		TableColumn<Vente, String> colDate = new TableColumn<>("Date");
		colDate.setMinWidth(80);
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		table.getColumns().add(colDate);
		
		TableColumn<Vente, String> colClient = new TableColumn<>("Client");
		colClient.setMinWidth(80);
		colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
		table.getColumns().add(colClient);

		TableColumn<Vente, Float> colTotal = new TableColumn<>("Total");
		colTotal.setMinWidth(80);
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		table.getColumns().add(colTotal);

	}

	private void initTableCmd(ObservableList<Commande> data) {
		tableCmd.setEditable(true);
		tableCmd.setItems(data);
		TableColumn<Commande, Integer> colId = new TableColumn<>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableCmd.getColumns().add(colId);

		TableColumn<Commande, String> colDesignation = new TableColumn<>("Quantite");
		colDesignation.setMinWidth(120);
		colDesignation.setCellValueFactory(new PropertyValueFactory<>("quantite"));
		tableCmd.getColumns().add(colDesignation);

		TableColumn<Commande, Float> colPrixAchat = new TableColumn<>("Total");
		colPrixAchat.setMinWidth(80);
		colPrixAchat.setCellValueFactory(new PropertyValueFactory<>("total"));
		tableCmd.getColumns().add(colPrixAchat);

		TableColumn<Commande, String> colPrixVente = new TableColumn<>("Vente");
		colPrixVente.setMinWidth(80);
		colPrixVente.setCellValueFactory(new PropertyValueFactory<>("vente"));
		tableCmd.getColumns().add(colPrixVente);

		TableColumn<Commande, String> colProduct = new TableColumn<>("Product");
		colProduct.setMinWidth(80);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
		tableCmd.getColumns().add(colProduct);
	}
	
	public void initPanes(){
//		list = FXCollections.observableArrayList(pm.findAll());
		ObservableList<Vente> data = FXCollections.observableArrayList(pm.findAll());
		ObservableList<Commande> dataCmd = FXCollections.observableArrayList(listCommand);
		FilteredList<Vente> items = new FilteredList<>(data);
		FilteredList<Commande> itemsCommand = new FilteredList<>(dataCmd);
		items.setPredicate(null);
		initTable(data);

		items.setPredicate(null);
		initTableCmd(dataCmd);

		loadComboCmd();
		loadCombo();

		loadComponents();

		txtQuantite.setOnKeyReleased(e -> {
			if(txtQuantite.getText().length()==0)
				txtTotalCmd.setText("0");
			else
				txtTotalCmd.setText(valueOf((pr.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()))));
		});

		btnAjouterCmd.setOnAction(e -> {
			if(txtVente.getText().length()!=0 && txtQuantite.getText().length()!=0) {
				float soustotal = (pr.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()));
				Commande cmnd = new Commande(1,pr.find(comboProduct.getValue().getId()),vm.find(Long.parseLong(txtVente.getText())),Integer.parseInt(txtQuantite.getText()), soustotal);
				for (Commande c : tableCmd.getItems()) {
					map.put(c.getProduct().getId(),c);
				}

				System.out.println(map);

				if(map.containsKey(cmnd.getProduct().getId())){
					System.out.println("what im i doing here");
					cmnd.setQuantite(cmnd.getQuantite()+map.get(cmnd.getProduct().getId()).getQuantite());
					cmnd.setTotal(cmnd.getQuantite()*cmnd.getProduct().getPrixVente());
					dataCmd.set(myIndexOf(listCom,cmnd),cmnd);
					cmd.update(cmnd);
					data.set(myIndexOf(data,pm.find(Integer.parseInt(txtVente.getText()))),pm.find(Integer.parseInt(txtVente.getText())));
					tableCmd.getSelectionModel().clearSelection();
					resetTextFieldsCmd();
				}
				else{
					long id = cmd.create(cmnd);
					System.out.println(id);
					if(id > 0) {
						System.out.println("what is this");
						cmnd.setId(id);
//						tableCmd.setItems(null);
//						dataCmd.add(cmd);
						dataCmd.add(cmnd);
						tableCmd.setItems(itemsCommand);
						data.set(myIndexOf(data,pm.find(Integer.parseInt(txtVente.getText()))),pm.find(Integer.parseInt(txtVente.getText())));


						resetTextFieldsCmd();
					}
				}

//				System.out.println(map);

			}
		});

		txtQuantite.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
				if (!newValue.matches("\\d*")) {
					txtQuantite.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});


		btnNouveauCmd.setOnAction(e -> {
			resetTextFieldsCmd();
			tableCmd.getSelectionModel().clearSelection();
		});

		btnModifierCmd.setOnAction(e -> {
			if(txtIdCmd.getText().length()!=0) {
				Commande p =cmd.find(Integer.parseInt(txtIdCmd.getText()));
				if(p!=null) {
					p.setVente(vm.find(Integer.parseInt(txtVente.getText())));
					p.setQuantite(Integer.parseInt(txtQuantite.getText()));
					p.setTotal(Float.parseFloat(txtTotalCmd.getText()));
					p.setProduct(pr.find(comboProduct.getValue().getId()));
					dataCmd.set(myIndexOf(dataCmd,p),p);

					cmd.update(p);

					data.set(myIndexOf(data,pm.find(Integer.parseInt(txtVente.getText()))),pm.find(Integer.parseInt(txtVente.getText())));

//					data.removeAll();
//					table.setItems(null);
//					data.addAll(pm.findAll());
//					table.setItems(data);

//					tableCmd.getSelectionModel().clearSelection();
					resetTextFieldsCmd();
				}
			}
		});

		btnSupprimerCmd.setOnAction(e -> {
			if(txtIdCmd.getText().length()!=0) {
				Commande p =cmd.find(Integer.parseInt(txtIdCmd.getText()));
				if(p!=null) {
					dataCmd.remove(myIndexOf(dataCmd,p));
					cmd.delete(p);
					data.set(myIndexOf(data,pm.find(Integer.parseInt(txtVente.getText()))),pm.find(Integer.parseInt(txtVente.getText())));
//					tableCmd.getSelectionModel().clearSelection();
					resetTextFieldsCmd();
				}
			}
		});

		txtSearch2.setOnKeyReleased(event -> {
			Predicate<Commande> id = i -> valueOf(i.getId()).contains(txtSearch2.getText());
			Predicate<Commande> quantite = i -> valueOf(i.getQuantite()).contains(txtSearch2.getText());
			Predicate<Commande> total = i -> valueOf(i.getTotal()).contains(txtSearch2.getText());
			Predicate<Commande> vente = i -> valueOf(i.getVente().getId()).contains(txtSearch2.getText());
			Predicate<Commande> product = i -> valueOf(i.getProduct()).contains(txtSearch2.getText());
			Predicate<Commande> predicate = quantite.or(total.or(vente.or(product.or(id))));

			tableCmd.setItems(itemsCommand);
			itemsCommand.setPredicate(predicate);
//				tableCmd.getItems()
		});


		tableCmd.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
			//Check whether item is selected and set value of selected item to Label
			if(tableCmd.getSelectionModel().getSelectedItem() != null)
			{
				resetTextFieldsCmd();
				Commande p = tableCmd.getSelectionModel().getSelectedItem();
				txtIdCmd.setText(valueOf(p.getId()));
				txtVente.setText(valueOf(p.getVente().getId()));
				txtQuantite.setText(valueOf(p.getQuantite()));
				txtTotalCmd.setText(valueOf(p.getTotal()));
				comboProduct.getSelectionModel().select((int)p.getProduct().getId()-1);
			}
		});


		btnAjouter.setOnAction(e -> {
			try {
				Vente v = new Vente(1, LocalDate.now().toString(),0, cm.find(comboClient.getValue().getId()));
//						System.out.println("before create : " + cm.find(comboClient.getValue().getId()));
				long id = pm.create(v);
				if(id > 0) {
					v.setId(id);
					data.add(v);
					resetTextFields();
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		});
				
		btnNouveau.setOnAction(e -> {
			resetTextFields();
			table.getSelectionModel().clearSelection();
		});
		
		btnModifier.setOnAction(e -> {
			if(txtId.getText().length()!=0) {
				Vente v =pm.find(Integer.parseInt(txtId.getText()));
				if(v!=null) {
					v.setDate(txtDate.getText());
					v.setTotal(Float.parseFloat(txtTotal.getText()));
					v.setClient(cm.find(comboClient.getValue().getId()));
					data.set(myIndexOf(data,v),v);
//					pm.update(v);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
//			else{
//				Alert alertAdd = new Alert(Alert.AlertType.CONFIRMATION);
//				alertAdd.setTitle("Message Here...");
//				alertAdd.setHeaderText("Look, an Information Dialog");
//				alertAdd.setContentText("I have a great message for you!");
//				alertAdd.show();
//
//			}
		});
		
		btnSupprimer.setOnAction(e -> {
			if(txtId.getText().length()!=0 ) {
				Vente p =pm.find(Long.parseLong(txtId.getText()));
				if(p!=null) {
					data.remove(myIndexOf(data,p));
//					pm.delete(p);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		txtSearch.setOnKeyReleased(event -> {
			Predicate<Vente> date = i -> String.valueOf(i.getDate()).contains(txtSearch.getText());
			Predicate<Vente> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
			Predicate<Vente> total = i -> String.valueOf(i.getTotal()).contains(txtSearch.getText());
			Predicate<Vente> client = i -> String.valueOf(i.getClient()).contains(txtSearch.getText());
			Predicate<Vente> predicate = date.or(total.or(client.or(id)));

			table.setItems(items);
			items.setPredicate(predicate);
//				table.getItems()
		});

		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
				
		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelDate,txtDate);
		grid.addRow(2, labelTotal, txtTotal);
		
		table.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
			//Check whether item is selected and set value of selected item to Label
			if(table.getSelectionModel().getSelectedItem() != null)
			{
				resetTextFields();
				Vente p = table.getSelectionModel().getSelectedItem();
				txtId.setText(String.valueOf(p.getId()));
				txtDate.setText(String.valueOf(p.getDate()));
				txtTotal.setText(String.valueOf(p.getTotal()));

				comboClient.getSelectionModel().select((int)p.getClient().getId()-1);
				txtVente.setText(String.valueOf(p.getId()));

				Predicate<Commande> id = i -> i.getVente().getId()==p.getId();

				tableCmd.setItems(itemsCommand);
//				cihm.tableCmd.setItems(itemsCommand);

//				tableCmd.setItems();
//					tableCommand.setItems(itemsCommand);
				itemsCommand.setPredicate(id);
			}
		 });
	

		
	}

	private int myIndexOf(ObservableList<Vente> al,Vente c) {
		int i;
		if(c!=null){
			for(i=0;i<al.size();i++){
				if(al.get(i).getId()==c.getId())
					return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) {
//		list = new ArrayList<Vente>();
		//list = pm.findAll();

//		listCommand = FXCollections.observableArrayList(cmd.findAll());
//
//		listClient = FXCollections.observableArrayList(cm.findAll());
		
		scene = new Scene(container);
		window.setTitle("Gestion vente");
		window.setHeight(600);
		window.setWidth(1000);
		initPanes();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}

}
