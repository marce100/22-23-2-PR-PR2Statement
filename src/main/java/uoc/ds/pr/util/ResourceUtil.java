package uoc.ds.pr.util;

import static uoc.ds.pr.UniversityEvents.*;

public class ResourceUtil {

    public static byte getFlag(byte... in) {
        byte ret = 0;
        for (byte b: in) {
            ret = (byte)(ret | b);

        }
        return ret;
    }

    public static boolean hasVideoProjector(byte flag) {
        return hasFlag(flag, FLAG_VIDEO_PROJECTOR);
    }

    public static boolean hasTouchScreen(byte flag) {
        return hasFlag(flag, FLAG_TOUCH_SCREEN);
    }

    public static boolean hasAuxiliaryMic(byte flag) {
        return hasFlag(flag, FLAG_AUXILIARY_MIC);
    }

    public static boolean hasComputer(byte flag) {
        return hasFlag(flag, FLAG_COMPUTER);
    }

    public static boolean hasAllOpts(byte flag){
        return FLAG_ALL_OPTS == flag;

    }
    private static boolean hasFlag(byte flagSrc, byte flagCmp) {
        byte res = (byte)(flagSrc & flagCmp);
        return (res>0);
    }
}
