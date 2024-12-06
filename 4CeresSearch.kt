package Event5_AOC

import java.io.File

//Get a column
fun List<String>.columns() = this[0].indices.map { x ->
    indices.map { y ->
        this[y][x]
    }.joinToString("")
}

//Get a diagonal starting from side
fun List<String>.diagonals1() = (-this[0].length+1 until this.size).map { y ->
    this[0].indices.map { x ->
        if(y + x >= 0 && y + x < this.size)
            this[y + x][x]
        else
            '.'
    }.joinToString("")
}
fun List<String>.diagonals2() = (0 until this.size + this[0].length-1).map { y ->
    this[0].indices.map { x ->
        if(y - x >= 0 && y - x < this.size)
            this[y - x][x]
        else
            '.'
    }.joinToString("")
}

val regex1 = "XMAS".toRegex()
val regex2 = "SAMX".toRegex()
fun String.countSearch(): Int {
    println(this)
    return regex1.findAll(this).count() + regex2.findAll(this).count()
}

fun main1(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    //Check lines
    val horizontal = lines.sumOf { it.countSearch() }
    println(horizontal)

    //Check columns
    val vertical = lines.columns().sumOf { it.countSearch() }
    println(vertical)

    //Check diagonals
    val diagonals1 = lines.diagonals1().sumOf { it.countSearch() }
    println(diagonals1)
    val diagonals2 = lines.diagonals2().sumOf { it.countSearch() }
    println(diagonals2)

    println(horizontal+vertical+diagonals1+diagonals2)
}

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val regex3 = "MAS".toRegex()
    val regex4 = "SAM".toRegex()

    //Find matching A coordinates
    val diagonals1 = lines.diagonals1().onEach { println(it) }.flatMapIndexed { index, s ->
        (regex3.findAll(s)+regex4.findAll(s)).map {
            Pair((-lines[0].length+1 until lines.size).toList()[index]+it.range.first+1, it.range.first+1)
        }
    }
    diagonals1.forEach { println(it) }
    val diagonals2 = lines.diagonals2().onEach { println(it) }.flatMapIndexed { index, s ->
        (regex3.findAll(s)+regex4.findAll(s)).map {
            Pair((0 until lines.size + lines[0].length-1).toList()[index]-it.range.first-1, it.range.first+1)
        }
    }
    diagonals2.forEach { println(it) }

    println(diagonals1.count { diagonals2.contains(it) })
}