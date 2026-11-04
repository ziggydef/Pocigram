package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public final class PociGramAutoReactionManager implements NotificationCenter.NotificationCenterDelegate {

    private static final String PREFS = "pocigram_auto_reactions";
    private static final long MIN_INTERVAL = 5000L;
    private static final int MAX_RECENT = 300;
    private static final PociGramAutoReactionManager INSTANCE = new PociGramAutoReactionManager();

    private final Set<String> processed = new LinkedHashSet<>();
    private int observedAccount = -1;
    private long lastReactionAt;

    private PociGramAutoReactionManager() {
    }

    public static PociGramAutoReactionManager getInstance() {
        return INSTANCE;
    }

    private SharedPreferences preferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void configure(boolean enabled, String emoji) {
        String value = emoji == null ? "" : emoji.trim();
        if (value.length() > 8) {
            value = value.substring(0, 8);
        }
        preferences().edit().putBoolean("enabled", enabled).putString("emoji", value).apply();
    }

    public boolean isEnabled() {
        return preferences().getBoolean("enabled", false);
    }

    public String getEmoji() {
        return preferences().getString("emoji", "❤️");
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
        NotificationCenter.getInstance(account).addObserver(this, NotificationCenter.didReceiveNewMessages);
    }

    public void stop() {
        if (observedAccount >= 0) {
            NotificationCenter.getInstance(observedAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
        }
        observedAccount = -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id != NotificationCenter.didReceiveNewMessages || !isEnabled() || account != observedAccount || args.length < 4) {
            return;
        }
        if (args[2] instanceof Boolean && (Boolean) args[2]) {
            return;
        }
        if (args[3] instanceof Integer && (Integer) args[3] != 0) {
            return;
        }
        ArrayList<MessageObject> messages = (ArrayList<MessageObject>) args[1];
        if (messages == null || messages.isEmpty()) {
            return;
        }
        MessageObject target = null;
        for (int i = messages.size() - 1; i >= 0; i--) {
            MessageObject candidate = messages.get(i);
            if (candidate == null || candidate.messageOwner == null || candidate.messageOwner.out || candidate.messageOwner.action != null || candidate.getId() <= 0) {
                continue;
            }
            target = candidate;
            break;
        }
        if (target == null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastReactionAt < MIN_INTERVAL) {
            return;
        }
        long dialogId = target.getDialogId();
        String key = account + ":" + dialogId + ":" + target.getId();
        if (processed.contains(key)) {
            return;
        }
        processed.add(key);
        while (processed.size() > MAX_RECENT) {
            processed.remove(processed.iterator().next());
        }
        lastReactionAt = now;

        TLRPC.InputPeer peer = MessagesController.getInstance(account).getInputPeer(dialogId);
        if (peer == null) {
            return;
        }
        String emoji = getEmoji();
        if (emoji.isEmpty()) {
            return;
        }

        TLRPC.TL_messages_sendReaction request = new TLRPC.TL_messages_sendReaction();
        request.flags |= 1;
        request.peer = peer;
        request.msg_id = target.getId();
        TLRPC.TL_reactionEmoji reaction = new TLRPC.TL_reactionEmoji();
        reaction.emoticon = emoji;
        request.reaction.add(reaction);
        ConnectionsManager.getInstance(account).sendRequest(request, (response, error) -> {
        });
    }
}
