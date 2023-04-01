package com.example.demo.AST.Statement;

import com.example.demo.AST.Expression.Expression;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public class IfStatement implements Statement {
    private Expression condition;
    private Statement then,Else;

    public IfStatement(Expression condition, Statement then, Statement Else){
        this.condition = condition;
        this.then = then;
        this.Else = Else;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("if (");
        this.condition.prettyPrint(s);
        s.append(") then ");
        this.then.prettyPrint(s);
        s.append(" else ");
        this.Else.prettyPrint(s);
    }

    public boolean eval(Player player) throws EvalError {
        if(this.condition.eval(player) > 0){
            if(!this.then.eval(player)) return false;
        }else{
            if(!this.Else.eval(player)) return false;
        }
        return true;
    }
}
