package com.pingcheck.controllers;

import javax.inject.Inject;
import javax.inject.Provider;

public class PingCheckControllerFactory
{
	public static final int TOA_LOBBY_REGION_ID = 13454;
	public static final int GAUNTLET_LOBBY_REGION_ID = 12127;
	public static final int TOB_LOBBY_REGION_ID = 14642;
	public static final int VORKATH_REGION_ID = 9023;
	public static final int ZULRAH_REGION_ID = 8496;
	public static final int ZULRAH_REGION_ID_2 = 8495;
	public static final int ZULRAH_REGION_ID_3 = 8751;
	public static final int CHAMBERS_OF_XERIC_REGION_ID = 4919;
	public static final int GWD_ENTRANCE_REGION_ID = 11577;
	public static final int GWD_SARADOMIN_REGION_ID = 11602;
	public static final int VARDORVIS_REGION_ID = 4405;
	public static final int DUKE_SUCELLUS_REGION_ID = 12132;
	public static final int WHISPERER_INSTANCED_REGION_ID = 10595;
	public static final int WHISPERER_RIGHT_OUTSIDE_INSTANCED_REGION_ID = 10596;
	public static final int WHISPERER_PALACE_INSTANCED_REGION_ID_2 = 10340;
	public static final int LEVIATHAN_REGION_ID = 8292;

	private final Provider<TombsOfAmascutController> tombsOfAmascutControllerProvider;
	private final Provider<GauntletController> gauntletControllerProvider;
	private final Provider<TheatreOfBloodController> theatreOfBloodControllerProvider;
	private final Provider<VorkathController> vorkathControllerProvider;
	private final Provider<ZulrahController> zulrahControllerProvider;
	private final Provider<ChambersOfXericController> chambersOfXericControllerProvider;
	private final Provider<GodWarsDungeonController> godWarsDungeonControllerProvider;
	private final Provider<VardorvisController> vardorvisControllerProvider;
	private final Provider<DukeSucellusController> dukeSucellusControllerProvider;
	private final Provider<WhispererController> whispererControllerProvider;
	private final Provider<LeviathanController> leviathanControllerProvider;

	@Inject
	private PingCheckControllerFactory(
		Provider<TombsOfAmascutController> tombsOfAmascutControllerProvider,
		Provider<GauntletController> gauntletControllerProvider,
		Provider<TheatreOfBloodController> theatreOfBloodControllerProvider,
		Provider<VorkathController> vorkathControllerProvider,
		Provider<ZulrahController> zulrahControllerProvider,
		Provider<ChambersOfXericController> chambersOfXericControllerProvider,
		Provider<GodWarsDungeonController> godWarsDungeonControllerProvider,
		Provider<VardorvisController> vardorvisControllerProvider,
		Provider<DukeSucellusController> dukeSucellusControllerProvider,
		Provider<WhispererController> whispererControllerProvider,
		Provider<LeviathanController> leviathanControllerProvider
	)
	{
		this.tombsOfAmascutControllerProvider = tombsOfAmascutControllerProvider;
		this.gauntletControllerProvider = gauntletControllerProvider;
		this.theatreOfBloodControllerProvider = theatreOfBloodControllerProvider;
		this.vorkathControllerProvider = vorkathControllerProvider;
		this.zulrahControllerProvider = zulrahControllerProvider;
		this.chambersOfXericControllerProvider = chambersOfXericControllerProvider;
		this.godWarsDungeonControllerProvider = godWarsDungeonControllerProvider;
		this.vardorvisControllerProvider = vardorvisControllerProvider;
		this.dukeSucellusControllerProvider = dukeSucellusControllerProvider;
		this.whispererControllerProvider = whispererControllerProvider;
		this.leviathanControllerProvider = leviathanControllerProvider;
	}

	public PingCheckController create(int regionId, int ping)
	{
		PingCheckController controller;

		switch (regionId)
		{
			case TOA_LOBBY_REGION_ID:
				controller = tombsOfAmascutControllerProvider.get();
				break;
			case GAUNTLET_LOBBY_REGION_ID:
				controller = gauntletControllerProvider.get();
				break;
			case TOB_LOBBY_REGION_ID:
				controller = theatreOfBloodControllerProvider.get();
				break;
			case VORKATH_REGION_ID:
				controller = vorkathControllerProvider.get();
				break;
			case ZULRAH_REGION_ID:
			case ZULRAH_REGION_ID_2:
			case ZULRAH_REGION_ID_3:
				controller = zulrahControllerProvider.get();
				break;
			case CHAMBERS_OF_XERIC_REGION_ID:
				controller = chambersOfXericControllerProvider.get();
				break;
			case GWD_ENTRANCE_REGION_ID:
			case GWD_SARADOMIN_REGION_ID:
				controller = godWarsDungeonControllerProvider.get();
				break;
			case VARDORVIS_REGION_ID:
				controller = vardorvisControllerProvider.get();
				break;
			case DUKE_SUCELLUS_REGION_ID:
				controller = dukeSucellusControllerProvider.get();
				break;
			case WHISPERER_INSTANCED_REGION_ID:
			case WHISPERER_RIGHT_OUTSIDE_INSTANCED_REGION_ID:
			case WHISPERER_PALACE_INSTANCED_REGION_ID_2:
				controller = whispererControllerProvider.get();
				break;
			case LEVIATHAN_REGION_ID:
				controller = leviathanControllerProvider.get();
				break;
			default:
				return null;
		}

		controller.setPing(ping);
		return controller;
	}
}
