package splusteam.center.ota.ota_center;

import android.os.Build;

public class Utils {

//    public static String getVersion() {

//        return SystemProperties.get("ro.otamanager.version");
//    }

    public static String getVersion() {
        return SystemProperties.get("ro.otamanager.version");
    }
    public static String getXposedSupport() {
        return SystemProperties.get("ro.otamanager.xposed");
    }
    public static String getOfficialState() {
        return SystemProperties.get("ro.otamanager.status");
    }
    public static String getBetaSupport() {
        return SystemProperties.get("ro.otamanager.beta");
    }
    public static long getInstalledBuildDate() {
        return Build.TIME / 1000;
    }
    public static String getChipset() {
        return SystemProperties.get("ro.product.board");
    }
    public static String getDensity() {
        return SystemProperties.get("ro.sf.lcd_density");
    }

}
