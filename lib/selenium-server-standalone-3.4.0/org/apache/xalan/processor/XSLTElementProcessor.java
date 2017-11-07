package org.apache.xalan.processor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.IntStack;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;








































public class XSLTElementProcessor
  extends ElemTemplateElement
{
  static final long serialVersionUID = 5597421564955304421L;
  private IntStack m_savedLastOrder;
  private XSLTElementDef m_elemDef;
  
  XSLTElementProcessor() {}
  
  XSLTElementDef getElemDef()
  {
    return m_elemDef;
  }
  





  void setElemDef(XSLTElementDef def)
  {
    m_elemDef = def;
  }
  













  public InputSource resolveEntity(StylesheetHandler handler, String publicId, String systemId)
    throws SAXException
  {
    return null;
  }
  














  public void notationDecl(StylesheetHandler handler, String name, String publicId, String systemId) {}
  













  public void unparsedEntityDecl(StylesheetHandler handler, String name, String publicId, String systemId, String notationName) {}
  













  public void startNonText(StylesheetHandler handler)
    throws SAXException
  {}
  













  public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
    throws SAXException
  {
    if (m_savedLastOrder == null)
      m_savedLastOrder = new IntStack();
    m_savedLastOrder.push(getElemDef().getLastOrder());
    getElemDef().setLastOrder(-1);
  }
  









  public void endElement(StylesheetHandler handler, String uri, String localName, String rawName)
    throws SAXException
  {
    if ((m_savedLastOrder != null) && (!m_savedLastOrder.empty())) {
      getElemDef().setLastOrder(m_savedLastOrder.pop());
    }
    if (!getElemDef().getRequiredFound()) {
      handler.error("ER_REQUIRED_ELEM_NOT_FOUND", new Object[] { getElemDef().getRequiredElem() }, null);
    }
  }
  










  public void characters(StylesheetHandler handler, char[] ch, int start, int length)
    throws SAXException
  {
    handler.error("ER_CHARS_NOT_ALLOWED", null, null);
  }
  













  public void ignorableWhitespace(StylesheetHandler handler, char[] ch, int start, int length)
    throws SAXException
  {}
  












  public void processingInstruction(StylesheetHandler handler, String target, String data)
    throws SAXException
  {}
  












  public void skippedEntity(StylesheetHandler handler, String name)
    throws SAXException
  {}
  












  void setPropertiesFromAttributes(StylesheetHandler handler, String rawName, Attributes attributes, ElemTemplateElement target)
    throws SAXException
  {
    setPropertiesFromAttributes(handler, rawName, attributes, target, true);
  }
  

















  Attributes setPropertiesFromAttributes(StylesheetHandler handler, String rawName, Attributes attributes, ElemTemplateElement target, boolean throwError)
    throws SAXException
  {
    XSLTElementDef def = getElemDef();
    AttributesImpl undefines = null;
    boolean isCompatibleMode = ((null != handler.getStylesheet()) && (handler.getStylesheet().getCompatibleMode())) || (!throwError);
    

    if (isCompatibleMode) {
      undefines = new AttributesImpl();
    }
    


    List processedDefs = new ArrayList();
    

    List errorDefs = new ArrayList();
    int nAttrs = attributes.getLength();
    
    for (int i = 0; i < nAttrs; i++)
    {
      String attrUri = attributes.getURI(i);
      
      if ((null != attrUri) && (attrUri.length() == 0) && ((attributes.getQName(i).startsWith("xmlns:")) || (attributes.getQName(i).equals("xmlns"))))
      {


        attrUri = "http://www.w3.org/XML/1998/namespace";
      }
      String attrLocalName = attributes.getLocalName(i);
      XSLTAttributeDef attrDef = def.getAttributeDef(attrUri, attrLocalName);
      
      if (null == attrDef)
      {
        if (!isCompatibleMode)
        {


          handler.error("ER_ATTR_NOT_ALLOWED", new Object[] { attributes.getQName(i), rawName }, null);

        }
        else
        {

          undefines.addAttribute(attrUri, attrLocalName, attributes.getQName(i), attributes.getType(i), attributes.getValue(i));

        }
        

      }
      else
      {

        if (handler.getStylesheetProcessor() == null)
          System.out.println("stylesheet processor null");
        if ((attrDef.getName().compareTo("*") == 0) && (handler.getStylesheetProcessor().isSecureProcessing()))
        {


          handler.error("ER_ATTR_NOT_ALLOWED", new Object[] { attributes.getQName(i), rawName }, null);


        }
        else
        {


          boolean success = attrDef.setAttrValue(handler, attrUri, attrLocalName, attributes.getQName(i), attributes.getValue(i), target);
          



          if (success) {
            processedDefs.add(attrDef);
          } else {
            errorDefs.add(attrDef);
          }
        }
      }
    }
    XSLTAttributeDef[] attrDefs = def.getAttributes();
    int nAttrDefs = attrDefs.length;
    
    for (int i = 0; i < nAttrDefs; i++)
    {
      XSLTAttributeDef attrDef = attrDefs[i];
      String defVal = attrDef.getDefault();
      
      if (null != defVal)
      {
        if (!processedDefs.contains(attrDef))
        {
          attrDef.setDefAttrValue(handler, target);
        }
      }
      
      if (attrDef.getRequired())
      {
        if ((!processedDefs.contains(attrDef)) && (!errorDefs.contains(attrDef))) {
          handler.error(XSLMessages.createMessage("ER_REQUIRES_ATTRIB", new Object[] { rawName, attrDef.getName() }), null);
        }
      }
    }
    


    return undefines;
  }
}
