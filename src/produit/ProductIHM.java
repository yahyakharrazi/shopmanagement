package produit;

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

public class ProductIHM extends Application {

	BorderPane root = new BorderPane();
	Scene scene = null;
	
	private static ProductDAOImpl pm = new ProductDAOImpl();
	private static CategorieDAOImpl cm = new CategorieDAOImpl();

	static List<Product> list = null;
	static ObservableList<Categorie> listCategorie = null;
	
	GridPane grid = new GridPane();
	HBox navBar = new HBox();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	BorderPane container = new BorderPane();
	
	Label labelId = new Label("ID : ");
	Label labelDesignation = new Label("Designation : ");
	Label labelPrixAchat = new Label("PrixAchat : ");
	Label labelPrixVente = new Label("PrixVente : ");
	Label labelCategorie = new Label("Categorie : ");
	
	Label labelNav = new Label("This is header ");
	
	TextField txtSearch = new TextField();
	
	TextField txtId = new TextField();
	TextField txtDesignation = new TextField();
	TextField txtPrixAchat = new TextField();
	TextField txtPrixVente = new TextField();
	ChoiceBox<Categorie> comboCategorie = null;
	TableView<Product> table = new TableView<Product>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;

	private void loadCombo() {
		System.out.println(cm.findAll());
		comboCategorie = new ChoiceBox<Categorie>(listCategorie);
		comboCategorie.getSelectionModel().selectFirst();
	}
	
	private void resetTextFields() {
		txtId.clear();
		txtDesignation.clear();
		txtPrixAchat.clear();
		txtPrixVente.clear();
	}
	
	private void initTable(ObservableList<Product> data) {
		table.setEditable(true);
		table.setItems(data);
		TableColumn<Product, Integer> colId = new TableColumn<Product, Integer>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Id"));
		table.getColumns().add(colId);

		TableColumn<Product, String> colDesignation = new TableColumn<Product, String>("Designation");
		colDesignation.setMinWidth(120);
		colDesignation.setCellValueFactory(new PropertyValueFactory<Product, String>("designation"));
		table.getColumns().add(colDesignation);
		
		TableColumn<Product, Float> colPrixAchat = new TableColumn<Product, Float>("Prix Achat");
		colPrixAchat.setMinWidth(80);
		colPrixAchat.setCellValueFactory(new PropertyValueFactory<Product, Float>("prixAchat"));
		table.getColumns().add(colPrixAchat);
		
		TableColumn<Product, Float> colPrixVente = new TableColumn<Product, Float>("Prix Achat");
		colPrixVente.setMinWidth(80);
		colPrixVente.setCellValueFactory(new PropertyValueFactory<Product, Float>("prixVente"));
		table.getColumns().add(colPrixVente);
		
		TableColumn<Product, String> colCategorie = new TableColumn<Product, String>("Categorie");
		colCategorie.setMinWidth(80);
		colCategorie.setCellValueFactory(new PropertyValueFactory<Product, String>("categorie"));
		table.getColumns().add(colCategorie);
	}
	
	private void initPanes(){
		ObservableList<Product> data = FXCollections.observableArrayList(list);
		FilteredList<Product> items = new FilteredList<>(data);
		items.setPredicate(null);
		initTable(data);
		
    	txtId.setDisable(true);

		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtDesignation.getText().length()!=0 && txtPrixAchat.getText().length()!=0 && txtPrixVente.getText().length()!=0) {
					Product p = new Product(1,txtDesignation.getText(),Float.parseFloat(txtPrixAchat.getText()), Float.parseFloat(txtPrixVente.getText()), cm.find(comboCategorie.getValue().getId()));
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
					Product p =pm.find(Integer.parseInt(txtId.getText()));
					if(p!=null) {
						p.setDesignation(txtDesignation.getText());
						p.setPrixAchat(Float.valueOf(txtPrixAchat.getText()));
						p.setPrixVente(Float.valueOf(txtPrixVente.getText()));
						p.setCategorie(cm.find(comboCategorie.getValue().getId()));
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
					Product p =pm.find(Integer.parseInt(txtId.getText()));
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
				Predicate<Product> designation = i -> i.getDesignation().contains(txtSearch.getText());
				Predicate<Product> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
				Predicate<Product> prixAchat = i -> String.valueOf(i.getPrixAchat()).contains(txtSearch.getText());
				Predicate<Product> prixVente = i -> String.valueOf(i.getPrixVente()).contains(txtSearch.getText());
				Predicate<Product> categorie = i -> String.valueOf(i.getCategorie()).contains(txtSearch.getText());
				Predicate<Product> predicate = designation.or(prixAchat.or(prixVente.or(categorie.or(id))));
				
				table.setItems(items);
				items.setPredicate(predicate);
//				table.getItems()
			}
		});
		
		navBar.getChildren().add(labelNav);
		
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
				
		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelDesignation,txtDesignation);
		grid.addRow(2, labelPrixAchat, txtPrixAchat);
		grid.addRow(3, labelPrixVente, txtPrixVente);
		grid.addRow(4, labelCategorie, comboCategorie);
		
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
		        	Product p = table.getSelectionModel().getSelectedItem();
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtDesignation.setText(p.getDesignation());
		        	txtPrixAchat.setText(String.valueOf(p.getPrixAchat()));
		        	txtPrixVente.setText(String.valueOf(p.getPrixVente()));
		        	comboCategorie.getSelectionModel().select((int)p.getCategorie().getId()-1);
		        }
		     }
	     });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {
		list = new ArrayList<Product>();
		list = pm.findAll();
		listCategorie = FXCollections.observableArrayList(cm.findAll());
		
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
