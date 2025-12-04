package com.company.Backend;

public class Move {
    private int x;
    private int y;
    private int newValue;
    private int oldValue;

    public Move(int x, int y, int newValue, int oldValue) {
        this.x = x;
        this.y = y;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public String serialize(){
        return "(" + x + "," + y + "," + newValue + "," + oldValue + ")";
    }
    public static Move deserialize(String line){
        line = line.substring(1, line.length() - 1);
        String[] parts = line.split(",");
        return new Move(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3])
        );
    }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getNewValue(){ return newValue; }
    public int getOldValue(){ return oldValue; }
}
