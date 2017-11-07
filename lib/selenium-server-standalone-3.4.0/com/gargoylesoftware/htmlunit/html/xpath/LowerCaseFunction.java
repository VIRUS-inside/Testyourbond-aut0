package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.Locale;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;























public class LowerCaseFunction
  extends FunctionDef1Arg
{
  public LowerCaseFunction() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    return new XString(((XString)getArg0AsString(xctxt)).str().toLowerCase(Locale.ROOT));
  }
}
