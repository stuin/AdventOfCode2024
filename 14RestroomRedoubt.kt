package Event5_AOC

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val start = lines.map { it.split("=", ",", " ") }.map { listOf(Pair(it[1].toInt(), it[2].toInt()), Pair(it[4].toInt(), it[5].toInt())) }
    println(start)

    //val edge = Pair(11, 7) //Example
    val edge = Pair(101, 103) //Full

    val seconds = 100

    //Skip straight to final step, then handle wrapping
    val end = start.map { robot->
        val target = robot[0] + Pair(robot[1].first * seconds, robot[1].second * seconds)
        val looped1 = Pair(target.first % edge.first, target.second % edge.second)
        val looped = looped1 + Pair(if(looped1.first < 0) edge.first else 0, if(looped1.second < 0) edge.second else 0)
        looped
    }
    println(end)

    val center = Pair(edge.first / 2, edge.second / 2)
    println(center)

    //List out each quadrant
    val safety = (0 until 4).map { quadrant->
        end.filter {
            if(quadrant % 2 == 0)
                it.first < center.first
            else
                it.first > center.first
        }.filter {
            if(quadrant < 2)
                it.second < center.second
            else
                it.second > center.second
        }
    }
    println(safety)
    println(safety.map { it.size }.reduce { acc, i -> acc * i })

    //Run through first 10000 variants the same way
    var iteration = (0 until 10000).map { seconds1->
        val robots = start.map { robot->
            val target = robot[0] + Pair(robot[1].first * seconds1, robot[1].second * seconds1)
            val looped1 = Pair(target.first % edge.first, target.second % edge.second)
            val looped = looped1 + Pair(if(looped1.first < 0) edge.first else 0, if(looped1.second < 0) edge.second else 0)
            looped
        }

        //Count robots in lines
        val groupingY = (0 until edge.second).maxOf { y ->
            robots.count { it.second == y }
        }
        val groupingX = (0 until edge.first).maxOf { x ->
            robots.count { it.first == x }
        }

        //Print out any iterations with unusually straight lines
        if(groupingX * groupingY > 1000) {
            (0 until edge.second).forEach { y ->
                println((0 until edge.first).map { x ->
                    if(robots.contains(Pair(x,y)))
                        '#'
                    else
                        '.'
                }.joinToString(""))
            }
            println("${groupingX * groupingY}/$seconds1\n")
        }
    }
}