package msgBoard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import msgBoard.Container;
import msgBoard.dto.Article;
import msgBoard.dto.Member;
import msgBoard.util.Util;

public class ArticleController extends Controller{
	private List<Article> articles;
	private Scanner sc;
	private String comm;
	private String actionMethodName;
	
	private int lastArticleId = 3;

	public ArticleController(Scanner sc) {
		articles = Container.articleDao.articles;
		this.sc = sc;
	}
	
	@Override
	public void action(String actionMethodName, String comm) {
		this.actionMethodName = actionMethodName;
		this.comm = comm;
		
		switch (actionMethodName) {
		case "write":
			doWrite();
			break;
		case "list":
			showList();
			break;
		case "detail":
			showDetail();
			break;
		case "modify":
			doModify();
			break;
		case "delete":
			doDelete();
			break;
		default:
			System.out.println("없는 기능입니다");
			break;
		}
	}

	private void doWrite() {
		int id = lastArticleId + 1;
		int memberId = loginedMember.id;
		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();
		String regDate = Util.getNowDateTimeStr();
		
		articles.add(new Article(id, memberId, title, body, regDate, regDate));
		lastArticleId++;
		System.out.println(id + "번 글이 작성되었습니다");
	}

	private void showList() {
		if(articles.size() == 0) {
			System.out.println("게시글이 없습니다");
			return;
		}
		
		System.out.println(" 번호  /  제목  /  조회  /  작성자");
		for(int i = articles.size() - 1; i >= 0; i--){
			Article article = articles.get(i);
			
			String writerName = null;
			List<Member> members = Container.memberDao.members;
			
			for(Member member : members) {
				if(member.id == article.memberId) {
					writerName = member.name;
					break;
				}
			}
			System.out.printf(" %d  /  %s  /  %d  /  %s  \n", article.id, article.title, article.hit, writerName);
		}
	}

	private void showDetail() {
		String[] commDiv = comm.split(" ");
		if(commDiv.length < 3) {
			System.out.println("게시물 번호를 확인해주세요");
			return;
		}
		
		int id = Integer.parseInt(commDiv[2]);
		Article foundArticle = getArticleById(id);
		
		if(foundArticle == null) {
			System.out.println("찾는 게시물이 없습니다");
			return;
		}
		
		String writerName = null;
		List<Member> members = Container.memberDao.members;
		
		for(Member member : members) {
			if(member.id == foundArticle.memberId) {
				writerName = member.name;
				break;
			}
		}
		
		System.out.println("번호 : " + foundArticle.id);
		System.out.println("조회 : " + foundArticle.hit);
		System.out.println("작성자 : " + writerName);
		System.out.println("제목 : " + foundArticle.title);
		System.out.println("내용 : " + foundArticle.body);
		System.out.println("작성시각 : " + foundArticle.regDate);
		System.out.println("수정시각 : " + foundArticle.updateDate);
		foundArticle.hit++;
	}

	private int getArticleIndexById(int id) {
		int i = 0;
		for(Article article : articles) {
			if(article.id == id) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private Article getArticleById(int id) {
		int index = getArticleIndexById(id);
		if(index == -1) {
			return null;
		}
		return articles.get(index);
	}

	private void doModify() {
		String[] commDiv = comm.split(" ");
		if(commDiv.length < 3) {
			System.out.println("게시물 번호를 확인해주세요");
			return;
		}
		
		int id = Integer.parseInt(commDiv[2]);
		Article foundArticle = getArticleById(id);
		
		if(foundArticle == null) {
			System.out.println("찾는 게시물이 없습니다");
			return;
		}
		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("수정 권한이 업습니다");
			return;
		}
		System.out.print("새제목 : ");
		String newTitle = sc.nextLine();
		System.out.print("새내용 : ");
		String newBody = sc.nextLine();
		String updateDate = Util.getNowDateTimeStr();
		
		foundArticle.title = newTitle;
		foundArticle.body = newBody;
		foundArticle.updateDate = updateDate;
		System.out.println(id + "번 글이 수정되었습니다");
	}

	private void doDelete() {
		String[] commDiv = comm.split(" ");
		if(commDiv.length < 3) {
			System.out.println("게시물 번호를 확인해주세요");
			return;
		}
		
		int id = Integer.parseInt(commDiv[2]);
		Article foundArticle = getArticleById(id);
		
		if(foundArticle == null) {
			System.out.println("찾는 게시물이 없습니다");
			return;
		}
		
		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("삭제 권한이 업습니다");
			return;
		}
		articles.remove(foundArticle);
		System.out.println(id + "번 글이 삭제되었습니다");
		
	}

	public void makeTestData() {
		System.out.println("article 테스트 데이터 생성");
		articles.add(new Article(1, 3, "제목1", "내용1", "2023-12-12 12:12:12", "2023-12-12 12:12:12"));
		articles.add(new Article(2, 2, "제목2", "내용2", "2023-12-12 12:12:12", "2023-12-12 12:12:12"));
		articles.add(new Article(3, 2, "제목3", "내용3", "2023-12-12 12:12:12", "2023-12-12 12:12:12"));
	}
}
