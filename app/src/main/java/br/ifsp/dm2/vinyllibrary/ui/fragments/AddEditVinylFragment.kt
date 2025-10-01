package br.ifsp.dm2.vinyllibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.ifsp.dm2.vinyllibrary.VinylLibraryApplication
import br.ifsp.dm2.vinyllibrary.viewmodel.VinylViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl
import br.ifsp.dm2.vinyllibrary.databinding.FragmentAddEditVinylBinding
import br.ifsp.dm2.vinyllibrary.viewmodel.VinylViewModel
import kotlinx.coroutines.launch

class AddEditVinylFragment : Fragment() {

    private var _binding: FragmentAddEditVinylBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinylViewModel by activityViewModels {
        VinylViewModelFactory((requireActivity().application as VinylLibraryApplication).repository)
    }

    private val args: AddEditVinylFragmentArgs by navArgs()

    private var currentVinyl: Vinyl? = null
    private val isEditing get() = args.vinylId != -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditVinylBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
        setupClickListeners()

        if (isEditing) {
            loadVinylData()
        }
    }

    private fun setupUI() {
        binding.textViewTitle.text = if (isEditing) "Editar Vinil" else "Adicionar Vinil"
        binding.buttonSave.text = if (isEditing) "Atualizar" else "Salvar"
    }

    private fun setupObservers() {
        // Observar estado de loading
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
                binding.buttonSave.isEnabled = !isLoading
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
        binding.buttonSave.setOnClickListener {
            saveVinyl()
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        if (isEditing) {
            binding.buttonDelete.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    deleteVinyl()
                }
            }
        }
    }

    private fun loadVinylData() {
        viewLifecycleOwner.lifecycleScope.launch {
            currentVinyl = viewModel.getVinylById(args.vinylId)
            currentVinyl?.let { vinyl ->
                binding.apply {
                    editTextAlbumName.setText(vinyl.albumName)
                    editTextArtistName.setText(vinyl.artistName)
                    editTextReleaseYear.setText(vinyl.releaseYear.toString())
                    editTextRecordLabel.setText(vinyl.recordLabel)
                }
            }
        }
    }

    private fun saveVinyl() {
        if (!validateInput()) return

        val albumName = binding.editTextAlbumName.text.toString().trim()
        val artistName = binding.editTextArtistName.text.toString().trim()
        val releaseYear = binding.editTextReleaseYear.text.toString().toInt()
        val recordLabel = binding.editTextRecordLabel.text.toString().trim()

        val vinyl = if (isEditing) {
            currentVinyl?.copy(
                albumName = albumName,
                artistName = artistName,
                releaseYear = releaseYear,
                recordLabel = recordLabel
            )
        } else {
            Vinyl(
                albumName = albumName,
                artistName = artistName,
                releaseYear = releaseYear,
                recordLabel = recordLabel
            )
        }

        vinyl?.let {
            if (isEditing) {
                viewModel.updateVinyl(it)
            } else {
                viewModel.insertVinyl(it)
            }

            Toast.makeText(
                requireContext(),
                if (isEditing) "Vinil atualizado com sucesso!" else "Vinil adicionado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigateUp()
        }
    }

    private fun deleteVinyl() {
        currentVinyl?.let { vinyl ->
            viewModel.deleteVinyl(vinyl)
            Toast.makeText(requireContext(), "Vinil excluído com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        binding.apply {
            if (editTextAlbumName.text.toString().trim().isEmpty()) {
                editTextAlbumName.error = "Nome do álbum é obrigatório"
                isValid = false
            }

            if (editTextArtistName.text.toString().trim().isEmpty()) {
                editTextArtistName.error = "Nome do artista é obrigatório"
                isValid = false
            }

            val yearText = editTextReleaseYear.text.toString().trim()
            if (yearText.isEmpty()) {
                editTextReleaseYear.error = "Ano de lançamento é obrigatório"
                isValid = false
            } else {
                try {
                    val year = yearText.toInt()
                    if (year < 1900 || year > 2030) {
                        editTextReleaseYear.error = "Ano deve estar entre 1900 e 2030"
                        isValid = false
                    }
                } catch (e: NumberFormatException) {
                    editTextReleaseYear.error = "Ano deve ser um número válido"
                    isValid = false
                }
            }

            if (editTextRecordLabel.text.toString().trim().isEmpty()) {
                editTextRecordLabel.error = "Gravadora é obrigatória"
                isValid = false
            }
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
