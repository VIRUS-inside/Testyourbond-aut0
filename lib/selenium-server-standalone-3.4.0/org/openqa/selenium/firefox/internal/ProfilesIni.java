package org.openqa.selenium.firefox.internal;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.io.TemporaryFilesystem;






















public class ProfilesIni
{
  private Map<String, File> profiles = Maps.newHashMap();
  
  public ProfilesIni() {
    File appData = locateAppDataDirectory(Platform.getCurrent());
    profiles = readProfiles(appData);
  }
  
  protected Map<String, File> readProfiles(File appData) {
    toReturn = Maps.newHashMap();
    
    File profilesIni = new File(appData, "profiles.ini");
    if (!profilesIni.exists())
    {
      return toReturn;
    }
    
    boolean isRelative = true;
    String name = null;
    String path = null;
    
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(profilesIni));
      
      String line = reader.readLine();
      
      while (line != null) {
        if (line.startsWith("[Profile")) {
          File profile = newProfile(name, appData, path, isRelative);
          if (profile != null) {
            toReturn.put(name, profile);
          }
          name = null;
          path = null;
        } else if (line.startsWith("Name=")) {
          name = line.substring("Name=".length());
        } else if (line.startsWith("IsRelative=")) {
          isRelative = line.endsWith("1");
        } else if (line.startsWith("Path=")) {
          path = line.substring("Path=".length());
        }
        
        line = reader.readLine();
      }
      




      File profile;
      




      File profile;
      



      return toReturn;
    }
    catch (IOException e)
    {
      throw new WebDriverException(e);
    } finally {
      try {
        if (reader != null) {
          profile = newProfile(name, appData, path, isRelative);
          if (profile != null) {
            toReturn.put(name, profile);
          }
          reader.close();
        }
      }
      catch (IOException localIOException2) {}
    }
  }
  


  protected File newProfile(String name, File appData, String path, boolean isRelative)
  {
    if ((name != null) && (path != null)) {
      File profileDir = isRelative ? new File(appData, path) : new File(path);
      return profileDir;
    }
    return null;
  }
  
  public FirefoxProfile getProfile(String profileName) {
    File profileDir = (File)profiles.get(profileName);
    if (profileDir == null) {
      return null;
    }
    

    File tempDir = TemporaryFilesystem.getDefaultTmpFS().createTempDir("userprofile", "copy");
    try {
      FileHandler.copy(profileDir, tempDir);
      

      File compreg = new File(tempDir, "compreg.dat");
      if ((compreg.exists()) && 
        (!compreg.delete())) {
        throw new WebDriverException("Cannot delete file from copy of profile " + profileName);
      }
    }
    catch (IOException e) {
      throw new WebDriverException(e);
    }
    
    return new FirefoxProfile(tempDir);
  }
  
  protected File locateAppDataDirectory(Platform os) { File appData;
    File appData;
    if (os.is(Platform.WINDOWS)) {
      appData = new File(MessageFormat.format("{0}\\Mozilla\\Firefox", new Object[] { System.getenv("APPDATA") }));
    } else { File appData;
      if (os.is(Platform.MAC))
      {
        appData = new File(MessageFormat.format("{0}/Library/Application Support/Firefox", new Object[] {
          System.getenv("HOME") }));
      }
      else {
        appData = new File(MessageFormat.format("{0}/.mozilla/firefox", new Object[] { System.getenv("HOME") }));
      }
    }
    if (!appData.exists())
    {

      return null;
    }
    
    if (!appData.isDirectory())
    {
      throw new WebDriverException("The discovered user firefox data directory (which normally contains the profiles) isn't a directory: " + appData.getAbsolutePath());
    }
    
    return appData;
  }
}
