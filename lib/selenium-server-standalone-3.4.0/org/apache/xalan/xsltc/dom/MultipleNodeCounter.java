package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;

























public abstract class MultipleNodeCounter
  extends NodeCounter
{
  private DTMAxisIterator _precSiblings = null;
  
  public MultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    super(translet, document, iterator);
  }
  
  public NodeCounter setStartNode(int node) {
    _node = node;
    _nodeType = _document.getExpandedTypeID(node);
    _precSiblings = _document.getAxisIterator(12);
    return this;
  }
  
  public String getCounter() {
    if (_value != -2.147483648E9D)
    {
      if (_value == 0.0D) return "0";
      if (Double.isNaN(_value)) return "NaN";
      if ((_value < 0.0D) && (Double.isInfinite(_value))) return "-Infinity";
      if (Double.isInfinite(_value)) return "Infinity";
      return formatNumbers((int)_value);
    }
    
    IntegerArray ancestors = new IntegerArray();
    

    int next = _node;
    ancestors.add(next);
    while (((next = _document.getParent(next)) > -1) && (!matchesFrom(next)))
    {
      ancestors.add(next);
    }
    

    int nAncestors = ancestors.cardinality();
    int[] counters = new int[nAncestors];
    for (int i = 0; i < nAncestors; i++) {
      counters[i] = Integer.MIN_VALUE;
    }
    

    int j = 0; for (int i = nAncestors - 1; i >= 0; j++) {
      int counter = counters[j];
      int ancestor = ancestors.at(i);
      
      if (matchesCount(ancestor)) {
        _precSiblings.setStartNode(ancestor);
        while ((next = _precSiblings.next()) != -1) {
          if (matchesCount(next)) {
            counters[j] = (counters[j] == Integer.MIN_VALUE ? 1 : counters[j] + 1);
          }
        }
        

        counters[j] = (counters[j] == Integer.MIN_VALUE ? 1 : counters[j] + 1);
      }
      i--;
    }
    















    return formatNumbers(counters);
  }
  

  public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    return new DefaultMultipleNodeCounter(translet, document, iterator);
  }
  
  static class DefaultMultipleNodeCounter extends MultipleNodeCounter
  {
    public DefaultMultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
    {
      super(document, iterator);
    }
  }
}
