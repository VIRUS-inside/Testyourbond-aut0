package org.yaml.snakeyaml.scanner;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.DocumentEndToken;
import org.yaml.snakeyaml.tokens.DocumentStartToken;
import org.yaml.snakeyaml.tokens.FlowEntryToken;
import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.Token.ID;
import org.yaml.snakeyaml.tokens.ValueToken;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.util.UriEncoder;










































public final class ScannerImpl
  implements Scanner
{
  private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
  









  public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap();
  














  public static final Map<Character, Integer> ESCAPE_CODES = new HashMap();
  private final StreamReader reader;
  
  static {
    ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
    

    ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
    
    ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
    
    ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
  }
  

  private boolean done = false;
  


  private int flowLevel = 0;
  

  private List<Token> tokens;
  

  private int tokensTaken = 0;
  

  private int indent = -1;
  













  private ArrayStack<Integer> indents;
  












  private boolean allowSimpleKey = true;
  



  private Map<Integer, SimpleKey> possibleSimpleKeys;
  



  public ScannerImpl(StreamReader reader)
  {
    this.reader = reader;
    tokens = new ArrayList(100);
    indents = new ArrayStack(10);
    
    possibleSimpleKeys = new LinkedHashMap();
    fetchStreamStart();
  }
  


  public boolean checkToken(Token.ID... choices)
  {
    while (needMoreTokens()) {
      fetchMoreTokens();
    }
    if (!tokens.isEmpty()) {
      if (choices.length == 0) {
        return true;
      }
      

      Token.ID first = ((Token)tokens.get(0)).getTokenId();
      for (int i = 0; i < choices.length; i++) {
        if (first == choices[i]) {
          return true;
        }
      }
    }
    return false;
  }
  


  public Token peekToken()
  {
    while (needMoreTokens()) {
      fetchMoreTokens();
    }
    return (Token)tokens.get(0);
  }
  


  public Token getToken()
  {
    if (!tokens.isEmpty()) {
      tokensTaken += 1;
      return (Token)tokens.remove(0);
    }
    return null;
  }
  




  private boolean needMoreTokens()
  {
    if (done) {
      return false;
    }
    
    if (tokens.isEmpty()) {
      return true;
    }
    

    stalePossibleSimpleKeys();
    return nextPossibleSimpleKey() == tokensTaken;
  }
  



  private void fetchMoreTokens()
  {
    scanToNextToken();
    
    stalePossibleSimpleKeys();
    

    unwindIndent(reader.getColumn());
    

    char ch = reader.peek();
    switch (ch)
    {
    case '\000': 
      fetchStreamEnd();
      return;
    
    case '%': 
      if (checkDirective()) {
        fetchDirective(); return;
      }
      

      break;
    case '-': 
      if (checkDocumentStart()) {
        fetchDocumentStart();
        return;
      }
      if (checkBlockEntry()) {
        fetchBlockEntry(); return;
      }
      

      break;
    case '.': 
      if (checkDocumentEnd()) {
        fetchDocumentEnd(); return;
      }
      


      break;
    case '[': 
      fetchFlowSequenceStart();
      return;
    
    case '{': 
      fetchFlowMappingStart();
      return;
    
    case ']': 
      fetchFlowSequenceEnd();
      return;
    
    case '}': 
      fetchFlowMappingEnd();
      return;
    
    case ',': 
      fetchFlowEntry();
      return;
    

    case '?': 
      if (checkKey()) {
        fetchKey(); return;
      }
      

      break;
    case ':': 
      if (checkValue()) {
        fetchValue(); return;
      }
      

      break;
    case '*': 
      fetchAlias();
      return;
    
    case '&': 
      fetchAnchor();
      return;
    
    case '!': 
      fetchTag();
      return;
    
    case '|': 
      if (flowLevel == 0) {
        fetchLiteral(); return;
      }
      

      break;
    case '>': 
      if (flowLevel == 0) {
        fetchFolded(); return;
      }
      

      break;
    case '\'': 
      fetchSingle();
      return;
    
    case '"': 
      fetchDouble();
      return;
    }
    
    if (checkPlain()) {
      fetchPlain();
      return;
    }
    


    String chRepresentation = String.valueOf(ch);
    for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
      String v = (String)ESCAPE_REPLACEMENTS.get(s);
      if (v.equals(chRepresentation)) {
        chRepresentation = "\\" + s;
        break;
      }
    }
    if (ch == '\t')
      chRepresentation = chRepresentation + "(TAB)";
    String text = String.format("found character %s '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { Character.valueOf(ch), chRepresentation, chRepresentation });
    

    throw new ScannerException("while scanning for the next token", null, text, reader.getMark());
  }
  










  private int nextPossibleSimpleKey()
  {
    if (!possibleSimpleKeys.isEmpty()) {
      return ((SimpleKey)possibleSimpleKeys.values().iterator().next()).getTokenNumber();
    }
    return -1;
  }
  









  private void stalePossibleSimpleKeys()
  {
    if (!possibleSimpleKeys.isEmpty()) {
      Iterator<SimpleKey> iterator = possibleSimpleKeys.values().iterator();
      while (iterator.hasNext()) {
        SimpleKey key = (SimpleKey)iterator.next();
        if ((key.getLine() != reader.getLine()) || (reader.getIndex() - key.getIndex() > 1024))
        {




          if (key.isRequired())
          {

            throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", reader.getMark());
          }
          
          iterator.remove();
        }
      }
    }
  }
  











  private void savePossibleSimpleKey()
  {
    boolean required = (flowLevel == 0) && (indent == reader.getColumn());
    
    if ((!allowSimpleKey) && (required))
    {


      throw new YAMLException("A simple key is required only if it is the first token in the current line");
    }
    



    if (allowSimpleKey) {
      removePossibleSimpleKey();
      int tokenNumber = tokensTaken + tokens.size();
      SimpleKey key = new SimpleKey(tokenNumber, required, reader.getIndex(), reader.getLine(), reader.getColumn(), reader.getMark());
      
      possibleSimpleKeys.put(Integer.valueOf(flowLevel), key);
    }
  }
  


  private void removePossibleSimpleKey()
  {
    SimpleKey key = (SimpleKey)possibleSimpleKeys.remove(Integer.valueOf(flowLevel));
    if ((key != null) && (key.isRequired())) {
      throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", reader.getMark());
    }
  }
  
























  private void unwindIndent(int col)
  {
    if (flowLevel != 0) {
      return;
    }
    

    while (indent > col) {
      Mark mark = reader.getMark();
      indent = ((Integer)indents.pop()).intValue();
      tokens.add(new BlockEndToken(mark, mark));
    }
  }
  


  private boolean addIndent(int column)
  {
    if (indent < column) {
      indents.push(Integer.valueOf(indent));
      indent = column;
      return true;
    }
    return false;
  }
  






  private void fetchStreamStart()
  {
    Mark mark = reader.getMark();
    

    Token token = new StreamStartToken(mark, mark);
    tokens.add(token);
  }
  
  private void fetchStreamEnd()
  {
    unwindIndent(-1);
    

    removePossibleSimpleKey();
    allowSimpleKey = false;
    possibleSimpleKeys.clear();
    

    Mark mark = reader.getMark();
    

    Token token = new StreamEndToken(mark, mark);
    tokens.add(token);
    

    done = true;
  }
  







  private void fetchDirective()
  {
    unwindIndent(-1);
    

    removePossibleSimpleKey();
    allowSimpleKey = false;
    

    Token tok = scanDirective();
    tokens.add(tok);
  }
  


  private void fetchDocumentStart()
  {
    fetchDocumentIndicator(true);
  }
  


  private void fetchDocumentEnd()
  {
    fetchDocumentIndicator(false);
  }
  




  private void fetchDocumentIndicator(boolean isDocumentStart)
  {
    unwindIndent(-1);
    


    removePossibleSimpleKey();
    allowSimpleKey = false;
    

    Mark startMark = reader.getMark();
    reader.forward(3);
    Mark endMark = reader.getMark();
    Token token;
    Token token; if (isDocumentStart) {
      token = new DocumentStartToken(startMark, endMark);
    } else {
      token = new DocumentEndToken(startMark, endMark);
    }
    tokens.add(token);
  }
  
  private void fetchFlowSequenceStart() {
    fetchFlowCollectionStart(false);
  }
  
  private void fetchFlowMappingStart() {
    fetchFlowCollectionStart(true);
  }
  












  private void fetchFlowCollectionStart(boolean isMappingStart)
  {
    savePossibleSimpleKey();
    

    flowLevel += 1;
    

    allowSimpleKey = true;
    

    Mark startMark = reader.getMark();
    reader.forward(1);
    Mark endMark = reader.getMark();
    Token token;
    Token token; if (isMappingStart) {
      token = new FlowMappingStartToken(startMark, endMark);
    } else {
      token = new FlowSequenceStartToken(startMark, endMark);
    }
    tokens.add(token);
  }
  
  private void fetchFlowSequenceEnd() {
    fetchFlowCollectionEnd(false);
  }
  
  private void fetchFlowMappingEnd() {
    fetchFlowCollectionEnd(true);
  }
  










  private void fetchFlowCollectionEnd(boolean isMappingEnd)
  {
    removePossibleSimpleKey();
    

    flowLevel -= 1;
    

    allowSimpleKey = false;
    

    Mark startMark = reader.getMark();
    reader.forward();
    Mark endMark = reader.getMark();
    Token token;
    Token token; if (isMappingEnd) {
      token = new FlowMappingEndToken(startMark, endMark);
    } else {
      token = new FlowSequenceEndToken(startMark, endMark);
    }
    tokens.add(token);
  }
  






  private void fetchFlowEntry()
  {
    allowSimpleKey = true;
    

    removePossibleSimpleKey();
    

    Mark startMark = reader.getMark();
    reader.forward();
    Mark endMark = reader.getMark();
    Token token = new FlowEntryToken(startMark, endMark);
    tokens.add(token);
  }
  





  private void fetchBlockEntry()
  {
    if (flowLevel == 0)
    {
      if (!allowSimpleKey) {
        throw new ScannerException(null, null, "sequence entries are not allowed here", reader.getMark());
      }
      


      if (addIndent(reader.getColumn())) {
        Mark mark = reader.getMark();
        tokens.add(new BlockSequenceStartToken(mark, mark));
      }
    }
    



    allowSimpleKey = true;
    

    removePossibleSimpleKey();
    

    Mark startMark = reader.getMark();
    reader.forward();
    Mark endMark = reader.getMark();
    Token token = new BlockEntryToken(startMark, endMark);
    tokens.add(token);
  }
  





  private void fetchKey()
  {
    if (flowLevel == 0)
    {
      if (!allowSimpleKey) {
        throw new ScannerException(null, null, "mapping keys are not allowed here", reader.getMark());
      }
      

      if (addIndent(reader.getColumn())) {
        Mark mark = reader.getMark();
        tokens.add(new BlockMappingStartToken(mark, mark));
      }
    }
    
    allowSimpleKey = (flowLevel == 0);
    

    removePossibleSimpleKey();
    

    Mark startMark = reader.getMark();
    reader.forward();
    Mark endMark = reader.getMark();
    Token token = new KeyToken(startMark, endMark);
    tokens.add(token);
  }
  





  private void fetchValue()
  {
    SimpleKey key = (SimpleKey)possibleSimpleKeys.remove(Integer.valueOf(flowLevel));
    if (key != null)
    {
      tokens.add(key.getTokenNumber() - tokensTaken, new KeyToken(key.getMark(), key.getMark()));
      



      if ((flowLevel == 0) && 
        (addIndent(key.getColumn()))) {
        tokens.add(key.getTokenNumber() - tokensTaken, new BlockMappingStartToken(key.getMark(), key.getMark()));
      }
      


      allowSimpleKey = false;

    }
    else
    {

      if (flowLevel == 0)
      {


        if (!allowSimpleKey) {
          throw new ScannerException(null, null, "mapping values are not allowed here", reader.getMark());
        }
      }
      




      if ((flowLevel == 0) && 
        (addIndent(reader.getColumn()))) {
        Mark mark = reader.getMark();
        tokens.add(new BlockMappingStartToken(mark, mark));
      }
      


      allowSimpleKey = (flowLevel == 0);
      

      removePossibleSimpleKey();
    }
    
    Mark startMark = reader.getMark();
    reader.forward();
    Mark endMark = reader.getMark();
    Token token = new ValueToken(startMark, endMark);
    tokens.add(token);
  }
  










  private void fetchAlias()
  {
    savePossibleSimpleKey();
    

    allowSimpleKey = false;
    

    Token tok = scanAnchor(false);
    tokens.add(tok);
  }
  









  private void fetchAnchor()
  {
    savePossibleSimpleKey();
    

    allowSimpleKey = false;
    

    Token tok = scanAnchor(true);
    tokens.add(tok);
  }
  





  private void fetchTag()
  {
    savePossibleSimpleKey();
    

    allowSimpleKey = false;
    

    Token tok = scanTag();
    tokens.add(tok);
  }
  






  private void fetchLiteral()
  {
    fetchBlockScalar('|');
  }
  





  private void fetchFolded()
  {
    fetchBlockScalar('>');
  }
  







  private void fetchBlockScalar(char style)
  {
    allowSimpleKey = true;
    

    removePossibleSimpleKey();
    

    Token tok = scanBlockScalar(style);
    tokens.add(tok);
  }
  


  private void fetchSingle()
  {
    fetchFlowScalar('\'');
  }
  


  private void fetchDouble()
  {
    fetchFlowScalar('"');
  }
  







  private void fetchFlowScalar(char style)
  {
    savePossibleSimpleKey();
    

    allowSimpleKey = false;
    

    Token tok = scanFlowScalar(style);
    tokens.add(tok);
  }
  



  private void fetchPlain()
  {
    savePossibleSimpleKey();
    



    allowSimpleKey = false;
    

    Token tok = scanPlain();
    tokens.add(tok);
  }
  








  private boolean checkDirective()
  {
    return reader.getColumn() == 0;
  }
  




  private boolean checkDocumentStart()
  {
    if ((reader.getColumn() == 0) && 
      ("---".equals(reader.prefix(3))) && (Constant.NULL_BL_T_LINEBR.has(reader.peek(3)))) {
      return true;
    }
    
    return false;
  }
  




  private boolean checkDocumentEnd()
  {
    if ((reader.getColumn() == 0) && 
      ("...".equals(reader.prefix(3))) && (Constant.NULL_BL_T_LINEBR.has(reader.peek(3)))) {
      return true;
    }
    
    return false;
  }
  



  private boolean checkBlockEntry()
  {
    return Constant.NULL_BL_T_LINEBR.has(reader.peek(1));
  }
  



  private boolean checkKey()
  {
    if (flowLevel != 0) {
      return true;
    }
    
    return Constant.NULL_BL_T_LINEBR.has(reader.peek(1));
  }
  




  private boolean checkValue()
  {
    if (flowLevel != 0) {
      return true;
    }
    
    return Constant.NULL_BL_T_LINEBR.has(reader.peek(1));
  }
  



















  private boolean checkPlain()
  {
    char ch = reader.peek();
    

    return (Constant.NULL_BL_T_LINEBR.hasNo(ch, "-?:,[]{}#&*!|>'\"%@`")) || ((Constant.NULL_BL_T_LINEBR.hasNo(reader.peek(1))) && ((ch == '-') || ((flowLevel == 0) && ("?:".indexOf(ch) != -1))));
  }
  


























  private void scanToNextToken()
  {
    if ((reader.getIndex() == 0) && (reader.peek() == 65279)) {
      reader.forward();
    }
    boolean found = false;
    while (!found) {
      int ff = 0;
      

      while (reader.peek(ff) == ' ') {
        ff++;
      }
      if (ff > 0) {
        reader.forward(ff);
      }
      



      if (reader.peek() == '#') {
        ff = 0;
        while (Constant.NULL_OR_LINEBR.hasNo(reader.peek(ff))) {
          ff++;
        }
        if (ff > 0) {
          reader.forward(ff);
        }
      }
      

      if (scanLineBreak().length() != 0) {
        if (flowLevel == 0)
        {

          allowSimpleKey = true;
        }
      } else {
        found = true;
      }
    }
  }
  

  private Token scanDirective()
  {
    Mark startMark = reader.getMark();
    
    reader.forward();
    String name = scanDirectiveName(startMark);
    List<?> value = null;
    Mark endMark; Mark endMark; if ("YAML".equals(name)) {
      value = scanYamlDirectiveValue(startMark);
      endMark = reader.getMark(); } else { Mark endMark;
      if ("TAG".equals(name)) {
        value = scanTagDirectiveValue(startMark);
        endMark = reader.getMark();
      } else {
        endMark = reader.getMark();
        int ff = 0;
        while (Constant.NULL_OR_LINEBR.hasNo(reader.peek(ff))) {
          ff++;
        }
        if (ff > 0)
          reader.forward(ff);
      }
    }
    scanDirectiveIgnoredLine(startMark);
    return new DirectiveToken(name, value, startMark, endMark);
  }
  






  private String scanDirectiveName(Mark startMark)
  {
    int length = 0;
    


    char ch = reader.peek(length);
    while (Constant.ALPHA.has(ch)) {
      length++;
      ch = reader.peek(length);
    }
    
    if (length == 0) {
      throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch + "(" + ch + ")", reader.getMark());
    }
    

    String value = reader.prefixForward(length);
    ch = reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
      throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch + "(" + ch + ")", reader.getMark());
    }
    

    return value;
  }
  
  private List<Integer> scanYamlDirectiveValue(Mark startMark)
  {
    while (reader.peek() == ' ') {
      reader.forward();
    }
    Integer major = scanYamlDirectiveNumber(startMark);
    if (reader.peek() != '.') {
      throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + reader.peek() + "(" + reader.peek() + ")", reader.getMark());
    }
    

    reader.forward();
    Integer minor = scanYamlDirectiveNumber(startMark);
    if (Constant.NULL_BL_LINEBR.hasNo(reader.peek())) {
      throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + reader.peek() + "(" + reader.peek() + ")", reader.getMark());
    }
    

    List<Integer> result = new ArrayList(2);
    result.add(major);
    result.add(minor);
    return result;
  }
  







  private Integer scanYamlDirectiveNumber(Mark startMark)
  {
    char ch = reader.peek();
    if (!Character.isDigit(ch)) {
      throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + ch + "(" + ch + ")", reader.getMark());
    }
    
    int length = 0;
    while (Character.isDigit(reader.peek(length))) {
      length++;
    }
    Integer value = Integer.valueOf(Integer.parseInt(reader.prefixForward(length)));
    return value;
  }
  












  private List<String> scanTagDirectiveValue(Mark startMark)
  {
    while (reader.peek() == ' ') {
      reader.forward();
    }
    String handle = scanTagDirectiveHandle(startMark);
    while (reader.peek() == ' ') {
      reader.forward();
    }
    String prefix = scanTagDirectivePrefix(startMark);
    List<String> result = new ArrayList(2);
    result.add(handle);
    result.add(prefix);
    return result;
  }
  







  private String scanTagDirectiveHandle(Mark startMark)
  {
    String value = scanTagHandle("directive", startMark);
    char ch = reader.peek();
    if (ch != ' ') {
      throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + reader.peek() + "(" + ch + ")", reader.getMark());
    }
    
    return value;
  }
  





  private String scanTagDirectivePrefix(Mark startMark)
  {
    String value = scanTagUri("directive", startMark);
    if (Constant.NULL_BL_LINEBR.hasNo(reader.peek())) {
      throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + reader.peek() + "(" + reader.peek() + ")", reader.getMark());
    }
    

    return value;
  }
  
  private String scanDirectiveIgnoredLine(Mark startMark)
  {
    int ff = 0;
    while (reader.peek(ff) == ' ') {
      ff++;
    }
    if (ff > 0) {
      reader.forward(ff);
    }
    if (reader.peek() == '#') {
      ff = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(reader.peek(ff))) {
        ff++;
      }
      reader.forward(ff);
    }
    char ch = reader.peek();
    String lineBreak = scanLineBreak();
    if ((lineBreak.length() == 0) && (ch != 0)) {
      throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + ch + "(" + ch + ")", reader.getMark());
    }
    

    return lineBreak;
  }
  











  private Token scanAnchor(boolean isAnchor)
  {
    Mark startMark = reader.getMark();
    char indicator = reader.peek();
    String name = indicator == '*' ? "alias" : "anchor";
    reader.forward();
    int length = 0;
    char ch = reader.peek(length);
    while (Constant.ALPHA.has(ch)) {
      length++;
      ch = reader.peek(length);
    }
    if (length == 0) {
      throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found but found " + ch, reader.getMark());
    }
    

    String value = reader.prefixForward(length);
    ch = reader.peek();
    if (Constant.NULL_BL_T_LINEBR.hasNo(ch, "?:,]}%@`")) {
      throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + ch + "(" + reader.peek() + ")", reader.getMark());
    }
    

    Mark endMark = reader.getMark();
    Token tok;
    Token tok; if (isAnchor) {
      tok = new AnchorToken(value, startMark, endMark);
    } else {
      tok = new AliasToken(value, startMark, endMark);
    }
    return tok;
  }
  

































  private Token scanTag()
  {
    Mark startMark = reader.getMark();
    

    char ch = reader.peek(1);
    String handle = null;
    String suffix = null;
    
    if (ch == '<')
    {

      reader.forward(2);
      suffix = scanTagUri("tag", startMark);
      if (reader.peek() != '>')
      {

        throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + reader.peek() + "' (" + reader.peek() + ")", reader.getMark());
      }
      

      reader.forward();
    } else if (Constant.NULL_BL_T_LINEBR.has(ch))
    {

      suffix = "!";
      reader.forward();

    }
    else
    {

      int length = 1;
      boolean useHandle = false;
      while (Constant.NULL_BL_LINEBR.hasNo(ch)) {
        if (ch == '!') {
          useHandle = true;
          break;
        }
        length++;
        ch = reader.peek(length);
      }
      handle = "!";
      

      if (useHandle) {
        handle = scanTagHandle("tag", startMark);
      } else {
        handle = "!";
        reader.forward();
      }
      suffix = scanTagUri("tag", startMark);
    }
    ch = reader.peek();
    

    if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
      throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + ch + "' (" + ch + ")", reader.getMark());
    }
    
    TagTuple value = new TagTuple(handle, suffix);
    Mark endMark = reader.getMark();
    return new TagToken(value, startMark, endMark);
  }
  

  private Token scanBlockScalar(char style)
  {
    boolean folded;
    boolean folded;
    if (style == '>') {
      folded = true;
    } else {
      folded = false;
    }
    StringBuilder chunks = new StringBuilder();
    Mark startMark = reader.getMark();
    
    reader.forward();
    Chomping chompi = scanBlockScalarIndicators(startMark);
    int increment = chompi.getIncrement();
    scanBlockScalarIgnoredLine(startMark);
    

    int minIndent = this.indent + 1;
    if (minIndent < 1) {
      minIndent = 1;
    }
    String breaks = null;
    int maxIndent = 0;
    int indent = 0;
    Mark endMark;
    if (increment == -1) {
      Object[] brme = scanBlockScalarIndentation();
      breaks = (String)brme[0];
      maxIndent = ((Integer)brme[1]).intValue();
      Mark endMark = (Mark)brme[2];
      indent = Math.max(minIndent, maxIndent);
    } else {
      indent = minIndent + increment - 1;
      Object[] brme = scanBlockScalarBreaks(indent);
      breaks = (String)brme[0];
      endMark = (Mark)brme[1];
    }
    
    String lineBreak = "";
    

    while ((reader.getColumn() == indent) && (reader.peek() != 0)) {
      chunks.append(breaks);
      boolean leadingNonSpace = " \t".indexOf(reader.peek()) == -1;
      int length = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(reader.peek(length))) {
        length++;
      }
      chunks.append(reader.prefixForward(length));
      lineBreak = scanLineBreak();
      Object[] brme = scanBlockScalarBreaks(indent);
      breaks = (String)brme[0];
      endMark = (Mark)brme[1];
      if ((reader.getColumn() != indent) || (reader.peek() == 0)) {
        break;
      }
      

      if ((folded) && ("\n".equals(lineBreak)) && (leadingNonSpace) && (" \t".indexOf(reader.peek()) == -1))
      {
        if (breaks.length() == 0) {
          chunks.append(" ");
        }
      } else {
        chunks.append(lineBreak);
      }
    }
    





    if (chompi.chompTailIsNotFalse()) {
      chunks.append(lineBreak);
    }
    if (chompi.chompTailIsTrue()) {
      chunks.append(breaks);
    }
    
    return new ScalarToken(chunks.toString(), false, startMark, endMark, style);
  }
  















  private Chomping scanBlockScalarIndicators(Mark startMark)
  {
    Boolean chomping = null;
    int increment = -1;
    char ch = reader.peek();
    if ((ch == '-') || (ch == '+')) {
      if (ch == '+') {
        chomping = Boolean.TRUE;
      } else {
        chomping = Boolean.FALSE;
      }
      reader.forward();
      ch = reader.peek();
      if (Character.isDigit(ch)) {
        increment = Integer.parseInt(String.valueOf(ch));
        if (increment == 0) {
          throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", reader.getMark());
        }
        

        reader.forward();
      }
    } else if (Character.isDigit(ch)) {
      increment = Integer.parseInt(String.valueOf(ch));
      if (increment == 0) {
        throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", reader.getMark());
      }
      

      reader.forward();
      ch = reader.peek();
      if ((ch == '-') || (ch == '+')) {
        if (ch == '+') {
          chomping = Boolean.TRUE;
        } else {
          chomping = Boolean.FALSE;
        }
        reader.forward();
      }
    }
    ch = reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
      throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + ch, reader.getMark());
    }
    

    return new Chomping(chomping, increment);
  }
  




  private String scanBlockScalarIgnoredLine(Mark startMark)
  {
    int ff = 0;
    
    while (reader.peek(ff) == ' ') {
      ff++;
    }
    if (ff > 0) {
      reader.forward(ff);
    }
    
    if (reader.peek() == '#') {
      ff = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(reader.peek(ff))) {
        ff++;
      }
      if (ff > 0) {
        reader.forward(ff);
      }
    }
    

    char ch = reader.peek();
    String lineBreak = scanLineBreak();
    if ((lineBreak.length() == 0) && (ch != 0)) {
      throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + ch, reader.getMark());
    }
    
    return lineBreak;
  }
  







  private Object[] scanBlockScalarIndentation()
  {
    StringBuilder chunks = new StringBuilder();
    int maxIndent = 0;
    Mark endMark = reader.getMark();
    


    while (Constant.LINEBR.has(reader.peek(), " \r")) {
      if (reader.peek() != ' ')
      {

        chunks.append(scanLineBreak());
        endMark = reader.getMark();

      }
      else
      {
        reader.forward();
        if (reader.getColumn() > maxIndent) {
          maxIndent = reader.getColumn();
        }
      }
    }
    
    return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
  }
  
  private Object[] scanBlockScalarBreaks(int indent)
  {
    StringBuilder chunks = new StringBuilder();
    Mark endMark = reader.getMark();
    int ff = 0;
    int col = reader.getColumn();
    

    while ((col < indent) && (reader.peek(ff) == ' ')) {
      ff++;
      col++;
    }
    if (ff > 0) {
      reader.forward(ff);
    }
    

    String lineBreak = null;
    while ((lineBreak = scanLineBreak()).length() != 0) {
      chunks.append(lineBreak);
      endMark = reader.getMark();
      

      ff = 0;
      col = reader.getColumn();
      while ((col < indent) && (reader.peek(ff) == ' ')) {
        ff++;
        col++;
      }
      if (ff > 0) {
        reader.forward(ff);
      }
    }
    
    return new Object[] { chunks.toString(), endMark };
  }
  






  private Token scanFlowScalar(char style)
  {
    boolean _double;
    




    boolean _double;
    




    if (style == '"') {
      _double = true;
    } else {
      _double = false;
    }
    StringBuilder chunks = new StringBuilder();
    Mark startMark = reader.getMark();
    char quote = reader.peek();
    reader.forward();
    chunks.append(scanFlowScalarNonSpaces(_double, startMark));
    while (reader.peek() != quote) {
      chunks.append(scanFlowScalarSpaces(startMark));
      chunks.append(scanFlowScalarNonSpaces(_double, startMark));
    }
    reader.forward();
    Mark endMark = reader.getMark();
    return new ScalarToken(chunks.toString(), false, startMark, endMark, style);
  }
  



  private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    
    for (;;)
    {
      int length = 0;
      while (Constant.NULL_BL_T_LINEBR.hasNo(reader.peek(length), "'\"\\")) {
        length++;
      }
      if (length != 0) {
        chunks.append(reader.prefixForward(length));
      }
      

      char ch = reader.peek();
      if ((!doubleQuoted) && (ch == '\'') && (reader.peek(1) == '\'')) {
        chunks.append("'");
        reader.forward(2);
      } else if (((doubleQuoted) && (ch == '\'')) || ((!doubleQuoted) && ("\"\\".indexOf(ch) != -1))) {
        chunks.append(ch);
        reader.forward();
      } else if ((doubleQuoted) && (ch == '\\')) {
        reader.forward();
        ch = reader.peek();
        if (ESCAPE_REPLACEMENTS.containsKey(Character.valueOf(ch)))
        {


          chunks.append((String)ESCAPE_REPLACEMENTS.get(Character.valueOf(ch)));
          reader.forward();
        } else if (ESCAPE_CODES.containsKey(Character.valueOf(ch)))
        {

          length = ((Integer)ESCAPE_CODES.get(Character.valueOf(ch))).intValue();
          reader.forward();
          String hex = reader.prefix(length);
          if (NOT_HEXA.matcher(hex).find()) {
            throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, reader.getMark());
          }
          


          int decimal = Integer.parseInt(hex, 16);
          String unicode = new String(Character.toChars(decimal));
          chunks.append(unicode);
          reader.forward(length);
        } else if (scanLineBreak().length() != 0) {
          chunks.append(scanFlowScalarBreaks(startMark));
        } else {
          throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + ch + "(" + ch + ")", reader.getMark());
        }
      }
      else
      {
        return chunks.toString();
      }
    }
  }
  
  private String scanFlowScalarSpaces(Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    int length = 0;
    

    while (" \t".indexOf(reader.peek(length)) != -1) {
      length++;
    }
    String whitespaces = reader.prefixForward(length);
    char ch = reader.peek();
    if (ch == 0)
    {
      throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", reader.getMark());
    }
    

    String lineBreak = scanLineBreak();
    if (lineBreak.length() != 0) {
      String breaks = scanFlowScalarBreaks(startMark);
      if (!"\n".equals(lineBreak)) {
        chunks.append(lineBreak);
      } else if (breaks.length() == 0) {
        chunks.append(" ");
      }
      chunks.append(breaks);
    } else {
      chunks.append(whitespaces);
    }
    return chunks.toString();
  }
  
  private String scanFlowScalarBreaks(Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    
    for (;;)
    {
      String prefix = reader.prefix(3);
      if ((("---".equals(prefix)) || ("...".equals(prefix))) && (Constant.NULL_BL_T_LINEBR.has(reader.peek(3))))
      {
        throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", reader.getMark());
      }
      

      while (" \t".indexOf(reader.peek()) != -1) {
        reader.forward();
      }
      

      String lineBreak = scanLineBreak();
      if (lineBreak.length() != 0) {
        chunks.append(lineBreak);
      } else {
        return chunks.toString();
      }
    }
  }
  










  private Token scanPlain()
  {
    StringBuilder chunks = new StringBuilder();
    Mark startMark = reader.getMark();
    Mark endMark = startMark;
    int indent = this.indent + 1;
    String spaces = "";
    for (;;)
    {
      int length = 0;
      
      if (reader.peek() == '#')
        break;
      char ch;
      for (;;) {
        ch = reader.peek(length);
        if ((Constant.NULL_BL_T_LINEBR.has(ch)) || ((flowLevel == 0) && (ch == ':') && (Constant.NULL_BL_T_LINEBR.has(reader.peek(length + 1)))) || ((flowLevel != 0) && (",:?[]{}".indexOf(ch) != -1))) {
          break;
        }
        


        length++;
      }
      
      if ((flowLevel != 0) && (ch == ':') && (Constant.NULL_BL_T_LINEBR.hasNo(reader.peek(length + 1), ",[]{}")))
      {
        reader.forward(length);
        throw new ScannerException("while scanning a plain scalar", startMark, "found unexpected ':'", reader.getMark(), "Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
      }
      

      if (length == 0) {
        break;
      }
      allowSimpleKey = false;
      chunks.append(spaces);
      chunks.append(reader.prefixForward(length));
      endMark = reader.getMark();
      spaces = scanPlainSpaces();
      
      if ((spaces.length() == 0) || (reader.peek() == '#') || ((flowLevel == 0) && (reader.getColumn() < indent))) {
        break;
      }
    }
    
    return new ScalarToken(chunks.toString(), startMark, endMark, true);
  }
  



  private String scanPlainSpaces()
  {
    int length = 0;
    while ((reader.peek(length) == ' ') || (reader.peek(length) == '\t')) {
      length++;
    }
    String whitespaces = reader.prefixForward(length);
    String lineBreak = scanLineBreak();
    if (lineBreak.length() != 0) {
      allowSimpleKey = true;
      String prefix = reader.prefix(3);
      if (("---".equals(prefix)) || (("...".equals(prefix)) && (Constant.NULL_BL_T_LINEBR.has(reader.peek(3)))))
      {
        return "";
      }
      StringBuilder breaks = new StringBuilder();
      for (;;) {
        if (reader.peek() == ' ') {
          reader.forward();
        } else {
          String lb = scanLineBreak();
          if (lb.length() == 0) break;
          breaks.append(lb);
          prefix = reader.prefix(3);
          if (("---".equals(prefix)) || (("...".equals(prefix)) && (Constant.NULL_BL_T_LINEBR.has(reader.peek(3)))))
          {
            return "";
          }
        }
      }
      


      if (!"\n".equals(lineBreak))
        return lineBreak + breaks;
      if (breaks.length() == 0) {
        return " ";
      }
      return breaks.toString();
    }
    return whitespaces;
  }
  





















  private String scanTagHandle(String name, Mark startMark)
  {
    char ch = reader.peek();
    if (ch != '!') {
      throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch + "(" + ch + ")", reader.getMark());
    }
    



    int length = 1;
    ch = reader.peek(length);
    if (ch != ' ')
    {



      while (Constant.ALPHA.has(ch)) {
        length++;
        ch = reader.peek(length);
      }
      


      if (ch != '!') {
        reader.forward(length);
        throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch + "(" + ch + ")", reader.getMark());
      }
      
      length++;
    }
    String value = reader.prefixForward(length);
    return value;
  }
  
















  private String scanTagUri(String name, Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    


    int length = 0;
    char ch = reader.peek(length);
    while (Constant.URI_CHARS.has(ch)) {
      if (ch == '%') {
        chunks.append(reader.prefixForward(length));
        length = 0;
        chunks.append(scanUriEscapes(name, startMark));
      } else {
        length++;
      }
      ch = reader.peek(length);
    }
    

    if (length != 0) {
      chunks.append(reader.prefixForward(length));
      length = 0;
    }
    if (chunks.length() == 0)
    {
      throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + ch + "(" + ch + ")", reader.getMark());
    }
    
    return chunks.toString();
  }
  












  private String scanUriEscapes(String name, Mark startMark)
  {
    int length = 1;
    while (reader.peek(length * 3) == '%') {
      length++;
    }
    



    Mark beginningMark = reader.getMark();
    ByteBuffer buff = ByteBuffer.allocate(length);
    while (reader.peek() == '%') {
      reader.forward();
      try {
        byte code = (byte)Integer.parseInt(reader.prefix(2), 16);
        buff.put(code);
      } catch (NumberFormatException nfe) {
        throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + reader.peek() + "(" + reader.peek() + ") and " + reader.peek(1) + "(" + reader.peek(1) + ")", reader.getMark());
      }
      



      reader.forward(2);
    }
    buff.flip();
    try {
      return UriEncoder.decode(buff);
    } catch (CharacterCodingException e) {
      throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e.getMessage(), beginningMark);
    }
  }
  

















  private String scanLineBreak()
  {
    char ch = reader.peek();
    if ((ch == '\r') || (ch == '\n') || (ch == '')) {
      if ((ch == '\r') && ('\n' == reader.peek(1))) {
        reader.forward(2);
      } else {
        reader.forward();
      }
      return "\n"; }
    if ((ch == ' ') || (ch == ' ')) {
      reader.forward();
      return String.valueOf(ch);
    }
    return "";
  }
  

  private static class Chomping
  {
    private final Boolean value;
    private final int increment;
    
    public Chomping(Boolean value, int increment)
    {
      this.value = value;
      this.increment = increment;
    }
    
    public boolean chompTailIsNotFalse() {
      return (value == null) || (value.booleanValue());
    }
    
    public boolean chompTailIsTrue() {
      return (value != null) && (value.booleanValue());
    }
    
    public int getIncrement() {
      return increment;
    }
  }
}
