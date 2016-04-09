package gregpearce.gifhub.rx

import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun Subscription.addToComposite(composite: CompositeSubscription) { composite.add(this) }