package vente;

import java.sql.Date;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	
	private static VenteDAOImpl pm = new VenteDAOImpl();
	private static ClientDAOImpl cm = new ClientDAOImpl();

	static List<Vente> list = null;
	static ObservableList<Client> listClient = null;
	
	GridPane grid = new GridPane();
	HBox navBar = new HBox();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	BorderPane container = new BorderPane();
	
	Label labelId = new Label("ID : ");
	Label labelDate = new Label("Date : ");
	Label labelTotal = new Label("Total : ");
	Label labelClient = new Label("Client : ");
	
	Label labelNav = new Label("This is header ");
	
	TextField txtSearch = new TextField();
	
	TextField txtId = new TextField();
	TextField txtDate = new TextField();
	TextField txtTotal = new TextField();
	ChoiceBox<Client> comboClient = null;
	TableView<Vente> table = new TableView<Vente>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;

	private void loadCombo() {
		System.out.println(pm.findAll());
		System.out.println(pm.find(1));
		comboClient = new ChoiceBox<Client>(listClient);
		comboClient.getSelectionModel().selectFirst();
	}
	
	private void resetTextFields() {
		txtId.clear();
		txtDate.clear();
		txtTotal.clear();
	}
	
	private void initTable(ObservableList<Vente> data) {
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
		
		TableColumn<Vente, Float> colPrixVente = new TableColumn<Vente, Float>("Prix Achat");
		colPrixVente.setMinWidth(80);
		colPrixVente.setCellValueFactory(new PropertyValueFactory<Vente, Float>("prixVente"));
		table.getColumns().add(colPrixVente);
		
	}
	
	private void initPanes(){
		ObservableList<Vente> data = FXCollections.observableArrayList(list);
		FilteredList<Vente> items = new FilteredList<>(data);
		items.setPredicate(null);
		initTable(data);
		
    	txtId.setDisable(true);
    	txtTotal.setDisable(true);
    	
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtDate.getText().length()!=0) {
					try {
						System.out.println("before create : " + comboClient.getValue().getId());
						Vente v = new Vente(1,txtDate.getText(),0, cm.find(comboClient.getValue().getId()));
						System.out.println("before create : " + cm.find(comboClient.getValue().getId()));
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
					
				}
			}
		});
				
		btnNouveau.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				resetTextFields();
				table.getSelectionModel().clearSelection();
			}
		});
		
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="") {
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
			}
		});
		
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="" ) {
					Vente p =pm.find(Integer.parseInt(txtId.getText()));
					if(p!=null) {
						pm.delete(p);
						data.remove(indexOfTable, indexOfTable+1);
						table.getSelectionModel().clearSelection();
						resetTextFields();
					}
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
		grid.addRow(3, labelClient, comboClient);
		sideBarRight.getChildren().addAll(txtSearch,table);
		
		navBar.getStyleClass().add("navBar");
		navBar.getStyleClass().add("labelNav");
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("sideBarLeft");
		
		
		container.setTop(navBar);
		container.setLeft(sideBarLeft);
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
		        }
		     }
	     });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {
		list = new ArrayList<Vente>();
		list = pm.findAll();
		listClient = FXCollections.observableArrayList(cm.findAll());
		
		loadCombo();
		scene = new Scene(container);
		window.setTitle("title");
		window.setHeight(600);
		window.setWidth(1000);
		initPanes();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}

}
