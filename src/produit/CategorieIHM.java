package produit;

import java.util.ArrayList;
import java.util.List;
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

public class CategorieIHM extends Application {

	BorderPane root = new BorderPane();
	Scene scene = null;
	
	private static CategorieDAOImpl pm = new CategorieDAOImpl();

	static List<Categorie> list = null;
	
	GridPane grid = new GridPane();
	HBox navBar = new HBox(),sideBarLeft = new HBox();
//	HBox sideBarLeft = new HBox();
	VBox sideBarRight = new VBox();
	BorderPane container = new BorderPane();
	
	Label labelId = new Label("ID : ");
	Label labelDesignation = new Label("Designation : ");
	Label labelNav = new Label("This is header ");
	
	TextField txtSearch = new TextField();
	
	TextField txtId = new TextField();
	TextField txtDesignation = new TextField();
	TableView<Categorie> table = new TableView<Categorie>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;
	
	private void resetTextFields() {
		txtId.clear();
		txtDesignation.clear();
	}
	
	private void initTable(ObservableList<Categorie> data) {
		table.setEditable(true);
		table.setItems(data);
		TableColumn<Categorie, Integer> colId = new TableColumn<Categorie, Integer>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<Categorie, Integer>("Id"));
		table.getColumns().add(colId);

		TableColumn<Categorie, String> colDesignation = new TableColumn<Categorie, String>("Designation");
		colDesignation.setMinWidth(120);
		colDesignation.setCellValueFactory(new PropertyValueFactory<Categorie, String>("designation"));
		table.getColumns().add(colDesignation);
	}
	
	private void initPanes(){
		ObservableList<Categorie> data = FXCollections.observableArrayList(list);
		FilteredList<Categorie> items = new FilteredList<>(data);
		items.setPredicate(null);
		initTable(data);
		
    	txtId.setDisable(true);

		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtDesignation.getText().length()!=0) {
					Categorie p = new Categorie(1,txtDesignation.getText());
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
					Categorie p =pm.find(Integer.parseInt(txtId.getText()));
					if(p!=null) {
						p.setDesignation(txtDesignation.getText());
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
					Categorie p =pm.find(Integer.parseInt(txtId.getText()));
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
				Predicate<Categorie> designation = i -> i.getDesignation().contains(txtSearch.getText());
				Predicate<Categorie> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
				Predicate<Categorie> predicate = designation.or(id);
				
				table.setItems(items);
				items.setPredicate(predicate);
//				table.getItems()
			}
		});
		
		navBar.getChildren().add(labelNav);
		
		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelDesignation,txtDesignation);		
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
		sideBarRight.getChildren().addAll(txtSearch,table);
		grid.add(sideBarLeft,0,2,2,1);
		
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
		        	Categorie p = table.getSelectionModel().getSelectedItem();
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtDesignation.setText(p.getDesignation());
		        	
		        }
		     }
	     });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {
		list = new ArrayList<Categorie>();
		list = pm.findAll();
		scene = new Scene(container);
		window.setTitle("title");
		window.setHeight(600);
		window.setWidth(800);
		initPanes();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}

}
