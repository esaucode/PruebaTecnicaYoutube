package com.esaudev.pruebatecnicayoutube.domain.usecase

import com.esaudev.pruebatecnicayoutube.domain.model.RandomUser
import com.esaudev.pruebatecnicayoutube.domain.repository.RandomUserRepository
import com.esaudev.pruebatecnicayoutube.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomUserUseCase @Inject constructor(
    private val repository: RandomUserRepository
) {

    suspend operator fun invoke(): Resource<RandomUser>{
        return repository.fetchRandomUser()
    }

}