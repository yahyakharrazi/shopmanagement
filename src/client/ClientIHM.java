package client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientIHM extends Application {

	BorderPane root = new BorderPane();
	public Scene scene = null;

	private static ClientDAOImpl pm = new ClientDAOImpl();

	static List<Client> list = null;

	public ClientIHM(){
		list = new ArrayList<>();
		list = pm.findAll();
	}

	public GridPane grid = new GridPane();
	public BorderPane container = new BorderPane();
	HBox navBar = new HBox();
	HBox sideBarLeft = new HBox();
	VBox sideBarRight = new VBox();

	Label labelId = new Label("ID : ");
	Label labelNom = new Label("Nom : ");
	Label labelPrenom = new Label("Prenom : ");
	Label labelTelephone = new Label("Telephone : ");
	Label labelEmail = new Label("Email : ");
	Label labelAdresse = new Label("Adresse : ");
	Label labelNav = new Label("Gestion clients");
	
	TextField txtSearch = new TextField();
	
	TextField txtId = new TextField();
	TextField txtNom = new TextField();
	TextField txtPrenom = new TextField();
	TextField txtTelephone = new TextField();
	TextField txtEmail = new TextField();
	TextField txtAdresse = new TextField();
	TableView<Client> table = new TableView<>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("Nouveau");
	Button btnSupprimer = new Button("Supprimer");	

	public int myIndexOf(List<Client> al,Client c){
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
		txtNom.clear();
		txtPrenom.clear();
		txtTelephone.clear();
		txtEmail.clear();
		txtAdresse.clear();
	}
	
	private void initTable(ObservableList<Client> data) {
		table.setEditable(true);
		table.setItems(data);
		TableColumn<Client, Long> colId = new TableColumn<>("ID");
		colId.setMinWidth(50);
		colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		table.getColumns().add(colId);

		TableColumn<Client, String> colNom = new TableColumn<>("NOM");
		colNom.setMinWidth(60);
		colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		table.getColumns().add(colNom);

		TableColumn<Client, String> colPrenom = new TableColumn<>("PRENOM");
		colPrenom.setMinWidth(60);
		colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		table.getColumns().add(colPrenom);

		TableColumn<Client, String> colEmail = new TableColumn<>("EMAIL");
		colEmail.setMinWidth(60);
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		table.getColumns().add(colEmail);

		TableColumn<Client, String> colTelephone = new TableColumn<>("TELEPHONE");
		colTelephone.setMinWidth(60);
		colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
		table.getColumns().add(colTelephone);

		TableColumn<Client, String> colAdresse = new TableColumn<>("ADRESSE");
		colAdresse.setMinWidth(60);
		colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
		table.getColumns().add(colAdresse);

	}
	
	public void initPanes(){
		ObservableList<Client> data = FXCollections.observableArrayList(list);
		FilteredList<Client> items = new FilteredList<>(data);
		items.setPredicate(null);
		initTable(data);
    	txtId.setDisable(true);

		btnAjouter.setOnAction(e -> {
			if(txtNom.getText().length()!=0 && txtPrenom.getText().length()!=0) {

				Client c = new Client(1,txtNom.getText(),txtPrenom.getText(),txtTelephone.getText(),txtEmail.getText(),txtAdresse.getText());
				long id = pm.create(c);
				if(id > 0) {
					c.setId(id);
					data.add(c);
					resetTextFields();
				}
			}
		});
				
		btnNouveau.setOnAction(e -> {
			resetTextFields();
			table.getSelectionModel().clearSelection();
		});
		
		btnModifier.setOnAction(e -> {
			if(txtId.getText().length()!=0) {
				Client p =pm.find(Integer.parseInt(txtId.getText()));
				if(p!=null) {
					p.setNom(txtNom.getText());
					p.setPrenom(txtPrenom.getText());
					p.setEmail(txtEmail.getText());
					p.setTelephone(txtTelephone.getText());
					p.setAdresse(txtAdresse.getText());
					data.set(myIndexOf(list,p),p);
//					list.set(myIndexOf(list,p),p);
//					data.clear();
//					data.addAll(list);
//					pm.update(p);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		btnSupprimer.setOnAction(e -> {
			if(txtId.getText().length()!=0) {
				Client p =pm.find(Integer.parseInt(txtId.getText()));
				if(p!=null) {
					data.remove(myIndexOf(data,p), myIndexOf(data,p)+1);
					pm.delete(p);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		txtSearch.setOnKeyReleased(event -> {
			Predicate<Client> nom = i -> i.getNom().contains(txtSearch.getText());
			Predicate<Client> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
			Predicate<Client> prenom = i -> i.getPrenom().contains(txtSearch.getText());
			Predicate<Client> email = i -> i.getEmail().contains(txtSearch.getText());
			Predicate<Client> telephone = i -> i.getTelephone().contains(txtSearch.getText());
			Predicate<Client> adresse = i -> i.getAdresse().contains(txtSearch.getText());
			Predicate<Client> predicate = id.or(nom.or(prenom.or(email.or(telephone.or(adresse)))));

			table.setItems(items);
			items.setPredicate(predicate);
//				table.getItems()
		});
		
		navBar.getChildren().add(labelNav);
		

		grid.addRow(0, labelId,txtId);
		grid.addRow(1, labelNom,txtNom);
		grid.addRow(2, labelPrenom, txtPrenom);
		grid.addRow(3, labelEmail,txtEmail);
		grid.addRow(4, labelTelephone, txtTelephone);		
		grid.addRow(5, labelAdresse, txtAdresse);
		grid.add(sideBarLeft,0,6, 2, 1);

		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);

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
				Client p = table.getSelectionModel().getSelectedItem();
				txtId.setText(String.valueOf(p.getId()));
				txtNom.setText(p.getNom());
				txtPrenom.setText(p.getPrenom());
				txtEmail.setText(p.getEmail());
				txtTelephone.setText(p.getTelephone());
				txtAdresse.setText(p.getAdresse());

			}
		 });
	

		
	}

	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

	@Override
	public void start(Stage window) throws Exception {
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
