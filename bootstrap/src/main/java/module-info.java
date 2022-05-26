module com.saschakiefer.bootstrap {
    requires domain;
    requires framework;
    requires application;
    requires info.picocli;
    requires maven.shared.utils;
    requires transitive quarkus.core;
    requires jakarta.inject.api;
    requires quarkus.picocli;

}