package com.naltynbekkz.nulife.di

import android.content.Context
import com.naltynbekkz.nulife.BaseApplication
import com.naltynbekkz.nulife.di.auth.AuthComponent
import com.naltynbekkz.nulife.di.main.MainComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        EmptyMapProvider::class,
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent {

    fun inject(app: BaseApplication)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory

}

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
object SubComponentsModule