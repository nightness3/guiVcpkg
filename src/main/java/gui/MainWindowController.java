package gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import vcpkgUtils.VcpkgPackage;
import vcpkgUtils.VcpkgPathWorker;
import vcpkgUtils.VcpkgWorker;

public class MainWindowController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<VcpkgPackage, String> allPackagesNameColumn;

    @FXML
    private Tab allPackagesTab;

    @FXML
    private TableView<VcpkgPackage> allPackagesTableView;

    @FXML
    private TableColumn<VcpkgPackage, String> allPackagesVersionColumn;

    @FXML
    private TabPane choosePackagesTab;

    @FXML
    private Button chooseVcpkgButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button installButton;

    @FXML
    private TableColumn<VcpkgPackage, String> installedPackagesNameColumn;

    @FXML
    private Tab installedPackagesTab;

    @FXML
    private TableView<VcpkgPackage> installedPackagesTableView;

    @FXML
    private TableColumn<VcpkgPackage, String> installedPackagesVersionColumn;

    @FXML
    private TextArea logTextArea;

    @FXML
    private AnchorPane mainWindowPane;

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
    private VcpkgWorker vcpkgWorker = new VcpkgWorker();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!VcpkgPathWorker.getPath().equals("")) {
            if ((new File(VcpkgPathWorker.getPath()).exists())) {
                pathInputTextField.setText(VcpkgPathWorker.getPath());
            }
        }

        //TODO: refactor into multithreading
        initializeTables();

        chooseVcpkgButton.setOnAction(event -> {
            fileChooser.setTitle("Select vcpkg");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select vcpkg.exe", "*.exe");
            fileChooser.getExtensionFilters().add(filter);
            if (!VcpkgPathWorker.getPath().equals("")) {
                if ((new File(VcpkgPathWorker.getPath())).exists()) {
                    fileChooser.setInitialDirectory(new File(VcpkgPathWorker.getPath()));
                }
            }
            File file = fileChooser.showOpenDialog(mainWindowPane.getScene().getWindow());
            String vcpkgPath = file.getPath();
            VcpkgPathWorker.setPath(vcpkgPath);
            pathInputTextField.setText(vcpkgPath);
        });

        //TODO: refactor into multithreading
        searchButton.setOnAction(event -> {
            String searchLine = searchInputTextField.getText();
            ArrayList<VcpkgPackage> searchPackages;
            if (choosePackagesTab.getSelectionModel().getSelectedIndex() == 0) {
                searchPackages = vcpkgWorker.searchInInstalledPackages(searchLine);
                setListInTable(installedPackagesTableView, installedPackagesNameColumn, installedPackagesVersionColumn, searchPackages);
            } else {
                searchPackages = vcpkgWorker.searchInAllPackages(searchLine);
                setListInTable(allPackagesTableView, allPackagesNameColumn, allPackagesVersionColumn, searchPackages);
            }
        });

        //TODO: refactor into multithreading
        refreshButton.setOnAction(event -> {
            ArrayList<VcpkgPackage> allPackagesInitialize = vcpkgWorker.searchInAllPackages("");
            setListInTable(allPackagesTableView, allPackagesNameColumn, allPackagesVersionColumn, allPackagesInitialize);
            ArrayList<VcpkgPackage> installedPackagesInitialize = vcpkgWorker.searchInInstalledPackages("");
            setListInTable(installedPackagesTableView, installedPackagesNameColumn, installedPackagesVersionColumn, installedPackagesInitialize);
        });

        installButton.setOnAction(event -> {
            if (choosePackagesTab.getSelectionModel().getSelectedIndex() == 1) {
                InstallTask installTask = new InstallTask(allPackagesTableView.getSelectionModel().getSelectedItem(), logTextArea);
                installTask.setOnRunning(runEvent -> {
                    statusLabel.setTextFill(Color.YELLOW);
                    statusLabel.setText("INSTALLING");
                });
                installTask.setOnSucceeded(successEvent -> {
                    statusLabel.setTextFill(Color.GREEN);
                    statusLabel.setText("INSTALLED");
                });
                installTask.setOnCancelled(cancelEvent -> {
                    statusLabel.setTextFill(Color.BLUE);
                    statusLabel.setText("CANCELED");
                });
                installTask.setOnFailed(failEvent -> {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("FAILED");
                });
                installTask.setOnScheduled(scheduleEvent -> statusLabel.setText("SCHEDULED"));
                ExecutorService executorService
                        = Executors.newFixedThreadPool(1);
                executorService.execute(installTask);
                executorService.shutdown();
            } else {
                //TODO: Dialog choose another tab
            }
        });

        removeButton.setOnAction(event -> {
            if (choosePackagesTab.getSelectionModel().getSelectedIndex() == 0) {
                RemoveTask removeTask = new RemoveTask(installedPackagesTableView.getSelectionModel().getSelectedItem(), logTextArea);
                removeTask.setOnRunning(runEvent -> {
                    statusLabel.setTextFill(Color.YELLOW);
                    statusLabel.setText("REMOVING");
                });
                removeTask.setOnSucceeded(successEvent -> {
                    statusLabel.setTextFill(Color.GREEN);
                    statusLabel.setText("REMOVED");
                });
                removeTask.setOnCancelled(cancelEvent -> {
                    statusLabel.setTextFill(Color.BLUE);
                    statusLabel.setText("CANCELED");
                });
                removeTask.setOnFailed(failEvent -> {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("FAILED");
                });
                removeTask.setOnScheduled(scheduleEvent -> statusLabel.setText("SCHEDULED"));
                ExecutorService executorService
                        = Executors.newFixedThreadPool(1);
                executorService.execute(removeTask);
                executorService.shutdown();
            } else {
                //TODO: Dialog choose another tab
            }
        });
    }

    private void initializeTables() {
        allPackagesTableView.setOnMouseClicked(event -> {
            VcpkgPackage vcpkgPackage = allPackagesTableView.getSelectionModel().getSelectedItem();
            nameLabel.setText(vcpkgPackage.getPkgName());
            versionLabel.setText(vcpkgPackage.getPkgVersion());
            descriptionLabel.setText(vcpkgPackage.getPkgDescription());
        });
        installedPackagesTableView.setOnMouseClicked(event -> {
            VcpkgPackage vcpkgPackage = installedPackagesTableView.getSelectionModel().getSelectedItem();
            nameLabel.setText(vcpkgPackage.getPkgName());
            versionLabel.setText(vcpkgPackage.getPkgVersion());
            descriptionLabel.setText(vcpkgPackage.getPkgDescription());
        });

        ArrayList<VcpkgPackage> allPackagesInitialize = vcpkgWorker.searchInAllPackages("");
        setListInTable(allPackagesTableView, allPackagesNameColumn, allPackagesVersionColumn, allPackagesInitialize);
        ArrayList<VcpkgPackage> installedPackagesInitialize = vcpkgWorker.searchInInstalledPackages("");
        setListInTable(installedPackagesTableView, installedPackagesNameColumn, installedPackagesVersionColumn, installedPackagesInitialize);
    }

    private void setListInTable(TableView<VcpkgPackage> table, TableColumn<VcpkgPackage, String> firstTableColumn,
                                TableColumn<VcpkgPackage, String> secondTableColumn, ArrayList<VcpkgPackage> listOfPackages) {
        firstTableColumn.setCellValueFactory(new PropertyValueFactory<>("pkgName"));
        secondTableColumn.setCellValueFactory(new PropertyValueFactory<>("pkgVersion"));
        ObservableList<VcpkgPackage> observableListOfPackages = FXCollections.observableList(listOfPackages);
        table.setItems(observableListOfPackages);
    }

    public class InstallTask extends Task<Void> {
        private final VcpkgPackage vcpkgPackage;
        private final TextArea logTextArea;

        public InstallTask (VcpkgPackage vcpkgPackage, TextArea logTextArea) {
            this.vcpkgPackage = vcpkgPackage;
            this.logTextArea = logTextArea;
        }

        @Override
        protected Void call() throws Exception {
            vcpkgWorker.installPackage(vcpkgPackage, logTextArea);
            return null;
        }
    }

    public class RemoveTask extends Task<Void> {
        private final VcpkgPackage vcpkgPackage;
        private final TextArea logTextArea;

        public RemoveTask (VcpkgPackage vcpkgPackage, TextArea logTextArea) {
            this.vcpkgPackage = vcpkgPackage;
            this.logTextArea = logTextArea;
        }

        @Override
        protected Void call() throws Exception {
            vcpkgWorker.removePackage(vcpkgPackage, logTextArea);
            return null;
        }
    }
}
