package org.apache.commons.exec.launcher;

import org.apache.commons.exec.OS;






























public final class CommandLauncherFactory
{
  private CommandLauncherFactory() {}
  
  public static CommandLauncher createVMLauncher()
  {
    CommandLauncher launcher;
    CommandLauncher launcher;
    if (OS.isFamilyOpenVms()) {
      launcher = new VmsCommandLauncher();
    } else {
      launcher = new Java13CommandLauncher();
    }
    
    return launcher;
  }
}
