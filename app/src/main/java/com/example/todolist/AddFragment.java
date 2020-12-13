package com.example.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView txtName = (TextView) view.findViewById(R.id.txtName);
        final TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);

        FirebaseHelper fireB = new FirebaseHelper();
        final DatabaseReference myRef = fireB.init("todo_list");

        view.findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence name = txtName.getText();
                CharSequence desc = txtDesc.getText();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String date = simpleDateFormat.format(new Date());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("done", false);
                    obj.put("name", name.toString());
                    obj.put("desc", desc.toString());
                    obj.put("date", date);
                    DatabaseReference dbRef = myRef.push();
                    dbRef.setValue(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NavHostFragment.findNavController(AddFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}