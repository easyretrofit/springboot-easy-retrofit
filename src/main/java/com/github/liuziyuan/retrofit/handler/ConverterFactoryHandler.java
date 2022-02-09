package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.Handler;
import lombok.SneakyThrows;
import retrofit2.Converter;

import java.lang.reflect.Method;

/**
 * @author liuziyuan
 */
public class ConverterFactoryHandler implements Handler<Converter.Factory> {
    private final Class<? extends Converter.Factory> converterFactoryClass;

    public ConverterFactoryHandler(Class<? extends Converter.Factory> converterFactoryClass) {
        this.converterFactoryClass = converterFactoryClass;
    }

    @SneakyThrows
    @Override
    public Converter.Factory generate() {
        final Method createMethod = converterFactoryClass.getDeclaredMethod("create");
        final Object invoke = createMethod.invoke(null);
        return (Converter.Factory) invoke;
    }
}
