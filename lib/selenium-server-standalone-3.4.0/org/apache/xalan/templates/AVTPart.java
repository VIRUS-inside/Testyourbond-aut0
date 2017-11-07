package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;





















































public abstract class AVTPart
  implements Serializable, XSLTVisitable
{
  static final long serialVersionUID = -1747749903613916025L;
  
  public AVTPart() {}
  
  public abstract String getSimpleString();
  
  public abstract void evaluate(XPathContext paramXPathContext, FastStringBuffer paramFastStringBuffer, int paramInt, PrefixResolver paramPrefixResolver)
    throws TransformerException;
  
  public void setXPathSupport(XPathContext support) {}
  
  public boolean canTraverseOutsideSubtree()
  {
    return false;
  }
  
  public abstract void fixupVariables(Vector paramVector, int paramInt);
}
