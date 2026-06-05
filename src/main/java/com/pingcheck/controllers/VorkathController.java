package com.pingcheck.controllers;

public class VorkathController extends PingCheckController
{

	@Override
	protected String[] getHiddenMenuOptions()
	{
		return new String[]{"Climb-over"};
	}
}
