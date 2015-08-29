package com.hoqii.sales.selfservice.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.activity.ImageZoomActivity;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.CartMenu;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.entity.Product;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;
import com.hoqii.sales.selfservice.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by ludviantoovandi on 29/01/15.
 */
public class ProductDetailFragment extends DefaultFragment {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private GestureDetector gestureDetector;

    @InjectView(R.id.image) ImageView image;
    @InjectView(R.id.text_title) TextView title;
    @InjectView(R.id.text_description) TextView description;
    @InjectView(R.id.text_price) TextView price;
    @InjectView(R.id.progressbar) ProgressBar progressBar;
    @InjectView(R.id.next) View btnNext;
    @InjectView(R.id.previous) View btnPrev;

    @InjectView(R.id.text_menu_price) TextView textMenuPrice;
    @InjectView(R.id.edit_quantity) EditText editQuantity;
    @InjectView(R.id.button_plus) Button btnPlus;
    @InjectView(R.id.button_minus) Button btnMinus;
    @InjectView(R.id.edit_order_desc) TextView orderDesc;

    private ProductDatabaseAdapter productDbAdapter;
    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;

//    private CartDatabaseAdapter cartDbAdapter;
//    private CartMenuDatabaseAdapter cartMenuDbAdapter;

    private ArrayList<String> products;
    private String productOrderId;
    private int position;
    private boolean isFirst;
    private boolean isLast;
    private double priceMenu = 0, sum = 0;
    private String desc;
    private String productId;
    private String orderId;
    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
    private List<CartMenu> cartMenus = new ArrayList<CartMenu>();
    private String cartId;
    private SharedPreferences preferences;

    @Override
    protected int layout() {
        return R.layout.fragment_swipe_product_detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        products = getArguments().getStringArrayList("products");
        position = getArguments().getInt("position");

        productId = products.get(position);
        isFirst = position <= 0;
        isLast = position == (products.size() - 1);

        if (isFirst) {
            btnPrev.setVisibility(View.INVISIBLE);
        }

        if (isLast) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        imageLoader.displayImage("file://" + ImageUtil.getImagePath(getActivity(), productId), image,  new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
                image.setImageResource(R.drawable.no_image);
            }
        });

        productDbAdapter = new ProductDatabaseAdapter(getActivity());
        orderDbAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(getActivity());

        Product product = productDbAdapter.findProductById(productId);
//        productId = product.getId();
        priceMenu = product.getSellPrice();
        String desc = product.getDescription();

        title.setText(product.getName());

//        description.setText(Html.fromHtml(desc));
        description.setText(desc);
        price.setText("Rp" + decimalFormat.format(priceMenu));
//        price.setText("Rp" + decimalFormat.format(product.getSellPrice()));

        if (product.getSellPrice() <= 0) {
            price.setVisibility(View.GONE);
        } else {
            price.setVisibility(View.VISIBLE);
        }

        gestureDetector = new GestureDetector(getActivity(), new SwipeGestureDetector() {
            @Override
            public void onSwipeLeft() {
                nextProduct();
            }

            @Override
            public void onSwipeRight() {
                previousProduct();
            }
        });

        calculateOrder();

        editQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String quantity = editQuantity.getText().toString().length() == 0 ? "1" : editQuantity.getText().toString();
                int last = Integer.parseInt(quantity);
