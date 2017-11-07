package org.seleniumhq.jetty9.servlet;



























public class Source
{
  public static final Source EMBEDDED = new Source(Origin.EMBEDDED, null);
  public static final Source JAVAX_API = new Source(Origin.JAVAX_API, null);
  
  public static enum Origin { EMBEDDED,  JAVAX_API,  DESCRIPTOR,  ANNOTATION;
    



    private Origin() {}
  }
  


  public Source(Origin o, String resource)
  {
    if (o == null)
      throw new IllegalArgumentException("Origin is null");
    _origin = o;
    _resource = resource;
  }
  



  public Origin getOrigin()
  {
    return _origin;
  }
  


  public Origin _origin;
  
  public String getResource()
  {
    return _resource;
  }
  


  public String _resource;
  

  public String toString()
  {
    return _origin + ":" + _resource;
  }
}
