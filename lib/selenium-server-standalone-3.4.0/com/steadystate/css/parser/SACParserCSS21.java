package com.steadystate.css.parser;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SelectorFactory;

public class SACParserCSS21 extends AbstractSACParser implements SACParserCSS21Constants
{
  public SACParserCSS21TokenManager token_source;
  public Token token;
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos;
  private Token jj_lastpos;
  private int jj_la;
  private int jj_gen;
  
  public SACParserCSS21()
  {
    this((CharStream)null);
  }
  
  public String getParserVersion() {
    return "http://www.w3.org/TR/CSS21/";
  }
  
  protected String getGrammarUri()
  {
    return "http://www.w3.org/TR/CSS21/grammar.html";
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
      case 42: 
      case 43: 
        break;
      
      default: 
        jj_la1[0] = jj_gen;
        break;
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 1: 
        jj_consume_token(1);
        break;
      
      case 42: 
        jj_consume_token(42);
        break;
      
      case 43: 
        jj_consume_token(43);
      }
      
    }
    jj_la1[1] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
    

    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 66: 
      charsetRule();
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
        case 42: 
        case 43: 
          break;
        
        default: 
          jj_la1[2] = jj_gen;
          break;
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 1: 
          jj_consume_token(1);
          break;
        
        case 42: 
          jj_consume_token(42);
          break;
        
        case 43: 
          jj_consume_token(43);
        }
        
      }
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    
    


    jj_la1[4] = jj_gen;
    




    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 19: 
    case 50: 
    case 52: 
    case 53: 
    case 57: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 88: 
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 63: 
        importRule(ruleFound);
        break;
      
      case 19: 
      case 50: 
      case 52: 
      case 53: 
      case 57: 
      case 62: 
      case 64: 
      case 65: 
      case 88: 
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 19: 
        case 50: 
        case 52: 
        case 53: 
        case 57: 
        case 62: 
          styleRule();
          break;
        
        case 65: 
          mediaRule();
          break;
        
        case 64: 
          pageRule();
          break;
        
        case 88: 
          unknownAtRule();
          break;
        
        default: 
          jj_la1[5] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        ruleFound = true;
        break;
      
      default: 
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      
      break;
    default: 
      jj_la1[7] = jj_gen;
      ParseException e = generateParseException();
      invalidRule();
      Token t = getNextToken();
      
      boolean charsetProcessed = false;
      if (kind == 66) {
        t = getNextToken();
        if (kind == 1) {
          t = getNextToken();
          if (kind == 22) {
            t = getNextToken();
            if (kind == 51) {
              getNextToken();
              charsetProcessed = true;
            }
          }
        }
        CSSParseException cpe = toCSSParseException("misplacedCharsetRule", e);
        getErrorHandler().error(cpe);
        getErrorHandler().warning(createSkipWarning("ignoringRule", cpe));
      }
      
      if (!charsetProcessed) {
        if ((kind == 0) && 
          ("" != null)) { return;
        }
        
        CSSParseException cpe = toCSSParseException("invalidRule", e);
        getErrorHandler().error(cpe);
        getErrorHandler().warning(createSkipWarning("ignoringRule", cpe));
        while ((kind != 47) && (kind != 0)) {
          t = getNextToken();
        }
        if ((kind == 0) && 
          ("" != null))
          return;
      }
      break;
    }
    for (;;) {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
      case 42: 
      case 43: 
        break;
      
      default: 
        jj_la1[8] = jj_gen;
        break;
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 1: 
        jj_consume_token(1);
        break;
      
      case 42: 
        jj_consume_token(42);
        break;
      
      case 43: 
        jj_consume_token(43);
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
    case 66: 
      charsetRule();
      break;
    
    case 63: 
      importRule(false);
      break;
    
    case 19: 
    case 50: 
    case 52: 
    case 53: 
    case 57: 
    case 62: 
      styleRule();
      break;
    
    case 65: 
      mediaRule();
      break;
    
    case 64: 
      pageRule();
      break;
    
    case 88: 
      unknownAtRule();
      break;
    
    default: 
      jj_la1[11] = jj_gen;
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
        jj_la1[12] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
  }
  
  public final void charsetRule() throws ParseException
  {
    try {
      jj_consume_token(66);
      Locator locator = createLocator(token);
      jj_consume_token(1);
      Token t = jj_consume_token(22);
      jj_consume_token(51);
      handleCharset(t.toString(), locator);
    } catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidCharsetRule", e));
    }
  }
  
  public final void unknownAtRule() throws ParseException
  {
    try {
      jj_consume_token(88);
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
      ParseException e = null;
      if (nonImportRuleFoundBefore)
      {
        e = generateParseException();
      }
      jj_consume_token(63);
      Locator locator = createLocator(token);
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
        jj_consume_token(1); }
      Token t;
      Token t; switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 22: 
        t = jj_consume_token(22);
        break;
      
      case 85: 
        t = jj_consume_token(85);
        break;
      
      default: 
        jj_la1[14] = jj_gen;
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
          jj_la1[15] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 19: 
        mediaList(ml);
        break;
      
      default: 
        jj_la1[16] = jj_gen;
      }
      
      jj_consume_token(51);
      if (nonImportRuleFoundBefore)
      {
        getErrorHandler().error(toCSSParseException("invalidImportRuleIgnored2", e));
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
      jj_consume_token(65);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[17] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      mediaList(ml);
      start = true;
      handleStartMedia(ml, locator);
      jj_consume_token(46);
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
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 19: 
      case 50: 
      case 52: 
      case 53: 
      case 57: 
      case 62: 
      case 63: 
      case 64: 
      case 88: 
        mediaRuleList();
        break;
      
      default: 
        jj_la1[19] = jj_gen;
      }
      
      jj_consume_token(47);
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      getErrorHandler().warning(createSkipWarning("ignoringRule", e));
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
        case 61: 
          break;
        
        default: 
          jj_la1[20] = jj_gen;
          break;
        }
        jj_consume_token(61);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[21] = jj_gen;
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
      case 19: 
      case 50: 
      case 52: 
      case 53: 
      case 57: 
      case 62: 
        styleRule();
        break;
      
      case 64: 
        pageRule();
        break;
      
      case 63: 
        importRule(true);
        break;
      
      case 88: 
        unknownAtRule();
        break;
      
      default: 
        jj_la1[22] = jj_gen;
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
          jj_la1[23] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      }
      
    }
    








    jj_la1[24] = jj_gen;
  }
  







  public final String medium()
    throws ParseException
  {
    Token t = jj_consume_token(19);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[25] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    String medium = unescape(image, false);
    handleMedium(medium, createLocator(t));
    return medium;
  }
  



  public final void pageRule()
    throws ParseException
  {
    String s = null;
    boolean start = false;
    try
    {
      jj_consume_token(64);
      Locator locator = createLocator(token);
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
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 52: 
        s = pseudoPage();
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
      }
      
      
      jj_la1[28] = jj_gen;
      

      jj_consume_token(46);
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
      start = true;
      handleStartPage(null, s, locator);
      styleDeclaration();
      jj_consume_token(47);
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      getErrorHandler().warning(createSkipWarning("ignoringRule", e));
      error_skipblock();
    } catch (ParseException e) {
      CSSParseException cpe = toCSSParseException("invalidPageRule", e);
      getErrorHandler().error(cpe);
      getErrorHandler().warning(createSkipWarning("ignoringRule", cpe));
      error_skipblock();
    } finally {
      if (start) {
        handleEndPage(null, s);
      }
    }
  }
  



  public final String pseudoPage()
    throws ParseException
  {
    jj_consume_token(52);
    Token t = jj_consume_token(19);
    return ":" + unescape(image, false);
  }
  



  public final LexicalUnit operator(LexicalUnit prev)
    throws ParseException
  {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 54: 
      jj_consume_token(54);
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
      return new LexicalUnitImpl(prev, (short)4);
    
    case 61: 
      jj_consume_token(61);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[31] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      return LexicalUnitImpl.createComma(prev);
    }
    
    jj_la1[32] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  





  public final char combinator()
    throws ParseException
  {
    char c = ' ';
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 59: 
      jj_consume_token(59);
      c = '+';
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
    

    case 60: 
      jj_consume_token(60);
      c = '>';
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[34] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
    

    case 1: 
      jj_consume_token(1);
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 59: 
      case 60: 
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 59: 
          jj_consume_token(59);
          c = '+';
          break;
        
        case 60: 
          jj_consume_token(60);
          c = '>';
          break;
        
        default: 
          jj_la1[35] = jj_gen;
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
            jj_la1[36] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
      }
      
      
      jj_la1[37] = jj_gen;
      

      break;
    
    default: 
      jj_la1[38] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    return c;
  }
  



  public final char unaryOperator()
    throws ParseException
  {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 55: 
      jj_consume_token(55);
      return '-';
    
    case 59: 
      jj_consume_token(59);
      return '+';
    }
    
    jj_la1[39] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  




  public final String property()
    throws ParseException
  {
    Token t = jj_consume_token(19);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[40] = jj_gen;
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
      jj_consume_token(46);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[41] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      start = true;
      handleStartSelector(selList, createLocator(next));
      styleDeclaration();
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 47: 
        jj_consume_token(47);
        break;
      
      case 0: 
        jj_consume_token(0);
        break;
      
      default: 
        jj_la1[42] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
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
        jj_la1[43] = jj_gen;
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
      case 61: 
        break;
      
      default: 
        jj_la1[44] = jj_gen;
        break;
      }
      jj_consume_token(61);
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
      

      while (jj_2_1(2))
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
          jj_la1[46] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
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
      case 19: 
      case 53: 
        simpleSel = elementName();
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 50: 
          case 52: 
          case 57: 
          case 62: 
            break;
          
          default: 
            jj_la1[47] = jj_gen;
            break;
          }
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
          case 62: 
            c = hash(c, null != pseudoElementSel);
            break;
          
          case 50: 
            c = _class(c, null != pseudoElementSel);
            break;
          
          case 57: 
            c = attrib(c, null != pseudoElementSel);
            break;
          
          case 52: 
            o = pseudo(c, null != pseudoElementSel);
            if ((o instanceof Condition)) {
              c = (Condition)o;
            } else {
              pseudoElementSel = (org.w3c.css.sac.SimpleSelector)o;
            }
            break;
          }
        }
        jj_la1[48] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      



      case 50: 
      case 52: 
      case 57: 
      case 62: 
        simpleSel = ((com.steadystate.css.parser.selectors.SelectorFactoryImpl)getSelectorFactory()).createSyntheticElementSelector();
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
          case 62: 
            c = hash(c, null != pseudoElementSel);
            break;
          
          case 50: 
            c = _class(c, null != pseudoElementSel);
            break;
          
          case 57: 
            c = attrib(c, null != pseudoElementSel);
            break;
          
          case 52: 
            o = pseudo(c, null != pseudoElementSel);
            if ((o instanceof Condition)) {
              c = (Condition)o;
            } else {
              pseudoElementSel = (org.w3c.css.sac.SimpleSelector)o;
            }
            break;
          
          default: 
            jj_la1[49] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          }
          
        }
        



        jj_la1[50] = jj_gen;
        break;
      



      default: 
        jj_la1[51] = jj_gen;
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
      jj_consume_token(50);
      Locator locator = createLocator(token);
      Token t = jj_consume_token(19);
      if (pseudoElementFound) throw pe;
      Condition c = getConditionFactory().createClassCondition(null, unescape(image, false));
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
      case 19: 
        Token t = jj_consume_token(19);
        org.w3c.css.sac.SimpleSelector sel = getSelectorFactory().createElementSelector(null, unescape(image, false));
        if ((sel instanceof Locatable))
        {
          ((Locatable)sel).setLocator(createLocator(token));
        }
        return sel;
      
      case 53: 
        jj_consume_token(53);
        org.w3c.css.sac.SimpleSelector sel = getSelectorFactory().createElementSelector(null, null);
        if ((sel instanceof Locatable))
        {
          ((Locatable)sel).setLocator(createLocator(token));
        }
        return sel;
      }
      
      jj_la1[52] = jj_gen;
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
      jj_consume_token(57);
      Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[53] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      if (pseudoElementFound) throw generateParseException();
      Token t = jj_consume_token(19);
      name = unescape(image, false);
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
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 44: 
      case 45: 
      case 56: 
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 56: 
          jj_consume_token(56);
          type = 1;
          break;
        
        case 44: 
          jj_consume_token(44);
          type = 2;
          break;
        
        case 45: 
          jj_consume_token(45);
          type = 3;
          break;
        
        default: 
          jj_la1[55] = jj_gen;
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
            jj_la1[56] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 19: 
          t = jj_consume_token(19);
          value = unescape(image, false);
          break;
        
        case 22: 
          t = jj_consume_token(22);
          value = unescape(image, false);
          break;
        
        default: 
          jj_la1[57] = jj_gen;
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
            jj_la1[58] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
      }
      
      
      jj_la1[59] = jj_gen;
      

      jj_consume_token(58);
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
      jj_consume_token(52);
      Locator locator = createLocator(token);
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 19: 
        Token t = jj_consume_token(19);
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
      case 86: 
        Token t = jj_consume_token(86);
        String function = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[60] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        t = jj_consume_token(19);
        String lang = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[61] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        jj_consume_token(49);
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
      case 87: 
        Token t = jj_consume_token(87);
        String function = unescape(image, false);
        for (;;)
        {
          switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
          {
          case 1: 
            break;
          
          default: 
            jj_la1[62] = jj_gen;
            break;
          }
          jj_consume_token(1);
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 19: 
          t = jj_consume_token(19);
          arg = unescape(image, false);
          for (;;)
          {
            switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
            {
            case 1: 
              break;
            
            default: 
              jj_la1[63] = jj_gen;
              break;
            }
            jj_consume_token(1);
          }
        }
        
        
        jj_la1[64] = jj_gen;
        

        jj_consume_token(49);
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
        jj_la1[65] = jj_gen;
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
      Token t = jj_consume_token(62);
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
    case 19: 
    case 53: 
      declaration();
      break;
    
    default: 
      jj_la1[66] = jj_gen;
    }
    
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 51: 
        break;
      
      default: 
        jj_la1[67] = jj_gen;
        break;
      }
      jj_consume_token(51);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[68] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 19: 
      case 53: 
        declaration();
        break;
      
      default: 
        jj_la1[69] = jj_gen;
      }
      
    }
  }
  






  public final void declaration()
    throws ParseException
  {
    boolean priority = false;
    Locator starHack = null;
    Locator locator = null;
    try {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 53: 
        jj_consume_token(53);
        starHack = createLocator(token);
        break;
      
      default: 
        jj_la1[70] = jj_gen;
      }
      
      String p = property();
      locator = createLocator(token);
      jj_consume_token(52);
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
      LexicalUnit e = expr();
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 67: 
        priority = prio();
        break;
      
      default: 
        jj_la1[72] = jj_gen;
      }
      
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 89: 
        Token t = jj_consume_token(89);
        locator = createLocator(token);
        CSSParseException cpe = toCSSParseException("invalidDeclarationInvalidChar", new String[] { image }, locator);
        getErrorHandler().error(cpe);
        error_skipdecl();
        break;
      
      default: 
        jj_la1[73] = jj_gen;
      }
      
      if (starHack != null)
      {
        if (isIeStarHackAccepted()) {
          handleProperty("*" + p, e, priority, locator);
          if ("" != null) return;
        }
        CSSParseException cpe = toCSSParseException("invalidDeclarationStarHack", new Object[0], starHack);
        getErrorHandler().error(cpe);
        if ("" != null) return;
      }
      handleProperty(p, e, priority, locator);
    } catch (CSSParseException ex) {
      getErrorHandler().error(ex);
      error_skipdecl();
    } catch (ParseException ex) {
      CSSParseException cpe = toCSSParseException("invalidDeclaration", ex);
      getErrorHandler().error(cpe);
      error_skipdecl();
    }
  }
  


  public final boolean prio()
    throws ParseException
  {
    jj_consume_token(67);
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
        case 17: case 18: case 19: case 22: 
        case 54: case 55: case 59: case 61: 
        case 62: case 68: case 69: case 70: 
        case 71: case 72: case 73: case 74: 
        case 75: case 76: case 77: case 78: 
        case 79: case 80: case 81: 
        case 82: case 83: case 84: 
        case 85: case 87: case 90: 
          break;
        case 20: case 21: 
        case 23: case 24: 
        case 25: case 26: 
        case 27: case 28: 
        case 29: case 30: 
        case 31: case 32: 
        case 33: case 34: 
        case 35: case 36: 
        case 37: case 38: 
        case 39: case 40: 
        case 41: case 42: 
        case 43: case 44: 
        case 45: case 46: 
        case 47: case 48: 
        case 49: case 50: 
        case 51: case 52: 
        case 53: case 56: 
        case 57: case 58: 
        case 60: case 63: 
        case 64: case 65: 
        case 66: case 67: 
        case 86: case 88: 
        case 89: 
        default: 
          jj_la1[75] = jj_gen;
          break;
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 54: 
        case 61: 
          body = operator(body);
          break;
        
        default: 
          jj_la1[76] = jj_gen;
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
    case 55: 
    case 59: 
      op = unaryOperator();
      break;
    
    default: 
      jj_la1[77] = jj_gen;
    }
    
    if (op != ' ')
    {
      locator = createLocator(token);
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 17: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 87: 
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 17: 
        Token t = jj_consume_token(17);
        try
        {
          value = LexicalUnitImpl.createNumber(prev, intValue(op, image));
        }
        catch (NumberFormatException e)
        {
          value = LexicalUnitImpl.createNumber(prev, floatValue(op, image));
        }
      

      case 83: 
        Token t = jj_consume_token(83);
        value = LexicalUnitImpl.createPercentage(prev, floatValue(op, image));
        break;
      
      case 70: 
        Token t = jj_consume_token(70);
        value = LexicalUnitImpl.createPixel(prev, floatValue(op, image));
        break;
      
      case 71: 
        Token t = jj_consume_token(71);
        value = LexicalUnitImpl.createCentimeter(prev, floatValue(op, image));
        break;
      
      case 72: 
        Token t = jj_consume_token(72);
        value = LexicalUnitImpl.createMillimeter(prev, floatValue(op, image));
        break;
      
      case 73: 
        Token t = jj_consume_token(73);
        value = LexicalUnitImpl.createInch(prev, floatValue(op, image));
        break;
      
      case 74: 
        Token t = jj_consume_token(74);
        value = LexicalUnitImpl.createPoint(prev, floatValue(op, image));
        break;
      
      case 75: 
        Token t = jj_consume_token(75);
        value = LexicalUnitImpl.createPica(prev, floatValue(op, image));
        break;
      
      case 68: 
        Token t = jj_consume_token(68);
        value = LexicalUnitImpl.createEm(prev, floatValue(op, image));
        break;
      
      case 69: 
        Token t = jj_consume_token(69);
        value = LexicalUnitImpl.createEx(prev, floatValue(op, image));
        break;
      
      case 76: 
        Token t = jj_consume_token(76);
        value = LexicalUnitImpl.createDegree(prev, floatValue(op, image));
        break;
      
      case 77: 
        Token t = jj_consume_token(77);
        value = LexicalUnitImpl.createRadian(prev, floatValue(op, image));
        break;
      
      case 78: 
        Token t = jj_consume_token(78);
        value = LexicalUnitImpl.createGradian(prev, floatValue(op, image));
        break;
      
      case 79: 
        Token t = jj_consume_token(79);
        value = LexicalUnitImpl.createMillisecond(prev, floatValue(op, image));
        break;
      
      case 80: 
        Token t = jj_consume_token(80);
        value = LexicalUnitImpl.createSecond(prev, floatValue(op, image));
        break;
      
      case 81: 
        Token t = jj_consume_token(81);
        value = LexicalUnitImpl.createHertz(prev, floatValue(op, image));
        break;
      
      case 82: 
        Token t = jj_consume_token(82);
        value = LexicalUnitImpl.createKiloHertz(prev, floatValue(op, image));
        break;
      
      case 87: 
        value = function(prev);
        break;
      case 18: case 19: case 20: case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: 
      case 45: case 46: case 47: case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 84: case 85: case 86: default: 
        jj_la1[78] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      
      break;
    case 22: 
      Token t = jj_consume_token(22);
      value = LexicalUnitImpl.createString(prev, unescape(image, false));
      break;
    
    case 90: 
      Token t = jj_consume_token(90);
      value = LexicalUnitImpl.createIdent(prev, skipUnit().trim());
      break;
    
    case 19: 
      Token t = jj_consume_token(19);
      value = LexicalUnitImpl.createIdent(prev, unescape(image, false));
      break;
    
    case 85: 
      Token t = jj_consume_token(85);
      value = LexicalUnitImpl.createURI(prev, unescape(image, true));
      break;
    
    case 62: 
      value = hexcolor(prev);
      break;
    
    case 84: 
      Token t = jj_consume_token(84);
      int n = getLastNumPos(image);
      value = LexicalUnitImpl.createDimension(prev, 
      
        floatValue(op, image.substring(0, n + 1)), image
        .substring(n + 1));
      break;
    
    case 18: 
      Token t = jj_consume_token(18);
      value = new LexicalUnitImpl(prev, (short)12, image);
      break;
    case 20: case 21: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: case 45: 
    case 46: case 47: case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: case 58: case 59: case 60: case 61: case 63: case 64: case 65: case 66: case 67: case 86: case 88: case 89: default: 
      jj_la1[79] = jj_gen;
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
        jj_la1[80] = jj_gen;
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
    Token t = jj_consume_token(87);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[81] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    LexicalUnit params = expr();
    jj_consume_token(49);
    return functionInternal(prev, unescape(image, true), params);
  }
  



  public final LexicalUnit hexcolor(LexicalUnit prev)
    throws ParseException
  {
    Token t = jj_consume_token(62);
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
      
      if (kind == 46) {
        nesting++;
      }
      else if (kind == 47) {
        nesting--;
      }
      
    } while (((kind != 47) && (kind != 51)) || (nesting > 0));
    
    return sb.toString();
  }
  
  String skipUnit() throws ParseException { StringBuilder sb = new StringBuilder();
    
    Token t = token;
    Token oldToken = null;
    while ((kind != 51) && (kind != 47) && (kind != 0)) {
      oldToken = t;
      sb.append(image);
      appendUnit(t, sb);
      
      t = getNextToken();
    }
    if (kind != 0) {
      token = oldToken;
    }
    
    return sb.toString();
  }
  
  void appendUnit(Token t, StringBuilder sb) throws ParseException { if (kind == 68) {
      sb.append("ems");
      return;
    }
    if (kind == 69) {
      sb.append("ex");
      return;
    }
    if (kind == 70) {
      sb.append("px");
      return;
    }
    if (kind == 71) {
      sb.append("cm");
      return;
    }
    if (kind == 72) {
      sb.append("mm");
      return;
    }
    if (kind == 73) {
      sb.append("in");
      return;
    }
    if (kind == 74) {
      sb.append("pt");
      return;
    }
    if (kind == 75) {
      sb.append("pc");
      return;
    }
    if (kind == 76) {
      sb.append("deg");
      return;
    }
    if (kind == 77) {
      sb.append("rad");
      return;
    }
    if (kind == 78) {
      sb.append("grad");
      return;
    }
    if (kind == 79) {
      sb.append("ms");
      return;
    }
    if (kind == 80) {
      sb.append('s');
      return;
    }
    if (kind == 81) {
      sb.append("hz");
      return;
    }
    if (kind == 82) {
      sb.append("khz");
      return;
    }
    if (kind == 83) {
      sb.append('%');
      return;
    }
  }
  
  void error_skipblock() throws ParseException {
    int nesting = 0;
    Token t;
    do { t = getNextToken();
      if (kind == 46) {
        nesting++;
      }
      else if (kind == 47) {
        nesting--;
      }
      
    } while ((kind != 0) && ((kind != 47) || (nesting > 0)));
  }
  
  void error_skipdecl() throws ParseException { Token t = getToken(1);
    if (kind == 46) {
      error_skipblock();
      return;
    }
    if (kind == 47)
    {
      return;
    }
    
    Token oldToken = token;
    while ((kind != 51) && (kind != 47) && (kind != 0)) {
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
    while ((kind != 51) && (kind != 0));
  }
  
  private boolean jj_2_1(int xla)
  {
    jj_la = xla;jj_lastpos = (this.jj_scanpos = token);
    try { return !jj_3_1();
    } catch (LookaheadSuccess ls) { return true;
    } finally { jj_save(0, xla);
    }
  }
  
  private boolean jj_3R_58() {
    if (jj_scan_token(59)) return true;
    return false;
  }
  

  private boolean jj_3R_55()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_58()) {
      jj_scanpos = xsp;
      if (jj_3R_59()) return true;
    }
    return false;
  }
  

  private boolean jj_3R_49()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_53()) {
      jj_scanpos = xsp;
      if (jj_3R_54()) return true;
    }
    return false;
  }
  
  private boolean jj_3R_69()
  {
    if (jj_scan_token(52)) return true;
    return false;
  }
  
  private boolean jj_3R_52()
  {
    if (jj_scan_token(1)) { return true;
    }
    Token xsp = jj_scanpos;
    if (jj_3R_55()) jj_scanpos = xsp;
    return false;
  }
  
  private boolean jj_3R_51()
  {
    if (jj_scan_token(60)) return true;
    Token xsp;
    do {
      xsp = jj_scanpos;
    } while (!jj_scan_token(1)); jj_scanpos = xsp;
    
    return false;
  }
  
  private boolean jj_3R_61()
  {
    if (jj_scan_token(53)) return true;
    return false;
  }
  
  private boolean jj_3R_50()
  {
    if (jj_scan_token(59)) return true;
    Token xsp;
    do {
      xsp = jj_scanpos;
    } while (!jj_scan_token(1)); jj_scanpos = xsp;
    
    return false;
  }
  
  private boolean jj_3R_65()
  {
    if (jj_3R_69()) return true;
    return false;
  }
  

  private boolean jj_3R_48()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_50()) {
      jj_scanpos = xsp;
      if (jj_3R_51()) {
        jj_scanpos = xsp;
        if (jj_3R_52()) return true;
      }
    }
    return false;
  }
  
  private boolean jj_3R_64()
  {
    if (jj_3R_68()) return true;
    return false;
  }
  
  private boolean jj_3R_67()
  {
    if (jj_scan_token(50)) return true;
    return false;
  }
  
  private boolean jj_3R_63()
  {
    if (jj_3R_67()) return true;
    return false;
  }
  

  private boolean jj_3R_57()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_62()) {
      jj_scanpos = xsp;
      if (jj_3R_63()) {
        jj_scanpos = xsp;
        if (jj_3R_64()) {
          jj_scanpos = xsp;
          if (jj_3R_65()) return true;
        }
      }
    }
    return false;
  }
  
  private boolean jj_3R_62()
  {
    if (jj_3R_66()) return true;
    return false;
  }
  
  private boolean jj_3_1()
  {
    if (jj_3R_48()) return true;
    if (jj_3R_49()) return true;
    return false;
  }
  
  private boolean jj_3R_60()
  {
    if (jj_scan_token(19)) return true;
    return false;
  }
  
  private boolean jj_3R_66()
  {
    if (jj_scan_token(62)) return true;
    return false;
  }
  

  private boolean jj_3R_54()
  {
    if (jj_3R_57()) return true;
    Token xsp;
    do { xsp = jj_scanpos;
    } while (!jj_3R_57()); jj_scanpos = xsp;
    
    return false;
  }
  
  private boolean jj_3R_59()
  {
    if (jj_scan_token(60)) return true;
    return false;
  }
  

  private boolean jj_3R_56()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_60()) {
      jj_scanpos = xsp;
      if (jj_3R_61()) return true;
    }
    return false;
  }
  
  private boolean jj_3R_68()
  {
    if (jj_scan_token(57)) return true;
    return false;
  }
  
  private boolean jj_3R_53()
  {
    if (jj_3R_56()) return true;
    return false;
  }
  










  private final int[] jj_la1 = new int[82];
  private static int[] jj_la1_0;
  private static int[] jj_la1_1;
  private static int[] jj_la1_2;
  
  static { jj_la1_init_0();
    jj_la1_init_1();
    jj_la1_init_2();
  }
  
  private static void jj_la1_init_0() { jj_la1_0 = new int[] { 2, 2, 2, 2, 0, 524288, 524288, 524288, 2, 2, 2, 524288, 2, 2, 4194304, 2, 524288, 2, 2, 524288, 0, 2, 524288, 2, 524288, 2, 2, 2, 0, 2, 2, 2, 0, 2, 2, 0, 2, 0, 2, 0, 2, 2, 1, 2, 0, 2, 2, 0, 0, 0, 0, 524288, 524288, 2, 2, 0, 2, 4718592, 2, 0, 2, 2, 2, 2, 524288, 524288, 524288, 0, 2, 524288, 0, 2, 0, 0, 2, 5111808, 0, 0, 131072, 5111808, 2, 2 }; }
  
  private static void jj_la1_init_1() {
    jj_la1_1 = new int[] { 3072, 3072, 3072, 3072, 0, 1110704128, -1036779520, -1036779520, 3072, 3072, 0, -1036779520, 0, 0, 0, 0, 0, 0, 0, -1036779520, 536870912, 0, -1036779520, 0, -1036779520, 0, 0, 0, 1048576, 0, 0, 0, 541065216, 0, 0, 402653184, 0, 402653184, 402653184, 142606336, 0, 0, 32768, 0, 536870912, 0, 0, 1108606976, 1108606976, 1108606976, 1108606976, 1110704128, 2097152, 0, 0, 16789504, 0, 0, 0, 16789504, 0, 0, 0, 0, 0, 0, 2097152, 524288, 0, 2097152, 2097152, 0, 0, 0, 0, 1757413376, 541065216, 142606336, 0, 1073741824, 0, 0 };
  }
  
  private static void jj_la1_init_2() { jj_la1_2 = new int[] { 0, 0, 0, 0, 4, 16777219, 16777219, 16777219, 0, 0, 0, 16777223, 0, 0, 2097152, 0, 0, 0, 0, 16777217, 0, 0, 16777217, 0, 16777217, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12582912, 0, 0, 0, 0, 0, 0, 8, 33554432, 0, 79691760, 0, 0, 9437168, 79691760, 0, 0 }; }
  
  private final JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;
  
  public SACParserCSS21(CharStream stream)
  {
    token_source = new SACParserCSS21TokenManager(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 82; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public void ReInit(CharStream stream)
  {
    token_source.ReInit(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 82; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public SACParserCSS21(SACParserCSS21TokenManager tm)
  {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 82; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public void ReInit(SACParserCSS21TokenManager tm)
  {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 82; i++) jj_la1[i] = -1;
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
    boolean[] la1tokens = new boolean[91];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 82; i++) {
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
    for (int i = 0; i < 91; i++) {
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
    for (int i = 0; i < 1; i++) {
      try {
        JJCalls p = jj_2_rtns[i];
        do
        {
          if (gen > jj_gen) {
            jj_la = arg;jj_lastpos = (this.jj_scanpos = first);
            switch (i) {
            case 0:  jj_3_1();
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
  
  void invalidRule()
    throws ParseException
  {}
  
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
