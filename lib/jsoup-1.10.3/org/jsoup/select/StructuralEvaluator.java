package org.jsoup.select;

import org.jsoup.nodes.Element;

abstract class StructuralEvaluator extends Evaluator {
  Evaluator evaluator;
  
  StructuralEvaluator() {}
  
  static class Root extends Evaluator {
    Root() {}
    
    public boolean matches(Element root, Element element) { return root == element; }
  }
  
  static class Has extends StructuralEvaluator
  {
    public Has(Evaluator evaluator) {
      this.evaluator = evaluator;
    }
    
    public boolean matches(Element root, Element element) {
      for (Element e : element.getAllElements()) {
        if ((e != element) && (evaluator.matches(root, e)))
          return true;
      }
      return false;
    }
    
    public String toString()
    {
      return String.format(":has(%s)", new Object[] { evaluator });
    }
  }
  
  static class Not extends StructuralEvaluator {
    public Not(Evaluator evaluator) {
      this.evaluator = evaluator;
    }
    
    public boolean matches(Element root, Element node) {
      return !evaluator.matches(root, node);
    }
    
    public String toString()
    {
      return String.format(":not%s", new Object[] { evaluator });
    }
  }
  
  static class Parent extends StructuralEvaluator {
    public Parent(Evaluator evaluator) {
      this.evaluator = evaluator;
    }
    
    public boolean matches(Element root, Element element) {
      if (root == element) {
        return false;
      }
      Element parent = element.parent();
      for (;;) {
        if (evaluator.matches(root, parent))
          return true;
        if (parent == root)
          break;
        parent = parent.parent();
      }
      return false;
    }
    
    public String toString()
    {
      return String.format(":parent%s", new Object[] { evaluator });
    }
  }
  
  static class ImmediateParent extends StructuralEvaluator {
    public ImmediateParent(Evaluator evaluator) {
      this.evaluator = evaluator;
    }
    
    public boolean matches(Element root, Element element) {
      if (root == element) {
        return false;
      }
      Element parent = element.parent();
      return (parent != null) && (evaluator.matches(root, parent));
    }
    
    public String toString()
    {
      return String.format(":ImmediateParent%s", new Object[] { evaluator });
    }
  }
  
  static class PreviousSibling extends StructuralEvaluator {
    public PreviousSibling(Evaluator evaluator) {
      this.evaluator = evaluator;
    }
    
    public boolean matches(Element root, Element element) {
      if (root == element) {
        return false;
      }
      Element prev = element.previousElementSibling();
      
      while (prev != null) {
        if (evaluator.matches(root, prev)) {
          return true;
        }
        prev = prev.previousElementSibling();
      }
      return false;
    }
    
    public String toString()
    {
      return String.format(":prev*%s", new Object[] { evaluator });
    }
  }
  
  static class ImmediatePreviousSibling extends StructuralEvaluator {
    public ImmediatePreviousSibling(Evaluator evaluator) {
      this.evaluator = evaluator;
    }
    
    public boolean matches(Element root, Element element) {
      if (root == element) {
        return false;
      }
      Element prev = element.previousElementSibling();
      return (prev != null) && (evaluator.matches(root, prev));
    }
    
    public String toString()
    {
      return String.format(":prev%s", new Object[] { evaluator });
    }
  }
}
