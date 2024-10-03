package com.pasc.lib.router;

import com.pasc.lib.router.exception.IncompleteParameterException;
import com.pasc.lib.router.param.AnimParam;
import com.pasc.lib.router.param.BooleanParam;
import com.pasc.lib.router.param.BundleParam;
import com.pasc.lib.router.param.ByteParam;
import com.pasc.lib.router.param.CallBack;
import com.pasc.lib.router.param.CharParam;
import com.pasc.lib.router.param.CharSequenceParam;
import com.pasc.lib.router.param.ContextParam;
import com.pasc.lib.router.param.DoubleParam;
import com.pasc.lib.router.param.EnterAnimParam;
import com.pasc.lib.router.param.ExitAnimParam;
import com.pasc.lib.router.param.FlagParam;
import com.pasc.lib.router.param.FloatParam;
import com.pasc.lib.router.param.ForResult;
import com.pasc.lib.router.param.IntParam;
import com.pasc.lib.router.param.LongParam;
import com.pasc.lib.router.param.ObjectParam;
import com.pasc.lib.router.param.OnlyJump;
import com.pasc.lib.router.param.ParcelableParam;
import com.pasc.lib.router.param.PathParam;
import com.pasc.lib.router.param.ReceiverActivity;
import com.pasc.lib.router.param.RequestCodeParam;
import com.pasc.lib.router.param.SerializableParam;
import com.pasc.lib.router.param.Service;
import com.pasc.lib.router.param.StringParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class RouterPath {

  private final int pathIndex;
  private final String path;
  private final Navigator navigator;
  private final ParameterHandler<?>[] parameterHandlers;
  private final boolean forResult;
  private final boolean isService;

  private RouterPath(Builder builder) {
    path = builder.path;
    pathIndex = builder.pathIndex;
    navigator = builder.navigator;
    parameterHandlers = builder.parameterHandlers;
    forResult = builder.forResult;
    isService = builder.isService;
  }

  public PostCard toPostCard(Object... args) throws Exception {
    String path = this.path;
    if (pathIndex != -1) {
      path = (String) args[pathIndex];
    }
    PostCard.PostCardBuilder
        postCardBuilder = new PostCard.PostCardBuilder(path, isService, forResult);

    @SuppressWarnings("unchecked") // It is an error to invoke a method with the wrong arg types.
        ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) parameterHandlers;

    int argumentCount = args != null ? args.length : 0;
    if (argumentCount != handlers.length) {
      throw new IllegalArgumentException("Argument count (" + argumentCount
          + ") doesn't match expected count (" + handlers.length + ")");
    }

    for (int p = 0; p < argumentCount; p++) {
      handlers[p].apply(postCardBuilder, args[p]);
    }

    return postCardBuilder.build();
  }

  public final static class Builder {
    private final Navigator navigator;
    private final Method method;
    private final Annotation[] methodAnnotations;
    private final Type[] parameterTypes;
    private final Annotation[][] parameterAnnotationsArray;
    private ParameterHandler<?>[] parameterHandlers;
    private String path;
    private boolean forResult;
    private boolean isService;
    private boolean receiverFound;
    private boolean requestCodeFound;
    private int pathIndex = -1;

    public Builder(Navigator navigator, Method method) {
      this.navigator = navigator;
      this.method = method;
      this.methodAnnotations = method.getAnnotations();
      this.parameterTypes = method.getGenericParameterTypes();
      this.parameterAnnotationsArray = method.getParameterAnnotations();
    }

    public RouterPath build() {
      for (Annotation annotation : methodAnnotations) {
        parseMethodAnnotation(annotation);
      }

      int parameterCount = parameterAnnotationsArray.length;
      parameterHandlers = new ParameterHandler<?>[parameterCount];
      for (int p = 0; p < parameterCount; p++) {
        Type parameterType = parameterTypes[p];
        if (Utils.hasUnresolvableType(parameterType)) {
          throw parameterError(p, "Parameter type must not include a type variable or wildcard: %s",
              parameterType);
        }

        Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
        if (parameterAnnotations == null) {
          throw parameterError(p, "No  annotation found.");
        }

        parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);

        if (forResult && (!receiverFound || !requestCodeFound)) {
          throw new IncompleteParameterException(
              "ForResult method must contains @ReceiverActivity and  @RequestCodeParam parameter!");
        }
      }
      return new RouterPath(this);
    }

    private ParameterHandler<?> parseParameter(int p, Type parameterType,
        Annotation[] annotations) {
      ParameterHandler<?> result = null;
      for (Annotation annotation : annotations) {
        ParameterHandler<?> annotationAction = parseParameterAnnotation(
            p, parameterType, annotations, annotation);

        if (annotationAction == null) {
          continue;
        }

        if (result != null) {
          throw parameterError(p, "Multiple Retrofit annotations found, only one allowed.");
        }

        result = annotationAction;
      }

      if (result == null) {
        throw parameterError(p, "No Retrofit annotation found.");
      }

      return result;
    }

    private ParameterHandler<?> parseParameterAnnotation(int p, Type type,
        Annotation[] annotations,
        Annotation annotation) {
      if (annotation instanceof StringParam) {
        StringParam intParam = (StringParam) annotation;
        String name = intParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          return new ParameterHandler.StringParam<>(name).iterable();
        } else if (rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          return new ParameterHandler.StringParam<>(name);
        }
      } else if (annotation instanceof IntParam) {
        IntParam intParam = (IntParam) annotation;
        String name = intParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          return new ParameterHandler.IntParam<>(name).iterable();
        } else if (rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          return new ParameterHandler.IntParam<>(name);
        }
      } else if (annotation instanceof BooleanParam) {
        BooleanParam booleanParam = (BooleanParam) annotation;
        String name = booleanParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with boolean is not supported!");
        } else {
          return new ParameterHandler.BooleanParam<>(name);
        }
      } else if (annotation instanceof FloatParam) {
        FloatParam floatParam = (FloatParam) annotation;
        String name = floatParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with float is not supported!");
        } else if (rawParameterType.isArray()) {
          return new ParameterHandler.FloatParam<>(name).array();
        } else {
          return new ParameterHandler.FloatParam<>(name);
        }
      } else if (annotation instanceof FlagParam) {
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          return new ParameterHandler.FlagParam<>().iterable();
        } else if (rawParameterType.isArray()) {
          return new ParameterHandler.FlagParam<>().array();
        } else {
          return new ParameterHandler.FlagParam<>();
        }
      } else if (annotation instanceof BundleParam) {
        BundleParam bundleParam = (BundleParam) annotation;
        String name = bundleParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with bundle is not supported!");
        } else {
          return new ParameterHandler.BundleParam<>(name);
        }
      } else if (annotation instanceof ParcelableParam) {
        ParcelableParam parcelableParam = (ParcelableParam) annotation;
        String name = parcelableParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          return new ParameterHandler.ParcelableParam<>(name).iterable();
        } else if (rawParameterType.isArray()) {
          return new ParameterHandler.ParcelableParam<>(name).array();
        } else {
          return new ParameterHandler.ParcelableParam<>(name);
        }
      } else if (annotation instanceof SerializableParam) {
        SerializableParam serializableParam = (SerializableParam) annotation;
        String name = serializableParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with Serializable is not supported!");
        } else {
          return new ParameterHandler.SerializableParam<>(name);
        }
      } else if (annotation instanceof ObjectParam) {
        ObjectParam objectParam = (ObjectParam) annotation;
        String name = objectParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with object is not supported!");
        } else {
          return new ParameterHandler.ObjectParam<>(name);
        }
      } else if (annotation instanceof PathParam) {
        this.pathIndex = p;
        return ParameterHandler.EMPTY;
      } else if (annotation instanceof CallBack) {
        return new ParameterHandler.CallBack<>();
      } else if (annotation instanceof ContextParam) {
        return new ParameterHandler.ContextParam<>();
      } else if (annotation instanceof ReceiverActivity) {
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          receiverFound = true;
          return new ParameterHandler.ReceiverActivity<>();
        }
      } else if (annotation instanceof RequestCodeParam) {
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          requestCodeFound = true;
          return new ParameterHandler.RequestCodeParam<>();
        }
      } else if (annotation instanceof ByteParam) {
        ByteParam byteParam = (ByteParam) annotation;
        String name = byteParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with byte is not supported!");
        } else if (rawParameterType.isArray()) {
          return new ParameterHandler.ByteParam<>(name).array();
        } else {
          return new ParameterHandler.ByteParam<>(name);
        }
      } else if (annotation instanceof CharParam) {
        CharParam charParam = (CharParam) annotation;
        String name = charParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with char is not supported!");
        } else if (rawParameterType.isArray()) {
          return new ParameterHandler.CharParam<>(name).array();
        } else {
          return new ParameterHandler.CharParam<>(name);
        }
      } else if (annotation instanceof CharSequenceParam) {
        CharSequenceParam charParam = (CharSequenceParam) annotation;
        String name = charParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType)) {
          return new ParameterHandler.CharSequenceParam<>(name).iterable();
        } else if (rawParameterType.isArray()) {
          return new ParameterHandler.CharSequenceParam<>(name).array();
        } else {
          return new ParameterHandler.CharSequenceParam<>(name);
        }
      } else if (annotation instanceof DoubleParam) {
        DoubleParam doubleParam = (DoubleParam) annotation;
        String name = doubleParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with boolean is not supported!");
        } else {
          return new ParameterHandler.DoubleParam<>(name);
        }
      } else if (annotation instanceof LongParam) {
        LongParam longParam = (LongParam) annotation;
        String name = longParam.value();
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with long is not supported!");
        } else {
          return new ParameterHandler.LongParam<>(name);
        }
      } else if (annotation instanceof EnterAnimParam) {
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          return new ParameterHandler.EnterAnim<>();
        }
      } else if (annotation instanceof ExitAnimParam) {
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          return new ParameterHandler.ExitAnim<>();
        }
      } else if (annotation instanceof AnimParam) {
        Class<?> rawParameterType = Utils.getRawType(type);
        if (Iterable.class.isAssignableFrom(rawParameterType) || rawParameterType.isArray()) {
          throw parameterError(p, rawParameterType.getSimpleName()
              + " parameterized with int is not supported!");
        } else {
          return new ParameterHandler.AnimParam<>();
        }
      }
      return ParameterHandler.EMPTY;
    }

    private void parseMethodAnnotation(Annotation annotation) {
      if (annotation instanceof OnlyJump) {
        parsePagePath(((OnlyJump) annotation).value(), false);
      } else if (annotation instanceof ForResult) {
        parsePagePath(((ForResult) annotation).value(), true);
      } else if (annotation instanceof Service) {
        parseServicePath(((Service) annotation).value());
      }
    }

    private void parseServicePath(String path) {
      this.path = path;
      isService = true;
    }

    private void parsePagePath(String path, boolean forResult) {
      this.path = path;
      this.forResult = forResult;
    }

    private RuntimeException parameterError(int p, String message, Object... args) {
      return methodError(message + " (parameter #" + (p + 1) + ")", args);
    }

    private RuntimeException methodError(String message, Object... args) {
      return methodError(null, message, args);
    }

    private RuntimeException methodError(Throwable cause, String message, Object... args) {
      message = String.format(message, args);
      return new IllegalArgumentException(message
          + "\n    for method "
          + method.getDeclaringClass().getSimpleName()
          + "."
          + method.getName(), cause);
    }
  }
}
