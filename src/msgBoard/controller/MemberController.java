package msgBoard.controller;

import java.util.List;
import java.util.Scanner;

import msgBoard.Container;
import msgBoard.dto.Article;
import msgBoard.dto.Member;
import msgBoard.util.Util;

public class MemberController extends Controller {
	private List<Member> members;
	private Scanner sc;
	private String comm;
	private String actionMethodName;

	private int lastMemberId = 3;
	
	public MemberController(Scanner sc) {
		members = Container.memberDao.members;
		this.sc = sc;
	}

	@Override
	public void action(String actionMethodName, String comm) {
		this.actionMethodName = actionMethodName;
		this.comm = comm;

		switch (actionMethodName) {
		case "join":
			doJoin();
			break;
		case "login":
			doLogin();
			break;
		case "logout":
			doLogout();
			break;
		default:
			System.out.println("없는 기능입니다");
			break;
		}
	}

	private void doJoin() {
		int id = lastMemberId + 1;
		String loginId = null;
		String loginPw = null;
		String name = null;
		
		while(true) {
			System.out.print("아이디 : ");
			loginId = sc.nextLine();
			if(loginId.length() == 0) {
				System.out.println("필수 입력!");
				continue;
			}
			if(isJoinableLoginId(loginId) == false) {
				System.out.println("이미 존재하는 아이디입니다");
				continue;
			}
			break;
		}
		while(true) {
			System.out.print("비밀번호 : ");
			loginPw = sc.nextLine();
			if(loginPw.length() == 0) {
				System.out.println("필수 입력!");
				continue;
			}
			System.out.print("비밀번호 확인 : ");
			String loginPwConfirm = sc.nextLine();
			if(loginPw.equals(loginPwConfirm) == false) {
				System.out.println("비밀번호를 확인해주세요");
				continue;
			}
			break;
		}
		while(true) {
			System.out.print("이름 : ");
			name = sc.nextLine();
			if(name.length() == 0) {
				System.out.println("필수 입력!");
				continue;
			}
			break;
		}
		String regDate = Util.getNowDateTimeStr();
		members.add(new Member(id, loginId, loginPw, name, regDate, regDate));
		System.out.println(loginId + "님, 회원가입되었습니다");
		lastMemberId++;
	}

	private boolean isJoinableLoginId(String loginId) {
		for(Member member : members) {
			if(member.loginId.equals(loginId)) {
				return false;
			}
		}
		return true;
	}

	private void doLogin() {
		Member foundMember = null;
		while(true) {
			System.out.print("아이디 : ");
			String loginId = sc.nextLine();
			if(loginId.length() == 0) {
				System.out.println("필수 입력!");
				continue;
			}
			
			foundMember = getMemberByLoginId(loginId);
			
			if(foundMember == null) {
				System.out.println("일치하는 회원이 없습니다");
				continue;
			}
			break;
		}
		while(true) {
			System.out.print("비밀번호 : ");
			String loginPw = sc.nextLine();
			if(loginPw.length() == 0) {
				System.out.println("필수 입력!");
				continue;
			}
			
			if(foundMember.loginPw.equals(loginPw) == false) {
				System.out.println("비밀번호가 틀렸습니다");
				continue;
			}
			break;
		}
		
		loginedMember = foundMember;
		System.out.println(foundMember.loginId + "님, 반갑습니다");
	}

	private Member getMemberByLoginId(String loginId) {
		for(Member member : members) {
			if(member.loginId.equals(loginId)){
				return member;
			}
		}
		return null;
	}

	private void doLogout() {
		loginedMember = null;
		System.out.println("로그아웃되었습니다");
	}
	
	public void makeTestData() {
		System.out.println("member 테스트 데이터 생성");
		members.add(new Member(1, "test1", "pw", "송강호", "2023-12-12 12:12:12", "2023-12-12 12:12:12"));
		members.add(new Member(2, "test2", "pw", "유해진", "2023-12-12 12:12:12", "2023-12-12 12:12:12"));
		members.add(new Member(3, "test3", "pw", "차인표", "2023-12-12 12:12:12", "2023-12-12 12:12:12"));
	}
}
