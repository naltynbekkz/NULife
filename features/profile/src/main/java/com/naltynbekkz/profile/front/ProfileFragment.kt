package com.naltynbekkz.profile.front

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.core.BottomNavigationController
import com.naltynbekkz.core.Convert
import com.naltynbekkz.profile.R
import com.naltynbekkz.profile.databinding.FragmentProfileBinding
import com.naltynbekkz.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.user = it
        })

        binding.terms.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://nulife.kz/terms")
            startActivity(intent)
        }
        binding.help.setOnClickListener {
            Convert.help(requireContext())
        }
        binding.profile.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setItems(
                    R.array.profile_actions,
                    fun(_: DialogInterface, which: Int) {
                        when (which) {
                            0 -> {
                                findNavController().navigate(
                                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                                )
                            }
                            1 -> {
                                (activity as BottomNavigationController.NavigatesToAuthActivity).navigateToAuthActivity()
                            }
                        }
                    })
                .show()
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToSettingsActivity()
                )
            }
        }
        return true
    }

}
