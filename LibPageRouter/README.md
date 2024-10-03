# 接入指南
## 引入库
		
提供了两种方式引入库：maven和git子仓

* maven接入		

	1、 在工程根目录添加仓库：
		
		
		maven{
				url 'http://10.25.84.18:8089/nexus/content/repositories/Android-Snapshots'
    		}	

    		
	2、以 
	
		api "com.pasc.lib.router:LibPageRouter:1.5.0-SNAPSHOT"
		
	引入
	
* git接入
	
	1、添加代码仓库
		
			git remote add librouter git@git-ma.paic.com.cn:sz_smt/LibPageRouter.git
	2、拉取此仓库的master分支到工程
	
			git subtree add  -P LibPageRouter librouter master
	3、在settings.gradle中添加module声明

	4、同步

	5、添加module的依赖。

## gradle配置

 ```
defaultConfig {
  ...
    /**
     * ARouter 路由配置
     */
    javaCompileOptions {
      annotationProcessorOptions {
        arguments = [AROUTER_MODULE_NAME: project.getName(), AROUTER_GENERATE_DOC: "enable"]
      }
    }
  }



  dependencies {
  ...
    annotationProcessor "com.alibaba:arouter-compiler:1.2.0"
  ...
  }

 ```
## 使用

### 基本使用
#### 跳转
1. 用@Route注解目标页面。
2. 定义接口文件，在方法上注解配置路由参数，如目标路径，携带的参数，flag等。e.g.：
	```
		 @OnlyJump(RouterTable.User.PATH_LOGIN_ACTIVITY)
  void jumpLoginAcitivty(@BooleanParam(RouterTable.User.KEY_NEED_LOGIN) boolean needLogin);
	```
3. 生成动态代理对象，发起路由。	
```
	RouterGraph.createGraph(UserRouterApi.class).jumpLoginAcitivty(needLogin);
```
####ForResult跳转
ForResult与普通跳转类似，不过要将OnlyJump改为ForResult,并且必须传ReceiverActivity和requestCode.e.g.:
```
 @ForResult(RouterTable.User.PATH_LOGIN_ACTIVITY)
  void jumpLoginAcitivty(
                            @BooleanParam(RouterTable.User.KEY_NEED_LOGIN) boolean needLogin,
                            @ReceiverActivity Activity receiver,
                            @RequestCodeParam int requestCode
  						);
```
	
	
#### 服务
1. 定义服务

```
 @Service(RouterTable.User.PATH_SERVICE_LOGIN)
  ILoginService getILoginService();
```
	
2. 获取服务

```
 public static ILoginService getILoginService() {

    return RouterGraph.createGraph(UserRouterApi.class).getILoginService();
  }
```  
	
#### 切面与拦截器
1. 定义拦截器，声明合适的优先级

```
    @Interceptor(priority = RouterTable.Priority.FUTURE_A)
    public class InitHook implements IInterceptor {
```
2. 复写方法，实现切面和初始化,具体使用参见demo.

	
	
	


	 		 