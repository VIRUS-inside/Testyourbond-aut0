package net.sourceforge.htmlunit.cyberneko.filters;

import java.util.Locale;
import net.sourceforge.htmlunit.cyberneko.HTMLAugmentations;
import net.sourceforge.htmlunit.cyberneko.HTMLEventInfo;
import net.sourceforge.htmlunit.cyberneko.HTMLEventInfo.SynthesizedItem;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;








































































public class Purifier
  extends DefaultFilter
{
  public static final String SYNTHESIZED_NAMESPACE_PREFX = "http://cyberneko.org/html/ns/synthesized/";
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
  protected static final HTMLEventInfo SYNTHESIZED_ITEM = new HTMLEventInfo.SynthesizedItem();
  



  protected boolean fNamespaces;
  



  protected boolean fAugmentations;
  



  protected boolean fSeenDoctype;
  



  protected boolean fSeenRootElement;
  


  protected boolean fInCDATASection;
  


  protected String fPublicId;
  


  protected String fSystemId;
  


  protected NamespaceContext fNamespaceContext;
  


  protected int fSynthesizedNamespaceCount;
  


  private QName fQName = new QName();
  

  private final HTMLAugmentations fInfosetAugs = new HTMLAugmentations();
  

  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  


  public Purifier() {}
  


  public void reset(XMLComponentManager manager)
    throws XMLConfigurationException
  {
    fInCDATASection = false;
    

    fNamespaces = manager.getFeature("http://xml.org/sax/features/namespaces");
    fAugmentations = manager.getFeature("http://cyberneko.org/html/features/augmentations");
  }
  






  public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
    throws XNIException
  {
    fNamespaceContext = (fNamespaces ? 
      new NamespaceBinder.NamespaceSupport() : null);
    fSynthesizedNamespaceCount = 0;
    handleStartDocument();
    super.startDocument(locator, encoding, augs);
  }
  


  public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext, Augmentations augs)
    throws XNIException
  {
    fNamespaceContext = nscontext;
    fSynthesizedNamespaceCount = 0;
    handleStartDocument();
    super.startDocument(locator, encoding, nscontext, augs);
  }
  

  public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
    throws XNIException
  {
    if ((version == null) || (!version.equals("1.0"))) {
      version = "1.0";
    }
    if ((encoding != null) && (encoding.length() == 0)) {
      encoding = null;
    }
    if (standalone != null) {
      if ((!standalone.equalsIgnoreCase("true")) && 
        (!standalone.equalsIgnoreCase("false"))) {
        standalone = null;
      }
      else {
        standalone = standalone.toLowerCase();
      }
    }
    super.xmlDecl(version, encoding, standalone, augs);
  }
  

  public void comment(XMLString text, Augmentations augs)
    throws XNIException
  {
    StringBuilder str = new StringBuilder(purifyText(text).toString());
    int length = str.length();
    for (int i = length - 1; i >= 0; i--) {
      char c = str.charAt(i);
      if (c == '-') {
        str.insert(i + 1, ' ');
      }
    }
    fStringBuffer.length = 0;
    fStringBuffer.append(str.toString());
    text = fStringBuffer;
    super.comment(text, augs);
  }
  


  public void processingInstruction(String target, XMLString data, Augmentations augs)
    throws XNIException
  {
    target = purifyName(target, true);
    data = purifyText(data);
    super.processingInstruction(target, data, augs);
  }
  

  public void doctypeDecl(String root, String pubid, String sysid, Augmentations augs)
    throws XNIException
  {
    fSeenDoctype = true;
    

    fPublicId = pubid;
    fSystemId = sysid;
    

    if ((fPublicId != null) && (fSystemId == null)) {
      fSystemId = "";
    }
  }
  



  public void startElement(QName element, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    handleStartElement(element, attrs);
    super.startElement(element, attrs, augs);
  }
  

  public void emptyElement(QName element, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    handleStartElement(element, attrs);
    super.emptyElement(element, attrs, augs);
  }
  
  public void startCDATA(Augmentations augs)
    throws XNIException
  {
    fInCDATASection = true;
    super.startCDATA(augs);
  }
  
  public void endCDATA(Augmentations augs)
    throws XNIException
  {
    fInCDATASection = false;
    super.endCDATA(augs);
  }
  

  public void characters(XMLString text, Augmentations augs)
    throws XNIException
  {
    text = purifyText(text);
    if (fInCDATASection) {
      StringBuilder str = new StringBuilder(text.toString());
      int length = str.length();
      for (int i = length - 1; i >= 0; i--) {
        char c = str.charAt(i);
        if (c == ']') {
          str.insert(i + 1, ' ');
        }
      }
      fStringBuffer.length = 0;
      fStringBuffer.append(str.toString());
      text = fStringBuffer;
    }
    super.characters(text, augs);
  }
  

  public void endElement(QName element, Augmentations augs)
    throws XNIException
  {
    element = purifyQName(element);
    if ((fNamespaces) && 
      (prefix != null) && (uri == null)) {
      uri = fNamespaceContext.getURI(prefix);
    }
    
    super.endElement(element, augs);
  }
  




  protected void handleStartDocument()
  {
    fSeenDoctype = false;
    fSeenRootElement = false;
  }
  


  protected void handleStartElement(QName element, XMLAttributes attrs)
  {
    element = purifyQName(element);
    int attrCount = attrs != null ? attrs.getLength() : 0;
    for (int i = attrCount - 1; i >= 0; i--)
    {
      attrs.getName(i, fQName);
      attrs.setName(i, purifyQName(fQName));
      

      if ((fNamespaces) && 
        (!fQName.rawname.equals("xmlns")) && 
        (!fQName.rawname.startsWith("xmlns:")))
      {


        attrs.getName(i, fQName);
        if ((fQName.prefix != null) && (fQName.uri == null)) {
          synthesizeBinding(attrs, fQName.prefix);
        }
      }
    }
    


    if ((fNamespaces) && 
      (prefix != null) && (uri == null)) {
      synthesizeBinding(attrs, prefix);
    }
    


    if ((!fSeenRootElement) && (fSeenDoctype)) {
      Augmentations augs = synthesizedAugs();
      super.doctypeDecl(rawname, fPublicId, fSystemId, augs);
    }
    

    fSeenRootElement = true;
  }
  

  protected void synthesizeBinding(XMLAttributes attrs, String ns)
  {
    String prefix = "xmlns";
    String localpart = ns;
    String qname = prefix + ':' + localpart;
    String uri = "http://cyberneko.org/html/properties/namespaces-uri";
    String atype = "CDATA";
    String avalue = "http://cyberneko.org/html/ns/synthesized/" + fSynthesizedNamespaceCount++;
    

    fQName.setValues(prefix, localpart, qname, uri);
    attrs.addAttribute(fQName, atype, avalue);
    

    XercesBridge.getInstance().NamespaceContext_declarePrefix(fNamespaceContext, ns, avalue);
  }
  

  protected final Augmentations synthesizedAugs()
  {
    HTMLAugmentations augs = null;
    if (fAugmentations) {
      augs = fInfosetAugs;
      augs.removeAllItems();
      augs.putItem("http://cyberneko.org/html/features/augmentations", SYNTHESIZED_ITEM);
    }
    return augs;
  }
  




  protected QName purifyQName(QName qname)
  {
    prefix = purifyName(prefix, true);
    localpart = purifyName(localpart, true);
    rawname = purifyName(rawname, false);
    return qname;
  }
  
  protected String purifyName(String name, boolean localpart)
  {
    if (name == null) {
      return name;
    }
    StringBuilder str = new StringBuilder();
    int length = name.length();
    boolean seenColon = localpart;
    for (int i = 0; i < length; i++) {
      char c = name.charAt(i);
      if (i == 0) {
        if (!XMLChar.isNameStart(c)) {
          str.append("_u" + toHexString(c, 4) + "_");
        }
        else {
          str.append(c);
        }
      }
      else {
        if (((fNamespaces) && (c == ':') && (seenColon)) || (!XMLChar.isName(c))) {
          str.append("_u" + toHexString(c, 4) + "_");
        }
        else {
          str.append(c);
        }
        seenColon = (seenColon) || (c == ':');
      }
    }
    return str.toString();
  }
  
  protected XMLString purifyText(XMLString text)
  {
    fStringBuffer.length = 0;
    for (int i = 0; i < length; i++) {
      char c = ch[(offset + i)];
      if (XMLChar.isInvalid(c)) {
        fStringBuffer.append("\\u" + toHexString(c, 4));
      }
      else {
        fStringBuffer.append(c);
      }
    }
    return fStringBuffer;
  }
  




  protected static String toHexString(int c, int padlen)
  {
    StringBuilder str = new StringBuilder(padlen);
    str.append(Integer.toHexString(c));
    int len = padlen - str.length();
    for (int i = 0; i < len; i++) {
      str.insert(0, '0');
    }
    return str.toString().toUpperCase(Locale.ENGLISH);
  }
}
