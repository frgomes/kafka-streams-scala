package com.github.aseigneurin.kafka.streams.scala

import com.github.aseigneurin.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.processor.StateStoreSupplier
import org.apache.kafka.streams.state.KeyValueStore

class KGroupedTableS[K, V](inner: KGroupedTable[K, V]) {

  def count(storeName: String): KTableS[K, Long] = {
    inner.count(storeName)
      .mapValues[Long](new ValueMapper[java.lang.Long, scala.Long] {
      override def apply(value: java.lang.Long): scala.Long = Long.box(value)
    })
  }

  def count(storeSupplier: StateStoreSupplier[KeyValueStore[_, _]]): KTableS[K, Long] = {
    inner.count(storeSupplier)
      .mapValues[Long](new ValueMapper[java.lang.Long, scala.Long] {
      override def apply(value: java.lang.Long): scala.Long = Long.box(value)
    })
  }

  def reduce(adder: (V, V) => V,
             subtractor: (V, V) => V,
             storeName: String): KTableS[K, V] = {
    val adderJ: Reducer[V] = (v1: V, v2: V) => adder(v1, v2)
    val subtractorJ: Reducer[V] = (v1: V, v2: V) => subtractor(v1, v2)
    inner.reduce(adderJ, subtractorJ, storeName)
  }

  def reduce(adder: Reducer[V],
             subtractor: Reducer[V],
             storeSupplier: StateStoreSupplier[KeyValueStore[_, _]]): KTableS[K, V] = {
    val adderJ: Reducer[V] = (v1: V, v2: V) => adder(v1, v2)
    val subtractorJ: Reducer[V] = (v1: V, v2: V) => subtractor(v1, v2)
    inner.reduce(adderJ, subtractorJ, storeSupplier)
  }

  def aggregate[VR](initializer: () => VR,
                    adder: (K, V, VR) => VR,
                    subtractor: (K, V, VR) => VR,
                    storeName: String): KTableS[K, VR] = {
    val initializerJ: Initializer[VR] = () => initializer()
    val adderJ: Aggregator[K, V, VR] = (k: K, v: V, va: VR) => adder(k, v, va)
    val subtractorJ: Aggregator[K, V, VR] = (k: K, v: V, va: VR) => subtractor(k, v, va)
    inner.aggregate(initializerJ, adderJ, subtractorJ, storeName)
  }

  def aggregate[VR](initializer: () => VR,
                    adder: (K, V, VR) => VR,
                    subtractor: (K, V, VR) => VR,
                    aggValueSerde: Serde[VR],
                    storeName: String): KTableS[K, VR] = {
    val initializerJ: Initializer[VR] = () => initializer()
    val adderJ: Aggregator[K, V, VR] = (k: K, v: V, va: VR) => adder(k, v, va)
    val subtractorJ: Aggregator[K, V, VR] = (k: K, v: V, va: VR) => subtractor(k, v, va)
    inner.aggregate(initializerJ, adderJ, subtractorJ, aggValueSerde, storeName)
  }

  def aggregate[VR](initializer: () => VR,
                    adder: (K, V, VR) => VR,
                    subtractor: (K, V, VR) => VR,
                    storeSupplier: StateStoreSupplier[KeyValueStore[_, _]]): KTableS[K, VR] = {
    val initializerJ: Initializer[VR] = () => initializer()
    val adderJ: Aggregator[K, V, VR] = (k: K, v: V, va: VR) => adder(k, v, va)
    val subtractorJ: Aggregator[K, V, VR] = (k: K, v: V, va: VR) => subtractor(k, v, va)
    inner.aggregate(initializerJ, adderJ, subtractorJ, storeSupplier)
  }

}
