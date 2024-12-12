package Event5_AOC

import java.io.File

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val rules = lines.dropLast(lines.size-lines.indexOfFirst { it == "" }).map { it.split("|").map { x -> x.toInt()} }
    println(rules)

    val updates = lines.drop(lines.indexOfFirst { it == "" }+1).map { it.split(",").map { x -> x.toInt()} }
    println(updates)

    //For each update, find if it breaks any rules
    val correct = updates.filter { pages ->
        rules.none { rule ->
            pages.containsAll(rule) && pages.indexOf(rule[0]) > pages.indexOf(rule[1])
        }
    }
    println(correct)
    println(correct.sumOf { it[it.size/2] })

    val incorrect = updates - correct.toSet()
    println(incorrect)

    //For each incorrect update, iterate through corrections
    val corrected = incorrect.map { pages ->
        val corrections: MutableList<Int> = mutableListOf()
        pages.forEach { page ->
            val relevant = rules.filter { it[0] == page && corrections.contains(it[1]) }.map { it[1] }
            if(relevant.isNotEmpty())
                corrections.add(corrections.indexOfFirst { relevant.contains(it) }, page)
            else
                corrections.add(page)
        }
        corrections
    }
    println(corrected)
    println(corrected.sumOf { it[it.size/2] })
}