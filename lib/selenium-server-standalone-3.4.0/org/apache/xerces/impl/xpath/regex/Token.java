package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

class Token
  implements Serializable
{
  private static final long serialVersionUID = 8484976002585487481L;
  static final boolean COUNTTOKENS = true;
  static int tokens = 0;
  static final int CHAR = 0;
  static final int DOT = 11;
  static final int CONCAT = 1;
  static final int UNION = 2;
  static final int CLOSURE = 3;
  static final int RANGE = 4;
  static final int NRANGE = 5;
  static final int PAREN = 6;
  static final int EMPTY = 7;
  static final int ANCHOR = 8;
  static final int NONGREEDYCLOSURE = 9;
  static final int STRING = 10;
  static final int BACKREFERENCE = 12;
  static final int LOOKAHEAD = 20;
  static final int NEGATIVELOOKAHEAD = 21;
  static final int LOOKBEHIND = 22;
  static final int NEGATIVELOOKBEHIND = 23;
  static final int INDEPENDENT = 24;
  static final int MODIFIERGROUP = 25;
  static final int CONDITION = 26;
  static final int UTF16_MAX = 1114111;
  final int type;
  static Token token_dot;
  static Token token_0to9;
  static Token token_wordchars;
  static Token token_not_0to9 = complementRanges(token_0to9);
  static Token token_not_wordchars = complementRanges(token_wordchars);
  static Token token_spaces;
  static Token token_not_spaces = complementRanges(token_spaces);
  static Token token_empty = new Token(7);
  static Token token_linebeginning = createAnchor(94);
  static Token token_linebeginning2 = createAnchor(64);
  static Token token_lineend = createAnchor(36);
  static Token token_stringbeginning = createAnchor(65);
  static Token token_stringend = createAnchor(122);
  static Token token_stringend2 = createAnchor(90);
  static Token token_wordedge = createAnchor(98);
  static Token token_not_wordedge = createAnchor(66);
  static Token token_wordbeginning = createAnchor(60);
  static Token token_wordend = createAnchor(62);
  static final int FC_CONTINUE = 0;
  static final int FC_TERMINAL = 1;
  static final int FC_ANY = 2;
  private static final Hashtable categories = new Hashtable();
  private static final Hashtable categories2 = new Hashtable();
  private static final String[] categoryNames = { "Cn", "Lu", "Ll", "Lt", "Lm", "Lo", "Mn", "Me", "Mc", "Nd", "Nl", "No", "Zs", "Zl", "Zp", "Cc", "Cf", null, "Co", "Cs", "Pd", "Ps", "Pe", "Pc", "Po", "Sm", "Sc", "Sk", "So", "Pi", "Pf", "L", "M", "N", "Z", "C", "P", "S" };
  static final int CHAR_INIT_QUOTE = 29;
  static final int CHAR_FINAL_QUOTE = 30;
  static final int CHAR_LETTER = 31;
  static final int CHAR_MARK = 32;
  static final int CHAR_NUMBER = 33;
  static final int CHAR_SEPARATOR = 34;
  static final int CHAR_OTHER = 35;
  static final int CHAR_PUNCTUATION = 36;
  static final int CHAR_SYMBOL = 37;
  private static final String[] blockNames = { "Basic Latin", "Latin-1 Supplement", "Latin Extended-A", "Latin Extended-B", "IPA Extensions", "Spacing Modifier Letters", "Combining Diacritical Marks", "Greek", "Cyrillic", "Armenian", "Hebrew", "Arabic", "Syriac", "Thaana", "Devanagari", "Bengali", "Gurmukhi", "Gujarati", "Oriya", "Tamil", "Telugu", "Kannada", "Malayalam", "Sinhala", "Thai", "Lao", "Tibetan", "Myanmar", "Georgian", "Hangul Jamo", "Ethiopic", "Cherokee", "Unified Canadian Aboriginal Syllabics", "Ogham", "Runic", "Khmer", "Mongolian", "Latin Extended Additional", "Greek Extended", "General Punctuation", "Superscripts and Subscripts", "Currency Symbols", "Combining Marks for Symbols", "Letterlike Symbols", "Number Forms", "Arrows", "Mathematical Operators", "Miscellaneous Technical", "Control Pictures", "Optical Character Recognition", "Enclosed Alphanumerics", "Box Drawing", "Block Elements", "Geometric Shapes", "Miscellaneous Symbols", "Dingbats", "Braille Patterns", "CJK Radicals Supplement", "Kangxi Radicals", "Ideographic Description Characters", "CJK Symbols and Punctuation", "Hiragana", "Katakana", "Bopomofo", "Hangul Compatibility Jamo", "Kanbun", "Bopomofo Extended", "Enclosed CJK Letters and Months", "CJK Compatibility", "CJK Unified Ideographs Extension A", "CJK Unified Ideographs", "Yi Syllables", "Yi Radicals", "Hangul Syllables", "Private Use", "CJK Compatibility Ideographs", "Alphabetic Presentation Forms", "Arabic Presentation Forms-A", "Combining Half Marks", "CJK Compatibility Forms", "Small Form Variants", "Arabic Presentation Forms-B", "Specials", "Halfwidth and Fullwidth Forms", "Old Italic", "Gothic", "Deseret", "Byzantine Musical Symbols", "Musical Symbols", "Mathematical Alphanumeric Symbols", "CJK Unified Ideographs Extension B", "CJK Compatibility Ideographs Supplement", "Tags" };
  static final String blockRanges = "\000ÿĀſƀɏɐʯʰ˿̀ͯͰϿЀӿ԰֏֐׿؀ۿ܀ݏހ޿ऀॿঀ৿਀੿઀૿଀୿஀௿ఀ౿ಀ೿ഀൿ඀෿฀๿຀໿ༀ࿿က႟Ⴀჿᄀᇿሀ፿Ꭰ᏿᐀ᙿ ᚟ᚠ᛿ក៿᠀᢯Ḁỿἀ῿ ⁯⁰₟₠⃏⃐⃿℀⅏⅐↏←⇿∀⋿⌀⏿␀␿⑀⑟①⓿─╿▀▟■◿☀⛿✀➿⠀⣿⺀⻿⼀⿟⿰⿿　〿぀ゟ゠ヿ㄀ㄯ㄰㆏㆐㆟ㆠㆿ㈀㋿㌀㏿㐀䶵一鿿ꀀ꒏꒐꓏가힣豈﫿ﬀﭏﭐ﷿︠︯︰﹏﹐﹯ﹰ﻾﻿﻿＀￯";
  static final int[] nonBMPBlockRanges = { 66304, 66351, 66352, 66383, 66560, 66639, 118784, 119039, 119040, 119295, 119808, 120831, 131072, 173782, 194560, 195103, 917504, 917631 };
  private static final int NONBMP_BLOCK_START = 84;
  static Hashtable nonxs = null;
  static final String viramaString = "्্੍્୍்్್്ฺ྄";
  private static Token token_grapheme = null;
  private static Token token_ccs = null;
  
  static ParenToken createLook(int paramInt, Token paramToken)
  {
    tokens += 1;
    return new ParenToken(paramInt, paramToken, 0);
  }
  
  static ParenToken createParen(Token paramToken, int paramInt)
  {
    tokens += 1;
    return new ParenToken(6, paramToken, paramInt);
  }
  
  static ClosureToken createClosure(Token paramToken)
  {
    tokens += 1;
    return new ClosureToken(3, paramToken);
  }
  
  static ClosureToken createNGClosure(Token paramToken)
  {
    tokens += 1;
    return new ClosureToken(9, paramToken);
  }
  
  static ConcatToken createConcat(Token paramToken1, Token paramToken2)
  {
    tokens += 1;
    return new ConcatToken(paramToken1, paramToken2);
  }
  
  static UnionToken createConcat()
  {
    tokens += 1;
    return new UnionToken(1);
  }
  
  static UnionToken createUnion()
  {
    tokens += 1;
    return new UnionToken(2);
  }
  
  static Token createEmpty()
  {
    return token_empty;
  }
  
  static RangeToken createRange()
  {
    tokens += 1;
    return new RangeToken(4);
  }
  
  static RangeToken createNRange()
  {
    tokens += 1;
    return new RangeToken(5);
  }
  
  static CharToken createChar(int paramInt)
  {
    tokens += 1;
    return new CharToken(0, paramInt);
  }
  
  private static CharToken createAnchor(int paramInt)
  {
    tokens += 1;
    return new CharToken(8, paramInt);
  }
  
  static StringToken createBackReference(int paramInt)
  {
    tokens += 1;
    return new StringToken(12, null, paramInt);
  }
  
  static StringToken createString(String paramString)
  {
    tokens += 1;
    return new StringToken(10, paramString, 0);
  }
  
  static ModifierToken createModifierGroup(Token paramToken, int paramInt1, int paramInt2)
  {
    tokens += 1;
    return new ModifierToken(paramToken, paramInt1, paramInt2);
  }
  
  static ConditionToken createCondition(int paramInt, Token paramToken1, Token paramToken2, Token paramToken3)
  {
    tokens += 1;
    return new ConditionToken(paramInt, paramToken1, paramToken2, paramToken3);
  }
  
  protected Token(int paramInt)
  {
    type = paramInt;
  }
  
  int size()
  {
    return 0;
  }
  
  Token getChild(int paramInt)
  {
    return null;
  }
  
  void addChild(Token paramToken)
  {
    throw new RuntimeException("Not supported.");
  }
  
  protected void addRange(int paramInt1, int paramInt2)
  {
    throw new RuntimeException("Not supported.");
  }
  
  protected void sortRanges()
  {
    throw new RuntimeException("Not supported.");
  }
  
  protected void compactRanges()
  {
    throw new RuntimeException("Not supported.");
  }
  
  protected void mergeRanges(Token paramToken)
  {
    throw new RuntimeException("Not supported.");
  }
  
  protected void subtractRanges(Token paramToken)
  {
    throw new RuntimeException("Not supported.");
  }
  
  protected void intersectRanges(Token paramToken)
  {
    throw new RuntimeException("Not supported.");
  }
  
  static Token complementRanges(Token paramToken)
  {
    return RangeToken.complementRanges(paramToken);
  }
  
  void setMin(int paramInt) {}
  
  void setMax(int paramInt) {}
  
  int getMin()
  {
    return -1;
  }
  
  int getMax()
  {
    return -1;
  }
  
  int getReferenceNumber()
  {
    return 0;
  }
  
  String getString()
  {
    return null;
  }
  
  int getParenNumber()
  {
    return 0;
  }
  
  int getChar()
  {
    return -1;
  }
  
  public String toString()
  {
    return toString(0);
  }
  
  public String toString(int paramInt)
  {
    return type == 11 ? "." : "";
  }
  
  final int getMinLength()
  {
    switch (type)
    {
    case 1: 
      int i = 0;
      for (int j = 0; j < size(); j++) {
        i += getChild(j).getMinLength();
      }
      return i;
    case 2: 
    case 26: 
      if (size() == 0) {
        return 0;
      }
      int k = getChild(0).getMinLength();
      for (int m = 1; m < size(); m++)
      {
        int n = getChild(m).getMinLength();
        if (n < k) {
          k = n;
        }
      }
      return k;
    case 3: 
    case 9: 
      if (getMin() >= 0) {
        return getMin() * getChild(0).getMinLength();
      }
      return 0;
    case 7: 
    case 8: 
      return 0;
    case 0: 
    case 4: 
    case 5: 
    case 11: 
      return 1;
    case 6: 
    case 24: 
    case 25: 
      return getChild(0).getMinLength();
    case 12: 
      return 0;
    case 10: 
      return getString().length();
    case 20: 
    case 21: 
    case 22: 
    case 23: 
      return 0;
    }
    throw new RuntimeException("Token#getMinLength(): Invalid Type: " + type);
  }
  
  final int getMaxLength()
  {
    int k;
    switch (type)
    {
    case 1: 
      int i = 0;
      for (int j = 0; j < size(); j++)
      {
        k = getChild(j).getMaxLength();
        if (k < 0) {
          return -1;
        }
        i += k;
      }
      return i;
    case 2: 
    case 26: 
      if (size() == 0) {
        return 0;
      }
      k = getChild(0).getMaxLength();
      for (int m = 1; (k >= 0) && (m < size()); m++)
      {
        int n = getChild(m).getMaxLength();
        if (n < 0)
        {
          k = -1;
          break;
        }
        if (n > k) {
          k = n;
        }
      }
      return k;
    case 3: 
    case 9: 
      if (getMax() >= 0) {
        return getMax() * getChild(0).getMaxLength();
      }
      return -1;
    case 7: 
    case 8: 
      return 0;
    case 0: 
      return 1;
    case 4: 
    case 5: 
    case 11: 
      return 2;
    case 6: 
    case 24: 
    case 25: 
      return getChild(0).getMaxLength();
    case 12: 
      return -1;
    case 10: 
      return getString().length();
    case 20: 
    case 21: 
    case 22: 
    case 23: 
      return 0;
    }
    throw new RuntimeException("Token#getMaxLength(): Invalid Type: " + type);
  }
  
  private static final boolean isSet(int paramInt1, int paramInt2)
  {
    return (paramInt1 & paramInt2) == paramInt2;
  }
  
  final int analyzeFirstCharacter(RangeToken paramRangeToken, int paramInt)
  {
    switch (type)
    {
    case 1: 
      int i = 0;
      for (int j = 0; j < size(); j++) {
        if ((i = getChild(j).analyzeFirstCharacter(paramRangeToken, paramInt)) != 0) {
          break;
        }
      }
      return i;
    case 2: 
      if (size() == 0) {
        return 0;
      }
      int k = 0;
      int m = 0;
      for (int n = 0; n < size(); n++)
      {
        k = getChild(n).analyzeFirstCharacter(paramRangeToken, paramInt);
        if (k == 2) {
          break;
        }
        if (k == 0) {
          m = 1;
        }
      }
      return m != 0 ? 0 : k;
    case 26: 
      int i1 = getChild(0).analyzeFirstCharacter(paramRangeToken, paramInt);
      if (size() == 1) {
        return 0;
      }
      if (i1 == 2) {
        return i1;
      }
      int i2 = getChild(1).analyzeFirstCharacter(paramRangeToken, paramInt);
      if (i2 == 2) {
        return i2;
      }
      return (i1 == 0) || (i2 == 0) ? 0 : 1;
    case 3: 
    case 9: 
      getChild(0).analyzeFirstCharacter(paramRangeToken, paramInt);
      return 0;
    case 7: 
    case 8: 
      return 0;
    case 0: 
      int i3 = getChar();
      paramRangeToken.addRange(i3, i3);
      if ((i3 < 65536) && (isSet(paramInt, 2)))
      {
        i3 = Character.toUpperCase((char)i3);
        paramRangeToken.addRange(i3, i3);
        i3 = Character.toLowerCase((char)i3);
        paramRangeToken.addRange(i3, i3);
      }
      return 1;
    case 11: 
      return 2;
    case 4: 
      paramRangeToken.mergeRanges(this);
      return 1;
    case 5: 
      paramRangeToken.mergeRanges(complementRanges(this));
      return 1;
    case 6: 
    case 24: 
      return getChild(0).analyzeFirstCharacter(paramRangeToken, paramInt);
    case 25: 
      paramInt |= ((ModifierToken)this).getOptions();
      paramInt &= (((ModifierToken)this).getOptionsMask() ^ 0xFFFFFFFF);
      return getChild(0).analyzeFirstCharacter(paramRangeToken, paramInt);
    case 12: 
      paramRangeToken.addRange(0, 1114111);
      return 2;
    case 10: 
      int i4 = getString().charAt(0);
      int i5;
      if ((REUtil.isHighSurrogate(i4)) && (getString().length() >= 2) && (REUtil.isLowSurrogate(i5 = getString().charAt(1)))) {
        i4 = REUtil.composeFromSurrogates(i4, i5);
      }
      paramRangeToken.addRange(i4, i4);
      if ((i4 < 65536) && (isSet(paramInt, 2)))
      {
        i4 = Character.toUpperCase((char)i4);
        paramRangeToken.addRange(i4, i4);
        i4 = Character.toLowerCase((char)i4);
        paramRangeToken.addRange(i4, i4);
      }
      return 1;
    case 20: 
    case 21: 
    case 22: 
    case 23: 
      return 0;
    }
    throw new RuntimeException("Token#analyzeHeadCharacter(): Invalid Type: " + type);
  }
  
  private final boolean isShorterThan(Token paramToken)
  {
    if (paramToken == null) {
      return false;
    }
    int i;
    if (type == 10) {
      i = getString().length();
    } else {
      throw new RuntimeException("Internal Error: Illegal type: " + type);
    }
    int j;
    if (type == 10) {
      j = paramToken.getString().length();
    } else {
      throw new RuntimeException("Internal Error: Illegal type: " + type);
    }
    return i < j;
  }
  
  final void findFixedString(FixedStringContainer paramFixedStringContainer, int paramInt)
  {
    switch (type)
    {
    case 1: 
      Token localToken = null;
      int i = 0;
      for (int j = 0; j < size(); j++)
      {
        getChild(j).findFixedString(paramFixedStringContainer, paramInt);
        if ((localToken == null) || (localToken.isShorterThan(token)))
        {
          localToken = token;
          i = options;
        }
      }
      token = localToken;
      options = i;
      return;
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 7: 
    case 8: 
    case 9: 
    case 11: 
    case 12: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 26: 
      token = null;
      return;
    case 0: 
      token = null;
      return;
    case 10: 
      token = this;
      options = paramInt;
      return;
    case 6: 
    case 24: 
      getChild(0).findFixedString(paramFixedStringContainer, paramInt);
      return;
    case 25: 
      paramInt |= ((ModifierToken)this).getOptions();
      paramInt &= (((ModifierToken)this).getOptionsMask() ^ 0xFFFFFFFF);
      getChild(0).findFixedString(paramFixedStringContainer, paramInt);
      return;
    }
    throw new RuntimeException("Token#findFixedString(): Invalid Type: " + type);
  }
  
  boolean match(int paramInt)
  {
    throw new RuntimeException("NFAArrow#match(): Internal error: " + type);
  }
  
  protected static RangeToken getRange(String paramString, boolean paramBoolean)
  {
    if (categories.size() == 0) {
      synchronized (categories)
      {
        Token[] arrayOfToken = new Token[categoryNames.length];
        for (int i = 0; i < arrayOfToken.length; i++) {
          arrayOfToken[i] = createRange();
        }
        for (int k = 0; k < 65536; k++)
        {
          int j = Character.getType((char)k);
          if ((j == 21) || (j == 22))
          {
            if ((k == 171) || (k == 8216) || (k == 8219) || (k == 8220) || (k == 8223) || (k == 8249)) {
              j = 29;
            }
            if ((k == 187) || (k == 8217) || (k == 8221) || (k == 8250)) {
              j = 30;
            }
          }
          arrayOfToken[j].addRange(k, k);
          switch (j)
          {
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
            j = 31;
            break;
          case 6: 
          case 7: 
          case 8: 
            j = 32;
            break;
          case 9: 
          case 10: 
          case 11: 
            j = 33;
            break;
          case 12: 
          case 13: 
          case 14: 
            j = 34;
            break;
          case 0: 
          case 15: 
          case 16: 
          case 18: 
          case 19: 
            j = 35;
            break;
          case 20: 
          case 21: 
          case 22: 
          case 23: 
          case 24: 
          case 29: 
          case 30: 
            j = 36;
            break;
          case 25: 
          case 26: 
          case 27: 
          case 28: 
            j = 37;
            break;
          case 17: 
          default: 
            throw new RuntimeException("org.apache.xerces.utils.regex.Token#getRange(): Unknown Unicode category: " + j);
          }
          arrayOfToken[j].addRange(k, k);
        }
        arrayOfToken[0].addRange(65536, 1114111);
        for (int m = 0; m < arrayOfToken.length; m++) {
          if (categoryNames[m] != null)
          {
            if (m == 0) {
              arrayOfToken[m].addRange(65536, 1114111);
            }
            categories.put(categoryNames[m], arrayOfToken[m]);
            categories2.put(categoryNames[m], complementRanges(arrayOfToken[m]));
          }
        }
        StringBuffer localStringBuffer = new StringBuffer(50);
        for (int n = 0; n < blockNames.length; n++)
        {
          localRangeToken1 = createRange();
          int i1;
          int i3;
          if (n < 84)
          {
            i1 = n * 2;
            int i2 = "\000ÿĀſƀɏɐʯʰ˿̀ͯͰϿЀӿ԰֏֐׿؀ۿ܀ݏހ޿ऀॿঀ৿਀੿઀૿଀୿஀௿ఀ౿ಀ೿ഀൿ඀෿฀๿຀໿ༀ࿿က႟Ⴀჿᄀᇿሀ፿Ꭰ᏿᐀ᙿ ᚟ᚠ᛿ក៿᠀᢯Ḁỿἀ῿ ⁯⁰₟₠⃏⃐⃿℀⅏⅐↏←⇿∀⋿⌀⏿␀␿⑀⑟①⓿─╿▀▟■◿☀⛿✀➿⠀⣿⺀⻿⼀⿟⿰⿿　〿぀ゟ゠ヿ㄀ㄯ㄰㆏㆐㆟ㆠㆿ㈀㋿㌀㏿㐀䶵一鿿ꀀ꒏꒐꓏가힣豈﫿ﬀﭏﭐ﷿︠︯︰﹏﹐﹯ﹰ﻾﻿﻿＀￯".charAt(i1);
            i3 = "\000ÿĀſƀɏɐʯʰ˿̀ͯͰϿЀӿ԰֏֐׿؀ۿ܀ݏހ޿ऀॿঀ৿਀੿઀૿଀୿஀௿ఀ౿ಀ೿ഀൿ඀෿฀๿຀໿ༀ࿿က႟Ⴀჿᄀᇿሀ፿Ꭰ᏿᐀ᙿ ᚟ᚠ᛿ក៿᠀᢯Ḁỿἀ῿ ⁯⁰₟₠⃏⃐⃿℀⅏⅐↏←⇿∀⋿⌀⏿␀␿⑀⑟①⓿─╿▀▟■◿☀⛿✀➿⠀⣿⺀⻿⼀⿟⿰⿿　〿぀ゟ゠ヿ㄀ㄯ㄰㆏㆐㆟ㆠㆿ㈀㋿㌀㏿㐀䶵一鿿ꀀ꒏꒐꓏가힣豈﫿ﬀﭏﭐ﷿︠︯︰﹏﹐﹯ﹰ﻾﻿﻿＀￯".charAt(i1 + 1);
            localRangeToken1.addRange(i2, i3);
          }
          else
          {
            i1 = (n - 84) * 2;
            localRangeToken1.addRange(nonBMPBlockRanges[i1], nonBMPBlockRanges[(i1 + 1)]);
          }
          localObject2 = blockNames[n];
          if (((String)localObject2).equals("Specials")) {
            localRangeToken1.addRange(65520, 65533);
          }
          if (((String)localObject2).equals("Private Use"))
          {
            localRangeToken1.addRange(983040, 1048573);
            localRangeToken1.addRange(1048576, 1114109);
          }
          categories.put(localObject2, localRangeToken1);
          categories2.put(localObject2, complementRanges(localRangeToken1));
          localStringBuffer.setLength(0);
          localStringBuffer.append("Is");
          if (((String)localObject2).indexOf(' ') >= 0) {
            for (i3 = 0; i3 < ((String)localObject2).length(); i3++) {
              if (((String)localObject2).charAt(i3) != ' ') {
                localStringBuffer.append(((String)localObject2).charAt(i3));
              }
            }
          } else {
            localStringBuffer.append((String)localObject2);
          }
          setAlias(localStringBuffer.toString(), (String)localObject2, true);
        }
        setAlias("ASSIGNED", "Cn", false);
        setAlias("UNASSIGNED", "Cn", true);
        RangeToken localRangeToken1 = createRange();
        localRangeToken1.addRange(0, 1114111);
        categories.put("ALL", localRangeToken1);
        categories2.put("ALL", complementRanges(localRangeToken1));
        registerNonXS("ASSIGNED");
        registerNonXS("UNASSIGNED");
        registerNonXS("ALL");
        RangeToken localRangeToken2 = createRange();
        localRangeToken2.mergeRanges(arrayOfToken[1]);
        localRangeToken2.mergeRanges(arrayOfToken[2]);
        localRangeToken2.mergeRanges(arrayOfToken[5]);
        categories.put("IsAlpha", localRangeToken2);
        categories2.put("IsAlpha", complementRanges(localRangeToken2));
        registerNonXS("IsAlpha");
        Object localObject2 = createRange();
        ((Token)localObject2).mergeRanges(localRangeToken2);
        ((Token)localObject2).mergeRanges(arrayOfToken[9]);
        categories.put("IsAlnum", localObject2);
        categories2.put("IsAlnum", complementRanges((Token)localObject2));
        registerNonXS("IsAlnum");
        RangeToken localRangeToken3 = createRange();
        localRangeToken3.mergeRanges(token_spaces);
        localRangeToken3.mergeRanges(arrayOfToken[34]);
        categories.put("IsSpace", localRangeToken3);
        categories2.put("IsSpace", complementRanges(localRangeToken3));
        registerNonXS("IsSpace");
        RangeToken localRangeToken4 = createRange();
        localRangeToken4.mergeRanges((Token)localObject2);
        localRangeToken4.addRange(95, 95);
        categories.put("IsWord", localRangeToken4);
        categories2.put("IsWord", complementRanges(localRangeToken4));
        registerNonXS("IsWord");
        RangeToken localRangeToken5 = createRange();
        localRangeToken5.addRange(0, 127);
        categories.put("IsASCII", localRangeToken5);
        categories2.put("IsASCII", complementRanges(localRangeToken5));
        registerNonXS("IsASCII");
        RangeToken localRangeToken6 = createRange();
        localRangeToken6.mergeRanges(arrayOfToken[35]);
        localRangeToken6.addRange(32, 32);
        categories.put("IsGraph", complementRanges(localRangeToken6));
        categories2.put("IsGraph", localRangeToken6);
        registerNonXS("IsGraph");
        RangeToken localRangeToken7 = createRange();
        localRangeToken7.addRange(48, 57);
        localRangeToken7.addRange(65, 70);
        localRangeToken7.addRange(97, 102);
        categories.put("IsXDigit", complementRanges(localRangeToken7));
        categories2.put("IsXDigit", localRangeToken7);
        registerNonXS("IsXDigit");
        setAlias("IsDigit", "Nd", true);
        setAlias("IsUpper", "Lu", true);
        setAlias("IsLower", "Ll", true);
        setAlias("IsCntrl", "C", true);
        setAlias("IsPrint", "C", false);
        setAlias("IsPunct", "P", true);
        registerNonXS("IsDigit");
        registerNonXS("IsUpper");
        registerNonXS("IsLower");
        registerNonXS("IsCntrl");
        registerNonXS("IsPrint");
        registerNonXS("IsPunct");
        setAlias("alpha", "IsAlpha", true);
        setAlias("alnum", "IsAlnum", true);
        setAlias("ascii", "IsASCII", true);
        setAlias("cntrl", "IsCntrl", true);
        setAlias("digit", "IsDigit", true);
        setAlias("graph", "IsGraph", true);
        setAlias("lower", "IsLower", true);
        setAlias("print", "IsPrint", true);
        setAlias("punct", "IsPunct", true);
        setAlias("space", "IsSpace", true);
        setAlias("upper", "IsUpper", true);
        setAlias("word", "IsWord", true);
        setAlias("xdigit", "IsXDigit", true);
        registerNonXS("alpha");
        registerNonXS("alnum");
        registerNonXS("ascii");
        registerNonXS("cntrl");
        registerNonXS("digit");
        registerNonXS("graph");
        registerNonXS("lower");
        registerNonXS("print");
        registerNonXS("punct");
        registerNonXS("space");
        registerNonXS("upper");
        registerNonXS("word");
        registerNonXS("xdigit");
      }
    }
    ??? = paramBoolean ? (RangeToken)categories.get(paramString) : (RangeToken)categories2.get(paramString);
    return ???;
  }
  
  protected static RangeToken getRange(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    RangeToken localRangeToken = getRange(paramString, paramBoolean1);
    if ((paramBoolean2) && (localRangeToken != null) && (isRegisterNonXS(paramString))) {
      localRangeToken = null;
    }
    return localRangeToken;
  }
  
  protected static void registerNonXS(String paramString)
  {
    if (nonxs == null) {
      nonxs = new Hashtable();
    }
    nonxs.put(paramString, paramString);
  }
  
  protected static boolean isRegisterNonXS(String paramString)
  {
    if (nonxs == null) {
      return false;
    }
    return nonxs.containsKey(paramString);
  }
  
  private static void setAlias(String paramString1, String paramString2, boolean paramBoolean)
  {
    Token localToken1 = (Token)categories.get(paramString2);
    Token localToken2 = (Token)categories2.get(paramString2);
    if (paramBoolean)
    {
      categories.put(paramString1, localToken1);
      categories2.put(paramString1, localToken2);
    }
    else
    {
      categories2.put(paramString1, localToken1);
      categories.put(paramString1, localToken2);
    }
  }
  
  static synchronized Token getGraphemePattern()
  {
    if (token_grapheme != null) {
      return token_grapheme;
    }
    RangeToken localRangeToken1 = createRange();
    localRangeToken1.mergeRanges(getRange("ASSIGNED", true));
    localRangeToken1.subtractRanges(getRange("M", true));
    localRangeToken1.subtractRanges(getRange("C", true));
    RangeToken localRangeToken2 = createRange();
    for (int i = 0; i < "्্੍્୍்్್്ฺ྄".length(); i++) {
      localRangeToken2.addRange(i, i);
    }
    RangeToken localRangeToken3 = createRange();
    localRangeToken3.mergeRanges(getRange("M", true));
    localRangeToken3.addRange(4448, 4607);
    localRangeToken3.addRange(65438, 65439);
    UnionToken localUnionToken = createUnion();
    localUnionToken.addChild(localRangeToken1);
    localUnionToken.addChild(token_empty);
    Object localObject = createUnion();
    ((Token)localObject).addChild(createConcat(localRangeToken2, getRange("L", true)));
    ((Token)localObject).addChild(localRangeToken3);
    localObject = createClosure((Token)localObject);
    localObject = createConcat(localUnionToken, (Token)localObject);
    token_grapheme = (Token)localObject;
    return token_grapheme;
  }
  
  static synchronized Token getCombiningCharacterSequence()
  {
    if (token_ccs != null) {
      return token_ccs;
    }
    Object localObject = createClosure(getRange("M", true));
    localObject = createConcat(getRange("M", false), (Token)localObject);
    token_ccs = (Token)localObject;
    return token_ccs;
  }
  
  static
  {
    token_dot = new Token(11);
    token_0to9 = createRange();
    token_0to9.addRange(48, 57);
    token_wordchars = createRange();
    token_wordchars.addRange(48, 57);
    token_wordchars.addRange(65, 90);
    token_wordchars.addRange(95, 95);
    token_wordchars.addRange(97, 122);
    token_spaces = createRange();
    token_spaces.addRange(9, 9);
    token_spaces.addRange(10, 10);
    token_spaces.addRange(12, 12);
    token_spaces.addRange(13, 13);
    token_spaces.addRange(32, 32);
  }
  
  static class UnionToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = -2568843945989489861L;
    Vector children;
    
    UnionToken(int paramInt)
    {
      super();
    }
    
    void addChild(Token paramToken)
    {
      if (paramToken == null) {
        return;
      }
      if (children == null) {
        children = new Vector();
      }
      if (type == 2)
      {
        children.addElement(paramToken);
        return;
      }
      if (type == 1)
      {
        for (i = 0; i < paramToken.size(); i++) {
          addChild(paramToken.getChild(i));
        }
        return;
      }
      int i = children.size();
      if (i == 0)
      {
        children.addElement(paramToken);
        return;
      }
      Object localObject = (Token)children.elementAt(i - 1);
      if (((type != 0) && (type != 10)) || ((type != 0) && (type != 10)))
      {
        children.addElement(paramToken);
        return;
      }
      int j = type == 0 ? 2 : paramToken.getString().length();
      StringBuffer localStringBuffer;
      int k;
      if (type == 0)
      {
        localStringBuffer = new StringBuffer(2 + j);
        k = ((Token)localObject).getChar();
        if (k >= 65536) {
          localStringBuffer.append(REUtil.decomposeToSurrogates(k));
        } else {
          localStringBuffer.append((char)k);
        }
        localObject = Token.createString(null);
        children.setElementAt(localObject, i - 1);
      }
      else
      {
        localStringBuffer = new StringBuffer(((Token)localObject).getString().length() + j);
        localStringBuffer.append(((Token)localObject).getString());
      }
      if (type == 0)
      {
        k = paramToken.getChar();
        if (k >= 65536) {
          localStringBuffer.append(REUtil.decomposeToSurrogates(k));
        } else {
          localStringBuffer.append((char)k);
        }
      }
      else
      {
        localStringBuffer.append(paramToken.getString());
      }
      string = new String(localStringBuffer);
    }
    
    int size()
    {
      return children == null ? 0 : children.size();
    }
    
    Token getChild(int paramInt)
    {
      return (Token)children.elementAt(paramInt);
    }
    
    public String toString(int paramInt)
    {
      Object localObject;
      String str;
      int i;
      if (type == 1)
      {
        if (children.size() == 2)
        {
          localObject = getChild(0);
          Token localToken = getChild(1);
          if ((type == 3) && (localToken.getChild(0) == localObject)) {
            str = ((Token)localObject).toString(paramInt) + "+";
          } else if ((type == 9) && (localToken.getChild(0) == localObject)) {
            str = ((Token)localObject).toString(paramInt) + "+?";
          } else {
            str = ((Token)localObject).toString(paramInt) + localToken.toString(paramInt);
          }
        }
        else
        {
          localObject = new StringBuffer();
          for (i = 0; i < children.size(); i++) {
            ((StringBuffer)localObject).append(((Token)children.elementAt(i)).toString(paramInt));
          }
          str = new String((StringBuffer)localObject);
        }
        return str;
      }
      if ((children.size() == 2) && (getChild1type == 7))
      {
        str = getChild(0).toString(paramInt) + "?";
      }
      else if ((children.size() == 2) && (getChild0type == 7))
      {
        str = getChild(1).toString(paramInt) + "??";
      }
      else
      {
        localObject = new StringBuffer();
        ((StringBuffer)localObject).append(((Token)children.elementAt(0)).toString(paramInt));
        for (i = 1; i < children.size(); i++)
        {
          ((StringBuffer)localObject).append('|');
          ((StringBuffer)localObject).append(((Token)children.elementAt(i)).toString(paramInt));
        }
        str = new String((StringBuffer)localObject);
      }
      return str;
    }
  }
  
  static class ModifierToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = -9114536559696480356L;
    final Token child;
    final int add;
    final int mask;
    
    ModifierToken(Token paramToken, int paramInt1, int paramInt2)
    {
      super();
      child = paramToken;
      add = paramInt1;
      mask = paramInt2;
    }
    
    int size()
    {
      return 1;
    }
    
    Token getChild(int paramInt)
    {
      return child;
    }
    
    int getOptions()
    {
      return add;
    }
    
    int getOptionsMask()
    {
      return mask;
    }
    
    public String toString(int paramInt)
    {
      return "(?" + (add == 0 ? "" : REUtil.createOptionString(add)) + (mask == 0 ? "" : REUtil.createOptionString(mask)) + ":" + child.toString(paramInt) + ")";
    }
  }
  
  static class ConditionToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = 4353765277910594411L;
    final int refNumber;
    final Token condition;
    final Token yes;
    final Token no;
    
    ConditionToken(int paramInt, Token paramToken1, Token paramToken2, Token paramToken3)
    {
      super();
      refNumber = paramInt;
      condition = paramToken1;
      yes = paramToken2;
      no = paramToken3;
    }
    
    int size()
    {
      return no == null ? 1 : 2;
    }
    
    Token getChild(int paramInt)
    {
      if (paramInt == 0) {
        return yes;
      }
      if (paramInt == 1) {
        return no;
      }
      throw new RuntimeException("Internal Error: " + paramInt);
    }
    
    public String toString(int paramInt)
    {
      String str;
      if (refNumber > 0) {
        str = "(?(" + refNumber + ")";
      } else if (condition.type == 8) {
        str = "(?(" + condition + ")";
      } else {
        str = "(?" + condition;
      }
      if (no == null) {
        str = str + yes + ")";
      } else {
        str = str + yes + "|" + no + ")";
      }
      return str;
    }
  }
  
  static class ParenToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = -5938014719827987704L;
    final Token child;
    final int parennumber;
    
    ParenToken(int paramInt1, Token paramToken, int paramInt2)
    {
      super();
      child = paramToken;
      parennumber = paramInt2;
    }
    
    int size()
    {
      return 1;
    }
    
    Token getChild(int paramInt)
    {
      return child;
    }
    
    int getParenNumber()
    {
      return parennumber;
    }
    
    public String toString(int paramInt)
    {
      String str = null;
      switch (type)
      {
      case 6: 
        if (parennumber == 0) {
          str = "(?:" + child.toString(paramInt) + ")";
        } else {
          str = "(" + child.toString(paramInt) + ")";
        }
        break;
      case 20: 
        str = "(?=" + child.toString(paramInt) + ")";
        break;
      case 21: 
        str = "(?!" + child.toString(paramInt) + ")";
        break;
      case 22: 
        str = "(?<=" + child.toString(paramInt) + ")";
        break;
      case 23: 
        str = "(?<!" + child.toString(paramInt) + ")";
        break;
      case 24: 
        str = "(?>" + child.toString(paramInt) + ")";
      }
      return str;
    }
  }
  
  static class ClosureToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = 1308971930673997452L;
    int min;
    int max;
    final Token child;
    
    ClosureToken(int paramInt, Token paramToken)
    {
      super();
      child = paramToken;
      setMin(-1);
      setMax(-1);
    }
    
    int size()
    {
      return 1;
    }
    
    Token getChild(int paramInt)
    {
      return child;
    }
    
    final void setMin(int paramInt)
    {
      min = paramInt;
    }
    
    final void setMax(int paramInt)
    {
      max = paramInt;
    }
    
    final int getMin()
    {
      return min;
    }
    
    final int getMax()
    {
      return max;
    }
    
    public String toString(int paramInt)
    {
      String str;
      if (type == 3)
      {
        if ((getMin() < 0) && (getMax() < 0)) {
          str = child.toString(paramInt) + "*";
        } else if (getMin() == getMax()) {
          str = child.toString(paramInt) + "{" + getMin() + "}";
        } else if ((getMin() >= 0) && (getMax() >= 0)) {
          str = child.toString(paramInt) + "{" + getMin() + "," + getMax() + "}";
        } else if ((getMin() >= 0) && (getMax() < 0)) {
          str = child.toString(paramInt) + "{" + getMin() + ",}";
        } else {
          throw new RuntimeException("Token#toString(): CLOSURE " + getMin() + ", " + getMax());
        }
      }
      else if ((getMin() < 0) && (getMax() < 0)) {
        str = child.toString(paramInt) + "*?";
      } else if (getMin() == getMax()) {
        str = child.toString(paramInt) + "{" + getMin() + "}?";
      } else if ((getMin() >= 0) && (getMax() >= 0)) {
        str = child.toString(paramInt) + "{" + getMin() + "," + getMax() + "}?";
      } else if ((getMin() >= 0) && (getMax() < 0)) {
        str = child.toString(paramInt) + "{" + getMin() + ",}?";
      } else {
        throw new RuntimeException("Token#toString(): NONGREEDYCLOSURE " + getMin() + ", " + getMax());
      }
      return str;
    }
  }
  
  static class CharToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = -4394272816279496989L;
    final int chardata;
    
    CharToken(int paramInt1, int paramInt2)
    {
      super();
      chardata = paramInt2;
    }
    
    int getChar()
    {
      return chardata;
    }
    
    public String toString(int paramInt)
    {
      String str1;
      switch (type)
      {
      case 0: 
        switch (chardata)
        {
        case 40: 
        case 41: 
        case 42: 
        case 43: 
        case 46: 
        case 63: 
        case 91: 
        case 92: 
        case 123: 
        case 124: 
          str1 = "\\" + (char)chardata;
          break;
        case 12: 
          str1 = "\\f";
          break;
        case 10: 
          str1 = "\\n";
          break;
        case 13: 
          str1 = "\\r";
          break;
        case 9: 
          str1 = "\\t";
          break;
        case 27: 
          str1 = "\\e";
          break;
        default: 
          if (chardata >= 65536)
          {
            String str2 = "0" + Integer.toHexString(chardata);
            str1 = "\\v" + str2.substring(str2.length() - 6, str2.length());
          }
          else
          {
            str1 = "" + (char)chardata;
          }
          break;
        }
        break;
      case 8: 
        if ((this == Token.token_linebeginning) || (this == Token.token_lineend)) {
          str1 = "" + (char)chardata;
        } else {
          str1 = "\\" + (char)chardata;
        }
        break;
      default: 
        str1 = null;
      }
      return str1;
    }
    
    boolean match(int paramInt)
    {
      if (type == 0) {
        return paramInt == chardata;
      }
      throw new RuntimeException("NFAArrow#match(): Internal error: " + type);
    }
  }
  
  static class ConcatToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = 8717321425541346381L;
    final Token child;
    final Token child2;
    
    ConcatToken(Token paramToken1, Token paramToken2)
    {
      super();
      child = paramToken1;
      child2 = paramToken2;
    }
    
    int size()
    {
      return 2;
    }
    
    Token getChild(int paramInt)
    {
      return paramInt == 0 ? child : child2;
    }
    
    public String toString(int paramInt)
    {
      String str;
      if ((child2.type == 3) && (child2.getChild(0) == child)) {
        str = child.toString(paramInt) + "+";
      } else if ((child2.type == 9) && (child2.getChild(0) == child)) {
        str = child.toString(paramInt) + "+?";
      } else {
        str = child.toString(paramInt) + child2.toString(paramInt);
      }
      return str;
    }
  }
  
  static class StringToken
    extends Token
    implements Serializable
  {
    private static final long serialVersionUID = -4614366944218504172L;
    String string;
    final int refNumber;
    
    StringToken(int paramInt1, String paramString, int paramInt2)
    {
      super();
      string = paramString;
      refNumber = paramInt2;
    }
    
    int getReferenceNumber()
    {
      return refNumber;
    }
    
    String getString()
    {
      return string;
    }
    
    public String toString(int paramInt)
    {
      if (type == 12) {
        return "\\" + refNumber;
      }
      return REUtil.quoteMeta(string);
    }
  }
  
  static class FixedStringContainer
  {
    Token token = null;
    int options = 0;
    
    FixedStringContainer() {}
  }
}
