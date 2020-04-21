package com.naltynbekkz.nulife.di.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.naltynbekkz.nulife.R
import dagger.Module
import dagger.Provides

@Module
object AuthModule {

    @AuthScope
    @Provides
    @JvmStatic
    fun provideAuthGoogleSignInClient(context: Context): GoogleSignInClient {
        return GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

}