package com.griffindfung.ballerstatsnow.ui.gamesearch

import androidx.lifecycle.*
import com.griffindfung.ballerstatsnow.network.BallDontLieRepository
import com.griffindfung.ballerstatsnow.network.BallDontLieService
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.GameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Viewmodel fetches data for a game via date. Handles state of request to be shown in GameSearchFragment.
class GameSearchViewModel (private val ballDontLieRepository: BallDontLieRepository) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isNoResults = MutableLiveData<Boolean>()
    val isNoResults: LiveData<Boolean> get() = _isNoResults
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val gamesSearchResponse = MediatorLiveData<GameResponse>()

    init {
        gamesSearchResponse.addSource(_searchQuery) {

            // When new _searchQuery is posted, fetch new list of games occurring on a specific date
            fun fetchSingleDateGameSearchResults(searchQuery: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    val fetchedSearchResponse =
                        withContext(Dispatchers.Default) {
                            _isLoading.postValue(true)
                            _isNoResults.postValue(false)
                            try {

                                // searchQuery is both start and end date to get all games on a singular day.
                                // Could alternatively use another api call accepting a list of dates instead,
                                // but plan on adding new feature in the future to include both single date search and
                                // date range search in fragment.
                                ballDontLieRepository.getGamesByStartEndDate(searchQuery, searchQuery)
                            } catch (exception: Exception) {
                                _errorMessage.postValue(exception.localizedMessage)
                                null
                            }
                        }
                    _isLoading.postValue(false)

                    // Show results in rv if valid response, otherwise show no results message
                    if (fetchedSearchResponse != null) {
                        gamesSearchResponse.postValue(fetchedSearchResponse!!)
                        _isNoResults.postValue(fetchedSearchResponse!!.gameData.isEmpty())
                    } else {
                        _isNoResults.postValue(true)
                    }
                }
            }
            fetchSingleDateGameSearchResults(it)
        }
    }

    fun setSearchQuery(searchQuery: String) {
        _searchQuery.postValue(searchQuery)
    }

    class GameSearchViewModelFactory(
        private val ballDontLieService: BallDontLieService
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = GameSearchViewModel(
            BallDontLieRepository(ballDontLieService)
        ) as T
    }
}