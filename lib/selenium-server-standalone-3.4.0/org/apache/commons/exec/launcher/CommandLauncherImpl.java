package org.apache.commons.exec.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.environment.EnvironmentUtils;























public abstract class CommandLauncherImpl
  implements CommandLauncher
{
  public CommandLauncherImpl() {}
  
  public Process exec(CommandLine cmd, Map<String, String> env)
    throws IOException
  {
    String[] envVar = EnvironmentUtils.toStrings(env);
    return Runtime.getRuntime().exec(cmd.toStrings(), envVar);
  }
  

  public abstract Process exec(CommandLine paramCommandLine, Map<String, String> paramMap, File paramFile)
    throws IOException;
  

  public boolean isFailure(int exitValue)
  {
    return exitValue != 0;
  }
}
