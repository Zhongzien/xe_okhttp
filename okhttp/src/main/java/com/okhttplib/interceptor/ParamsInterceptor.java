package com.okhttplib.interceptor;

import java.util.HashMap;

/**
 * 参数链接器
 * 对进行网络访问前，需要提交的参数进行拦截处理，例如：数据加密
 * 注意：如果设置了 ParamsInterceptor , 处理后返回的 Map 将会覆盖原来的 Map
 */

public interface ParamsInterceptor {

    /**
     * 参数拦截处理的方法
     *
     * <p>
     *      HashMap<String, String> params = info.getParams();
     *      if (paramsInterceptor != null) {
     *          params = paramsInterceptor.intercept(params == null ? null : (HashMap<String, String>) params.clone());
     *      }
     * </p>
     *
     * @param params 持有一个原map参数的副本
     * @return 返回一个经过加工处理的的 map
     */
    HashMap<String, String> intercept(HashMap<String, String> params);

}
