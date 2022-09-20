package com.moutamid.talktogetheradminapp.ui.rooms;

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
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.talktogetheradminapp.Adapters.Adapter_Detail;
import com.moutamid.talktogetheradminapp.Adapters.FollowUserListAdapter;
import com.moutamid.talktogetheradminapp.ItemClickListener;
import com.moutamid.talktogetheradminapp.Model.RoomDetails;
import com.moutamid.talktogetheradminapp.Model.User;
import com.moutamid.talktogetheradminapp.R;
import com.moutamid.talktogetheradminapp.databinding.FragmentRoomBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class RoomsFragment extends Fragment {

    private FragmentRoomBinding binding;
    private Adapter_Detail adapter_detail;
    private DatabaseReference roomDB;
    private ArrayList<RoomDetails> roomDetailsArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRoomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        roomDB = FirebaseDatabase.getInstance().getReference().child("Rooms");
        roomDetailsArrayList = new ArrayList<>();
        getRooms();

        return root;
    }

    private void getRooms() {
        roomDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    roomDetailsArrayList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        RoomDetails model = ds.getValue(RoomDetails.class);
                        roomDetailsArrayList.add(model);
                    }

                    adapter_detail = new Adapter_Detail(getActivity(),roomDetailsArrayList);
                    binding.recyclerViewDetail.setAdapter(adapter_detail);
                    adapter_detail.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            String id = roomDetailsArrayList.get(position).getId();
                            showPopup(position,id,view);
                        }
                    });
                    adapter_detail.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showPopup(int position, String id, View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.inflate(R.menu.room_menu_main);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_edit:
                        Toast.makeText(getActivity(),"Edit",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_delete:
                        roomDB.child(id).removeValue();
                        roomDetailsArrayList.remove(position);
                        adapter_detail.notifyItemRemoved(position);
                        adapter_detail.notifyItemRangeRemoved(position,roomDetailsArrayList.size());
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