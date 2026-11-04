package org.telegram.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
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
        addAction(context, root, "Bubble Studio · Glass Violet", "Полупрозрачный фиолетовый стиль исходящих сообщений", () -> applyBubblePreset(Color.argb(210, 76, 48, 112), false));
        addAction(context, root, "Bubble Studio · Obsidian", "Тёмный графитовый стиль исходящих сообщений", () -> applyBubblePreset(Color.argb(220, 35, 31, 45), false));
        addAction(context, root, "Bubble Studio · Сброс", "Вернуть цвета пузырей из текущей темы", () -> applyBubblePreset(0, true));
        addAction(context, root, "UI Studio · Оформление", "Открыть полный редактор тем Telegram", () -> presentFragment(new ThemeActivity(ThemeActivity.THEME_TYPE_BASIC)));
        addAction(context, root, "Приватность и безопасность", "Настройки приватности аккаунта", () -> presentFragment(new PrivacySettingsActivity()));
        addAction(context, root, "Уведомления", "Звуки, исключения и уведомления", () -> presentFragment(new NotificationsSettingsActivity()));
        addAction(context, root, "Исключения уведомлений", "Настроить уведомления для отдельных чатов и контактов", () -> presentFragment(new NotificationsSettingsActivity()));
        addAction(context, root, "Quick Reactions", "Настроить реакцию быстрого двойного нажатия", () -> presentFragment(new ReactionsDoubleTapManageActivity()));
        addAction(context, root, "Видимость аккаунта", "Штатные настройки времени посещения, номера телефона и других данных профиля", () -> presentFragment(new PrivacySettingsActivity()));
        addAction(context, root, "Данные и хранилище", "Управление загрузками, использованием сети и локальным хранилищем", () -> presentFragment(new DataSettingsActivity()));
        addAction(context, root, "Профиль аккаунта", "Изменить имя, фамилию и другие доступные данные профиля", () -> presentFragment(new UserInfoActivity()));
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
        }
        Theme.saveCurrentTheme(Theme.getCurrentTheme(), false, false, false);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, false, false);
    }

    private void applyBubblePreset(int color, boolean reset) {
        Theme.setColor(Theme.key_chat_outBubble, color, reset);
        Theme.setColor(Theme.key_chat_outBubbleGradient1, color, reset);
        Theme.setColor(Theme.key_chat_outBubbleGradient2, color, reset);
        Theme.setColor(Theme.key_chat_outBubbleGradient3, color, reset);
        Theme.saveCurrentTheme(Theme.getCurrentTheme(), false, false, false);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, false, false);
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
