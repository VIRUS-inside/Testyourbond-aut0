package org.apache.commons.exec.launcher;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.CommandLine;






















public abstract class CommandLauncherProxy
  extends CommandLauncherImpl
{
  private final CommandLauncher myLauncher;
  
  public CommandLauncherProxy(CommandLauncher launcher)
  {
    myLauncher = launcher;
  }
  













  public Process exec(CommandLine cmd, Map<String, String> env)
    throws IOException
  {
    return myLauncher.exec(cmd, env);
  }
}
