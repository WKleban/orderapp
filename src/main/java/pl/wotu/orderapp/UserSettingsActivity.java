package pl.wotu.orderapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import pl.wotu.orderapp.database.DatabaseHelper;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class UserSettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    private Toolbar toolbar;

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
//        Toolbar bar = findViewById(R.id.order_toolbar);
//        root.addView(bar, 0); // insert at top
//        bar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

////        toolbar = findViewById(R.id.settings_toolbar);
//        setSupportActionBar(bar);
////        setupActionBar();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Bundle b = getIntent().getExtras();
            switch(b.getString("pref")){
                case "pref_user":
                    this.getFragmentManager().beginTransaction().replace(android.R.id.content, new UserPreferenceFragment()).commit();
                    break;
                case "pref_general_settings":
                    this.getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralSettingsPreferenceFragment()).commit();
                    break;

                default:
                    this.getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralSettingsPreferenceFragment()).commit();
                    break;
            }

//        this.getFragmentManager().beginTransaction().replace(android.R.id.content, new UserPreferenceFragment()).commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();
//        setSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public void onBuildHeaders(List<Header> target) {
////        loadHeadersFromResource(R.xml.pref_headers, target);
//    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
//                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
//                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || UserPreferenceFragment.class.getName().equals(fragmentName)
//                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || GeneralSettingsPreferenceFragment.class.getName().equals(fragmentName);

    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public static class GeneralPreferenceFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.pref_general);
//            setHasOptionsMenu(true);
//
//            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
//            // to their values. When their values change, their summaries are
//            // updated to reflect the new value, per the Android Design
//            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("example_text"));
//            bindPreferenceSummaryToValue(findPreference("example_list"));
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == android.R.id.home) {
//                startActivity(new Intent(getActivity(), UserSettingsActivity.class));
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralSettingsPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general_settings);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), UserSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class UserPreferenceFragment extends PreferenceFragment {
        private Snackbar snackbar;
        private SharedPreferences pref;
        private SharedPreferences.Editor editor;
        private String nazwaSkrApteki;
        private long nrApteki;
        private String licencja;
        private String nazwaSkroc,nazwa,nazwa2,kodPoczt,miejscowosc,ul,telefon,email,nazwPrzedst,telDoPrzedst;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);



