package org.openqa.selenium.remote.server.rest;

public abstract interface RestishHandler<T>
{
  public abstract T handle()
    throws Exception;
}
