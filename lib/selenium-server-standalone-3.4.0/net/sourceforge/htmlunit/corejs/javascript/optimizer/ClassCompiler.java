package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import net.sourceforge.htmlunit.corejs.javascript.CompilerEnvirons;
import net.sourceforge.htmlunit.corejs.javascript.IRFactory;
import net.sourceforge.htmlunit.corejs.javascript.JavaAdapter;
import net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap;
import net.sourceforge.htmlunit.corejs.javascript.Parser;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ast.AstRoot;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;







public class ClassCompiler
{
  private String mainMethodClassName;
  private CompilerEnvirons compilerEnv;
  private Class<?> targetExtends;
  private Class<?>[] targetImplements;
  
  public ClassCompiler(CompilerEnvirons compilerEnv)
  {
    if (compilerEnv == null)
      throw new IllegalArgumentException();
    this.compilerEnv = compilerEnv;
    mainMethodClassName = "net.sourceforge.htmlunit.corejs.javascript.optimizer.OptRuntime";
  }
  








  public void setMainMethodClass(String className)
  {
    mainMethodClassName = className;
  }
  




  public String getMainMethodClass()
  {
    return mainMethodClassName;
  }
  


  public CompilerEnvirons getCompilerEnv()
  {
    return compilerEnv;
  }
  


  public Class<?> getTargetExtends()
  {
    return targetExtends;
  }
  





  public void setTargetExtends(Class<?> extendsClass)
  {
    targetExtends = extendsClass;
  }
  


  public Class<?>[] getTargetImplements()
  {
    return targetImplements == null ? null : 
      (Class[])targetImplements.clone();
  }
  







  public void setTargetImplements(Class<?>[] implementsClasses)
  {
    targetImplements = (implementsClasses == null ? null : (Class[])implementsClasses.clone());
  }
  







  protected String makeAuxiliaryClassName(String mainClassName, String auxMarker)
  {
    return mainClassName + auxMarker;
  }
  












  public Object[] compileToClassFiles(String source, String sourceLocation, int lineno, String mainClassName)
  {
    Parser p = new Parser(compilerEnv);
    AstRoot ast = p.parse(source, sourceLocation, lineno);
    IRFactory irf = new IRFactory(compilerEnv);
    ScriptNode tree = irf.transformTree(ast);
    

    irf = null;
    ast = null;
    p = null;
    
    Class<?> superClass = getTargetExtends();
    Class<?>[] interfaces = getTargetImplements();
    
    boolean isPrimary = (interfaces == null) && (superClass == null);
    String scriptClassName; String scriptClassName; if (isPrimary) {
      scriptClassName = mainClassName;
    } else {
      scriptClassName = makeAuxiliaryClassName(mainClassName, "1");
    }
    
    Codegen codegen = new Codegen();
    codegen.setMainMethodClass(mainMethodClassName);
    byte[] scriptClassBytes = codegen.compileToClassFile(compilerEnv, scriptClassName, tree, tree
      .getEncodedSource(), false);
    
    if (isPrimary) {
      return new Object[] { scriptClassName, scriptClassBytes };
    }
    int functionCount = tree.getFunctionCount();
    ObjToIntMap functionNames = new ObjToIntMap(functionCount);
    for (int i = 0; i != functionCount; i++) {
      FunctionNode ofn = tree.getFunctionNode(i);
      String name = ofn.getName();
      if ((name != null) && (name.length() != 0)) {
        functionNames.put(name, ofn.getParamCount());
      }
    }
    if (superClass == null) {
      superClass = ScriptRuntime.ObjectClass;
    }
    byte[] mainClassBytes = JavaAdapter.createAdapterCode(functionNames, mainClassName, superClass, interfaces, scriptClassName);
    

    return new Object[] { mainClassName, mainClassBytes, scriptClassName, scriptClassBytes };
  }
}
