package org.jsoup.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.parser.TokenQueue;





public class QueryParser
{
  private static final String[] combinators = { ",", ">", "+", "~", " " };
  private static final String[] AttributeEvals = { "=", "!=", "^=", "$=", "*=", "~=" };
  
  private TokenQueue tq;
  private String query;
  private List<Evaluator> evals = new ArrayList();
  



  private QueryParser(String query)
  {
    this.query = query;
    tq = new TokenQueue(query);
  }
  



  public static Evaluator parse(String query)
  {
    try
    {
      QueryParser p = new QueryParser(query);
      return p.parse();
    } catch (IllegalArgumentException e) {
      throw new Selector.SelectorParseException(e.getMessage(), new Object[0]);
    }
  }
  



  Evaluator parse()
  {
    tq.consumeWhitespace();
    
    if (tq.matchesAny(combinators)) {
      evals.add(new StructuralEvaluator.Root());
      combinator(tq.consume());
    } else {
      findElements();
    }
    
    while (!tq.isEmpty())
    {
      boolean seenWhite = tq.consumeWhitespace();
      
      if (tq.matchesAny(combinators)) {
        combinator(tq.consume());
      } else if (seenWhite) {
        combinator(' ');
      } else {
        findElements();
      }
    }
    
    if (evals.size() == 1) {
      return (Evaluator)evals.get(0);
    }
    return new CombiningEvaluator.And(evals);
  }
  
  private void combinator(char combinator) {
    tq.consumeWhitespace();
    String subQuery = consumeSubQuery();
    


    Evaluator newEval = parse(subQuery);
    boolean replaceRightMost = false;
    Evaluator currentEval;
    Evaluator rootEval; if (evals.size() == 1) { Evaluator currentEval;
      Evaluator rootEval = currentEval = (Evaluator)evals.get(0);
      
      if (((rootEval instanceof CombiningEvaluator.Or)) && (combinator != ',')) {
        currentEval = ((CombiningEvaluator.Or)currentEval).rightMostEvaluator();
        replaceRightMost = true;
      }
    }
    else {
      rootEval = currentEval = new CombiningEvaluator.And(evals);
    }
    evals.clear();
    

    if (combinator == '>') {
      currentEval = new CombiningEvaluator.And(new Evaluator[] { newEval, new StructuralEvaluator.ImmediateParent(currentEval) });
    } else if (combinator == ' ') {
      currentEval = new CombiningEvaluator.And(new Evaluator[] { newEval, new StructuralEvaluator.Parent(currentEval) });
    } else if (combinator == '+') {
      currentEval = new CombiningEvaluator.And(new Evaluator[] { newEval, new StructuralEvaluator.ImmediatePreviousSibling(currentEval) });
    } else if (combinator == '~') {
      currentEval = new CombiningEvaluator.And(new Evaluator[] { newEval, new StructuralEvaluator.PreviousSibling(currentEval) });
    } else if (combinator == ',') {
      CombiningEvaluator.Or or;
      if ((currentEval instanceof CombiningEvaluator.Or)) {
        CombiningEvaluator.Or or = (CombiningEvaluator.Or)currentEval;
        or.add(newEval);
      } else {
        or = new CombiningEvaluator.Or();
        or.add(currentEval);
        or.add(newEval);
      }
      currentEval = or;
    }
    else {
      throw new Selector.SelectorParseException("Unknown combinator: " + combinator, new Object[0]);
    }
    if (replaceRightMost)
      ((CombiningEvaluator.Or)rootEval).replaceRightMostEvaluator(currentEval); else
      rootEval = currentEval;
    evals.add(rootEval);
  }
  
  private String consumeSubQuery() {
    StringBuilder sq = new StringBuilder();
    while (!tq.isEmpty())
      if (tq.matches("(")) {
        sq.append("(").append(tq.chompBalanced('(', ')')).append(")");
      } else if (tq.matches("[")) {
        sq.append("[").append(tq.chompBalanced('[', ']')).append("]");
      } else { if (tq.matchesAny(combinators)) {
          break;
        }
        sq.append(tq.consume());
      }
    return sq.toString();
  }
  
