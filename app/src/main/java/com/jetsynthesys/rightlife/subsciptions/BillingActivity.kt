package com.jetsynthesys.rightlife.subsciptions

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.jetsynthesys.rightlife.R

class BillingActivity : AppCompatActivity(), PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient
    private lateinit var purchaseButton: Button
    private lateinit var subscriptionButton: Button
    private lateinit var subscriptionStatusText: TextView

    private val consumableProductId = "product_test_1"
    private val subscriptionProductId = "bundle_monthly"
    private var receivedProductType = "BOOSTER" // Default value, can be overridden by intent
    private var receivedProductId: String? = null // Will be set from intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)
         receivedProductId = intent.getStringExtra("PRODUCT_ID").toString() // Use the same key
         receivedProductType = intent.getStringExtra("PRODUCT_TYPE").toString() // Use the same key


        purchaseButton = findViewById(R.id.purchaseButton)
        subscriptionButton = findViewById(R.id.subscriptionButton)
        subscriptionStatusText = findViewById(R.id.subscriptionStatusText)

        setupBillingClient()

        purchaseButton.setOnClickListener {
            if (receivedProductType == "BOOSTER") {
                if (receivedProductId != null) {
                    queryProductDetails(receivedProductId!!, BillingClient.ProductType.INAPP)
                }
            }else if (receivedProductType == "SUBSCRIPTION") {
                if (receivedProductId != null) {
                    queryProductDetails(receivedProductId!!, BillingClient.ProductType.SUBS)
                }
            }

        }

        subscriptionButton.setOnClickListener {
            queryProductDetails(subscriptionProductId, BillingClient.ProductType.SUBS)
        }
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("Billing", "Billing setup successful")
                    //restoreSubscription()
                    if (receivedProductType == "BOOSTER") {
                        if (receivedProductId != null) {
                            queryProductDetails(receivedProductId!!, BillingClient.ProductType.INAPP)
                            //queryProductDetails("product_test_2", BillingClient.ProductType.INAPP)
                        }
                    }else if (receivedProductType == "SUBSCRIPTION") {
                        if (receivedProductId != null) {
                            queryProductDetails(receivedProductId!!, BillingClient.ProductType.SUBS)
                        }
                    }
                } else {
                    Log.e("Billing", "Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w("Billing", "Billing service disconnected")
                setupBillingClient()
            }
        })
    }

    private fun queryProductDetails(productId: String, productType: String) {
        Log.d("Billing", "Querying product: $productId of type: $productType")
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(productType)
                        .build()
                )
            ).build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (productDetailsList.isNotEmpty()) {
                    launchBillingFlow(productDetailsList[0])
                } else {
                    Toast.makeText(this, "Product not found: $productId", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Query failed: ${billingResult.debugMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchBillingFlow(productDetails: ProductDetails) {
        if (productDetails.productType == BillingClient.ProductType.SUBS) {
            val offerDetails = productDetails.subscriptionOfferDetails?.firstOrNull()
            if (offerDetails == null) {
                Toast.makeText(this, "No subscription offers available", Toast.LENGTH_SHORT).show()
                return
            }

            val offerToken = offerDetails.offerToken
            if (offerToken.isNullOrEmpty()) {
                Toast.makeText(this, "Offer token missing", Toast.LENGTH_SHORT).show()
                return
            }
            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken) // ✅ REQUIRED
                .build()

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()

            billingClient.launchBillingFlow(this, billingFlowParams)
        } else {
            // For consumables or non-subscription
            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()

            billingClient.launchBillingFlow(this, billingFlowParams)
        }
    }

    /*fun launchBillingFlow(productDetails: ProductDetails) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()

        val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
        Log.d("Billing", "Launch result: ${billingResult.responseCode} - ${billingResult.debugMessage}")
    }*/
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "Purchase canceled", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Purchase error: ${billingResult.debugMessage}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            val productId = purchase.products.firstOrNull()

            when (receivedProductType) {
                "BOOSTER" -> {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            Toast.makeText(this, "Consumable purchase successful", Toast.LENGTH_SHORT).show()
                            //call your method to update UI or backend
                        } else {
                            Toast.makeText(this, "Consume failed: ${billingResult.debugMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "SUBSCRIPTION" -> {
                    if (!purchase.isAcknowledged) {
                        val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()

                        billingClient.acknowledgePurchase(acknowledgeParams) { result ->
                            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                Toast.makeText(this, "Subscription acknowledged", Toast.LENGTH_SHORT).show()
                                //call your method to update UI or backend
                                showSubscriptionStatus(purchase)
                            } else {
                                Toast.makeText(this, "Acknowledge failed: ${result.debugMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        showSubscriptionStatus(purchase)
                    }
                }
            }
        }
    }

    private fun restoreSubscription() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchasesList) {
                    handlePurchase(purchase)
                }
            }
        }
    }

    private fun showSubscriptionStatus(purchase: Purchase) {
        val purchaseTime = java.text.SimpleDateFormat("dd MMM yyyy HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date(purchase.purchaseTime))
        subscriptionStatusText.text = "Subscription Active\nStart: $purchaseTime"
    }
}
