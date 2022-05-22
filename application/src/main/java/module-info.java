module application {
    requires domain;
    requires static lombok;
    exports com.saschakiefer.slipbox.application.ports.output;
    exports com.saschakiefer.slipbox.application.ports.input;
    exports com.saschakiefer.slipbox.application.usecase;

    provides com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase with com.saschakiefer.slipbox.application.ports.input.CreateSlipNoteInputPort;
}