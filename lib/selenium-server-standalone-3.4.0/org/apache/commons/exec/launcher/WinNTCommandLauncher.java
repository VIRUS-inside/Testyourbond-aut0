package org.apache.commons.exec.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.CommandLine;























public class WinNTCommandLauncher
  extends CommandLauncherProxy
{
  public WinNTCommandLauncher(CommandLauncher launcher)
  {
    super(launcher);
  }
  













  public Process exec(CommandLine cmd, Map<String, String> env, File workingDir)
    throws IOException
  {
    if (workingDir == null) {
      return exec(cmd, env);
    }
    


    CommandLine newCmd = new CommandLine("cmd");
    newCmd.addArgument("/c");
    newCmd.addArguments(cmd.toStrings());
    
    return exec(newCmd, env);
  }
}
