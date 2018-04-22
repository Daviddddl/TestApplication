package com.example.didonglin.testapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HistoryLayout extends LinearLayout implements OnClickListener,OnTouchListener,OnItemClickListener {
	private Context context;
	private final String FILENAME = "history";
	
	private List<String> equationList = new ArrayList<>();
	private String hisHistory = "";
	private StringBuilder hisHistorySB = new StringBuilder();
	private ListView hisListView;
	private String[] hisItemArray;
	private String exp;
	private String result;
	private String[] hisItem;
	private EquationAdapter eAdapter;
	private boolean appendHistory;	//是否需要追加history字符串
	private Button clearHisBtn;
	private ClipboardManager clipboard;
	private ClipData clip;
	private String str;
	private Button btn;
	
	private FileInputStream in;
	private BufferedReader reader;
	private StringBuilder contentSB;
	private String fileDir;
	private File file;
	
	public HistoryLayout(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.history_layout, this);
		eAdapter = new EquationAdapter(context, R.layout.history_item, equationList);
		hisListView = (ListView)findViewById(R.id.history_list_view);
		hisListView.setAdapter(eAdapter);
		hisListView.setOnItemClickListener(this);
		
		clearHisBtn = (Button)findViewById(R.id.history_clear);
		clearHisBtn.setOnTouchListener(this);
		clearHisBtn.setOnClickListener(this);
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		str = parent.getItemAtPosition(position).toString();
		clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		clip = ClipData.newPlainText("simple text",str);
	 	clipboard.setPrimaryClip(clip);
	 	
	 	Toast.makeText(context, "已成功将这条历史复制到剪贴板", Toast.LENGTH_SHORT).show();
	} 



	@Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	btn = (Button)v;
	    	switch ( event.getAction() ){
	    	case MotionEvent.ACTION_DOWN:
	    		btn.setBackgroundColor(Color.parseColor("#F2F2F2"));
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		btn.setBackgroundColor(Color.WHITE);
	    		break;
	    	default:
	    		break;
	    	}
	    	return false;
	    }



	@Override
	public void onClick(View v) {
		this.hisHistorySB.setLength(0);
		equationList.clear();
		eAdapter.notifyDataSetChanged();
	}

	public void updateHistory(String calHistory){
		hisHistorySB.append(calHistory);
		hisItemArray = calHistory.split(";");
		if ( hisItemArray.length > 0 ){
			for (String item:hisItemArray){
				if ( ! TextUtils.isEmpty(item) ) {
					equationList.add(item.replace(",", "\n"));
				}
			}
			eAdapter.notifyDataSetChanged();
		}
	}
	



	public String getHistory(){
		return hisHistorySB.toString();
	}
	



	public String load(){
		in = null;
		reader = null;
		contentSB = new StringBuilder();
		fileDir = context.getFilesDir() + File.separator + FILENAME;
		file = new File(fileDir);
		try {
			if ( ! file.exists() )
				return "";
			in = context.getApplicationContext().openFileInput(FILENAME);
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ( (line = reader.readLine()) != null) {
				contentSB.append(line);
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return contentSB.toString();
	}
}