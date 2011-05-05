package com.twitter.cassie

import connection._
import java.net.InetSocketAddress
import scala.collection.JavaConversions._
import com.twitter.cassie.connection.SocketAddressCluster
import com.twitter.cassie.connection.CCluster
import com.twitter.util.Duration
import com.twitter.conversions.time._

/**
 * A Cassandra cluster.
 *
 * @param seedHosts list of some hosts in the cluster
 * @param seedPort the port number for '''all''' hosts in the cluster */
class Cluster(seedHosts: Set[String], seedPort: Int) {

  /**
    * @param seedHosts A comma separated list of seed hosts for a cluster. The rest of the
    *                  hosts can be found via mapping the cluser. See KeyspaceBuilder.mapHostsEvery.
    *                  The port number is assumed to be 9160. */
  def this(seedHosts: String) = this(seedHosts.split(',').filter{ !_.isEmpty }.toSet, 9160)

  /**
    * @param seedHosts A collection of seed host addresses. The port number is assumed to be 9160*/
  def this(seedHosts: java.util.Collection[String]) = this(asScalaIterable(seedHosts).toSet, 9160)

  /**
    * Returns a  [[com.twitter.cassie.KeyspaceBuilder]] instance.
    * @param name the keyspace's name */
  def keyspace(name: String): KeyspaceBuilder = KeyspaceBuilder(name)

  case class KeyspaceBuilder(
    _name: String,
    _mapHostsEvery: Duration = 10.minutes,
    _retryAttempts: Int = 0,
    _requestTimeoutInMS: Int = 10000,
    _connectionTimeoutInMS: Int = 10000,
    _minConnectionsPerHost: Int = 1,
    _maxConnectionsPerHost: Int = 5,
    _removeAfterIdleForMS: Int = 60000) {

    /**
      * connect to the cluster with the specified parameters */
    def connect(): Keyspace = {
      val hosts = if (_mapHostsEvery > 0)
        // either map the cluster for this keyspace: TODO: use all seed hosts
        new ClusterRemapper(_name, seedHosts.head, _mapHostsEvery)
      else
        // or connect directly to the hosts that were given as seeds
        new SocketAddressCluster(seedHosts.map{ host => new InetSocketAddress(host, seedPort) }.toSeq)

      // TODO: move to builder pattern as well
      val ccp = new ClusterClientProvider(hosts, _name, _retryAttempts, _requestTimeoutInMS, _connectionTimeoutInMS, _minConnectionsPerHost, _maxConnectionsPerHost, _removeAfterIdleForMS)
      new Keyspace(_name, ccp)
    }

    /**
      * @param d Cassie will query the cassandra cluster every [[d]] period
      *          to refresh its host list. */
    def mapHostsEvery(d: Duration): KeyspaceBuilder = copy(_mapHostsEvery = d)
    def retryAttempts(r: Int): KeyspaceBuilder = copy(_retryAttempts = r)
    /**
      * @see requestTimeout in [[http://twitter.github.com/finagle/finagle-core/target/doc/main/api/com/twitter/finagle/builder/ClientBuilder.html]] */
    def requestTimeoutInMS(r: Int): KeyspaceBuilder = copy(_requestTimeoutInMS = r)
    /**
      * @see connectionTimeout in [[http://twitter.github.com/finagle/finagle-core/target/doc/main/api/com/twitter/finagle/builder/ClientBuilder.html]]*/
    def connectionTimeoutInMS(r: Int): KeyspaceBuilder = copy(_connectionTimeoutInMS = r)
    def minConnectionsPerHost(m: Int): KeyspaceBuilder =
      copy(_minConnectionsPerHost = m)
    def maxConnectionsPerHost(m: Int): KeyspaceBuilder =
      copy(_maxConnectionsPerHost = m)
    def removeAfterIdleForMS(r: Int): KeyspaceBuilder =
      copy(_removeAfterIdleForMS = r)
  }
}