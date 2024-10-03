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

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
@Interceptor(priority = 2)
public class CertificationInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        /***是否需要认证***/
        boolean needCertification = FlagUtil.flagIsEnable(postcard.getExtra(), BaseRouterTable.Flag.FLAG_NEED_CERTIFICATION);
        if (!needCertification && postcard != null) {
            Bundle bundle = postcard.getExtras();
            if (bundle != null) {
                Object needCertificationObj = bundle.get(BaseRouterTable.BundleKey.KEY_NEED_IDENTITY);
                if (needCertificationObj != null) {
                    if (needCertificationObj instanceof Boolean) {
                        needCertification = (boolean) needCertificationObj;
                    } else if (needCertificationObj instanceof String) {
                        if ("true".equals(((String) needCertificationObj).trim().toLowerCase())) {
                            needCertification = true;
                        } else {
                            needCertification = false;
                        }
                    }
                }

                if (!needCertification) {
                    // 新增一个实名认证的字段
                    Object needCertObj = bundle.get(BaseRouterTable.BundleKey.KEY_NEED_CERT);
                    if (needCertObj != null) {
                        if (needCertObj instanceof Boolean) {
                            needCertification = (boolean) needCertObj;
                        } else if (needCertObj instanceof String) {
                            if ("true".equals(((String) needCertObj).trim().toLowerCase())) {
                                needCertification = true;
                            } else {
                                needCertification = false;
                            }
                        }
                    }
                }
            }
        }
        if (needCertification) {
            /****是否已经认证****/
            boolean isCertification = RouterManager.instance().getApiGet().isCertification();
            if (isCertification) {
                callback.onContinue(postcard);
            } else {
                if (RouterManager.instance().getApiGet().enableInterceptorNotifyCallBack())
                    notifyCallBack(false);
                RouterManager.instance().getApiGet().gotoCertification(postcard.getPath(), removeBundleInfo(postcard.getExtras()));
                CertificationHolder.instance().setPostcard(postcard);
                CertificationHolder.instance().setCallback(callback);

            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        reset();
    }

    static void reset() {
        CertificationHolder.instance().clear();
    }

    public static void notifyCallBack(boolean pass) {
        if (CertificationHolder.instance().getPostcard() != null && CertificationHolder.instance().getCallback() != null) {
            if (pass) {
                CertificationHolder.instance().getCallback().onContinue(CertificationHolder.instance().getPostcard());
            } else {
                CertificationHolder.instance().getCallback().onInterrupt(null);
            }
            reset();
        }
    }
}
