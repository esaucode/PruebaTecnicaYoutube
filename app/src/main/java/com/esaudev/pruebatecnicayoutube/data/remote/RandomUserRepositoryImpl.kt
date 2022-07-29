package com.esaudev.pruebatecnicayoutube.data.remote

import com.esaudev.pruebatecnicayoutube.data.mappers.toDomain
import com.esaudev.pruebatecnicayoutube.domain.model.RandomUser
import com.esaudev.pruebatecnicayoutube.domain.repository.RandomUserRepository
import com.esaudev.pruebatecnicayoutube.domain.util.Resource
import javax.inject.Inject

class RandomUserRepositoryImpl @Inject constructor(
    private val randomUserApi: RandomUserApi
) : RandomUserRepository {

    override suspend fun fetchRandomUser(): Resource<RandomUser> {
        return try {
            Resource.Success(
                data = randomUserApi.fetchRandomUser().results.first().toDomain()
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Unknown error"
            )
        }
    }

}