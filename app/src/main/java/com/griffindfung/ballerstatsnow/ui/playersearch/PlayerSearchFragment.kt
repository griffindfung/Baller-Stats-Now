package com.griffindfung.ballerstatsnow.ui.playersearch

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.griffindfung.ballerstatsnow.R
import com.griffindfung.ballerstatsnow.network.RetrofitBuilder
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.PlayerResponse
import com.griffindfung.ballerstatsnow.databinding.FragmentPlayerSearchBinding
import com.griffindfung.ballerstatsnow.ui.adapters.PlayersRecyclerViewAdapter

/*
Fragment allows user to launch a request to search for a list of players by entering some substring
of the desired player. Clicking on a player result leads to PlayerDetailsActivity to display more data.
 */
class PlayerSearchFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerSearchFragment()
    }
    private var _binding: FragmentPlayerSearchBinding? = null
    private val binding get() = _binding!!

    private var playersRecyclerViewAdapter: PlayersRecyclerViewAdapter = PlayersRecyclerViewAdapter()
    private lateinit var viewModel: PlayerSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerSearchBinding.inflate(inflater, container, false)
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
            PlayerSearchViewModel.PlayerSearchViewModelFactory(ballDontLieService)
        )
            .get(PlayerSearchViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        setupButtons()
    }

    // Add onclicks for buttons to sort list of players fetched
    fun setupButtons() {
        binding.btnSortAscOrder.setOnClickListener { playersRecyclerViewAdapter.sortByAscendingOrder() }
        binding.btnSortDescOrder.setOnClickListener { playersRecyclerViewAdapter.sortByDescendingOrder() }
    }

    fun setupToolbar() {
        binding.apply {
            tbPlayerSearch.inflateMenu(R.menu.basic_search_menu)
            val searchView = tbPlayerSearch.menu.findItem(R.id.action_search).actionView as SearchView

            // Expands width of SearchView as far as possible
            searchView.maxWidth = Integer.MAX_VALUE

            // Offsets the inherent padding of SearchView so that it is inline with rest of UI
            searchView.setPadding(-16, 0 ,0, 0)
            searchView.queryHint = getString(R.string.player_search_toolbar_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                // Launch search for players based on query submitted
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.setSearchQuery(query?: "")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    fun setupObservers() {

        // Update player list data shown in recycler view
        val playerObserver = Observer<PlayerResponse> {
            playersRecyclerViewAdapter.setPlayers(it.data)
        }

        // Show loading progressbar and hide player list until result returned
        val loadingObserver = Observer<Boolean> {
            binding.pbSearchLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            if (it) {
                binding.rvPlayerSearchResults.visibility = View.INVISIBLE
                binding.tvNoPlayersFound.visibility = View.INVISIBLE
            }
        }

        // Handles visibility of no results message and player list when there are no players returned
        val noResultsObserver = Observer<Boolean> { isNoResults ->
            binding.tvNoPlayersFound.visibility = if (isNoResults) View.VISIBLE else View.INVISIBLE
            binding.rvPlayerSearchResults.visibility = if (isNoResults) View.INVISIBLE else View.VISIBLE
        }
        // Show toast msg of any possible error from network requests
        val errorObserver = Observer<String> {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.playersSearchResponse.observe(viewLifecycleOwner, playerObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.isNoResults.observe(viewLifecycleOwner, noResultsObserver)
        viewModel.errorMessage.observe(viewLifecycleOwner, errorObserver)
    }

    fun setupRecyclerView() {
        binding.rvPlayerSearchResults.apply {
            adapter = playersRecyclerViewAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}