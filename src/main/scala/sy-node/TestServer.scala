import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.{higherKinds, postfixOps}

import co.upvest.arweave4s.adt.Data
import co.upvest.arweave4s.adt.Transaction
import co.upvest.arweave4s.adt.Wallet._
import co.upvest.arweave4s.api
import co.upvest.arweave4s.api.Config
// Import response handler for Future
import co.upvest.arweave4s.api.future._

import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend


object TestTutorial extends App {

  // Create configuration. Set the sttp's async backend for returning Futures
  val TestHost = (sys.env get "TESTNET_HOST" getOrElse "localhost") + ":1984"
  implicit val c = Config(host = TestHost, AsyncHttpClientFutureBackend())

  // Data to persist on the blockchain.
  val testData = Data(bytes = "Hi Mom!".getBytes("UTF-8"))
  // Let's get a new wallet
  val wallet = co.upvest.arweave4s.adt.Wallet.generate()
  val curBlock =  api.block.current()

  for {
      // using the API method to estimate the price for the transaction in `Winstons`
      price    <- api.price.estimate(testData)
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

  println(wallet.owner)
  println(testData)
  println(curBlock)
}
