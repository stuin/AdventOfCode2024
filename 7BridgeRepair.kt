package Event5_AOC

import java.io.File
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

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Check each line
    //Runs in parallel
    println(answers.zip(values).parallelStream().filter { test ->
        var output: Long = test.second.sum()
        var permutation = 0
        //Brute force every combination
        while(output != test.first && permutation < 3.0.pow(test.second.size)) {
            permutation++
            //Use base 2 or 3 to track each combination of operators
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