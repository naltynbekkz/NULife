package com.naltynbekkz.nulife.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.MapKey
import dagger.Module
import dagger.Reusable
import dagger.multibindings.Multibinds
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Reusable
open class ViewModelProviderFactory @Inject constructor(
    private val assistedFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModelAssistedFactory<out ViewModel>>,
    private val viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) {
    /**
     * Creates instance of ViewModel either annotated with @AssistedInject or @Inject and passes dependencies it needs.
     */
    fun create(owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null) =
        object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val viewModel =
                    createAssistedInjectViewModel(modelClass, handle)
                        ?: createInjectViewModel(modelClass)
                        ?: throw IllegalArgumentException("Unknown model class $modelClass")

                try {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }

    /**
     * Creates ViewModel based on @AssistedInject constructor and its factory
     */
    private fun <T : ViewModel?> createAssistedInjectViewModel(
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): ViewModel? {
        val creator = assistedFactories[modelClass]
            ?: assistedFactories.asIterable()
                .firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: return null

        return creator.create(handle)
    }

    /**
     * Creates ViewModel based on regular Dagger @Inject constructor
     */
    private fun <T : ViewModel?> createInjectViewModel(modelClass: Class<T>): ViewModel? {
        val creator = viewModelProviders[modelClass]
            ?: viewModelProviders.asIterable()
                .firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: return null

        return creator.get()
    }
}

interface ViewModelAssistedFactory<T : ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): T
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)


@Module
abstract class EmptyMapProvider {

    @Multibinds
    abstract fun viewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModel>

    @Multibinds
    abstract fun assistedViewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModelAssistedFactory<out ViewModel>>
}