package org.jsoup.parser;

import java.util.ArrayList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class XmlTreeBuilder extends TreeBuilder
{
  public XmlTreeBuilder() {}
  
  ParseSettings defaultSettings()
  {
    return ParseSettings.preserveCase;
  }
  
  Document parse(String input, String baseUri) {
    return parse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
  }
  
  protected void initialiseParse(String input, String baseUri, ParseErrorList errors, ParseSettings settings)
  {
    super.initialiseParse(input, baseUri, errors, settings);
    stack.add(doc);
    doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
  }
  

  protected boolean process(Token token)
  {
    switch (1.$SwitchMap$org$jsoup$parser$Token$TokenType[type.ordinal()]) {
    case 1: 
      insert(token.asStartTag());
      break;
    case 2: 
      popStackToClose(token.asEndTag());
      break;
    case 3: 
      insert(token.asComment());
      break;
    case 4: 
      insert(token.asCharacter());
      break;
    case 5: 
      insert(token.asDoctype());
      break;
    case 6: 
      break;
    default: 
      Validate.fail("Unexpected token type: " + type);
    }
    return true;
  }
  
  private void insertNode(Node node) {
    currentElement().appendChild(node);
  }
  
  Element insert(Token.StartTag startTag) {
    Tag tag = Tag.valueOf(startTag.name(), settings);
    
    Element el = new Element(tag, baseUri, settings.normalizeAttributes(attributes));
    insertNode(el);
    if (startTag.isSelfClosing()) {
      tokeniser.acknowledgeSelfClosingFlag();
      if (!tag.isKnownTag())
        tag.setSelfClosing();
    } else {
      stack.add(el);
    }
    return el;
  }
  
  void insert(Token.Comment commentToken) {
    Comment comment = new Comment(commentToken.getData(), baseUri);
    Node insert = comment;
    if (bogus)
    {
      String data = comment.getData();
      if ((data.length() > 1) && ((data.startsWith("!")) || (data.startsWith("?")))) {
        Document doc = org.jsoup.Jsoup.parse("<" + data.substring(1, data.length() - 1) + ">", baseUri, Parser.xmlParser());
        Element el = doc.child(0);
        insert = new org.jsoup.nodes.XmlDeclaration(settings.normalizeTag(el.tagName()), comment.baseUri(), data.startsWith("!"));
        insert.attributes().addAll(el.attributes());
      }
    }
    insertNode(insert);
  }
  
  void insert(Token.Character characterToken) {
    Node node = new org.jsoup.nodes.TextNode(characterToken.getData(), baseUri);
    insertNode(node);
  }
  
  void insert(Token.Doctype d) {
    DocumentType doctypeNode = new DocumentType(settings.normalizeTag(d.getName()), d.getPubSysKey(), d.getPublicIdentifier(), d.getSystemIdentifier(), baseUri);
    insertNode(doctypeNode);
  }
  





  private void popStackToClose(Token.EndTag endTag)
  {
    String elName = endTag.name();
    Element firstFound = null;
    
    for (int pos = stack.size() - 1; pos >= 0; pos--) {
      Element next = (Element)stack.get(pos);
      if (next.nodeName().equals(elName)) {
        firstFound = next;
        break;
      }
    }
    if (firstFound == null) {
      return;
    }
    for (int pos = stack.size() - 1; pos >= 0; pos--) {
      Element next = (Element)stack.get(pos);
      stack.remove(pos);
      if (next == firstFound)
        break;
    }
  }
  
  java.util.List<Node> parseFragment(String inputFragment, String baseUri, ParseErrorList errors, ParseSettings settings) {
    initialiseParse(inputFragment, baseUri, errors, settings);
    runParser();
    return doc.childNodes();
  }
}
