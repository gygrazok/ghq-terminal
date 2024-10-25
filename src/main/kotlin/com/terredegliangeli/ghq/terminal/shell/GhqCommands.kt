package com.terredegliangeli.ghq.terminal.shell

import com.terredegliangeli.ghq.terminal.configuration.TerminalSettings
import com.terredegliangeli.ghq.terminal.service.FilePrintService
import com.terredegliangeli.ghq.terminal.utils.readFile
import org.jline.utils.InfoCmp
import org.springframework.shell.standard.AbstractShellComponent
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod


@ShellComponent
class GhqCommands(private val settings: TerminalSettings,
    private val filePrintService: FilePrintService) : AbstractShellComponent() {

    @ShellMethod(key = ["_panic"])
    fun panic() {
        settings.panicMode = true
        terminal.puts(InfoCmp.Capability.clear_screen)
        println(readFile("panic.txt"))
    }


    @ShellMethod(key = ["_printpdf"])
    fun printPdfFile(fileName:String, printerName:String? = null) {
        println("Printing file $fileName on printer $printerName")
        filePrintService.printFile(fileName)
    }
}

