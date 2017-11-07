package org.apache.commons.exec.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.environment.EnvironmentUtils;











































public class Java13CommandLauncher
  extends CommandLauncherImpl
{
  public Java13CommandLauncher() {}
  
  public Process exec(CommandLine cmd, Map<String, String> env, File workingDir)
    throws IOException
  {
    String[] envVars = EnvironmentUtils.toStrings(env);
    
    return Runtime.getRuntime().exec(cmd.toStrings(), envVars, workingDir);
  }
}
