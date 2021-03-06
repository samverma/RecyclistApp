package comsamverma.github.recyclist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by sam on 4/20/2016.
 */
public class EditListingActivity extends AppCompatActivity{
    private String mobile_token = "we4mluxnRqNfCTZe2ccWEmWpa28OQCwv";
    String currUserToken;
    int id;
    private EditText eltitle, elcost, elcontent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlisting);
        Intent intent = getIntent();
        currUserToken = intent.getStringExtra("currentUserToken");
        id = intent.getIntExtra("id",0);
        eltitle = (EditText)findViewById(R.id.eltitle);
        elcost = (EditText)findViewById(R.id.elcost);
        elcontent = (EditText)findViewById(R.id.elcontent);

        Ion.with(this)
                .load("http://recyclist.us/api/listing/full?mobile_token=we4mluxnRqNfCTZe2ccWEmWpa28OQCwv&id="+id)
                .setHeader("Authorization", "Bearer" + currUserToken)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d("full listing", result.toString());
                        eltitle.setText(result.get("title").getAsString(), TextView.BufferType.EDITABLE);
                        elcost.setText(result.get("cost").getAsString(), TextView.BufferType.EDITABLE);
                        elcontent.setText(result.get("content").getAsString(), TextView.BufferType.EDITABLE);

                    }
                });



        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setTitle("RECYCLIST");
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf");
        collapsingToolbar.setCollapsedTitleTypeface(tf1);
        collapsingToolbar.setExpandedTitleTypeface(tf1);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_home:
                Intent intent4 = new Intent(this, MainActivity.class);
                intent4.putExtra("currentUserToken",currUserToken);
                finish();
                this.startActivity(intent4);
                break;
            case R.id.action_search:
                Intent intent = new Intent(this,ResListingsActivity.class);
                intent.putExtra("currentUserToken",currUserToken);
                finish();
                this.startActivity(intent);
                break;
            case R.id.action_aboutus:
                Intent intent2 = new Intent(this, AboutActivity.class);
                intent2.putExtra("currentUserToken",currUserToken);
                finish();
                this.startActivity(intent2);
                break;
            case R.id.action_resources:
                Intent intent3 = new Intent(this, CommInfoActivity.class);
                intent3.putExtra("currentUserToken",currUserToken);
                finish();
                this.startActivity(intent3);
                break;
            case R.id.action_news:
                Intent intent5 = new Intent(this, NewsFeedActivity.class);
                intent5.putExtra("currentUserToken",currUserToken);
                finish();
                this.startActivity(intent5);
                break;
            case R.id.action_tou:
                Intent intent6 = new Intent(this, LegalActivity.class);
                intent6.putExtra("currentUserToken",currUserToken);
                finish();
                this.startActivity(intent6);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void clickSaveListing(View view){
        String uptitle = eltitle.getText().toString();
        String upcost = elcost.getText().toString();
        String upcontent = elcontent.getText().toString();
        JsonObject params = new JsonObject();
        //  params.addProperty("Authorization","Bearer "+currentUserToken);
        params.addProperty("mobile_token", mobile_token);
        params.addProperty("id",id);
        params.addProperty("title",uptitle);
        params.addProperty("content", upcontent);
        params.addProperty("cost",upcost);
        Ion.with(this)
                .load("http://recyclist.us/api/listing/update")
                .setHeader("Authorization", "Bearer" + currUserToken)
                .setJsonObjectBody(params)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        Log.d("Update attempt", jsonObject.toString());
                        if(jsonObject.has("error")) {
                            Log.d("Update failure", jsonObject.get("error").toString());
                            //TextView tv = (TextView)findViewById(R.id.Test);
                            //tv.setText(jsonObject.toString());
                            //Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                            Toast.makeText(EditListingActivity.this, jsonObject.get("error").toString(), Toast.LENGTH_LONG).show();
                            Intent intent = getIntent();
                            intent.putExtra("currentUserToken", currUserToken);
                            intent.putExtra("id", id);
                            finish();
                            startActivity(getIntent());
                        }
                        else{
                            Intent i = new Intent(EditListingActivity.this, YourListingsActivity.class);
                            //i.putExtra("currentUser",currUser);
                            //i.putExtra("newAccountEmail", email);
                            i.putExtra("currentUserToken", currUserToken);
                            finish();
                            startActivity(i);
                        }
                    }
                });
    }

}
