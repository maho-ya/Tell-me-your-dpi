package com.maho_ya.ui.home

import androidx.lifecycle.*
import com.maho_ya.domain.device.DeviceUseCase
import com.maho_ya.model.Device
import com.maho_ya.result.data
import kotlinx.coroutines.launch

class HomeVieModel(
    private val deviceUseCase: DeviceUseCase
) : ViewModel() {

    private val _device = MutableLiveData<Device>()
    val device: LiveData<Device>
        get() = _device

    init {
        viewModelScope.launch {
            _device.value = deviceUseCase().data
        }
    }
}

class HomeViewModelFactory(
    private val deviceUseCase: DeviceUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeVieModel(deviceUseCase) as T
    }
}