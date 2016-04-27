package com.spider.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class IpUtils {
    /**
     * 获取全路径IP
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded;
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                ip = realIp + "/" + forwarded.replaceAll(", " + realIp, "");
            }
        }
        if (ip.length() > 90) {
            //System.err.println("ip:" + ip);
            ip = ip.substring(0, 90);
        }
        return ip;
    }
    private static String localIp;

    static {
        localIp = getLocalAddress();
    }

    public static String getIp() {
        return localIp;
    }

    /**
     * 获取本地IP
     * @return
     */
    static String getLocalAddress() {
        String result = null;
        Enumeration<NetworkInterface> enu = null;
        try {
            enu = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return "localhost";
        }
        while (enu.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) (enu.nextElement());
            Enumeration<InetAddress> ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
                InetAddress i = (InetAddress) (ias.nextElement());
                String addr = i.getHostAddress();
                if (addr.startsWith("192.")) {
                    return addr;
                }
            }
        }
        return result;
    }
    
    /**
     * 获取公网IP或代理IP
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.indexOf(", ") > -1) {
            ip = ip.substring(ip.lastIndexOf(", ") + 2);
        }
        return ip;
    }
}
