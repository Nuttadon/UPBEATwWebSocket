package com.example.demo.GameState;

import com.example.demo.AST.Plan;
import com.example.demo.AST.RestricWord.Direction;
import com.example.demo.Exception.*;
import com.example.demo.Parser.StatementParser;
import com.example.demo.Tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Player {
    private String name;
    private Territory territory;
    private Plan construction_Plan;
    public  Map<String, Double> variables = new HashMap<>();
    public Region curCityCenter;
    private double budget;
    private int intPlanMin;
    private int intPlanSec;
    private int planRevMin;
    private int planRevSec;
    private int revCost;
    private int initCenterDep;
    private int[] curCityCenterPos = new int[2];//0 row 1 col
    private int[] curCityCrewPos = new int[2];

    public Player(String name,Territory ter){
        Path file = Paths.get("configFile.txt");
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if(line.contains("init_plan_min")){
                    line = line.replace("init_plan_min=","");
                    intPlanMin = Integer.parseInt(line);
                }if(line.contains("init_plan_sec")) {
                    line = line.replace("init_plan_sec=","");
                    intPlanSec = Integer.parseInt(line);
                }if(line.contains("init_budget")) {
                    line = line.replace("init_budget=","");
                    budget = Integer.parseInt(line);
                }if(line.contains("init_center_dep")) {
                    line = line.replace("init_center_dep=","");
                    initCenterDep = Integer.parseInt(line);
                }if(line.contains("plan_rev_min")) {
                    line = line.replace("plan_rev_min=","");
                    planRevMin = Integer.parseInt(line);
                }if(line.contains("plan_rev_sec")) {
                    line = line.replace("plan_rev_sec=","");
                    planRevSec = Integer.parseInt(line);
                }if(line.contains("rev_cost")) {
                    line = line.replace("rev_cost=","");
                    revCost = Integer.parseInt(line);
                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        this.name = name;
        this.territory = ter;
        Region[][] r = ter.getRegions();
        do{
//            curCityCenterPos[0] = (int) (((Math.random()*100)%t.getRows())+1);
            if(name.equals("Player1"))curCityCenterPos[0] = (int) (3+1);
            else curCityCenterPos[0] = (int) (1+1);
//            curCityCenterPos[1] = (int) (((Math.random()*100)%t.getCols())+1);
            if(name.equals("Player1"))curCityCenterPos[1] = (int) (4+1);
            else curCityCenterPos[1] = (int) (4+1);
            curCityCenter = r[curCityCenterPos[0]-1][curCityCenterPos[1]-1];
        }while(curCityCenter.getOwner()!=null);
        r[curCityCenterPos[0]-1][curCityCenterPos[1]-1].conquer(this);
        r[curCityCenterPos[0]-1][curCityCenterPos[1]-1].setAsCityCenter();
        r[curCityCenterPos[0]-1][curCityCenterPos[1]-1].deposit(initCenterDep);
        curCityCrewPos[0] = curCityCenterPos[0];
        curCityCrewPos[1] = curCityCenterPos[1];
    }
    public Territory getTerritory(){
        return territory;
    }

    public Region getCityCenter(){
        return curCityCenter;
    }

    public double getBudget(){return budget;}

    public int[] getCurCityCrewPos(){
        return curCityCrewPos;
    }

    public Region getCurCrewReg(){
        Region[][] r = territory.getRegions();
        return r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
    }
    private void readPlan() throws IOException, SyntaxError {
        StringBuilder sb = new StringBuilder();
        String player ;
        if(name.equals("Player1")) player = "player1Plan.txt";
        else player = "player2Plan.txt";
        Path file = Paths.get(player);
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(" ");
            }
        }
        Tokenizer t = new Tokenizer(sb.toString());
        StatementParser p = new StatementParser(t);
        this.construction_Plan = (Plan) p.parse();
    }

    public void startPlan() throws EvalError, SyntaxError, IOException {
        curCityCrewPos[0] = curCityCenterPos[0];
        curCityCrewPos[1] = curCityCenterPos[1];
        readPlan();
        StringBuilder s = new StringBuilder();
        construction_Plan.prettyPrint(s);
        System.out.println(s.toString());
        construction_Plan.eval(this);
    }

    public void initPlan(String s){
        String player ;
        if(name.equals("Player1")) player = "player1Plan.txt";
        else player = "player2Plan.txt";
        Path file2 = Paths.get(player);  // path string
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(file2, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public void rePlan(String s){
        if(budget-revCost>=0){
            String player ;
            if(name.equals("Player1")) player = "player1Plan.txt";
            else player = "player2Plan.txt";
            Path file2 = Paths.get(player);  // path string
            Charset charset = Charset.forName("UTF-8");
            try (BufferedWriter writer = Files.newBufferedWriter(file2, charset)) {
                writer.write(s, 0, s.length());
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
            budget-=revCost;
        }
    }
    public int opponent() {
        Region[][] r = territory.getRegions() ;
        int distance = 0;
        int direction = 0;
        StringBuilder sb = new StringBuilder();
        int crewPos0 = curCityCrewPos[0];
        int crewPos1 = curCityCrewPos[1];
        int dis = 0;
        //upleft
        while(true){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                if(distance<=dis) {
                    distance = dis;
                    dis=0;
                    direction=6;
                }
                break;
            }
            if(curCityCrewPos[0]==1||curCityCrewPos[1]==1) break;
            moveNoCost(Direction.upleft);
            dis++;
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        dis =0;
        //downleft
        while(true){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                if(distance<=dis) {
                    distance = dis;
                    dis=0;
                    direction=5;
                }
                break;
            }
            if(curCityCrewPos[0]==territory.getRows()||curCityCrewPos[1]==1) break;
            moveNoCost(Direction.downleft);
            dis++;
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        dis =0;
        //down
        while(true){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                if(distance<=dis) {
                    distance = dis;
                    dis=0;
                    direction=4;
                }
                break;
            }
            if(curCityCrewPos[0]==territory.getRows()) break;
            moveNoCost(Direction.down);
            dis++;
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        dis =0;
        //downright
        while(true){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                if(distance<=dis) {
                    distance = dis;
                    dis=0;
                    direction=3;
                }
                break;
            }
            if(curCityCrewPos[0]==territory.getRows()||curCityCrewPos[1]==territory.getCols()) break;
            moveNoCost(Direction.downright);
            dis++;
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        dis =0;
        //upright
        while(true){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                if(distance<=dis) {
                    distance = dis;
                    dis=0;
                    direction=2;
                }
                break;
            }
            if(curCityCrewPos[0]==1||curCityCrewPos[1]==territory.getCols()) break;
            moveNoCost(Direction.upright);
            dis++;
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        dis =0;
        //up
        while(true){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                distance = dis;
                dis =0;
                direction=1;
                break;
            }
            if(curCityCrewPos[0]==1) break;
            moveNoCost(Direction.up);
            dis++;
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        String sDistance = Integer.toString(distance) ;
        String sDirection = Integer.toString(direction) ;
        sb.append(sDistance);
        sb.append(sDirection);
        return Integer.parseInt(sb.toString());
    }
    public int nearby(Direction direction) {
        Region[][] r = territory.getRegions() ;
        int distance = 0;
        Region regionFound = null;
        int crewPos0 = curCityCrewPos[0];
        int crewPos1 = curCityCrewPos[1];
        //up
        int dis = 0;
        if(direction.equals(Direction.up)){
            while(true){
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                    distance = dis;
                    dis =0;
                    regionFound = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                    break;
                }
                if(curCityCrewPos[0]==1) break;
                moveNoCost(Direction.up);
                dis++;
            }
        }else if(direction.equals(Direction.upright)){
            while(true){
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                    distance = dis;
                    dis=0;
                    regionFound = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                    break;
                }
                if(curCityCrewPos[0]==1||curCityCrewPos[1]==territory.getCols()) break;
                moveNoCost(Direction.upright);
                dis++;
            }
        }else if(direction.equals(Direction.downright)){
            while(true){
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                    distance = dis;
                    dis=0;
                    regionFound = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                    break;
                }
                if(curCityCrewPos[0]==territory.getRows()||curCityCrewPos[1]==territory.getCols()) break;
                moveNoCost(Direction.downright);
                dis++;
            }
        }else if(direction.equals(Direction.down)){
            while(true){
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                    distance = dis;
                    dis=0;
                    regionFound = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                    break;
                }
                if(curCityCrewPos[0]==territory.getRows()) break;
                moveNoCost(Direction.down);
                dis++;
            }
        }else if(direction.equals(Direction.downleft)){
            while(true){
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                    distance = dis;
                    dis=0;
                    regionFound = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                    break;
                }
                if(curCityCrewPos[0]==territory.getRows()||curCityCrewPos[1]==1) break;
                moveNoCost(Direction.downleft);
                dis++;
            }
        }else{
            while(true){
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&!(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this))){
                    distance = dis;
                    dis=0;
                    regionFound = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                    break;
                }
                if(curCityCrewPos[0]==1||curCityCrewPos[1]==1) break;
                moveNoCost(Direction.upleft);
                dis++;
            }
        }
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        int output;
        if(regionFound==null) output = 0 ;
        else{
            int sDistance = distance*100;
            int y = Integer.toString((int)regionFound.getDep()).length();
            output = sDistance+y;
        }
        return output;
    }

    public boolean move(Direction direction) {
        if(budget-1>=0){
            if(direction.equals(Direction.up)){
                if(curCityCrewPos[0]-1>0){
                    if(budget-1>=0){
                        curCityCrewPos[0]-=1;
                        budget-=1;
                    }
                }
            }
            else if(direction.equals(Direction.upright)){
                if(curCityCrewPos[1]%2==0){
                    if(curCityCrewPos[0]-1>0&&curCityCrewPos[1]+1<=territory.getCols()){
                        if(budget-1>=0){
                            curCityCrewPos[0]-=1;
                            curCityCrewPos[1]+=1;
                            budget-=1;
                        }
                    }
                }else{
                    if(curCityCrewPos[1]+1<=territory.getCols()){
                        if(budget-1>=0){
                            curCityCrewPos[1]+=1;
                            budget-=1;
                        }
                    }
                }
            }
            else if(direction.equals(Direction.downright)){
                if(curCityCrewPos[1]%2==0){
                    if(curCityCrewPos[1]+1<=territory.getCols()){
                        if(budget-1>=0){
                            curCityCrewPos[1]+=1;
                            budget-=1;
                        }
                    }
                }else{
                    if(curCityCrewPos[0]+1<=territory.getRows()&&curCityCrewPos[1]+1<=territory.getCols()){
                        if(budget-1>=0){
                            curCityCrewPos[0]+=1;
                            curCityCrewPos[1]+=1;
                            budget-=1;
                        }
                    }
                }
            }
            else if(direction.equals(Direction.down)){
                if(curCityCenterPos[0]+1<territory.getRows()){
                    if(budget-1>=0){
                        curCityCrewPos[0]+=1;
                        budget-=1;
                    }
                }
            }
            else if(direction.equals(Direction.downleft)){
                if(curCityCrewPos[1]%2==0){
                    if(curCityCrewPos[1]-1>0){
                        if(budget-1>=0){
                            curCityCrewPos[1]-=1;
                            budget-=1;
                        }
                    }
                }else{
                    if(curCityCrewPos[0]+1<=territory.getRows()&&curCityCrewPos[1]-1>0){
                        if(budget-1>=0){
                            curCityCrewPos[0]+=1;
                            curCityCrewPos[1]-=1;
                            budget-=1;
                        }

                    }
                }
            }
            else {
                if(curCityCrewPos[1]%2==0){
                    if(curCityCrewPos[1]-1>0&&curCityCrewPos[0]-1>0){
                        if(budget-1>=0){
                            curCityCrewPos[0]-=1;
                            curCityCrewPos[1]-=1;
                            budget-=1;
                        }
                    }
                }else{
                    if(curCityCrewPos[1]-1>0){
                        if(budget-1>=0){
                            curCityCrewPos[1]-=1;
                            budget-=1;
                        }

                    }
                }
            }
        }else{return false;}
        return true;
    }
    public void shoot(double damage, Direction direction) {
        Region[][] r = territory.getRegions() ;
        int crewPos0 = curCityCrewPos[0];
        int crewPos1 = curCityCrewPos[1];
        int attackLand[] = new int[2];
        if(budget-damage-1<0){
            return;
        }else{
            if(direction.equals(Direction.up)){
                moveNoCost(Direction.up);
                attackLand[0] = curCityCrewPos[0];
                attackLand[1] = curCityCrewPos[1];
                curCityCrewPos[0] = crewPos0;
                curCityCrewPos[1] = crewPos1;
                if(r[attackLand[0]-1][attackLand[1]-1].getDep()-damage<1) {
                    r[attackLand[0]-1][attackLand[1]-1].setDep(0);
                    r[attackLand[0]-1][attackLand[1]-1].freeOwner();
                    if(r[attackLand[0]-1][attackLand[1]-1].getCityCenterOwner()!=null) {
                        r[attackLand[0]-1][attackLand[1]-1].freeCityCenter();
                        //endgame
                    }

                }else{
                    r[attackLand[0]-1][attackLand[1]-1].withdrawn(damage);
                }
            }else if(direction.equals(Direction.upright)){
                moveNoCost(Direction.upright);
                attackLand[0] = curCityCrewPos[0];
                attackLand[1] = curCityCrewPos[1];
                curCityCrewPos[0] = crewPos0;
                curCityCrewPos[1] = crewPos1;
                if(r[attackLand[0]-1][attackLand[1]-1].getDep()-damage<1) {
                    r[attackLand[0]-1][attackLand[1]-1].setDep(0);
                    r[attackLand[0]-1][attackLand[1]-1].freeOwner();
                    if(r[attackLand[0]-1][attackLand[1]-1].getCityCenterOwner()!=null) {
                        r[attackLand[0]-1][attackLand[1]-1].freeCityCenter();
                        //endgame
                    }

                }else{
                    r[attackLand[0]-1][attackLand[1]-1].withdrawn(damage);
                }
            }else if(direction.equals(Direction.downright)){
                moveNoCost(Direction.downright);
                attackLand[0] = curCityCrewPos[0];
                attackLand[1] = curCityCrewPos[1];
                curCityCrewPos[0] = crewPos0;
                curCityCrewPos[1] = crewPos1;
                if(r[attackLand[0]-1][attackLand[1]-1].getDep()-damage<1) {
                    r[attackLand[0]-1][attackLand[1]-1].setDep(0);
                    r[attackLand[0]-1][attackLand[1]-1].freeOwner();
                    if(r[attackLand[0]-1][attackLand[1]-1].getCityCenterOwner()!=null) {
                        r[attackLand[0]-1][attackLand[1]-1].freeCityCenter();
                        //endgame
                    }

                }else{
                    r[attackLand[0]-1][attackLand[1]-1].withdrawn(damage);
                }
            }else if(direction.equals(Direction.down)){
                moveNoCost(Direction.down);
                attackLand[0] = curCityCrewPos[0];
                attackLand[1] = curCityCrewPos[1];
                curCityCrewPos[0] = crewPos0;
                curCityCrewPos[1] = crewPos1;
                if(r[attackLand[0]-1][attackLand[1]-1].getDep()-damage<1) {
                    r[attackLand[0]-1][attackLand[1]-1].setDep(0);
                    r[attackLand[0]-1][attackLand[1]-1].freeOwner();
                    if(r[attackLand[0]-1][attackLand[1]-1].getCityCenterOwner()!=null) {
                        r[attackLand[0]-1][attackLand[1]-1].freeCityCenter();
                        //endgame
                    }

                }else{
                    r[attackLand[0]-1][attackLand[1]-1].withdrawn(damage);
                }
            }else if(direction.equals(Direction.downleft)){
                moveNoCost(Direction.downleft);
                attackLand[0] = curCityCrewPos[0];
                attackLand[1] = curCityCrewPos[1];
                curCityCrewPos[0] = crewPos0;
                curCityCrewPos[1] = crewPos1;
                if(r[attackLand[0]-1][attackLand[1]-1].getDep()-damage<1) {
                    r[attackLand[0]-1][attackLand[1]-1].setDep(0);
                    r[attackLand[0]-1][attackLand[1]-1].freeOwner();
                    if(r[attackLand[0]-1][attackLand[1]-1].getCityCenterOwner()!=null) {
                        r[attackLand[0]-1][attackLand[1]-1].freeCityCenter();
                        //endgame
                    }

                }else{
                    r[attackLand[0]-1][attackLand[1]-1].withdrawn(damage);
                }
            }else{
                moveNoCost(Direction.upleft);
                attackLand[0] = curCityCrewPos[0];
                attackLand[1] = curCityCrewPos[1];
                curCityCrewPos[0] = crewPos0;
                curCityCrewPos[1] = crewPos1;
                if(r[attackLand[0]-1][attackLand[1]-1].getDep()-damage<1) {
                    r[attackLand[0]-1][attackLand[1]-1].setDep(0);
                    r[attackLand[0]-1][attackLand[1]-1].freeOwner();
                    if(r[attackLand[0]-1][attackLand[1]-1].getCityCenterOwner()!=null) {
                        r[attackLand[0]-1][attackLand[1]-1].freeCityCenter();
                        //endgame
                    }

                }else{
                    r[attackLand[0]-1][attackLand[1]-1].withdrawn(damage);
                }
            }
        }
        budget-=(damage+1);
    }
    public boolean invest(double amount) {
        Region[][] r = territory.getRegions() ;
        if(budget-amount-1>=0){
            if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()==null&&checkAdjacentLand()) {
                r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].deposit(amount);
                r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].conquer(this);
                budget-=(amount+1);
            }else if(itMyCity()){
                r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].deposit(amount);
                budget-=(amount+1);
            }else{
                budget-=1;
            }
        }else{
            if(budget-1>=0){
                budget-=1;
                return true;
            } else{
                return false;
            }
        }
        return true;
    }
    public boolean collect(double amount) {
        Region[][] r = territory.getRegions() ;
        if(budget-1>=0){
            if(itMyCity() &&r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getDep()>=amount) {
                r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].withdrawn(amount);
                if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getDep()==0){
                    r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].freeOwner();
                    if(r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].equals(curCityCenter)){
                        r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].freeCityCenter();
                        curCityCenter = null;
                    }
                }
                budget+=amount;
                budget-=1;
            }else{
                budget-=1;
            }
            return true;
        }else{return false;}

    }
    public void relocate(double cost) {
        Region[][] r = territory.getRegions() ;
        double reloCost = cost;
        if(budget-reloCost>=0){
            if(itMyCity()) {
                r[curCityCenterPos[0]-1][curCityCenterPos[1]-1].freeCityCenter();
                r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].setAsCityCenter();
                curCityCenter = r[curCityCrewPos[0]-1][curCityCrewPos[1]-1];
                budget-=reloCost;
            }
        }
    }
    private void moveNoCost(Direction direction){
        if(direction.equals(Direction.up)){
            if(curCityCrewPos[0]-1>0){
                if(budget-1>=0){
                    curCityCrewPos[0]-=1;
                }
            }
        }
        else if(direction.equals(Direction.upright)){
            if(curCityCrewPos[1]%2==0){
                if(curCityCrewPos[0]-1>0&&curCityCrewPos[1]+1<=territory.getCols()){
                    if(budget-1>=0){
                        curCityCrewPos[0]-=1;
                        curCityCrewPos[1]+=1;
                    }
                }
            }else{
                if(curCityCrewPos[1]+1<=territory.getCols()){
                    if(budget-1>=0){
                        curCityCrewPos[1]+=1;
                    }
                }
            }
        }
        else if(direction.equals(Direction.downright)){
            if(curCityCrewPos[1]%2==0){
                if(curCityCrewPos[1]+1<=territory.getCols()){
                    if(budget-1>=0){
                        curCityCrewPos[1]+=1;
                    }
                }
            }else{
                if(curCityCrewPos[0]+1<=territory.getRows()&&curCityCrewPos[1]+1<=territory.getCols()){
                    if(budget-1>=0){
                        curCityCrewPos[0]+=1;
                        curCityCrewPos[1]+=1;
                    }
                }
            }
        }
        else if(direction.equals(Direction.down)){
            if(curCityCenterPos[0]+1<territory.getRows()){
                if(budget-1>=0){
                    curCityCrewPos[0]+=1;
                }
            }
        }
        else if(direction.equals(Direction.downleft)){
            if(curCityCrewPos[1]%2==0){
                if(curCityCrewPos[1]-1>0){
                    if(budget-1>=0){
                        curCityCrewPos[1]-=1;
                    }
                }
            }else{
                if(curCityCrewPos[0]+1<=territory.getRows()&&curCityCrewPos[1]-1>0){
                    if(budget-1>=0){
                        curCityCrewPos[0]+=1;
                        curCityCrewPos[1]-=1;
                    }

                }
            }
        }
        else {
            if(curCityCrewPos[1]%2==0){
                if(curCityCrewPos[1]-1>0&&curCityCrewPos[0]-1>0){
                    if(budget-1>=0){
                        curCityCrewPos[0]-=1;
                        curCityCrewPos[1]-=1;
                    }
                }
            }else{
                if(curCityCrewPos[1]-1>0){
                    if(budget-1>=0){
                        curCityCrewPos[1]-=1;
                    }

                }
            }
        }
    }
    private boolean itMyCity(){
        Region[][] r = territory.getRegions() ;
        return r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner()!=null&&r[curCityCrewPos[0]-1][curCityCrewPos[1]-1].getOwner().equals(this);
    }
    private boolean checkAdjacentLand(){
        Region[][] r = territory.getRegions() ;
        int crewPos0 = curCityCrewPos[0];
        int crewPos1 = curCityCrewPos[1];
        boolean adjacentLand = false;
        moveNoCost(Direction.up);
        if(itMyCity())adjacentLand = true;
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        moveNoCost(Direction.upright);
        if(itMyCity())adjacentLand = true;
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        moveNoCost(Direction.downright);
        if(itMyCity())adjacentLand = true;
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        moveNoCost(Direction.down);
        if(itMyCity())adjacentLand = true;
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        moveNoCost(Direction.downleft);
        if(itMyCity())adjacentLand = true;
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        moveNoCost(Direction.upleft);
        if(itMyCity())adjacentLand = true;
        curCityCrewPos[0] = crewPos0;
        curCityCrewPos[1] = crewPos1;
        return adjacentLand;
    }
}
