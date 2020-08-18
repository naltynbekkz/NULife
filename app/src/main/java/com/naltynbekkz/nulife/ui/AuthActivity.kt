package com.naltynbekkz.nulife.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.naltynbekkz.nulife.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        signInButton.setOnClickListener {
            val intent = viewModel.getSignInIntent()
            startActivityForResult(intent,
                PERMISSION_CODE
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERMISSION_CODE && resultCode == Activity.RESULT_OK) {

            try {
                viewModel.signIn(
                    data,
                    success = fun() {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    fail = fun() {
                        onFail()
                    }
                )
            } catch (e: ApiException) {
                onFail()
            }
        }
    }

    private fun onFail() {
        Toast.makeText(this, "Unsuccessful login", Toast.LENGTH_SHORT).show()
        viewModel.signOut()
    }

    companion object {
        private const val PERMISSION_CODE = 9999
    }

}