//                CalculateAll();

                sum = priceMenu * last ;
                String sumPrice = decimalFormat.format(sum);

                textMenuPrice.setText("Rp " + sumPrice);

                Log.i(getClass().getSimpleName(), "Total harga: " + sumPrice);

                if(last > 1) {
                    btnMinus.setEnabled(true);
                } else {
                    btnMinus.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editQuantity.getText().toString().isEmpty() || editQuantity.getText().toString().equals("0")){
                    editQuantity.setText("1");
                }
            }
        });
    }

    @OnClick(R.id.image)
    public void onClick(ImageView imageView) {
        if (imageView.getId() == R.id.image) {
            Intent intent = new Intent(getActivity(), ImageZoomActivity.class);
            intent.putExtra("product_id", products.get(position));

            startActivity(intent);
        }
    }

    @OnTouch(R.id.detail_content)
    public boolean onSwipe(ScrollView view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @OnClick(R.id.next)
    public void nextProduct() {
        if (!isLast) {
            changeContent(products, position + 1);
        }
    }

    @OnClick(R.id.close)
    public void back() {
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.previous)
    public void previousProduct() {
        if (!isFirst) {
            changeContent(products, position - 1);
        }
    }

    @OnClick(R.id.button_plus)
    public void addQuantity() {

        String quantity = editQuantity.getText().toString();
        int last = (Integer.parseInt(quantity.length() == 0 ? "0" : quantity)) + 1;
        Log.v(getClass().getSimpleName(), last + "+++");
        editQuantity.setText(String.valueOf(last));
//        CalculateAll();

        if(last > 0) {
            sum = priceMenu * last;
            String sumPrice = decimalFormat.format(sum);

            textMenuPrice.setText("Rp " + sumPrice);
            btnMinus.setEnabled(true);
        } else {
            sum = priceMenu * last;
            String sumPrice = decimalFormat.format(sum);

            textMenuPrice.setText("Rp " + sumPrice);
        }
    }

    @OnClick(R.id.button_minus)
    public void subtractQuantity() {
        String quantity = editQuantity.getText().toString();
        int last = (Integer.parseInt(quantity.length() == 0 ? "0" : quantity)) - 1;
        Log.v(getClass().getSimpleName(), last + "+++");
        editQuantity.setText(String.valueOf(last));
//        CalculateAll();

        if(last > 1) {
            sum = priceMenu * last;
            String sumPrice = decimalFormat.format(sum);

            textMenuPrice.setText("Rp " + sumPrice);
        } else {
            sum = priceMenu * last;
            String sumPrice = decimalFormat.format(sum);
            btnMinus.setEnabled(false);

            textMenuPrice.setText("Rp " + sumPrice);
        }
    }

    @OnClick(R.id.add_to_cart)
    public void addToCart() {
        String orderId = orderDbAdapter.getOrderId();
        int q = Integer.parseInt(editQuantity.getText().toString());

        if(orderDesc.getText().toString().isEmpty()) {
            desc = "";
        } else {
            desc = orderDesc.getText().toString();
        }

        if(orderId == null) {
            Log.d(getClass().getSimpleName(), "Order Id = NULL");

            orderId = saveOrder();
            OrderMenu orderMenu = new OrderMenu();
            Product product = new Product();
            product.setId(productId);

            orderMenu.getOrder().setId(orderId);
            orderMenu.setQty(q);
            orderMenu.setProduct(product);
            orderMenu.setSellPrice(priceMenu);
            orderMenu.setDescription(desc);

            Log.d(getClass().getSimpleName(), "1.Order Id = " + orderId);
            Log.d(getClass().getSimpleName(), "1.Product Id = " + productId);

            orderMenuDbAdapter.saveOrderMenu(orderMenu);
        } else {
            Log.d(getClass().getSimpleName(), "2.Order Id = " + orderId);
            Log.d(getClass().getSimpleName(), "2.Product Id = " + productId);

            OrderMenu orderMenu = new OrderMenu();
            Product product = new Product();
            product.setId(productId);

            orderMenu.getOrder().setId(orderId);
            orderMenu.setQty(q);
            orderMenu.setProduct(product);
            orderMenu.setSellPrice(priceMenu);
            orderMenu.setDescription(desc);

            orderMenuDbAdapter.saveOrderMenu(orderMenu);
        }

        dialogItemAddedToCart();
//        Toast.makeText(getActivity(), "Barang telah ditambahkan ke Cart", Toast.LENGTH_SHORT).show();
    }

    public String saveOrder() {
        Order order = new Order();

        order.setSiteId("");
        order.setOrderType("1");
        order.setReceiptNumber("");
        order.getLogInformation().setCreateBy(AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        order.getLogInformation().setCreateDate(new Date());

        return orderDbAdapter.saveOrder(order);
    }

    private void calculateOrder(){
        String quantity = editQuantity.getText().toString().length() == 0 ? "1" : editQuantity.getText().toString();
        int last = Integer.parseInt(quantity);

        sum = priceMenu * last ;
        String sumPrice = decimalFormat.format(sum);

        textMenuPrice.setText("Rp " + sumPrice);
    }

    private void dialogItemAddedToCart() {
        View view = View.inflate(getActivity(), R.layout.view_add_to_cart, null);

        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);

        orderId = orderDbAdapter.getOrderId();
        if(orderId != null) {
            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);
        }

        textItem.setText("Anda memiliki " + orderMenus.size() + " item di dalam keranjang belanja Anda");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(R.string.added_to_cart);
        builder.setPositiveButton(getString(R.string.go_to_cart), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fragment fragment = new OrderListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
//                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
            }
        });
        builder.setNegativeButton(getString(R.string.continue_shopping), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeContent(ArrayList<String> products, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putStringArrayList("products", products);

        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        fm.popBackStack();

        FragmentTransaction tx = fm.beginTransaction();
        FragmentTransactionExtended ext = new FragmentTransactionExtended(getActivity(), tx, this, fragment, R.id.container);
        ext.addTransition(FragmentTransactionExtended.FADE);
        ext.commit();
    }

    private String imageUrl(String id) {
//        return SignageVariables.SERVER_URL + "/product/image/" + id;
        return preferences.getString("server_url", "") + "/api/product/image/" + id;
    }

    private abstract class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwipeLeft();

                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwipeRight();

                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        public abstract void onSwipeLeft();

        public abstract void onSwipeRight();
    }
}
