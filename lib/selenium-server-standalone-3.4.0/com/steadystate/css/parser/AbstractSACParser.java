package com.steadystate.css.parser;

import com.steadystate.css.parser.selectors.ConditionFactoryImpl;
import com.steadystate.css.parser.selectors.SelectorFactoryImpl;
import com.steadystate.css.sac.DocumentHandlerExt;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.DOMException;






















abstract class AbstractSACParser
  implements SACParser
{
  private DocumentHandler documentHandler_;
  private ErrorHandler errorHandler_;
  private InputSource source_;
  private Locale locale_;
  private SelectorFactory selectorFactory_;
  private ConditionFactory conditionFactory_;
  private ResourceBundle sacParserMessages_;
  private boolean ieStarHackAccepted_;
  private static final String NUM_CHARS = "0123456789.";
  
  AbstractSACParser() {}
  
  protected DocumentHandler getDocumentHandler()
  {
    if (documentHandler_ == null) {
      setDocumentHandler(new HandlerBase());
    }
    return documentHandler_;
  }
  
  public void setDocumentHandler(DocumentHandler handler) {
    documentHandler_ = handler;
  }
  
  protected ErrorHandler getErrorHandler() {
    if (errorHandler_ == null) {
      setErrorHandler(new HandlerBase());
    }
    return errorHandler_;
  }
  
  public void setErrorHandler(ErrorHandler eh) {
    errorHandler_ = eh;
  }
  
  protected InputSource getInputSource() {
    return source_;
  }
  
  public void setIeStarHackAccepted(boolean accepted) {
    ieStarHackAccepted_ = accepted;
  }
  
  public boolean isIeStarHackAccepted() {
    return ieStarHackAccepted_;
  }
  
  public void setLocale(Locale locale) {
    if (locale_ != locale) {
      sacParserMessages_ = null;
    }
    locale_ = locale;
  }
  
  protected Locale getLocale() {
    if (locale_ == null) {
      setLocale(Locale.getDefault());
    }
    return locale_;
  }
  
  protected SelectorFactory getSelectorFactory() {
    if (selectorFactory_ == null) {
      selectorFactory_ = new SelectorFactoryImpl();
    }
    return selectorFactory_;
  }
  
  public void setSelectorFactory(SelectorFactory selectorFactory) {
    selectorFactory_ = selectorFactory;
  }
  
  protected ConditionFactory getConditionFactory() {
    if (conditionFactory_ == null) {
      conditionFactory_ = new ConditionFactoryImpl();
    }
    return conditionFactory_;
  }
  
  public void setConditionFactory(ConditionFactory conditionFactory) {
    conditionFactory_ = conditionFactory;
  }
  
  protected ResourceBundle getSACParserMessages() {
    if (sacParserMessages_ == null) {
      try {
        sacParserMessages_ = ResourceBundle.getBundle("com.steadystate.css.parser.SACParserMessages", 
        
          getLocale());
      }
      catch (MissingResourceException e) {
        e.printStackTrace();
      }
    }
    return sacParserMessages_;
  }
  
  protected Locator createLocator(Token t) {
    return new LocatorImpl(getInputSource().getURI(), t == null ? 0 : beginLine, t == null ? 0 : beginColumn);
  }
  

  protected String add_escapes(String str)
  {
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      switch (ch) {
      case '\000': 
        break;
      case '\b': 
        sb.append("\\b");
        break;
      case '\t': 
        sb.append("\\t");
        break;
      case '\n': 
        sb.append("\\n");
        break;
      case '\f': 
        sb.append("\\f");
        break;
      case '\r': 
        sb.append("\\r");
        break;
      case '"': 
        sb.append("\\\"");
        break;
      case '\'': 
        sb.append("\\'");
        break;
      case '\\': 
        sb.append("\\\\");
        break;
      default: 
        if ((ch < ' ') || (ch > '~')) {
          String s = "0000" + Integer.toString(ch, 16);
          sb.append("\\u" + s.substring(s.length() - 4, s.length()));
        }
        else {
          sb.append(ch);
        }
        break;
      }
    }
    return sb.toString();
  }
  
  protected CSSParseException toCSSParseException(String key, ParseException e) {
    String messagePattern1 = getSACParserMessages().getString("invalidExpectingOne");
    String messagePattern2 = getSACParserMessages().getString("invalidExpectingMore");
    int maxSize = 0;
    StringBuilder expected = new StringBuilder();
    for (int i = 0; i < expectedTokenSequences.length; i++) {
      if (maxSize < expectedTokenSequences[i].length) {
        maxSize = expectedTokenSequences[i].length;
      }
      for (int j = 0; j < expectedTokenSequences[i].length; j++) {
        expected.append(tokenImage[expectedTokenSequences[i][j]]);
      }
      if (i < expectedTokenSequences.length - 1) {
        expected.append(", ");
      }
    }
    StringBuilder invalid = new StringBuilder();
    Token tok = currentToken.next;
    for (int i = 0; i < maxSize; i++) {
      if (i != 0) {
        invalid.append(" ");
      }
      if (kind == 0) {
        invalid.append(tokenImage[0]);
        break;
      }
      invalid.append(add_escapes(image));
      tok = next;
    }
    String s = null;
    try {
      s = getSACParserMessages().getString(key);
    }
    catch (MissingResourceException ex) {
      s = key;
    }
    StringBuilder message = new StringBuilder(s);
    message.append(" (");
    if (expectedTokenSequences.length == 1) {
      message.append(MessageFormat.format(messagePattern1, new Object[] { invalid, expected }));
    }
    else {
      message.append(MessageFormat.format(messagePattern2, new Object[] { invalid, expected }));
    }
    message.append(")");
    
    return new CSSParseException(message.toString(), getInputSource().getURI(), currentToken.next.beginLine, currentToken.next.beginColumn);
  }
  
  protected CSSParseException toCSSParseException(DOMException e)
  {
    String messagePattern = getSACParserMessages().getString("domException");
    
    return new CSSParseException(MessageFormat.format(messagePattern, new Object[] {e.getMessage() }), getInputSource().getURI(), 1, 1);
  }
  
  protected CSSParseException toCSSParseException(TokenMgrError e) {
    String messagePattern = getSACParserMessages().getString("tokenMgrError");
    return new CSSParseException(messagePattern, getInputSource().getURI(), 1, 1);
  }
  
  protected CSSParseException toCSSParseException(String messageKey, Object[] msgParams, Locator locator)
  {
    String messagePattern = getSACParserMessages().getString(messageKey);
    return new CSSParseException(MessageFormat.format(messagePattern, msgParams), locator);
  }
  
  protected CSSParseException createSkipWarning(String key, CSSParseException e) {
    String s = null;
    try {
      s = getSACParserMessages().getString(key);
    }
    catch (MissingResourceException ex) {
      s = key;
    }
    return new CSSParseException(s, e.getURI(), e.getLineNumber(), e.getColumnNumber());
  }
  
  public void parseStyleSheet(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    try {
      styleSheet();
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidStyleSheet", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
  }
  
  public void parseStyleSheet(String uri) throws IOException {
    parseStyleSheet(new InputSource(uri));
  }
  
  public void parseStyleDeclaration(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    try {
      styleDeclaration();
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidStyleDeclaration", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
  }
  
  public void parseRule(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    try {
      styleSheetRuleSingle();
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidRule", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
  }
  
  public SelectorList parseSelectors(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    SelectorList sl = null;
    try {
      sl = parseSelectorsInternal();
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidSelectorList", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
    return sl;
  }
  
  public LexicalUnit parsePropertyValue(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    LexicalUnit lu = null;
    try {
      lu = expr();
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidExpr", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
    return lu;
  }
  
  public boolean parsePriority(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    boolean b = false;
    try {
      b = prio();
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidPrio", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
    return b;
  }
  
  public SACMediaList parseMedia(InputSource source) throws IOException {
    source_ = source;
    ReInit(getCharStream(source));
    SACMediaListImpl ml = new SACMediaListImpl();
    try {
      mediaList(ml);
    }
    catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidMediaList", e));
    }
    catch (TokenMgrError e) {
      getErrorHandler().error(toCSSParseException(e));
    }
    catch (CSSParseException e) {
      getErrorHandler().error(e);
    }
    return ml;
  }
  
  private CharStream getCharStream(InputSource source) throws IOException {
    if (source.getCharacterStream() != null) {
      return new CssCharStream(source.getCharacterStream(), 1, 1);
    }
    if (source.getByteStream() != null)
    {
      String encoding = source.getEncoding();
      InputStreamReader reader; InputStreamReader reader; if ((encoding == null) || (encoding.length() < 1)) {
        reader = new InputStreamReader(source.getByteStream(), Charset.defaultCharset());
      }
      else {
        reader = new InputStreamReader(source.getByteStream(), encoding);
      }
      return new CssCharStream(reader, 1, 1);
    }
    if (source.getURI() != null) {
      InputStreamReader reader = new InputStreamReader(new URL(source.getURI()).openStream());
      return new CssCharStream(reader, 1, 1);
    }
    return null; }
  
  public abstract String getParserVersion();
  
  protected abstract String getGrammarUri();
  
  protected abstract void ReInit(CharStream paramCharStream);
  
  protected abstract void styleSheet() throws CSSParseException, ParseException;
  protected abstract void styleDeclaration() throws ParseException;
  protected abstract void styleSheetRuleSingle() throws ParseException;
  protected abstract SelectorList parseSelectorsInternal() throws ParseException;
  protected abstract SelectorList selectorList() throws ParseException;
  protected abstract LexicalUnit expr() throws ParseException;
  protected abstract boolean prio() throws ParseException;
  protected abstract void mediaList(SACMediaListImpl paramSACMediaListImpl) throws ParseException;
  protected void handleStartDocument() { getDocumentHandler().startDocument(getInputSource()); }
  
  protected void handleEndDocument()
  {
    getDocumentHandler().endDocument(getInputSource());
  }
  
  protected void handleIgnorableAtRule(String s, Locator locator) {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).ignorableAtRule(s, locator);
    }
    else {
      documentHandler.ignorableAtRule(s);
    }
  }
  
  protected void handleCharset(String characterEncoding, Locator locator) {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).charset(characterEncoding, locator);
    }
  }
  
  protected void handleImportStyle(String uri, SACMediaList media, String defaultNamespaceURI, Locator locator)
  {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).importStyle(uri, media, defaultNamespaceURI, locator);
    }
    else {
      documentHandler.importStyle(uri, media, defaultNamespaceURI);
    }
  }
  
  protected void handleStartMedia(SACMediaList media, Locator locator) {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).startMedia(media, locator);
    }
    else {
      documentHandler.startMedia(media);
    }
  }
  

  protected void handleMedium(String medium, Locator locator) {}
  
  protected void handleEndMedia(SACMediaList media)
  {
    getDocumentHandler().endMedia(media);
  }
  
  protected void handleStartPage(String name, String pseudoPage, Locator locator) {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).startPage(name, pseudoPage, locator);
    }
    else {
      documentHandler.startPage(name, pseudoPage);
    }
  }
  
  protected void handleEndPage(String name, String pseudoPage) {
    getDocumentHandler().endPage(name, pseudoPage);
  }
  
  protected void handleStartFontFace(Locator locator) {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).startFontFace(locator);
    }
    else {
      documentHandler.startFontFace();
    }
  }
  
  protected void handleEndFontFace() {
    getDocumentHandler().endFontFace();
  }
  

  protected void handleSelector(Selector selector) {}
  
  protected void handleStartSelector(SelectorList selectors, Locator locator)
  {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).startSelector(selectors, locator);
    }
    else {
      documentHandler.startSelector(selectors);
    }
  }
  
  protected void handleEndSelector(SelectorList selectors) {
    getDocumentHandler().endSelector(selectors);
  }
  
  protected void handleProperty(String name, LexicalUnit value, boolean important, Locator locator)
  {
    DocumentHandler documentHandler = getDocumentHandler();
    if ((documentHandler instanceof DocumentHandlerExt)) {
      ((DocumentHandlerExt)documentHandler).property(name, value, important, locator);
    }
    else {
      documentHandler.property(name, value, important);
    }
  }
  

  protected LexicalUnit functionInternal(LexicalUnit prev, String funct, LexicalUnit params)
  {
    if ("counter(".equalsIgnoreCase(funct)) {
      return LexicalUnitImpl.createCounter(prev, params);
    }
    if ("counters(".equalsIgnoreCase(funct)) {
      return LexicalUnitImpl.createCounters(prev, params);
    }
    if ("attr(".equalsIgnoreCase(funct)) {
      return LexicalUnitImpl.createAttr(prev, params.getStringValue());
    }
    if ("rect(".equalsIgnoreCase(funct)) {
      return LexicalUnitImpl.createRect(prev, params);
    }
    if ("rgb(".equalsIgnoreCase(funct)) {
      return LexicalUnitImpl.createRgbColor(prev, params);
    }
    return LexicalUnitImpl.createFunction(prev, funct
    
      .substring(0, funct.length() - 1), params);
  }
  

  protected LexicalUnit hexcolorInternal(LexicalUnit prev, Token t)
  {
    int i = 1;
    int r = 0;
    int g = 0;
    int b = 0;
    int len = image.length() - 1;
    try {
      if (len == 3) {
        r = Integer.parseInt(image.substring(1, 2), 16);
        g = Integer.parseInt(image.substring(2, 3), 16);
        b = Integer.parseInt(image.substring(3, 4), 16);
        r = r << 4 | r;
        g = g << 4 | g;
        b = b << 4 | b;
      }
      else if (len == 6) {
        r = Integer.parseInt(image.substring(1, 3), 16);
        g = Integer.parseInt(image.substring(3, 5), 16);
        b = Integer.parseInt(image.substring(5, 7), 16);
      }
      else {
        String pattern = getSACParserMessages().getString("invalidColor");
        

        throw new CSSParseException(MessageFormat.format(pattern, new Object[] { t }), getInputSource().getURI(), beginLine, beginColumn);
      }
      


      LexicalUnit lr = LexicalUnitImpl.createNumber(null, r);
      LexicalUnit lc1 = LexicalUnitImpl.createComma(lr);
      LexicalUnit lg = LexicalUnitImpl.createNumber(lc1, g);
      LexicalUnit lc2 = LexicalUnitImpl.createComma(lg);
      LexicalUnitImpl.createNumber(lc2, b);
      
      return LexicalUnitImpl.createRgbColor(prev, lr);
    }
    catch (NumberFormatException ex) {
      String pattern = getSACParserMessages().getString("invalidColor");
      

      throw new CSSParseException(MessageFormat.format(pattern, new Object[] { t }), getInputSource().getURI(), beginLine, beginColumn, ex);
    }
  }
  
  int intValue(char op, String s)
  {
    int result = Integer.parseInt(s);
    if (op == '-') {
      return -1 * result;
    }
    return result;
  }
  
  float floatValue(char op, String s) {
    float result = Float.parseFloat(s);
    if (op == '-') {
      return -1.0F * result;
    }
    return result;
  }
  
  int getLastNumPos(String s) {
    for (int i = 0; 
        i < s.length(); i++) {
      if ("0123456789.".indexOf(s.charAt(i)) < 0) {
        break;
      }
    }
    return i - 1;
  }
  







  public String unescape(String s, boolean unescapeDoubleQuotes)
  {
    if (s == null) {
      return s;
    }
    

    StringBuilder buf = null;
    int index = -1;
    int len = s.length();
    len--;
    if (unescapeDoubleQuotes) {
      while (index < len) {
        char c = s.charAt(++index);
        
        if ((c == '\\') || (c == '"')) {
          buf = new StringBuilder(len);
          buf.append(s.substring(0, index));
          index--;
          break;
        }
      }
    }
    
    while (index < len) {
      if ('\\' == s.charAt(++index)) {
        buf = new StringBuilder(len);
        buf.append(s.substring(0, index));
        index--;
      }
    }
    


    if (null == buf) {
      return s;
    }
    

    int numValue = -1;
    
    int digitCount = 0;
    
    while (index < len) {
      char c = s.charAt(++index);
      
      if (numValue > -1) {
        int hexval = hexval(c);
        if (hexval != -1) {
          numValue = numValue * 16 + hexval;
          digitCount++; if (digitCount < 6) {
            continue;
          }
          
          if ((numValue > 65535) || (numValue == 0)) {
            numValue = 65533;
          }
          buf.append((char)numValue);
          numValue = -1;
          continue;
        }
        
        if (digitCount > 0) {
          if ((numValue > 65535) || (numValue == 0)) {
            numValue = 65533;
          }
          
          buf.append((char)numValue);
          
          if ((c == ' ') || (c == '\t')) {
            numValue = -1;
            continue;
          }
        }
        
        numValue = -1;
        if ((digitCount == 0) && (c == '\\')) {
          buf.append('\\');
          continue;
        }
        
        if ((c == '\n') || (c == '\f')) {
          continue;
        }
        if (c == '\r') {
          if ((index >= len) || 
            (s.charAt(index + 1) != '\n')) continue;
          index++; continue;
        }
      }
      



      if (c == '\\') {
        numValue = 0;
        digitCount = 0;
      }
      else
      {
        if ((c == '"') && (!unescapeDoubleQuotes)) {
          buf.append('\\');
        }
        
        buf.append(c);
      }
    }
    if (numValue > -1) {
      if (digitCount == 0) {
        buf.append('\\');
      }
      else {
        if ((numValue > 65535) || (numValue == 0)) {
          numValue = 65533;
        }
        buf.append((char)numValue);
      }
    }
    
    return buf.toString();
  }
  
  private static int hexval(char c) {
    switch (c) {
    case '0': 
      return 0;
    case '1': 
      return 1;
    case '2': 
      return 2;
    case '3': 
      return 3;
    case '4': 
      return 4;
    case '5': 
      return 5;
    case '6': 
      return 6;
    case '7': 
      return 7;
    case '8': 
      return 8;
    case '9': 
      return 9;
    
    case 'A': 
    case 'a': 
      return 10;
    case 'B': 
    case 'b': 
      return 11;
    case 'C': 
    case 'c': 
      return 12;
    case 'D': 
    case 'd': 
      return 13;
    case 'E': 
    case 'e': 
      return 14;
    case 'F': 
    case 'f': 
      return 15;
    }
    return -1;
  }
}
