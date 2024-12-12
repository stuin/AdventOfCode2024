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

    var finished = 0

    val skips: MutableMap<Long?,List<Long>> = mutableMapOf()
    iteration1.forEach { skips[it] = simulate25(it) }

    val iteration2 = iteration1.mapNotNull { skips[it] }.flatten()
    println(iteration2.size)
    //blink 25 done

    var todo = iteration2.toSet().filter { !skips.contains(it) }
    println(todo.size)
    todo.forEach { skips[it] = simulate25(it) }
    println(skips.size)
    println(skips.maxOf { it.value.size })

    val final1 = iteration2.parallelStream().map { first->
        //println("${finished++}/${iteration.size}")
        skips[first]?.filter { skips[it] != null }?.sumOf { skips[it]?.size?.toLong() ?: 0 } ?: 0
    }.toList().filterNotNull().sum()
    println(final1)

    finished = 0
    val iteration3 = iteration2.parallelStream().map { first->
        //println("${finished++}/${iteration.size}")
        skips[first]?.filter { skips[it] == null }
    }.toList().filterNotNull().flatten()
    println(iteration3.size)
    //blink 50 done

    todo = iteration3.toSet().filter { !skips.contains(it) }
    println(todo.size)
    todo.forEach { skips[it] = simulate25(it) }
    println(skips.size)
    println(skips.maxOf { it.value.size })

    finished = 0
    val final2 = iteration3.parallelStream().map {
        //println("${finished++}/${iteration.size}")
        skips[it]?.size?.toLong() ?: 0
    }.toList().filterNotNull().sum()
    println(final1 + final2)
    //blink 75 done 26803144528360 2624640240 238317474993392

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}