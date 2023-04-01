package com.example.demo.AST.Expression;

import com.example.demo.AST.RestricWord.Direction;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;
import com.example.demo.GameState.Region;

public class Opponent implements Expression{
    public Opponent() {};

    public double eval(Player player) throws EvalError {
        return player.opponent();
    }

    public void prettyPrint(StringBuilder s) {
        s.append("opponent");
    }
}
