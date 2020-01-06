package login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginIHM extends Application {

    BorderPane root = new BorderPane();
    Scene scene = null;
    VBox sideBar = new VBox();
    BorderPane container = new BorderPane();

    public void initContainer(){

    }

    public static void main(String[] args) {

    }

    @Override
    public void start(Stage window) throws Exception {
        scene = new Scene(container);
        window.setTitle("Gestion Magasin");
        window.setHeight(600);
        window.setWidth(600);
//        initContainer();
        scene.getStylesheets().add("MyCss.css");
        window.setScene(scene);
        window.show();
    }
}
