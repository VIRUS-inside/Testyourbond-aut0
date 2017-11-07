package javax.xml.stream;

public abstract interface XMLReporter
{
  public abstract void report(String paramString1, String paramString2, Object paramObject, Location paramLocation)
    throws XMLStreamException;
}
