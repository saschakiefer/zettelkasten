module application {
    requires domain;
    requires static lombok;
    exports com.saschakiefer.slipbox.application.ports.output;
    exports com.saschakiefer.slipbox.application.ports.input;
}