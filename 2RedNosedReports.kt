package Event5_AOC

import java.io.File

fun main(args: Array<String>) {
    val reader = File(args[0])
    val lines = reader.readLines()

    val reports = lines.map { it.split(" ").map { second -> second.toInt() } }

    val safe = reports.count { levels ->
        var ascending = levels.mapIndexed() { index, i ->
            if(index > 0) i > levels[index-1] && i <= levels[index-1] + 3 else true
        }.all { it }
        var descending = levels.mapIndexed { index, i ->
            if(index > 0) i < levels[index-1] && i >= levels[index-1] - 3 else true
        }.all { it }
        if (!ascending && !descending) {
            for(skip in 0 until levels.size) {
                if (!ascending && !descending) {
                    val tempLevels = levels.filterIndexed { index, _ -> index != skip }
                    ascending = tempLevels.mapIndexed() { index, i ->
                        if (index > 0) i > tempLevels[index - 1] && i <= tempLevels[index - 1] + 3 else true
                    }.all { it }
                    descending = tempLevels.mapIndexed { index, i ->
                        if (index > 0) i < tempLevels[index - 1] && i >= tempLevels[index - 1] - 3 else true
                    }.all { it }
                }
            }
        }

        ascending || descending
    }

    println(safe)
}