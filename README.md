# xe_okhttp
first upload to git

##V1.0
只提供post和get的异步请求。

允许用户自定义Configuration，如不定义就使用默认的Configuration。

可以自定义消息拦截器 MsgInterceptor ,对返回信息进行处理，并通过Configuration注册访问框架。

Configuration的配置方式
    
    如果没有特殊的需求 Configuration 可以不去配置，框架会提供默认配置。
    如果需要配置可以按下面这种方式进行配置：
    
    方式一：（实例化一个新的 Configuration 重新配置）
    Configuration config = new Configuration.Builder().
                    addParamsInterceptor(new MyInterceptor()).
                    build();
    OkHttpInvoker.config(config);
    
    方式二：（获取默认的 Configuration 根据需要覆盖参数）
    OkHttpInvoker.config(
                    Configuration.getConfigBulider().
                    addParamsInterceptor(new MyInterceptor()).
                    build());
    
异步请求

        方式一：
         HashMap<String, String> map = new HashMap<>();
         map.put("username", "iyihua");
         map.put("password", "123456");
         OkHttpInvoker.getDefaultBuilder().setUrl("http://192.168.1.128:8091/login").
         addParams(map).build().doPostAsync(new OnResultCallBack() {
             @Override
             public void onResponse(HttpInfo info) {
                 if (info.isSuccess()) {
                     Log.i(TAG, "doPostAsync:" + info.getResultBody());
                 } else {
                     Log.i(TAG, "doPostAsync:" + info.getMsg());
                 }
             }
         });
        
        方式二：
        OkHttpInvoker.getDefaultBuilder().setUrl("http://192.168.1.128:8091/login").
                        addParam("username", "iyihua").
                        addParam("password", "123456").
                        build().doPostAsync(new OnResultCallBack() {
                    @Override
                    public void onResponse(HttpInfo info) {
                        if (info.isSuccess()) {
                            Log.i(TAG, "doGetAsync:" + info.getResultBody());
                        } else {
                            Log.i(TAG, "doGetAsync:" + info.getMsg());
                        }
                    }
                });
                
        上面例子是均为POST请求，若需要GET请求只需要在build以后调用doGetAsync请求机即可。
        
##v1.1 
新增同步网络请求

新增参数拦截器，应对在提交数据之前进行数据加密、增加默认参数等情况,并通过Configuration注册访问框架。

对OnResultCallBack进行了修改，上一个有OnSuccess 和 onFailure 两个函数，现修个为OnResponse

同步请求

        同步请求同意提供POST和GET两种请求方式（doPostSync和doGetSync），的参数添加方式和异步的参数添加方式一样。
        例子如下：
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpInvoker.getDefaultBuilder().setUrl("http://192.168.1.128:8091/login").
                        addParam("username", "iyihua").
                        addParam("password", "123456").build().
                        doPostSync(new OnResultCallBack() {
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

##v2.0
新增文件上传，支持多格式文件上传

上传方式提供同步/异步两种，同时均支持单文件/多文件上传

可以通过实现 MediaTypeInterceptor 接口，自定上传文件的格式（系统默认设有默认格式拦截器）,最后通过Configuration注册访问框架。

单文件上传：

        同步上传：
        OkHttpInvoker.getDefaultBuilder().
                    setUrl(url).addUploadFile("file", filePathOne).
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
            
        同步上传：
        new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpInvoker.getDefaultBuilder().
                            addUploadFile("file2", filePathTwo).
                            setUrl(url).build().
                            doUploadSync(new OnResultCallBack() {
                        @Override
                        public void onResponse(HttpInfo info) {
                            if (info.isSuccess()) {
                                Log.i(TAG, "success:" + info.getResultBody());
                            } else {
                                Log.i(TAG, "failure:" + info.getResultBody());
                            }
                        }
                    });
                }
            }).start();
    
多文件上传
    
    方式一：
    OkHttpInvoker.getDefaultBuilder().
                setUrl(url).addUploadFile("file1", filePathOne).
                addUploadFile("file2", filePathTwo).
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
        
        方式二：
        List<UploadFileInfo> infoList = new ArrayList<>();
        infoList.add(new UploadFileInfo("file1",filePathOne));
        infoList.add(new UploadFileInfo("file2",filePathTwo));
        OkHttpInvoker.getDefaultBuilder().
                        setUrl(url).addUploadFiles(infoList).
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
    以上是多文件异步上传，多文件同步上传同多文件异步上传，在调用方式上没有区别，只是注意要在外层包裹着子线程
    