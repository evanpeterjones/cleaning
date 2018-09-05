package src;

import com.jaunt.*;
import java.util.ArrayList;
//import com.gargoylesoftware.htmlunit.*;

public class brandScrape
{
    private String du = "https://duckduckgo.com/?q=";
    private String ck = "&t=hi&ia=web";//&atb=v127-6ba&ia=answer
    private String userAgent = "";
    private String upc = "";
    private String brand = "";
    private ArrayList<ArrayList<String>> possibleBrands = new ArrayList<ArrayList<String>>();
    private ArrayList<String> links = new ArrayList<String>();

    public brandScrape(String upc)
    {
        this.upc=upc;    
    }

    public String runAndReturn() throws Exception
    {
        return "";
        //return s.doc.innerHTML();
        /*
        UserAgent userAgent = new UserAgent();      //create new userAgent (headless browser)
        userAgent.visit("http://google.com");       //visit google
        userAgent.doc.apply("butterflies");         //apply form input (starting at first editable field)
        userAgent.doc.submit("Google Search");      //click submit button labelled "Google Search"
        
        Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
        for(Element link : links) System.out.println(link.getAt("href"));
        // complete a duck_duck_go search and create array of url links ?/maybe store each description to compare?
        // loop through array*/
     
    }

    public static void main(String[] args) throws Exception
    {
        //brandScrape foo = new brandScrape(args[0]);
        UserAgent s = new UserAgent();
        s.settings.autoSaveAsHTML = true;
        //s.visit(du + upc + ck);
        s.visit("https://duckduckgo.com");
        //s.doc.apply( args[0] );
        //s.doc.submit("search__button");
        System.out.println(s.doc.innerHTML());
    }
}