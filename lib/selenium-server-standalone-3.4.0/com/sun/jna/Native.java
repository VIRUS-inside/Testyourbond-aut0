package com.sun.jna;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.WeakHashMap;



















































public final class Native
  implements Version
{
  public static final String DEFAULT_ENCODING = "utf8";
  static final boolean DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
  static final boolean DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
  

  static String jnidispatchPath = null;
  private static Map options = new WeakHashMap();
  private static Map libraries = new WeakHashMap();
  private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler()
  {
    public void uncaughtException(Callback c, Throwable e) {
      System.err.println("JNA: Callback " + c + " threw the following exception:");
      e.printStackTrace();
    }
  };
  private static Callback.UncaughtExceptionHandler callbackExceptionHandler = DEFAULT_HANDLER;
  

  public static final int POINTER_SIZE;
  
  public static final int LONG_SIZE;
  
  public static final int WCHAR_SIZE;
  
  public static final int SIZE_T_SIZE;
  
  private static final int TYPE_VOIDP = 0;
  
  private static final int TYPE_LONG = 1;
  
  private static final int TYPE_WCHAR_T = 2;
  
  private static final int TYPE_SIZE_T = 3;
  

  static
  {
    loadNativeDispatchLibrary();
    POINTER_SIZE = sizeof(0);
    LONG_SIZE = sizeof(1);
    WCHAR_SIZE = sizeof(2);
    SIZE_T_SIZE = sizeof(3);
    


    initIDs();
    if (Boolean.getBoolean("jna.protected")) {
      setProtected(true);
    }
    String version = getNativeVersion();
    if (!"4.0.0".equals(version)) {
      String LS = System.getProperty("line.separator");
      throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system" + LS + (jnidispatchPath != null ? "(at " + jnidispatchPath + ")" : System.getProperty("java.library.path")) + "." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
    }
  }
  







  static final int MAX_ALIGNMENT = (Platform.isSPARC()) || (Platform.isWindows()) || ((Platform.isLinux()) && ((Platform.isARM()) || (Platform.isPPC()))) || (Platform.isAIX()) || (Platform.isAndroid()) ? 8 : LONG_SIZE;
  



  static final int MAX_PADDING = (Platform.isMac()) && (Platform.isPPC()) ? 8 : MAX_ALIGNMENT;
  


  private static final Object finalizer = new Object()
  {
    protected void finalize() {}
  };
  static final String JNA_TMPLIB_PREFIX = "jna";
  
  private static void dispose()
  {
    NativeLibrary.disposeAll();
    jnidispatchPath = null;
  }
  










  static boolean deleteLibrary(File lib)
  {
    if (lib.delete()) {
      return true;
    }
    

    markTemporaryFile(lib);
    
    return false;
  }
  









































  /**
   * @deprecated
   */
  public static boolean getPreserveLastError()
  {
    return true;
  }
  



  public static long getWindowID(Window w)
    throws HeadlessException
  {
    return AWT.getWindowID(w);
  }
  




  public static long getComponentID(Component c)
    throws HeadlessException
  {
    return AWT.getComponentID(c);
  }
  




  public static Pointer getWindowPointer(Window w)
    throws HeadlessException
  {
    return new Pointer(AWT.getWindowID(w));
  }
  




  public static Pointer getComponentPointer(Component c)
    throws HeadlessException
  {
    return new Pointer(AWT.getComponentID(c));
  }
  




  public static Pointer getDirectBufferPointer(Buffer b)
  {
    long peer = _getDirectBufferPointer(b);
    return peer == 0L ? null : new Pointer(peer);
  }
  





  public static String toString(byte[] buf)
  {
    return toString(buf, getDefaultStringEncoding());
  }
  




  public static String toString(byte[] buf, String encoding)
  {
    String s = null;
    if (encoding != null) {
      try {
        s = new String(buf, encoding);
      }
      catch (UnsupportedEncodingException e) {
        System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
      }
    }
    
    if (s == null) {
      System.err.println("JNA Warning: Decoding with fallback " + System.getProperty("file.encoding"));
      s = new String(buf);
    }
    int term = s.indexOf(0);
    if (term != -1) {
      s = s.substring(0, term);
    }
    return s;
  }
  


  public static String toString(char[] buf)
  {
    String s = new String(buf);
    int term = s.indexOf(0);
    if (term != -1) {
      s = s.substring(0, term);
    }
    return s;
  }
  









  public static Object loadLibrary(Class interfaceClass)
  {
    return loadLibrary(null, interfaceClass);
  }
  












  public static Object loadLibrary(Class interfaceClass, Map options)
  {
    return loadLibrary(null, interfaceClass, options);
  }
  











  public static Object loadLibrary(String name, Class interfaceClass)
  {
    return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
  }
  
















  public static Object loadLibrary(String name, Class interfaceClass, Map options)
  {
    Library.Handler handler = new Library.Handler(name, interfaceClass, options);
    
    ClassLoader loader = interfaceClass.getClassLoader();
    Library proxy = (Library)Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
    

    cacheOptions(interfaceClass, options, proxy);
    return proxy;
  }
  




  private static void loadLibraryInstance(Class cls)
  {
    synchronized (libraries) {
      if ((cls != null) && (!libraries.containsKey(cls))) {
        try {
          Field[] fields = cls.getFields();
          for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if ((field.getType() == cls) && (Modifier.isStatic(field.getModifiers())))
            {

              libraries.put(cls, new WeakReference(field.get(null)));
              break;
            }
          }
        }
        catch (Exception e) {
          throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
        }
      }
    }
  }
  




  static Class findEnclosingLibraryClass(Class cls)
  {
    if (cls == null) {
      return null;
    }
    

    synchronized (libraries) {
      if (options.containsKey(cls)) {
        return cls;
      }
    }
    if (Library.class.isAssignableFrom(cls)) {
      return cls;
    }
    if (Callback.class.isAssignableFrom(cls)) {
      cls = CallbackReference.findCallbackClass(cls);
    }
    Class declaring = cls.getDeclaringClass();
    Class fromDeclaring = findEnclosingLibraryClass(declaring);
    if (fromDeclaring != null) {
      return fromDeclaring;
    }
    return findEnclosingLibraryClass(cls.getSuperclass());
  }
  










  public static Map getLibraryOptions(Class type)
  {
    synchronized (libraries) {
      if (options.containsKey(type)) {
        return (Map)options.get(type);
      }
    }
    Class mappingClass = findEnclosingLibraryClass(type);
    if (mappingClass != null) {
      loadLibraryInstance(mappingClass);
    }
    else {
      mappingClass = type;
    }
    synchronized (libraries) {
      if (options.containsKey(mappingClass)) {
        Map libraryOptions = (Map)options.get(mappingClass);
        options.put(type, libraryOptions);
        return libraryOptions;
      }
      Map libraryOptions = null;
      try {
        Field field = mappingClass.getField("OPTIONS");
        field.setAccessible(true);
        libraryOptions = (Map)field.get(null);
      }
      catch (NoSuchFieldException e) {
        libraryOptions = Collections.EMPTY_MAP;
      }
      catch (Exception e) {
        throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + mappingClass);
      }
      

      libraryOptions = new HashMap(libraryOptions);
      if (!libraryOptions.containsKey("type-mapper")) {
        libraryOptions.put("type-mapper", lookupField(mappingClass, "TYPE_MAPPER", TypeMapper.class));
      }
      if (!libraryOptions.containsKey("structure-alignment")) {
        libraryOptions.put("structure-alignment", lookupField(mappingClass, "STRUCTURE_ALIGNMENT", Integer.class));
      }
      if (!libraryOptions.containsKey("string-encoding")) {
        libraryOptions.put("string-encoding", lookupField(mappingClass, "STRING_ENCODING", String.class));
      }
      options.put(mappingClass, libraryOptions);
      
      if (type != mappingClass) {
        options.put(type, libraryOptions);
      }
      return libraryOptions;
    }
  }
  
  private static Object lookupField(Class mappingClass, String fieldName, Class resultClass) {
    try {
      Field field = mappingClass.getField(fieldName);
      field.setAccessible(true);
      return field.get(null);
    }
    catch (NoSuchFieldException e) {
      return null;
    }
    catch (Exception e) {
      throw new IllegalArgumentException(fieldName + " must be a public field of type " + resultClass.getName() + " (" + e + "): " + mappingClass);
    }
  }
  




  public static TypeMapper getTypeMapper(Class cls)
  {
    return (TypeMapper)getLibraryOptions(cls).get("type-mapper");
  }
  




  public static String getStringEncoding(Class cls)
  {
    String encoding = (String)getLibraryOptions(cls).get("string-encoding");
    return encoding != null ? encoding : getDefaultStringEncoding();
  }
  


  public static String getDefaultStringEncoding()
  {
    return System.getProperty("jna.encoding", "utf8");
  }
  


  public static int getStructureAlignment(Class cls)
  {
    Integer alignment = (Integer)getLibraryOptions(cls).get("structure-alignment");
    return alignment == null ? 0 : alignment.intValue();
  }
  


  static byte[] getBytes(String s)
  {
    return getBytes(s, getDefaultStringEncoding());
  }
  



  static byte[] getBytes(String s, String encoding)
  {
    if (encoding != null) {
      try {
        return s.getBytes(encoding);
      }
      catch (UnsupportedEncodingException e) {
        System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
      }
    }
    
    System.err.println("JNA Warning: Encoding with fallback " + System.getProperty("file.encoding"));
    
    return s.getBytes();
  }
  


  public static byte[] toByteArray(String s)
  {
    return toByteArray(s, getDefaultStringEncoding());
  }
  


  public static byte[] toByteArray(String s, String encoding)
  {
    byte[] bytes = getBytes(s, encoding);
    byte[] buf = new byte[bytes.length + 1];
    System.arraycopy(bytes, 0, buf, 0, bytes.length);
    return buf;
  }
  


  public static char[] toCharArray(String s)
  {
    char[] chars = s.toCharArray();
    char[] buf = new char[chars.length + 1];
    System.arraycopy(chars, 0, buf, 0, chars.length);
    return buf;
  }
  





  private static void loadNativeDispatchLibrary()
  {
    if (!Boolean.getBoolean("jna.nounpack")) {
      try {
        removeTemporaryFiles();
      }
      catch (IOException e) {
        System.err.println("JNA Warning: IOException removing temporary files: " + e.getMessage());
      }
    }
    
    String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
    String bootPath = System.getProperty("jna.boot.library.path");
    if (bootPath != null)
    {
      StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
      while (dirs.hasMoreTokens()) {
        String dir = dirs.nextToken();
        File file = new File(new File(dir), System.mapLibraryName(libName).replace(".dylib", ".jnilib"));
        String path = file.getAbsolutePath();
        if (DEBUG_JNA_LOAD) {
          System.out.println("Looking in " + path);
        }
        if (file.exists()) {
          try {
            if (DEBUG_JNA_LOAD) {
              System.out.println("Trying " + path);
            }
            System.setProperty("jnidispatch.path", path);
            System.load(path);
            jnidispatchPath = path;
            if (DEBUG_JNA_LOAD) {
              System.out.println("Found jnidispatch at " + path);
            }
            return;
          }
          catch (UnsatisfiedLinkError ex) {}
        }
        


        if (Platform.isMac()) { String ext;
          String orig;
          String ext; if (path.endsWith("dylib")) {
            String orig = "dylib";
            ext = "jnilib";
          } else {
            orig = "jnilib";
            ext = "dylib";
          }
          path = path.substring(0, path.lastIndexOf(orig)) + ext;
          if (DEBUG_JNA_LOAD) {
            System.out.println("Looking in " + path);
          }
          if (new File(path).exists()) {
            try {
              if (DEBUG_JNA_LOAD) {
                System.out.println("Trying " + path);
              }
              System.setProperty("jnidispatch.path", path);
              System.load(path);
              jnidispatchPath = path;
              if (DEBUG_JNA_LOAD) {
                System.out.println("Found jnidispatch at " + path);
              }
              return;
            } catch (UnsatisfiedLinkError ex) {
              System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
            }
          }
        }
      }
    }
    if (!Boolean.getBoolean("jna.nosys")) {
      try {
        if (DEBUG_JNA_LOAD) {
          System.out.println("Trying (via loadLibrary) " + libName);
        }
        System.loadLibrary(libName);
        if (DEBUG_JNA_LOAD) {
          System.out.println("Found jnidispatch on system path");
        }
        return;
      }
      catch (UnsatisfiedLinkError e) {}
    }
    
    if (!Boolean.getBoolean("jna.noclasspath")) {
      loadNativeDispatchLibraryFromClasspath();
    }
    else {
      throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
    }
  }
  



  private static void loadNativeDispatchLibraryFromClasspath()
  {
    try
    {
      String libName = "/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
      File lib = extractFromResourcePath(libName, Native.class.getClassLoader());
      if ((lib == null) && 
        (lib == null)) {
        throw new UnsatisfiedLinkError("Could not find JNA native support");
      }
      
      if (DEBUG_JNA_LOAD) {
        System.out.println("Trying " + lib.getAbsolutePath());
      }
      System.setProperty("jnidispatch.path", lib.getAbsolutePath());
      System.load(lib.getAbsolutePath());
      jnidispatchPath = lib.getAbsolutePath();
      if (DEBUG_JNA_LOAD) {
        System.out.println("Found jnidispatch at " + jnidispatchPath);
      }
      



      if ((isUnpacked(lib)) && (!Boolean.getBoolean("jnidispatch.preserve")))
      {
        deleteLibrary(lib);
      }
    }
    catch (IOException e) {
      throw new UnsatisfiedLinkError(e.getMessage());
    }
  }
  
  static boolean isUnpacked(File file)
  {
    return file.getName().startsWith("jna");
  }
  









  public static File extractFromResourcePath(String name)
    throws IOException
  {
    return extractFromResourcePath(name, null);
  }
  










  public static File extractFromResourcePath(String name, ClassLoader loader)
    throws IOException
  {
    boolean DEBUG = (DEBUG_LOAD) || ((DEBUG_JNA_LOAD) && (name.indexOf("jnidispatch") != -1));
    
    if (loader == null) {
      loader = Thread.currentThread().getContextClassLoader();
      
      if (loader == null) {
        loader = Native.class.getClassLoader();
      }
    }
    if (DEBUG) {
      System.out.println("Looking in classpath from " + loader + " for " + name);
    }
    String libname = name.startsWith("/") ? name : NativeLibrary.mapSharedLibraryName(name);
    String resourcePath = Platform.RESOURCE_PREFIX + "/" + libname;
    if (resourcePath.startsWith("/")) {
      resourcePath = resourcePath.substring(1);
    }
    URL url = loader.getResource(resourcePath);
    if ((url == null) && (resourcePath.startsWith(Platform.RESOURCE_PREFIX)))
    {
      url = loader.getResource(libname);
    }
    if (url == null) {
      String path = System.getProperty("java.class.path");
      if ((loader instanceof URLClassLoader)) {
        path = Arrays.asList(((URLClassLoader)loader).getURLs()).toString();
      }
      throw new IOException("Native library (" + resourcePath + ") not found in resource path (" + path + ")");
    }
    if (DEBUG) {
      System.out.println("Found library resource at " + url);
    }
    
    lib = null;
    if (url.getProtocol().toLowerCase().equals("file")) {
      try {
        lib = new File(new URI(url.toString()));
      }
      catch (URISyntaxException e) {
        lib = new File(url.getPath());
      }
      if (DEBUG) {
        System.out.println("Looking in " + lib.getAbsolutePath());
      }
      if (!lib.exists()) {
        throw new IOException("File URL " + url + " could not be properly decoded");
      }
    }
    else if (!Boolean.getBoolean("jna.nounpack")) {
      InputStream is = loader.getResourceAsStream(resourcePath);
      if (is == null) {
        throw new IOException("Can't obtain InputStream for " + resourcePath);
      }
      
      FileOutputStream fos = null;
      

      try
      {
        File dir = getTempDir();
        lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
        if (!Boolean.getBoolean("jnidispatch.preserve")) {
          lib.deleteOnExit();
        }
        fos = new FileOutputStream(lib);
        
        byte[] buf = new byte['Ѐ'];
        int count; while ((count = is.read(buf, 0, buf.length)) > 0) {
          fos.write(buf, 0, count);
        }
        










        return lib;
      }
      catch (IOException e)
      {
        throw new IOException("Failed to create temporary file for " + name + " library: " + e.getMessage());
      } finally {
        try {
          is.close(); } catch (IOException e) {}
        if (fos != null) {
          try { fos.close();
          }
          catch (IOException e) {}
        }
      }
    }
  }
  


































  public static Library synchronizedLibrary(final Library library)
  {
    Class cls = library.getClass();
    if (!Proxy.isProxyClass(cls)) {
      throw new IllegalArgumentException("Library must be a proxy class");
    }
    InvocationHandler ih = Proxy.getInvocationHandler(library);
    if (!(ih instanceof Library.Handler)) {
      throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
    }
    Library.Handler handler = (Library.Handler)ih;
    InvocationHandler newHandler = new InvocationHandler() {
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized (val$handler.getNativeLibrary()) {
          return val$handler.invoke(library, method, args);
        }
      }
    };
    return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
  }
  
















  public static String getWebStartLibraryPath(String libName)
  {
    if (System.getProperty("javawebstart.version") == null) {
      return null;
    }
    try {
      ClassLoader cl = Native.class.getClassLoader();
      Method m = (Method)AccessController.doPrivileged(new PrivilegedAction() {
        public Object run() {
          try {
            Method m = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[] { String.class });
            m.setAccessible(true);
            return m;
          }
          catch (Exception e) {}
          return null;
        }
        
      });
      String libpath = (String)m.invoke(cl, new Object[] { libName });
      if (libpath != null) {
        return new File(libpath).getParent();
      }
      return null;
    }
    catch (Exception e) {}
    return null;
  }
  



  static void markTemporaryFile(File file)
  {
    try
    {
      File marker = new File(file.getParentFile(), file.getName() + ".x");
      marker.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  

  static File getTempDir()
    throws IOException
  {
    String prop = System.getProperty("jna.tmpdir");
    File jnatmp; if (prop != null) {
      File jnatmp = new File(prop);
      jnatmp.mkdirs();
    }
    else {
      File tmp = new File(System.getProperty("java.io.tmpdir"));
      


      jnatmp = new File(tmp, "jna-" + System.getProperty("user.name").hashCode());
      jnatmp.mkdirs();
      if ((!jnatmp.exists()) || (!jnatmp.canWrite())) {
        jnatmp = tmp;
      }
    }
    if (!jnatmp.exists()) {
      throw new IOException("JNA temporary directory '" + jnatmp + "' does not exist");
    }
    if (!jnatmp.canWrite()) {
      throw new IOException("JNA temporary directory '" + jnatmp + "' is not writable");
    }
    return jnatmp;
  }
  
  static void removeTemporaryFiles() throws IOException
  {
    File dir = getTempDir();
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return (name.endsWith(".x")) && (name.startsWith("jna"));
      }
    };
    File[] files = dir.listFiles(filter);
    for (int i = 0; (files != null) && (i < files.length); i++) {
      File marker = files[i];
      String name = marker.getName();
      name = name.substring(0, name.length() - 2);
      File target = new File(marker.getParentFile(), name);
      if ((!target.exists()) || (target.delete())) {
        marker.delete();
      }
    }
  }
  


  public static int getNativeSize(Class type, Object value)
  {
    if (type.isArray()) {
      int len = Array.getLength(value);
      if (len > 0) {
        Object o = Array.get(value, 0);
        return len * getNativeSize(type.getComponentType(), o);
      }
      
      throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
    }
    if ((Structure.class.isAssignableFrom(type)) && (!Structure.ByReference.class.isAssignableFrom(type)))
    {
      return Structure.size(type, (Structure)value);
    }
    try {
      return getNativeSize(type);
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e.getMessage());
    }
  }
  





  public static int getNativeSize(Class cls)
  {
    if (NativeMapped.class.isAssignableFrom(cls)) {
      cls = NativeMappedConverter.getInstance(cls).nativeType();
    }
    
    if ((cls == Boolean.TYPE) || (cls == Boolean.class)) return 4;
    if ((cls == Byte.TYPE) || (cls == Byte.class)) return 1;
    if ((cls == Short.TYPE) || (cls == Short.class)) return 2;
    if ((cls == Character.TYPE) || (cls == Character.class)) return WCHAR_SIZE;
    if ((cls == Integer.TYPE) || (cls == Integer.class)) return 4;
    if ((cls == Long.TYPE) || (cls == Long.class)) return 8;
    if ((cls == Float.TYPE) || (cls == Float.class)) return 4;
    if ((cls == Double.TYPE) || (cls == Double.class)) return 8;
    if (Structure.class.isAssignableFrom(cls)) {
      if (Structure.ByValue.class.isAssignableFrom(cls)) {
        return Structure.size(cls);
      }
      return POINTER_SIZE;
    }
    if ((Pointer.class.isAssignableFrom(cls)) || ((Platform.HAS_BUFFERS) && (Buffers.isBuffer(cls))) || (Callback.class.isAssignableFrom(cls)) || (String.class == cls) || (WString.class == cls))
    {



      return POINTER_SIZE;
    }
    throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
  }
  



  public static boolean isSupportedNativeType(Class cls)
  {
    if (Structure.class.isAssignableFrom(cls)) {
      return true;
    }
    try {
      return getNativeSize(cls) != 0;
    }
    catch (IllegalArgumentException e) {}
    return false;
  }
  




  public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh)
  {
    callbackExceptionHandler = eh == null ? DEFAULT_HANDLER : eh;
  }
  
  public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler()
  {
    return callbackExceptionHandler;
  }
  




  public static void register(String libName)
  {
    register(findDirectMappedClass(getCallingClass()), libName);
  }
  




  public static void register(NativeLibrary lib)
  {
    register(findDirectMappedClass(getCallingClass()), lib);
  }
  
  static Class findDirectMappedClass(Class cls)
  {
    Method[] methods = cls.getDeclaredMethods();
    for (int i = 0; i < methods.length; i++) {
      if ((methods[i].getModifiers() & 0x100) != 0) {
        return cls;
      }
    }
    int idx = cls.getName().lastIndexOf("$");
    if (idx != -1) {
      String name = cls.getName().substring(0, idx);
      try {
        return findDirectMappedClass(Class.forName(name, true, cls.getClassLoader()));
      }
      catch (ClassNotFoundException e) {}
    }
    
    throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
  }
  


  static Class getCallingClass()
  {
    Class[] context = new SecurityManager() {
      public Class[] getClassContext() {
        return super.getClassContext();
      }
    }.getClassContext();
    if (context == null) {
      throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
    }
    if (context.length < 4) {
      throw new IllegalStateException("This method must be called from the static initializer of a class");
    }
    return context[3];
  }
  




  public static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer)
  {
    CallbackReference.setCallbackThreadInitializer(cb, initializer);
  }
  

  private static Map registeredClasses = new HashMap();
  private static Map registeredLibraries = new HashMap();
  private static Object unloader = new Object() {
    protected void finalize() { Iterator i;
      synchronized (Native.registeredClasses) {
        for (i = Native.registeredClasses.entrySet().iterator(); i.hasNext();) {
          Map.Entry e = (Map.Entry)i.next();
          Native.unregister((Class)e.getKey(), (long[])e.getValue());
          i.remove();
        }
      }
    } };
  static final int CB_HAS_INITIALIZER = 1;
  private static final int CVT_UNSUPPORTED = -1;
  private static final int CVT_DEFAULT = 0;
  private static final int CVT_POINTER = 1;
  private static final int CVT_STRING = 2;
  private static final int CVT_STRUCTURE = 3;
  
  public static void unregister() { unregister(findDirectMappedClass(getCallingClass())); }
  




  public static void unregister(Class cls)
  {
    synchronized (registeredClasses) {
      if (registeredClasses.containsKey(cls)) {
        unregister(cls, (long[])registeredClasses.get(cls));
        registeredClasses.remove(cls);
        registeredLibraries.remove(cls);
      }
    }
  }
  


  private static String getSignature(Class cls)
  {
    if (cls.isArray()) {
      return "[" + getSignature(cls.getComponentType());
    }
    if (cls.isPrimitive()) {
      if (cls == Void.TYPE) return "V";
      if (cls == Boolean.TYPE) return "Z";
      if (cls == Byte.TYPE) return "B";
      if (cls == Short.TYPE) return "S";
      if (cls == Character.TYPE) return "C";
      if (cls == Integer.TYPE) return "I";
      if (cls == Long.TYPE) return "J";
      if (cls == Float.TYPE) return "F";
      if (cls == Double.TYPE) return "D";
    }
    return "L" + replace(".", "/", cls.getName()) + ";";
  }
  
  static String replace(String s1, String s2, String str)
  {
    StringBuilder buf = new StringBuilder();
    for (;;) {
      int idx = str.indexOf(s1);
      if (idx == -1) {
        buf.append(str);
        break;
      }
      
      buf.append(str.substring(0, idx));
      buf.append(s2);
      str = str.substring(idx + s1.length());
    }
    
    return buf.toString();
  }
  



  private static final int CVT_STRUCTURE_BYVAL = 4;
  


  private static final int CVT_BUFFER = 5;
  


  private static final int CVT_ARRAY_BYTE = 6;
  

  private static final int CVT_ARRAY_SHORT = 7;
  

  private static final int CVT_ARRAY_CHAR = 8;
  

  private static final int CVT_ARRAY_INT = 9;
  

  private static final int CVT_ARRAY_LONG = 10;
  

  private static int getConversion(Class type, TypeMapper mapper)
  {
    if (type == Boolean.class) { type = Boolean.TYPE;
    } else if (type == Byte.class) { type = Byte.TYPE;
    } else if (type == Short.class) { type = Short.TYPE;
    } else if (type == Character.class) { type = Character.TYPE;
    } else if (type == Integer.class) { type = Integer.TYPE;
    } else if (type == Long.class) { type = Long.TYPE;
    } else if (type == Float.class) { type = Float.TYPE;
    } else if (type == Double.class) { type = Double.TYPE;
    } else if (type == Void.class) { type = Void.TYPE;
    }
    if ((mapper != null) && ((mapper.getFromNativeConverter(type) != null) || (mapper.getToNativeConverter(type) != null)))
    {

      return 21;
    }
    
    if (Pointer.class.isAssignableFrom(type)) {
      return 1;
    }
    if (String.class == type) {
      return 2;
    }
    if (WString.class.isAssignableFrom(type)) {
      return 18;
    }
    if ((Platform.HAS_BUFFERS) && (Buffers.isBuffer(type))) {
      return 5;
    }
    if (Structure.class.isAssignableFrom(type)) {
      if (Structure.ByValue.class.isAssignableFrom(type)) {
        return 4;
      }
      return 3;
    }
    if (type.isArray()) {
      switch (type.getName().charAt(1)) {
      case 'Z':  return 13;
      case 'B':  return 6;
      case 'S':  return 7;
      case 'C':  return 8;
      case 'I':  return 9;
      case 'J':  return 10;
      case 'F':  return 11;
      case 'D':  return 12;
      }
      
    }
    if (type.isPrimitive()) {
      return type == Boolean.TYPE ? 14 : 0;
    }
    if (Callback.class.isAssignableFrom(type)) {
      return 15;
    }
    if (IntegerType.class.isAssignableFrom(type)) {
      return 19;
    }
    if (PointerType.class.isAssignableFrom(type)) {
      return 20;
    }
    if (NativeMapped.class.isAssignableFrom(type)) {
      return 17;
    }
    return -1;
  }
  








  public static void register(Class cls, String libName)
  {
    Map options = new HashMap();
    options.put("classloader", cls.getClassLoader());
    register(cls, NativeLibrary.getInstance(libName, options));
  }
  








  public static void register(Class cls, NativeLibrary lib)
  {
    Method[] methods = cls.getDeclaredMethods();
    List mlist = new ArrayList();
    TypeMapper mapper = (TypeMapper)lib.getOptions().get("type-mapper");
    

    for (int i = 0; i < methods.length; i++) {
      if ((methods[i].getModifiers() & 0x100) != 0) {
        mlist.add(methods[i]);
      }
    }
    long[] handles = new long[mlist.size()];
    for (int i = 0; i < handles.length; i++) {
      Method method = (Method)mlist.get(i);
      String sig = "(";
      Class rclass = method.getReturnType();
      
      Class[] ptypes = method.getParameterTypes();
      long[] atypes = new long[ptypes.length];
      long[] closure_atypes = new long[ptypes.length];
      int[] cvt = new int[ptypes.length];
      ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
      FromNativeConverter fromNative = null;
      int rcvt = getConversion(rclass, mapper);
      boolean throwLastError = false;
      long closure_rtype; long rtype; switch (rcvt) {
      case -1: 
        throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
      case 21: 
        fromNative = mapper.getFromNativeConverter(rclass);
        closure_rtype = getpeer;
        rtype = getnativeTypepeer;
        break;
      case 17: 
      case 19: 
      case 20: 
        closure_rtype = getpeer;
        rtype = getgetInstancenativeTypepeer;
        break;
      case 3: 
        closure_rtype = rtype = getpeer;
        break;
      case 4: 
        closure_rtype = getpeer;
        rtype = getpeer;
        break;
      case 0: case 1: case 2: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 18: default: 
        closure_rtype = rtype = getpeer;
      }
      
      for (int t = 0; t < ptypes.length; t++) {
        Class type = ptypes[t];
        sig = sig + getSignature(type);
        cvt[t] = getConversion(type, mapper);
        if (cvt[t] == -1) {
          throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
        }
        if ((cvt[t] == 17) || (cvt[t] == 19))
        {
          type = NativeMappedConverter.getInstance(type).nativeType();
        }
        else if (cvt[t] == 21) {
          toNative[t] = mapper.getToNativeConverter(type);
        }
        


        switch (cvt[t]) {
        case 4: 
        case 17: 
        case 19: 
        case 20: 
          atypes[t] = getpeer;
          closure_atypes[t] = getpeer;
          break;
        case 21: 
          if (type.isPrimitive()) {
            closure_atypes[t] = getpeer;
          } else
            closure_atypes[t] = getpeer;
          atypes[t] = getnativeTypepeer;
          break;
        case 0: 
          long tmp796_793 = getpeer;atypes[t] = tmp796_793;closure_atypes[t] = tmp796_793;
          break;
        default: 
          long tmp819_816 = getpeer;atypes[t] = tmp819_816;closure_atypes[t] = tmp819_816;
        }
        
      }
      sig = sig + ")";
      sig = sig + getSignature(rclass);
      
      Class[] etypes = method.getExceptionTypes();
      for (int e = 0; e < etypes.length; e++) {
        if (LastErrorException.class.isAssignableFrom(etypes[e])) {
          throwLastError = true;
          break;
        }
      }
      
      Function f = lib.getFunction(method.getName(), method);
      try {
        handles[i] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, peer, f.getCallingConvention(), throwLastError, toNative, fromNative, encoding);




      }
      catch (NoSuchMethodError e)
      {



        throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
      }
    }
    synchronized (registeredClasses) {
      registeredClasses.put(cls, handles);
      registeredLibraries.put(cls, lib);
    }
    cacheOptions(cls, lib.getOptions(), null);
  }
  


  private static void cacheOptions(Class cls, Map libOptions, Object proxy)
  {
    libOptions = new HashMap(libOptions);
    synchronized (libraries) {
      options.put(cls, libOptions);
      if (proxy != null) {
        libraries.put(cls, new WeakReference(proxy));
      }
      



      if ((!cls.isInterface()) && (Library.class.isAssignableFrom(cls)))
      {
        Class[] ifaces = cls.getInterfaces();
        for (int i = 0; i < ifaces.length; i++) {
          if (Library.class.isAssignableFrom(ifaces[i])) {
            cacheOptions(ifaces[i], libOptions, proxy);
            break;
          }
        }
      }
    }
  }
  




















  private static NativeMapped fromNative(Class cls, Object value)
  {
    return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
  }
  
  private static Class nativeType(Class cls) {
    return NativeMappedConverter.getInstance(cls).nativeType();
  }
  

  private static Object toNative(ToNativeConverter cvt, Object o)
  {
    return cvt.toNative(o, new ToNativeContext());
  }
  
  private static Object fromNative(FromNativeConverter cvt, Object o, Class cls)
  {
    return cvt.fromNative(o, new FromNativeContext(cls));
  }
  














  public static void main(String[] args)
  {
    String DEFAULT_TITLE = "Java Native Access (JNA)";
    String DEFAULT_VERSION = "4.1.0";
    String DEFAULT_BUILD = "4.1.0 (package information missing)";
    Package pkg = Native.class.getPackage();
    String title = pkg != null ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
    
    if (title == null) title = "Java Native Access (JNA)";
    String version = pkg != null ? pkg.getSpecificationVersion() : "4.1.0";
    
    if (version == null) version = "4.1.0";
    title = title + " API Version " + version;
    System.out.println(title);
    version = pkg != null ? pkg.getImplementationVersion() : "4.1.0 (package information missing)";
    
    if (version == null) version = "4.1.0 (package information missing)";
    System.out.println("Version: " + version);
    System.out.println(" Native: " + getNativeVersion() + " (" + getAPIChecksum() + ")");
    
    System.out.println(" Prefix: " + Platform.RESOURCE_PREFIX);
  }
  







  private static final int CVT_ARRAY_FLOAT = 11;
  






  private static final int CVT_ARRAY_DOUBLE = 12;
  






  private static final int CVT_ARRAY_BOOLEAN = 13;
  






  private static final int CVT_BOOLEAN = 14;
  






  private static final int CVT_CALLBACK = 15;
  






  private static final int CVT_FLOAT = 16;
  





  private static final int CVT_NATIVE_MAPPED = 17;
  





  private static final int CVT_WSTRING = 18;
  





  private static final int CVT_INTEGER_TYPE = 19;
  





  private static final int CVT_POINTER_TYPE = 20;
  





  private static final int CVT_TYPE_MAPPER = 21;
  





  static final int CB_OPTION_DIRECT = 1;
  





  static final int CB_OPTION_IN_DLL = 2;
  





  static Structure invokeStructure(long fp, int callFlags, Object[] args, Structure s)
  {
    invokeStructure(fp, callFlags, args, getPointerpeer, getTypeInfopeer);
    
    return s;
  }
  











  static long open(String name)
  {
    return open(name, -1);
  }
  





















































  static Pointer getPointer(long addr)
  {
    long peer = _getPointer(addr);
    return peer == 0L ? null : new Pointer(peer);
  }
  



  static String getString(long addr)
  {
    return getString(addr, getDefaultStringEncoding());
  }
  
  static String getString(long addr, String encoding) {
    byte[] data = getStringBytes(addr);
    if (encoding != null) {
      try {
        return new String(data, encoding);
      }
      catch (UnsupportedEncodingException e) {}
    }
    
    return new String(data);
  }
  

























































  public static void detach(boolean detach)
  {
    Thread thread = Thread.currentThread();
    if (detach)
    {





      nativeThreads.remove(thread);
      Pointer p = (Pointer)nativeThreadTerminationFlag.get();
      setDetachState(true, 0L);

    }
    else if (!nativeThreads.containsKey(thread)) {
      Pointer p = (Pointer)nativeThreadTerminationFlag.get();
      nativeThreads.put(thread, p);
      setDetachState(false, peer);
    }
  }
  
  static Pointer getTerminationFlag(Thread t)
  {
    return (Pointer)nativeThreads.get(t);
  }
  
  private static Map nativeThreads = Collections.synchronizedMap(new WeakHashMap());
  
  private static ThreadLocal nativeThreadTerminationFlag = new ThreadLocal()
  {
    protected Object initialValue() {
      Memory m = new Memory(4L);
      m.clear();
      return m; } };
  
  private Native() {}
  
  public static abstract interface ffi_callback { public abstract void invoke(long paramLong1, long paramLong2, long paramLong3); }
  
  private static class Buffers { private Buffers() {}
    
    static boolean isBuffer(Class cls) { return Buffer.class.isAssignableFrom(cls); }
  }
  
  private static native void initIDs();
  
  private static class AWT {
    private AWT() {}
    
    static long getWindowID(Window w) throws HeadlessException {
      return getComponentID(w);
    }
    
    static long getComponentID(Object o) throws HeadlessException
    {
      if (GraphicsEnvironment.isHeadless()) {
        throw new HeadlessException("No native windows when headless");
      }
      Component c = (Component)o;
      if (c.isLightweight()) {
        throw new IllegalArgumentException("Component must be heavyweight");
      }
      if (!c.isDisplayable()) {
        throw new IllegalStateException("Component must be displayable");
      }
      if ((Platform.isX11()) && (System.getProperty("java.version").startsWith("1.4")))
      {
        if (!c.isVisible()) {
          throw new IllegalStateException("Component must be visible");
        }
      }
      


      return Native.getWindowHandle0(c);
    }
  }
  
  public static synchronized native void setProtected(boolean paramBoolean);
  
  public static synchronized native boolean isProtected();
  
  /**
   * @deprecated
   */
  public static void setPreserveLastError(boolean enable) {}
  
  static native long getWindowHandle0(Component paramComponent);
  
  private static native long _getDirectBufferPointer(Buffer paramBuffer);
  
  private static native int sizeof(int paramInt);
  
  private static native String getNativeVersion();
  
  private static native String getAPIChecksum();
  
  public static native int getLastError();
  
  public static native void setLastError(int paramInt);
  
  private static native void unregister(Class paramClass, long[] paramArrayOfLong);
  
  private static native long registerMethod(Class paramClass1, String paramString1, String paramString2, int[] paramArrayOfInt, long[] paramArrayOfLong1, long[] paramArrayOfLong2, int paramInt1, long paramLong1, long paramLong2, Class paramClass2, long paramLong3, int paramInt2, boolean paramBoolean, ToNativeConverter[] paramArrayOfToNativeConverter, FromNativeConverter paramFromNativeConverter, String paramString3);
  
  public static native long ffi_prep_cif(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static native void ffi_call(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public static native long ffi_prep_closure(long paramLong, ffi_callback paramFfi_callback);
  
  public static native void ffi_free_closure(long paramLong);
  
  static native int initialize_ffi_type(long paramLong);
  
  static synchronized native void freeNativeCallback(long paramLong);
  
  static synchronized native long createNativeCallback(Callback paramCallback, Method paramMethod, Class[] paramArrayOfClass, Class paramClass, int paramInt1, int paramInt2, String paramString);
  
  static native int invokeInt(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long invokeLong(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native void invokeVoid(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native float invokeFloat(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native double invokeDouble(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long invokePointer(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  private static native void invokeStructure(long paramLong1, int paramInt, Object[] paramArrayOfObject, long paramLong2, long paramLong3);
  
  static native Object invokeObject(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long open(String paramString, int paramInt);
  
  static native void close(long paramLong);
  
  static native long findSymbol(long paramLong, String paramString);
  
  static native long indexOf(long paramLong, byte paramByte);
  
  static native void read(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, short[] paramArrayOfShort, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, int[] paramArrayOfInt, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, long[] paramArrayOfLong, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, short[] paramArrayOfShort, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, int[] paramArrayOfInt, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, long[] paramArrayOfLong, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  static native byte getByte(long paramLong);
  
  static native char getChar(long paramLong);
  
  static native short getShort(long paramLong);
  
  static native int getInt(long paramLong);
  
  static native long getLong(long paramLong);
  
  static native float getFloat(long paramLong);
  
  static native double getDouble(long paramLong);
  
  private static native long _getPointer(long paramLong);
  
  static native String getWideString(long paramLong);
  
  static native byte[] getStringBytes(long paramLong);
  
  static native void setMemory(long paramLong1, long paramLong2, byte paramByte);
  
  static native void setByte(long paramLong, byte paramByte);
  
  static native void setShort(long paramLong, short paramShort);
  
  static native void setChar(long paramLong, char paramChar);
  
  static native void setInt(long paramLong, int paramInt);
  
  static native void setLong(long paramLong1, long paramLong2);
  
  static native void setFloat(long paramLong, float paramFloat);
  
  static native void setDouble(long paramLong, double paramDouble);
  
  static native void setPointer(long paramLong1, long paramLong2);
  
  static native void setWideString(long paramLong, String paramString);
  
  public static native long malloc(long paramLong);
  
  public static native void free(long paramLong);
  
  public static native ByteBuffer getDirectByteBuffer(long paramLong1, long paramLong2);
  
  private static native void setDetachState(boolean paramBoolean, long paramLong);
}
