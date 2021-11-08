package com.emmm.mobv

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.ViewModel
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
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

    fun sendMoney() {

        AsyncTask.execute {
            val server = Server("https://horizon-testnet.stellar.org")

            val source = KeyPair.fromSecretSeed("SBBNTQCXA3YADJ24HNUN3UW4X7FBO4UXMUH2XQ2UCZJOK6EKBC4M2KWA")
            val destination = KeyPair.fromAccountId("GB6QKHM7V55TZWC5GXDPVTMSTECAC26KEBWSMM4DC6GUBAV43VPD5MKZ")

            server.accounts().account(source.accountId)
            server.accounts().account(destination.accountId)

            val sourceAccount = server.accounts().account(source.accountId)

            val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
                .addOperation(
                    PaymentOperation.Builder(destination.accountId, AssetTypeNative(), "5000").build()
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
}