package Event5_AOC

import java.io.File

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readText()

    val regex = """mul\([0123456789]*,[0123456789]*\)|do\(\)|don't\(\)|\n""".toRegex()

    val matches = regex.findAll(lines)
    matches.forEach { print(it.groupValues[0]) }
    println()
    println(matches.count { it.groupValues[0][0] == 'm' })
    println(matches.count { it.groupValues[0] == "do()" })
    println(matches.count { it.groupValues[0] == "don't()" })

    val disabledIndexes = matches.mapIndexed { index: Int, it: MatchResult ->
        Pair(index, it.groupValues[0])
    }.filter { it.second[0] == 'd' }.map { Pair(it.first, it.second == "do()") }
    println(disabledIndexes)

    val kept = matches.filterIndexed { index, matchResult ->
        val enabled = disabledIndexes.lastOrNull { it.first < index }
        enabled == null || enabled.second
    }.filter { it.groupValues[0][0] == 'm' }
    kept.forEach { print(it.groupValues[0]) }
    println()
    println(kept.count()) //424

    val all = matches.filter { it.groupValues[0][0] == 'm' }
    val allPairs = all.map {
        val parts = it.groupValues[0].split(',', '(', ')')
        Pair(parts[1].toInt(), parts[2].toInt())
    }
    println(allPairs.map { it.first * it.second }.sum())

    val keptPairs = kept.map {
        val parts = it.groupValues[0].split(',', '(', ')')
        Pair(parts[1].toInt(), parts[2].toInt())
    }

    println(keptPairs.map { it.first * it.second }.sum()) //102360389 100105423
    //100450138
}