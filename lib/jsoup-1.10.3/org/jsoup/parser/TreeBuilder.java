package org.jsoup.parser;

import java.util.ArrayList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


abstract class TreeBuilder
{
  CharacterReader reader;
  Tokeniser tokeniser;
  protected Document doc;
  protected ArrayList<Element> stack;
  protected String baseUri;
  protected Token currentToken;
  protected ParseErrorList errors;
  protected ParseSettings settings;
  
  TreeBuilder() {}
  
  private Token.StartTag start = new Token.StartTag();
  private Token.EndTag end = new Token.EndTag();
  
  abstract ParseSettings defaultSettings();
  
  protected void initialiseParse(String input, String baseUri, ParseErrorList errors, ParseSettings settings) {
    Validate.notNull(input, "String input must not be null");
    Validate.notNull(baseUri, "BaseURI must not be null");
    
    doc = new Document(baseUri);
    this.settings = settings;
    reader = new CharacterReader(input);
    this.errors = errors;
    tokeniser = new Tokeniser(reader, errors);
    stack = new ArrayList(32);
    this.baseUri = baseUri;
  }
  
  Document parse(String input, String baseUri, ParseErrorList errors, ParseSettings settings) {
    initialiseParse(input, baseUri, errors, settings);
    runParser();
    return doc;
  }
  
  protected void runParser() {
    for (;;) {
      Token token = tokeniser.read();
      process(token);
      token.reset();
      
      if (type == Token.TokenType.EOF)
        break;
    }
  }
  
  protected abstract boolean process(Token paramToken);
  
  protected boolean processStartTag(String name) {
    if (currentToken == start) {
      return process(new Token.StartTag().name(name));
    }
    return process(start.reset().name(name));
  }
  
  public boolean processStartTag(String name, Attributes attrs) {
    if (currentToken == start) {
      return process(new Token.StartTag().nameAttr(name, attrs));
    }
    start.reset();
    start.nameAttr(name, attrs);
    return process(start);
  }
  
  protected boolean processEndTag(String name) {
    if (currentToken == end) {
      return process(new Token.EndTag().name(name));
    }
    return process(end.reset().name(name));
  }
  
  protected Element currentElement()
  {
    int size = stack.size();
    return size > 0 ? (Element)stack.get(size - 1) : null;
  }
}
