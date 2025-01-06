package com.ticketpro.parking.activity;

import com.ticketpro.model.Ticket;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PlatePriorTicketFragmentAdapter extends FragmentPagerAdapter {
	private String message = "";
	
	public PlatePriorTicketFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int pos) {
		PlatePriorTicketFragment ticketFragment = new PlatePriorTicketFragment();
		Bundle data = new Bundle();
		
		PlateLookupResultPriorTickets lookupActivity = TPApplication.getInstance().getPlateLookupActivity();
		PlateLookupResultAdvance plateLookupResult = TPApplication.getInstance().getPlateLookupResultActivity();
		
		//if (lookupActivity.getGroupPos() == (lookupActivity.getGroupCount() - 1)  && (lookupActivity.getPermitList()!=null)  && ("permit".equalsIgnoreCase(lookupActivity.getDisplayType()))) {
			if (("permit".equalsIgnoreCase(lookupActivity.getDisplayType()))) {
			message = plateLookupResult.getPermitHistoryHTML(lookupActivity.getPermitList().get(pos));
		} else {
			Ticket ticket = lookupActivity.getTicketList().get(pos);
			message = plateLookupResult.getTicketHistory(lookupActivity.getTicketList(), pos);
			String ticketType = plateLookupResult.getTicketType(ticket);
			long CitationNumber = ticket.getCitationNumber();

			data.putString("ticketType", ticketType);
			data.putLong("CitationNumber", CitationNumber);
		}

		data.putString("message", message);
		data.putInt("current_page", pos);
		data.putInt("total_page", lookupActivity.getSize());
		
		ticketFragment.setArguments(data);
		
		return ticketFragment;
	}

	@Override
	public int getCount() {
		PlateLookupResultPriorTickets lookupActivity = TPApplication.getInstance().getPlateLookupActivity();
		return lookupActivity.getSize();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "<" + (position + 1) + ">";
	}
}
