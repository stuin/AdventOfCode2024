package Event5_AOC

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val sections = (0..lines.size / 8).map {
        lines.dropLast(lines.size-(it*8+7)).drop(it*8)
    }
    println(sections)

    val locks = sections.filter { it[0] == "#####" && it[6] == "....." }.map { lock->
        (0 until 5).map { col->
            lock.map { it[col] }.filter { it=='#' }.size-1
        }
    }
    println(locks)

    val keys = sections.filter { it[6] == "#####" && it[0] == "....." }.map { lock->
        (0 until 5).map { col->
            lock.map { it[col] }.filter { it=='#' }.size-1
        }
    }
    println(keys)

    val fitting = locks.map { lock->
        keys.count { key->
            key.indices.none { lock[it]+key[it] > 5 }
        }
    }
    println(fitting.sum())
}