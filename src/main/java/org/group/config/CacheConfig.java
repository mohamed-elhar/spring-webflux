package org.group.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig  extends CachingConfigurerSupport {
    public static final String CACHE_NAME = "NAPTRCache";
}
