package org.apache.xalan.xsltc;

import java.io.PrintStream;

































public class ProcessorVersion
{
  public ProcessorVersion() {}
  
  private static int MAJOR = 1;
  private static int MINOR = 0;
  private static int DELTA = 0;
  
  public static void main(String[] args) {
    System.out.println("XSLTC version " + MAJOR + "." + MINOR + (DELTA > 0 ? "." + DELTA : ""));
  }
}
