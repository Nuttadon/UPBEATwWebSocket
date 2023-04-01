package com.example.demo.AST.Statement;

import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

import java.util.LinkedList;

public class BlockStatement implements Statement {
    private LinkedList<Statement> statementList;

    public BlockStatement(LinkedList<Statement> list){
        this.statementList = list;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("{");
        for (Statement statement : this.statementList){
            s.append("\n");
            statement.prettyPrint(s);
        }
        if(!this.statementList.isEmpty()) s.append("\n");
        s.append("}");
    }

    public boolean eval(Player player) throws EvalError {
        for (Statement statement : this.statementList){
            if(!statement.eval(player)) return false;
        }
        return true;
    }
}
