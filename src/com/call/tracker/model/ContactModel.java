package com.call.tracker.model;

import java.util.ArrayList;

public class ContactModel implements Comparable<ContactModel> {

	private String contactId;
	private String name;
	private String number1;
	private String number2;
	private String number3;
	private String number4;
	private ArrayList<String> groups = new ArrayList<String>();;
	private boolean isCheck = false;

	public ContactModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getGroup() {
		return groups;
	}

	public void addGroup(String name) {
		if (null == this.groups)
			this.groups = new ArrayList<String>();
		this.groups.add(name);
	}

	public String getNumber1() {
		return number1;
	}

	public void setNumber1(String number1) {
		this.number1 = number1;
	}

	public String getNumber2() {
		return number2;
	}

	public void setNumber2(String number2) {
		this.number2 = number2;
	}

	public String getNumber3() {
		return number3;
	}

	public void setNumber3(String number3) {
		this.number3 = number3;
	}

	public String getNumber4() {
		return number4;
	}

	public void setNumber4(String number4) {
		this.number4 = number4;
	}

	public int compareTo(ContactModel another) {
		// TODO Auto-generated method stub
		int res = String.CASE_INSENSITIVE_ORDER.compare(this.getName(),
				another.getName());
		return (res != 0) ? res : this.getName().compareTo(another.getName());
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return contactId;
	}

	// public static Comparator<ContactData> contactNameComparator = new
	// Comparator<ContactData>() {
	// public int compare(ContactData object1, ContactData object2) {
	// int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getName(),
	// object2.getName());
	// return (res != 0) ? res : object1.getName().compareTo(
	// object2.getName());
	// }
	// };

}
