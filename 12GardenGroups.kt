package Event5_AOC

import java.io.File
import java.time.Instant
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.time.TimeSource

/*operator fun Pair<Int,Int>.compareTo(point: Pair<Int,Int>): Int {
    return Pair(this.first-point.first, this.second-point.second)
}*/

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val edge = Pair(lines[0].length, lines.size)

    val types = lines.flatMap { it.toSet() }.toSet().filter { it != '.' }
    println(types)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    val visited = lines.map { line-> line.map { false }.toMutableList() }.toMutableList()

    fun Pair<Int,Int>.visitNeighbors(): List<Pair<Int,Int>> {
        if(visited[this.second][this.first])
           return listOf()
        visited[this.second][this.first] = true
        return listOf(this) + this.sideNeighbors().filter {
            edge.contains(it) && !visited[it.second][it.first] && lines[it.second][it.first] == lines[this.second][this.first]
        }.flatMap { it.visitNeighbors() }
    }

    val plants = types.map { f ->
        lines.mapIndexed { y: Int, s: String ->
            s.mapIndexed { x, c ->
                if(c == f && !visited[y][x]) {
                    Pair(x,y).visitNeighbors()
                } else
                    null
            }.filterNotNull()
        }.flatten()
    }
    println(types.zip(plants).map { groups-> listOf(groups.first) + groups.second.map { it.size } })

    val price = plants.flatten().map { group->
        val outside = group.map { it.sideNeighbors() }.flatten().filter { !group.contains(it) }
        val long1 = outside.filter { outside.contains(it+Pair(-1,0)) || outside.contains(it+Pair(0,-1)) }
        val long = long1.toSet().flatMap { u-> Array(ceil(outside.count {it==u}/2.0).toInt(), {u}).toList() }
        val ladel = outside.toSet().filter { u-> outside.count { it==u } == 3 }.filter { u-> u.sideNeighbors().count { long.contains(it) }>0 }
        listOf(types.indexOf(lines[group[0].second][group[0].first]), group.size, outside.size, long.size, ladel.size, outside.size-long.size+ladel.size)
    }
    println(price.map { it.mapIndexed { index, i -> if(index == 0) types[i] else i } })
    println(price.sumOf { it[1]*it[2] })
    println(price.sumOf { it[1]*it[5] })

    val endTime = timeSource.markNow() //1005072
    println(endTime-startTime)


}