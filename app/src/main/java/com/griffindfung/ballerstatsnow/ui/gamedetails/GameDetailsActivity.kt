package com.griffindfung.ballerstatsnow.ui.gamedetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.griffindfung.ballerstatsnow.R
import com.griffindfung.ballerstatsnow.network.RetrofitBuilder
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.Game
import com.griffindfung.ballerstatsnow.databinding.ActivityGameDetailsBinding
import com.griffindfung.ballerstatsnow.helpers.StringFormatter

/*
Activity receives gameid from intent and uses it to populate game details. Activity shows the main
information of teams, scores, and status.
 */
class GameDetailsActivity : AppCompatActivity() {

    companion object {
        const val ID_KEY = "id"
    }

    private lateinit var viewModel: GameDetailsViewModel
    private lateinit var binding: ActivityGameDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val ballDontLieService = RetrofitBuilder.getInstance()
        viewModel = ViewModelProvider(this, GameDetailsViewModel.GameDetailsViewModelFactory(ballDontLieService)).get(
            GameDetailsViewModel::class.java)

        setupToolbar()
        setupObservers()

        // Fetch game data based on passed game id from intent
        val intentGameId = intent.getIntExtra(ID_KEY, -1)
        viewModel.fetchGameDetails(intentGameId)
    }

    // Setup back arrow in tb so user has alternative way to move to previous activity
    private fun setupToolbar() {
        binding.tbGameDetails.apply {
            setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    // Update views to display Game details
    fun setupObservers() {
        val gameDetailsObserver = Observer<Game> {
            binding.apply {
                val formattedDate = StringFormatter.formatGameDateToItemDate(it.date)
                tvGameDate.text = String.format(getString(R.string.game_details_date), formattedDate)
                tvGameSeason.text = String.format(getString(R.string.game_details_season), it.season.toString())
                tvGamePeriod.text = String.format(getString(R.string.game_details_period), it.period.toString())
                tvGameStatus.text = String.format(getString(R.string.game_details_status), it.status)

                tvHomeTeamFullName.text = it.homeTeam.fullName
                tvHomeTeamAbbreviation.text = it.homeTeam.abbreviation
                tvHomeTeamScore.text = it.homeTeamScore.toString()

                tvVisitorTeamAbbreviation.text = it.visitorTeam.abbreviation
                tvVisitorTeamFullName.text = it.visitorTeam.fullName
                tvVisitorTeamScore.text = it.visitorTeamScore.toString()
            }
        }

        // Hides game stats and show message if no valid game data
        val noResultsObserver = Observer<Boolean> { isNoResults->
            binding.ltGameScoreBoard.visibility = if (isNoResults) View.INVISIBLE else View.VISIBLE
            binding.ltGameStatus.visibility = if (isNoResults) View.INVISIBLE else View.VISIBLE
        }

        // Show toast msg of any possible error from network requests
        val errorObserver = Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.gameDetails.observe(this, gameDetailsObserver)
        viewModel.isNoResults.observe(this, noResultsObserver)
        viewModel.errorMessage.observe(this, errorObserver)
    }
}