package org.apache.xerces.impl;

import java.io.PrintStream;

public class Version
{
  /**
   * @deprecated
   */
  public static String fVersion = "Xerces-J 2.11.0";
  private static final String fImmutableVersion = "Xerces-J 2.11.0";
  
  public Version() {}
  
  public static String getVersion()
  {
    return "Xerces-J 2.11.0";
  }
  
  public static void main(String[] paramArrayOfString)
  {
    System.out.println(fVersion);
  }
}
