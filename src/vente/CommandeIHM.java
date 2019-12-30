package vente;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

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
import produit.Product;
import produit.ProductDAOImpl;

public class CommandeIHM extends Application {

	BorderPane root = new BorderPane();
	Scene scene = null;
	
	private static CommandeDAOImpl pm = new CommandeDAOImpl();
	private static ProductDAOImpl cm = new ProductDAOImpl();
	private static VenteDAOImpl vm = new VenteDAOImpl();

	static List<Commande> list = null;
	static List<Commande> listVente = null;
	static ObservableList<Product> listProduct = null;
	
	GridPane grid = new GridPane();
	HBox navBar = new HBox();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	BorderPane container = new BorderPane();
	
	Label labelId = new Label("ID : ");
	Label labelVente = new Label("Vente : ");
	Label labelTotal = new Label("Total : ");
	Label labelQuantite = new Label("Quantite : ");
	Label labelProduct = new Label("Produit : ");
	
	Label labelNav = new Label("Commande form ");
	
	TextField txtSearch = new TextField();
	
	TextField txtId = new TextField();
	TextField txtVente = new TextField();
	TextField txtTotal = new TextField();
	TextField txtQuantite = new TextField();
	ChoiceBox<Product> comboProduct = null;
	TableView<Commande> table = new TableView<Commande>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;

	private void loadCombo() {
		System.out.println(cm.findAll());
		comboProduct = new ChoiceBox<Product>(listProduct);
		comboProduct.getSelectionModel().selectFirst();
	}
	
	private void resetTextFields() {
		txtId.clear();
		txtVente.clear();
		txtQuantite.clear();
		txtTotal.clear();
	}
	
	private void initTable(ObservableList<Commande> data) {
		table.setEditable(true);
		table.setItems(data);
		TableColumn<Commande, Integer> colId = new TableColumn<Commande, Integer>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<Commande, Integer>("Id"));
		table.getColumns().add(colId);

		TableColumn<Commande, String> colDesignation = new TableColumn<Commande, String>("Quantite");
		colDesignation.setMinWidth(120);
		colDesignation.setCellValueFactory(new PropertyValueFactory<Commande, String>("quantite"));
		table.getColumns().add(colDesignation);
		
		TableColumn<Commande, Float> colPrixAchat = new TableColumn<Commande, Float>("Total");
		colPrixAchat.setMinWidth(80);
		colPrixAchat.setCellValueFactory(new PropertyValueFactory<Commande, Float>("total"));
		table.getColumns().add(colPrixAchat);
		
		TableColumn<Commande, String> colPrixVente = new TableColumn<Commande, String>("Vente");
		colPrixVente.setMinWidth(80);
		colPrixVente.setCellValueFactory(new PropertyValueFactory<Commande, String>("vente"));
		table.getColumns().add(colPrixVente);
		
		TableColumn<Commande, String> colProduct = new TableColumn<Commande, String>("Product");
		colProduct.setMinWidth(80);
		colProduct.setCellValueFactory(new PropertyValueFactory<Commande, String>("produit"));
		table.getColumns().add(colProduct);
	}
	
	private void initPanes(){
		ObservableList<Commande> data = FXCollections.observableArrayList(list);
		FilteredList<Commande> items = new FilteredList<>(data);
		items.setPredicate(null);
		initTable(data);
		
    	txtId.setDisable(true);
    	txtTotal.setDisable(true);

    	txtQuantite.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				if(txtQuantite.getText().length()==0) 
					txtTotal.setText("0");
				else
					txtTotal.setText(String.valueOf((cm.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()))));				
			}
    		
    	});
    	
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtVente.getText().length()!=0 && txtQuantite.getText().length()!=0) {
					float soustotal = (cm.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()));
					Commande p = new Commande(1,cm.find(comboProduct.getValue().getId()),vm.find(Long.parseLong(txtVente.getText())),Integer.parseInt(txtQuantite.getText()), soustotal);
					
					long id = pm.create(p);
					if(id > 0) {
						p.setId(id);
						data.add(p);
						resetTextFields();
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
					Commande p =pm.find(Integer.parseInt(txtId.getText()));
					if(p!=null) {
						p.setVente(vm.find(Integer.valueOf(txtVente.getText())));
						p.setQuantite(Integer.valueOf(txtQuantite.getText()));
						p.setTotal(Float.valueOf(txtTotal.getText()));
						p.setProduct(cm.find(comboProduct.getValue().getId()));
						data.remove(indexOfTable, indexOfTable+1);
						data.add(indexOfTable+1,p);
						pm.update(p);
						table.getSelectionModel().clearSelection();
						resetTextFields();
					}
				}
			}
		});
		
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="") {
					Commande p =pm.find(Integer.parseInt(txtId.getText()));
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
				Predicate<Commande> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
				Predicate<Commande> quantite = i -> String.valueOf(i.getQuantite()).contains(txtSearch.getText());
				Predicate<Commande> total = i -> String.valueOf(i.getTotal()).contains(txtSearch.getText());
				Predicate<Commande> vente = i -> String.valueOf(i.getVente().getId()).contains(txtSearch.getText());
				Predicate<Commande> product = i -> String.valueOf(i.getProduct()).contains(txtSearch.getText());
				Predicate<Commande> predicate = quantite.or(total.or(vente.or(product.or(id))));
				
				table.setItems(items);
				items.setPredicate(predicate);
//				table.getItems()
			}
		});
		
		navBar.getChildren().add(labelNav);
		
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
				
		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelQuantite, txtQuantite);
		grid.addRow(2, labelTotal, txtTotal);
		grid.addRow(3, labelVente, txtVente);
		grid.addRow(4, labelProduct, comboProduct);
		
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
		        	Commande p = table.getSelectionModel().getSelectedItem();
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtVente.setText(String.valueOf(p.getVente().getId()));
		        	txtQuantite.setText(String.valueOf(p.getQuantite()));
		        	txtTotal.setText(String.valueOf(p.getTotal()));
		        	comboProduct.getSelectionModel().select((int)p.getProduct().getId()-1);
		        }
		     }
	     });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {
		list = new ArrayList<Commande>();
		list = pm.findAll();
		listProduct = FXCollections.observableArrayList(cm.findAll());
		
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
