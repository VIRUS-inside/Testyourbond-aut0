package net.sourceforge.htmlunit.corejs.javascript;

import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;

public abstract interface Evaluator
{
  public abstract Object compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean);
  
  public abstract Function createFunctionObject(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2);
  
  public abstract Script createScriptObject(Object paramObject1, Object paramObject2);
  
  public abstract void captureStackInfo(RhinoException paramRhinoException);
  
  public abstract String getSourcePositionFromStack(Context paramContext, int[] paramArrayOfInt);
  
  public abstract String getPatchedStack(RhinoException paramRhinoException, String paramString);
  
  public abstract List<String> getScriptStack(RhinoException paramRhinoException);
  
  public abstract void setEvalScriptFlag(Script paramScript);
}
