package com.swsoft.walkingtogether;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RoomListFragment extends Fragment {

    RecyclerView recyclerView; //봉
    RoomListAdapter adapter; //현수막회사
    ArrayList<RoomListItem> items = new ArrayList<>(); //현수막들어갈데이터

    //이어하기 : 프레그먼트가 화면에 보여질때
    //화면이 가려질때가 매우 많은 경우수가 있어서 .... 고려해야함!!!
    @Override
    public void onResume() {
        super.onResume();

        //임시데이터
        for(int i = 0 ; i <20 ; i++){

            items.add(0,new RoomListItem("Test"+i, "00:00"));
            adapter.notifyItemInserted(0);//아이템이 0번째에 추가될때 알려줌
        }
        
//        adapter.notifyDataSetChanged();//전체 통보하기
//        adapter.notifyItemChanged();//변경될때
//        adapter.notifyItemInserted();//아이템이 추가될때

    }//onResume

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roomlist, container,false);

        recyclerView = view.findViewById(R.id.roomlist_recyclerview);

        //프래그먼트는 context가 아니기에 context가 필요할때 context를 상속받은 Activity를 호출하여 사용할 수 있다. getActivity()
        adapter = new RoomListAdapter(getActivity(),items);
        //봉에게 아답터 전달
        recyclerView.setAdapter(adapter);

        return view;
    }//oncreateView
}// RoomLsitFragment
