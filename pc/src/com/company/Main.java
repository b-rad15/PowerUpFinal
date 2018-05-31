package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static Hashtable<Integer, Team> teams = new Hashtable<>();
    static File newData = new File(System.getProperty("user.dir"), "New Data"),
        teamData = new File(System.getProperty("user.dir"), "Team Data");
    static Path apkPath = Paths.get(System.getProperty("user.dir"), "release\\app-release.apk");
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Files.createDirectories(newData.toPath());
        Files.createDirectories(teamData.toPath());
        Scanner sc = new Scanner(System.in);
//        for (Team t : Team.getTeams()){
//            teams.put(t.number,t);
//        }
//        String teamNums = Main.teams.keys().toString();
//        System.out.println(teamNums.substring(1,teamNums.length()-1).replaceAll(",","\n"));
        Team team;
        ArrayList<Team> teamsRead;
        System.out.print("\nEnter something:");
        String s = sc.nextLine();
        int[] teamNums;
        Team.pullFiles();
        get();
        while(!s.equalsIgnoreCase("stop")) {
            switch (s.toLowerCase()) {
                case "pull":
                    Team.pullFiles();
                    break;
//        Team t = new Team(Paths.get("C:\\Users\\brado\\100_Match_425.csv"));
                case "new":
                    System.out.print("\nNumber:");
                    team = new Team(sc.nextInt());
                    Main.teams.put(team.number, team);
                    break;
                case "write":
                    for(Team t:teams.values()) {
                        t.newFile();
                    }
                    break;
                case "analyze":
                    System.out.print("\nType:");
                    ArrayList<Team> readTeams = Team.getTeams();
                    Team.setCompareParam(sc.nextLine());
                    Collections.sort(readTeams);
                    for(Team t : readTeams){
                        System.out.println(t.number + "\t");
                    }
                    break;
                case "install":
                    install();
//                    for(int i = 1; i < 4; i++)
//                        if(!install()) break;
                    break;
                case "write teams":
                    for (Team t: teams.values()) {
                        Path teamfile = Paths.get(teamData.toString(), t.number + ".team");
                        FileOutputStream fos = new FileOutputStream(teamfile.toFile());
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(t);
                        fos.close();
                        oos.close();
                    }
                    break;
                case "get":
                    Team.pullFiles();
                    get();
                    break;
                case "read team":
                    for (File f : teamData.listFiles()) {
                        if(f.getName().split("\\.")[1].equalsIgnoreCase("team")) {
                            FileInputStream fis = new FileInputStream(f);
                            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
                            Team t = (Team) objectInputStream.readObject();
                            System.out.println(t.toString());
                        }
                    }
                    break;
                case "":
                    break;
                default:
                    System.out.print("\nAction not recognized");
            }
            System.out.print("\nEnter something:");
            s = sc.nextLine();
        }
//        for(Team t : Main.teams.values()){
//            t.newFile();
//        }
        for(Team t:teams.values()) {
            t.newFile();
        }
    }

    static boolean install() throws IOException {
        boolean flag = true;
        try {
            ArrayList<String> devices = Team.getDevices();
            for (String s : devices){
                try {
                    Process p = new ProcessBuilder("adb", "-s", s, "install", apkPath.toAbsolutePath().toString()).start();
                    for (String o : Team.getProcessOutput(p))
                        System.out.println(o);
                } catch (Exception e) {
                    System.out.println("Installation failed at " + s);
                    e.printStackTrace();
                    flag = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static ArrayList<String> printOutput(Process p){
        Scanner sc = new Scanner(p.getInputStream());
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = sc.nextLine()) != null) {
            System.out.println(line);
            lines.add(line);
        }
        return lines;
    }

    public static boolean isIn(Object[] a, Object val){
        for(Object o:a) {
            if (o.equals(val)) return true;
        }
        return false;
    }

    public static <T extends Enum<T>> int[] enumNameToIntArray(T[] values) {
        int i = 0;
        int[] result = new int[values.length];
        for (T value: values) {
            result[i++] = Integer.parseInt(value.name());
        }
        return result;
    }

    public static void get() throws IOException {
            File[] files = teamData.listFiles();
            int[] teamNums;
            for(File f:files) {
                Team t = new Team(f.toPath());
                teams.put(t.number, t);
                File[] dataFiles = newData.listFiles();
                for (File g : dataFiles) {
                    String gName = g.getName().replaceAll("\\.csv", "");
                    int gNum = Integer.parseInt(gName.split("_")[0]);
                    int gMatch = Integer.parseInt(gName.split("_")[2]);
                    if (gNum == t.number && !isIn(t.matches.toArray(), gMatch)) {
                        t.add(g.toPath());
                    }
                }
                teamNums = Arrays.stream(teams.keySet().toArray()).mapToInt(o -> (int) o).toArray();
                System.out.println(Arrays.toString(teamNums));
            }
            for (File f : newData.listFiles()) {
                String fName = f.getName();
                int fNum = Integer.parseInt(fName.split("_")[0]);
//            teamNums = Arrays.stream(teams.keySet().toArray()).mapToInt(o -> (int)o).toArray();
                if (!teams.contains(fNum)) {
                    Team team = null;
                    try {
                        team = new Team(fNum);
                    } catch (Exception e) {
                        System.out.println("Team: " + fNum + " not found");
                    }
                    try {
                        teams.put(team.number, team);
                    } catch (Exception e) {
                        System.out.println("Team: " + fNum + " not found");
                    }
                }
            }

    }
}

