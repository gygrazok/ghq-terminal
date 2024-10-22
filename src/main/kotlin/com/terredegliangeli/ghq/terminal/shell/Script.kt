package com.terredegliangeli.ghq.terminal.shell

class Script() {
    private val commands = mutableListOf<ScriptRow>()

    fun addCommand(command:String, args:List<String>) {
        commands.add(ScriptRow(command, args))
    }

    fun getNextCommand():ScriptRow? {
        if (commands.isEmpty()) {
            return null
        }
        return commands.removeAt(0)
    }

    fun hasNext():Boolean {
        return commands.isNotEmpty()
    }

    fun clear() {
        commands.clear()
    }
}

class ScriptRow(val command:String, val args:List<String>);