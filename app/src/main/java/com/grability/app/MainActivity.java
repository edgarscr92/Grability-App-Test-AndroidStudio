package com.grability.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v4.view.PagerTitleStrip;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;

    private String TAG = MainActivity.class.getSimpleName();
    private static Context context;
    private static Feed feed = null;
    private static List<CategoryManager> categories = new ArrayList<CategoryManager>();

    private static boolean isTablet;
    private SharedPreferences mSettings;
    private static List<String> tempCategories = new ArrayList<String>();

    static Handler h = new Handler();
    static Runnable runnable;

    static TextView internetAdvice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        isTablet = getResources().getBoolean(R.bool.isTablet);
        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagerTitle);
        pagerTitleStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Categories.class);
                startActivity(intent);
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        internetAdvice = (TextView) findViewById(R.id.internetAdvice);
        internetAdvice.setVisibility(View.INVISIBLE);

        internetAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerStatus = false;
                internetAdvice.setVisibility(View.INVISIBLE);
                h.removeCallbacks(runnable);
                Intent intent = new Intent(context, AlertActivity.class);
                intent.putExtra("CurrentClass", "MainActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mSettings = getSharedPreferences("ProductsDB", Context.MODE_PRIVATE);
        ReadJson(GetDB());

        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        final Handler h = new Handler();
        GoRunnable(5000);
    }

    private static boolean handlerStatus = true;
    public static void RunHandler(int setTime){
        internetAdvice.setVisibility(View.VISIBLE);
        handlerStatus = true;
        GoRunnable(setTime);
    }

    private static void GoRunnable(final int setTime){
        h.postDelayed(new Runnable() {
            public void run() {

                runnable = this;
                h.postDelayed(this, setTime);
                if(handlerStatus) {
                    if (!ConnectivityReceiver.isConnected(context)){
                        handlerStatus = false;
                        internetAdvice.setVisibility(View.INVISIBLE);
                        h.removeCallbacks(runnable);
                        Intent intent = new Intent(context, AlertActivity.class);
                        intent.putExtra("CurrentClass", "MainActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        internetAdvice.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }, setTime);
    }

    public static boolean GetDevice() { return isTablet; }

    public static CategoryManager getCategory(int i){ return categories.get(i); }
    public static int getCategoryCount(){ return categories.size(); }
    public static List<String> getCategories(){ return tempCategories; }
    public static void goToCategory(int i) {mViewPager.setCurrentItem(i);}

    private void ReadJson(String str){

        Author newAuthor = null;
        List<Entry> newEntry = new ArrayList<Entry>();
        Updated newUpdated = null;
        Rights newRights = null;
        Title newTitle = null;
        Icon newIcon = null;
        List<Link> newLink = new ArrayList<Link>();
        Id newId = null;

        try {
            JSONObject json = new JSONObject(str);
            JSONObject feedObj = json.getJSONObject("feed");
            if (feedObj != null) {

                newAuthor = new Author(
                        new Name(feedObj.getJSONObject("author").getJSONObject("name").getString("label")),
                        new Uri(feedObj.getJSONObject("author").getJSONObject("uri").getString("label"))
                );

                JSONArray entries = feedObj.getJSONArray("entry");
                for (int i = 0; i < entries.length(); i++) {
                    JSONObject entry = (JSONObject) entries.get(i);

                    List<ImImage> tempImgs = new ArrayList<ImImage>(); // Creamos listado vacio de imagenes
                    JSONArray appImages = entry.getJSONArray("im:image"); // Obtenemos listado de imagenes
                    for (int g = 0; g < appImages.length(); g++) {
                        JSONObject images = (JSONObject) appImages.get(g);
                        tempImgs.add(new ImImage(images.getString("label"), GetAttributes(images.getJSONObject("attributes"))));
                    }

                    Entry tempEntry = new Entry(
                            new ImName(entry.getJSONObject("im:name").getString("label")), tempImgs,
                            new Summary(entry.getJSONObject("summary").getString("label")),
                            new ImPrice(entry.getJSONObject("im:price").getString("label"), GetAttributes(entry.getJSONObject("im:price").getJSONObject("attributes"))),
                            new ImContentType(GetAttributes(entry.getJSONObject("im:contentType").getJSONObject("attributes"))),
                            new Rights(entry.getJSONObject("rights").getString("label")),
                            new Title(entry.getJSONObject("title").getString("label")),
                            new Link(GetAttributes(entry.getJSONObject("link").getJSONObject("attributes"))),
                            new Id(entry.getJSONObject("id").getString("label"), GetAttributes(entry.getJSONObject("id").getJSONObject("attributes"))),
                            new ImArtist(entry.getJSONObject("im:artist").getString("label"), GetAttributes(entry.getJSONObject("im:artist").getJSONObject("attributes"))),
                            new Category(GetAttributes(entry.getJSONObject("category").getJSONObject("attributes"))),
                            new ImReleaseDate(entry.getJSONObject("im:releaseDate").getString("label"), GetAttributes(entry.getJSONObject("im:releaseDate").getJSONObject("attributes")))
                    );

                    newEntry.add(tempEntry);
                    AppControll(tempEntry);
                }

                newUpdated = new Updated(feedObj.getJSONObject("updated").getString("label"));
                newRights = new Rights(feedObj.getJSONObject("rights").getString("label"));
                newTitle = new Title(feedObj.getJSONObject("title").getString("label"));
                newIcon = new Icon(feedObj.getJSONObject("icon").getString("label"));

                JSONArray tempLink = feedObj.getJSONArray("link");
                for (int i = 0; i < tempLink.length(); i++) {
                    JSONObject tempData = (JSONObject) tempLink.get(i);
                    newLink.add(new Link(GetAttributes(tempData.getJSONObject("attributes"))));
                }

                newId = new Id(feedObj.getJSONObject("id").getString("label"), new Attributes());

                feed = new Feed(newAuthor, newEntry, newUpdated, newRights, newTitle, newIcon, newLink, newId);

                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //TODO Mostrar alerta
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "Failed to load. Have a look at LogCat.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Attributes GetAttributes(JSONObject array) throws JSONException {

        Attributes newAtt = new Attributes();
        String label = "";
        String rel = "";
        String type = "";
        String href = "";
        String schema = "";
        String term = "";
        String imId = "";
        String imBundleId = "";
        String amount = "";
        String currency = "";
        String height = "";

        if (!array.isNull("label"))
            label = array.getString("label");

        if (!array.isNull("rel"))
            rel = array.getString("rel");

        if (!array.isNull("type"))
            type = array.getString("type");

        if (!array.isNull("href"))
            href = array.getString("href");

        if (!array.isNull("schema"))
            schema = array.getString("schema");

        if (!array.isNull("term"))
            term = array.getString("term");

        if (!array.isNull("im:id"))
            imId = array.getString("im:id");

        if (!array.isNull("im:bundleId"))
            imBundleId = array.getString("im:bundleId");

        if (!array.isNull("amount"))
            amount = array.getString("amount");

        if (!array.isNull("currency"))
            currency = array.getString("currency");

        if (!array.isNull("height"))
            height = array.getString("height");

        newAtt = new Attributes(label, rel, type, href, schema, term, imId, imBundleId, amount, currency, height);
        return newAtt;
    }

    private String GetDB() { return mSettings.getString("ProductsKey", "ProductsDB"); }


    private void AppControll(Entry entry) {
        if (entry.getCategory().getAttributes().getLabel() == "" || entry.getCategory().getAttributes().getLabel() == null) {
            Log.e(TAG, "VACIO!");
            return;
        }
        if(tempCategories.contains(entry.getCategory().getAttributes().getLabel())) {
            for (int i = 0; i < tempCategories.size(); i++) {
                if (tempCategories.get(i).equals(entry.getCategory().getAttributes().getLabel())) {
                    if(!categories.get(i).getEntryList().contains(entry));
                        categories.get(i).newEntry(entry);
                }
            }
        } else {
            tempCategories.add(entry.getCategory().getAttributes().getLabel());
            categories.add(new CategoryManager(entry.getCategory().getAttributes().getLabel(), entry));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment  {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private GridView grid;
        private ListView listView;
        private static final String ARG_SECTION_NUMBER = "asd";
        private static final String CATEGORY_NAME = "";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String catName) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(CATEGORY_NAME,catName);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            if (MainActivity.GetDevice()) { // Tablet
                grid = (GridView) rootView.findViewById(R.id.main_grid_view);
            } else { // Phone
                listView = (ListView) rootView.findViewById(R.id.main_list_view);
            }

            List<Entry> entryList = new ArrayList<Entry>();

            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategory() == getArguments().get(CATEGORY_NAME))
                    entryList = categories.get(i).getEntryList();
            }

            final List<Entry> finalEntryList = entryList;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (feed != null) {
                        if (MainActivity.GetDevice()) { // Tablet
                            grid.setAdapter(new GridAdapter(getContext(), finalEntryList));
                            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Intent intent = new Intent(getContext(), ProductDetails.class);

                                    ViewHolder holder = (ViewHolder) view.getTag();
                                    Entry temp = (Entry) holder.image.getTag();
                                    intent.putExtra("app_title", temp.getTitle().getLabel());
                                    intent.putExtra("app_price", temp.getImPrice().getAttributes().getAmount() + " " + temp.getImPrice().getAttributes().getCurrency());
                                    intent.putExtra("app_desc", temp.getSummary().getLabel());
                                    intent.putExtra("app_image", temp.getImImage().get(0).getLabel());
                                    startActivity(intent);
                                }
                            });
                        } else { // Phone
                            listView.setAdapter(new GridAdapter(getContext(), finalEntryList));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Intent intent = new Intent(getContext(), ProductDetails.class);

                                    ViewHolder holder = (ViewHolder) view.getTag();
                                    Entry temp = (Entry) holder.image.getTag();
                                    intent.putExtra("app_title", temp.getTitle().getLabel());
                                    intent.putExtra("app_price", temp.getImPrice().getAttributes().getAmount() + " " + temp.getImPrice().getAttributes().getCurrency());
                                    intent.putExtra("app_desc", temp.getSummary().getLabel());
                                    intent.putExtra("app_image", temp.getImImage().get(0).getLabel());
                                    startActivity(intent);
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getContext(), "No hay datos.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, getCategory(position).getCategory());
        }

        @Override
        public int getCount() {
            return getCategoryCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getCategory(position).getCategory();
        }
    }
}

class CategoryManager {
    private String category;
    private List<Entry> entryList = new ArrayList<Entry>();

    CategoryManager(String category, Entry entry){
        this.category = category;
        this.entryList.add(entry);
    }

    public String getCategory(){ return this.category; }
    public List<Entry> getEntryList(){ return this.entryList; }

    public void newEntry(Entry entry){ this.entryList.add(entry); }

    public int getCategoryCount() { return this.entryList.size(); }
}

class ViewHolder {
    ImageView image;
    TextView title;
    TextView desc;
    TextView price;

    ViewHolder(View v) {
        if (MainActivity.GetDevice()) { // Tablet
            image = (ImageView) v.findViewById(R.id.grid_app_image);
            image.setLayoutParams(new RelativeLayout.LayoutParams(190, 190));
            price = (TextView) v.findViewById(R.id.main_app_price);
        } else { // Phone
            image = (ImageView) v.findViewById(R.id.grid_app_image);
            image.setLayoutParams(new RelativeLayout.LayoutParams(120, 120));
            title = (TextView) v.findViewById(R.id.main_app_title);
            price = (TextView) v.findViewById(R.id.main_app_price);
        }
    }
}

class GridAdapter extends BaseAdapter {
    List<Entry> list = new ArrayList<Entry>();
    Context context;

    public GridAdapter(Context context, List<Entry> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.main_item, viewGroup, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Entry temp = list.get(i);
        Picasso.with(context).load(temp.getImImage().get(0).getLabel()).into(holder.image);

        if (MainActivity.GetDevice()) { // Tablet

        } else { // Phone
            holder.title.setText(temp.getTitle().getLabel());
        }

        holder.price.setText(temp.getImPrice().getAttributes().getAmount() + " " + temp.getImPrice().getAttributes().getCurrency());
        holder.image.setTag(temp);
        return row;
    }
}