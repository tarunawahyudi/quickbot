package com.quickbot.app.core.config;

import lombok.Setter;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Setter
@Component
public class ApplicationResource implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public String getProperties(String key) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/selector.properties");
        Properties properties = new Properties();

        try(var inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        }

        return properties.getProperty(key);
    }
}
