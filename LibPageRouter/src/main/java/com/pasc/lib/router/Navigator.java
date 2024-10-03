package com.pasc.lib.router;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Navigator implements Application.ActivityLifecycleCallbacks {
  private static volatile boolean inited;
  private final Map<Method, RouterPath> routerPathCache = new ConcurrentHashMap<>();

  private final boolean validateEagerly;
  private Activity activity;

  private Navigator(boolean validateEagerly) {
    this.validateEagerly = validateEagerly;
  }

  @SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
  public <T> T create(final Class<T> service) {
    Utils.validateServiceInterface(service);
    if (validateEagerly) {
      eagerlyValidateMethods(service);
    }
    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
          private final Platform platform = Platform.get();

          @Override public Object invoke(Object proxy, Method method, @Nullable Object[] args)
              throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }
            if (platform.isDefaultMethod(method)) {
              return platform.invokeDefaultMethod(method, service, proxy, args);
            }
            RouterPath routerPath = loadRouterPath(method);
            RouterPostCard postCard = new RouterPostCard(routerPath, args);
            return postCard.navigation(activity);
          }
        });
  }

  private void eagerlyValidateMethods(Class<?> service) {
    Platform platform = Platform.get();
    for (Method method : service.getDeclaredMethods()) {
      if (!platform.isDefaultMethod(method)) {
        loadRouterPath(method);
      }
    }
  }

  private RouterPath loadRouterPath(Method method) {
    RouterPath result = routerPathCache.get(method);
    if (result != null) return result;

    synchronized (routerPathCache) {
      result = routerPathCache.get(method);
      if (result == null) {
        result = new RouterPath.Builder(this, method).build();
        routerPathCache.put(method, result);
      }
    }
    return result;
  }

  public static Class<?> findDestination(String path) {
    Postcard postcard = ARouter.getInstance().build(path);
    LogisticsCenter.completion(postcard);
    return postcard.getDestination();
  }

  public static void initRouter(Application application, boolean debug) {
    if (debug) {
      ARouter.openLog();
      ARouter.openDebug();
    }
    ARouter.init(application);
    inited = true;
  }

  public static void release() {
    if (inited) {
      ARouter.getInstance().destroy();
      inited = false;
    }
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

  }

  @Override public void onActivityStarted(Activity activity) {

  }

  @Override public void onActivityResumed(Activity activity) {
    this.activity = activity;
  }

  @Override public void onActivityPaused(Activity activity) {

  }

  @Override public void onActivityStopped(Activity activity) {

  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override public void onActivityDestroyed(Activity activity) {
    this.activity = null;
  }

  public static final class Builder {
    private boolean validateEagerly;

    public Builder validateEagerly(boolean validateEagerly) {
      this.validateEagerly = validateEagerly;
      return this;
    }

    public Navigator build() {
      return new Navigator(validateEagerly);
    }
  }
}
