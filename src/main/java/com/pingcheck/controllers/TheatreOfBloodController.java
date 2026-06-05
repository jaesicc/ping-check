package com.pingcheck.controllers;

public class TheatreOfBloodController extends PingCheckController {

    @Override
    protected String[] getHiddenMenuOptions() {
        return new String[]{ "Read", "Enter" };
    }
}
