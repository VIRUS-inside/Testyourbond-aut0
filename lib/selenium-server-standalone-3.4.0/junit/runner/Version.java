package junit.runner;

import java.io.PrintStream;


public class Version
{
  private Version() {}
  
  public static String id()
  {
    return "4.12";
  }
  
  public static void main(String[] args) {
    System.out.println(id());
  }
}
