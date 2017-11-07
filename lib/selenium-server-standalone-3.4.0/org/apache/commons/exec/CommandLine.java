package org.apache.commons.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.exec.util.StringUtils;




























public class CommandLine
{
  private final Vector<Argument> arguments = new Vector();
  




  private final String executable;
  




  private Map<String, ?> substitutionMap;
  



  private final boolean isFile;
  




  public static CommandLine parse(String line)
  {
    return parse(line, null);
  }
  








  public static CommandLine parse(String line, Map<String, ?> substitutionMap)
  {
    if (line == null)
      throw new IllegalArgumentException("Command line can not be null");
    if (line.trim().length() == 0) {
      throw new IllegalArgumentException("Command line can not be empty");
    }
    String[] tmp = translateCommandline(line);
    
    CommandLine cl = new CommandLine(tmp[0]);
    cl.setSubstitutionMap(substitutionMap);
    for (int i = 1; i < tmp.length; i++) {
      cl.addArgument(tmp[i]);
    }
    
    return cl;
  }
  





  public CommandLine(String executable)
  {
    isFile = false;
    this.executable = toCleanExecutable(executable);
  }
  




  public CommandLine(File executable)
  {
    isFile = true;
    this.executable = toCleanExecutable(executable.getAbsolutePath());
  }
  





  public CommandLine(CommandLine other)
  {
    executable = other.getExecutable();
    isFile = other.isFile();
    arguments.addAll(arguments);
    
    if (other.getSubstitutionMap() != null)
    {
      Map<String, Object> omap = new HashMap();
      substitutionMap = omap;
      Iterator<String> iterator = substitutionMap.keySet().iterator();
      while (iterator.hasNext())
      {
        String key = (String)iterator.next();
        omap.put(key, other.getSubstitutionMap().get(key));
      }
    }
  }
  







  public String getExecutable()
  {
    return StringUtils.fixFileSeparatorChar(expandArgument(executable));
  }
  




  public boolean isFile()
  {
    return isFile;
  }
  





  public CommandLine addArguments(String[] addArguments)
  {
    return addArguments(addArguments, true);
  }
  






  public CommandLine addArguments(String[] addArguments, boolean handleQuoting)
  {
    if (addArguments != null) {
      for (String addArgument : addArguments) {
        addArgument(addArgument, handleQuoting);
      }
    }
    
    return this;
  }
  







  public CommandLine addArguments(String addArguments)
  {
    return addArguments(addArguments, true);
  }
  








  public CommandLine addArguments(String addArguments, boolean handleQuoting)
  {
    if (addArguments != null) {
      String[] argumentsArray = translateCommandline(addArguments);
      addArguments(argumentsArray, handleQuoting);
    }
    
    return this;
  }
  






  public CommandLine addArgument(String argument)
  {
    return addArgument(argument, true);
  }
  







  public CommandLine addArgument(String argument, boolean handleQuoting)
  {
    if (argument == null)
    {
      return this;
    }
    


    if (handleQuoting)
    {
      StringUtils.quoteArgument(argument);
    }
    
    arguments.add(new Argument(argument, handleQuoting, null));
    return this;
  }
  







  public String[] getArguments()
  {
    String[] result = new String[arguments.size()];
    
    for (int i = 0; i < result.length; i++) {
      Argument currArgument = (Argument)arguments.get(i);
      String expandedArgument = expandArgument(currArgument.getValue());
      result[i] = (currArgument.isHandleQuoting() ? StringUtils.quoteArgument(expandedArgument) : expandedArgument);
    }
    
    return result;
  }
  


  public Map<String, ?> getSubstitutionMap()
  {
    return substitutionMap;
  }
  





  public void setSubstitutionMap(Map<String, ?> substitutionMap)
  {
    this.substitutionMap = substitutionMap;
  }
  




  public String[] toStrings()
  {
    String[] result = new String[arguments.size() + 1];
    result[0] = getExecutable();
    System.arraycopy(getArguments(), 0, result, 1, result.length - 1);
    return result;
  }
  







  public String toString()
  {
    return "[" + StringUtils.toString(toStrings(), ", ") + "]";
  }
  







  private String expandArgument(String argument)
  {
    StringBuffer stringBuffer = StringUtils.stringSubstitution(argument, getSubstitutionMap(), true);
    return stringBuffer.toString();
  }
  







  private static String[] translateCommandline(String toProcess)
  {
    if ((toProcess == null) || (toProcess.length() == 0))
    {
      return new String[0];
    }
    


    int normal = 0;
    int inQuote = 1;
    int inDoubleQuote = 2;
    int state = 0;
    StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
    ArrayList<String> list = new ArrayList();
    StringBuilder current = new StringBuilder();
    boolean lastTokenHasBeenQuoted = false;
    
    while (tok.hasMoreTokens()) {
      String nextTok = tok.nextToken();
      switch (state) {
      case 1: 
        if ("'".equals(nextTok)) {
          lastTokenHasBeenQuoted = true;
          state = 0;
        } else {
          current.append(nextTok);
        }
        break;
      case 2: 
        if ("\"".equals(nextTok)) {
          lastTokenHasBeenQuoted = true;
          state = 0;
        } else {
          current.append(nextTok);
        }
        break;
      default: 
        if ("'".equals(nextTok)) {
          state = 1;
        } else if ("\"".equals(nextTok)) {
          state = 2;
        } else if (" ".equals(nextTok)) {
          if ((lastTokenHasBeenQuoted) || (current.length() != 0)) {
            list.add(current.toString());
            current = new StringBuilder();
          }
        } else {
          current.append(nextTok);
        }
        lastTokenHasBeenQuoted = false;
      }
      
    }
    
    if ((lastTokenHasBeenQuoted) || (current.length() != 0)) {
      list.add(current.toString());
    }
    
    if ((state == 1) || (state == 2)) {
      throw new IllegalArgumentException("Unbalanced quotes in " + toProcess);
    }
    

    String[] args = new String[list.size()];
    return (String[])list.toArray(args);
  }
  






  private String toCleanExecutable(String dirtyExecutable)
  {
    if (dirtyExecutable == null)
      throw new IllegalArgumentException("Executable can not be null");
    if (dirtyExecutable.trim().length() == 0) {
      throw new IllegalArgumentException("Executable can not be empty");
    }
    return StringUtils.fixFileSeparatorChar(dirtyExecutable);
  }
  


  class Argument
  {
    private final String value;
    
    private final boolean handleQuoting;
    

    private Argument(String value, boolean handleQuoting)
    {
      this.value = value.trim();
      this.handleQuoting = handleQuoting;
    }
    
    private String getValue()
    {
      return value;
    }
    
    private boolean isHandleQuoting()
    {
      return handleQuoting;
    }
  }
}
