package br.ifsp.dm2.vinyllibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl
import br.ifsp.dm2.vinyllibrary.databinding.ItemVinylBinding

class VinylAdapter(
    private val onItemClick: (Vinyl) -> Unit,
    private val onDeleteClick: (Vinyl) -> Unit
) : ListAdapter<Vinyl, VinylAdapter.VinylViewHolder>(VinylDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VinylViewHolder {
        val binding = ItemVinylBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VinylViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VinylViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VinylViewHolder(
        private val binding: ItemVinylBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vinyl: Vinyl) {
            binding.apply {
                textViewAlbumName.text = vinyl.albumName
                textViewArtistName.text = vinyl.artistName
                textViewReleaseYear.text = vinyl.releaseYear.toString()
                textViewRecordLabel.text = vinyl.recordLabel

                root.setOnClickListener {
                    onItemClick(vinyl)
                }

                buttonDelete.setOnClickListener {
                    onDeleteClick(vinyl)
                }
            }
        }
    }

    class VinylDiffCallback : DiffUtil.ItemCallback<Vinyl>() {
        override fun areItemsTheSame(oldItem: Vinyl, newItem: Vinyl): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vinyl, newItem: Vinyl): Boolean {
            return oldItem == newItem
        }
    }
}
