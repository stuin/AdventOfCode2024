package Event5_AOC

import java.io.File
import kotlin.math.abs

fun andGate(first: Boolean?, second: Boolean?): Boolean = (first ?: false) && (second ?: false)
fun orGate(first: Boolean?, second: Boolean?): Boolean = (first ?: false) || (second ?: false)
fun xorGate(first: Boolean?, second: Boolean?): Boolean = (first ?: false) xor (second ?: false)
fun nullGate(first: Boolean?, second: Boolean?): Boolean = first ?: false

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val start = lines.dropLast(lines.size-lines.indexOfFirst { it == "" }).map { it.split(": ") }
    //println(start)

    val gatesIn = lines.drop(lines.indexOfFirst { it == "" }+1).map { it.split(" ") }
    //println(gatesIn)

    val wires = start.map { Pair(it[0], if(it[1][0] == '1') true else false) }.toMap().toMutableMap()
    println(wires)

    var gates = gatesIn.mapIndexed { index,gate->
        Pair(
            listOf(gate[0], gate[2], index.toString()),
            Pair(
                when(gate[1]) {
                    "AND"-> ::andGate
                    "OR"-> ::orGate
                    "XOR"-> ::xorGate
                    else-> ::nullGate
                },
                gate[4]
            )
        )
    }.toMap()
    println(gates)
    println(gates.values.map { it.first.name }.toSet())
    println(gates.size)
    println(gatesIn.size)

    val goalWires = gates.map { it.value.second }.filter { it[0] == 'z' }.sortedDescending()
    println(goalWires)

    while(!wires.keys.containsAll(goalWires)) {
        val finished = listOf<List<String>>().toMutableSet()
        gates.forEach { gate->
            if(wires.containsKey(gate.key[0]) && wires.containsKey(gate.key[1]) && !wires.containsKey(gate.value.second)) {
                wires[gate.value.second] = gate.value.first(wires[gate.key[0]], wires[gate.key[1]])
                finished.add(gate.key)
            }
        }
        if(finished.isEmpty()) {
            println("No solutions found")
            return
        }
        gates = gates.filter { !finished.contains(it.key) }
        println(gates.size)
        println(wires)
    }

    val output = goalWires.map { if(wires[it] == true) '1' else '0' }.joinToString("")
    println(output)
    println(output.toLong(2))
}