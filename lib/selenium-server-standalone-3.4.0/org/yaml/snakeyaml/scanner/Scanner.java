package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.Token.ID;

public abstract interface Scanner
{
  public abstract boolean checkToken(Token.ID... paramVarArgs);
  
  public abstract Token peekToken();
  
  public abstract Token getToken();
}
