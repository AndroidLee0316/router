package com.pasc.lib.router;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.pasc.lib.router.interceptor.BaseRouterTable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Iterator;

public class Utils {
  static <T> void validateServiceInterface(Class<T> service) {
    if (!service.isInterface()) {
      throw new IllegalArgumentException("API declarations must be interfaces.");
    }
    // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
    // Android (http://b.android.com/58753) but it forces composition of API declarations which is
    // the recommended pattern.
    if (service.getInterfaces().length > 0) {
      throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
    }
  }


  static boolean hasUnresolvableType(Type type) {
    if (type instanceof Class<?>) {
      return false;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
        if (hasUnresolvableType(typeArgument)) {
          return true;
        }
      }
      return false;
    }
    if (type instanceof GenericArrayType) {
      return hasUnresolvableType(((GenericArrayType) type).getGenericComponentType());
    }
    if (type instanceof TypeVariable) {
      return true;
    }
    if (type instanceof WildcardType) {
      return true;
    }
    String className = type == null ? "null" : type.getClass().getName();
    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
        + "GenericArrayType, but <" + type + "> is of type " + className);
  }




  static Class<?> getRawType(Type type) {
    if (type == null) throw new NullPointerException("type == null");

    if (type instanceof Class<?>) {
      // Type is a normal class.
      return (Class<?>) type;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
      // suspects some pathological case related to nested classes exists.
      Type rawType = parameterizedType.getRawType();
      if (!(rawType instanceof Class)) throw new IllegalArgumentException();
      return (Class<?>) rawType;
    }
    if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    }
    if (type instanceof TypeVariable) {
      // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
      // type that's more general than necessary is okay.
      return Object.class;
    }
    if (type instanceof WildcardType) {
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    }

    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
        + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
  }



  static Type getParameterUpperBound(int index, ParameterizedType type) {
    Type[] types = type.getActualTypeArguments();
    if (index < 0 || index >= types.length) {
      throw new IllegalArgumentException(
          "Index " + index + " not in range [0," + types.length + ") for " + type);
    }
    Type paramType = types[index];
    if (paramType instanceof WildcardType) {
      return ((WildcardType) paramType).getUpperBounds()[0];
    }
    return paramType;
  }

  /**
   * 移除登录认证相关的info
   * @return
   */
  public static Bundle removeBundleInfo(Bundle bundle){
    if(bundle==null){
      return bundle;
    }
    Bundle tempBundle=new Bundle();
    tempBundle.putAll(bundle);
    tempBundle.remove(BaseRouterTable.BundleKey.KEY_NEED_LOGIN);
    tempBundle.remove(BaseRouterTable.BundleKey.KEY_NEED_IDENTITY);
    Log.d("bundle","remove bundle "+String.valueOf(tempBundle!=null?tempBundle.get("certify_bank_temp"):""));
    return tempBundle;
  }
}
