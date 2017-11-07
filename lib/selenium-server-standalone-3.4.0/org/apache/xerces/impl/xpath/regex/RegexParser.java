package org.apache.xerces.impl.xpath.regex;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

class RegexParser
{
  static final int T_CHAR = 0;
  static final int T_EOF = 1;
  static final int T_OR = 2;
  static final int T_STAR = 3;
  static final int T_PLUS = 4;
  static final int T_QUESTION = 5;
  static final int T_LPAREN = 6;
  static final int T_RPAREN = 7;
  static final int T_DOT = 8;
  static final int T_LBRACKET = 9;
  static final int T_BACKSOLIDUS = 10;
  static final int T_CARET = 11;
  static final int T_DOLLAR = 12;
  static final int T_LPAREN2 = 13;
  static final int T_LOOKAHEAD = 14;
  static final int T_NEGATIVELOOKAHEAD = 15;
  static final int T_LOOKBEHIND = 16;
  static final int T_NEGATIVELOOKBEHIND = 17;
  static final int T_INDEPENDENT = 18;
  static final int T_SET_OPERATIONS = 19;
  static final int T_POSIX_CHARCLASS_START = 20;
  static final int T_COMMENT = 21;
  static final int T_MODIFIERS = 22;
  static final int T_CONDITION = 23;
  static final int T_XMLSCHEMA_CC_SUBTRACTION = 24;
  int offset;
  String regex;
  int regexlen;
  int options;
  ResourceBundle resources;
  int chardata;
  int nexttoken;
  protected static final int S_NORMAL = 0;
  protected static final int S_INBRACKETS = 1;
  protected static final int S_INXBRACKETS = 2;
  int context = 0;
  int parenOpened = 1;
  int parennumber = 1;
  boolean hasBackReferences;
  Vector references = null;
  
  public RegexParser()
  {
    setLocale(Locale.getDefault());
  }
  
  public RegexParser(Locale paramLocale)
  {
    setLocale(paramLocale);
  }
  
  public void setLocale(Locale paramLocale)
  {
    try
    {
      if (paramLocale != null) {
        resources = ResourceBundle.getBundle("org.apache.xerces.impl.xpath.regex.message", paramLocale);
      } else {
        resources = ResourceBundle.getBundle("org.apache.xerces.impl.xpath.regex.message");
      }
    }
    catch (MissingResourceException localMissingResourceException)
    {
      throw new RuntimeException("Installation Problem???  Couldn't load messages: " + localMissingResourceException.getMessage());
    }
  }
  
  final ParseException ex(String paramString, int paramInt)
  {
    return new ParseException(resources.getString(paramString), paramInt);
  }
  
  protected final boolean isSet(int paramInt)
  {
    return (options & paramInt) == paramInt;
  }
  
  synchronized Token parse(String paramString, int paramInt)
    throws ParseException
  {
    options = paramInt;
    offset = 0;
    setContext(0);
    parennumber = 1;
    parenOpened = 1;
    hasBackReferences = false;
    regex = paramString;
    if (isSet(16)) {
      regex = REUtil.stripExtendedComment(regex);
    }
    regexlen = regex.length();
    next();
    Token localToken = parseRegex();
    if (offset != regexlen) {
      throw ex("parser.parse.1", offset);
    }
    if (references != null)
    {
      for (int i = 0; i < references.size(); i++)
      {
        ReferencePosition localReferencePosition = (ReferencePosition)references.elementAt(i);
        if (parennumber <= refNumber) {
          throw ex("parser.parse.2", position);
        }
      }
      references.removeAllElements();
    }
    return localToken;
  }
  
  protected final void setContext(int paramInt)
  {
    context = paramInt;
  }
  
  final int read()
  {
    return nexttoken;
  }
  
