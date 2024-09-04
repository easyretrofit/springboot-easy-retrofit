package io.github.easyretrofit.spring.boot;

import io.github.easyretrofit.core.util.PropertiesFileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SpringBootRetrofitExtensionScanner {

    private static final String RETROFIT_EXTENSION_PROPERTIES = "META-INF/retrofit-extension.properties";
    private static final String RETROFIT_EXTENSION_CLASS_NAME = "retrofit.extension.name";

    /**
     * Scan packageName
     *
     * @return
     * @throws IOException
     */
    public Set<String> scan() throws IOException {
        // 获取类路径下的所有META-INF/spring.factories文件
        Set<String> extensionNames = new HashSet<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:" + RETROFIT_EXTENSION_PROPERTIES;
        Resource[] resources = null;
        try {
            resources = resolver.getResources(pattern);
            // 遍历并打印每个文件的内容
            for (Resource resource : resources) {
                if (resource.exists() && resource.isReadable()) {
                    Set<String> propertiesKeys = PropertiesFileUtils.getPropertiesKeys(new InputStreamReader(resource.getInputStream()), RETROFIT_EXTENSION_CLASS_NAME);
                    extensionNames.addAll(propertiesKeys);
                }
            }
        } catch (IOException ignored) {
        }
        return extensionNames;
    }

    private void setExtensionNames(String classname, Set<String> extensionNames) {
        int lastDotIndex = classname.lastIndexOf('.');
        String packageName = classname.substring(0, lastDotIndex);
        extensionNames.add(packageName);
    }
}
