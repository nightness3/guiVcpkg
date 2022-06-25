package vcpkgUtils;

import java.util.Objects;

public class VcpkgPackage {
    private final String pkgName;
    private final String pkgVersion;
    private final String pkgDescription;

    public VcpkgPackage(String pkgName, String pkgVersion, String pkgDescription) {
        this.pkgName = pkgName;
        this.pkgVersion = pkgVersion;
        this.pkgDescription = pkgDescription;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getPkgVersion() {
        return pkgVersion;
    }

    public String getPkgDescription() {
        return pkgDescription;
    }

    @Override
    public String toString() {
        return "vcpkgPackage{" +
                "pkgName='" + pkgName + '\'' +
                ", pkgVersion='" + pkgVersion + '\'' +
                ", pkgDescription='" + pkgDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VcpkgPackage that = (VcpkgPackage) o;
        return getPkgName().equals(that.getPkgName()) && getPkgVersion().equals(that.getPkgVersion()) && Objects.equals(getPkgDescription(), that.getPkgDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPkgName(), getPkgVersion(), getPkgDescription());
    }
}
