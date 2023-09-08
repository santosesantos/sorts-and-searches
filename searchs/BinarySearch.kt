package searchs

import sorts.MergeSort

fun searcher(arr: Array<Int>, target: Int, start: Int, final: Int): Int {
    val middle = (start + final) / 2

    if (middle == start)
        return -1

    return if (target == arr[middle]) {
        middle
    } else if (target < arr[middle]) {
        searcher(arr, target, start, middle)
    } else {
        searcher(arr, target, middle, final)
    }
}

fun main() {
    val elements: Array<Int> = arrayOf(5, 8, 7, 1, 3, 6, 4)
    MergeSort.mergeSort(elements, 0, elements.size)

    print("[ ")
    elements.forEach { print("$it ") }
    print("]\n")

    // Searching
    val target = 7
    val index = searcher(elements, target, 0, (elements.size - 1))
    println("Index: $index")
    if (index > 0) println("Value: ${elements[index]}")
}