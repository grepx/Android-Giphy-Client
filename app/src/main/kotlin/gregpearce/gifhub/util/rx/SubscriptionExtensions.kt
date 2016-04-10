package gregpearce.gifhub.util.rx

import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun Subscription.addToComposite(composite: CompositeSubscription) { composite.add(this) }