  final void next()
  {
    if (offset >= regexlen)
    {
      chardata = -1;
      nexttoken = 1;
      return;
    }
    int j = regex.charAt(offset++);
    chardata = j;
    int i;
    if (context == 1)
    {
      switch (j)
      {
      case 92: 
        i = 10;
        if (offset >= regexlen) {
          throw ex("parser.next.1", offset - 1);
        }
        chardata = regex.charAt(offset++);
        break;
      case 45: 
        if ((offset < regexlen) && (regex.charAt(offset) == '['))
        {
          offset += 1;
          i = 24;
        }
        else
        {
          i = 0;
        }
        break;
      case 91: 
        if ((!isSet(512)) && (offset < regexlen) && (regex.charAt(offset) == ':'))
        {
          offset += 1;
          i = 20;
        }
        break;
      }
      if ((REUtil.isHighSurrogate(j)) && (offset < regexlen))
      {
        int k = regex.charAt(offset);
        if (REUtil.isLowSurrogate(k))
        {
          chardata = REUtil.composeFromSurrogates(j, k);
          offset += 1;
        }
      }
      i = 0;
      nexttoken = i;
      return;
    }
    switch (j)
    {
    case 124: 
      i = 2;
      break;
    case 42: 
      i = 3;
      break;
    case 43: 
      i = 4;
      break;
    case 63: 
      i = 5;
      break;
    case 41: 
      i = 7;
      break;
    case 46: 
      i = 8;
      break;
    case 91: 
      i = 9;
      break;
    case 94: 
      if (isSet(512)) {
        i = 0;
      } else {
        i = 11;
      }
      break;
    case 36: 
      if (isSet(512)) {
        i = 0;
      } else {
        i = 12;
      }
      break;
    case 40: 
      i = 6;
      if ((offset < regexlen) && (regex.charAt(offset) == '?'))
      {
        if (++offset >= regexlen) {
          throw ex("parser.next.2", offset - 1);
        }
        j = regex.charAt(offset++);
        switch (j)
        {
        case 58: 
          i = 13;
          break;
        case 61: 
          i = 14;
          break;
        case 33: 
          i = 15;
          break;
        case 91: 
          i = 19;
          break;
        case 62: 
          i = 18;
          break;
        case 60: 
          if (offset >= regexlen) {
            throw ex("parser.next.2", offset - 3);
          }
          j = regex.charAt(offset++);
          if (j == 61) {
            i = 16;
          } else if (j == 33) {
            i = 17;
          } else {
            throw ex("parser.next.3", offset - 3);
          }
          break;
        case 35: 
          while (offset < regexlen)
          {
            j = regex.charAt(offset++);
            if (j == 41) {
              break;
            }
          }
          if (j != 41) {
            throw ex("parser.next.4", offset - 1);
          }
          i = 21;
          break;
        default: 
          if ((j == 45) || ((97 <= j) && (j <= 122)) || ((65 <= j) && (j <= 90)))
          {
            offset -= 1;
            i = 22;
          }
          else if (j == 40)
          {
            i = 23;
          }
          else
          {
            throw ex("parser.next.2", offset - 2);
          }
          break;
        }
      }
      break;
    case 92: 
      i = 10;
      if (offset >= regexlen) {
        throw ex("parser.next.1", offset - 1);
      }
      chardata = regex.charAt(offset++);
      break;
    default: 
      i = 0;
    }
    nexttoken = i;
  }
  
  Token parseRegex()
    throws ParseException
  {
    Object localObject = parseTerm();
    Token.UnionToken localUnionToken = null;
    while (read() == 2)
    {
      next();
      if (localUnionToken == null)
      {
        localUnionToken = Token.createUnion();
        localUnionToken.addChild((Token)localObject);
        localObject = localUnionToken;
      }
      ((Token)localObject).addChild(parseTerm());
    }
    return localObject;
  }
  
  Token parseTerm()
    throws ParseException
  {
    int i = read();
    if ((i == 2) || (i == 7) || (i == 1)) {
      return Token.createEmpty();
    }
    Object localObject = parseFactor();
    Token.UnionToken localUnionToken = null;
    while (((i = read()) != 2) && (i != 7) && (i != 1))
    {
      if (localUnionToken == null)
      {
        localUnionToken = Token.createConcat();
        localUnionToken.addChild((Token)localObject);
        localObject = localUnionToken;
      }
      localUnionToken.addChild(parseFactor());
    }
    return localObject;
  }
  
  Token processCaret()
    throws ParseException
  {
    next();
    return Token.token_linebeginning;
  }
  
  Token processDollar()
    throws ParseException
  {
    next();
    return Token.token_lineend;
  }
  
