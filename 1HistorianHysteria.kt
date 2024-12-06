package Event5_AOC

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val left = lines.map { it.split("   ")[0].toInt() }.sorted()
    val right = lines.map { it.split("   ")[1].toInt() }.sorted()

    val difference = left.zip(right).map { abs(it.first - it.second) }

    val similarity = left.map { it * right.count { second -> second == it }}

    println(difference.sum())
    println(similarity.sum())
}