package src;

import org.jsoup.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.net.SocketTimeoutException;

public class brandScrape
{
    private String url= "https://www.nelsonwholesale.com/items/";
    private String userAgent = "";
    private String suid = "";
    private String pageHTML = "";
    private String brand = "";
    private String brandLoc = "itemprop=\"name\">";
    //private String imagepath = System.getProperty("user.dir")+"/images"; <-not sure if this works
    private ArrayList<ArrayList<String>> possibleBrands = new ArrayList<ArrayList<String>>();
    private ArrayList<String> links = new ArrayList<String>();

    public brandScrape(String upc) throws Exception
    {
        this.suid=upc;  
        fetch();  
    }

    public void fetch() throws Exception
    {        
        try{                 
            brand = findBrand(Jsoup.connect(url+suid)
                .userAgent(new RandomUserAgent()
                .getRandomUserAgent())
                .timeout(30*1000)
                .followRedirects(false)
                .ignoreHttpErrors(true)
                .get().toString()
            );            
        } catch (SocketTimeoutException e) {
            System.err.println("Socket timed out, trying again...");
            Thread.sleep(2000);
            fetch();
        }
    }

    public String findBrand(String html) {
        if (html.contains(brandLoc)) {
            String br = html.split(brandLoc)[1].split("</span>")[0];            
            if (br.contains(":")) {
                String parse = br.split(":")[0];
                System.out.println("Got: "+parse);
                return parse;
            } else {
                return " ";
            }
        }
        return " ";
    }

    public String getBrand() { return this.brand; }

    public static void main(String[] args) throws Exception
    {        
        File f = new File(args[0]);
        Scanner file = new Scanner(f);        
        PrintWriter o = new PrintWriter("out.txt");
        String current;
        while ((current = file.next()) != null) {            
            o.println(new brandScrape(current).getBrand().toUpperCase());
            Thread.sleep((long) (Math.random() * 4000));
            o.flush();
        }
        file.close();
        System.out.println("DONE");
    }
}