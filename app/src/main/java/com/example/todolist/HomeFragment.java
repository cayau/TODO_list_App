package com.example.todolist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context t = getActivity().getBaseContext();
        final TableLayout table = (TableLayout) view.findViewById(R.id.tblList);
        final List<View> pedding = new ArrayList<View>();
        final List<View> done = new ArrayList<View>();

        FirebaseHelper fireB = new FirebaseHelper();
        final DatabaseReference myRef = fireB.init("todo_list");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int d16 = (int) (getResources().getDisplayMetrics().density * 16);
                int d8 = (int) (getResources().getDisplayMetrics().density * 8);
                table.removeAllViews();
                pedding.clear();
                done.clear();

                View divideri = new View(t);
                int dividerHeight = (int) (getResources().getDisplayMetrics().density * 16); // 1dp to pixels
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight);
                params.setMargins(0, 5, 0, 5); //substitute parameters for left, top, right, bottom
                divideri.setLayoutParams(params);
                table.addView(divideri);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    try {
                        final String elementID = child.getKey().toString();
                        final JSONObject element = new JSONObject( child.getValue().toString() );

                        TableRow tblRow = new TableRow(t);
                        tblRow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        tblRow.setPadding(d16,d16,d16,d16);
                            CheckBox chkItem = new CheckBox(t);
                            Boolean checked = element.getBoolean("done");
                            chkItem.setChecked(checked);
                            TextView txtname = new TextView(t);
                            txtname.setText(element.getString("name"));
                            Typeface typeface = txtname.getTypeface();
                            txtname.setTypeface(typeface, checked?Typeface.ITALIC:Typeface.BOLD);
                            txtname.setTextSize(checked?16:18);
                            if(checked)
                                txtname.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        View divider = new View(t);
                        divider.setLayoutParams(params);

                        tblRow.addView(chkItem);
                        tblRow.addView(txtname);
                        if(checked){
                            done.add(tblRow);
                            done.add(divider);
                        }else{
                            pedding.add(tblRow);
                            pedding.add(divider);
                        }

                        chkItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Boolean checked = element.getBoolean("done");
                                    String name = element.getString("name");
                                    String desc = element.getString("desc");
                                    if(checked){
                                        Toast.makeText(getActivity(), "Task Undone!, description: " + desc, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Task Done!, description: " + desc, Toast.LENGTH_LONG).show();
                                    }
                                    DatabaseReference elementUpdate = myRef.child(elementID);
                                    JSONObject obj = new JSONObject();
                                    try {
                                        obj.put("done", !checked);
                                        obj.put("name", name);
                                        obj.put("desc", desc);
                                        obj.put("date", element.getString("date"));
                                        elementUpdate.setValue(obj.toString() );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        tblRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("taskId", elementID);
                                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_SecondFragment_to_detailFragment, bundle);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int i=0; i<pedding.size(); i++) {
                    table.addView(pedding.get(i) );
                }
                for (int i=0; i<done.size(); i++) {
                    table.addView(done.get(i) );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}