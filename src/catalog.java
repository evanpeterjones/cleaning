/** the Cata-log Program
 *
 *  This is a program that reads excel files and converts the data so
 *  they are ready for import to the Universal Product Database.
 *
 * @version 0.9.1 ?
 * @author Evan Jones
 *
 **/

package src;

import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Arrays;

/** catalog
 * this class includes methods to parse file-types tsv
 * and xlsx(?) for data and filter data. 
 */

public class catalog {
    private String filePath;
    //private Sheet sheet;
    //following variables store column #s from the excel file
    private BufferedWriter toTSV;
    private BufferedReader read;
    private FileInputStream file;
    private File tsv;
    
    public String getFilePath() {      
        return tsv.getPath();
    }

    public catalog(String fileName) throws IOException {
        file = new FileInputStream(new File(fileName));
        if (fileName.contains("xlsx")){
            //Workbook wb = new XSSFWorkbook(file);
        } else if (fileName.contains("tsv")) {
            
        } else {
            System.out.println("Error, file not acceptable");
            System.exit(1);
        }
        read = new BufferedReader(new FileReader( new File(fileName)));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDateTime now = LocalDateTime.now();
        tsv = new File(fileName.replace(".tsv","_"+dtf.format(now)+".tsv"));
        toTSV = new BufferedWriter(new FileWriter(tsv));
    }
    
    /** run
     * 
     * reads and creates row objects to check data, if no exception thrown
     * it returns a String with the path to the new tsv file created
     * 
     */
    public String run() throws IOException {
        String line = read.readLine();
        //TODO: add method to use item name if no receipt alias and add column
        //in Row class we can define a method to run item name and add this 
        toTSV.append(line.toUpperCase().replace("LENGTH\t","")+"\r\n");
        List<String> cv = Arrays.asList(line.split("\t"));

        line = read.readLine();
        while (line != null) {
            row currentRow = new row(line.split("\t"), cv);
            output(currentRow.getLine());
            line = read.readLine();
        }
        toTSV.close();
        file.close();
        return getFilePath();
    }
    /** readCatalog(Void)
     *
     * This method takes a workbook with an excel file, and reads all the data and processes it row by row.
     * The first row of the excel file must be the names for the data stored in the columns, i.e.
     * "UPC", "RECEIPT ALIAS". caps does not matter, all output is Capitalized
     *
     * @param book

    private void readCatalog(Workbook book) {
        sheet = book.getSheetAt(0);
        int rowNum = 0;
        int ColumnNum = -1;
        String currentCell = "";
        for (Row row : sheet) {
            //the following line is arbitrary because the algorithm is relatively linear
            //data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                ColumnNum++;
                currentCell = cell.toString().toUpperCase();
                if (rowNum == 0) {
                    columnValues[ColumnNum] = currentCell;
                }
                switch (columnValues[ColumnNum]) {
                    case "RECEIPT ALIAS":
                        alias = currentCell;
                        //output(getReceiptAlias(currentCell));
                        //TODO: write algorithm to parse and determine the value of the new String
                        //with the data, once correctly formatted, we could skip storing in the arraylist
                        //and just automatically write it to the cells of a new excel file.
                        break;
                    case "UPC":
                        upc = getUPC(currentCell);
                        //output(getUPC(currentCell));
                        break;
                    case "MSRP":
                        msrp = currentCell;
                        //output(getMSRP(currentCell));
                        break;
                    case "BRAND":
                        brand = currentCell;
                        break;
                    default :
                        //output(currentCell);
                }
            }
            rowWrite();
            //switch case could come out here, and each row could be first loaded into variables so that the Receipt Aliases
            //can exclude brand info and be more concise!!
            rowNum++;
        }
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
}
