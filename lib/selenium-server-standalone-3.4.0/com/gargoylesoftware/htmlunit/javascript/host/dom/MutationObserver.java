package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.CharacterDataChangeEvent;
import com.gargoylesoftware.htmlunit.html.CharacterDataChangeListener;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.TopLevel.Builtins;


























@JsxClass
public class MutationObserver
  extends SimpleScriptable
  implements HtmlAttributeChangeListener, CharacterDataChangeListener
{
  private Function function_;
  private Node node_;
  private boolean childList_;
  private boolean attaributes_;
  private boolean attributeOldValue_;
  private NativeArray attributeFilter_;
  private boolean characterData_;
  private boolean characterDataOldValue_;
  private boolean subtree_;
  
  public MutationObserver() {}
  
  @JsxConstructor
  public MutationObserver(Function function)
  {
    function_ = function;
  }
  




  @JsxFunction
  public void observe(Node node, NativeObject options)
  {
    node_ = node;
    attaributes_ = Boolean.TRUE.equals(options.get("attributes"));
    attributeOldValue_ = Boolean.TRUE.equals(options.get("attributeOldValue"));
    childList_ = Boolean.TRUE.equals(options.get("childList"));
    characterData_ = Boolean.TRUE.equals(options.get("characterData"));
    characterDataOldValue_ = Boolean.TRUE.equals(options.get("characterDataOldValue"));
    subtree_ = Boolean.TRUE.equals(options.get("subtree"));
    attributeFilter_ = ((NativeArray)options.get("attributeFilter"));
    
    if ((attaributes_) && ((node_.getDomNodeOrDie() instanceof HtmlElement))) {
      ((HtmlElement)node_.getDomNodeOrDie()).addHtmlAttributeChangeListener(this);
    }
    if (characterData_) {
      node.getDomNodeOrDie().addCharacterDataChangeListener(this);
    }
  }
  


  @JsxFunction
  public void disconnect()
  {
    if ((attaributes_) && ((node_.getDomNodeOrDie() instanceof HtmlElement))) {
      ((HtmlElement)node_.getDomNodeOrDie()).removeHtmlAttributeChangeListener(this);
    }
    if (characterData_) {
      node_.getDomNodeOrDie().removeCharacterDataChangeListener(this);
    }
  }
  



  @JsxFunction
  public NativeArray takeRecords()
  {
    return new NativeArray(0L);
  }
  



  public void characterDataChanged(CharacterDataChangeEvent event)
  {
    ScriptableObject target = event.getCharacterData().getScriptableObject();
    if ((subtree_) || (target == node_)) {
      MutationRecord mutationRecord = new MutationRecord();
      Scriptable scope = getParentScope();
      mutationRecord.setParentScope(scope);
      mutationRecord.setPrototype(getPrototype(mutationRecord.getClass()));
      
      mutationRecord.setType("characterData");
      mutationRecord.setTarget(target);
      if (characterDataOldValue_) {
        mutationRecord.setOldValue(event.getOldValue());
      }
      
      NativeArray array = new NativeArray(new Object[] { mutationRecord });
      ScriptRuntime.setBuiltinProtoAndParent(array, scope, TopLevel.Builtins.Array);
      Context context = Context.enter();
      try {
        function_.call(context, scope, this, new Object[] { array });
      }
      finally {
        Context.exit();
      }
    }
  }
  





  public void attributeAdded(HtmlAttributeChangeEvent event) {}
  




  public void attributeRemoved(HtmlAttributeChangeEvent event) {}
  




  public void attributeReplaced(HtmlAttributeChangeEvent event)
  {
    HtmlElement target = event.getHtmlElement();
    if ((subtree_) || (target == node_.getDomNodeOrDie())) {
      String attributeName = event.getName();
      if ((attributeFilter_ == null) || (attributeFilter_.contains(attributeName))) {
        MutationRecord mutationRecord = new MutationRecord();
        Scriptable scope = getParentScope();
        mutationRecord.setParentScope(scope);
        mutationRecord.setPrototype(getPrototype(mutationRecord.getClass()));
        
        mutationRecord.setType("attributes");
        mutationRecord.setTarget(target.getScriptableObject());
        if (attributeOldValue_) {
          mutationRecord.setOldValue(event.getValue());
        }
        
        NativeArray array = new NativeArray(new Object[] { mutationRecord });
        ScriptRuntime.setBuiltinProtoAndParent(array, scope, TopLevel.Builtins.Array);
        Context context = Context.enter();
        try {
          function_.call(context, scope, this, new Object[] { array });
        }
        finally {
          Context.exit();
        }
      }
    }
  }
}
