package com.naltynbekkz.nulife.di.main

import com.naltynbekkz.nulife.di.main.clubs.ClubsComponent
import com.naltynbekkz.nulife.di.main.courses.CoursesComponent
import com.naltynbekkz.nulife.di.main.food.FoodComponent
import com.naltynbekkz.nulife.di.main.market.MarketComponent
import com.naltynbekkz.nulife.di.main.profile.ProfileComponent
import com.naltynbekkz.nulife.di.main.timetable.TimetableComponent
import com.naltynbekkz.nulife.repository.UserRepository
import com.naltynbekkz.nulife.ui.MainActivity
import dagger.Module
import dagger.Subcomponent
import javax.inject.Scope

@MainScope
@Subcomponent(
    modules = [
        MainModule::class,
        SubComponentsModule::class,
        MainViewModelsModule::class
    ]
)
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun coursesComponent(): CoursesComponent.Factory
    fun timetableComponent(): TimetableComponent.Factory
    fun clubsComponent(): ClubsComponent.Factory
    fun foodComponent(): FoodComponent.Factory
    fun profileComponent(): ProfileComponent.Factory
    fun marketComponent(): MarketComponent.Factory

    fun inject(mainActivity: MainActivity)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope


@Module(
    subcomponents = [
        CoursesComponent::class,
        TimetableComponent::class,
        FoodComponent::class,
        ClubsComponent::class,
        ProfileComponent::class,
        MarketComponent::class
    ]
)
object SubComponentsModule


