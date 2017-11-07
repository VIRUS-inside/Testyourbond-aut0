package org.openqa.selenium.os;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;
import org.openqa.selenium.Platform;




















public class WindowsUtils
{
  private static Logger LOG = Logger.getLogger(WindowsUtils.class.getName());
  private static final boolean THIS_IS_WINDOWS = Platform.getCurrent().is(Platform.WINDOWS);
  private static String taskkill = null;
  private static Properties env = null;
  

  public WindowsUtils() {}
  

  public static void killByName(String name)
  {
    executeCommand(findTaskKill(), new String[] { "/f", "/t", "/im", name });
  }
  




  public static void killPID(String processID)
  {
    executeCommand(findTaskKill(), new String[] { "/f", "/t", "/pid", processID });
  }
  
  private static String executeCommand(String commandName, String... args) {
    CommandLine cmd = new CommandLine(commandName, args);
    cmd.execute();
    
    String output = cmd.getStdOut();
    if ((cmd.getExitCode() == 0) || (cmd.getExitCode() == 128) || (cmd.getExitCode() == 255)) {
      return output;
    }
    throw new RuntimeException("exec return code " + cmd.getExitCode() + ": " + output);
  }
  




  public static synchronized Properties loadEnvironment()
  {
    if (env != null) {
      return env;
    }
    env = new Properties();
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      env.put(entry.getKey(), entry.getValue());
    }
    return env;
  }
  





  public static String getProgramFilesPath()
  {
    return getEnvVarPath("ProgramFiles", "C:\\Program Files");
  }
  
  public static String getProgramFiles86Path() {
    return getEnvVarPath("ProgramFiles(x86)", "C:\\Program Files (x86)");
  }
  
  private static String getEnvVarPath(String envVar, String defaultValue) {
    String pf = getEnvVarIgnoreCase(envVar);
    if (pf != null) {
      File programFiles = new File(pf);
      if (programFiles.exists()) {
        return programFiles.getAbsolutePath();
      }
    }
    return new File(defaultValue).getAbsolutePath();
  }
  
  public static ImmutableList<String> getPathsInProgramFiles(String childPath) {
    return 
    

      new ImmutableList.Builder().add(getFullPath(getProgramFilesPath(), childPath)).add(getFullPath(getProgramFiles86Path(), childPath)).build();
  }
  
  private static String getFullPath(String parent, String child) {
    return new File(parent, child).getAbsolutePath();
  }
  
  public static String getEnvVarIgnoreCase(String var) {
    Properties p = loadEnvironment();
    for (String key : p.stringPropertyNames()) {
      if (key.equalsIgnoreCase(var)) {
        return env.getProperty(key);
      }
    }
    return null;
  }
  




  public static File findSystemRoot()
  {
    Properties p = loadEnvironment();
    String systemRootPath = p.getProperty("SystemRoot");
    if (systemRootPath == null) {
      systemRootPath = p.getProperty("SYSTEMROOT");
    }
    if (systemRootPath == null) {
      systemRootPath = p.getProperty("systemroot");
    }
    if (systemRootPath == null) {
      throw new RuntimeException("SystemRoot apparently not set!");
    }
    File systemRoot = new File(systemRootPath);
    if (!systemRoot.exists()) {
      throw new RuntimeException("SystemRoot doesn't exist: " + systemRootPath);
    }
    return systemRoot;
  }
  





  public static String findTaskKill()
  {
    if (taskkill != null) {
      return taskkill;
    }
    File systemRoot = findSystemRoot();
    File taskkillExe = new File(systemRoot, "system32/taskkill.exe");
    if (taskkillExe.exists()) {
      taskkill = taskkillExe.getAbsolutePath();
      return taskkill;
    }
    LOG.warning("Couldn't find taskkill! Hope it's on the path...");
    taskkill = "taskkill";
    return taskkill;
  }
  




  public static boolean thisIsWindows()
  {
    return THIS_IS_WINDOWS;
  }
}
