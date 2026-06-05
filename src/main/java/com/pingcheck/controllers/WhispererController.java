package com.pingcheck.controllers;

public class WhispererController extends PingCheckController {

    @Override
    protected String[] getHiddenMenuOptions() {
        return new String[]{"Disturb"};
    }
}
