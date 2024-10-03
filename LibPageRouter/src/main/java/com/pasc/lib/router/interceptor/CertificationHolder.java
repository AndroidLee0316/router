package com.pasc.lib.router.interceptor;

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
class CertificationHolder extends BaseHolder {

    private CertificationHolder() {

    }

    private final static class SingleHolder {
        private static final CertificationHolder INTERCEPTOR_HOLDER = new CertificationHolder ();
    }

    public static CertificationHolder instance() {
        return SingleHolder.INTERCEPTOR_HOLDER;
    }

}