//            getActivity().setContentView(R.layout.activity_orders);

            addPreferencesFromResource(R.xml.pref_user);
            setHasOptionsMenu(true);

            pref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_tag), 0); // 0 - for private mode
            editor = pref.edit();
            nrApteki = pref.getLong("nrApteki", 0);
            licencja = pref.getString("licencja","Brak licencji");
            nazwaSkrApteki = pref.getString("nazwaApteki","Nie wpisano nazwy");

            nazwaSkroc = pref.getString("nazwaSkroc","");
            nazwa = pref.getString("nazwa","");
            nazwa2 = pref.getString("nazwa2","");
            kodPoczt = pref.getString("kodPoczt","");
            miejscowosc = pref.getString("miejscowosc","");
            ul = pref.getString("ul","");
            telefon = pref.getString("telefon","");
            email = pref.getString("email","");
            nazwPrzedst = pref.getString("nazwPrzedst","");
            telDoPrzedst = pref.getString("telDoPrzedst","");


            PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("pref_group_user");
            preferenceCategory.setTitle("Informacje o aptece "+nrApteki);

            Preference licencePreference = findPreference("pref_licence");
            licencePreference.setTitle("Licencja");
            licencePreference.setSummary(licencja);

            Preference logoutPreference = findPreference("pref_logout");
            logoutPreference.setTitle("Wyloguj się...");
            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    snackbar = Snackbar
                            .make(getView(), "Czy chcesz się wylogować?", Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(Color.RED)
                            .setDuration(Snackbar.LENGTH_SHORT)

                            .setAction("TAK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    editor.clear();
                                    editor.commit();

                                    DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());

                                    db.czyscBazePrzyWylogowaniu();


                                    getActivity().onBackPressed();
                                }
                            });

                    snackbar.show();
                    return false;
                }
            });

            EditTextPreference nazwaSkrAptekiPreference = (EditTextPreference) findPreference("pref_display_short_name");
            nazwaSkrAptekiPreference.setText(nazwaSkrApteki);


            Preference nazwaAptekiPreference = findPreference("pref_display_name");
            nazwaAptekiPreference.setTitle("Nazwa");
            nazwaAptekiPreference.setSummary(nazwa+"\n"+nazwa2);

            Preference ulicaPreference = findPreference("pref_display_street");
            ulicaPreference.setTitle("Ulica");
            ulicaPreference.setSummary(ul);

            Preference kodPocztPreference = findPreference("pref_postal_code");
            kodPocztPreference.setTitle("Kod pocztowy");
            kodPocztPreference.setSummary(kodPoczt);

            Preference miejscowoscPreference = findPreference("pref_city");
            miejscowoscPreference.setTitle("Miejscowość");
            miejscowoscPreference.setSummary(miejscowosc);

            EditTextPreference emailPreference = (EditTextPreference) findPreference("pref_email");
            emailPreference.setText(email);

            EditTextPreference telefonPreference = (EditTextPreference) findPreference("pref_phone");
            telefonPreference.setText(telefon);

            Preference przedstPreference = findPreference("pref_przedstawiciel");
            if (nazwPrzedst.equals("")) {
                przedstPreference.setTitle("Przedstawiciel");
            }else{
                przedstPreference.setTitle("Przedstawiciel: "+nazwPrzedst);
            }
            przedstPreference.setSummary(telDoPrzedst);

            przedstPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (telDoPrzedst.equals("")){
//                        ZmienneIFunkcjeStatyczne.showToast(prefer,"Brak przypisanego przedstawiciela handlowego",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);
                    }else {
                        Intent intent_MENU_CONTACT_REPRESENTATIVE_ITEM = new Intent(Intent.ACTION_DIAL);
                        intent_MENU_CONTACT_REPRESENTATIVE_ITEM.setData(Uri.parse("tel:" + telDoPrzedst));
                        startActivity(intent_MENU_CONTACT_REPRESENTATIVE_ITEM);
                    }

//
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:"+telDoPrzedst));
//                    startActivity(intent);

                    return false;
                }
            });

            bindPreferenceSummaryToValue(findPreference("pref_display_short_name"));
            bindPreferenceSummaryToValue(findPreference("pref_email"));
            bindPreferenceSummaryToValue(findPreference("pref_phone"));
//            bindPreferenceSummaryToValue(findPreference("pref_przedstawiciel"));
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), UserSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public static class NotificationPreferenceFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.pref_notification);
//            setHasOptionsMenu(true);
//
//            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
//            // to their values. When their values change, their summaries are
//            // updated to reflect the new value, per the Android Design
//            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == android.R.id.home) {
//                startActivity(new Intent(getActivity(), UserSettingsActivity.class));
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public static class DataSyncPreferenceFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.pref_data_sync);
//            setHasOptionsMenu(true);
//
//            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
//            // to their values. When their values change, their summaries are
//            // updated to reflect the new value, per the Android Design
//            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == android.R.id.home) {
//                startActivity(new Intent(getActivity(), UserSettingsActivity.class));
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//    }

//    public static void sendFeedback(Context context) {
//        String body = null;
//        try {
//            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
//            body = "\nTEST APLIKACJI APS\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
//                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
//                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
//        } catch (PackageManager.NameNotFoundException e) {
//        }
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("message/rfc822");
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"wkleban@lubfarm.pl"});
//        intent.putExtra(Intent.EXTRA_SUBJECT, "APS - Query from android app");
//        intent.putExtra(Intent.EXTRA_TEXT, body);
//        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
//    }
}
