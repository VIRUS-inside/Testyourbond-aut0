package com.steadystate.css.parser;

import java.util.List;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SimpleSelector;

public class SACParserCSSmobileOKBasic1 extends AbstractSACParser implements SACParserCSSmobileOKBasic1Constants
{
  public SACParserCSSmobileOKBasic1TokenManager token_source;
  public Token token;
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos;
  private Token jj_lastpos;
  private int jj_la;
  private int jj_gen;
  
  public SACParserCSSmobileOKBasic1()
  {
    this((CharStream)null);
  }
  
  public String getParserVersion() {
    return "http://www.w3.org/TR/mobileOK-basic10-tests/#validity";
  }
  
  protected String getGrammarUri()
  {
    return "CSSgrammarMobileOKBasic1.0.txt";
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
  
  public final void styleSheetRuleList() throws ParseException
  {
    boolean ruleFound = false;
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
      case 27: 
      case 28: 
        break;
      
      default: 
        jj_la1[0] = jj_gen;
        break;
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 1: 
        jj_consume_token(1);
        break;
      
      case 27: 
        jj_consume_token(27);
        break;
      
      case 28: 
        jj_consume_token(28);
      }
      
    }
    jj_la1[1] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
    



    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 3: case 4: case 5: case 6: case 9: case 13: case 16: case 29: case 30: case 32: 
      break;
    case 7: case 8: 
    case 10: case 11: 
    case 12: case 14: 
    case 15: case 17: 
    case 18: case 19: 
    case 20: case 21: 
    case 22: case 23: 
    case 24: case 25: 
    case 26: case 27: 
    case 28: 
    case 31: 
    default: 
      jj_la1[2] = jj_gen;
      break;
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 29: 
      importRule(ruleFound);
      break;
    
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 9: 
    case 13: 
    case 16: 
    case 30: 
    case 32: 
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 9: 
      case 13: 
      case 16: 
        styleRule();
        break;
      
      case 30: 
        mediaRule();
        break;
      
      case 32: 
        unknownAtRule();
        break;
      case 7: case 8: case 10: case 11: case 12: case 14: case 15: case 17: case 18: case 19: case 20: 
      case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 31: default: 
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ruleFound = true;
      break;
    case 7: case 8: case 10: case 11: case 12: case 14: case 15: case 17: case 18: case 19: case 20: 
    case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 31: default: 
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
      case 27: 
      case 28: 
        break;
      
      default: 
        jj_la1[5] = jj_gen;
        break;
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 1: 
        jj_consume_token(1);
        break;
      
      case 27: 
        jj_consume_token(27);
        break;
      
