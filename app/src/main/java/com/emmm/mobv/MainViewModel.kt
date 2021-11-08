package com.emmm.mobv

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.ViewModel
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


class MainViewModel : ViewModel() {

    fun createAccount() {
        AsyncTask.execute {
            val pair: KeyPair = KeyPair.random()

            Log.i("TEEEEEEEEEEEEEEEEST", String(pair.secretSeed))
            Log.i("TEEEEEEEEEEEEEEEEST", pair.accountId)


            fundMoney(pair.accountId)

//            checkBalance(pair.accountId)
        }
    }

    fun fundMoney(accountId: String) {
        val friendbotUrl = java.lang.String.format(
            "https://friendbot.stellar.org/?addr=%s",
            accountId
        )
        val response: InputStream = URL(friendbotUrl).openStream()
        val body: String = Scanner(response, "UTF-8").useDelimiter("\\A").next()
        Log.i("TEEEEEEEEEEEEEEEEST", "SUCCESS! You have a new account :)\n$body")
    }

    fun checkBalance(accountId: String) {
        AsyncTask.execute {
            val server = Server("https://horizon-testnet.stellar.org")
            val account: AccountResponse = server.accounts().account(accountId)
            Log.i("TEEEEEEEEEEEEEEEEST", "Balances for account " + accountId)
            val balances = account.balances
            for (balance in balances) {
                Log.i(
                    "TEEEEEEEEEEEEEEEEST", String.format(
                        "Type: %s, Code: %s, Balance: %s%n",
                        balance.assetType,
                        balance.assetCode,
                        balance.balance
                    )
                )
            }
        }
    }

    fun sendMoney(fromAccountSecret: String, toAccountId: String, amount: String) {

        AsyncTask.execute {
            val server = Server("https://horizon-testnet.stellar.org")

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
                println("Success!")
                println(response)
            } catch (e: Exception) {
                println("Something went wrong!")
                println(e.message)

            }
        }
    }

    fun showTransactions(accountId: String) {
        AsyncTask.execute {
            val output: StringBuilder = StringBuilder()

            val server = Server("https://horizon-testnet.stellar.org")

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

                    Log.i("TEEEEEEEEEEEEEEEEST", "transakcie\n\n$output")
                } else if (payment is CreateAccountOperationResponse) {
                    output.append("Funder ")
                    output.append(payment.funder)
                    output.append("\nStarting balance ")
                    output.append(payment.startingBalance)
                    Log.i("TEEEEEEEEEEEEEEEEST", "else vetva")
                }
            }
        }
    }
}