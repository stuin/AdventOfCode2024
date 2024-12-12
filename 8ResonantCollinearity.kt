package Event5_AOC

import java.io.File
import java.time.Instant
import kotlin.math.pow
import kotlin.time.TimeSource

//Find every combination of two items from list
fun <T> List<T>.pairCombinations(): List<Pair<T,T>> {
    return this.mapIndexed { indexA, a->
        this.filterIndexed { indexB, b->
            b!=a && indexA<indexB
        }.map { b->
                Pair(a,b)
        }
    }.flatten()
}

operator fun Pair<Int,Int>.minus(point: Pair<Int,Int>): Pair<Int,Int> {
    return Pair(this.first-point.first, this.second-point.second)
}

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val edge = Pair(lines[0].length, lines.size)

    //List out all frequency names
    val frequencies = lines.flatMap { it.toSet() }.toSet().filter { it != '.' }
    println(frequencies)

    //List out antenna points by frequency
    val antenna = frequencies.map { f ->
        lines.mapIndexed { y: Int, s: String ->
            s.mapIndexed { x, c ->
                if(c == f)
                    Pair(x,y)
                else
                    null
            }.filterNotNull()
        }.flatten()
    }
    println(antenna)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //List out all combinations of 2 antenna in each frequency
    val combinations = antenna.map { it.pairCombinations() }
    println(combinations)

    //Find all antinodes for each pair
    val antinodes = combinations.map { f->
        f.map { positions->
            val difference = positions.second-positions.first
            val found = mutableListOf(positions.first, positions.second)

            //Iterate backwards along linear steps
            var back = positions.first-difference
            while(edge.contains(back)) {
                found += back
                back -= difference
            }

            //Iterate forwards along linear steps
            var forward = positions.second+difference
            while(edge.contains(forward)) {
                found += forward
                forward += difference
            }
            found
        }.flatten()
    }.flatten().toSet()
    println(antinodes)
    println(antinodes.count())

    val endTime = timeSource.markNow()
    println(endTime-startTime)


}