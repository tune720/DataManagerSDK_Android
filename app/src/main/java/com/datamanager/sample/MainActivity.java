package com.datamanager.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.enliple.datamanagersdk.ENDataManager;
import com.enliple.datamanagersdk.Utils.LogPrint;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ENDataManager.enableLog(AppSettings.enableLog);
        ENDataManager.init(this, AppSettings.appKey);

        SampleAdapter sampleAdapter = new SampleAdapter(this);

        listView = findViewById(R.id.listView);
        listView.setAdapter(sampleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = (Bundle) adapterView.getItemAtPosition(i);
                String title = bundle.getString(PresetData.nTtileNameKey);

                LogPrint.d("MainActivity", "onItemClick : " + title);

                if (title.equals(PresetData.WebViewTestTitle)) {
                    Intent intent = new Intent(MainActivity.this, WebViewTestActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, DataInputActivity.class);
                    intent.putExtra("uiData", bundle);

                    startActivity(intent);
                }

            }
        });

        sampleAdapter.loadItems();
    }



    static class SampleAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<Bundle> mItems = new ArrayList<>();


        TextView textViewTitle;

        public SampleAdapter(Context context) {
            super();
            this.mContext = context;
        }

        private void loadItems() {
            mItems.add(PresetData.makeSignUpData());
            mItems.add(PresetData.makeSignInData());
            mItems.add(PresetData.makeSignOutData());
            mItems.add(PresetData.makeModifyUserData());
            mItems.add(PresetData.makeViewedProductData());
            mItems.add(PresetData.makeFavoriteData());
            mItems.add(PresetData.makeCartData());
            mItems.add(PresetData.makeOrderData());
            mItems.add(PresetData.makeOrderCancelData());
            mItems.add(PresetData.makeOrderOutData());
            mItems.add(PresetData.makeInstallData());
            mItems.add(PresetData.makeVisitData());
            mItems.add(PresetData.makePageViewData());
            mItems.add(PresetData.makeOutData());
            mItems.add(PresetData.makeCustomData());
            mItems.add(PresetData.makeWebViewData());
        }



        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_listitem, parent, false);
            }

            Bundle bundle = mItems.get(position);
            textViewTitle = convertView.findViewById(R.id.textView);

            textViewTitle.setText(bundle.getString(PresetData.nTtileNameKey));

            return convertView;
        }
    }
}
