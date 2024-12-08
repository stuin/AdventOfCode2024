package Event5_AOC

import java.io.File
import java.time.Instant
import kotlin.math.pow
import kotlin.time.TimeSource

fun Int.getBit(position: Int): Boolean {
    return (this.getDigit(position,2)) != 0
}

fun Int.getDigit(position: Int, base: Int): Int {
    val l = this.toString(base).toList().reversed()
    if(position >= l.size)
        return 0
    return l[position].digitToInt()
}

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val answers = lines.map { s-> s.split(":")[0].toLong() }
    val values = lines.map { s-> s.split(":")[1].split(' ').filter { it.isNotEmpty() }.map { it.toLong() } }

    println(answers)
    println(values)

    val example = listOf<Long>(3, 2, 9, 4, 8, 6, 796, 23, 8, 5, 3, 9)
    var permutation1 = 0
    /*while(permutation1 < 3.0.pow(example.size)) {
        permutation1 += 2

        print(example.reduceIndexed { index, acc, i ->
            when(permutation1.getDigit(index, 3)) {
                0 -> acc + i
                1 -> acc * i
                2 -> (acc.toString() + i.toString()).toLong()
                else -> acc
            }
        })
        print(" ")
        example.reduceIndexed {index, acc, l ->
            print(permutation1.getDigit(index, 3))
            acc
        }
        println()
    }
    println()*/

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    println(answers.zip(values).parallelStream().filter { test ->
        var output: Long = test.second.sum()
        var permutation = 0
        while(output != test.first && permutation < 3.0.pow(test.second.size)) {
            permutation++
            output = test.second.reduceIndexed { index, acc, i ->
                when(permutation.getDigit(index, 3)) {
                    0 -> acc + i
                    1 -> acc * i
                    2 -> (acc.toString() + i.toString()).toLong()
                    else -> acc
                }
            }
        }
        if(output == test.first)
            println("$output == ${test.first}")
        output == test.first
    }.reduce { acc, i -> Pair(acc.first + i.first, listOf()) })

    val endTime = timeSource.markNow()
    println(endTime-startTime)


}