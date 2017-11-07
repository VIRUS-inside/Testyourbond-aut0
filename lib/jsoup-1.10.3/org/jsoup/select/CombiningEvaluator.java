package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;



abstract class CombiningEvaluator
  extends Evaluator
{
  final ArrayList<Evaluator> evaluators;
  int num = 0;
  
  CombiningEvaluator()
  {
    evaluators = new ArrayList();
  }
  
  CombiningEvaluator(Collection<Evaluator> evaluators) {
    this();
    this.evaluators.addAll(evaluators);
    updateNumEvaluators();
  }
  
  Evaluator rightMostEvaluator() {
    return num > 0 ? (Evaluator)evaluators.get(num - 1) : null;
  }
  
  void replaceRightMostEvaluator(Evaluator replacement) {
    evaluators.set(num - 1, replacement);
  }
  
  void updateNumEvaluators()
  {
    num = evaluators.size();
  }
  
  static final class And extends CombiningEvaluator {
    And(Collection<Evaluator> evaluators) {
      super();
    }
    
    And(Evaluator... evaluators) {
      this(Arrays.asList(evaluators));
    }
    
    public boolean matches(Element root, Element node)
    {
      for (int i = 0; i < num; i++) {
        Evaluator s = (Evaluator)evaluators.get(i);
        if (!s.matches(root, node))
          return false;
      }
      return true;
    }
    
    public String toString()
    {
      return StringUtil.join(evaluators, " ");
    }
  }
  


  static final class Or
    extends CombiningEvaluator
  {
    Or(Collection<Evaluator> evaluators)
    {
      if (num > 1) {
        this.evaluators.add(new CombiningEvaluator.And(evaluators));
      } else
        this.evaluators.addAll(evaluators);
      updateNumEvaluators();
    }
    
    Or(Evaluator... evaluators) { this(Arrays.asList(evaluators)); }
    

    Or() {}
    
    public void add(Evaluator e)
    {
      evaluators.add(e);
      updateNumEvaluators();
    }
    
    public boolean matches(Element root, Element node)
    {
      for (int i = 0; i < num; i++) {
        Evaluator s = (Evaluator)evaluators.get(i);
        if (s.matches(root, node))
          return true;
      }
      return false;
    }
    
    public String toString()
    {
      return String.format(":or%s", new Object[] { evaluators });
    }
  }
}
