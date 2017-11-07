package com.beust.jcommander.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DefaultConsole implements Console
{
  public DefaultConsole() {}
  
  public void print(String msg)
  {
    System.out.print(msg);
  }
  
  public void println(String msg) {
    System.out.println(msg);
  }
  
  public char[] readPassword(boolean echoInput)
  {
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(isr);
      String result = in.readLine();
      return result.toCharArray();
    }
    catch (java.io.IOException e) {
      throw new com.beust.jcommander.ParameterException(e);
    }
  }
}
