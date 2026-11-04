package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.telegram.messenger.browser.Browser;

public class PociGramStudioActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(org.telegram.messenger.R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("PociGram Studio");

        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        scrollView.setClipToPadding(false);
        scrollView.setBackgroundColor(Color.rgb(9, 8, 15));

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(AndroidUtilities.dp(20), AndroidUtilities.dp(22), AndroidUtilities.dp(20), AndroidUtilities.dp(24));
        root.setBackgroundColor(Color.rgb(9, 8, 15));
        scrollView.addView(root, new ScrollView.LayoutParams(-1, -2));

        TextView intro = new TextView(context);
        intro.setText("Персонализация PociGram");
        intro.setTextColor(Color.rgb(245, 241, 255));
        intro.setTextSize(22);
        intro.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(intro, new LinearLayout.LayoutParams(-1, AndroidUtilities.dp(48)));

        TextView subtitle = new TextView(context);
        subtitle.setText("Быстрый доступ к системам оформления, приватности и уведомлений Telegram.");
        subtitle.setTextColor(Color.rgb(175, 164, 197));
        subtitle.setTextSize(14);
        subtitle.setPadding(0, 0, 0, AndroidUtilities.dp(18));
        root.addView(subtitle, new LinearLayout.LayoutParams(-1, -2));

        addAction(context, root, "UI Studio · Ziggy Glass", "Чёрный интерфейс с фиолетовым стеклянным акцентом", () -> applyUiPreset(0));
        addAction(context, root, "UI Studio · Obsidian", "Графитовый интерфейс и приглушённые сообщения", () -> applyUiPreset(1));
        addAction(context, root, "UI Studio · Сброс", "Вернуть стандартные цвета текущей темы", () -> applyUiPreset(2));
        addAction(context, root, "Bubble Studio · Прозрачность", "Выбрать уровень прозрачности исходящих сообщений", () -> showBubbleOpacityOptions(context));
        addAction(context, root, "Bubble Studio · Цвет", "Выбрать оттенок исходящих сообщений", () -> showBubbleColorOptions(context));
        addAction(context, root, "Bubble Studio · Glass Violet", "Полупрозрачный фиолетовый стиль исходящих сообщений", () -> applyBubblePreset(Color.argb(210, 76, 48, 112), false));
        addAction(context, root, "Bubble Studio · Obsidian", "Тёмный графитовый стиль исходящих сообщений", () -> applyBubblePreset(Color.argb(220, 35, 31, 45), false));
        addAction(context, root, "Bubble Studio · Matte Obsidian", "Плотный матовый графит без прозрачности", () -> applyBubbleColor(Color.rgb(35, 31, 45)));
        addAction(context, root, "Bubble Studio · Сброс", "Вернуть цвета пузырей из текущей темы", () -> applyBubblePreset(0, true));
        addAction(context, root, "UI Studio · Оформление", "Открыть полный редактор тем Telegram", () -> presentFragment(new ThemeActivity(ThemeActivity.THEME_TYPE_BASIC)));
        addAction(context, root, "Приватность и безопасность", "Настройки приватности аккаунта", () -> presentFragment(new PrivacySettingsActivity()));
        addAction(context, root, "Уведомления", "Звуки, исключения и уведомления", () -> presentFragment(new NotificationsSettingsActivity()));
        addAction(context, root, "Исключения уведомлений", "Настроить уведомления для выбранных личных чатов и контактов Telegram", () -> presentFragment(new NotificationsSettingsActivity(true)));
        addAction(context, root, "Special Contacts · Online alerts", "Получать локальное уведомление, когда выбранный контакт появляется в сети", () -> showSpecialContactsDialog(context));
        addAction(context, root, "Быстрая реакция", "Настроить реакцию быстрого двойного нажатия Telegram", () -> presentFragment(new ReactionsDoubleTapManageActivity()));
        addAction(context, root, "Ghost Mode · приватность", "Штатные настройки видимости времени посещения, номера и прочих данных. Полная невидимость Telegram не гарантируется.", () -> presentFragment(new PrivacySettingsActivity()));
        addAction(context, root, "Данные и хранилище", "Управление загрузками, использованием сети и локальным хранилищем", () -> presentFragment(new DataSettingsActivity()));
        addAction(context, root, "Профиль аккаунта", "Изменить имя, фамилию и другие доступные данные профиля", () -> presentFragment(new UserInfoActivity()));
        addAction(context, root, "Auto Reactions", "Автоматически ставить выбранную реакцию на новые входящие сообщения", () -> showAutoReactionDialog(context));
        addAction(context, root, "Local Profile Hide", "Скрыть номер телефона в собственном профиле только на этом устройстве", () -> showLocalProfileHideDialog(context));
        addAction(context, root, "Live Last Name", "Обновлять фамилию временем каждые 60 секунд, пока приложение работает", () -> showLiveLastNameDialog(context));
        addAction(context, root, "Резервная копия оформления", "Экспортировать или восстановить пресеты PociGram Studio в JSON", () -> showBackupOptions(context));
        addAction(context, root, "Проверить обновления", "Открыть официальный список релизов PociGram", () -> Browser.openUrl(context, "https://github.com/ziggydef/Pocigram/releases"));

        TextView note = new TextView(context);
        note.setText("PociGram · разработано ziGGy\nЧасть параметров управляется Telegram и зависит от ограничений платформы.");
        note.setTextColor(Color.rgb(135, 125, 153));
        note.setTextSize(12);
        note.setPadding(AndroidUtilities.dp(4), AndroidUtilities.dp(18), AndroidUtilities.dp(4), 0);
        root.addView(note, new LinearLayout.LayoutParams(-1, -2));

        fragmentView = scrollView;
        return fragmentView;
    }

    private void applyUiPreset(int preset) {
        boolean reset = preset == 2;
        int background = preset == 0 ? Color.rgb(12, 10, 19) : Color.rgb(17, 17, 22);
        int surface = preset == 0 ? Color.rgb(24, 19, 35) : Color.rgb(30, 30, 37);
        int incoming = preset == 0 ? Color.rgb(43, 35, 57) : Color.rgb(43, 43, 50);
        int outgoing = preset == 0 ? Color.rgb(78, 51, 112) : Color.rgb(58, 55, 67);
        int accent = preset == 0 ? Color.rgb(151, 105, 255) : Color.rgb(170, 160, 190);
        if (!reset) {
            Theme.setColor(Theme.key_windowBackgroundWhite, background, false);
            Theme.setColor(Theme.key_windowBackgroundGray, surface, false);
            Theme.setColor(Theme.key_actionBarDefault, background, false);
            Theme.setColor(Theme.key_actionBarDefaultIcon, accent, false);
            Theme.setColor(Theme.key_actionBarDefaultTitle, Color.rgb(245, 241, 255), false);
            Theme.setColor(Theme.key_chat_inBubble, incoming, false);
            Theme.setColor(Theme.key_chat_outBubble, outgoing, false);
            Theme.setColor(Theme.key_chat_outBubbleGradient1, outgoing, false);
            Theme.setColor(Theme.key_chat_outBubbleGradient2, outgoing, false);
            Theme.setColor(Theme.key_chat_outBubbleGradient3, outgoing, false);
            Theme.setColor(Theme.key_dialogBackground, surface, false);
            Theme.setColor(Theme.key_listSelector, accent, false);
            Theme.setColor(Theme.key_settings_listSelector, accent, false);
            Theme.setColor(Theme.key_windowBackgroundWhiteBlackText, Color.rgb(245, 241, 255), false);
            Theme.setColor(Theme.key_windowBackgroundWhiteGrayText, Color.rgb(174, 163, 194), false);
            Theme.setColor(Theme.key_windowBackgroundWhiteGrayText2, Color.rgb(151, 140, 170), false);
            Theme.setColor(Theme.key_windowBackgroundWhiteHintText, Color.rgb(130, 119, 149), false);
            Theme.setColor(Theme.key_windowBackgroundWhiteBlueText, accent, false);
            Theme.setColor(Theme.key_windowBackgroundWhiteBlueHeader, accent, false);
            Theme.setColor(Theme.key_windowBackgroundWhiteLinkText, accent, false);
            Theme.setColor(Theme.key_divider, Color.rgb(47, 40, 61), false);
            Theme.setColor(Theme.key_actionBarDefaultSubtitle, Color.rgb(174, 163, 194), false);
            Theme.setColor(Theme.key_actionBarDefaultSelector, Color.argb(45, 151, 105, 255), false);
            Theme.setColor(Theme.key_chat_serviceBackground, surface, false);
            Theme.setColor(Theme.key_chat_messagePanelBackground, surface, false);
            Theme.setColor(Theme.key_chat_messagePanelText, Color.rgb(245, 241, 255), false);
            Theme.setColor(Theme.key_chat_messagePanelHint, Color.rgb(151, 140, 170), false);
            Theme.setColor(Theme.key_chat_messagePanelCursor, accent, false);
            Theme.setColor(Theme.key_chat_messagePanelIcons, accent, false);
            Theme.setColor(Theme.key_chat_messagePanelSend, accent, false);
            Theme.setColor(Theme.key_switchTrack, Color.rgb(85, 76, 101), false);
            Theme.setColor(Theme.key_switchTrackChecked, accent, false);
            Theme.setColor(Theme.key_progressCircle, accent, false);
        } else {
            Theme.setColor(Theme.key_windowBackgroundWhite, 0, true);
            Theme.setColor(Theme.key_windowBackgroundGray, 0, true);
            Theme.setColor(Theme.key_actionBarDefault, 0, true);
            Theme.setColor(Theme.key_actionBarDefaultIcon, 0, true);
            Theme.setColor(Theme.key_actionBarDefaultTitle, 0, true);
            Theme.setColor(Theme.key_chat_inBubble, 0, true);
            Theme.setColor(Theme.key_chat_outBubble, 0, true);
            Theme.setColor(Theme.key_chat_outBubbleGradient1, 0, true);
            Theme.setColor(Theme.key_chat_outBubbleGradient2, 0, true);
            Theme.setColor(Theme.key_chat_outBubbleGradient3, 0, true);
            Theme.setColor(Theme.key_dialogBackground, 0, true);
            Theme.setColor(Theme.key_listSelector, 0, true);
            Theme.setColor(Theme.key_settings_listSelector, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteBlackText, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteGrayText, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteGrayText2, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteHintText, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteBlueText, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteBlueHeader, 0, true);
            Theme.setColor(Theme.key_windowBackgroundWhiteLinkText, 0, true);
            Theme.setColor(Theme.key_divider, 0, true);
            Theme.setColor(Theme.key_actionBarDefaultSubtitle, 0, true);
            Theme.setColor(Theme.key_actionBarDefaultSelector, 0, true);
            Theme.setColor(Theme.key_chat_serviceBackground, 0, true);
            Theme.setColor(Theme.key_chat_messagePanelBackground, 0, true);
            Theme.setColor(Theme.key_chat_messagePanelText, 0, true);
            Theme.setColor(Theme.key_chat_messagePanelHint, 0, true);
            Theme.setColor(Theme.key_chat_messagePanelCursor, 0, true);
            Theme.setColor(Theme.key_chat_messagePanelIcons, 0, true);
            Theme.setColor(Theme.key_chat_messagePanelSend, 0, true);
            Theme.setColor(Theme.key_switchTrack, 0, true);
            Theme.setColor(Theme.key_switchTrackChecked, 0, true);
            Theme.setColor(Theme.key_progressCircle, 0, true);
        }
        getStudioPreferences().edit().putInt("ui_preset", preset).apply();
        Theme.saveCurrentTheme(Theme.getCurrentTheme(), false, false, false);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, false, false);
    }

    private boolean isSupportedBubbleColor(int color) {
        if (color == 0) {
            return true;
        }
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        boolean supportedRgb =
                (red == 76 && green == 48 && blue == 112) ||
                (red == 35 && green == 31 && blue == 45) ||
                (red == 53 && green == 51 && blue == 61) ||
                (red == 34 && green == 55 && blue == 91) ||
                (red == 89 && green == 39 && blue == 63);
        return supportedRgb && Color.alpha(color) >= 60;
    }

    private void showBubbleColorOptions(Context context) {
        CharSequence[] names = {"Ziggy Violet", "Obsidian", "Graphite", "Deep Blue", "Burgundy"};
        int[] colors = {
                Color.rgb(76, 48, 112),
                Color.rgb(35, 31, 45),
                Color.rgb(53, 51, 61),
                Color.rgb(34, 55, 91),
                Color.rgb(89, 39, 63)
        };
        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Цвет пузырей")
                .setItems(names, (dialog, which) -> applyBubbleColor(Color.argb(220, Color.red(colors[which]), Color.green(colors[which]), Color.blue(colors[which]))))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showBubbleOpacityOptions(Context context) {
        CharSequence[] options = {"35%", "50%", "65%", "80%", "100%"};
        int[] alphaValues = {89, 128, 166, 204, 255};
        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Прозрачность пузырей")
                .setItems(options, (dialog, which) -> applyBubbleColor(Color.argb(alphaValues[which], 76, 48, 112)))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void applyBubbleColor(int color) {
        Theme.setColor(Theme.key_chat_outBubble, color, false);
        Theme.setColor(Theme.key_chat_outBubbleGradient1, color, false);
        Theme.setColor(Theme.key_chat_outBubbleGradient2, color, false);
        Theme.setColor(Theme.key_chat_outBubbleGradient3, color, false);
        getStudioPreferences().edit().putInt("bubble_color", color).apply();
        Theme.saveCurrentTheme(Theme.getCurrentTheme(), false, false, false);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, false, false);
    }

    private void applyBubblePreset(int color, boolean reset) {
        Theme.setColor(Theme.key_chat_outBubble, color, reset);
        Theme.setColor(Theme.key_chat_outBubbleGradient1, color, reset);
        Theme.setColor(Theme.key_chat_outBubbleGradient2, color, reset);
        Theme.setColor(Theme.key_chat_outBubbleGradient3, color, reset);
        getStudioPreferences().edit().putInt("bubble_color", reset ? 0 : color).apply();
        Theme.saveCurrentTheme(Theme.getCurrentTheme(), false, false, false);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, false, false);
    }


    private android.content.SharedPreferences getStudioPreferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences("pocigram_settings", Context.MODE_PRIVATE);
    }

    private void showSpecialContactsDialog(Context context) {
        java.util.Set<String> ids = PociGramSpecialContactsManager.getInstance().getUserIds();
        StringBuilder message = new StringBuilder();
        if (ids.isEmpty()) {
            message.append("Список пуст. Добавь контакты Telegram, чтобы получать уведомления об их появлении в сети.");
        } else {
            message.append("Отслеживаемые контакты:\n");
            for (String value : ids) {
                try {
                    long userId = Long.parseLong(value);
                    org.telegram.tgnet.TLRPC.User user = org.telegram.messenger.MessagesController.getInstance(org.telegram.messenger.UserConfig.selectedAccount).getUser(userId);
                    String name = user == null ? "ID " + value : org.telegram.messenger.ContactsController.formatName(user.first_name, user.last_name);
                    message.append("• ").append(name).append('\n');
                } catch (Exception ignored) {
                }
            }
            message.append("\nУведомления приходят при переходе контакта из офлайна в онлайн. Нужны разрешения Android на уведомления.");
        }
        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Special Contacts")
                .setMessage(message.toString())
                .setPositiveButton("Добавить контакт", (dialog, which) -> openSpecialContactPicker())
                .setNeutralButton("Удалить из списка", (dialog, which) -> showSpecialContactRemovalDialog(context))
                .setNegativeButton("Закрыть", null)
                .show();
    }

    private void openSpecialContactPicker() {
        Bundle args = new Bundle();
        args.putBoolean("onlyUsers", true);
        args.putBoolean("destroyAfterSelect", true);
        args.putBoolean("returnAsResult", true);
        args.putBoolean("allowSelf", false);
        args.putBoolean("allowBots", false);
        args.putBoolean("needPhonebook", false);
        ContactsActivity picker = new ContactsActivity(args);
        picker.setDelegate((user, param, activity) -> {
            PociGramSpecialContactsManager.getInstance().addUser(user.id);
            android.widget.Toast.makeText(getParentActivity(), "Контакт добавлен в Special Contacts", android.widget.Toast.LENGTH_SHORT).show();
        });
        presentFragment(picker);
    }

    private void showSpecialContactRemovalDialog(Context context) {
        java.util.ArrayList<String> ids = new java.util.ArrayList<>(PociGramSpecialContactsManager.getInstance().getUserIds());
        if (ids.isEmpty()) {
            android.widget.Toast.makeText(context, "Список контактов пуст", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        CharSequence[] names = new CharSequence[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            String value = ids.get(i);
            String name = "ID " + value;
            try {
                long userId = Long.parseLong(value);
                org.telegram.tgnet.TLRPC.User user = org.telegram.messenger.MessagesController.getInstance(org.telegram.messenger.UserConfig.selectedAccount).getUser(userId);
                if (user != null) {
                    name = org.telegram.messenger.ContactsController.formatName(user.first_name, user.last_name);
                }
            } catch (Exception ignored) {
            }
            names[i] = name;
        }
        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Удалить контакт")
                .setItems(names, (dialog, which) -> {
                    PociGramSpecialContactsManager.getInstance().removeUser(ids.get(which));
                    android.widget.Toast.makeText(context, "Контакт удалён", android.widget.Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showLocalProfileHideDialog(Context context) {
        android.content.SharedPreferences prefs = ApplicationLoader.applicationContext.getSharedPreferences("pocigram_local_profile", Context.MODE_PRIVATE);
        CheckBox hidePhone = new CheckBox(context);
        hidePhone.setText("Скрывать номер телефона в моём профиле на этом устройстве");
        hidePhone.setChecked(prefs.getBoolean("hide_own_phone", false));
        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Local Profile Hide")
                .setMessage("Это только локальное скрытие строки номера в твоём профиле PociGram. Оно не меняет настройки приватности Telegram и не скрывает номер от других пользователей. Перезапусти экран профиля после изменения.")
                .setView(hidePhone)
                .setPositiveButton("Сохранить", (dialog, which) -> prefs.edit().putBoolean("hide_own_phone", hidePhone.isChecked()).apply())
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showAutoReactionDialog(Context context) {
        PociGramAutoReactionManager manager = PociGramAutoReactionManager.getInstance();
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(AndroidUtilities.dp(22), AndroidUtilities.dp(10), AndroidUtilities.dp(22), AndroidUtilities.dp(6));

        EditText emoji = new EditText(context);
        emoji.setSingleLine(true);
        emoji.setHint("Один эмодзи, например ❤️");
        emoji.setText(manager.getEmoji());
        content.addView(emoji, new LinearLayout.LayoutParams(-1, AndroidUtilities.dp(48)));

        CheckBox enabled = new CheckBox(context);
        enabled.setText("Включить автоматические реакции");
        enabled.setChecked(manager.isEnabled());
        content.addView(enabled, new LinearLayout.LayoutParams(-1, -2));

        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Auto Reactions")
                .setMessage("PociGram будет добавлять выбранную реакцию к новым входящим сообщениям. Не более одной реакции за 5 секунд. Настройка действует только на этом устройстве и использует обычный API Telegram.")
                .setView(content)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String value = emoji.getText().toString().trim();
                    String[] supported = {"❤️", "👍", "🔥", "😂", "😍", "🎉", "👎"};
                    boolean valid = false;
                    for (String item : supported) {
                        if (item.equals(value)) {
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) {
                        Toast.makeText(context, "Выбери реакцию: ❤️ 👍 🔥 😂 😍 🎉 👎", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    manager.configure(enabled.isChecked(), value);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showLiveLastNameDialog(Context context) {
        android.content.SharedPreferences prefs = ApplicationLoader.applicationContext.getSharedPreferences("pocigram_live_last_name", Context.MODE_PRIVATE);
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(AndroidUtilities.dp(22), AndroidUtilities.dp(10), AndroidUtilities.dp(22), AndroidUtilities.dp(6));

        EditText prefix = new EditText(context);
        prefix.setSingleLine(true);
        prefix.setHint("Текст перед временем");
        prefix.setText(prefs.getString("prefix", ""));
        content.addView(prefix, new LinearLayout.LayoutParams(-1, AndroidUtilities.dp(48)));

        EditText suffix = new EditText(context);
        suffix.setSingleLine(true);
        suffix.setHint("Текст после времени");
        suffix.setText(prefs.getString("suffix", ""));
        content.addView(suffix, new LinearLayout.LayoutParams(-1, AndroidUtilities.dp(48)));

        CheckBox use24Hour = new CheckBox(context);
        use24Hour.setText("24-часовой формат (HH:mm)");
        use24Hour.setChecked(prefs.getBoolean("use_24_hour", true));
        content.addView(use24Hour, new LinearLayout.LayoutParams(-1, -2));

        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Live Last Name")
                .setMessage("Фамилия аккаунта будет обновляться на сервере каждую минуту. Изменение видно другим пользователям. При отключении PociGram попробует восстановить исходную фамилию. Обновления выполняются раз в минуту, пока процесс приложения работает; Android может ограничивать фоновые обновления.")
                .setView(content)
                .setPositiveButton("Включить / сохранить", (dialog, which) ->
                        PociGramLiveLastNameManager.configure(prefix.getText().toString(), suffix.getText().toString(), use24Hour.isChecked(), true))
                .setNeutralButton("Отключить и восстановить", (dialog, which) ->
                        PociGramLiveLastNameManager.configure(prefix.getText().toString(), suffix.getText().toString(), use24Hour.isChecked(), false))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showBackupOptions(Context context) {
        new org.telegram.ui.ActionBar.AlertDialog.Builder(context)
                .setTitle("Резервная копия оформления")
                .setMessage("Экспортируются только пресеты оформления PociGram Studio. Данные аккаунта, чаты и пароли не включаются.")
                .setPositiveButton("Экспорт", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/json");
                    intent.putExtra(Intent.EXTRA_TITLE, "pocigram-studio-backup.json");
                    startActivityForResult(intent, 7401);
                })
                .setNegativeButton("Импорт", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/json");
                    startActivityForResult(intent, 7402);
                })
                .setNeutralButton("Отмена", null)
                .show();
    }

    @Override
    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        super.onActivityResultFragment(requestCode, resultCode, data);
        if (resultCode != android.app.Activity.RESULT_OK || data == null || data.getData() == null) {
            return;
        }
        Uri uri = data.getData();
        try {
            if (requestCode == 7401) {
                JSONObject backup = new JSONObject();
                backup.put("format", "pocigram-studio");
                backup.put("version", 1);
                backup.put("ui_preset", getStudioPreferences().getInt("ui_preset", -1));
                backup.put("bubble_color", getStudioPreferences().getInt("bubble_color", 0));
                try (OutputStream output = ApplicationLoader.applicationContext.getContentResolver().openOutputStream(uri)) {
                    if (output == null) {
                        throw new IllegalStateException("Не удалось открыть файл для записи");
                    }
                    output.write(backup.toString(2).getBytes(StandardCharsets.UTF_8));
                    output.flush();
                }
                Toast.makeText(getParentActivity(), "Резервная копия сохранена", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 7402) {
                StringBuilder json = new StringBuilder();
                try (InputStream input = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri)) {
                    if (input == null) {
                        throw new IllegalStateException("Не удалось открыть файл");
                    }
                    byte[] buffer = new byte[4096];
                    int count;
                    while ((count = input.read(buffer)) != -1) {
                        if (json.length() + count > 65536) {
                            throw new IllegalArgumentException("Файл слишком большой");
                        }
                        json.append(new String(buffer, 0, count, StandardCharsets.UTF_8));
                    }
                }
                JSONObject backup = new JSONObject(json.toString());
                if (!"pocigram-studio".equals(backup.optString("format")) || backup.optInt("version", -1) != 1) {
                    throw new IllegalArgumentException("Неподдерживаемый формат резервной копии");
                }
                int uiPreset = backup.optInt("ui_preset", -1);
                int bubbleColor = backup.optInt("bubble_color", 0);
                if (uiPreset < -1 || uiPreset > 2) {
                    throw new IllegalArgumentException("Некорректный пресет оформления");
                }
                if (!isSupportedBubbleColor(bubbleColor)) {
                    throw new IllegalArgumentException("Некорректный цвет пузырей");
                }
                getStudioPreferences().edit().putInt("ui_preset", uiPreset).putInt("bubble_color", bubbleColor).apply();
                if (uiPreset >= 0) {
                    applyUiPreset(uiPreset);
                }
                applyBubblePreset(bubbleColor, bubbleColor == 0);
                Toast.makeText(getParentActivity(), "Оформление восстановлено", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getParentActivity(), "Ошибка резервной копии: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addAction(Context context, LinearLayout root, String title, String description, Runnable action) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(13), AndroidUtilities.dp(16), AndroidUtilities.dp(13));
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.argb(205, 25, 20, 38));
        background.setCornerRadius(AndroidUtilities.dp(16));
        background.setStroke(AndroidUtilities.dp(1), Color.argb(150, 151, 105, 255));
        card.setBackground(background);
        card.setClickable(true);
        card.setFocusable(true);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextColor(Color.rgb(245, 241, 255));
        titleView.setTextSize(16);
        card.addView(titleView, new LinearLayout.LayoutParams(-1, -2));

        TextView descriptionView = new TextView(context);
        descriptionView.setText(description);
        descriptionView.setTextColor(Color.rgb(165, 153, 190));
        descriptionView.setTextSize(12);
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(-1, -2);
        descriptionParams.topMargin = AndroidUtilities.dp(4);
        card.addView(descriptionView, descriptionParams);

        card.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = AndroidUtilities.dp(10);
        root.addView(card, params);
    }
}
