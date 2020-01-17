package paiement;

import banque.Compte;
import client.Client;
import client.ClientDAOImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import vente.VenteDAOImpl;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PaiementIHM extends Application {

//    public Node container;
    BorderPane root = new BorderPane();
    Scene scene = null;

    private static PaiementDAOImpl pm = new PaiementDAOImpl();
    private static VenteDAOImpl vm = new VenteDAOImpl();

    static List<Paiement> list = null;

    public PaiementIHM(){
        list = pm.findAll();

        ObservableList<Paiement> data = FXCollections.observableArrayList(list);
        FilteredList<Paiement> items = new FilteredList<>(data);

    }

    public GridPane grid = new GridPane();
    HBox navBar = new HBox();
    HBox sideBarLeft = new HBox();
    VBox sideBarRight = new VBox();
    public BorderPane container = new BorderPane();
    StackPane sp = new StackPane();

    Label labelId = new Label("ID : ");
    Label labelVente = new Label("Vente : ");
    Label labelTotal = new Label("Total : ");
    Label labelDate = new Label("Date : ");
    Label labelCategorie = new Label("Categorie : ");
    Label labelNav = new Label("Gestion des Paiements");


    TextField txtSearch = new TextField();

    TextField txtId = new TextField();
    TextField txtVente = new TextField();
    TextField txtTotal = new TextField();
    TextField txtDate = new TextField();
    DatePicker dp = new DatePicker();

    ChoiceBox comboCategorie = new ChoiceBox(FXCollections.observableArrayList("espece","cheque","online","traites"));
    TableView<Paiement> table = new TableView<>();

    Button btnModifier = new Button("Modifier");
    Button btnAjouter = new Button("Ajouter");
    Button btnNouveau = new Button("Nouveau");
    Button btnSupprimer = new Button("Supprimer");

    private void loadCombo() {
//        comboCategorie.getItems().addAll("espece","cheque","online","traites");
        comboCategorie.getSelectionModel().selectFirst();
    }

    public int myIndexOf(List<Paiement> al, Paiement c){
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
        txtVente.clear();
        txtTotal.clear();
        txtDate.clear();
    }

    private void initTable(ObservableList<Paiement> data) {
        table.setEditable(true);
        table.setItems(data);

        TableColumn<Paiement, Long> colId = new TableColumn<>("ID");
        colId.setMinWidth(40);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        table.getColumns().add(colId);

        TableColumn<Paiement, Long> colVente = new TableColumn<>("Vente");
        colVente.setMinWidth(70);
        colVente.setCellValueFactory(new PropertyValueFactory<>("vente"));
        table.getColumns().add(colVente);

        TableColumn<Paiement, Float> colTotal = new TableColumn<>("Montant");
        colTotal.setMinWidth(80);
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        table.getColumns().add(colTotal);

        TableColumn<Paiement, String> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(80);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.getColumns().add(colDate);

        TableColumn<Paiement, String> colCategorie = new TableColumn<>("Reglement");
        colCategorie.setMinWidth(98);
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("reglement"));
        table.getColumns().add(colCategorie);
    }

    public void initPanes(){

        ObservableList<Paiement> data = FXCollections.observableArrayList(list);
        FilteredList<Paiement> items = new FilteredList<>(data);
        items.setPredicate(null);
        initTable(data);

        dp.setValue(LocalDate.now());
        txtId.setDisable(true);

        txtVente.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtVente.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtTotal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtTotal.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        System.out.println(comboCategorie.getValue());

        btnAjouter.setOnAction(e -> {
            if(txtVente.getText().length()!=0 && txtTotal.getText().length()!=0) {
//            if(true) {
                Paiement p = new Paiement(1,vm.find(Long.parseLong(txtVente.getText())),Float.parseFloat(txtTotal.getText()), dp.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), comboCategorie.getValue().toString());
//                long id=0;
                switch (comboCategorie.getValue().toString()){
                    case "online":
                        Dialog<Pair<String, String>> dialog = new Dialog<>();
                        dialog.setTitle("Cmi paiement");

                        // Set the button types.
                        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                        GridPane gridPane = new GridPane();
                        gridPane.setHgap(10);
                        gridPane.setVgap(10);
                        gridPane.setPadding(new Insets(20, 150, 10, 10));

                        TextField proprietaire = new TextField();
                        proprietaire.setPromptText("RIB");
                        TextField code = new TextField();
                        code.setPromptText("Code");

                        proprietaire.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                proprietaire.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        });

                        code.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                code.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        });

                        gridPane.addRow(0,new Label("RIB : "),proprietaire);
                        gridPane.addRow(1,new Label("Code : "),code);

                        dialog.getDialogPane().setContent(gridPane);

                        // Request focus on the username field by default.
                        // Platform.runLater(() -> from.requestFocus());

                        // Convert the result to a username-password-pair when the login button is clicked.
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == loginButtonType) {
                                return new Pair<>(proprietaire.getText(), code.getText());
                            }
                            return null;
                        });

                        Optional<Pair<String, String>> result = dialog.showAndWait();

                        result.ifPresent(pair -> {
                            try {
                                Compte c = new Compte((new ClientDAOImpl().find(vm.find(Integer.parseInt(txtVente.getText())).getClient().getId())).getNom(),Float.parseFloat(txtTotal.getText()), code.getText(),proprietaire.getText());
                                Socket s = new Socket("localhost", 8818);
                                OutputStream os = s.getOutputStream();
                                ObjectOutputStream dos = new ObjectOutputStream(os);
                                dos.writeObject(c);
                                InputStream is = s.getInputStream();
                                DataInputStream dis = new DataInputStream(is);
                                String res = String.valueOf(dis.readByte());
                                System.out.println(res);
                                if(res.equals("1")) {
                                    long id = pm.create(p);
                                    if (id > 0) {
                                        p.setId(id);
                                        data.add(p);
                                        resetTextFields();
                                    }
                                }

                                s.close();
                            }
                            catch (Exception ex){
                                ex.printStackTrace();
                            }
//                            System.out.println("Name=" + pair.getKey() + ", Code=" + pair.getValue());
                        });

                        break;
                    default :
                        long id = pm.create(p);

                        if(id > 0) {
                            p.setId(id);
                            data.add(p);
                            resetTextFields();
                        }
                        break;
                }
