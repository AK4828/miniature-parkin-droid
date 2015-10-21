package org.meruvian.esales.collector.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by meruvian on 16/09/15.
 */
public class ScannerImeiFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getActivity(), "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        /*Fragment fg = new BuyerOrderDetailFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fg);
        transaction.addToBackStack(null);
        transaction.commit();*/

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

}
