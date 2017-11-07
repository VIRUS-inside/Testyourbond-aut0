package net.sourceforge.htmlunit.cyberneko;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.xerces.xni.Augmentations;





































public class HTMLAugmentations
  implements Augmentations
{
  protected final Hashtable<String, Object> fItems = new Hashtable();
  




  public HTMLAugmentations() {}
  




  HTMLAugmentations(Augmentations augs)
  {
    for (Enumeration keys = augs.keys(); keys.hasMoreElements();) {
      String key = (String)keys.nextElement();
      Object value = augs.getItem(key);
      if ((value instanceof HTMLScanner.LocationItem)) {
        value = new HTMLScanner.LocationItem((HTMLScanner.LocationItem)value);
      }
      fItems.put(key, value);
    }
  }
  



  public void removeAllItems()
  {
    fItems.clear();
  }
  


  public void clear()
  {
    fItems.clear();
  }
  














  public Object putItem(String key, Object item)
  {
    return fItems.put(key, item);
  }
  










  public Object getItem(String key)
  {
    return fItems.get(key);
  }
  







  public Object removeItem(String key)
  {
    return fItems.remove(key);
  }
  



  public Enumeration keys()
  {
    return fItems.keys();
  }
}
