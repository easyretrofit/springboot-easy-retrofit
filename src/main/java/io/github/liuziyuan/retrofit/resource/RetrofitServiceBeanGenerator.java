package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.annotation.*;
import io.github.liuziyuan.retrofit.exception.RetrofitStarterException;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * generate RetrofitServiceBean object
 *
 * @author liuziyuan
 */
public class RetrofitServiceBeanGenerator implements Generator<RetrofitServiceBean> {
    private final Class<?> clazz;
    private final Environment environment;

    public RetrofitServiceBeanGenerator(Class<?> clazz, Environment environment) {
        this.clazz = clazz;
        this.environment = environment;
    }

    @Override
    public RetrofitServiceBean generate() {
        Class<?> retrofitBuilderClazz = getParentRetrofitBuilderClazz();
        RetrofitServiceBean retrofitServiceBean = new RetrofitServiceBean();
        retrofitServiceBean.setSelfClazz(clazz);
        retrofitServiceBean.setParentClazz(retrofitBuilderClazz);
        RetrofitBuilder retrofitBuilderAnnotation = retrofitBuilderClazz.getDeclaredAnnotation(RetrofitBuilder.class);
        retrofitServiceBean.setRetrofitBuilder(retrofitBuilderAnnotation);
        Set<RetrofitInterceptor> interceptors = this.getInterceptors(retrofitBuilderClazz);
        Set<RetrofitInterceptor> myInterceptors = this.getInterceptors(clazz);
        retrofitServiceBean.setMyInterceptors(myInterceptors);
        retrofitServiceBean.setInterceptors(interceptors);
        final RetrofitUrlPrefix retrofitUrlPrefix = clazz.getDeclaredAnnotation(RetrofitUrlPrefix.class);
        final RetrofitDynamicBaseUrl retrofitDynamicBaseUrl = clazz.getDeclaredAnnotation(RetrofitDynamicBaseUrl.class);
        String retrofitDynamicBaseUrlValue = retrofitDynamicBaseUrl == null ? null : retrofitDynamicBaseUrl.value();
        RetrofitUrl url = new RetrofitUrl(retrofitBuilderAnnotation.baseUrl(),
                retrofitDynamicBaseUrlValue,
                retrofitUrlPrefix == null ? null : retrofitUrlPrefix.value(),
                environment);
        retrofitServiceBean.setRetrofitUrl(url);
        return retrofitServiceBean;

    }

    private Class<?> getParentRetrofitBuilderClazz() {
        final Class<?> parentClazzIncludeRetrofitBuilderAndBase = findParentClazzIncludeRetrofitBuilderAndBase(clazz);
//        if (parentClazzIncludeRetrofitBuilderAndBase.getDeclaredAnnotation(RetrofitBuilder.class) == null) {
//            throw new RetrofitStarterException("The baseApi of @RetrofitBase in the [" + clazz.getSimpleName() + "] Interface, does not define @RetrofitBuilder");
//        }
        return parentClazzIncludeRetrofitBuilderAndBase;
    }

    private Class<?> findParentClazzIncludeRetrofitBuilderAndBase(Class<?> clazz) {
        Class<?> retrofitBuilderClazz;
        if (clazz.getDeclaredAnnotation(RetrofitBase.class) != null) {
            retrofitBuilderClazz = findParentRetrofitBaseClazz(clazz);
        } else {
            retrofitBuilderClazz = findParentRetrofitBuilderClazz(clazz);
        }
        if (retrofitBuilderClazz.getDeclaredAnnotation(RetrofitBuilder.class) == null) {
            retrofitBuilderClazz = findParentClazzIncludeRetrofitBuilderAndBase(retrofitBuilderClazz);
        }
        return retrofitBuilderClazz;
    }

    private Class<?> findParentRetrofitBuilderClazz(Class<?> clazz) {
        RetrofitBuilder retrofitBuilder = clazz.getDeclaredAnnotation(RetrofitBuilder.class);
        Class<?> targetClazz = clazz;
        if (retrofitBuilder == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                targetClazz = findParentRetrofitBuilderClazz(interfaces[0]);
            }else {
                if (clazz.getDeclaredAnnotation(RetrofitBase.class) == null) {
                    throw new RetrofitStarterException("The baseApi of @RetrofitBase in the [" + clazz.getSimpleName() + "] Interface, does not define @RetrofitBuilder");
                }
            }
        }
        return targetClazz;
    }

    private Class<?> findParentRetrofitBaseClazz(Class<?> clazz) {
        RetrofitBase retrofitBase = clazz.getDeclaredAnnotation(RetrofitBase.class);
        Class<?> targetClazz = clazz;
        if (retrofitBase != null) {
            final Class<?> baseApiClazz = retrofitBase.baseApi();
            if (baseApiClazz != null) {
                targetClazz = findParentRetrofitBaseClazz(baseApiClazz);
            }
        }
        return targetClazz;
    }

    private Set<RetrofitInterceptor> getInterceptors(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        Set<RetrofitInterceptor> retrofitInterceptorAnnotations = new LinkedHashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Interceptors) {
                RetrofitInterceptor[] values = ((Interceptors) annotation).value();
                Collections.addAll(retrofitInterceptorAnnotations, values);
            } else if (annotation instanceof RetrofitInterceptor) {
                retrofitInterceptorAnnotations.add((RetrofitInterceptor) annotation);
            }
        }
        return retrofitInterceptorAnnotations;
    }


}
