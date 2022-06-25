package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import vcpkgUtils.VcpkgPathWorker;

public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab allPackagesTab;

    @FXML
    private AnchorPane mainWindowPane;

    @FXML
    private TabPane choosePackagesTab;

    @FXML
    private Button chooseVcpkgButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button installButton;

    @FXML
    private Tab installedPackagesTab;

    @FXML
    private TextArea logTextArea;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField pathInputTextField;

    @FXML
    private Button refreshButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchInputTextField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label versionLabel;

    private FileChooser fileChooser = new FileChooser();

    @FXML
    void initialize() {
        if(!VcpkgPathWorker.getPath().equals("")) {
            pathInputTextField.setText(VcpkgPathWorker.getPath());
        }

        chooseVcpkgButton.setOnAction(event -> {
            fileChooser.setTitle("Select vcpkg");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select vcpkg.exe", "*.exe");
            fileChooser.getExtensionFilters().add(filter);
            if (!VcpkgPathWorker.getPath().equals("")) {
                fileChooser.setInitialDirectory(new File(VcpkgPathWorker.getPath()));
            }
            File file = fileChooser.showOpenDialog(mainWindowPane.getScene().getWindow());
            String vcpkgPath = file.getPath();
            VcpkgPathWorker.setPath(vcpkgPath);
            pathInputTextField.setText(vcpkgPath);
        });
    }

}
