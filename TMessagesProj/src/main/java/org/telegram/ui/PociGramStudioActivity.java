package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
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
        addAction(context, root, "Bubble Studio · Сброс", "Вернуть цвета пузырей из текущей темы", () -> applyBubblePreset(0, true));
        addAction(context, root, "UI Studio · Оформление", "Открыть полный редактор тем Telegram", () -> presentFragment(new ThemeActivity(ThemeActivity.THEME_TYPE_BASIC)));
        addAction(context, root, "Приватность и безопасность", "Настройки приватности аккаунта", () -> presentFragment(new PrivacySettingsActivity()));
        addAction(context, root, "Уведомления", "Звуки, исключения и уведомления", () -> presentFragment(new NotificationsSettingsActivity()));
        addAction(context, root, "Special Contacts", "Открыть список исключений уведомлений для личных чатов и контактов", () -> presentFragment(new NotificationsSettingsActivity(true)));
        addAction(context, root, "Быстрая реакция", "Настроить реакцию быстрого двойного нажатия Telegram", () -> presentFragment(new ReactionsDoubleTapManageActivity()));
        addAction(context, root, "Видимость аккаунта", "Штатные настройки времени посещения, номера телефона и других данных профиля", () -> presentFragment(new PrivacySettingsActivity()));
        addAction(context, root, "Данные и хранилище", "Управление загрузками, использованием сети и локальным хранилищем", () -> presentFragment(new DataSettingsActivity()));
        addAction(context, root, "Профиль аккаунта", "Изменить имя, фамилию и другие доступные данные профиля", () -> presentFragment(new UserInfoActivity()));
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
