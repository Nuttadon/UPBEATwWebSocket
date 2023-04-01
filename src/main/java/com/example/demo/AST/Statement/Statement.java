package com.example.demo.AST.Statement;

import com.example.demo.AST.Node;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public interface Statement extends Node {
    boolean eval(Player player) throws EvalError;
}
