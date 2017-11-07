package org.openqa.grid.shared;

import java.io.PrintStream;


















public class CliUtils
{
  public CliUtils() {}
  
  public static void printWrappedLine(String prefix, String msg)
  {
    printWrappedLine(System.out, prefix, msg, true);
  }
  
  public static void printWrappedLine(PrintStream output, String prefix, String msg, boolean first) {
    output.print(prefix);
    if (!first) {
      output.print("  ");
    }
    int defaultWrap = 70;
    int wrap = defaultWrap - prefix.length();
    if (wrap > msg.length()) {
      output.println(msg);
      return;
    }
    String lineRaw = msg.substring(0, wrap);
    int spaceIndex = lineRaw.lastIndexOf(' ');
    if (spaceIndex == -1) {
      spaceIndex = lineRaw.length();
    }
    String line = lineRaw.substring(0, spaceIndex);
    output.println(line);
    printWrappedLine(output, prefix, msg.substring(spaceIndex + 1), false);
  }
}
