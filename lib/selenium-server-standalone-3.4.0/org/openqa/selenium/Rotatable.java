package org.openqa.selenium;

public abstract interface Rotatable
{
  public abstract void rotate(ScreenOrientation paramScreenOrientation);
  
  public abstract ScreenOrientation getOrientation();
  
  public abstract void rotate(DeviceRotation paramDeviceRotation);
  
  public abstract DeviceRotation rotation();
}
