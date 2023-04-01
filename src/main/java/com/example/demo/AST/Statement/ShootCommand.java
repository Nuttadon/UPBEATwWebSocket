package com.example.demo.AST.Statement;

import com.example.demo.AST.RestricWord.Direction;
import com.example.demo.AST.Expression.Expression;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public class ShootCommand implements Statement {
    private Direction direction;
    private Expression expression;

    public ShootCommand(Direction direction, Expression expression){
        this.direction = direction;
        this.expression = expression;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("shoot ");
        s.append(this.direction);
        s.append(" ");
        this.expression.prettyPrint(s);
    }

    public boolean eval(Player player) throws EvalError {
        player.shoot(expression.eval(player),direction);
        return true;
    }
}
