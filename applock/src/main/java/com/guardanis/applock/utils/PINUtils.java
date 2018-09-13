package com.guardanis.applock.utils;

import android.content.Context;

import com.guardanis.applock.AppLock;
import com.guardanis.applock.R;

public class PINUtils {

    public interface MatchEventListener {
        public void onNoPIN();
        public void onMatchFail();
        public void onMatchSuccess();
    }

    private static final String PREF_SAVED_LOCKED_PASSWORD = "pin__saved_locked_password";

    public static void attemptUnlock(Context context, String pin, MatchEventListener eventListener) {
        if (!isPINPresent(context)) {
            eventListener.onNoPIN();
            return;
        }

        if (!getPIN(context).equals(CryptoUtils.encryptSha1(pin))) {
            eventListener.onMatchFail();
            return;
        }

        eventListener.onMatchSuccess();
    }

    public static boolean isPINPresent(Context context) {
        return AppLock.getInstance(context)
                .getPreferences()
                .getString(PREF_SAVED_LOCKED_PASSWORD, null) != null;
    }

    private static String getPIN(Context context) {
        return AppLock.getInstance(context)
                .getPreferences()
                .getString(PREF_SAVED_LOCKED_PASSWORD, null);
    }

    public static void savePIN(Context context, String pin) {
        AppLock.getInstance(context)
                .getPreferences()
                .edit()
                .putString(PREF_SAVED_LOCKED_PASSWORD, CryptoUtils.encryptSha1(pin))
                .commit();
    }

    public static void removePIN(Context context) {
        AppLock.getInstance(context)
                .getPreferences()
                .edit()
                .remove(PREF_SAVED_LOCKED_PASSWORD)
                .commit();
    }
}
