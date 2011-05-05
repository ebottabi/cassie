package com.twitter.cassie.tests

import org.scalatest.matchers.MustMatchers
import org.mockito.Mockito.when
import org.scalatest.{BeforeAndAfterAll, Spec}
import com.twitter.cassie.tests.util.MockCassandraServer
import java.net.{SocketAddress, InetSocketAddress}
import org.apache.cassandra.finagle.thrift

import com.twitter.cassie.ClusterRemapper
import com.twitter.logging.Logger
import scala.collection.JavaConversions._
import com.twitter.conversions.time._

class ClusterRemapperTest extends Spec with MustMatchers with BeforeAndAfterAll {
  // val server = new MockCassandraServer(MockCassandraServer.choosePort())
  // val ring = tr("start", "end", "c1.example.com") ::
  //   tr("start", "end", "c2.example.com") :: Nil
  // when(server.cassandra.describe_ring("keyspace")).thenReturn(asJavaList(ring))
  //
  // def tr(start: String, end: String, endpoints: String*): thrift.TokenRange = {
  //   val tr = new thrift.TokenRange()
  //   tr.setStart_token(start)
  //   tr.setEnd_token(end)
  //   tr.setEndpoints(asJavaList(endpoints))
  // }
  //
  // override protected def beforeAll() {
  //   server.start()
  // }
  //
  // override protected def afterAll() {
  //   server.stop()
  // }

  // describe("mapping a cluster") {
  //   it("returns the set of nodes in the cluster") {
  //     val mapper = new ClusterRemapper("keyspace", "127.0.0.1", 10.minutes, server.port)
  //
  //     val mapped = mapper.fetchHosts(Seq(new InetSocketAddress("127.0.0.1", server.port)))
  //
  //     mapped must equal(List(
  //       addr("c1.example.com", server.port), addr("c2.example.com", server.port)
  //     ))
  //   }
  // }
  //
  // def addr(host: String, port: Int) = new InetSocketAddress(host, port)
}