package com.company.Frontend;

public class UserAction {
    private int x;
    private int y;
    private int newValue;
    private int oldValue;

    public UserAction(int x, int y, int newValue, int oldValue){
        this.x = x;
        this.y = y;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNewValue() {
        return newValue;
    }

    public int getOldValue() {
        return oldValue;
    }
}
