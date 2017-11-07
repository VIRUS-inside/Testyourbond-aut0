package javax.xml.stream;

public class XMLStreamException
  extends Exception
{
  private static final long serialVersionUID = 2018819321811497362L;
  protected Throwable nested;
  protected Location location;
  
  public XMLStreamException() {}
  
  public XMLStreamException(String paramString)
  {
    super(paramString);
  }
  
  public XMLStreamException(Throwable paramThrowable)
  {
    nested = paramThrowable;
  }
  
  public XMLStreamException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    nested = paramThrowable;
  }
  
  public XMLStreamException(String paramString, Location paramLocation, Throwable paramThrowable)
  {
    super(paramString);
    location = paramLocation;
    nested = paramThrowable;
  }
  
  public XMLStreamException(String paramString, Location paramLocation)
  {
    super(paramString);
    location = paramLocation;
  }
  
  public Throwable getNestedException()
  {
    return nested;
  }
  
  public Location getLocation()
  {
    return location;
  }
}
