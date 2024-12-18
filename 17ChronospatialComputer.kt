package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.time.TimeSource

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val AS = lines[0].split(" ")[2].toInt()
    val BS = lines[1].split(" ")[2].toInt()
    val CS = lines[2].split(" ")[2].toInt()
    val program = lines[4].split(" ")[1].split(",").map { it.toInt() }
    val programL = program.map { it.toLong() }.toMutableList()

    println("A=$AS, B=$BS, C=$CS")
    println(program)

    val instructions = listOf("adv","bxl","bst","jnz","bxc","out","bdv","cdv")
    println(program.filterIndexed { index,i -> index % 2 == 0 }.map { instructions[it] })
    println()

    val timeSource = TimeSource.Monotonic
    val startTime = timeSource.markNow()

    //Manual searching for part 2 by range
    //Started by figuring out range with correct length
    //Second half of answer seemed to correspond to large digits
    //Then sort through that range to get final answer
    val start = 109685312000000
    val end =   109685330000000
    //val iteration =   1000000
    //val end = 140471702000000
    val iteration = 1.toLong()
    val margin = 8

    val working = (start/iteration until end/iteration).toList().parallelStream().filter { attempt->
        val out: MutableList<Long> = mutableListOf()
        var A = attempt * iteration
        var B = BS.toLong()
        var C = CS.toLong()
        var IC = 0
        var runtime = 0

        while(IC >= 0 && IC < program.size && runtime < 200) {
            val opcode = program[IC]
            val literal = program[IC + 1]
            val combo = when(literal) {
                0 -> 0
                1 -> 1
                2 -> 2
                3 -> 3
                4 -> A
                5 -> B
                6 -> C
                else -> 0
            }

            IC += 2
            runtime++
            when(opcode) {
                0 -> A = (A / 2.0.pow(combo.toDouble())).toLong()   //adv
                1 -> B = B xor literal.toLong()         //bxl
                2 -> B = combo % 8                      //bst
                3 -> if(A != 0.toLong()) IC = literal   //jnz
                4 -> B = B xor C                        //bxc
                5 -> out.add(combo % 8)                 //out
                6 -> B = (A / 2.0.pow(combo.toDouble())).toLong()   //bdv
                7 -> C = (A / 2.0.pow(combo.toDouble())).toLong()   //cdv
            }
            //println("${instructions[opcode]}: A=$A, B=$B, C=$C, IC=$IC, out=${out.joinToString(",")}")
        }

        println("${attempt * iteration}: ${out.joinToString(",")} ${out.size}")
        out.size == program.size && out.drop(8) == programL.drop(8)
        //out.size == program.size && out.dropLast(8) == programL.dropLast(8)
    }
    println()
    val final = working.toList()
    println(final)
    if(final.size > 0) {
        println(final.size)
        println(final.min() * iteration)
        println(final.max() * iteration)
    }
    println("${program.joinToString(",")} ${program.size}")

    val endTime = timeSource.markNow()
    println(endTime-startTime)
}