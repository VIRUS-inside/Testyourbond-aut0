package com.steadystate.css.parser;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SelectorFactory;

public class SACParserCSS2 extends AbstractSACParser implements SACParserCSS2Constants
{
  public SACParserCSS2TokenManager token_source;
  public Token token;
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos;
  private Token jj_lastpos;
  private int jj_la;
  private int jj_gen;
  
  public SACParserCSS2()
  {
    this((CharStream)null);
  }
  
  public String getParserVersion() {
    return "http://www.w3.org/TR/REC-CSS2/";
  }
  
  protected String getGrammarUri()
  {
    return "http://www.w3.org/TR/REC-CSS2/grammar.html";
  }
  




  public final void styleSheet()
    throws ParseException
  {
    try
    {
      handleStartDocument();
      styleSheetRuleList();
      jj_consume_token(0);
      
      handleEndDocument(); } finally { handleEndDocument();
    }
  }
  
  public final void styleSheetRuleList() throws ParseException { boolean ruleFound = false;
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
      case 25: 
      case 26: 
        break;
      
      default: 
        jj_la1[0] = jj_gen;
        break;
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 1: 
        jj_consume_token(1);
        break;
      
      case 25: 
        jj_consume_token(25);
        break;
      
      case 26: 
        jj_consume_token(26);
      }
      
    }
    jj_la1[1] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
    

    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 33: 
      charsetRule();
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
        case 25: 
        case 26: 
          break;
        
        default: 
          jj_la1[2] = jj_gen;
          break;
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 1: 
          jj_consume_token(1);
          break;
        
        case 25: 
          jj_consume_token(25);
          break;
        
        case 26: 
          jj_consume_token(26);
        }
        
      }
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    
    


    jj_la1[4] = jj_gen;
    



    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
    {
    case 9: 
    case 11: 
    case 12: 
    case 18: 
    case 20: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 34: 
    case 58: 
      break;
    
    default: 
      jj_la1[5] = jj_gen;
      break;
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 29: 
      importRule(ruleFound);
      break;
    
    case 9: 
    case 11: 
    case 12: 
    case 18: 
    case 20: 
    case 30: 
    case 31: 
    case 32: 
    case 34: 
    case 58: 
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 9: 
      case 11: 
      case 12: 
      case 18: 
      case 20: 
      case 58: 
        styleRule();
        break;
      
      case 31: 
        mediaRule();
        break;
      
      case 30: 
        pageRule();
        break;
      
      case 32: 
        fontFaceRule();
        break;
      
      case 34: 
        unknownAtRule();
        break;
      
      default: 
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ruleFound = true;
      break;
    
    default: 
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
      case 25: 
      case 26: 
        break;
      
      default: 
        jj_la1[8] = jj_gen;
        break;
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 1: 
        jj_consume_token(1);
        break;
      
      case 25: 
        jj_consume_token(25);
        break;
      
      case 26: 
        jj_consume_token(26);
      }
      
    }
    jj_la1[9] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  




  public final void styleSheetRuleSingle()
    throws ParseException
  {
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[10] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 33: 
      charsetRule();
      break;
    
    case 29: 
      importRule(false);
      break;
    
    case 9: 
    case 11: 
    case 12: 
    case 18: 
    case 20: 
    case 58: 
      styleRule();
      break;
    
    case 31: 
      mediaRule();
      break;
    
    case 30: 
      pageRule();
      break;
    
    case 32: 
      fontFaceRule();
      break;
    
    case 34: 
      unknownAtRule();
      break;
    case 10: case 13: case 14: case 15: case 16: case 17: case 19: case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 35: case 36: case 37: case 38: case 39: 
    case 40: case 41: case 42: case 43: case 44: case 45: case 46: case 47: case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: default: 
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }
  
  public final void charsetRule() throws ParseException
  {
    try {
      jj_consume_token(33);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[12] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      Token t = jj_consume_token(21);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[13] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      jj_consume_token(10);
      handleCharset(t.toString(), locator);
    } catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidCharsetRule", e));
    }
  }
  
  public final void unknownAtRule() throws ParseException
  {
    try {
      jj_consume_token(34);
      Locator locator = createLocator(token);
      String s = skip();
      handleIgnorableAtRule(s, locator);
    } catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidUnknownRule", e));
    }
  }
  




  public final void importRule(boolean nonImportRuleFoundBefore)
    throws ParseException
  {
    SACMediaListImpl ml = new SACMediaListImpl();
    try
    {
      jj_consume_token(29);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[14] = jj_gen;
          break;
        }
        jj_consume_token(1); }
      Token t;
      Token t; switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 21: 
        t = jj_consume_token(21);
        break;
      
      case 24: 
        t = jj_consume_token(24);
        break;
      
      default: 
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      Token t;
      for (;;) {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[16] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 58: 
        mediaList(ml);
        break;
      
      default: 
        jj_la1[17] = jj_gen;
      }
      
      jj_consume_token(10);
      if (nonImportRuleFoundBefore)
      {
        handleImportStyle(unescape(image, false), ml, null, locator);
      }
      else
      {
        handleImportStyle(unescape(image, false), ml, null, locator);
      }
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      error_skipAtRule();
    } catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidImportRule", e));
      error_skipAtRule();
    }
  }
  


  public final void mediaRule()
    throws ParseException
  {
    boolean start = false;
    SACMediaListImpl ml = new SACMediaListImpl();
    try
    {
      jj_consume_token(31);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[18] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      mediaList(ml);
      start = true;
      handleStartMedia(ml, locator);
      jj_consume_token(6);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[19] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 9: 
      case 11: 
      case 12: 
      case 18: 
      case 20: 
      case 30: 
      case 34: 
      case 58: 
        mediaRuleList();
        break;
      
      default: 
        jj_la1[20] = jj_gen;
      }
      
      jj_consume_token(7);
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      error_skipblock();
    } catch (ParseException e) {
      CSSParseException cpe = toCSSParseException("invalidMediaRule", e);
      getErrorHandler().error(cpe);
      getErrorHandler().warning(createSkipWarning("ignoringRule", cpe));
      error_skipblock();
    } finally {
      if (start) {
        handleEndMedia(ml);
      }
    }
  }
  
  public final void mediaList(SACMediaListImpl ml) throws ParseException {
    try {
      String s = medium();
      ml.setLocator(createLocator(token));
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 8: 
          break;
        
        default: 
          jj_la1[21] = jj_gen;
          break;
        }
        jj_consume_token(8);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[22] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        ml.add(s);
        s = medium();
      }
      ml.add(s);
    } catch (ParseException e) {
      throw toCSSParseException("invalidMediaList", e);
    }
    String s;
  }
  
  public final void mediaRuleList() throws ParseException {
    for (;;) {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 9: 
      case 11: 
      case 12: 
      case 18: 
      case 20: 
      case 58: 
        styleRule();
        break;
      
      case 30: 
        pageRule();
        break;
      
      case 34: 
        unknownAtRule();
        break;
      
      default: 
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[24] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      }
      
    }
    







    jj_la1[25] = jj_gen;
  }
  






  public final String medium()
    throws ParseException
  {
    Token t = jj_consume_token(58);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[26] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    handleMedium(image, createLocator(t));
    return image;
  }
  



  public final void pageRule()
    throws ParseException
  {
    Token t = null;
    String s = null;
    boolean start = false;
    try
    {
      jj_consume_token(30);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[27] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 11: 
      case 58: 
        if (jj_2_1(2)) {
          t = jj_consume_token(58);
          s = pseudoPage();
          for (;;)
          {
            switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
            {
            case 1: 
              break;
            
            default: 
              jj_la1[28] = jj_gen;
              break;
            }
            jj_consume_token(1);
          }
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 58: 
          t = jj_consume_token(58);
          for (;;)
          {
            switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
            {
            case 1: 
              break;
            
            default: 
              jj_la1[29] = jj_gen;
              break;
            }
            jj_consume_token(1);
          }
        

        case 11: 
          s = pseudoPage();
          for (;;)
          {
            switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
            {
            case 1: 
              break;
            
            default: 
              jj_la1[30] = jj_gen;
              break;
            }
            jj_consume_token(1);
          }
        }
        
        
        jj_la1[31] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      
      


      jj_la1[32] = jj_gen;
      

      jj_consume_token(6);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[33] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      start = true;
      handleStartPage(t != null ? unescape(image, false) : null, s, locator);
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 58: 
        declaration();
        break;
      
      default: 
        jj_la1[34] = jj_gen;
      }
      
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 10: 
          break;
        
        default: 
          jj_la1[35] = jj_gen;
          break;
        }
        jj_consume_token(10);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[36] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 58: 
          declaration();
          break;
        
        default: 
          jj_la1[37] = jj_gen;
        }
        
      }
      jj_consume_token(7);
    } catch (ParseException e) {
      throw toCSSParseException("invalidPageRule", e);
    } finally {
      if (start) {
        handleEndPage(t != null ? unescape(image, false) : null, s);
      }
    }
    
    Locator locator;
  }
  

  public final String pseudoPage()
    throws ParseException
  {
    jj_consume_token(11);
    Token t = jj_consume_token(58);
    return ":" + unescape(image, false);
  }
  



  public final void fontFaceRule()
    throws ParseException
  {
    boolean start = false;
    try
    {
      jj_consume_token(32);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[38] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      jj_consume_token(6);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[39] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      start = true;handleStartFontFace(locator);
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 58: 
        declaration();
        break;
      
      default: 
        jj_la1[40] = jj_gen;
      }
      
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 10: 
          break;
        
        default: 
          jj_la1[41] = jj_gen;
          break;
        }
        jj_consume_token(10);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[42] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 58: 
          declaration();
          break;
        
        default: 
          jj_la1[43] = jj_gen;
        }
        
      }
      jj_consume_token(7);
    } catch (ParseException e) {
      throw toCSSParseException("invalidFontFaceRule", e);
    } finally {
      if (start) {
        handleEndFontFace();
      }
    }
    
    Locator locator;
  }
  

  public final LexicalUnit operator(LexicalUnit prev)
    throws ParseException
  {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 13: 
      jj_consume_token(13);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[44] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      return new LexicalUnitImpl(prev, (short)4);
    
    case 8: 
      jj_consume_token(8);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[45] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      return LexicalUnitImpl.createComma(prev);
    }
    
    jj_la1[46] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  



  public final char combinator()
    throws ParseException
  {
    char c = ' ';
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 14: 
      jj_consume_token(14);
      c = '+';
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[47] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
    

    case 17: 
      jj_consume_token(17);
      c = '>';
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[48] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
    

    case 1: 
      jj_consume_token(1);
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 14: 
      case 17: 
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 14: 
          jj_consume_token(14);
          c = '+';
          break;
        
        case 17: 
          jj_consume_token(17);
          c = '>';
          break;
        
        default: 
          jj_la1[49] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[50] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
      }
      
      
      jj_la1[51] = jj_gen;
      

      break;
    
    default: 
      jj_la1[52] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    return c;
  }
  



  public final char unaryOperator()
    throws ParseException
  {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 15: 
      jj_consume_token(15);
      return '-';
    
    case 14: 
      jj_consume_token(14);
      return '+';
    }
    
    jj_la1[53] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  




  public final String property()
    throws ParseException
  {
    Token t = jj_consume_token(58);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[54] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    return unescape(image, false);
  }
  



  public final void styleRule()
    throws ParseException
  {
    org.w3c.css.sac.SelectorList selList = null;
    boolean start = false;
    try
    {
      Token t = token;
      selList = selectorList();
      jj_consume_token(6);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[55] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      start = true;
      handleStartSelector(selList, createLocator(next));
      styleDeclaration();
      jj_consume_token(7);
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      getErrorHandler().warning(createSkipWarning("ignoringRule", e));
      error_skipblock();
    } catch (ParseException e) {
      CSSParseException cpe = toCSSParseException("invalidStyleRule", e);
      getErrorHandler().error(cpe);
      getErrorHandler().warning(createSkipWarning("ignoringFollowingDeclarations", cpe));
      error_skipblock();
    } finally {
      if (start) {
        handleEndSelector(selList);
      }
    }
  }
  
  public final org.w3c.css.sac.SelectorList parseSelectorsInternal() throws ParseException
  {
    for (;;) {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[56] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    org.w3c.css.sac.SelectorList selectors = selectorList();
    jj_consume_token(0);
    return selectors;
  }
  
  public final org.w3c.css.sac.SelectorList selectorList() throws ParseException { SelectorListImpl selList = new SelectorListImpl();
    
    org.w3c.css.sac.Selector sel = selector();
    if ((sel instanceof Locatable))
    {
      selList.setLocator(((Locatable)sel).getLocator());
    }
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 8: 
        break;
      
      default: 
        jj_la1[57] = jj_gen;
        break;
      }
      jj_consume_token(8);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[58] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      selList.add(sel);
      sel = selector();
    }
    selList.add(sel);
    return selList;
  }
  



  public final org.w3c.css.sac.Selector selector()
    throws ParseException
  {
    try
    {
      org.w3c.css.sac.Selector sel = simpleSelector(null, ' ');
      

      while (jj_2_2(2))
      {



        char comb = combinator();
        sel = simpleSelector(sel, comb);
      }
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[59] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      handleSelector(sel);
      return sel;
    } catch (ParseException e) { throw toCSSParseException("invalidSelector", e);
    }
  }
  


  public final org.w3c.css.sac.Selector simpleSelector(org.w3c.css.sac.Selector sel, char comb)
    throws ParseException
  {
    org.w3c.css.sac.SimpleSelector simpleSel = null;
    Condition c = null;
    org.w3c.css.sac.SimpleSelector pseudoElementSel = null;
    Object o = null;
    try {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 12: 
      case 58: 
        simpleSel = elementName();
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 9: 
          case 11: 
          case 18: 
          case 20: 
            break;
          
          default: 
            jj_la1[60] = jj_gen;
            break;
          }
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
          case 20: 
            c = hash(c, null != pseudoElementSel);
            break;
          
          case 9: 
            c = _class(c, null != pseudoElementSel);
            break;
          
          case 18: 
            c = attrib(c, null != pseudoElementSel);
            break;
          
          case 11: 
            o = pseudo(c, null != pseudoElementSel);
            if ((o instanceof Condition)) {
              c = (Condition)o;
            } else {
              pseudoElementSel = (org.w3c.css.sac.SimpleSelector)o;
            }
            break;
          }
        }
        jj_la1[61] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      



      case 9: 
      case 11: 
      case 18: 
      case 20: 
        simpleSel = ((com.steadystate.css.parser.selectors.SelectorFactoryImpl)getSelectorFactory()).createSyntheticElementSelector();
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
          case 20: 
            c = hash(c, null != pseudoElementSel);
            break;
          
          case 9: 
            c = _class(c, null != pseudoElementSel);
            break;
          
          case 18: 
            c = attrib(c, null != pseudoElementSel);
            break;
          
          case 11: 
            o = pseudo(c, null != pseudoElementSel);
            if ((o instanceof Condition)) {
              c = (Condition)o;
            } else {
              pseudoElementSel = (org.w3c.css.sac.SimpleSelector)o;
            }
            break;
          
          default: 
            jj_la1[62] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          }
          
        }
        



        jj_la1[63] = jj_gen;
        break;
      



      default: 
        jj_la1[64] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      if (c != null) {
        simpleSel = getSelectorFactory().createConditionalSelector(simpleSel, c);
      }
      
      if (sel == null) {
        sel = simpleSel;
      } else {
        switch (comb) {
        case ' ': 
          sel = getSelectorFactory().createDescendantSelector(sel, simpleSel);
          break;
        case '+': 
          sel = getSelectorFactory().createDirectAdjacentSelector(sel.getSelectorType(), sel, simpleSel);
          break;
        case '>': 
          sel = getSelectorFactory().createChildSelector(sel, simpleSel);
        }
        
      }
      if (pseudoElementSel != null) {}
      
      return getSelectorFactory().createDescendantSelector(sel, pseudoElementSel);
    }
    catch (ParseException e)
    {
      throw toCSSParseException("invalidSimpleSelector", e);
    }
  }
  




  public final Condition _class(Condition pred, boolean pseudoElementFound)
    throws ParseException
  {
    ParseException pe = null;
    try {
      if (pseudoElementFound) pe = generateParseException();
      jj_consume_token(9);
      Locator locator = createLocator(token);
      Token t = jj_consume_token(58);
      if (pseudoElementFound) throw pe;
      Condition c = getConditionFactory().createClassCondition(null, image);
      if ((c instanceof Locatable))
      {
        ((Locatable)c).setLocator(locator);
      }
      return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
    } catch (ParseException e) { throw toCSSParseException("invalidClassSelector", e);
    }
  }
  



  public final org.w3c.css.sac.SimpleSelector elementName()
    throws ParseException
  {
    try
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 58: 
        Token t = jj_consume_token(58);
        org.w3c.css.sac.SimpleSelector sel = getSelectorFactory().createElementSelector(null, unescape(image, false));
        if ((sel instanceof Locatable))
        {
          ((Locatable)sel).setLocator(createLocator(token));
        }
        return sel;
      
      case 12: 
        jj_consume_token(12);
        org.w3c.css.sac.SimpleSelector sel = getSelectorFactory().createElementSelector(null, null);
        if ((sel instanceof Locatable))
        {
          ((Locatable)sel).setLocator(createLocator(token));
        }
        return sel;
      }
      
      jj_la1[65] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    catch (ParseException e) {
      throw toCSSParseException("invalidElementName", e);
    }
  }
  




  public final Condition attrib(Condition pred, boolean pseudoElementFound)
    throws ParseException
  {
    String name = null;
    String value = null;
    int type = 0;
    try
    {
      jj_consume_token(18);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[66] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      if (pseudoElementFound) throw generateParseException();
      Token t = jj_consume_token(58);
      name = unescape(image, false);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[67] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 16: 
      case 27: 
      case 28: 
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 16: 
          jj_consume_token(16);
          type = 1;
          break;
        
        case 27: 
          jj_consume_token(27);
          type = 2;
          break;
        
        case 28: 
          jj_consume_token(28);
          type = 3;
          break;
        
        default: 
          jj_la1[68] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[69] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 58: 
          t = jj_consume_token(58);
          value = image;
          break;
        
        case 21: 
          t = jj_consume_token(21);
          value = unescape(image, false);
          break;
        
        default: 
          jj_la1[70] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[71] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
      }
      
      
      jj_la1[72] = jj_gen;
      

      jj_consume_token(19);
      Condition c = null;
      switch (type) {
      case 0: 
        c = getConditionFactory().createAttributeCondition(name, null, false, null);
        break;
      case 1: 
        c = getConditionFactory().createAttributeCondition(name, null, null != value, value);
        break;
      case 2: 
        c = getConditionFactory().createOneOfAttributeCondition(name, null, null != value, value);
        break;
      case 3: 
        c = getConditionFactory().createBeginHyphenAttributeCondition(name, null, null != value, value);
      }
      
      if ((c instanceof Locatable)) {
        ((Locatable)c).setLocator(locator);
      }
      return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
    } catch (ParseException e) { throw toCSSParseException("invalidAttrib", e);
    }
  }
  





  public final Object pseudo(Condition pred, boolean pseudoElementFound)
    throws ParseException
  {
    org.w3c.css.sac.SimpleSelector pseudoElementSel = null;
    Condition c = null;
    

    String arg = "";
    try
    {
      jj_consume_token(11);
      Locator locator = createLocator(token);
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 58: 
        Token t = jj_consume_token(58);
        String s = unescape(image, false);
        if (pseudoElementFound) throw toCSSParseException("duplicatePseudo", new String[] { s }, locator);
        if (("first-line".equals(s)) || 
          ("first-letter".equals(s)) || 
          ("before".equals(s)) || 
          ("after".equals(s)))
        {
          pseudoElementSel = getSelectorFactory().createPseudoElementSelector(null, s);
          if ((pseudoElementSel instanceof Locatable)) {
            ((Locatable)pseudoElementSel).setLocator(locator);
          }
          return pseudoElementSel; }
        c = getConditionFactory().createPseudoClassCondition(null, s);
        if ((c instanceof Locatable)) {
          ((Locatable)c).setLocator(locator);
        }
        if ("" != null)
        {
          return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
        }
        break;
      case 56: 
        Token t = jj_consume_token(56);
        String function = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[73] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        t = jj_consume_token(58);
        String lang = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[74] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        jj_consume_token(22);
        if (pseudoElementFound) throw toCSSParseException("duplicatePseudo", new String[] { "lang(" + lang + ")" }, locator);
        c = getConditionFactory().createLangCondition(lang);
        if ((c instanceof Locatable)) {
          ((Locatable)c).setLocator(locator);
        }
        if ("" != null)
        {
          return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
        }
        break;
      case 57: 
        Token t = jj_consume_token(57);
        String function = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[75] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        t = jj_consume_token(58);
        arg = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[76] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        jj_consume_token(22);
        if (pseudoElementFound) throw toCSSParseException("duplicatePseudo", new String[] { function + arg + ")" }, locator);
        c = getConditionFactory().createPseudoClassCondition(null, function + arg + ")");
        if ((c instanceof Locatable)) {
          ((Locatable)c).setLocator(locator);
        }
        if ("" != null)
        {
          return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
        }
        break;
      default: 
        jj_la1[77] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException(); }
    } catch (ParseException e) {
      Token t;
      throw toCSSParseException("invalidPseudo", e); }
    Locator locator;
    Token t; return null;
  }
  
  public final Condition hash(Condition pred, boolean pseudoElementFound) throws ParseException {
    ParseException pe = null;
    try {
      if (pseudoElementFound) pe = generateParseException();
      Token t = jj_consume_token(20);
      if (pseudoElementFound) throw pe;
      Condition c = getConditionFactory().createIdCondition(unescape(image.substring(1), false));
      if ((c instanceof Locatable))
      {
        ((Locatable)c).setLocator(createLocator(token));
      }
      return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
    } catch (ParseException e) { throw toCSSParseException("invalidHash", e);
    }
  }
  
  public final void styleDeclaration() throws ParseException {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 58: 
      declaration();
      break;
    
    default: 
      jj_la1[78] = jj_gen;
    }
    
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 10: 
        break;
      
      default: 
        jj_la1[79] = jj_gen;
        break;
      }
      jj_consume_token(10);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[80] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 58: 
        declaration();
        break;
      
      default: 
        jj_la1[81] = jj_gen;
      }
      
    }
  }
  





  public final void declaration()
    throws ParseException
  {
    boolean priority = false;
    Locator locator = null;
    try {
      String p = property();
      locator = createLocator(token);
      jj_consume_token(11);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[82] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      LexicalUnit e = expr();
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 35: 
        priority = prio();
        break;
      
      default: 
        jj_la1[83] = jj_gen;
      }
      
      handleProperty(p, e, priority, locator);
    } catch (CSSParseException ex) {
      getErrorHandler().error(ex);
      getErrorHandler().warning(createSkipWarning("ignoringFollowingDeclarations", ex));
      error_skipdecl();
    } catch (ParseException ex) {
      CSSParseException cpe = toCSSParseException("invalidDeclaration", ex);
      getErrorHandler().error(cpe);
      getErrorHandler().warning(createSkipWarning("ignoringFollowingDeclarations", cpe));
      error_skipdecl();
    }
  }
  


  public final boolean prio()
    throws ParseException
  {
    jj_consume_token(35);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[84] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    return true;
  }
  


  public final LexicalUnit expr()
    throws ParseException
  {
    try
    {
      LexicalUnit head = term(null);
      LexicalUnit body = head;
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 8: case 13: 
        case 14: case 15: 
        case 20: case 21: 
        case 24: case 36: 
        case 37: case 38: 
        case 39: case 40: 
        case 41: case 42: 
        case 43: case 44: 
        case 45: case 46: 
        case 47: case 48: 
        case 49: case 50: 
        case 51: 
        case 52: 
        case 53: 
        case 54: 
        case 55: 
        case 57: 
        case 58: 
        case 61: 
          break;
        case 9: case 10: 
        case 11: case 12: 
        case 16: case 17: 
        case 18: case 19: 
        case 22: case 23: 
        case 25: case 26: 
        case 27: case 28: 
        case 29: case 30: 
        case 31: case 32: 
        case 33: case 34: 
        case 35: case 56: 
        case 59: 
        case 60: 
        default: 
          jj_la1[85] = jj_gen;
          break;
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 8: 
        case 13: 
          body = operator(body);
          break;
        
        default: 
          jj_la1[86] = jj_gen;
        }
        
        body = term(body);
      }
      return head;
    } catch (ParseException ex) { throw toCSSParseException("invalidExpr", ex);
    }
  }
  






  public final LexicalUnit term(LexicalUnit prev)
    throws ParseException
  {
    char op = ' ';
    LexicalUnit value = null;
    Locator locator = null;
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 14: 
    case 15: 
      op = unaryOperator();
      break;
    
    default: 
      jj_la1[87] = jj_gen;
    }
    
    if (op != ' ')
    {
      locator = createLocator(token);
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 54: 
    case 57: 
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 54: 
        Token t = jj_consume_token(54);
        try
        {
          value = LexicalUnitImpl.createNumber(prev, intValue(op, image));
        }
        catch (NumberFormatException e)
        {
          value = LexicalUnitImpl.createNumber(prev, floatValue(op, image));
        }
      

      case 52: 
        Token t = jj_consume_token(52);
        value = LexicalUnitImpl.createPercentage(prev, floatValue(op, image));
        break;
      
      case 39: 
        Token t = jj_consume_token(39);
        value = LexicalUnitImpl.createPixel(prev, floatValue(op, image));
        break;
      
      case 40: 
        Token t = jj_consume_token(40);
        value = LexicalUnitImpl.createCentimeter(prev, floatValue(op, image));
        break;
      
      case 41: 
        Token t = jj_consume_token(41);
        value = LexicalUnitImpl.createMillimeter(prev, floatValue(op, image));
        break;
      
      case 42: 
        Token t = jj_consume_token(42);
        value = LexicalUnitImpl.createInch(prev, floatValue(op, image));
        break;
      
      case 43: 
        Token t = jj_consume_token(43);
        value = LexicalUnitImpl.createPoint(prev, floatValue(op, image));
        break;
      
      case 44: 
        Token t = jj_consume_token(44);
        value = LexicalUnitImpl.createPica(prev, floatValue(op, image));
        break;
      
      case 37: 
        Token t = jj_consume_token(37);
        value = LexicalUnitImpl.createEm(prev, floatValue(op, image));
        break;
      
      case 38: 
        Token t = jj_consume_token(38);
        value = LexicalUnitImpl.createEx(prev, floatValue(op, image));
        break;
      
      case 45: 
        Token t = jj_consume_token(45);
        value = LexicalUnitImpl.createDegree(prev, floatValue(op, image));
        break;
      
      case 46: 
        Token t = jj_consume_token(46);
        value = LexicalUnitImpl.createRadian(prev, floatValue(op, image));
        break;
      
      case 47: 
        Token t = jj_consume_token(47);
        value = LexicalUnitImpl.createGradian(prev, floatValue(op, image));
        break;
      
      case 48: 
        Token t = jj_consume_token(48);
        value = LexicalUnitImpl.createMillisecond(prev, floatValue(op, image));
        break;
      
      case 49: 
        Token t = jj_consume_token(49);
        value = LexicalUnitImpl.createSecond(prev, floatValue(op, image));
        break;
      
      case 50: 
        Token t = jj_consume_token(50);
        value = LexicalUnitImpl.createHertz(prev, floatValue(op, image));
        break;
      
      case 51: 
        Token t = jj_consume_token(51);
        value = LexicalUnitImpl.createKiloHertz(prev, floatValue(op, image));
        break;
      
      case 57: 
        value = function(prev);
        break;
      case 53: case 55: 
      case 56: default: 
        jj_la1[88] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      
      break;
    case 21: 
      Token t = jj_consume_token(21);
      value = LexicalUnitImpl.createString(prev, unescape(image, false));
      break;
    
    case 58: 
      Token t = jj_consume_token(58);
      value = LexicalUnitImpl.createIdent(prev, unescape(image, false));
      break;
    
    case 24: 
      Token t = jj_consume_token(24);
      value = LexicalUnitImpl.createURI(prev, image);
      break;
    
    case 61: 
      Token t = jj_consume_token(61);
      value = new LexicalUnitImpl(prev, (short)39, image);
      break;
    
    case 55: 
      value = rgb(prev);
      break;
    
    case 20: 
      value = hexcolor(prev);
      break;
    
    case 53: 
      Token t = jj_consume_token(53);
      int n = getLastNumPos(image);
      value = LexicalUnitImpl.createDimension(prev, 
      
        floatValue(op, image.substring(0, n + 1)), image
        .substring(n + 1));
      break;
    
    case 36: 
      Token t = jj_consume_token(36);
      value = new LexicalUnitImpl(prev, (short)12, image);
      break;
    case 22: case 23: case 25: case 26: case 27: case 28: case 29: case 30: case 31: 
    case 32: case 33: case 34: case 35: case 56: case 59: case 60: default: 
      jj_la1[89] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    if (locator == null)
    {
      locator = createLocator(token);
    }
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[90] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    if ((value instanceof Locatable))
    {
      ((Locatable)value).setLocator(locator);
    }
    return value;
  }
  




  public final LexicalUnit function(LexicalUnit prev)
    throws ParseException
  {
    Token t = jj_consume_token(57);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[91] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    LexicalUnit params = expr();
    jj_consume_token(22);
    return functionInternal(prev, image, params);
  }
  



  public final LexicalUnit rgb(LexicalUnit prev)
    throws ParseException
  {
    jj_consume_token(55);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[92] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    LexicalUnit params = expr();
    jj_consume_token(22);
    return LexicalUnitImpl.createRgbColor(prev, params);
  }
  



  public final LexicalUnit hexcolor(LexicalUnit prev)
    throws ParseException
  {
    Token t = jj_consume_token(20);
    return hexcolorInternal(prev, t);
  }
  
  String skip() throws ParseException { StringBuilder sb = new StringBuilder();
    int nesting = 0;
    Token t = getToken(0);
    if (image != null) {
      sb.append(image);
    }
    do
    {
      t = getNextToken();
      if (kind == 0) {
        break;
      }
      sb.append(image);
      appendUnit(t, sb);
      
      if (kind == 6) {
        nesting++;
      }
      else if (kind == 7) {
        nesting--;
      }
      
    } while (((kind != 7) && (kind != 10)) || (nesting > 0));
    
    return sb.toString();
  }
  
  void appendUnit(Token t, StringBuilder sb) throws ParseException { if (kind == 37) {
      sb.append("ems");
      return;
    }
    if (kind == 38) {
      sb.append("ex");
      return;
    }
    if (kind == 39) {
      sb.append("px");
      return;
    }
    if (kind == 40) {
      sb.append("cm");
      return;
    }
    if (kind == 41) {
      sb.append("mm");
      return;
    }
    if (kind == 42) {
      sb.append("in");
      return;
    }
    if (kind == 43) {
      sb.append("pt");
      return;
    }
    if (kind == 44) {
      sb.append("pc");
      return;
    }
    if (kind == 45) {
      sb.append("deg");
      return;
    }
    if (kind == 46) {
      sb.append("rad");
      return;
    }
    if (kind == 47) {
      sb.append("grad");
      return;
    }
    if (kind == 48) {
      sb.append("ms");
      return;
    }
    if (kind == 49) {
      sb.append('s');
      return;
    }
    if (kind == 50) {
      sb.append("hz");
      return;
    }
    if (kind == 51) {
      sb.append("khz");
      return;
    }
    if (kind == 52) {
      sb.append('%');
      return;
    }
  }
  
  void error_skipblock() throws ParseException {
    int nesting = 0;
    Token t;
    do { t = getNextToken();
      if (kind == 6) {
        nesting++;
      }
      else if (kind == 7) {
        nesting--;
      }
      
    } while ((kind != 0) && ((kind != 7) || (nesting > 0)));
  }
  
  void error_skipdecl() throws ParseException { Token t = getToken(1);
    if (kind == 6) {
      error_skipblock();
      return;
    }
    if (kind == 7)
    {
      return;
    }
    
    Token oldToken = token;
    while ((kind != 10) && (kind != 7) && (kind != 0)) {
      oldToken = t;
      t = getNextToken();
    }
    if (kind != 0)
      token = oldToken;
  }
  
  void error_skipAtRule() throws ParseException {
    Token t = null;
    do {
      t = getNextToken();
    }
    while ((kind != 10) && (kind != 0));
  }
  
  private boolean jj_2_1(int xla)
  {
    jj_la = xla;jj_lastpos = (this.jj_scanpos = token);
    try { return !jj_3_1();
    } catch (LookaheadSuccess ls) { return true;
    } finally { jj_save(0, xla);
    }
  }
  
  private boolean jj_2_2(int xla) {
    jj_la = xla;jj_lastpos = (this.jj_scanpos = token);
    try { return !jj_3_2();
    } catch (LookaheadSuccess ls) { return true;
    } finally { jj_save(1, xla);
    }
  }
  
  private boolean jj_3R_79() {
    if (jj_scan_token(18)) return true;
    return false;
  }
  
  private boolean jj_3R_64()
  {
    if (jj_3R_67()) return true;
    return false;
  }
  
  private boolean jj_3R_69()
  {
    if (jj_scan_token(14)) return true;
    return false;
  }
  

  private boolean jj_3R_66()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_69()) {
      jj_scanpos = xsp;
      if (jj_3R_70()) return true;
    }
    return false;
  }
  

  private boolean jj_3R_60()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_64()) {
      jj_scanpos = xsp;
      if (jj_3R_65()) return true;
    }
    return false;
  }
  
  private boolean jj_3R_80()
  {
    if (jj_scan_token(11)) return true;
    return false;
  }
  
  private boolean jj_3R_63()
  {
    if (jj_scan_token(1)) { return true;
    }
    Token xsp = jj_scanpos;
    if (jj_3R_66()) jj_scanpos = xsp;
    return false;
  }
  
  private boolean jj_3R_62()
  {
    if (jj_scan_token(17)) return true;
    Token xsp;
    do {
      xsp = jj_scanpos;
    } while (!jj_scan_token(1)); jj_scanpos = xsp;
    
    return false;
  }
  
  private boolean jj_3_1()
  {
    if (jj_scan_token(58)) return true;
    if (jj_3R_58()) return true;
    return false;
  }
  
  private boolean jj_3R_61()
  {
    if (jj_scan_token(14)) return true;
    Token xsp;
    do {
      xsp = jj_scanpos;
    } while (!jj_scan_token(1)); jj_scanpos = xsp;
    
    return false;
  }
  
  private boolean jj_3R_72()
  {
    if (jj_scan_token(12)) return true;
    return false;
  }
  
  private boolean jj_3R_76()
  {
    if (jj_3R_80()) return true;
    return false;
  }
  

  private boolean jj_3R_59()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_61()) {
      jj_scanpos = xsp;
      if (jj_3R_62()) {
        jj_scanpos = xsp;
        if (jj_3R_63()) return true;
      }
    }
    return false;
  }
  
  private boolean jj_3R_75()
  {
    if (jj_3R_79()) return true;
    return false;
  }
  
  private boolean jj_3R_78()
  {
    if (jj_scan_token(9)) return true;
    return false;
  }
  
  private boolean jj_3R_74()
  {
    if (jj_3R_78()) return true;
    return false;
  }
  
  private boolean jj_3R_73()
  {
    if (jj_3R_77()) return true;
    return false;
  }
  

  private boolean jj_3R_68()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_73()) {
      jj_scanpos = xsp;
      if (jj_3R_74()) {
        jj_scanpos = xsp;
        if (jj_3R_75()) {
          jj_scanpos = xsp;
          if (jj_3R_76()) return true;
        }
      }
    }
    return false;
  }
  
  private boolean jj_3_2()
  {
    if (jj_3R_59()) return true;
    if (jj_3R_60()) return true;
    return false;
  }
  
  private boolean jj_3R_71()
  {
    if (jj_scan_token(58)) return true;
    return false;
  }
  
  private boolean jj_3R_77()
  {
    if (jj_scan_token(20)) return true;
    return false;
  }
  

  private boolean jj_3R_65()
  {
    if (jj_3R_68()) return true;
    Token xsp;
    do { xsp = jj_scanpos;
    } while (!jj_3R_68()); jj_scanpos = xsp;
    
    return false;
  }
  
  private boolean jj_3R_70()
  {
    if (jj_scan_token(17)) return true;
    return false;
  }
  
  private boolean jj_3R_58()
  {
    if (jj_scan_token(11)) return true;
    return false;
  }
  

  private boolean jj_3R_67()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_71()) {
      jj_scanpos = xsp;
      if (jj_3R_72()) return true;
    }
    return false;
  }
  










  private final int[] jj_la1 = new int[93];
  private static int[] jj_la1_0;
  private static int[] jj_la1_1;
  private static int[] jj_la1_2;
  
  static { jj_la1_init_0();
    jj_la1_init_1();
    jj_la1_init_2();
  }
  
  private static void jj_la1_init_0() { jj_la1_0 = new int[] { 100663298, 100663298, 100663298, 100663298, 0, -535553536, -1072424448, -535553536, 100663298, 100663298, 2, -535553536, 2, 2, 2, 18874368, 2, 0, 2, 2, 1075059200, 256, 2, 1075059200, 2, 1075059200, 2, 2, 2, 2, 2, 2048, 2048, 2, 0, 1024, 2, 0, 2, 2, 0, 1024, 2, 0, 2, 2, 8448, 2, 2, 147456, 2, 147456, 147458, 49152, 2, 2, 2, 256, 2, 2, 1313280, 1313280, 1313280, 1313280, 1317376, 4096, 2, 2, 402718720, 2, 2097152, 2, 402718720, 2, 2, 2, 2, 0, 0, 1024, 2, 0, 2, 0, 2, 19980544, 8448, 49152, 0, 19922944, 2, 2, 2 }; }
  
  private static void jj_la1_init_1() {
    jj_la1_1 = new int[] { 0, 0, 0, 0, 2, 67108869, 67108869, 67108869, 0, 0, 0, 67108871, 0, 0, 0, 0, 0, 67108864, 0, 0, 67108868, 0, 0, 67108868, 0, 67108868, 0, 0, 0, 0, 0, 67108864, 67108864, 0, 67108864, 0, 0, 67108864, 0, 0, 67108864, 0, 0, 67108864, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67108864, 67108864, 0, 0, 0, 0, 67108864, 0, 0, 0, 0, 0, 0, 117440512, 67108864, 0, 0, 67108864, 0, 8, 0, 654311408, 0, 0, 39845856, 654311408, 0, 0, 0 };
  }
  
  private static void jj_la1_init_2() { jj_la1_2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; }
  
  private final JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;
  
  public SACParserCSS2(CharStream stream)
  {
    token_source = new SACParserCSS2TokenManager(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 93; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public void ReInit(CharStream stream)
  {
    token_source.ReInit(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 93; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public SACParserCSS2(SACParserCSS2TokenManager tm)
  {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 93; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public void ReInit(SACParserCSS2TokenManager tm)
  {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 93; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if (token).next != null) token = token.next; else
      token = (token.next = token_source.getNextToken());
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen += 1;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (gen < jj_gen) first = null;
            c = next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }
  


  private final LookaheadSuccess jj_ls = new LookaheadSuccess(null);
  
  private boolean jj_scan_token(int kind) { if (jj_scanpos == jj_lastpos) {
      jj_la -= 1;
      if (jj_scanpos.next == null) {
        jj_lastpos = (this.jj_scanpos = jj_scanpos.next = token_source.getNextToken());
      } else {
        jj_lastpos = (this.jj_scanpos = jj_scanpos.next);
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; for (Token tok = token; 
          (tok != null) && (tok != jj_scanpos); tok = next) i++;
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if ((jj_la == 0) && (jj_scanpos == jj_lastpos)) throw jj_ls;
    return false;
  }
  

  public final Token getNextToken()
  {
    if (token.next != null) token = token.next; else
      token = (token.next = token_source.getNextToken());
    jj_ntk = -1;
    jj_gen += 1;
    return token;
  }
  
  public final Token getToken(int index)
  {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (next != null) t = next; else
        t = t.next = token_source.getNextToken();
    }
    return t;
  }
  
  private int jj_ntk_f() {
    if ((this.jj_nt = token.next) == null) {
      return this.jj_ntk = token.next = token_source.getNextToken()).kind;
    }
    return this.jj_ntk = jj_nt.kind;
  }
  
  private java.util.List<int[]> jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;
  
  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) {
      return;
    }
    
    if (pos == jj_endpos + 1) {
      jj_lasttokens[(jj_endpos++)] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      
      for (int[] oldentry : jj_expentries) {
        if (oldentry.length == jj_expentry.length) {
          boolean isMatched = true;
          
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              isMatched = false;
              break;
            }
          }
          
          if (isMatched) {
            jj_expentries.add(jj_expentry);
            break;
          }
        }
      }
      
      if (pos != 0) {
        int tmp201_200 = pos;jj_endpos = tmp201_200;jj_lasttokens[(tmp201_200 - 1)] = kind;
      }
    }
  }
  
  public ParseException generateParseException()
  {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[80];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 93; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & 1 << j) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & 1 << j) != 0) {
            la1tokens[(32 + j)] = true;
          }
          if ((jj_la1_2[i] & 1 << j) != 0) {
            la1tokens[(64 + j)] = true;
          }
        }
      }
    }
    for (int i = 0; i < 80; i++) {
      if (la1tokens[i] != 0) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = ((int[])jj_expentries.get(i));
    }
    return new ParseException(token, exptokseq, tokenImage);
  }
  







  private void jj_rescan_token()
  {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
      try {
        JJCalls p = jj_2_rtns[i];
        do
        {
          if (gen > jj_gen) {
            jj_la = arg;jj_lastpos = (this.jj_scanpos = first);
            switch (i) {
            case 0:  jj_3_1(); break;
            case 1:  jj_3_2();
            }
          }
          p = next;
        } while (p != null);
      }
      catch (LookaheadSuccess ls) {}
    }
    jj_rescan = false;
  }
  
  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (gen > jj_gen) {
      if (next == null) { p = p.next = new JJCalls(); break; }
      p = next;
    }
    
    gen = (jj_gen + xla - jj_la);
    first = token;
    arg = xla;
  }
  
  public final void enable_tracing() {}
  
  public final void disable_tracing() {}
  
  static final class JJCalls
  {
    int gen;
    Token first;
    int arg;
    JJCalls next;
    
    JJCalls() {}
  }
  
  private static final class LookaheadSuccess
    extends Error
  {
    private LookaheadSuccess() {}
  }
}
