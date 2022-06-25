package vcpkgUtils;

import java.util.HashSet;

public class VcpkgWorker {
    private HashSet<VcpkgPackage> setOfInstalledPackages;
    private HashSet<VcpkgPackage> setOfAllPackages;

    public VcpkgWorker() {
        this.setOfInstalledPackages = new HashSet<>();
        this.setOfAllPackages = new HashSet<>();
    }

    public void installPackage() {

    }

    public void removePackage() {

    }

    public HashSet<VcpkgPackage> getSetOfInstalledPackages() {
        return new HashSet<>();
    }

    public HashSet<VcpkgPackage> getSetOfAllPackages() {
        return new HashSet<>();
    }

    public void refreshSetsOfPackages() {

    }
}
