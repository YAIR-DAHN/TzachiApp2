package com.shahareinisim.tzachiapp;

import static com.shahareinisim.tzachiapp.MainActivity.DONATION_LINK;
import static com.shahareinisim.tzachiapp.MainActivity.MAIN_WEB_LINK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.net.URISyntaxException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WebViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebViewFragment newInstance(String param1, String param2) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    WebView webView;
    LinearProgressIndicator progressBar;
    MaterialCardView adView;
    Button adButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_web_view, container, false);

        progressBar = root.findViewById(R.id.progress_bar);
        adView = root.findViewById(R.id.ad_view);
        adButton = root.findViewById(R.id.ad_button);
        webView = root.findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(IntentFilter());
        webView.loadUrl(mParam1);

        ((TextView) root.findViewById(R.id.ad_text)).setSelected(true);
        adButton.setOnClickListener(view -> {
            if (!mParam1.equals(DONATION_LINK)) webView.loadUrl(DONATION_LINK);
        });

        return root;
    }

    public boolean canGoBack() {

        if (webView == null) return false;
        return webView.canGoBack();
    }

    public void goBack() {
        if (webView != null) webView.goBack();
    }

    public WebViewClient IntentFilter() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                Log.d("##### WebView URL #####", uri.toString() + "\n" + uri.getScheme());
                switch (uri.getScheme()) {
                    case "tel":
                        intentDial(uri);
                        return true;
                    case "mailto":
                        openMailApp(uri);
                        return true;
                    case "intent":
                        intentOpenApp(uri, getContext());
                        return true;
                    case "whatsapp":
                        openWhatsapp(uri);
                        return true;
                }

                if (uri.toString().startsWith("https://play.google.com/store/apps/details")) {
                    openPlayStore(uri);
                    return true;
                }

//                if (uri.toString().startsWith("http")) {
//                    FragmentManager fm = requireActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fm.beginTransaction()
//                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    fragmentTransaction.add(R.id.fragment_container, WebViewFragment.newInstance(uri.toString(), ""), "tag");
//                    fragmentTransaction.addToBackStack("");
//
//                    fragmentTransaction.commit();
//                    return true;
//                }
//
//                return true;

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (url.startsWith(MAIN_WEB_LINK))
                    adView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                if (!url.startsWith(MAIN_WEB_LINK))
                    adView.setVisibility(View.GONE);
            }
        };
    }

    private void intentDial(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    private void openMailApp(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(intent);
    }

    private void intentOpenApp(Uri uri, Context context) {
        Intent intent = null;
        try {
            intent = Intent.parseUri(String.valueOf(uri), Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            Toast.makeText(context, "null intent", Toast.LENGTH_SHORT).show();
        }

        if (intent != null) {
            PackageManager packageManager = context.getPackageManager();
            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (info != null) {
                context.startActivity(intent);
            } else {
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
//                                view.loadUrl(fallbackUrl);

                // or call external broswer
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
                context.startActivity(browserIntent);
            }
        }
    }

    private void openWhatsapp(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        try {
            intent.setData(uri);
            startActivity(intent);
            webView.goBack();
        } catch (ActivityNotFoundException e) {
            String fallbackUrl = "https://play.google.com/store/apps/details?id=com.whatsapp";
            openPlayStore(Uri.parse(fallbackUrl));
        }
    }

    private void openPlayStore(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
            Snackbar.make(requireView(), "No App Store Found!", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

}