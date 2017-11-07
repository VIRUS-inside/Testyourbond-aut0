package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;





























public class FuncSubstring
  extends Function3Args
{
  static final long serialVersionUID = -5996676095024715502L;
  
  public FuncSubstring() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    XMLString s1 = m_arg0.execute(xctxt).xstr();
    double start = m_arg1.execute(xctxt).num();
    int lenOfS1 = s1.length();
    

    if (lenOfS1 <= 0) {
      return XString.EMPTYSTRING;
    }
    
    int startIndex;
    int startIndex;
    if (Double.isNaN(start))
    {



      start = -1000000.0D;
      startIndex = 0;
    }
    else
    {
      start = Math.round(start);
      startIndex = start > 0.0D ? (int)start - 1 : 0; }
    XMLString substr;
    XMLString substr;
    if (null != m_arg2)
    {
      double len = m_arg2.num(xctxt);
      int end = (int)(Math.round(len) + start) - 1;
      

      if (end < 0) {
        end = 0;
      } else if (end > lenOfS1) {
        end = lenOfS1;
      }
      if (startIndex > lenOfS1) {
        startIndex = lenOfS1;
      }
      substr = s1.substring(startIndex, end);
    }
    else
    {
      if (startIndex > lenOfS1)
        startIndex = lenOfS1;
      substr = s1.substring(startIndex);
    }
    

    return (XString)substr;
  }
  







  public void checkNumberArgs(int argNum)
    throws WrongNumberArgsException
  {
    if (argNum < 2) {
      reportWrongNumberArgs();
    }
  }
  



  protected void reportWrongNumberArgs()
    throws WrongNumberArgsException
  {
    throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_TWO_OR_THREE", null));
  }
}
