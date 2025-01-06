package com.ticketpro.parking.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ticketpro.model.Permit;
import com.ticketpro.model.PlateLookupResult;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;

import java.util.ArrayList;

//import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;

public class PlateLookupResultPriorTickets extends FragmentActivity implements OnClickListener {

    private PlateLookupResult activeLookupResult;
    private int pos = 0;
    private int groupPos = 0;
    private int groupCount;
    private int size = 1;
    private ArrayList<Ticket> ticketList;
    private ArrayList<Permit> permitList;
    private Button firstBtn, backBtn, lastBtn;
    private RelativeLayout ticketTypeBgLayout;
    private TextView text;
    private ViewPager pager;
    private String displayType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prior_ticket);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", pos);
        groupCount = intent.getIntExtra("groupCount", groupCount);
        groupPos = intent.getIntExtra("groupPos", groupPos);
        displayType = intent.getStringExtra("displayType");

        activeLookupResult = TPApplication.getInstance().getActiveLookupResult();
        TPApplication.getInstance().setPlateLookupActivity(this);

        try {
            pager = (ViewPager) findViewById(R.id.pager);
            firstBtn = (Button) findViewById(R.id.prior_tkt_first_btn);
            backBtn = (Button) findViewById(R.id.prior_tkt_back_btn);
            ticketTypeBgLayout = (RelativeLayout) findViewById(R.id.ticket_type_bg);
            lastBtn = (Button) findViewById(R.id.prior_tkt_last_btn);
            text = (TextView) findViewById(R.id.text);

            if (activeLookupResult != null) {
                permitList = activeLookupResult.getAllPermit();
				/*if (groupPos == (groupCount - 1) && permitList != null && displayType.equalsIgnoreCase("permit") && permitList.size()!=0) {
					size = permitList.size();
				} else {
					ticketList = TPApplication.getInstance().getTicketList();
					size = ticketList.size();
					pager.setCurrentItem(pos);
				}*/

                if (displayType.equalsIgnoreCase("permit")) {
                    size = permitList.size();
                    pager.setCurrentItem(pos);
                } else {
                    ticketList = TPApplication.getInstance().getTicketList();
                    size = ticketList.size();
                    pager.setCurrentItem(pos);
                }
            }


            FragmentManager fm = getSupportFragmentManager();
            PlatePriorTicketFragmentAdapter pagerAdapter = new PlatePriorTicketFragmentAdapter(fm);
            pager.setAdapter(pagerAdapter);
            pager.setCurrentItem(pos);

            backBtn.setOnClickListener(this);
            firstBtn.setOnClickListener(this);
            lastBtn.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeBackGroundColor(int pos) {
        text.setText("V");
        ticketTypeBgLayout.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prior_tkt_first_btn:
                pager.setCurrentItem(0);
                break;

            case R.id.prior_tkt_back_btn:
                finish();
                break;

            case R.id.prior_tkt_last_btn:
                pager.setCurrentItem(size);
                break;

            default:
                break;
        }
    }

    public PlateLookupResult getActiveLookupResult() {
        return activeLookupResult;
    }

    public void setActiveLookupResult(PlateLookupResult activeLookupResult) {
        this.activeLookupResult = activeLookupResult;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getGroupPos() {
        return groupPos;
    }

    public void setGroupPos(int groupPos) {
        this.groupPos = groupPos;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public ArrayList<Permit> getPermitList() {
        return permitList;
    }

    public void setPermitList(ArrayList<Permit> permitList) {
        this.permitList = permitList;
    }

    public Button getFirstBtn() {
        return firstBtn;
    }

    public void setFirstBtn(Button firstBtn) {
        this.firstBtn = firstBtn;
    }

    public Button getBackBtn() {
        return backBtn;
    }

    public void setBackBtn(Button backBtn) {
        this.backBtn = backBtn;
    }

    public Button getLastBtn() {
        return lastBtn;
    }

    public void setLastBtn(Button lastBtn) {
        this.lastBtn = lastBtn;
    }

    public RelativeLayout getTicketTypeBgLayout() {
        return ticketTypeBgLayout;
    }

    public void setTicketTypeBgLayout(RelativeLayout ticketTypeBgLayout) {
        this.ticketTypeBgLayout = ticketTypeBgLayout;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

    public ViewPager getPager() {
        return pager;
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

}
