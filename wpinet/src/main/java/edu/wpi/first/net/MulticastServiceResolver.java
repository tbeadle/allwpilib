// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.net;

import edu.wpi.first.util.WPICleaner;
import java.lang.ref.Cleaner.Cleanable;

/** Class to resolve a service over mDNS. */
public class MulticastServiceResolver implements AutoCloseable {
  private final int m_handle;
  private final Cleanable m_cleanable;

  private static Runnable cleanupAction(int handle) {
    return () -> WPINetJNI.freeMulticastServiceResolver(handle);
  }

  /**
   * Creates a MulticastServiceResolver.
   *
   * @param serviceType service type to look for
   */
  public MulticastServiceResolver(String serviceType) {
    m_handle = WPINetJNI.createMulticastServiceResolver(serviceType);
    m_cleanable = WPICleaner.register(this, cleanupAction(m_handle));
  }

  @Override
  public void close() {
    m_cleanable.clean();
  }

  public void start() {
    WPINetJNI.startMulticastServiceResolver(m_handle);
  }

  public void stop() {
    WPINetJNI.stopMulticastServiceResolver(m_handle);
  }

  public boolean hasImplementation() {
    return WPINetJNI.getMulticastServiceResolverHasImplementation(m_handle);
  }

  public int getEventHandle() {
    return WPINetJNI.getMulticastServiceResolverEventHandle(m_handle);
  }

  public ServiceData[] getData() {
    return WPINetJNI.getMulticastServiceResolverData(m_handle);
  }
}
