public class row{
    private String UPC = "";
    private String ItemName = "";
    private String ReceiptAlias = "";
    private String Brand = "";
    private String MSRP = "";
    private String Size = "";
    private String[] rowdata;

    public row(String rowd) {
        rowdata = rowd.split("\t");

    }
}