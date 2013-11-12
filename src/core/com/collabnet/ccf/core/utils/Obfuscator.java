package com.collabnet.ccf.core.utils;

import org.apache.commons.codec.binary.Base64;

public class Obfuscator {

    private static final String OBFUSCATED_PASSWORD_PREFIX = "OBF:";

    public static String deObfuscatePassword(String password) {
        if (password == null) {
            return "";
        }
        if (!password.startsWith(OBFUSCATED_PASSWORD_PREFIX)) {
            return password;
        }
        return deObfuscateString(password.substring(OBFUSCATED_PASSWORD_PREFIX
                .length()));
    }

    public static String deObfuscateString(String string) {
        byte[] bytes;
        bytes = Base64.decodeBase64(string.getBytes());
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = cyclicShiftBitsLeft(bytes[i], 4);
        }
        return new String(bytes);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
            System.exit(1);
        }
        System.out.println(obfuscatePassword(args[0]));
    }

    public static String obfuscatePassword(String password) {
        return OBFUSCATED_PASSWORD_PREFIX
                + obfuscateString((password == null) ? "" : password);
    }

    public static String obfuscateString(String string) {
        byte[] bytes = string.getBytes();
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = cyclicShiftBitsRight(bytes[i], 4);
        }

        return new String(Base64.encodeBase64(bytes));
    }

    private static byte cyclicShiftBitsLeft(int b, int i) {
        if (b < 0) {
            b += 256;
        }
        int j = ((b << i) | ((b >>> (8 - i)) % 256));
        if (j > 127) {
            j -= 256;
        }
        return (byte) j;
    }

    private static byte cyclicShiftBitsRight(int b, int i) {
        if (b < 0) {
            b += 256;
        }
        int j = (((b >>> i) % 256) | (b << (8 - i)));
        if (j > 127) {
            j -= 256;
        }
        return (byte) j;
    }

    private static void usage() {
        System.err.println("usage: java " + Obfuscator.class.getName()
                + " <passwordToObfuscate>");
    }

}
