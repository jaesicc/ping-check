package com.pingcheck.controllers;

public class VardorvisController extends PingCheckController
{

	@Override
	protected String[] getHiddenMenuOptions()
	{
		return new String[]{"Climb-over"};
	}
}
