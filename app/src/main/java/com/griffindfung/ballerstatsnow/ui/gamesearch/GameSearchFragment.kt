package com.griffindfung.ballerstatsnow.ui.gamesearch

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.griffindfung.ballerstatsnow.ui.adapters.GamesRecyclerViewAdapter
import com.griffindfung.ballerstatsnow.R
import com.griffindfung.ballerstatsnow.network.RetrofitBuilder
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.GameResponse
import com.griffindfung.ballerstatsnow.databinding.FragmentGameSearchBinding
import com.griffindfung.ballerstatsnow.helpers.StringFormatter
import java.util.*

/*
Fragment used to search for games via dates. Search results can be clicked to access more details
about the game. Fragment currently supports single date search but plan to support date range search
in the future.
 */
class GameSearchFragment : Fragment() {

    companion object {
        fun newInstance() = GameSearchFragment()
    }
    private var _binding: FragmentGameSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameSearchViewModel
    private var gameAdapter: GamesRecyclerViewAdapter = GamesRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val ballDontLieService = RetrofitBuilder.getInstance()
        viewModel = ViewModelProvider(this,
            GameSearchViewModel.GameSearchViewModelFactory(ballDontLieService)
        ).get(GameSearchViewModel::class.java)
        setupRecyclerView()
        setupObservers()
    }

    fun setupToolbar() {
        binding.apply {

            // Create search menu item in toolbar that opens a calendar date picker.
            // When a date is selected, all games occurring on that day are requested.
            tbGameSearch.inflateMenu(R.menu.game_search_menu)
            tbGameSearch.menu.findItem(R.id.action_search).setOnMenuItemClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    tbGameSearch.context,
                    { view, year, month, day ->

                        // month + 1 because indexing starts at 0
                        val date = StringFormatter.formatDateToStringQuery(year, month + 1, day)
                        viewModel.setSearchQuery(date)
                    },
                    year, month, day
                )
                datePickerDialog.show()
                true
            }
        }
    }

    fun setupObservers() {

        // Show all games returned for selected date
        val gameResponseObserver = Observer<GameResponse> {
            gameAdapter.setGames(it.gameData)
        }

        // Show loading in progress when requesting for games
        val loadingObserver = Observer<Boolean> {
            binding.pbSearchLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        // Show no results if no games returned
        val noResultsObserver = Observer<Boolean> { isNoResults ->
            binding.tvNoGamesFound.visibility = if (isNoResults) View.VISIBLE else View.INVISIBLE
        }

        // Show error if any network request error
        val errorObserver = Observer<String> {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.gamesSearchResponse.observe(viewLifecycleOwner, gameResponseObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.isNoResults.observe(viewLifecycleOwner, noResultsObserver)
        viewModel.errorMessage.observe(viewLifecycleOwner, errorObserver)
    }

    fun setupRecyclerView() {
        binding.rvGameSearchResults.apply {
            adapter = gameAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}