package Event5_AOC

import java.io.File
import kotlin.math.abs

fun Pair<Int,Int>.toArrow(): Char {
    return when(this) {
        Pair(-1,0) -> '<'
        Pair(0,1) -> '^'
        Pair(1,0) -> '>'
        Pair(0,-1) -> 'v'
        Pair(0,0) -> 'A'
        else -> '#'
    }
}

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    //7 8 9
    //4 5 6
    //1 2 3
    //  0 A
    val numpad = listOf(
        Pair('0', Pair(-1,0)),
        Pair('1', Pair(-2,1)),
        Pair('2', Pair(-1,1)),
        Pair('3', Pair(0,1)),
        Pair('4', Pair(-2,2)),
        Pair('5', Pair(-1,2)),
        Pair('6', Pair(0,2)),
        Pair('7', Pair(-2,3)),
        Pair('8', Pair(-1,3)),
        Pair('9', Pair(0,3)),
        Pair('A', Pair(0,0)),
    ).toMap()

    //   ^  A
    //<  v  >
    val arrowPad = listOf(
        Pair('^', Pair(-1,0)),
        Pair('<', Pair(-2,-1)),
        Pair('v', Pair(-1,-1)),
        Pair('>', Pair(0,-1)),
        Pair('A', Pair(0,0)),
    ).toMap()

    val avoid = Pair(-2,0)

    //Find each route to enter code on pad
    fun padOrder(code: String, pad: Map<Char, Pair<Int,Int>>): List<List<String>> {
        var current = Pair(0,0)
        val moves = code.map { c->
            val target = pad[c] ?: Pair(0,0)

            //X distance
            val first = if(current.first < target.first)
                (current.first until target.first).map { Pair(1,0) }
            else if(current.first > target.first)
                (target.first until current.first).map { Pair(-1,0) }
            else
                listOf()

            //Y distance
            val second = if(current.second < target.second)
                (current.second until target.second).map { Pair(0,1) }
            else if(current.second > target.second)
                (target.second until current.second).map { Pair(0,-1) }
            else
                listOf()

            //List options depending on avoided square
            val endL = listOf(Pair(0,0))
            if(target.first == avoid.first && current.second == avoid.second) {
                current = target
                listOf(second + first + endL)
            } else if(current.first == avoid.first && target.second == avoid.second) {
                current = target
                listOf(first + second + endL)
            } else {
                current = target
                setOf(first + second + endL, second + first + endL).toList()
            }
        }.map { step-> step.map { option-> option.map { it.toArrow() }.joinToString("") } }
        return moves
    }

    //Recombine list of lists of possible move choices into one list of codes
    fun listOptions(inputs: List<List<String>>): List<String> {
        var options = setOf("")
        inputs.forEach { step->
            options = step.map { newOption->
                options.map { oldOption->
                    oldOption+newOption
                }
            }.flatten().toSet()
        }
        return options.toList()
    }

    val out = lines.map { code->
        println(code)
        val num = code.dropLast(1).toInt()
        print(code.map { c-> numpad[c] })
        val inputN = listOptions(padOrder(code, numpad))//.minBy { it.length }
        println(inputN)

        //Does not work for Part 2 yet
        val midRobots = 2

        //Try each route for each code
        var current = inputN
        (0 until midRobots).forEach {
            println(it)
            //current = listOptions(padOrder(current, arrowPad)).minBy { it.length }
            current = current.map { listOptions(padOrder(it, arrowPad)) }.flatten()
        }

        val out = current.minBy { it.length }

        //Final answer
        println("${out.length} * $num = ${out.length*num}")
        out.length * num
    }
    println(out.sum())
}