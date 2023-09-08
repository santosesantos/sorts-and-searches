package sorts

class MergeSort {
    companion object Sorter {
        private fun merge(arr: Array<Int>, start: Int, middle: Int, final: Int) {
            val left = arr.copyOfRange(start, middle)
            val right = arr.copyOfRange(middle, final)

            var leftIndex = 0
            var rightIndex = 0

            for (i in start until final) {
                if (leftIndex == left.size) {
                    arr[i] = right[rightIndex]
                    rightIndex++
                    continue
                }

                if (rightIndex == right.size) {
                    arr[i] = left[leftIndex]
                    leftIndex++
                    continue
                }

                if (left[leftIndex] < right[rightIndex]) {
                    arr[i] = left[leftIndex]
                    leftIndex++
                } else {
                    arr[i] = right[rightIndex]
                    rightIndex++
                }
            }
        }

        fun mergeSort(arr: Array<Int>, start: Int, final: Int) {
            if (final - start > 1) {
                val middle = (final + start) / 2
                mergeSort(arr, start, middle)
                mergeSort(arr, middle, final)
                merge(arr, start, middle, final)
            }
        }
    }
}

fun main() {
    val elements: Array<Int> = arrayOf(5, 69, 70, 14, 42, 6, 9, 10, 1, 50)

    MergeSort.mergeSort(elements, 0, elements.size)

    print("[ ")
    elements.forEach { print("$it ") }
    print("]")
}