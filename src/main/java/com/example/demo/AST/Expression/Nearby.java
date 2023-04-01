package com.example.demo.AST.Expression;

import com.example.demo.AST.RestricWord.Direction;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;
import com.example.demo.GameState.Region;

public class Nearby implements Expression{
    private Direction direction;

    public Nearby(Direction direction){
        this.direction = direction;
    }

    public double eval(Player player) throws EvalError {
        return  player.nearby(direction);
    }

    public void prettyPrint(StringBuilder s) {
        s.append("nearby ");
        s.append(this.direction);
    }
}
