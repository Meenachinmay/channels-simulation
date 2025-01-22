package org.polarmeet

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

// ANSI Colors and Emojis
val RESET = "\u001B[0m"
val CYAN = "\u001B[36m"
val YELLOW = "\u001B[33m"
val GREEN = "\u001B[32m"
val RED = "\u001B[31m"
val PURPLE = "\u001B[35m"

fun main() = runBlocking {
    val conveyorBelt = Channel<Int>(capacity = 3) // Conveyor belt with space for 3 items
    var bufferSize = 0 // Track buffer size manually

    // ASCII Art Header
    println("${PURPLE}🏭 ==== Assembly Line Simulation ==== 🏭$RESET")
    println("${CYAN}[Conveyor Belt] Buffer size: 3$RESET\n")

    // Producer Robot 🤖
    val producerBot = launch {
        for (productId in 1..5) {
            println("${YELLOW}🤖 [ROBOT] Building Product #$productId...$RESET")
            delay(150) // Time to build
            conveyorBelt.send(productId)
            bufferSize++ // Increment buffer size
            println("${CYAN}📦 [BELT] Product #$productId placed on the conveyor!$RESET")
            printBuffer(bufferSize)
        }
        conveyorBelt.close()
        println("\n${YELLOW}🤖 [ROBOT] Shutting down. All products sent!$RESET")
    }

    // Consumer Worker 👷
    val consumerWorker = launch {
        for (product in conveyorBelt) {
            println("${GREEN}👷 [WORKER] Packaging Product #$product...$RESET")
            delay(300) // Time to package
            bufferSize-- // Decrement buffer size
            println("${RED}📦 [SHIPPED] Product #$product ready for delivery!$RESET")
            printBuffer(bufferSize)
        }
        println("\n${GREEN}👷 [WORKER] All products shipped!$RESET")
    }

    // Wait for completion
    joinAll(producerBot, consumerWorker)
    println("\n${PURPLE}🏭 ==== Simulation Complete! ==== 🏭$RESET")
}

// Helper function to print the buffer state
fun printBuffer(size: Int) {
    print("${CYAN}⚙️ [BUFFER] Conveyor belt has $size item(s): ")
    repeat(size) { print("📦 ") }
    println("$RESET")
}