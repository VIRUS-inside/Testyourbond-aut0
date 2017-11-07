package org.openqa.selenium;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




























public enum Platform
{
  WINDOWS(new String[] { "" }), 
  




  XP(new String[] { "Windows Server 2003", "xp", "windows", "winnt" }), 
  








  VISTA(new String[] { "windows vista", "Windows Server 2008", "windows 7", "win7" }), 
  








  WIN8(new String[] { "Windows Server 2012", "windows 8", "win8" }), 
  





  WIN8_1(new String[] { "windows 8.1", "win8.1" }), 
  





  WIN10(new String[] { "windows 10", "win10" }), 
  





  MAC(new String[] { "mac", "darwin", "os x" }), 
  
  SNOW_LEOPARD(new String[] { "snow leopard", "os x 10.6" }), 
  









  MOUNTAIN_LION(new String[] { "mountain lion", "os x 10.8" }), 
  









  MAVERICKS(new String[] { "mavericks", "os x 10.9" }), 
  









  YOSEMITE(new String[] { "yosemite", "os x 10.10" }), 
  









  EL_CAPITAN(new String[] { "el capitan", "os x 10.11" }), 
  









  SIERRA(new String[] { "sierra", "macos 10.12" }), 
  












  UNIX(new String[] { "solaris", "bsd" }), 
  
  LINUX(new String[] { "linux" }), 
  





  ANDROID(new String[] { "android", "dalvik" }), 
  








  ANY(new String[] { "" });
  



  private final String[] partOfOsName;
  

  private int minorVersion = 0;
  private int majorVersion = 0;
  private static Platform current;
  
  private Platform(String... partOfOsName) { this.partOfOsName = partOfOsName; }
  
  public String[] getPartOfOsName()
  {
    return partOfOsName;
  }
  






  public static Platform getCurrent()
  {
    if (current == null) {
      current = extractFromSysProperty(System.getProperty("os.name"));
      
      String version = System.getProperty("os.version", "0.0.0");
      int major = 0;
      int min = 0;
      
      Pattern pattern = Pattern.compile("^(\\d+)\\.(\\d+).*");
      Matcher matcher = pattern.matcher(version);
      if (matcher.matches()) {
        try {
          major = Integer.parseInt(matcher.group(1));
          min = Integer.parseInt(matcher.group(2));
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      

      currentmajorVersion = major;
      currentminorVersion = min;
    }
    return current;
  }
  







  public static Platform extractFromSysProperty(String osName)
  {
    return extractFromSysProperty(osName, System.getProperty("os.version"));
  }
  








  public static Platform extractFromSysProperty(String osName, String osVersion)
  {
    osName = osName.toLowerCase();
    
    if ("dalvik".equalsIgnoreCase(System.getProperty("java.vm.name"))) {
      return ANDROID;
    }
    
    if ((osVersion.equals("6.2")) && (osName.startsWith("windows nt"))) {
      return WIN8;
    }
    
    if ((osVersion.equals("6.3")) && (osName.startsWith("windows nt"))) {
      return WIN8_1;
    }
    Platform mostLikely = UNIX;
    String previousMatch = null;
    for (Platform os : values()) {
      for (String matcher : partOfOsName) {
        if (!"".equals(matcher))
        {

          matcher = matcher.toLowerCase();
          if (os.isExactMatch(osName, matcher)) {
            return os;
          }
          if ((os.isCurrentPlatform(osName, matcher)) && (isBetterMatch(previousMatch, matcher))) {
            previousMatch = matcher;
            mostLikely = os;
          }
        }
      }
    }
    
    return mostLikely;
  }
  




  public static Platform fromString(String name)
  {
    try
    {
      return valueOf(name);
    } catch (IllegalArgumentException ex) {
      for (Platform os : values()) {
        for (String matcher : partOfOsName) {
          if (name.toLowerCase().equals(matcher.toLowerCase())) {
            return os;
          }
        }
      }
      throw new WebDriverException("Unrecognized platform: " + name);
    }
  }
  







  private static boolean isBetterMatch(String previous, String matcher)
  {
    return (previous == null) || (matcher.length() >= previous.length());
  }
  







  public boolean is(Platform compareWith)
  {
    return (this == compareWith) || (family().is(compareWith));
  }
  





  public Platform family()
  {
    return ANY;
  }
  
  private boolean isCurrentPlatform(String osName, String matchAgainst) {
    return osName.contains(matchAgainst);
  }
  
  private boolean isExactMatch(String osName, String matchAgainst) {
    return matchAgainst.equals(osName);
  }
  




  public int getMajorVersion()
  {
    return majorVersion;
  }
  




  public int getMinorVersion()
  {
    return minorVersion;
  }
}
