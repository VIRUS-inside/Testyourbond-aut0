package org.jsoup.parser;

import java.util.Arrays;





 enum TokeniserState
{
  Data, 
  






















  CharacterReferenceInData, 
  




  Rcdata, 
  























  CharacterReferenceInRcdata, 
  



  Rawtext, 
  



  ScriptData, 
  



  PLAINTEXT, 
  
















  TagOpen, 
  
























  EndTagOpen, 
  
















  TagName, 
  































  RcdataLessthanSign, 
  

















  RCDATAEndTagOpen, 
  











  RCDATAEndTagName, 
  












































  RawtextLessthanSign, 
  









  RawtextEndTagOpen, 
  



  RawtextEndTagName, 
  



  ScriptDataLessthanSign, 
  
















  ScriptDataEndTagOpen, 
  



  ScriptDataEndTagName, 
  



  ScriptDataEscapeStart, 
  








  ScriptDataEscapeStartDash, 
  








  ScriptDataEscaped, 
  

























  ScriptDataEscapedDash, 
  


























  ScriptDataEscapedDashDash, 
  





























  ScriptDataEscapedLessthanSign, 
  














  ScriptDataEscapedEndTagOpen, 
  











  ScriptDataEscapedEndTagName, 
  



  ScriptDataDoubleEscapeStart, 
  



  ScriptDataDoubleEscaped, 
  

























  ScriptDataDoubleEscapedDash, 
  

























  ScriptDataDoubleEscapedDashDash, 
  




























  ScriptDataDoubleEscapedLessthanSign, 
  









  ScriptDataDoubleEscapeEnd, 
  



  BeforeAttributeName, 
  










































  AttributeName, 
  








































  AfterAttributeName, 
  











































  BeforeAttributeValue, 
  















































  AttributeValue_doubleQuoted, 
  






























  AttributeValue_singleQuoted, 
  






























  AttributeValue_unquoted, 
  














































  AfterAttributeValue_quoted, 
  




























  SelfClosingStartTag, 
  


















  BogusComment, 
  











  MarkupDeclarationOpen, 
  
















  CommentStart, 
  


























  CommentStartDash, 
  


























  Comment, 
  




















  CommentEndDash, 
  





















  CommentEnd, 
  































  CommentEndBang, 
  


























  Doctype, 
  

























  BeforeDoctypeName, 
  

































  DoctypeName, 
  

































  AfterDoctypeName, 
  


























  AfterDoctypePublicKeyword, 
  






































  BeforeDoctypePublicIdentifier, 
  



































  DoctypePublicIdentifier_doubleQuoted, 
  


























  DoctypePublicIdentifier_singleQuoted, 
  


























  AfterDoctypePublicIdentifier, 
  




































  BetweenDoctypePublicAndSystemIdentifiers, 
  



































  AfterDoctypeSystemKeyword, 
  






































  BeforeDoctypeSystemIdentifier, 
  



































  DoctypeSystemIdentifier_doubleQuoted, 
  


























  DoctypeSystemIdentifier_singleQuoted, 
  


























  AfterDoctypeSystemIdentifier, 
  

























  BogusDoctype, 
  
















  CdataSection;
  
  static final char nullChar = '\000';
  private static final char[] attributeSingleValueCharsSorted;
  private static final char[] attributeDoubleValueCharsSorted;
  private static final char[] attributeNameCharsSorted;
  private static final char[] attributeValueUnquoted;
  private static final char replacementChar = '�';
  private static final String replacementStr;
  private static final char eof = '￿';
  
  static
  {
    attributeSingleValueCharsSorted = new char[] { '\'', '&', '\000' };
    attributeDoubleValueCharsSorted = new char[] { '"', '&', '\000' };
    attributeNameCharsSorted = new char[] { '\t', '\n', '\r', '\f', ' ', '/', '=', '>', '\000', '"', '\'', '<' };
    attributeValueUnquoted = new char[] { '\t', '\n', '\r', '\f', ' ', '&', '>', '\000', '"', '\'', '<', '=', '`' };
    

    replacementStr = String.valueOf(65533);
    


    Arrays.sort(attributeSingleValueCharsSorted);
    Arrays.sort(attributeDoubleValueCharsSorted);
    Arrays.sort(attributeNameCharsSorted);
    Arrays.sort(attributeValueUnquoted);
  }
  



  private static void handleDataEndTag(Tokeniser t, CharacterReader r, TokeniserState elseTransition)
  {
    if (r.matchesLetter()) {
      String name = r.consumeLetterSequence();
      tagPending.appendTagName(name);
      dataBuffer.append(name);
      return;
    }
    
    boolean needsExitTransition = false;
    if ((t.isAppropriateEndTagToken()) && (!r.isEmpty())) {
      char c = r.consume();
      switch (c) {
      case '\t': 
      case '\n': 
      case '\f': 
      case '\r': 
      case ' ': 
        t.transition(BeforeAttributeName);
        break;
      case '/': 
        t.transition(SelfClosingStartTag);
        break;
      case '>': 
        t.emitTagPending();
        t.transition(Data);
        break;
      default: 
        dataBuffer.append(c);
        needsExitTransition = true;
      }
    } else {
      needsExitTransition = true;
    }
    
    if (needsExitTransition) {
      t.emit("</" + dataBuffer.toString());
      t.transition(elseTransition);
    }
  }
  
  private static void readData(Tokeniser t, CharacterReader r, TokeniserState current, TokeniserState advance) {
    switch (r.current()) {
    case '<': 
      t.advanceTransition(advance);
      break;
    case '\000': 
      t.error(current);
      r.advance();
      t.emit(65533);
      break;
    case '￿': 
      t.emit(new Token.EOF());
      break;
    default: 
      String data = r.consumeToAny(new char[] { '<', '\000' });
      t.emit(data);
    }
  }
  
  private static void readCharRef(Tokeniser t, TokeniserState advance)
  {
    int[] c = t.consumeCharacterReference(null, false);
    if (c == null) {
      t.emit('&');
    } else
      t.emit(c);
    t.transition(advance);
  }
  
  private static void readEndTag(Tokeniser t, CharacterReader r, TokeniserState a, TokeniserState b) {
    if (r.matchesLetter()) {
      t.createTagPending(false);
      t.transition(a);
    } else {
      t.emit("</");
      t.transition(b);
    }
  }
  
  private static void handleDataDoubleEscapeTag(Tokeniser t, CharacterReader r, TokeniserState primary, TokeniserState fallback) {
    if (r.matchesLetter()) {
      String name = r.consumeLetterSequence();
      dataBuffer.append(name);
      t.emit(name);
      return;
    }
    
    char c = r.consume();
    switch (c) {
    case '\t': 
    case '\n': 
    case '\f': 
    case '\r': 
    case ' ': 
    case '/': 
    case '>': 
      if (dataBuffer.toString().equals("script")) {
        t.transition(primary);
      } else
        t.transition(fallback);
      t.emit(c);
      break;
    default: 
      r.unconsume();
      t.transition(fallback);
    }
  }
  
  private TokeniserState() {}
  
  abstract void read(Tokeniser paramTokeniser, CharacterReader paramCharacterReader);
}
