package com.example.demo.AST.Expression;

import com.example.demo.Exception.EvalError;
import com.example.demo.GameState.Player;

public class BinaryArithExpr implements Expression {
    private Expression left,right;
    private String op;
    public BinaryArithExpr(Expression left, String op, Expression right){
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public void prettyPrint(StringBuilder s) {
        s.append("(");
        this.left.prettyPrint(s);
        s.append(this.op);
        this.right.prettyPrint(s);
        s.append(")");
    }

    public double eval(Player player) throws EvalError {
        double lValue = left.eval(player);
        double rValue = right.eval(player);
        if(op.equals("+")) return lValue + rValue;
        else if(op.equals("-")) return lValue - rValue;
        else if(op.equals("*")) return lValue * rValue;
        else if(op.equals("/")){
            if (rValue == 0) throw new ArithmeticException("/ by zero");;
            return Math.floor(lValue/rValue);
        }
        else if(op.equals("%")){
            if (rValue == 0) throw new ArithmeticException("% by zero");
            return lValue % rValue;
        }else if(op.equals("^")){
            if (rValue == 0 && lValue == 0) throw new ArithmeticException("0^0 undefined");
            return Math.floor(Math.pow(lValue,rValue));
        }
        throw new EvalError("unknown op -> " + op);
    }
}
