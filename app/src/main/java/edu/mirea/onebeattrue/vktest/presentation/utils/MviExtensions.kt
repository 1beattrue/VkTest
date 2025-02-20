package edu.mirea.onebeattrue.vktest.presentation.utils

import android.util.Log
import androidx.annotation.MainThread
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Executor.Callbacks
import com.arkivanov.mvikotlin.core.utils.internal.InternalMviKotlinApi
import com.arkivanov.mvikotlin.core.utils.internal.atomic
import com.arkivanov.mvikotlin.core.utils.internal.initialize
import com.arkivanov.mvikotlin.core.utils.internal.requireValue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

val ComponentContext.scope
    get() = CoroutineScope(
        Dispatchers.Main.immediate + SupervisorJob()
    ).apply {
        lifecycle.doOnDestroy { this.cancel() }
    }

/**
 * A [SafeCoroutineBootstrapper] that provides a structured and safe environment for launching coroutines.
 * It includes an exception handler to handle uncaught exceptions in the coroutines.
 *
 * @param Action The type of actions dispatched to the [Store].
 * @param mainContext The main coroutine context, defaults to [Dispatchers.Main].
 */
abstract class SafeCoroutineBootstrapper<Action : Any>(
    mainContext: CoroutineContext = Dispatchers.Main
) : Bootstrapper<Action> {

    @OptIn(InternalMviKotlinApi::class)
    private val actionConsumer = atomic<(Action) -> Unit>()

    /**
     * A [CoroutineExceptionHandler] that logs and handles uncaught exceptions in the coroutines.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    /**
     * A [CoroutineScope] with a custom context that includes the exception handler.
     * The scope is automatically cancelled on dispose.
     */
    protected val scope: CoroutineScope = CoroutineScope(mainContext + SupervisorJob() + exceptionHandler)

    @OptIn(InternalMviKotlinApi::class)
    final override fun init(actionConsumer: (Action) -> Unit) {
        this.actionConsumer.initialize(actionConsumer)
    }

    /**
     * Dispatches the `Action` to the [Store]. Must be called on the main thread.
     *
     * @param action An `Action` to be dispatched.
     */
    @OptIn(InternalMviKotlinApi::class)
    @MainThread
    protected fun dispatch(action: Action) {
        actionConsumer.requireValue().invoke(action)
    }

    /**
     * Handles uncaught exceptions thrown in coroutines within the scope.
     * Override this method to provide custom exception handling logic.
     *
     * @param throwable The uncaught exception.
     */
    protected open fun handleException(throwable: Throwable) {
        Log.e("SafeCoroutineBootstrapper", "Unhandled exception", throwable)
    }

    /**
     * Cancels the [CoroutineScope], stopping all active coroutines.
     */
    override fun dispose() {
        scope.cancel()
    }
}

/**
 * An abstract implementation of the [Executor] that exposes a [CoroutineScope] for coroutines launching.
 * It includes an exception handler for handling uncaught exceptions in the coroutines.
 *
 * @param mainContext a [CoroutineContext] to be used by the exposed [CoroutineScope]
 */
open class SafeCoroutineExecutor<in Intent : Any, Action : Any, State : Any, Message : Any, Label : Any>(
    mainContext: CoroutineContext = Dispatchers.Main
) : Executor<Intent, Action, State, Message, Label> {

    @OptIn(InternalMviKotlinApi::class)
    private val callbacks = atomic<Callbacks<State, Message, Action, Label>>()

    /**
     * A [CoroutineExceptionHandler] that logs and handles uncaught exceptions in the coroutines.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    /**
     * A [CoroutineScope] that can be used by the [SafeCoroutineExecutor] descendants to launch coroutines.
     * The [CoroutineScope] is automatically cancelled on dispose.
     */
    protected val scope: CoroutineScope = CoroutineScope(mainContext + SupervisorJob() + exceptionHandler)

    @OptIn(InternalMviKotlinApi::class)
    final override fun init(callbacks: Callbacks<State, Message, Action, Label>) {
        this.callbacks.initialize(callbacks)
    }

    /**
     * Handles uncaught exceptions thrown in coroutines within the scope.
     * Override this method to provide custom exception handling logic.
     *
     * @param throwable The uncaught exception.
     */
    protected open fun handleException(throwable: Throwable) {
        Log.e("SafeCoroutineExecutor", "Unhandled exception", throwable)
    }

    /**
     * Returns the *current* [State] of the [Store][com.arkivanov.mvikotlin.core.store.Store].
     *
     * The [State] may change between subsequent invocations if accessed on a non-main thread.
     */
    @OptIn(InternalMviKotlinApi::class)
    protected fun state(): State =
        callbacks.requireValue().state

    override fun executeIntent(intent: Intent) {
        // no-op
    }

    override fun executeAction(action: Action) {
        // no-op
    }

    override fun dispose() {
        scope.cancel()
    }

    /**
     * Sends the provided [action] to the [Store] and then forwards the [action] back to the [Executor].
     * This is the recommended way of executing actions from the [Executor], as it allows
     * any wrapping Stores to also handle those actions (e.g. logging or time-traveling).
     *
     * Must be called on the main thread.
     *
     * @param action an [Action] to be forwarded back to the [Executor] via [Store].
     */
    @OptIn(InternalMviKotlinApi::class)
    protected fun forward(action: Action) {
        callbacks.requireValue().onAction(action)
    }

    /**
     * Dispatches the provided [Message] to the [Reducer].
     * The updated [State] will be available immediately after this method returns.
     *
     * Must be called on the main thread.
     *
     * @param message a [Message] to be dispatched to the [Reducer].
     */
    @OptIn(InternalMviKotlinApi::class)
    @MainThread
    protected fun dispatch(message: Message) {
        callbacks.requireValue().onMessage(message)
    }

    /**
     * Sends the provided [Label] to the [Store] for publication.
     *
     * Must be called on the main thread.
     *
     * @param label a [Label] to be published.
     */
    @OptIn(InternalMviKotlinApi::class)
    @MainThread
    protected fun publish(label: Label) {
        callbacks.requireValue().onLabel(label)
    }
}
