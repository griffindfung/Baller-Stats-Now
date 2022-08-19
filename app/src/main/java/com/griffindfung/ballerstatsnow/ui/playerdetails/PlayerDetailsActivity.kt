package com.griffindfung.ballerstatsnow.ui.playerdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.griffindfung.ballerstatsnow.R
import com.griffindfung.ballerstatsnow.network.RetrofitBuilder
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.Player
import com.griffindfung.ballerstatsnow.apiobjects.seasonaveragesdata.SeasonAveragesResponse
import com.griffindfung.ballerstatsnow.databinding.ActivityPlayerDetailsBinding
import com.griffindfung.ballerstatsnow.helpers.StringFormatter

/*
Activity receives intent with player id and fetches basic player info and season averages based on
that id. Initial season averages shown default to current season. User can also search for other
season averages and will be shown on this activity if any.
 */
class PlayerDetailsActivity : AppCompatActivity() {

    companion object {
        const val ID_KEY = "id"
    }

    private lateinit var viewModel: PlayerDetailsViewModel
    private lateinit var binding: ActivityPlayerDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentGameId = intent.getIntExtra("id", -1)
        val ballDontLieService = RetrofitBuilder.getInstance()
        viewModel = ViewModelProvider(this, PlayerDetailsViewModel.PlayerDetailsViewModelFactory(ballDontLieService))
            .get(PlayerDetailsViewModel::class.java)

        setupToolbar()
        setupObservers()
        setupSeasonSearch()
        viewModel.setPlayerId(intentGameId)
        viewModel.initData()
    }

    // Setup back arrow in tb so user has alternative way to move to previous activity
    private fun setupToolbar() {
        binding.tbPlayerName.apply {
            setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    // Add listener to editText to search for season averages when user enters search on keyboard
    private fun setupSeasonSearch() {
        binding.etSeasonSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                // launch search request
                if (textView.text.isNotEmpty()) viewModel.setSearchQuery(textView.text.toString().toInt())
                true
            }
            false
        }
    }

    private fun setupObservers() {

        // Shows the player's name that the stats are for in the toolbar
        val playerDetailsObserver = Observer<Player>() {
            binding.tbPlayerName.title = StringFormatter.formatPlayerFullName(it)
        }

        // Sets textViews with player stats
        val seasonAveragesObserver = Observer<SeasonAveragesResponse> {
            updateTextViews(it)
        }

        // When fetching data, loading is shown and season averages views are hidden.
        // When complete, hides loading but visbility of season averages handled by other observers afterward.
        val loadingObserver = Observer<Boolean> {
            binding.pbSearchLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            if (it) {
                binding.ltSeasonAverages.visibility = View.INVISIBLE
                binding.tvNoSeasonAveragesFound.visibility = View.INVISIBLE
            }
        }

        // Shows no results message and hides season averages views when no results, vice versa otherwise.
        val noResultsObserver = Observer<Boolean> { isNoResults ->
            binding.tvNoSeasonAveragesFound.visibility = if (isNoResults) View.VISIBLE else View.INVISIBLE
            binding.ltSeasonAverages.visibility = if (isNoResults) View.INVISIBLE else View.VISIBLE
        }

        // Show toast msg of any possible error from network requests
        val errorObserver = Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.playerDetails.observe(this, playerDetailsObserver)
        viewModel.seasonAveragesResponse.observe(this, seasonAveragesObserver)
        viewModel.isLoading.observe(this, loadingObserver)
        viewModel.isNoResults.observe(this, noResultsObserver)
        viewModel.errorMessage.observe(this, errorObserver)
    }

    // Displays season average stats from network response "seasonAverageResponse" in textViews
    fun updateTextViews(seasonAveragesResponse: SeasonAveragesResponse) {
        if (seasonAveragesResponse.data.isEmpty()) return
        val data = seasonAveragesResponse.data[0]
        binding.apply {
            tvSeasonAveragesTitle.text = String.format(getString(R.string.player_details_season_averages_title), data.season)
            tvGamesPlayed.text = String.format(getString(R.string.player_details_games_played), data.gamesPlayed)
            tvMinutesPlayed.text = String.format(getString(R.string.player_details_minutes_played), data.min)
            tvPoints.text = String.format(getString(R.string.player_details_points), data.pts)
            tvSteals.text = String.format(getString(R.string.player_details_steals), data.stl)
            tvPersonalFouls.text = String.format(getString(R.string.player_details_personal_fouls, data.pf))
            tvTurnovers.text = String.format(getString(R.string.player_details_turnovers), data.turnover)
            tvBlocks.text = String.format(getString(R.string.player_details_blocks), data.blk)
            tvAssists.text = String.format(getString(R.string.player_details_assists), data.ast)
            tvOffensiveRebounds.text = String.format(getString(R.string.player_details_offensive_rebounds), data.oreb)
            tvDefensiveRebounds.text = String.format(getString(R.string.player_details_defensive_rebounds), data.dreb)
            tvRebounds.text = String.format(getString(R.string.player_details_rebounds), data.reb)

            tvFieldGoalsMade.text = String.format(getString(R.string.player_details_field_goals_made), data.fgm)
            tvFieldGoalsAttempted.text = String.format(getString(R.string.player_details_field_goals_attempted), data.fga)
            tvFieldGoalsPercentage.text = String.format(getString(R.string.player_details_field_goals_percentage), data.fgPct * 100)

            tvFieldGoalsThreePointersMade.text = String.format(getString(R.string.player_details_field_goal_3_pointers_made), data.fg3m)
            tvFieldGoalsThreePointersAttempted.text = String.format(getString(R.string.player_details_field_goal_3_pointers_attempted), data.fg3a)
            tvFieldGoalsThreePointersPercentage.text = String.format(getString(R.string.player_details_field_goal_3_pointers_percentage), data.fg3Pct * 100)

            tvFreeThrowsMade.text = String.format(getString(R.string.player_details_free_throws_made), data.ftm)
            tvFreeThrowsAttempted.text = String.format(getString(R.string.player_details_field_goals_attempted), data.fta)
            tvFreeThrowsPercentage.text = String.format(getString(R.string.player_details_free_throws_percentage), data.ftPct * 100)
        }

    }
}