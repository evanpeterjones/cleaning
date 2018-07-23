package src;

/** the Cata-log Program
 *
 *  This is a program that reads excel files and converts the data so
 *  they are ready for import to the Universal Product Database.
 *  The name should be read like "catapult", but catalog, lol!!
 *
 * @version 0.9.1
 * @author Evan Jones
 *
 */
import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class catalog {
    private String filePath;
    //private Sheet sheet;
    public static final Map<String, String> aliases = createMap();
    //following variables store column #s from the excel file
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
        //readCatalog(wb);
        read(read);
    }
    public catalog() {
        //this is for testing methods
        //remove this;
    }
    public void read(BufferedReader read) throws IOException {
        String line = read.readLine();
        toTSV.append(line+"\n");
        String columvals[] = line.split("\t");
        line = read.readLine();
        String row[];
        String currentCell = "";
        char aliasRowChar = (char)(Arrays.asList(columvals).indexOf("RECEIPT ALIAS")+65);
        int rownum = 0;

        while (line != null) {
            row = line.split("\t");
            rownum++;
            for (int i = 0; i < row.length; i++) {
                currentCell = row[i];
                switch (columvals[i].toUpperCase()) {
                    case "LENGTH":
                        //UNCOMMENT THIS TO TEST RECEIPT ALIAS LENGTH OUTPUT
                       // output("=if(len("+aliasRowChar+rownum+")>32, len("+aliasRowChar+rownum+"),\".\")");
                        break;
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
            toTSV.append("\n");
            line = read.readLine();
        }
    }
    private static Map<String, String> createMap() {
        Map<String, String> myMap = new HashMap<String, String>();
        myMap.put("WITH ", "W/");myMap.put("AND ", "& ");myMap.put("HIGH ", "HI ");myMap.put("LOW ", "LO ");
        myMap.put("AS ", "");myMap.put("SOME ", "");myMap.put("ZERO ", "0 ");myMap.put("ONE ", "1 ");
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
        myMap.put("RELEASE ", "REL ");myMap.put("DE ", "");

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
    private String getUPC(String theUPC) {
        String UPC = (theUPC.charAt(0) == '0' && theUPC.length() == 12) ? (theUPC.substring(theUPC.length() - 11,
                theUPC.length())) : theUPC;
        String newUPC = "";
        switch (UPC.length()) {
            case 11:
                newUPC = UPC + checkDigit(UPC);
                break;
            default:
                if (UPC.length() < 11) {
                    //only UPCs of length 11 have shown to not include the check digit
                    for (int i = 0; i < (11 - UPC.length()); i++) { newUPC += "0"; }
                    newUPC += UPC;
                    newUPC += checkDigit(newUPC);
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
        if (UPC.length() != 11) { System.out.println("ERROR, UPC LENGTH"); }
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

    /** receiptBrute(String, int)
     *
     * Recursively remove shortest words from String
     *
     * @param full
     * @param run
     * @return
     */
    private String receiptBrute(String full, int run) {
        int rem = full.length() - 32;
        String[] sentence = full.split(" ");
        String newStr = "";
        int shortest = 0;
        int index =0;
        for (String word : sentence) {
            if (word.length() < sentence[shortest].length()) {
                shortest = index;
            } index++;
        }
        for (int j = 0; j < sentence.length; j++) {
            if (j != shortest) {
                newStr += j != sentence.length-1 ? sentence[j]+" " : sentence[j];
            }
        }
        if (newStr.length() > 32 && run < 5) {
            newStr = receiptBrute(newStr, run+1);
        }
        return newStr;
    }

    /** getReceiptAlias(String)
     *
     * this method takes a String value and returns the minimized receipt alias
     *
     * @param cellValue
     */
    private String getReceiptAlias(String cellValue, String brand) {
        if ( cellValue.length() <= 32 ){ return cellValue.toUpperCase(); }
        String newString = "";
        String[] parsed = cellValue.toUpperCase().split(" ");
        int NUM_REMOVE = cellValue.length() - 32;
        int track = NUM_REMOVE;
        int numActRemoved = 0;
        String[] chars = {"A","E","I","O","U"};

        /*
        oh god, this is a mess, please simplify this so it's legible
        */
        // TODO: add a check for years, so "2018" become "18"
        for (int i = 0; i < parsed.length; i++) {
            String sub = (i == parsed.length-1) ? parsed[i].replace("\n", "") : parsed[i];
            //if substring is the brand, skip concatenating it
            if (sub.equals(brand)) { NUM_REMOVE -= brand.length()+1; continue; }
            if (sub.matches("^[1-2][0-9]{3}$")) { NUM_REMOVE-=2; newString += " "+sub.substring(2,4); continue;}
            //if substring already has an alias, concatenate that and continue
            if (aliases.get(sub+" ") != null) {
                String fromAlias = aliases.get(sub + " ");
                newString += fromAlias;
                NUM_REMOVE -= sub.length() - fromAlias.length();
               // System.out.println("Hashmap is working! Saved " + fromAlias.length() + " many characters!!");
                continue;
            }
            //otherwise this fucking mess... remove vowels? ?
            else {
                String temp = sub;
                int numRemoved = 0;
                for (String ch : chars) {
                    int v = temp.lastIndexOf(ch);
                    if (v > 0 && v < temp.length()-1 && NUM_REMOVE > 0) {
                        temp = temp.substring(0,v) + (temp.substring(v+1, temp.length()));
                        numRemoved++;
                        NUM_REMOVE--;
                    }
                }
                newString+= i==0 ? temp : " "+temp;
                numActRemoved += numRemoved;
            }
        }
        if (NUM_REMOVE > 0) {
            return receiptBrute(newString, 0);
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
        catalog wb = new catalog(args[0]);
        /*catalog wb = new catalog();

        String value = "BROUWERIJ VERHAEGHE DUCHESSE DE BOURGOGNE FLEMISH RD 6";
        String a = wb.getReceiptAlias(value, "test");
        System.out.println(a + " " + a.length());
        */
        // TESTING
         /*
        String test = "BROUWERIJ VERHAEGHE DUCHESSE DE BOURGOGNE FLEMISH RD 2006\n";
        String ret = wb.getReceiptAlias(test, "JACKSON");
        System.out.println("Start Phrase: " + test.length() + "\n" + test);
        System.out.println("End Phrase: " + ret.length() + "\n" + ret);

        //catalog test = new catalog();
        //System.out.println(test.checkDigit("03600024147"));
        */
    }
}
