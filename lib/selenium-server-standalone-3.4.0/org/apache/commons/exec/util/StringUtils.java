package org.apache.commons.exec.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;





















































public class StringUtils
{
  private static final String SINGLE_QUOTE = "'";
  private static final String DOUBLE_QUOTE = "\"";
  private static final char SLASH_CHAR = '/';
  private static final char BACKSLASH_CHAR = '\\';
  
  public StringUtils() {}
  
  public static StringBuffer stringSubstitution(String argStr, Map<? super String, ?> vars, boolean isLenient)
  {
    StringBuffer argBuf = new StringBuffer();
    
    if ((argStr == null) || (argStr.length() == 0)) {
      return argBuf;
    }
    
    if ((vars == null) || (vars.size() == 0)) {
      return argBuf.append(argStr);
    }
    
    int argStrLength = argStr.length();
    
    for (int cIdx = 0; cIdx < argStrLength;)
    {
      char ch = argStr.charAt(cIdx);
      char del = ' ';
      
      switch (ch)
      {
      case '$': 
        StringBuilder nameBuf = new StringBuilder();
        del = argStr.charAt(cIdx + 1);
        if (del == '{') {
          cIdx++;
          
          for (cIdx++; cIdx < argStr.length(); cIdx++) {
            ch = argStr.charAt(cIdx);
            if ((ch != '_') && (ch != '.') && (ch != '-') && (ch != '+') && (!Character.isLetterOrDigit(ch))) break;
            nameBuf.append(ch);
          }
          



          if (nameBuf.length() >= 0)
          {

            Object temp = vars.get(nameBuf.toString());
            String value;
            String value; if ((temp instanceof File))
            {

              value = fixFileSeparatorChar(((File)temp).getAbsolutePath());
            }
            else {
              value = temp != null ? temp.toString() : null;
            }
            
            if (value != null) {
              argBuf.append(value);
            }
            else if (isLenient)
            {
              argBuf.append("${").append(nameBuf.toString()).append("}");
            }
            else {
              throw new RuntimeException("No value found for : " + nameBuf);
            }
            

            del = argStr.charAt(cIdx);
            
            if (del != '}') {
              throw new RuntimeException("Delimiter not found for : " + nameBuf);
            }
          }
          
          cIdx++;
        }
        else {
          argBuf.append(ch);
          cIdx++;
        }
        
        break;
      
      default: 
        argBuf.append(ch);
        cIdx++;
      }
      
    }
    
    return argBuf;
  }
  







  public static String[] split(String input, String splitChar)
  {
    StringTokenizer tokens = new StringTokenizer(input, splitChar);
    List<String> strList = new ArrayList();
    while (tokens.hasMoreTokens()) {
      strList.add(tokens.nextToken());
    }
    return (String[])strList.toArray(new String[strList.size()]);
  }
  











  public static String fixFileSeparatorChar(String arg)
  {
    return arg.replace('/', File.separatorChar).replace('\\', File.separatorChar);
  }
  







  public static String toString(String[] strings, String separator)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < strings.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(strings[i]);
    }
    return sb.toString();
  }
  












  public static String quoteArgument(String argument)
  {
    String cleanedArgument = argument.trim();
    

    while ((cleanedArgument.startsWith("'")) || (cleanedArgument.startsWith("\""))) {
      cleanedArgument = cleanedArgument.substring(1);
    }
    
    while ((cleanedArgument.endsWith("'")) || (cleanedArgument.endsWith("\""))) {
      cleanedArgument = cleanedArgument.substring(0, cleanedArgument.length() - 1);
    }
    
    StringBuilder buf = new StringBuilder();
    if (cleanedArgument.indexOf("\"") > -1) {
      if (cleanedArgument.indexOf("'") > -1) {
        throw new IllegalArgumentException("Can't handle single and double quotes in same argument");
      }
      
      return "'" + cleanedArgument + "'";
    }
    if ((cleanedArgument.indexOf("'") > -1) || (cleanedArgument.indexOf(" ") > -1))
    {
      return "\"" + cleanedArgument + "\"";
    }
    
    return cleanedArgument;
  }
  







  public static boolean isQuoted(String argument)
  {
    return ((argument.startsWith("'")) && (argument.endsWith("'"))) || ((argument.startsWith("\"")) && (argument.endsWith("\"")));
  }
}
