package Event5_AOC

import java.io.File
import java.time.Instant
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.time.TimeSource

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val edge = Pair(lines[0].length, lines.size)

    //List out all possible group names
    val types = lines.flatMap { it.toSet() }.toSet().filter { it != '.' }
    println(types)

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Bitmap and recursive function for grouping
    val visited = lines.map { line-> line.map { false }.toMutableList() }.toMutableList()

    fun Pair<Int,Int>.visitNeighbors(): List<Pair<Int,Int>> {
        if(visited[this.second][this.first])
           return listOf()
        visited[this.second][this.first] = true
        return listOf(this) + this.sideNeighbors().filter {
            edge.contains(it) && !visited[it.second][it.first] && lines[it.second][it.first] == lines[this.second][this.first]
        }.flatMap { it.visitNeighbors() }
    }

    //List all points in each group, sorted by name
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

    //Price calculations per group
    val price = plants.flatten().map { group->
        //List all outside nodes by direction
        val outside = Pair(0,0).sideNeighbors().map { side -> group.map { it+side }.filter { !group.contains(it) } }

        //Count nodes that are part of a longer line
        val long = outside.sumOf { side-> side.count { side.contains(it+Pair(-1,0)) || side.contains(it+Pair(0,-1)) } }

        //Store results in debuggable format
        listOf(types.indexOf(lines[group[0].second][group[0].first]), group.size, outside.flatten().size, long, outside.flatten().size-long)
    }

    //Final printout and answers
    println(price.map { it.mapIndexed { index, i -> if(index == 0) types[i] else i } })
    println(price.sumOf { it[1]*it[2] })
    println(price.sumOf { it[1]*it[4] })

    val endTime = timeSource.markNow()
    println(endTime-startTime)


}