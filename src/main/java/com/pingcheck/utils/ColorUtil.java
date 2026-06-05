package com.pingcheck.utils;
import java.awt.Color;

public class ColorUtil
{
    /**
     * Takes any color and forces it to have the specified alpha.
     */
    public static Color applyAlpha(Color userColor, int alpha)
    {
        if (userColor == null)
        {
            return null;
        }

        // Extract the original Red, Green, and Blue channels
        int r = userColor.getRed();
        int g = userColor.getGreen();
        int b = userColor.getBlue();

        return new Color(r, g, b, alpha);
    }
}