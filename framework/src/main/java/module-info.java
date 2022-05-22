module framework {
    requires domain;
    requires application;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires static org.slf4j;
    requires static lombok;

    provides com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort with com.saschakiefer.slipbox.framework.adapter.output.file.SlipNoteManagementFileAdapter;

    uses com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
}