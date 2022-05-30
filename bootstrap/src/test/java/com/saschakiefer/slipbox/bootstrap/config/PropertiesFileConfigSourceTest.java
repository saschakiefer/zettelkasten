package com.saschakiefer.slipbox.bootstrap.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertiesFileConfigSourceTest {
    @Test
    public void createConfigSource_returnsConfigSource() {
        PropertiesFileConfigSource configSource = new PropertiesFileConfigSource();

        assertNotNull(configSource.getProperties());
    }
}