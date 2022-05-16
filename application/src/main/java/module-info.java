module application {
    requires domain;
    requires static lombok;
    exports com.saschakiefer.application.ports.output;
    exports com.saschakiefer.application.ports.input;
}