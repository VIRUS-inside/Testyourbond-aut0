package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSIDCDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;

public abstract class IdentityConstraint
  implements XSIDCDefinition
{
  protected short type;
  protected final String fNamespace;
  protected final String fIdentityConstraintName;
  protected final String fElementName;
  protected Selector fSelector;
  protected int fFieldCount;
  protected Field[] fFields;
  protected XSAnnotationImpl[] fAnnotations = null;
  protected int fNumAnnotations;
  
  protected IdentityConstraint(String paramString1, String paramString2, String paramString3)
  {
    fNamespace = paramString1;
    fIdentityConstraintName = paramString2;
    fElementName = paramString3;
  }
  
  public String getIdentityConstraintName()
  {
    return fIdentityConstraintName;
  }
  
  public void setSelector(Selector paramSelector)
  {
    fSelector = paramSelector;
  }
  
  public Selector getSelector()
  {
    return fSelector;
  }
  
  public void addField(Field paramField)
  {
    if (fFields == null) {
      fFields = new Field[4];
    } else if (fFieldCount == fFields.length) {
      fFields = resize(fFields, fFieldCount * 2);
    }
    fFields[(fFieldCount++)] = paramField;
  }
  
  public int getFieldCount()
  {
    return fFieldCount;
  }
  
  public Field getFieldAt(int paramInt)
  {
    return fFields[paramInt];
  }
  
  public String getElementName()
  {
    return fElementName;
  }
  
  public String toString()
  {
    String str = super.toString();
    int i = str.lastIndexOf('$');
    if (i != -1) {
      return str.substring(i + 1);
    }
    int j = str.lastIndexOf('.');
    if (j != -1) {
      return str.substring(j + 1);
    }
    return str;
  }
  
  public boolean equals(IdentityConstraint paramIdentityConstraint)
  {
    boolean bool = fIdentityConstraintName.equals(fIdentityConstraintName);
    if (!bool) {
      return false;
    }
    bool = fSelector.toString().equals(fSelector.toString());
    if (!bool) {
      return false;
    }
    bool = fFieldCount == fFieldCount;
    if (!bool) {
      return false;
    }
    for (int i = 0; i < fFieldCount; i++) {
      if (!fFields[i].toString().equals(fFields[i].toString())) {
        return false;
      }
    }
    return true;
  }
  
  static final Field[] resize(Field[] paramArrayOfField, int paramInt)
  {
    Field[] arrayOfField = new Field[paramInt];
    System.arraycopy(paramArrayOfField, 0, arrayOfField, 0, paramArrayOfField.length);
    return arrayOfField;
  }
  
  public short getType()
  {
    return 10;
  }
  
  public String getName()
  {
    return fIdentityConstraintName;
  }
  
  public String getNamespace()
  {
    return fNamespace;
  }
  
  public short getCategory()
  {
    return type;
  }
  
  public String getSelectorStr()
  {
    return fSelector != null ? fSelector.toString() : null;
  }
  
  public StringList getFieldStrs()
  {
    String[] arrayOfString = new String[fFieldCount];
    for (int i = 0; i < fFieldCount; i++) {
      arrayOfString[i] = fFields[i].toString();
    }
    return new StringListImpl(arrayOfString, fFieldCount);
  }
  
  public XSIDCDefinition getRefKey()
  {
    return null;
  }
  
  public XSObjectList getAnnotations()
  {
    return new XSObjectListImpl(fAnnotations, fNumAnnotations);
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return null;
  }
  
  public void addAnnotation(XSAnnotationImpl paramXSAnnotationImpl)
  {
    if (paramXSAnnotationImpl == null) {
      return;
    }
    if (fAnnotations == null)
    {
      fAnnotations = new XSAnnotationImpl[2];
    }
    else if (fNumAnnotations == fAnnotations.length)
    {
      XSAnnotationImpl[] arrayOfXSAnnotationImpl = new XSAnnotationImpl[fNumAnnotations << 1];
      System.arraycopy(fAnnotations, 0, arrayOfXSAnnotationImpl, 0, fNumAnnotations);
      fAnnotations = arrayOfXSAnnotationImpl;
    }
    fAnnotations[(fNumAnnotations++)] = paramXSAnnotationImpl;
  }
}
