package com.pumpkin.webCache.domain.matcher

import android.content.res.Resources
import android.net.Uri
import android.util.JsonReader
import com.pumpkin.webCache.R
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets.UTF_8

class UrlMatcher {
    private val blockList = BlockList
    private val previousMatched = HashSet<String>()
    private val previousUnmatched = HashSet<String>()

    fun isBlock(uri: Uri, resource: Resources): Boolean {
        val host = uri.host ?: return false

        if (previousUnmatched.contains(host)) {
            return false
        }

        if (previousMatched.contains(host)) {
            return true
        }

        return blockList.get(resource).findNode(host)?.let {
            previousMatched.add(host)
            true
        } ?: kotlin.run {
            previousUnmatched.add(host)
            false
        }
    }

}

private object BlockList {
    private val blocklistFile: Int = R.raw.domain_block_list

    @Volatile
    private var blockTrie: Trie? = null

    fun get(resource: Resources): Trie {
        val localTrie = blockTrie
        if (localTrie != null) {
            return localTrie
        }
        return synchronized(this) {
            var trie = blockTrie
            if (trie == null) {
                trie = try {
                    createTrie(resource)
                } catch (ignore: Exception) {
                    null
                } ?: Trie.createRootNode()
                blockTrie = trie
            }
            trie
        }
    }

    private fun createTrie(resource: Resources): Trie {
        val reader = InputStreamReader(resource.openRawResource(blocklistFile), UTF_8)
        return JsonReader(reader).use {
            createTrie(it)
        }
    }

    private fun createTrie(jsonReader: JsonReader): Trie {
        val rootNode = Trie.createRootNode()
        jsonReader.beginArray()
        while (jsonReader.hasNext()) {
            val str = jsonReader.nextString()
            rootNode.put(str)
        }
        jsonReader.endArray()
        return rootNode
    }
}