  private void findElements() {
    if (tq.matchChomp("#")) {
      byId();
    } else if (tq.matchChomp(".")) {
      byClass();
    } else if ((tq.matchesWord()) || (tq.matches("*|"))) {
      byTag();
    } else if (tq.matches("[")) {
      byAttribute();
    } else if (tq.matchChomp("*")) {
      allElements();
    } else if (tq.matchChomp(":lt(")) {
      indexLessThan();
    } else if (tq.matchChomp(":gt(")) {
      indexGreaterThan();
    } else if (tq.matchChomp(":eq(")) {
      indexEquals();
    } else if (tq.matches(":has(")) {
      has();
    } else if (tq.matches(":contains(")) {
      contains(false);
    } else if (tq.matches(":containsOwn(")) {
      contains(true);
    } else if (tq.matches(":containsData(")) {
      containsData();
    } else if (tq.matches(":matches(")) {
      matches(false);
    } else if (tq.matches(":matchesOwn(")) {
      matches(true);
    } else if (tq.matches(":not(")) {
      not();
    } else if (tq.matchChomp(":nth-child(")) {
      cssNthChild(false, false);
    } else if (tq.matchChomp(":nth-last-child(")) {
      cssNthChild(true, false);
    } else if (tq.matchChomp(":nth-of-type(")) {
      cssNthChild(false, true);
    } else if (tq.matchChomp(":nth-last-of-type(")) {
      cssNthChild(true, true);
    } else if (tq.matchChomp(":first-child")) {
      evals.add(new Evaluator.IsFirstChild());
    } else if (tq.matchChomp(":last-child")) {
      evals.add(new Evaluator.IsLastChild());
    } else if (tq.matchChomp(":first-of-type")) {
      evals.add(new Evaluator.IsFirstOfType());
    } else if (tq.matchChomp(":last-of-type")) {
      evals.add(new Evaluator.IsLastOfType());
    } else if (tq.matchChomp(":only-child")) {
      evals.add(new Evaluator.IsOnlyChild());
    } else if (tq.matchChomp(":only-of-type")) {
      evals.add(new Evaluator.IsOnlyOfType());
    } else if (tq.matchChomp(":empty")) {
      evals.add(new Evaluator.IsEmpty());
    } else if (tq.matchChomp(":root")) {
      evals.add(new Evaluator.IsRoot());
    } else {
      throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", new Object[] { query, tq.remainder() });
    }
  }
  
  private void byId() {
    String id = tq.consumeCssIdentifier();
    Validate.notEmpty(id);
    evals.add(new Evaluator.Id(id));
  }
  
  private void byClass() {
    String className = tq.consumeCssIdentifier();
    Validate.notEmpty(className);
    evals.add(new Evaluator.Class(className.trim()));
  }
  
  private void byTag() {
    String tagName = tq.consumeElementSelector();
    
    Validate.notEmpty(tagName);
    

    if (tagName.startsWith("*|")) {
      evals.add(new CombiningEvaluator.Or(new Evaluator[] { new Evaluator.Tag(Normalizer.normalize(tagName)), new Evaluator.TagEndsWith(Normalizer.normalize(tagName.replace("*|", ":"))) }));
    }
    else {
      if (tagName.contains("|")) {
        tagName = tagName.replace("|", ":");
      }
      evals.add(new Evaluator.Tag(tagName.trim()));
    }
  }
  
  private void byAttribute() {
    TokenQueue cq = new TokenQueue(tq.chompBalanced('[', ']'));
    String key = cq.consumeToAny(AttributeEvals);
    Validate.notEmpty(key);
    cq.consumeWhitespace();
    
    if (cq.isEmpty()) {
      if (key.startsWith("^")) {
        evals.add(new Evaluator.AttributeStarting(key.substring(1)));
      } else {
        evals.add(new Evaluator.Attribute(key));
      }
    } else if (cq.matchChomp("=")) {
      evals.add(new Evaluator.AttributeWithValue(key, cq.remainder()));
    }
    else if (cq.matchChomp("!=")) {
      evals.add(new Evaluator.AttributeWithValueNot(key, cq.remainder()));
    }
    else if (cq.matchChomp("^=")) {
      evals.add(new Evaluator.AttributeWithValueStarting(key, cq.remainder()));
    }
    else if (cq.matchChomp("$=")) {
      evals.add(new Evaluator.AttributeWithValueEnding(key, cq.remainder()));
    }
    else if (cq.matchChomp("*=")) {
      evals.add(new Evaluator.AttributeWithValueContaining(key, cq.remainder()));
    }
    else if (cq.matchChomp("~=")) {
      evals.add(new Evaluator.AttributeWithValueMatching(key, Pattern.compile(cq.remainder())));
    } else {
      throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", new Object[] { query, cq.remainder() });
    }
  }
  
