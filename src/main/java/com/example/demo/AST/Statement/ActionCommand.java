package com.example.demo.AST.Statement;

import com.example.demo.AST.RestricWord.Command;
import com.example.demo.GameState.Player;

public class ActionCommand implements Statement {
    private Command action;

    public ActionCommand(Command command){
        if(command.equals(Command.done) || command.equals(Command.relocate)) this.action = command;
    }
    public void prettyPrint(StringBuilder s) {
        s.append(this.action);
    }

    public boolean eval(Player player){
        if(action.equals(Command.relocate)){ // relocate command
            int ccx = player.getCityCenter().getRowPos(); //CityCenterX
            int ccy = player.getCityCenter().getColPos(); //CityCenterY
            int newCCX = player.getCurCityCrewPos()[0];
            int newCCY = player.getCurCityCrewPos()[1];
            int r = ccx - (ccy - ccy % 2) / 2;
            int newR = newCCX - (newCCY - newCCY % 2) / 2;
            int minDistance = (Math.abs(newCCY - ccy) + Math.abs(newR - r) + Math.abs((-newCCY-newR) + (ccy + r))) / 2;
            double cost = 5 * minDistance + 10;
            player.relocate(cost);
        }
        return false;
    }
}
