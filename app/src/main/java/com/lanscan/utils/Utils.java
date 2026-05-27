package com.lanscan.utils;

import android.content.Context;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String readAsset(Context ctx, String path) {
        try {
            InputStream in = ctx.getAssets().open(path);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String l;
            while ((l = r.readLine()) != null) sb.append(l).append("\n");
            r.close();
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void saveFile(Context ctx, String name, String content) {
        try (FileOutputStream fos = ctx.openFileOutput(name, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
        } catch (Exception ignored) {}
    }
}
