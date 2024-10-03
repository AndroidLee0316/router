package com.pasc.lib.router;

import android.app.Application;

public class RouterGraph {

  private static Navigator navigator;

  static {
    Navigator.Builder builder = new Navigator.Builder();
    navigator = builder.build();
  }

  public static <T> T createGraph(Class<T> api) {
    return navigator.create(api);
  }

  public static void unregisterActivityCallBack(Application application) {
    if (application != null) {
      application.unregisterActivityLifecycleCallbacks(navigator);
    }
  }

  public static void registerActivityCallBack(Application application) {
    if (application != null) {
      application.registerActivityLifecycleCallbacks(navigator);
    }
  }
}
