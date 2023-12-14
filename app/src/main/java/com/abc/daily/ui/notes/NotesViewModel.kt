package com.abc.daily.ui.notes

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

    val notesListLiveData = MutableLiveData<List<Note>>()
    val weatherLiveData = MutableLiveData<CurrentWeather>()
    var orderNotes: Pair<Int, Int> = OrderDialog.ORDER_DSC to OrderDialog.ORDER_BY_DATE
    var defaultCity: String? = null

    init {
        getOrderPrefs()
        gettingIfHasDefaultCity()
    }

    fun setOrderPrefs(order: Pair<Int, Int>) {
        val orderType: String = if (order.first == OrderDialog.ORDER_DSC) Constants.NOTES_ORDER_DSC else Constants.NOTES_ORDER_ASC
        val orderBy: String = when (order.second) {
            OrderDialog.ORDER_BY_DATE -> Constants.NOTES_ORDER_DATE
            OrderDialog.ORDER_BY_NAME -> Constants.NOTES_ORDER_NAME
            else -> Constants.NOTES_ORDER_DATE
        }
        viewModelScope.launch {
            appPrefsDataStoreDomain.orderNotesPrefsDataStore.invoke(orderType, orderBy)
        }
        getOrderPrefs()
    }

    fun getOrderPrefs() {
        var orderType: Int
        var orderBy: Int
        viewModelScope.launch {
            appPrefsDataStoreDomain.orderNotesPrefsDataStore.invoke().onEach { orderSet ->
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

    fun orderNotes(chars: String = "") {
        val orderNotesType: OrderType = if (orderNotes.first == OrderDialog.ORDER_DSC) OrderType.Descending else OrderType.Ascending
        when (orderNotes.second) {
            OrderDialog.ORDER_BY_DATE -> getNotes(NoteOrder.ByDate(orderNotesType), chars)
            OrderDialog.ORDER_BY_NAME -> getNotes(NoteOrder.ByName(orderNotesType), chars)
        }
    }

    fun getNotes(order: NoteOrder, chars: String) {
        notesDomain.getNotesList(order, chars).onEach {
            notesListLiveData.value = it
        }.launchIn(viewModelScope)
    }


    fun fillWeatherParams(city: String?, location: Pair<String, String>?) {
        val params: HashMap<String, String> = HashMap()
        params["appid"] = Constants.API_KEY
        if (defaultCity.isNullOrBlank().not() || city.isNullOrBlank().not()) {
            defaultCity?.let {
                params.put("q", it)
            }
            city?.let {
                saveDefaultCity(city)
                params.put("q", city)
            }
        } else {
            location?.let {
                params.put("lat", location.first)
                params.put("lon", location.second)
            }
        }
        requestCurrentWeatherFromApi(params)
    }

    private fun gettingIfHasDefaultCity() {
        viewModelScope.launch {
            appPrefsDataStoreDomain.defaultCityPrefsDataStore.invoke().collect { defaultCity = it }
        }
    }

    private fun requestCurrentWeatherFromApi(params: HashMap<String, String>) {
        viewModelScope.launch {
            weatherDomain.getWeather.invoke(params).flowOn(Dispatchers.IO).collect {
                weatherLiveData.postValue(it.body())
            }
        }
    }

    private fun saveDefaultCity(city: String) {
        viewModelScope.launch {
            appPrefsDataStoreDomain.defaultCityPrefsDataStore.invoke(city)
        }
    }

}