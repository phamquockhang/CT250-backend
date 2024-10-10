
package com.dvk.ct250backend.infrastructure.config.elastic;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.dvk.ct250backend.infrastructure.repository")
@ComponentScan(basePackages = "com.dvk.ct250backend.domain.flight.service")
public class ElasticConfig {

}