package org.jsoup.safety;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;























































public class Whitelist
{
  private Set<TagName> tagNames;
  private Map<TagName, Set<AttributeKey>> attributes;
  private Map<TagName, Map<AttributeKey, AttributeValue>> enforcedAttributes;
  private Map<TagName, Map<AttributeKey, Set<Protocol>>> protocols;
  private boolean preserveRelativeLinks;
  
  public static Whitelist none()
  {
    return new Whitelist();
  }
  






  public static Whitelist simpleText()
  {
    return new Whitelist().addTags(new String[] { "b", "em", "i", "strong", "u" });
  }
  





























  public static Whitelist basic()
  {
    return new Whitelist().addTags(new String[] { "a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "u", "ul" }).addAttributes("a", new String[] { "href" }).addAttributes("blockquote", new String[] { "cite" }).addAttributes("q", new String[] { "cite" }).addProtocols("a", "href", new String[] { "ftp", "http", "https", "mailto" }).addProtocols("blockquote", "cite", new String[] { "http", "https" }).addProtocols("cite", "cite", new String[] { "http", "https" }).addEnforcedAttribute("a", "rel", "nofollow");
  }
  










  public static Whitelist basicWithImages()
  {
    return basic().addTags(new String[] { "img" }).addAttributes("img", new String[] { "align", "alt", "height", "src", "title", "width" }).addProtocols("img", "src", new String[] { "http", "https" });
  }
  




































  public static Whitelist relaxed()
  {
    return new Whitelist().addTags(new String[] { "a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul" }).addAttributes("a", new String[] { "href", "title" }).addAttributes("blockquote", new String[] { "cite" }).addAttributes("col", new String[] { "span", "width" }).addAttributes("colgroup", new String[] { "span", "width" }).addAttributes("img", new String[] { "align", "alt", "height", "src", "title", "width" }).addAttributes("ol", new String[] { "start", "type" }).addAttributes("q", new String[] { "cite" }).addAttributes("table", new String[] { "summary", "width" }).addAttributes("td", new String[] { "abbr", "axis", "colspan", "rowspan", "width" }).addAttributes("th", new String[] { "abbr", "axis", "colspan", "rowspan", "scope", "width" }).addAttributes("ul", new String[] { "type" }).addProtocols("a", "href", new String[] { "ftp", "http", "https", "mailto" }).addProtocols("blockquote", "cite", new String[] { "http", "https" }).addProtocols("cite", "cite", new String[] { "http", "https" }).addProtocols("img", "src", new String[] { "http", "https" }).addProtocols("q", "cite", new String[] { "http", "https" });
  }
  








  public Whitelist()
  {
    tagNames = new HashSet();
    attributes = new HashMap();
    enforcedAttributes = new HashMap();
    protocols = new HashMap();
    preserveRelativeLinks = false;
  }
  





  public Whitelist addTags(String... tags)
  {
    Validate.notNull(tags);
    
    for (String tagName : tags) {
      Validate.notEmpty(tagName);
      tagNames.add(TagName.valueOf(tagName));
    }
    return this;
  }
  





  public Whitelist removeTags(String... tags)
  {
    Validate.notNull(tags);
    
    for (String tag : tags) {
      Validate.notEmpty(tag);
      TagName tagName = TagName.valueOf(tag);
      
      if (tagNames.remove(tagName)) {
        attributes.remove(tagName);
        enforcedAttributes.remove(tagName);
        protocols.remove(tagName);
      }
    }
    return this;
  }
  














  public Whitelist addAttributes(String tag, String... attributes)
  {
    Validate.notEmpty(tag);
    Validate.notNull(attributes);
    Validate.isTrue(attributes.length > 0, "No attribute names supplied.");
    
    TagName tagName = TagName.valueOf(tag);
    if (!tagNames.contains(tagName))
      tagNames.add(tagName);
    Set<AttributeKey> attributeSet = new HashSet();
    for (String key : attributes) {
      Validate.notEmpty(key);
      attributeSet.add(AttributeKey.valueOf(key));
    }
    if (this.attributes.containsKey(tagName)) {
      Object currentSet = (Set)this.attributes.get(tagName);
      ((Set)currentSet).addAll(attributeSet);
    } else {
      this.attributes.put(tagName, attributeSet);
    }
    return this;
  }
  














  public Whitelist removeAttributes(String tag, String... attributes)
  {
    Validate.notEmpty(tag);
    Validate.notNull(attributes);
    Validate.isTrue(attributes.length > 0, "No attribute names supplied.");
    
    TagName tagName = TagName.valueOf(tag);
    Set<AttributeKey> attributeSet = new HashSet();
    for (String key : attributes) {
      Validate.notEmpty(key);
      attributeSet.add(AttributeKey.valueOf(key)); }
    Object currentSet;
    if ((tagNames.contains(tagName)) && (this.attributes.containsKey(tagName))) {
      currentSet = (Set)this.attributes.get(tagName);
      ((Set)currentSet).removeAll(attributeSet);
      
      if (((Set)currentSet).isEmpty())
        this.attributes.remove(tagName);
    }
    if (tag.equals(":all"))
      for (currentSet = this.attributes.keySet().iterator(); ((Iterator)currentSet).hasNext();) { TagName name = (TagName)((Iterator)currentSet).next();
        Object currentSet = (Set)this.attributes.get(name);
        ((Set)currentSet).removeAll(attributeSet);
        
        if (((Set)currentSet).isEmpty())
          this.attributes.remove(name);
      }
    return this;
  }
  












  public Whitelist addEnforcedAttribute(String tag, String attribute, String value)
  {
    Validate.notEmpty(tag);
    Validate.notEmpty(attribute);
    Validate.notEmpty(value);
    
    TagName tagName = TagName.valueOf(tag);
    if (!tagNames.contains(tagName))
      tagNames.add(tagName);
    AttributeKey attrKey = AttributeKey.valueOf(attribute);
    AttributeValue attrVal = AttributeValue.valueOf(value);
    
    if (enforcedAttributes.containsKey(tagName)) {
      ((Map)enforcedAttributes.get(tagName)).put(attrKey, attrVal);
    } else {
      Map<AttributeKey, AttributeValue> attrMap = new HashMap();
      attrMap.put(attrKey, attrVal);
      enforcedAttributes.put(tagName, attrMap);
    }
    return this;
  }
  






  public Whitelist removeEnforcedAttribute(String tag, String attribute)
  {
    Validate.notEmpty(tag);
    Validate.notEmpty(attribute);
    
    TagName tagName = TagName.valueOf(tag);
    if ((tagNames.contains(tagName)) && (enforcedAttributes.containsKey(tagName))) {
      AttributeKey attrKey = AttributeKey.valueOf(attribute);
      Map<AttributeKey, AttributeValue> attrMap = (Map)enforcedAttributes.get(tagName);
      attrMap.remove(attrKey);
      
      if (attrMap.isEmpty())
        enforcedAttributes.remove(tagName);
    }
    return this;
  }
  














  public Whitelist preserveRelativeLinks(boolean preserve)
  {
    preserveRelativeLinks = preserve;
    return this;
  }
  















  public Whitelist addProtocols(String tag, String attribute, String... protocols)
  {
    Validate.notEmpty(tag);
    Validate.notEmpty(attribute);
    Validate.notNull(protocols);
    
    TagName tagName = TagName.valueOf(tag);
    AttributeKey attrKey = AttributeKey.valueOf(attribute);
    
    Map<AttributeKey, Set<Protocol>> attrMap;
    Map<AttributeKey, Set<Protocol>> attrMap;
    if (this.protocols.containsKey(tagName)) {
      attrMap = (Map)this.protocols.get(tagName);
    } else {
      attrMap = new HashMap();
      this.protocols.put(tagName, attrMap); }
    Set<Protocol> protSet;
    Set<Protocol> protSet; if (attrMap.containsKey(attrKey)) {
      protSet = (Set)attrMap.get(attrKey);
    } else {
      protSet = new HashSet();
      attrMap.put(attrKey, protSet);
    }
    for (String protocol : protocols) {
      Validate.notEmpty(protocol);
      Protocol prot = Protocol.valueOf(protocol);
      protSet.add(prot);
    }
    return this;
  }
  











  public Whitelist removeProtocols(String tag, String attribute, String... removeProtocols)
  {
    Validate.notEmpty(tag);
    Validate.notEmpty(attribute);
    Validate.notNull(removeProtocols);
    
    TagName tagName = TagName.valueOf(tag);
    AttributeKey attr = AttributeKey.valueOf(attribute);
    


    Validate.isTrue(protocols.containsKey(tagName), "Cannot remove a protocol that is not set.");
    Map<AttributeKey, Set<Protocol>> tagProtocols = (Map)protocols.get(tagName);
    Validate.isTrue(tagProtocols.containsKey(attr), "Cannot remove a protocol that is not set.");
    
    Set<Protocol> attrProtocols = (Set)tagProtocols.get(attr);
    for (String protocol : removeProtocols) {
      Validate.notEmpty(protocol);
      attrProtocols.remove(Protocol.valueOf(protocol));
    }
    
    if (attrProtocols.isEmpty()) {
      tagProtocols.remove(attr);
      if (tagProtocols.isEmpty())
        protocols.remove(tagName);
    }
    return this;
  }
  




  protected boolean isSafeTag(String tag)
  {
    return tagNames.contains(TagName.valueOf(tag));
  }
  






  protected boolean isSafeAttribute(String tagName, Element el, Attribute attr)
  {
    TagName tag = TagName.valueOf(tagName);
    AttributeKey key = AttributeKey.valueOf(attr.getKey());
    
    Set<AttributeKey> okSet = (Set)attributes.get(tag);
    if ((okSet != null) && (okSet.contains(key))) {
      if (protocols.containsKey(tag)) {
        Map<AttributeKey, Set<Protocol>> attrProts = (Map)protocols.get(tag);
        
        return (!attrProts.containsKey(key)) || (testValidProtocol(el, attr, (Set)attrProts.get(key)));
      }
      return true;
    }
    

    Map<AttributeKey, AttributeValue> enforcedSet = (Map)enforcedAttributes.get(tag);
    if (enforcedSet != null) {
      Attributes expect = getEnforcedAttributes(tagName);
      String attrKey = attr.getKey();
      if (expect.hasKeyIgnoreCase(attrKey)) {
        return expect.getIgnoreCase(attrKey).equals(attr.getValue());
      }
    }
    
    return (!tagName.equals(":all")) && (isSafeAttribute(":all", el, attr));
  }
  

  private boolean testValidProtocol(Element el, Attribute attr, Set<Protocol> protocols)
  {
    String value = el.absUrl(attr.getKey());
    if (value.length() == 0)
      value = attr.getValue();
    if (!preserveRelativeLinks) {
      attr.setValue(value);
    }
    for (Protocol protocol : protocols) {
      String prot = protocol.toString();
      
      if (prot.equals("#")) {
        if (isValidAnchor(value)) {
          return true;
        }
        
      }
      else
      {
        prot = prot + ":";
        
        if (Normalizer.lowerCase(value).startsWith(prot))
          return true;
      }
    }
    return false;
  }
  
  private boolean isValidAnchor(String value) {
    return (value.startsWith("#")) && (!value.matches(".*\\s.*"));
  }
  
  Attributes getEnforcedAttributes(String tagName) {
    Attributes attrs = new Attributes();
    TagName tag = TagName.valueOf(tagName);
    if (enforcedAttributes.containsKey(tag)) {
      Map<AttributeKey, AttributeValue> keyVals = (Map)enforcedAttributes.get(tag);
      for (Map.Entry<AttributeKey, AttributeValue> entry : keyVals.entrySet()) {
        attrs.put(((AttributeKey)entry.getKey()).toString(), ((AttributeValue)entry.getValue()).toString());
      }
    }
    return attrs;
  }
  
  static class TagName extends Whitelist.TypedValue
  {
    TagName(String value)
    {
      super();
    }
    
    static TagName valueOf(String value) {
      return new TagName(value);
    }
  }
  
  static class AttributeKey extends Whitelist.TypedValue {
    AttributeKey(String value) {
      super();
    }
    
    static AttributeKey valueOf(String value) {
      return new AttributeKey(value);
    }
  }
  
  static class AttributeValue extends Whitelist.TypedValue {
    AttributeValue(String value) {
      super();
    }
    
    static AttributeValue valueOf(String value) {
      return new AttributeValue(value);
    }
  }
  
  static class Protocol extends Whitelist.TypedValue {
    Protocol(String value) {
      super();
    }
    
    static Protocol valueOf(String value) {
      return new Protocol(value);
    }
  }
  
  static abstract class TypedValue {
    private String value;
    
    TypedValue(String value) {
      Validate.notNull(value);
      this.value = value;
    }
    
    public int hashCode()
    {
      int prime = 31;
      int result = 1;
      result = 31 * result + (value == null ? 0 : value.hashCode());
      return result;
    }
    
    public boolean equals(Object obj)
    {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      TypedValue other = (TypedValue)obj;
      if (value == null) {
        if (value != null) return false;
      } else if (!value.equals(value)) return false;
      return true;
    }
    
    public String toString()
    {
      return value;
    }
  }
}
