package com.griffindfung.ballerstatsnow.ui.playerdetails

import androidx.lifecycle.*
import com.griffindfung.ballerstatsnow.network.BallDontLieRepository
import com.griffindfung.ballerstatsnow.network.BallDontLieService
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.Player
import com.griffindfung.ballerstatsnow.apiobjects.seasonaveragesdata.SeasonAveragesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
Viewmodel handles requesting basic data and season averages for player based on set playerId.
Contains state information for loading progress in activity.
 */
class PlayerDetailsViewModel(private val ballDontLieRepository: BallDontLieRepository) : ViewModel()  {
//    val playerDetails: MutableLiveData<Player> by lazy {
//        MutableLiveData<Player>()
//    }

    private var _id = 0
    private var _playerDetails = MutableLiveData<Player>()
    val playerDetails: LiveData<Player> get() = _playerDetails
    val seasonAveragesResponse = MediatorLiveData<SeasonAveragesResponse>()
    private val _searchQuery = MutableLiveData<Int?>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isNoResults = MutableLiveData<Boolean>()
    val isNoResults: LiveData<Boolean> get() = _isNoResults
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        seasonAveragesResponse.addSource(_searchQuery) {
            fetchSeasonAverages(it)
        }
    }

    // Call before first network req in this vm so requests know what player data to get
    fun setPlayerId(id: Int) {
        _id = id
    }

    // Call for initial population textViews
    fun initData() {
        fetchPlayerData()
        fetchSeasonAverages(null)
    }

    // User setSearyQuery for every network request after initData()
    fun setSearchQuery(searchQuery: Int?) {
        _searchQuery.postValue(searchQuery)
    }

    // Launch request to get player basic info.
    // Should only need to call this once per player since data doesn't change.
    fun fetchPlayerData() {
        CoroutineScope(Dispatchers.IO).launch {
            val playerDetailsResponse =
                withContext(Dispatchers.Default) {
                    try {
                        ballDontLieRepository.getPlayerById(_id)
                    } catch (exception: Exception) {
                        _errorMessage.postValue(exception.localizedMessage)
                        null
                    }
                }
            if (playerDetailsResponse != null) {
                _playerDetails.postValue(playerDetailsResponse!!)
            }
        }
    }

    // Launch request to get player season averages by season.
    // Passing null will default to returning season avg for current season.
    fun fetchSeasonAverages(season: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            val playerSeasonAveragesResponse =
                withContext(Dispatchers.Default) {
                    _isLoading.postValue(true)
                    try {
                        ballDontLieRepository.getSeasonAverages(season, listOf(_id))
                    } catch (exception: Exception) {
                        _errorMessage.postValue(exception.localizedMessage)
                        null
                    }
                }
            _isLoading.postValue(false)
            if (playerSeasonAveragesResponse != null) {
                seasonAveragesResponse.postValue(playerSeasonAveragesResponse!!)
                _isNoResults.postValue(playerSeasonAveragesResponse!!.data.isEmpty())
            } else {
                _isNoResults.postValue(true)
            }
        }
    }

    class PlayerDetailsViewModelFactory(
        private val ballDontLieService: BallDontLieService
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = PlayerDetailsViewModel(
            BallDontLieRepository(ballDontLieService)
        ) as T
    }
}