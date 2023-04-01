package com.example.demo.AST.Statement;

import com.example.demo.AST.RestricWord.Command;
import com.example.demo.AST.Expression.Expression;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public class RegionCommand implements Statement {
    private Command action;
    private Expression expression;

    public RegionCommand(Command command, Expression expression){
        if(command.equals(Command.invest) || command.equals(Command.collect)){
            this.action = command;
            this.expression = expression;
        }
    }

    public void prettyPrint(StringBuilder s) {
        s.append(this.action);
        s.append(" ");
        this.expression.prettyPrint(s);
    }

    public boolean eval(Player player) throws EvalError {
        if(action.equals(Command.invest)){ // invest command
            return player.invest(expression.eval(player));
        }else{ // collect command
            return player.collect(expression.eval(player));
        }
    }
}
