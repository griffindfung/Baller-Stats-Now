package com.griffindfung.ballerstatsnow.ui.today

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.griffindfung.ballerstatsnow.ui.adapters.GamesRecyclerViewAdapter
import com.griffindfung.ballerstatsnow.network.RetrofitBuilder
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.GameResponse
import com.griffindfung.ballerstatsnow.databinding.FragmentMainBinding

/*
Fragment shows current games happening today. Users can find more detail about the game by clicking
on the game item in the rv.
 */
class TodayFragment : Fragment() {

    companion object {
        fun newInstance() = TodayFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TodayViewModel
    private var gameAdapter: GamesRecyclerViewAdapter = GamesRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val ballDontLieService = RetrofitBuilder.getInstance()
        viewModel = ViewModelProvider(this, TodayViewModel.MainViewModelFactory(ballDontLieService))
            .get(TodayViewModel::class.java)
        binding.rvGames.adapter = gameAdapter
        binding.rvGames.layoutManager = LinearLayoutManager(activity)
        setupObservers()
        viewModel.fetchGamesToday()
    }

    private fun setupObservers() {

        // Create observer to check when list of games is updated and what to do with that data accordingly
        val gameObserver = Observer<GameResponse> {
            gameAdapter.setGames(it.gameData)
        }

        // Showing loading when requesting data
        val loadingObserver = Observer<Boolean> {
            binding.pbSearchLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            binding.rvGames.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }

        // Show no results message if no games today
        val noResultsObserver = Observer<Boolean> { isNoResults ->
            binding.tvNoGamesFound.visibility = if (isNoResults) View.VISIBLE else View.INVISIBLE
        }

        // Show toast when error occurs
        val errorObserver = Observer<String> {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.gameResponse.observe(viewLifecycleOwner, gameObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.isNoResults.observe(viewLifecycleOwner, noResultsObserver)
        viewModel.errorMessage.observe(viewLifecycleOwner, errorObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}