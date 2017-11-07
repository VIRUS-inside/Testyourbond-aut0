package javax.xml.transform;

public abstract interface ErrorListener
{
  public abstract void warning(TransformerException paramTransformerException)
    throws TransformerException;
  
  public abstract void error(TransformerException paramTransformerException)
    throws TransformerException;
  
  public abstract void fatalError(TransformerException paramTransformerException)
    throws TransformerException;
}
