package src;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileInputStream;

public class Test {

  private AutoCompleteInterface ac; 

  public static void main(String[] args) {
    if(args.length != 1){
      System.out.println("Usage: java Test <dictionary file>");
    } else {
      try{
        new Test(args);
      } catch(IOException e){
        System.out.println("Error opening dictionary file " + e.getMessage());
      }
    }
  }

  public Test(String[] args) throws java.io.IOException {
    ac = new AutoComplete();
    //read in the dictionary
    Scanner fileScan = new Scanner(new FileInputStream(args[0]));
    while(fileScan.hasNextLine()){
      String word = fileScan.nextLine();
      ac.add(word);
    }
    fileScan.close();
    testAutoComplete(); 
  }

  /**
   * Provide word suggestions for 
   * one character at a time user input.
   */
  private void testAutoComplete(){
    System.out.println("Testing autocomplete:");

    Scanner scan = new Scanner(System.in);
    StringBuilder currentString = new StringBuilder();
    char c;

    while(true){
      System.out.println("Enter one letter then press enter " +
                        "to get auto-complete suggestions (enter < to delete last character and . to stop) ...");
      while(true){     
        String input = scan.nextLine();
        if(input.length() == 0){
          System.out.println("Enter one letter then press enter " +
          "to get auto-complete suggestions (enter < to delete last character and . to stop) ...");
          continue;
        } 
        c = input.charAt(0);
        if(c == '.'){
          break;
        } 
        if(c == '<'){
          ac.retreat();
          currentString.deleteCharAt(currentString.length()-1);
        } else {
          currentString.append(c);
          ac.advance(c);
        }
        int nPredictions = ac.getNumberOfPredictions();
        if(nPredictions > 0){
          System.out.println(currentString.toString() + " --> " + ac.retrievePrediction() + " (" + ac.getNumberOfPredictions() + " predictions total)");
        } else {
          System.out.println("No predictions found for " + currentString.toString());
        }
      }
      if(!ac.isWord()){
        System.out.println("Do you want to add " + currentString.toString() + "? (y/n)");
        c = Character.toUpperCase(scan.nextLine().charAt(0));
        if(c == 'Y'){
          ac.add();
        }
      }

      System.out.println("Do you want to continue? (y/n)");
      c = Character.toUpperCase(scan.nextLine().charAt(0));
      ac.reset();
      currentString = new StringBuilder();
      if(c != 'Y'){
        break;
      }      
    }
    scan.close();
  }
}
