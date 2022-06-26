package vcpkgUtils;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

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
                logTextArea.appendText(lineFromOutput);
                logTextArea.appendText("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePackage(VcpkgPackage vcpkgPackage, TextArea logTextArea) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        try {
            Process installPackageProcess = new ProcessBuilder().command(pathToVcpkg, "remove", vcpkgPackage.getPkgName(), "--recurse").start();
            Reader readerFromVcpkg = new InputStreamReader(installPackageProcess.getInputStream());
            BufferedReader bufferedReaderFromVcpkg = new BufferedReader(readerFromVcpkg);
            String lineFromOutput;
            while ((lineFromOutput = bufferedReaderFromVcpkg.readLine()) != null && !lineFromOutput.isEmpty()) {
                logTextArea.appendText(lineFromOutput);
                logTextArea.appendText("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<VcpkgPackage> searchInInstalledPackages(String searchLine) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        ArrayList<VcpkgPackage> listOfPackages = new ArrayList<>();
        try {
            Process getInstalledPackagesProcess = new ProcessBuilder().command(pathToVcpkg, "list", searchLine).start();
            ArrayList<String> listOfLinesFromOutput = processWorker.wrapProcessOutput(getInstalledPackagesProcess);
            listOfPackages = packageWorker.listToSetOfPackages(listOfLinesFromOutput, 65);
        } catch (IOException e) {
            //TODO: Error dialog
        }
        return listOfPackages;
    }

    public ArrayList<VcpkgPackage> searchInAllPackages(String searchLine) {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        ArrayList<VcpkgPackage> listOfPackages = new ArrayList<>();
        try {
            Process getAllPackagesProcess = new ProcessBuilder().command(pathToVcpkg, "search", searchLine).start();
            ArrayList<String> listOfLinesFromOutput = processWorker.wrapProcessOutput(getAllPackagesProcess);
            listOfLinesFromOutput.remove(listOfLinesFromOutput.size()-1);
            listOfPackages = packageWorker.listToSetOfPackages(listOfLinesFromOutput, 40);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            //TODO: Error dialog
        }
        return listOfPackages;
    }
}
