package com.wbn.uom.btdevicechain.view;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wbn.uom.btdevicechain.MainActivity;
import com.wbn.uom.btdevicechain.R;
import com.wbn.uom.btdevicechain.model.Device;

import java.util.ArrayList;
import java.util.List;


public class SearchDeviceFragment extends Fragment {

    private RecyclerView recyclerView;
    private static DeviceListNotConnectedAdapter deviceListNotConnectedAdapter;
    private List<Device> deviceList = new ArrayList<>();

//    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Log.d("DEVICELIST", "Bluetooth device found\n");
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Create a new device item
//                Device newDevice = new Device(device.getName(), device.getAddress(), "false");
//                // Add it to our adapter
//                deviceListNotConnectedAdapter.add(newDevice);
//                deviceListNotConnectedAdapter.notifyDataSetChanged();
//            }
//        }
//    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search_device, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.search_device_list_recycler_view);

        deviceListNotConnectedAdapter = new DeviceListNotConnectedAdapter(deviceList);

        ToggleButton scan = (ToggleButton) view.findViewById(R.id.search_btn);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewItemDecorator(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(deviceListNotConnectedAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity().getApplicationContext(),
                recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Device device = deviceList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), device.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), "hah haa long click!", Toast.LENGTH_SHORT).show();
            }
        }));


        scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    deviceListNotConnectedAdapter.clear();
                    ((MainActivity)getActivity()).bluetooth.startDeviceDiscovery();
//                    getActivity().registerReceiver(bReciever, filter);
//                    ((MainActivity)getActivity()).BTAdapter.startDiscovery();
                } else {
                    ((MainActivity)getActivity()).bluetooth.cancelDeviceDiscovery();
//                    getActivity().unregisterReceiver(bReciever);
//                    ((MainActivity)getActivity()).BTAdapter.cancelDiscovery();
                }
            }
        });

        return view;

    }

    public static void addDeviceToAdapter(Device device){
        deviceListNotConnectedAdapter.add(device);
        deviceListNotConnectedAdapter.notifyDataSetChanged();
    }
}
