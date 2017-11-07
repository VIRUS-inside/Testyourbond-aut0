package org.apache.commons.exec.launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.util.StringUtils;



























public class VmsCommandLauncher
  extends Java13CommandLauncher
{
  public VmsCommandLauncher() {}
  
  public Process exec(CommandLine cmd, Map<String, String> env)
    throws IOException
  {
    CommandLine vmsCmd = new CommandLine(createCommandFile(cmd, env).getPath());
    


    return super.exec(vmsCmd, env);
  }
  






  public Process exec(CommandLine cmd, Map<String, String> env, File workingDir)
    throws IOException
  {
    CommandLine vmsCmd = new CommandLine(createCommandFile(cmd, env).getPath());
    


    return super.exec(vmsCmd, env, workingDir);
  }
  


  public boolean isFailure(int exitValue)
  {
    return exitValue % 2 == 0;
  }
  



  private File createCommandFile(CommandLine cmd, Map<String, String> env)
    throws IOException
  {
    File script = File.createTempFile("EXEC", ".TMP");
    script.deleteOnExit();
    PrintWriter out = null;
    try {
      out = new PrintWriter(new FileWriter(script.getAbsolutePath(), true));
      

      if (env != null) {
        Set<Map.Entry<String, String>> entries = env.entrySet();
        
        for (Map.Entry<String, String> entry : entries) {
          out.print("$ ");
          out.print((String)entry.getKey());
          out.print(" == ");
          out.println('"');
          String value = (String)entry.getValue();
          
          if (value.indexOf('"') > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < value.length(); i++) {
              char c = value.charAt(i);
              if (c == '"') {
                sb.append('"');
              }
              sb.append(c);
            }
            value = sb.toString();
          }
          out.print(value);
          out.println('"');
        }
      }
      
      String command = cmd.getExecutable();
      if (cmd.isFile()) {
        out.print("$ @");
        
        String[] parts = StringUtils.split(command, "/");
        out.print(parts[0]);
        out.print(":[");
        out.print(parts[1]);
        int lastPart = parts.length - 1;
        for (int i = 2; i < lastPart; i++) {
          out.print(".");
          out.print(parts[i]);
        }
        out.print("]");
        out.print(parts[lastPart]);
      } else {
        out.print("$ ");
        out.print(command);
      }
      String[] args = cmd.getArguments();
      for (String arg : args) {
        out.println(" -");
        out.print(arg);
      }
      out.println();
    } finally {
      if (out != null) {
        out.close();
      }
    }
    return script;
  }
}
