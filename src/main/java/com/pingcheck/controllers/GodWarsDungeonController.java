package com.pingcheck.controllers;

public class GodWarsDungeonController extends PingCheckController {

    @Override
    protected String[] getHiddenMenuOptions() {
        return new String[]{"Crawl-through"};
    }
}
