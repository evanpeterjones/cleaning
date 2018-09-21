package com.ecrs.catalog;

import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class importFailures {
    public importFailures(String fileName, String oldTSV) throws FileNotFoundException, IOException {
        ArrayList<String> upc_errors = new ArrayList<String>();
        BufferedReader input = new BufferedReader(new FileReader(new File(fileName)));
        String currentLine;
        while ((currentLine = input.readLine()) != null) {
            currentLine = input.readLine();
            //read line twice to accomodate the extra line
            //upc_errors.append(currentLine.)
            upc_errors.add(currentLine.replaceAll("[a-z A-Z.-]+", ""));
        }
        System.out.println(upc_errors.isEmpty() ? " Empty " : upc_errors.get(0));
        if (!upc_errors.isEmpty()) {
            System.out.println("Successfully read UPCS, creating new file...");
        } else {
            System.out.println("Error reading UPCs, program exited...");
            System.exit(1);
        }
        input.close();
        input = new BufferedReader(new FileReader(new File(oldTSV)));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDateTime now = LocalDateTime.now();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(oldTSV.replace(".tsv","_FIXED.tsv"))));
        PrintWriter err = new PrintWriter(new BufferedWriter(new FileWriter(oldTSV.replace(".tsv","_REMOVED.txt"))));
        err.print("Items Removed From File "+oldTSV +"\r\n\r\n");
        String currentIndex;
        err.print(input.readLine().toUpperCase());
        while ((currentLine = input.readLine()) != null) {
            currentIndex = currentLine.split("\t")[0];
            if (upc_errors.contains(currentIndex)){
                err.println(currentLine.toUpperCase());
            } else {
                out.print(currentLine.toUpperCase()+"\r\n");
            }
        }
        err.close();
        out.close();
        input.close();
        //idea is that it will read file copied from ecoportal,
        //and loop through original file, a new file is created to fill
        //with the UPCs with incorrect info to be checked an imported separarately.
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        importFailures n = new importFailures(args[0], args[1]);
    }

}
