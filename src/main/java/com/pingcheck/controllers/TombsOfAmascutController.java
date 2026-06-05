package com.pingcheck.controllers;

public class TombsOfAmascutController extends PingCheckController
{

	@Override
	protected String[] getHiddenMenuOptions()
	{
		return new String[]{"Enter", "Inspect"};
	}
}
