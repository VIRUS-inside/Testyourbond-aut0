package org.seleniumhq.jetty9.http.pathmap;




public abstract class PathSpec
  implements Comparable<PathSpec>
{
  protected String pathSpec;
  


  protected PathSpecGroup group;
  


  protected int pathDepth;
  


  protected int specLength;
  

  protected String prefix;
  

  protected String suffix;
  


  public PathSpec() {}
  


  public int compareTo(PathSpec other)
  {
    int diff = group.ordinal() - group.ordinal();
    if (diff != 0)
    {
      return diff;
    }
    

    diff = specLength - specLength;
    if (diff != 0)
    {
      return diff;
    }
    

    return pathSpec.compareTo(pathSpec);
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    PathSpec other = (PathSpec)obj;
    if (pathSpec == null)
    {
      if (pathSpec != null)
      {
        return false;
      }
    }
    else if (!pathSpec.equals(pathSpec))
    {
      return false;
    }
    return true;
  }
  
  public PathSpecGroup getGroup()
  {
    return group;
  }
  







  public int getPathDepth()
  {
    return pathDepth;
  }
  







  public abstract String getPathInfo(String paramString);
  






  public abstract String getPathMatch(String paramString);
  






  public String getDeclaration()
  {
    return pathSpec;
  }
  




  public String getPrefix()
  {
    return prefix;
  }
  




  public String getSuffix()
  {
    return suffix;
  }
  





  public abstract String getRelativePath(String paramString1, String paramString2);
  





  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (pathSpec == null ? 0 : pathSpec.hashCode());
    return result;
  }
  




  public abstract boolean matches(String paramString);
  




  public String toString()
  {
    StringBuilder str = new StringBuilder();
    str.append(getClass().getSimpleName()).append("[\"");
    str.append(pathSpec);
    str.append("\",pathDepth=").append(pathDepth);
    str.append(",group=").append(group);
    str.append("]");
    return str.toString();
  }
}
