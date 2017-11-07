package org.openqa.selenium.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;


















public class BuildInfo
{
  public BuildInfo() {}
  
  private static final Properties BUILD_PROPERTIES = ;
  
  private static Properties loadBuildProperties() {
    Properties properties = new Properties();
    
    Manifest manifest = null;
    JarFile jar = null;
    ZipEntry entry;
    try { URL url = BuildInfo.class.getProtectionDomain().getCodeSource().getLocation();
      File file = new File(url.toURI());
      jar = new JarFile(file);
      entry = jar.getEntry("META-INF/build-stamp.properties");
      if (entry != null) {
        InputStream stream = jar.getInputStream(entry);Throwable localThrowable3 = null;
        try { properties.load(stream);
        }
        catch (Throwable localThrowable1)
        {
          localThrowable3 = localThrowable1;throw localThrowable1;
        } finally {
          if (stream != null) if (localThrowable3 != null) try { stream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else stream.close();
        }
      }
      manifest = jar.getManifest();
      





      if (jar != null) {
        try {
          jar.close();
        }
        catch (IOException localIOException) {}
      }
      


      if (manifest != null) {
        break label224;
      }
    }
    catch (IllegalArgumentException|IOException|NullPointerException|URISyntaxException localIllegalArgumentException) {}finally
    {
      if (jar != null) {
        try {
          jar.close();
        }
        catch (IOException localIOException2) {}
      }
    }
    


    return properties;
    try
    {
      label224:
      Attributes attributes = manifest.getAttributes("Build-Info");
      Set<Map.Entry<Object, Object>> entries = attributes.entrySet();
      for (Map.Entry<Object, Object> e : entries) {
        properties.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
      }
    }
    catch (NullPointerException localNullPointerException) {}
    

    return properties;
  }
  
  public String getReleaseLabel()
  {
    return BUILD_PROPERTIES.getProperty("Selenium-Version", "unknown").trim();
  }
  
  public String getBuildRevision()
  {
    return BUILD_PROPERTIES.getProperty("Build-Revision", "unknown");
  }
  
  public String getBuildTime()
  {
    return BUILD_PROPERTIES.getProperty("Build-Time", "unknown");
  }
  
  public String toString()
  {
    return String.format("Build info: version: '%s', revision: '%s', time: '%s'", new Object[] {
      getReleaseLabel(), getBuildRevision(), getBuildTime() });
  }
}
