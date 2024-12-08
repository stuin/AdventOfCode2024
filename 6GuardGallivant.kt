package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.time.TimeSource

fun Pair<Int,Int>.contains(point: Pair<Int,Int>): Boolean {
    return point.first >= 0 && point.second >= 0 &&
            point.first < this.first && point.second < this.second
}

operator fun Pair<Int,Int>.plus(point: Pair<Int,Int>): Pair<Int,Int> {
    return Pair(this.first+point.first, this.second+point.second)
}

val rotate = mapOf(
    Pair(Pair(0, -1), Pair(1, 0)),
    Pair(Pair(1, 0), Pair(0, 1)),
    Pair(Pair(0, 1), Pair(-1, 0)),
    Pair(Pair(-1, 0), Pair(0, -1)),
)

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val start = Pair(
        lines.first { it.contains('^') }.indexOf('^'),
        lines.indexOfFirst { it.contains('^') },
    )
    println(start)

    val edge = Pair(lines[0].length, lines.size)

    var current = start
    var movement = Pair(0, -1)
    val path = mutableListOf(current)
    var filled = lines.map { it.toMutableList() }

    val obstacles = lines.mapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c ->
            if(c == '#')
                Pair(x,y)
            else
                null
        }.filterNotNull()
    }.flatten()
    println(obstacles)

    while(edge.contains(current + movement)) {
        filled[current.second][current.first] = 'X'

        if(obstacles.contains(current + movement))
            movement = rotate[movement]!!
        current += movement
        filled[current.second][current.first] = '@'

        if(!path.contains(current))
            path.add(current)
    }

    filled.forEach { println(it.joinToString("")) }
    println("Original")

    println(path.size)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    println(path.parallelStream().map { added ->
        val obstacles2 = obstacles+added
        var current = start
        var movement = Pair(0, -1)
        var filled = lines.map { it.toMutableList() }
        filled[added.second][added.first] = 'O'
        val history = mutableListOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()

        while(edge.contains(current + movement) && !history.contains(Pair(current,movement))) {
            history.add(Pair(current,movement))
            filled[current.second][current.first] = 'X'

            if(obstacles2.contains(current + movement))
                movement = rotate[movement]!!
            if(obstacles2.contains(current + movement))
                movement = rotate[movement]!!
            current += movement
            filled[current.second][current.first] = '@'
        }
        if(edge.contains(current + movement)) {
            //filled.forEach { println(it.joinToString("")) }
            //println("$index/${path.size}")
        }

        edge.contains(current + movement)
    }.filter { it }.count())

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}