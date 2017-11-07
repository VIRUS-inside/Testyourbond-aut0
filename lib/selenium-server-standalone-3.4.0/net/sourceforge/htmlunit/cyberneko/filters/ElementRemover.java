package net.sourceforge.htmlunit.cyberneko.filters;

import java.util.HashMap;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;























































































public class ElementRemover
  extends DefaultFilter
{
  protected static final Object NULL = new Object();
  







  protected HashMap<String, Object> fAcceptedElements = new HashMap();
  

  protected HashMap<String, Object> fRemovedElements = new HashMap();
  




  protected int fElementDepth;
  



  protected int fRemovalElementDepth;
  




  public ElementRemover() {}
  




  public void acceptElement(String element, String[] attributes)
  {
    String key = element.toLowerCase();
    Object value = NULL;
    if (attributes != null) {
      String[] newarray = new String[attributes.length];
      for (int i = 0; i < attributes.length; i++) {
        newarray[i] = attributes[i].toLowerCase();
      }
      value = attributes;
    }
    fAcceptedElements.put(key, value);
  }
  







  public void removeElement(String element)
  {
    String key = element.toLowerCase();
    Object value = NULL;
    fRemovedElements.put(key, value);
  }
  








  public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext, Augmentations augs)
    throws XNIException
  {
    fElementDepth = 0;
    fRemovalElementDepth = Integer.MAX_VALUE;
    super.startDocument(locator, encoding, nscontext, augs);
  }
  



  public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
    throws XNIException
  {
    startDocument(locator, encoding, null, augs);
  }
  

  public void startPrefixMapping(String prefix, String uri, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.startPrefixMapping(prefix, uri, augs);
    }
  }
  

  public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
    throws XNIException
  {
    if ((fElementDepth <= fRemovalElementDepth) && (handleOpenTag(element, attributes))) {
      super.startElement(element, attributes, augs);
    }
    fElementDepth += 1;
  }
  

  public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
    throws XNIException
  {
    if ((fElementDepth <= fRemovalElementDepth) && (handleOpenTag(element, attributes))) {
      super.emptyElement(element, attributes, augs);
    }
  }
  

  public void comment(XMLString text, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.comment(text, augs);
    }
  }
  

  public void processingInstruction(String target, XMLString data, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.processingInstruction(target, data, augs);
    }
  }
  

  public void characters(XMLString text, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.characters(text, augs);
    }
  }
  

  public void ignorableWhitespace(XMLString text, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.ignorableWhitespace(text, augs);
    }
  }
  

  public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.startGeneralEntity(name, id, encoding, augs);
    }
  }
  

  public void textDecl(String version, String encoding, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.textDecl(version, encoding, augs);
    }
  }
  

  public void endGeneralEntity(String name, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.endGeneralEntity(name, augs);
    }
  }
  
  public void startCDATA(Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.startCDATA(augs);
    }
  }
  
  public void endCDATA(Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.endCDATA(augs);
    }
  }
  

  public void endElement(QName element, Augmentations augs)
    throws XNIException
  {
    if ((fElementDepth <= fRemovalElementDepth) && (elementAccepted(rawname))) {
      super.endElement(element, augs);
    }
    fElementDepth -= 1;
    if (fElementDepth == fRemovalElementDepth) {
      fRemovalElementDepth = Integer.MAX_VALUE;
    }
  }
  

  public void endPrefixMapping(String prefix, Augmentations augs)
    throws XNIException
  {
    if (fElementDepth <= fRemovalElementDepth) {
      super.endPrefixMapping(prefix, augs);
    }
  }
  




  protected boolean elementAccepted(String element)
  {
    Object key = element.toLowerCase();
    return fAcceptedElements.containsKey(key);
  }
  
  protected boolean elementRemoved(String element)
  {
    Object key = element.toLowerCase();
    return fRemovedElements.containsKey(key);
  }
  
  protected boolean handleOpenTag(QName element, XMLAttributes attributes)
  {
    if (elementAccepted(rawname)) {
      Object key = rawname.toLowerCase();
      Object value = fAcceptedElements.get(key);
      if (value != NULL) {
        String[] anames = (String[])value;
        int attributeCount = attributes.getLength();
        for (int i = 0; i < attributeCount; i++) {
          String aname = attributes.getQName(i).toLowerCase();
          int j = 0;
          while (!anames[j].equals(aname))
          {
            j++; if (j >= anames.length)
            {



              attributes.removeAttributeAt(i--);
              attributeCount--;
            }
          }
        }
      } else { attributes.removeAllAttributes();
      }
      return true;
    }
    if (elementRemoved(rawname)) {
      fRemovalElementDepth = fElementDepth;
    }
    return false;
  }
}
