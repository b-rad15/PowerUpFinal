package com.brado.powerupscouting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Teleop.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Teleop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Teleop extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	private EditText teamNum, matchNum;
	private Button switchPeriod, write;
	private TextView blocksASwitchAuto, blocksScaleAuto, blocksPUAuto, blocksOSwitchAuto, teamDataAuto, blocksExchangeAuto, blocksASwitch, blocksOSwitch, blocksScale, blocksExchange, blocksPU, levitate, boost, force, robotClimb, wrungClimb, climbing;
	private CheckBox blockCheck, lineCheck, dropCheck, blockPlaceCheck, lev3, lev2, lev1, force3, force2, force1, boost3, boost2, boost1;
	private Switch       switchScale;
	private ToggleButton posL, posM, posR, climbToggle1, climbToggle2, climbToggle0;
	private ToggleButton[] posGroup;
	private String dataDir;
	private View[] autonomousElements = {switchScale, blockCheck, lineCheck, dropCheck, blockPlaceCheck, posL, posM, posR},
			teleopElements = {levitate, lev1, lev2, lev3, boost, boost1, boost2, boost3, force, force1, force2, force3, robotClimb, wrungClimb, climbing, climbToggle1, climbToggle2};
	private CheckBox[] levBoxes = {lev1, lev2, lev3},
			forceBoxes = {force1, force2, force3},
			boostBoxes = {boost1, boost2, boost3};
	static        File file;
	public static int  teamNumber = -1, matchNumber = -1, abas, absw, abos, abpu, abe, tbas, tbsw, tbos, tbpu, tbe, levitateAmount, boostAmount, forceAmount, climbNumber;
	public static char pos = 'X';
	public static boolean climbedWrung = false,
			climbed = false,
			blockStart = false,
			crossedLine = false,
			droppedBlock = false,
			blockPlaced = false,
			auto = true;
	public static String scaleOrSwitch;
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private OnFragmentInteractionListener mListener;
	
	public Teleop(){
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Teleop.
	 */
	// TODO: Rename and change types and number of parameters
	public static Teleop newInstance(String param1, String param2){
		Teleop fragment = new Teleop();
		Bundle args     = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri){
		if(mListener != null){
			mListener.onFragmentInteraction(uri);
		}
	}
	
	@Override
	public void onAttach(Context context){
		super.onAttach(context);
		if(context instanceof OnFragmentInteractionListener){
			mListener = (OnFragmentInteractionListener) context;
		} else{
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_teleop, container, false);
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		mListener = null;
	}
//	public void setTelements(){
//		teamNum = findViewById(R.id.teamNum);
//		matchNum = findViewById(R.id.matchNum);
//		blocksASwitch  = findViewById(R.id.blocksASwitchAuto);
//		blocksOSwitch  = findViewById(R.id.blocksOSwitch );
//		blocksScale  = findViewById(R.id.blocksScale );
//		blocksExchange  = findViewById(R.id.blocksExchange );
//		blocksPU  = findViewById(R.id.blocksPU );
//		lev1 = findViewById(R.id.lev1);
//		lev2 = findViewById(R.id.lev2);
//		lev3 = findViewById(R.id.lev3);
//		force1 = findViewById(R.id.force1);
//		force2 = findViewById(R.id.force2);
//		force3 = findViewById(R.id.force3);
//		boost1 = findViewById(R.id.boost1);
//		boost2 = findViewById(R.id.boost2);
//		boost3 = findViewById(R.id.boost3);
//		climbToggle1 = findViewById(R.id.climbToggle1);
//		climbToggle2 = findViewById(R.id.climbToggle2);
//		//		wrungClimb = findViewById(R.id.wrungClimb);
//		//		robotClimb = findViewById(R.id.robotClimb);
//		dataDir = getExternalFilesDir(null).toString();
//		if(teamNumber != -1)teamNum.setText(teamNumber, TextView.BufferType.EDITABLE);
//		if(matchNumber != -1)matchNum.setText(matchNumber, TextView.BufferType.EDITABLE);
//	}
//
//	public void plusBlocks(View v){
//		if(blocksPU == null) blocksPU = findViewById(R.id.blocksPU);
//		changeVal(v, blocksPU, 1);
//	}
//
//	public void minusBlocks(View v){
//		if(blocksPU == null) blocksPU = findViewById(R.id.blocksPU);
//		changeVal(v, blocksPU, -1);
//	}
//
//	public void plusScale(View v){
//		if(blocksScale == null) blocksScale = findViewById(R.id.blocksScale);
//		changeVal(v, blocksScale, 1);
//	}
//
//	public void minusScale(View v){
//		if(blocksScale == null) blocksScale = findViewById(R.id.blocksScale);
//		changeVal(v, blocksScale, -1);
//	}
//
//	public void plusASwitch(View v){
//		if(blocksASwitch == null) blocksASwitch = findViewById(R.id.blocksASwitch);
//		changeVal(v, blocksASwitch, 1);
//	}
//
//	public void minusASwitch(View v){
//		if(blocksASwitch == null) blocksASwitch = findViewById(R.id.blocksASwitch);
//		changeVal(v, blocksASwitch, -1);
//
//	}
//
//	public void plusOSwitch(View v){
//		if(blocksOSwitch == null) blocksOSwitch = findViewById(R.id.blocksOSwitch);
//		changeVal(v, blocksOSwitch, 1);
//	}
//
//	public void minusOSwitch(View v){
//		if(blocksOSwitch == null) blocksOSwitch = findViewById(R.id.blocksOSwitch);
//		changeVal(v, blocksOSwitch, -1);
//
//	}
//
//	public void plusThing(View v){
//		if(blocksExchange == null) blocksExchange = findViewById(R.id.blocksExchange);
//		changeVal(v, blocksExchange, 1);
//	}
//
//	public void minusThing(View v){
//		if(blocksExchange == null) blocksExchange = findViewById(R.id.blocksExchange);
//		changeVal(v, blocksExchange, -1);
//
//	}
	
	
	private String boolToYesNo(boolean b) {
		if(b){
			return "Yes";
		} else {
			return "No";
		}
	}
	
//	private void setValues(boolean auto){
//		teamNumber = i(teamNum);
//		matchNumber = i(matchNum);
//		if(auto){
//			setAutolements();
//			blockStart = blockCheck.isChecked();
//			droppedBlock = dropCheck.isChecked();
//			blockPlaced= blockPlaceCheck.isChecked();
//			crossedLine = lineCheck.isChecked();
//			if(!blockPlaced){
//				if (switchScale.isChecked())scaleOrSwitch = "Scale";
//				else scaleOrSwitch = "Switch";
//			} else scaleOrSwitch = "N/A";
//			pos = getPosition();
//			abpu = i(blocksPU);
//			abas = i(blocksASwitchAuto);
//			abos = i(blocksOSwitchAuto);
//			absw = i(blocksScaleAuto);
//			abe = i(blocksExchangeAuto);
//		} else{
//			setTelements();
//			tbpu = i(blocksPU);
//			tbas = i(blocksASwitch);
//			tbos = i(blocksOSwitch);
//			tbsw = i(blocksScale);
//			tbe = i(blocksExchange);
//			levitateAmount = getLev();
//			boostAmount = getBoost();
//			forceAmount = getForce();
//		}
//	}
	
	public void levChecking(View v){
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
		String s = "";
		if(robotClimb.isSelected()){
			s = "On Robot";
		} else if(wrungClimb.isSelected()) {
			s = "On Wrung";
		}
		return s;
	}
	
	public int getClimbOnThis(){
		int i = 0;
		if(climbToggle2.isSelected()){
			i = 2;
		} else if(climbToggle1.isSelected()){
			i = 1;
		}
		return i;
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
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
