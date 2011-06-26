package db.dialservices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] list = getResources().getStringArray(R.array.ussd_array);
		
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		ll.setPadding(0, 15, 0, 0);
		ll.setBackgroundColor(Color.rgb(152, 152, 152));
		for (int i = 0; i < list.length; i++) {
			final String service = list[i];
			View v = inflater.inflate(R.layout.mainlist_item, null);
			Button b = ((Button) v.findViewById(R.id.ussdservice));
			b.setText(service);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent;
					if(service.equals("Extravaganza/Extreme?")){
						intent = new Intent(MainListActivity.this, NumberInsertionActivity.class);
						intent.putExtra("Command", (String) "*#109*9xxxxxxxx#");
					}else{
						intent = new Intent(MainListActivity.this, ListActivity.class);
						intent.putExtra("Service", (String) service);
					}
					startActivity(intent);
				}
			});
			v.setPadding(0, 11, 0, 0);
			ll.addView(v);
		}
		setContentView(ll);
	}

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.generalmenu, menu);
        return true;
    }
    

    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
        switch (item.getItemId()) {
            case R.id.iabout: 	intent = new Intent(this, AboutActivity.class);
  	      						startActivityForResult(intent, 0);
            					break;
            case R.id.ishare: 	intent = new Intent(android.content.Intent.ACTION_SEND);
            					intent.setType("text/plain");
            					intent.putExtra(Intent.EXTRA_SUBJECT, "Aplica‹o Android - USSD R‡pido");
            					intent.putExtra(Intent.EXTRA_TEXT, "Liga‹o ao AndroidMarket...em breve!");
            					startActivity(Intent.createChooser(intent, "Partilhar"));
            					break;
        }
        return true;
    }

}
