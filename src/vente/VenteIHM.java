package vente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import client.Client;
import client.ClientDAOImpl;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VenteIHM extends Application {

	BorderPane root = new BorderPane();
	Scene scene = null;

	static List<Vente> list = null;
	static ObservableList<Client> listClient = null;
	static ObservableList<Commande> listCommand = null;

	private static VenteDAOImpl pm = new VenteDAOImpl();
	private static ClientDAOImpl cm = new ClientDAOImpl();
	private  static CommandeDAOImpl cmd = new CommandeDAOImpl();

	public VenteIHM(){
		list = pm.findAll();
		listCommand = FXCollections.observableArrayList(cmd.findAll());
		listClient = FXCollections.observableArrayList(cm.findAll());

	}



	public GridPane grid = new GridPane();
	public GridPane gridCommande = new GridPane();
	HBox navBar = new HBox();
	HBox sideBarLeft = new HBox();
	VBox sideBarRight = new VBox();
	public BorderPane container = new BorderPane();
	
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
	TableView<Vente> table = new TableView<Vente>();
	TableView<Commande> tableCommand = new TableView<Commande>();

	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;

	private void loadCombo() {
//		System.out.println(pm.findAll());
//		System.out.println(pm.find(1));
		comboClient = new ChoiceBox<Client>(listClient);
		comboClient.getSelectionModel().selectFirst();
	}
	
	private void resetTextFields() {
		txtId.clear();
		txtDate.clear();
		txtTotal.clear();
	}
	
	private void initTable(ObservableList<Vente> data,ObservableList<Commande> dataCommand) {
		table.setEditable(true);
		table.setItems(data);

		TableColumn<Vente, Long> colId = new TableColumn<Vente, Long>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<Vente, Long>("Id"));
		table.getColumns().add(colId);

		TableColumn<Vente, String> colDate = new TableColumn<Vente, String>("Date");
		colDate.setMinWidth(80);
		colDate.setCellValueFactory(new PropertyValueFactory<Vente, String>("date"));
		table.getColumns().add(colDate);
		
		TableColumn<Vente, String> colClient = new TableColumn<Vente, String>("Client");
		colClient.setMinWidth(80);
		colClient.setCellValueFactory(new PropertyValueFactory<Vente, String>("client"));
		table.getColumns().add(colClient);

		TableColumn<Vente, Float> colTotal = new TableColumn<Vente, Float>("Total");
		colTotal.setMinWidth(80);
		colTotal.setCellValueFactory(new PropertyValueFactory<Vente, Float>("total"));
		table.getColumns().add(colTotal);

	}
	
	public void initPanes(){
		ObservableList<Vente> data = FXCollections.observableArrayList(list);
		ObservableList<Commande> dataCommand = FXCollections.observableArrayList(listCommand);
		FilteredList<Vente> items = new FilteredList<>(data);
		FilteredList<Commande> itemsCommand = new FilteredList<>(dataCommand);
		items.setPredicate(null);
		initTable(data,dataCommand);
		
    	txtId.setDisable(true);
    	txtTotal.setDisable(true);
    	txtDate.setDisable(true);

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
					v.setTotal(Float.valueOf(txtTotal.getText()));
					v.setClient(cm.find(comboClient.getValue().getId()));
					data.remove(indexOfTable, indexOfTable+1);
					data.add(indexOfTable+1,v);
					pm.update(v);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
			else{
				Alert alertAdd = new Alert(Alert.AlertType.CONFIRMATION);
				alertAdd.setTitle("Message Here...");
				alertAdd.setHeaderText("Look, an Information Dialog");
				alertAdd.setContentText("I have a great message for you!");
				alertAdd.show();

			}
		});
		
		btnSupprimer.setOnAction(e -> {
			if(txtId.getText()!="" ) {
				Vente p =pm.find(Long.parseLong(txtId.getText()));
				if(p!=null) {
					pm.delete(p);
					data.remove(indexOfTable, indexOfTable+1);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Predicate<Vente> date = i -> String.valueOf(i.getDate()).contains(txtSearch.getText());
				Predicate<Vente> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
				Predicate<Vente> total = i -> String.valueOf(i.getTotal()).contains(txtSearch.getText());
				Predicate<Vente> client = i -> String.valueOf(i.getClient()).contains(txtSearch.getText());
				Predicate<Vente> predicate = date.or(total.or(client.or(id)));
				
				table.setItems(items);
				items.setPredicate(predicate);
//				table.getItems()
			}
		});
		
		navBar.getChildren().add(labelNav);
		
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
				
		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelDate,txtDate);
		grid.addRow(2, labelTotal, txtTotal);
		loadCombo();
		CommandeIHM cihm = new CommandeIHM();
		cihm.loadCombo();
		cihm.initPanes();

		grid.addRow(3, labelClient, comboClient);
		grid.add(sideBarLeft,0,4,2,1);
		grid.add( txtSearch,0,5,2,1);
		grid.add(table,0,6,2,1);
		sideBarRight.getChildren().addAll(cihm.grid,cihm.table);

		navBar.getStyleClass().add("navBar");
		navBar.getStyleClass().add("labelNav");
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("sideBarLeft");
		
		
		container.setTop(navBar);
//		container.setLeft(sideBarLeft);
		container.setRight(sideBarRight);
		container.setCenter(grid);
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        //Check whether item is selected and set value of selected item to Label
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {   
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	Vente p = table.getSelectionModel().getSelectedItem();
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtDate.setText(String.valueOf(p.getDate()));
		        	txtTotal.setText(String.valueOf(p.getTotal()));
		        	comboClient.getSelectionModel().select((int)p.getClient().getId()-1);
					cihm.txtId.setText(String.valueOf(p.getId()));


					Predicate<Commande> id = i -> i.getVente().getId()==p.getId();
					Predicate<Commande> predicate = id;

					tableCommand.setItems(itemsCommand);
					itemsCommand.setPredicate(predicate);
		        }
		     }
	     });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {
//		list = new ArrayList<Vente>();
		list = pm.findAll();

//		listCommand = FXCollections.observableArrayList(cmd.findAll());
//
//		listClient = FXCollections.observableArrayList(cm.findAll());
		
		scene = new Scene(container);
		window.setTitle("");
		window.setHeight(600);
		window.setWidth(1000);
		initPanes();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}

}
