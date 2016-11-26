package ca.ualberta.cs.drivr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

public class PaymentActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient mGoogleApiClient;
    private SupportWalletFragment mWalletFragment;

    private MaskedWallet mMaskedWallet;
    private FullWallet mFullWallet;

    public static final int MASKED_WALLET_REQUEST_CODE = 888;
    public static final int FULL_WALLET_REQUEST_CODE = 889;

    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);





        if(mWalletFragment == null) {
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setMaskedWalletDetailsBackgroundColor(ContextCompat.getColor(this,android.R.color.white))
                    .setMaskedWalletDetailsBackgroundResource(android.R.color.holo_blue_bright)
                   ;

            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_DARK)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();

            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);


            WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                    .setMaskedWalletRequest(generateMaskedWalletRequest())
                    .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE);
            mWalletFragment.initialize(startParamsBuilder.build());

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                .commit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();

        setContentView(R.layout.activity_payment);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case MASKED_WALLET_REQUEST_CODE :
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mMaskedWallet =
                                data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        break;

                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case FULL_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        mFullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        Toast.makeText(this, mFullWallet.getProxyCard().getPan().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case WalletConstants.RESULT_ERROR:
                Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private MaskedWalletRequest generateMaskedWalletRequest() {
        MaskedWalletRequest maskedWalletRequest = MaskedWalletRequest.newBuilder()
                .setMerchantName("Google I/O Codelab")
                .setPhoneNumberRequired(true)
                .setShippingAddressRequired(true)
                .setCurrencyCode("USD")
                .setEstimatedTotalPrice("10.00")
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.00")
                        .addLineItem(LineItem.newBuilder().setCurrencyCode("USD")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .build())
                .build();
        return maskedWalletRequest;
    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionID) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.10")
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice(".10")
                                .build())
                        .build())
                .build();
        return fullWalletRequest;
    }

    public void requestFullWallet(View view) {
        if(mGoogleApiClient.isConnected()) {
            Wallet.Payments.loadFullWallet(mGoogleApiClient,
                    generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
                    FULL_WALLET_REQUEST_CODE);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}