package com.naltynbekkz.nulife.ui.courses.answers.front

import android.view.MenuItem
import android.widget.Toast
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.util.Convert

class EditAnswerFragment : NewAnswerFragment() {

    override fun init() {

        binding.uploadImage.apply {
            alpha = 0.5F
            isEnabled = false
        }
        binding.answer = viewModel.answer
        binding.anonymousSwitch.isChecked = (viewModel.answer.author.name == "Anonymous")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (binding.answer!!.isValid()) {
                    binding.loading = true

                    viewModel.editAnswer(
                        anonymous = binding.anonymousSwitch.isChecked,
                        failure = fun() {
                            binding.loading = null
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong. Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        success = requireActivity()::onBackPressed
                    )

                } else {
                    binding.loading = false
                }
            }
            R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return true
    }

}
