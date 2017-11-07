package org.seleniumhq.jetty9.http.pathmap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;





















public class RegexPathSpec
  extends PathSpec
{
  protected Pattern pattern;
  
  protected RegexPathSpec() {}
  
  public RegexPathSpec(String regex)
  {
    pathSpec = regex;
    if (regex.startsWith("regex|"))
      pathSpec = regex.substring("regex|".length());
    pathDepth = 0;
    specLength = pathSpec.length();
    
    boolean inGrouping = false;
    StringBuilder signature = new StringBuilder();
    for (char c : pathSpec.toCharArray())
    {
      switch (c)
      {
      case '[': 
        inGrouping = true;
        break;
      case ']': 
        inGrouping = false;
        signature.append('g');
        break;
      case '*': 
        signature.append('g');
        break;
      case '/': 
        if (!inGrouping)
        {
          pathDepth += 1;
        }
        break;
      default: 
        if (!inGrouping)
        {
          if (Character.isLetterOrDigit(c))
          {
            signature.append('l');
          }
        }
        break;
      }
    }
    pattern = Pattern.compile(pathSpec);
    

    String sig = signature.toString();
    
    if (Pattern.matches("^l*$", sig))
    {
      group = PathSpecGroup.EXACT;
    }
    else if (Pattern.matches("^l*g+", sig))
    {
      group = PathSpecGroup.PREFIX_GLOB;
    }
    else if (Pattern.matches("^g+l+$", sig))
    {
      group = PathSpecGroup.SUFFIX_GLOB;
    }
    else
    {
      group = PathSpecGroup.MIDDLE_GLOB;
    }
  }
  
  public Matcher getMatcher(String path)
  {
    return pattern.matcher(path);
  }
  


  public String getPathInfo(String path)
  {
    if (group == PathSpecGroup.PREFIX_GLOB)
    {
      Matcher matcher = getMatcher(path);
      if (matcher.matches())
      {
        if (matcher.groupCount() >= 1)
        {
          String pathInfo = matcher.group(1);
          if ("".equals(pathInfo))
          {
            return "/";
          }
          

          return pathInfo;
        }
      }
    }
    
    return null;
  }
  

  public String getPathMatch(String path)
  {
    Matcher matcher = getMatcher(path);
    if (matcher.matches())
    {
      if (matcher.groupCount() >= 1)
      {
        int idx = matcher.start(1);
        if (idx > 0)
        {
          if (path.charAt(idx - 1) == '/')
          {
            idx--;
          }
          return path.substring(0, idx);
        }
      }
      return path;
    }
    return null;
  }
  
  public Pattern getPattern()
  {
    return pattern;
  }
  


  public String getRelativePath(String base, String path)
  {
    return null;
  }
  

  public boolean matches(String path)
  {
    int idx = path.indexOf('?');
    if (idx >= 0)
    {

      return getMatcher(path.substring(0, idx)).matches();
    }
    


    return getMatcher(path).matches();
  }
}
