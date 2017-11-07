package com.gargoylesoftware.htmlunit.html.applets;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





















public class AppletClassLoader
  extends URLClassLoader
{
  private static final Log LOG = LogFactory.getLog(AppletClassLoader.class);
  



  public AppletClassLoader(Window window)
  {
    super(new URL[0]);
    try
    {
      loadOurNetscapeStuff("netscape.javascript.JSException");
      Class<?> jsObjectClass = loadOurNetscapeStuff("netscape.javascript.JSObject");
      MethodUtils.invokeExactStaticMethod(jsObjectClass, "setWindow", new Object[] { window });
    }
    catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }
  



  public void addArchiveToClassPath(URL jarUrl)
  {
    addURL(jarUrl);
  }
  




  public void addClassToClassPath(String className, WebResponse webResponse)
    throws IOException
  {
    byte[] bytes = IOUtils.toByteArray(webResponse.getContentAsStream());
    defineClass(className, bytes, 0, bytes.length);
  }
  
  private Class<?> loadOurNetscapeStuff(String classNane) throws IOException {
    String myNetscapeClassName = classNane.replace('.', '/') + ".class";
    
    Enumeration<URL> locations = getClass().getClassLoader().getResources(myNetscapeClassName);
    URL myLocation = getClass().getProtectionDomain().getCodeSource().getLocation();
    while (locations.hasMoreElements()) {
      URL pos = (URL)locations.nextElement();
      if (pos.toExternalForm().contains(myLocation.toExternalForm())) {
        Object localObject1 = null;Object localObject4 = null; Object localObject3; try { InputStream byteStream = pos.openStream();
          try { byte[] bytes = IOUtils.toByteArray(byteStream);
            return defineClass(classNane, bytes, 0, bytes.length);
          } finally { if (byteStream != null) byteStream.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
        }
      } }
    return null;
  }
}
