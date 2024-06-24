package eu.dkgl.departurealarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eu.dkgl.departurealarm.AppDatabase
import eu.dkgl.departurealarm.dao.PlannedDepartureDao
import eu.dkgl.departurealarm.entity.PlannedDeparture
import eu.dkgl.departurealarm.repository.PlannedDepartureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlannedDepartureViewModel(application: Application): AndroidViewModel(application) {
    private val plannedDepartureDao: PlannedDepartureDao =
        AppDatabase.getDatabase(application).plannedDepartureDao()
    private val plannedDepartureRepository = PlannedDepartureRepository(plannedDepartureDao)

    val allDepartures: LiveData<List<PlannedDeparture>> = plannedDepartureRepository.all

    fun insert(departure: PlannedDeparture) = viewModelScope.launch(Dispatchers.IO) {
        plannedDepartureRepository.insert(departure)
    }

    fun delete(departure: PlannedDeparture) = viewModelScope.launch(Dispatchers.IO) {
        plannedDepartureRepository.delete(departure)
    }
}