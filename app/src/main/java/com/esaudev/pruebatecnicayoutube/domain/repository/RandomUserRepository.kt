package com.esaudev.pruebatecnicayoutube.domain.repository

import com.esaudev.pruebatecnicayoutube.domain.model.RandomUser
import com.esaudev.pruebatecnicayoutube.domain.util.Resource

interface RandomUserRepository {

    suspend fun fetchRandomUser(): Resource<RandomUser>

}