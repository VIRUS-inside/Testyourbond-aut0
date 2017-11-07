package org.jsoup.parser;

import java.util.Arrays;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;




final class Tokeniser
{
  static final char replacementChar = '�';
  private static final char[] notCharRefCharsSorted = { '\t', '\n', '\r', '\f', ' ', '<', '&' };
  private final CharacterReader reader;
  
  static { Arrays.sort(notCharRefCharsSorted); }
  


  private final ParseErrorList errors;
  
  private TokeniserState state = TokeniserState.Data;
  private Token emitPending;
  private boolean isEmitPending = false;
  private String charsString = null;
  private StringBuilder charsBuilder = new StringBuilder(1024);
  StringBuilder dataBuffer = new StringBuilder(1024);
  
  Token.Tag tagPending;
  Token.StartTag startPending = new Token.StartTag();
  Token.EndTag endPending = new Token.EndTag();
  Token.Character charPending = new Token.Character();
  Token.Doctype doctypePending = new Token.Doctype();
  Token.Comment commentPending = new Token.Comment();
  private String lastStartTag;
  private boolean selfClosingFlagAcknowledged = true;
  
  Tokeniser(CharacterReader reader, ParseErrorList errors) {
    this.reader = reader;
    this.errors = errors;
  }
  
  Token read() {
    if (!selfClosingFlagAcknowledged) {
      error("Self closing flag not acknowledged");
      selfClosingFlagAcknowledged = true;
    }
    
    while (!isEmitPending) {
      state.read(this, reader);
    }
    
    if (charsBuilder.length() > 0) {
      String str = charsBuilder.toString();
      charsBuilder.delete(0, charsBuilder.length());
      charsString = null;
      return charPending.data(str); }
    if (charsString != null) {
      Token token = charPending.data(charsString);
      charsString = null;
      return token;
    }
    isEmitPending = false;
    return emitPending;
  }
  
  void emit(Token token)
  {
    Validate.isFalse(isEmitPending, "There is an unread token pending!");
    
    emitPending = token;
    isEmitPending = true;
    
    if (type == Token.TokenType.StartTag) {
      Token.StartTag startTag = (Token.StartTag)token;
      lastStartTag = tagName;
      if (selfClosing)
        selfClosingFlagAcknowledged = false;
    } else if (type == Token.TokenType.EndTag) {
      Token.EndTag endTag = (Token.EndTag)token;
      if (attributes != null) {
        error("Attributes incorrectly present on end tag");
      }
    }
  }
  
  void emit(String str)
  {
    if (charsString == null) {
      charsString = str;
    }
    else {
      if (charsBuilder.length() == 0) {
        charsBuilder.append(charsString);
      }
      charsBuilder.append(str);
    }
  }
  
  void emit(char[] chars) {
    emit(String.valueOf(chars));
  }
  
  void emit(int[] codepoints) {
    emit(new String(codepoints, 0, codepoints.length));
  }
  
  void emit(char c) {
    emit(String.valueOf(c));
  }
  
  TokeniserState getState() {
    return state;
  }
  
  void transition(TokeniserState state) {
    this.state = state;
  }
  
  void advanceTransition(TokeniserState state) {
    reader.advance();
    this.state = state;
  }
  
  void acknowledgeSelfClosingFlag() {
    selfClosingFlagAcknowledged = true;
  }
  
  private final int[] codepointHolder = new int[1];
  private final int[] multipointHolder = new int[2];
  
