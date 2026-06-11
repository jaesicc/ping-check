package com.pingcheck;

import java.awt.Color;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(PingCheckConfig.GROUP)
public interface PingCheckConfig extends Config
{
	String GROUP = "pingcheck";
	// Sections
	@ConfigSection(
		name = "General",
		description = "General settings",
		position = 0
	)
	String generalSection = "general";

	@ConfigSection(
		name = "Places Enabled",
		description = "Where this plugin is enabled",
		position = 1,
		closedByDefault = true
	)
	String placesEnabled = "placesEnabled";

	// Items
	@Range(
		max = 300
	)
	@ConfigItem(
		keyName = "pingThreshold",
		name = "Ping Threshold",
		description = "The maximum ping you want to have without triggering this plugin",
		section = generalSection,
		position = 0
	)
	@Units(Units.MILLISECONDS)
	default int pingThreshold()
	{
		return 100;
	}

	@ConfigItem(
		name = "Hide entry menu options",
		description = "When enabled, hide menu options for entering content",
		position = 1,
		keyName = "hideMenuOptions",
		section = generalSection
	)
	default boolean isHideMenuOptions()
	{
		return true;
	}

	@ConfigItem(
		keyName = "isScreenFlashingEnabled",
		name = "Flash whole screen when enabled",
		description = "Flashes a text box that alerts you to check your ping",
		position = 2,
		section = generalSection
	)
	default boolean isScreenFlashingEnabled()
	{
		return false;
	}

	@ConfigItem(
		keyName = "isTextBoxEnabled",
		name = "Text Overlay Alert Enabled",
		description = "Flashes a text box that alerts you to check your ping",
		position = 3,
		section = generalSection
	)
	default boolean isTextBoxEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "textColor",
		name = "Text Color",
		description = "The color of the blinking text alert",
		position = 4,
		section = generalSection
	)
	default Color textColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
		keyName = "flashColor",
		name = "Flash Color",
		description = "The color any enabled alerts will flash in",
		position = 5,
		section = generalSection
	)
	default Color flashColor()
	{
		return new Color(255, 0, 0, 50);
	}

	@ConfigItem(
		name = "Gauntlet",
		description = "Enable plugin within the Gauntlet",
		position = 0,
		keyName = "gauntlet",
		section = placesEnabled
	)
	default boolean isGauntletEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Tombs of Amascut",
		description = "Enable plugin within Tombs of Amascut",
		position = 1,
		keyName = "toa",
		section = placesEnabled
	)
	default boolean isTombsOfAmascutEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Theatre of Blood",
		description = "Enable plugin within Theatre of Blood",
		position = 2,
		keyName = "tob",
		section = placesEnabled
	)
	default boolean isTheatreOfBloodEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Chambers of Xeric",
		description = "Enable plugin within Chambers of Xeric",
		position = 3,
		keyName = "chambersOfXeric",
		section = placesEnabled
	)
	default boolean isChambersOfXericEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Vorkath",
		description = "Enable plugin within Vorkath",
		position = 4,
		keyName = "vorkath",
		section = placesEnabled
	)
	default boolean isVorkathEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Zulrah",
		description = "Enable plugin within Zulrah",
		position = 5,
		keyName = "zulrah",
		section = placesEnabled
	)
	default boolean isZulrahEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "God Wars Dungeon",
		description = "Get alerted while in Trollheim entrance or in main dungeon room",
		position = 6,
		keyName = "godWarsDungeon",
		section = placesEnabled
	)
	default boolean isGodWarsDungeonEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Vardorvis",
		description = "Get alerted right outside of Vardorvis",
		position = 7,
		keyName = "vardorvis",
		section = placesEnabled
	)
	default boolean isVardorvisEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "Duke Sucellus",
		description = "Get alerted right outside of Duke Sucellus",
		position = 8,
		keyName = "dukeSucellus",
		section = placesEnabled
	)
	default boolean isDukeSucellusEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "The Whisperer",
		description = "Enable plugin within The Whisperer",
		position = 9,
		keyName = "whisperer",
		section = placesEnabled
	)
	default boolean isWhispererEnabled()
	{
		return true;
	}

	@ConfigItem(
		name = "The Leviathan",
		description = "Enable plugin within The Leviathan",
		position = 10,
		keyName = "leviathan",
		section = placesEnabled
	)
	default boolean isLeviathanEnabled()
	{
		return true;
	}

}
