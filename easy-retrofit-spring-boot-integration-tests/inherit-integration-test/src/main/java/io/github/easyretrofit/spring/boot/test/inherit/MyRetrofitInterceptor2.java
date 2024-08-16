package io.github.easyretrofit.spring.boot.test.inherit;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:44 PM
 */
public class MyRetrofitInterceptor2 extends BaseInterceptor {

    public MyRetrofitInterceptor2(RetrofitResourceContext context) {
        super(context);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
