package com.emmm.mobv.util

import android.util.Log
import org.stellar.sdk.*
import org.stellar.sdk.requests.PaymentsRequestBuilder
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import java.io.InputStream
import java.net.URL
import java.util.*

class StellarUtil {

    companion object {
        val TESTNET_URL = "https://horizon-testnet.stellar.org"
        private const val FRIEND_BOT_URL_FORMAT = "https://friendbot.stellar.org/?addr=%s"

        suspend fun createAccount(): String {
            Log.i("StellarUtil", "creating new Stellar account")

            val pair: KeyPair = KeyPair.random()

            //TODO remove secretSeed logging
            Log.i(
                "StellarUtil",
                "new account created, accountId=${pair.accountId} \nsecretSeed=${pair.secretSeed}"
            )

            fundMoneyFromFriendBot(pair.accountId)

            //TODO ugly but..., change later
            return pair.secretSeed.toString()
        }

        private suspend fun fundMoneyFromFriendBot(accountId: String) {
            Log.i("StellarUtil", "funding money from friend bot")

            val friendBotUrl = java.lang.String.format(FRIEND_BOT_URL_FORMAT, accountId)
            val response: InputStream = URL(friendBotUrl).openStream()
            val body: String = Scanner(response, "UTF-8").useDelimiter("\\A").next()
            Log.i("StellarUtil", "successfully funded money to new account \n$body")
        }

        fun checkBalance(accountId: String) {
            Log.i("StellarUtil", "checking balance for account $accountId")

            val server = Server(TESTNET_URL)
            val account: AccountResponse = server.accounts().account(accountId)
            val balances = account.balances
            for (balance in balances) {
                Log.i(
                    "StellarUtil",
                    String.format(
                        "Balance: %s, Type: %s, Code: %s %n",
                        balance.balance,
                        balance.assetType,
                        balance.assetCode
                    )
                )
            }
        }

        fun sendMoney(fromAccountSecret: String, toAccountId: String, amount: String) {
            Log.i("StellarUtil", "sending money from $fromAccountSecret to $toAccountId")

            val server = Server(TESTNET_URL)
            val source = KeyPair.fromSecretSeed(fromAccountSecret)
            val destination = KeyPair.fromAccountId(toAccountId)

            server.accounts().account(source.accountId)
            server.accounts().account(destination.accountId)

            val sourceAccount = server.accounts().account(source.accountId)

            val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
                .addOperation(
                    PaymentOperation.Builder(destination.accountId, AssetTypeNative(), amount).build()
                )
                .addMemo(Memo.text("Test Transaction"))
                .setTimeout(180)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build()
            transaction.sign(source)

            try {
                val response: SubmitTransactionResponse = server.submitTransaction(transaction)
                Log.i("StellarUtil", "sending money successful\n$response")
            } catch (e: Exception) {
                Log.i("StellarUtil", "error while sending money\n${e.message}")
            }
        }

        fun showTransactions(accountId: String) {
            val output: StringBuilder = StringBuilder()

            val server = Server(TESTNET_URL)
            val account = KeyPair.fromAccountId(accountId)
            val paymentRequestBuilder: PaymentsRequestBuilder = server.payments().forAccount(account.accountId)
            val operations: ArrayList<OperationResponse> = paymentRequestBuilder.execute().records

            for (payment in operations) {
                if (payment is PaymentOperationResponse) {
                    val amount: String = payment.amount
                    val asset: Asset = payment.asset
                    val assetName: String = if (asset == AssetTypeNative()) {
                        "lumens"
                    } else {
                        val assetNameBuilder: StringBuilder = StringBuilder()
                        assetNameBuilder.append(((asset as AssetTypeCreditAlphaNum).code))
                        assetNameBuilder.append(":")
                        assetNameBuilder.append(asset.issuer)
                        assetNameBuilder.toString()
                    }
                    output.append(amount)
                    output.append(" ")
                    output.append(assetName)
                    output.append("\n from ")
                    output.append(payment.from)
                    output.append("\n to ")
                    output.append(payment.to)
                    output.append("\n\n   ")
                } else if (payment is CreateAccountOperationResponse) {
                    output.append("Funder ")
                    output.append(payment.funder)
                    output.append("\nStarting balance ")
                    output.append(payment.startingBalance)
                }
            }
            Log.i("StellarUtil", "transactions for $accountId\n$output")
        }

        fun getAccountIdFromSecretSeed(secretSeed: String): String {
            return KeyPair.fromSecretSeed(secretSeed).accountId
        }
    }
}