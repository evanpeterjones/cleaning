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

public class row {
    private String item = "";
    private String upc = "";
    private String alias = "";
    private String msrp = "";
    private String brand = "";
    private String def = "";
    private boolean isOrganic = false;
    private static final Map<String, String> aliases = createMap();
    private static String[] chars = {"I","A","O","U","E"};
    private String line = ""; //to be returned
    private boolean createAlias=false;

    public row() {
        //just to test
    }
    /** row
     * Constructor 
     * 
     * @param ORIGINAL String[] column values parsed by '\t'
     * @param columnNames String[] column name order
     */
    public row(String[] ORIGINAL, List<String> columnNames, boolean ca) {                
        createAlias=ca;
        String currentCell = "";
        for (String cell : columnNames) {
            currentCell = ORIGINAL[columnNames.indexOf(cell)];       
            //System.out.println("Array Index: "+columnNames.indexOf(cell)+" cell:"+cell+ " "+currentCell);
            switch (cell) {
                case "LENGTH": break;
                case "BRAND":
                    brand = currentCell;
                    //once fixed this will be changed to something like...
                    //brand = (currentCell==null) ? new brandScrape(this.upc).runAndReturn() : currentCell;
                    break;
                case "ITEM NAME":
                    System.out.println("Current Item: "+currentCell);    
                    item = currentCell.replace("^[0-9a-zA-Z]","");
                    if (createAlias) {
                        isOrganic = currentCell.contains("[ ORG.]");
                        alias = getReceiptAlias(currentCell.replace("^[0-9a-zA-Z]","").replace("[ ORG.]", ""));
                        System.out.println("Manipulated: "+alias);
                    }
                    break;
                case "RECEIPT ALIAS":
                    //this will be changed separately                                        
                    isOrganic = currentCell.contains(" ORG.*");
                    alias = getReceiptAlias(currentCell.replace("^[0-9a-zA-Z]","").replace(" ORG.*", ""));
                    break;
                case "UPC":
                    upc = getUPC(currentCell);
                    break;
                case "MSRP":
                    msrp = getMSRP(currentCell);
                    break;
                default:
                    def += currentCell+"\t";
                    break;
            }
        }
        createLine(columnNames);
    }

    public void createLine(List<String> order) {
        String[] unchanged = def.split("\t");  
        int len = unchanged.length;
        int default_index = 0;
        for (String cell : order) {
            switch (cell) {
                case "LENGTH": break;
                case "BRAND" :  
                    line += brand+"\t";
                    break;
                case "UPC":
                    line += upc+"\t";
                    break;
                case "MSRP":
                    line += msrp+"\t";
                    break;
                case "RECEIPT ALIAS":
                    line += alias+"\t";
                    break;
                case "ITEM NAME":
                    line += item+"\t";
                    if (createAlias) {
                        line += alias+"\t";
                    }
                    break;
                default:
                    if (default_index<len) {
                        line += unchanged[default_index] + "\t";
                    }
                    default_index++;
                    break;
            }
        }
    }

