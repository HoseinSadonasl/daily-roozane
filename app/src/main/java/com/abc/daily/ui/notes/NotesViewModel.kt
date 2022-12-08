package com.abc.daily.ui.notes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.model.weather.CurrentWeather
import com.abc.daily.domain.use_case.NotesDomain
import com.abc.daily.domain.use_case.WeatherDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesDomain: NotesDomain,
    private val weatherDomain: WeatherDomain
) : ViewModel() {

    val notesList = MutableLiveData<List<Note>>()

    val weather = MutableLiveData<CurrentWeather>()

    init {
        getNotes()
        getWeather("tehran")
    }

    fun getNotes() = notesDomain.getNotesList()
        .onEach {
            notesList.value = it
        }.launchIn(viewModelScope)


    fun getWeather(location: String) {
        viewModelScope.launch {
            weatherDomain.getWeather(location).flowOn(Dispatchers.IO).collect {
                weather.postValue(it.body())
                Log.e(::getWeather.name, "getWeather: ${it.body()}")
            }
        }
    }


}