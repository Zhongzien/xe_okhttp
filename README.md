# xe_okhttp使用手册

由于我是用公司的接口来测试的这里就不提供接口和数据

##1.配置

<1>.gradle 配置

在 project 最外层的 gradle 中加入注释 ⑴ 的代码 

    // Top-level build file where you can add configuration options common to all sub-projects/modules.
    
    buildscript {
        repositories {
            jcenter()
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:2.3.2'
            classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
            // NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
        }
    }
    
    allprojects {
        repositories {
            jcenter()
            maven { url 'https://jitpack.io' } // ⑴ 加入这行代码
        }
    }
    
    task clean(type: Delete) {
        delete rootProject.buildDir
    }

然后引入下面这个包：

    compile 'com.github.Zhongzien:xe_okhttp:v2.1.1'

<2>.Configuration 配置

    Configuration.getConfigBuilder(this).addParamsInterceptor(new MyInterceptor()).bindConfig();

说明：

Configuration 的配置建议在 Application 中进行配置，且整个程序有且只有一个 Configuration ；在实际应用中如果没有特殊需求可以不配 Configuration ，系统会默认配置。

##2.网络请求：

<1>.异步post请求：
    
            OkHttpInvoker.getBuilder().setUrl(url)
                    .setCallTag(tag) // ①
                    .addParam(key, value)
                    .addParam(key, value)
                    .build().doPostAsync(new OnResultCallBack() {
                @Override
                public void onResponse(HttpInfo info) {
                    if (info.isSuccess()) {
                        Log.i(TAG, "doPostAsync:" + info.getResultBody());
                    } else {
                        Log.i(TAG, "doPostAsync:" + info.getMsg());
                    }
                }
            });
            
<2>.同步post请求：

            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpInvoker.getBuilder().setUrl(url)
                            .setCallTag(tag) // ①
                            .addParam(key, value)
                            .addParam(key, value)
                            .build().doPostSync(new OnResultCallBack() {
                        @Override
                        public void onResponse(HttpInfo info) {
                            if (info.isSuccess()) {
                                Log.i(TAG, "doPostSync:" + info.getResultBody());
                            } else {
                                Log.i(TAG, "doPostSync:" + info.getMsg());
                            }
                        }
                    });
                }
            }).start();
            
<3>.异步get请求：
    
            OkHttpInvoker.getBuilder().setUrl(url)
                    .setCallTag(tag) // ①
                    .build().doGetAsync(new OnResultCallBack() {
                @Override
                public void onResponse(HttpInfo info) {
                    if (info.isSuccess()) {
                        Log.i(TAG, "doGetAsync:" + info.getResultBody());
                    } else {
                        Log.i(TAG, "doGetAsync:" + info.getMsg());
                    }
                }
            });

<4>.同步get请求:

            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpInvoker.getBuilder().setUrl(url)
                            .setCallTag(tag) // ①
                            .build().
                            doGetSync(new OnResultCallBack() {
                                @Override
                                public void onResponse(HttpInfo info) {
                                    if (info.isSuccess()) {
                                        Log.i(TAG, "doGetSync:" + info.getResultBody());
                                    } else {
                                        Log.i(TAG, "doGetAsync:" + info.getMsg());
                                    }
                                }
                            });
                }
            }).start();
            
说明：

get 请求同样可以使用 post 请求的传参方式，框架会自动把 URL 拼接完整。

上面注释 ① 处代码如果不需要对网络访问进行分组管理可以不调用，不会对程序正常运行造成影响。若调用了该方法可以把分组管理交由程序去完成，也可以自行对分组进行管理，管理所需方法见 API 。

##3.文件上传

            OkHttpInvoker.getBuilder().
                    setUrl(url).addUploadFile(key, filePath).
                    build().doUploadAsync(new OnResultCallBack() {
                @Override
                public void onResponse(HttpInfo info) {
                    if (info.isSuccess()) {
                        Log.i(TAG, "success:" + info.getResultBody());
                    } else {
                        Log.i(TAG, "failure:" + info.getResultBody());
                    }
                }
            });
     
说明：

文件上传支持单文件上传、多文件上传、批量上传，上传的同时可以带除文件信息以外信息一起上传。

#4.文件下载

            OkHttpInvoker.getBuilder().
                    addDownloadFile(url, saveDir, saveFileName, new OnProgressCallBack() {
                        @Override
                        public void onProgress(long percent, long curLength, long totalLength) {
                            Log.i(TAG, "curLength:" + curLength);
                        }
    
                        @Override
                        public void onResponse(HttpInfo info) {
                            Log.i(TAG, "onResponse:" + info.getResultBody());
                        }
                    }).build().doDownloadAsync();
                    
说明：

文件下载支持批量加载，支持断点下载。同一个文件可以重复下载，程序内置重名防止文件相互覆盖。

##5.API

>class com.okhttplib.OkHttpInter

>>异步 post 请求  
@param callBack 网络访问回调接口  
void doPostAsync(OnResultCallBack callBack) 

>>异步 get 请求  
@param callBack 网络访问回调接口  
**void doGetAsync(OnResultCallBack callBack)**

>>同步 post 请求  
@param callBack 网络访问回调接口  
**void doPostSync(OnResultCallBack callBack)**

>>同步 get 请求  
@param callBack 网络访问回调接口  
**void doGetSync(OnResultCallBack callBack)**

>>文件上传  
@param callBack 网络访问回调接口  
**void doUploadAsync(OnResultCallBack callBack)**

>>文件下载  
**void doDownloadAsync()**

>>用于网络访问分组管理，向分组集合中添加新的 call  
@param key 分组标识 不能为 null  
@param call 需要管理的请求  
**static void putCall(String key, Call call)**

>>用于网络访问分组管理，根据 key 删除 key 标识的分组集合元素  
@param key 分组标识 不能为 null  
**static void removeCallOrSet(String key)**

>>用于网络访问分组管理，根据 key 和 call 删除分组集合元素，如果 call 为 null 删除 key 标识的分组集合元素  
@param key 分组标识 不能为 null  
@param call 需要管理的请求  
**static void removeCallOrSer(String key, Call call)**


>>用于网络访问分组管理，根据 key 和 call 删除分组集合元素，如果 call 为 null 操作无效  
@param key 分组标识 不能为 null  
@param call 需要管理的请求  
**static void removeCall(String key, Call call)**


>>根据 key 标识停止下载  
@param key 不能为 null  
**static void stop(String key)**

>>根据 key 标识暂停下载  
@param key 不能为 null  
**static void pause(String key)**

