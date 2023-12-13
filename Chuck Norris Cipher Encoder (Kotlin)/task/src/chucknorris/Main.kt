package chucknorris

class ChuckNorris {

    fun beChuckNorris() {
        println("Please input operation (encode/decode/exit):")
        var action = readln()
        while (action != "exit") {
            when (action) {
                "encode" -> encodeStringToChuck()
                "decode" -> decodeChuckToString()
                else -> println("There is no '$action' operation\n")
            }
            println("Please input operation (encode/decode/exit):")
            action = readln()
        }

        println("Bye")
    }

    /**
     * Encode the message given by the user :
     * Read the user input (It needs to be a string like this : `CC`)
     * Transform the input into a binary string (`CC` => "10000111000011")
     * Call `encodeMessage` with the binary string as the parameter
     */
    private fun encodeStringToChuck() {
        println("Input string:")
        val input = readln()
        var inputBinary = ""
        input.forEach { ch ->
            inputBinary += String.format("%07d", Integer.toBinaryString(ch.code).toInt())
        }

        println("Encoded string:")
        val encodedMessage = encodeMessage(inputBinary)
        println(encodedMessage + "\n")
    }

    /**
     * Encode the message given by the user by using the Chuck Norris technique
     * The technique is as follows : The message is composed 0 or 1,
     * If you have a succession of 0 :
     * create a block like this : 00 __ (replace __ with the number of consecutive 0s)
     *
     * If you have a succession of 1 :
     * create a block like this : 0 __ (replace __ with the number of consecutive 1s )
     *
     * Example : 1011 = 0 0 00 0 0 00
     * @param message : The message to encode
     *
     * @return `encodedMessage`: A string containing the encoded message
     */
    private fun encodeMessage(message: String): String {
        var currNumber = message[0]
        var encodedMessage = if (currNumber == '0') "00 0" else "0 0"

        /*
        Use of index var to go through each character of the message
        (Starting with the second character)
        */
        var messageIndex = 1

        // Looping through the message until we reach the end
        while (messageIndex < message.length) {

            // Test to check if the current char(0 or 1) is the same as the one before
            if (message[messageIndex] == currNumber) {
                encodedMessage += 0
            } else {
                encodedMessage += if (message[messageIndex] == '0') " 00 0" else " 0 0"
                currNumber = message[messageIndex]
            }
            messageIndex++
        }
        return encodedMessage
    }

    /**
     * Decode a message encoded with the Chuck Norris technique :
     * Read the user input (It needs to be like this : `0 000 00 0 0 00 00 0000`),
     * Separate the string with spaces and the sub-strings in a list
     * Call the function `decodeMessage` with the list as the parameter
     */
    private fun decodeChuckToString() {
        println("Input encoded string:")
        val input = readln().split(" ")
        val listBlock = mutableListOf<Pair<String, String>>()
        for (i in 0 .. input.lastIndex  step 2) {
            listBlock.add(Pair(input[i], if (i + 1 > input.lastIndex) "" else input[i + 1]))
        }
        val decodedMessage = checkValidityMessage(listBlock)
        if (decodedMessage != "error") {
            println("Decoded string:")
            println(decodedMessage + "\n")
        }
    }

    /**
     * Check that the encoded message is valid
     * @param list : List of every blocks that compose the message
     *
     * @return "error" if the message is not valid. Otherwise, the decoded message
     */
    private fun checkValidityMessage(list: List<Pair<String, String>>): String {
        val firstBlockCheck = checkFirstBlock(list.map { it.first})
        val charactersCheck =  checkCharacters(list)
        val decodedMessageSizeCheck = decodeMessage(list)

        when {
            firstBlockCheck || decodedMessageSizeCheck == "error" ||
                    charactersCheck -> {
                println("Encoded string is not valid\n")
                return "error"
            }
            else -> return decodedMessageSizeCheck
        }
    }

    /**
     * Check the first part of every block is `0` or `00`
     * @param list : List of the blocks first part that compose the encoded message
     *
     * @return true if there is an error, false if everything is good
     */
    private fun checkFirstBlock(list: List<String>): Boolean {
        list.forEach {
            if (it != "0" && it != "00") return true
        }
        return false
    }

    /**
     * Check that the encoded message only has non-empty pairs of 0
     * @param list : List of the blocks that compose the encoded message
     *
     * @return true if there is a wrong character or an empty pair, false if everything is good
     */
    private fun checkCharacters(list: List<Pair<String, String>>): Boolean {
        list.forEach { block ->
            if (block.second.isEmpty()) return true
            block.first.forEach {
                if (it != '0') return true
            }
            block.second.forEach {
                if (it != '0') return true

            }
        }
        return false
    }

    /**
     * Decode a message encoded with the Chuck Norris technique
     * @param list : A list of pair of string :
     * First string is always `0` or `00`
     * Second string is at least `0` but can have more 0
     *
     * @return `decodedMessage` : The decoded message
     */
    private fun decodeMessage(list: List<Pair<String, String>>): String{
        var message = ""
        var decodedMessage = ""
        list.forEach{block ->
            val symbol = if (block.first == "0") "1" else "0"
            message += symbol.repeat(block.second.length)

        }

        if (message.length % 7 != 0) return "error"
        while (message !=  "") {
            decodedMessage += message.substring(0,7).toInt(2).toChar()
            message = message.substring(7)
        }

        return decodedMessage
    }
}

fun main() {
    val chuck = ChuckNorris()
    chuck.beChuckNorris()
}