module framework {
    requires domain;
    requires application;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires spring.web;
    requires static org.slf4j;
    requires static lombok;
    requires info.picocli;
    requires quarkus.picocli;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.inject.api;
    requires microprofile.config.api;

    exports com.saschakiefer.slipbox.framework.adapter.input.cli;
    exports com.saschakiefer.slipbox.framework.adapter.output.file;
}