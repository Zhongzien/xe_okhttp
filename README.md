# xe_okhttp
first upload to git
V1.0
只提供post和get的异步请求。
允许用户自定义Configuration，如不定义就使用默认的Configuration。
可以自定义消息拦截器 MsgInterceptor ,对返回信息进行处理，并通过Configuration注册访问框架。

v1.1 
新增同步网络请求
新增参数拦截器，应对在提交数据之前进行数据加密、增加默认参数等情况
对OnResultCallBack进行了修改，上一个有OnSuccess 和 onFailure 两个函数，现修个为OnResponse

2017/05/05
发现传递没有后缀名的文件出现报错
