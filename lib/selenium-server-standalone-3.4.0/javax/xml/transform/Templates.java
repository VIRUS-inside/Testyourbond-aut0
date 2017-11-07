package javax.xml.transform;

import java.util.Properties;

public abstract interface Templates
{
  public abstract Transformer newTransformer()
    throws TransformerConfigurationException;
  
  public abstract Properties getOutputProperties();
}
