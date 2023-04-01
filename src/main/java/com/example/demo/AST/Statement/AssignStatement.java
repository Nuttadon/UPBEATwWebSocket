package com.example.demo.AST.Statement;

import com.example.demo.AST.Expression.Expression;
import com.example.demo.AST.Expression.Identifier;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public class AssignStatement implements Statement {
    private static final String[] specialVar = {"rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random"};
    private Identifier variable;
    private Expression expression;

    public AssignStatement(Identifier variable, Expression expression){
        this.variable = variable;
        this.expression = expression;
    }

    public void prettyPrint(StringBuilder s) {
        this.variable.prettyPrint(s);
        s.append(" = ");
        this.expression.prettyPrint(s);
    }

    public boolean eval(Player player) throws EvalError {
        String var = this.variable.getName();
        for(String sv : specialVar){
            if(var.equals(sv)) return true;
        }
        player.variables.put(var, this.expression.eval(player));
        return true;
    }
}
