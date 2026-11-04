package org.telegram.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class PociGramLoginGlass {

    private PociGramLoginGlass() {
    }

    public static void apply(ViewGroup root) {
        if (root == null) {
            return;
        }

        Context context = root.getContext();
        float density = context.getResources().getDisplayMetrics().density;

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

        styleChildren(root, density);
    }

    private static void styleChildren(ViewGroup parent, float density) {
        int textIndex = 0;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child.getClass().getSimpleName().equals("OutlineTextContainerView")) {
                GradientDrawable glass = new GradientDrawable();
                glass.setShape(GradientDrawable.RECTANGLE);
                glass.setCornerRadius(18f * density);
                glass.setColor(Color.argb(190, 25, 20, 38));
                glass.setStroke(
                        Math.max(1, (int) density),
                        Color.argb(150, 151, 105, 255)
                );

                child.setBackground(glass);
                child.setElevation(5f * density);
            }

            if (child instanceof TextView) {
                TextView text = (TextView) child;

                if (textIndex == 0) {
                    text.setTextColor(Color.WHITE);
                    text.setTextSize(22);
                    text.setTypeface(Typeface.DEFAULT_BOLD);
                } else if (textIndex == 1) {
                    text.setTextColor(Color.rgb(190, 177, 215));
                }

                textIndex++;
            }

            if (child instanceof ViewGroup) {
                styleChildren((ViewGroup) child, density);
            }
        }
    }
}
