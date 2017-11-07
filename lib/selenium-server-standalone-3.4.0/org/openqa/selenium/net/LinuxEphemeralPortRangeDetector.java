package org.openqa.selenium.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

















public class LinuxEphemeralPortRangeDetector
  implements EphemeralPortRangeDetector
{
  final int firstEphemeralPort;
  final int lastEphemeralPort;
  
  public static LinuxEphemeralPortRangeDetector getInstance()
  {
    File file = new File("/proc/sys/net/ipv4/ip_local_port_range");
    if ((file.exists()) && (file.canRead())) {
      Reader inputFil = null;
      try {
        inputFil = new FileReader(file);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
      return new LinuxEphemeralPortRangeDetector(inputFil);
    }
    return new LinuxEphemeralPortRangeDetector(new StringReader("49152 65535"));
  }
  
  LinuxEphemeralPortRangeDetector(Reader inputFil) {
    FixedIANAPortRange defaultRange = new FixedIANAPortRange();
    int lowPort = defaultRange.getLowestEphemeralPort();
    int highPort = defaultRange.getHighestEphemeralPort();
    try {
      BufferedReader in = new BufferedReader(inputFil);
      
      String s = in.readLine();
      String[] split = s.split("\\s");
      lowPort = Integer.parseInt(split[0]);
      highPort = Integer.parseInt(split[1]);
    }
    catch (IOException localIOException) {}
    firstEphemeralPort = lowPort;
    lastEphemeralPort = highPort;
  }
  
  public int getLowestEphemeralPort() {
    return firstEphemeralPort;
  }
  
  public int getHighestEphemeralPort() {
    return lastEphemeralPort;
  }
}
