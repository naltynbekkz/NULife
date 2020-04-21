package com.naltynbekkz.nulife.di.main.profile

import com.naltynbekkz.nulife.ui.profile.front.CategoriesFragment
import com.naltynbekkz.nulife.ui.profile.front.EditProfileFragment
import com.naltynbekkz.nulife.ui.profile.front.ProfileFragment
import dagger.Subcomponent
import javax.inject.Scope

@ProfileScope
@Subcomponent(
    modules = [
        ProfileViewModelsModule::class
    ]
)
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)
    fun inject(fragment: CategoriesFragment)
    fun inject(fragment: EditProfileFragment)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ProfileScope