package com.maho_ya.domain.device

import com.maho_ya.data.device.DeviceRepository
import com.maho_ya.domain.UseCase
import com.maho_ya.model.Device
import javax.inject.Inject

class DeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : UseCase<Device>() {

    override suspend fun execute(): Device = deviceRepository
        .getDevice()
}
