package Event5_AOC

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    //All connections, alphabetic computer first
    val connections = lines.map { it.split("-").sorted() }.sortedBy { it[0]+it[1] }
    println(connections)

    //Each unique computer
    val computers = (connections.map { it[0] } + connections.map { it[1] }).sorted().toSet()
    println(computers)

    //Restructure connections as pairs of 1 computer followed by all linked computers with later names
    val computerConnections = computers.map { first->
        Pair(first, connections.filter { it[0] == first }.map { it[1] })
    }
    println(computerConnections)

    //Find all sets of 3
    val threes = computerConnections.pairCombinations().filter { sets->
        sets.first.second.contains(sets.second.first)
    }.flatMap { sets->
        sets.first.second.filter { sets.second.second.contains(it) }.map { listOf(sets.first.first, sets.second.first, it) }
    }
    println(threes)
    println(threes.size)

    //Find sets with a t, Part 1 answer
    val tcomps = computers.filter { it[0] == 't' }
    val final = threes.filter { inter-> inter.any { tcomps.contains(it) } }
    println(final)
    println(final.size)

    //For each computer, list all the sets of 3 that it is in front of
    val networks = computers.map { comp->
        threes.filter { comp==it[0] }
    }

    //Find the biggest network list and remove duplicates
    val biggest = networks.maxBy { it.size }.flatten().toSet().toList().sorted()
    println(biggest.joinToString(","))
}