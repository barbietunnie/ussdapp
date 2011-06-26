package db.dialservices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;  
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NumberInsertionActivity extends Activity{
	  
	private static final int CONTACT_PICKER_RESULT = 1001;
	private String command;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        command = getIntent().getExtras().getString("Command");
        setContentView(R.layout.numberinsertion	);
        ((Button) findViewById(R.id.addcontact)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        	    startActivityForResult(new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI), CONTACT_PICKER_RESULT);  
            }
        });
        ((Button) findViewById(R.id.docontact)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            	String text = ((EditText) findViewById(R.id.entry)).getText().toString();
            	if(!text.equals(""))
            		packDial(text);
            }
        });
	}  
    
    public void doLaunchContactPicker(View view) {  
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
	            Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {  
            switch (requestCode) {  
            case CONTACT_PICKER_RESULT: 
            	Uri result = data.getData();
            	// get the contact id from the Uri  
            	String id = result.getLastPathSegment();  
            	Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
    			if (pCur.moveToNext()) {
    				// Do something with phones
    				String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
    		        EditText editText = (EditText) findViewById(R.id.entry);
    		        editText.setText(threatPhoneNumber(phoneNumber));
    			} 
    			pCur.close();
            	break;  
            }  
        }
    }  
    
    void packDial(String phoneNumber){
    	command = command.replaceAll("9xxxxxxxx", phoneNumber);
    	call(command.replaceAll("#", Uri.encode("#")));
	}
    
    protected void call(String phoneNumber) { 
        startActivity(new Intent("android.intent.action.CALL",Uri.parse("tel:"+ phoneNumber))); 
    } 
    
    String threatPhoneNumber(String phoneNumber){
    	String[] aux;
		if(phoneNumber.contains("-")){
			aux = phoneNumber.split("-");
			phoneNumber = "";
			for(int i = 0; i < aux.length; i++)
				phoneNumber+= aux[i];
		}
		if(phoneNumber.startsWith("+351"))
			phoneNumber = phoneNumber.substring(4);
		else if(phoneNumber.startsWith("00351"))
				phoneNumber = phoneNumber.substring(5);
		return phoneNumber;
		
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
