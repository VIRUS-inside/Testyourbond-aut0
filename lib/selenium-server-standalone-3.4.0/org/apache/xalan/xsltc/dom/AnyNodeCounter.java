package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;
























public abstract class AnyNodeCounter
  extends NodeCounter
{
  public AnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    super(translet, document, iterator);
  }
  
  public NodeCounter setStartNode(int node) {
    _node = node;
    _nodeType = _document.getExpandedTypeID(node);
    return this;
  }
  
  public String getCounter()
  {
    if (_value != -2.147483648E9D)
    {
      if (_value == 0.0D) return "0";
      if (Double.isNaN(_value)) return "NaN";
      if ((_value < 0.0D) && (Double.isInfinite(_value))) return "-Infinity";
      if (Double.isInfinite(_value)) return "Infinity";
      return formatNumbers((int)_value);
    }
    
    int next = _node;
    int root = _document.getDocument();
    int result = 0;
    while ((next >= root) && (!matchesFrom(next))) {
      if (matchesCount(next)) {
        result++;
      }
      next--;
    }
    










    return formatNumbers(result);
  }
  

  public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    return new DefaultAnyNodeCounter(translet, document, iterator);
  }
  
  static class DefaultAnyNodeCounter
    extends AnyNodeCounter
  {
    public DefaultAnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) { super(document, iterator); }
    
    public String getCounter() {
      int result;
      int result;
      if (_value != -2.147483648E9D)
      {
        if (_value == 0.0D) return "0";
        if (Double.isNaN(_value)) return "NaN";
        if ((_value < 0.0D) && (Double.isInfinite(_value))) return "-Infinity";
        if (Double.isInfinite(_value)) return "Infinity";
        result = (int)_value;
      }
      else {
        int next = _node;
        result = 0;
        int ntype = _document.getExpandedTypeID(_node);
        int root = _document.getDocument();
        while (next >= 0) {
          if (ntype == _document.getExpandedTypeID(next)) {
            result++;
          }
          

          if (next == root) {
            break;
          }
          
          next--;
        }
      }
      
      return formatNumbers(result);
    }
  }
}
