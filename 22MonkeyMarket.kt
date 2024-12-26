package Event5_AOC

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val start = lines.map { it.toLong() }

    fun mixPrune(current: Long, value: Long): Long {
        return (current xor value) % 16777216
    }

    fun next(current: Long): Long {
        val s1 = mixPrune(current, current * 64)
        val s2 = mixPrune(s1, s1/32)
        val s3 = mixPrune(s2, s2*2048)
        return s3
    }

    //Get all secrets for each buyer
    val all = start.map { secret->
        var current = secret
        (0 until 2000).map { step ->
            current = next(current)
            current
        }
    }

    //Part 1 answer
    println(all.sumOf { it.last() })

    //Convert secrets to prices
    val prices = all.map { buyer-> buyer.map { it.toString().last().digitToInt() } }
    println(prices[0])

    //Match prices with sequences of four changes
    val allSeq = prices.parallelStream().map { buyer->
        (4 until buyer.size).map {
            Pair(buyer[it],listOf(
                buyer[it-3] - buyer[it-4],
                buyer[it-2] - buyer[it-3],
                buyer[it-1] - buyer[it-2],
                buyer[it] - buyer[it-1]
            ))
        }
    }

    //Only first instance of each sequence per buyer
    val firstSeq = allSeq.map { buyer-> buyer.filterIndexed { index, pair -> index == buyer.indexOfFirst { pair.second == it.second } } }.toList()
    println(firstSeq.count())

    //Get all sequences that result in a 9 for at least 1 buyer
    val bestSeq = firstSeq.flatMap { buyer-> buyer.filter { it.first == 9 }.map { it.second } }.toSet()
    println(bestSeq.size)

    //Try all best sequences
    val fullRun = bestSeq.parallelStream().map { seq->
        val sell = firstSeq.mapNotNull { buyer -> buyer.firstOrNull { it.second == seq } }
        //println(sell)
        sell.sumOf { it.first }
    }
    println(fullRun.toList().max())
}