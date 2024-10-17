package com.example.news.data.remote.response

import java.io.Serializable

data class Source(
    val id: String?,
    val name: String?
): Serializable {
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(name.isNullOrEmpty()){
            result = 31 * result + name.hashCode()
        }
        return result
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Source

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }
}