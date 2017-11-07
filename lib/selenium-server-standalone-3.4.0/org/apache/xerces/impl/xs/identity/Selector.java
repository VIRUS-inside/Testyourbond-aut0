package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.impl.xpath.XPath.Axis;
import org.apache.xerces.impl.xpath.XPath.Step;
import org.apache.xerces.impl.xpath.XPathException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSTypeDefinition;

public class Selector
{
  protected final XPath fXPath;
  protected final IdentityConstraint fIdentityConstraint;
  protected IdentityConstraint fIDConstraint;
  
  public Selector(XPath paramXPath, IdentityConstraint paramIdentityConstraint)
  {
    fXPath = paramXPath;
    fIdentityConstraint = paramIdentityConstraint;
  }
  
  public XPath getXPath()
  {
    return fXPath;
  }
  
  public IdentityConstraint getIDConstraint()
  {
    return fIdentityConstraint;
  }
  
  public XPathMatcher createMatcher(FieldActivator paramFieldActivator, int paramInt)
  {
    return new Matcher(fXPath, paramFieldActivator, paramInt);
  }
  
  public String toString()
  {
    return fXPath.toString();
  }
  
  public class Matcher
    extends XPathMatcher
  {
    protected final FieldActivator fFieldActivator;
    protected final int fInitialDepth;
    protected int fElementDepth;
    protected int fMatchedDepth;
    
    public Matcher(Selector.XPath paramXPath, FieldActivator paramFieldActivator, int paramInt)
    {
      super();
      fFieldActivator = paramFieldActivator;
      fInitialDepth = paramInt;
    }
    
    public void startDocumentFragment()
    {
      super.startDocumentFragment();
      fElementDepth = 0;
      fMatchedDepth = -1;
    }
    
    public void startElement(QName paramQName, XMLAttributes paramXMLAttributes)
    {
      super.startElement(paramQName, paramXMLAttributes);
      fElementDepth += 1;
      if (isMatched())
      {
        fMatchedDepth = fElementDepth;
        fFieldActivator.startValueScopeFor(fIdentityConstraint, fInitialDepth);
        int i = fIdentityConstraint.getFieldCount();
        for (int j = 0; j < i; j++)
        {
          Field localField = fIdentityConstraint.getFieldAt(j);
          XPathMatcher localXPathMatcher = fFieldActivator.activateField(localField, fInitialDepth);
          localXPathMatcher.startElement(paramQName, paramXMLAttributes);
        }
      }
    }
    
    public void endElement(QName paramQName, XSTypeDefinition paramXSTypeDefinition, boolean paramBoolean, Object paramObject, short paramShort, ShortList paramShortList)
    {
      super.endElement(paramQName, paramXSTypeDefinition, paramBoolean, paramObject, paramShort, paramShortList);
      if (fElementDepth-- == fMatchedDepth)
      {
        fMatchedDepth = -1;
        fFieldActivator.endValueScopeFor(fIdentityConstraint, fInitialDepth);
      }
    }
    
    public IdentityConstraint getIdentityConstraint()
    {
      return fIdentityConstraint;
    }
    
    public int getInitialDepth()
    {
      return fInitialDepth;
    }
  }
  
  public static class XPath
    extends XPath
  {
    public XPath(String paramString, SymbolTable paramSymbolTable, NamespaceContext paramNamespaceContext)
      throws XPathException
    {
      super(paramSymbolTable, paramNamespaceContext);
      for (int i = 0; i < fLocationPaths.length; i++)
      {
        XPath.Axis localAxis = fLocationPaths[i].steps[(fLocationPaths[i].steps.length - 1)].axis;
        if (type == 2) {
          throw new XPathException("c-selector-xpath");
        }
      }
    }
    
    private static String normalize(String paramString)
    {
      StringBuffer localStringBuffer = new StringBuffer(paramString.length() + 5);
      int i = -1;
      for (;;)
      {
        if ((!XMLChar.trim(paramString).startsWith("/")) && (!XMLChar.trim(paramString).startsWith("."))) {
          localStringBuffer.append("./");
        }
        i = paramString.indexOf('|');
        if (i == -1)
        {
          localStringBuffer.append(paramString);
          break;
        }
        localStringBuffer.append(paramString.substring(0, i + 1));
        paramString = paramString.substring(i + 1, paramString.length());
      }
      return localStringBuffer.toString();
    }
  }
}
