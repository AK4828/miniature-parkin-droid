package com.meruvian.pxc.selfservice.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.CategoryDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Category;
import com.meruvian.pxc.selfservice.entity.Order;
import com.meruvian.pxc.selfservice.entity.OrderMenu;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.meruvian.pxc.selfservice.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by miftakhul on 12/2/15.
 */
public class OrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView prodcutThumb;
    private TextView productName, productCategory, productNameSub, productPrice, productDesc, orderType, orderPrice, orderDesc;
    private Product product;
    private Category category;
    private ImageLoader imagePreview;
    private Spinner orderSpin;
    private FloatingActionButton orderButtonFloat;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout linearLayoutDesc;
    private CardView categoryLayout;


    private ProductDatabaseAdapter productDatabaseAdapter;
    private CategoryDatabaseAdapter categoryDatabaseAdapter;
    private OrderDatabaseAdapter orderDatabaseAdapter;
    private OrderMenuDatabaseAdapter orderMenuDatabaseAdapter;

    private int tx = 0, mutedColor, lightMutedColor, vibrantColor;
    private String productId, categoryId;
    private long orderMenuPrice;
    private List<Integer> orderCountList = new ArrayList<Integer>();
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private OrderMenu.OrderType orderMenuType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            tx = getIntent().getExtras().getInt("tx", 0);
            productId = getIntent().getExtras().getString("productId", null);

            product = productDatabaseAdapter.findAllProductById(productId);
            categoryId = product.getParentCategory().getId();
            Log.d("Category id      ","==="+product.getParentCategory().getId());
            category = categoryDatabaseAdapter.findCategoryById(categoryId);
            Log.d("Cek name", category.getName());
        }

        initSet();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                super.onBackPressed();
                break;
            case R.id.menu_cart :
                setResult(RESULT_OK);
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_order);
        prodcutThumb = (ImageView) findViewById(R.id.order_product_preview);
        productName = (TextView) findViewById(R.id.order_product_name);
        productNameSub = (TextView) findViewById(R.id.order_product_name_sub);
        productPrice = (TextView) findViewById(R.id.order_product_price);
        productDesc = (TextView) findViewById(R.id.order_product_desc);
        productCategory = (TextView) findViewById(R.id.order_product_category);
        orderType = (TextView) findViewById(R.id.order_type);
        orderPrice = (TextView) findViewById(R.id.order_price);
        orderDesc = (TextView) findViewById(R.id.order_desc);
        orderSpin = (Spinner) findViewById(R.id.order_spin);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        orderButtonFloat = (FloatingActionButton) findViewById(R.id.order_float_button);
        linearLayoutDesc = (LinearLayout) findViewById(R.id.linearLayout_desc);
        categoryLayout = (CardView) findViewById(R.id.layout_category);

        productDatabaseAdapter = new ProductDatabaseAdapter(this);
        categoryDatabaseAdapter = new CategoryDatabaseAdapter(this);
        orderDatabaseAdapter = new OrderDatabaseAdapter(this);
        orderMenuDatabaseAdapter = new OrderMenuDatabaseAdapter(this);
        imagePreview = ImageLoader.getInstance();
    }

    private void initSet() {
        String loadImage = SignageVariables.SERVER_URL+"/api/products/" + product.getId() + "/image?access_token=" + AuthenticationUtils.getCurrentAuthentication().getAccessToken();
        collapsingToolbarLayout.setTitle("Order " + product.getName());
        imagePreview.displayImage(loadImage, prodcutThumb);
        productName.setText(product.getName());
        productNameSub.setText(product.getName());
        productDesc.setText(product.getDescription());
        productPrice.setText(decimalFormat.format(product.getSellPrice()) + " Pt");
        productCategory.setText(category.getName());

        switch (tx) {
            case 0:
                orderMenuType = OrderMenu.OrderType.REQUESITION;
//                orderType.setText(OrderMenu.OrderType.REQUESITION.name());
                break;
            case 1:
                orderMenuType = OrderMenu.OrderType.QUOTATION;
//                orderType.setText(OrderMenu.OrderType.QUOTATION.name());
                break;
            case 2:
                orderMenuType = OrderMenu.OrderType.PURCHASE_ORDER;
//                orderType.setText(OrderMenu.OrderType.PURCHASE_ORDER.name());
                break;
        }

        orderType.setText(orderMenuType.name());

        for (int x = 1; x <= 100; x++) {
            orderCountList.add(x);
        }

        ArrayAdapter<Integer> orderSpinAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, orderCountList);

        orderSpin.setAdapter(orderSpinAdapter);
        orderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Long productPrice = product.getSellPrice();
                Long price = productPrice * (position + 1);

                orderMenuPrice = price;

                orderPrice.setText("Total : " + decimalFormat.format(productPrice) + " Pt");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderButtonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOrder();
            }
        });


        try{
            Bitmap linearBitmap = BitmapFactory.decodeFile(loadImage);
            Palette.from(linearBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    mutedColor = palette.getMutedColor(R.color.colorPrimaryDark);
                    lightMutedColor = palette.getLightMutedColor(R.color.colorPrimaryDark);
                    vibrantColor = palette.getVibrantColor(R.color.colorPrimaryDark);

                    linearLayoutDesc.setBackgroundColor(mutedColor);
                    categoryLayout.setCardBackgroundColor(vibrantColor);
                }
            });
        }catch (IllegalArgumentException e) {
            Log.e("Bitmap status", e.getMessage());
        }

    }

    public String saveOrder() {
        Order order = new Order();

        order.setSiteId("");
        order.setOrderType("1");
        order.setReceiptNumber("");
        order.getLogInformation().setCreateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
        order.getLogInformation().setCreateDate(new Date());
        order.getLogInformation().setLastUpdateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
        order.getLogInformation().setSite(AuthenticationUtils.getCurrentAuthentication().getSite().getId());

        order.setStatus(Order.OrderStatus.PROCESSED);

        return orderDatabaseAdapter.saveOrder(order);
    }

    private void dialogOrder(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Order");
        alert.setMessage("Order " + product.getName() + " ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String orderId = orderDatabaseAdapter.getOrderId();
                int q = Integer.parseInt(orderSpin.getSelectedItem().toString());

                if (orderId == null) {
                    orderId = saveOrder();
                    OrderMenu orderMenu = new OrderMenu();

                    orderMenu.getLogInformation().setCreateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
                    orderMenu.getLogInformation().setLastUpdateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
                    orderMenu.getLogInformation().setSite(AuthenticationUtils.getCurrentAuthentication().getSite().getId());

                    orderMenu.getOrder().setId(orderId);
                    orderMenu.setQty(q);
                    orderMenu.setProduct(product);
                    orderMenu.setSellPrice(orderMenuPrice);
                    orderMenu.setDescription(orderDesc.getText().toString());
                    orderMenu.setType(orderMenuType.name());

                    orderMenuDatabaseAdapter.saveOrderMenu(orderMenu);
                } else {
                    OrderMenu orderMenu = new OrderMenu();

                    orderMenu.getLogInformation().setCreateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
                    orderMenu.getLogInformation().setLastUpdateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
                    orderMenu.getLogInformation().setSite(AuthenticationUtils.getCurrentAuthentication().getSite().getId());

                    orderMenu.getOrder().setId(orderId);
                    orderMenu.setQty(q);
                    orderMenu.setProduct(product);
                    orderMenu.setSellPrice(orderMenuPrice);
                    orderMenu.setDescription(orderDesc.getText().toString());
                    orderMenu.setType(orderMenuType.name());

                    orderMenuDatabaseAdapter.saveOrderMenu(orderMenu);
                }

                dialogCart();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private void dialogCart(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Cart");
        alert.setMessage("Lihat keranjang belanja ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }


}
