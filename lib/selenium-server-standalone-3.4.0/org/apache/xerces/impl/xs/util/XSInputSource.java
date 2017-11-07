package org.apache.xerces.impl.xs.util;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSObject;

public final class XSInputSource
  extends XMLInputSource
{
  private SchemaGrammar[] fGrammars;
  private XSObject[] fComponents;
  
  public XSInputSource(SchemaGrammar[] paramArrayOfSchemaGrammar)
  {
    super(null, null, null);
    fGrammars = paramArrayOfSchemaGrammar;
    fComponents = null;
  }
  
  public XSInputSource(XSObject[] paramArrayOfXSObject)
  {
    super(null, null, null);
    fGrammars = null;
    fComponents = paramArrayOfXSObject;
  }
  
  public SchemaGrammar[] getGrammars()
  {
    return fGrammars;
  }
  
  public void setGrammars(SchemaGrammar[] paramArrayOfSchemaGrammar)
  {
    fGrammars = paramArrayOfSchemaGrammar;
  }
  
  public XSObject[] getComponents()
  {
    return fComponents;
  }
  
  public void setComponents(XSObject[] paramArrayOfXSObject)
  {
    fComponents = paramArrayOfXSObject;
  }
}
