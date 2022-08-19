package com.griffindfung.ballerstatsnow.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.Player
import com.griffindfung.ballerstatsnow.databinding.ItemPlayerSearchResultBinding
import com.griffindfung.ballerstatsnow.helpers.StringFormatter
import com.griffindfung.ballerstatsnow.ui.playerdetails.PlayerDetailsActivity

/*
Adapter displays a list of players by their full game. Each item is clickable and opens a new
PlayerDetailsActivity. The clicked player's id is passed as an intent and used to populate details
in PlayerDetailsActivity
 */
class PlayersRecyclerViewAdapter: RecyclerView.Adapter<PlayersRecyclerViewAdapter.ViewHolder>() {
    private var playersList: List<Player> = emptyList()

    // Replaces current list of players and refreshes rv to show players set
    fun setPlayers(players: List<Player>) {
        playersList = players
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPlayerSearchResultBinding): RecyclerView.ViewHolder(binding.root) {

        // Updates views in viewholder with corresponding Player data in "playersList"
        fun bind(player: Player) {
            binding.run {
                tvPlayerName.text = StringFormatter.formatPlayerFullName(player)

                // Clicking on a player's name opens a new activity of that player's seasonal averages
                // Player id is passed to activity in order to retrieve player's seaonal averages
                ltPlayerPreviewItem.setOnClickListener {
                    val intent = Intent(binding.root.context, PlayerDetailsActivity::class.java).apply {
                        putExtra(PlayerDetailsActivity.ID_KEY, player.id)
                    }
                    root.context.startActivity(intent)
                }
            }
        }
    }

    // Sorts playerList in alphabetical order
    fun sortByAscendingOrder() {
        setPlayers(playersList.sortedBy { StringFormatter.formatPlayerFullName(it) })
    }

    // Sorts playerList in reverse alphabetical order
    fun sortByDescendingOrder() {
        setPlayers(playersList.sortedByDescending { StringFormatter.formatPlayerFullName(it) })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPlayerSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player: Player = playersList[position]
        holder.bind(player)
    }

    override fun getItemCount(): Int {
        return playersList.size
    }
}