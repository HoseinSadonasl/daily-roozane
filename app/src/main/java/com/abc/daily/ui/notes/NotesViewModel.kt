package com.abc.daily.ui.notes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.model.weather.CurrentWeather
import com.abc.daily.domain.use_case.NotesDomain
import com.abc.daily.domain.use_case.PrefsDataStoreDomain
import com.abc.daily.domain.use_case.WeatherDomain
import com.abc.daily.domain.util.NoteOrder
import com.abc.daily.domain.util.OrderType
import com.abc.daily.util.Constants
import com.abc.daily.util.OrderDialog
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
    private val weatherDomain: WeatherDomain,
    private val appPrefsDataStoreDomain: PrefsDataStoreDomain
) : ViewModel() {

    val notesList = MutableLiveData<List<Note>>()
    val weather = MutableLiveData<CurrentWeather>()
    var orderNotes: Pair<Int, Int> = OrderDialog.ORDER_DSC to OrderDialog.ORDER_BY_DATE

    init {
        getOrderPrefs()
    }

    fun setOrderPrefs(order: Pair<Int, Int>) {
        val orderType: String = if (order.first == OrderDialog.ORDER_DSC) Constants.NOTES_ORDER_DSC else Constants.NOTES_ORDER_ASC
        val orderBy: String = when (order.second) {
            OrderDialog.ORDER_BY_DATE -> Constants.NOTES_ORDER_DATE
            OrderDialog.ORDER_BY_NAME -> Constants.NOTES_ORDER_NAME
            else -> Constants.NOTES_ORDER_DATE
        }
        viewModelScope.launch {
            appPrefsDataStoreDomain.appPrefsDataStore.invoke(orderType, orderBy)
        }
        getOrderPrefs()
    }

    fun getOrderPrefs() {
        var orderType: Int
        var orderBy: Int
        viewModelScope.launch {
            appPrefsDataStoreDomain.appPrefsDataStore.invoke().onEach { orderSet ->
                orderType = if (orderSet.first() == Constants.NOTES_ORDER_DSC) OrderDialog.ORDER_DSC else OrderDialog.ORDER_ASC
                orderSet.toList()[1].let { order ->
                    orderBy = when (order) {
                        Constants.NOTES_ORDER_DATE -> OrderDialog.ORDER_BY_DATE
                        Constants.NOTES_ORDER_NAME -> OrderDialog.ORDER_BY_NAME
                        else -> OrderDialog.ORDER_BY_DATE
                    }
                }
                orderNotes = orderType to orderBy
            }.launchIn(viewModelScope)
            orderNotes()
        }
    }

    fun orderNotes() {
        val orderNotesType: OrderType = if (orderNotes.first == OrderDialog.ORDER_DSC) OrderType.Descending else OrderType.Ascending
        when (orderNotes.second) {
            OrderDialog.ORDER_BY_DATE -> getNotes(NoteOrder.ByDate(orderNotesType))
            OrderDialog.ORDER_BY_NAME -> getNotes(NoteOrder.ByName(orderNotesType))
        }
    }

    fun getNotes(order: NoteOrder) {
        notesDomain.getNotesList(order).onEach {
            notesList.value = it
        }.launchIn(viewModelScope)
    }


    fun getWeather(city: String, location: Pair<String, String>) {
        viewModelScope.launch {
            weatherDomain.getWeather(city, location).flowOn(Dispatchers.IO).collect {
                weather.postValue(it.body())
                Log.e(::getWeather.name, "getWeather: ${it.body()}")
            }
        }
    }


}