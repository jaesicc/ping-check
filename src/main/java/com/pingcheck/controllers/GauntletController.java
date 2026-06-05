package com.pingcheck.controllers;

public class GauntletController extends PingCheckController {

    @Override
    protected String[] getHiddenMenuOptions() {
        return new String[]{"Enter", "Enter-corrupted"};
    }
}