  Token processLookahead()
    throws ParseException
  {
    next();
    Token.ParenToken localParenToken = Token.createLook(20, parseRegex());
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return localParenToken;
  }
  
  Token processNegativelookahead()
    throws ParseException
  {
    next();
    Token.ParenToken localParenToken = Token.createLook(21, parseRegex());
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return localParenToken;
  }
  
  Token processLookbehind()
    throws ParseException
  {
    next();
    Token.ParenToken localParenToken = Token.createLook(22, parseRegex());
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return localParenToken;
  }
  
  Token processNegativelookbehind()
    throws ParseException
  {
    next();
    Token.ParenToken localParenToken = Token.createLook(23, parseRegex());
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return localParenToken;
  }
  
  Token processBacksolidus_A()
    throws ParseException
  {
    next();
    return Token.token_stringbeginning;
  }
  
  Token processBacksolidus_Z()
    throws ParseException
  {
    next();
    return Token.token_stringend2;
  }
  
  Token processBacksolidus_z()
    throws ParseException
  {
    next();
    return Token.token_stringend;
  }
  
  Token processBacksolidus_b()
    throws ParseException
  {
    next();
    return Token.token_wordedge;
  }
  
  Token processBacksolidus_B()
    throws ParseException
  {
    next();
    return Token.token_not_wordedge;
  }
  
  Token processBacksolidus_lt()
    throws ParseException
  {
    next();
    return Token.token_wordbeginning;
  }
  
  Token processBacksolidus_gt()
    throws ParseException
  {
    next();
    return Token.token_wordend;
  }
  
  Token processStar(Token paramToken)
    throws ParseException
  {
    next();
    if (read() == 5)
    {
      next();
      return Token.createNGClosure(paramToken);
    }
    return Token.createClosure(paramToken);
  }
  
  Token processPlus(Token paramToken)
    throws ParseException
  {
    next();
    if (read() == 5)
    {
      next();
      return Token.createConcat(paramToken, Token.createNGClosure(paramToken));
    }
    return Token.createConcat(paramToken, Token.createClosure(paramToken));
  }
  
  Token processQuestion(Token paramToken)
    throws ParseException
  {
    next();
    Token.UnionToken localUnionToken = Token.createUnion();
    if (read() == 5)
    {
      next();
      localUnionToken.addChild(Token.createEmpty());
      localUnionToken.addChild(paramToken);
    }
    else
    {
      localUnionToken.addChild(paramToken);
      localUnionToken.addChild(Token.createEmpty());
    }
    return localUnionToken;
  }
  
  boolean checkQuestion(int paramInt)
  {
    return (paramInt < regexlen) && (regex.charAt(paramInt) == '?');
  }
  
  Token processParen()
    throws ParseException
  {
    next();
    int i = parenOpened++;
    Token.ParenToken localParenToken = Token.createParen(parseRegex(), i);
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    parennumber += 1;
    next();
    return localParenToken;
  }
  
  Token processParen2()
    throws ParseException
  {
    next();
    Token.ParenToken localParenToken = Token.createParen(parseRegex(), 0);
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return localParenToken;
  }
  
  Token processCondition()
    throws ParseException
  {
    if (offset + 1 >= regexlen) {
      throw ex("parser.factor.4", offset);
    }
    int i = -1;
    Token localToken1 = null;
    int j = regex.charAt(offset);
    if ((49 <= j) && (j <= 57))
    {
      i = j - 48;
      int k = i;
      if (parennumber <= i) {
        throw ex("parser.parse.2", offset);
      }
      while (offset + 1 < regexlen)
      {
        j = regex.charAt(offset + 1);
        if ((48 > j) || (j > 57)) {
          break;
        }
        i = i * 10 + (j - 48);
        if (i >= parennumber) {
          break;
        }
        k = i;
        offset += 1;
      }
      hasBackReferences = true;
      if (references == null) {
        references = new Vector();
      }
      references.addElement(new ReferencePosition(k, offset));
      offset += 1;
      if (regex.charAt(offset) != ')') {
        throw ex("parser.factor.1", offset);
      }
      offset += 1;
    }
    else
    {
      if (j == 63) {
        offset -= 1;
      }
      next();
      localToken1 = parseFactor();
      switch (type)
      {
      case 20: 
      case 21: 
      case 22: 
      case 23: 
        break;
      case 8: 
        if (read() != 7) {
          throw ex("parser.factor.1", offset - 1);
        }
        break;
      default: 
        throw ex("parser.factor.5", offset);
      }
    }
    next();
    Token localToken2 = parseRegex();
    Token localToken3 = null;
    if (type == 2)
    {
      if (localToken2.size() != 2) {
        throw ex("parser.factor.6", offset);
      }
      localToken3 = localToken2.getChild(1);
      localToken2 = localToken2.getChild(0);
    }
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return Token.createCondition(i, localToken1, localToken2, localToken3);
  }
  
