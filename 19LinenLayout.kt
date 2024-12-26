package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.time.TimeSource

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val rawTowels = lines[0].split(", ")
    val patterns = lines.drop(2)
    println(rawTowels)
    println(patterns)

    //Map of all previously found answers
    val towels: MutableMap<String, Long> = mutableMapOf()
    println(towels)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Function to recursively try combinations and record numbers
    fun recursiveMatching(pattern: String): Long {
        if(pattern.isEmpty())
            return 1
        if(towels.contains(pattern))
            return towels[pattern] ?: 0
        val matching = rawTowels.filter {
            it.length <= pattern.length &&
            pattern.dropLast(pattern.length-it.length) == it
        }.sumOf { t-> recursiveMatching(pattern.drop(t.length)) }
        towels[pattern] = matching
        println("$pattern = $matching")
        //for(t in matching) {
        //    if(recursiveMatching(pattern.drop(t.length)))
        //        return true
        //}
        //return false
        //println(matching)
        return matching
    }

    //Final parallel run through target patterns
    var count = 0
    val possible = patterns.parallelStream().map { target->
        val out = recursiveMatching(target)
        println("$target=$out ${count++}/${patterns.size}")
        out
    }
    println(possible.reduce { t, u -> t+u })
    //println(towels)

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}