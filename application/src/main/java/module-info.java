module application {
    requires domain;
    requires static lombok;
    exports com.saschakiefer.slipbox.application.ports.output;
    exports com.saschakiefer.slipbox.application.ports.input;
    exports com.saschakiefer.slipbox.application.usecase;

    requires jakarta.enterprise.cdi.api;
    requires jakarta.inject.api;
}