package vcpkgUtils;

import java.util.prefs.Preferences;

public class VcpkgPathWorker {

    private static final Preferences pathNode = Preferences.userRoot().node("pathToVcpkg");
    private static String pathToVcpkg = pathNode.get("pathToVcpkg", "");

    public static String getPath() {
        return pathToVcpkg;
    }

    public static void setPath(String newVcpkgPath) {
        pathToVcpkg = newVcpkgPath;
        pathNode.put("pathToVcpkg", pathToVcpkg);
    }

}
