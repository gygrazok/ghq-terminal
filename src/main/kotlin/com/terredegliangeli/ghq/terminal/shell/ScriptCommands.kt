package com.terredegliangeli.ghq.terminal.shell

import com.terredegliangeli.ghq.terminal.configuration.CustomPromptProvider
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.lang.Math.random

@ShellComponent
class ScriptCommands(val promptProvider: CustomPromptProvider) {
    var script = Script()
    private var repeat = 1
    private var connected = false
    private var target:String? = null
    private var varName:String? = null;
    private var retrievedData:String? = null

    @ShellMethod(key = ["BEGIN"])
    fun begin() {
        println("Opening GHQ-BASIC script editor. Use END to execute the script.")
        promptProvider.scriptMode = true
    }

    @ShellMethod(key = ["FOR"])
    fun commandFor(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("FOR", args)
    }

    @ShellMethod(key = ["REPEAT"])
    fun commandRepeat(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("REPEAT", args)
    }

    @ShellMethod(key = ["OPEN"])
    fun open(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("OPEN", args)
    }

    @ShellMethod(key = ["NEXT"])
    fun next(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("NEXT", args)
    }

    @ShellMethod(key = ["PEEK"])
    fun peek(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("PEEK", args)
    }

    @ShellMethod(key = ["PRINT"])
    fun commandPrint(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("PRINT", args)
    }

    @ShellMethod(key = ["END"])
    fun end() {
        if (!promptProvider.scriptMode) {
            printError("Script command detected outside script editor. Use BEGIN to open the script editor.")
            return
        }
        promptProvider.scriptMode = false
        evaluateCommands()
    }


    private fun addScriptCommand(key:String, params:List<String>) {
        if (!promptProvider.scriptMode) {
            printError("Script command detected outside script editor. Use BEGIN to open the script editor.")
            return
        }
        script.addCommand(key, params)
    }

    private fun evaluateCommands() {
        while (script.hasNext()) {
            executeCommand(script.getNextCommand())
        }
        repeat = 1
        connected = false
        target = null
        varName = null
        retrievedData = null
    }

    private fun delayedMessage(message:String) {
        for (i in 1..3) {
            print(".")
            val delay = (random() * 200 + 300).toLong()
            Thread.sleep(delay)
        }
        println(message)
    }

    private fun executeCommand(row:ScriptRow?) {
        row?.args?.let {args ->
            when (row.command) {
                "REPEAT" -> {
                    when {
                        args.isEmpty() -> {
                            printError("Error: expecting the number of repetitions for REPEAT command")
                            return
                        }
                        args.size > 1 -> {
                            printError("Error: too many arguments for REPEAT command")
                            return
                        }
                        args[0].toIntOrNull() == null -> {
                            printError("Error: expecting a number for REPEAT command")
                            return
                        }
                        else -> repeat = args[0].toInt()
                    }
                }
                "OPEN" -> {
                    when {
                        args.isEmpty() || args.size == 1 -> {
                            printError("Error: missing arguments")
                            return
                        }

                        args.size > 2 -> {
                            printError("Error: too many arguments for OPEN command")
                            return
                        }

                        else -> {
                            target = args[1]
                            if (target == "USA-5552368" || target == "ITA-579705") {
                                for (i in 1..repeat) {
                                    print("CONNECTION ATTEMPT $i/$repeat")
                                    if (i == 3) {
                                        delayedMessage("SUCCESS!")
                                        connected = true;
                                        break
                                    } else {
                                        delayedMessage("FAILED!")
                                    }
                                }
                            } else {
                                for (i in 1..repeat) {
                                    print("CONNECTION ATTEMPT $i/$repeat")
                                    delayedMessage("FAILED!")
                                }
                            }
                            if (!connected) {
                                printError("Unable to connect to $target")
                            }
                        }
                    }
                }
                "PEEK" -> {
                    if (!connected) {
                        printError("Error: not connected; cannot execute PEEK command")
                        return;
                    }
                    when {
                        args.isEmpty() -> {
                            printError("Error: missing arguments")
                            return
                        }
                        args.size > 2 -> {
                            printError("Error: too many arguments for PEEK command")
                            return
                        }
                        else -> {
                            if (args[0] != "DATABASE;") {
                                printError("Error: invalid PEEK target or missing semicolon")
                                return
                            }
                            varName = args[1]
                            if (target == "ITA-579705") {
                                retrievedData = """Lorem ipsum dolor sit amet, consectetur adipiscing elit"""
                            } else if (target == "USA-5552368") {
                                retrievedData = """Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"""
                            }
                        }
                    }
                }
                "PRINT" -> {
                    when {
                        args.isEmpty() -> {
                            printError("missing arguments")
                            return
                        }

                        args.size > 1 -> {
                            printError("too many arguments for PRINT command")
                            return
                        }

                        else -> {
                            if (!connected) {
                                return
                            }
                            if (args[0] == varName) {
                                println(retrievedData)
                            } else {
                                printError("no data stored in variable $varName")
                            }
                        }
                    }
                }
            }
        }
    }

    fun printError(message:String) {
        println("\u001B[31mError: $message\u001B[0m")
    }
}