  Token processModifiers()
    throws ParseException
  {
    int i = 0;
    int j = 0;
    int k = -1;
    int m;
    while (offset < regexlen)
    {
      k = regex.charAt(offset);
      m = REUtil.getOptionValue(k);
      if (m == 0) {
        break;
      }
      i |= m;
      offset += 1;
    }
    if (offset >= regexlen) {
      throw ex("parser.factor.2", offset - 1);
    }
    if (k == 45)
    {
      for (offset += 1; offset < regexlen; offset += 1)
      {
        k = regex.charAt(offset);
        m = REUtil.getOptionValue(k);
        if (m == 0) {
          break;
        }
        j |= m;
      }
      if (offset >= regexlen) {
        throw ex("parser.factor.2", offset - 1);
      }
    }
    Token.ModifierToken localModifierToken;
    if (k == 58)
    {
      offset += 1;
      next();
      localModifierToken = Token.createModifierGroup(parseRegex(), i, j);
      if (read() != 7) {
        throw ex("parser.factor.1", offset - 1);
      }
      next();
    }
    else if (k == 41)
    {
      offset += 1;
      next();
      localModifierToken = Token.createModifierGroup(parseRegex(), i, j);
    }
    else
    {
      throw ex("parser.factor.3", offset);
    }
    return localModifierToken;
  }
  
  Token processIndependent()
    throws ParseException
  {
    next();
    Token.ParenToken localParenToken = Token.createLook(24, parseRegex());
    if (read() != 7) {
      throw ex("parser.factor.1", offset - 1);
    }
    next();
    return localParenToken;
  }
  
  Token processBacksolidus_c()
    throws ParseException
  {
    int i;
    if ((offset >= regexlen) || (((i = regex.charAt(offset++)) & 0xFFE0) != '@')) {
      throw ex("parser.atom.1", offset - 1);
    }
    next();
    return Token.createChar(i - 64);
  }
  
  Token processBacksolidus_C()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_i()
    throws ParseException
  {
    Token.CharToken localCharToken = Token.createChar(105);
    next();
    return localCharToken;
  }
  
  Token processBacksolidus_I()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_g()
    throws ParseException
  {
    next();
    return Token.getGraphemePattern();
  }
  
  Token processBacksolidus_X()
    throws ParseException
  {
    next();
    return Token.getCombiningCharacterSequence();
  }
  
  Token processBackreference()
    throws ParseException
  {
    int i = chardata - 48;
    int j = i;
    if (parennumber <= i) {
      throw ex("parser.parse.2", offset - 2);
    }
    while (offset < regexlen)
    {
      int k = regex.charAt(offset);
      if ((48 > k) || (k > 57)) {
        break;
      }
      i = i * 10 + (k - 48);
      if (i >= parennumber) {
        break;
      }
      offset += 1;
      j = i;
      chardata = k;
    }
    Token.StringToken localStringToken = Token.createBackReference(j);
    hasBackReferences = true;
    if (references == null) {
      references = new Vector();
    }
    references.addElement(new ReferencePosition(j, offset - 2));
    next();
    return localStringToken;
  }
  
