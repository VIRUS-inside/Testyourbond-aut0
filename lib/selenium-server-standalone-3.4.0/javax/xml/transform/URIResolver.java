package javax.xml.transform;

public abstract interface URIResolver
{
  public abstract Source resolve(String paramString1, String paramString2)
    throws TransformerException;
}
