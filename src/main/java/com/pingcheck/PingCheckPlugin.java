package com.pingcheck;

import com.google.inject.Provides;

import com.pingcheck.controllers.PingCheckController;
import com.pingcheck.controllers.PingCheckControllerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.pingcheck.alerts.AlertScreenFlashOverlay;
import com.pingcheck.alerts.AlertTextOverlay;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostMenuSort;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.worldhopper.ping.Ping;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.http.api.worlds.World;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;


@Slf4j
@PluginDescriptor(
	name = "Ping Check",
	description = "Make sure your ping isn't too high when entering a PvM encounter",
	tags = {"ping", "check"}
)
public class PingCheckPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private WorldService worldService;

	@Nullable
	private PingCheckController controller = null;

	private boolean shouldEvaluate = false;
	private Set<Integer> validRegions;
	private Map<Integer, Supplier<Boolean>> regionToEnabledFlagMap;

	@Inject
	private PingCheckConfig config;

	@Inject
	private PingCheckControllerFactory controllerFactory;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AlertScreenFlashOverlay screenFlashOverlay;

	@Inject
	private AlertTextOverlay textOverlay;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Ping Check started!");
		Integer[] regionArr = {
			PingCheckControllerFactory.TOA_LOBBY_REGION_ID,
			PingCheckControllerFactory.GAUNTLET_LOBBY_REGION_ID,
			PingCheckControllerFactory.TOB_LOBBY_REGION_ID,
			PingCheckControllerFactory.VORKATH_REGION_ID,
			PingCheckControllerFactory.ZULRAH_REGION_ID,
			PingCheckControllerFactory.ZULRAH_REGION_ID_2,
			PingCheckControllerFactory.ZULRAH_REGION_ID_3,
			PingCheckControllerFactory.CHAMBERS_OF_XERIC_REGION_ID,
			PingCheckControllerFactory.GWD_ENTRANCE_REGION_ID,
			PingCheckControllerFactory.GWD_SARADOMIN_REGION_ID,
			PingCheckControllerFactory.VARDORVIS_REGION_ID,
			PingCheckControllerFactory.DUKE_SUCELLUS_REGION_ID,
			PingCheckControllerFactory.WHISPERER_INSTANCED_REGION_ID,
			PingCheckControllerFactory.WHISPERER_RIGHT_OUTSIDE_INSTANCED_REGION_ID,
			PingCheckControllerFactory.WHISPERER_PALACE_INSTANCED_REGION_ID_2,
			PingCheckControllerFactory.LEVIATHAN_REGION_ID
		};
		validRegions = new HashSet<>(Arrays.asList(regionArr));
		regionToEnabledFlagMap = new HashMap<>();
		regionToEnabledFlagMap.put(PingCheckControllerFactory.TOA_LOBBY_REGION_ID, () -> config.isTombsOfAmascutEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.GAUNTLET_LOBBY_REGION_ID, () -> config.isGauntletEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.TOB_LOBBY_REGION_ID, () -> config.isTheatreOfBloodEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.VORKATH_REGION_ID, () -> config.isVorkathEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.ZULRAH_REGION_ID, () -> config.isZulrahEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.ZULRAH_REGION_ID_2, () -> config.isZulrahEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.ZULRAH_REGION_ID_3, () -> config.isZulrahEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.CHAMBERS_OF_XERIC_REGION_ID, () -> config.isChambersOfXericEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.GWD_ENTRANCE_REGION_ID, () -> config.isGodWarsDungeonEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.GWD_SARADOMIN_REGION_ID, () -> config.isGodWarsDungeonEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.VARDORVIS_REGION_ID, () -> config.isVardorvisEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.DUKE_SUCELLUS_REGION_ID, () -> config.isDukeSucellusEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.WHISPERER_INSTANCED_REGION_ID, () -> config.isWhispererEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.WHISPERER_RIGHT_OUTSIDE_INSTANCED_REGION_ID, () -> config.isWhispererEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.WHISPERER_PALACE_INSTANCED_REGION_ID_2, () -> config.isWhispererEnabled());
		regionToEnabledFlagMap.put(PingCheckControllerFactory.LEVIATHAN_REGION_ID, () -> config.isLeviathanEnabled());
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Ping Check stopped!");
		reset();
	}


	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			// Don't read region here! Just schedule a check for the next tick.
			shouldEvaluate = true;
			reset();
		}
	}

	@Subscribe
	protected void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(PingCheckConfig.GROUP))
		{
			return;
		}

		shouldEvaluate = true;
		reset();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (shouldEvaluate)
		{
			shouldEvaluate = false;

			// The player object and scene are now fully loaded and valid
			if (client.getLocalPlayer() != null)
			{
				int regionId = getCurrentRegionId();
				if (
					validRegions.contains(regionId) &&
						regionToEnabledFlagMap.containsKey(regionId) &&
						regionToEnabledFlagMap.get(regionId).get()
				)
				{
					executor.execute(() -> {
						int currentWorldId = client.getWorld();

						// Find world metadata to get the actual server host address
						World currentWorld = Objects.requireNonNull(worldService.getWorlds()).findWorld(currentWorldId);
						if (currentWorld == null)
						{
							return;
						}

						// Ping the current world server safely
						int pingResult = Ping.ping(currentWorld, false);
						controller = controllerFactory.create(regionId, pingResult);
						if (controller != null && controller.isHighPing())
						{
							controller.alert();
						}
					});
				}
				else
				{
					controller = null;
				}
			}
		}
	}

	@Subscribe
	public void onPostMenuSort(final PostMenuSort postMenuSort)
	{
		if (
			client.isMenuOpen() || controller == null || !controller.isHighPing() || !config.isHideMenuOptions()
		)
		{
			return;
		}

		controller.hideMenuEntries();
	}

	@Provides
	PingCheckConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PingCheckConfig.class);
	}

	private void reset()
	{
		controller = null;
		overlayManager.remove(textOverlay);
		overlayManager.remove(screenFlashOverlay);
	}

	private int getCurrentRegionId()
	{
		int regionId = client.getLocalPlayer().getWorldLocation().getRegionID();
		if (validRegions.contains(regionId) || !client.isInInstancedRegion())
		{
			return regionId;
		}

		WorldPoint instancedWorldPoint = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
		int instancedRegionId = instancedWorldPoint.getRegionID();
		if (validRegions.contains(instancedRegionId))
		{
			return instancedRegionId;
		}

		return regionId;
	}
}
