package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
























































































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class XPathResult
  extends SimpleScriptable
{
  @JsxConstant
  public static final int ANY_TYPE = 0;
  @JsxConstant
  public static final int NUMBER_TYPE = 1;
  @JsxConstant
  public static final int STRING_TYPE = 2;
  @JsxConstant
  public static final int BOOLEAN_TYPE = 3;
  @JsxConstant
  public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;
  @JsxConstant
  public static final int ORDERED_NODE_ITERATOR_TYPE = 5;
  @JsxConstant
  public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;
  @JsxConstant
  public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;
  @JsxConstant
  public static final int ANY_UNORDERED_NODE_TYPE = 8;
  @JsxConstant
  public static final int FIRST_ORDERED_NODE_TYPE = 9;
  private List<?> result_;
  private int resultType_;
  private int iteratorIndex_;
  
  @JsxConstructor
  public XPathResult() {}
  
  void init(List<?> result, int type)
  {
    result_ = result;
    resultType_ = -1;
    if (result_.size() == 1) {
      Object o = result_.get(0);
      if ((o instanceof Number)) {
        resultType_ = 1;
      }
      else if ((o instanceof String)) {
        resultType_ = 2;
      }
      else if ((o instanceof Boolean)) {
        resultType_ = 3;
      }
    }
    
    if (resultType_ == -1) {
      if (type != 0) {
        resultType_ = type;
      }
      else {
        resultType_ = 4;
      }
    }
    iteratorIndex_ = 0;
  }
  



  @JsxGetter
  public int getResultType()
  {
    return resultType_;
  }
  



  @JsxGetter
  public int getSnapshotLength()
  {
    if ((resultType_ != 6) && (resultType_ != 7)) {
      throw Context.reportRuntimeError("Cannot get snapshotLength for type: " + resultType_);
    }
    return result_.size();
  }
  



  @JsxGetter
  public Node getSingleNodeValue()
  {
    if ((resultType_ != 8) && (resultType_ != 9)) {
      throw Context.reportRuntimeError("Cannot get singleNodeValue for type: " + resultType_);
    }
    if (!result_.isEmpty()) {
      return (Node)((DomNode)result_.get(0)).getScriptableObject();
    }
    return null;
  }
  



  @JsxFunction
  public Node iterateNext()
  {
    if ((resultType_ != 4) && (resultType_ != 5)) {
      throw Context.reportRuntimeError("Cannot get iterateNext for type: " + resultType_);
    }
    if (iteratorIndex_ < result_.size()) {
      return (Node)((DomNode)result_.get(iteratorIndex_++)).getScriptableObject();
    }
    return null;
  }
  





  @JsxFunction
  public Node snapshotItem(int index)
  {
    if ((resultType_ != 6) && (resultType_ != 7)) {
      throw Context.reportRuntimeError("Cannot get snapshotLength for type: " + resultType_);
    }
    if ((index >= 0) && (index < result_.size())) {
      return (Node)((DomNode)result_.get(index)).getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public double getNumberValue()
  {
    if (resultType_ != 1) {
      throw Context.reportRuntimeError("Cannot get numberValue for type: " + resultType_);
    }
    String asString = asString();
    Double answer;
    try {
      answer = Double.valueOf(Double.parseDouble(asString));
    } catch (NumberFormatException e) {
      Double answer;
      answer = Double.valueOf(NaN.0D);
    }
    return answer.doubleValue();
  }
  



  @JsxGetter
  public boolean getBooleanValue()
  {
    if (resultType_ != 3) {
      throw Context.reportRuntimeError("Cannot get booleanValue for type: " + resultType_);
    }
    return result_.size() > 0;
  }
  



  @JsxGetter
  public String getStringValue()
  {
    if (resultType_ != 2) {
      throw Context.reportRuntimeError("Cannot get stringValue for type: " + resultType_);
    }
    return asString();
  }
  
  private String asString() {
    Object resultObj = result_.get(0);
    if ((resultObj instanceof DomAttr)) {
      return ((DomAttr)resultObj).getValue();
    }
    if ((resultObj instanceof DomNode)) {
      return ((DomNode)resultObj).asText();
    }
    return resultObj.toString();
  }
}
