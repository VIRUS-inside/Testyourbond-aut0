package org.junit.experimental.theories;

import java.util.List;

public abstract class ParameterSupplier
{
  public ParameterSupplier() {}
  
  public abstract List<PotentialAssignment> getValueSources(ParameterSignature paramParameterSignature)
    throws Throwable;
}
