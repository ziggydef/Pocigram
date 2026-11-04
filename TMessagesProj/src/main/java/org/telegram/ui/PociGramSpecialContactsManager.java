package org.telegram.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

import java.util.HashSet;
import java.util.Set;

public final class PociGramSpecialContactsManager implements NotificationCenter.NotificationCenterDelegate {

    private static final String PREFS = "pocigram_special_contacts";
    private static final String CHANNEL_ID = "pocigram_special_contacts";
    private static final PociGramSpecialContactsManager INSTANCE = new PociGramSpecialContactsManager();

    private int observedAccount = -1;

    private PociGramSpecialContactsManager() {
    }

    public static PociGramSpecialContactsManager getInstance() {
        return INSTANCE;
    }

    private SharedPreferences preferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public Set<String> getUserIds() {
        return new HashSet<>(preferences().getStringSet("user_ids", new HashSet<>()));
    }

    public void addUser(long userId) {
        Set<String> ids = getUserIds();
        ids.add(Long.toString(userId));
        preferences().edit().putStringSet("user_ids", ids).apply();
        TLRPC.User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(userId);
        preferences().edit().putBoolean("online_" + userId, isOnline(user, UserConfig.selectedAccount)).apply();
    }

    public void removeUser(String userId) {
        Set<String> ids = getUserIds();
        ids.remove(userId);
        preferences().edit().putStringSet("user_ids", ids).remove("online_" + userId).apply();
    }

    public void start() {
        int account = UserConfig.selectedAccount;
        if (observedAccount == account) {
            return;
        }
        stop();
        if (!UserConfig.getInstance(account).isClientActivated()) {
            return;
        }
        observedAccount = account;
        NotificationCenter.getInstance(account).addObserver(this, NotificationCenter.updateInterfaces);
        refreshStatuses(false);
        createNotificationChannel();
    }

    public void stop() {
        if (observedAccount >= 0) {
            NotificationCenter.getInstance(observedAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        }
        observedAccount = -1;
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id != NotificationCenter.updateInterfaces || account != observedAccount || args.length == 0 || !(args[0] instanceof Integer)) {
            return;
        }
        int mask = (Integer) args[0];
        if ((mask & MessagesController.UPDATE_MASK_STATUS) != 0) {
            refreshStatuses(true);
        }
    }

    private void refreshStatuses(boolean notify) {
        int account = observedAccount;
        if (account < 0) {
            return;
        }
        SharedPreferences prefs = preferences();
        Set<String> ids = getUserIds();
        SharedPreferences.Editor editor = prefs.edit();
        for (String value : ids) {
            long userId;
            try {
                userId = Long.parseLong(value);
            } catch (Exception e) {
                continue;
            }
            TLRPC.User user = MessagesController.getInstance(account).getUser(userId);
            boolean online = isOnline(user, account);
            String key = "online_" + userId;
            boolean known = prefs.contains(key);
            boolean wasOnline = prefs.getBoolean(key, false);
            editor.putBoolean(key, online);
            if (notify && known && online && !wasOnline && user != null) {
                showOnlineNotification(account, user);
            }
        }
        editor.apply();
    }

    private boolean isOnline(TLRPC.User user, int account) {
        return user != null && user.status != null && user.status.expires > ConnectionsManager.getInstance(account).getCurrentTime();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        Context context = ApplicationLoader.applicationContext;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null && manager.getNotificationChannel(CHANNEL_ID) == null) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Special Contacts", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    private void showOnlineNotification(int account, TLRPC.User user) {
        Context context = ApplicationLoader.applicationContext;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) {
            return;
        }
        String name = org.telegram.messenger.ContactsController.formatName(user.first_name, user.last_name);
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(context, CHANNEL_ID)
                : new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Контакт в сети")
                .setContentText(name)
                .setAutoCancel(true);
        try {
            manager.notify((int) (user.id & 0x7fffffff), builder.build());
        } catch (Exception ignored) {
        }
    }
}
