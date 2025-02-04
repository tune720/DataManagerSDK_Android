package com.datamanager.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.enliple.datamanagersdk.ENDataManager;
import com.enliple.datamanagersdk.Utils.LogPrint;
import com.enliple.datamanagersdk.events.EventModel;

import java.util.ArrayList;
import java.util.UUID;


public class ProductInputActivity extends AppCompatActivity {

    private LinearLayout layoutInputRoot;

    Bundle dataBundle;
    ArrayList<Bundle> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_input);

        layoutInputRoot = findViewById(R.id.linearLayoutInputViewRoot);

        findViewById(R.id.buttonAddParam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                CustomInputView customInputView = new CustomInputView(ProductInputActivity.this);
                customInputView.updateView("", dataBundle, CustomInputView.InputType.TEXT, null);
                layoutInputRoot.addView(customInputView);

            }
        });

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
//                submitData();

                UUID uuid = UUID.randomUUID();
                String uuidStr = uuid.toString();
                LogPrint.d("uuidStr : " + uuidStr);
            }
        });

        initUI();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(layoutInputRoot.getWindowToken(), 0);
    }

    private void initUI() {
        Intent intent = getIntent();
        dataBundle = PresetData.makeProductData();

        if (dataBundle != null) {
            String title = dataBundle.getString(PresetData.nTtileNameKey);

            for (String key : dataBundle.keySet()) {
                if (key.equals(PresetData.nTtileNameKey) || key.equals(ENDataManager.Params.type)) {
                    continue;
                }

                CustomInputView customInputView = new CustomInputView(this);
                customInputView.updateView(key, dataBundle, CustomInputView.InputType.TEXT, null);

                layoutInputRoot.addView(customInputView);
            }
        }
    }

    private void submitData() {
        int childCount = layoutInputRoot.getChildCount();

        for( int index = 0; index < childCount; index++) {
            View subView = layoutInputRoot.getChildAt(index);
            if (subView instanceof CustomInputView) {
                CustomInputView customInputView = (CustomInputView) subView;

                String key = customInputView.getKey();
                if (key == null || key.isEmpty()) {
                    continue;
                }

                if(key.equals(ENDataManager.Params.productQty) ||
                        key.equals(ENDataManager.Params.productPrice))
                {
                    try {
                        dataBundle.putInt(key, Integer.parseInt(customInputView.getValue()));
                    }
                    catch (NumberFormatException e) {
                        dataBundle.putInt(key, 1);
                    }

                }
                else {
                    dataBundle.putString(key, customInputView.getValue());
                }

            }
        }

        Intent intent = new Intent();
        intent.putExtra("productData", dataBundle);
        setResult(RESULT_OK, intent);

        finish();
    }
}