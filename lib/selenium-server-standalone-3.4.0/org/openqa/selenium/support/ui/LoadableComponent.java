package org.openqa.selenium.support.ui;



















public abstract class LoadableComponent<T extends LoadableComponent<T>>
{
  public LoadableComponent() {}
  

















  public T get()
  {
    try
    {
      isLoaded();
      return this;
    } catch (Error e) {
      load();
      

      isLoaded();
    }
    return this;
  }
  
  protected abstract void load();
  
  protected abstract void isLoaded()
    throws Error;
}
