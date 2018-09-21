/** the Cata-log Program
 *
 *  This is a program that reads excel files and converts the data so
 *  they are ready for import to the Universal Product Database.
 *
 * @version 0.9.1 ?
 * @author Evan Jones
 *
 **/

package com.ecrs.catalog;

import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Arrays;
/*
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/** catalog
 * this class includes methods to parse file-types tsv
 * and xlsx(?) for data and filter data. 
 */

public class catalog 
{
    private String filePath;
    private BufferedWriter toTSV;
    private BufferedReader read;
    private File tsv;
    //column-values
    private List<String> cv;
    private String line;
    private boolean isTSV;
    private boolean needsAlias=false;
    
    public String getFilePath() 
    {      
        return tsv.getPath();
    }

    public catalog(String fileName) throws IOException
    {
        try 
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
            LocalDateTime now = LocalDateTime.now();

            if (fileName.contains("xlsx"))
            {
                //readXLSX( new XSSFWorkbook( new File(fileName)));
            } 
            else if (fileName.contains("tsv")) 
            {
                
                tsv = new File(fileName.replace(".tsv","_"+dtf.format(now)+".tsv"));
                toTSV = new BufferedWriter(new FileWriter(tsv));                
                read = new BufferedReader( new FileReader( new File(fileName)));       
                line = read.readLine().toUpperCase().replace("LENGTH\t", "")+"\r\n";                
                needsAlias = !line.contains("RECEIPT ALIAS");
                cv = Arrays.asList(line.split("\t"));
                line = (!needsAlias) ? line : line.split("ITEM NAME")[0]+"ITEM NAME\tRECEIPT ALIAS"+line.split("ITEM NAME")[1];
                toTSV.append(line);
                System.out.println(line);
                readTSV();
            }
            else
            {
                System.out.println("Error, file-type must be 'xslx' or 'tsv'");
                System.exit(1);
            }            
        } 
        catch (Exception e)
        {
            System.err.println("Error: "+e);
        }
    }
    
    /** readTSV
     * 
     * reads tsv and creates row objects to check data, if no exception thrown
     * it returns a String with the path to the new tsv file created
     * 
     */
    public void readTSV() throws IOException 
    {
        line = read.readLine();
        while (line != null) 
        {
            String current = new row(line.toUpperCase().split("\t"), cv, needsAlias).getLine();
            if (current != null) {
                output(current);
            }
            line = read.readLine();
        }
        toTSV.close();
        read.close();
        System.out.println("TSV Read and Created");
        filePath = getFilePath();
    }
    /** readXLSX
     *
     * This method takes a workbook with an excel file, and reads all the data and processes it row by row.
     * The first row of the excel file must be the names for the data stored in the columns, i.e.
     * "UPC", "RECEIPT ALIAS". caps does not matter, all output is Capitalized
     *
     * @param book
     * 
     */
/*
    private void readXLSX(XSSFWorkbook book) throws IOException 
    {
        //sheet = book.getSheetAt(0);
        XSSFSheet sheet = book.getSheet("WORKING");
        int rowNum = 0;
        int ColumnNum = 0;
        String currentCell = "";
        for (Row cols : sheet) 
        {
            String[] vals = new String[cv.size()];
            for (int i = 0; i < cv.size(); i++)
            {
                vals[i] = cols.getCell(i).getStringCellValue();
            }
            row r = new row(vals, cv);
            output(r.getLine());
        }
        filePath = getFilePath();
    }

    /** output
     *
     * helper method for readability
     * just writes a completed line to output file,
     *
     * @param append
     * @throws IOException
     */
    private void output(String append) throws IOException {
        toTSV.append(append+"\r\n");
    }

    public static void main(String[] args) throws IOException {
        if (args!=null)
        {
            catalog k = new catalog(args[0]);
        }
    }
}
