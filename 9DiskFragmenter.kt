package Event5_AOC

import java.io.File
import kotlin.time.TimeSource

fun main(args: Array<String>) {
    val reader = File(args[0])
    val line = reader.readLines()[0]

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Fill out list of file numbers
    //Empty spots become -{number of empty spots so far}
    var empty = 1
    val before = line.mapIndexed { index, c->
        if(index % 2 == 0)
            Array(c.digitToInt()) { index/2 }.toList()
        else
            Array(c.digitToInt()) { -empty++ }.toList()
    }.flatten()
    println(before)
    println(empty)

    val ending = before.reversed().filter { it>=0 }
    println(ending)

    //Replace empty spots with their reversed index
    val after1 = before.mapIndexed { index, c->
        if(c < 0)
            ending[-c - 1].toLong()
        else
            c.toLong()
    }.dropLast(before.size-ending.size)
    println(after1.joinToString(""))

    //First part answer
    val checksum1 = after1.reduceIndexed {index, acc, i ->
        acc + index * i
    }
    println(checksum1)

    //List each space by (start index, length)
    val spaces = mutableListOf(mutableListOf(0,0))
    var continuous = false
    before.forEachIndexed { index, i ->
        if(i<0) {
            if(continuous)
                spaces[spaces.size-1][1]++
            else {
                spaces.add(mutableListOf(index,1))
                continuous = true
            }
        } else
            continuous = false
    }
    println(spaces)

    //Iteratively find matching spaces
    val after2 = before.toMutableList()
    var current = before.last()
    var currentLength = 0
    before.reversed().forEachIndexed { index, i ->
        if(i == current)
            currentLength++
        else {
            val space = spaces.firstOrNull { it[1] >= currentLength }
            //Insert file at spot and mark space full
            if(space != null && current >= 0 && space[0] < after2.size-index) {
                (0 until currentLength).forEach {
                    after2[space[0] + it] = current
                    after2[after2.size - (index - it)] = 0
                }
                space[0] += currentLength
                space[1] -= currentLength
            }

            current = i
            currentLength = 1
        }
    }
    val after3 = after2.map { if(it<0) 0 else it.toLong() }
    println(after3)

    //Part 2 final answer
    val checksum2 = after3.reduceIndexed {index, acc, i ->
        acc + index * i
    }
    println(checksum2)

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}