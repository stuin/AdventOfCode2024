package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.time.TimeSource


fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    //val end = Pair(6,6)
    //val simulated = 12
    val end = Pair(70,70)
    val simulatedStart = 1024

    val start = Pair(0,0)
    val edge = end + Pair(1,1)
    println(edge)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //List fallen bytes as points
    val obstacles = lines.map { it.split(",") }.map { Pair(it[0].toInt(), it[1].toInt()) }

    //Try each obstacle count
    val finalByte = (simulatedStart until obstacles.size).toList().parallelStream().filter { simulated ->
        val fallen = obstacles.dropLast(obstacles.size - simulated)
        //println(fallen)

        val filled = (0 until edge.second).map { y ->
            (0 until edge.first).map { x ->
                if(fallen.contains(Pair(x,y)))
                    '#'
                else
                    '.'
            }.toMutableList()
        }.toMutableList()
        //filled.forEach { println(it.joinToString("")) }
        //println()

        //A*
        //Point, parent Point, (f,g)
        val open = mutableListOf(mutableListOf(start,start,Pair(0,0)))
        val closed: MutableList<MutableList<Pair<Int,Int>>> = mutableListOf()
        var final: MutableList<Pair<Int,Int>>? = null

        while(open.isNotEmpty()) {
            val q = open.minBy { it[2].first }
            open.remove(q)
            closed.add(q)

            //Find and calculate all options
            val successors = q[0].sideNeighbors().filter { !fallen.contains(it) && edge.contains(it) }.map {
                val g = q[2].second + 1
                val h = (end.first - it.first) + (end.second - it.second)
                mutableListOf(it,q[0],Pair(g + h,g))
            }

            //Check if finished
            if(successors.any { it[0] == end }) {
                final = successors.first { it[0] == end }
                println("Found end")
                break
            }

            //Do not record worse paths to a previous location
            open.addAll(successors.filter { node ->
                !open.any { node[0] == it[0] && node[2].first >= it[2].first } &&
                        !closed.any { node[0] == it[0] && node[2].first >= it[2].first }
            })
            //println(open)
            //println(closed.map { it[0] })
            //println()
        }

        //Trace final best path
        if(final != null) {
            var backtrack: MutableList<Pair<Int,Int>> = final
            while(backtrack[0] != start) {
                backtrack = closed.first { it[0] == backtrack[1] }
                filled[backtrack[0].second][backtrack[0].first] = 'O'
                //println(backtrack)
            }
        }

        //filled.forEach { println(it.joinToString("")) }
        //println()
        if(final == null)
            println("$simulated: null")
        else
            println("$simulated: ${final[2].second}")

        final != null
    }.toList()

    //Find last valid path
    println(finalByte.max())
    println(obstacles[finalByte.max()])

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}