package vcpkgUtils;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.ArrayList;

public class VcpkgWorker {
    private final ProcessWorker processWorker;
    private final PackageWorker packageWorker;

    public VcpkgWorker() {
        this.processWorker = new ProcessWorker();
        this.packageWorker = new PackageWorker();
    }

    public void installPackage(VcpkgPackage vcpkgPackage, TextArea logTextArea, Label statusLabel) {
        //TODO: make install function
    }

    public void removePackage(VcpkgPackage vcpkgPackage, TextArea logTextArea, Label statusLabel) {
        //TODO: make remove function
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
        } catch (IOException e) {
            //TODO: Error dialog
        }
        return listOfPackages;
    }
}
