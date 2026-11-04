package org.telegram.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.telegram.ui.Components.OutlineTextContainerView;

public final class PociGramLoginGlass {

    private static final int TEXT_PRIMARY = Color.rgb(245, 241, 255);
    private static final int TEXT_SECONDARY = Color.rgb(190, 177, 215);
    private static final int FIELD_TEXT = Color.rgb(250, 247, 255);
    private static final int FIELD_HINT = Color.rgb(153, 139, 177);
    private static final int GLASS_FILL = Color.argb(205, 25, 20, 38);
    private static final int GLASS_STROKE = Color.argb(175, 151, 105, 255);

    private PociGramLoginGlass() {
    }

    public static void apply(ViewGroup root, TextView title, TextView subtitle, EditText... fields) {
        if (root == null) {
            return;
        }

        GradientDrawable background = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(8, 7, 14),
                        Color.rgb(17, 10, 29),
                        Color.rgb(7, 7, 12)
                }
        );
        root.setBackground(background);
        root.setClipToPadding(false);

        styleContainers(root);

        if (title != null) {
            title.setTextColor(TEXT_PRIMARY);
            title.setTextSize(22);
        }
        if (subtitle != null) {
            subtitle.setTextColor(TEXT_SECONDARY);
        }
        if (fields != null) {
            for (EditText field : fields) {
                if (field != null) {
                    field.setTextColor(FIELD_TEXT);
                    field.setHintTextColor(FIELD_HINT);
                }
            }
        }
    }

    private static void styleContainers(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof OutlineTextContainerView) {
                GradientDrawable glass = new GradientDrawable();
                glass.setShape(GradientDrawable.RECTANGLE);
                glass.setCornerRadius(18f * child.getResources().getDisplayMetrics().density);
                glass.setColor(GLASS_FILL);
                glass.setStroke(
                        Math.max(1, (int) child.getResources().getDisplayMetrics().density),
                        GLASS_STROKE
                );
                child.setBackground(glass);
                child.setElevation(5f * child.getResources().getDisplayMetrics().density);
            }

            if (child instanceof ViewGroup) {
                styleContainers((ViewGroup) child);
            }
        }
    }
}
