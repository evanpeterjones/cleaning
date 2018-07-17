public class checkDigit {
  public static void main(String args[]) {
    checkDigit test = new checkDigit();
    String val = args[0];
    System.out.println("Starting UPC: " + val);
    System.out.println("Check Digit: " + test.check(val));
  }

  public checkDigit(){
    //nothing, tesing
  }

  public char check(String UPC) {
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
}
