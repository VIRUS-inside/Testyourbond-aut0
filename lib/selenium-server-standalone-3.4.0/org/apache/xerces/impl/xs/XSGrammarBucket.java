package org.apache.xerces.impl.xs;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class XSGrammarBucket
{
  Hashtable fGrammarRegistry = new Hashtable();
  SchemaGrammar fNoNSGrammar = null;
  
  public XSGrammarBucket() {}
  
  public SchemaGrammar getGrammar(String paramString)
  {
    if (paramString == null) {
      return fNoNSGrammar;
    }
    return (SchemaGrammar)fGrammarRegistry.get(paramString);
  }
  
  public void putGrammar(SchemaGrammar paramSchemaGrammar)
  {
    if (paramSchemaGrammar.getTargetNamespace() == null) {
      fNoNSGrammar = paramSchemaGrammar;
    } else {
      fGrammarRegistry.put(paramSchemaGrammar.getTargetNamespace(), paramSchemaGrammar);
    }
  }
  
  public boolean putGrammar(SchemaGrammar paramSchemaGrammar, boolean paramBoolean)
  {
    SchemaGrammar localSchemaGrammar1 = getGrammar(fTargetNamespace);
    if (localSchemaGrammar1 != null) {
      return localSchemaGrammar1 == paramSchemaGrammar;
    }
    if (!paramBoolean)
    {
      putGrammar(paramSchemaGrammar);
      return true;
    }
    Vector localVector1 = paramSchemaGrammar.getImportedGrammars();
    if (localVector1 == null)
    {
      putGrammar(paramSchemaGrammar);
      return true;
    }
    Vector localVector2 = (Vector)localVector1.clone();
    for (int i = 0; i < localVector2.size(); i++)
    {
      SchemaGrammar localSchemaGrammar2 = (SchemaGrammar)localVector2.elementAt(i);
      SchemaGrammar localSchemaGrammar3 = getGrammar(fTargetNamespace);
      if (localSchemaGrammar3 == null)
      {
        Vector localVector3 = localSchemaGrammar2.getImportedGrammars();
        if (localVector3 != null) {
          for (j = localVector3.size() - 1; j >= 0; j--)
          {
            localSchemaGrammar3 = (SchemaGrammar)localVector3.elementAt(j);
            if (!localVector2.contains(localSchemaGrammar3)) {
              localVector2.addElement(localSchemaGrammar3);
            }
          }
        }
      }
      else if (localSchemaGrammar3 != localSchemaGrammar2)
      {
        return false;
      }
    }
    putGrammar(paramSchemaGrammar);
    for (int j = localVector2.size() - 1; j >= 0; j--) {
      putGrammar((SchemaGrammar)localVector2.elementAt(j));
    }
    return true;
  }
  
  public boolean putGrammar(SchemaGrammar paramSchemaGrammar, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!paramBoolean2) {
      return putGrammar(paramSchemaGrammar, paramBoolean1);
    }
    SchemaGrammar localSchemaGrammar1 = getGrammar(fTargetNamespace);
    if (localSchemaGrammar1 == null) {
      putGrammar(paramSchemaGrammar);
    }
    if (!paramBoolean1) {
      return true;
    }
    Vector localVector1 = paramSchemaGrammar.getImportedGrammars();
    if (localVector1 == null) {
      return true;
    }
    Vector localVector2 = (Vector)localVector1.clone();
    for (int i = 0; i < localVector2.size(); i++)
    {
      SchemaGrammar localSchemaGrammar2 = (SchemaGrammar)localVector2.elementAt(i);
      SchemaGrammar localSchemaGrammar3 = getGrammar(fTargetNamespace);
      if (localSchemaGrammar3 == null)
      {
        Vector localVector3 = localSchemaGrammar2.getImportedGrammars();
        if (localVector3 != null) {
          for (j = localVector3.size() - 1; j >= 0; j--)
          {
            localSchemaGrammar3 = (SchemaGrammar)localVector3.elementAt(j);
            if (!localVector2.contains(localSchemaGrammar3)) {
              localVector2.addElement(localSchemaGrammar3);
            }
          }
        }
      }
      else
      {
        localVector2.remove(localSchemaGrammar2);
      }
    }
    for (int j = localVector2.size() - 1; j >= 0; j--) {
      putGrammar((SchemaGrammar)localVector2.elementAt(j));
    }
    return true;
  }
  
  public SchemaGrammar[] getGrammars()
  {
    int i = fGrammarRegistry.size() + (fNoNSGrammar == null ? 0 : 1);
    SchemaGrammar[] arrayOfSchemaGrammar = new SchemaGrammar[i];
    Enumeration localEnumeration = fGrammarRegistry.elements();
    int j = 0;
    while (localEnumeration.hasMoreElements()) {
      arrayOfSchemaGrammar[(j++)] = ((SchemaGrammar)localEnumeration.nextElement());
    }
    if (fNoNSGrammar != null) {
      arrayOfSchemaGrammar[(i - 1)] = fNoNSGrammar;
    }
    return arrayOfSchemaGrammar;
  }
  
  public void reset()
  {
    fNoNSGrammar = null;
    fGrammarRegistry.clear();
  }
}
