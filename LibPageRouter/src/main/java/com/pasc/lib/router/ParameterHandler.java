package com.pasc.lib.router;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

abstract class ParameterHandler<T> {
  public static final ParameterHandler<?> EMPTY = new ParameterHandler<Object>() {
    @Override void apply(PostCard.PostCardBuilder builder, Object value) throws IOException {

    }
  };

  abstract void apply(PostCard.PostCardBuilder builder, T value) throws IOException;

  ParameterHandler<Iterable<T>> iterable() {
    return null;
  }

  static class BooleanParam<T> extends ParameterHandler<T> {

    private final String name;

    BooleanParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addBooleanParam(name, (Boolean) value);
    }
  }

  static class StringParam<T> extends ParameterHandler<T> {

    private final String name;

    StringParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addStringParam(name, value == null ? "" : String.valueOf(value));
    }

    @Override ParameterHandler<Iterable<T>> iterable() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          builder.addStringListParam(name, (List<String>) value);
        }
      };
    }
  }

  static class IntParam<T> extends ParameterHandler<T> {

    private final String name;

    IntParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      if (value instanceof Integer) {
        Integer integer = (Integer) value;
        builder.addIntParam(name, integer);
      } else {
        builder.addIntParam(name, (Integer) value);
      }
    }

    @Override ParameterHandler<Iterable<T>> iterable() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          ArrayList<Integer> integers = new ArrayList<>();
          for (T t : value) {
            integers.add((Integer) t);
          }
          builder.addIntListParam(name, integers);
        }
      };
    }
  }

  static class FloatParam<T> extends ParameterHandler<T> {

    private final String name;

    FloatParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      if (value instanceof Float) {
        Float temp = (Float) value;
        builder.addFloatParam(name, temp);
      } else {
        builder.addFloatParam(name, (Float) value);
      }
    }

    public ParameterHandler<?> array() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          if (value == null) return; // Skip null values.
          int length = Array.getLength(value);
          float[] floats = new float[length];
          for (int i = 0; i < length; i++) {
            floats[i] = (float) Array.get(value, i);
          }
          builder.addFloatArrayParam(name, floats);
        }
      };
    }
  }

  static class FlagParam<T> extends ParameterHandler<T> {

    FlagParam() {
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addFlagParam((Integer) value);
    }

    @Override ParameterHandler<Iterable<T>> iterable() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          ArrayList<Integer> flags = new ArrayList<>();
          for (T t : value) {
            flags.add((Integer) t);
          }
          builder.addFlagListParam(flags);
        }
      };
    }

    public ParameterHandler<?> array() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          if (value == null) return; // Skip null values.
          int length = Array.getLength(value);
          int[] flags = new int[length];
          for (int i = 0; i < length; i++) {
            flags[i] = (int) Array.get(value, i);
          }
          builder.addFlagArrayParam(flags);
        }
      };
    }
  }

  static class BundleParam<T> extends ParameterHandler<T> {

    private final String name;

    BundleParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addBundleParam(name, (Bundle) value);
    }
  }

  static class ParcelableParam<T> extends ParameterHandler<T> {

    private final String name;

    ParcelableParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addParcelableParam(name, (Parcelable) value);
    }

    @Override ParameterHandler<Iterable<T>> iterable() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          ArrayList<Parcelable> parcelables = new ArrayList<>();
          for (T t : value) {
            parcelables.add((Parcelable) t);
          }
          builder.addParcelableListParam(name, parcelables);
        }
      };
    }

    public ParameterHandler<?> array() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          if (value == null) return; // Skip null values.
          int length = Array.getLength(value);
          Parcelable[] parcelables = new Parcelable[length];
          for (int i = 0; i < length; i++) {
            parcelables[i] = (Parcelable) Array.get(value, i);
          }
          builder.addParcelableArrayParam(name, parcelables);
        }
      };
    }
  }

  static class SerializableParam<T> extends ParameterHandler<T> {

    private final String name;

    SerializableParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addSerializableParam(name, (Serializable) value);
    }
  }

  static class ObjectParam<T> extends ParameterHandler<T> {

    private final String name;

    ObjectParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addObjectParam(name, value);
    }
  }

  static class ByteParam<T> extends ParameterHandler<T> {

    private final String name;

    ByteParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addByteParam(name, (Byte) value);
    }

    public ParameterHandler<?> array() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          if (value == null) return; // Skip null values.
          int length = Array.getLength(value);
          byte[] bytes = new byte[length];
          for (int i = 0; i < length; i++) {
            bytes[i] = (byte) Array.get(value, i);
          }
          builder.addByteArrayParam(name, bytes);
        }
      };
    }
  }

  static class CharParam<T> extends ParameterHandler<T> {

    private final String name;

    CharParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addCharParam(name, (Character) value);
    }

    public ParameterHandler<?> array() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          if (value == null) return; // Skip null values.
          int length = Array.getLength(value);
          char[] chars = new char[length];
          for (int i = 0; i < length; i++) {
            chars[i] = (char) Array.get(value, i);
          }
          builder.addCharArrayParam(name, chars);
        }
      };
    }
  }

  static class CharSequenceParam<T> extends ParameterHandler<T> {

    private final String name;

    CharSequenceParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addCharSequenceParam(name, (CharSequence) value);
    }

    @Override ParameterHandler<Iterable<T>> iterable() {

      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          ArrayList<CharSequence> charSequences = new ArrayList<>();
          for (T item : value) {
            charSequences.add((CharSequence) item);
          }
          builder.addCharSequenceListParam(name, charSequences);
        }
      };
    }

    public ParameterHandler<?> array() {
      return new ParameterHandler<Iterable<T>>() {
        @Override void apply(PostCard.PostCardBuilder builder, Iterable<T> value)
            throws IOException {
          if (value == null) return; // Skip null values.
          int length = Array.getLength(value);
          CharSequence[] chars = new CharSequence[length];
          for (int i = 0; i < length; i++) {
            chars[i] = (CharSequence) Array.get(value, i);
          }
          builder.addCharSequenceArrayParam(name, chars);
        }
      };
    }
  }

  static class DoubleParam<T> extends ParameterHandler<T> {

    private final String name;

    DoubleParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addDoubleParam(name, (Double) value);
    }
  }

  static class LongParam<T> extends ParameterHandler<T> {

    private final String name;

    LongParam(String name) {
      this.name = name;
    }

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addLongParam(name, (Long) value);
    }
  }

  static class ReceiverActivity<T> extends ParameterHandler<T> {

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addReceiverActivity((Activity) value);
    }
  }

  static class RequestCodeParam<T> extends ParameterHandler<T> {

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addRequestCode((Integer) value);
    }
  }

  static class ContextParam<T> extends ParameterHandler<T> {

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addContext((Context) value);
    }
  }

  static class CallBack<T> extends ParameterHandler<T> {

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addCallBack((NavigationCallback) value);
    }
  }

  static class EnterAnim<T> extends ParameterHandler<T> {

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addEnterAnim((Integer) value);
    }
  }

  static class ExitAnim<T> extends ParameterHandler<T> {

    @Override void apply(PostCard.PostCardBuilder builder, T value) throws IOException {
      builder.addExitAnim((Integer) value);
    }
  }

  static class AnimParam<T> extends ParameterHandler<T> {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) @Override void apply(
        PostCard.PostCardBuilder builder, Object value) throws IOException {
      builder.addAnimCompat((ActivityOptionsCompat) value);
    }
  }
}
