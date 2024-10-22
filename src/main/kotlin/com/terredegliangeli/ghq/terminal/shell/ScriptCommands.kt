package com.terredegliangeli.ghq.terminal.shell

import com.terredegliangeli.ghq.terminal.configuration.TerminalSettings
import com.terredegliangeli.ghq.terminal.utils.*
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class ScriptCommands(val settings: TerminalSettings) {
    var script = Script()
    private var repeat = 1
    private var connected = false
    private var varName: String? = null
    private var connectedTo: String? = null
    private var retrievedMessage: String? = null


    @ShellMethod(key = ["BEGIN"])
    fun begin() {
        println("Opening GHQ-BASIC script editor. Use END to execute the script.")
        settings.scriptMode = true
    }

    @ShellMethod(key = ["REPEAT"])
    fun commandRepeat(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("REPEAT", args)
    }

    @ShellMethod(key = ["OPEN"])
    fun open(@ShellOption(arity = Integer.MAX_VALUE, defaultValue = "") args: List<String>) {
        addScriptCommand("OPEN", args)
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
        addScriptCommand("END", emptyList())
        evaluateCommands()
    }


    private fun addScriptCommand(key: String, params: List<String>) {
        if (!settings.scriptMode) {
            printError("Script command detected outside script editor. Use BEGIN to open the script editor.")
            return
        }
        script.addCommand(key, params)
    }

    private fun evaluateCommands() {
        var ok = true
        while (script.hasNext() && ok) {
            ok = executeCommand(script.getNextCommand()!!)
        }
        repeat = 1
        connected = false
        connectedTo = null
        varName = null
        retrievedMessage = null
        settings.scriptMode = false
        script.clear()
    }

    private fun executeCommand(row: ScriptRow): Boolean {
        return when (row.command) {
            "REPEAT" -> executeRepeat(row.args)
            "OPEN" -> executeOpen(row.args)
            "PEEK" -> executePeek(row.args)
            "PRINT" -> executePrint(row.args)
            "END" -> true
            else -> {
                printError("unknown command: ${row.command}")
                false
            }
        }
    }

    private fun executeRepeat(args: List<String>): Boolean {
        return when {
            args.isEmpty() -> {
                printError("expecting the number of repetitions for REPEAT command")
                false
            }

            args.size > 1 -> {
                printError("too many arguments for REPEAT command")
                false
            }

            args[0].toIntOrNull() == null -> {
                printError("expecting a number for REPEAT command")
                false
            }

            else -> {
                repeat = args[0].toInt()
                true
            }
        }
    }

    private fun executeOpen(args: List<String>): Boolean {
        return when {
            args.isEmpty() || args.size == 1 -> {
                printError("missing arguments for OPEN command")
                false
            }

            args.size > 2 -> {
                printError("too many arguments for OPEN command")
                false
            }

            else -> {
                connectedTo = args[1]
                for (i in 1..repeat) {
                    print("CONNECTION ATTEMPT $i/$repeat")
                    if (i == 3 && fileExists(connectedTo)) {
                        retrievedMessage = readFile(connectedTo)
                        delayedMessage("SUCCESS!")
                        connected = true
                        break
                    } else {
                        delayedMessage("FAILED!")
                    }
                }
                if (!connected) {
                    printError("Unable to connect to $connectedTo")
                    connectedTo = null
                    false
                } else {
                    println("Connected to $connectedTo")
                    true
                }
            }
        }
    }

    private fun executePeek(args: List<String>): Boolean {
        return when {
            !connected -> {
                printError("not connected; cannot execute PEEK command")
                false
            }

            args.isEmpty() -> {
                printError("missing arguments for PEEK command")
                false
            }

            args.size > 2 -> {
                printError("too many arguments for PEEK command")
                false
            }

            args[0] != "DATABASE;" -> {
                printError("invalid PEEK target or missing semicolon")
                false
            }

            else -> {
                varName = args[1]
                true
            }
        }
    }

    private fun executePrint(args: List<String>): Boolean {
        return when {
            args.isEmpty() -> {
                printError("missing arguments for PRINT command")
                false
            }

            args.size > 1 -> {
                printError("too many arguments for PRINT command")
                false
            }

            else -> {
                if (connectedTo == null || retrievedMessage == null) {
                    printError("You are not connected to any database")
                    false
                }
                if (args[0] == varName) {
                    printMessageLineByLine(retrievedMessage!!)
                    true
                } else {
                    printError("no data stored in variable $varName")
                    false
                }
            }
        }
    }
}