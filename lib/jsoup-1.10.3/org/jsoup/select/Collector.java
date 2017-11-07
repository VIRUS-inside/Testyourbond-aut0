package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;












public class Collector
{
  private Collector() {}
  
  public static Elements collect(Evaluator eval, Element root)
  {
    Elements elements = new Elements();
    new NodeTraversor(new Accumulator(root, elements, eval)).traverse(root);
    return elements;
  }
  
  private static class Accumulator implements NodeVisitor {
    private final Element root;
    private final Elements elements;
    private final Evaluator eval;
    
    Accumulator(Element root, Elements elements, Evaluator eval) {
      this.root = root;
      this.elements = elements;
      this.eval = eval;
    }
    
    public void head(Node node, int depth) {
      if ((node instanceof Element)) {
        Element el = (Element)node;
        if (eval.matches(root, el)) {
          elements.add(el);
        }
      }
    }
    
    public void tail(Node node, int depth) {}
  }
}
