package Event5_AOC

import java.io.File
import java.util.Optional
import kotlin.math.abs
import kotlin.streams.toList
import kotlin.time.TimeSource

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val iteration1 = lines[0].split(" ").map { it.toLong() }
    println(iteration1)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Simulate 1 stone over 25 blinks
    fun simulate25(start: Long): List<Long> {
        var iteration = listOf(start)
        (1..25).forEach { _ ->
            iteration = iteration.flatMap { stone->
                val str = stone.toString()
                if(0.toLong() == stone)
                    listOf(1.toLong())
                else if(str.length % 2 == 0)
                    listOf(str.dropLast(str.length/2).toLong(), str.drop(str.length/2).toLong())
                else
                    listOf(stone * 2024)
            }
        }
        return iteration
    }


    //Map of what 1 stone becomes after 25 blinks
    val skips: MutableMap<Long?,List<Long>> = mutableMapOf()
    iteration1.forEach { skips[it] = simulate25(it) }

    //Use map to get entire list for blink 25
    val iteration2 = iteration1.mapNotNull { skips[it] }.flatten()
    println(iteration2.size)

    //Update map with new stone numbers
    var todo = iteration2.toSet().filter { !skips.contains(it) }
    println(todo.size)
    todo.forEach { skips[it] = simulate25(it) }
    println(skips.size)
    println(skips.maxOf { it.value.size })

    //Any known stones at blink 50 can skip straight to final count at blink 75
    //No need to store them
    var finished = 0
    val final1 = iteration2.parallelStream().map { first->
        //println("${finished++}/${iteration.size}")
        skips[first]?.filter { skips[it] != null }?.sumOf { skips[it]?.size?.toLong() ?: 0 } ?: 0
    }.toList().filterNotNull().sum()
    println(final1)

    //List remaining unknown stones for blink 50
    finished = 0
    val iteration3 = iteration2.parallelStream().map { first->
        //println("${finished++}/${iteration.size}")
        skips[first]?.filter { skips[it] == null }
    }.toList().filterNotNull().flatten()
    println(iteration3.size)

    //Update map with new stone numbers
    todo = iteration3.toSet().filter { !skips.contains(it) }
    println(todo.size)
    todo.forEach { skips[it] = simulate25(it) }
    println(skips.size)
    println(skips.maxOf { it.value.size })

    //Count up remaining stones at blink 75
    finished = 0
    val final2 = iteration3.parallelStream().map {
        //println("${finished++}/${iteration.size}")
        skips[it]?.size?.toLong() ?: 0
    }.toList().filterNotNull().sum()
    println(final1 + final2)

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}