  Token parseFactor()
    throws ParseException
  {
    int i = read();
    switch (i)
    {
    case 11: 
      return processCaret();
    case 12: 
      return processDollar();
    case 14: 
      return processLookahead();
    case 15: 
      return processNegativelookahead();
    case 16: 
      return processLookbehind();
    case 17: 
      return processNegativelookbehind();
    case 21: 
      next();
      return Token.createEmpty();
    case 10: 
      switch (chardata)
      {
      case 65: 
        return processBacksolidus_A();
      case 90: 
        return processBacksolidus_Z();
      case 122: 
        return processBacksolidus_z();
      case 98: 
        return processBacksolidus_b();
      case 66: 
        return processBacksolidus_B();
      case 60: 
        return processBacksolidus_lt();
      case 62: 
        return processBacksolidus_gt();
      }
      break;
    }
    Object localObject = parseAtom();
    i = read();
    switch (i)
    {
    case 3: 
      return processStar((Token)localObject);
    case 4: 
      return processPlus((Token)localObject);
    case 5: 
      return processQuestion((Token)localObject);
    case 0: 
      if ((chardata == 123) && (offset < regexlen))
      {
        int j = offset;
        int k = 0;
        int m = -1;
        if (((i = regex.charAt(j++)) >= '0') && (i <= 57))
        {
          k = i - 48;
          do
          {
            k = k * 10 + i - 48;
            if (k < 0) {
              throw ex("parser.quantifier.5", offset);
            }
            if ((j >= regexlen) || ((i = regex.charAt(j++)) < '0')) {
              break;
            }
          } while (i <= 57);
        }
        else
        {
          throw ex("parser.quantifier.1", offset);
        }
        m = k;
        if (i == 44)
        {
          if (j >= regexlen) {
            throw ex("parser.quantifier.3", offset);
          }
          if (((i = regex.charAt(j++)) >= '0') && (i <= 57))
          {
            m = i - 48;
            while ((j < regexlen) && ((i = regex.charAt(j++)) >= '0') && (i <= 57))
            {
              m = m * 10 + i - 48;
              if (m < 0) {
                throw ex("parser.quantifier.5", offset);
              }
            }
            if (k > m) {
              throw ex("parser.quantifier.4", offset);
            }
          }
          else
          {
            m = -1;
          }
        }
        if (i != 125) {
          throw ex("parser.quantifier.2", offset);
        }
        if (checkQuestion(j))
        {
          localObject = Token.createNGClosure((Token)localObject);
          offset = (j + 1);
        }
        else
        {
          localObject = Token.createClosure((Token)localObject);
          offset = j;
        }
        ((Token)localObject).setMin(k);
        ((Token)localObject).setMax(m);
        next();
      }
      break;
    }
    return localObject;
  }
  
  Token parseAtom()
    throws ParseException
  {
    int i = read();
    Object localObject = null;
    switch (i)
    {
    case 6: 
      return processParen();
    case 13: 
      return processParen2();
    case 23: 
      return processCondition();
    case 22: 
      return processModifiers();
    case 18: 
      return processIndependent();
    case 8: 
      next();
      localObject = Token.token_dot;
      break;
    case 9: 
      return parseCharacterClass(true);
    case 19: 
      return parseSetOperations();
    case 10: 
      int j;
      switch (chardata)
      {
      case 68: 
      case 83: 
      case 87: 
      case 100: 
      case 115: 
      case 119: 
        localObject = getTokenForShorthand(chardata);
        next();
        return localObject;
      case 101: 
      case 102: 
      case 110: 
      case 114: 
      case 116: 
      case 117: 
      case 118: 
      case 120: 
        j = decodeEscaped();
        if (j < 65536) {
          localObject = Token.createChar(j);
        } else {
          localObject = Token.createString(REUtil.decomposeToSurrogates(j));
        }
        break;
      case 99: 
        return processBacksolidus_c();
      case 67: 
        return processBacksolidus_C();
      case 105: 
        return processBacksolidus_i();
      case 73: 
        return processBacksolidus_I();
      case 103: 
        return processBacksolidus_g();
      case 88: 
        return processBacksolidus_X();
      case 49: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 54: 
      case 55: 
      case 56: 
      case 57: 
        return processBackreference();
      case 80: 
      case 112: 
        j = offset;
        localObject = processBacksolidus_pP(chardata);
        if (localObject == null) {
          throw ex("parser.atom.5", j);
        }
        break;
      case 58: 
      case 59: 
      case 60: 
      case 61: 
      case 62: 
      case 63: 
      case 64: 
      case 65: 
      case 66: 
      case 69: 
      case 70: 
      case 71: 
      case 72: 
      case 74: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 81: 
      case 82: 
      case 84: 
      case 85: 
      case 86: 
      case 89: 
      case 90: 
      case 91: 
      case 92: 
      case 93: 
      case 94: 
      case 95: 
      case 96: 
      case 97: 
      case 98: 
      case 104: 
      case 106: 
      case 107: 
      case 108: 
      case 109: 
      case 111: 
      case 113: 
      default: 
        localObject = Token.createChar(chardata);
      }
      next();
      break;
    case 0: 
      if ((chardata == 93) || (chardata == 123) || (chardata == 125)) {
        throw ex("parser.atom.4", offset - 1);
      }
      localObject = Token.createChar(chardata);
      int k = chardata;
      next();
      if ((REUtil.isHighSurrogate(k)) && (read() == 0) && (REUtil.isLowSurrogate(chardata)))
      {
        char[] arrayOfChar = new char[2];
        arrayOfChar[0] = ((char)k);
        arrayOfChar[1] = ((char)chardata);
        localObject = Token.createParen(Token.createString(new String(arrayOfChar)), 0);
        next();
      }
      break;
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 7: 
    case 11: 
    case 12: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 20: 
    case 21: 
    default: 
      throw ex("parser.atom.4", offset - 1);
    }
    return localObject;
  }
  
