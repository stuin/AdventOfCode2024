package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.time.TimeSource

operator fun Pair<Int,Int>.times(factor: Int): Pair<Int,Int> {
    return Pair(this.first*factor, this.second*factor)
}

operator fun Pair<Int,Int>.times(point: Pair<Int,Int>): Pair<Int,Int> {
    return Pair(this.first*point.first, this.second*point.second)
}

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines1 = reader.readLines()

    //Map of moves
    val moveDir = mapOf(
        Pair('>', Pair(1, 0)),
        Pair('v', Pair(0, 1)),
        Pair('<', Pair(-1, 0)),
        Pair('^', Pair(0, -1)),
    )

    val lines = lines1.dropLast(lines1.size-lines1.indexOfFirst { it=="" })
    val moves = lines1.drop(lines1.indexOfFirst { it=="" }+1).flatMap { line-> line.map { moveDir[it] } }.filterNotNull()
    println(lines)
    println(moves)

    val start = Pair(
        lines.first { it.contains('@') }.indexOf('@'),
        lines.indexOfFirst { it.contains('@') },
    )
    println(start)

    val edge = Pair(lines[0].length, lines.size)

    var current = start
    var filled = lines.map { it.toMutableList() }.toMutableList()

    //List obstacles as points
    var obstacles = lines.mapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c ->
            if(c == '#')
                Pair(x,y)
            else
                null
        }.filterNotNull()
    }.flatten()
    println(obstacles)

    //List boxes as points
    val boxStart = lines.mapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c ->
            if(c == 'O')
                Pair(x,y)
            else
                null
        }.filterNotNull()
    }.flatten()
    println(boxStart)
    var boxes = boxStart.toMutableList()

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Iteratively move to edge
    moves.forEachIndexed { index, dir ->
        var i = 1
        var pushing: MutableList<Pair<Int,Int>> = mutableListOf()
        var pushingI: MutableList<Int> = mutableListOf()
        //println(dir)

        //Find all objects being pushed
        while(!obstacles.contains(current + dir*i) && boxes.contains(current + dir*i)) {
            pushing.add(current + dir*(i+1))
            pushingI.add(boxes.indexOf(current + dir*i))
            i++
        }

        //Move pushed boxes
        if(!obstacles.contains(current + dir*i)) {
            //println(pushing)
            filled[current.second][current.first] = '.'
            current += dir
            filled[current.second][current.first] = '@'

            pushingI.forEach {
                boxes[it] += dir
            }
            if(pushing.size > 0)
                filled[pushing.last().second][pushing.last().first] = 'O'

            //filled.forEach { println(it.joinToString("")) }
            //println("$index")
        }
    }
    filled.forEach { println(it.joinToString("")) }
    println()

    var coords = boxes.map { it.first + it.second * 100 }
    //println(coords)
    println(coords.sum())

    //Create second map
    obstacles = obstacles.flatMap { listOf(Pair(it.first * 2, it.second), Pair(it.first * 2 + 1, it.second)) }
    val wideBoxes = boxStart.map { listOf(Pair(it.first * 2, it.second), Pair(it.first * 2 + 1, it.second)).toMutableList() }.toMutableList()

    current = start * Pair(2,1)
    filled = lines.map { it.flatMap { c-> mutableListOf(c, c) }.toMutableList() }.toMutableList()
    filled[current.second][current.first+1] = '.'
    filled.forEach { println(it.joinToString("")) }
    println()

    moves.forEachIndexed { index, dir ->
        var i = 1
        var pushing: MutableList<Pair<Int,Int>> = mutableListOf()
        var pushingI: MutableList<Int> = mutableListOf()
        var edge = mutableListOf(current)
        //println(dir)

        //Find all objects being pushed
        while(!edge.any { obstacles.contains(it + dir) } && edge.isNotEmpty()) {
            val edgeBoxes = wideBoxes.mapIndexed { index, pairs -> Pair(index, pairs) } .filter {
                c-> edge.any { b-> (b+dir == c.second[0]) || (b+dir == c.second[1]) }
            }.map { it.first }.toSet()
            //println(edgeBoxes)
            edge = edgeBoxes.flatMap { pi->
                pushingI.add(pi)
                i++
                pushing.add(wideBoxes[pi][0])
                pushing.add(wideBoxes[pi][1])
                if(dir.first == 0) {
                    wideBoxes[pi]
                } else {
                    mutableListOf(edge.last()+dir*2)
                }

            }.toSet().toMutableList()
            //println(edge)
        }

        //Move pushed boxes
        if(!edge.any { obstacles.contains(it + dir) }) {
            //println(pushing)
            filled[current.second][current.first] = '.'
            current += dir

            pushingI.forEach {
                //print(wideBoxes[it])
                wideBoxes[it][0] += dir
                wideBoxes[it][1] += dir
                //print("->")
                //println(wideBoxes[it])
            }
            pushing.forEach {
                filled[it.second][it.first] = '.'
            }
            pushing.forEach {
                filled[(it + dir).second][(it + dir).first] = 'O'
            }
            filled[current.second][current.first] = '@'

            if(pushing.isNotEmpty()) {
                //filled.forEach { println(it.joinToString("")) }
                //println("$index/$dir")
            }
        }
    }
    filled.forEach { println(it.joinToString("")) }
    println()

    val coords2 = wideBoxes.map { it[0].first.toLong() + it[0].second.toLong() * 100 }
    //println(coords)
    println(coords2.sum())

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}