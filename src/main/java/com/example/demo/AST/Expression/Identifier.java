package com.example.demo.AST.Expression;

import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public class Identifier implements Expression {
    private String name;

    public Identifier(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public double eval(Player player) throws EvalError {
        if(name.equals("rows")) return player.getTerritory().getRows();
        else if(name.equals("cols")) return player.getTerritory().getCols();
        else if(name.equals("currow")) return player.getCurCityCrewPos()[0];
        else if(name.equals("curcol")) return player.getCurCityCrewPos()[1];
        else if(name.equals("budget")) return Math.floor(player.getBudget());
        else if(name.equals("deposit")){
            double deposit = Math.floor(player.getCurCrewReg().getDep());
            if(player.getCurCrewReg().getOwner()!=null&&player.getCurCrewReg().getOwner().equals(player)) return deposit;
            else return (-1)*deposit;
        }
        else if(name.equals("int")) return Math.round(player.getCurCrewReg().getInt());
        else if(name.equals("maxdeposit")) return player.getTerritory().getMaxDep();
        else if(name.equals("random")) return Math.floor(Math.random()*1000);

        if(!player.variables.containsKey(name)) return 0;
        return player.variables.get(name);
    }

    public void prettyPrint(StringBuilder s) {
        s.append(name);
    }
}
