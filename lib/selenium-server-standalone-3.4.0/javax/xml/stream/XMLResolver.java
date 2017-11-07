package javax.xml.stream;

public abstract interface XMLResolver
{
  public abstract Object resolveEntity(String paramString1, String paramString2, String paramString3, String paramString4)
    throws XMLStreamException;
}
