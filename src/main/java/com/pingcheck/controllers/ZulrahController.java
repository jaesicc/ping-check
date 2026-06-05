package com.pingcheck.controllers;

public class ZulrahController extends PingCheckController
{

	@Override
	protected String[] getHiddenMenuOptions()
	{
		return new String[]{"Board", "Quick-Board"};
	}
}
