package org.telegram.messenger.rubika;


import android.util.Log;
import android.widget.Toast;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.MessagesController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class MyLog {
    public static boolean isDebugAble = BuildConfig.DEBUG;
    public static boolean throwApiExeption = false;

    public static void e(String tag, String message) {
        if (isDebugAble) {

            int maxLogSize = 2000;
            if (message.length() < maxLogSize) {
                Log.e("AAA " + tag, message);
                return;
            }

            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.e("AAA " + tag, message.substring(start, end));
            }


        }
    }

    public static void e(String tag, String s, Throwable e) {
    }


    public static void handleExceptionThrowOnDebug(Throwable e) {
        if (isDebugAble) {
            e.printStackTrace();

        } else {

        }
    }
}
