module domain {
    requires static lombok;
    requires microprofile.config.api;
    requires jakarta.inject.api;

    exports com.saschakiefer.slipbox.domain.entity;
    exports com.saschakiefer.slipbox.domain.specification;
    exports com.saschakiefer.slipbox.domain.vo;
    exports com.saschakiefer.slipbox.domain.exception;
}