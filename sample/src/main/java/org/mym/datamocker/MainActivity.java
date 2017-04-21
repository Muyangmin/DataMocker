package org.mym.datamocker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FieldOverviewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.main_recycler));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FieldOverviewAdapter();
        recyclerView.setAdapter(mAdapter);
        //Resolve an empty object (without mocking data)
        mAdapter.resolveFieldsAndRefresh(new User(), true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                final DataMocker dataMocker = new DataMocker();

                dataMocker.mockBoolean();
                dataMocker.mockInt();

                dataMocker.mockInt(0, 10000);

                dataMocker.mockDouble();

                dataMocker.mockDouble(-3, 100);

                dataMocker.mockStringAlphaNumeric(20);

                dataMocker.mockStringMatchesRegex("[\\u4e00-\\u9fa5]");

                User user = dataMocker.mockObject(User.class);
                mAdapter.resolveFieldsAndRefresh(user, false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.library_version, BuildConfig.VERSION_NAME))
                    .setCancelable(true)
                    .create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class FieldOverviewAdapter extends RecyclerView.Adapter<FieldHolder> {

        private static final int ITEM_HEADER = 44;
        private static final int ITEM_CONTENT = ITEM_HEADER + 1;

        private List<FieldInfo> mFieldInfoList = new ArrayList<>();

        private boolean mIsFirstList;

        private void resolveFieldsAndRefresh(@NonNull Object o, boolean isFirstList) {
            Class<?> clz = o.getClass();
            Field[] fields = clz.getDeclaredFields();
            List<FieldInfo> list = new ArrayList<>();
            for (Field field : fields) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                FieldInfo info = new FieldInfo();
                info.name = field.getName();
                info.type = field.getType().getSimpleName();
                try {
                    Object val = field.get(o);
                    info.value = val == null ? null : val.toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(isAccessible);
                list.add(info);
            }
            mFieldInfoList = list;
            mIsFirstList = isFirstList;
            notifyDataSetChanged();
        }

        @Override
        public FieldHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main_object_attributes, parent, false);
            return new FieldHolder(view);
        }

        @Override
        public void onBindViewHolder(FieldHolder holder, int position) {
            switch (getItemViewType(position)) {
                case ITEM_HEADER:
                    holder.displayTableHeader();
                    break;
                case ITEM_CONTENT:
                    int dataPosition = position - 1;
                    holder.displayData(mFieldInfoList.get(dataPosition), dataPosition,
                            mIsFirstList);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mFieldInfoList.isEmpty() ? 0 : mFieldInfoList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? ITEM_HEADER : ITEM_CONTENT;
        }
    }

    @SuppressWarnings("WeakerAccess")
    private static class FieldHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvType;
        TextView tvValue;

        public FieldHolder(View itemView) {
            super(itemView);
            tvName = ((TextView) itemView.findViewById(R.id.item_tv_field_name));
            tvType = ((TextView) itemView.findViewById(R.id.item_tv_field_type));
            tvValue = ((TextView) itemView.findViewById(R.id.item_tv_field_value));
        }

        public void displayTableHeader() {
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            tvName.setText(R.string.main_field_name);
            tvType.setText(R.string.main_field_type);
            tvValue.setText(R.string.main_field_value);
        }

        public void displayData(FieldInfo info, int dataPosition, boolean isFirstList) {
            tvName.setText(info.name == null ? "N/A" : info.name);
            tvType.setText(info.type == null ? "N/A" : info.type);
            tvValue.setText(info.value == null ? "null" : info.value);

            if (!isFirstList) {
                tvValue.setTextColor(Color.RED);
            }
            itemView.setBackgroundColor(dataPosition % 2 == 0 ?
                    Color.LTGRAY : Color.WHITE);
        }
    }

    private static class FieldInfo {
        String name;
        String type;
        String value;
    }
}
