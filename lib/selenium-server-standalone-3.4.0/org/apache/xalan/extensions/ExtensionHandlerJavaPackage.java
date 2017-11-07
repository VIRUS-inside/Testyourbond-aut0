package org.apache.xalan.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;


























































public class ExtensionHandlerJavaPackage
  extends ExtensionHandlerJava
{
  public ExtensionHandlerJavaPackage(String namespaceUri, String scriptLang, String className)
  {
    super(namespaceUri, scriptLang, className);
  }
  













  public boolean isFunctionAvailable(String function)
  {
    try
    {
      String fullName = m_className + function;
      int lastDot = fullName.lastIndexOf('.');
      if (lastDot >= 0)
      {
        Class myClass = getClassForName(fullName.substring(0, lastDot));
        Method[] methods = myClass.getMethods();
        int nMethods = methods.length;
        function = fullName.substring(lastDot + 1);
        for (int i = 0; i < nMethods; i++)
        {
          if (methods[i].getName().equals(function)) {
            return true;
          }
        }
      }
    }
    catch (ClassNotFoundException cnfe) {}
    return false;
  }
  









  public boolean isElementAvailable(String element)
  {
    try
    {
      String fullName = m_className + element;
      int lastDot = fullName.lastIndexOf('.');
      if (lastDot >= 0)
      {
        Class myClass = getClassForName(fullName.substring(0, lastDot));
        Method[] methods = myClass.getMethods();
        int nMethods = methods.length;
        element = fullName.substring(lastDot + 1);
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
      }
    }
    catch (ClassNotFoundException cnfe) {}
    
    return false;
  }
  

















































  public Object callFunction(String funcName, Vector args, Object methodKey, ExpressionContext exprContext)
    throws TransformerException
  {
    int lastDot = funcName.lastIndexOf('.');
    



    try
    {
      TransformerImpl trans = exprContext != null ? (TransformerImpl)exprContext.getXPathContext().getOwnerObject() : null;
      Class[] paramTypes;
      if (funcName.endsWith(".new"))
      {
        Object[] methodArgs = new Object[args.size()];
        Object[][] convertedArgs = new Object[1][];
        for (int i = 0; i < methodArgs.length; i++)
        {
          methodArgs[i] = args.get(i);
        }
        
        Constructor c = methodKey != null ? (Constructor)getFromCache(methodKey, null, methodArgs) : null;
        

        if (c != null)
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
        


        String className = m_className + funcName.substring(0, lastDot);
        Class classObj;
        try {
          classObj = getClassForName(className);
        }
        catch (ClassNotFoundException e)
        {
          throw new TransformerException(e);
        }
        c = MethodResolver.getConstructor(classObj, methodArgs, convertedArgs, exprContext);
        


        if (methodKey != null) {
          putToCache(methodKey, null, methodArgs, c);
        }
        if ((trans != null) && (trans.getDebug())) {
          trans.getTraceManager().fireExtensionEvent(new ExtensionEvent(trans, c, convertedArgs[0]));
          Object result;
          try {
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
      
      if (-1 != lastDot)
      {
        Object[] methodArgs = new Object[args.size()];
        Object[][] convertedArgs = new Object[1][];
        for (int i = 0; i < methodArgs.length; i++)
        {
          methodArgs[i] = args.get(i);
        }
        Method m = methodKey != null ? (Method)getFromCache(methodKey, null, methodArgs) : null;
        

        if ((m != null) && (!trans.getDebug()))
        {
          try
          {
            paramTypes = m.getParameterTypes();
            MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
            return m.invoke(null, convertedArgs[0]);
          }
          catch (InvocationTargetException ite)
          {
            throw ite;
          }
          catch (Exception e) {}
        }
        


        String className = m_className + funcName.substring(0, lastDot);
        String methodName = funcName.substring(lastDot + 1);
        Class classObj;
        try {
          classObj = getClassForName(className);
        }
        catch (ClassNotFoundException e)
        {
          throw new TransformerException(e);
        }
        m = MethodResolver.getMethod(classObj, methodName, methodArgs, convertedArgs, exprContext, 1);
        




        if (methodKey != null) {
          putToCache(methodKey, null, methodArgs, m);
        }
        if ((trans != null) && (trans.getDebug())) {
          trans.getTraceManager().fireExtensionEvent(m, null, convertedArgs[0]);
          Object result;
          try {
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
      


      if (args.size() < 1)
      {
        throw new TransformerException(XSLMessages.createMessage("ER_INSTANCE_MTHD_CALL_REQUIRES", new Object[] { funcName }));
      }
      
      Object targetObject = args.get(0);
      if ((targetObject instanceof XObject))
        targetObject = ((XObject)targetObject).object();
      Object[] methodArgs = new Object[args.size() - 1];
      Object[][] convertedArgs = new Object[1][];
      for (int i = 0; i < methodArgs.length; i++)
      {
        methodArgs[i] = args.get(i + 1);
      }
      Method m = methodKey != null ? (Method)getFromCache(methodKey, targetObject, methodArgs) : null;
      

      if (m != null)
      {
        try
        {
          paramTypes = m.getParameterTypes();
          MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
          return m.invoke(targetObject, convertedArgs[0]);
        }
        catch (InvocationTargetException ite)
        {
          throw ite;
        }
        catch (Exception e) {}
      }
      


      Class classObj = targetObject.getClass();
      m = MethodResolver.getMethod(classObj, funcName, methodArgs, convertedArgs, exprContext, 2);
      




      if (methodKey != null) {
        putToCache(methodKey, targetObject, methodArgs, m);
      }
      if ((trans != null) && (trans.getDebug())) {
        trans.getTraceManager().fireExtensionEvent(m, targetObject, convertedArgs[0]);
        Object result;
        try {
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
    //   8: invokevirtual 35	org/apache/xalan/extensions/ExtensionHandlerJavaPackage:getFromCache	(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   11: checkcast 52	java/lang/reflect/Method
    //   14: astore 8
    //   16: aconst_null
    //   17: aload 8
    //   19: if_acmpne +167 -> 186
    //   22: new 7	java/lang/StringBuffer
    //   25: dup
    //   26: invokespecial 8	java/lang/StringBuffer:<init>	()V
    //   29: aload_0
    //   30: getfield 9	org/apache/xalan/extensions/ExtensionHandlerJavaPackage:m_className	Ljava/lang/String;
    //   33: invokevirtual 10	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   36: aload_1
    //   37: invokevirtual 10	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   40: invokevirtual 11	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   43: astore 9
    //   45: aload 9
    //   47: bipush 46
    //   49: invokevirtual 12	java/lang/String:lastIndexOf	(I)I
    //   52: istore 10
    //   54: iload 10
    //   56: ifge +25 -> 81
    //   59: new 42	javax/xml/transform/TransformerException
    //   62: dup
    //   63: ldc 67
    //   65: iconst_1
    //   66: anewarray 32	java/lang/Object
    //   69: dup
    //   70: iconst_0
    //   71: aload 9
    //   73: aastore
    //   74: invokestatic 58	org/apache/xalan/res/XSLMessages:createMessage	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   77: invokespecial 59	javax/xml/transform/TransformerException:<init>	(Ljava/lang/String;)V
    //   80: athrow
    //   81: aload 9
    //   83: iconst_0
    //   84: iload 10
    //   86: invokevirtual 13	java/lang/String:substring	(II)Ljava/lang/String;
    //   89: invokestatic 14	org/apache/xalan/extensions/ExtensionHandlerJavaPackage:getClassForName	(Ljava/lang/String;)Ljava/lang/Class;
    //   92: astore 7
    //   94: goto +15 -> 109
    //   97: astore 11
    //   99: new 42	javax/xml/transform/TransformerException
    //   102: dup
    //   103: aload 11
    //   105: invokespecial 43	javax/xml/transform/TransformerException:<init>	(Ljava/lang/Throwable;)V
    //   108: athrow
    //   109: aload 9
    //   111: iload 10
    //   113: iconst_1
    //   114: iadd
    //   115: invokevirtual 16	java/lang/String:substring	(I)Ljava/lang/String;
    //   118: astore_1
    //   119: aload 7
    //   121: aload_1
    //   122: invokestatic 68	org/apache/xalan/extensions/MethodResolver:getElementMethod	(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Method;
    //   125: astore 8
    //   127: aload 8
    //   129: invokevirtual 69	java/lang/reflect/Method:getModifiers	()I
    //   132: invokestatic 70	java/lang/reflect/Modifier:isStatic	(I)Z
    //   135: ifne +25 -> 160
    //   138: new 42	javax/xml/transform/TransformerException
    //   141: dup
    //   142: ldc 71
    //   144: iconst_1
    //   145: anewarray 32	java/lang/Object
    //   148: dup
    //   149: iconst_0
    //   150: aload 9
    //   152: aastore
    //   153: invokestatic 58	org/apache/xalan/res/XSLMessages:createMessage	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   156: invokespecial 59	javax/xml/transform/TransformerException:<init>	(Ljava/lang/String;)V
    //   159: athrow
    //   160: goto +15 -> 175
    //   163: astore 9
    //   165: new 42	javax/xml/transform/TransformerException
    //   168: dup
    //   169: aload 9
    //   171: invokespecial 43	javax/xml/transform/TransformerException:<init>	(Ljava/lang/Throwable;)V
    //   174: athrow
    //   175: aload_0
    //   176: aload 5
    //   178: aconst_null
    //   179: aconst_null
    //   180: aload 8
    //   182: invokevirtual 45	org/apache/xalan/extensions/ExtensionHandlerJavaPackage:putToCache	(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   185: pop
    //   186: new 72	org/apache/xalan/extensions/XSLProcessorContext
    //   189: dup
    //   190: aload_3
    //   191: aload 4
    //   193: invokespecial 73	org/apache/xalan/extensions/XSLProcessorContext:<init>	(Lorg/apache/xalan/transformer/TransformerImpl;Lorg/apache/xalan/templates/Stylesheet;)V
    //   196: astore 9
    //   198: aload_3
    //   199: invokevirtual 46	org/apache/xalan/transformer/TransformerImpl:getDebug	()Z
    //   202: ifeq +109 -> 311
    //   205: aload_3
    //   206: invokevirtual 47	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   209: aload 8
    //   211: aconst_null
    //   212: iconst_2
    //   213: anewarray 32	java/lang/Object
    //   216: dup
    //   217: iconst_0
    //   218: aload 9
    //   220: aastore
    //   221: dup
    //   222: iconst_1
    //   223: aload_2
    //   224: aastore
    //   225: invokevirtual 55	org/apache/xalan/trace/TraceManager:fireExtensionEvent	(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)V
    //   228: aload 8
    //   230: aconst_null
    //   231: iconst_2
    //   232: anewarray 32	java/lang/Object
    //   235: dup
    //   236: iconst_0
    //   237: aload 9
    //   239: aastore
    //   240: dup
    //   241: iconst_1
    //   242: aload_2
    //   243: aastore
    //   244: invokevirtual 53	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   247: astore 6
    //   249: aload_3
    //   250: invokevirtual 47	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   253: aload 8
    //   255: aconst_null
    //   256: iconst_2
    //   257: anewarray 32	java/lang/Object
    //   260: dup
    //   261: iconst_0
    //   262: aload 9
    //   264: aastore
    //   265: dup
    //   266: iconst_1
    //   267: aload_2
    //   268: aastore
    //   269: invokevirtual 56	org/apache/xalan/trace/TraceManager:fireExtensionEndEvent	(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)V
    //   272: goto +36 -> 308
    //   275: astore 10
    //   277: aload 10
    //   279: athrow
    //   280: astore 12
    //   282: aload_3
    //   283: invokevirtual 47	org/apache/xalan/transformer/TransformerImpl:getTraceManager	()Lorg/apache/xalan/trace/TraceManager;
    //   286: aload 8
    //   288: aconst_null
    //   289: iconst_2
    //   290: anewarray 32	java/lang/Object
    //   293: dup
    //   294: iconst_0
    //   295: aload 9
    //   297: aastore
    //   298: dup
    //   299: iconst_1
    //   300: aload_2
    //   301: aastore
    //   302: invokevirtual 56	org/apache/xalan/trace/TraceManager:fireExtensionEndEvent	(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)V
    //   305: aload 12
    //   307: athrow
    //   308: goto +24 -> 332
    //   311: aload 8
    //   313: aconst_null
    //   314: iconst_2
    //   315: anewarray 32	java/lang/Object
    //   318: dup
    //   319: iconst_0
    //   320: aload 9
    //   322: aastore
    //   323: dup
    //   324: iconst_1
    //   325: aload_2
    //   326: aastore
    //   327: invokevirtual 53	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   330: astore 6
    //   332: goto +61 -> 393
    //   335: astore 10
    //   337: aload 10
    //   339: astore 11
    //   341: aload 10
    //   343: invokevirtual 63	java/lang/reflect/InvocationTargetException:getTargetException	()Ljava/lang/Throwable;
    //   346: astore 12
    //   348: aload 12
    //   350: instanceof 42
    //   353: ifeq +9 -> 362
    //   356: aload 12
    //   358: checkcast 42	javax/xml/transform/TransformerException
    //   361: athrow
    //   362: aload 12
    //   364: ifnull +7 -> 371
    //   367: aload 12
    //   369: astore 11
    //   371: new 42	javax/xml/transform/TransformerException
    //   374: dup
    //   375: aload 11
    //   377: invokespecial 43	javax/xml/transform/TransformerException:<init>	(Ljava/lang/Throwable;)V
    //   380: athrow
    //   381: astore 10
    //   383: new 42	javax/xml/transform/TransformerException
    //   386: dup
    //   387: aload 10
    //   389: invokespecial 43	javax/xml/transform/TransformerException:<init>	(Ljava/lang/Throwable;)V
    //   392: athrow
    //   393: aload 6
    //   395: ifnull +12 -> 407
    //   398: aload 9
    //   400: aload 4
    //   402: aload 6
    //   404: invokevirtual 74	org/apache/xalan/extensions/XSLProcessorContext:outputToResultTree	(Lorg/apache/xalan/templates/Stylesheet;Ljava/lang/Object;)V
    //   407: return
    // Line number table:
    //   Java source line #465	-> byte code offset #0
    //   Java source line #468	-> byte code offset #3
    //   Java source line #469	-> byte code offset #16
    //   Java source line #473	-> byte code offset #22
    //   Java source line #474	-> byte code offset #45
    //   Java source line #475	-> byte code offset #54
    //   Java source line #476	-> byte code offset #59
    //   Java source line #479	-> byte code offset #81
    //   Java source line #484	-> byte code offset #94
    //   Java source line #481	-> byte code offset #97
    //   Java source line #483	-> byte code offset #99
    //   Java source line #485	-> byte code offset #109
    //   Java source line #486	-> byte code offset #119
    //   Java source line #487	-> byte code offset #127
    //   Java source line #488	-> byte code offset #138
    //   Java source line #494	-> byte code offset #160
    //   Java source line #490	-> byte code offset #163
    //   Java source line #493	-> byte code offset #165
    //   Java source line #495	-> byte code offset #175
    //   Java source line #498	-> byte code offset #186
    //   Java source line #503	-> byte code offset #198
    //   Java source line #504	-> byte code offset #205
    //   Java source line #506	-> byte code offset #228
    //   Java source line #510	-> byte code offset #249
    //   Java source line #511	-> byte code offset #272
    //   Java source line #507	-> byte code offset #275
    //   Java source line #508	-> byte code offset #277
    //   Java source line #510	-> byte code offset #280
    //   Java source line #513	-> byte code offset #311
    //   Java source line #531	-> byte code offset #332
    //   Java source line #515	-> byte code offset #335
    //   Java source line #517	-> byte code offset #337
    //   Java source line #518	-> byte code offset #341
    //   Java source line #520	-> byte code offset #348
    //   Java source line #521	-> byte code offset #356
    //   Java source line #522	-> byte code offset #362
    //   Java source line #523	-> byte code offset #367
    //   Java source line #525	-> byte code offset #371
    //   Java source line #527	-> byte code offset #381
    //   Java source line #530	-> byte code offset #383
    //   Java source line #533	-> byte code offset #393
    //   Java source line #535	-> byte code offset #398
    //   Java source line #538	-> byte code offset #407
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	408	0	this	ExtensionHandlerJavaPackage
    //   0	408	1	localPart	String
    //   0	408	2	element	org.apache.xalan.templates.ElemTemplateElement
    //   0	408	3	transformer	TransformerImpl
    //   0	408	4	stylesheetTree	org.apache.xalan.templates.Stylesheet
    //   0	408	5	methodKey	Object
    //   1	402	6	result	Object
    //   92	28	7	classObj	Class
    //   14	298	8	m	Method
    //   43	108	9	fullName	String
    //   163	7	9	e	Exception
    //   196	203	9	xpc	XSLProcessorContext
    //   52	60	10	lastDot	int
    //   275	3	10	e	Exception
    //   335	7	10	ite	InvocationTargetException
    //   381	7	10	e	Exception
    //   97	7	11	e	ClassNotFoundException
    //   339	37	11	resultException	Throwable
    //   280	26	12	localObject1	Object
    //   346	22	12	targetException	Throwable
    // Exception table:
    //   from	to	target	type
    //   81	94	97	java/lang/ClassNotFoundException
    //   22	160	163	java/lang/Exception
    //   228	249	275	java/lang/Exception
    //   228	249	280	finally
    //   275	282	280	finally
    //   198	332	335	java/lang/reflect/InvocationTargetException
    //   198	332	381	java/lang/Exception
  }
}
