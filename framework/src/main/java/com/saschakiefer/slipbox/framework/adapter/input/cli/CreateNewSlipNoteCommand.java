package com.saschakiefer.slipbox.framework.adapter.input.cli;

import picocli.CommandLine;

import javax.enterprise.context.Dependent;

@Dependent
@CommandLine.Command(name = "create", mixinStandardHelpOptions = true, description = "Create a new slip note")
public class CreateNewSlipNoteCommand implements Runnable {
    @CommandLine.Option(names = {"-p", "--parent"}, description = "Parent slip note (full file name)")
    String parent;

    @CommandLine.Option(names = {"-t", "--title"}, description = "Slip note title", required = true)
    String title;

    @Override
    public void run() {
        System.out.println(String.format("Create Slip note '%s' with parent '%s'", title, parent));
    }
}
