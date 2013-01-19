package uk.co.zutty.glarena.util;

import java.io.*;

/**
 * General IO utils
 */
public class IOUtils {
    public static void closeSilently(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {}
        }
    }

    public static CharSequence readSource(String filename) {
        Reader reader = new InputStreamReader(IOUtils.class.getResourceAsStream(filename));

        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        int len = 0;

        try {
            while((len = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, len);
            }
        } catch (IOException e) {
            return "";
        }

        return builder.toString();
    }
}
