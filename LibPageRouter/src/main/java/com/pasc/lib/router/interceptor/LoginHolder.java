package com.pasc.lib.router.interceptor;

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
 class LoginHolder extends BaseHolder{

    private LoginHolder(){

    }
    private final static class SingleHolder{
       private static final LoginHolder INTERCEPTOR_HOLDER=new LoginHolder ();
    }

    public static LoginHolder instance(){
        return SingleHolder.INTERCEPTOR_HOLDER;
    }

}
