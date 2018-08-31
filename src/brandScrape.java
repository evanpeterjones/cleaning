package src;

import com.jaunt.*;
import java.util.ArrayList;

public class brandScrape
{
    private String du = "https://duckduckgo.com/?q=";
    private String ck = "&t=hi&ia=web";
    private String userAgent = "";
    private String upc = "";
    private String brand = "";
    private ArrayList<ArrayList<String>> possibleBrands = new ArrayList<ArrayList<String>>();
    private ArrayList<String> links = new ArrayList<String>();

    public brandScrape(String upc)
    {
        this.upc=upc;
        userAgent = new RandomUserAgent().getRandomUserAgent();
    }

    public String runAndReturn()
    {
        try
        {
            UserAgent s = new UserAgent();
            s.visit(du + upc + ck);
            System.out.println(s.doc.innerHTML());
            // complete a duck_duck_go search and create array of url links ?/maybe store each description to compare?
            // loop through array
        } catch (JauntException e)
        {
            System.err.println("Error: " + e);
        }

        return brand;
    }

    private void duck()
    {
        String url = "";
        //search duck duck go, and append urls to array
        links.append();
    }

    public static void main(String[] args)
    {
        brandScrape foo = new brandScrape(args[0]);
        System.out.println(foo.runAndReturn());
    }
}