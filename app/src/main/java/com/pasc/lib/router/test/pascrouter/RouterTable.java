package com.pasc.lib.router.test.pascrouter;


public interface RouterTable {
    interface User{
         String USER_NEEDLOGIN_PATH = "/user/needlogin/main";
         String USER_NEEDLOGINCERTIFICATION_PATH = "/user/loginCertification/main";
         String USER_CERTIFICATION_PATH="/user/certification/main";
         String USER_LOGIN_PATH="/user/login/main";
    }

    interface Api{
        String SERVICE_API_PATH="/service/api/main";
    }

    interface Other{
        String OTHER_DEEPLINK_PATH="/other/deeplink/main";
    }

}
