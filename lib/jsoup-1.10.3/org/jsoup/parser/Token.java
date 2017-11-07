package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;




abstract class Token
{
  TokenType type;
  
  private Token() {}
  
  String tokenType()
  {
    return getClass().getSimpleName();
  }
  


  abstract Token reset();
  

  static void reset(StringBuilder sb)
  {
    if (sb != null) {
      sb.delete(0, sb.length());
    }
  }
  
  static final class Doctype extends Token {
    final StringBuilder name = new StringBuilder();
    String pubSysKey = null;
    final StringBuilder publicIdentifier = new StringBuilder();
    final StringBuilder systemIdentifier = new StringBuilder();
    boolean forceQuirks = false;
    
    Doctype() { super();
      type = Token.TokenType.Doctype;
    }
    
    Token reset()
    {
      reset(name);
      pubSysKey = null;
      reset(publicIdentifier);
      reset(systemIdentifier);
      forceQuirks = false;
      return this;
    }
    
    String getName() {
      return name.toString();
    }
    
    String getPubSysKey() {
      return pubSysKey;
    }
    
    String getPublicIdentifier() {
      return publicIdentifier.toString();
    }
    
    public String getSystemIdentifier() {
      return systemIdentifier.toString();
    }
    

    public boolean isForceQuirks() { return forceQuirks; } }
  
  static abstract class Tag extends Token { protected String tagName;
    
    Tag() { super(); }
    
    protected String normalName;
    private String pendingAttributeName;
    private StringBuilder pendingAttributeValue = new StringBuilder();
    private String pendingAttributeValueS;
    private boolean hasEmptyAttributeValue = false;
    private boolean hasPendingAttributeValue = false;
    boolean selfClosing = false;
    Attributes attributes;
    
    Tag reset()
    {
      tagName = null;
      normalName = null;
      pendingAttributeName = null;
      reset(pendingAttributeValue);
      pendingAttributeValueS = null;
      hasEmptyAttributeValue = false;
      hasPendingAttributeValue = false;
      selfClosing = false;
      attributes = null;
      return this;
    }
    
    final void newAttribute() {
      if (attributes == null) {
        attributes = new Attributes();
      }
      if (pendingAttributeName != null)
      {
        pendingAttributeName = pendingAttributeName.trim();
        if (pendingAttributeName.length() > 0) { Attribute attribute;
          Attribute attribute;
          if (hasPendingAttributeValue)
          {
            attribute = new Attribute(pendingAttributeName, pendingAttributeValue.length() > 0 ? pendingAttributeValue.toString() : pendingAttributeValueS); } else { Attribute attribute;
            if (hasEmptyAttributeValue) {
              attribute = new Attribute(pendingAttributeName, "");
            } else
              attribute = new BooleanAttribute(pendingAttributeName); }
          attributes.put(attribute);
        }
      }
      pendingAttributeName = null;
      hasEmptyAttributeValue = false;
      hasPendingAttributeValue = false;
      reset(pendingAttributeValue);
      pendingAttributeValueS = null;
    }
    
    final void finaliseTag()
    {
      if (pendingAttributeName != null)
      {
        newAttribute();
      }
    }
    
    final String name() {
      Validate.isFalse((tagName == null) || (tagName.length() == 0));
      return tagName;
    }
    
    final String normalName() {
      return normalName;
    }
    
    final Tag name(String name) {
      tagName = name;
      normalName = Normalizer.lowerCase(name);
      return this;
    }
    
    final boolean isSelfClosing() {
      return selfClosing;
    }
    
    final Attributes getAttributes()
    {
      return attributes;
    }
    
    final void appendTagName(String append)
    {
      tagName = (tagName == null ? append : tagName.concat(append));
      normalName = Normalizer.lowerCase(tagName);
    }
    
    final void appendTagName(char append) {
      appendTagName(String.valueOf(append));
    }
    
    final void appendAttributeName(String append) {
      pendingAttributeName = (pendingAttributeName == null ? append : pendingAttributeName.concat(append));
    }
    
