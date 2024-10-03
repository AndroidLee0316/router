package com.pasc.lib.router.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.pasc.lib.router.RouterManager;
import com.pasc.lib.router.aspect.FlagUtil;

import static com.pasc.lib.router.Utils.removeBundleInfo;

@Interceptor(priority = 1)
public class LoginInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        RouterManager.instance ().getApiGet ().beforeInterceptor (postcard.getPath (), postcard.getExtras ());
        /***是否需要登陆***/
        boolean needLogin = FlagUtil.flagIsEnable (postcard.getExtra (), BaseRouterTable.Flag.FLAG_NEED_LOGIN);
        if (!needLogin && postcard != null) {
            Bundle bundle = postcard.getExtras ();
            if (bundle != null) {
                Object needLoginObj = bundle.get (BaseRouterTable.BundleKey.KEY_NEED_LOGIN);
                if (needLoginObj != null) {
                    if (needLoginObj instanceof Boolean) {
                        needLogin = (boolean) needLoginObj;
                    } else if (needLoginObj instanceof String) {
                        if ("true".equals (((String) needLoginObj).trim ().toLowerCase ())) {
                            needLogin = true;
                        }
                    }
                }
            }
        }

        if (needLogin) {
            /***是否已经登陆***/
            boolean isLogin = RouterManager.instance ().getApiGet ().isLogin ();
            if (isLogin) {
                callback.onContinue (postcard);
            } else {
                if (RouterManager.instance ().getApiGet ().enableInterceptorNotifyCallBack ())
                    notifyCallBack (false);
                RouterManager.instance ().getApiGet ().gotoLogin (postcard.getPath (), removeBundleInfo(postcard.getExtras ()));
                LoginHolder.instance ().setPostcard (postcard);
                LoginHolder.instance ().setCallback (callback);

            }
        } else {
            callback.onContinue (postcard);
        }
    }

    @Override
    public void init(Context context) {
        reset ();
    }

    static void reset() {
        LoginHolder.instance ().clear ();
    }

    public static void notifyCallBack(boolean pass) {
        if (LoginHolder.instance ().getPostcard () != null && LoginHolder.instance ().getCallback () != null) {
            if (pass) {
                LoginHolder.instance ().getCallback ().onContinue (LoginHolder.instance ().getPostcard ());
            } else {
                LoginHolder.instance ().getCallback ().onInterrupt (null);
            }
            reset ();
        }
    }
}
