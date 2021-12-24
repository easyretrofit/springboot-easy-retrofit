package com.github.liuziyuan.retrofit.annotation;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:38 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableRetrofit {

    /**
     * Scan base package paths of retrofit resources
     * Same as basePackages method
     *
     * @return basePackages
     */
    String[] value() default {};

    /**
     * Scan base package paths of retrofit resources
     * @return basePackages
     */
    String[] basePackages() default {};
}
