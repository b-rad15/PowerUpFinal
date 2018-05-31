package com.company;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Team implements Comparable, Comparator, Serializable {
    static Path dataPath = Paths.get(System.getProperty("user.dir"), "New Data");
    static String phoneStoragePath = "/storage/emulated/0/Android/data/com.brado.powerupscouting/files/";
    static Path teamPath = Paths.get(System.getProperty("user.dir"), "Team Data");
    static String compareQuality = "Scale";
    private static String compareParam;
    ArrayList<Match> matches = new ArrayList<>();
    ArrayList<Integer> matchNums = new ArrayList<>();
    int eSum = 0;
    int eCount = 0;
    Double eScore = 0.0;
    int osSum = 0;
    int osCount = 0;
    Double osScore = 0.0;
    int asSum = 0;
    int asCount = 0;
    Double asScore = 0.0;
    int sSum = 0;
    int sCount = 0;
    Double sScore = 0.0;
    int bSum = 0;
    int bCount = 0;
    Double bScore = 0.0;
    int pSum = 0;
    int pCount = 0;
    Double pScore = 0.0;
    int climbOther = 0;
    int climbFail = 0;
    int climbWrung = 0;
    int climbNo = 0;
    Double climbScore = 0.0;
    Comparator c;
    Hashtable<String, Integer> positions = new Hashtable<>();
    int number;

    public Team(int num, Match match) {
        matches.add(match);
        number = num;
    }

    public Team(int num, Collection<Match> match) {
        number = num;
        matches.addAll(match);
    }

    public Team(Path dataFile) {
        try {
            final int nummatches = Match.getMatches(dataFile);
            for (int i = 1; i <= nummatches; i++) {
                this.matches.add(new Match(dataFile, i));
            }
            this.number = this.matches.get(0).team;
        } catch (IOException e) {
            System.out.println("Failed to read: " + dataFile);
        } catch (NumberFormatException e) {
            System.out.println("Team number in file " + dataFile + " doesn't exist");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Out of Bounds");
        }
        init();
    }

    public Team(int num) throws IOException {
        this.number = num;
//        System.out.println(dataPath.toAbsolutePath());
        Path p = Paths.get(teamPath.toAbsolutePath().toString(), num + ".csv");
        Team t;
        if(Files.exists(p)) t = new Team(p);
        for (File file : Objects.requireNonNull(dataPath.toFile().listFiles())) {
            String[] split1 = file.getName().replaceAll("\\.csv", "").split("_");
            if (split1[0].equalsIgnoreCase(String.valueOf(this.number)) && !objectIsIn(split1[2], new ArrayList[]{this.matches})) {
                List<String> strings = Files.readAllLines(file.toPath());
                String[] split = strings.get(0).split(",");
                final int nummatches = Match.getMatches(file.toPath());
                for (int i = 1; i <= nummatches; i++) {
                    this.matches.add(new Match(file.toPath(), i));
                }
            }
        }
        Collections.sort(matches);
        init();
    }

    public static String print(List<Integer> list) {
        String s = list.toString().replaceAll(", ", "\t");
        return s.substring(1, s.length() - 1);
    }

    public static String bprint(List<Boolean> list) {
        String temp = list.toString().replaceAll(", ", "\t");
        return temp.substring(1, temp.length() - 1);
    }

    public static void pullFiles() throws IOException {
        System.out.println(dataPath);
        Runtime rt = Runtime.getRuntime();
        ArrayList<String> devices = null;
        try {
            devices = getDevices();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No devices connected");
        }
        if (devices == null) {
            System.out.println("No devices found");
            return;
        }
        for (String device : devices) {
            ProcessBuilder processBuilder = new ProcessBuilder("adb", "-s", device, "pull", phoneStoragePath, "\"" + dataPath.toAbsolutePath().toString() + "\"");
            System.out.println(processBuilder.command().toString().replaceAll(",", ""));
            processBuilder.start();
        }
        File file = new File(dataPath + "\\files");
        File[] files;
        try {
            files = file.listFiles();
            if (files == null) {
                System.out.println("No files found");
                return;
            }
            for (File f : files) {
                try {
                    if (f.getName().contains(".csv")) {
                        Files.move(f.toPath(), Paths.get(dataPath.toAbsolutePath().toString(), f.getName()), REPLACE_EXISTING);
                    }
                } catch (Exception e) {
                    System.out.println("File:" + f.getName() + "not moved");
                }
            }
            try {
                Files.delete(Paths.get(dataPath + "\\files"));
            } catch (Exception e) {
                System.out.println("Directory not empty exception");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(File p : files){
//            String fileName = p.getName();
//            System.out.println(fileName);
//            try {
////                String substring = fileName.substring(fileName.length() - 4, fileName.length());
////                String[] split = fileName.split(".");
////                String s = split[1];
////                boolean isCSV = s.equalsIgnoreCase("csv");
//                boolean containsCSV = fileName.contains(".csv");
//                if (!containsCSV) {
//                    p.delete();
//                }
//            }catch (ArrayIndexOutOfBoundsException e){
//                p.delete();
//            }
//        }
    }

    public static ArrayList<String> getProcessOutput(Process p) {
        ArrayList<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(p.getInputStream());
        try {
            String s = sc.nextLine();
            while (!s.equalsIgnoreCase("")) {
                lines.add(s);
                s = sc.nextLine();
            }
        } catch (Exception e) {

        }
        return lines;
    }

    public static ArrayList<String> getDevices() throws IOException {
        ArrayList<String> devices = new ArrayList<>();
        Scanner sc = new Scanner(new ProcessBuilder("adb", "devices").start().getInputStream());
        sc.nextLine();
        String s = sc.nextLine();
        while (!s.equals("")) {
            String[] args = s.split("\t");
            devices.add(args[0]);
            if (!args[1].equalsIgnoreCase("offline")) s = sc.nextLine();
        }
        return devices;
    }

    public static ArrayList<Team> getTeams() throws IOException {
        ArrayList<Team> teams = new ArrayList<>();
        try {
            for (File f : Objects.requireNonNull(teamPath.toFile().listFiles())) {
                teams.add(new Team(f.toPath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teams;
    }

    public static void setCompareParam(String compare) {
        compareParam = compare;
    }

    private void init() {
        this.eSum = 0;
        this.eCount = 0;
        this.eScore = 0.0;
        this.osSum = 0;
        this.osCount = 0;
        this.osScore = 0.0;
        this.asSum = 0;
        this.asCount = 0;
        this.asScore = 0.0;
        this.sSum = 0;
        this.sCount = 0;
        this.sScore = 0.0;
        this.bSum = 0;
        this.bCount = 0;
        this.bScore = 0.0;
        this.pSum = 0;
        this.pCount = 0;
        this.pScore = 0.0;
        this.climbOther = 0;
        this.climbFail = 0;
        this.climbWrung = 0;
        this.climbNo = 0;
        this.climbScore = 0.0;
        this.matchNums = new ArrayList<>();
        this.positions = new Hashtable<>();
        int left = 0;
        int mid = 0;
        int right = 0;
        int dns = 0;
        for (Match m : matches) {
            int num = m.aBPU + m.tBPU;
            this.bSum += num;
            if (num > 0) bCount++;
            num = m.aASB + m.tASB;
            this.asSum += num;
            if (num > 0) asCount++;
            num = m.aOSB + m.tOSB;
            this.osSum += num;
            if (num > 0) osCount++;
            num = m.aBE + m.tBE;
            this.eSum += num;
            if (num > 0) eCount++;
            num = m.aSB + m.tSB;
            this.sSum += num;
            if (num > 0) sCount++;
            num = m.placed;
            this.pSum += num;
            if (num > 0) pCount++;
            switch (m.climb.toLowerCase()){
                case "did not climb":
                    this.climbNo++;
                    break;
                case "failed to climb":
                    this.climbFail++;
                    break;
                case "climbed onto wrung":
                    this.climbWrung++;
                    break;
                case "climbed using other robot":
                    this.climbOther++;
                    break;
                default:
                    this.climbNo++;
                    break;
            }
            try {
                switch (m.position){
                    case "Left":
                        left++;
                        break;
                    case "Middle":
                        mid++;
                        break;
                    case "Right":
                        right++;
                    case "DNS":
                        dns++;
                        break;
                    default:
                        dns++;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Position not in file");
            }
        }
        if (bCount!=0) {
            this.bScore =(double) bSum/bCount;
        } else {
            bScore = 0.0;
        }
        if (asCount!=0) {
            this.asScore =(double) asSum/asCount;
        } else {
            asScore = 0.0;
        }
        if (osCount!=0) {
            this.osScore =(double) osSum/osCount;
        } else {
            osScore=0.0;
        }
        if (eCount!=0) {
            this.eScore =(double) eSum/eCount;
        } else {
            eScore=0.0;
        }
        if (pCount!=0) {
            this.pScore =(double) pSum/pCount;
        } else {
            pScore=0.0;
        }
        if (matches.size() != 0) {
            this.climbScore = (climbWrung + climbOther*1.5)/matches.size();
        } else {
            climbScore = 0.0;
        }
        for(Match m:this.matches) {
            this.matchNums.add(m.number);
        }
        this.positions.put("Left", left);
        this.positions.put("Middle", mid);
        this.positions.put("Right", right);
        this.positions.put("DNS", dns);
    }

    public Hashtable<String, Integer> getPositions() {
        int d = 0;
        int l = 0;
        int m = 0;
        int r = 0;
        for (Match match : matches) {
            switch (match.position) {
                case "DNS":
                    d++;
                    break;
                case "Left":
                    l++;
                    break;
                case "Middle":
                    m++;
                    break;
                case "Right":
                    r++;
                    break;
                default:
                    d++;
                    break;
            }
        }
        Hashtable<String, Integer> pos = new Hashtable<>();
        pos.put("DNS", d);
        pos.put("Left", l);
        pos.put("Middle", m);
        pos.put("Right", r);
        return pos;
    }

    public void addToFile() throws IOException {
        File[] teams = new File(System.getProperty("user.dir") + "\\Team Data").listFiles();
        Path teamFile = null;
        int searched = 0;
        for (File file : teams) {
            if (file.getName().equals(number + ".txt")) {
                teamFile = file.toPath();
                break;
            } else {
                searched++;
                if (searched >= teams.length) {
                    try {
                        this.newFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        List<String> lines = Files.readAllLines(teamFile, Charset.forName("UTF-8"));
    }

    public void newFile() throws IOException {
        Path file = Paths.get(System.getProperty("user.dir"), "Team Data", Integer.toString(number) + ".csv");
        System.out.println(file.toAbsolutePath());
        String[] lines = Match.lineStarts.clone();
        lines[0] = lines[0] + "," + this.number;
        for (Match match : this.matches)
            for (int l = 1; l < lines.length; l++)
                lines[l] = lines[l] + "," + match.getDataArray()[l];
        Files.write(file, Arrays.asList(lines));
    }

    public boolean add(Path p) throws IOException {
        return add(new Match(p));
    }

    public boolean add(Match m) {
        return this.matches.add(m);
    }

    public boolean addAll(List<Match> m) {
        return this.matches.addAll(m);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Team && this.compareTo(obj) == 0;
    }

    public void print() {
        System.out.println("Team:\t\t" + number +
                "\nMatches:\t" + matches);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("");
        sb.append(number).append("\t");
        sb.append(round(pScore,2)).append("\t");
        sb.append(round(bScore,2)).append("\t");
        sb.append(round(eScore,2)).append("\t");
        sb.append(round(asScore,2)).append("\t");
        sb.append(round(sScore,2)).append("\t");
        sb.append(round(osScore,2)).append("\t");
        sb.append(round(climbScore,2)).append("\t");
        return sb.toString();
    }

    public int getMedian(List<Integer> list) {
        int size = list.size();
        if (size % 2 == 0) {
            return (list.get(size / 2 - 1) + list.get(size / 2)) / 2;
        } else {
            return (list.get(size / 2 + 1));
        }
    }

    public int getMedian(int[] list) {
        if (list.length % 2 == 0) {
            return (list[list.length / 2 - 1] + list[list.length / 2]) / 2;
        } else {
            return (list[list.length / 2 + 1]);
        }
    }

    public int compareTo(Team t, String compare) {
        setCompareParam(compare);
        return this.compareTo(t);
    }

    @Override
    public int compareTo(Object o) {
        Team t = (Team) o;
        Integer thisone = 0;
        Integer otherone = 0;
        switch (compareParam) {
            case "Exchange":
//                for (Match m : this.matches) {
//                    thisone += m.aBE;
//                    thisone += m.tBE;
//                }
//                for (Match m : t.matches) {
//                    otherone += m.aBE;
//                    otherone += m.tBE;
//                }
                return this.eScore.compareTo(t.eScore);
            case "Scale":
//                for (Match m : this.matches) {
//                    thisone += m.aSB;
//                    thisone += m.tSB;
//                }
//                for (Match m : t.matches) {
//                    otherone += m.aSB;
//                    otherone += m.tSB;
//                }
                return this.sScore.compareTo(t.sScore);
            case "OSwitch":
//                for (Match m : this.matches) {
//                    thisone += m.aOSB;
//                    thisone += m.tOSB;
//                }
//                for (Match m : t.matches) {
//                    otherone += m.aOSB;
//                    otherone += m.tOSB;
//                }
                return this.osScore.compareTo(t.osScore);
            case "ASwitch":
//                for (Match m : this.matches) {
//                    thisone += m.aASB;
//                    thisone += m.tASB;
//                }
//                for (Match m : t.matches) {
//                    otherone += m.aASB;
//                    otherone += m.tASB;
//                }
                return this.asScore.compareTo(t.asScore);
            case "Climb":
                return this.climbScore.compareTo(t.climbScore);
        }
        return 0;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Team t1 = (Team) o1;
        Team t = (Team) o2;
        Integer thisone = 0;
        Integer otherone = 0;
        switch (compareParam) {
            case "Exchange":
                for (Match m : t1.matches) {
                    thisone += m.aBE;
                    thisone += m.tBE;
                }
                for (Match m : t.matches) {
                    otherone += m.aBE;
                    otherone += m.tBE;
                }
                return thisone.compareTo(otherone);
        }
        return 0;
    }

    public boolean objectIsIn(Object o, List<Object> os) {
        return os.indexOf(o) != -1;
    }

    public boolean objectIsIn(Object o, Object[] os) {
        return objectIsIn(o, Arrays.asList(os));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
