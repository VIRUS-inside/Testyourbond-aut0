package org.jsoup.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;


































































public class Selector
{
  private final Evaluator evaluator;
  private final Element root;
  
  private Selector(String query, Element root)
  {
    Validate.notNull(query);
    query = query.trim();
    Validate.notEmpty(query);
    Validate.notNull(root);
    
    evaluator = QueryParser.parse(query);
    
    this.root = root;
  }
  
  private Selector(Evaluator evaluator, Element root) {
    Validate.notNull(evaluator);
    Validate.notNull(root);
    
    this.evaluator = evaluator;
    this.root = root;
  }
  







  public static Elements select(String query, Element root)
  {
    return new Selector(query, root).select();
  }
  






  public static Elements select(Evaluator evaluator, Element root)
  {
    return new Selector(evaluator, root).select();
  }
  






  public static Elements select(String query, Iterable<Element> roots)
  {
    Validate.notEmpty(query);
    Validate.notNull(roots);
    Evaluator evaluator = QueryParser.parse(query);
    ArrayList<Element> elements = new ArrayList();
    IdentityHashMap<Element, Boolean> seenElements = new IdentityHashMap();
    

    for (Element root : roots) {
      Elements found = select(evaluator, root);
      for (Element el : found) {
        if (!seenElements.containsKey(el)) {
          elements.add(el);
          seenElements.put(el, Boolean.TRUE);
        }
      }
    }
    return new Elements(elements);
  }
  
  private Elements select() {
    return Collector.collect(evaluator, root);
  }
  
  static Elements filterOut(Collection<Element> elements, Collection<Element> outs)
  {
    Elements output = new Elements();
    for (Element el : elements) {
      boolean found = false;
      for (Element out : outs) {
        if (el.equals(out)) {
          found = true;
          break;
        }
      }
      if (!found)
        output.add(el);
    }
    return output;
  }
  
  public static class SelectorParseException extends IllegalStateException {
    public SelectorParseException(String msg, Object... params) {
      super();
    }
  }
}
