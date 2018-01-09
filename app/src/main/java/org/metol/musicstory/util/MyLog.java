package org.metol.musicstory.util;

import android.util.Log;

import org.metol.musicstory.Common;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class MyLog {
    public static void i(final String tag, final String msg) {
        if(Common.IS_DEBUG) {
            final StackTraceElement stackTrace = new Exception().getStackTrace()[1];

            String fileName = stackTrace.getFileName();
            if (fileName == null)
                fileName = "";  // It is necessary if you want to use proguard obfuscation.

            final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                    + stackTrace.getLineNumber() + ")";

            Log.i(tag, info + ":" + msg);
        }
    }

    public static void e(final String tag, final String msg){
        if(Common.IS_DEBUG) {
            Throwable throwable = new Throwable(msg);
            Log.e(tag, Log.getStackTraceString(throwable));
        }
    }
}
