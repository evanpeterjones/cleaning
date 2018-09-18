package src;

import org.jsoup.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.net.SocketTimeoutException;

public class imgScrape
{
    private String url= "https://www.nelsonwholesale.com";
    private String userAgent = "";
    private String suid = "";
    private String pageHTML = "";
    private String imgURL = "";
    private String upc = "";
    private String filetype = "";
    private String SUIDFiles = "";
    private String dir = "";
    private String brandLoc = "id=\"image01\"";
    //private String imagepath = System.getProperty("user.dir")+"/images"; <-not sure if this works
    private ArrayList<ArrayList<String>> possibleBrands = new ArrayList<ArrayList<String>>();
    private ArrayList<String> links = new ArrayList<String>();

    public imgScrape(String file) throws Exception
    {
        File f = new File(System.getProperty("user.dir")+"/images/");
        this.dir = f.mkdirs() ? f.getAbsolutePath() : null;
        this.SUIDFiles = file;        
        fetch("/items/"+this.suid);
    }

    public Connection.Response fetch(String i) throws Exception
    {        
        Connection.Response l = null;
        try{          
            l = Jsoup.connect(url+i)
                .userAgent(new RandomUserAgent()
                .getRandomUserAgent())
                .timeout(30*1000)
                .followRedirects(false)
                .ignoreHttpErrors(true)
                .execute();
            findImage(l.toString());
            return l;
        } catch (SocketTimeoutException e) {
            System.err.println("Socket timed out, trying again...");
            Thread.sleep(2000);
            fetch(i);
        }
        return l;
    }
    public void getUPC(String html) {
        this.upc = html.split("item-avail-sn'>")[1].split("</span")[0];
    }

    public void findImage(String val) throws Exception {
        getUPC(val);
        String html = val;
        if (html.contains(brandLoc)) {
            html = html.split(brandLoc)[1].split(">")[0];            
            if (!html.contains("DEFAULTIMAGE")) {
                html = html.split("\" data-zoom-image")[0];
                this.imgURL = html.split("src=\"")[1];
                this.filetype = imgURL.substring(imgURL.lastIndexOf('.'),imgURL.length());
                saveImage(this.imgURL);
            } else {
                return;
            }
        }
    }

    public void saveImage(String i) throws Exception, FileNotFoundException, IOException {
        Connection.Response s = fetch(this.url+i);
        FileOutputStream out = (new FileOutputStream(new java.io.File(dir+upc+filetype)));
        out.write(s.bodyAsBytes());
        out.close();
    }    

    public static void main(String[] args) throws Exception
    {        
        File f = new File(args[0]);
        Scanner file = new Scanner(f);                
        String current;
        while ((current = file.next()) != null) {            
            new imgScrape(current);
            Thread.sleep((long) (Math.random() * 4000));            
        }
        file.close();
        System.out.println("DONE");
    }
}