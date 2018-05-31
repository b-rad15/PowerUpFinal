package com.brado.powerupscouting;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ScoutingActivity extends AppCompatActivity {
	
	private EditText teamNum, matchNum;
	private Button switchPeriod, write;
	private TextView blocksASwitchAuto, blocksScaleAuto, blocksPUAuto, blocksOSwitchAuto, teamDataAuto, blocksExchangeAuto, blocksASwitch, blocksOSwitch, blocksScale, blocksExchange, blocksPU, levitate, boost, force, robotClimb, wrungClimb, climbing;
	private CheckBox blockCheck, lineCheck, dropCheck, blockPlaceCheck, lev3, lev2, lev1, force3, force2, force1, boost3, boost2, boost1;
	private Switch       switchScale;
	private ToggleButton posL, posM, posR, DNS, climbToggle1, climbToggle2, climbToggle0;
	private ToggleButton[] posGroup, climbNumGroup;
	private String dataDir, climbType;
	private View[] autonomousElements = {switchScale, blockCheck, lineCheck, dropCheck, blockPlaceCheck, posL, posM, posR},
			teleopElements = {levitate, lev1, lev2, lev3, boost, boost1, boost2, boost3, force, force1, force2, force3, robotClimb, wrungClimb, climbing, climbToggle1, climbToggle2};
	private CheckBox[] levBoxes,
			forceBoxes,
			boostBoxes;
	private RadioButton[] climbChoices;
	static        File file;
	public static int  teamNumber = -1, matchNumber = -1, abas, absw, abos, abpu, abe, tbas, tbsw, tbos, tbpu, tbe, levitateAmount, boostAmount, forceAmount, climbNumber;
	public static String pos = String.valueOf('X');
	public static boolean climbedWrung = false,
			climbed = false,
			blockStart = false,
			crossedLine = false,
			droppedBlock = false,
			blockPlaced = false,
			auto = true,
			error = false;
	public static String scaleOrSwitch;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scouting);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		
		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
		
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view){
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//			}
//		});
//		setAutolements();
	
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		setAutolements();
		setTelements();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scouting, menu);
		setAutolements();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if(id == R.id.action_settings){
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setTelements(){
		blocksASwitch  = findViewById(R.id.blocksASwitch);
		blocksOSwitch  = findViewById(R.id.blocksOSwitch );
		blocksScale  = findViewById(R.id.blocksScale );
		blocksExchange  = findViewById(R.id.blocksExchange );
		blocksPU  = findViewById(R.id.blocksPU );
		lev1 = findViewById(R.id.lev1);
		lev2 = findViewById(R.id.lev2);
		lev3 = findViewById(R.id.lev3);
		force1 = findViewById(R.id.force1);
		force2 = findViewById(R.id.force2);
		force3 = findViewById(R.id.force3);
		boost1 = findViewById(R.id.boost1);
		boost2 = findViewById(R.id.boost2);
		boost3 = findViewById(R.id.boost3);
		climbToggle0 = findViewById(R.id.climbToggle0);
		climbToggle1 = findViewById(R.id.climbToggle1);
		climbToggle2 = findViewById(R.id.climbToggle2);
		levBoxes = new CheckBox[]{lev1, lev2, lev3};
		forceBoxes = new CheckBox[]{force1, force2, force3};
		boostBoxes = new CheckBox[]{boost1, boost2, boost3};
		climbNumGroup = new ToggleButton[]{climbToggle0, climbToggle1, climbToggle2};
//		wrungClimb = findViewById(R.id.wrungClimb);
//		robotClimb = findViewById(R.id.robotClimb);
//		dataDir = getExternalFilesDir(null).toString();
//		if(teamNumber != -1)teamNum.setText(teamNumber, TextView.BufferType.EDITABLE);
//		if(matchNumber != -1)matchNum.setText(matchNumber, TextView.BufferType.EDITABLE);
	}

	public void setAutolements(){
		teamNum = findViewById(R.id.teamNumber);
		matchNum = findViewById(R.id.matchNumber);
		blocksASwitchAuto = findViewById(R.id.blocksASwitchAuto);
		blocksOSwitchAuto = findViewById(R.id.blocksOSwitchAuto);
		blocksScaleAuto = findViewById(R.id.blocksScaleAuto);
		blocksExchangeAuto = findViewById(R.id.blocksExchangeAuto);
		blocksPUAuto = findViewById(R.id.blocksPUAuto);
		teamDataAuto = findViewById(R.id.teamDataAuto);
		lineCheck = findViewById(R.id.lineCheck);
		dropCheck = findViewById(R.id.dropCheck);
		switchScale = findViewById(R.id.switchScale);
		posL = findViewById(R.id.posL);
		posM = findViewById(R.id.posM);
		posR = findViewById(R.id.posR);
		DNS = findViewById(R.id.DNS);
		posGroup = new ToggleButton[]{posL, posM, posR, DNS};
		blockPlaceCheck = findViewById(R.id.blockPlaceCheck);
//		write = findViewById(R.id.writeAuto);
		dataDir = getExternalFilesDir(null).toString();
//		if(teamNumber != -1) teamNum.setText(teamNumber, TextView.BufferType.EDITABLE);
//		if(matchNumber != -1) matchNum.setText(matchNumber, TextView.BufferType.EDITABLE);
	}

	public String getPosition(){
		for(ToggleButton t : posGroup)
			if(t.isChecked()) return t.getText().toString();
		return "DNS";
	}
	
	public void plusBlocksAuto(View v){
		if(blocksPUAuto == null) blocksPUAuto = findViewById(R.id.blocksPUAuto);
		changeVal(v, blocksPUAuto, 1);
	}
	
	public void minusBlocksAuto(View v){
		if(blocksPUAuto == null) blocksPUAuto = findViewById(R.id.blocksPUAuto);
		changeVal(v, blocksPUAuto, -1);
	}
	
	public void plusScaleAuto(View v){
		if(blocksScaleAuto == null) blocksScaleAuto = findViewById(R.id.blocksScaleAuto);
		changeVal(v, blocksScaleAuto, 1);
	}
	
	public void minusScaleAuto(View v){
		if(blocksScaleAuto == null) blocksScaleAuto = findViewById(R.id.blocksScaleAuto);
		changeVal(v, blocksScaleAuto, -1);
	}
	
	public void plusASwitchAuto(View v){
		if(blocksASwitchAuto == null) blocksASwitchAuto = findViewById(R.id.blocksASwitchAuto);
		changeVal(v, blocksASwitchAuto, 1);
	}
	
	public void minusASwitchAuto(View v){
		if(blocksASwitchAuto == null) blocksASwitchAuto = findViewById(R.id.blocksASwitchAuto);
		changeVal(v, blocksASwitchAuto, -1);
		
	}
	
	public void plusOSwitchAuto(View v){
		if(blocksOSwitchAuto == null) blocksOSwitchAuto = findViewById(R.id.blocksOSwitchAuto);
		changeVal(v, blocksOSwitchAuto, 1);
	}
	
	public void minusOSwitchAuto(View v){
		if(blocksOSwitchAuto == null) blocksOSwitchAuto = findViewById(R.id.blocksOSwitchAuto);
		changeVal(v, blocksOSwitchAuto, -1);
		
	}
	
	public void plusThingAuto(View v){
		if(blocksExchangeAuto == null) blocksExchangeAuto = findViewById(R.id.blocksExchangeAuto);
		changeVal(v, blocksExchangeAuto, 1);
	}
	
	public void minusThingAuto(View v){
		if(blocksExchangeAuto == null) blocksExchangeAuto = findViewById(R.id.blocksExchangeAuto);
		changeVal(v, blocksExchangeAuto, -1);
		
	}
	
	public void switchClick(View c){
		if(blockPlaceCheck == null) blockPlaceCheck = findViewById(R.id.blockPlaceCheck);
		blockPlaceCheck.setChecked(true);
	}
	
	public void setPosVals(){
		if(posL == null) posL = findViewById(R.id.posL);
		if(posM == null) posM = findViewById(R.id.posM);
		if(posR == null) posR = findViewById(R.id.posR);
	}
	
	public void position(View v){
		setPosVals();
		toggleChecker(v, posGroup);
	}
	
	public void setClimbVals(){
		if(climbToggle0 == null) climbToggle0 = findViewById(R.id.climbToggle0);
		if(climbToggle1 == null) climbToggle1 = findViewById(R.id.climbToggle1);
		if(climbToggle2 == null) climbToggle2 = findViewById(R.id.climbToggle2);
		climbNumGroup = new ToggleButton[]{climbToggle0, climbToggle1, climbToggle2};
	}
	
	public void climb(View v){
		setClimbVals();
		toggleChecker(v, climbNumGroup);
	}
	
	public void toggleChecker(View v, ToggleButton[] tBa){
		for(ToggleButton b : tBa)
			if(b == v){
				b.setChecked(true);
				b.setClickable(false);
			} else{
				b.setChecked(false);
				b.setClickable(true);
			}
	}
	
	public void write(View v) throws IOException{
		file = new File(dataDir, teamNum.getText() + "_Match_" + matchNum.getText() + ".csv");
		final View root = v.getRootView();
		StringBuilder s = new StringBuilder();
		//if(file.createNewFile()) {
		boolean created = file.createNewFile();
		setValues(auto);
		if(created) {
			FileOutputStream fos = new FileOutputStream(file);
//			String  lc = "No",
//					dc = "No",
//					bc = "No",
//					scale = "N/A";
//			if (lineCheck.isChecked()) {
//				lc = "Yes";
//			}
//			if (blockCheck.isChecked()) {
//				bc = "Yes";
//			}
//			if (dropCheck.isChecked()) {
//				dc = "Yes";
//			}
//			if (blockPlaceCheck.isChecked()) {
//				if (!switchScale.isChecked()) {
//					scale = "Switch";
//				} else {
//					scale = "Scale";
//				}
//			}
//			String toWrite = "Team:," + teamNum.getText() +
//			                 "\nMatch:," + matchNum.getText() +
//			                 "\nAUTONOMOUS" +
//			                 "\nStarted with block:," + boolToYesNo(blockStart) +
//			                 "\nCrossed Line:," + lc +
//			                 "\nDropped Block:," + dc +
//			                 "\nBlock Placement:," + scale +
//			                 "\nStarting Position:," + pos +
//			                 "\nTELEOP" +
//			                 "\nBlocks Picked up" + blocksPUAuto.getText() +
//			                 "\nAlliance Switch Blocks:," + blocksASwitchAuto.getText() +
//			                 "\nScale Blocks:," + blocksScaleAuto.getText() +
//			                 "\nOpponent Switch Blocks:" + blocksOSwitchAuto.getText();
			fos.write(writeString().getBytes());
			Snackbar sn = Snackbar.make(root, file.getAbsolutePath(), Snackbar.LENGTH_LONG);
			sn.show();
		} else {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScoutingActivity.this);
			builder.setMessage(R.string.OverwriteMessage)
			       .setTitle("Overwrite File?")
			       .setPositiveButton(R.string.Overwrite, new DialogInterface.OnClickListener() {
				       @Override
				       public void onClick(DialogInterface dialog, int which) {
					       file.delete();
					       FileOutputStream fos = null;
					       try {
						       fos = new FileOutputStream(file);
					       } catch (FileNotFoundException e) {
						       e.printStackTrace();
					       }
					       try {
						       fos.write(writeString().getBytes());
						       Snackbar sn = Snackbar.make(root, file.getAbsolutePath(), Snackbar.LENGTH_LONG);
						       sn.show();
					       } catch (IOException e) {
						       e.printStackTrace();
					       }
				       }
			       })
			       .setNeutralButton(R.string.ChangeMatchNumber, new DialogInterface.OnClickListener() {
				       @Override
				       public void onClick(DialogInterface dialog, int which) {
					       matchNum.setText("");
				       }
			       })
			       .setNegativeButton(R.string.ChangeTeam, new DialogInterface.OnClickListener() {
				       @Override
				       public void onClick(DialogInterface dialog, int which) {
					       teamNum.setText("");
				       }
			       });
			android.app.AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
	
	public String writeString(){
//		setValues(auto);
		String toWrite = "";
		toWrite +=
				"Team:," + teamNumber +
				"\nMatch:," + matchNumber +
				"\nPosition," + getPosition()  +
				"\nAUTONOMOUS" +
//				"\nStarted with block:," + boolToYesNo(blockStart) +
				"\nCrossed Line:," + boolToYesNo(crossedLine) +
				"\nDropped Block:," + boolToYesNo(droppedBlock) +
				"\nBlock Placement:," + scaleOrSwitch +
				"\nBlocks Picked Up:," + abpu +
				"\nAlliance Switch Blocks:," + abas +
				"\nScale Blocks:," + absw +
				"\nOpponent Switch Blocks:," + abos +
				"\nBlocks in Exchange:," + abe +
				"\nTELEOP" +
				"\nBlocks Picked Up:," + tbpu +
				"\nAlliance Switch Blocks:," + tbas +
				"\nScale Blocks:," + tbsw +
				"\nOpponent Switch Blocks:," + tbos +
				"\nBlocks in Exchange:," + tbe +
				"\nLevitate:," + levitateAmount +
				"\nForce:," + forceAmount +
				"\nBoost:," + boostAmount +
				"\nENDGAME" +
				"\nClimb:," + climbType +
				"\nNumber Climbing on This:," + climbNumber +
				"\nError:," + boolToYesNo(((CheckBox)findViewById(R.id.error)).isChecked()) +
				"\nDefensive:," + boolToYesNo(((CheckBox)findViewById(R.id.defensive)).isChecked()) +
		        "\nComments:," + ((EditText)findViewById(R.id.teamData)).getText();
		return toWrite;
	}
	
	private int i(TextView v){
		try{
			CharSequence vText  = v.getText();
			String       text   = vText.toString();
			int          number = Integer.parseInt(text);
			return number;
		} catch(Exception e){
			return 0;
		}
	}
	
	private String boolToYesNo(boolean b) {
		if(b){
			return "Yes";
		} else {
			return "No";
		}
	}
	
	private void setValues(boolean auto){
		teamNumber = i(teamNum);
		matchNumber = i(matchNum);
//		if(auto){
			setAutolements();
			droppedBlock = dropCheck.isChecked();
			blockPlaced= blockPlaceCheck.isChecked();
			crossedLine = lineCheck.isChecked();
			if(blockPlaced){
				if (switchScale.isChecked())scaleOrSwitch = "Scale";
				else scaleOrSwitch = "Switch";
			} else scaleOrSwitch = "N/A";
			pos = getPosition();
			abpu = i(blocksPU);
			abas = i(blocksASwitchAuto);
			abos = i(blocksOSwitchAuto);
			absw = i(blocksScaleAuto);
			abe = i(blocksExchangeAuto);
//		} else{
			setTelements();
			tbpu = i(blocksPU);
			tbas = i(blocksASwitch);
			tbos = i(blocksOSwitch);
			tbsw = i(blocksScale);
			tbe = i(blocksExchange);
			levitateAmount = getLev();
			boostAmount = getBoost();
			forceAmount = getForce();
			climbType = getClimb();
			climbNumber = getClimbOnThis();
//		}
	}
	
	public void plusBlocks(View v){
		if(blocksPU == null) blocksPU = findViewById(R.id.blocksPU);
		changeVal(v, blocksPU, 1);
	}
	
	public void minusBlocks(View v){
		if(blocksPU == null) blocksPU = findViewById(R.id.blocksPU);
		changeVal(v, blocksPU, -1);
	}
	
	public void plusScale(View v){
		if(blocksScale == null) blocksScale = findViewById(R.id.blocksScale);
		changeVal(v, blocksScale, 1);
	}
	
	public void minusScale(View v){
		if(blocksScale == null) blocksScale = findViewById(R.id.blocksScale);
		changeVal(v, blocksScale, -1);
	}
	
	public void plusASwitch(View v){
		if(blocksASwitch == null) blocksASwitch = findViewById(R.id.blocksASwitch);
		changeVal(v, blocksASwitch, 1);
	}
	
	public void minusASwitch(View v){
		if(blocksASwitch == null) blocksASwitch = findViewById(R.id.blocksASwitch);
		changeVal(v, blocksASwitch, -1);
		
	}
	
	public void plusOSwitch(View v){
		if(blocksOSwitch == null) blocksOSwitch = findViewById(R.id.blocksOSwitch);
		changeVal(v, blocksOSwitch, 1);
	}
	
	public void minusOSwitch(View v){
		if(blocksOSwitch == null) blocksOSwitch = findViewById(R.id.blocksOSwitch);
		changeVal(v, blocksOSwitch, -1);
		
	}
	
	public void plusThing(View v){
		if(blocksExchange == null) blocksExchange = findViewById(R.id.blocksExchange);
		changeVal(v, blocksExchange, 1);
	}
	
	public void minusThing(View v){
		if(blocksExchange == null) blocksExchange = findViewById(R.id.blocksExchange);
		changeVal(v, blocksExchange, -1);
		
	}
	
	public void levChecking(View v){
		setLev();
		if(v.equals(lev1)){
			lev2.setChecked(false);
			lev3.setChecked(false);
		} else if(v.equals(lev3)){
			lev2.setChecked(true);
			lev1.setChecked(true);
		} else if(v.equals(lev2)){
			lev1.setChecked(true);
			lev3.setChecked(false);
		}
	}
	
	public void setLev(){
		if(lev1 == null) lev1 = findViewById(R.id.lev1);
		if(lev2 == null) lev2 = findViewById(R.id.lev2);
		if(lev3 == null) lev3 = findViewById(R.id.lev3);
	}
	
	public int getLev(){
		int i = 0;
		for(CheckBox lev : levBoxes){
			if(lev.isChecked()){
				i++;
			}
		}
		return i;
	}
	
	public void boostChecking(View v){
		setBoost();
		if(v.equals(boost1)){
			boost2.setChecked(false);
			boost3.setChecked(false);
		} else if(v.equals(boost3)){
			boost2.setChecked(true);
			boost1.setChecked(true);
		} else if(v.equals(boost2)){
			boost1.setChecked(true);
			boost3.setChecked(false);
		}
	}
	
	public void setBoost(){
		if(boost1 == null) boost1 = findViewById(R.id.boost1);
		if(boost2 == null) boost2 = findViewById(R.id.boost2);
		if(boost3 == null) boost3 = findViewById(R.id.boost3);
	}
	
	public int getBoost(){
		int i = 0;
		for(CheckBox boost : boostBoxes){
			if(boost.isChecked()){
				i++;
			}
		}
		return i;
	}
	
	public void forceChecking(View v){
		setForce();
		if(v.equals(force1)){
			force2.setChecked(false);
			force3.setChecked(false);
		} else if(v.equals(force3)){
			force2.setChecked(true);
			force1.setChecked(true);
		} else if(v.equals(force2)){
			force1.setChecked(true);
			force3.setChecked(false);
		}
	}
	
	public void setForce(){
		if(force1 == null) force1 = findViewById(R.id.force1);
		if(force2 == null) force2 = findViewById(R.id.force2);
		if(force3 == null) force3 = findViewById(R.id.force3);
	}
	
	public int getForce(){
		int i = 0;
		for(CheckBox force : forceBoxes){
			if(force.isChecked()){
				i++;
			}
		}
		return i;
	}
	
	public String getClimb(){
		RadioGroup climbGroup = findViewById(R.id.climbingRadioGroup);
		RadioButton selected  = findViewById(climbGroup.getCheckedRadioButtonId());
		return (String) selected.getText();
	}
	
	public int getClimbOnThis(){
		String text = "0";
		for(ToggleButton r: climbNumGroup){
			if(r.isSelected()) text = (String)r.getText();
		}
//		String text = (String)climbNumGroup[0].getText();
		return Integer.parseInt(text);
	}
	
	private void changeVal(View v, TextView t, int i){
		String s =  Integer.toString((Integer.parseInt((String) t.getText()) + i));
		if(Integer.parseInt(s) < 0){
			s = "0";
		}
		t.setText(s);
	}
	
	public void numClimbChecking(View v){
		if(v.equals(climbToggle0)){
			v.setClickable(false);
			climbToggle1.setChecked(false);
			climbToggle1.setClickable(true);
			climbToggle2.setChecked(false);
			climbToggle2.setClickable(true);
		}
		if(v.equals(climbToggle1)){
			v.setClickable(false);
			climbToggle0.setChecked(false);
			climbToggle0.setClickable(true);
			climbToggle2.setChecked(false);
			climbToggle2.setClickable(true);
		}
		if(v.equals(climbToggle2)){
			v.setClickable(false);
			climbToggle0.setChecked(false);
			climbToggle0.setClickable(true);
			climbToggle1.setChecked(false);
			climbToggle1.setClickable(true);
		}
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		public PlaceholderFragment(){
		}
		
		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber){
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle              args     = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View rootView;
			switch(getArguments().getInt(ARG_SECTION_NUMBER)){
				case 1:
					rootView = inflater.inflate(R.layout.fragment_scouting, container, false);
					EditText teamNum = rootView.findViewById(R.id.teamNumber);
					teamNum.setWidth(rootView.findViewById(R.id.matchNumber).getWidth());
					break;
				case 2:
					rootView = inflater.inflate(R.layout.fragment_teleop, container, false);
					ScrollView scrollView = rootView.findViewById(R.id.scrollViewTeleop);
					EditText teamData = rootView.findViewById(R.id.teamData);
					teamData.setText(String.valueOf(scrollView.getHeight()), TextView.BufferType.EDITABLE);
					break;
				default:
					rootView = inflater.inflate(R.layout.fragment_scouting, container, false);
					break;
			}
			return rootView;
		}
		
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm){
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position){
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
//			setAutolements();
			return PlaceholderFragment.newInstance(position + 1);
		}
		
		@Override
		public int getCount(){
			// Show 2 total pages.
			return 2;
		}
	}
}
