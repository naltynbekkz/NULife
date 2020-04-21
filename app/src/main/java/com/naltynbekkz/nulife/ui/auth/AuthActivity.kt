package com.naltynbekkz.nulife.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.naltynbekkz.nulife.BaseApplication
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    private val viewModel: AuthViewModel by viewModels { viewModelProvider.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as BaseApplication).appComponent.authComponent().create().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        signInButton.setOnClickListener {
            val intent = viewModel.getSignInIntent()
            startActivityForResult(intent, PERMISSION_CODE)
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
