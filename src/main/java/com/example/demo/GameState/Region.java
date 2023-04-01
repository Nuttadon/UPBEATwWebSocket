package com.example.demo.GameState;


public class Region  {
    private Player owner;
    private Player cityCenterOf ;
    private double curDeposit;
    private double maxDeposit;
    private int colpos;
    private int rowpos;

    public Region(double maxDep,int x,int y) {
        owner = null;
        cityCenterOf = null;
        curDeposit = 0;
        this.maxDeposit = maxDep;
        colpos=x;
        rowpos=y;
    }
    public void conquer(Player p) {
        owner = p;
    }
    public void freeOwner() {
        if(owner!=null) owner=null;
    }
    public void freeCityCenter() {
        if(cityCenterOf!=null) cityCenterOf=null;
    }
    public double getInt(){return 0;}
    public void setAsCityCenter() {
        if(owner!=null) cityCenterOf = owner;
    }
    public void deposit(double i) {
        if(curDeposit+i<= maxDeposit) curDeposit += i;
        else curDeposit = maxDeposit;
    }
    public void withdrawn(double i) {
        if(curDeposit-i>=0) curDeposit -= i;
        else curDeposit = 0;
    }
    public double getDep() {
        double d ;
        if(owner!=null) d = curDeposit;
        else d = (-1)*curDeposit;
        return d;
    }
    public Player getOwner() {
        Player p = owner;
        return p;
    }
    public int getColPos() {
        int x = colpos;
        return x;
    }
    public int getRowPos() {
        int y = rowpos;
        return y;
    }
    public void setDep(int amount){
        curDeposit = amount;
    }
    public Player getCityCenterOwner(){
        return cityCenterOf;
    }
}
