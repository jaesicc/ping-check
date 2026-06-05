package com.pingcheck.controllers;

public class ChambersOfXericController extends PingCheckController
{

	@Override
	protected String[] getHiddenMenuOptions()
	{
		return new String[]{"Enter"};
	}
}