    /** createMap
     *  <?> replace with an enum <?>
     */
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
        myMap.put("MIXED ", "MIX ");myMap.put("ALCOHOL ", "ALC ");myMap.put("FREE ", "FR ");myMap.put("VEGETARIAN ", "VEG ");
        myMap.put("CONCENTRATE ", "CONC ");myMap.put("VEGGIE ", "VEG ");myMap.put("CONCENTRATE ", "CONC ");
        myMap.put("DAILY ", "DLY ");myMap.put("ONCE ", "1");myMap.put("TWICE ", "2");myMap.put("DOUBLE ", "DBL ");
        myMap.put("METABOLISM ", "METABLSM ");myMap.put("METABLSM ", "MTBLSM");myMap.put("ARGAN ", "ARGN ");
        myMap.put("ARGN ", "ARG ");myMap.put("FACIAL ", "FACL ");myMap.put("FACL ", "FAC ");
        myMap.put("TREATMENT ", "TRTMNT ");myMap.put("CHAMOMILE ", "CHAM ");myMap.put("SUSTAINED ", "SUST ");
        myMap.put("RELEASE ", "REL ");myMap.put("MOZZARELLA ", "MOZZ ");myMap.put("VANILLA ", "VAN ");
        return myMap;
    }

    private String removeLead(String start) {
        for (int stInd = 0; stInd < start.length(); stInd++) {
            if (start.charAt(stInd) != '0') {
                return start.substring(stInd, start.length());
            }
        }
        return start;
    }
    /** getUPC(String)
     *
     *  ensures each UPC is at least 12 digits and adds the check digit
     *
     * @param UPC
     * @return
     */

    private String getUPC(String theUPC) {
        String UPC = (theUPC.charAt(0) == '0' && theUPC.length() == 12) ? removeLead(theUPC) : theUPC;
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
        if (UPC.length() != 11) { System.out.println("ERROR, UPC LENGTH"); System.exit(1); }
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
     * @param numRemoved
     * @return
     */
    private String receiptBrute(String full, int numRemoved) {
        String[] sentence = full.split(" ");
        String newStr = "";
        int shortest = 0;
        String word = "";
        //for (String word : sentence) {
        for (int i = sentence.length-1; i > 0; i--) {
            shortest = (sentence[i].length() < sentence[shortest].length()) ? i : shortest;
        }
        for (int j = 0; j < sentence.length; j++) {
            if (j != shortest) {
                newStr += j < sentence.length ? sentence[j]+" " : sentence[j];
            }
        }
        if (newStr.length() > 32) {
            return newStr; //receiptBrute(newStr, numRemoved+1);
            //should assume more criteria, so the length of string and number of characters to remove is
            //taken into account
        }
        return newStr;
    }

    /** getReceiptAlias(String)
     *
     * this method takes a String value and returns the minimized receipt alias
     *
     * @param cellValue
     */
    private String getReceiptAlias(String cellValue) {
        String newString = "";
        if (cellValue.length() <= 32 ){ return cellValue.replaceAll("[^0-9A-Za-z\\. ]",""); }
        if (cellValue.contains(" ORGANIC")) {
            cellValue = cellValue.replace(" ORGANIC", "");
            this.isOrganic = true;
        }
        cellValue = cellValue.contains(brand) ? cellValue.replace(brand+" ", "") : cellValue;
        String[] parsed = cellValue.replaceAll("[^0-9A-Za-z\\. ]","").split(" " );

        int NUM_REMOVE = cellValue.length() - 32;
        int numActRemoved = 0;
        for (int i = 0; i < parsed.length; i++) {
            String sub = (i == parsed.length-1) ? parsed[i].replace("\n", "") : parsed[i];
            //Regex to fix year
            if (sub.matches("^[1-2][0-9]{3}$")) { NUM_REMOVE-=2; newString += " "+sub.substring(2,4); continue;}
            //if substring already has an alias, concatenate that and continue
            if (aliases.get(sub+" ") != null) {
                String fromAlias = aliases.get(sub + " ");
                newString += fromAlias;
                NUM_REMOVE -= sub.length() - fromAlias.length();
                continue;
            }
            //otherwise this mess... remove vowels ?
            else {
                String temp = sub;
                int numRemoved = 0;
                for (String ch : chars) {
                    int v = temp.lastIndexOf(ch);
                    //excludes first and last character
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
        return newString.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll("  "," ");
    }
    private void setBrand(String a) { this.brand = a; }

    private String getMSRP(String msrp) {
        //THIS METHOD IS SO UNDREADABLE
        if (msrp.length() == 0)
            return "";
        String[] pieces = msrp.split("\\.");
        String newstr= "";
        switch(pieces.length) {
            case 1 :
                // if MSRP is an integer value
                //($125) 12                                              (5-1)
                newstr = pieces[0].substring(0,pieces[0].length()-1)+""+((char)(pieces[0].charAt(pieces[0].length()-1)-1));
                //     124       .99
                return newstr + ".99";
            case 2:
                newstr = pieces[0] + ".";
                //accounts for cases where msrp decimal is incomplete "12.7"
                newstr += (pieces[1].length()==2 && pieces[1].charAt(0) != '0') ?
                        (((Integer.parseInt(pieces[1].substring(0,2))+5)/10)-1)+"9":
                        ((((Integer.parseInt(pieces[1])*10)+5)/10)-1)+"9";
                return newstr;
            default :
                return "";
        }
    }

    public String getLine() {
        return line;
    }

    public static void main(String[] args) throws IOException {
        row wb = new row();
       /* System.out.println(wb.getMSRP("11.43"));
        System.out.println(wb.getMSRP("11.65"));
*/
        wb.setBrand("BROUWERIJ VERHAEGHE");
        String test = "BROUWERIJ VERHAEGHE DUCHESSE DE BOURGOGNE FLEMISH RD 2006\n";
        String tst = "TORTILLA WHITE FLOUR 12 12 CT FOOD SERVICE STACEYS ORGANIC TORTILLAS\n";
        String ret = wb.getReceiptAlias(test);
        String rt = wb.getReceiptAlias(tst);
        System.out.println("Start Phrase: " + test.length() + "\n" + test);
        System.out.println("End Phrase: " + ret.length() + "\n" + ret);

        System.out.println("Start Phrase: " + tst.length() + "\n" + tst);
        System.out.println("End Phrase: " + rt.length() + "\n" + rt);

        String the = "071537075403	POLAR	4.12	1007540	POLAR	EA	1	CARBO	3GJ	WATER	CARB	POL PURIFIED 3GAL CARBOY	POL PURIFIED 3GAL CARBOY	071537075403	071537075403	4.12";
        String ts = "UPC	BRAND	COST EACH	SUID	VENDOR	UOM	CASE PK	SIZE	PACK SIZE	Category	FLAVOR	ITEM NAME	RECEIPT ALIAS	UNITUPC	CASEUPC	CASE PRICE";
        row t = new row(the.split("\t"), Arrays.asList(ts.split("\t")), false);
        System.out.println("\n"+t.def);
        System.out.println(t.getLine());
        //catalog test = new catalog();
        //System.out.println(test.checkDigit("03600024147"));

    }
}