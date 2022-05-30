module bootstrap {
    requires static lombok;

    requires application;
    requires domain;
    requires framework;

    requires info.picocli;
    requires jakarta.inject.api;
    requires maven.shared.utils;
    requires microprofile.config.api;
    requires org.apache.commons.io;
    requires quarkus.core;
    requires quarkus.picocli;

    exports com.saschakiefer.slipbox.bootstrap.config;

}