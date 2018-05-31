package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class test{

    static Path dataPath = Paths.get(System.getProperty("user.dir"), "New Data");
    static String phoneStoragePath = "/storage/emulated/0/Android/data/com.brado.powerupscouting/files/";
        public static void main(String args[]) throws IOException {
            Runtime rt = Runtime.getRuntime();
//            String command = "adb pull " + phoneStoragePath + " \"" + dataPath.toAbsolutePath() + "\"";
            System.out.println("Done");
//            Process process = rt.exec("adb", "devives");
            ProcessBuilder pb = new ProcessBuilder("adb", "devices");
            Process process = pb.start();
            InputStream is = process.getInputStream();
            Scanner sc = new Scanner(is);
            sc.nextLine();
            String s = sc.nextLine();
            ArrayList<String> devices = new ArrayList<>();
            while(!s.equals("")){
                devices.add(s.split("\t")[0]);
                s = sc.nextLine();
            }
            for(String device : devices){
                ProcessBuilder p = new ProcessBuilder("adb", "pull", "-s", device, phoneStoragePath, dataPath.toAbsolutePath().toString());
                getOutput(p.start());
            }
        }

        public static Process getOutput(Process p){
            Scanner sc = new Scanner(p.getInputStream());
            String s = null;
            try {
                s = sc.nextLine();
                while (!s.equals("")){
                    System.out.println(s);
                    s = sc.nextLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }
}
