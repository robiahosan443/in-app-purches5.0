package com.alphabetlore3d.simsoundboard.p.Models;

public class AppScreen {
    private int index;
    private String screen;
    private String allScreen;

    public AppScreen(int index, String screen) {
        this.index = index;
        this.screen = screen;
        this.allScreen = index + screen;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getAllScreen() {
        return allScreen;
    }

    public void setAllScreen(String allScreen) {
        this.allScreen = allScreen;
    }
}
