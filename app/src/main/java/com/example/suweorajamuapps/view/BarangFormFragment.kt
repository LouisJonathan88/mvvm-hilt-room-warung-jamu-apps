package com.example.suweorajamuapps.view

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.suweorajamuapps.R
import com.example.suweorajamuapps.data.model.Barang
import com.example.suweorajamuapps.databinding.FragmentBarangFormBinding
import com.example.suweorajamuapps.util.Imageutils.copyImageToInternalStorage
import com.example.suweorajamuapps.viewmodel.BarangViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class BarangFormFragment : Fragment() {

    private var _binding: FragmentBarangFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarangViewModel by viewModels()

    private var barang: Barang? = null
    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            val safeUri = copyImageToInternalStorage(requireContext(), uri)
            safeUri?.let { newUri ->
                imageUri = newUri
                binding.imagePreview.setImageURI(newUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barang = arguments?.getParcelable("barang")

        barang?.let {
            binding.editNama.setText(it.nama)
            binding.editHarga.setText(it.harga.toString())
            imageUri = Uri.parse(it.imageUri)

            val file = File(imageUri?.path ?: "")
            if(file.exists()) {
                binding.imagePreview.setImageURI(imageUri)
            } else {
                binding.imagePreview.setImageResource(R.drawable.placeholder)
            }
            binding.buttonHapusBarang.visibility = View.VISIBLE
        }

        binding.imagePreview.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.buttonPilihDariGaleri.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.buttonSimpanBarang.setOnClickListener {
            val nama = binding.editNama.text.toString()
            val harga = binding.editHarga.text.toString().toDoubleOrNull()
            if (nama.isBlank() || harga == null || imageUri == null) {
                Toast.makeText(requireContext(), "Semua field harus diisi dan pilih gambar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonSimpanBarang.isEnabled = false
            lifecycleScope.launch {
                val barangBaru = Barang(
                    id = barang?.id ?: 0,
                    nama = nama,
                    harga = harga,
                    imageUri = imageUri.toString()
                )

                if (barang == null) {
                    viewModel.insertBarang(barangBaru)
                    Toast.makeText(requireContext(), "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.updateBarang(barangBaru)
                    Toast.makeText(requireContext(), "Barang berhasil diupdate", Toast.LENGTH_SHORT).show()
                }

                delay(500)
                binding.progressBar.visibility = View.GONE
                binding.buttonSimpanBarang.isEnabled = true
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonHapusBarang.setOnClickListener {
            barang?.let {
                AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menghapus barang ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deleteBarang(it)
                            Toast.makeText(requireContext(), "Barang berhasil dihapus", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}