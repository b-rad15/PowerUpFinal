package com.company;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Match implements Comparable, Serializable{
    
    public Integer team;
    public Integer number;
    public Integer aBPU;
    public Integer aASB;
    public Integer aSB;
    public Integer aOSB;
    public Integer aBE;
    public Integer tBPU;
    public Integer tASB;
    public Integer tSB;
    public Integer tOSB;
    public Integer tBE;
    public Integer placed;
    public Integer lev;
    public Integer force;
    public Integer boost;
    public Integer numClimbOnThis;
    public String position;
    public String crossLine;
    public String dropBlock;
    public String blockPlacement;
    public String climb;
    public String error;
    public String defensive;
    public String comments;
    public final static String[] lineStarts = new String[]{"Team:", "Match:","Position", "AUTONOMOUS", "Crossed Line:", "Dropped Block:", "Block Placement:", "Blocks Picked Up:", "Alliance Switch Blocks:", "Scale Blocks:", "Opponent Switch Blocks:", "Blocks in Exchange:","TELEOP","Blocks Picked Up:", "Alliance Switch Blocks:", "Scale Blocks:", "Opponent Switch Blocks:", "Blocks in Exchange:","Levitate:", "Force:","Boost:","ENDGAME","Climb:","Number Climbing on This:", "Error:", "Defensive:", "Comments:"};
//    public Hashtable data;

    public Match(Integer numberEntered,Integer aBPUEntered,Integer aASBEntered,Integer aSBEntered,Integer aOSBEntered,Integer aBEEntered,Integer tBPUEntered,Integer tASBEntered,Integer tSBEntered,Integer tOSBEntered,Integer tBEEntered,Integer levEntered,Integer forceEntered,Integer boostEntered,Integer numClimbOnThisEntered, String crossLineEntered, String dropBlockEntered, String blockPlacementEntered, String climbEntered){
        number = numberEntered;
        aBPU = aBPUEntered;
        aSB = aSBEntered;
        aOSB = aOSBEntered;
        aBE = aBEEntered;
        aASB = aASBEntered;
        tBPU = tBPUEntered;
        tSB = tSBEntered;
        tOSB = tOSBEntered;
        tBE = tBEEntered;
        tASB = tASBEntered;
        lev = levEntered;
        boost = boostEntered;
        force = forceEntered;
        numClimbOnThis = numClimbOnThisEntered;
        climb = climbEntered;
        crossLine = crossLineEntered;
        dropBlock = dropBlockEntered;
        blockPlacement = blockPlacementEntered;
    }

    public Match(Path file) throws IOException {
        this(file, 1);
    }

    public Match(Path file, int column) throws IOException {
        Hashtable<String, Integer> teleData = new Hashtable<>();
        Hashtable<String, Object> data = new Hashtable<>();
        Hashtable workingData = data;
        List<String> lines = Files.readAllLines(file);
        ArrayList<Hashtable> datas = new ArrayList<>();
        datas.add(data);
        datas.add(teleData);
        int index = 0;
        for(String line : lines){
            if(line.contains("AUTONOMOUS") || line.contains("ENDGAME")){
                index = 0;
            }else if(line.contains("TELEOP")){
                index = 1;
            } else {
                String[] columns = line.split(",");
                if (columns[column] == null) {
                    columns[column] = "";
                }
                try {
                    datas.get(index).put(columns[0].replace(':', ' ').trim(), Integer.parseInt(columns[column].trim()));
                } catch (NumberFormatException e) {
                    datas.get(index).put(columns[0].replace(':', ' ').trim(), columns[column]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    datas.get(index).put(columns[0].replace(':', ' ').trim(), "");
                }
            }
        }
        this.team = Integer.parseInt(lines.get(0).split(",")[1]);
        this.number =(Integer) data.get("Match");
        this.position = (String) data.get("Position");
        this.crossLine =(String) data.get("Crossed Line");
        this.dropBlock = (String) data.get("Dropped Block");
        this.blockPlacement =(String) data.get("Block Placement");
        this.aBPU =(Integer) data.get("Blocks Picked Up");
        this.aASB =(Integer) data.get("Alliance Switch Blocks");
        this.aSB =(Integer) data.get("Scale Blocks");
        this.aOSB =(Integer) data.get("Opponent Switch Blocks");
        this.aBE =(Integer) data.get("Blocks in Exchange");
        this.tBPU = teleData.get("Blocks Picked Up");
        this.tASB = teleData.get("Alliance Switch Blocks");
        this.tSB = teleData.get("Scale Blocks");
        this.tOSB = teleData.get("Opponent Switch Blocks");
        this.tBE = teleData.get("Blocks in Exchange");
        this.lev = teleData.get("Levitate");
        this.boost = teleData.get("Boost");
        this.force = teleData.get("Force");
        this.climb = (String) data.get("Climb");
        this.numClimbOnThis = (Integer) data.get("Number Climbing on This");
        this.error = (String) data.get("Error");
        this.defensive = (String) data.get("Defensive");
        this.comments = String.valueOf(data.get("Comments"));
        this.placed =  tBPU + aASB + tASB + aSB + tSB + aOSB + tOSB + aBE + tBE;
    }

    public Match(int match, Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        List<String> matches = Arrays.asList(lines.get(1).split(","));
        matches.remove(0);
        matches.indexOf(Integer.toString(match));
        new Match(file, matches.indexOf(Integer.toString(match)));
    }

    public Match(){}

    public static int getMatches(Path file) throws IOException {
        return Files.readAllLines(file).get(1).split(",").length - 1;
    }
    
    public Object[] getDataArray(){
        return new Object[]{team,number,position,"",crossLine,dropBlock,blockPlacement,aBPU,aASB,aSB,aOSB,aBE,"",tBPU,tASB,tSB,tOSB,tBE,lev,force,boost,"",climb,numClimbOnThis,error,defensive,comments};
    }

    @Override
    public int compareTo(Object o) {
        Match m = (Match) o;
        return this.number.compareTo(m.number);
    }
}
