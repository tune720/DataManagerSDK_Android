package com.datamanager.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.enliple.datamanagersdk.ENDataManager;

import java.util.Locale;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class CustomInputView extends LinearLayout {

    private EditText inputBox1;
    private EditText inputBox2;
    private Switch toggleSwitch;
    private Spinner dropdown;
    private Button addButton;

    InputType inputType = InputType.TEXT;
    Bundle data;
    String key;


    public enum InputType {
        TEXT, SWITCH, DROPDOWN, BUTTON
    }


    public CustomInputView(Context context) {
        super(context);
        init(context);
    }

    public CustomInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_input_view, this, true);

        inputBox1 = findViewById(R.id.inputBox1);
        inputBox2 = findViewById(R.id.inputBox2);

        toggleSwitch = findViewById(R.id.toggleSwitch);
        dropdown = findViewById(R.id.dropdown);
        addButton = findViewById(R.id.button);

        inputBox2.setVisibility(View.VISIBLE);
        toggleSwitch.setVisibility(View.GONE);
        dropdown.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
    }


    public void updateView(String key, Bundle data, InputType inputType, String[] dropdownItems) {
        this.key = key;
        this.data = data;
        this.inputType = inputType;


        switch (inputType) {
            case SWITCH:
                inputBox2.setVisibility(View.GONE);
                toggleSwitch.setVisibility(View.VISIBLE);
                dropdown.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                break;

            case DROPDOWN:
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dropdownItems == null ? new String[]{} : dropdownItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);

                inputBox2.setVisibility(View.GONE);
                toggleSwitch.setVisibility(View.GONE);
                dropdown.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
                break;

            case BUTTON:
                inputBox2.setVisibility(View.GONE);
                toggleSwitch.setVisibility(View.GONE);
                dropdown.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                break;

            case TEXT:
            default:
                inputBox2.setVisibility(View.VISIBLE);
                toggleSwitch.setVisibility(View.GONE);
                dropdown.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                break;
        }

        updateViewData();
    }

    private void updateViewData() {

        if (key == null || key.isEmpty()) {
            return;
        }

        try {
            inputBox1.setText(key);

            switch (inputType) {
                case SWITCH:
                    boolean checked = data.getBoolean(key);
                    toggleSwitch.setChecked(checked);
                    break;

                case DROPDOWN:
                    if (key.compareTo(ENDataManager.Params.trigger) == 0) {
                        int index = data.getString(key).compareTo(ENDataManager.TriggerType.loadPage) == 0 ? 0 : 1;
                        dropdown.setSelection(index);
                    }
                    else {
                        int index = data.getString(key).compareTo("남") == 0 ? 0 : 1;
                        dropdown.setSelection(index);
                    }

                    break;

                case BUTTON:
                    break;

                case TEXT:
                default:
                    if (key.toUpperCase().contains("QTY") || key.toUpperCase().contains("QUANTITY")) {
                        inputBox2.setText(String.format("%d", data.getInt(key)));
                    }
                    else {
                        inputBox2.setText(data.getString(key));
                    }
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(OnClickListener listener) {
        addButton.setOnClickListener(listener);
    }

    public String getKey() {
        return inputBox1.getText().toString();
    }

    public String getValue() {
        switch (inputType) {
            case SWITCH:
                return String.format(Locale.getDefault(), "%b", toggleSwitch.isChecked());

            case DROPDOWN:
                return dropdown.getSelectedItem().toString();

            case BUTTON:
                return "";

            case TEXT:
            default:
                return inputBox2.getText().toString();
        }
    }

    public InputType getInputType() {
        return inputType;
    }
}