package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindowInterface.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Image image = new Image("/img/folder_icon.png");
        primaryStage.getIcons().add(image);
        primaryStage.setTitle("vcpkgGUI");
        primaryStage.show();
    }

    public void run() {
        launch();
    }
}
