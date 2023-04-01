package com.example.demo.AST.Statement;

import com.example.demo.AST.RestricWord.Direction;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;
import com.example.demo.GameState.Region;

public class MoveCommand implements Statement {
    private Direction direction;

    public MoveCommand(Direction direction){
        this.direction = direction;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("move ");
        s.append(this.direction);
    }

    public boolean eval(Player player) throws EvalError {
        return player.move(direction);
    }
}
