package com.lanscan.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class NetworkUtils {
    public static String getLocalIP() {
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
                    if (!addr.isLoopbackAddress() && addr.getAddress().length == 4) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {}
        return "127.0.0.1";
    }

    public static String getSubnet(String ip) {
        String[] parts = ip.split("\\.");
        return parts[0] + "." + parts[1] + "." + parts[2] + ".0/24";
    }
}
