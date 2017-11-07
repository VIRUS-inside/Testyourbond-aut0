package net.sourceforge.htmlunit.corejs.javascript.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.UniqueTag;
















public class ScriptableInputStream
  extends ObjectInputStream
{
  private Scriptable scope;
  private ClassLoader classLoader;
  
  public ScriptableInputStream(InputStream in, Scriptable scope)
    throws IOException
  {
    super(in);
    this.scope = scope;
    enableResolveObject(true);
    Context cx = Context.getCurrentContext();
    if (cx != null) {
      classLoader = cx.getApplicationClassLoader();
    }
  }
  
  protected Class<?> resolveClass(ObjectStreamClass desc)
    throws IOException, ClassNotFoundException
  {
    String name = desc.getName();
    if (classLoader != null) {
      try {
        return classLoader.loadClass(name);
      }
      catch (ClassNotFoundException localClassNotFoundException) {}
    }
    
    return super.resolveClass(desc);
  }
  
  protected Object resolveObject(Object obj) throws IOException
  {
    if ((obj instanceof ScriptableOutputStream.PendingLookup))
    {
      String name = ((ScriptableOutputStream.PendingLookup)obj).getName();
      obj = ScriptableOutputStream.lookupQualifiedName(scope, name);
      if (obj == Scriptable.NOT_FOUND) {
        throw new IOException("Object " + name + " not found upon deserialization.");
      }
    }
    else if ((obj instanceof UniqueTag)) {
      obj = ((UniqueTag)obj).readResolve();
    } else if ((obj instanceof Undefined)) {
      obj = ((Undefined)obj).readResolve();
    }
    return obj;
  }
}
