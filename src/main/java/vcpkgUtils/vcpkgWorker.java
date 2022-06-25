package vcpkgUtils;

import java.util.HashSet;

public class vcpkgWorker {

    private HashSet<vcpkgPackage> setOfInstalledPackages;
    private HashSet<vcpkgPackage> setOfAllPackages;
    private String path;

    public vcpkgWorker() {
        this.setOfInstalledPackages = new HashSet<>();
        this.setOfAllPackages = new HashSet<>();
    }

    public void installPackage() {

    }

    public void removePackage() {

    }

    public HashSet<vcpkgPackage> getSetOfInstalledPackages() {
        return new HashSet<>();
    }

    public HashSet<vcpkgPackage> getSetOfAllPackages() {
        return new HashSet<>();
    }

    public void refreshSetsOfPackages() {

    }

}
