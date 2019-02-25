# BaseProject
[ ![Download](https://api.bintray.com/packages/zhaoyingtao/maven/common_library/images/download.svg) ](https://bintray.com/zhaoyingtao/maven/common_library/_latestVersion)



一、在项目中直接引入

将下面的 x.y.z 更改为上面显示的版本号   
`
compile 'com.bintray.library:common_library:x.y.z'
`

二、代码中使用
在项目的 Application的oncreate 方法中初始化：  
```     
      //今日头条适配配置
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.NONE);
      //初始化ARouter
      ARouter.init(this);
      //common依赖库的相关初始化
      CommonLibConstant.init()
                .setAppContext(this)
                .setIsDebug(AppConstant.is_debug)
                .setNoNetWorkRemind("无网络")
                .setSharedPreferencesName("base_db")
                .setCrashSavePath(AppConstant.LOCAL_PATH)
                .setExternalNetworkIP();
        //网络请求初始化
        RetrofitClientUtil.initClient()
                .useCookiesInterceptor(new AddCookiesInterceptor(APPApplication.this))
                .setConnectionPoolNums(8)
                .setConnectionPoolKeepTime(15)
                .setRequestOutTime(20)
                .setRequestBaseUrl(AppConstant.base_url)//必须设置
                .build();//必须设置

  ```
  在AndroidManifest文件中添加：
  ``` 
  <meta-data
 
android:name="design_width_in_dp"
 
android:value="375" />
 
<meta-data
 
android:name="design_height_in_dp"
 
android:value="667" />
  ``` 
  
  其他功能说明参考：[博客地址](https://blog.csdn.net/qq_31796651/article/details/87918188) 
  
  建议自定义基础类继承依赖库中的类，方便你的后续公共处理。重要！重要！重要！
  
  感觉对你有用，请给个 star 支持下
