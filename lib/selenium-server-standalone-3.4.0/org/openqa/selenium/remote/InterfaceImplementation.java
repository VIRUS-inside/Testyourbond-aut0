package org.openqa.selenium.remote;

import java.lang.reflect.Method;

public abstract interface InterfaceImplementation
{
  public abstract Object invoke(ExecuteMethod paramExecuteMethod, Object paramObject, Method paramMethod, Object... paramVarArgs);
}