  protected RangeToken processBacksolidus_pP(int paramInt)
    throws ParseException
  {
    next();
    if ((read() != 0) || (chardata != 123)) {
      throw ex("parser.atom.2", offset - 1);
    }
    boolean bool = paramInt == 112;
    int i = offset;
    int j = regex.indexOf('}', i);
    if (j < 0) {
      throw ex("parser.atom.3", offset);
    }
    String str = regex.substring(i, j);
    offset = (j + 1);
    return Token.getRange(str, bool, isSet(512));
  }
  
  int processCIinCharacterClass(RangeToken paramRangeToken, int paramInt)
  {
    return decodeEscaped();
  }
  
  protected RangeToken parseCharacterClass(boolean paramBoolean)
    throws ParseException
  {
    setContext(1);
    next();
    int i = 0;
    RangeToken localRangeToken1 = null;
    RangeToken localRangeToken2;
    if ((read() == 0) && (chardata == 94))
    {
      i = 1;
      next();
      if (paramBoolean)
      {
        localRangeToken2 = Token.createNRange();
      }
      else
      {
        localRangeToken1 = Token.createRange();
        localRangeToken1.addRange(0, 1114111);
        localRangeToken2 = Token.createRange();
      }
    }
    else
    {
      localRangeToken2 = Token.createRange();
    }
    int j;
    for (int k = 1; (j = read()) != 1; k = 0)
    {
      if ((j == 0) && (chardata == 93) && (k == 0)) {
        break;
      }
      int m = chardata;
      int n = 0;
      int i1;
      if (j == 10)
      {
        switch (m)
        {
        case 68: 
        case 83: 
        case 87: 
        case 100: 
        case 115: 
        case 119: 
          localRangeToken2.mergeRanges(getTokenForShorthand(m));
          n = 1;
          break;
        case 67: 
        case 73: 
        case 99: 
        case 105: 
          m = processCIinCharacterClass(localRangeToken2, m);
          if (m >= 0) {
            break;
          }
          n = 1;
          break;
        case 80: 
        case 112: 
          i1 = offset;
          RangeToken localRangeToken4 = processBacksolidus_pP(m);
          if (localRangeToken4 == null) {
            throw ex("parser.atom.5", i1);
          }
          localRangeToken2.mergeRanges(localRangeToken4);
          n = 1;
          break;
        default: 
          m = decodeEscaped();
          break;
        }
      }
      else if (j == 20)
      {
        i1 = regex.indexOf(':', offset);
        if (i1 < 0) {
          throw ex("parser.cc.1", offset);
        }
        boolean bool = true;
        if (regex.charAt(offset) == '^')
        {
          offset += 1;
          bool = false;
        }
        String str = regex.substring(offset, i1);
        RangeToken localRangeToken5 = Token.getRange(str, bool, isSet(512));
        if (localRangeToken5 == null) {
          throw ex("parser.cc.3", offset);
        }
        localRangeToken2.mergeRanges(localRangeToken5);
        n = 1;
        if ((i1 + 1 >= regexlen) || (regex.charAt(i1 + 1) != ']')) {
          throw ex("parser.cc.1", i1);
        }
        offset = (i1 + 2);
      }
      else if ((j == 24) && (k == 0))
      {
        if (i != 0)
        {
          i = 0;
          if (paramBoolean)
          {
            localRangeToken2 = (RangeToken)Token.complementRanges(localRangeToken2);
          }
          else
          {
            localRangeToken1.subtractRanges(localRangeToken2);
            localRangeToken2 = localRangeToken1;
          }
        }
        RangeToken localRangeToken3 = parseCharacterClass(false);
        localRangeToken2.subtractRanges(localRangeToken3);
        if ((read() == 0) && (chardata == 93)) {
          break;
        }
        throw ex("parser.cc.5", offset);
      }
      next();
      if (n == 0) {
        if ((read() != 0) || (chardata != 45))
        {
          if ((!isSet(2)) || (m > 65535)) {
            localRangeToken2.addRange(m, m);
          } else {
            addCaseInsensitiveChar(localRangeToken2, m);
          }
        }
        else
        {
          if (j == 24) {
            throw ex("parser.cc.8", offset - 1);
          }
          next();
          if ((j = read()) == 1) {
            throw ex("parser.cc.2", offset);
          }
          if ((j == 0) && (chardata == 93))
          {
            if ((!isSet(2)) || (m > 65535)) {
              localRangeToken2.addRange(m, m);
            } else {
              addCaseInsensitiveChar(localRangeToken2, m);
            }
            localRangeToken2.addRange(45, 45);
          }
          else
          {
            int i2 = chardata;
            if (j == 10) {
              i2 = decodeEscaped();
            }
            next();
            if (m > i2) {
              throw ex("parser.ope.3", offset - 1);
            }
            if ((!isSet(2)) || ((m > 65535) && (i2 > 65535))) {
              localRangeToken2.addRange(m, i2);
            } else {
              addCaseInsensitiveCharRange(localRangeToken2, m, i2);
            }
          }
        }
      }
      if ((isSet(1024)) && (read() == 0) && (chardata == 44)) {
        next();
      }
    }
    if (read() == 1) {
      throw ex("parser.cc.2", offset);
    }
    if ((!paramBoolean) && (i != 0))
    {
      localRangeToken1.subtractRanges(localRangeToken2);
      localRangeToken2 = localRangeToken1;
    }
    localRangeToken2.sortRanges();
    localRangeToken2.compactRanges();
    setContext(0);
    next();
    return localRangeToken2;
  }
  
