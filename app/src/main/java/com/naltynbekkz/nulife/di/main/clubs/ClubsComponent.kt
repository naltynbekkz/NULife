package com.naltynbekkz.nulife.di.main.clubs

import com.naltynbekkz.nulife.ui.clubs.front.*
import dagger.Subcomponent
import javax.inject.Scope

@ClubsScope
@Subcomponent(
    modules = [
        ClubsViewModelsModule::class
    ]
)
interface ClubsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ClubsComponent
    }

    fun inject(fragment: FeedFragment)
    fun inject(fragment: ClubsFragment)
    fun inject(fragment: EventsFragment)
    fun inject(fragment: ClubFragment)
    fun inject(fragment: EventFragment)
    fun inject(activity: ClubsActivity)
    fun inject(activity: EventsActivity)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ClubsScope