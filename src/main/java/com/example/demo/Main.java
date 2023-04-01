package com.example.demo;

import com.example.demo.AST.Plan;
import com.example.demo.AST.RestricWord.Direction;
import com.example.demo.AST.Statement.*;
import com.example.demo.Exception.EvalError;
import com.example.demo.Exception.SyntaxError;
import com.example.demo.GameState.Player;
import com.example.demo.GameState.Region;
import com.example.demo.GameState.Territory;
import com.example.demo.Parser.StatementParser;
import com.example.demo.Tokenizer.Tokenizer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws EvalError, SyntaxError, IOException {
        Territory t = new Territory();
        t.players[0].initPlan("move up move up x1 = currow y1 = curcol");
        t.players[0].startPlan();
        System.out.println(t.players[0].variables.get("x1"));
        System.out.println(t.players[0].variables.get("y1"));
        t.players[0].initPlan("move up move up x1 = currow y1 = curcol");
        t.players[0].startPlan();
        System.out.println(t.players[0].variables.get("x1"));
        System.out.println(t.players[0].variables.get("y1"));


    }
}