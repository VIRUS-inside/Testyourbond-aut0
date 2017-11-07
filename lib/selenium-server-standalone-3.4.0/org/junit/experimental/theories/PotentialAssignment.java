package org.junit.experimental.theories;

public abstract class PotentialAssignment
{
  public PotentialAssignment() {}
  
  public static class CouldNotGenerateValueException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public CouldNotGenerateValueException() {}
    
    public CouldNotGenerateValueException(Throwable e) {
      super();
    }
  }
  
  public static PotentialAssignment forValue(final String name, Object value) {
    new PotentialAssignment()
    {
      public Object getValue() {
        return val$value;
      }
      
      public String toString()
      {
        return String.format("[%s]", new Object[] { val$value });
      }
      
      public String getDescription()
      {
        String valueString;
        String valueString;
        if (val$value == null) {
          valueString = "null";
        } else {
          try {
            valueString = String.format("\"%s\"", new Object[] { val$value });
          } catch (Throwable e) {
            valueString = String.format("[toString() threw %s: %s]", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
          }
        }
        

        return String.format("%s <from %s>", new Object[] { valueString, name });
      }
    };
  }
  
  public abstract Object getValue()
    throws PotentialAssignment.CouldNotGenerateValueException;
  
  public abstract String getDescription()
    throws PotentialAssignment.CouldNotGenerateValueException;
}
