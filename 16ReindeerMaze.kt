package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.time.TimeSource


fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()
    println(lines)

    val start = Pair(
        lines.first { it.contains('S') }.indexOf('S'),
        lines.indexOfFirst { it.contains('S') },
    )
    println(start)

    val end = Pair(
        lines.first { it.contains('E') }.indexOf('E'),
        lines.indexOfFirst { it.contains('E') },
    )
    println(end)

    val edge = Pair(lines[0].length, lines.size)

    var current = start
    var filled = lines.map { it.toMutableList() }.toMutableList()

    //List obstacles as points
    val obstacles = lines.mapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c ->
            if(c == '#')
                Pair(x,y)
            else
                null
        }.filterNotNull()
    }.flatten()
    println(obstacles)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //A*
    //Point, parent Point, (f,g)
    val open = mutableListOf(mutableListOf(start, start, Pair(0, 0)))
    val closed: MutableList<MutableList<Pair<Int,Int>>> = mutableListOf()
    var finals: MutableList<List<Pair<Int,Int>>> = mutableListOf()

    while(open.isNotEmpty()) {
        val q = open.minBy { it[2].second }
        val diff = q[0] - q[1]
        open.remove(q)
        closed.add(q)

        //Find and calculate all options
        val successors = q[0].sideNeighbors().filter { !obstacles.contains(it) }.map {
            val g = q[2].second + if(it == q[0] + diff) 1 else 1001
            val h = (end.first - it.first + 1000*abs(diff.second)) + (end.second - it.second + 1000*abs(diff.first))
            mutableListOf(it, q[0], Pair(g+h, g))
        }

        //Check if finished
        if(successors.any { it[0] == end })
            finals.add(successors.first { it[0] == end })

        //Do not record worse paths to a previous location
        open.addAll(successors.filter { node->
            !open.any { node[0] == it[0] && node[2].first >= it[2].first } &&
                    !closed.any { node[0] == it[0] && node[2].first >= it[2].first }
        })
        //println(open)
        //println(closed)
        //println()
    }
    closed.forEach {
        val s = "000" + it[2].second.toString()
        filled[it[0].second][it[0].first] = s[s.length-4]
    }

    //Trace final best paths
    if(finals.isNotEmpty()) {
        var max = finals.minOf { it[2].second }
        var max2 = finals.minOf { it[2].second }
        var backtracks: List<List<Pair<Int,Int>>> = finals.filter { it[2].second == max }
        println("$max: ${backtracks.size}")
        while(!backtracks.any { it[0] == start }) {
            backtracks = backtracks.flatMap { backtrack->
                closed.filter {
                    it[0].sideNeighbors().contains(backtrack[0]) &&
                            it[2].first<=backtrack[2].first && it[2].second <= max
                }.also { filled[backtrack[0].second][backtrack[0].first] = 'O' }
            }.toSet().toList()
            max = max2
            max2 = backtracks.maxOf { it[2].second }
            println(backtracks)
        }
    }

    filled.forEach { println(it.joinToString("")) }
    println()
    println(finals)

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}