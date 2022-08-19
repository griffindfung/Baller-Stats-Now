package com.griffindfung.ballerstatsnow.ui.gamedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.griffindfung.ballerstatsnow.network.BallDontLieRepository
import com.griffindfung.ballerstatsnow.network.BallDontLieService
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Viewmodel used to fetch specific game details by id. Handles state of request to be shown in activity.
class GameDetailsViewModel (private val ballDontLieRepository: BallDontLieRepository) : ViewModel() {

    private val _gameDetails = MutableLiveData<Game>()
    val gameDetails: LiveData<Game> get() = _gameDetails
    private val _isNoResults = MutableLiveData<Boolean>()
    val isNoResults: LiveData<Boolean> get() = _isNoResults
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Get game details based on game "id" and update status livedata as request is being processed
    fun fetchGameDetails(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val gameDetailsResponse =
                withContext(Dispatchers.Default) {
                    _isNoResults.postValue(false)
                    try {
                        ballDontLieRepository.getGameById(id)
                    } catch (exception: Exception) {
                        _errorMessage.postValue(exception.localizedMessage)
                        null
                    }
                }
            if (gameDetailsResponse != null) {
                _isNoResults.postValue(false)
                _gameDetails.postValue(gameDetailsResponse!!)
            } else {
                _isNoResults.postValue(true)
            }
        }
    }

    class GameDetailsViewModelFactory(
        private val ballDontLieService: BallDontLieService
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = GameDetailsViewModel(
            BallDontLieRepository(ballDontLieService)
        ) as T
    }
}