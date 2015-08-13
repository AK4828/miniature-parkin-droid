package com.hoqii.sales.selfservice.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.CampaignAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.CampaignDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.CampaignDetailDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.CampaignDetail;
import com.hoqii.sales.selfservice.entity.Promo;

import org.meruvian.midas.core.defaults.DefaultActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by ludviantoovandi on 05/02/15.
 */
public class PromoActivity extends DefaultActivity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @InjectView(R.id.viewflipper_promo) AdapterViewFlipper promo;

    private CampaignAdapter campaignAdapter;
    private CampaignDatabaseAdapter campaignDatabaseAdapter;
    private CampaignDetailDatabaseAdapter campaignDetailDbAdapter;

    private GestureDetector detector;

    private SharedPreferences preferences;

    private int close = 1;

    @Override
    protected int layout() {
        return R.layout.activity_promo;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        campaignDetailDbAdapter = new CampaignDetailDatabaseAdapter(this);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    decorView.setSystemUiVisibility(uiOptions);
                                }
                            }, 3000);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });

        detector = new GestureDetector(this, new SwipeGestureDetector());

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        campaignAdapter = new CampaignAdapter(this, R.layout.adapter_promo, dataCampaign());
        promo.setAdapter(campaignAdapter);

        if (preferences.getBoolean("slide_show", false)) {
            promo.setAutoStart(true);
            promo.setFlipInterval(Integer.parseInt(preferences.getString("slide_timer", "0")) * 1000);
            promo.startFlipping();
        }

        promo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                promo.stopFlipping();

                if (preferences.getBoolean("slide_show", false)) {
                    startFlipping();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        Handler handler = new Handler();

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (close == 1) {
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_LONG).show();
                close++;

                handler.postDelayed(new Runnable() {
                    public void run() {
                        close = 1;
                    }
                }, 1500);
            } else if (close > 1) {
                finish();
            }
        }
    }

    private void startFlipping() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                promo.startFlipping();
            }
        }, Integer.parseInt(preferences.getString("slide_idle", "0")) * 60 * 1000);
    }

    private List<CampaignDetail> dataCampaign(){
        List<CampaignDetail> campaignDetails = new ArrayList<CampaignDetail>();

        campaignDetails = campaignDetailDbAdapter.findAllActiveCampaignDetail();

        return  campaignDetails;
    }

    private List<Promo> data() {
        List<Promo> promos = new ArrayList<Promo>();

        Promo promo1 = new Promo();
        promo1.setImage(R.drawable.image_1);
        promo1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam in enim et tellus efficitur aliquam nec vel sem. Fusce ac justo tellus. Suspendisse at felis pharetra nisi dapibus commodo. Fusce sed nunc quis turpis bibendum egestas. Praesent condimentum ullamcorper velit eget posuere. In ornare vitae metus non consequat. Aliquam erat volutpat. Maecenas ullamcorper mauris est, pulvinar aliquam odio porttitor vitae. Fusce nec fermentum urna, nec consectetur diam. Integer consequat risus in dolor consectetur, sed mollis libero maximus. In hac habitasse platea dictumst.");
        promos.add(promo1);

        Promo promo2 = new Promo();
        promo2.setImage(R.drawable.image_2);
        promo2.setDescription("Nunc dapibus erat ac fringilla fermentum. Integer porta nisi eu ullamcorper commodo. Suspendisse potenti. Vivamus dolor tortor, suscipit ac ante at, convallis consequat lorem. Vivamus vestibulum ornare diam, ut vestibulum leo pharetra a. Fusce dapibus neque sapien, sed venenatis odio accumsan fringilla. Vestibulum sed sapien non nulla suscipit fermentum ut nec neque. Sed bibendum dignissim luctus. Nam et elit id sapien cursus bibendum. Etiam et turpis commodo, rutrum orci vitae, efficitur leo. Curabitur eu cursus libero, sed fringilla dui. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. In facilisis justo a lectus porttitor, quis lacinia arcu euismod. Morbi rhoncus, augue sed viverra fermentum, libero orci sollicitudin dolor, ac condimentum mauris sapien vitae massa. Ut et felis blandit, luctus libero et, tincidunt sapien. Duis ut urna non justo pulvinar vulputate.");
        promos.add(promo2);

        Promo promo3 = new Promo();
        promo3.setImage(R.drawable.image_3);
        promo3.setDescription("Donec purus metus, condimentum vel lectus sed, rutrum hendrerit lorem. Integer justo elit, tempus non malesuada ac, molestie eget ex. Maecenas sit amet lacinia nibh. Morbi lobortis tempus fermentum. Cras sed arcu euismod, rhoncus tortor ut, rhoncus dui. In sit amet leo id lectus vehicula lobortis eu in nisl. Aenean mattis suscipit dui, ac ultricies mauris tempor ac. Phasellus eu dolor ac risus mattis feugiat. Mauris vitae posuere velit. In nec rhoncus dui.");
        promos.add(promo3);

        Promo promo4 = new Promo();
        promo4.setImage(R.drawable.image_4);
        promo4.setDescription("Curabitur tempus tellus et aliquam mollis. Sed tincidunt leo ligula. Mauris et placerat lorem, et lacinia arcu. Donec congue cursus vulputate. Donec ut interdum turpis. In fringilla gravida est a rhoncus. Sed dignissim purus ut turpis euismod vestibulum. Sed vulputate iaculis maximus. Nam quis arcu ultricies, venenatis ex in, bibendum ante. Donec malesuada tortor et lorem sodales vulputate. Nulla facilisi. Aenean aliquam libero et dapibus venenatis. Proin interdum leo nec libero mollis congue.");
        promos.add(promo4);

        Promo promo5 = new Promo();
        promo5.setImage(R.drawable.image_5);
        promo5.setDescription("Morbi vel nisi nisi. Quisque orci elit, fringilla at felis nec, porttitor varius sem. Quisque condimentum cursus ipsum a euismod. Praesent pellentesque odio vel enim fermentum, non accumsan augue venenatis. Nunc eget elit elementum, porttitor nunc at, mollis est. Interdum et malesuada fames ac ante ipsum primis in faucibus. Aliquam mattis erat sapien, pretium blandit dolor cursus a. Curabitur sed hendrerit elit, vestibulum posuere est. Nam vitae cursus augue. Mauris viverra diam a aliquet sagittis. Quisque ut turpis tincidunt, interdum libero ut, consectetur ante.");
        promos.add(promo5);

        return promos;
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    promo.showNext();

                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    promo.showPrevious();

                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        finish();
    }
}
