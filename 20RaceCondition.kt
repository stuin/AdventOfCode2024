package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.time.TimeSource

//Diamond of points at a specific distance from center
fun Pair<Int,Int>.rangeNeighbors(distance: Int, edge: Pair<Int,Int>): List<Pair<Int,Int>> {
    if(distance == 1)
        return sideNeighbors().filter { edge.contains(it) }
    return (0 until distance).flatMap { offset->
        listOf(
            this+Pair(distance-offset,offset),
            this+Pair(offset,offset-distance),
            this+Pair(offset-distance,-offset),
            this+Pair(-offset,distance-offset),
        ).filter { edge.contains(it) }
    }
}

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
    //println(obstacles)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //A*
    //Point, parent Point, (f,g)
    val open = mutableListOf(listOf(start, start, Pair(0, 0)))
    val closed: MutableList<List<Pair<Int,Int>>> = mutableListOf()
    val finals: MutableList<List<Pair<Int,Int>>> = mutableListOf()

    while(open.isNotEmpty()) {
        val q = open.minBy { it[2].second }
        val diff = q[0] - q[1]
        open.remove(q)
        closed.add(q)

        //Find and calculate all options
        val successors = q[0].sideNeighbors().filter { !obstacles.contains(it) }.map {
            val g = q[2].second + 1
            val h = (end.first - it.first) + (end.second - it.second)
            listOf(it, q[0], Pair(g+h, g))
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

    //Display distance 10's digit on map
    closed.forEach {
        val s = "000" + it[2].second.toString()
        filled[it[0].second][it[0].first] = s[s.length-2]
    }

    //Print grid and path length
    filled.forEach { println(it.joinToString("")) }
    println()
    println(finals[0][2].second)

    //Find all 2-step skips
    val targetLength = 100
    val shortSkips = closed.flatMap { skipS->
        //For each open neighbor of a blocked neighbor of a point on the path
        skipS[0].sideNeighbors().filter { obstacles.contains(it) }.flatMap { skipM->
            skipM.sideNeighbors().asSequence()
                .filter { skipE-> skipE != skipS[0] && edge.contains(skipE) && !obstacles.contains(skipE) }
                //Use stored distance along path to calculate gained time
                .map { skipE-> closed.first { it[0]==skipE }[2].second-skipS[2].second-2 }
                .filter { it>=targetLength }
                .toList()
        }
    }

    //Part 1 example check and answer
    //println(shortSkips.toSortedSet().map { save-> Pair(save, shortSkips.count { it==save }) })
    println(shortSkips.size)

    //Find all long range skips
    var index = 0
    val longSkips = closed.parallelStream().flatMap { skipS->
        println("${index++}/${closed.size}")
        //Find each neighbor at each range
        (2 until 21).flatMap { distance->
            skipS[0].rangeNeighbors(distance, edge).asSequence()
                .filter { skipE-> !obstacles.contains(skipE) }
                //Use stored distance along path to calculate gained time
                .map { skipE-> closed.first { it[0]==skipE }[2].second-skipS[2].second-distance }
                .filter { it>=targetLength }
                .toList()
        }.parallelStream()
    }.toList()

    //Part 2 example check and answer
    println(longSkips.toSortedSet().map { save-> Pair(save, longSkips.count { it==save }) })
    println(longSkips.count()) //896962 1022577

    //filled.forEach { println(it.joinToString("")) }
    //println()

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}