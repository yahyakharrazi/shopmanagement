package login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginIHM extends Application {

    BorderPane root = new BorderPane();
    Scene scene = null;
    HBox sideBar = new HBox();
    GridPane gridOfLogin = new GridPane();
    BorderPane container = new BorderPane();

    TextField txtUsername = new TextField();
    PasswordField txtPassword = new PasswordField();
    Label labelUsername = new Label("Username : ");
    Label labelPassword = new Label("Password : ");
    Button btnLogin = new Button("Signin");
    Button btnSignUp = new Button("Signup");

    public void initContainer(){
        gridOfLogin.addRow(0,labelUsername,txtUsername);
        gridOfLogin.addRow(1,labelPassword,txtPassword);
        gridOfLogin.addRow(2,btnLogin,btnSignUp);

        sideBar.setPrefWidth(200);

        gridOfLogin.getStyleClass().add("gridOfLogin");
        txtUsername.getStyleClass().add("materialInput");
        txtPassword.getStyleClass().add("materialInput");
        container.setCenter(gridOfLogin);
        container.setLeft(sideBar);

        btnLogin.setOnAction(event -> {

        });

        btnSignUp.setOnAction(event -> {

        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        scene = new Scene(container);
        window.setTitle("Gestion Magasin");
        window.setHeight(400);
        window.setWidth(600);
        initContainer();
        scene.getStylesheets().add("MyCss.css");
        window.setScene(scene);
        window.show();
    }
}
