package com.pingcheck.controllers;

public class DukeSucellusController extends PingCheckController {

    @Override
    protected String[] getHiddenMenuOptions() {
        return new String[]{ "Open" };
    }
}
