package com.naltynbekkz.nulife.di.auth

import com.naltynbekkz.nulife.ui.auth.AuthActivity
import dagger.Subcomponent
import javax.inject.Scope

@AuthScope
@Subcomponent(
    modules = [
        AuthModule::class,
        AuthViewModelsModule::class
    ]
)
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(authActivity: AuthActivity)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthScope
