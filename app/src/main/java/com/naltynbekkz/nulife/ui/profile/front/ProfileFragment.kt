package com.naltynbekkz.nulife.ui.profile.front

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentProfileBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.auth.AuthActivity
import com.naltynbekkz.nulife.ui.market.front.MyItemsActivity
import com.naltynbekkz.nulife.ui.profile.viewmodel.ProfileViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: ProfileViewModel by viewModels { viewModelProvider.create(this) }
    lateinit var binding: FragmentProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).profileComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        setHasOptionsMenu(true)

        (activity as MainActivity).setToolbar(binding.toolbar)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.user = it
        })

        binding.savedItems.setOnClickListener {
            val intent = Intent(context, MyItemsActivity::class.java)
            startActivity(intent)
        }
        binding.categories.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToCategoriesFragment()
            )
        }
        binding.terms.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://nulife.kz/terms")
            startActivity(intent)
        }
        binding.help.setOnClickListener {
            Convert.help(context!!)
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
                                FirebaseAuth.getInstance().signOut()
                                startActivity(Intent(activity, AuthActivity::class.java))
                                activity!!.finish()
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
