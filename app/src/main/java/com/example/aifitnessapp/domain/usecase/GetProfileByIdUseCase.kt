package com.example.aifitnessapp.domain.usecase

import com.example.aifitnessapp.data.local.dao.ProfileDao

class GetProfileByIdUseCase(
    private val dao: ProfileDao
) {
    suspend operator fun invoke(id: Int) = dao.getProfileById(id)
}
