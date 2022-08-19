package com.griffindfung.ballerstatsnow.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.Game
import com.griffindfung.ballerstatsnow.databinding.ItemGameSearchResultBinding
import com.griffindfung.ballerstatsnow.helpers.StringFormatter
import com.griffindfung.ballerstatsnow.ui.gamedetails.GameDetailsActivity

/*
Displays a preview of games and their date times. Each item is clickable and starts a new GameDetailsActivity
with the game id of the clicked item passed. Clicking the item allows the user to see more details about the game.
*/
class GamesRecyclerViewAdapter(): RecyclerView.Adapter<GamesRecyclerViewAdapter.ViewHolder>() {

    private var gamesList: List<Game> = emptyList()

    // Replaces current list of games and refreshes rv to show game set
    fun setGames(games: List<Game>) {
        gamesList = games
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemGameSearchResultBinding): RecyclerView.ViewHolder(binding.root) {

        // Updates views in viewholder with corresponding Game data in "gamesList"
        fun bind(game: Game) {
            binding.run {
                tvGameDate.text = StringFormatter.formatGameDateToItemDate(game.date)
                tvGameTime.text = StringFormatter.formatGameDateToItemTime(game.date)
                tvVisitorTeamName.text = game.visitorTeam.fullName
                tvHomeTeamName.text = game.homeTeam.fullName

                // Clicking on a game item opens a new activity with its corresponding game details
                // Game id passed for new activity to fetch corresponding game details
                ltGamePreviewItem.setOnClickListener {
                    val intent = Intent(binding.root.context, GameDetailsActivity::class.java).apply {

                        // Note: Could just make Game serializable and pass it as an extra since the data is the same
                        // for gameid specific api calls but wanted to practice using more retrofit calls.
                        // This additional api call method is more expensive**
                        putExtra(GameDetailsActivity.ID_KEY, game.id)
                    }

                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemGameSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game: Game = gamesList[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }
}