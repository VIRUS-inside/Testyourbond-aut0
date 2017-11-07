package org.apache.commons.exec.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.CommandLine;

public abstract interface CommandLauncher
{
  public abstract Process exec(CommandLine paramCommandLine, Map<String, String> paramMap)
    throws IOException;
  
  public abstract Process exec(CommandLine paramCommandLine, Map<String, String> paramMap, File paramFile)
    throws IOException;
  
  public abstract boolean isFailure(int paramInt);
}
