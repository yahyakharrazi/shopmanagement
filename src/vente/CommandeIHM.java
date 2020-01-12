package vente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import client.Client;
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

import static java.lang.String.valueOf;

public class CommandeIHM extends Application {




	BorderPane root = new BorderPane();
	Scene scene = null;
	
	private static CommandeDAOImpl pm = new CommandeDAOImpl();
	private static ProductDAOImpl cm = new ProductDAOImpl();
	private static VenteDAOImpl vm = new VenteDAOImpl();



	static List<Commande> list = null;
//	static List<Commande> listVente = null;
	static ObservableList<Product> listProduct = null;
	HashMap<Long,Commande> map = new HashMap<>();

	public CommandeIHM(){
		list = pm.findAll();
		listProduct = FXCollections.observableArrayList(cm.findAll());
	}



	GridPane grid = new GridPane();
	HBox navBar = new HBox();
	HBox sideBarLeft = new HBox();
	VBox sideBarRight = new VBox();
	BorderPane container = new BorderPane();
	
	Label labelId = new Label("ID : ");
	Label labelVente = new Label("ID Vente : ");
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
	TableView<Commande> table = new TableView<>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

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

	public void loadCombo() {
		comboProduct = new ChoiceBox<>(listProduct);
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
		TableColumn<Commande, Integer> colId = new TableColumn<>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		table.getColumns().add(colId);

		TableColumn<Commande, String> colDesignation = new TableColumn<>("Quantite");
		colDesignation.setMinWidth(120);
		colDesignation.setCellValueFactory(new PropertyValueFactory<>("quantite"));
		table.getColumns().add(colDesignation);
		
		TableColumn<Commande, Float> colPrixAchat = new TableColumn<>("Total");
		colPrixAchat.setMinWidth(80);
		colPrixAchat.setCellValueFactory(new PropertyValueFactory<>("total"));
		table.getColumns().add(colPrixAchat);
		
		TableColumn<Commande, String> colPrixVente = new TableColumn<>("Vente");
		colPrixVente.setMinWidth(80);
		colPrixVente.setCellValueFactory(new PropertyValueFactory<>("vente"));
		table.getColumns().add(colPrixVente);
		
		TableColumn<Commande, String> colProduct = new TableColumn<>("Product");
		colProduct.setMinWidth(80);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
		table.getColumns().add(colProduct);
	}
	
	public void initPanes(){
		ObservableList<Commande> data = FXCollections.observableArrayList(list);
		FilteredList<Commande> items = new FilteredList<>(data);
		items.setPredicate(null);
		initTable(data);
		
    	txtId.setDisable(true);
    	txtVente.setDisable(true);
    	txtTotal.setDisable(true);

    	txtQuantite.setOnKeyReleased(e -> {
			if(txtQuantite.getText().length()==0)
				txtTotal.setText("0");
			else
				txtTotal.setText(valueOf((cm.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()))));
		});
    	
		btnAjouter.setOnAction(e -> {
			if(txtVente.getText().length()!=0 && txtQuantite.getText().length()!=0) {
				float soustotal = (cm.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()));
				Commande cmd = new Commande(1,cm.find(comboProduct.getValue().getId()),vm.find(Long.parseLong(txtVente.getText())),Integer.parseInt(txtQuantite.getText()), soustotal);
				for (Commande c : list) {
					map.put(c.getProduct().getId(),c);
				}

				System.out.println(map);

				if(map.containsKey(cmd.getProduct().getId())){
					cmd.setQuantite(cmd.getQuantite()+map.get(cmd.getProduct().getId()).getQuantite());
					cmd.setTotal(cmd.getQuantite()*cmd.getProduct().getPrixVente());
					data.set(myIndexOf(list,cmd),cmd);
					pm.update(cmd);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
				else{
					long id = pm.create(cmd);
					if(id > 0) {
						cmd.setId(id);
						data.add(cmd);
						resetTextFields();
					}
				}

				System.out.println(map);

			}
		});
				
		btnNouveau.setOnAction(e -> {
			resetTextFields();
			table.getSelectionModel().clearSelection();
		});
		
		btnModifier.setOnAction(e -> {
			if(txtId.getText().length()!=0) {
				Commande p =pm.find(Integer.parseInt(txtId.getText()));
				if(p!=null) {
					p.setVente(vm.find(Integer.parseInt(txtVente.getText())));
					p.setQuantite(Integer.parseInt(txtQuantite.getText()));
					p.setTotal(Float.parseFloat(txtTotal.getText()));
					p.setProduct(cm.find(comboProduct.getValue().getId()));
					data.set(myIndexOf(data,p),p);
					pm.update(p);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		btnSupprimer.setOnAction(e -> {
			if(txtId.getText().length()!=0) {
				Commande p =pm.find(Integer.parseInt(txtId.getText()));
				if(p!=null) {
					data.remove(myIndexOf(data,p));
					pm.delete(p);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		txtSearch.setOnKeyReleased(event -> {
			Predicate<Commande> id = i -> valueOf(i.getId()).contains(txtSearch.getText());
			Predicate<Commande> quantite = i -> valueOf(i.getQuantite()).contains(txtSearch.getText());
			Predicate<Commande> total = i -> valueOf(i.getTotal()).contains(txtSearch.getText());
			Predicate<Commande> vente = i -> valueOf(i.getVente().getId()).contains(txtSearch.getText());
			Predicate<Commande> product = i -> valueOf(i.getProduct()).contains(txtSearch.getText());
			Predicate<Commande> predicate = quantite.or(total.or(vente.or(product.or(id))));

			table.setItems(items);
			items.setPredicate(predicate);
//				table.getItems()
		});
		
		navBar.getChildren().add(labelNav);
		
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
				
		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelQuantite, txtQuantite);
		grid.addRow(2, labelTotal, txtTotal);
		grid.addRow(3, labelVente, txtVente);
		grid.addRow(4, labelProduct, comboProduct);
		grid.add(sideBarLeft,0,5,2,1);
		sideBarRight.getChildren().addAll(txtSearch,table);
		
		navBar.getStyleClass().add("navBar");
		navBar.getStyleClass().add("labelNav");
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("sideBarLeft");
		
		
		container.setTop(navBar);
//		container.setLeft(sideBarLeft);
		container.setRight(sideBarRight);
		container.setCenter(grid);
		
		table.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
			//Check whether item is selected and set value of selected item to Label
			if(table.getSelectionModel().getSelectedItem() != null)
			{
				resetTextFields();
				Commande p = table.getSelectionModel().getSelectedItem();
				txtId.setText(valueOf(p.getId()));
				txtVente.setText(valueOf(p.getVente().getId()));
				txtQuantite.setText(valueOf(p.getQuantite()));
				txtTotal.setText(valueOf(p.getTotal()));
				comboProduct.getSelectionModel().select((int)p.getProduct().getId()-1);
			}
		 });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {

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
