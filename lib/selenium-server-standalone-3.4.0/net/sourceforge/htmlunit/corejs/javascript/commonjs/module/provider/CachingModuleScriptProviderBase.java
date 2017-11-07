package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.ModuleScript;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.ModuleScriptProvider;






















public abstract class CachingModuleScriptProviderBase
  implements ModuleScriptProvider, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final int loadConcurrencyLevel = Runtime.getRuntime()
    .availableProcessors() * 8;
  private static final int loadLockShift;
  private static final int loadLockMask;
  private static final int loadLockCount;
  
  static { int sshift = 0;
    int ssize = 1;
    while (ssize < loadConcurrencyLevel) {
      sshift++;
      ssize <<= 1;
    }
    loadLockShift = 32 - sshift;
    loadLockMask = ssize - 1;
    loadLockCount = ssize; }
  
  private final Object[] loadLocks = new Object[loadLockCount];
  
  protected CachingModuleScriptProviderBase(ModuleSourceProvider moduleSourceProvider) { for (int i = 0; i < loadLocks.length; i++) {
      loadLocks[i] = new Object();
    }
    











    this.moduleSourceProvider = moduleSourceProvider;
  }
  



















  private final ModuleSourceProvider moduleSourceProvider;
  


















  public static class CachedModuleScript
  {
    private final ModuleScript moduleScript;
    


















    private final Object validator;
    



















    public CachedModuleScript(ModuleScript moduleScript, Object validator)
    {
      this.moduleScript = moduleScript;
      this.validator = validator;
    }
    




    ModuleScript getModule()
    {
      return moduleScript;
    }
    




    Object getValidator()
    {
      return validator;
    }
  }
  
  private static Object getValidator(CachedModuleScript cachedModule) {
    return cachedModule == null ? null : cachedModule.getValidator();
  }
  
  private static boolean equal(Object o1, Object o2) {
    return o1 == null ? false : o2 == null ? true : o1.equals(o2);
  }
  




  protected static int getConcurrencyLevel()
  {
    return loadLockCount;
  }
  
  /* Error */
  public ModuleScript getModuleScript(net.sourceforge.htmlunit.corejs.javascript.Context cx, String moduleId, java.net.URI moduleUri, java.net.URI baseUri, net.sourceforge.htmlunit.corejs.javascript.Scriptable paths)
    throws java.lang.Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_2
    //   2: invokevirtual 6	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:getLoadedModule	(Ljava/lang/String;)Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase$CachedModuleScript;
    //   5: astore 6
    //   7: aload 6
    //   9: invokestatic 7	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:getValidator	(Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase$CachedModuleScript;)Ljava/lang/Object;
    //   12: astore 7
    //   14: aload_3
    //   15: ifnonnull +20 -> 35
    //   18: aload_0
    //   19: getfield 5	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:moduleSourceProvider	Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSourceProvider;
    //   22: aload_2
    //   23: aload 5
    //   25: aload 7
    //   27: invokeinterface 8 4 0
    //   32: goto +17 -> 49
    //   35: aload_0
    //   36: getfield 5	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:moduleSourceProvider	Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSourceProvider;
    //   39: aload_3
    //   40: aload 4
    //   42: aload 7
    //   44: invokeinterface 9 4 0
    //   49: astore 8
    //   51: aload 8
    //   53: getstatic 10	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSourceProvider:NOT_MODIFIED	Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSource;
    //   56: if_acmpne +9 -> 65
    //   59: aload 6
    //   61: invokevirtual 11	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase$CachedModuleScript:getModule	()Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/ModuleScript;
    //   64: areturn
    //   65: aload 8
    //   67: ifnonnull +5 -> 72
    //   70: aconst_null
    //   71: areturn
    //   72: aload 8
    //   74: invokevirtual 12	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSource:getReader	()Ljava/io/Reader;
    //   77: astore 9
    //   79: aload_2
    //   80: invokevirtual 13	java/lang/String:hashCode	()I
    //   83: istore 10
    //   85: aload_0
    //   86: getfield 4	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:loadLocks	[Ljava/lang/Object;
    //   89: iload 10
    //   91: getstatic 14	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:loadLockShift	I
    //   94: iushr
    //   95: getstatic 15	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:loadLockMask	I
    //   98: iand
    //   99: aaload
    //   100: dup
    //   101: astore 11
    //   103: monitorenter
    //   104: aload_0
    //   105: aload_2
    //   106: invokevirtual 6	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:getLoadedModule	(Ljava/lang/String;)Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase$CachedModuleScript;
    //   109: astore 12
    //   111: aload 12
    //   113: ifnull +34 -> 147
    //   116: aload 7
    //   118: aload 12
    //   120: invokestatic 7	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:getValidator	(Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase$CachedModuleScript;)Ljava/lang/Object;
    //   123: invokestatic 16	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:equal	(Ljava/lang/Object;Ljava/lang/Object;)Z
    //   126: ifne +21 -> 147
    //   129: aload 12
    //   131: invokevirtual 11	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase$CachedModuleScript:getModule	()Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/ModuleScript;
    //   134: astore 13
    //   136: aload 11
    //   138: monitorexit
    //   139: aload 9
    //   141: invokevirtual 17	java/io/Reader:close	()V
    //   144: aload 13
    //   146: areturn
    //   147: aload 8
    //   149: invokevirtual 18	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSource:getUri	()Ljava/net/URI;
    //   152: astore 13
    //   154: new 19	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/ModuleScript
    //   157: dup
    //   158: aload_1
    //   159: aload 9
    //   161: aload 13
    //   163: invokevirtual 20	java/net/URI:toString	()Ljava/lang/String;
    //   166: iconst_1
    //   167: aload 8
    //   169: invokevirtual 21	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSource:getSecurityDomain	()Ljava/lang/Object;
    //   172: invokevirtual 22	net/sourceforge/htmlunit/corejs/javascript/Context:compileReader	(Ljava/io/Reader;Ljava/lang/String;ILjava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Script;
    //   175: aload 13
    //   177: aload 8
    //   179: invokevirtual 23	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSource:getBase	()Ljava/net/URI;
    //   182: invokespecial 24	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/ModuleScript:<init>	(Lnet/sourceforge/htmlunit/corejs/javascript/Script;Ljava/net/URI;Ljava/net/URI;)V
    //   185: astore 14
    //   187: aload_0
    //   188: aload_2
    //   189: aload 14
    //   191: aload 8
    //   193: invokevirtual 25	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/ModuleSource:getValidator	()Ljava/lang/Object;
    //   196: invokevirtual 26	net/sourceforge/htmlunit/corejs/javascript/commonjs/module/provider/CachingModuleScriptProviderBase:putLoadedModule	(Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/commonjs/module/ModuleScript;Ljava/lang/Object;)V
    //   199: aload 14
    //   201: astore 15
    //   203: aload 11
    //   205: monitorexit
    //   206: aload 9
    //   208: invokevirtual 17	java/io/Reader:close	()V
    //   211: aload 15
    //   213: areturn
    //   214: astore 16
    //   216: aload 11
    //   218: monitorexit
    //   219: aload 16
    //   221: athrow
    //   222: astore 17
    //   224: aload 9
    //   226: invokevirtual 17	java/io/Reader:close	()V
    //   229: aload 17
    //   231: athrow
    // Line number table:
    //   Java source line #70	-> byte code offset #0
    //   Java source line #71	-> byte code offset #7
    //   Java source line #72	-> byte code offset #14
    //   Java source line #73	-> byte code offset #27
    //   Java source line #74	-> byte code offset #44
    //   Java source line #76	-> byte code offset #51
    //   Java source line #77	-> byte code offset #59
    //   Java source line #79	-> byte code offset #65
    //   Java source line #80	-> byte code offset #70
    //   Java source line #82	-> byte code offset #72
    //   Java source line #84	-> byte code offset #79
    //   Java source line #85	-> byte code offset #85
    //   Java source line #87	-> byte code offset #104
    //   Java source line #89	-> byte code offset #111
    //   Java source line #90	-> byte code offset #116
    //   Java source line #91	-> byte code offset #129
    //   Java source line #104	-> byte code offset #139
    //   Java source line #91	-> byte code offset #144
    //   Java source line #94	-> byte code offset #147
    //   Java source line #95	-> byte code offset #154
    //   Java source line #96	-> byte code offset #163
    //   Java source line #97	-> byte code offset #169
    //   Java source line #96	-> byte code offset #172
    //   Java source line #98	-> byte code offset #179
    //   Java source line #99	-> byte code offset #187
    //   Java source line #100	-> byte code offset #193
    //   Java source line #99	-> byte code offset #196
    //   Java source line #101	-> byte code offset #199
    //   Java source line #104	-> byte code offset #206
    //   Java source line #101	-> byte code offset #211
    //   Java source line #102	-> byte code offset #214
    //   Java source line #104	-> byte code offset #222
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	232	0	this	CachingModuleScriptProviderBase
    //   0	232	1	cx	net.sourceforge.htmlunit.corejs.javascript.Context
    //   0	232	2	moduleId	String
    //   0	232	3	moduleUri	java.net.URI
    //   0	232	4	baseUri	java.net.URI
    //   0	232	5	paths	net.sourceforge.htmlunit.corejs.javascript.Scriptable
    //   5	55	6	cachedModule1	CachedModuleScript
    //   12	105	7	validator1	Object
    //   49	143	8	moduleSource	ModuleSource
    //   77	148	9	reader	java.io.Reader
    //   83	7	10	idHash	int
    //   109	21	12	cachedModule2	CachedModuleScript
    //   134	11	13	localModuleScript1	ModuleScript
    //   152	24	13	sourceUri	java.net.URI
    //   185	15	14	moduleScript	ModuleScript
    //   201	11	15	localModuleScript2	ModuleScript
    //   214	6	16	localObject1	Object
    //   222	8	17	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   104	139	214	finally
    //   147	206	214	finally
    //   214	219	214	finally
    //   79	139	222	finally
    //   147	206	222	finally
    //   214	224	222	finally
  }
  
  protected abstract void putLoadedModule(String paramString, ModuleScript paramModuleScript, Object paramObject);
  
  protected abstract CachedModuleScript getLoadedModule(String paramString);
}
