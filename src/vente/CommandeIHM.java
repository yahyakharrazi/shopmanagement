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



	GridPane grid2 = new GridPane();
	HBox navBar = new HBox();
	HBox sideBarLeft2 = new HBox();
	VBox sideBarRight2 = new VBox();
	BorderPane container2 = new BorderPane();
	
	Label labelIdCmd = new Label("ID : ");
	Label labelVente = new Label("ID Vente : ");
	Label labelTotalCmd = new Label("Total : ");
	Label labelQuantite = new Label("Quantite : ");
	Label labelProduct = new Label("Produit : ");
	
	Label labelNav = new Label("Commande form ");
	
	TextField txtSearch2 = new TextField();
	
	TextField txtIdCmd = new TextField();
	TextField txtVente = new TextField();
	TextField txtTotalCmd = new TextField();
	TextField txtQuantite = new TextField();
	ChoiceBox<Product> comboProduct = null;
	TableView<Commande> tableCmd = new TableView<>();
			
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
		navBar.getStyleClass().add("labelNav");
		sideBarLeft2.getStyleClass().add("sideBarLeft");
		sideBarRight2.getStyleClass().add("sideBarRight");
		grid2.getStyleClass().add("sideBarLeft");

		container2.setTop(navBar);
//		container2.setLeft(sideBarLeft);
		container2.setRight(sideBarRight2);
		container2.setCenter(grid2);

	}

	public void loadCombo() {
		comboProduct = new ChoiceBox<>(listProduct);
		comboProduct.getSelectionModel().selectFirst();
	}
	
	private void resetTextFieldsCmd() {
		txtIdCmd.clear();
		txtVente.clear();
		txtQuantite.clear();
		txtTotalCmd.clear();
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
		ObservableList<Commande> dataCmd = FXCollections.observableArrayList(list);
		FilteredList<Commande> itemsCommand = new FilteredList<>(dataCmd);
		itemsCommand.setPredicate(null);
		initTableCmd(dataCmd);

    	txtQuantite.setOnKeyReleased(e -> {
			if(txtQuantite.getText().length()==0)
				txtTotalCmd.setText("0");
			else
				txtTotalCmd.setText(valueOf((cm.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()))));
		});
    	
		btnAjouterCmd.setOnAction(e -> {
			if(txtVente.getText().length()!=0 && txtQuantite.getText().length()!=0) {
				float soustotal = (cm.find(comboProduct.getValue().getId()).getPrixVente()*Integer.parseInt(txtQuantite.getText()));
				Commande cmd = new Commande(1,cm.find(comboProduct.getValue().getId()),vm.find(Long.parseLong(txtVente.getText())),Integer.parseInt(txtQuantite.getText()), soustotal);
				for (Commande c : tableCmd.getItems()) {
					map.put(c.getProduct().getId(),c);
				}

				System.out.println(map);

				if(map.containsKey(cmd.getProduct().getId())){
					System.out.println("what im i doing here");
					cmd.setQuantite(cmd.getQuantite()+map.get(cmd.getProduct().getId()).getQuantite());
					cmd.setTotal(cmd.getQuantite()*cmd.getProduct().getPrixVente());
					dataCmd.set(myIndexOf(list,cmd),cmd);
					pm.update(cmd);
					tableCmd.getSelectionModel().clearSelection();
					resetTextFieldsCmd();
				}
				else{
					long id = pm.create(cmd);
					System.out.println(id);
					if(id > 0) {
						System.out.println("what is this");
						cmd.setId(id);
//						tableCmd.setItems(null);
//						dataCmd.add(cmd);
						itemsCommand.add(cmd);
						tableCmd.setItems(itemsCommand);

						resetTextFieldsCmd();
					}
				}

//				System.out.println(map);

			}
		});
				
		btnNouveauCmd.setOnAction(e -> {
			resetTextFieldsCmd();
			tableCmd.getSelectionModel().clearSelection();
		});
		
		btnModifierCmd.setOnAction(e -> {
			if(txtIdCmd.getText().length()!=0) {
				Commande p =pm.find(Integer.parseInt(txtIdCmd.getText()));
				if(p!=null) {
					p.setVente(vm.find(Integer.parseInt(txtVente.getText())));
					p.setQuantite(Integer.parseInt(txtQuantite.getText()));
					p.setTotal(Float.parseFloat(txtTotalCmd.getText()));
					p.setProduct(cm.find(comboProduct.getValue().getId()));
					dataCmd.set(myIndexOf(dataCmd,p),p);

					pm.update(p);

					tableCmd.getSelectionModel().clearSelection();
					resetTextFieldsCmd();
				}
			}
		});
		
		btnSupprimerCmd.setOnAction(e -> {
			if(txtIdCmd.getText().length()!=0) {
				Commande p =pm.find(Integer.parseInt(txtIdCmd.getText()));
				if(p!=null) {
					dataCmd.remove(myIndexOf(dataCmd,p));
					pm.delete(p);
					tableCmd.getSelectionModel().clearSelection();
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

	}

	public static void main(String[] args) {	Application.launch(args); }

	@Override
	public void start(Stage window) throws Exception {

		loadCombo();
		scene = new Scene(container2);
		window.setTitle("title");
		window.setHeight(600);
		window.setWidth(1000);
		initPanes();
		scene.getStylesheets().add("MyCss.css");
		window.setScene(scene);
		window.show();
	}

}
