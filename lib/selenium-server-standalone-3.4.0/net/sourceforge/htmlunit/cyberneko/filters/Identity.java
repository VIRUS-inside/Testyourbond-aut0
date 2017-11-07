package net.sourceforge.htmlunit.cyberneko.filters;

import net.sourceforge.htmlunit.cyberneko.HTMLEventInfo;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;























































public class Identity
  extends DefaultFilter
{
  protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
  protected static final String FILTERS = "http://cyberneko.org/html/properties/filters";
  
  public Identity() {}
  
  public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
    throws XNIException
  {
    if ((augs == null) || (!synthesized(augs))) {
      super.startElement(element, attributes, augs);
    }
  }
  

  public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
    throws XNIException
  {
    if ((augs == null) || (!synthesized(augs))) {
      super.emptyElement(element, attributes, augs);
    }
  }
  

  public void endElement(QName element, Augmentations augs)
    throws XNIException
  {
    if ((augs == null) || (!synthesized(augs))) {
      super.endElement(element, augs);
    }
  }
  




  protected static boolean synthesized(Augmentations augs)
  {
    HTMLEventInfo info = (HTMLEventInfo)augs.getItem("http://cyberneko.org/html/features/augmentations");
    return info != null ? info.isSynthesized() : false;
  }
}
