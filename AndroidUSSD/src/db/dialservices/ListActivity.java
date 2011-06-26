package db.dialservices;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TypedArray list = null;
		String service = getIntent().getExtras().getString("Service");
		if(service.equals("Consultas"))
			list = getResources().obtainTypedArray(R.array.ussdconsulta);
		else if(service.equals("Toking"))
			list = getResources().obtainTypedArray(R.array.ussdtoking);
		else if(service.equals("SOSTransfer"))
			list = getResources().obtainTypedArray(R.array.ussdsostransfer);
		else if(service.equals("SOSPagas"))
			list = getResources().obtainTypedArray(R.array.ussdsospagas);
		else if(service.equals("How are you?"))
			list = getResources().obtainTypedArray(R.array.ussdhowareyou);
		else if(service.equals("Aditivo"))
			list = getResources().obtainTypedArray(R.array.ussdaditivo);

		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		ll.setPadding(0, 15, 0, 0);
		ll.setBackgroundColor(Color.rgb(152, 152, 152));
		for (int i = 0; i < list.length(); i++) {
			JSONObject j = null;
			try {
				j = new JSONObject(list.getString(i));
				View v = inflater.inflate(R.layout.list_item, null);
				((TextView) v.findViewById(R.id.ussdtitle)).setText(j.getString("title"));
				
				final String command = j.getString("command");
				((Button) v.findViewById(R.id.ussdtitle)).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent;
						if(command.contains("xxxxxxxx")){
							intent = new Intent(ListActivity.this, NumberInsertionActivity.class);
							intent.putExtra("Command", command);
				            startActivity(intent);
						}else{ 
							startActivity(new Intent("android.intent.action.CALL",Uri.parse("tel:"+ command.replaceAll("#", Uri.encode("#"))))); 
						}
					}
				});
				
				final String description = j.getString("description");
				((Button) v.findViewById(R.id.ussdinfobutton)).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Dialog alertDialog = new Dialog(ListActivity.this); 
						alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
						alertDialog.setContentView(R.layout.info_layout); 
						alertDialog.setCancelable(true);

						TextView code = (TextView) alertDialog.findViewById(R.id.ussdcode);
					    String aux = (String) code.getText();
					    code.setText(aux + command);
					    TextView description2 = (TextView) alertDialog.findViewById(R.id.ussddescription);
					    aux = (String) description2.getText();
					    description2.setText(aux + description);
						
					    final Dialog auxDialog = alertDialog;
						((Button) alertDialog.findViewById(R.id.closebutton)).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								auxDialog.dismiss();
							}
						});
						alertDialog.show(); 
					}
				});
				
				v.setPadding(0, 11, 0, 0);
				ll.addView(v);
			} catch (JSONException e) {
				System.out.println("Informa‹o falhou");
			}
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
