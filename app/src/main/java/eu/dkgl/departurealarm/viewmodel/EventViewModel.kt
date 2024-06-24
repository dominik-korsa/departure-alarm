package eu.dkgl.departurealarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eu.dkgl.departurealarm.AlarmManager
import eu.dkgl.departurealarm.AppDatabase
import eu.dkgl.departurealarm.dao.EventDao
import eu.dkgl.departurealarm.entity.Event
import eu.dkgl.departurealarm.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel(application: Application): AndroidViewModel(application) {
    private val eventDao: EventDao =
        AppDatabase.getDatabase(application).eventDao()
    private val eventRepository = EventRepository(eventDao, AlarmManager(application))

    val allDepartures: LiveData<List<Event>> = eventRepository.all

    fun insert(departure: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.insert(departure)
    }

    fun delete(departure: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.delete(departure)
    }
}