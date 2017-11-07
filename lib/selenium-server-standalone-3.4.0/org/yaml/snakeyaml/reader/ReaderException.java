package org.yaml.snakeyaml.reader;

import org.yaml.snakeyaml.error.YAMLException;













public class ReaderException
  extends YAMLException
{
  private static final long serialVersionUID = 8710781187529689083L;
  private final String name;
  private final char character;
  private final int position;
  
  public ReaderException(String name, int position, char character, String message)
  {
    super(message);
    this.name = name;
    this.character = character;
    this.position = position;
  }
  
  public String getName() {
    return name;
  }
  
  public char getCharacter() {
    return character;
  }
  
  public int getPosition() {
    return position;
  }
  
  public String toString()
  {
    return "unacceptable character '" + character + "' (0x" + Integer.toHexString(character).toUpperCase() + ") " + getMessage() + "\nin \"" + name + "\", position " + position;
  }
}
