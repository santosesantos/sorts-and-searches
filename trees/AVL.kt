package trees

import kotlin.math.max

// Classes and interfaces
data class AVLNode(
    val value: Int,
    var left: AVLNode? = null,
    var right: AVLNode? = null,
    var balance: Int = 0
)

data class HeightIterator(
    var height: Int = 0
)

data class UpdateResult(
    val maxHeight: Int,
    val afterBalance: Boolean,
    val childNode: AVLNode?
)

data class InsertResult(
    val isInserted: Boolean,
    val childNode: AVLNode?
)

// Global variables
var root: AVLNode? = null

// Functions
// Returns false and the last node if the value wasn't found
fun search(root: AVLNode?, value: Int): Boolean {
    if (root == null)
        return false

    return if (value < root.value) {
        search(root.left, value)
    } else if (value > root.value) {
        search(root.right, value)
    } else {
        true
    }
}

// Returns if the node was inserted and the child node to update the parent
fun searchInsertUpdate(root: AVLNode?, newNode: AVLNode): InsertResult {
    if (root == null)
        return InsertResult(false, null)

    if (newNode.value < root.value) {
        val result = searchInsertUpdate(root.left, newNode)

        // Insertion
        if (result.childNode == null) {
            root.left = newNode

            // Returns that the node was inserted
            return InsertResult(true, root)
        } else {
            root.left = result.childNode

            // Maybe a verification to when it's balanced
            if (result.isInserted) {
                // Update balances and do the necessary rotations
                val updateResult = updateBalance(root)

                if (updateResult.afterBalance) {
                    return InsertResult(true, updateResult.childNode)
                }

                // Returns that the node was inserted
                return InsertResult(true, root)
            } else {
                return InsertResult(false, root)
            }
        }
    } else if (newNode.value > root.value) {
        val result = searchInsertUpdate(root.right, newNode)

        // Insertion
        if (result.childNode == null) {
            root.right = newNode

            // Returns that the node was inserted
            return InsertResult(true, root)
        } else {
            root.right = result.childNode

            // Maybe a verification to when it's balanced
            if (result.isInserted) {
                // Update balances and do the necessary rotations
                val updateResult = updateBalance(root)

                if (updateResult.afterBalance) {
                    return InsertResult(true, updateResult.childNode)
                }

                // Returns that the node was inserted
                return InsertResult(true, root)
            } else {
                return InsertResult(false, root)
            }
        }
    } else {
        return InsertResult(false, root)
    }
}

fun updateBalance(root: AVLNode?, iterator: HeightIterator = HeightIterator(-1)): UpdateResult {
    if (root == null)
        return UpdateResult(0, false, null)

    iterator.height++

    val rightIterator = HeightIterator()
    val leftIterator = HeightIterator()

    val rightResult = updateBalance(root.right, rightIterator)
    val leftResult = updateBalance(root.left, leftIterator)

    // If there was a rotation, there is no need to verify balances and heights again
    if (rightResult.afterBalance || leftResult.afterBalance) {
        root.right = rightResult.childNode
        root.left = leftResult.childNode

        val updateResult = updateBalance(root)

        return UpdateResult(updateResult.maxHeight, true, root)
    }

    val rightHeight = rightResult.maxHeight + rightIterator.height
    val leftHeight = leftResult.maxHeight + leftIterator.height

    val balance = rightHeight - leftHeight
    root.balance = balance

    val maxHeight = max(rightHeight, leftHeight)

    // Default return, without rotations
    if (balance > -2 && balance < 2)
        return UpdateResult(maxHeight, false, root)

    if (balance <= -2) {
        val leftNode = (root.left as AVLNode)

        if (leftNode.balance == -1) {
            // RSD
            root.left = leftNode.right
            leftNode.right = root

            val updateResult = updateBalance(leftNode)

            return UpdateResult(updateResult.maxHeight, true, leftNode)
        } else if (leftNode.balance == 1){
            // RDD
            val leftRightNode = (leftNode.right as AVLNode)

            leftNode.right = leftRightNode.left
            leftRightNode.left = leftNode

            root.left = leftRightNode.right
            leftRightNode.right = root

            val updateResult = updateBalance(leftRightNode)

            return UpdateResult(updateResult.maxHeight, true, leftRightNode)
        }
    } else {
        val rightNode = (root.right as AVLNode)

        if (rightNode.balance == 1) {
            // RSE
            root.right = rightNode.left
            rightNode.left = root

            val updateResult = updateBalance(rightNode)

            return UpdateResult(updateResult.maxHeight, true, rightNode)
        } else if (rightNode.balance == -1){
            // RDE
            val rightLeftNode = (rightNode.left as AVLNode)

            rightNode.left = rightLeftNode.right
            rightLeftNode.right = rightNode

            root.right = rightLeftNode.left
            rightLeftNode.left = root

            val updateResult = updateBalance(rightLeftNode)

            return UpdateResult(updateResult.maxHeight, true, rightLeftNode)
        }
    }

    return UpdateResult(maxHeight, false, root)
}

fun insertNode(value: Int): Boolean {
    val newNode = AVLNode(value, null, null)

    if (root == null) {
        root = newNode
        return true
    }

    val insertionResult = searchInsertUpdate(root, newNode)

    root = insertionResult.childNode

    return insertionResult.isInserted
}

fun printTree(root: AVLNode?) {
    if (root != null) {
        printTree(root.left)
        print(" ${root.value} ")
        printTree(root.right)
    }
}

fun printTreePreOrder(root: AVLNode?) {
    if (root != null) {
        print(" ${root.value} ")
        printTreePreOrder(root.left)
        printTreePreOrder(root.right)
    }
}

fun main() {
    println("Insertion of node: 1 => ${insertNode(1)}")
    println("Insertion of node: 2 => ${insertNode(2)}")
    println("Insertion of node: 5 => ${insertNode(5)}")
    println("Insertion of node: 3 => ${insertNode(3)}")
    println("Insertion of node: 4 => ${insertNode(4)}")
    println("Insertion of node: 6 => ${insertNode(6)}")
    println("----------")
    print("[")
    printTree(root)
    print("]")
    print("\n[")
    printTreePreOrder(root)
    print("]")
}