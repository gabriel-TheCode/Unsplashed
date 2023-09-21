package com.gabrielthecode.unsplashed.core.usecases

import com.gabrielthecode.unsplashed.core.repositories.MainRepository
import javax.inject.Inject

class ResetAppPreferences @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke() {
        repository.clearAppData()
    }
}
