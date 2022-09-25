package com.moutamid.talktogetheradminapp.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.talktogetheradminapp.Adapters.FollowUserListAdapter;
import com.moutamid.talktogetheradminapp.ItemClickListener;
import com.moutamid.talktogetheradminapp.Model.User;
import com.moutamid.talktogetheradminapp.R;
import com.moutamid.talktogetheradminapp.databinding.FragmentUserBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UsersFragment extends Fragment {

    private FragmentUserBinding binding;
    private FollowUserListAdapter adapter;
    private List<User> userList;
    private DatabaseReference db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference().child("Users");
        getUsers();

        return root;
    }

    private void getUsers() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        User model = ds.getValue(User.class);
                        userList.add(model);
                    }

                    adapter = new FollowUserListAdapter(getActivity(),userList);
                    binding.recyclerViewDetail.setAdapter(adapter);
                    adapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            String id = userList.get(position).getId();
                            showPopup(position,id,view);
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showPopup(int position, String id, View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.inflate(R.menu.user_menu_main);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_banned:

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("banned",true);
                        db.child(id).updateChildren(hashMap);
                        Toast.makeText(getActivity(),"Banned",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_delete:
                        db.child(id).removeValue();
                        userList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position,userList.size());
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}