package java_cup.runtime;

public abstract interface Scanner
{
  public abstract Symbol next_token()
    throws Exception;
}
