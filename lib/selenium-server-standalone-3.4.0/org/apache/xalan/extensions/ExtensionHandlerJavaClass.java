package org.apache.xalan.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;









































public class ExtensionHandlerJavaClass
  extends ExtensionHandlerJava
{
  private Class m_classObj = null;
  





  private Object m_defaultInstance = null;
  










  public ExtensionHandlerJavaClass(String namespaceUri, String scriptLang, String className)
  {
    super(namespaceUri, scriptLang, className);
    try
    {
      m_classObj = getClassForName(className);
    }
    catch (ClassNotFoundException e) {}
  }
  














  public boolean isFunctionAvailable(String function)
  {
    Method[] methods = m_classObj.getMethods();
    int nMethods = methods.length;
    for (int i = 0; i < nMethods; i++)
    {
      if (methods[i].getName().equals(function))
        return true;
    }
    return false;
  }
  









  public boolean isElementAvailable(String element)
  {
    Method[] methods = m_classObj.getMethods();
    int nMethods = methods.length;
    for (int i = 0; i < nMethods; i++)
    {
      if (methods[i].getName().equals(element))
      {
        Class[] paramTypes = methods[i].getParameterTypes();
        if ((paramTypes.length == 2) && (paramTypes[0].isAssignableFrom(XSLProcessorContext.class)) && (paramTypes[1].isAssignableFrom(ElemExtensionCall.class)))
        {




          return true;
        }
      }
    }
    return false;
  }
  













































  public Object callFunction(String funcName, Vector args, Object methodKey, ExpressionContext exprContext)
    throws TransformerException
  {
    try
    {
      TransformerImpl trans = exprContext != null ? (TransformerImpl)exprContext.getXPathContext().getOwnerObject() : null;
      Class[] paramTypes;
      if (funcName.equals("new"))
      {
        Object[] methodArgs = new Object[args.size()];
        Object[][] convertedArgs = new Object[1][];
        for (int i = 0; i < methodArgs.length; i++)
        {
          methodArgs[i] = args.get(i);
        }
        Constructor c = null;
        if (methodKey != null) {
          c = (Constructor)getFromCache(methodKey, null, methodArgs);
        }
        if ((c != null) && (!trans.getDebug()))
        {
          try
          {
            paramTypes = c.getParameterTypes();
            MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
            
            return c.newInstance(convertedArgs[0]);
          }
          catch (InvocationTargetException ite)
          {
            throw ite;
          }
          catch (Exception e) {}
        }
        


        c = MethodResolver.getConstructor(m_classObj, methodArgs, convertedArgs, exprContext);
        


        if (methodKey != null) {
          putToCache(methodKey, null, methodArgs, c);
        }
        if ((trans != null) && (trans.getDebug())) {
          trans.getTraceManager().fireExtensionEvent(new ExtensionEvent(trans, c, convertedArgs[0]));
          Object result;
          try
          {
            result = c.newInstance(convertedArgs[0]);
          } catch (Exception e) {
            throw e;
          } finally {
            trans.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(trans, c, convertedArgs[0]));
          }
          
          return result;
        }
        return c.newInstance(convertedArgs[0]);
      }
      




      Object targetObject = null;
      Object[] methodArgs = new Object[args.size()];
      Object[][] convertedArgs = new Object[1][];
      for (int i = 0; i < methodArgs.length; i++)
      {
        methodArgs[i] = args.get(i);
      }
      Method m = null;
      if (methodKey != null) {
        m = (Method)getFromCache(methodKey, null, methodArgs);
      }
      if ((m != null) && (!trans.getDebug()))
      {
        try
        {
          paramTypes = m.getParameterTypes();
          MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
          
          if (Modifier.isStatic(m.getModifiers())) {
            return m.invoke(null, convertedArgs[0]);
          }
          


          int nTargetArgs = convertedArgs[0].length;
          if (ExpressionContext.class.isAssignableFrom(paramTypes[0]))
            nTargetArgs--;
          if (methodArgs.length <= nTargetArgs) {
            return m.invoke(m_defaultInstance, convertedArgs[0]);
          }
          
          targetObject = methodArgs[0];
          
          if ((targetObject instanceof XObject)) {
            targetObject = ((XObject)targetObject).object();
          }
          return m.invoke(targetObject, convertedArgs[0]);

        }
        catch (InvocationTargetException ite)
        {

          throw ite;
        }
        catch (Exception e) {}
      }
      
      int resolveType;
      
      int resolveType;
      if (args.size() > 0)
      {
        targetObject = methodArgs[0];
        
        if ((targetObject instanceof XObject))
          targetObject = ((XObject)targetObject).object();
        int resolveType;
        if (m_classObj.isAssignableFrom(targetObject.getClass())) {
          resolveType = 4;
        } else {
          resolveType = 3;
        }
      }
      else {
        targetObject = null;
        resolveType = 3;
      }
      
      m = MethodResolver.getMethod(m_classObj, funcName, methodArgs, convertedArgs, exprContext, resolveType);
      




      if (methodKey != null) {
        putToCache(methodKey, null, methodArgs, m);
      }
      if (4 == resolveType) {
        if ((trans != null) && (trans.getDebug())) {
          trans.getTraceManager().fireExtensionEvent(m, targetObject, convertedArgs[0]);
          Object result;
          try
          {
            result = m.invoke(targetObject, convertedArgs[0]);
          } catch (Exception e) {
            throw e;
          } finally {
            trans.getTraceManager().fireExtensionEndEvent(m, targetObject, convertedArgs[0]);
          }
          
          return result;
        }
        return m.invoke(targetObject, convertedArgs[0]);
      }
      

      if (Modifier.isStatic(m.getModifiers())) {
        if ((trans != null) && (trans.getDebug())) {
          trans.getTraceManager().fireExtensionEvent(m, null, convertedArgs[0]);
          Object result;
          try
          {
            result = m.invoke(null, convertedArgs[0]);
          } catch (Exception e) {
            throw e;
          } finally {
            trans.getTraceManager().fireExtensionEndEvent(m, null, convertedArgs[0]);
          }
          
          return result;
        }
        return m.invoke(null, convertedArgs[0]);
      }
      

      if (null == m_defaultInstance)
      {
        if ((trans != null) && (trans.getDebug())) {
          trans.getTraceManager().fireExtensionEvent(new ExtensionEvent(trans, m_classObj));
          try
          {
            m_defaultInstance = m_classObj.newInstance();
          } catch (Exception e) {
            throw e;
          } finally {
            trans.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(trans, m_classObj));
          }
        }
        else {
          m_defaultInstance = m_classObj.newInstance();
        } }
      if ((trans != null) && (trans.getDebug())) {
        trans.getTraceManager().fireExtensionEvent(m, m_defaultInstance, convertedArgs[0]);
        Object result;
        try
        {
          result = m.invoke(m_defaultInstance, convertedArgs[0]);
        } catch (Exception e) {
          throw e;
        } finally {
          trans.getTraceManager().fireExtensionEndEvent(m, m_defaultInstance, convertedArgs[0]);
        }
        
        return result;
      }
      return m.invoke(m_defaultInstance, convertedArgs[0]);


    }
    catch (InvocationTargetException ite)
    {


      Throwable resultException = ite;
      Throwable targetException = ite.getTargetException();
      
      if ((targetException instanceof TransformerException))
        throw ((TransformerException)targetException);
      if (targetException != null) {
        resultException = targetException;
      }
      throw new TransformerException(resultException);

    }
    catch (Exception e)
    {
      throw new TransformerException(e);
    }
  }
  











  public Object callFunction(FuncExtFunction extFunction, Vector args, ExpressionContext exprContext)
    throws TransformerException
  {
    return callFunction(extFunction.getFunctionName(), args, extFunction.getMethodKey(), exprContext);
  }
  
  /* Error */
  public void processElement(String localPart, org.apache.xalan.templates.ElemTemplateElement element, TransformerImpl transformer, org.apache.xalan.templates.Stylesheet stylesheetTree, Object methodKey)
    throws TransformerException, java.io.IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: aload_0
    //   4: aload 5
    //   6: aconst_null
    //   7: aconst_null
    //   8: invokevirtual 28	org/apache/xalan/extensions/ExtensionHandlerJavaClass:getFromCache	(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   11: checkcast 43	java/lang/reflect/Method
    //   14: astore 7
    //   16: aconst_null
    //   17: aload 7
    //   19: if_acmpne +165 -> 184
    //   22: aload_0
    //   23: getfield 7	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_classObj	Ljava/lang/Class;
    //   26: aload_1
    //   27: invokestatic 63	org/apache/xalan/extensions/MethodResolver:getElementMethod	(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Method;
    //   30: astore 7
    //   32: aconst_null
    //   33: aload_0
    //   34: getfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   37: if_acmpne +116 -> 153
    //   40: aload 7
    //   42: invokevirtual 44	java/lang/reflect/Method:getModifiers	()I
    //   45: invokestatic 45	java/lang/reflect/Modifier:isStatic	(I)Z
    //   48: ifne +105 -> 153
    //   51: aload_3
    //   52: invokevirtual 30	org/apache/xalan/transformer/TransformerImpl:getDebug	()Z
    //   55: ifeq +87 -> 142
    //   58: aload_3
    //   59: invokevirtual 38	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   62: new 39	org/apache/xalan/trace/ExtensionEvent
    //   65: dup
    //   66: aload_3
    //   67: aload_0
    //   68: getfield 7	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_classObj	Ljava/lang/Class;
    //   71: invokespecial 55	org/apache/xalan/trace/ExtensionEvent:<init>	(Lorg/apache/xalan/transformer/TransformerImpl;Ljava/lang/Class;)V
    //   74: invokevirtual 41	org/apache/xalan/trace/TraceManager:fireExtensionEvent	(Lorg/apache/xalan/trace/ExtensionEvent;)V
    //   77: aload_0
    //   78: aload_0
    //   79: getfield 7	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_classObj	Ljava/lang/Class;
    //   82: invokevirtual 56	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   85: putfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   88: aload_3
    //   89: invokevirtual 38	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   92: new 39	org/apache/xalan/trace/ExtensionEvent
    //   95: dup
    //   96: aload_3
    //   97: aload_0
    //   98: getfield 7	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_classObj	Ljava/lang/Class;
    //   101: invokespecial 55	org/apache/xalan/trace/ExtensionEvent:<init>	(Lorg/apache/xalan/transformer/TransformerImpl;Ljava/lang/Class;)V
    //   104: invokevirtual 42	org/apache/xalan/trace/TraceManager:fireExtensionEndEvent	(Lorg/apache/xalan/trace/ExtensionEvent;)V
    //   107: goto +32 -> 139
    //   110: astore 8
    //   112: aload 8
    //   114: athrow
    //   115: astore 9
    //   117: aload_3
    //   118: invokevirtual 38	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   121: new 39	org/apache/xalan/trace/ExtensionEvent
    //   124: dup
    //   125: aload_3
    //   126: aload_0
    //   127: getfield 7	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_classObj	Ljava/lang/Class;
    //   130: invokespecial 55	org/apache/xalan/trace/ExtensionEvent:<init>	(Lorg/apache/xalan/transformer/TransformerImpl;Ljava/lang/Class;)V
    //   133: invokevirtual 42	org/apache/xalan/trace/TraceManager:fireExtensionEndEvent	(Lorg/apache/xalan/trace/ExtensionEvent;)V
    //   136: aload 9
    //   138: athrow
    //   139: goto +14 -> 153
    //   142: aload_0
    //   143: aload_0
    //   144: getfield 7	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_classObj	Ljava/lang/Class;
    //   147: invokevirtual 56	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   150: putfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   153: goto +20 -> 173
    //   156: astore 8
    //   158: new 58	javax/xml/transform/TransformerException
    //   161: dup
    //   162: aload 8
    //   164: invokevirtual 64	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   167: aload 8
    //   169: invokespecial 65	javax/xml/transform/TransformerException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   172: athrow
    //   173: aload_0
    //   174: aload 5
    //   176: aconst_null
    //   177: aconst_null
    //   178: aload 7
    //   180: invokevirtual 37	org/apache/xalan/extensions/ExtensionHandlerJavaClass:putToCache	(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   183: pop
    //   184: new 66	org/apache/xalan/extensions/XSLProcessorContext
    //   187: dup
    //   188: aload_3
    //   189: aload 4
    //   191: invokespecial 67	org/apache/xalan/extensions/XSLProcessorContext:<init>	(Lorg/apache/xalan/transformer/TransformerImpl;Lorg/apache/xalan/templates/Stylesheet;)V
    //   194: astore 8
    //   196: aload_3
    //   197: invokevirtual 30	org/apache/xalan/transformer/TransformerImpl:getDebug	()Z
    //   200: ifeq +121 -> 321
    //   203: aload_3
    //   204: invokevirtual 38	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   207: aload 7
    //   209: aload_0
    //   210: getfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   213: iconst_2
    //   214: anewarray 25	java/lang/Object
    //   217: dup
    //   218: iconst_0
    //   219: aload 8
    //   221: aastore
    //   222: dup
    //   223: iconst_1
    //   224: aload_2
    //   225: aastore
    //   226: invokevirtual 53	org/apache/xalan/trace/TraceManager:fireExtensionEvent	(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)V
    //   229: aload 7
    //   231: aload_0
    //   232: getfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   235: iconst_2
    //   236: anewarray 25	java/lang/Object
    //   239: dup
    //   240: iconst_0
    //   241: aload 8
    //   243: aastore
    //   244: dup
    //   245: iconst_1
    //   246: aload_2
    //   247: aastore
    //   248: invokevirtual 46	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   251: astore 6
    //   253: aload_3
    //   254: invokevirtual 38	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   257: aload 7
    //   259: aload_0
    //   260: getfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   263: iconst_2
    //   264: anewarray 25	java/lang/Object
    //   267: dup
    //   268: iconst_0
    //   269: aload 8
    //   271: aastore
    //   272: dup
    //   273: iconst_1
    //   274: aload_2
    //   275: aastore
    //   276: invokevirtual 54	org/apache/xalan/trace/TraceManager:fireExtensionEndEvent	(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)V
    //   279: goto +39 -> 318
    //   282: astore 9
    //   284: aload 9
    //   286: athrow
    //   287: astore 10
    //   289: aload_3
    //   290: invokevirtual 38	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   293: aload 7
    //   295: aload_0
    //   296: getfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   299: iconst_2
    //   300: anewarray 25	java/lang/Object
    //   303: dup
    //   304: iconst_0
    //   305: aload 8
    //   307: aastore
    //   308: dup
    //   309: iconst_1
    //   310: aload_2
    //   311: aastore
    //   312: invokevirtual 54	org/apache/xalan/trace/TraceManager:fireExtensionEndEvent	(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)V
    //   315: aload 10
    //   317: athrow
    //   318: goto +27 -> 345
    //   321: aload 7
    //   323: aload_0
    //   324: getfield 8	org/apache/xalan/extensions/ExtensionHandlerJavaClass:m_defaultInstance	Ljava/lang/Object;
    //   327: iconst_2
    //   328: anewarray 25	java/lang/Object
    //   331: dup
    //   332: iconst_0
    //   333: aload 8
    //   335: aastore
    //   336: dup
    //   337: iconst_1
    //   338: aload_2
    //   339: aastore
    //   340: invokevirtual 46	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   343: astore 6
    //   345: goto +78 -> 423
    //   348: astore 9
    //   350: aload 9
    //   352: invokevirtual 57	java/lang/reflect/InvocationTargetException:getTargetException	()Ljava/lang/Throwable;
    //   355: astore 10
    //   357: aload 10
    //   359: instanceof 58
    //   362: ifeq +9 -> 371
    //   365: aload 10
    //   367: checkcast 58	javax/xml/transform/TransformerException
    //   370: athrow
    //   371: aload 10
    //   373: ifnull +18 -> 391
    //   376: new 58	javax/xml/transform/TransformerException
    //   379: dup
    //   380: aload 10
    //   382: invokevirtual 68	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   385: aload 10
    //   387: invokespecial 65	javax/xml/transform/TransformerException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   390: athrow
    //   391: new 58	javax/xml/transform/TransformerException
    //   394: dup
    //   395: aload 9
    //   397: invokevirtual 69	java/lang/reflect/InvocationTargetException:getMessage	()Ljava/lang/String;
    //   400: aload 9
    //   402: invokespecial 65	javax/xml/transform/TransformerException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   405: athrow
    //   406: astore 9
    //   408: new 58	javax/xml/transform/TransformerException
    //   411: dup
    //   412: aload 9
    //   414: invokevirtual 64	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   417: aload 9
    //   419: invokespecial 65	javax/xml/transform/TransformerException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   422: athrow
    //   423: aload 6
    //   425: ifnull +12 -> 437
    //   428: aload 8
    //   430: aload 4
    //   432: aload 6
    //   434: invokevirtual 70	org/apache/xalan/extensions/XSLProcessorContext:outputToResultTree	(Lorg/apache/xalan/templates/Stylesheet;Ljava/lang/Object;)V
    //   437: return
    // Line number table:
    //   Java source line #467	-> byte code offset #0
    //   Java source line #469	-> byte code offset #3
    //   Java source line #470	-> byte code offset #16
    //   Java source line #474	-> byte code offset #22
    //   Java source line #475	-> byte code offset #32
    //   Java source line #477	-> byte code offset #51
    //   Java source line #478	-> byte code offset #58
    //   Java source line #481	-> byte code offset #77
    //   Java source line #485	-> byte code offset #88
    //   Java source line #487	-> byte code offset #107
    //   Java source line #482	-> byte code offset #110
    //   Java source line #483	-> byte code offset #112
    //   Java source line #485	-> byte code offset #115
    //   Java source line #489	-> byte code offset #142
    //   Java source line #496	-> byte code offset #153
    //   Java source line #492	-> byte code offset #156
    //   Java source line #495	-> byte code offset #158
    //   Java source line #497	-> byte code offset #173
    //   Java source line #500	-> byte code offset #184
    //   Java source line #505	-> byte code offset #196
    //   Java source line #506	-> byte code offset #203
    //   Java source line #509	-> byte code offset #229
    //   Java source line #513	-> byte code offset #253
    //   Java source line #515	-> byte code offset #279
    //   Java source line #510	-> byte code offset #282
    //   Java source line #511	-> byte code offset #284
    //   Java source line #513	-> byte code offset #287
    //   Java source line #517	-> byte code offset #321
    //   Java source line #535	-> byte code offset #345
    //   Java source line #519	-> byte code offset #348
    //   Java source line #521	-> byte code offset #350
    //   Java source line #523	-> byte code offset #357
    //   Java source line #524	-> byte code offset #365
    //   Java source line #525	-> byte code offset #371
    //   Java source line #526	-> byte code offset #376
    //   Java source line #529	-> byte code offset #391
    //   Java source line #531	-> byte code offset #406
    //   Java source line #534	-> byte code offset #408
    //   Java source line #537	-> byte code offset #423
    //   Java source line #539	-> byte code offset #428
    //   Java source line #542	-> byte code offset #437
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	438	0	this	ExtensionHandlerJavaClass
    //   0	438	1	localPart	String
    //   0	438	2	element	org.apache.xalan.templates.ElemTemplateElement
    //   0	438	3	transformer	TransformerImpl
    //   0	438	4	stylesheetTree	org.apache.xalan.templates.Stylesheet
    //   0	438	5	methodKey	Object
    //   1	432	6	result	Object
    //   14	308	7	m	Method
    //   110	3	8	e	Exception
    //   156	12	8	e	Exception
    //   194	235	8	xpc	XSLProcessorContext
    //   115	22	9	localObject1	Object
    //   282	3	9	e	Exception
    //   348	53	9	e	InvocationTargetException
    //   406	12	9	e	Exception
    //   287	29	10	localObject2	Object
    //   355	31	10	targetException	Throwable
    // Exception table:
    //   from	to	target	type
    //   77	88	110	java/lang/Exception
    //   77	88	115	finally
    //   110	117	115	finally
    //   22	153	156	java/lang/Exception
    //   229	253	282	java/lang/Exception
    //   229	253	287	finally
    //   282	289	287	finally
    //   196	345	348	java/lang/reflect/InvocationTargetException
    //   196	345	406	java/lang/Exception
  }
}
