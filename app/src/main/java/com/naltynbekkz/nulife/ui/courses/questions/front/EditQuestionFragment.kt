package com.naltynbekkz.nulife.ui.courses.questions.front

import android.view.MenuItem
import android.widget.Toast
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.util.Convert

class EditQuestionFragment : NewQuestionFragment() {

    override fun init() {
        binding.toolbar.title = "Edit question"

        binding.question = viewModel.question
        binding.allSectionsSwitch.isChecked = viewModel.question.sectionId == 0L
        binding.anonymousSwitch.isChecked = viewModel.question.author.name == "Anonymous"


        arrayListOf(
            binding.selectTopic,
            binding.topic,
            binding.allSections,
            binding.allSectionsSwitch,
            binding.uploadImage
        ).forEach {
            it.isEnabled = false
            it.alpha = 0.5F
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (binding.question!!.isValid()) {
                    binding.loading = true

                    viewModel.editQuestion(
                        question = binding.question!!,
                        anonymous = binding.anonymousSwitch.isChecked,
                        success = requireActivity()::onBackPressed,
                        failure = fun() {
                            binding.loading = null
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong. Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