  int[] consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) { if (reader.isEmpty())
      return null;
    if ((additionalAllowedCharacter != null) && (additionalAllowedCharacter.charValue() == reader.current()))
      return null;
    if (reader.matchesAnySorted(notCharRefCharsSorted)) {
      return null;
    }
    int[] codeRef = codepointHolder;
    reader.mark();
    if (reader.matchConsume("#")) {
      boolean isHexMode = reader.matchConsumeIgnoreCase("X");
      String numRef = isHexMode ? reader.consumeHexSequence() : reader.consumeDigitSequence();
      if (numRef.length() == 0) {
        characterReferenceError("numeric reference with no numerals");
        reader.rewindToMark();
        return null;
      }
      if (!reader.matchConsume(";"))
        characterReferenceError("missing semicolon");
      int charval = -1;
      try {
        int base = isHexMode ? 16 : 10;
        charval = Integer.valueOf(numRef, base).intValue();
      }
      catch (NumberFormatException localNumberFormatException) {}
      if ((charval == -1) || ((charval >= 55296) && (charval <= 57343)) || (charval > 1114111)) {
        characterReferenceError("character outside of valid range");
        codeRef[0] = 65533;
        return codeRef;
      }
      

      codeRef[0] = charval;
      return codeRef;
    }
    

    String nameRef = reader.consumeLetterThenDigitSequence();
    boolean looksLegit = reader.matches(';');
    
    boolean found = (Entities.isBaseNamedEntity(nameRef)) || ((Entities.isNamedEntity(nameRef)) && (looksLegit));
    
    if (!found) {
      reader.rewindToMark();
      if (looksLegit)
        characterReferenceError(String.format("invalid named referenece '%s'", new Object[] { nameRef }));
      return null;
    }
    if (inAttribute) if ((!reader.matchesLetter()) && (!reader.matchesDigit())) { if (!reader.matchesAny(new char[] { '=', '-', '_' })) {}
      } else {
        reader.rewindToMark();
        return null;
      }
    if (!reader.matchConsume(";"))
      characterReferenceError("missing semicolon");
    int numChars = Entities.codepointsForName(nameRef, multipointHolder);
    if (numChars == 1) {
      codeRef[0] = multipointHolder[0];
      return codeRef; }
    if (numChars == 2) {
      return multipointHolder;
    }
    Validate.fail("Unexpected characters returned for " + nameRef);
    return multipointHolder;
  }
  

  Token.Tag createTagPending(boolean start)
  {
    tagPending = (start ? startPending.reset() : endPending.reset());
    return tagPending;
  }
  
  void emitTagPending() {
    tagPending.finaliseTag();
    emit(tagPending);
  }
  
  void createCommentPending() {
    commentPending.reset();
  }
  
  void emitCommentPending() {
    emit(commentPending);
  }
  
  void createDoctypePending() {
    doctypePending.reset();
  }
  
  void emitDoctypePending() {
    emit(doctypePending);
  }
  
  void createTempBuffer() {
    Token.reset(dataBuffer);
  }
  
  boolean isAppropriateEndTagToken() {
    return (lastStartTag != null) && (tagPending.name().equalsIgnoreCase(lastStartTag));
  }
  
  String appropriateEndTagName() {
    if (lastStartTag == null)
      return null;
    return lastStartTag;
  }
  
  void error(TokeniserState state) {
    if (errors.canAddError())
      errors.add(new ParseError(reader.pos(), "Unexpected character '%s' in input state [%s]", new Object[] { Character.valueOf(reader.current()), state }));
  }
  
  void eofError(TokeniserState state) {
    if (errors.canAddError())
      errors.add(new ParseError(reader.pos(), "Unexpectedly reached end of file (EOF) in input state [%s]", new Object[] { state }));
  }
  
  private void characterReferenceError(String message) {
    if (errors.canAddError())
      errors.add(new ParseError(reader.pos(), "Invalid character reference: %s", new Object[] { message }));
  }
  
  private void error(String errorMsg) {
    if (errors.canAddError()) {
      errors.add(new ParseError(reader.pos(), errorMsg));
    }
  }
  
  boolean currentNodeInHtmlNS() {
    return true;
  }
  






  String unescapeEntities(boolean inAttribute)
  {
    StringBuilder builder = new StringBuilder();
    while (!reader.isEmpty()) {
      builder.append(reader.consumeTo('&'));
      if (reader.matches('&')) {
        reader.consume();
        int[] c = consumeCharacterReference(null, inAttribute);
        if ((c == null) || (c.length == 0)) {
          builder.append('&');
        } else {
          builder.appendCodePoint(c[0]);
          if (c.length == 2) {
            builder.appendCodePoint(c[1]);
          }
        }
      }
    }
    return builder.toString();
  }
}
