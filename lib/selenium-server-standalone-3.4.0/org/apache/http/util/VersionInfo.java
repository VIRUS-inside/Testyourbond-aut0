package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;



































































public class VersionInfo
{
  public static final String UNAVAILABLE = "UNAVAILABLE";
  public static final String VERSION_PROPERTY_FILE = "version.properties";
  public static final String PROPERTY_MODULE = "info.module";
  public static final String PROPERTY_RELEASE = "info.release";
  public static final String PROPERTY_TIMESTAMP = "info.timestamp";
  private final String infoPackage;
  private final String infoModule;
  private final String infoRelease;
  private final String infoTimestamp;
  private final String infoClassloader;
  
  protected VersionInfo(String pckg, String module, String release, String time, String clsldr)
  {
    Args.notNull(pckg, "Package identifier");
    infoPackage = pckg;
    infoModule = (module != null ? module : "UNAVAILABLE");
    infoRelease = (release != null ? release : "UNAVAILABLE");
    infoTimestamp = (time != null ? time : "UNAVAILABLE");
    infoClassloader = (clsldr != null ? clsldr : "UNAVAILABLE");
  }
  






  public final String getPackage()
  {
    return infoPackage;
  }
  





  public final String getModule()
  {
    return infoModule;
  }
  





  public final String getRelease()
  {
    return infoRelease;
  }
  





  public final String getTimestamp()
  {
    return infoTimestamp;
  }
  







  public final String getClassloader()
  {
    return infoClassloader;
  }
  






  public String toString()
  {
    StringBuilder sb = new StringBuilder(20 + infoPackage.length() + infoModule.length() + infoRelease.length() + infoTimestamp.length() + infoClassloader.length());
    



    sb.append("VersionInfo(").append(infoPackage).append(':').append(infoModule);
    



    if (!"UNAVAILABLE".equals(infoRelease)) {
      sb.append(':').append(infoRelease);
    }
    if (!"UNAVAILABLE".equals(infoTimestamp)) {
      sb.append(':').append(infoTimestamp);
    }
    
    sb.append(')');
    
    if (!"UNAVAILABLE".equals(infoClassloader)) {
      sb.append('@').append(infoClassloader);
    }
    
    return sb.toString();
  }
  











  public static VersionInfo[] loadVersionInfo(String[] pckgs, ClassLoader clsldr)
  {
    Args.notNull(pckgs, "Package identifier array");
    List<VersionInfo> vil = new ArrayList(pckgs.length);
    for (String pckg : pckgs) {
      VersionInfo vi = loadVersionInfo(pckg, clsldr);
      if (vi != null) {
        vil.add(vi);
      }
    }
    
    return (VersionInfo[])vil.toArray(new VersionInfo[vil.size()]);
  }
  













  public static VersionInfo loadVersionInfo(String pckg, ClassLoader clsldr)
  {
    Args.notNull(pckg, "Package identifier");
    ClassLoader cl = clsldr != null ? clsldr : Thread.currentThread().getContextClassLoader();
    
    Properties vip = null;
    
    try
    {
      InputStream is = cl.getResourceAsStream(pckg.replace('.', '/') + "/" + "version.properties");
      
      if (is != null) {
        try {
          Properties props = new Properties();
          props.load(is);
          vip = props;
        } finally {
          is.close();
        }
      }
    }
    catch (IOException ex) {}
    

    VersionInfo result = null;
    if (vip != null) {
      result = fromMap(pckg, vip, cl);
    }
    
    return result;
  }
  











  protected static VersionInfo fromMap(String pckg, Map<?, ?> info, ClassLoader clsldr)
  {
    Args.notNull(pckg, "Package identifier");
    String module = null;
    String release = null;
    String timestamp = null;
    
    if (info != null) {
      module = (String)info.get("info.module");
      if ((module != null) && (module.length() < 1)) {
        module = null;
      }
      
      release = (String)info.get("info.release");
      if ((release != null) && ((release.length() < 1) || (release.equals("${pom.version}"))))
      {
        release = null;
      }
      
      timestamp = (String)info.get("info.timestamp");
      if ((timestamp != null) && ((timestamp.length() < 1) || (timestamp.equals("${mvn.timestamp}"))))
      {


        timestamp = null;
      }
    }
    
    String clsldrstr = null;
    if (clsldr != null) {
      clsldrstr = clsldr.toString();
    }
    
    return new VersionInfo(pckg, module, release, timestamp, clsldrstr);
  }
  














  public static String getUserAgent(String name, String pkg, Class<?> cls)
  {
    VersionInfo vi = loadVersionInfo(pkg, cls.getClassLoader());
    String release = vi != null ? vi.getRelease() : "UNAVAILABLE";
    String javaVersion = System.getProperty("java.version");
    
    return String.format("%s/%s (Java/%s)", new Object[] { name, release, javaVersion });
  }
}
