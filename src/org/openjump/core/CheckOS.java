package org.openjump.core;

public class CheckOS {

    /**
     * taken from http://developer.apple.com/technotes/tn2002/tn2110.html
     *
     * @return returns true if the Operating System of the computer is a Mac-OS
     * X
     */
    public static boolean isMacOsx() {
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean MAC_OS_X = lcOSName.startsWith("mac os x");
        return MAC_OS_X;
    }
}
