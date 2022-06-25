package vcpkgUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class VcpkgWorker {
    private ArrayList<VcpkgPackage> listOfInstalledPackages;
    private ArrayList<VcpkgPackage> listOfAllPackages;
    private final ProcessWorker processWorker;
    private final PackageWorker packageWorker;

    public VcpkgWorker() {
        this.listOfInstalledPackages = new ArrayList<>();
        this.listOfAllPackages = new ArrayList<>();
        this.processWorker = new ProcessWorker();
        this.packageWorker = new PackageWorker();
    }

    public void installPackage() {

    }

    public void removePackage() {

    }

    public ArrayList<VcpkgPackage> getSetOfInstalledPackages() {
        return new ArrayList<>();
    }

    public ArrayList<VcpkgPackage> getSetOfAllPackages() {
        String pathToVcpkg = VcpkgPathWorker.getPath();
        ArrayList<VcpkgPackage> listOfPackages = new ArrayList<>();
        try {
            Process getAllPackagesProcess = new ProcessBuilder().command(pathToVcpkg, "search").start();
            ArrayList<String> listOfLinesFromOutput = processWorker.wrapProcessOutput(getAllPackagesProcess);
            listOfLinesFromOutput.remove(listOfLinesFromOutput.size()-1);
            listOfPackages = packageWorker.listToSetOfPackages(listOfLinesFromOutput);
            this.listOfAllPackages = listOfPackages;
        } catch (IOException e) {
            //TODO: Error dialog
        }
        return listOfPackages;
    }

    public void refreshSetsOfPackages() {

    }
}
