package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class PociGramLiveLastNameManager {

    private static final String PREFS = "pocigram_live_last_name";
    private static final long UPDATE_INTERVAL = 60_000L;
    private static boolean running;
    private static boolean requestInFlight;
    private static Runnable updateRunnable;
    private static String lastAppliedName = "";

    private PociGramLiveLastNameManager() {
    }

    private static SharedPreferences preferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void configure(String prefix, String suffix, boolean use24Hour, boolean enabled) {
        SharedPreferences prefs = preferences();
        String safePrefix = prefix == null ? "" : prefix.trim();
        String safeSuffix = suffix == null ? "" : suffix.trim();
        boolean wasEnabled = prefs.getBoolean("enabled", false);
        if (enabled && !wasEnabled) {
            TLRPC.User currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
            prefs.edit()
                    .putString("original_last_name", currentUser == null || currentUser.last_name == null ? "" : currentUser.last_name)
                    .putBoolean("original_saved", true)
                    .apply();
        }
        prefs.edit()
                .putString("prefix", safePrefix)
                .putString("suffix", safeSuffix)
                .putBoolean("use_24_hour", use24Hour)
                .putBoolean("enabled", enabled)
                .apply();

        if (!enabled) {
            stop();
            if (prefs.getBoolean("original_saved", false)) {
                restoreOriginalName();
            }
        } else if (running) {
            updateNow();
        }
    }

    public static void start() {
        if (running || !preferences().getBoolean("enabled", false)) {
            return;
        }
        running = true;
        if (updateRunnable == null) {
            updateRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!running) {
                        return;
                    }
                    updateNow();
                    AndroidUtilities.runOnUIThread(this, UPDATE_INTERVAL);
                }
            };
        }
        AndroidUtilities.cancelRunOnUIThread(updateRunnable);
        updateRunnable.run();
    }

    public static void stop() {
        running = false;
        if (updateRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(updateRunnable);
        }
    }

    private static void updateNow() {
        SharedPreferences prefs = preferences();
        if (!prefs.getBoolean("enabled", false) || requestInFlight) {
            return;
        }
        int account = UserConfig.selectedAccount;
        TLRPC.User user = UserConfig.getInstance(account).getCurrentUser();
        if (user == null) {
            return;
        }

        String pattern = prefs.getBoolean("use_24_hour", true) ? "HH:mm" : "hh:mm a";
        String time = new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
        String newName = (prefs.getString("prefix", "") + time + prefs.getString("suffix", "")).trim();
        if (newName.isEmpty() || newName.equals(user.last_name) || newName.equals(lastAppliedName)) {
            return;
        }

        TL_account.updateProfile request = new TL_account.updateProfile();
        request.flags |= 2;
        request.last_name = newName;
        requestInFlight = true;
        ConnectionsManager.getInstance(account).sendRequest(request, (response, error) -> AndroidUtilities.runOnUIThread(() -> {
            requestInFlight = false;
            if (error != null) {
                FileLog.e("PociGram Live Last Name update failed: " + error.text);
                return;
            }
            TLRPC.User current = UserConfig.getInstance(account).getCurrentUser();
            if (current != null) {
                current.last_name = newName;
                UserConfig.getInstance(account).saveConfig(false);
                NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.updateInterfaces, 1);
                lastAppliedName = newName;
            }
        }));
    }

    private static void restoreOriginalName() {
        SharedPreferences prefs = preferences();
        if (!prefs.getBoolean("original_saved", false)) {
            return;
        }
        if (requestInFlight) {
            AndroidUtilities.runOnUIThread(PociGramLiveLastNameManager::restoreOriginalName, 1000);
            return;
        }
        int account = UserConfig.selectedAccount;
        String original = prefs.getString("original_last_name", "");
        TL_account.updateProfile request = new TL_account.updateProfile();
        request.flags |= 2;
        request.last_name = original;
        MessagesController.getInstance(account).getConnectionsManager().sendRequest(request, (response, error) -> AndroidUtilities.runOnUIThread(() -> {
            if (error != null) {
                FileLog.e("PociGram Live Last Name restore failed: " + error.text);
                return;
            }
            TLRPC.User current = UserConfig.getInstance(account).getCurrentUser();
            if (current != null) {
                current.last_name = original;
                MessagesController.getInstance(account).putUser(current, false);
                NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.updateInterfaces, 1);
            }
            prefs.edit().remove("original_last_name").putBoolean("original_saved", false).apply();
            lastAppliedName = "";
        }));
    }
}
