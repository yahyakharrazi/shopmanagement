package paiement;

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
import vente.VenteDAOImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PaiementIHM extends Application {

    BorderPane root = new BorderPane();
    Scene scene = null;

    private static PaiementDAOImpl pm = new PaiementDAOImpl();
    private static VenteDAOImpl vm = new VenteDAOImpl();

    static List<Paiement> list = null;

    GridPane grid = new GridPane();
    HBox navBar = new HBox();
    VBox sideBarLeft = new VBox();
    VBox sideBarRight = new VBox();
    BorderPane container = new BorderPane();

    Label labelId = new Label("ID : ");
    Label labelVente = new Label("Vente : ");
    Label labelTotal = new Label("Total : ");
    Label labelDate = new Label("Date : ");
    Label labelCategorie = new Label("Categorie : ");

    Label labelNav = new Label("This is header ");

    TextField txtSearch = new TextField();

    TextField txtId = new TextField();
    TextField txtVente = new TextField();
    TextField txtTotal = new TextField();
    TextField txtDate = new TextField();
    ChoiceBox comboCategorie;
    TableView<Paiement> table = new TableView<Paiement>();

    Button btnModifier = new Button("Modifier");
    Button btnAjouter = new Button("Ajouter");
    Button btnNouveau = new Button("Nouveau");
    Button btnSupprimer = new Button("Supprimer");

    int indexOfTable=-1;

    private void loadCombo() {
        comboCategorie = new ChoiceBox();
        comboCategorie.getItems().addAll("espece","cheque","online","traites");
        comboCategorie.getSelectionModel().selectFirst();
    }

    private void resetTextFields() {
        txtId.clear();
        txtVente.clear();
        txtTotal.clear();
        txtDate.clear();
    }

    private void initTable(ObservableList<Paiement> data) {
        table.setEditable(true);
        table.setItems(data);
        TableColumn<Paiement, Long> colId = new TableColumn<Paiement, Long>("ID");
        colId.setMinWidth(50);
        colId.setCellValueFactory(new PropertyValueFactory<Paiement, Long>("id"));
        table.getColumns().add(colId);

        TableColumn<Paiement, Long> colVente = new TableColumn<Paiement, Long>("Vente");
        colVente.setMinWidth(120);
        colVente.setCellValueFactory(new PropertyValueFactory<Paiement, Long>("vente"));
        table.getColumns().add(colVente);

        TableColumn<Paiement, Float> colTotal = new TableColumn<Paiement, Float>("Montantt");
        colTotal.setMinWidth(80);
        colTotal.setCellValueFactory(new PropertyValueFactory<Paiement, Float>("total"));
        table.getColumns().add(colTotal);

        TableColumn<Paiement, String> colDate = new TableColumn<Paiement, String>("Date");
        colDate.setMinWidth(80);
        colDate.setCellValueFactory(new PropertyValueFactory<Paiement, String>("date"));
        table.getColumns().add(colDate);

        TableColumn<Paiement, String> colCategorie = new TableColumn<Paiement, String>("Reglement");
        colCategorie.setMinWidth(80);
        colCategorie.setCellValueFactory(new PropertyValueFactory<Paiement, String>("reglement"));
        table.getColumns().add(colCategorie);
    }

    private void initPanes(){
        ObservableList<Paiement> data = FXCollections.observableArrayList(list);
        FilteredList<Paiement> items = new FilteredList<>(data);
        items.setPredicate(null);
        initTable(data);

        txtId.setDisable(true);

        btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(txtVente.getText().length()!=0 && txtTotal.getText().length()!=0 && txtDate.getText().length()!=0) {
                    Paiement p = new Paiement(1,vm.find(Long.valueOf(txtVente.getText())),Float.parseFloat(txtTotal.getText()), txtDate.getText(), comboCategorie.getValue().toString());
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
                if(txtId.getText().length()!=0) {
                    Paiement p =pm.find(Integer.parseInt(txtId.getText()));
                    if(p!=null) {
                        p.setVente(vm.find(Long.valueOf(txtVente.getText())));
                        p.setTotal(Float.valueOf(txtTotal.getText()));
                        p.setReglement(txtDate.getText());
                        p.setReglement(comboCategorie.getValue().toString());
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
                    Paiement p =pm.find(Integer.parseInt(txtId.getText()));
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
                Predicate<Paiement> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
                Predicate<Paiement> total = i -> String.valueOf(i.getTotal()).contains(txtSearch.getText());
                Predicate<Paiement> date = i -> String.valueOf(i.getDate()).contains(txtSearch.getText());
                Predicate<Paiement> predicate = total.or(date.or(id));

                table.setItems(items);
                items.setPredicate(predicate);
//				table.getItems()
            }
        });

        navBar.getChildren().add(labelNav);

        sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);

        grid.addRow(0, labelId,txtId);
        grid.addRow(1, labelVente,txtVente);
        grid.addRow(2, labelTotal, txtTotal);
        grid.addRow(3, labelDate, txtDate);
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
                    Paiement p = table.getSelectionModel().getSelectedItem();
                    txtId.setText(String.valueOf(p.getId()));
                    txtVente.setText(p.getVente().getId()+"");
                    txtTotal.setText(String.valueOf(p.getTotal()));
                    txtDate.setText(String.valueOf(p.getDate()));
                    comboCategorie.getSelectionModel().select(p.getReglement());
                }
            }
        });



    }

    public static void main(String[] args) {

        Application.launch(args);

    }

    @Override
    public void start(Stage window) throws Exception {
        list = new ArrayList<Paiement>();
        list = pm.findAll();

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
