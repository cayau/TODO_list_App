package com.example.todolist;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "taskId";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView txtName = (TextView) view.findViewById(R.id.txtName);
        final TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        final TextView txtDate = (TextView) view.findViewById(R.id.txtDate);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d(TAG, "Task ID." + mParam1);
            FirebaseHelper fireB = new FirebaseHelper();
            final DatabaseReference myRef = fireB.init("todo_list");
            final DatabaseReference taskDetails = myRef.child(mParam1);
            // Read from the database
            taskDetails.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        final JSONObject element = new JSONObject(dataSnapshot.getValue().toString());
                        txtName.setText(element.getString("name"));
                        txtDesc.setText(element.getString("desc"));
                        txtDate.setText(element.getString("date"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Task details:" + dataSnapshot.toString());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            view.findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("done", true);
                        obj.put("name",  txtName.getText());
                        obj.put("desc", txtDesc.getText());
                        obj.put("date", txtDate.getText());
                        taskDetails.setValue(obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NavHostFragment.findNavController(DetailFragment.this)
                            .navigate(R.id.action_detailFragment_to_SecondFragment);
                }
            });

            view.findViewById(R.id.btnCancel2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(DetailFragment.this)
                            .navigate(R.id.action_detailFragment_to_SecondFragment);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}