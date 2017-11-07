package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;


























public abstract class SingleNodeCounter
  extends NodeCounter
{
  private static final int[] EmptyArray = new int[0];
  DTMAxisIterator _countSiblings = null;
  

  public SingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    super(translet, document, iterator);
  }
  
  public NodeCounter setStartNode(int node) {
    _node = node;
    _nodeType = _document.getExpandedTypeID(node);
    _countSiblings = _document.getAxisIterator(12);
    return this;
  }
  
  public String getCounter() { int result;
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
      if (!matchesCount(next)) {
        while (((next = _document.getParent(next)) > -1) && 
          (!matchesCount(next)))
        {

          if (matchesFrom(next)) {
            next = -1;
          }
        }
      }
      

      if (next != -1) {
        _countSiblings.setStartNode(next);
        do {
          if (matchesCount(next)) result++;
        } while ((next = _countSiblings.next()) != -1);
      }
      else
      {
        return formatNumbers(EmptyArray);
      }
    }
    return formatNumbers(result);
  }
  

  public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    return new DefaultSingleNodeCounter(translet, document, iterator);
  }
  
  static class DefaultSingleNodeCounter extends SingleNodeCounter
  {
    public DefaultSingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      super(document, iterator);
    }
    
    public NodeCounter setStartNode(int node) {
      _node = node;
      _nodeType = _document.getExpandedTypeID(node);
      _countSiblings = _document.getTypedAxisIterator(12, _document.getExpandedTypeID(node));
      

      return this;
    }
    
    public String getCounter() { int result;
      int result;
      if (_value != -2.147483648E9D)
      {
        if (_value == 0.0D) return "0";
        if (Double.isNaN(_value)) return "NaN";
        if ((_value < 0.0D) && (Double.isInfinite(_value))) return "-Infinity";
        if (Double.isInfinite(_value)) return "Infinity";
        result = (int)_value;
      }
      else
      {
        result = 1;
        _countSiblings.setStartNode(_node);
        int next; while ((next = _countSiblings.next()) != -1) {
          result++;
        }
      }
      return formatNumbers(result);
    }
  }
}