    final void appendAttributeName(char append) {
      appendAttributeName(String.valueOf(append));
    }
    
    final void appendAttributeValue(String append) {
      ensureAttributeValue();
      if (pendingAttributeValue.length() == 0) {
        pendingAttributeValueS = append;
      } else {
        pendingAttributeValue.append(append);
      }
    }
    
    final void appendAttributeValue(char append) {
      ensureAttributeValue();
      pendingAttributeValue.append(append);
    }
    
    final void appendAttributeValue(char[] append) {
      ensureAttributeValue();
      pendingAttributeValue.append(append);
    }
    
    final void appendAttributeValue(int[] appendCodepoints) {
      ensureAttributeValue();
      for (int codepoint : appendCodepoints) {
        pendingAttributeValue.appendCodePoint(codepoint);
      }
    }
    
    final void setEmptyAttributeValue() {
      hasEmptyAttributeValue = true;
    }
    
    private void ensureAttributeValue() {
      hasPendingAttributeValue = true;
      
      if (pendingAttributeValueS != null) {
        pendingAttributeValue.append(pendingAttributeValueS);
        pendingAttributeValueS = null;
      }
    }
  }
  
  static final class StartTag extends Token.Tag
  {
    StartTag() {
      attributes = new Attributes();
      type = Token.TokenType.StartTag;
    }
    
    Token.Tag reset()
    {
      super.reset();
      attributes = new Attributes();
      
      return this;
    }
    
    StartTag nameAttr(String name, Attributes attributes) {
      tagName = name;
      this.attributes = attributes;
      normalName = Normalizer.lowerCase(tagName);
      return this;
    }
    
    public String toString()
    {
      if ((attributes != null) && (attributes.size() > 0)) {
        return "<" + name() + " " + attributes.toString() + ">";
      }
      return "<" + name() + ">";
    }
  }
  
  static final class EndTag extends Token.Tag
  {
    EndTag() {
      type = Token.TokenType.EndTag;
    }
    
    public String toString()
    {
      return "</" + name() + ">";
    }
  }
  
  static final class Comment extends Token {
    final StringBuilder data = new StringBuilder();
    boolean bogus = false;
    
    Token reset()
    {
      reset(data);
      bogus = false;
      return this;
    }
    
    Comment() { super();
      type = Token.TokenType.Comment;
    }
    
    String getData() {
      return data.toString();
    }
    
    public String toString()
    {
      return "<!--" + getData() + "-->";
    }
  }
  
  static final class Character extends Token {
    private String data;
    
    Character() {
      super();
      type = Token.TokenType.Character;
    }
    
    Token reset()
    {
      data = null;
      return this;
    }
    
    Character data(String data) {
      this.data = data;
      return this;
    }
    
    String getData() {
      return data;
    }
    


    public String toString() { return getData(); }
  }
  
  static final class EOF extends Token {
    EOF() {
      super();
      type = Token.TokenType.EOF;
    }
    
    Token reset()
    {
      return this;
    }
  }
  
  final boolean isDoctype() {
    return type == TokenType.Doctype;
  }
  
  final Doctype asDoctype() {
    return (Doctype)this;
  }
  
  final boolean isStartTag() {
    return type == TokenType.StartTag;
  }
  
  final StartTag asStartTag() {
    return (StartTag)this;
  }
  
  final boolean isEndTag() {
    return type == TokenType.EndTag;
  }
  
  final EndTag asEndTag() {
    return (EndTag)this;
  }
  
  final boolean isComment() {
    return type == TokenType.Comment;
  }
  
  final Comment asComment() {
    return (Comment)this;
  }
  
  final boolean isCharacter() {
    return type == TokenType.Character;
  }
  
  final Character asCharacter() {
    return (Character)this;
  }
  
  final boolean isEOF() {
    return type == TokenType.EOF;
  }
  
  static enum TokenType {
    Doctype, 
    StartTag, 
    EndTag, 
    Comment, 
    Character, 
    EOF;
    
    private TokenType() {}
  }
}
