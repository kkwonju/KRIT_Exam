package msgBoard.controller;

import msgBoard.dto.Member;

public abstract class Controller {
	public static Member loginedMember = null;
	
	public static boolean isLogined() {
		if(loginedMember == null) {
			return false;
		}
		return true;
	}
	
	public abstract void action(String actionMethodName, String comm);
	
	public abstract void makeTestData();
}
