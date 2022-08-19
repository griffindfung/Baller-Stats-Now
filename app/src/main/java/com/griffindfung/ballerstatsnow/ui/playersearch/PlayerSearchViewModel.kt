package com.griffindfung.ballerstatsnow.ui.playersearch

import androidx.lifecycle.*
import com.griffindfung.ballerstatsnow.network.BallDontLieRepository
import com.griffindfung.ballerstatsnow.network.BallDontLieService
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.PlayerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
Handles sending the search request received by PlayerSearchFragment. Stores player list and state of
request to be displayed in fragment.
 */
class PlayerSearchViewModel (private val ballDontLieRepository: BallDontLieRepository) : ViewModel()  {

    val playersSearchResponse = MediatorLiveData<PlayerResponse>()
    private val _searchQuery = MutableLiveData<String>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isNoResults = MutableLiveData<Boolean>()
    val isNoResults: LiveData<Boolean> get() = _isNoResults
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        playersSearchResponse.addSource(_searchQuery) {

            // When new _searchQuery is posted, submit query to get list of players matching criteria
            fun fetchPlayerSearchResults(searchQuery: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    val fetchedSearchResponse =
                        withContext(Dispatchers.Default) {
                            _isLoading.postValue(true)
                            try {
                                ballDontLieRepository.getPlayersByName(searchQuery)
                            } catch (exception: Exception) {
                                _errorMessage.postValue(exception.localizedMessage)
                                null
                            }

                        }
                    _isLoading.postValue(false)

                    // Display returned player by updating playersSearchResponse and update view statuses
                    if (fetchedSearchResponse != null) {
                        playersSearchResponse.postValue(fetchedSearchResponse!!)
                        _isNoResults.postValue(fetchedSearchResponse!!.data.isEmpty())
                    } else {
                        _isNoResults.postValue(true)
                    }
                }
            }
            fetchPlayerSearchResults(it)
        }
    }

    fun setSearchQuery(searchQuery: String) {
        _searchQuery.postValue(searchQuery)
    }

    class PlayerSearchViewModelFactory(
        private val ballDontLieService: BallDontLieService
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = PlayerSearchViewModel(
            BallDontLieRepository(ballDontLieService)
        ) as T
    }
}