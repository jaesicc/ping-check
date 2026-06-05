package com.pingcheck.alerts;

import com.pingcheck.PingCheckConfig;
import com.pingcheck.utils.ColorUtil;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlertScreenFlashOverlay extends Overlay
{
	private final Client client;
	private final PingCheckConfig config;

	// Track when the flash was triggered so it doesn't blink indefinitely
	private long flashStartTime = -1;

	@Inject
	private AlertScreenFlashOverlay(Client client, PingCheckConfig config)
	{
		this.client = client;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	public void startFlash()
	{
		this.flashStartTime = System.currentTimeMillis();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (flashStartTime == -1)
		{
			return null;
		}

		long elapsed = System.currentTimeMillis() - flashStartTime;

		// Stop flashing after 10 seconds (1500ms)
		if (elapsed > 10000)
		{
			flashStartTime = -1;
			return null;
		}

		// Alternate every 250ms for a rapid flashing panic effect
		boolean isFlashCycleOn = (elapsed / 500) % 2 == 0;

		if (isFlashCycleOn)
		{
			// Fill the whole canvas with the custom config color
			graphics.setColor(ColorUtil.applyAlpha(config.flashColor(), 50));
			graphics.fillRect(0, 0, client.getCanvasWidth(), client.getCanvasHeight());
		}

		return null;
	}
}
