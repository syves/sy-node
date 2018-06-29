import co.upvest.arweave4s.adt.Data
import co.upvest.arweave4s.adt.Transaction
import co.upvest.arweave4s.adt.Wallet._

import co.upvest.arweave4s.api //package includes id, price, ect, future, EncodedStringHandler
import co.upvest.arweave4s.api.Config

// Import response handler for Future
import co.upvest.arweave4s.api.future
import scala.concurrent.ExecutionContext.global

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.{higherKinds, postfixOps}
import scala.util.Try

import com.softwaremill.sttp.{Response, SttpBackend, UriContext, sttp, asString}
import com.softwaremill.sttp.HttpURLConnectionBackend
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend

object TestTutorial extends App {

  //then create a configuration context and weave-it-up.
  //implicit val c = api.Config(host = "TestHost", HttpURLConnectionBackend())

  /*def estimate[F[_], G[_]](d: Data)(implicit
  c: AbstractConfig[F, G], esh: EncodedStringHandler[F]
  */

  // Create configuration. Set the sttp's async backend for returning Futures
  implicit val c = Config(host = "TestHost", AsyncHttpClientFutureBackend())
  implicit val esh = EncodedStringHandler[String]

  //val curBlock =  api.block.current()

  // Data to persist on the blockchain.
  val testData = Data(bytes = "Hi Mom!".getBytes("UTF-8"))
  // Let's get a new wallet
  val wallet = co.upvest.arweave4s.adt.Wallet.generate()



  for {
      // using the API method to estimate the price for the transaction in `Winstons`
      price    <- api.price.estimate(testData)(c, esh)
      // Get the last transaction of the sender wallet
      lastTx   <- api.address.lastTx(wallet)
      // Construct and send the transaction.
      ()       <- api.tx.submit(Transaction.Data(
            id     = Transaction.Id.generate(),
            lastTx = lastTx,
            owner  = wallet,
            data   = testData,
            reward = price)
            // Here we actually sign our transaction
            .sign(wallet)
          )
        } yield ()

  //println(wallet.owner)
  //println(testData)
  //println(curBlock)
}
