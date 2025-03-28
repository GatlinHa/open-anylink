package com.hibob.anylink.common.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

import static com.hibob.anylink.common.constants.Const.SPLIT_V;

@Slf4j
public class CommonUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private CommonUtil() {

    }

    public static String conUniqueId(String account, String clientId) {
        return account + SPLIT_V + clientId;
    }

    @Getter
    public static String localIp;

    static {
        localIp = initLocalIp();
    }

    private static String initLocalIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // 排除回环接口和虚拟接口
                if (iface.isLoopback() || iface.isVirtual() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    // 检查是否是 IPv4 地址
                    if (address.getAddress().length == 4) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("getLocalIp exception, exception is {}", e.getMessage());
        }

        return null;
    }


    /**
     *  fromId 和 toId 按照字典序排序，如果fromId小则返回true
     * @param fromId
     * @param toId
     * @return
     */
    public static String[] sortId(String fromId, String toId) {
        if (fromId.compareTo(toId) < 0) {
            return new String[] {fromId, toId};
        } else {
            return new String[] {toId, fromId};
        }
    }

    /**
     *  fromId 和 toId 按照字典序排序，较小+较大
     * @param fromId
     * @param toId
     * @return
     */
    public static String combineId(String fromId, String toId) {
        if (fromId.compareTo(toId) < 0) {
            return fromId + SPLIT_V + toId;
        } else {
            return toId + SPLIT_V + fromId;
        }
    }

    /**
     * 字符串截取
     * @param str
     * @param maxLength
     * @return
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength);
    }

    public static String generateRandomStr(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

}
