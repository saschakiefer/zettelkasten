package com.saschakiefer.slipbox.bootstrap.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class PropertiesFileConfigSource implements ConfigSource {
    private Map<String, String> props = new HashMap<>();

    public PropertiesFileConfigSource() {
        File configFile = FileUtils.getFile(FileUtils.getUserDirectory(), ".config", "slipbox.conf");

        try {
            FileReader reader = new FileReader(configFile);
            Properties properties = new Properties();
            properties.load(reader);

            props = properties.entrySet().stream().collect(
                    Collectors.toMap(
                            e -> String.valueOf(e.getKey()),
                            e -> String.valueOf(e.getValue()),
                            (prev, next) -> next, HashMap::new
                    ));

            log.debug("User Configuration:");
            props.forEach((key, value) -> log.debug("\t{}={}", key, value));
        } catch (Exception e) {
            log.warn("No local config file found. Falling back to default.");
        }

    }

    @Override
    public Map<String, String> getProperties() {
        return props;
    }

    @Override
    public Set<String> getPropertyNames() {
        return props.keySet();
    }

    @Override
    public int getOrdinal() {
//      will only be used if the config engine is unable to find a value in System Properties,
//      Environment Variables from System or Environment Variables from .env file in this order
        return 275;
    }

    @Override
    public String getValue(String propertyName) {
        return props.get(propertyName);
    }

    @Override
    public String getName() {
        return "PropertyFileConfigSource";
    }
}
