package com.example.demo.AST.Expression;

import com.example.demo.AST.Node;
import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public interface Expression extends Node {
    double eval(Player player) throws EvalError;
}
