https://www.jianshu.com/p/186fa07fc48a
https://www.jianshu.com/p/1b1d77f58e84



一、基础
1、概念
组件化
车插件化

2、概念


二、实践
1、新建 library
2、
gradle.properties 添加标识变量
IsRunAlone=false

3、新建 不同的 module ,尽量避免资源冲突，不同的 module 资源（所有资源，res 下的） 可以 通过以标识 Module 名的名字开头，同时
build.gradle 添加 resourcePrefix 约束 ，as 中 refactor rename 。
如果保证资源不冲突，不改也行。但是一定注意 类名，布局等名称的冲突问题。



4、对于 Module,通过 IsRunAlone 来使用不同的插件
```
println IsRunAlone.toBoolean()
if (IsRunAlone.toBoolean()) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}
```

同时 ApplicationId 也要根据情况判断是否指定：
```
  if (IsRunAlone.toBoolean()) {
            applicationId "demo.ss.com.myapplication"
        }
```

因为 module 独立运行时和作为 module 运行 manifest 会不同，如果不指定的话，作为 module 运行还指定原来的 manifest 桌面会有出现 module 快捷方式。因此
在不同的module 下，在 src/main 下新建一个文件夹，复制 manifest 并修改，接着在 module build.gradle 根据不同的状态使用不同的文件：
```
    sourceSets {
        main {
            if (!IsRunAlone.toBoolean()) {
                manifest.srcFile 'src/main/moduleReader/AndroidManifest.xml'
            }
        }
    }
```

在 Module 中 依赖组件 library,为了使主 module 能够间接使用到底层library，应该使用 api:
```
 api project(':baselibrary')
```

5、在 主  module 中
根据条件加载不同的 Module 或者组件
```
    if (!IsRunAlone.toBoolean()) {
        implementation project(':read')
        implementation project(':write')
    } else {
        implementation project(':baselibrary')
    }
```


此时，如果配置 IsRunAlone 为 false，那么在 android studio 中，只有主 module ：app 是可以运行的
read 与 write 是不可以的。并且 主 module 可以使用底层依赖库 baselibrary

当修改 IsRunAlone 为 true, module read 与 module write 都可以独立运行。



三、组件跳转
使用 ARouter
1、在 baselibrary 中，添加依赖
```
android {
    compileSdkVersion 27
    defaultConfig {
    ...


        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ moduleName : project.getName() ]
            }
        }


    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:27.1.1'
    ...

    api 'com.alibaba:arouter-api:1.2.1.1'
    annotationProcessor 'com.alibaba:arouter-compiler:1.1.2.1'

}

```
2、在需要用到跳转的 Module 的 build.gradle 添加如下依赖：

defaultConfig:
```

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ moduleName : project.getName() ]
            }
        }
```
```
  annotationProcessor 'com.alibaba:arouter-compiler:1.1.2.1'
```
3、
为了初始化 ARouter 并且在 module 都可以使用，这里在 library 中初始化
baselibrary 中 新建 BaseApplication 并 初始化 ARouter
```
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(线上版本需要关闭,否则有安全风险)
        ARouter.init( this ); // 初始化
    }
}

```

各个 Module 中的 application 中都指定集成上面的 application

别忘了 manifest 设置,单独运行指定的 Manifest 中修改，就行
```
        android:name=".app.MyApplication"

```

4、在 Module read 中，新建一个测试类，添加 Route 注解
```
@Route(path = "/read/ActivityRead")
public class ActivityRead extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);
    }
}

```

5、主 Module 向 Read Module 跳转
```
    public void Read(View view) {
        //发起路由跳转
        ARouter.getInstance().build("/read/ActivityRead").navigation();
    }
```

5、同样的 在 module write 新建测试类，测试 read 到 write 跳转
```


@Route(path = "/write/ActivityWrite")
public class ActivityWrite extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wriet_test);
    }
}

```

Read To Wirte
```
   public void ReadToWrite(){
        ARouter.getInstance().build("/write/ActivityWrite").navigation();
    }
```


6、单独运行 moduel 当组件跳转时，找不到会有Toast 提示
也可以自己设置路由监听
```

        ARouter.getInstance().build("/write/ActivityWrite").navigation(this, new NavCallback() {

            @Override
            public void onFound(Postcard postcard) {
                Log.i(TAG, "onFound: ");
            }

            @Override
            public void onLost(Postcard postcard) {
                Log.i(TAG, "onLost: ");
            }

            @Override
            public void onArrival(Postcard postcard) {
                Log.i(TAG, "onArrival: ");
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                Log.i(TAG, "onInterrupt: ");
            }
        });

```