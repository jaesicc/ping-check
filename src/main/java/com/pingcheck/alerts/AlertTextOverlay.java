package com.pingcheck.alerts;

import com.pingcheck.PingCheckConfig;
import com.pingcheck.utils.ColorUtil;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlertTextOverlay extends OverlayPanel
{
    private static final String ALERT_TITLE = "Ping Check!";
    private static final String ALERT_DESCRIPTION = "Your ping is higher than you would prefer for this content. Go to a different world or configure your Ping Check settings differently.";

    private final PingCheckConfig config;

    @Inject
    private AlertTextOverlay(PingCheckConfig config)
    {
        this.config = config;
        setPosition(OverlayPosition.TOP_RIGHT);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();

        boolean isBlinkOn = (System.currentTimeMillis() / 500) % 2 == 0;
        Color activeBgColor = isBlinkOn ? ColorUtil.applyAlpha(config.flashColor(), 80) : ComponentConstants.STANDARD_BACKGROUND_COLOR;
        panelComponent.setBackgroundColor(activeBgColor);

        panelComponent.getChildren().add(TitleComponent.builder()
            .text(ALERT_TITLE)
            .color(config.textColor())
            .build());

        Color descriptionColor = getMutedTextColor(config.textColor());
        panelComponent.getChildren().add(LineComponent.builder()
            .left(ALERT_DESCRIPTION)
            .leftColor(descriptionColor)
            .build());

        return super.render(graphics);
    }

    private Color getMutedTextColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int rgb = Color.HSBtoRGB(hsb[0], hsb[1] * 0.35f, hsb[2] * 0.85f);
        return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, 220);
    }
}