  private void allElements() {
    evals.add(new Evaluator.AllElements());
  }
  
  private void indexLessThan()
  {
    evals.add(new Evaluator.IndexLessThan(consumeIndex()));
  }
  
  private void indexGreaterThan() {
    evals.add(new Evaluator.IndexGreaterThan(consumeIndex()));
  }
  
  private void indexEquals() {
    evals.add(new Evaluator.IndexEquals(consumeIndex()));
  }
  

  private static final Pattern NTH_AB = Pattern.compile("((\\+|-)?(\\d+)?)n(\\s*(\\+|-)?\\s*\\d+)?", 2);
  private static final Pattern NTH_B = Pattern.compile("(\\+|-)?(\\d+)");
  
  private void cssNthChild(boolean backwards, boolean ofType) {
    String argS = Normalizer.normalize(tq.chompTo(")"));
    Matcher mAB = NTH_AB.matcher(argS);
    Matcher mB = NTH_B.matcher(argS);
    int b;
    if ("odd".equals(argS)) {
      int a = 2;
      b = 1; } else { int b;
      if ("even".equals(argS)) {
        int a = 2;
        b = 0; } else { int b;
        if (mAB.matches()) {
          int a = mAB.group(3) != null ? Integer.parseInt(mAB.group(1).replaceFirst("^\\+", "")) : 1;
          b = mAB.group(4) != null ? Integer.parseInt(mAB.group(4).replaceFirst("^\\+", "")) : 0; } else { int b;
          if (mB.matches()) {
            int a = 0;
            b = Integer.parseInt(mB.group().replaceFirst("^\\+", ""));
          } else {
            throw new Selector.SelectorParseException("Could not parse nth-index '%s': unexpected format", new Object[] { argS }); } } } }
    int b;
    int a; if (ofType) {
      if (backwards) {
        evals.add(new Evaluator.IsNthLastOfType(a, b));
      } else {
        evals.add(new Evaluator.IsNthOfType(a, b));
      }
    } else if (backwards) {
      evals.add(new Evaluator.IsNthLastChild(a, b));
    } else {
      evals.add(new Evaluator.IsNthChild(a, b));
    }
  }
  
  private int consumeIndex() {
    String indexS = tq.chompTo(")").trim();
    Validate.isTrue(StringUtil.isNumeric(indexS), "Index must be numeric");
    return Integer.parseInt(indexS);
  }
  
  private void has()
  {
    tq.consume(":has");
    String subQuery = tq.chompBalanced('(', ')');
    Validate.notEmpty(subQuery, ":has(el) subselect must not be empty");
    evals.add(new StructuralEvaluator.Has(parse(subQuery)));
  }
  
  private void contains(boolean own)
  {
    tq.consume(own ? ":containsOwn" : ":contains");
    String searchText = TokenQueue.unescape(tq.chompBalanced('(', ')'));
    Validate.notEmpty(searchText, ":contains(text) query must not be empty");
    if (own) {
      evals.add(new Evaluator.ContainsOwnText(searchText));
    } else {
      evals.add(new Evaluator.ContainsText(searchText));
    }
  }
  
  private void containsData() {
    tq.consume(":containsData");
    String searchText = TokenQueue.unescape(tq.chompBalanced('(', ')'));
    Validate.notEmpty(searchText, ":containsData(text) query must not be empty");
    evals.add(new Evaluator.ContainsData(searchText));
  }
  
  private void matches(boolean own)
  {
    tq.consume(own ? ":matchesOwn" : ":matches");
    String regex = tq.chompBalanced('(', ')');
    Validate.notEmpty(regex, ":matches(regex) query must not be empty");
    
    if (own) {
      evals.add(new Evaluator.MatchesOwn(Pattern.compile(regex)));
    } else {
      evals.add(new Evaluator.Matches(Pattern.compile(regex)));
    }
  }
  
  private void not() {
    tq.consume(":not");
    String subQuery = tq.chompBalanced('(', ')');
    Validate.notEmpty(subQuery, ":not(selector) subselect must not be empty");
    
    evals.add(new StructuralEvaluator.Not(parse(subQuery)));
  }
}
