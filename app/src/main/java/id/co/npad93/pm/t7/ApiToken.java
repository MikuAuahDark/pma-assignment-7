package id.co.npad93.pm.t7;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class ApiToken {
    private static String token;

    public static void initialize(AssetManager am) throws RuntimeException {
        Class<ApiToken> current = ApiToken.class;

        try {
            // Try to load encrypted apikey.txt
            InputStream apikey = am.open("apikey.txt");
            byte[] header = new byte[16];
            if (apikey.read(header) < 16) {
                throw new RuntimeException("read less 16");
            }

            // Load decrypter
            Class<?> c = current.getClassLoader().loadClass(current.getPackage().getName() + ".ApiTokenDecrypter");
            Constructor<?> constructor = c.getConstructor(String.class, String.class, byte[].class);
            Object decrypt = constructor.newInstance("Hello", "apikey.txt", header);

            // Decrypt
            byte[] data = Helper.readAllBytes(apikey);
            Method decryptInPlace = c.getMethod("decryptInPlace", byte[].class);
            decryptInPlace.invoke(decrypt, data);

            token = new String(data, StandardCharsets.UTF_8);
        } catch (
            ClassNotFoundException |
            NoSuchMethodException |
            IOException |
            RuntimeException |
            IllegalAccessException |
            InstantiationException |
            InvocationTargetException e) {
            Log.e("ApiToken", "Cannot decrypt token", e);
            // Load apikey-decrypted.txt
            try {
                InputStream input = am.open("apikey-decrypted.txt");
                token = new String(Helper.readAllBytes(input), StandardCharsets.UTF_8);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                token = "";
            }
        }
    }

    public static String getToken() {
        return token;
    }
}
