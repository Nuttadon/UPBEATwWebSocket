package com.example.demo.AST.Expression;

import com.example.demo.GameState.Player;

public class Number implements Expression {
    private final long valLong;

    public Number(long val){
        this.valLong = val;
    }

    public double eval(Player player) {
        return valLong;
    }

    public void prettyPrint(StringBuilder s) {
        s.append(valLong);
    }
}
