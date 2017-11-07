package net.sourceforge.htmlunit.cyberneko.parsers;

import net.sourceforge.htmlunit.cyberneko.HTMLConfiguration;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XNIException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;




































public class DOMParser
  extends org.apache.xerces.parsers.DOMParser
{
  public DOMParser()
  {
    super(new HTMLConfiguration());
    try
    {
      setProperty("http://apache.org/xml/properties/dom/document-class-name", 
        "org.apache.html.dom.HTMLDocumentImpl");
    }
    catch (SAXNotRecognizedException e) {
      throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not recognized");
    }
    catch (SAXNotSupportedException e) {
      throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not supported");
    }
  }
  
















  public void doctypeDecl(String root, String pubid, String sysid, Augmentations augs)
    throws XNIException
  {
    String VERSION = XercesBridge.getInstance().getVersion();
    boolean okay = true;
    if (VERSION.startsWith("Xerces-J 2.")) {
      okay = getParserSubVersion() > 5;



    }
    else if (VERSION.startsWith("XML4J")) {
      okay = false;
    }
    

    if (okay) {
      super.doctypeDecl(root, pubid, sysid, augs);
    }
  }
  




  private static int getParserSubVersion()
  {
    try
    {
      String VERSION = XercesBridge.getInstance().getVersion();
      int index1 = VERSION.indexOf('.') + 1;
      int index2 = VERSION.indexOf('.', index1);
      if (index2 == -1) index2 = VERSION.length();
      return Integer.parseInt(VERSION.substring(index1, index2));
    }
    catch (Exception e) {}
    return -1;
  }
}
