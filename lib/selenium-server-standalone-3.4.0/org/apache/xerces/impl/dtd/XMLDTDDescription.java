package org.apache.xerces.impl.dtd;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDTDDescription
  extends XMLResourceIdentifierImpl
  implements org.apache.xerces.xni.grammars.XMLDTDDescription
{
  protected String fRootName = null;
  protected ArrayList fPossibleRoots = null;
  
  public XMLDTDDescription(XMLResourceIdentifier paramXMLResourceIdentifier, String paramString)
  {
    setValues(paramXMLResourceIdentifier.getPublicId(), paramXMLResourceIdentifier.getLiteralSystemId(), paramXMLResourceIdentifier.getBaseSystemId(), paramXMLResourceIdentifier.getExpandedSystemId());
    fRootName = paramString;
    fPossibleRoots = null;
  }
  
  public XMLDTDDescription(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    setValues(paramString1, paramString2, paramString3, paramString4);
    fRootName = paramString5;
    fPossibleRoots = null;
  }
  
  public XMLDTDDescription(XMLInputSource paramXMLInputSource)
  {
    setValues(paramXMLInputSource.getPublicId(), null, paramXMLInputSource.getBaseSystemId(), paramXMLInputSource.getSystemId());
    fRootName = null;
    fPossibleRoots = null;
  }
  
  public String getGrammarType()
  {
    return "http://www.w3.org/TR/REC-xml";
  }
  
  public String getRootName()
  {
    return fRootName;
  }
  
  public void setRootName(String paramString)
  {
    fRootName = paramString;
    fPossibleRoots = null;
  }
  
  public void setPossibleRoots(ArrayList paramArrayList)
  {
    fPossibleRoots = paramArrayList;
  }
  
  public void setPossibleRoots(Vector paramVector)
  {
    fPossibleRoots = (paramVector != null ? new ArrayList(paramVector) : null);
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof XMLGrammarDescription)) {
      return false;
    }
    if (!getGrammarType().equals(((XMLGrammarDescription)paramObject).getGrammarType())) {
      return false;
    }
    XMLDTDDescription localXMLDTDDescription = (XMLDTDDescription)paramObject;
    if (fRootName != null)
    {
      if ((fRootName != null) && (!fRootName.equals(fRootName))) {
        return false;
      }
      if ((fPossibleRoots != null) && (!fPossibleRoots.contains(fRootName))) {
        return false;
      }
    }
    else if (fPossibleRoots != null)
    {
      if (fRootName != null)
      {
        if (!fPossibleRoots.contains(fRootName)) {
          return false;
        }
      }
      else
      {
        if (fPossibleRoots == null) {
          return false;
        }
        boolean bool = false;
        int i = fPossibleRoots.size();
        for (int j = 0; j < i; j++)
        {
          String str = (String)fPossibleRoots.get(j);
          bool = fPossibleRoots.contains(str);
          if (bool) {
            break;
          }
        }
        if (!bool) {
          return false;
        }
      }
    }
    if (fExpandedSystemId != null)
    {
      if (!fExpandedSystemId.equals(fExpandedSystemId)) {
        return false;
      }
    }
    else if (fExpandedSystemId != null) {
      return false;
    }
    if (fPublicId != null)
    {
      if (!fPublicId.equals(fPublicId)) {
        return false;
      }
    }
    else if (fPublicId != null) {
      return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    if (fExpandedSystemId != null) {
      return fExpandedSystemId.hashCode();
    }
    if (fPublicId != null) {
      return fPublicId.hashCode();
    }
    return 0;
  }
}