//                long id = pm.create(p);
//
//                if(id > 0) {
//                    p.setId(id);
//                    data.add(p);
//                    resetTextFields();
//                }
            }
        });

        btnNouveau.setOnAction(e -> {
            resetTextFields();
            table.getSelectionModel().clearSelection();
        });

        btnModifier.setOnAction(e -> {
            if(txtId.getText().length()!=0) {
                Paiement p =pm.find(Integer.parseInt(txtId.getText()));
                if(p!=null) {
                    p.setVente(vm.find(Long.parseLong(txtVente.getText())));
                    p.setTotal(Float.parseFloat(txtTotal.getText()));
                    p.setReglement(dp.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    p.setReglement(comboCategorie.getValue().toString());
                    data.set(myIndexOf(data,p),p);
                    pm.update(p);
                    table.getSelectionModel().clearSelection();
                    resetTextFields();
                }
            }
        });

        btnSupprimer.setOnAction(e -> {
            if(txtId.getText().length()!=0) {
                Paiement p =pm.find(Integer.parseInt(txtId.getText()));
                if(p!=null) {
                    data.remove(myIndexOf(data,p), myIndexOf(data,p)+1);
                    pm.delete(p);
                    table.getSelectionModel().clearSelection();
                    resetTextFields();
                }
            }
        });

        txtSearch.setOnKeyReleased(event -> {
            Predicate<Paiement> id = i -> String.valueOf(i.getId()).contains(txtSearch.getText());
            Predicate<Paiement> total = i -> String.valueOf(i.getTotal()).contains(txtSearch.getText());
            Predicate<Paiement> date = i -> String.valueOf(i.getDate()).contains(txtSearch.getText());
            Predicate<Paiement> predicate = total.or(date.or(id));

            table.setItems(items);
            items.setPredicate(predicate);
//				table.getItems()
        });

        navBar.getChildren().add(labelNav);

        sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);

        loadCombo();

        grid.addRow(0, labelId,txtId);
        grid.addRow(1, labelVente,txtVente);
        grid.addRow(2, labelTotal, txtTotal);
        grid.addRow(3, labelDate, dp);
        grid.addRow(4, labelCategorie, comboCategorie);

        sideBarRight.getChildren().addAll(txtSearch,table);
        grid.add(sideBarLeft,0,5,2,1);

        navBar.getStyleClass().add("navBar");
        navBar.getStyleClass().add("labelNav");
        sideBarLeft.getStyleClass().add("sideBarLeft");
        sideBarRight.getStyleClass().add("sideBarRight");
        grid.getStyleClass().add("sideBarLeft");


        container.setTop(navBar);
//        container.setLeft(sideBarLeft);
        container.setRight(sideBarRight);
        container.setCenter(grid);

        table.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if(table.getSelectionModel().getSelectedItem() != null)
            {
                System.out.println(comboCategorie.getValue());
                resetTextFields();
                Paiement p = table.getSelectionModel().getSelectedItem();
                txtId.setText(String.valueOf(p.getId()));
                txtVente.setText(p.getVente().getId()+"");
                txtTotal.setText(String.valueOf(p.getTotal()));
                dp.setValue(LocalDate.parse(p.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//                System.out.println(dp.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//                DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                dp.setValue(LocalDate.parse(p.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                txtDate.setText(String.valueOf(p.getDate()));
                comboCategorie.getSelectionModel().select(p.getReglement());
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
