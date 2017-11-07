package net.sourceforge.htmlunit.cyberneko;

import java.util.ArrayList;
import java.util.List;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;

























class LostText
{
  LostText() {}
  
  static class Entry
  {
    private XMLString text_;
    private Augmentations augs_;
    
    public Entry(XMLString text, Augmentations augs)
    {
      char[] chars = new char[length];
      System.arraycopy(ch, offset, chars, 0, length);
      text_ = new XMLString(chars, 0, chars.length);
      if (augs != null)
        augs_ = new HTMLAugmentations(augs);
    } }
  
  private final List<Entry> entries = new ArrayList();
  



  public void add(XMLString text, Augmentations augs)
  {
    if ((!entries.isEmpty()) || (text.toString().trim().length() > 0)) {
      entries.add(new Entry(text, augs));
    }
  }
  


  public void refeed(XMLDocumentHandler tagBalancer)
  {
    for (Entry entry : entries) {
      Entry lostEntry = entry;
      tagBalancer.characters(text_, augs_);
    }
    
    entries.clear();
  }
  



  public boolean isEmpty()
  {
    return entries.isEmpty();
  }
}
