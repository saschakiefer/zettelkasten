module framework {
    requires domain;
    requires application;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires static org.slf4j;
    requires static lombok;
    requires info.picocli;
    requires quarkus.picocli;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.inject.api;

    exports com.saschakiefer.slipbox.framework.adapter.input.cli;
}