package org.jsoup.parser;

import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;














public class ParseSettings
{
  public static final ParseSettings htmlDefault = new ParseSettings(false, false);
  public static final ParseSettings preserveCase = new ParseSettings(true, true);
  

  private final boolean preserveTagCase;
  

  private final boolean preserveAttributeCase;
  


  public ParseSettings(boolean tag, boolean attribute)
  {
    preserveTagCase = tag;
    preserveAttributeCase = attribute;
  }
  
  String normalizeTag(String name) {
    name = name.trim();
    if (!preserveTagCase)
      name = Normalizer.lowerCase(name);
    return name;
  }
  
  String normalizeAttribute(String name) {
    name = name.trim();
    if (!preserveAttributeCase)
      name = Normalizer.lowerCase(name);
    return name;
  }
  
  Attributes normalizeAttributes(Attributes attributes) {
    if (!preserveAttributeCase) {
      for (Attribute attr : attributes) {
        attr.setKey(Normalizer.lowerCase(attr.getKey()));
      }
    }
    return attributes;
  }
}
