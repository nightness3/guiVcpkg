package vcpkgUtils;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class VcpkgWorker {
    private final ProcessWorker processWorker;
    private final PackageWorker packageWorker;

    public VcpkgWorker() {
        this.processWorker = new ProcessWorker();
        this.packageWorker = new PackageWorker();
    }

    public void installPackage(VcpkgPackage vcpkgPackage, TextArea logTextArea) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        try {
            Process installPackageProcess = new ProcessBuilder().command(pathToVcpkg, "install", vcpkgPackage.getPkgName()).start();
            Reader readerFromVcpkg = new InputStreamReader(installPackageProcess.getInputStream());
            BufferedReader bufferedReaderFromVcpkg = new BufferedReader(readerFromVcpkg);
            String lineFromOutput;
            while ((lineFromOutput = bufferedReaderFromVcpkg.readLine()) != null && !lineFromOutput.isEmpty()) {
                String finalLineFromOutput = lineFromOutput;
                Platform.runLater(() -> {
                    logTextArea.appendText(finalLineFromOutput);
                    logTextArea.appendText("\n");
                });
            }
            if (installPackageProcess.waitFor() != 0) {
                return;
            }
        } catch (IOException | InterruptedException ignored) {

        }
    }

    public void removePackage(VcpkgPackage vcpkgPackage, TextArea logTextArea) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        try {
            Process removePackageProcess = new ProcessBuilder().command(pathToVcpkg, "remove", vcpkgPackage.getPkgName(), "--recurse").start();
            Reader readerFromVcpkg = new InputStreamReader(removePackageProcess.getInputStream());
            BufferedReader bufferedReaderFromVcpkg = new BufferedReader(readerFromVcpkg);
            String lineFromOutput;
            while ((lineFromOutput = bufferedReaderFromVcpkg.readLine()) != null && !lineFromOutput.isEmpty()) {
                String finalLineFromOutput = lineFromOutput;
                Platform.runLater(() -> {
                    logTextArea.appendText(finalLineFromOutput);
                    logTextArea.appendText("\n");
                });
            }
            if (removePackageProcess.waitFor() != 0) {
                return;
            }
        } catch (IOException | InterruptedException ignored) {

        }
    }

    public ArrayList<VcpkgPackage> searchInInstalledPackages(String searchLine, Label statusLabel) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        ArrayList<VcpkgPackage> listOfPackages = new ArrayList<>();
        try {
            Process getInstalledPackagesProcess = new ProcessBuilder().command(pathToVcpkg, "list", searchLine).start();
            ArrayList<String> listOfLinesFromOutput = processWorker.wrapProcessOutput(getInstalledPackagesProcess, statusLabel);
            listOfPackages = packageWorker.listToSetOfPackages(listOfLinesFromOutput, 65);
            if (getInstalledPackagesProcess.waitFor() != 0) {
                return listOfPackages;
            }
        } catch (IOException | InterruptedException ignored) {

        }
        return listOfPackages;
    }

    public ArrayList<VcpkgPackage> searchInAllPackages(String searchLine, Label statusLabel) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        ArrayList<VcpkgPackage> listOfPackages = new ArrayList<>();
        try {
            Process getAllPackagesProcess = new ProcessBuilder().command(pathToVcpkg, "search", searchLine).start();
            ArrayList<String> listOfLinesFromOutput = processWorker.wrapProcessOutput(getAllPackagesProcess, statusLabel);
            if (listOfLinesFromOutput.size() > 0) {
                listOfLinesFromOutput.remove(listOfLinesFromOutput.size() - 1);
                listOfPackages = packageWorker.listToSetOfPackages(listOfLinesFromOutput, 40);
            }
            if (getAllPackagesProcess.waitFor() != 0) {
                return listOfPackages;
            }
        } catch (IOException | InterruptedException ignored) {

        }
        return listOfPackages;
    }
}