package com.example.demo.AST;

import com.example.demo.AST.Statement.Statement;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

import java.util.LinkedList;


public class Plan implements Node{
    private LinkedList<Statement> statementList;

    public Plan(LinkedList<Statement> list){
        this.statementList = list;
    }

    public void prettyPrint(StringBuilder s) {
        for (Statement statement : this.statementList){
            statement.prettyPrint(s);
            s.append("\n");
        }
    }

    public void eval(Player player) throws EvalError {
        for (Statement statement : this.statementList){
            if(!statement.eval(player)) return;
        }
    }
}
