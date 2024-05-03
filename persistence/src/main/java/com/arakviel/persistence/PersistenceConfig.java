package com.arakviel.persistence;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.arakviel.persistence")
@PropertySource("classpath:application.properties")
public class PersistenceConfig {}
