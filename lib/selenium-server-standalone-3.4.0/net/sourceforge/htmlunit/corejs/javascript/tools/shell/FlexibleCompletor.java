package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;























































































































































































































































































































































































































class FlexibleCompletor
  implements InvocationHandler
{
  private Method completeMethod;
  private Scriptable global;
  
  FlexibleCompletor(Class<?> completorClass, Scriptable global)
    throws NoSuchMethodException
  {
    this.global = global;
    completeMethod = completorClass.getMethod("complete", new Class[] { String.class, Integer.TYPE, List.class });
  }
  

  public Object invoke(Object proxy, Method method, Object[] args)
  {
    if (method.equals(completeMethod)) {
      int result = complete((String)args[0], ((Integer)args[1])
        .intValue(), (List)args[2]);
      return Integer.valueOf(result);
    }
    throw new NoSuchMethodError(method.toString());
  }
  





  public int complete(String buffer, int cursor, List<String> candidates)
  {
    int m = cursor - 1;
    while (m >= 0) {
      char c = buffer.charAt(m);
      if ((!Character.isJavaIdentifierPart(c)) && (c != '.'))
        break;
      m--;
    }
    String namesAndDots = buffer.substring(m + 1, cursor);
    String[] names = namesAndDots.split("\\.", -1);
    Scriptable obj = global;
    for (int i = 0; i < names.length - 1; i++) {
      Object val = obj.get(names[i], global);
      if ((val instanceof Scriptable)) {
        obj = (Scriptable)val;
      } else {
        return buffer.length();
      }
    }
    
    Object[] ids = (obj instanceof ScriptableObject) ? ((ScriptableObject)obj).getAllIds() : obj.getIds();
    String lastPart = names[(names.length - 1)];
    for (int i = 0; i < ids.length; i++)
      if ((ids[i] instanceof String))
      {
        String id = (String)ids[i];
        if (id.startsWith(lastPart)) {
          if ((obj.get(id, obj) instanceof Function))
            id = id + "(";
          candidates.add(id);
        }
      }
    return buffer.length() - lastPart.length();
  }
}
