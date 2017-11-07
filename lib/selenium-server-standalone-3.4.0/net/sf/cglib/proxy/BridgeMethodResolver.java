package net.sf.cglib.proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..MethodVisitor;
import net.sf.cglib.core.Signature;




























class BridgeMethodResolver
{
  private final Map declToBridge;
  
  public BridgeMethodResolver(Map declToBridge)
  {
    this.declToBridge = declToBridge;
  }
  



  public Map resolveAll()
  {
    Map resolved = new HashMap();
    for (Iterator entryIter = declToBridge.entrySet().iterator(); entryIter.hasNext();) {
      Map.Entry entry = (Map.Entry)entryIter.next();
      Class owner = (Class)entry.getKey();
      Set bridges = (Set)entry.getValue();
      try
      {
        new .ClassReader(owner.getName()).accept(new BridgedFinder(bridges, resolved), 6);
      }
      catch (IOException localIOException) {}
    }
    return resolved;
  }
  
  private static class BridgedFinder extends .ClassVisitor
  {
    private Map resolved;
    private Set eligableMethods;
    private Signature currentMethod = null;
    
    BridgedFinder(Set eligableMethods, Map resolved) {
      super();
      this.resolved = resolved;
      this.eligableMethods = eligableMethods;
    }
    

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {}
    

    public .MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
      Signature sig = new Signature(name, desc);
      if (eligableMethods.remove(sig)) {
        currentMethod = sig;
        new .MethodVisitor(327680)
        {
          public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if ((opcode == 183) && (currentMethod != null)) {
              Signature target = new Signature(name, desc);
              





              if (!target.equals(currentMethod)) {
                resolved.put(currentMethod, target);
              }
              currentMethod = null;
            }
          }
        };
      }
      return null;
    }
  }
}
