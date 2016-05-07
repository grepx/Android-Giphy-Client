package gregpearce.gifhub.util.rx

import rx.Observable

data class IndexedItem<T>(val item: T, var index: Int)

/**
 *  Convert items in a stream to IndexedItems, which have an index field set to the order they are in the stream.
 */
fun <T> Observable<T>.indexItems() =
        map { IndexedItem(it, 0) }
                .scan { current, next ->
                    next.index = current.index + 1
                    next
                }
