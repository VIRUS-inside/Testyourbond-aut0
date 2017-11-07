package org.seleniumhq.jetty9.http.pathmap;

import org.seleniumhq.jetty9.util.StringUtil;


























public class ServletPathSpec
  extends PathSpec
{
  public static String normalize(String pathSpec)
  {
    if ((StringUtil.isNotBlank(pathSpec)) && (!pathSpec.startsWith("/")) && (!pathSpec.startsWith("*")))
      return "/" + pathSpec;
    return pathSpec;
  }
  

  public ServletPathSpec(String servletPathSpec)
  {
    if (servletPathSpec == null)
      servletPathSpec = "";
    if (servletPathSpec.startsWith("servlet|"))
      servletPathSpec = servletPathSpec.substring("servlet|".length());
    assertValidServletPathSpec(servletPathSpec);
    

    if (servletPathSpec.length() == 0)
    {
      pathSpec = "";
      pathDepth = -1;
      specLength = 1;
      group = PathSpecGroup.ROOT;
      return;
    }
    

    if ("/".equals(servletPathSpec))
    {
      pathSpec = "/";
      pathDepth = -1;
      specLength = 1;
      group = PathSpecGroup.DEFAULT;
      return;
    }
    
    specLength = servletPathSpec.length();
    pathDepth = 0;
    char lastChar = servletPathSpec.charAt(specLength - 1);
    
    if ((servletPathSpec.charAt(0) == '/') && (specLength > 1) && (lastChar == '*'))
    {
      group = PathSpecGroup.PREFIX_GLOB;
      prefix = servletPathSpec.substring(0, specLength - 2);

    }
    else if (servletPathSpec.charAt(0) == '*')
    {
      group = PathSpecGroup.SUFFIX_GLOB;
      suffix = servletPathSpec.substring(2, specLength);
    }
    else
    {
      group = PathSpecGroup.EXACT;
      prefix = servletPathSpec;
    }
    
    for (int i = 0; i < specLength; i++)
    {
      int cp = servletPathSpec.codePointAt(i);
      if (cp < 128)
      {
        char c = (char)cp;
        switch (c)
        {
        case '/': 
          pathDepth += 1;
        }
        
      }
    }
    
    pathSpec = servletPathSpec;
  }
  
  private void assertValidServletPathSpec(String servletPathSpec)
  {
    if ((servletPathSpec == null) || (servletPathSpec.equals("")))
    {
      return;
    }
    
    int len = servletPathSpec.length();
    
    if (servletPathSpec.charAt(0) == '/')
    {

      if (len == 1)
      {
        return;
      }
      int idx = servletPathSpec.indexOf('*');
      if (idx < 0)
      {
        return;
      }
      
      if (idx != len - 1)
      {
        throw new IllegalArgumentException("Servlet Spec 12.2 violation: glob '*' can only exist at end of prefix based matches: bad spec \"" + servletPathSpec + "\"");
      }
      
      if ((idx < 1) || (servletPathSpec.charAt(idx - 1) != '/'))
      {
        throw new IllegalArgumentException("Servlet Spec 12.2 violation: suffix glob '*' can only exist after '/': bad spec \"" + servletPathSpec + "\"");
      }
    }
    else if (servletPathSpec.startsWith("*."))
    {

      int idx = servletPathSpec.indexOf('/');
      
      if (idx >= 0)
      {
        throw new IllegalArgumentException("Servlet Spec 12.2 violation: suffix based path spec cannot have path separators: bad spec \"" + servletPathSpec + "\"");
      }
      
      idx = servletPathSpec.indexOf('*', 2);
      
      if (idx >= 1)
      {
        throw new IllegalArgumentException("Servlet Spec 12.2 violation: suffix based path spec cannot have multiple glob '*': bad spec \"" + servletPathSpec + "\"");
      }
    }
    else
    {
      throw new IllegalArgumentException("Servlet Spec 12.2 violation: path spec must start with \"/\" or \"*.\": bad spec \"" + servletPathSpec + "\"");
    }
  }
  


  public String getPathInfo(String path)
  {
    if (group == PathSpecGroup.PREFIX_GLOB)
    {
      if (path.length() == specLength - 2)
      {
        return null;
      }
      return path.substring(specLength - 2);
    }
    
    return null;
  }
  

  public String getPathMatch(String path)
  {
    switch (1.$SwitchMap$org$eclipse$jetty$http$pathmap$PathSpecGroup[group.ordinal()])
    {
    case 1: 
      if (pathSpec.equals(path))
      {
        return path;
      }
      

      return null;
    
    case 2: 
      if (isWildcardMatch(path))
      {
        return path.substring(0, specLength - 2);
      }
      

      return null;
    
    case 3: 
      if (path.regionMatches(path.length() - (specLength - 1), pathSpec, 1, specLength - 1))
      {
        return path;
      }
      

      return null;
    
    case 4: 
      return path;
    }
    return null;
  }
  


  public String getRelativePath(String base, String path)
  {
    String info = getPathInfo(path);
    if (info == null)
    {
      info = path;
    }
    
    if (info.startsWith("./"))
    {
      info = info.substring(2);
    }
    if (base.endsWith("/"))
    {
      if (info.startsWith("/"))
      {
        path = base + info.substring(1);
      }
      else
      {
        path = base + info;
      }
    }
    else if (info.startsWith("/"))
    {
      path = base + info;
    }
    else
    {
      path = base + "/" + info;
    }
    return path;
  }
  

  private boolean isWildcardMatch(String path)
  {
    int cpl = specLength - 2;
    if ((group == PathSpecGroup.PREFIX_GLOB) && (path.regionMatches(0, pathSpec, 0, cpl)))
    {
      if ((path.length() == cpl) || ('/' == path.charAt(cpl)))
      {
        return true;
      }
    }
    return false;
  }
  

  public boolean matches(String path)
  {
    switch (1.$SwitchMap$org$eclipse$jetty$http$pathmap$PathSpecGroup[group.ordinal()])
    {
    case 1: 
      return pathSpec.equals(path);
    case 2: 
      return isWildcardMatch(path);
    case 3: 
      return path.regionMatches(path.length() - specLength + 1, pathSpec, 1, specLength - 1);
    
    case 5: 
      return "/".equals(path);
    
    case 4: 
      return true;
    }
    return false;
  }
}
