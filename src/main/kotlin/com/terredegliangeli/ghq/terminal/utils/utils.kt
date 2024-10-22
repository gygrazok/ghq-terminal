package com.terredegliangeli.ghq.terminal.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun printMessageLineByLine(message: String) = runBlocking {
    message.lines().forEach {
        println(it)
        val delayMillis = (Random.nextDouble(0.1, 0.4) * 1000).toLong()
        delay(delayMillis)
    }
}

fun delayedMessage(message: String) = runBlocking {
    for (i in 1..3) {
        print(".")
        val delayMillis = (Random.nextDouble(0.2, 0.5) * 1000).toLong()
        delay(delayMillis)
    }
    println(message)
}

fun printError(message: String) {
    println("\u001B[31mError: $message\u001B[0m")
}

fun fileExists(filePath: String?): Boolean {
    if (filePath == null) return false
    return {}::class.java.classLoader.getResourceAsStream(filePath) != null
}

fun readFile(filePath: String?): String {
    if (filePath == null) return ""
    val inputStream = {}::class.java.classLoader.getResourceAsStream(filePath)
    return inputStream?.bufferedReader()?.use { it.readText() } ?: ""
}