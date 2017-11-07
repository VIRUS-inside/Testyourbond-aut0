package org.jsoup.nodes;

import java.io.IOException;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;













public class TextNode
  extends Node
{
  private static final String TEXT_KEY = "text";
  String text;
  
  public TextNode(String text, String baseUri)
  {
    this.baseUri = baseUri;
    this.text = text;
  }
  
  public String nodeName() {
    return "#text";
  }
  




  public String text()
  {
    return normaliseWhitespace(getWholeText());
  }
  




  public TextNode text(String text)
  {
    this.text = text;
    if (attributes != null)
      attributes.put("text", text);
    return this;
  }
  



  public String getWholeText()
  {
    return attributes == null ? text : attributes.get("text");
  }
  



  public boolean isBlank()
  {
    return StringUtil.isBlank(getWholeText());
  }
  





  public TextNode splitText(int offset)
  {
    Validate.isTrue(offset >= 0, "Split offset must be not be negative");
    Validate.isTrue(offset < text.length(), "Split offset must not be greater than current text length");
    
    String head = getWholeText().substring(0, offset);
    String tail = getWholeText().substring(offset);
    text(head);
    TextNode tailNode = new TextNode(tail, baseUri());
    if (parent() != null) {
      parent().addChildren(siblingIndex() + 1, new Node[] { tailNode });
    }
    return tailNode;
  }
  
  void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
    if ((out.prettyPrint()) && (((siblingIndex() == 0) && ((parentNode instanceof Element)) && (((Element)parentNode).tag().formatAsBlock()) && (!isBlank())) || ((out.outline()) && (siblingNodes().size() > 0) && (!isBlank())))) {
      indent(accum, depth, out);
    }
    
    boolean normaliseWhite = (out.prettyPrint()) && ((parent() instanceof Element)) && (!Element.preserveWhitespace(parent()));
    Entities.escape(accum, getWholeText(), out, false, normaliseWhite, false);
  }
  
  void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
  
  public String toString()
  {
    return outerHtml();
  }
  





  public static TextNode createFromEncoded(String encodedText, String baseUri)
  {
    String text = Entities.unescape(encodedText);
    return new TextNode(text, baseUri);
  }
  
  static String normaliseWhitespace(String text) {
    text = StringUtil.normaliseWhitespace(text);
    return text;
  }
  
  static String stripLeadingWhitespace(String text) {
    return text.replaceFirst("^\\s+", "");
  }
  
  static boolean lastCharIsWhitespace(StringBuilder sb) {
    return (sb.length() != 0) && (sb.charAt(sb.length() - 1) == ' ');
  }
  
  private void ensureAttributes()
  {
    if (attributes == null) {
      attributes = new Attributes();
      attributes.put("text", text);
    }
  }
  
  public String attr(String attributeKey)
  {
    ensureAttributes();
    return super.attr(attributeKey);
  }
  
  public Attributes attributes()
  {
    ensureAttributes();
    return super.attributes();
  }
  
  public Node attr(String attributeKey, String attributeValue)
  {
    ensureAttributes();
    return super.attr(attributeKey, attributeValue);
  }
  
  public boolean hasAttr(String attributeKey)
  {
    ensureAttributes();
    return super.hasAttr(attributeKey);
  }
  
  public Node removeAttr(String attributeKey)
  {
    ensureAttributes();
    return super.removeAttr(attributeKey);
  }
  
  public String absUrl(String attributeKey)
  {
    ensureAttributes();
    return super.absUrl(attributeKey);
  }
}
