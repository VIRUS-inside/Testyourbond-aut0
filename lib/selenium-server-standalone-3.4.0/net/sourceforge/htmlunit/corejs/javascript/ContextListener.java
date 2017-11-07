package net.sourceforge.htmlunit.corejs.javascript;

@Deprecated
public abstract interface ContextListener
  extends ContextFactory.Listener
{
  @Deprecated
  public abstract void contextEntered(Context paramContext);
  
  @Deprecated
  public abstract void contextExited(Context paramContext);
}
