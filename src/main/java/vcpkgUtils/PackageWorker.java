package vcpkgUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageWorker {
    public ArrayList<VcpkgPackage> listToSetOfPackages(ArrayList lines) {
        ArrayList<VcpkgPackage> setOfPackages = new ArrayList<>();
        lines.stream().forEach(x -> setOfPackages.add(lineToPackage(x.toString())));
        return setOfPackages;
    }

    private VcpkgPackage lineToPackage(String line) {
        String[] splittedLine = line.split("[\\s]{1,}", 2);
        String pkgName = splittedLine[0];
        String pkgVersion = "";
        String pkgDescription = "";
        if (splittedLine.length > 1) {
            Pattern pattern = Pattern.compile(Pattern.quote(splittedLine[1]));
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            int start = matcher.start();
            if (start > 40) {
                pkgDescription = splittedLine[1];
            } else {
                splittedLine = splittedLine[1].split("[\\s]{1,}", 2);
                pkgVersion = splittedLine[0];
                if (splittedLine.length > 1) {
                    pkgDescription = splittedLine[1];
                }
            }
        }
        VcpkgPackage vcpkgPackage = new VcpkgPackage(pkgName, pkgVersion, pkgDescription);
        return vcpkgPackage;
    }
}
