package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.Arrays;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;























@ManagedObject("Servlet Mapping")
public class ServletMapping
{
  private String[] _pathSpecs;
  private String _servletName;
  private boolean _default;
  private Source _source;
  
  public ServletMapping()
  {
    this(Source.EMBEDDED);
  }
  
  public ServletMapping(Source source)
  {
    _source = source;
  }
  




  @ManagedAttribute(value="url patterns", readonly=true)
  public String[] getPathSpecs()
  {
    return _pathSpecs;
  }
  




  @ManagedAttribute(value="servlet name", readonly=true)
  public String getServletName()
  {
    return _servletName;
  }
  




  public void setPathSpecs(String[] pathSpecs)
  {
    _pathSpecs = pathSpecs;
  }
  






  public boolean containsPathSpec(String pathSpec)
  {
    if ((_pathSpecs == null) || (_pathSpecs.length == 0)) {
      return false;
    }
    for (String p : _pathSpecs)
    {
      if (p.equals(pathSpec))
        return true;
    }
    return false;
  }
  




  public void setPathSpec(String pathSpec)
  {
    _pathSpecs = new String[] { pathSpec };
  }
  




  public void setServletName(String servletName)
  {
    _servletName = servletName;
  }
  


  @ManagedAttribute(value="default", readonly=true)
  public boolean isDefault()
  {
    return _default;
  }
  


  public void setDefault(boolean fromDefault)
  {
    _default = fromDefault;
  }
  

  public Source getSource()
  {
    return _source;
  }
  

  public String toString()
  {
    return (_pathSpecs == null ? "[]" : Arrays.asList(_pathSpecs).toString()) + "=>" + _servletName;
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(String.valueOf(this)).append("\n");
  }
}
