package org.apache.xerces.impl.xs;

import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.PSVIDOMImplementationImpl;
import org.apache.xerces.impl.xs.util.LSInputListImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.ls.LSInput;

public class XSImplementationImpl
  extends PSVIDOMImplementationImpl
  implements XSImplementation
{
  static final XSImplementationImpl singleton = new XSImplementationImpl();
  
  public XSImplementationImpl() {}
  
  public static DOMImplementation getDOMImplementation()
  {
    return singleton;
  }
  
  public boolean hasFeature(String paramString1, String paramString2)
  {
    return ((paramString1.equalsIgnoreCase("XS-Loader")) && ((paramString2 == null) || (paramString2.equals("1.0")))) || (super.hasFeature(paramString1, paramString2));
  }
  
  public XSLoader createXSLoader(StringList paramStringList)
    throws XSException
  {
    XSLoaderImpl localXSLoaderImpl = new XSLoaderImpl();
    if (paramStringList == null) {
      return localXSLoaderImpl;
    }
    for (int i = 0; i < paramStringList.getLength(); i++) {
      if (!paramStringList.item(i).equals("1.0"))
      {
        String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { paramStringList.item(i) });
        throw new XSException((short)1, str);
      }
    }
    return localXSLoaderImpl;
  }
  
  public StringList createStringList(String[] paramArrayOfString)
  {
    int i = paramArrayOfString != null ? paramArrayOfString.length : 0;
    return i != 0 ? new StringListImpl((String[])paramArrayOfString.clone(), i) : StringListImpl.EMPTY_LIST;
  }
  
  public LSInputList createLSInputList(LSInput[] paramArrayOfLSInput)
  {
    int i = paramArrayOfLSInput != null ? paramArrayOfLSInput.length : 0;
    return i != 0 ? new LSInputListImpl((LSInput[])paramArrayOfLSInput.clone(), i) : LSInputListImpl.EMPTY_LIST;
  }
  
  public StringList getRecognizedVersions()
  {
    StringListImpl localStringListImpl = new StringListImpl(new String[] { "1.0" }, 1);
    return localStringListImpl;
  }
}
