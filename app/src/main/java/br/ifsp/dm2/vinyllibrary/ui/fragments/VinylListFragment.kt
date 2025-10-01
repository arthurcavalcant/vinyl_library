package br.ifsp.dm2.vinyllibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.ifsp.dm2.vinyllibrary.R
import br.ifsp.dm2.vinyllibrary.VinylLibraryApplication
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl
import br.ifsp.dm2.vinyllibrary.databinding.FragmentVinylListBinding
import br.ifsp.dm2.vinyllibrary.ui.adapters.VinylAdapter
import br.ifsp.dm2.vinyllibrary.viewmodel.VinylViewModel
import br.ifsp.dm2.vinyllibrary.viewmodel.VinylViewModelFactory
import kotlinx.coroutines.launch

class VinylListFragment : Fragment() {

    private var _binding: FragmentVinylListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinylViewModel by activityViewModels {
        VinylViewModelFactory((requireActivity().application as VinylLibraryApplication).repository)
    }

    private lateinit var adapter: VinylAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVinylListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = VinylAdapter(
            onItemClick = { vinyl ->
                // Navegar para tela de edição
                val bundle = Bundle().apply {
                    putLong("vinylId", vinyl.id)
                }
                findNavController().navigate(R.id.action_vinylList_to_addEditVinyl, bundle)
            },
            onDeleteClick = { vinyl ->
                viewModel.deleteVinyl(vinyl)
            }
        )

        binding.recyclerViewVinyls.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@VinylListFragment.adapter
        }
    }

    private fun setupObservers() {
        // Observar lista de vinyls
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allVinyls.collect { vinyls ->
                adapter.submitList(vinyls)
                binding.textViewEmptyState.visibility =
                    if (vinyls.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Observar estado de loading
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // Observar mensagens de erro
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collect { message ->
                message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.clearErrorMessage()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddVinyl.setOnClickListener {
            // Navegar para tela de adicionar
            val bundle = Bundle().apply {
                putLong("vinylId", -1L)
            }
            findNavController().navigate(R.id.action_vinylList_to_addEditVinyl, bundle)
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText ?: ""
                if (query.isEmpty()) {
                    // Se a busca estiver vazia, mostrar todos os vinyls
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.allVinyls.collect { vinyls ->
                            adapter.submitList(vinyls)
                        }
                    }
                } else {
                    // Realizar busca
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.searchVinyls(query).collect { filteredVinyls ->
                            adapter.submitList(filteredVinyls)
                        }
                    }
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
