package org.jsoup.parser;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.helper.Validate;






public class Tag
{
  private static final Map<String, Tag> tags = new HashMap();
  
  private String tagName;
  private boolean isBlock = true;
  private boolean formatAsBlock = true;
  private boolean canContainInline = true;
  private boolean empty = false;
  private boolean selfClosing = false;
  private boolean preserveWhitespace = false;
  private boolean formList = false;
  private boolean formSubmit = false;
  
  private Tag(String tagName) {
    this.tagName = tagName;
  }
  




  public String getName()
  {
    return tagName;
  }
  









  public static Tag valueOf(String tagName, ParseSettings settings)
  {
    Validate.notNull(tagName);
    Tag tag = (Tag)tags.get(tagName);
    
    if (tag == null) {
      tagName = settings.normalizeTag(tagName);
      Validate.notEmpty(tagName);
      tag = (Tag)tags.get(tagName);
      
      if (tag == null)
      {
        tag = new Tag(tagName);
        isBlock = false;
      }
    }
    return tag;
  }
  








  public static Tag valueOf(String tagName)
  {
    return valueOf(tagName, ParseSettings.preserveCase);
  }
  




  public boolean isBlock()
  {
    return isBlock;
  }
  




  public boolean formatAsBlock()
  {
    return formatAsBlock;
  }
  


  /**
   * @deprecated
   */
  public boolean canContainBlock()
  {
    return isBlock;
  }
  




  public boolean isInline()
  {
    return !isBlock;
  }
  




  public boolean isData()
  {
    return (!canContainInline) && (!isEmpty());
  }
  




  public boolean isEmpty()
  {
    return empty;
  }
  




  public boolean isSelfClosing()
  {
    return (empty) || (selfClosing);
  }
  




  public boolean isKnownTag()
  {
    return tags.containsKey(tagName);
  }
  





  public static boolean isKnownTag(String tagName)
  {
    return tags.containsKey(tagName);
  }
  




  public boolean preserveWhitespace()
  {
    return preserveWhitespace;
  }
  



  public boolean isFormListed()
  {
    return formList;
  }
  



  public boolean isFormSubmittable()
  {
    return formSubmit;
  }
  
  Tag setSelfClosing() {
    selfClosing = true;
    return this;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Tag)) { return false;
    }
    Tag tag = (Tag)o;
    
    if (!tagName.equals(tagName)) return false;
    if (canContainInline != canContainInline) return false;
    if (empty != empty) return false;
    if (formatAsBlock != formatAsBlock) return false;
    if (isBlock != isBlock) return false;
    if (preserveWhitespace != preserveWhitespace) return false;
    if (selfClosing != selfClosing) return false;
    if (formList != formList) return false;
    return formSubmit == formSubmit;
  }
  
  public int hashCode()
  {
    int result = tagName.hashCode();
    result = 31 * result + (isBlock ? 1 : 0);
    result = 31 * result + (formatAsBlock ? 1 : 0);
    result = 31 * result + (canContainInline ? 1 : 0);
    result = 31 * result + (empty ? 1 : 0);
    result = 31 * result + (selfClosing ? 1 : 0);
    result = 31 * result + (preserveWhitespace ? 1 : 0);
    result = 31 * result + (formList ? 1 : 0);
    result = 31 * result + (formSubmit ? 1 : 0);
    return result;
  }
  
  public String toString()
  {
    return tagName;
  }
  


  private static final String[] blockTags = { "html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame", "noframes", "section", "nav", "aside", "hgroup", "header", "footer", "p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", "div", "blockquote", "hr", "address", "figure", "figcaption", "form", "fieldset", "ins", "del", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th", "td", "video", "audio", "canvas", "details", "menu", "plaintext", "template", "article", "main", "svg", "math" };
  






  private static final String[] inlineTags = { "object", "base", "font", "tt", "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "time", "acronym", "mark", "ruby", "rt", "rp", "a", "img", "br", "wbr", "map", "q", "sub", "sup", "bdo", "iframe", "embed", "span", "input", "select", "textarea", "label", "button", "optgroup", "option", "legend", "datalist", "keygen", "output", "progress", "meter", "area", "param", "source", "track", "summary", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track", "data", "bdi", "s" };
  






  private static final String[] emptyTags = { "meta", "link", "base", "frame", "img", "br", "wbr", "embed", "hr", "input", "keygen", "col", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track" };
  


  private static final String[] formatAsInlineTags = { "title", "a", "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", "style", "ins", "del", "s" };
  


  private static final String[] preserveWhitespaceTags = { "pre", "plaintext", "title", "textarea" };
  



  private static final String[] formListedTags = { "button", "fieldset", "input", "keygen", "object", "output", "select", "textarea" };
  

  private static final String[] formSubmitTags = { "input", "keygen", "object", "select", "textarea" };
  


  static
  {
    for (String tagName : blockTags) {
      Tag tag = new Tag(tagName);
      register(tag);
    }
    for (String tagName : inlineTags) {
      Tag tag = new Tag(tagName);
      isBlock = false;
      formatAsBlock = false;
      register(tag);
    }
    

    for (String tagName : emptyTags) {
      Tag tag = (Tag)tags.get(tagName);
      Validate.notNull(tag);
      canContainInline = false;
      empty = true;
    }
    
    for (String tagName : formatAsInlineTags) {
      Tag tag = (Tag)tags.get(tagName);
      Validate.notNull(tag);
      formatAsBlock = false;
    }
    
    for (String tagName : preserveWhitespaceTags) {
      Tag tag = (Tag)tags.get(tagName);
      Validate.notNull(tag);
      preserveWhitespace = true;
    }
    
    for (String tagName : formListedTags) {
      Tag tag = (Tag)tags.get(tagName);
      Validate.notNull(tag);
      formList = true;
    }
    
    for (String tagName : formSubmitTags) {
      Tag tag = (Tag)tags.get(tagName);
      Validate.notNull(tag);
      formSubmit = true;
    }
  }
  
  private static void register(Tag tag) {
    tags.put(tagName, tag);
  }
}
