package org.apache.xerces.impl.xs.util;

import java.util.ArrayList;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.util.XMLGrammarPoolImpl.Entry;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xs.XSModel;

public class XSGrammarPool
  extends XMLGrammarPoolImpl
{
  public XSGrammarPool() {}
  
  public XSModel toXSModel()
  {
    return toXSModel((short)1);
  }
  
  public XSModel toXSModel(short paramShort)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < fGrammars.length; i++) {
      for (XMLGrammarPoolImpl.Entry localEntry = fGrammars[i]; localEntry != null; localEntry = next) {
        if (desc.getGrammarType().equals("http://www.w3.org/2001/XMLSchema")) {
          localArrayList.add(grammar);
        }
      }
    }
    int j = localArrayList.size();
    if (j == 0) {
      return toXSModel(new SchemaGrammar[0], paramShort);
    }
    SchemaGrammar[] arrayOfSchemaGrammar = (SchemaGrammar[])localArrayList.toArray(new SchemaGrammar[j]);
    return toXSModel(arrayOfSchemaGrammar, paramShort);
  }
  
  protected XSModel toXSModel(SchemaGrammar[] paramArrayOfSchemaGrammar, short paramShort)
  {
    return new XSModelImpl(paramArrayOfSchemaGrammar, paramShort);
  }
}
