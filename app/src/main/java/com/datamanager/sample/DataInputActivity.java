package com.datamanager.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.enliple.datamanagersdk.ENDataManager;
import com.enliple.datamanagersdk.Utils.LogPrint;
import com.enliple.datamanagersdk.events.EventModel;

import java.util.ArrayList;

public class DataInputActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private LinearLayout layoutInputRoot;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    Bundle dataBundle;
    ArrayList<Bundle> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        textViewTitle = findViewById(R.id.textViewTitle);
        layoutInputRoot = findViewById(R.id.linearLayoutInputViewRoot);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getBundleExtra("productData");

                        if (products == null) {
                            products = new ArrayList<>();
                        }
                        products.add(bundle);
                    }
                }
        );



        findViewById(R.id.buttonAddParam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                CustomInputView customInputView = new CustomInputView(DataInputActivity.this);
                customInputView.updateView("", dataBundle, CustomInputView.InputType.TEXT, null);
                layoutInputRoot.addView(customInputView);

            }
        });

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                submitData();
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
        dataBundle = intent.getBundleExtra("uiData");

        if (dataBundle != null) {
            String title = dataBundle.getString(PresetData.nTtileNameKey);
            textViewTitle.setText(title);

            for (String key : dataBundle.keySet()) {
                if (key.equals(PresetData.nTtileNameKey) || key.equals(ENDataManager.Params.type)) {
                    continue;
                }

                CustomInputView customInputView = new CustomInputView(this);
                if (key.compareTo(ENDataManager.Params.products) == 0) {
                    customInputView.updateView(key, dataBundle, CustomInputView.InputType.BUTTON, null);
                    customInputView.setOnButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard();
                            Intent intent = new Intent(DataInputActivity.this, ProductInputActivity.class);
                            activityResultLauncher.launch(intent);
                        }

                    });
                } else if (key.compareTo(ENDataManager.Params.gender) == 0) {
                    customInputView.updateView(key, dataBundle, CustomInputView.InputType.DROPDOWN, new String[]{"남", "여"});
                } else if (key.compareTo(ENDataManager.Params.trigger) == 0) {
                    customInputView.updateView(key, dataBundle, CustomInputView.InputType.DROPDOWN, new String[]{"클릭", "페이지 로딩"});
                } else if (key.compareTo(ENDataManager.Params.smsAllowed) == 0 || key.compareTo(ENDataManager.Params.emailAllowed) == 0) {
                    customInputView.updateView(key, dataBundle, CustomInputView.InputType.SWITCH, null);
                } else {
                    customInputView.updateView(key, dataBundle, CustomInputView.InputType.TEXT, null);
                }

                layoutInputRoot.addView(customInputView);
            }
        }
    }



    private void submitData() {
        ArrayList<String> keysToRemove = new ArrayList<>();

        for (String key : dataBundle.keySet()) {
            if (key.equals(PresetData.nTtileNameKey) || key.equals(ENDataManager.Params.type)) {
                continue;
            }
            keysToRemove.add(key);
        }

        for (String key : keysToRemove) {
            dataBundle.remove(key);
        }


        int childCount = layoutInputRoot.getChildCount();

        for( int index = 0; index < childCount; index++) {
            View subView = layoutInputRoot.getChildAt(index);
            if (subView instanceof CustomInputView) {
                CustomInputView customInputView = (CustomInputView) subView;

                String key = customInputView.getKey();
                if (key == null || key.isEmpty()) {
                    continue;
                }

                switch (customInputView.getInputType()) {
                    case TEXT:
                        if (key.compareTo(ENDataManager.Params.productQty) == 0 ||
                                key.compareTo(ENDataManager.Params.totalQuantity) == 0 ||
                                key.compareTo(ENDataManager.Params.productPrice) == 0 ||
                                key.compareTo(ENDataManager.Params.totalPrice) == 0) {

                            try {
                                dataBundle.putInt(key, Integer.parseInt(customInputView.getValue()));
                            }
                            catch (NumberFormatException e) {
                                dataBundle.putInt(key, 0);
                            }
                        }
                        else {
                            dataBundle.putString(key, customInputView.getValue());
                        }

                        break;

                    case SWITCH:
                        dataBundle.putBoolean(key, (customInputView.getValue().toUpperCase().compareTo("TRUE") == 0));
                        break;

                    case DROPDOWN:
                        String value = "";
                        if (key.compareTo(ENDataManager.Params.gender) == 0) {
                            value = customInputView.getValue();
                        } else if (key.compareTo(ENDataManager.Params.trigger) == 0) {
                            value = customInputView.getValue().compareTo("클릭") == 0 ? ENDataManager.TriggerType.click : ENDataManager.TriggerType.loadPage;
                        }
                        dataBundle.putString(key, value);
                        break;

                    case BUTTON:
                    default:
                        break;
                }
            }
        }

        if (products != null && !products.isEmpty()) {
            dataBundle.putParcelableArrayList(ENDataManager.Params.products, products);
        }

        LogPrint.w(dataBundle.toString());

        EventModel eventModel = DataConvert.getInstance().convertData(dataBundle);
        LogPrint.w(eventModel.toJson(this, "").toString());

        ENDataManager.getInstance().addEvent(eventModel);
    }

}

