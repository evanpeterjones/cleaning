package src;

/** the Cata-log Program
 *
 *  This is a program that reads excel files and converts the data so
 *  they are ready for import to the Universal Product Database.
 *  The name should be read like "catapult", but catalog, lol!!
 *
 * @version 0.1
 * @author Evan Jones
 *
 */

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;/*
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/

public class catalog {
    private String filePath;
    //private Sheet sheet;
    public static final Map<String, String> aliases = createMap();
    //following variables store column #s from the excel file
    public String[] columnValues;
    private BufferedWriter toTSV;
    private static String[] removals = {
        "AS ", "SOME ", "'", "\"", ",", "-", "_", ".",
    };
    public String alias = "";
    public String upc = "";
    public String msrp = "";
    public String brand = "";

    public catalog(String fileName) throws IOException {
        FileInputStream file = new FileInputStream(new File(fileName));
        BufferedReader read;
        if (fileName.contains("xlsx")){
            //Workbook wb = new XSSFWorkbook(file);
        } else if (fileName.contains("tsv")) {
            
        } else {
            System.out.println("Error, file not acceptable");
            System.exit(1);
        }
        read = new BufferedReader(new FileReader( new File(fileName)));
        File tsv = new File("rename_me.tsv");
        toTSV = new BufferedWriter(new FileWriter(tsv));
        columnValues = read.readLine().split("\t");
        //readCatalog(wb);
        read(read);
    }
    public catalog() {
        //this is for testing methods
        //remove this;
    }
    public void read(BufferedReader read) throws IOException {
        String line = read.readLine();
        String row[] = line.split("\t");
        String currentCell = "";
        while (line != null) {
            row = line.split("\t");
            for (int i = 0; i < row.length; i++) {
                currentCell = row[i];
                switch (currentCell) {
                    case "RECEIPT ALIAS":
                        //alias = currentCell;
                        output(getReceiptAlias(currentCell, brand));
                        //TODO: write algorithm to parse and determine the value of the new String
                        //with the data, once correctly formatted, we could skip storing in the arraylist
                        //and just automatically write it to the cells of a new excel file.
                        break;
                    case "UPC":
                        //upc = getUPC(currentCell);
                        output(getUPC(currentCell));
                        break;
                    case "MSRP":
                        //msrp = getMSRP(currentCell);
                        output(getMSRP(currentCell));
                        break;
                    //case "BRAND":
                        //brand = currentCell;

                      //  break;
                    default :
                        output(currentCell);
                        break;
                }
            }
            line = read.readLine();
        }
    }
    private static Map<String, String> createMap() {
        Map<String, String> myMap = new HashMap<String, String>();
        myMap.put("WITH ", "W/");myMap.put("AND ", "& ");myMap.put("HIGH ", "HI ");myMap.put("LOW ", "LO ");
        myMap.put("AS ", null);myMap.put("SOME ", null);myMap.put("ZERO ", "0 ");myMap.put("ONE ", "1 ");
        myMap.put("TWO ", "2 ");myMap.put("THREE ", "3 ");myMap.put("FOUR ", "4 ");myMap.put("FIVE ", "5 ");
        myMap.put("SIX ", "6 ");myMap.put("SEVEN ", "7 ");myMap.put("EIGHT ", "8 ");myMap.put("NINE ", "9 ");
        myMap.put("ULTRA ", "ULT ");myMap.put("ANTI-OXIDANT ", "ANTI-OXI ");myMap.put("UNSWEETENED ", "UNSWTND ");
        myMap.put("UNSWTND ", "UNSWT ");myMap.put("GRAPEFRUIT ", "GRPFRUT");myMap.put("GRPFRUT ", "GRPFRT");
        myMap.put("CALCIUM ", "CLCM ");myMap.put("POTASSIUM ", "POTASSM ");myMap.put("POTASSM ", "POTASM ");
        myMap.put("MAGNESIUM ", "MAGN ");myMap.put("MAGN ", "MGN ");myMap.put("REDUCED ", "REDUX ");
        myMap.put("SODIUM ", "SODM ");myMap.put("SODM ", "SOD ");myMap.put("COMPLEX ", "CMPLX ");
        myMap.put("CMPLX ", "COMP ");myMap.put("LOZENGE ", "LZNG ");myMap.put("VITAMIN ", "VIT ");myMap.put("PURPOSE ", "PURP ");
        myMap.put("MIXED ", "MIX ");myMap.put("ACTIVATED ", "ACTVTED ");/*NOT SURE IF THIS ONE WORKS???*/
        myMap.put("ALCOHOL ", "ALC ");myMap.put("FREE ", "FR ");myMap.put("VEGETARIAN ", "VEG ");myMap.put("CONCENTRATE ", "CONC ");
        myMap.put("VEGGIE ", "VEG ");myMap.put("CONCENTRATE ", "CONC ");myMap.put("DAILY ", "DLY ");myMap.put("ONCE ", "1");
        myMap.put("TWICE ", "2");myMap.put("DOUBLE ", "DBL ");myMap.put("METABOLISM ", "METABLSM ");myMap.put("METABLSM ", "MTBLSM");
        myMap.put("ARGAN ", "ARGN ");myMap.put("ARGN ", "ARG ");myMap.put("FACIAL ", "FACL ");myMap.put("FACL ", "FAC ");
        myMap.put("TREATMENT ", "TRTMNT ");myMap.put("CHAMOMILE ", "CHAM ");myMap.put("SUSTAINED ", "SUST ");
        myMap.put("RELEASE ", "REL ");

        return myMap;
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

    /** rowWrite
     *  helper method that gathers and checks all data before it's written to the output file. this method ensures
     *  the data for each product is self-aware, somewhat.
     */
    private void rowWrite() {
     //   output(upc + "\t" + getReceiptAlias(alias, ) );
    }

    /** output
     *
     * helper method for readability
     * just writes a complete string to output file,
     *
     * @param append
     * @throws IOException
     */
    private void output(String append) throws IOException {
        toTSV.append(append+"\t");
    }

    /** getUPC(String)
     *
     *  ensures each UPC is at least 12 digits and adds the check digit
     *
     * @param UPC
     * @return
     */
    private String getUPC(String UPC) {
        String newUPC = "";
        switch (UPC.length()) {
            case 11:
                newUPC = UPC + checkDigit(UPC);
                break;
            case 12:
                newUPC = UPC;
                break;
            default:
                if (UPC.length() < 11) {
                    //only UPCs of length 11 have shown to not include the check digit
                    for (int i = 1; i < (12 - UPC.length()); i++) { newUPC += "0"; }
                    newUPC += UPC;
                } else {
                    newUPC = UPC;
                }
        }
        return newUPC;
    }

    /** checkDigit(String)
     *
     * takes UPC as a String, then computes and returns the check digit as a char
     *
     * @param UPC
     * @return checkDigChar
     */
    private char checkDigit(String UPC) {
        char checkDigChar;
        int odd = 0;
        int even = 0;
        int total;
        //loop for odd digits
        for (int i = 0; i < UPC.length(); i+=2) {
            odd += ((int)UPC.charAt(i)-48);
        } odd *= 3;
        //loop for even digits
        for (int i = 1; i < UPC.length(); i+=2) {
            even += ((int)UPC.charAt(i)-48);
        } total = (odd + even) % 10;
        if (total == 0) return '0';
        checkDigChar = ((char) ((10 - total) + 48));
        return checkDigChar;
    }

    /** getReceiptAlias(String)
     *
     * this method takes a String value and returns the minimized receipt alias
     *
     * @param cellValue
     */
    private String getReceiptAlias(String cellValue, String brand) {
        if ( cellValue.length() <= 32 ){ return cellValue.toUpperCase(); }
        String originalvalue = cellValue.toUpperCase();
        String newString = "";
        String[] parsed = originalvalue.split(" ");
        int in = 0;
        int NUM_REMOVE = cellValue.length() - 32;
        System.out.println(NUM_REMOVE + " " + parsed.length + " " + parsed[0]);
        String[] chars = {"A", "E","I","O","U"};
        /*
        oh god, this is a mess, please simplify this so it's legible
        */
        for (int i = 0; i < parsed.length; i++) {
            String sub = parsed[i];
            if (sub == brand) { NUM_REMOVE -= brand.length(); continue; }
            if (aliases.containsKey(sub)) {
                newString += aliases.get(sub + " ") + " ";
            } else {
                in = 0;
                String temp = sub;
                while (NUM_REMOVE > 0) {
                    int val = temp.lastIndexOf(chars[in]);
                    temp = temp.substring(0, val-1) + temp.substring(val, sub.length());
                    in = in == chars.length-1 ? 0 : (in + 1);
                    NUM_REMOVE--;
                }
                newString += temp + " ";
            }
        }
        return newString;
    }

    private String getMSRP(String msrp) {
        String newMSRP = "";
        String[] pieces = msrp.split(".", 1);
        newMSRP += pieces[0] + ".";
        int val = ((Integer.parseInt(pieces[1])+5)/10)*10;
        return newMSRP;
    }

     public static void main(String[] args) throws IOException {
        //String fileName = args[0];
        catalog wb = new catalog();
        //row test = new row(new BufferedWriter(new File("hes.txt")));
        String test = "this and that testestestestestetest";
        System.out.println(test.length());
        System.out.println(wb.getReceiptAlias(test, "hello"));

        //catalog test = new catalog();
        //System.out.println(test.checkDigit("03600024147"));
    }
}
