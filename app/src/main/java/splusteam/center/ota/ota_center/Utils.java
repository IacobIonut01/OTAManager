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

    public static String getSDK() {
        return SystemProperties.get("ro.build.version.sdk");
    }

    public static String getOfficialState() {
        return SystemProperties.get("ro.otamanager.status");
    }


    public static long getInstalledBuildDate() {
        return Build.TIME / 1000;
    }

}
