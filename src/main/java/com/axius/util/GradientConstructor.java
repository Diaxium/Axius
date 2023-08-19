package com.axius.util;

import net.minecraft.ChatFormatting;

public class GradientConstructor {
    public static String createGradient(String text, int[] color1, int[] color2, int[] color3) {
        StringBuilder gradientBuilder = new StringBuilder();

        int steps = text.length();

        for (int i = 0; i < steps; i++) {
            double progress = (double) i / (steps - 1);
            int[] interpolatedColor = interpolateColor(color1, color2, color3, progress);
            gradientBuilder.append(ChatFormatting.getByCode(getClosestColorCode(interpolatedColor))).append(text.charAt(i));
        }

        return gradientBuilder.toString();
    }

    private static int[] interpolateColor(int[] color1, int[] color2, int[] color3, double progress) {
        int interpolatedR = (int) (color1[0] + progress * (color2[0] - color1[0]));
        int interpolatedG = (int) (color1[1] + progress * (color2[1] - color1[1]));
        int interpolatedB = (int) (color1[2] + progress * (color2[2] - color1[2]));

        if (progress > 0.5) {
            double innerProgress = (progress - 0.5) * 2.0;
            interpolatedR += (int) (innerProgress * (color3[0] - color2[0]));
            interpolatedG += (int) (innerProgress * (color3[1] - color2[1]));
            interpolatedB += (int) (innerProgress * (color3[2] - color2[2]));
        }

        return new int[] { interpolatedR, interpolatedG, interpolatedB };
    }

    private static char getClosestColorCode(int[] rgb) {
        int minDistance = Integer.MAX_VALUE;
        char closestCode = 'f'; // Default to white

        for (ChatFormatting chatFormat : ChatFormatting.values()) {
            if (chatFormat.isColor()) {
                int[] colorRgb = { (chatFormat.getColor() >> 16) & 0xFF,
                        (chatFormat.getColor() >> 8) & 0xFF,
                        chatFormat.getColor() & 0xFF };

                int distance = calculateColorDistance(rgb, colorRgb);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestCode = chatFormat.getChar();
                }
            }
        }

        return closestCode;
    }

    private static int calculateColorDistance(int[] color1, int[] color2) {
        int rDiff = color1[0] - color2[0];
        int gDiff = color1[1] - color2[1];
        int bDiff = color1[2] - color2[2];
        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }
}
