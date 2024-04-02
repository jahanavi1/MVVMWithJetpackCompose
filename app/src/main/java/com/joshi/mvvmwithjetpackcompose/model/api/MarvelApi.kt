package com.joshi.mvvmwithjetpackcompose.model.api

import com.joshi.mvvmwithjetpackcompose.model.CharactersApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {
    @GET("characters")
    fun getCharacters(@Query("nameStartsWith") name : String) : Call<CharactersApiResponse>
}