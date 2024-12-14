package Event5_AOC

import java.io.File
import kotlin.math.abs
import kotlin.math.round

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    var offset = 0.toLong()

    //Part 2 offset
    if(true)
        offset = 10000000000000

    val AP = lines.filterIndexed { index, s -> index % 4 == 0 }.map { it.split("+", ",") }.map { Pair(it[1].toInt(), it[3].toInt()) }
    val BP = lines.filterIndexed { index, s -> index % 4 == 1 }.map { it.split("+", ",") }.map { Pair(it[1].toInt(), it[3].toInt()) }
    val Prize = lines.filterIndexed { index, s -> index % 4 == 2 }.map { it.split("=", ",") }.map { Pair(it[1].toLong()+offset, it[3].toLong()+offset) }

    //(Target, (A, B))
    val machines = Prize.zip(AP.zip(BP))
    println(machines)

    val moves = machines.map { targets->
        //PX = AX*A + BX*B
        //PY = AY*A + BY*B
        val PX = targets.first.first
        val PY = targets.first.second
        val matrix = listOf(
            listOf(targets.second.first.first, targets.second.second.first),
            listOf(targets.second.first.second, targets.second.second.second),
        )

        //Calculate inverse matrix
        val det = matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0]
        val det1 = 1.0/det
        val inverse = listOf(
            listOf(det1*matrix[1][1], -det1*matrix[0][1]),
            listOf(-det1*matrix[1][0], det1*matrix[0][0])
        )

        //Debug / check results
        val identity = listOf(
            listOf(matrix[0][0]*inverse[0][0]+matrix[0][1]*inverse[1][0], matrix[0][0]*inverse[0][1]+matrix[0][1]*inverse[1][1]),
            listOf(matrix[1][0]*inverse[0][0]+matrix[1][1]*inverse[1][0], matrix[1][0]*inverse[0][1]+matrix[1][1]*inverse[1][1]),
        )
        //println(matrix)
        //println(inverse)
        //println(identity)

        //Get equations solution
        val A = round(PX*inverse[0][0] + PY*inverse[0][1]).toLong()
        val B = round(PX*inverse[1][0] + PY*inverse[1][1]).toLong()

        //Test if correct solution
        if(PX == matrix[0][0]*A + matrix[0][1]*B && PY == matrix[1][0]*A + matrix[1][1]*B)
            Pair(A,B)
        else
            null
    }
    println(moves)

    val tokens = moves.filterNotNull().map { it.first*3 + it.second }
    println(tokens)
    println(tokens.sum())
}