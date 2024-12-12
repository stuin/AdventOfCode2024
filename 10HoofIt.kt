package Event5_AOC

import java.io.File
import kotlin.math.abs

fun Pair<Int,Int>.sideNeighbors(): List<Pair<Int,Int>> {
    return listOf(this+Pair(0,1), this+Pair(1,0), this+Pair(0,-1), this+Pair(-1,0))
}

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines().map { line-> line.map { it.digitToInt() } }

    val edge = Pair(lines[0].size, lines.size)

    val zeros = lines.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            if(c == 0)
                Pair(x,y)
            else
                null
        }.filterNotNull()
    }.flatten()
    println(zeros)

    fun recursiveCheck(point: Pair<Int,Int>, level: Int): List<Pair<Int,Int>> {
        if(level == 10)
            return listOf(point)
        return point.sideNeighbors().filter { edge.contains(it) && lines[it.second][it.first] == level }.flatMap { recursiveCheck(it, level+1) }
    }

    val trailheads1 = zeros.map { head->
       recursiveCheck(head, 1).toSet()
    }.map { it.size }
    println(trailheads1)
    println(trailheads1.sum())

    val trailheads2 = zeros.map { head->
        recursiveCheck(head, 1)
    }.map { it.size }
    println(trailheads2)
    println(trailheads2.sum())
}