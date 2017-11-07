package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.lang.reflect.Member;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;


































public class RecursiveFunctionObject
  extends FunctionObject
{
  public RecursiveFunctionObject(String name, Member methodOrConstructor, Scriptable scope)
  {
    super(name, methodOrConstructor, scope);
  }
  



  public boolean has(String name, Scriptable start)
  {
    if (super.has(name, start)) {
      return true;
    }
    for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass(); 
        c != null; c = c.getSuperclass()) {
      Object scripatble = getParentScope().get(c.getSimpleName(), this);
      if (((scripatble instanceof Scriptable)) && (((Scriptable)scripatble).has(name, start))) {
        return true;
      }
    }
    return false;
  }
  



  public Object[] getIds()
  {
    Set<Object> objects = new LinkedHashSet();
    for (Object o : super.getIds()) {
      objects.add(o);
    }
    for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass(); 
        c != null; c = c.getSuperclass()) {
      Object scripatble = getParentScope().get(c.getSimpleName(), this);
      if ((scripatble instanceof Scriptable)) {
        for (Object id : ((Scriptable)scripatble).getIds()) {
          objects.add(id);
        }
      }
    }
    return objects.toArray(new Object[objects.size()]);
  }
  



  public BrowserVersion getBrowserVersion()
  {
    Scriptable parent = getParentScope();
    while (!(parent instanceof Window)) {
      parent = parent.getParentScope();
    }
    return ((Window)parent).getBrowserVersion();
  }
  



  public String getFunctionName()
  {
    String functionName = super.getFunctionName();
    String str1; switch ((str1 = functionName).hashCode()) {case -1926269803:  if (str1.equals("Option")) {} break; case -1925471606:  if (str1.equals("webkitIDBRequest")) {} break; case -1777131805:  if (str1.equals("WebKitTransitionEvent")) {} break; case -1672322848:  if (str1.equals("webkitIDBDatabase")) {} break; case -1662882790:  if (str1.equals("webkitOfflineAudioContext")) {} break; case -1519448743:  if (str1.equals("webkitIDBTransaction")) {} break; case -1275975743:  if (str1.equals("WebKitMutationObserver")) {} break; case -1230218981:  if (str1.equals("webkitAudioContext")) {} break; case -944137331:  if (str1.equals("webkitIDBIndex")) {} break; case -653996195:  if (str1.equals("webkitIDBObjectStore")) {} break; case -512401773:  if (str1.equals("webkitSpeechRecognition")) {} break; case -463655155:  if (str1.equals("webkitURL")) {} break; case -356376542:  if (str1.equals("webkitMediaStream")) {} break; case 70760763:  if (str1.equals("Image")) break; break; case 181920005:  if (str1.equals("webkitIDBFactory")) {} break; case 313802689:  if (str1.equals("webkitSpeechGrammarList")) {} break; case 337400587:  if (str1.equals("V8BreakIterator")) {} break; case 631633915:  if (str1.equals("webkitIDBCursor")) {} break; case 638560053:  if (str1.equals("webkitSpeechRecognitionError")) {} break; case 638666695:  if (str1.equals("webkitSpeechRecognitionEvent")) {} break; case 1295482815:  if (str1.equals("webkitRTCPeerConnection")) {} break; case 1321737603:  if (str1.equals("webkitIDBKeyRange")) {} break; case 1401164931:  if (str1.equals("webkitSpeechGrammar")) {} break; case 1722609944:  if (!str1.equals("WebKitAnimationEvent")) {
        return functionName;
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_IMAGE_HTML_IMAGE_ELEMENT)) {
          return "HTMLImageElement";
          



          if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OPTION_HTML_OPTION_ELEMENT)) {
            return "HTMLOptionElement";
            



            return "v8BreakIterator";
            

            return "RTCPeerConnection";
            

            return "SpeechRecognition";
          }
        }
      } else { return "AnimationEvent";
        

        return "MutationObserver";
        

        return "TransitionEvent";
        

        return "AudioContext";
        

        return "IDBCursor";
        

        return "IDBDatabase";
        

        return "IDBFactory";
        

        return "IDBIndex";
        

        return "IDBKeyRange";
        

        return "IDBObjectStore";
        

        return "IDBRequest";
        

        return "IDBTransaction";
        

        return "MediaStream";
        

        return "OfflineAudioContext";
        

        return "SpeechGrammar";
        

        return "SpeechGrammarList";
        

        return "SpeechRecognitionError";
        

        return "SpeechRecognitionEvent";
        

        return "URL";
      }
      break;
    }
    return functionName;
  }
  
  /* Error */
  public Object get(String name, Scriptable start)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 104	net/sourceforge/htmlunit/corejs/javascript/FunctionObject:getFunctionName	()Ljava/lang/String;
    //   4: astore_3
    //   5: ldc -29
    //   7: aload_1
    //   8: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   11: ifeq +67 -> 78
    //   14: aload_3
    //   15: dup
    //   16: astore 4
    //   18: invokevirtual 106	java/lang/String:hashCode	()I
    //   21: lookupswitch	default:+57->78, 67043:+27->48, 77388366:+40->61
    //   48: aload 4
    //   50: ldc -27
    //   52: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   55: ifne +19 -> 74
    //   58: goto +20 -> 78
    //   61: aload 4
    //   63: ldc -25
    //   65: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   68: ifne +6 -> 74
    //   71: goto +7 -> 78
    //   74: getstatic 233	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:NOT_FOUND	Ljava/lang/Object;
    //   77: areturn
    //   78: aload_0
    //   79: aload_1
    //   80: aload_2
    //   81: invokespecial 236	net/sourceforge/htmlunit/corejs/javascript/FunctionObject:get	(Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;
    //   84: astore 5
    //   86: aload 5
    //   88: getstatic 233	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:NOT_FOUND	Ljava/lang/Object;
    //   91: if_acmpne +170 -> 261
    //   94: ldc -116
    //   96: aload_3
    //   97: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   100: ifne +161 -> 261
    //   103: ldc 111
    //   105: aload_3
    //   106: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   109: ifne +152 -> 261
    //   112: ldc -19
    //   114: aload_3
    //   115: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   118: ifeq +16 -> 134
    //   121: aload_0
    //   122: invokevirtual 162	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:getBrowserVersion	()Lcom/gargoylesoftware/htmlunit/BrowserVersion;
    //   125: getstatic 239	com/gargoylesoftware/htmlunit/BrowserVersionFeatures:JS_WEBGL_CONTEXT_EVENT_CONSTANTS	Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;
    //   128: invokevirtual 169	com/gargoylesoftware/htmlunit/BrowserVersion:hasFeature	(Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;)Z
    //   131: ifeq +130 -> 261
    //   134: aload_0
    //   135: invokevirtual 242	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:getPrototypeProperty	()Ljava/lang/Object;
    //   138: invokevirtual 246	java/lang/Object:getClass	()Ljava/lang/Class;
    //   141: astore 6
    //   143: aload_0
    //   144: invokevirtual 162	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:getBrowserVersion	()Lcom/gargoylesoftware/htmlunit/BrowserVersion;
    //   147: astore 7
    //   149: goto +94 -> 243
    //   152: aload 6
    //   154: ldc -7
    //   156: invokevirtual 251	java/lang/Class:asSubclass	(Ljava/lang/Class;)Ljava/lang/Class;
    //   159: aload 7
    //   161: invokestatic 255	com/gargoylesoftware/htmlunit/javascript/configuration/AbstractJavaScriptConfiguration:getClassConfiguration	(Ljava/lang/Class;Lcom/gargoylesoftware/htmlunit/BrowserVersion;)Lcom/gargoylesoftware/htmlunit/javascript/configuration/ClassConfiguration;
    //   164: astore 8
    //   166: aload 8
    //   168: ifnull +68 -> 236
    //   171: aload 8
    //   173: invokevirtual 261	com/gargoylesoftware/htmlunit/javascript/configuration/ClassConfiguration:getConstants	()Ljava/util/List;
    //   176: invokeinterface 267 1 0
    //   181: astore 10
    //   183: goto +43 -> 226
    //   186: aload 10
    //   188: invokeinterface 273 1 0
    //   193: checkcast 278	com/gargoylesoftware/htmlunit/javascript/configuration/ClassConfiguration$ConstantInfo
    //   196: astore 9
    //   198: aload 9
    //   200: invokevirtual 280	com/gargoylesoftware/htmlunit/javascript/configuration/ClassConfiguration$ConstantInfo:getName	()Ljava/lang/String;
    //   203: aload_1
    //   204: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   207: ifeq +19 -> 226
    //   210: aload_0
    //   211: invokevirtual 242	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:getPrototypeProperty	()Ljava/lang/Object;
    //   214: checkcast 48	net/sourceforge/htmlunit/corejs/javascript/Scriptable
    //   217: aload_1
    //   218: invokestatic 283	net/sourceforge/htmlunit/corejs/javascript/ScriptableObject:getProperty	(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
    //   221: astore 5
    //   223: goto +13 -> 236
    //   226: aload 10
    //   228: invokeinterface 289 1 0
    //   233: ifne -47 -> 186
    //   236: aload 6
    //   238: invokevirtual 34	java/lang/Class:getSuperclass	()Ljava/lang/Class;
    //   241: astore 6
    //   243: aload 5
    //   245: getstatic 233	com/gargoylesoftware/htmlunit/javascript/RecursiveFunctionObject:NOT_FOUND	Ljava/lang/Object;
    //   248: if_acmpne +13 -> 261
    //   251: ldc -7
    //   253: aload 6
    //   255: invokevirtual 293	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
    //   258: ifne -106 -> 152
    //   261: aload 5
    //   263: areturn
    // Line number table:
    //   Java source line #200	-> byte code offset #0
    //   Java source line #201	-> byte code offset #5
    //   Java source line #202	-> byte code offset #14
    //   Java source line #205	-> byte code offset #74
    //   Java source line #210	-> byte code offset #78
    //   Java source line #212	-> byte code offset #86
    //   Java source line #213	-> byte code offset #112
    //   Java source line #214	-> byte code offset #121
    //   Java source line #215	-> byte code offset #134
    //   Java source line #217	-> byte code offset #143
    //   Java source line #218	-> byte code offset #149
    //   Java source line #220	-> byte code offset #152
    //   Java source line #219	-> byte code offset #161
    //   Java source line #221	-> byte code offset #166
    //   Java source line #222	-> byte code offset #171
    //   Java source line #223	-> byte code offset #198
    //   Java source line #224	-> byte code offset #210
    //   Java source line #225	-> byte code offset #223
    //   Java source line #222	-> byte code offset #226
    //   Java source line #229	-> byte code offset #236
    //   Java source line #218	-> byte code offset #243
    //   Java source line #232	-> byte code offset #261
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	264	0	this	RecursiveFunctionObject
    //   0	264	1	name	String
    //   0	264	2	start	Scriptable
    //   4	111	3	superFunctionName	String
    //   16	46	4	str1	String
    //   84	178	5	value	Object
    //   141	113	6	klass	Class<?>
    //   147	13	7	browserVersion	BrowserVersion
    //   164	8	8	config	com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration
    //   196	3	9	constantInfo	com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo
    //   181	46	10	localIterator	java.util.Iterator
  }
  
  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    Object object = super.call(cx, scope, thisObj, args);
    if ((object instanceof Scriptable)) {
      Scriptable result = (Scriptable)object;
      if (result.getPrototype() == null) {
        Scriptable proto = getClassPrototype();
        if (result != proto) {
          result.setPrototype(proto);
        }
      }
      if (result.getParentScope() == null) {
        Scriptable parent = getParentScope();
        if (result != parent) {
          result.setParentScope(parent);
        }
      }
    }
    return object;
  }
}
