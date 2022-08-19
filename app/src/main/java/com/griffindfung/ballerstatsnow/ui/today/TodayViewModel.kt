package com.griffindfung.ballerstatsnow.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.griffindfung.ballerstatsnow.network.BallDontLieRepository
import com.griffindfung.ballerstatsnow.network.BallDontLieService
import com.griffindfung.ballerstatsnow.helpers.DateHelper
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.GameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
Gets today's date and sends a request for games occurring today. Viewmodel stores data for games today
and state progress of request.
 */
class TodayViewModel (private val ballDontLieRepository: BallDontLieRepository) : ViewModel() {

//    val gameResponse: MutableLiveData<GameResponse> by lazy {
//        MutableLiveData<GameResponse>()
//    }

    private val _gameResponse = MutableLiveData<GameResponse>()
    val gameResponse: LiveData<GameResponse> get() = _gameResponse
    private val _isNoResults = MutableLiveData<Boolean>()
    val isNoResults: LiveData<Boolean> get() = _isNoResults
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    val errorMessage = MutableLiveData<String>()

    // Get all games happening today and update views according to returned response
    fun fetchGamesToday() {
        _isLoading.postValue(true)
        _isNoResults.postValue(false)
        CoroutineScope(Dispatchers.IO).launch {
            val gameSearchResponse =
                withContext(Dispatchers.Default) {
                    val todaysDate = DateHelper.getCurrentDateFormattedString()
                    try {
                        ballDontLieRepository.getGamesByStartEndDate(todaysDate, todaysDate)
                    } catch (exception: Exception) {
                        errorMessage.postValue(exception.localizedMessage)
                        null
                    }
                }
            _isLoading.postValue(false)
            if (gameSearchResponse != null) {
                _gameResponse.postValue(gameSearchResponse!!)
                _isNoResults.postValue(gameSearchResponse.gameData.isEmpty())
            } else {
                _isNoResults.postValue(true)
            }
        }
    }

    class MainViewModelFactory(
        private val ballDontLieService: BallDontLieService
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = TodayViewModel(
            BallDontLieRepository(ballDontLieService)
        ) as T
    }
}