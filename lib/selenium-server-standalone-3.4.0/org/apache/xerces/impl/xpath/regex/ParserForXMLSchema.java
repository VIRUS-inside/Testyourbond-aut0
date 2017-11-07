package org.apache.xerces.impl.xpath.regex;

import java.util.Hashtable;
import java.util.Locale;

class ParserForXMLSchema
  extends RegexParser
{
  private static Hashtable ranges = null;
  private static Hashtable ranges2 = null;
  private static final String SPACES = "\t\n\r\r  ";
  private static final String NAMECHARS = "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣";
  private static final String LETTERS = "AZazÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣";
  private static final String DIGITS = "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩";
  
  public ParserForXMLSchema() {}
  
  public ParserForXMLSchema(Locale paramLocale)
  {
    super(paramLocale);
  }
  
  Token processCaret()
    throws ParseException
  {
    next();
    return Token.createChar(94);
  }
  
  Token processDollar()
    throws ParseException
  {
    next();
    return Token.createChar(36);
  }
  
  Token processLookahead()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processNegativelookahead()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processLookbehind()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processNegativelookbehind()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_A()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_Z()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_z()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_b()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_B()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_lt()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_gt()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processStar(Token paramToken)
    throws ParseException
  {
    next();
    return Token.createClosure(paramToken);
  }
  
  Token processPlus(Token paramToken)
    throws ParseException
  {
    next();
    return Token.createConcat(paramToken, Token.createClosure(paramToken));
  }
  
  Token processQuestion(Token paramToken)
    throws ParseException
  {
    next();
    Token.UnionToken localUnionToken = Token.createUnion();
    localUnionToken.addChild(paramToken);
    localUnionToken.addChild(Token.createEmpty());
    return localUnionToken;
  }
  
  boolean checkQuestion(int paramInt)
  {
    return false;
  }
  
  Token processParen()
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
  
  Token processParen2()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processCondition()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processModifiers()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processIndependent()
    throws ParseException
  {
    throw ex("parser.process.1", offset);
  }
  
  Token processBacksolidus_c()
    throws ParseException
  {
    next();
    return getTokenForShorthand(99);
  }
  
  Token processBacksolidus_C()
    throws ParseException
  {
    next();
    return getTokenForShorthand(67);
  }
  
  Token processBacksolidus_i()
    throws ParseException
  {
    next();
    return getTokenForShorthand(105);
  }
  
  Token processBacksolidus_I()
    throws ParseException
  {
    next();
    return getTokenForShorthand(73);
  }
  
  Token processBacksolidus_g()
    throws ParseException
  {
    throw ex("parser.process.1", offset - 2);
  }
  
  Token processBacksolidus_X()
    throws ParseException
  {
    throw ex("parser.process.1", offset - 2);
  }
  
  Token processBackreference()
    throws ParseException
  {
    throw ex("parser.process.1", offset - 4);
  }
  
  int processCIinCharacterClass(RangeToken paramRangeToken, int paramInt)
  {
    paramRangeToken.mergeRanges(getTokenForShorthand(paramInt));
    return -1;
  }
  
  protected RangeToken parseCharacterClass(boolean paramBoolean)
    throws ParseException
  {
    setContext(1);
    next();
    int i = 0;
    int j = 0;
    RangeToken localRangeToken1 = null;
    RangeToken localRangeToken2;
    if ((read() == 0) && (chardata == 94))
    {
      i = 1;
      next();
      localRangeToken1 = Token.createRange();
      localRangeToken1.addRange(0, 1114111);
      localRangeToken2 = Token.createRange();
    }
    else
    {
      localRangeToken2 = Token.createRange();
    }
    int k;
    for (int m = 1; (k = read()) != 1; m = 0)
    {
      j = 0;
      if ((k == 0) && (chardata == 93) && (m == 0))
      {
        if (i == 0) {
          break;
        }
        localRangeToken1.subtractRanges(localRangeToken2);
        localRangeToken2 = localRangeToken1;
        break;
      }
      int n = chardata;
      int i1 = 0;
      if (k == 10)
      {
        switch (n)
        {
        case 68: 
        case 83: 
        case 87: 
        case 100: 
        case 115: 
        case 119: 
          localRangeToken2.mergeRanges(getTokenForShorthand(n));
          i1 = 1;
          break;
        case 67: 
        case 73: 
        case 99: 
        case 105: 
          n = processCIinCharacterClass(localRangeToken2, n);
          if (n >= 0) {
            break;
          }
          i1 = 1;
          break;
        case 80: 
        case 112: 
          int i2 = offset;
          RangeToken localRangeToken4 = processBacksolidus_pP(n);
          if (localRangeToken4 == null) {
            throw ex("parser.atom.5", i2);
          }
          localRangeToken2.mergeRanges(localRangeToken4);
          i1 = 1;
          break;
        case 45: 
          n = decodeEscaped();
          j = 1;
          break;
        default: 
          n = decodeEscaped();
          break;
        }
      }
      else if ((k == 24) && (m == 0))
      {
        if (i != 0)
        {
          localRangeToken1.subtractRanges(localRangeToken2);
          localRangeToken2 = localRangeToken1;
        }
        RangeToken localRangeToken3 = parseCharacterClass(false);
        localRangeToken2.subtractRanges(localRangeToken3);
        if ((read() == 0) && (chardata == 93)) {
          break;
        }
        throw ex("parser.cc.5", offset);
      }
      next();
      if (i1 == 0)
      {
        if (k == 0)
        {
          if (n == 91) {
            throw ex("parser.cc.6", offset - 2);
          }
          if (n == 93) {
            throw ex("parser.cc.7", offset - 2);
          }
          if ((n == 45) && (chardata != 93) && (m == 0)) {
            throw ex("parser.cc.8", offset - 2);
          }
        }
        if ((read() != 0) || (chardata != 45) || ((n == 45) && (m != 0)))
        {
          if ((!isSet(2)) || (n > 65535)) {
            localRangeToken2.addRange(n, n);
          } else {
            RegexParser.addCaseInsensitiveChar(localRangeToken2, n);
          }
        }
        else
        {
          next();
          if ((k = read()) == 1) {
            throw ex("parser.cc.2", offset);
          }
          if ((k == 0) && (chardata == 93))
          {
            if ((!isSet(2)) || (n > 65535)) {
              localRangeToken2.addRange(n, n);
            } else {
              RegexParser.addCaseInsensitiveChar(localRangeToken2, n);
            }
            localRangeToken2.addRange(45, 45);
          }
          else
          {
            if (k == 24) {
              throw ex("parser.cc.8", offset - 1);
            }
            int i3 = chardata;
            if (k == 0)
            {
              if (i3 == 91) {
                throw ex("parser.cc.6", offset - 1);
              }
              if (i3 == 93) {
                throw ex("parser.cc.7", offset - 1);
              }
              if (i3 == 45) {
                throw ex("parser.cc.8", offset - 2);
              }
            }
            else if (k == 10)
            {
              i3 = decodeEscaped();
            }
            next();
            if (n > i3) {
              throw ex("parser.ope.3", offset - 1);
            }
            if ((!isSet(2)) || ((n > 65535) && (i3 > 65535))) {
              localRangeToken2.addRange(n, i3);
            } else {
              RegexParser.addCaseInsensitiveCharRange(localRangeToken2, n, i3);
            }
          }
        }
      }
    }
    if (read() == 1) {
      throw ex("parser.cc.2", offset);
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
    throw ex("parser.process.1", offset);
  }
  
  Token getTokenForShorthand(int paramInt)
  {
    switch (paramInt)
    {
    case 100: 
      return getRange("xml:isDigit", true);
    case 68: 
      return getRange("xml:isDigit", false);
    case 119: 
      return getRange("xml:isWord", true);
    case 87: 
      return getRange("xml:isWord", false);
    case 115: 
      return getRange("xml:isSpace", true);
    case 83: 
      return getRange("xml:isSpace", false);
    case 99: 
      return getRange("xml:isNameChar", true);
    case 67: 
      return getRange("xml:isNameChar", false);
    case 105: 
      return getRange("xml:isInitialNameChar", true);
    case 73: 
      return getRange("xml:isInitialNameChar", false);
    }
    throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(paramInt, 16));
  }
  
  int decodeEscaped()
    throws ParseException
  {
    if (read() != 10) {
      throw ex("parser.next.1", offset - 1);
    }
    int i = chardata;
    switch (i)
    {
    case 110: 
      i = 10;
      break;
    case 114: 
      i = 13;
      break;
    case 116: 
      i = 9;
      break;
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 45: 
    case 46: 
    case 63: 
    case 91: 
    case 92: 
    case 93: 
    case 94: 
    case 123: 
    case 124: 
    case 125: 
      break;
    default: 
      throw ex("parser.process.1", offset - 2);
    }
    return i;
  }
  
  protected static synchronized RangeToken getRange(String paramString, boolean paramBoolean)
  {
    if (ranges == null)
    {
      ranges = new Hashtable();
      ranges2 = new Hashtable();
      localRangeToken = Token.createRange();
      setupRange(localRangeToken, "\t\n\r\r  ");
      ranges.put("xml:isSpace", localRangeToken);
      ranges2.put("xml:isSpace", Token.complementRanges(localRangeToken));
      localRangeToken = Token.createRange();
      setupRange(localRangeToken, "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩");
      ranges.put("xml:isDigit", localRangeToken);
      ranges2.put("xml:isDigit", Token.complementRanges(localRangeToken));
      localRangeToken = Token.createRange();
      setupRange(localRangeToken, "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩");
      ranges.put("xml:isDigit", localRangeToken);
      ranges2.put("xml:isDigit", Token.complementRanges(localRangeToken));
      localRangeToken = Token.createRange();
      setupRange(localRangeToken, "AZazÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣");
      localRangeToken.mergeRanges((Token)ranges.get("xml:isDigit"));
      ranges.put("xml:isWord", localRangeToken);
      ranges2.put("xml:isWord", Token.complementRanges(localRangeToken));
      localRangeToken = Token.createRange();
      setupRange(localRangeToken, "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣");
      ranges.put("xml:isNameChar", localRangeToken);
      ranges2.put("xml:isNameChar", Token.complementRanges(localRangeToken));
      localRangeToken = Token.createRange();
      setupRange(localRangeToken, "AZazÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣");
      localRangeToken.addRange(95, 95);
      localRangeToken.addRange(58, 58);
      ranges.put("xml:isInitialNameChar", localRangeToken);
      ranges2.put("xml:isInitialNameChar", Token.complementRanges(localRangeToken));
    }
    RangeToken localRangeToken = paramBoolean ? (RangeToken)ranges.get(paramString) : (RangeToken)ranges2.get(paramString);
    return localRangeToken;
  }
  
  static void setupRange(Token paramToken, String paramString)
  {
    int i = paramString.length();
    for (int j = 0; j < i; j += 2) {
      paramToken.addRange(paramString.charAt(j), paramString.charAt(j + 1));
    }
  }
}