  protected RangeToken parseSetOperations()
    throws ParseException
  {
    RangeToken localRangeToken1 = parseCharacterClass(false);
    int i;
    while ((i = read()) != 7)
    {
      int j = chardata;
      if (((i == 0) && ((j == 45) || (j == 38))) || (i == 4))
      {
        next();
        if (read() != 9) {
          throw ex("parser.ope.1", offset - 1);
        }
        RangeToken localRangeToken2 = parseCharacterClass(false);
        if (i == 4) {
          localRangeToken1.mergeRanges(localRangeToken2);
        } else if (j == 45) {
          localRangeToken1.subtractRanges(localRangeToken2);
        } else if (j == 38) {
          localRangeToken1.intersectRanges(localRangeToken2);
        } else {
          throw new RuntimeException("ASSERT");
        }
      }
      else
      {
        throw ex("parser.ope.2", offset - 1);
      }
    }
    next();
    return localRangeToken1;
  }
  
  Token getTokenForShorthand(int paramInt)
  {
    Token localToken;
    switch (paramInt)
    {
    case 100: 
      localToken = isSet(32) ? Token.getRange("Nd", true) : Token.token_0to9;
      break;
    case 68: 
      localToken = isSet(32) ? Token.getRange("Nd", false) : Token.token_not_0to9;
      break;
    case 119: 
      localToken = isSet(32) ? Token.getRange("IsWord", true) : Token.token_wordchars;
      break;
    case 87: 
      localToken = isSet(32) ? Token.getRange("IsWord", false) : Token.token_not_wordchars;
      break;
    case 115: 
      localToken = isSet(32) ? Token.getRange("IsSpace", true) : Token.token_spaces;
      break;
    case 83: 
      localToken = isSet(32) ? Token.getRange("IsSpace", false) : Token.token_not_spaces;
      break;
    default: 
      throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(paramInt, 16));
    }
    return localToken;
  }
  
  int decodeEscaped()
    throws ParseException
  {
    if (read() != 10) {
      throw ex("parser.next.1", offset - 1);
    }
    int i = chardata;
    int j;
    int k;
    switch (i)
    {
    case 101: 
      i = 27;
      break;
    case 102: 
      i = 12;
      break;
    case 110: 
      i = 10;
      break;
    case 114: 
      i = 13;
      break;
    case 116: 
      i = 9;
      break;
    case 120: 
      next();
      if (read() != 0) {
        throw ex("parser.descape.1", offset - 1);
      }
      if (chardata == 123)
      {
        j = 0;
        for (k = 0;; k = k * 16 + j)
        {
          next();
          if (read() != 0) {
            throw ex("parser.descape.1", offset - 1);
          }
          if ((j = hexChar(chardata)) < 0) {
            break;
          }
          if (k > k * 16) {
            throw ex("parser.descape.2", offset - 1);
          }
        }
        if (chardata != 125) {
          throw ex("parser.descape.3", offset - 1);
        }
        if (k > 1114111) {
          throw ex("parser.descape.4", offset - 1);
        }
        i = k;
      }
      else
      {
        j = 0;
        if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
          throw ex("parser.descape.1", offset - 1);
        }
        k = j;
        next();
        if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
          throw ex("parser.descape.1", offset - 1);
        }
        k = k * 16 + j;
        i = k;
      }
      break;
    case 117: 
      j = 0;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      i = k;
      break;
    case 118: 
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      next();
      if ((read() != 0) || ((j = hexChar(chardata)) < 0)) {
        throw ex("parser.descape.1", offset - 1);
      }
      k = k * 16 + j;
      if (k > 1114111) {
        throw ex("parser.descappe.4", offset - 1);
      }
      i = k;
      break;
    case 65: 
    case 90: 
    case 122: 
      throw ex("parser.descape.5", offset - 2);
    }
    return i;
  }
  
  private static final int hexChar(int paramInt)
  {
    if (paramInt < 48) {
      return -1;
    }
    if (paramInt > 102) {
      return -1;
    }
    if (paramInt <= 57) {
      return paramInt - 48;
    }
    if (paramInt < 65) {
      return -1;
    }
    if (paramInt <= 70) {
      return paramInt - 65 + 10;
    }
    if (paramInt < 97) {
      return -1;
    }
    return paramInt - 97 + 10;
  }
  
  protected static final void addCaseInsensitiveChar(RangeToken paramRangeToken, int paramInt)
  {
    int[] arrayOfInt = CaseInsensitiveMap.get(paramInt);
    paramRangeToken.addRange(paramInt, paramInt);
    if (arrayOfInt != null) {
      for (int i = 0; i < arrayOfInt.length; i += 2) {
        paramRangeToken.addRange(arrayOfInt[i], arrayOfInt[i]);
      }
    }
  }
  
  protected static final void addCaseInsensitiveCharRange(RangeToken paramRangeToken, int paramInt1, int paramInt2)
  {
    int i;
    int j;
    if (paramInt1 <= paramInt2)
    {
      i = paramInt1;
      j = paramInt2;
    }
    else
    {
      i = paramInt2;
      j = paramInt1;
    }
    paramRangeToken.addRange(i, j);
    for (int k = i; k <= j; k++)
    {
      int[] arrayOfInt = CaseInsensitiveMap.get(k);
      if (arrayOfInt != null) {
        for (int m = 0; m < arrayOfInt.length; m += 2) {
          paramRangeToken.addRange(arrayOfInt[m], arrayOfInt[m]);
        }
      }
    }
  }
  
  static class ReferencePosition
  {
    int refNumber;
    int position;
    
    ReferencePosition(int paramInt1, int paramInt2)
    {
      refNumber = paramInt1;
      position = paramInt2;
    }
  }
}
