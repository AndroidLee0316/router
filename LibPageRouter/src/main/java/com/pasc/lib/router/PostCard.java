package com.pasc.lib.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PostCard {

  private final Postcard rawPostcard;
  private final String path;
  private final boolean isService;
  private final boolean forResult;
  private final int requestCode;
  private final Activity receiver;
  private final int enterAnim;
  private final int exitAnim;
  private final Context context;
  private final NavigationCallback callback;

  PostCard(PostCardBuilder builder) {
    this.rawPostcard = builder.postcard;
    this.path = builder.path;
    this.isService = builder.isService;
    this.forResult = builder.forResult;
    this.requestCode = builder.requestCode;
    this.receiver = builder.receiver;
    this.enterAnim = builder.enterAnim;
    this.exitAnim = builder.exitAnim;
    this.context = builder.context;
    this.callback = builder.callback;
  }

  public Object navigation(Activity activity) {
    if (enterAnim > 0 && exitAnim > 0) {
      rawPostcard.withTransition(enterAnim, exitAnim);
    }
    if (forResult) {
      if (callback != null) {
        rawPostcard.navigation(receiver, requestCode, callback);
      } else {
        rawPostcard.navigation(receiver, requestCode);
      }
      return null;
    } else {
      if (callback != null && context != null) {
        return rawPostcard.navigation(context, callback);
      } else if (context != null) {
        return rawPostcard.navigation(context);
      } else if (activity != null) {
        return rawPostcard.navigation(activity);
      }
      return rawPostcard.navigation();
    }
  }

  static final class PostCardBuilder {

    private final String path;
    private final boolean isService;
    private final boolean forResult;
    private final Postcard postcard;
    private int requestCode;
    private int enterAnim;
    private int exitAnim;
    private Activity receiver;
    private Context context;
    private NavigationCallback callback;

    PostCardBuilder(String path, boolean isService, boolean forResult) {
      this.path = path;
      this.isService = isService;
      this.forResult = forResult;
      if (path.startsWith("/")) {
        postcard = ARouter.getInstance().build(path);
      } else {
        Uri parse = Uri.parse(path);
        postcard = ARouter.getInstance().build(parse);
      }
      LogisticsCenter.completion(postcard);
    }

    PostCard build() {
      return new PostCard(this);
    }

    void addBooleanParam(String key, boolean value) {
      postcard.withBoolean(key, value);
    }

    void addStringParam(String key, String value) {
      postcard.withString(key, value);
    }

    void addStringListParam(String key, List<String> value) {
      postcard.withStringArrayList(key, new ArrayList<>(value));
    }

    void addIntParam(String key, int value) {
      postcard.withInt(key, value);
    }

    void addIntListParam(String key, List<Integer> value) {
      postcard.withIntegerArrayList(key, new ArrayList<>(value));
    }

    void addFloatParam(String key, float value) {
      postcard.withFloat(key, value);
    }

    void addFloatArrayParam(String key, float[] value) {
      postcard.withFloatArray(key, value);
    }

    void addFlagParam(int value) {
      postcard.withFlags(value);
    }

    void addFlagArrayParam(int[] value) {
      for (int flag : value) {
        postcard.withFlags(flag);
      }
    }

    void addFlagListParam(List<Integer> value) {
      for (int flag : value) {
        postcard.withFlags(flag);
      }
    }

    void addBundleParam(String key, Bundle value) {
      postcard.withBundle(key, value);
    }

    void addParcelableParam(String key, Parcelable value) {
      postcard.withParcelable(key, value);
    }

    void addParcelableArrayParam(String key, Parcelable[] values) {
      postcard.withParcelableArray(key, values);
    }

    void addParcelableListParam(String key, List<Parcelable> values) {
      postcard.withParcelableArrayList(key, new ArrayList<>(values));
    }

    void addSerializableParam(String key, Serializable value) {
      postcard.withSerializable(key, value);
    }

    void addAnim(int enter, int exit) {
      postcard.withTransition(enter, exit);
    }

    void addObjectParam(String key, Object value) {
      postcard.withObject(key, value);
    }

    void addCharParam(String key, Character value) {
      postcard.withChar(key, value);
    }

    void addCharArrayParam(String key, char[] values) {
      postcard.withCharArray(key, values);
    }

    void addCharSequenceParam(String key, CharSequence value) {
      postcard.withCharSequence(key, value);
    }

    void addCharSequenceArrayParam(String key, CharSequence[] values) {
      postcard.withCharSequenceArray(key, values);
    }

    void addCharSequenceListParam(String key, List<CharSequence> values) {
      postcard.withCharSequenceArrayList(key, new ArrayList<>(values));
    }

    void addDoubleParam(String key, double value) {
      postcard.withDouble(key, value);
    }

    void addLongParam(String key, long value) {
      postcard.withLong(key, value);
    }

    void addByteParam(String name, Byte value) {
      postcard.withByte(name, value);
    }

    void addByteArrayParam(String name, byte[] bytes) {
      postcard.withByteArray(name, bytes);
    }

    public void addReceiverActivity(Activity receiver) {
      this.receiver = receiver;
    }

    public void addEnterAnim(int enterAnim) {
      this.enterAnim = enterAnim;
    }

    public void addExitAnim(int exitAnim) {
      this.exitAnim = exitAnim;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addAnimCompat(ActivityOptionsCompat compat) {
      postcard.withOptionsCompat(compat);
    }

    public void addRequestCode(int requestCode) {
      this.requestCode = requestCode;
    }

    public void addContext(Context context) {
      this.context = context;
    }

    public void addCallBack(NavigationCallback callback) {
      this.callback = callback;
    }
  }
}
