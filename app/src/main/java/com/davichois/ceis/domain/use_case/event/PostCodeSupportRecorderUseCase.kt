package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.domain.repository.EventRepository
import javax.inject.Inject

class PostCodeSupportRecorderUseCase @Inject constructor(
    private val repository: EventRepository,
) {

    suspend operator fun invoke(codeAssistance: String): Resource<AttendanceRegisterDTO> {
        return repository.postCodeSupportRecorder(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", codeAssistance = codeAssistance)
    }

}