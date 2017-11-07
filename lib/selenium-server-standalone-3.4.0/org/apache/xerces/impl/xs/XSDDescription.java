package org.apache.xerces.impl.xs;

import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;

public class XSDDescription
  extends XMLResourceIdentifierImpl
  implements XMLSchemaDescription
{
  public static final short CONTEXT_INITIALIZE = -1;
  public static final short CONTEXT_INCLUDE = 0;
  public static final short CONTEXT_REDEFINE = 1;
  public static final short CONTEXT_IMPORT = 2;
  public static final short CONTEXT_PREPARSE = 3;
  public static final short CONTEXT_INSTANCE = 4;
  public static final short CONTEXT_ELEMENT = 5;
  public static final short CONTEXT_ATTRIBUTE = 6;
  public static final short CONTEXT_XSITYPE = 7;
  protected short fContextType;
  protected String[] fLocationHints;
  protected QName fTriggeringComponent;
  protected QName fEnclosedElementName;
  protected XMLAttributes fAttributes;
  
  public XSDDescription() {}
  
  public String getGrammarType()
  {
    return "http://www.w3.org/2001/XMLSchema";
  }
  
  public short getContextType()
  {
    return fContextType;
  }
  
  public String getTargetNamespace()
  {
    return fNamespace;
  }
  
  public String[] getLocationHints()
  {
    return fLocationHints;
  }
  
  public QName getTriggeringComponent()
  {
    return fTriggeringComponent;
  }
  
  public QName getEnclosingElementName()
  {
    return fEnclosedElementName;
  }
  
  public XMLAttributes getAttributes()
  {
    return fAttributes;
  }
  
  public boolean fromInstance()
  {
    return (fContextType == 6) || (fContextType == 5) || (fContextType == 4) || (fContextType == 7);
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof XMLSchemaDescription)) {
      return false;
    }
    XMLSchemaDescription localXMLSchemaDescription = (XMLSchemaDescription)paramObject;
    if (fNamespace != null) {
      return fNamespace.equals(localXMLSchemaDescription.getTargetNamespace());
    }
    return localXMLSchemaDescription.getTargetNamespace() == null;
  }
  
  public int hashCode()
  {
    return fNamespace == null ? 0 : fNamespace.hashCode();
  }
  
  public void setContextType(short paramShort)
  {
    fContextType = paramShort;
  }
  
  public void setTargetNamespace(String paramString)
  {
    fNamespace = paramString;
  }
  
  public void setLocationHints(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    fLocationHints = new String[i];
    System.arraycopy(paramArrayOfString, 0, fLocationHints, 0, i);
  }
  
  public void setTriggeringComponent(QName paramQName)
  {
    fTriggeringComponent = paramQName;
  }
  
  public void setEnclosingElementName(QName paramQName)
  {
    fEnclosedElementName = paramQName;
  }
  
  public void setAttributes(XMLAttributes paramXMLAttributes)
  {
    fAttributes = paramXMLAttributes;
  }
  
  public void reset()
  {
    super.clear();
    fContextType = -1;
    fLocationHints = null;
    fTriggeringComponent = null;
    fEnclosedElementName = null;
    fAttributes = null;
  }
  
  public XSDDescription makeClone()
  {
    XSDDescription localXSDDescription = new XSDDescription();
    fAttributes = fAttributes;
    fBaseSystemId = fBaseSystemId;
    fContextType = fContextType;
    fEnclosedElementName = fEnclosedElementName;
    fExpandedSystemId = fExpandedSystemId;
    fLiteralSystemId = fLiteralSystemId;
    fLocationHints = fLocationHints;
    fPublicId = fPublicId;
    fNamespace = fNamespace;
    fTriggeringComponent = fTriggeringComponent;
    return localXSDDescription;
  }
}
