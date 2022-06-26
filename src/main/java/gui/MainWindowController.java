package gui;

import java.io.File;
import java.net.URL;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pathInputTextField.setEditable(false);
        logTextArea.setEditable(false);

        if(!VcpkgPathWorker.getPath().equals("")) {
            if ((new File(VcpkgPathWorker.getPath()).exists())) {
                pathInputTextField.setText(VcpkgPathWorker.getPath());
                InitializeTask initializeTask = new InitializeTask();
                executorService.submit(initializeTask);
            }
        }

        chooseVcpkgButton.setOnAction(event -> {
            fileChooser.setTitle("Select vcpkg");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select vcpkg.exe", "*.exe");
            fileChooser.getExtensionFilters().add(filter);
            if (!VcpkgPathWorker.getPath().equals("")) {
                File executableFile = new File(VcpkgPathWorker.getPath());
                File folderWithExecutableFile = new File(executableFile.getParent());
                if (folderWithExecutableFile.exists()) {
                    fileChooser.setInitialDirectory(folderWithExecutableFile);
                }
            }
            File file = fileChooser.showOpenDialog(mainWindowPane.getScene().getWindow());
            if (file != null) {
                String vcpkgPath = file.getPath();
                VcpkgPathWorker.setPath(vcpkgPath);
                pathInputTextField.setText(vcpkgPath);
            }
        });

        searchButton.setOnAction(event -> {
            if (VcpkgPathWorker.getPath().equals("") || !(new File(VcpkgPathWorker.getPath()).exists())) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("You must specify the path to vcpkg");
            } else {
                SearchTask searchTask = new SearchTask();
                executorService.submit(searchTask);
            }
        });

        refreshButton.setOnAction(event -> {
            if (VcpkgPathWorker.getPath().equals("") || !(new File(VcpkgPathWorker.getPath()).exists())) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("You must specify the path to vcpkg");
            } else {
                RefreshTask refreshTask = new RefreshTask();
                executorService.submit(refreshTask);
            }
        });

        installButton.setOnAction(event -> {
            if (VcpkgPathWorker.getPath().equals("") || !(new File(VcpkgPathWorker.getPath()).exists())) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("You must specify the path to vcpkg");
            } else {
                if (choosePackagesTab.getSelectionModel().getSelectedIndex() == 1) {
                    InstallTask installTask = new InstallTask(allPackagesTableView.getSelectionModel().getSelectedItem(), logTextArea);
                    installTask.setOnRunning(runEvent -> {
                        statusLabel.setTextFill(Color.YELLOW);
                        statusLabel.setText("INSTALLING");
                    });
                    installTask.setOnSucceeded(successEvent -> {
                        statusLabel.setTextFill(Color.GREEN);
                        statusLabel.setText("INSTALLED");
                        switchInstallAndRemoveButtons();
                    });
                    installTask.setOnCancelled(cancelEvent -> {
                        statusLabel.setTextFill(Color.BLUE);
                        statusLabel.setText("CANCELED");
                        switchInstallAndRemoveButtons();
                    });
                    installTask.setOnFailed(failEvent -> {
                        statusLabel.setTextFill(Color.RED);
                        statusLabel.setText("FAILED");
                        switchInstallAndRemoveButtons();
                    });
                    installTask.setOnScheduled(scheduleEvent -> statusLabel.setText("SCHEDULED"));
                    executorService.submit(installTask);
                } else {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("You should select the All tab");
                }
            }
        });

        removeButton.setOnAction(event -> {
            if (VcpkgPathWorker.getPath().equals("") || !(new File(VcpkgPathWorker.getPath()).exists())) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("You must specify the path to vcpkg");
            } else {
                if (choosePackagesTab.getSelectionModel().getSelectedIndex() == 0) {
                    RemoveTask removeTask = new RemoveTask(installedPackagesTableView.getSelectionModel().getSelectedItem(), logTextArea);
                    removeTask.setOnRunning(runEvent -> {
                        statusLabel.setTextFill(Color.YELLOW);
                        statusLabel.setText("REMOVING");
                    });
                    removeTask.setOnSucceeded(successEvent -> {
                        statusLabel.setTextFill(Color.GREEN);
                        statusLabel.setText("REMOVED");
                        switchInstallAndRemoveButtons();
                    });
                    removeTask.setOnCancelled(cancelEvent -> {
                        statusLabel.setTextFill(Color.BLUE);
                        statusLabel.setText("CANCELED");
                        switchInstallAndRemoveButtons();
                    });
                    removeTask.setOnFailed(failEvent -> {
                        statusLabel.setTextFill(Color.RED);
                        statusLabel.setText("FAILED");
                        switchInstallAndRemoveButtons();
                    });
                    removeTask.setOnScheduled(scheduleEvent -> statusLabel.setText("SCHEDULED"));
                    ExecutorService executorService
                            = Executors.newFixedThreadPool(1);
                    executorService.submit(removeTask);
                } else {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("You should select the Installed tab");
                }
            }
        });
    }

    private void setListInTable(TableView<VcpkgPackage> table, TableColumn<VcpkgPackage, String> firstTableColumn,
                                TableColumn<VcpkgPackage, String> secondTableColumn, ArrayList<VcpkgPackage> listOfPackages) {
        firstTableColumn.setCellValueFactory(new PropertyValueFactory<>("pkgName"));
        secondTableColumn.setCellValueFactory(new PropertyValueFactory<>("pkgVersion"));
        ObservableList<VcpkgPackage> observableListOfPackages = FXCollections.observableList(listOfPackages);
        table.setItems(observableListOfPackages);
    }

    private synchronized void switchInstallAndRemoveButtons() {
        if (installButton.isDisabled() && removeButton.isDisabled()) {
            installButton.setDisable(false);
            removeButton.setDisable(false);
        } else {
            installButton.setDisable(true);
            removeButton.setDisable(true);
        }
    }

    public class InstallTask extends Task<Void> {
        private final VcpkgPackage vcpkgPackage;
        private final TextArea logTextArea;

        public InstallTask (VcpkgPackage vcpkgPackage, TextArea logTextArea) {
            this.vcpkgPackage = vcpkgPackage;
            this.logTextArea = logTextArea;
        }

        @Override
        protected Void call() {
            switchInstallAndRemoveButtons();
            vcpkgWorker.installPackage(vcpkgPackage, logTextArea, statusLabel);
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
        protected Void call() {
            switchInstallAndRemoveButtons();
            vcpkgWorker.removePackage(vcpkgPackage, logTextArea, statusLabel);
            return null;
        }
    }

    public class InitializeTask extends Task<Void> {

        @Override
        protected Void call() {
            allPackagesTableView.setOnMouseClicked(event -> {
                try {
                    VcpkgPackage vcpkgPackage = allPackagesTableView.getSelectionModel().getSelectedItem();
                    nameLabel.setText(vcpkgPackage.getPkgName());
                    versionLabel.setText(vcpkgPackage.getPkgVersion());
                    descriptionLabel.setText(vcpkgPackage.getPkgDescription());
                } catch (NullPointerException ignored) {

                }
            });
            installedPackagesTableView.setOnMouseClicked(event -> {
                try {
                    VcpkgPackage vcpkgPackage = installedPackagesTableView.getSelectionModel().getSelectedItem();
                    nameLabel.setText(vcpkgPackage.getPkgName());
                    versionLabel.setText(vcpkgPackage.getPkgVersion());
                    descriptionLabel.setText(vcpkgPackage.getPkgDescription());
                } catch (NullPointerException ignored) {

                }
            });

            ArrayList<VcpkgPackage> allPackagesInitialize = vcpkgWorker.searchInAllPackages("", statusLabel);
            setListInTable(allPackagesTableView, allPackagesNameColumn, allPackagesVersionColumn, allPackagesInitialize);
            ArrayList<VcpkgPackage> installedPackagesInitialize = vcpkgWorker.searchInInstalledPackages("", statusLabel);
            setListInTable(installedPackagesTableView, installedPackagesNameColumn, installedPackagesVersionColumn, installedPackagesInitialize);
            return null;
        }
    }

    public class SearchTask extends Task<Void> {

        @Override
        protected Void call() {
            searchButton.setDisable(true);
            String searchLine = searchInputTextField.getText();
            ArrayList<VcpkgPackage> searchPackages;
            if (choosePackagesTab.getSelectionModel().getSelectedIndex() == 0) {
                searchPackages = vcpkgWorker.searchInInstalledPackages(searchLine, statusLabel);
                setListInTable(installedPackagesTableView, installedPackagesNameColumn, installedPackagesVersionColumn, searchPackages);
            } else {
                searchPackages = vcpkgWorker.searchInAllPackages(searchLine, statusLabel);
                setListInTable(allPackagesTableView, allPackagesNameColumn, allPackagesVersionColumn, searchPackages);
            }
            searchButton.setDisable(false);
            return null;
        }
    }

    public class RefreshTask extends Task<Void> {

        @Override
        protected Void call() {
            refreshButton.setDisable(true);
            ArrayList<VcpkgPackage> allPackagesInitialize = vcpkgWorker.searchInAllPackages("", statusLabel);
            setListInTable(allPackagesTableView, allPackagesNameColumn, allPackagesVersionColumn, allPackagesInitialize);
            ArrayList<VcpkgPackage> installedPackagesInitialize = vcpkgWorker.searchInInstalledPackages("", statusLabel);
            setListInTable(installedPackagesTableView, installedPackagesNameColumn, installedPackagesVersionColumn, installedPackagesInitialize);
            refreshButton.setDisable(false);
            return null;
        }
    }
}