      case 28: 
        jj_consume_token(28);
      }
      
    }
    jj_la1[6] = jj_gen;
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
        jj_la1[7] = jj_gen;
        break;
      }
      jj_consume_token(1);
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 29: 
      importRule(false);
      break;
    
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 9: 
    case 13: 
    case 16: 
      styleRule();
      break;
    
    case 30: 
      mediaRule();
      break;
    
    case 32: 
      unknownAtRule();
      break;
    case 7: case 8: case 10: case 11: case 12: case 14: case 15: case 17: case 18: case 19: case 20: 
    case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 31: default: 
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }
  
  public final void unknownAtRule() throws ParseException
  {
    try {
      jj_consume_token(32);
      org.w3c.css.sac.Locator locator = createLocator(token);
      String s = skip();
      handleIgnorableAtRule(s, locator);
    } catch (ParseException e) {
      getErrorHandler().error(
        toCSSParseException("invalidUnknownRule", e));
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
      jj_consume_token(29);
      org.w3c.css.sac.Locator locator = createLocator(token);
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 1: 
          break;
        
        default: 
          jj_la1[9] = jj_gen;
          break;
        }
        jj_consume_token(1); }
      Token t;
      Token t; switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 24: 
        t = jj_consume_token(24);
        break;
      
      case 26: 
        t = jj_consume_token(26);
        break;
      
      default: 
        jj_la1[10] = jj_gen;
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
          jj_la1[11] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 3: 
        mediaList(ml);
        break;
      
      default: 
        jj_la1[12] = jj_gen;
      }
      
      jj_consume_token(14);
      if (nonImportRuleFoundBefore)
      {
        getErrorHandler().error(toCSSParseException("invalidImportRuleIgnored", e));
      }
      else
      {
        handleImportStyle(unescape(image, false), ml, null, locator);
      }
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      error_skipAtRule();
    } catch (ParseException e) {
      getErrorHandler().error(
        toCSSParseException("invalidImportRule", e));
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
      jj_consume_token(30);
      org.w3c.css.sac.Locator locator = createLocator(token);
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
      mediaList(ml);
      start = true;
      handleStartMedia(ml, locator);
      jj_consume_token(10);
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
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 9: 
      case 13: 
      case 16: 
      case 32: 
        mediaRuleList();
        break;
      case 7: case 8: case 10: case 11: case 12: case 14: case 15: case 17: case 18: case 19: case 20: case 21: 
      case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: default: 
        jj_la1[15] = jj_gen;
      }
      
      jj_consume_token(11);
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
        case 12: 
          break;
        
        default: 
          jj_la1[16] = jj_gen;
          break;
        }
        jj_consume_token(12);
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
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 9: 
      case 13: 
      case 16: 
        styleRule();
        break;
      
      case 32: 
        unknownAtRule();
        break;
      case 7: case 8: case 10: case 11: case 12: case 14: case 15: case 17: case 18: case 19: case 20: case 21: 
      case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: default: 
        jj_la1[18] = jj_gen;
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
          jj_la1[19] = jj_gen;
          break;
        }
        jj_consume_token(1);
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      }
      
    }
    







    jj_la1[20] = jj_gen;
  }
  






  public final String medium()
    throws ParseException
  {
    Token t = jj_consume_token(3);
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
    handleMedium(image, createLocator(t));
    return image;
  }
  



  public final LexicalUnit operator(LexicalUnit prev)
    throws ParseException
  {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 17: 
      jj_consume_token(17);
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
      return new LexicalUnitImpl(prev, (short)4);
    
    case 12: 
      jj_consume_token(12);
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
      return LexicalUnitImpl.createComma(prev);
    }
    
    jj_la1[24] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  




  public final char unaryOperator()
    throws ParseException
  {
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 19: 
      jj_consume_token(19);
      return '-';
    
    case 18: 
      jj_consume_token(18);
      return '+';
    }
    
    jj_la1[25] = jj_gen;
    jj_consume_token(-1);
    throw new ParseException();
  }
  




  public final String property()
    throws ParseException
  {
    Token t = jj_consume_token(3);
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
      jj_consume_token(10);
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
      start = true;
      handleStartSelector(selList, createLocator(next));
      declaration();
      for (;;)
      {
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
        {
        case 14: 
          break;
        
        default: 
          jj_la1[28] = jj_gen;
          break;
        }
        jj_consume_token(14);
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
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 3: 
          declaration();
          break;
        
        default: 
          jj_la1[30] = jj_gen;
        }
        
      }
      jj_consume_token(11);
    } catch (CSSParseException e) {
      getErrorHandler().error(e);
      error_skipblock();
    } catch (ParseException e) {
      getErrorHandler().error(toCSSParseException("invalidStyleRule", e));
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
        jj_la1[31] = jj_gen;
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
      case 12: 
        break;
      
      default: 
        jj_la1[32] = jj_gen;
        break;
      }
      jj_consume_token(12);
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
      selList.add(sel);
      sel = selector();
    }
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
    selList.add(sel);
    return selList;
  }
  



  public final org.w3c.css.sac.Selector selector()
    throws ParseException
  {
    SimpleSelector pseudoElementSel = null;
    try {
      org.w3c.css.sac.Selector sel = simpleSelector(null, ' ');
      

      while (jj_2_1(2))
      {



        jj_consume_token(1);
        sel = simpleSelector(sel, ' ');
      }
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 7: 
      case 8: 
        pseudoElementSel = pseudoElement();
        break;
      
      default: 
        jj_la1[35] = jj_gen;
      }
      
      if (pseudoElementSel != null)
      {
        sel = getSelectorFactory().createDescendantSelector(sel, pseudoElementSel);
      }
      handleSelector(sel);
      return sel;
    } catch (ParseException e) { throw toCSSParseException("invalidSelector", e);
    }
  }
  




  public final org.w3c.css.sac.Selector simpleSelector(org.w3c.css.sac.Selector sel, char comb)
    throws ParseException
  {
    SimpleSelector simpleSel = null;
    Condition c = null;
    try {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 3: 
      case 16: 
        simpleSel = elementName();
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 9: 
          c = hash(c);
          break;
        
        default: 
          jj_la1[36] = jj_gen;
        }
        
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 13: 
          c = _class(c);
          break;
        
        default: 
          jj_la1[37] = jj_gen;
        }
        
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 4: 
        case 5: 
        case 6: 
          c = pseudoClass(c);
          break;
        
        default: 
          jj_la1[38] = jj_gen;
        }
        
        break;
      
      case 9: 
        c = hash(c);
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 13: 
          c = _class(c);
          break;
        
        default: 
          jj_la1[39] = jj_gen;
        }
        
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 4: 
        case 5: 
        case 6: 
          c = pseudoClass(c);
          break;
        
        default: 
          jj_la1[40] = jj_gen;
        }
        
        break;
      
      case 13: 
        c = _class(c);
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 4: 
        case 5: 
        case 6: 
          c = pseudoClass(c);
          break;
        
        default: 
          jj_la1[41] = jj_gen;
        }
        
        break;
      
      case 4: 
      case 5: 
      case 6: 
        c = pseudoClass(c);
        break;
      case 7: case 8: case 10: case 11: 
      case 12: case 14: case 15: default: 
        jj_la1[42] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      if (c != null) {
        simpleSel = getSelectorFactory().createConditionalSelector(simpleSel, c);
      }
      
      if (sel != null) {
        switch (comb) {
        case ' ': 
          sel = getSelectorFactory().createDescendantSelector(sel, simpleSel);
        }
        
      }
      return simpleSel;
    }
    catch (ParseException e)
    {
      throw toCSSParseException("invalidSimpleSelector", e);
    }
  }
  



  public final Condition _class(Condition pred)
    throws ParseException
  {
    try
    {
      jj_consume_token(13);
      org.w3c.css.sac.Locator locator = createLocator(token);
      Token t = jj_consume_token(3);
      Condition c = getConditionFactory().createClassCondition(null, image);
      if ((c instanceof Locatable))
      {
        ((Locatable)c).setLocator(locator);
      }
      return pred == null ? c : getConditionFactory().createAndCondition(pred, c);
    } catch (ParseException e) { throw toCSSParseException("invalidClassSelector", e);
    }
  }
  



  public final SimpleSelector elementName()
    throws ParseException
  {
    try
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 3: 
        Token t = jj_consume_token(3);
        SimpleSelector sel = getSelectorFactory().createElementSelector(null, unescape(image, false));
        if ((sel instanceof Locatable))
        {
          ((Locatable)sel).setLocator(createLocator(token));
        }
        return sel;
      
      case 16: 
        jj_consume_token(16);
        SimpleSelector sel = getSelectorFactory().createElementSelector(null, null);
        if ((sel instanceof Locatable))
        {
          ((Locatable)sel).setLocator(createLocator(token));
        }
        return sel;
      }
      
      jj_la1[43] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    catch (ParseException e) {
      throw toCSSParseException("invalidElementName", e);
    }
  }
  
  public final Condition pseudoClass(Condition pred)
    throws ParseException
  {
    try
    {
      Token t;
      Token t;
      Token t;
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 4: 
        t = jj_consume_token(4);
        break;
      
      case 5: 
        t = jj_consume_token(5);
        break;
      
      case 6: 
        t = jj_consume_token(6);
        break;
      
      default: 
        jj_la1[44] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      
      Token t;
      String s = image;
      Condition c = getConditionFactory().createPseudoClassCondition(null, s);
      if ((c instanceof Locatable))
      {
        ((Locatable)c).setLocator(createLocator(token));
      }
      if ("" != null)
      {
        return pred == null ? c : getConditionFactory().createAndCondition(pred, c); }
    } catch (ParseException e) {
      throw toCSSParseException("invalidPseudoClass", e); }
    Token t;
    Condition c; return null;
  }
  

  public final SimpleSelector pseudoElement()
    throws ParseException
  {
    try
    {
      Token t;
      
      Token t;
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 8: 
        t = jj_consume_token(8);
        break;
      
      case 7: 
        t = jj_consume_token(7);
        break;
      
      default: 
        jj_la1[45] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException(); }
      Token t;
      String s = image;
      SimpleSelector sel = getSelectorFactory().createPseudoElementSelector(null, s);
      if ((sel instanceof Locatable))
      {
        ((Locatable)sel).setLocator(createLocator(token));
      }
      return sel;
    } catch (ParseException e) { throw toCSSParseException("invalidPseudoElement", e);
    }
  }
  
  public final Condition hash(Condition pred) throws ParseException {
    try {
      Token t = jj_consume_token(9);
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
    case 3: 
      declaration();
      break;
    
    default: 
      jj_la1[46] = jj_gen;
    }
    
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 14: 
        break;
      
      default: 
        jj_la1[47] = jj_gen;
        break;
      }
      jj_consume_token(14);
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
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 3: 
        declaration();
        break;
      
      default: 
        jj_la1[49] = jj_gen;
      }
      
    }
  }
  





  public final void declaration()
    throws ParseException
  {
    boolean priority = false;
    org.w3c.css.sac.Locator locator = null;
    try {
      String p = property();
      locator = createLocator(token);
      jj_consume_token(15);
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
      LexicalUnit e = expr();
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 31: 
        priority = prio();
        break;
      
      default: 
        jj_la1[51] = jj_gen;
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
    jj_consume_token(31);
    for (;;)
    {
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk)
      {
      case 1: 
        break;
      
      default: 
        jj_la1[52] = jj_gen;
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
        case 3: case 9: case 12: 
        case 17: case 18: case 19: 
        case 24: case 26: 
        case 33: case 34: 
        case 35: case 36: 
        case 37: case 38: 
        case 39: case 40: 
        case 41: case 42: 
        case 43: case 47: 
          break;
        case 4: case 5: 
        case 6: case 7: 
        case 8: case 10: 
        case 11: case 13: 
        case 14: case 15: 
        case 16: case 20: 
        case 21: case 22: 
        case 23: case 25: 
        case 27: case 28: 
        case 29: case 30: 
        case 31: case 32: 
        case 44: case 45: 
        case 46: 
        default: 
          jj_la1[53] = jj_gen;
          break;
        }
        switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
        case 12: 
        case 17: 
          body = operator(body);
          break;
        
        default: 
          jj_la1[54] = jj_gen;
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
    org.w3c.css.sac.Locator locator = null;
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 18: 
    case 19: 
      op = unaryOperator();
      break;
    
    default: 
      jj_la1[55] = jj_gen;
    }
    
    if (op != ' ')
    {
      locator = createLocator(token);
    }
    switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
      switch (jj_ntk == -1 ? jj_ntk_f() : jj_ntk) {
      case 42: 
        Token t = jj_consume_token(42);
        try
        {
          value = LexicalUnitImpl.createNumber(prev, intValue(op, image));
        }
        catch (NumberFormatException e)
        {
          value = LexicalUnitImpl.createNumber(prev, floatValue(op, image));
        }
      

      case 41: 
        Token t = jj_consume_token(41);
        value = LexicalUnitImpl.createPercentage(prev, floatValue(op, image));
        break;
      
      case 35: 
        Token t = jj_consume_token(35);
        value = LexicalUnitImpl.createPixel(prev, floatValue(op, image));
        break;
      
      case 36: 
        Token t = jj_consume_token(36);
        value = LexicalUnitImpl.createCentimeter(prev, floatValue(op, image));
        break;
      
      case 37: 
        Token t = jj_consume_token(37);
        value = LexicalUnitImpl.createMillimeter(prev, floatValue(op, image));
        break;
      
      case 38: 
        Token t = jj_consume_token(38);
        value = LexicalUnitImpl.createInch(prev, floatValue(op, image));
        break;
      
      case 39: 
        Token t = jj_consume_token(39);
        value = LexicalUnitImpl.createPoint(prev, floatValue(op, image));
        break;
      
      case 40: 
        Token t = jj_consume_token(40);
        value = LexicalUnitImpl.createPica(prev, floatValue(op, image));
        break;
      
      case 33: 
        Token t = jj_consume_token(33);
        value = LexicalUnitImpl.createEm(prev, floatValue(op, image));
        break;
      
      case 34: 
        Token t = jj_consume_token(34);
        value = LexicalUnitImpl.createEx(prev, floatValue(op, image));
        break;
      
      default: 
        jj_la1[56] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      
      break;
    case 24: 
      Token t = jj_consume_token(24);
      value = new LexicalUnitImpl(prev, (short)36, image);
      break;
    
    case 3: 
      Token t = jj_consume_token(3);
      value = new LexicalUnitImpl(prev, (short)35, image);
      break;
    
    case 26: 
      Token t = jj_consume_token(26);
      value = new LexicalUnitImpl(prev, (short)24, image);
      break;
    
    case 47: 
      Token t = jj_consume_token(47);
      value = new LexicalUnitImpl(prev, (short)39, image);
      break;
    
    case 43: 
      value = rgb(prev);
      break;
    
    case 9: 
      value = hexcolor(prev);
      break;
    case 4: case 5: case 6: case 7: case 8: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19: 
    case 20: case 21: case 22: case 23: case 25: case 27: case 28: case 29: case 30: case 31: case 32: case 44: case 45: case 46: default: 
      jj_la1[57] = jj_gen;
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
        jj_la1[58] = jj_gen;
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
  



  public final LexicalUnit rgb(LexicalUnit prev)
    throws ParseException
  {
    jj_consume_token(43);
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
    LexicalUnit params = expr();
    jj_consume_token(25);
    return LexicalUnitImpl.createRgbColor(prev, params);
  }
  



  public final LexicalUnit hexcolor(LexicalUnit prev)
    throws ParseException
  {
    Token t = jj_consume_token(9);
    return hexcolorInternal(prev, t);
  }
  
  String skip() throws ParseException { StringBuilder sb = new StringBuilder();
    int nesting = 0;
    Token t = getToken(0);
    if (image != null) {
      sb.append(image);
    }
    do {
      t = getNextToken();
      if (kind == 0)
        break;
      sb.append(image);
      if (kind == 10) {
        nesting++;
      } else if (kind == 11)
        nesting--; else {
        if ((kind == 14) && (nesting <= 0))
          break;
      }
    } while ((kind != 11) || (nesting > 0));
    
    return sb.toString();
  }
  
  void error_skipblock() throws ParseException {
    int nesting = 0;
    Token t;
    do { t = getNextToken();
      if (kind == 10) {
        nesting++;
      }
      else if (kind == 11) {
        nesting--;
      }
      
    } while ((kind != 0) && ((kind != 11) || (nesting > 0)));
  }
  
  void error_skipdecl() throws ParseException { Token t = getToken(1);
    if (kind == 10) {
      error_skipblock();
      return;
    }
    if (kind == 11)
    {
      return;
    }
    
    Token oldToken = token;
    while ((kind != 14) && (kind != 11) && (kind != 0)) {
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
    while ((kind != 14) && (kind != 0));
  }
  
  private boolean jj_2_1(int xla)
  {
    jj_la = xla;jj_lastpos = (this.jj_scanpos = token);
    try { return !jj_3_1();
    } catch (LookaheadSuccess ls) { return true;
    } finally { jj_save(0, xla);
    }
  }
  
  private boolean jj_3R_33() {
    if (jj_3R_37()) return true;
    return false;
  }
  
  private boolean jj_3R_41()
  {
    if (jj_scan_token(3)) return true;
    return false;
  }
  
  private boolean jj_3R_36()
  {
    if (jj_3R_40()) return true;
    return false;
  }
  
  private boolean jj_3_1()
  {
    if (jj_scan_token(1)) return true;
    if (jj_3R_32()) return true;
    return false;
  }
  

  private boolean jj_3R_37()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_41()) {
      jj_scanpos = xsp;
      if (jj_3R_42()) return true;
    }
    return false;
  }
  

  private boolean jj_3R_32()
  {
    Token xsp = jj_scanpos;
    if (jj_3R_33()) {
      jj_scanpos = xsp;
      if (jj_3R_34()) {
        jj_scanpos = xsp;
        if (jj_3R_35()) {
          jj_scanpos = xsp;
          if (jj_3R_36()) return true;
        }
      }
    }
    return false;
  }
  
  private boolean jj_3R_35()
  {
    if (jj_3R_39()) return true;
    return false;
  }
  
  private boolean jj_3R_42()
  {
    if (jj_scan_token(16)) return true;
    return false;
  }
  
  private boolean jj_3R_34()
  {
    if (jj_3R_38()) return true;
    return false;
  }
  

  private boolean jj_3R_40()
  {
    Token xsp = jj_scanpos;
    if (jj_scan_token(4)) {
      jj_scanpos = xsp;
      if (jj_scan_token(5)) {
        jj_scanpos = xsp;
        if (jj_scan_token(6)) return true;
      }
    }
    return false;
  }
  
  private boolean jj_3R_39()
  {
    if (jj_scan_token(13)) return true;
    return false;
  }
  
  private boolean jj_3R_38()
  {
    if (jj_scan_token(9)) return true;
    return false;
  }
  










  private final int[] jj_la1 = new int[60];
  private static int[] jj_la1_0;
  private static int[] jj_la1_1;
  private static int[] jj_la1_2;
  
  static { jj_la1_init_0();
    jj_la1_init_1();
    jj_la1_init_2();
  }
  
  private static void jj_la1_init_0() { jj_la1_0 = new int[] { 402653186, 402653186, 1610687096, 1073816184, 1610687096, 402653186, 402653186, 2, 1610687096, 2, 83886080, 2, 8, 2, 2, 74360, 4096, 2, 74360, 2, 74360, 2, 2, 2, 135168, 786432, 2, 2, 16384, 2, 8, 2, 4096, 2, 2, 384, 512, 8192, 112, 8192, 112, 112, 74360, 65544, 112, 384, 8, 16384, 2, 8, 2, Integer.MIN_VALUE, 2, 84808200, 135168, 786432, 0, 83886600, 2, 2 }; }
  
  private static void jj_la1_init_1() {
    jj_la1_1 = new int[] { 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36862, 0, 0, 2046, 36862, 0, 0 };
  }
  
  private static void jj_la1_init_2() { jj_la1_2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; }
  
  private final JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;
  
  public SACParserCSSmobileOKBasic1(CharStream stream)
  {
    token_source = new SACParserCSSmobileOKBasic1TokenManager(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 60; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public void ReInit(CharStream stream)
  {
    token_source.ReInit(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 60; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public SACParserCSSmobileOKBasic1(SACParserCSSmobileOKBasic1TokenManager tm)
  {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 60; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }
  
  public void ReInit(SACParserCSSmobileOKBasic1TokenManager tm)
  {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 60; i++) jj_la1[i] = -1;
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
  
  private List<int[]> jj_expentries = new java.util.ArrayList();
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
    boolean[] la1tokens = new boolean[68];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 60; i++) {
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
    for (int i = 0; i < 68; i++) {
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
