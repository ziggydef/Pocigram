package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessageObject;

import java.util.WeakHashMap;

public final class PociGramLocalMessageEdits {

    private static final String PREFS = "pocigram_local_message_edits";
    private static final WeakHashMap<MessageObject, CharSequence> originals = new WeakHashMap<>();

    private PociGramLocalMessageEdits() {
    }

    private static SharedPreferences preferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private static String key(MessageObject message) {
        return message.currentAccount + ":" + message.getDialogId() + ":" + message.getId();
    }

    public static synchronized void save(MessageObject message, String text) {
        if (message == null || text == null) {
            return;
        }
        rememberOriginal(message);
        preferences().edit().putString(key(message), text).apply();
        apply(message);
    }

    public static synchronized void clear(MessageObject message) {
        if (message == null) {
            return;
        }
        preferences().edit().remove(key(message)).apply();
        CharSequence original = originals.get(message);
        if (original != null) {
            message.messageText = original;
        }
    }

    public static synchronized void apply(MessageObject message) {
        if (message == null || message.messageText == null) {
            return;
        }
        rememberOriginal(message);
        String replacement = preferences().getString(key(message), null);
        if (replacement != null) {
            message.messageText = replacement;
        } else {
            CharSequence original = originals.get(message);
            if (original != null) {
                message.messageText = original;
            }
        }
    }

    private static void rememberOriginal(MessageObject message) {
        if (!originals.containsKey(message)) {
            originals.put(message, message.messageText);
        }
    }
}
