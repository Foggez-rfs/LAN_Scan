package com.lanscan.utils;

import android.content.Context;
import java.io.*;

public class FileUtils {
    public static String readFromAssets(Context ctx, String path) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = ctx.getAssets().open(path);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String l;
            while ((l = r.readLine()) != null) sb.append(l).append("\n");
            r.close();
        } catch (Exception ignored) {}
        return sb.toString();
    }

    public static void writeToFile(Context ctx, String name, String content) {
        try (FileOutputStream fos = ctx.openFileOutput(name, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
        } catch (Exception ignored) {}
    }
}
